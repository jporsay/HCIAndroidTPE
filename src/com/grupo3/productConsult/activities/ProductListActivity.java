package com.grupo3.productConsult.activities;

import java.io.Serializable;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.grupo3.productConsult.Product;
import com.grupo3.productConsult.R;
import com.grupo3.productConsult.services.CategoriesSearchService;

public class ProductListActivity extends ListActivity {

	private List<Product> currList;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle recdData = getIntent().getExtras();
		currList = (List<Product>) recdData.getSerializable("products");
		String breadCrumb = recdData.getString("breadCrumb");
		setTitle(breadCrumb);
		String[] products = getProductNames();
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, products));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		Animation a = AnimationUtils.makeInAnimation(getBaseContext(), false);
		a.setDuration(500);
		lv.setAnimation(a);
	}

	@Override
	protected void onListItemClick(ListView l, View view, int position, long id) {
		loadProduct(currList.get(position).getId());
	}

	private String[] getProductNames() {
		String[] names = new String[currList.size()];
		int i = 0;
		for (Product c : currList) {
			names[i++] = c.getName() + " - " + Product.CURRENCY + " "
					+ c.getPrice();
		}
		return names;
	}

	private void loadProduct(int prodId) {
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				CategoriesSearchService.class);
		intent.putExtra("command", CategoriesSearchService.LOAD_PRODUCT);
		intent.putExtra("productId", prodId + "");
		intent.putExtra("receiver", new ResultReceiver(new Handler()) {
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				switch (resultCode) {
				case CategoriesSearchService.STATUS_SUCCESS:
					Serializable product = resultData
							.getSerializable("product");
					Intent intent = new Intent(ProductListActivity.this,
							ProductDisplayActivity.class);
					Bundle b = new Bundle();
					b.putSerializable("product", product);
					b.putString("breadCrumb", getTitle().toString()
							+ ((Product) product).getName());
					intent.putExtras(b);
					startActivity(intent);
					break;
				case CategoriesSearchService.STATUS_ERROR:
					CharSequence text = getText(R.string.connectionError);
					Toast.makeText(getApplicationContext(), text,
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
		startService(intent);
	}
}
