package com.yugy.qianban.asisClass;

import org.json.JSONException;
import org.json.JSONObject;

public class Song {

	public String albumCoverUrl;
	public String songUrl;
	public String author;
	public String albumName;
	public String title;
	
	public void parse(JSONObject json){
		try {
			albumCoverUrl = json.getString("picture");
			if(albumCoverUrl.contains("mpic")){
				albumCoverUrl = albumCoverUrl.replace("mpic", "lpic");
			}
			songUrl = json.getString("url");
			author = json.getString("artist");
			albumName = json.getString("albumtitle");
			title = json.getString("title");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
