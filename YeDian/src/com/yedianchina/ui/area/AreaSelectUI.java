package com.yedianchina.ui.area;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yedianchina.ui.R;



public class AreaSelectUI extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.area_select);
	}

	
	public void toCities(View view)
	{
		Intent intent = new Intent(this,CityUI.class);
		startActivity(intent);
	}
}
