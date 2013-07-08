package com.yugy.qianban.sdk;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class LRC {

	private static AsyncHttpClient client;
	
	private static void init(){
		if(client == null){
			client = new AsyncHttpClient();
		}
	}
	
	public static void searchLrc(String songName, String authorName, AsyncHttpResponseHandler responseHandler){
		init();
		client.get("http://box.zhangmen.baidu.com/x?op=12&count=1&title=" + songName + "$$" + authorName + "$$$$", responseHandler);
	}
	
	public static void downloadLrc(String lrcUrl, final AsyncHttpResponseHandler responseHandler){
		init();
		client.get(lrcUrl, new AsyncHttpResponseHandler("GBK"){
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				responseHandler.onSuccess(content);
				super.onSuccess(content);
			}
		});
	}
	
}
