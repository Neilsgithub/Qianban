package com.yugy.qianban.database;

import org.json.JSONException;
import org.json.JSONObject;

public class Account {
	public String ck;
	public String name;
	public int id;
	public int liked;
	public int banned;
	public int played;
	
	public Account(){
		
	}
	
	public Account parse(JSONObject json){
		try {
			JSONObject user = json.getJSONObject("user_info");
			ck = user.getString("ck");
			name = user.getString("name");
			id = user.getInt("id");
			JSONObject record = user.getJSONObject("play_record");
			liked = record.getInt("liked");
			banned = record.getInt("banned");
			played = record.getInt("played");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
}
