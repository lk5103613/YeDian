package com.yedianchina.ui.wallfall;



import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.yedianchina.ui.R;

public class PhotoWallFallUI extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wall_fall);
	}

}
