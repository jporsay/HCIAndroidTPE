package com.grupo3.productConsult.services;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.grupo3.productConsult.utilities.ServerURLGenerator;
import com.grupo3.productConsult.utilities.XMLParser;

public class LoginService extends IntentService {

	public static final String DO_LOGIN = "doLogin";
	
	public static final int STATUS_ERROR = -1;
	public static final int STATUS_CONNECTION_ERROR = -2;
	public static final int STATUS_OK = 0;
	private final String TAG = getClass().getSimpleName();
	
	public LoginService() {
		super("LoginService");
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ResultReceiver receiver = intent.getParcelableExtra("receiver");
		String command = intent.getStringExtra("command");
		String user = intent.getStringExtra("username");
		String password = intent.getStringExtra("password");
		Bundle b = new Bundle();
		try {
			if (command.equals(DO_LOGIN)) {
				if(doLogin(receiver, b, user, password)) {
					receiver.send(STATUS_OK, b);
				} else {
					receiver.send(STATUS_ERROR, b);
				}
			}
		} catch (SocketTimeoutException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_CONNECTION_ERROR, b);
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_ERROR, b);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_ERROR, b);
		}
	}
	
	private boolean doLogin(ResultReceiver r, Bundle b, String userName, String password)
		throws IOException, ClientProtocolException {
		
		ServerURLGenerator security = new ServerURLGenerator("Security");
		security.addParameter("method", "SignIn");
		security.addParameter("username", userName);
		security.addParameter("password", password);
		String url = security.getFullUrl();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(new HttpGet(url));
		try {
			XMLParser xp = new XMLParser(response);
			return this.checkLogin(b, xp);
		} catch (Exception e) {
		}
		return false;
		
	}
	
	private boolean checkLogin(Bundle b, XMLParser xp) {
		if (xp.getErrorMessage() != null) {
			b.putString("errorMessage", xp.getErrorMessage());
			return false;
		}
		b.putString("authToken", xp.getStringFromSingleElement("token"));
		return true;
	}
}
