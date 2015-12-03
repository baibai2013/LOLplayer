package com.lyj.lolplayer;

import java.awt.Point;

public class MouseEventMessage {
	public static final int LEFT_CLICK = 0;
	public static final int RIGHT_CLICK = 1;
	public static final int MOVE_TO = 2;
	public static final int MOVE_TO_OFFSET = 3;
	public static final int CLICK_KEY = 4;
	
	public Point moveto;
	public int what;
	public int key;
	
	public MouseEventMessage() {
		// TODO Auto-generated constructor stub
	}
	
	MouseEventMessage(int what){
		this.what = what;
	}
	
	MouseEventMessage(int what,Point p){
		this.what = what;
		this.moveto = p;
	}
	
	MouseEventMessage(int what,int key){
		this.what = what;
		this.key = key;
	}
}
