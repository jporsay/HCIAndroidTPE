package com.grupo3.productConsult.services;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

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
		try {
			HttpResponse response;
			response = order.getServerResponse();
			XMLParser xp = new XMLParser(response);
			if (xp.getErrorMessage() != null) {
			} else {
				NodeList items = xp.getElements("product_id");
				ArrayList<Product> products = new ArrayList<Product>();
				for (int j = 0; j < items.getLength(); j++) {
					Log.d("id", items.item(j).getFirstChild().getNodeValue());
					products.add(CategoriesSearchService.fetchProduct(new Integer(items.item(j).getFirstChild().getNodeValue())));
				}
				Bundle b = new Bundle();
				b.putSerializable("products", products);
				receiver.send(STATUS_OK, b);
			}
		} catch (ClientProtocolException e) {
			Log.e("error", "ClientProtocolException");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("error", "IOException");
			e.printStackTrace();
		} catch (ParseException e) {
			Log.e("error", "ParseException");
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			Log.e("error", "ParserConfigurationException");
			e.printStackTrace();
		} catch (SAXException e) {
			Log.e("error", "SAXException");
			e.printStackTrace();
		}
	}
	
	
}
