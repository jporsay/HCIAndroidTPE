package com.grupo3.productConsult.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Toast;

import com.grupo3.productConsult.R;
import com.grupo3.productConsult.services.LoginService;
import com.grupo3.productConsult.services.OrderCategoriesListService;
import com.grupo3.productConsult.services.RefreshOrdersService;

public class MenuActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.startOrderRefreshService(getIntent().getExtras());
		setContentView(R.layout.menu);
	}
	
	public void doLogOut(View button) {
		
	}
	
	public void startOrderRefreshService(Bundle b) {
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this, RefreshOrdersService.class);
		intent.putExtra("userName", b.getString("userName"));
		intent.putExtra("authToken", b.getString("authToken"));
		intent.putExtra("receiver", new ResultReceiver(new Handler()) {
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				switch (resultCode) {
				case RefreshOrdersService.STATUS_OK:
					break;
					
				case RefreshOrdersService.STATUS_CONNECTION_ERROR:
					break;
					
				case RefreshOrdersService.STATUS_ERROR:
					if (resultData.containsKey("errorMessage")) {
						String errorMessage = resultData.getString("errorMessage");
						Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
					}
					break;
					
				default:
					break;
				}
			}
		});
		startService(intent);
	}
	
	public void doViewCategories(View button) {
		Intent intent = new Intent(MenuActivity.this, CategoryListActivity.class);
		startActivity(intent);
	}
	
	public void doViewOrders(View button) {
		Intent intent = new Intent(MenuActivity.this, OrderCategoriesListService.class);
		startService(intent);
	}
}
