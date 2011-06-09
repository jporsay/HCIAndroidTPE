package com.grupo3.productConsult.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.grupo3.productConsult.CategoryManager;
import com.grupo3.productConsult.R;

public class SubCategoryListView extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle recdData = getIntent().getExtras();
		int pos = Integer.parseInt(recdData.getString("categoryPos"));

		CategoryManager catManager = CategoryManager.getInstance();
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				catManager.getSubCategoryNames(pos)));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CharSequence text = ((TextView) view).getText();
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
