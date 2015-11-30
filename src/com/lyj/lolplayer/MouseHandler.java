package com.lyj.lolplayer;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Cubic;

/**
 * 鼠标处理器
 * 
 * @author lili
 *
 */
public class MouseHandler implements Runnable{

	private Dimension dim;
	private Robot robot;
	private boolean runing = true;

	private Point mousePosion = new Point(200,200);
	
	TweenManager manager;  
	
	public boolean isRuning() {
		return runing;
	}


	public void setRuning(boolean runing) {
		this.runing = runing;
	}




	public MouseHandler() {

		dim = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println("屏幕宽高：[" + dim.getWidth() + "x" + dim.getHeight()+"]");
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		
		Tween.setPoolEnabled(true);
		manager = new TweenManager();
		
		
		
	}

	/**
	 * 移动到位置
	 */
	public void moveMouseTo(Point pos) {
		System.out.println("moveMouseTo");
		TweenableMove tweenableMove = new TweenableMove(mousePosion);
		Tween tween = Tween.to(tweenableMove, TweenableMove.XY, 3000, Cubic.OUT).target((float)pos.getX(),(float)pos.getY());
		manager.add(tween.start());
		
	}
	

	public void moveMouseTo(int offsetX, int offsetY) {
		System.out.println("moveMouseTo");

		Point mousePoint = MouseInfo.getPointerInfo().getLocation();
		System.out.println("移动前[" + mousePoint.x + ":" + mousePoint.y + "]");
		offsetX += mousePoint.x;
		offsetY += mousePoint.y;

		robot.delay(1000);
		robot.mouseMove(offsetX, offsetY);
		System.out.println("移动后[" + offsetX + ":" + offsetY + "]");
	}

	public void leftClickMouse() {

	}

	public void rightClick() {

	}
	
	public static void main(String[] args) {
		MouseHandler mouseHandler = new MouseHandler();
		Thread thread = new Thread(mouseHandler);
		thread.start();
		
		mouseHandler.moveMouseTo(new Point(500,100));
//		mouseHandler.moveMouseTo(new Point(700,900));
//		mouseHandler.moveMouseTo(new Point(200,1000));
	}


	@Override
	public void run() {
		
		while (runing) {
			if(manager.getTweenCount() > 0){
				robot.delay(1000);
				robot.mouseMove(mousePosion.x,mousePosion.y);
			}
		}
		
	}

}
