package com.yedianchina.tools;

import android.content.Context;  
import android.graphics.Color;  
import android.util.TypedValue;  
import android.view.Gravity;  
import android.widget.LinearLayout;  
import android.widget.TextView;  
import android.widget.Toast;  
import android.widget.LinearLayout.LayoutParams;  

  
/** 
 * 弹出Toast的一个工具类，这里主要是增加了对系统Toast背景的修改 
 * @author Administrator 
 * 
 */  
public class MsgTools {  
      
    /** 
     *  
     * @param context 上下文对象 
     * @param msg 要显示的信息 
     * @param timeTag 时间参数 若是“s”表示短时间显示  
     *                           若是“l”（小写L）表示长时间显示 
     */  
    public static void toast(Context context, String msg, String timeTag){  
        int time = Toast.LENGTH_SHORT;  
        if(timeTag == null || "l".equals(timeTag)){  
            time = Toast.LENGTH_LONG;  
        }  
          
        Toast toast = Toast.makeText(context, null, time);  
        LinearLayout layout = (LinearLayout)toast.getView();   
        /*layout.setLayoutParams(new WindowManager.LayoutParams(10000, 
                android.view.WindowManager.LayoutParams.WRAP_CONTENT,  
                WindowManager.LayoutParams.TYPE_TOAST, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE 
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE 
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,  
                PixelFormat.TRANSLUCENT));*/  
        //layout.setBackgroundResource(R.drawable.bg_msg_toast);  
        layout.setOrientation(LinearLayout.HORIZONTAL);  
        layout.setGravity(Gravity.CENTER_VERTICAL);  
        TextView tv = new TextView(context);  
        tv.setLayoutParams(new  LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));     
        tv.setGravity(Gravity.CENTER_VERTICAL);  
        tv.setTextColor(Color.parseColor("#ffffffff"));  
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);  
        tv.setPadding(0, 0, 0, 0);  
        tv.setText(msg);  
        layout.addView(tv);  
        toast.show();  
    }  
}  