package com.yugy.qianban.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.fedorvlasov.lazylist.ImageLoader;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yugy.qianban.R;
import com.yugy.qianban.asisClass.Conf;
import com.yugy.qianban.asisClass.FuncInt;
import com.yugy.qianban.asisClass.Song;
import com.yugy.qianban.sdk.Douban;
import com.yugy.qianban.widget.CoverFlow;
import com.yugy.qianban.widget.Titlebar;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.app.Activity;
import android.content.Intent;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	private Titlebar titlebar;
	private CoverFlow coverFlow;
	private ImageButton previous;
	private ImageButton play;
	private ImageButton next;
	private SeekBar seekBar;
	
	private int currentSongId;
	private boolean isCached;
	
	private ArrayList<Song> albums;
	private Douban douban;
	private ImageLoader imageLoader;
	private JSONObject catelog;
	private AlbumAdapter albumAdapter;
	private MediaPlayer mediaPlayer;
	private Timer timer;
	private TimerTask timerTask;
	String CatalogId = "";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getCatelog();
        getSongs("1");
    }
    
    private void init(){
    	titlebar = (Titlebar)findViewById(R.id.main_titlebar);
    	titlebar.setTitle("Qianban");
    	titlebar.setLeftButtonIcon(R.drawable.catelog_button_icon);
    	titlebar.setRightButtonIcon(R.drawable.setting_button_icon);
    	
    	coverFlow = (CoverFlow)findViewById(R.id.main_coverflow);
    	
    	previous = (ImageButton)findViewById(R.id.main_lastsong);
    	play = (ImageButton)findViewById(R.id.main_play);
    	next = (ImageButton)findViewById(R.id.main_nextsong);
    	setButtonClick();
    	
    	douban = new Douban();
    	imageLoader = new ImageLoader(this);
    	albums = new ArrayList<Song>();
    	albumAdapter = new AlbumAdapter();
    	coverFlow.setAdapter(albumAdapter);
    	coverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(arg2 > currentSongId){
					nextSong();
				}else if(arg2 < currentSongId){
					lastSong();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	seekBar = (SeekBar)findViewById(R.id.main_seekbar);
    	seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				if(arg2){
					mediaPlayer.seekTo(arg1);
				}
			}
		});
    	initMediaPlayer();
    	timer = new Timer();
    	timerTask = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(mediaPlayer.isPlaying()){
					seekBar.setProgress(mediaPlayer.getCurrentPosition());
				}
			}
		};
		timer.schedule(timerTask, 0, 1000);
    }
    
    private void initMediaPlayer(){
    	mediaPlayer = new MediaPlayer();
    	mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				isCached = true;
				mediaPlayer.start();
				seekBar.setMax(mediaPlayer.getDuration());
				setAsPlay();
			}
		});
    	mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				if(currentSongId != albums.size() - 1){
					nextSong();
					coverFlow.flipToNext();
				}else{
					setAsPause();
				}
			}
		});
    	mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			
			@Override
			public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
    }
    
    private void setAsPlay(){
    	play.setImageResource(R.drawable.pause);
    }
    
    private void setAsPause(){
    	play.setImageResource(R.drawable.play);
    }
    
    private void lastSong(){
    	if(currentSongId != 0){
			currentSongId--;
			mediaPlayer.reset();
			try {
				mediaPlayer.setDataSource(albums.get(currentSongId).songUrl);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mediaPlayer.prepareAsync();
    	}
    }
    
    private void nextSong(){
    	if(currentSongId != albums.size() - 1){
			currentSongId++;
			mediaPlayer.reset();
			try {
				mediaPlayer.setDataSource(albums.get(currentSongId).songUrl);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mediaPlayer.prepareAsync();
    	}
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	
    	if(requestCode == Conf.REQUEST_CATALOG_CODE && resultCode == Conf.REQUEST_CATALOG_OK){
    		CatalogId = data.getStringExtra("id");
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void setButtonClick() {
    	titlebar.setLeftClick(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(catelog != null){
					Intent intent = new Intent(MainActivity.this, CatalogActivity.class);
					intent.putExtra("json", catelog.toString());
					startActivityForResult(intent, Conf.REQUEST_CATALOG_CODE);
				}
			}
		});
    	
    	previous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				coverFlow.flipToPrevious();
				lastSong();
			}
		});
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				coverFlow.flipToNext();
				nextSong();
			}
		});
		play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mediaPlayer.isPlaying()){
					mediaPlayer.pause();
					setAsPause();
				}else if(isCached){
					mediaPlayer.start();
					setAsPlay();
				}
			}
		});
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
    
    private void getSongs(String catalogId){
    	douban.getSongs(catalogId, new JsonHttpResponseHandler(){
    		@Override
    		public void onSuccess(JSONArray response) {
    			// TODO Auto-generated method stub
    			Song song;
    			for(int i = 0; i < response.length(); i++){
    				song = new Song();
    				try {
						song.parse(response.getJSONObject(i));
						albums.add(song);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			albumAdapter.notifyDataSetChanged();
    			currentSongId = 0;
    			try {
					mediaPlayer.setDataSource(albums.get(currentSongId).songUrl);
					mediaPlayer.prepareAsync();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
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
    
    private class AlbumAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return albums.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			final ImageView image = new ImageView(MainActivity.this);
			LayoutParams layoutParams = new LayoutParams(FuncInt.dp(MainActivity.this, 175), FuncInt.dp(MainActivity.this, 175));
			image.setLayoutParams(layoutParams);
			imageLoader.DisplayImage(albums.get(arg0).albumCoverUrl, image);
//			songName.setText(albums.get(arg0).albumName);
//			authorName.setText(albums.get(arg0).author);
			return image;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ImageView image;
			if(arg1 == null){
				image = new ImageView(MainActivity.this);
				LayoutParams layoutParams = new LayoutParams(FuncInt.dp(MainActivity.this, 175), FuncInt.dp(MainActivity.this, 175));
				image.setLayoutParams(layoutParams);
			}else{
				image = (ImageView) arg1;
			}
			imageLoader.DisplayImage(albums.get(arg0).albumCoverUrl, image);
//			songName.setText(albums.get(arg0).albumName);
//			authorName.setText(albums.get(arg0).author);
			return image;
		}
    	
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	if(mediaPlayer != null){
    		mediaPlayer.release();
    	}
    	timerTask.cancel();
    	super.onDestroy();
    }
}
