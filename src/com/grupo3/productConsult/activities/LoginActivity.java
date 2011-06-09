package com.grupo3.productConsult.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.grupo3.productConsult.R;
import com.grupo3.productConsult.services.LoginService;

public class LoginActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}
	
	public void doLogin(View button) {
		final EditText username = (EditText) findViewById(R.id.usernameInput);
		final EditText password = (EditText) findViewById(R.id.passwordInput);
		
		String name = username.getText().toString();
		String passwd = password.getText().toString();
		
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this, LoginService.class);
		intent.putExtra("username", name);
		intent.putExtra("password", passwd);
		intent.putExtra("command", LoginService.DO_LOGIN);
		intent.putExtra("receiver", new ResultReceiver(new Handler()) {
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				switch (resultCode) {
					case LoginService.STATUS_OK:
						Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
					break;
					
					case LoginService.STATUS_CONNECTION_ERROR:
					break;
					
					case LoginService.STATUS_ERROR:
						if (resultData.containsKey("errorMessage")) {
							String errorMessage = resultData.getString("errorMessage");
							Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
						}
					break;
					
					default:
					break;
				}
			}
		});
		startService(intent);
	}
}
