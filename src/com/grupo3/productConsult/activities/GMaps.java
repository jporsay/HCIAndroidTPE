package com.grupo3.productConsult.activities;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.grupo3.productConsult.R;
import com.grupo3.productConsult.utilities.OrderOverlay;

public class GMaps extends MapActivity {

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Bundle b = getIntent().getExtras();
		int latitude = new Integer(b.getString("latitude"));
		int longitude = new Integer(b.getString("longitude"));
		setContentView(R.layout.map);
		MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    MapController myMapController = mapView.getController();
	    GeoPoint point = new GeoPoint(latitude * 1000000, longitude * 1000000);
	    myMapController.setCenter(point);
	    myMapController.setZoom(10);
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.map_pin);
	    OrderOverlay itemizedoverlay = new OrderOverlay(drawable, this);
	    OverlayItem overlayitem = new OverlayItem(point, "Order", "Your order");
	    itemizedoverlay.addOverlay(overlayitem);
	    mapOverlays.add(itemizedoverlay);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
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
