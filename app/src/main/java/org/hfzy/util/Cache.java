package org.hfzy.util;


import android.content.Context;

public class Cache {

	public static void SetChche(String url, String json, Context context) {

		Sf_Util.setParam(context, url, json);
	}

	public static String GetChche(String url, String faultValue, Context context) {

		String cache = (String)Sf_Util.getParam(context, url, faultValue);
		return cache;
	}
}
