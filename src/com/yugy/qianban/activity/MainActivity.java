package com.yugy.qianban.activity;

import java.util.ArrayList;
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
import com.yugy.qianban.service.MusicService;
import com.yugy.qianban.widget.CoverFlow;
import com.yugy.qianban.widget.Titlebar;

import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	private Titlebar titlebar;
	private CoverFlow coverFlow;
	private ImageButton previous;
	private ImageButton play;
	private ImageButton next;
	private SeekBar seekBar;
	private TextView song;
	private TextView author;
	
//	private int currentSongId;
	
	private ArrayList<Song> albums;
	private Douban douban;
	private MusicService musicService;
	private ImageLoader imageLoader;
	private JSONObject catelog;
	private AlbumAdapter albumAdapter;
	String catalogId = "1";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getCatelog();
    }
    
    private void init(){
    	Intent intent = new Intent(this, MusicService.class);
    	bindService(intent, new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				musicService = null;
			}
			
			@Override
			public void onServiceConnected(ComponentName arg0, IBinder arg1) {
				musicService = ((MusicService.LocalBinder)arg1).getService();
				setButtonClick();
				initCoverFlow();
				musicService.setSeekbar(seekBar);
				getSongs(catalogId);
			}
		}, Context.BIND_AUTO_CREATE);
    	
    	titlebar = (Titlebar)findViewById(R.id.main_titlebar);
    	titlebar.setTitle("Qianban");
    	titlebar.setLeftButtonIcon(R.drawable.catelog_button_icon);
    	titlebar.setRightButtonIcon(R.drawable.setting_button_icon);
    	
    	coverFlow = (CoverFlow)findViewById(R.id.main_coverflow);
    	
    	previous = (ImageButton)findViewById(R.id.main_lastsong);
    	play = (ImageButton)findViewById(R.id.main_play);
    	next = (ImageButton)findViewById(R.id.main_nextsong);
    	
    	song = (TextView)findViewById(R.id.main_infosong);
    	author = (TextView)findViewById(R.id.main_infoauthor);
    	
    	douban = new Douban(this);
    	imageLoader = new ImageLoader(this);
    	albums = new ArrayList<Song>();
    	albumAdapter = new AlbumAdapter();
    	
    	seekBar = (SeekBar)findViewById(R.id.main_seekbar);
    }
    
    private void initCoverFlow(){
    	coverFlow.setAdapter(albumAdapter);
    	coverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				song.setText(albums.get(arg2).title);
				author.setText(albums.get(arg2).author + " - " + albums.get(arg2).albumName);
				if(arg2 > musicService.currentSongId){
					musicService.nextSongWithoutFlip();
				}else if(arg2 < musicService.currentSongId){
					musicService.lastSongWithoutFlip();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
    	coverFlow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2 == musicService.currentSongId){
					//Func.toast(MainActivity.this, "123");
				}
			}
		});
    	musicService.setCoverFlow(coverFlow);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	if(requestCode == Conf.REQUEST_CATALOG_CODE && resultCode == Conf.REQUEST_CATALOG_OK){
    		catalogId = data.getStringExtra("id");
    		albums = new ArrayList<Song>();
    		getSongs(catalogId);
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void setButtonClick() {
    	titlebar.setLeftClick(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
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
				musicService.playLastSong();
			}
		});
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				musicService.playNextSong();
			}
		});
		musicService.setPlayButton(play);
	}

	private void getCatelog(){
    	douban.getCatalog(new JsonHttpResponseHandler(){
        	@Override
        	public void onSuccess(JSONObject response) {
        		
        		catelog= response;
        		super.onSuccess(response);
        	}
        });
    }
    
    private void getSongs(String id){
    	musicService.resetMediaPlayer();
    	douban.getSongs(id, new JsonHttpResponseHandler(){
    		@Override
    		public void onSuccess(JSONArray response) {
    			
    			Song song;
    			for(int i = 0; i < response.length(); i++){
    				song = new Song();
    				try {
						song.parse(response.getJSONObject(i));
						albums.add(song);
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
    			}
    			albumAdapter.notifyDataSetChanged();
    			coverFlow.setSelection(0);
    			MainActivity.this.song.setText(albums.get(0).title);
				author.setText(albums.get(0).author + " - " + albums.get(0).albumName);
				musicService.setSongList(albums);
    			super.onSuccess(response);
    		}
    		
    		@Override
    		public void onFailure(Throwable error, String content) {
    			
    			super.onFailure(error, content);
    		}
    	});
    }
    
    private class AlbumAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			
			return albums.size();
		}

		@Override
		public Object getItem(int arg0) {
			
			final ImageView image = new ImageView(MainActivity.this);
			LayoutParams layoutParams = new LayoutParams(FuncInt.dp(MainActivity.this, 140), FuncInt.dp(MainActivity.this, 140));
			image.setLayoutParams(layoutParams);
			imageLoader.DisplayImage(albums.get(arg0).albumCoverUrl, image);
			return image;
		}

		@Override
		public long getItemId(int arg0) {
			
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			
			ImageView image;
			if(arg1 == null){
				image = new ImageView(MainActivity.this);
				LayoutParams layoutParams = new LayoutParams(FuncInt.dp(MainActivity.this, 140), FuncInt.dp(MainActivity.this, 140));
				image.setLayoutParams(layoutParams);
			}else{
				image = (ImageView) arg1;
			}
			imageLoader.DisplayImage(albums.get(arg0).albumCoverUrl, image);
			return image;
		}
    	
    }
}
