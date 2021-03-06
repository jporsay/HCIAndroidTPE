package com.grupo3.productConsult.activities;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.grupo3.productConsult.Category;
import com.grupo3.productConsult.CategoryManager;
import com.grupo3.productConsult.R;
import com.grupo3.productConsult.services.CategoriesSearchService;
import com.grupo3.productConsult.services.RefreshOrdersService;

public class MenuActivity extends Activity {
	private static boolean refreshOrderStarted = false;
	private boolean isLoggedIn = true;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		if (!refreshOrderStarted) {
			this.toggleOrders(false);
			this.startOrderRefreshService(getIntent().getExtras());
			refreshOrderStarted = true;
		}
	}

	public void toggleOrders(boolean enabled) {
		final Button button = (Button) findViewById(R.id.mOrders);
		button.setEnabled(enabled);
	}
	
	public void doLogOut(View button) {
		isLoggedIn = false;
		Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
		startActivity(intent);
	}

	public void startOrderRefreshService(Bundle b) {
		if (!isLoggedIn) {
			showLoginErrorStatus();
			return;
		}
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				RefreshOrdersService.class);
		intent.putExtra("userName", b.getString("userName"));
		intent.putExtra("authToken", b.getString("authToken"));
		final MenuActivity me = this;
		intent.putExtra("receiver", new ResultReceiver(new Handler()) {
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				switch (resultCode) {
				case RefreshOrdersService.STATUS_OK:
					me.toggleOrders(true);
					break;
				case RefreshOrdersService.STATUS_ERROR:
					if (resultData.containsKey("errorMessage")) {
						String errorMessage = resultData
								.getString("errorMessage");
						Toast.makeText(getApplicationContext(), errorMessage,
								Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					break;
				}
			}
		});
		startService(intent);
	}

	public void doViewCategories(View button) {
		if (!isLoggedIn) {
			showLoginErrorStatus();
			return;
		}
		if (CategoryManager.getInstance().categoriesLoaded()) {
			startCategoriesActivity();
			return;
		}
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				CategoriesSearchService.class);
		intent.putExtra("command", CategoriesSearchService.LOAD_CATEGORIES);
		intent.putExtra("receiver", new ResultReceiver(new Handler()) {
			@SuppressWarnings("unchecked")
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				switch (resultCode) {
				case CategoriesSearchService.STATUS_SUCCESS:
					List<Category> categories = (List<Category>) resultData
							.getSerializable("categories");
					CategoryManager.getInstance().saveCategoryList(categories);
					startCategoriesActivity();
					break;
				case CategoriesSearchService.STATUS_ERROR:
					break;
				}
			}
		});
		startService(intent);
	}

	private void showLoginErrorStatus() {
		CharSequence errorMessage = getText(R.string.notLoggedIn);
		Context cont = getApplicationContext();
		Toast.makeText(cont, errorMessage, Toast.LENGTH_LONG).show();
	}

	public void doViewOrders(View button) {
		if (!isLoggedIn) {
			showLoginErrorStatus();
			return;
		}
		Bundle b = getIntent().getExtras();
		Intent intent = new Intent(MenuActivity.this, OrderListActivity.class);
		intent.putExtra("userName", b.getString("userName"));
		intent.putExtra("authToken", b.getString("authToken"));
		startActivity(intent);
	}

	private void startCategoriesActivity() {
		Intent intent = new Intent(MenuActivity.this,
				CategoryListActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
