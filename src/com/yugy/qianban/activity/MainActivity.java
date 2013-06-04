package com.yugy.qianban.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.fedorvlasov.lazylist.ImageLoader;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yugy.qianban.R;
import com.yugy.qianban.asisClass.FuncInt;
import com.yugy.qianban.asisClass.Song;
import com.yugy.qianban.sdk.Douban;
import com.yugy.qianban.widget.CoverFlow;
import com.yugy.qianban.widget.Titlebar;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.app.Activity;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	private Titlebar titlebar;
	private CoverFlow coverFlow;
	private ImageButton previous;
	private ImageButton play;
	private ImageButton next;
	
	private String currentSongUrl;
	
	private ArrayList<Song> albums;
	private Douban douban;
	private ImageLoader imageLoader;
	private JSONObject catelog;
	private AlbumAdapter albumAdapter;
	
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

    }
    
    private void setButtonClick() {
    	previous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				coverFlow.flipToPrevious();
			}
		});
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				coverFlow.flipToNext();
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
			ImageView image = new ImageView(MainActivity.this);
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
}
