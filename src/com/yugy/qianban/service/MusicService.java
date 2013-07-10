package com.yugy.qianban.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.yugy.qianban.R;
import com.yugy.qianban.activity.MainActivity.UIController;
import com.yugy.qianban.asisClass.Func;
import com.yugy.qianban.asisClass.Song;
import com.yugy.qianban.widget.CoverFlow;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MusicService extends Service{

	private MediaPlayer mediaPlayer;
	private IBinder binder;
	private Timer timer;
	private TimerTask timerTask;
	private SeekBar seekBar;
	private ArrayList<Song> songs;
	private ImageButton play;
	private CoverFlow coverFlow;
	private UIController controller;
	
	public int currentSongId = 0;
	
	public void setUIController(UIController c){
		controller = c;
	}
	
	private OnPreparedListener onPreparedListener = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			controller.hideProgressBar();
			controller.setCoverFlowSelectable(true);
			mediaPlayer.start();
			seekBar.setMax(mediaPlayer.getDuration());
			play.setImageResource(R.drawable.pause);
		}
	};
	
	private OnCompletionListener onCompletionListener = new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			if(currentSongId != songs.size() - 1){
				nextSongWithoutFlip();
				coverFlow.flipToNext();
			}else{
				play.setImageResource(R.drawable.play);
			}
		}
	};
	
	@Override
	public void onCreate() {
		initMediaPlayer();
		super.onCreate();
	}
	
	public void setCoverFlow(CoverFlow c){
		coverFlow = c;
	}
	
	public int getDuration(){
		if(mediaPlayer != null){
			return mediaPlayer.getCurrentPosition();
		}else{
			return 0;
		}
	}
	
	public void setPlayButton(ImageButton button){
		play = button;
		play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(mediaPlayer.isPlaying()){
					mediaPlayer.pause();
					play.setImageResource(R.drawable.play);
				}else if(mediaPlayer != null){
					mediaPlayer.start();
					play.setImageResource(R.drawable.pause);
				}
			}
		});
	}
	
	public void setSongList(ArrayList<Song> list){
		songs = list;
		currentSongId = 0;
		playSong(songs.get(currentSongId).songUrl);
	}
	
	public void resetMediaPlayer(){
		mediaPlayer.reset();
	}
	
	private void initMediaPlayer(){
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	}
	
	/**
	 * 下一首歌，不带封面翻转控制
	 */
	public void lastSongWithoutFlip(){
		if(currentSongId != 0){
			currentSongId--;
			playSong(songs.get(currentSongId).songUrl);
			Func.log("Start to cache " + songs.get(currentSongId).songUrl);
    	}
	}
	
	/**
	 *  上一首歌，不带封面翻转控制
	 */
	public void nextSongWithoutFlip(){
		if(currentSongId != songs.size() - 1){
			currentSongId++;
			playSong(songs.get(currentSongId).songUrl);
			Func.log("Start to cache " + songs.get(currentSongId).songUrl);
    	}
	}
	
	public void playLastSong(){
		coverFlow.flipToPrevious();
	}
	
	public void playNextSong(){
		coverFlow.flipToNext();
	}
	
	public void setSeekbar(final SeekBar seekBar){
		this.seekBar = seekBar;
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				if(arg2){
					mediaPlayer.seekTo(arg1);
					if(!mediaPlayer.isPlaying()){
						mediaPlayer.start();
					}
				}
			}
		});
		timer = new Timer();
		timerTask = new TimerTask() {
			
			@Override
			public void run() {
				if(mediaPlayer != null){
					if(mediaPlayer.isPlaying()){
						seekBar.setProgress(mediaPlayer.getCurrentPosition());
					}
				}
			}
		};
		timer.schedule(timerTask, 0, 1000);
	}
	
	private void playSong(String url){
		controller.loadLrc(currentSongId);
		mediaPlayer.reset();
		controller.showProgressBar();
		controller.setCoverFlowSelectable(false);
    	try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepareAsync();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	mediaPlayer.setOnPreparedListener(onPreparedListener);
    	mediaPlayer.setOnCompletionListener(onCompletionListener);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		binder = new MusicService.LocalBinder();
		return binder;
	}
	
	public class LocalBinder extends Binder{
		
		public MusicService getService(){
			return MusicService.this;
		}
	}
	
	public void seekTo(int time){
		mediaPlayer.seekTo(time);
	}
	
	public boolean isPlaying(){
		if(mediaPlayer != null){
			return mediaPlayer.isPlaying();
		}
		return false;
	}
	
	@Override
	public void onDestroy() {
		if(mediaPlayer != null){
			mediaPlayer.release();
		}
		timerTask.cancel();
		timer.cancel();
		super.onDestroy();
	}
}
