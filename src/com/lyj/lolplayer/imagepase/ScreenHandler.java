package com.lyj.lolplayer.imagepase;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_ml.TrainData;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import com.lyj.lolplayer.utils.PathUtils;

/**
 * 截屏处理
 * 
 * @author lili
 *
 */
public class ScreenHandler {
	private Robot robot;
	public static ScreenHandler defaultScreenHandler = new ScreenHandler();

	public static ScreenHandler getInstance() {
		return defaultScreenHandler;
	}

	public ScreenHandler() {

		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void ShowImage(IplImage image, String caption) {
		CvMat mat = image.asCvMat();
		int width = mat.cols();
		if (width < 1)
			width = 1;
		int height = mat.rows();
		if (height < 1)
			height = 1;
		double aspect = 1.0 * width / height;
		if (height < 128) {
			height = 128;
			width = (int) (height * aspect);
		}
		if (width < 128)
			width = 128;
		height = (int) (width / aspect);
		ShowImage(image, caption, width, height);
	}

	public static void ShowImage(IplImage image, String caption, int size) {
		if (size < 128)
			size = 128;
		CvMat mat = image.asCvMat();
		int width = mat.cols();
		if (width < 1)
			width = 1;
		int height = mat.rows();
		if (height < 1)
			height = 1;
		double aspect = 1.0 * width / height;
		if (height != size) {
			height = size;
			width = (int) (height * aspect);
		}
		if (width != size)
			width = size;
		height = (int) (width / aspect);
		ShowImage(image, caption, width, height);
	}

	public static void ShowImage(IplImage image, String caption, int width,
			int height) {
		CanvasFrame canvas = new CanvasFrame(caption, 1); // gamma=1
		canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		canvas.setCanvasSize(width, height);
		OpenCVFrameConverter converter = new OpenCVFrameConverter.ToIplImage();
		canvas.showImage(converter.convert(image));
	}

	public void opencvImage(String path) {
		IplImage image = opencv_imgcodecs.cvLoadImageBGRA(path);
		if (image != null) {
			// opencv_imgproc.cvSmooth(image, image);

			// // 灰度图
			// IplImage grayImage = cvCreateImage(cvGetSize(image),
			// IPL_DEPTH_8U,
			// 1);
			// cvCvtColor(image, grayImage, CV_BGR2GRAY);
			// ShowImage(grayImage, "grayimage");
			// cvRelease(grayImage);;
			// opencv_imgcodecs.cvSaveImage(path + "gray.jpg", grayImage);

			// 角点
			// IplImage src = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U,
			// 1);
			// IplImage dst = cvCreateImage(cvGetSize(image), IPL_DEPTH_32F,
			// 1);
			// cvCopy(grayImage, src);

			// hairrs角点
			// int blockSize = 3;
			// int apertureSize = 5;
			// double k = 0.04;
			// cvCornerHarris(src, dst, blockSize, apertureSize, k);

//			// canny边缘检测
//			IplImage dst = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
//			cvCanny(image, dst, 50, 100);
//			ShowImage(dst, "dst");
//			ShowImage(dst, "dst");
//			opencv_imgcodecs.cvSaveImage(path + "canney.jpg", dst);
//			cvReleaseImage(dst);
//			cvReleaseImage(image);
			
			//角点提取
			final int MAX_CORNERS = 500;
			IntPointer corner_count = new IntPointer(1).put(MAX_CORNERS);
			CvPoint2D32f corners = new CvPoint2D32f(MAX_CORNERS);
			IplImage srcImage,desImage,grayImage,corner1,corner2;
			CvScalar color = cvScalar(255, 0, 0, 0);
			srcImage = cvLoadImage(path);
			
			grayImage = cvCreateImage(cvGetSize(srcImage), IPL_DEPTH_8U, 1);
			desImage = cvCreateImage(cvGetSize(srcImage), IPL_DEPTH_8U, 1);
			
			cvCvtColor(srcImage, grayImage, CV_BGR2GRAY);
			cvCopy(grayImage, desImage);
			
			corner1 = cvCreateImage(cvGetSize(srcImage), IPL_DEPTH_32F, 1);
			corner2 = cvCreateImage(cvGetSize(srcImage), IPL_DEPTH_32F, 1);
			
			CvArr mask = null;
			cvGoodFeaturesToTrack(grayImage, corner1, corner2, corners, corner_count,0.04,10);
			System.out.println("number corners found :"+ corner_count.get());
			
			
			if(corner_count.get() > 0){
				for (int j = 0; j < corner_count.get(); j++) {
					CvPoint2D32f corner = corners.position(j);
						cvCircle(desImage, cvPoint((int)corner.x(),(int)corner.y()),8 , color);
						System.out.println(cvPoint((int)corner.x(),(int)corner.y()).toString());
				}
			}
			
			
			ShowImage(desImage, "desss");
			cvSaveImage(path + "goodfeaturestotrack.jpg", desImage);
			
			cvReleaseImage(corner1);
			cvReleaseImage(corner2);
			cvReleaseImage(srcImage);
			cvReleaseImage(desImage);
			cvReleaseImage(grayImage);
			
			
		}

	}

	public static void main(String[] args) {

		// String path = ScreenHandler.getInstance().catFullScreen();
		ScreenHandler.getInstance().opencvImage(
				PathUtils.getAppScreenShotsPath() + "111.jpg");

		// ReportHandler.getInstance().sendEmailToQQ(path);
	}

	/**
	 * 全屏截图并保存
	 */
	public String catFullScreen() {

		BufferedImage fullScreen = robot.createScreenCapture(new Rectangle(
				Toolkit.getDefaultToolkit().getScreenSize()));

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = "jpg";
		String saveName = sdf.format(cal.getTime()) + "." + format;
		String savePath = PathUtils.getAppScreenShotsPath() + saveName;

		try {
			ImageIO.write(fullScreen, format, new File(savePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return savePath;
	}

}
