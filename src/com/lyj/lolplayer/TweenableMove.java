package com.lyj.lolplayer;

import java.awt.Point;

import aurelienribon.tweenengine.Tweenable;

public class TweenableMove implements Tweenable {  
    // The following lines define the different possible tween types.  
    // It's up to you to define what you need :-)  
  
    public static final int X = 1;  
    public static final int Y = 2;  
    public static final int XY = 3;  
  
    // Composition pattern  
  
    private Point target;  
  
    // Constructor  
  
    public TweenableMove(Point particule) {  
            this.target = particule;  
    }  
  
    // Tweenable implementation  
  
    @Override  
    public int getTweenValues(int tweenType, float[] returnValues) {  
    	System.out.println("getTweenValues ");
            switch (tweenType) {  
                    case X: returnValues[0] = (float) target.getX(); return 1;  
                    case Y: returnValues[0] = (float) target.getY(); return 1;  
                    case XY:  
                            returnValues[0] = (float) target.getX();  
                            returnValues[1] = (float) target.getY();  
                            return 2;  
                    default: assert false; return 0;  
            }  
    }  
      
    @Override  
    public void onTweenUpdated(int tweenType, float[] newValues) {  
    	System.out.println("onTweenUpdated ");
            switch (tweenType) {  
                    case X: target.setLocation(newValues[0], target.getY()); break;  
                    case Y: target.setLocation(target.getX(), newValues[1]);; break;  
                    case XY:  
                            target.setLocation(newValues[0], newValues[1]); 
                            break;  
                    default: assert false; break;  
            }  
    }  
} 