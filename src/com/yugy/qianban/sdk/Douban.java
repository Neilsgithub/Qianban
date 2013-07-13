package com.yugy.qianban.sdk;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class Douban {
	
	private AsyncHttpClient client;
	private Context context;
	
	public Douban(Context c){
		context = c;
		client = new AsyncHttpClient();
		client.setCookieStore(new PersistentCookieStore(context));
	}
	
	public void getCaptcha(final BinaryHttpResponseHandler responseHandler){
		
		client.get("http://douban.fm/j/new_captcha", new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				String captcha = content.substring(1, content.length() - 1);
				responseHandler.onSuccess(captcha);
				
				client.get("http://douban.fm/misc/captcha?size=m&id=" + captcha, new BinaryHttpResponseHandler(){
					@Override
					public void onSuccess(byte[] binaryData) {
						// TODO Auto-generated method stub
						responseHandler.onSuccess(binaryData);
						super.onSuccess(binaryData);
					}
				});
				super.onSuccess(content);
			}
		});
		
		
	}
	
	public void login(String account, String password, String captcha, String captchaId, JsonHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		params.put("source", "radio");
		params.put("alias", account);
		params.put("form_password", password);
		params.put("captcha_solution", captcha);
		params.put("captcha_id", captchaId);
		params.put("remember", "on");
		params.put("task", "sync_channel_list");
		client.setCookieStore(new PersistentCookieStore(context));
		client.post("http://douban.fm/j/login", params, responseHandler);
	}
	
	public void logout(String ck){
		client.get("http://douban.fm/partner/logout?source=radio&ck=" + ck + "&no_login=y", new AsyncHttpResponseHandler());
	}
	
	public void getCatalog(final JsonHttpResponseHandler responseHandler){
		final JSONObject result = new JSONObject();
		client.get("http://douban.fm/j/explore/hot_channels?start=0&limit=20", new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				// TODO Auto-generated method stub
				try {
					result.put("hot_channels", response.getJSONObject("data").getJSONArray("channels"));
					responseHandler.onSuccess(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onSuccess(statusCode, response);
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
			}
		});
	}
	
	public void getSongs(String cid, final JsonHttpResponseHandler responseHandler){
		client.get("http://douban.fm/j/mine/playlist?type=n&channel=" + cid + "&from=mainsite", new JsonHttpResponseHandler(){
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
	
	public void getNextSongs(String cid, int sid, final JsonHttpResponseHandler responseHandler){
		client.get("http://douban.fm/j/mine/playlist?type=p&channel=" + cid + "&sid=" + sid + "&from=mainsite", new JsonHttpResponseHandler(){
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
