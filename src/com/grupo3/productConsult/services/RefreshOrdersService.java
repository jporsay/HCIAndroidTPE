package com.grupo3.productConsult.services;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.grupo3.productConsult.R;
import com.grupo3.productConsult.activities.MenuActivity;
import com.grupo3.productConsult.utilities.Order;
import com.grupo3.productConsult.utilities.PhoneUtils;
import com.grupo3.productConsult.utilities.ServerURLGenerator;
import com.grupo3.productConsult.utilities.XMLParser;

public class RefreshOrdersService extends IntentService {
	public static final int STATUS_ERROR = -1;
	public static final int STATUS_CONNECTION_ERROR = -2;
	public static final int STATUS_OK = 0;
	private static final int ZFNOTIFICATION_ID = 1;
	private static final int REFRESH_RATE_IN_SECONDS = 10;
	public boolean first;
	private TimerTask tTask;
	private static List<Order> oList;

	public RefreshOrdersService() {
		super("RefreshOrdersService");
		this.first = true;
		RefreshOrdersService.oList = new ArrayList<Order>();
	}

	public static List<Order> getOrders() {
		return RefreshOrdersService.oList;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		final String userName = intent.getStringExtra("userName");
		final String authToken = intent.getStringExtra("authToken");
		final Bundle b = new Bundle();
		Timer timer = new Timer();
		final RefreshOrdersService me = this;
		this.tTask = new TimerTask() {
			public void run() {
				try {
					if (getOrderInfo(receiver, b, userName, authToken)) {
						Integer order = null;
						if (b.containsKey("orderId")) {
							order = b.getInt("orderId");
						}
						if (!me.first) {
							me.createNotification(order);
						} else {
							me.first = false;
						}
						b.putString("updated", "yes");
					}
					receiver.send(STATUS_OK, b);
				} catch (SocketTimeoutException e) {
					receiver.send(STATUS_CONNECTION_ERROR, b);
				} catch (Exception e) {
					receiver.send(STATUS_ERROR, b);
				}
			};
		};
		this.tTask.run();
		timer.scheduleAtFixedRate(this.tTask, new Date(),
				1000 * REFRESH_RATE_IN_SECONDS);
	}

	private void createNotification(Integer order) {
		String langId = PhoneUtils.getLanguageId();
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		CharSequence tickerText;
		int icon = R.drawable.icon;
		if (langId.equals(PhoneUtils.ENGLISH)) {
			tickerText = "Orders updated";
		} else {
			tickerText = "Ordenes actualizadas";
		}
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);

		Context context = getApplicationContext();
		CharSequence contentTitle;
		CharSequence contentText;
		// TODO use correct class
		Intent notificationIntent = new Intent(this, MenuActivity.class);
		contentTitle = getString(R.string.orderModified);
		if (order != null) {
			contentText = getString(R.string.orderUpdated) + " " + order;
			notificationIntent.putExtra("orderId", order);
		} else {
			contentText = getString(R.string.orderModified);
		}
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		mNotificationManager.notify(ZFNOTIFICATION_ID, notification);
	}

	private boolean getOrderInfo(ResultReceiver receiver, Bundle b,
			String username, String token) throws ClientProtocolException,
			IOException, SocketTimeoutException {

		ServerURLGenerator order = new ServerURLGenerator("Order");
		order.addParameter("method", "GetOrderList");
		order.addParameter("username", username);
		order.addParameter("authentication_token", token);
		HttpResponse response = order.getServerResponse();
		try {
			XMLParser xp;
			xp = new XMLParser(response);
			List<Order> newOrders = this.parseOrderResponse(b, xp);
			if (newOrders == null) {
				return false;
			}
			if (this.compareWithOldOrders(newOrders, b)) {
				RefreshOrdersService.oList = newOrders;
				return true;
			}
			return false;
		} catch (Exception e) {
			b.putString("error", e.getMessage());
			receiver.send(STATUS_CONNECTION_ERROR, b);
		}
		return false;
	}

	private boolean compareWithOldOrders(List<Order> old, Bundle b) {
		if (old.size() != RefreshOrdersService.oList.size()) {
			return true;
		} else {
			for (int i = 0; i < old.size(); i++) {
				for (int j = 0; j < RefreshOrdersService.oList.size(); j++) {
					Order oi = old.get(i);
					Order oj = RefreshOrdersService.oList.get(j);
					if (oi.getId().equals(oj.getId())) {
						if (!(oi.getLatitude().equals(oj.getLatitude())
								&& oi.getLongitude().equals(oj.getLongitude()) && oi
								.getStatus().equals(oj.getStatus()))) {
							b.putInt("orderId", new Integer(oi.getId()));
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private List<Order> parseOrderResponse(Bundle b, XMLParser xp) {
		if (xp.getErrorMessage() != null) {
			Log.d("errorMessage", xp.getErrorMessage());
			b.putString("errorMessage", xp.getErrorMessage());
			return null;
		}
		NodeList orders = xp.getElements("order");
		List<Order> orderList = new ArrayList<Order>();
		for (int i = 0; i < orders.getLength(); i++) {
			this.fillOrderData(orderList, orders.item(i), xp);
		}
		return orderList;
	}

	private void fillOrderData(List<Order> oList, Node order, XMLParser xp) {
		Order o = new Order(getApplicationContext());
		o.setId(xp.getAttribute((Element) order, "id"));
		o.setLatitude(xp
				.getStringFromSingleElement("latitude", (Element) order));
		o.setLongitude(xp.getStringFromSingleElement("longitude",
				(Element) order));
		o.setStatus(xp.getStringFromSingleElement("status", (Element) order));
		oList.add(o);
	}
}
