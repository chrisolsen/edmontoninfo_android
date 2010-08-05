package com.chrisolsen.edmontoninfo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.chrisolsen.edmontoninfo.maps.IMapDelegate;
import com.chrisolsen.edmontoninfo.maps.MapContextActivity;
import com.chrisolsen.edmontoninfo.maps.MapContextValues;
import com.chrisolsen.edmontoninfo.maps.MapItemizedOverlay;
import com.chrisolsen.edmontoninfo.models.RecFacility;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class RecFacilityMapActivity extends MapActivity implements IMapDelegate {

	public static final String DATA_KEY = "rec_facilities_key";	

	MapItemizedOverlay markerOverlays;
	RecFacility facility;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.google_map);
		
		facility = (RecFacility)getIntent().getSerializableExtra(DATA_KEY);
		setTitle(facility.name);
		
		// add marker
		Drawable marker = getResources().getDrawable( R.drawable.marker );
		GeoPoint geoPoint = new GeoPoint( (int)(facility.latitude*1E6), (int)(facility.longitude*1E6) );
		OverlayItem item = new OverlayItem(geoPoint, facility.name, facility.address);
		
		markerOverlays = new MapItemizedOverlay(marker, this);
		markerOverlays.addOverlay( item, facility );
		
		MapView mapView = (MapView)findViewById( R.id.mapview );
		mapView.getOverlays().add(markerOverlays);
		mapView.setBuiltInZoomControls(true);		
		mapView.getController().animateTo( geoPoint );
		mapView.getController().setZoom( 17 );
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * IMapDelegate methods
	 */
	@Override
	public boolean handleTap(Object data) {
		RecFacility facility = (RecFacility)data;
		
		MapContextValues vals = new MapContextValues();
		vals.name = facility.name;
		vals.address = facility.address;
		vals.phoneNumber = facility.phone;
		vals.latitude = facility.latitude;
		vals.longitude = facility.longitude;
		
		Intent intent = new Intent(this, MapContextActivity.class);
		intent.putExtra(MapContextActivity.KEY, vals);
		startActivity(intent);
		
		return true;
	}	

}
