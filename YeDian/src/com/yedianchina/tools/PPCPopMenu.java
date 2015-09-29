package com.yedianchina.tools;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yedianchina.po.Area;
import com.yedianchina.ui.R;


public class PPCPopMenu {
	private ArrayList<String> itemList;
    private Context context;
    private PopupWindow popupWindow ;
    private ListView listView;
    
    public PPCPopMenu(Context context) {
            this.context = context;
            View view = LayoutInflater.from(context).inflate(R.layout.ppc_popmenu, null);
            itemList = new ArrayList<String>();
   
    listView = (ListView)view.findViewById(R.id.listView);
    
    listView.setAdapter(new PopAdapter());
    popupWindow = new PopupWindow(view, 200, LayoutParams.WRAP_CONTENT);
    popupWindow = new PopupWindow(view, context.getResources().getDimensionPixelSize(R.dimen.ppcpopmenu_width), LayoutParams.WRAP_CONTENT);
    popupWindow.setBackgroundDrawable(new BitmapDrawable());
    
    }
    
    
   public void setOnItemClickListener(android.widget.AdapterView.OnItemClickListener listener) {
            listView.setOnItemClickListener(listener);
    }

   
   public void addItems(List<Area> items) {
            for (Area s : items)
                    itemList.add(s.getAreaName());
    }

    //������Ӳ˵���
   public void addItem(String item) {
            itemList.add(item);
    }
    
    public Object getItem(int position) {
            return itemList.get(position);
    }

   
    public void showAsDropDown(View parent) {
           
           popupWindow.showAsDropDown(parent,5,0);
           
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
