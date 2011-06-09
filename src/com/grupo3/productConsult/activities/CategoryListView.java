package com.grupo3.productConsult.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.grupo3.productConsult.CategoryManager;
import com.grupo3.productConsult.R;

public class CategoryListView extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CategoryManager catManager = CategoryManager.getInstance();
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				catManager.getCategoryNames()));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CharSequence text = ((TextView) view).getText();
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();

				Bundle bundle = new Bundle();
				bundle.putString("categoryPos", position + "");
				Intent newIntent = new Intent(CategoryListView.this
						.getApplicationContext(), SubCategoryListView.class);
				newIntent.putExtras(bundle);
				startActivityForResult(newIntent, 0);
			}
		});
	}
}
