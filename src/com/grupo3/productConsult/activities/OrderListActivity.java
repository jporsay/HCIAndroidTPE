package com.grupo3.productConsult.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.grupo3.productConsult.R;
import com.grupo3.productConsult.services.RefreshOrdersService;
import com.grupo3.productConsult.utilities.Order;

public class OrderListActivity extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.refreshList();
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
	}
	
	@Override
	protected void onListItemClick(ListView l, View view, int position, long id) {
		int type = position + 1;
		Bundle b = getIntent().getExtras();
		String userName = b.getString("userName");
		String token = b.getString("authToken");
		Intent intent = new Intent(OrderListActivity.this, OrderListByTypeActivity.class);
		intent.putExtra("userName", userName);
		intent.putExtra("authToken", token);
		intent.putExtra("type", Integer.toString(type));
		startActivity(intent);
	}
	
	private void refreshList() {
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, this.getListItems()));
	}
	
	private boolean listContains(List<String> l, String s) {
		int oldV, newV;
		for (int i = 0; i < l.size(); i++) {
			oldV = new Integer(l.get(i));
			newV = new Integer(s);
			if (oldV == newV) {
				return true;
			}
		}
		return false;
	}
	
	private List<String> getListItems() {
		List<String> types = this.getOrderTypes();
		List<String> items = new ArrayList<String>();
		Map<String, String> strings;
		strings = this.getTypeStrings();
		
		for (int i = 0; i < types.size(); i++) {
			items.add(strings.get(types.get(i)));
		}
		
		return items;
	}
	
	private List<String> getOrderTypes() {
		List<Order> orders = RefreshOrdersService.getOrders();
		List<String> types = new ArrayList<String>();
		
		for(int i = 0; i < orders.size(); i++) {
			String status = orders.get(i).getStatus();
			if (!this.listContains(types, status)) {
				if (types.size() == 0) {
					types.add(status);
				} else {
					boolean added;
					int size = types.size();
					for (int j = 0; j < size; j++) {
						added = false;
						Integer newItem = new Integer(status);
						Integer currItem = new Integer(types.get(j));
						int comparison = newItem.compareTo(currItem);
						if (comparison < 0 && newItem != currItem) {
							added = true;
							types.add(j, status);
							break;
						} else if (j == types.size() - 1 && !added) {
							types.add(j, status);
							added = true;
						}
						
						if (added) {
							break;
						}
					}
				}
			}
		}
		
		return types;
	}
	
	private Map<String, String> getTypeStrings() {
		Map<String, String> strings = new HashMap<String, String>();
		strings.put("1", getString(R.string.created));
		strings.put("2", getString(R.string.confirmed));
		strings.put("3", getString(R.string.transported));
		strings.put("4", getString(R.string.delivered));
		return strings;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Intent intent = new Intent(this, MenuActivity.class);
			intent.putExtras(getIntent());
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
