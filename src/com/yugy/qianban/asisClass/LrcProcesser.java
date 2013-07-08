package com.yugy.qianban.asisClass;

import android.annotation.SuppressLint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("UseSparseArrays")
public class LrcProcesser {

	public LrcFormat process (InputStream inputStream){
		String currentLine;
		LrcFormat lrcList = new LrcFormat();
		HashMap<Integer, String> lrcMess = new HashMap<Integer, String>();
		InputStreamReader inputReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputReader);
		Pattern lrcPattern = Pattern.compile("\\[([0-9][^\\]]+)\\]");
		String lrcStr = null;
		String timeStr = null;
		int time2ms[] = new int[100];
		int count = 0;
		try {
			while ((currentLine = bufferedReader.readLine()) != null){
				Matcher m = lrcPattern.matcher(currentLine);                   //匹配当前行
				lrcStr = currentLine.replaceAll("\\[([0-9][^\\]]+)\\]", "");   //将当前行中的所有时间格式替换成""
				while (m.find()){
					timeStr = m.group();
					time2ms[count] = time2ms(timeStr.substring(1, timeStr.length() - 1));
					lrcMess.put(time2ms[count], lrcStr);
					count ++;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sort(time2ms);
		for(int i = 0; i < count; i ++){
			//lrcList.add(time2ms[i], new String(lrcMess.get(time2ms[i]).getBytes("GBK"), "unicode"));
			lrcList.add(time2ms[i], lrcMess.get(time2ms[i]));
		}
		return lrcList;
	}
	
	public void sort(int[] a){
		int all = 0;
		for(int i = 0; i < a.length; i ++){
			if(a[i] != 0 || i == 0){
				all ++;
			}
		}
		for (int i = 1; i < all; i++)    
        {    
            int t = a[i];    
            int j = i;    
            while ((j > 0) && (a[j - 1] > t))    
            {    
                a[j] = a[j - 1];//交换顺序    
                --j;    
            }    
            a[j] = t;    
        }
	}

	public int time2ms(String timeStr) {
		String s[] = timeStr.split(":");
		int min = Integer.parseInt(s[0]);
		int sec = 0;
		int mill = 0;
		//LRC文件支持三种不同的时间格式
		//mm:ss.ms
		//mm:ss:ms
		//mm:ss
		
		//如果格式为mm:ss:ms
		if(s.length > 2){
			sec = Integer.parseInt(s[1]);
			mill = Integer.parseInt(s[2]);
		}
		else{
			String ss[] = s[1].split("\\.");
			//如果格式为mm:ss.ms
			if(ss.length > 1){
				sec = Integer.parseInt(ss[0]);
				mill = Integer.parseInt(ss[1]);
			}
			//如果格式为mm:ss
			else{
				sec = Integer.parseInt(ss[0]);
				mill = 0;
			}
		}
		return min * 60 * 1000 + sec * 1000 + mill * 10;
	}
}
