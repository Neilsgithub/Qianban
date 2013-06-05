package com.yugy.qianban;

import java.util.ArrayList;
import java.util.List;

import com.boqi.widget.TagsGridView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CatalogActivity extends Activity{
	
	private TagsGridView gridView;
	
	private String[] names = {"1dfawefaewrfastfer", "2", "3", "4", "5", "6", "7", "8", "9"};
	
	private int[] images = new int[]{        
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,  
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,  
            R.drawable.ic_launcher, R.drawable.ic_launcher,R.drawable.ic_launcher  
    }; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catalog);
/*		
		Intent intent = new Intent();
		
		*/
		init();
	}
	
	private void init(){
		gridView = (TagsGridView)findViewById(R.id.private_broadcast_catalog);
		PictureAdapter adapter = new PictureAdapter(names, images, this);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() 
        { 
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
            { 
                Toast.makeText(CatalogActivity.this, "pic" + (position+1), Toast.LENGTH_SHORT).show(); 
            } 
        });
	}
	
}

//◊‘∂®“Â  ≈‰∆˜ 
class PictureAdapter extends BaseAdapter{ 
  private LayoutInflater inflater; 
  private List<Picture> pictures; 

  public PictureAdapter(String[] names, int[] images, Context context) 
  { 
      super(); 
      pictures = new ArrayList<Picture>(); 
      inflater = LayoutInflater.from(context); 
      for (int i = 0; i < images.length; i++) 
      { 
          Picture picture = new Picture(names[i], images[i]); 
          pictures.add(picture); 
      } 
  } 

  @Override
  public int getCount() 
  { 
      if (null != pictures) 
      { 
          return pictures.size(); 
      } else
      { 
          return 0; 
      } 
  } 

  @Override
  public Object getItem(int position) 
  { 
      return pictures.get(position); 
  } 

  @Override
  public long getItemId(int position) 
  { 
      return position; 
  } 

  @Override
  public View getView(int position, View convertView, ViewGroup parent) 
  { 
      ViewHolder viewHolder; 
      if (convertView == null) 
      { 
          convertView = inflater.inflate(R.layout.catalog_pic_title, null); 
          viewHolder = new ViewHolder(); 
          viewHolder.name = (TextView) convertView.findViewById(R.id.name); 
          viewHolder.image = (ImageView) convertView.findViewById(R.id.image); 
          convertView.setTag(viewHolder); 
      } else
      { 
          viewHolder = (ViewHolder) convertView.getTag(); 
      } 
      viewHolder.name.setText(pictures.get(position).getName()); 
      viewHolder.image.setImageResource(pictures.get(position).getImageId()); 
      return convertView; 
  } 

} 

class ViewHolder 
{ 
  public TextView name; 
  public ImageView image; 
} 

class Picture 
{ 
  private String name; 
  private int imageId; 

  public Picture() 
  { 
      super(); 
  } 

  public Picture(String name, int imageId) 
  { 
      super(); 
      this.name = name; 
      this.imageId = imageId; 
  } 

  public String getName() 
  { 
      return name; 
  } 

  public void setName(String name) 
  { 
      this.name = name; 
  } 

  public int getImageId() 
  { 
      return imageId; 
  } 

  public void setImageId(int imageId) 
  { 
      this.imageId = imageId; 
  } 
} 
