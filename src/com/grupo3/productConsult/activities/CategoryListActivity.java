package com.grupo3.productConsult.activities;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.grupo3.productConsult.Category;
import com.grupo3.productConsult.CategoryManager;
import com.grupo3.productConsult.R;
import com.grupo3.productConsult.services.CategoriesSearchService;
import com.grupo3.productConsult.utilities.CustomAdapter;

public class CategoryListActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CategoryManager catManager = CategoryManager.getInstance();
		setListAdapter(new CustomAdapter(this,
				R.layout.list_item, catManager
						.getCategoryNames()));
		setTitle(R.string.browserTitle);
		ListView lv = getListView();

		CharSequence text = getText(R.string.listHint);
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

		lv.setTextFilterEnabled(true);
		Animation a = AnimationUtils.makeInAnimation(getBaseContext(), false);
		a.setDuration(500);
		lv.setAnimation(a);
	}

	@Override
	protected void onListItemClick(ListView l, View view, int position, long id) {
		CharSequence text = ((TextView)view.findViewById(R.id.listText)).getText();
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
				.show();
		if (!CategoryManager.getInstance().subCategoryLoaded(position)) {
			loadSubCategory(position);
		} else {
			startSubCategoriesActivity(position);
		}
	}

	private void loadSubCategory(final int catIndex) {
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				CategoriesSearchService.class);
		intent.putExtra("command", CategoriesSearchService.LOAD_SUBCATEGORIES);
		int catId = CategoryManager.getInstance().getCategoryList().get(
				catIndex).getId();
		intent.putExtra("subCategoryId", catId + "");
		intent.putExtra("receiver", new ResultReceiver(new Handler()) {
			@SuppressWarnings("unchecked")
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				switch (resultCode) {
				case CategoriesSearchService.STATUS_SUCCESS:
					List<Category> subCategories = (List<Category>) resultData
							.getSerializable("subCategories");
					CategoryManager.getInstance().saveSubCategories(catIndex,
							subCategories);
					startSubCategoriesActivity(catIndex);
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

	private void startSubCategoriesActivity(int catIndex) {
		Intent intent = new Intent(CategoryListActivity.this,
				SubCategoryListActivity.class);
		Bundle b = new Bundle();
		b.putString("categoryPos", catIndex + "");
		b.putString("breadCrumb", CategoryManager.getInstance()
				.getCategoryList().get(catIndex).getName());
		intent.putExtras(b);
		startActivity(intent);
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
