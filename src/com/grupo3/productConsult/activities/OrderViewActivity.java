package com.grupo3.productConsult.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.grupo3.productConsult.R;
import com.grupo3.productConsult.services.OrderCategoriesListService;
import com.grupo3.productConsult.utilities.Order;

public class OrderViewActivity extends Activity {
	private String userName;
	private String token;
	private Order order;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_info);
		Bundle b = getIntent().getExtras();

		this.userName = b.getString("userName");
		this.token = b.getString("authToken");
		this.order = (Order) b.getSerializable("order");
		setInformation(b);
	}

	private void setInformation(Bundle data) {
		TextView t = (TextView) findViewById(R.id.title);
		t.setText(R.string.orderInformation);
		setLabels();
		setValues();
	}

	private void setLabels() {
		TextView t;
		t = (TextView) findViewById(R.id.orderStatusLabel);
		t.setText(R.string.orderStatusLabel);
		t = (TextView) findViewById(R.id.shippedDateLabel);
		t.setText(R.string.orderShippedDateLabel);
		t = (TextView) findViewById(R.id.locationLabel);
		t.setText(R.string.orderLocationLabel);
		Button b;
		b = (Button) findViewById(R.id.viewProductsBtn);
		b.setText(R.string.orderProductBtn);
		b = (Button) findViewById(R.id.viewMapBtn);
		b.setText(R.string.orderLocationBtn);
	}

	private void setValues() {
		TextView t;
		t = (TextView) findViewById(R.id.orderStatusValue);
		t.setText(" " + order.getStatusName());
		t = (TextView) findViewById(R.id.shippedDateValue);
		t.setText(" " + order.getShippedDate());
		t = (TextView) findViewById(R.id.locationValue);
		String coord = " ( " + order.getLatitude() + ", "
				+ order.getLongitude() + " )";
		t.setText(coord);
	}

	public void viewOnMap(View button) {
		Intent intent = new Intent(OrderViewActivity.this, GMaps.class);
		intent.putExtra("latitude", order.getLatitude());
		intent.putExtra("longitude", order.getLongitude());
		startActivity(intent);
	}

	public void viewOrderItems(View button) {
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				OrderCategoriesListService.class);
		intent.putExtra("id", order.getId());
		intent.putExtra("userName", this.userName);
		intent.putExtra("authToken", this.token);
		final Bundle b = getIntent().getExtras();
		intent.putExtra("receiver", new ResultReceiver(new Handler()) {
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				switch (resultCode) {
				case OrderCategoriesListService.STATUS_OK:
					Intent intent = new Intent(OrderViewActivity.this,
							ProductListActivity.class);
					intent.putExtras(b);
					startActivity(intent);
					break;

				case OrderCategoriesListService.STATUS_ERROR:
					CharSequence text = getText(R.string.connectionError);
					Toast.makeText(getApplicationContext(), text,
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
		startService(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Intent intent = new Intent(this, MenuActivity.class);
			intent.putExtras(getIntent());
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
