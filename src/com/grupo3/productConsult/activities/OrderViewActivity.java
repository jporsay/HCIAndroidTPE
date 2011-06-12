package com.grupo3.productConsult.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.grupo3.productConsult.services.OrderCategoriesListService;
import com.grupo3.productConsult.utilities.Order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class OrderViewActivity extends Activity {
	private String id;
	private String userName;
	private String token;
	private Order order;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		this.userName = b.getString("userName");
		this.token = b.getString("authToken");
		this.order = (Order) b.getSerializable("order");
	}
	
	private void viewOrderItems() {
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				OrderCategoriesListService.class);
		intent.putExtra("id", order.getId());
		intent.putExtra("userName", this.userName);
		intent.putExtra("authToken", this.token);
		final Bundle b = getIntent().getExtras();
		intent.putExtra("receiver", new ResultReceiver(new Handler()) {
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				switch (resultCode) {
					case OrderCategoriesListService.STATUS_OK:
						Intent intent = new Intent(OrderViewActivity.this,
								ProductListActivity.class);
						intent.putExtras(b);
						startActivity(intent);
					break;
					
					case OrderCategoriesListService.STATUS_ERROR:
					break;
				}
			}
		});
		startService(intent);
	}
}
