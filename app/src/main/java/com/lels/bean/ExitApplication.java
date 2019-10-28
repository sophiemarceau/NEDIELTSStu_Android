package com.lels.bean;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class ExitApplication extends Application{
	 private List<Activity> list = new ArrayList<Activity>();
	 private List<Activity> listExit = new ArrayList<Activity>();
	 
	    private static ExitApplication ea;

	    private ExitApplication() {

	    }

	    public static ExitApplication getInstance() {
	        if (null == ea) {
	            ea = new ExitApplication();
	        }
	        return ea;
	    }

	   
	    public void addActivity(Activity activity) {
	        list.add(activity);
	    }
	    public void addActivitylistExit(Activity activity) {
	    	listExit.add(activity);
	    }
	    public void exitK(){
	    	 for (Activity activity : list) {
		            activity.finish();
		        }
	    }
	    public void exit() {
	        for (Activity activity : listExit) {
	            activity.finish();
	        }
	        System.exit(0);
	    }
	    
	
}
