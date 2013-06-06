package com.yugy.qianban.asisClass;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Func {

	public static void log(String log){
		Log.d(Conf.TAG, log);
	}
	
	public static void toast(Context context, String a){
		Toast.makeText(context, a, Toast.LENGTH_SHORT).show();
	}
	
}
