package com.grupo3.productConsult.utilities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grupo3.productConsult.R;

public class CustomAdapter extends ArrayAdapter<String> {
	private List<String> items;
	
	public CustomAdapter(Context context, int textViewResourceId, List<String> items) {
		super(context, textViewResourceId, items);
        this.items = items;
	}
	
	public CustomAdapter(Context context, int textViewResourceId, String[] items) {
		super(context, textViewResourceId, items);
		List<String> r = new ArrayList<String>();
		for (String item : items) {
			r.add(item);
		}
		this.items = r;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item, null);
        }
        String s = items.get(position);
        if (s != null) {
	        TextView tt = (TextView) v.findViewById(R.id.listText);
	        if (tt != null) {
	        	tt.setText(Html.fromHtml(s));
	        }
        }
        return v;
	}
}
