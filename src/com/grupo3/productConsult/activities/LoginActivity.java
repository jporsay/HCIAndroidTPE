package com.grupo3.productConsult.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.grupo3.productConsult.R;

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
	}
}
