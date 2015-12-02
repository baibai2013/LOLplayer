package com.lyj.lolplayer.utils;

import java.io.File;

import com.lyj.lolplayer.Common;

public class PathUtils {
	
	public static final String appSavePath = "/Users/li/Desktop/"+Common.AppName;
	
	public static String getAppPath(){
		return appSavePath;
	}
	
	public static String getAppScreenShotsPath(){
		String screenShotsPath = getAppPath() + "/screenShots/";
		return checkAndMkdirs(screenShotsPath);
	}
	
	public static String getSimplePath(){
		String screenShotsPath = getAppPath() + "/sample/";
		return checkAndMkdirs(screenShotsPath);
	}
	
	public static String checkAndMkdirs(String dir) {
		File file = new File(dir);
		if (file.exists() == false) {
			file.mkdirs();
		}
		return dir;
	}
	
}
