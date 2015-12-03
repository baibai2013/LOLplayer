package com.lyj.lolplayer;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.util.Stack;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Cubic;

/**
 * 鼠标处理器
 * 
 * @author lili
 *
 */
public class MouseHandler implements Runnable {

	private Dimension dim;
	private Robot robot;
	private boolean runing = true;

	private Stack<MouseEventMessage> mouseEvents = new Stack<MouseEventMessage>();

	public boolean isRuning() {
		return runing;
	}

	public void setRuning(boolean runing) {
		this.runing = runing;
	}

	public MouseHandler() {

		dim = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println("屏幕宽高：[" + dim.getWidth() + "x" + dim.getHeight()
				+ "]");
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		Tween.setPoolEnabled(true);

	}

	/**
	 * 移动到位置
	 */
	public void moveMouse(int x, int y) {
		robot.mouseMove(x, y);
	}

	// public void moveMouseTo(Point pos) {
	// System.out.println("moveMouseTo");
	// TweenableMove tweenableMove = new TweenableMove(mousePosion);
	// Tween tween = Tween.to(tweenableMove, TweenableMove.XY, 3000,
	// Cubic.OUT).target((float)pos.getX(),(float)pos.getY());
	// manager.add(tween.start());
	//
	// }
	//
	//
	// public void moveMouseTo(int offsetX, int offsetY) {
	// System.out.println("moveMouseTo");
	//
	// Point mousePoint = MouseInfo.getPointerInfo().getLocation();
	// System.out.println("移动前[" + mousePoint.x + ":" + mousePoint.y + "]");
	// offsetX += mousePoint.x;
	// offsetY += mousePoint.y;
	//
	// robot.delay(1000);
	// robot.mouseMove(offsetX, offsetY);
	// System.out.println("移动后[" + offsetX + ":" + offsetY + "]");
	// }

	
	public void clickKey(int keycode){
		robot.keyPress(keycode);
	}
	
	public void leftClickMouse() {
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	public void rightClick() {
		robot.mousePress(InputEvent.BUTTON3_MASK);
		robot.mouseRelease(InputEvent.BUTTON3_MASK);
	}

	public static void main(String[] args) {

		
		MouseHandler mouseHandler =   MouseHandler.getDefaultMouseHandler();
		
		mouseHandler.sendMouseEventMSG(new MouseEventMessage(MouseEventMessage.MOVE_TO,new Point(100, 100)));
		mouseHandler.sendMouseEventMSG(new MouseEventMessage(MouseEventMessage.LEFT_CLICK));
		mouseHandler.sendMouseEventMSG(new MouseEventMessage(MouseEventMessage.MOVE_TO,new Point(568, 1054)));
		mouseHandler.sendMouseEventMSG(new MouseEventMessage(MouseEventMessage.RIGHT_CLICK));
//		mouseHandler.sendMouseEventMSG(new MouseEventMessage(MouseEventMessage.MOVE_TO,new Point(618, 1054)));
//		mouseHandler.sendMouseEventMSG(new MouseEventMessage(MouseEventMessage.LEFT_CLICK));
		mouseHandler.sendMouseEventMSG(new MouseEventMessage(MouseEventMessage.MOVE_TO_OFFSET,new Point(50, -180)));
		mouseHandler.sendMouseEventMSG(new MouseEventMessage(MouseEventMessage.LEFT_CLICK));
		
		
	}
	
	
	public static MouseHandler getDefaultMouseHandler(){
		MouseHandler mouseHandler = new MouseHandler();
		Thread thread = new Thread(mouseHandler);
		thread.start();
		return mouseHandler;
	}
	
	
	public void sendMouseEventMSG(MouseEventMessage msg){
		mouseEvents.add(msg);
	}

	@Override
	public void run() {
		while (runing) {
			synchronized (mouseEvents) {
				if (!mouseEvents.isEmpty()) {
					System.out.println("->MouseHandler handerMouseEvent...");
					
					robot.delay(1 * 1000);
					MouseEventMessage msg = mouseEvents.pop();
					switch (msg.what) {
					case MouseEventMessage.LEFT_CLICK:
					{
						leftClickMouse();
						Point point = msg.moveto;
						if(point != null)
							moveMouse(point.x, point.y);
					}
						break;
					case MouseEventMessage.RIGHT_CLICK:
					{
						rightClick();
						Point point = msg.moveto;
						if(point != null)
							moveMouse(point.x, point.y);
					}
						break;
					case MouseEventMessage.MOVE_TO:
					{
						Point point = msg.moveto;
						moveMouse(point.x, point.y);
					}
						break;
					case MouseEventMessage.MOVE_TO_OFFSET:
					{
						Point point = msg.moveto;
						Point mousePoint = MouseInfo.getPointerInfo().getLocation();
						mousePoint.translate(point.x, point.y);
						moveMouse(mousePoint.x, mousePoint.y);
					}
					break;
					case MouseEventMessage.CLICK_KEY:
					{
						int key = msg.key;
						clickKey(key);;
					}
					break;
					default:
						break;
					}

					

				}
				
			}

		}

	}
}
