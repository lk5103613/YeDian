package com.yedianchina.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yedianchina.po.City;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.tools.DBHelper;
import com.yedianchina.ui.MyLetterListView.OnTouchingLetterChangedListener;


public class CitySelectUI extends Activity {
	private BaseAdapter adapter;
	private ListView personList;
	private TextView overlay; // 对话框首字母textview
	private MyLetterListView letterListView; // A-Z listview
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private String[] sections;// 存放存在的汉语拼音首字母
	private Handler handler;
	private OverlayThread overlayThread; // 显示首字母对话框
	private OverlayCityThread overlayCityThread;
	
	private ArrayList<City> allCity_lists; // 所有城市列表
	private ArrayList<City> city_lists;// 城市列表
	ListAdapter.TopViewHolder topViewHolder;
	private String lngCityName = "当前城市:上海";
	
	TextView lng_city;
	
	Handler locCityHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
		 
			if (what == 1) {
				//lng_city.setText("上海");
			}
		}
	};
	///////////////////////////////////////////////
	private class OverlayCityThread implements Runnable {
		@Override
		public void run() {
			locCityHandler.sendEmptyMessage(1);
		}

	}
	
	
	
	
	
	
	
	
	//////////////////////////////////////////////

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 
		
		setContentView(R.layout.main);
		
		TextView NavigateTitle=(TextView)this.findViewById(R.id.NavigateTitle);
		if(NavigateTitle!=null){
			NavigateTitle.setText("城市切换");
		}
		//
		
		TextView qiandaoBtn=(TextView)this.findViewById(R.id.qiandaoBtn);
		if(qiandaoBtn!=null){
			qiandaoBtn.setVisibility(View.INVISIBLE);
		}
		
		
		lng_city=(TextView)this.findViewById(R.id.lng_city);
		
		
		personList = (ListView) findViewById(R.id.list_view);
		allCity_lists = new ArrayList<City>();
		letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
		letterListView
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
		//MainUI.setLocateIn(new GetCityName());
		alphaIndexer = new HashMap<String, Integer>();
		handler = new Handler();
		overlayThread = new OverlayThread();
		personList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			}
		});
		personList.setAdapter(adapter);
		initOverlay();
		hotCityInit();
		setAdapter(allCity_lists);
		////
		overlayCityThread = new OverlayCityThread();
		Handler handler = new Handler();
		handler.postDelayed(overlayCityThread, 3000);
		//
		ImageView backBtn=(ImageView)this.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				CitySelectUI.this.finish();
				
			}
		});
		
	}
	
	

	/**
	 * 热门城市
	 */
	public void hotCityInit() {
		City city = new City("", "-");   
		allCity_lists.add(city);
		city = new City("", "-");
		allCity_lists.add(city);
		city = new City("上海", "");
		allCity_lists.add(city);
		city = new City("北京", "");
		allCity_lists.add(city);
		city = new City("广州", "");
		allCity_lists.add(city);
		city = new City("深圳", "");
		allCity_lists.add(city);
		city = new City("武汉", "");
		allCity_lists.add(city);
		city = new City("天津", "");
		allCity_lists.add(city);
		city = new City("西安", "");
		allCity_lists.add(city);
		city = new City("南京", "");
		allCity_lists.add(city);
		city = new City("杭州", "");
		allCity_lists.add(city);
		city = new City("成都", "");
		allCity_lists.add(city);
		city = new City("重庆", "");
		allCity_lists.add(city);
		city_lists = getCityList();
		allCity_lists.addAll(city_lists);
	}

	private ArrayList<City> getCityList() {
		DBHelper dbHelper = new DBHelper(this);
		ArrayList<City> list = new ArrayList<City>();
		try {
			dbHelper.createDataBase();
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery("select * from city", null);
			City city;
			while (cursor.moveToNext()) {
				city = new City(cursor.getString(1), cursor.getString(2));
				list.add(city);
			}
			cursor.close();
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Collections.sort(list, comparator);
		return list;
	}

	/**
	 * a-z排序
	 */
	Comparator comparator = new Comparator<City>() {
		@Override
		public int compare(City lhs, City rhs) {
			String a = lhs.getPinyi().substring(0, 1);
			String b = rhs.getPinyi().substring(0, 1);
			int flag = a.compareTo(b);
			if (flag == 0) {
				return a.compareTo(b);
			} else {
				return flag;
			}

		}
	};

	private void setAdapter(List<City> list) {
		adapter = new ListAdapter(this, list);
		personList.setAdapter(adapter);
	}

	public class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<City> list;
		final int VIEW_TYPE = 3;

		public ListAdapter(Context context, List<City> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				// 当前汉语拼音首字母
				String currentStr = getAlpha(list.get(i).getPinyi());
				// 上一个汉语拼音首字母，如果不存在为“ ”
				String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
						.getPinyi()) : " ";
				if (!previewStr.equals(currentStr)) {
					String name = getAlpha(list.get(i).getPinyi());
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			int type = 0;
			if (position == 0) {
				type = 2;
			} else if (position == 1) {
				type = 1;
			}
			return type;
		}

		@Override
		public int getViewTypeCount() {// 这里需要返回需要集中布局类型，总大小为类型的种数的下标
			return VIEW_TYPE;
		}
		
	

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			int viewType = getItemViewType(position);
			if (viewType == 1) {
				if (convertView == null) {
					topViewHolder = new TopViewHolder();
					convertView = inflater.inflate(R.layout.frist_list_item,
							null);
					topViewHolder.alpha = (TextView) convertView
							.findViewById(R.id.alpha);
					topViewHolder.name = (TextView) convertView
							.findViewById(R.id.lng_city);
					convertView.setTag(topViewHolder);
				} else {
					topViewHolder = (TopViewHolder) convertView.getTag();
				}

				topViewHolder.name.setText(lngCityName);
				topViewHolder.alpha.setVisibility(View.VISIBLE);
				topViewHolder.alpha.setText("定位城市");

			} else if (viewType == 2) {
				final ShViewHolder shViewHolder;
				if (convertView == null) {
					shViewHolder = new ShViewHolder();
					convertView = inflater.inflate(R.layout.search_item, null);
					shViewHolder.editText = (EditText) convertView
							.findViewById(R.id.sh);
					convertView.setTag(shViewHolder);
				} else {
					shViewHolder = (ShViewHolder) convertView.getTag();
				}
			} else {
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.list_item_city, null);
					holder = new ViewHolder();
					holder.alpha = (TextView) convertView
							.findViewById(R.id.alpha);
					holder.name = (TextView) convertView
							.findViewById(R.id.name);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				if (position >= 1) {
					
					holder.name.setText(list.get(position).getName());
					String currentStr = getAlpha(list.get(position).getPinyi());
					final String finalCityName=list.get(position).getName();
					String previewStr = (position - 1) >= 0 ? getAlpha(list
							.get(position - 1).getPinyi()) : " ";
					if (!previewStr.equals(currentStr)) {
						holder.alpha.setVisibility(View.VISIBLE);
						if (currentStr.equals("#")) {
							currentStr = "热门城市";
						}
						holder.alpha.setText(currentStr);
					} else {
						holder.alpha.setVisibility(View.GONE);
					}
					
					convertView.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Log.e("当前城市", "currentStr="+finalCityName);
							
							 
								
								 Intent intent = new Intent(action);  
					                intent.putExtra("selectCity", finalCityName);  
					                intent.putExtra("selectCityFn", "1");  
					                sendBroadcast(intent);  
							
							 
							
							CitySelectUI.this.finish();
							
						}
					});
				 
					
					
					
				}
			}
			
			return convertView;
		}

		private class ViewHolder {
			TextView alpha; // 首字母标题
			TextView name; // 城市名字
		}

		private class TopViewHolder {
			TextView alpha; // 首字母标题
			TextView name; // 城市名字
		}

		private class ShViewHolder {
			EditText editText;

		}
	}

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				personList.setSelection(position);
				overlay.setText(sections[position]);
				
				 
				 
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1500);
			}
		}

	}

	// 设置overlay不可见
	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}

	// 获得汉语拼音首字母
	private String getAlpha(String str) {

		if (str.equals("-")) {
			return "&";
		}
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}

	private class GetCityName implements LocateIn {
		@Override
		public void getCityName(String name) {
			System.out.println(name);
			if (topViewHolder.name != null) {
				lngCityName = name;
				adapter.notifyDataSetChanged();
			}
		}
	}
	static LocateIn tin;

	public static void setLocateIn(LocateIn in) {
		tin = in;
	}

	public interface LocateIn {
		public void getCityName(String name);
	}
	
	public static final String action = "cityselectui.broadcast.action";  

}