package com.yugy.qianban.sdk;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class GeCiMe {

	private static AsyncHttpClient client;
	
	private static void init(){
		if(client == null){
			client = new AsyncHttpClient();
		}
	}
	
	public static void getLrc(String songName, String authorName, JsonHttpResponseHandler responseHandler){
		init();
		client.get("http://geci.me/api/lyric/" + songName + "/" + authorName, responseHandler);
	}
	
	
}
