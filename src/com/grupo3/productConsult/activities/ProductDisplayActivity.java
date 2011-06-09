package com.grupo3.productConsult.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupo3.productConsult.Product;
import com.grupo3.productConsult.R;

public class ProductDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product);

		Intent intent = getIntent();
		Product p = (Product) intent.getSerializableExtra("product");

		Log.d("PRODUCTO: ", p.toString());

		TextView t = (TextView) findViewById(R.id.title);
		t.setText(p.getName());

		t = (TextView) findViewById(R.id.price);
		t.setText(Product.CURRENCY + " " + p.getPrice());

		ImageView iv = (ImageView) findViewById(R.id.image);
		// iv.setImageURI(Uri.parse(p.getImgSrc()));

	}

}
