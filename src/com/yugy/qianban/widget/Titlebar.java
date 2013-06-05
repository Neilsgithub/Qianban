package com.yugy.qianban.widget;

import com.yugy.qianban.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Titlebar extends LinearLayout{

	public Titlebar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public Titlebar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private TextView title;
	private ImageButton leftButton;
	private ImageButton rightButton;
	
	private void init(){
		LayoutInflater layoutInflater = LayoutInflater.from(getContext());
		layoutInflater.inflate(R.layout.widget_titlebar, this);
		title = (TextView)findViewById(R.id.titlebar_title);
		leftButton = (ImageButton)findViewById(R.id.titlebar_left);
		rightButton = (ImageButton)findViewById(R.id.titlebar_right);
	}
	
	public void setTitle(String title){
		this.title.setText(title);
	}
	
	public void setLeftButtonIcon(int id){
		leftButton.setImageResource(id);
	}
	
	public void setRightButtonIcon(int id){
		rightButton.setImageResource(id);
	}
	
	public void setLeftButtonBackground(int id){
		leftButton.setBackgroundResource(id);
	}
	
	public void setRightButtonBackground(int id){
		rightButton.setBackgroundResource(id);
	}
	
	public void setLeftClick(OnClickListener l){
		leftButton.setOnClickListener(l);
	}
	
	public void setRightClick(OnClickListener l){
		rightButton.setOnClickListener(l);
	}
}
