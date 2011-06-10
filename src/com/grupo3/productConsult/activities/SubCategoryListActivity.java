package com.grupo3.productConsult.activities;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

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

public class SubCategoryListActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle recdData = getIntent().getExtras();
		final int catPos = Integer.parseInt(recdData.getString("categoryPos"));

		try {
			CategoryManager catManager = CategoryManager.getInstance();
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
					catManager.getSubCategoryNames(catPos)));

			ListView lv = getListView();
			lv.setTextFilterEnabled(true);
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					CharSequence text = ((TextView) view).getText();
					Toast.makeText(getApplicationContext(), text,
							Toast.LENGTH_SHORT).show();

					Intent newIntent = new Intent(SubCategoryListActivity.this
							.getApplicationContext(), ProductListActivity.class);

					Bundle bundle = new Bundle();
					bundle.putString("categoryPos", catPos + "");
					bundle.putString("subCategoryPos", position + "");
					newIntent.putExtras(bundle);
					startActivityForResult(newIntent, 0);
				}
			});
		} catch (IndexOutOfBoundsException e) {
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
	}
}
