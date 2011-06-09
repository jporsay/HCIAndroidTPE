package com.grupo3.productConsult.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.grupo3.productConsult.R;

public class MenuActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
	}
	
	public void doLogOut(View button) {
		
	}
	
	public void doViewCategories(View button) {
		Intent intent = new Intent(MenuActivity.this, CategoryListActivity.class);
		startActivity(intent);
	}
	
	public void doViewOrders(View button) {
	}
}
