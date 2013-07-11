package com.yugy.qianban.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.yugy.qianban.R;
import com.yugy.qianban.asisClass.Conf;
import com.yugy.qianban.asisClass.Func;
import com.yugy.qianban.database.Account;
import com.yugy.qianban.database.DatabaseManager;

import android.os.Bundle;
import android.os.Process;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SettingActivity extends Activity {

	private TextView login;
	private TextView cache;
	private TextView exit;
	private DatabaseManager database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		database = new DatabaseManager(this);
		
		login = (TextView)findViewById(R.id.setting_login);
		cache = (TextView)findViewById(R.id.setting_cache);
		exit = (TextView)findViewById(R.id.setting_exit);
		
		Account account = database.getAccount();
		if(account == null){
			login.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent  = new Intent(SettingActivity.this, LoginActivity.class);
					startActivityForResult(intent, Conf.REQUEST_LOGIN);
				}
			});
		}else{
			login.setText(account.name);
			login.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(SettingActivity.this, AccountActivity.class);
					startActivityForResult(intent, Conf.REQUEST_LOGOUT);
				}
			});
		}
		
		
		cache.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Func.toast(SettingActivity.this, "cache");
			}
		});
		
		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				android.os.Process.killProcess(Process.myPid());
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == Conf.REQUEST_LOGIN && resultCode == Conf.REQUEST_LOGIN_OK){
			Account account = new Account();
			try {
				account.parse(new JSONObject(data.getStringExtra("result")));
				login.setText(account.name);
				login.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SettingActivity.this, AccountActivity.class);
						startActivityForResult(intent, Conf.REQUEST_LOGOUT);
					}
				});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(requestCode == Conf.REQUEST_LOGOUT && resultCode == Conf.REQUEST_LOGOUT_OK){
			login.setText("登陆");
			login.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent  = new Intent(SettingActivity.this, LoginActivity.class);
					startActivityForResult(intent, Conf.REQUEST_LOGIN);
				}
			});
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
