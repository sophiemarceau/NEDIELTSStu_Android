package com.lels.bean;

public class ButtonControl {
	
	    private static long lastClickTime;
	    public synchronized static boolean isFastClick() {
	        long time = System.currentTimeMillis();   
	        if ( time - lastClickTime < 1000) {   
	            return true;   
	        }   
	        lastClickTime = time;   
	        return false;   
	    }
	
}
