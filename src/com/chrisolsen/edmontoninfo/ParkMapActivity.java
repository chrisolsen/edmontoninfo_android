package com.chrisolsen.edmontoninfo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.chrisolsen.edmontoninfo.db.Park;
import com.chrisolsen.edmontoninfo.maps.IMapDelegate;
import com.chrisolsen.edmontoninfo.maps.MapContextActivity;
import com.chrisolsen.edmontoninfo.maps.MapContextValues;
import com.chrisolsen.edmontoninfo.maps.MapItemizedOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class ParkMapActivity extends MapActivity implements IMapDelegate {

	public static final String DATA_KEY = "park_data_key";
	private Park park;
	private MapItemizedOverlay markerOverlays;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.google_map);
		
		park = (Park)getIntent().getSerializableExtra(DATA_KEY);
		setTitle(park.name);
		
		// add marker
		Drawable marker = getResources().getDrawable( R.drawable.marker );
		GeoPoint geoPoint = new GeoPoint( (int)(park.latitude*1E6), (int)(park.longitude*1E6) );
		OverlayItem item = new OverlayItem(geoPoint, park.name, park.address);
		
		markerOverlays = new MapItemizedOverlay(marker, this);
		markerOverlays.addOverlay( item, park );
		
		MapView mapView = (MapView)findViewById( R.id.mapview );
		mapView.getOverlays().add(markerOverlays);
		mapView.setBuiltInZoomControls(true);		
		mapView.getController().animateTo( geoPoint );
		mapView.getController().setZoom( 17 );
	}

	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleTap(Object data) {
		Park park = (Park)data;
		
		MapContextValues vals = new MapContextValues();
		vals.name = park.name;
		vals.latitude = park.latitude;
		vals.longitude = park.longitude;
		
		Intent i = new Intent(this, MapContextActivity.class);
		i.putExtra(MapContextActivity.KEY, vals);
		
		startActivity(i);
		
		return true;
	}
}
