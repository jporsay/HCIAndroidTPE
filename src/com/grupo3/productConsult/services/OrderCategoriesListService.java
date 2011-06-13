package com.grupo3.productConsult.services;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.w3c.dom.NodeList;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.grupo3.productConsult.Product;
import com.grupo3.productConsult.utilities.ServerURLGenerator;
import com.grupo3.productConsult.utilities.XMLParser;

public class OrderCategoriesListService extends IntentService {

	public static final int STATUS_ERROR = -1;
	public static final int STATUS_CONNECTION_ERROR = -2;
	public static final int STATUS_OK = 0;

	public OrderCategoriesListService() {
		super("OrderCategoriesListService");
	}

	@Override
	protected void onHandleIntent(Intent i) {
		ResultReceiver receiver = i.getParcelableExtra("receiver");
		String userName = i.getExtras().getString("userName");
		String authToken = i.getExtras().getString("authToken");
		String orderId = i.getExtras().getString("id");

		ServerURLGenerator order = new ServerURLGenerator("Order");
		order.addParameter("method", "GetOrder");
		order.addParameter("authentication_token", authToken);
		order.addParameter("username", userName);
		order.addParameter("order_id", orderId);
		Bundle b = new Bundle();
		try {
			HttpResponse response;
			response = order.getServerResponse();
			XMLParser xp = new XMLParser(response);
			if (xp.getErrorMessage() != null) {
			} else {
				NodeList items = xp.getElements("product_id");
				ArrayList<Product> products = new ArrayList<Product>();
				for (int j = 0; j < items.getLength(); j++) {
					products.add(CategoriesSearchService
							.fetchProduct(new Integer(items.item(j)
									.getFirstChild().getNodeValue())));
				}
				b.putSerializable("products", products);
				receiver.send(STATUS_OK, b);
			}
		} catch (Exception e) {
			b.putString("error", e.getMessage());
			receiver.send(STATUS_ERROR, b);
		}
	}

}
