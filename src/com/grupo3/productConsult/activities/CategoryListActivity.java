package com.grupo3.productConsult.activities;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.grupo3.productConsult.Category;
import com.grupo3.productConsult.CategoryManager;
import com.grupo3.productConsult.R;
import com.grupo3.productConsult.services.CategoriesSearchService;

public class CategoryListActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CategoryManager catManager = CategoryManager.getInstance();
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				catManager.getCategoryNames()));
		setTitle(R.string.browserTitle);
		ListView lv = getListView();

		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CharSequence text = ((TextView) view).getText();
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
				loadSubCategory(position);
			}
		});
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
					Intent intent = new Intent(CategoryListActivity.this,
							SubCategoryListActivity.class);
					Bundle b = new Bundle();
					b.putString("categoryPos", catIndex + "");
					b.putString("breadCrumb", CategoryManager.getInstance()
							.getCategoryList().get(catIndex).getName());
					intent.putExtras(b);
					startActivity(intent);
					break;
				case CategoriesSearchService.STATUS_ERROR:
					break;
				}
			}
		});
		startService(intent);
	}
}
