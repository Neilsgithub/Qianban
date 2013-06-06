package com.yugy.qianban.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.boqi.qianban.widget.TagsGridView;
import com.fedorvlasov.lazylist.ImageLoader;
import com.yugy.qianban.R;
import com.yugy.qianban.R.drawable;
import com.yugy.qianban.R.id;
import com.yugy.qianban.R.layout;
import com.yugy.qianban.asisClass.Conf;

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
	
	private String[] hot_names;
	private String[] fast_names;
	private String[] com_names;
	private ImageLoader imageLoader;
	private String[] hot_Picture_Uri;
	private String[] fast_Picture_Uri;
	private String[] com_Picture_Uri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catalog);
		
		Intent intent = getIntent();
		String catalogString = intent.getStringExtra("json");
		JSONObject catalogObject = null;
		try {
			catalogObject = new JSONObject(catalogString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setCatalog(catalogObject);
	}
	
	private void setCatalog(JSONObject catalog){
		imageLoader = new ImageLoader(this);
		JSONArray hot_channels = new JSONArray();
		JSONArray fast_channels = new JSONArray();
		JSONArray com_channels = new JSONArray();
		try {
			hot_channels = catalog.getJSONArray("hot_channels");
			fast_channels = catalog.getJSONArray("fast_channels");
			com_channels = catalog.getJSONArray("com_channels");
			hot_names = new String[hot_channels.length()];
			fast_names = new String[fast_channels.length()];
			com_names = new String[com_channels.length()];
			hot_Picture_Uri = new String[hot_channels.length()];
			fast_Picture_Uri = new String[fast_channels.length()];
			com_Picture_Uri = new String[com_channels.length()];
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < hot_channels.length(); i ++){
			try {
				hot_names[i] = hot_channels.getJSONObject(i).getString("name") + "MHz";
				hot_Picture_Uri[i] = hot_channels.getJSONObject(i).getString("cover");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i = 0; i < fast_channels.length(); i ++){
			try {
				fast_names[i] = fast_channels.getJSONObject(i).getString("name") + "MHz";
				fast_Picture_Uri[i] = fast_channels.getJSONObject(i).getString("cover");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i = 0; i < com_channels.length(); i ++){
			try {
				com_names[i] = com_channels.getJSONObject(i).getString("name") + "MHz";
				com_Picture_Uri[i] = com_channels.getJSONObject(i).getString("cover");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		init(hot_names, hot_Picture_Uri, R.id.hot_module_catalog, hot_channels);
		init(fast_names, fast_Picture_Uri, R.id.fast_module_catalog, fast_channels);
		init(com_names, com_Picture_Uri, R.id.com_module_catalog, com_channels);
	}
	
	private void init(String[] names, String[] Uri, int gridId, final JSONArray module){
		TagsGridView gridView = (TagsGridView)findViewById(gridId);
		PictureAdapter adapter = new PictureAdapter(names, Uri, this, imageLoader);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() 
        { 
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) //position是要选中在图标
            { 
                //Toast.makeText(CatalogActivity.this, "pic" + (position+1), Toast.LENGTH_SHORT).show(); 
            	String Mhz_Id = null;
            	try {
					Mhz_Id = module.getJSONObject(position).getString("id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	Intent intent = new Intent();
            	intent.putExtra("id", Mhz_Id);
            	setResult(Conf.REQUEST_CATALOG_OK, intent);
            	finish();
            } 
        });
	}
	
}

// �Զ���������
class PictureAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Picture> pictures;
	private ImageLoader imageLoader;

	public PictureAdapter(String[] names, String[] Uri, Context context, ImageLoader imageLoader) {
		super();
		this.imageLoader = imageLoader;
		pictures = new ArrayList<Picture>();
		inflater = LayoutInflater.from(context);
		for (int i = 0; i < names.length; i++) {
			Picture picture = new Picture(names[i], Uri[i]);
			pictures.add(picture);
		}
	}

	@Override
	public int getCount() {
		if (null != pictures) {
			return pictures.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return pictures.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.catalog_pic_title, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.name.setText(pictures.get(position).getName());
		imageLoader.DisplayImage(pictures.get(position).getUri(), viewHolder.image);
		return convertView;
	}

}

class ViewHolder {
	public TextView name;
	public ImageView image;
}

class Picture {
	private String name;
	private String Uri;

	public Picture() {
		super();
	}

	public Picture(String name, String Uri) {
		super();
		this.name = name;
		this.Uri = Uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return Uri;
	}

	public void setUri(String Uri) {
		this.Uri = Uri;
	}
}
