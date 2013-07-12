package com.yugy.qianban.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fedorvlasov.lazylist.ImageLoader;
import com.yugy.qianban.R;
import com.yugy.qianban.asisClass.Conf;
import com.yugy.qianban.database.Account;
import com.yugy.qianban.database.DatabaseManager;
import com.yugy.qianban.widget.TagsGridView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CatalogActivity extends Activity {

	private String[] hot_names;
	private ImageLoader imageLoader;
	private String[] hot_Picture_Uri;
	private DatabaseManager databaseManager;

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

		databaseManager = new DatabaseManager(this);
		Account account = databaseManager.getAccount();
		if (account == null) {// 没登陆
			unLogging();
		} else {
			logging();
		}
	}

	private void unLogging() {
		String[] titles = { "登陆" };
		int[] images = { R.drawable.logging };
		TagsGridView gridView = (TagsGridView) findViewById(R.id.private_broadcast_catalog);
		PersonalPictureAdapter adapter = new PersonalPictureAdapter(titles,
				images, this);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CatalogActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
	}

	private void logging() {
		String[] titles = { "私人Mhz", "红心Mhz" };
		int[] images = { R.drawable.personal, R.drawable.like };
		TagsGridView gridView = (TagsGridView) findViewById(R.id.private_broadcast_catalog);
		PersonalPictureAdapter adapter = new PersonalPictureAdapter(titles,
				images, this);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) // position是要选中在图标
			{
				Intent intent = new Intent();
				if(position == 0){
					intent.putExtra("id", "0");
				}else if(position == 1){
					intent.putExtra("id", "-3");
				}
				setResult(Conf.REQUEST_CATALOG_OK, intent);
				finish();
			}
		});
	}

	private void setCatalog(JSONObject catalog) {
		imageLoader = new ImageLoader(this, "qianban/cache", getResources()
				.getDrawable(R.drawable.stub));
		JSONArray hot_channels = new JSONArray();
		try {
			hot_channels = catalog.getJSONArray("hot_channels");
			hot_names = new String[hot_channels.length()];
			hot_Picture_Uri = new String[hot_channels.length()];
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < hot_channels.length(); i++) {
			try {
				hot_names[i] = hot_channels.getJSONObject(i).getString("name")
						+ "MHz";
				hot_Picture_Uri[i] = hot_channels.getJSONObject(i).getString(
						"cover");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		init(hot_names, hot_Picture_Uri, R.id.hot_module_catalog, hot_channels);
	}

	private void init(String[] names, String[] Uri, int gridId,
			final JSONArray module) {
		TagsGridView gridView = (TagsGridView) findViewById(gridId);
		PictureAdapter adapter = new PictureAdapter(names, Uri, this,
				imageLoader);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) // position是要选中在图标
			{
				// Toast.makeText(CatalogActivity.this, "pic" + (position+1),
				// Toast.LENGTH_SHORT).show();
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

	public PictureAdapter(String[] names, String[] Uri, Context context,
			ImageLoader imageLoader) {
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
		imageLoader.DisplayImage(pictures.get(position).getUri(),
				viewHolder.image);
		return convertView;
	}

}

// 自定义适配器
class PersonalPictureAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<PersonalPicture> pictures;

	public PersonalPictureAdapter(String[] titles, int[] images, Context context) {
		super();
		pictures = new ArrayList<PersonalPicture>();
		inflater = LayoutInflater.from(context);
		for (int i = 0; i < images.length; i++) {
			PersonalPicture picture = new PersonalPicture(titles[i], images[i]);
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
			viewHolder.image.setBackgroundColor(Color.LTGRAY);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.name.setText(pictures.get(position).getTitle());
		viewHolder.image.setImageResource(pictures.get(position).getImageId());
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

class PersonalPicture {
	private String title;
	private int imageId;

	public PersonalPicture() {
		super();
	}

	public PersonalPicture(String title, int imageId) {
		super();
		this.title = title;
		this.imageId = imageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
}
