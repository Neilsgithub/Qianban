package com.yugy.qianban.widget;

import com.yugy.qianban.asisClass.LrcFormat;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class LrcView extends ScrollView{
	
	private Paint mPaint;   //播放前和播放后歌词
	private Paint sPaint;   //正在播放歌词
	private float mX;
	private float middleY; //Y轴中心
	private static int DY = 35; //每行间隔
	public int index = 0;
	private LrcFormat lrcFormat;
	
	public LrcView(Context context) {
		super(context);
		init();
	}
	
	public LrcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LrcView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init(){
		setFocusable(true);
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(25);
		mPaint.setColor(Color.WHITE);
		mPaint.setTypeface(Typeface.SERIF);
		mPaint.setAlpha(100);
		
		sPaint = new Paint();
		sPaint.setAntiAlias(true);
		sPaint.setTextSize(25);
		sPaint.setColor(Color.WHITE);
		sPaint.setTypeface(Typeface.SERIF);
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint mp = mPaint;
		Paint sp = sPaint;
		mp.setTextAlign(Paint.Align.CENTER);
		if(lrcFormat.getIndex() == -1){
			return;
		}
		sp.setTextAlign(Paint.Align.CENTER);
		//先画出正在播放部分
		canvas.drawText(lrcFormat.getLrc(index), mX, middleY, sp);
		float tempY = middleY;
		//画出本句之前的句子
		for(int i = index - 1; i >= 0; i --){
			tempY = tempY - DY;
			canvas.drawText(lrcFormat.getLrc(i), mX, tempY, mp);
		}
		tempY = middleY;
		//画出本句之后的句子
		for(int i = index + 1; i < lrcFormat.getIndex(); i ++){
			tempY = tempY + DY;
			canvas.drawText(lrcFormat.getLrc(i), mX, tempY, mp);
		}
	}
	protected void onSizeChanged(int w, int h, int ow, int oh){
		super.onSizeChanged(w, h, ow, oh);
		mX = w * 0.5f;
		middleY = h * 0.5f;
	}
	
	public void setLrc(LrcFormat lrc){
		lrcFormat = lrc;
	}
}
