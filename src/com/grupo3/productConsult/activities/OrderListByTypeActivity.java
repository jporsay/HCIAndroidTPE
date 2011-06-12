package com.grupo3.productConsult.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.grupo3.productConsult.R;
import com.grupo3.productConsult.services.OrderCategoriesListService;
import com.grupo3.productConsult.services.RefreshOrdersService;
import com.grupo3.productConsult.activities.GMaps;
import com.grupo3.productConsult.utilities.Order;

public class OrderListByTypeActivity extends ListActivity {
	private String type;
	private String userName;
	private String token;
	private List<Order> orders;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		this.type = b.getString("type");
		this.userName = b.getString("userName");
		this.token = b.getString("authToken");
		this.orders = new ArrayList<Order>();
		this.setViewTitle();
		this.loadOrders();
		this.setClickCallback();
	}
	
	private void setViewTitle() {
		Map<String, String> strings = new HashMap<String, String>();
		strings.put("1", getString(R.string.created));
		strings.put("2", getString(R.string.confirmed));
		strings.put("3", getString(R.string.transported));
		strings.put("4", getString(R.string.delivered));
		
		setTitle(strings.get(this.type));
	}
	
	private void setClickCallback() {
		final ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		final OrderListByTypeActivity me = this;
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				me.launchOrdersByType(position, (String)((TextView)view).getText());
			}
		});
	}
	
	private void launchOrdersByType(int position, final String title) {
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				OrderCategoriesListService.class);
		intent.putExtra("id", this.orders.get(position).getId());
		intent.putExtra("userName", this.userName);
		intent.putExtra("authToken", this.token);
		final Order order = this.orders.get(position);
		intent.putExtra("receiver", new ResultReceiver(new Handler()) {
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				switch (resultCode) {
					case OrderCategoriesListService.STATUS_OK:
						Intent intent = new Intent(OrderListByTypeActivity.this, GMaps.class);
						intent.putExtra("latitude", order.getLatitude());
						intent.putExtra("longitude", order.getLongitude());
						/*Serializable productList = resultData
						.getSerializable("products");
						Intent intent = new Intent(OrderListByTypeActivity.this,
								OrderViewActivity.class);
						Bundle b = new Bundle();
						b.putSerializable("products", productList);
						b.putSerializable("order", order);
						b.putString("breadCrumb", title + " > ");
						intent.putExtras(b);*/
						startActivity(intent);
					break;
					
					case OrderCategoriesListService.STATUS_ERROR:
					break;
				}
			}
		});
		startService(intent);
	}
	
	private void loadOrders() {
		this.fillOrders();
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, this.getItemStringList()));
	}
	
	private List<String> getItemStringList() {
		List<String> strings = new ArrayList<String>();
		
		for( Order o : this.orders) {
			strings.add(getString(R.string.orderTitle) + " " + o.getId());
		}
		
		return strings;
	}
	
	private void fillOrders() {
		List<Order> orders = RefreshOrdersService.getOrders();
		for (int i = 0; i < orders.size(); i++) {
			String o = orders.get(i).getStatus();
			if (o.equals(this.type)) {
				this.orders.add(orders.get(i));
			}
		}
	}
}
