package com.grupo3.productConsult.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.grupo3.productConsult.R;

public class SplashActivity extends Activity {
	private static final int STOP = 0;
	private static final int TIME_TO_WAIT_IN_MS = 2000;
	
	private Handler splashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SplashActivity.STOP:
					Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
					break;
			}
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Message msg = new Message();
		msg.what = STOP;
		splashHandler.sendMessageDelayed(msg, TIME_TO_WAIT_IN_MS);
	}
}
