package com.yedianchina.tools;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yedianchina.po.JobsPO;
import com.yedianchina.ui.R;

//职位下拉框
public class JobsPopMenu {
	private ArrayList<String> itemList;
    private Context context;
    private PopupWindow popupWindow ;
    private ListView listView;
    int offset;
    
    public JobsPopMenu(Context context,int _offset,int h) {
            this.context = context;
            View view = LayoutInflater.from(context).inflate(R.layout.ppc_popmenu, null);
            itemList = new ArrayList<String>();
            this.offset=_offset;
   
    listView = (ListView)view.findViewById(R.id.listView);
    
    listView.setAdapter(new PopAdapter());
//    popupWindow = new PopupWindow(view, _offset,
//			ViewGroup.LayoutParams.WRAP_CONTENT);
    popupWindow = new PopupWindow(view, context.getResources().getDimensionPixelSize(R.dimen.ppcpopmenu_width),LinearLayout.LayoutParams.WRAP_CONTENT);
    popupWindow.setBackgroundDrawable(new BitmapDrawable());
    
    }
    
    
   public void setOnItemClickListener(android.widget.AdapterView.OnItemClickListener listener) {
            listView.setOnItemClickListener(listener);
    }

   
   public void addItems(List<JobsPO> items) {
            for (JobsPO s : items)
                    itemList.add(s.getName());
    }

   
   public void addItem(String item) {
            itemList.add(item);
    }
    
    public Object getItem(int position) {
            return itemList.get(position);
    }

   
    public void showAsDropDown(View parent,View v,int yOffSet) {
           
           //popupWindow.showAsDropDown(parent,5,135);
    	//offset 是弹出下拉菜单对于X偏移
    	//popupWindow.showAsDropDown(parent,this.offset,135);
    	/////////////
    	int[] location = new int[2];  
        v.getLocationOnScreen(location);  
        //int h=parent.getHeight();
        //popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0], location[1]-popupWindow.getHeight()+h);  
        //popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0], location[1]+h+yOffSet);  
        popupWindow.showAsDropDown(parent,this.offset,yOffSet);
    	////////////////
           
   popupWindow.setFocusable(true);
    
   popupWindow.setOutsideTouchable(true);
    //ˢ��״̬
   popupWindow.update();
   
   
   
   
   
   
   
   
    }
    
    //���ز˵�
   public void dismiss() {
            popupWindow.dismiss();
    }

    // ������
   private final class PopAdapter extends BaseAdapter {

            public int getCount() {
                    return itemList.size();
            }
            
            public Object getItem(int position) {
                    return itemList.get(position);
            }
            
            public long getItemId(int position) {
                    return position;
            }
            
            public View getView(int position, View convertView, ViewGroup parent) {
                    
                    convertView = LayoutInflater.from(context).inflate(R.layout.ppc_pomenu_item, null);
                    TextView groupItem = (TextView) convertView.findViewById(R.id.textViews);
                    TextView imgTV = (TextView) convertView.findViewById(R.id.imgTV);
                    if (position == itemList.size() - 1) {
                            imgTV.setVisibility(View.GONE);
                    }
                    groupItem.setText(itemList.get(position));
                    return convertView;
            }
    }
}

