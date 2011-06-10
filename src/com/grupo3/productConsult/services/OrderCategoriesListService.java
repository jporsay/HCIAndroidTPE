package com.grupo3.productConsult.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.grupo3.productConsult.utilities.Order;

public class OrderCategoriesListService extends IntentService {
	private Map<String, String> enOrderTypes;
	private Map<String, String> esOrderTypes;
	public OrderCategoriesListService() {
		super("OrderCategoriesListService");
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		List<Order> orders = RefreshOrdersService.getOrders();
		List<String> vals = new ArrayList<String>();
		
		for(int i = 0; i < orders.size(); i++) {
			String status = orders.get(i).getStatus();
			if (!vals.contains(status)) {
				vals.add(status);
				Log.d("OCLS", status);
			}
		}
	}
	
	
}
