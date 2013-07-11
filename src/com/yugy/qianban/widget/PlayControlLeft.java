package com.yugy.qianban.widget;

import com.yugy.qianban.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class PlayControlLeft extends RelativeLayout{

	public ImageButton last;
	public ImageButton play;
	public ImageButton next;
	
	
	public PlayControlLeft(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.widget_playcontrol_left, this);
		last = (ImageButton)findViewById(R.id.playcontrol_lastsong);
		play = (ImageButton)findViewById(R.id.playcontrol_play);
		next = (ImageButton)findViewById(R.id.playcontrol_nextsong);
	}
}
