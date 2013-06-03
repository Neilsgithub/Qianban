package com.yugy.qianban.activity;

import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yugy.qianban.R;
import com.yugy.qianban.sdk.Douban;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {

	private Douban douban;
	private JSONObject catelog;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getCatelog();
    }
    
    private void init(){
    	douban = new Douban();
    }
    
    private void getCatelog(){
    	douban.getCatalog(new JsonHttpResponseHandler(){
        	@Override
        	public void onSuccess(JSONObject response) {
        		// TODO Auto-generated method stub
        		catelog= response;
        		super.onSuccess(response);
        	}
        });
    }
}
