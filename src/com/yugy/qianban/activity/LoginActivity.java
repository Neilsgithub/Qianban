package com.yugy.qianban.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yugy.qianban.R;
import com.yugy.qianban.asisClass.Conf;
import com.yugy.qianban.asisClass.Func;
import com.yugy.qianban.database.Account;
import com.yugy.qianban.database.DatabaseManager;
import com.yugy.qianban.sdk.Douban;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private String mCaptchaCode;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mCaptcha;
	private ImageView captcha;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	private String captchaId;
	
	private Douban douban;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mEmailView = (EditText) findViewById(R.id.email);

		mPasswordView = (EditText) findViewById(R.id.password);
		
		mCaptcha = (EditText)findViewById(R.id.captcha);

		captcha = (ImageView)findViewById(R.id.sign_in_captcha);
		
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		douban = new Douban(this);
		getCaptcha();
	}
	
	private void getCaptcha(){
		captcha.setOnClickListener(null);
		douban.getCaptcha(new BinaryHttpResponseHandler(){
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				captchaId = content;
				super.onSuccess(content);
			}
			
			@Override
			public void onSuccess(byte[] binaryData) {
				// TODO Auto-generated method stub
				Bitmap bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
				captcha.setImageBitmap(bitmap);
				captcha.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getCaptcha();
					}
				});
				super.onSuccess(binaryData);
			}
		});
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mCaptcha.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mCaptchaCode = mCaptcha.getText().toString();

		boolean cancel = false;

		if (TextUtils.isEmpty(mCaptchaCode)) {
			mCaptcha.setError("此项不能为空");
			cancel = true;
		}
		
		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError("此项不能为空");
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError("此项不能为空");
			cancel = true;
		}

		if(!cancel){
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			login();
		}
	}
	
	private void login(){
		douban.login(mEmail, mPassword, mCaptchaCode, captchaId, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response) {
				// TODO Auto-generated method stub
				try {
					if(response.getInt("r") == 1){
						Func.toast(LoginActivity.this, response.getString("err_msg"));
						getCaptcha();
						showProgress(false);
					}else if(response.getInt("r") == 0){
						DatabaseManager database = new DatabaseManager(LoginActivity.this);
						database.setAccount(new Account().parse(response));
						showProgress(false);
						Intent intent = new Intent();
						intent.putExtra("result", response.toString());
						setResult(Conf.REQUEST_LOGIN_OK, intent);
						finish();
					}
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

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

}
