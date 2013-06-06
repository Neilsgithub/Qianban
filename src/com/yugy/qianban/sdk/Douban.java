package com.yugy.qianban.sdk;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

public class Douban {
	
	private AsyncHttpClient client;
	
	public Douban(Context context){
		client = new AsyncHttpClient();
		client.setCookieStore(new PersistentCookieStore(context));
	}
	
	public void getCatalog(final JsonHttpResponseHandler responseHandler){
		client.get("http://douban.fm/", new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				JSONObject result = new JSONObject(); 
				Pattern hotPattern = Pattern.compile("window\\.hot_channels_json = ([^;]*)");
				Matcher hotMatcher = hotPattern.matcher(content); 
				Pattern fastPattern = Pattern.compile("window\\.fast_channels_json = ([^;]*)");
				Matcher fastMatcher = fastPattern.matcher(content); 
				Pattern comPattern = Pattern.compile("window\\.com_channels_json = ([^;]*)");
				Matcher comMatcher = comPattern.matcher(content);
				try {
					if(hotMatcher.find()){
						JSONArray jsonArray = new JSONArray(hotMatcher.group(1));
						result.put("hot_channels", jsonArray);
					}
					if(fastMatcher.find()){
						JSONArray jsonArray = new JSONArray(fastMatcher.group(1));
						result.put("fast_channels", jsonArray);
					}
					if(comMatcher.find()){
						JSONArray jsonArray = new JSONArray(comMatcher.group(1));
						result.put("com_channels", jsonArray);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//json����ʧ��
					e.printStackTrace();
				}
				responseHandler.onSuccess(result);
				super.onSuccess(content);
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
			}
		});
	}
	
	public void getSongs(String id, final JsonHttpResponseHandler responseHandler){
		client.get("http://douban.fm/j/mine/playlist?type=n&channel=" + id + "&pb=64&from=mainsite", new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response) {
				// TODO Auto-generated method stub
				try {
					responseHandler.onSuccess(response.getJSONArray("song"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onSuccess(response);
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
			}
		});
	}
	
}
