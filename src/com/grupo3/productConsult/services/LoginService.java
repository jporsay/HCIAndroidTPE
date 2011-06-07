package com.grupo3.productConsult.services;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.grupo3.productConsult.utilities.ServerURLGenerator;

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
	protected void onHandleIntent(final Intent intent) {
		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		final String command = intent.getStringExtra("command");
		final String user = intent.getStringExtra("username");
		final String password = intent.getStringExtra("password");
		final Bundle b = new Bundle();
		try {
			if (command.equals(DO_LOGIN)) {
				doLogin(receiver, b, user, password);
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
	
	private void doLogin(ResultReceiver r, Bundle b, String userName, String password)
		throws IOException, ClientProtocolException {
		
		ServerURLGenerator security = new ServerURLGenerator("Security");
		security.addParameter("method", "SignIn");
		security.addParameter("username", userName);
		security.addParameter("password", password);
		String url = security.getFullUrl();
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpResponse response = client.execute(new HttpGet(url));
		try {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();
		xr.setContentHandler(new XMLHandler());
		xr.parse(new InputSource(response.getEntity().getContent()));
		} catch (ParserConfigurationException e) {
			
		} catch (SAXException e) {
			
		} catch (IOException e) {
			
		}
	}
	
	private class XMLHandler extends DefaultHandler {
		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.startDocument();
		}
		
		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.endDocument();
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub
			super.startElement(uri, localName, qName, attributes);
		}
		
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			super.characters(ch, start, length);
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			// TODO Auto-generated method stub
			super.endElement(uri, localName, qName);
		}
	}
}
