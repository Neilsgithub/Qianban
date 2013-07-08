package com.yugy.qianban.asisClass;

import java.util.ArrayList;

public class LrcFormat {
	
	private ArrayList<Integer> timelist;
	private ArrayList<String> lrclist;
	private int index = -1;
	
	public LrcFormat(){
		timelist = new ArrayList<Integer>();
		lrclist = new ArrayList<String>();
	}
	
	public LrcFormat(ArrayList<Integer> t, ArrayList<String> l){
		if(t.size() == l.size()){
			timelist = t;
			lrclist = l;
			index = t.size() - 1;
		}
	}
	
	public void add(int t, String l){
		timelist.add(t);
		lrclist.add(l);
		index ++;
	}
	
	public ArrayList<Integer> getTimeList(){
		return timelist;
	}
	
	public ArrayList<String> getLrcList(){
		return lrclist;
	}
	
	public int getTime(int index){
		return timelist.get(index);
	}
	
	public String getLrc(int index){
		return lrclist.get(index);
	}
	
	public int getIndex(){
		return index;
	}
	
	public void clear(){
		timelist.clear();
		lrclist.clear();
		index = -1;
	}
	
	//待测
	public int getCurrentIndexFromTime(long time){
		int currentIndex = 0;
		if(index <= -1){
			return -1;
		}
		while(true){
			if(time >= getTime(currentIndex)){
				if(currentIndex < index){
					currentIndex ++;
				}
				else
					return currentIndex;
			}
			else if (time < getTime(currentIndex)) {
				if(currentIndex == 0){
					return -2;
				}
				else
					return currentIndex - 1;
			}
		}
	}
	
	//待测
	public long getSleeptimeFromIndex(int index){
		if(index == this.index){
			return 900;
		}
		else
			return getTime(index + 1) - getTime(index);
	}
}
