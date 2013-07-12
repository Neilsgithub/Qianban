package com.yugy.qianban.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.yugy.qianban.R;

public class PlayControlRight extends RelativeLayout{

	public ImageButton love;
	public ImageButton ban;
	public ImageButton download;
	public ImageButton share;
	
	
	public PlayControlRight(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.widget_playcontrol_right, this);
		love = (ImageButton)findViewById(R.id.playcontrol_love);
		ban = (ImageButton)findViewById(R.id.playcontrol_ban);
		download = (ImageButton)findViewById(R.id.playcontrol_download);
		share = (ImageButton)findViewById(R.id.playcontrol_share);
	}
	
}
