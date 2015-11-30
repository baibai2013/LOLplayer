package com.lyj.lolplayer.imagepase;

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

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

import com.lyj.lolplayer.utils.PathUtils;

/**
 * 截屏处理
 * @author lili
 *
 */
public class ScreenHandler {
	private Robot robot;
	public static ScreenHandler defaultScreenHandler = new ScreenHandler();

	
	public static ScreenHandler getInstance(){
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
	
	
	public void opencvImage(String path){
		IplImage image = opencv_imgcodecs.cvLoadImageBGRA(path);
		if(image != null){
			opencv_imgproc.cvSmooth(image, image);
			opencv_imgcodecs.cvSaveImage(path+".jpg",image);
//			opencv_imgcodecs.cvReleaseImage(image);
		}
		
	}
	
	
	public static void main(String[] args) {
		
		String path = ScreenHandler.getInstance().catFullScreen();
		ScreenHandler.getInstance().opencvImage(path);
		
//		ReportHandler.getInstance().sendEmailToQQ(path);
	}
	
	
	/**
	 * 	全屏截图并保存
	 */
	public String catFullScreen(){
		
		BufferedImage fullScreen = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = "jpg";
		String saveName = sdf.format(cal.getTime()) +"."+format;
		String savePath = PathUtils.getAppScreenShotsPath() + saveName;
		
		try {
			ImageIO.write(fullScreen, format, new File(savePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return savePath;
	}
	
	
}
