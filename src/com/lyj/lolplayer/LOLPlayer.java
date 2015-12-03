package com.lyj.lolplayer;

import java.awt.Event;
import java.awt.event.KeyEvent;
import java.util.Stack;

import javax.swing.RepaintManager;

import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Rect;

import com.lyj.lolplayer.imagepase.ScreenHandler;
import com.lyj.lolplayer.play.report.ReportHandler;
import com.lyj.lolplayer.utils.PathUtils;

public class LOLPlayer implements Runnable {

	public Stack<String> imgPaths = new Stack<String>();

	public MouseHandler mouseHandler;
	public ScreenHandler screenHandler;
	public ReportHandler reportHandler;

	private boolean isruning = true;

	public boolean isIsruning() {
		return isruning;
	}

	public void setIsruning(boolean isruning) {
		this.isruning = isruning;
	}

	public LOLPlayer() {

		mouseHandler = MouseHandler.getDefaultMouseHandler();
		screenHandler = ScreenHandler.getDefaultScreenHandler(this);
		reportHandler = ReportHandler.getInstance();

	}

	public static void main(String[] args) {

		LOLPlayer player = new LOLPlayer();
		Thread trThread = new Thread(player);
		trThread.start();

	}

	@Override
	public void run() {
		while (isruning) {
			synchronized (imgPaths) {
				if (!imgPaths.isEmpty()) {
					lookAndFeedback();
				}
			}
		}
	}

	private void lookAndFeedback() {
		System.out.println("->LOLPlayer handleScreen");
		String srcPath = imgPaths.pop();
		
//		lookTextTitle(srcPath);
//		if (lookYoudao(srcPath) && !lookText2(srcPath)) {
//			System.out.println("->result: contents youdao");
//		} else if (lookTextIcon(srcPath)) {
//			System.out.println("->result: contents lookTextIcon");
//		} else if (lookTextTitle(srcPath)) {
//			System.out.println("->result: contents lookTextTitle");
//		}

		ScreenHandler.removeimg(srcPath);
	}

	private boolean lookText2(String srcPath) {
		// look 有道词典
		String youdaoTemplatePath = PathUtils.getAppPath() + "sublimetitle2.png";
		System.out.println(srcPath);
		System.out.println(youdaoTemplatePath);
		Point p = screenHandler.templateMatching(youdaoTemplatePath, srcPath);
		
		Rect rect = new Rect(48, 3, 115,23);
		boolean contents = rect.contains(p);
//		if (contents) {
//			mouseHandler
//			.sendMouseEventMSG(new MouseEventMessage(
//					MouseEventMessage.MOVE_TO, new java.awt.Point(
//							p.x(), p.y())));
//			mouseHandler.sendMouseEventMSG(new MouseEventMessage(
//					MouseEventMessage.LEFT_CLICK));
//			
			// reportHandler.sendEmailToQQ(srcPath);
//		}
		return contents;
	}
	private boolean lookTextIcon(String srcPath) {
		// look 有道词典
		String youdaoTemplatePath = PathUtils.getAppPath() + "sub.png";
		System.out.println(srcPath);
		System.out.println(youdaoTemplatePath);
		Point p = screenHandler.templateMatching(youdaoTemplatePath, srcPath);

		Rect rect = new Rect(1310, 793, 151, 125);
		boolean contents = rect.contains(p);
		if (contents) {
			mouseHandler
					.sendMouseEventMSG(new MouseEventMessage(
							MouseEventMessage.MOVE_TO, new java.awt.Point(
									p.x(), p.y())));
			mouseHandler.sendMouseEventMSG(new MouseEventMessage(
					MouseEventMessage.LEFT_CLICK));

			// reportHandler.sendEmailToQQ(srcPath);
		}
		return contents;
	}

	private boolean lookTextTitle(String srcPath) {
		// look 有道词典
		String youdaoTemplatePath = PathUtils.getAppPath() + "sublimetitle.png";
		System.out.println(srcPath);
		System.out.println(youdaoTemplatePath);
		Point p = screenHandler.templateMatching(youdaoTemplatePath, srcPath);

		Rect rect = new Rect(913, 15,67,19);
		boolean contents = rect.contains(p);
		if (contents) {
			mouseHandler.sendMouseEventMSG(new MouseEventMessage(
					MouseEventMessage.CLICK_KEY, KeyEvent.VK_H));
			mouseHandler.sendMouseEventMSG(new MouseEventMessage(
					MouseEventMessage.CLICK_KEY, KeyEvent.VK_E));
			mouseHandler.sendMouseEventMSG(new MouseEventMessage(
					MouseEventMessage.CLICK_KEY, KeyEvent.VK_L));
			mouseHandler.sendMouseEventMSG(new MouseEventMessage(
					MouseEventMessage.CLICK_KEY, KeyEvent.VK_L));
			mouseHandler.sendMouseEventMSG(new MouseEventMessage(
					MouseEventMessage.CLICK_KEY, KeyEvent.VK_O));
			mouseHandler.sendMouseEventMSG(new MouseEventMessage(
					MouseEventMessage.CLICK_KEY, KeyEvent.VK_SPACE));
			mouseHandler.sendMouseEventMSG(new MouseEventMessage(
					MouseEventMessage.CLICK_KEY, KeyEvent.VK_Y));
			mouseHandler.sendMouseEventMSG(new MouseEventMessage(
					MouseEventMessage.CLICK_KEY, KeyEvent.VK_A));
			mouseHandler.sendMouseEventMSG(new MouseEventMessage(
					MouseEventMessage.CLICK_KEY, KeyEvent.VK_J));
			mouseHandler.sendMouseEventMSG(new MouseEventMessage(
					MouseEventMessage.CLICK_KEY, KeyEvent.VK_U));
			mouseHandler.sendMouseEventMSG(new MouseEventMessage(
					MouseEventMessage.CLICK_KEY, KeyEvent.VK_N));

			// reportHandler.sendEmailToQQ(srcPath);
		}
		return contents;
	}

	private boolean lookYoudao(String srcPath) {
		// look 有道词典
		String youdaoTemplatePath = PathUtils.getAppPath() + "lau.png";
		System.out.println(srcPath);
		System.out.println(youdaoTemplatePath);
		Point p = screenHandler.templateMatching(youdaoTemplatePath, srcPath);

		Rect rect = new Rect(620, 1037, 35, 39);
		boolean contents = rect.contains(p);
		if (contents) {
			mouseHandler
					.sendMouseEventMSG(new MouseEventMessage(
							MouseEventMessage.MOVE_TO, new java.awt.Point(
									p.x(), p.y())));
			mouseHandler.sendMouseEventMSG(new MouseEventMessage(
					MouseEventMessage.LEFT_CLICK));

			// reportHandler.sendEmailToQQ(srcPath);
		}
		return contents;
	}

}
