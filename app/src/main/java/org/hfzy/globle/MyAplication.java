package org.hfzy.globle;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class MyAplication extends Application {

	private static Context context;
	private static int mainTid;
	private static Handler handler;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = getApplicationContext();
		handler = new Handler();
		mainTid = android.os.Process.myTid();
		
	}

	public static Context getContext() {
		return context;
	}

	public static int getMainThreadId() {
		return mainTid;
	}

	public static Handler getHandler() {
		return handler;
	}
	
	
}
