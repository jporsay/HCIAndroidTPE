package com.grupo3.productConsult.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupo3.productConsult.Product;
import com.grupo3.productConsult.R;

public class ProductDisplayActivity extends Activity {

	private static final int[] textFieldIds = new int[] { R.id.info1,
			R.id.info2, R.id.info3, R.id.info4, R.id.info5, R.id.info6,
			R.id.info6, R.id.info7, R.id.info8 };
	private static final String[] dvd_fields = { "actors", "format",
			"language", "subtitles", "region", "number_discs", "release_date",
			"run_time", "ASIN" };
	private static final String[] book_fields = { "authors", "publisher",
			"published_date", "ISBN_10", "ISBN_13" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product);

		Bundle recdData = getIntent().getExtras();
		Product p = (Product) recdData.getSerializable("product");
		String breadCrumb = recdData.getString("breadCrumb");
		setTitle(breadCrumb);
		
		TextView t = (TextView) findViewById(R.id.title);
		t.setText(p.getName());
		t = (TextView) findViewById(R.id.price);
		t.setText(Product.CURRENCY + " " + p.getPrice());

		Object obj = fetch(p.getImgSrc());
		if (obj != null) {
			Drawable img = Drawable.createFromStream((InputStream) obj, "src");
			ImageView imgView = (ImageView) findViewById(R.id.image);
			imgView.setImageDrawable(img);
		}

		String[] fields;
		Log.d("Category", p.getCategoryId() + "");
		switch (p.getCategoryId()) {
		case 1:
			fields = dvd_fields;
			break;
		case 2:
			fields = book_fields;
			break;
		default:
			fields = dvd_fields;
		}

		int i = 0;
		for (String fieldName : fields) {
			t = (TextView) findViewById(textFieldIds[i]);
			String value = p.getProperty(fieldName);
			t.setText(fieldName + ": " + value);
			if (value != null) {
				i++;
			}
		}
		for (; i < textFieldIds.length; i++) {
			t = (TextView) findViewById(textFieldIds[i]);
			t.setText("");
		}
	}

	private Object fetch(String address) {
		try {
			URL url;
			url = new URL(address);
			Object content = url.getContent();
			return content;
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

}
