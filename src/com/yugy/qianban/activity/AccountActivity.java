package com.yugy.qianban.activity;

import com.yugy.qianban.R;
import com.yugy.qianban.R.layout;
import com.yugy.qianban.R.menu;
import com.yugy.qianban.asisClass.Conf;
import com.yugy.qianban.database.Account;
import com.yugy.qianban.database.DatabaseManager;
import com.yugy.qianban.sdk.Douban;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AccountActivity extends Activity {

	private TextView id;
	private TextView name;
	private TextView record;
	private Button logout;
	private DatabaseManager database;
	private Douban douban;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		id = (TextView)findViewById(R.id.account_id);
		name = (TextView)findViewById(R.id.account_name);
		record = (TextView)findViewById(R.id.account_record);
		logout = (Button)findViewById(R.id.account_logout);
		
		database = new DatabaseManager(this);
		douban = new Douban(this);
		final Account account = database.getAccount();
		id.setText(account.id + "");
		name.setText(account.name);
		record.setText("累计收听" + account.played +"首 加红心" + account.liked + "首 不再播放" + account.banned + "首");
		
		logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				douban.logout(account.ck);
				database.removeAccount();
				setResult(Conf.REQUEST_LOGOUT_OK);
				finish();
			}
		});
	}

}
