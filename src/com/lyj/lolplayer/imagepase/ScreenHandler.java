package com.lyj.lolplayer.imagepase;

import static org.bytedeco.javacpp.opencv_core.CV_32FC1;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_32F;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCopy;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateMat;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_core.cvReleaseImage;
import static org.bytedeco.javacpp.opencv_core.cvScalar;
import static org.bytedeco.javacpp.opencv_core.cvSetZero;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_core.cvarrToMat;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCircle;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvGoodFeaturesToTrack;
import static org.bytedeco.javacpp.opencv_imgproc.cvResize;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_objdetect.HOGDescriptor;
import org.bytedeco.javacpp.helper.opencv_core.CvArr;
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

			// // canny边缘检测
			// IplImage dst = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
			// cvCanny(image, dst, 50, 100);
			// ShowImage(dst, "dst");
			// ShowImage(dst, "dst");
			// opencv_imgcodecs.cvSaveImage(path + "canney.jpg", dst);
			// cvReleaseImage(dst);
			// cvReleaseImage(image);

			// 角点提取
			final int MAX_CORNERS = 500;
			IntPointer corner_count = new IntPointer(1).put(MAX_CORNERS);
			CvPoint2D32f corners = new CvPoint2D32f(MAX_CORNERS);
			IplImage srcImage, desImage, grayImage, corner1, corner2;
			CvScalar color = cvScalar(255, 0, 0, 0);
			srcImage = cvLoadImage(path);

			grayImage = cvCreateImage(cvGetSize(srcImage), IPL_DEPTH_8U, 1);
			desImage = cvCreateImage(cvGetSize(srcImage), IPL_DEPTH_8U, 1);

			cvCvtColor(srcImage, grayImage, CV_BGR2GRAY);
			cvCopy(grayImage, desImage);

			corner1 = cvCreateImage(cvGetSize(srcImage), IPL_DEPTH_32F, 1);
			corner2 = cvCreateImage(cvGetSize(srcImage), IPL_DEPTH_32F, 1);

			CvArr mask = null;
			cvGoodFeaturesToTrack(grayImage, corner1, corner2, corners,
					corner_count, 0.04, 10);
			System.out.println("number corners found :" + corner_count.get());

			if (corner_count.get() > 0) {
				for (int j = 0; j < corner_count.get(); j++) {
					CvPoint2D32f corner = corners.position(j);
					cvCircle(desImage,
							cvPoint((int) corner.x(), (int) corner.y()), 8,
							color);
					System.out.println(cvPoint((int) corner.x(),
							(int) corner.y()).toString());
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

	/**
	 * 制作列表
	 */
	public void makeSampleList() {
		ArrayList<String> strArr = new ArrayList<String>();
		String simplePath = PathUtils.getSimplePath();
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(
					PathUtils.getAppPath() + "/samples.txt", true);
			readPathAndType(strArr, simplePath);

			System.out.println("str size:" + strArr.size());

			for (int i = 0; i < strArr.size(); i++) {
				fileWriter.write(strArr.get(i) + "\n");
			}

			fileWriter.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void readPathAndType(ArrayList<String> strArr, String path) {

		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return;
		}

		for (File file : dir.listFiles()) {

			if (file.isFile()) {

				if (file.getName().equals(".DS_Store"))
					continue;

				String spath = file.getAbsolutePath();
				String stype = file.getParentFile().getName();

				System.out.println("spath:" + spath);
				System.out.println("stype:" + stype);

				strArr.add(spath);
				strArr.add(stype);
			} else if (file.isDirectory()) {
				readPathAndType(strArr, file.getAbsolutePath());
			}

		}

	}

	/**
	 * hog svm 训练
	 */
	public void trainHOGAndSVM() {

		ArrayList<String> imgPaths = new ArrayList<String>();
		ArrayList<Integer> imgTypes = new ArrayList<Integer>();
		int nLine = 0;
		try {
			String encodeing = "UTF-8";
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					new File(PathUtils.getAppPath() + "/samples.txt")),
					encodeing);
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;

			while ((lineTxt = bufferedReader.readLine()) != null) {
				nLine++;
				if (nLine % 2 == 0) { // 奇数路径 偶数标签
					imgTypes.add(Integer.parseInt(lineTxt.trim()));
					System.out.println("type:" + lineTxt);
				} else {
					imgPaths.add(lineTxt);
					System.out.println("path:" + lineTxt);
				}
				
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CvMat dataMat, resMat;
		int nImgNum = imgPaths.size();
		int nTypeNum = imgTypes.size();
		dataMat = cvCreateMat(nImgNum, 324, CV_32FC1);// 第二个参数，即矩阵的列是由下面的descriptors的大小决定的，可以由descriptors.size()得到，且对于不同大小的输入训练图片，这个值是不同的
		cvSetZero(dataMat);
		//类型矩阵，存储每个样本类型标志
		resMat = cvCreateMat(nTypeNum, 1, CV_32FC1);
		cvSetZero(dataMat);
		
		IplImage src;
		IplImage traimImg = cvCreateImage(cvSize(80,140), 8, 3);//需要分析的图片，这里默认设定图片是28*28大小，所以上面定义了324，如果要更改图片大小，可以先用debug查看一下descriptors是多少，然后设定好再运行    
	    
		//处理HOG特征
		for(int i = 0;i< nImgNum; i++){
			src = cvLoadImage(imgPaths.get(i));
			if(src == null){
				System.out.println("图片获取失败");
				continue;
			}
			System.out.println("开始处理:"+ imgPaths.get(i));
			
			cvResize(src, traimImg);
			HOGDescriptor hog = new HOGDescriptor();
//			hog.svmDetector(HOGDescriptor.getDaimlerPeopleDetector());
			FloatPointer descriptors = new FloatPointer();
			hog.compute(cvarrToMat(traimImg), descriptors);//Hog特征计算
			System.out.println("descriptorslength:"+descriptors.get());
//			int n = 0;
//			for(int j = 0; j< descriptors.length;j++){
//				cvmSet(dataMat, i, n, descriptors[i]);
//				n++;
//			}
//			cvmSet(resMat, i, 0, imgTypes.get(i));
			System.out.println("结束处理:"+ imgPaths.get(i));
		}
		
//		SVM svm = SVM.create();
//		Mat mdataMat = new Mat(dataMat);
//		Mat mresMat = new Mat(resMat);
//		svm.train(mdataMat,10,mresMat);
//		svm.save(PathUtils.getAppPath() + "/HOG_SVM_DATA.xml");

	}

	public static void main(String[] args) {

		// String path = ScreenHandler.getInstance().catFullScreen();
		// ScreenHandler.getInstance().opencvImage(
		// PathUtils.getAppScreenShotsPath() + "111.jpg");

		// ReportHandler.getInstance().sendEmailToQQ(path);

		// ScreenHandler.getInstance().makeSampleList();
		ScreenHandler.getInstance().trainHOGAndSVM();
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
