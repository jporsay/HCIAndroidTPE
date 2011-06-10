package com.grupo3.productConsult.services;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Element;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.grupo3.productConsult.utilities.ServerURLGenerator;
import com.grupo3.productConsult.utilities.XMLParser;

public class RefreshOrdersService extends IntentService {
	private final String TAG = getClass().getSimpleName();
	public static final int STATUS_ERROR = -1;
	public static final int STATUS_CONNECTION_ERROR = -2;
	public static final int STATUS_OK = 0;
	private TimerTask tTask;
	private String url;
	
	public RefreshOrdersService() {
		super("RefreshOrdersService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		final String userName = intent.getStringExtra("userName");
		final String authToken = intent.getStringExtra("authToken");
		final Bundle b = new Bundle();
		Timer timer = new Timer();
		this.tTask = new TimerTask() {
			public void run() {
				try {
					if(getOrderInfo(receiver, b, userName, authToken)) {
						receiver.send(STATUS_OK, b);
					} else {
						receiver.send(STATUS_ERROR, b);
					}
				} catch (SocketTimeoutException e) {
					Log.e(TAG, e.getMessage());
					receiver.send(STATUS_CONNECTION_ERROR, b);
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
					receiver.send(STATUS_ERROR, b);
				}
			};
		};
		
		timer.scheduleAtFixedRate(this.tTask, new Date(), 1000);
	}
	
	private boolean getOrderInfo(ResultReceiver receiver, Bundle b, String username, String token) throws
		ClientProtocolException, IOException, SocketTimeoutException {

		ServerURLGenerator order = new ServerURLGenerator("Order");
		order.addParameter("method", "GetOrderList");
		order.addParameter("username", username);
		order.addParameter("authentication_token", token);
		String url = order.getFullUrl();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(new HttpGet(url));
		try {
			XMLParser xp = new XMLParser(response);
			return this.parseOrderResponse(b, xp);
		} catch (Exception e) {
		}
		return false;
	}
	
	private boolean parseOrderResponse(Bundle b, XMLParser xp) {
		if (xp.getErrorMessage() != null) {
			b.putString("errorMessage", xp.getErrorMessage());
			return false;
		}
		return true;
	}
}
