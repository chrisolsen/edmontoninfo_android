package com.chrisolsen.edmontoninfo;

import com.chrisolsen.edmontoninfo.maps.IMapDelegate;
import com.chrisolsen.edmontoninfo.maps.MapContextActivity;
import com.chrisolsen.edmontoninfo.maps.MapContextValues;
import com.chrisolsen.edmontoninfo.maps.MapItemizedOverlay;
import com.chrisolsen.edmontoninfo.models.School;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class SchoolMapActivity extends MapActivity implements IMapDelegate {

	public static final String DATA_KEY = "datakey";
	
	private School school;
	private MapItemizedOverlay markerOverlays;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.google_map);
		
		school = (School)getIntent().getSerializableExtra(DATA_KEY);
		setTitle(school.name);
		
		// add marker
		Drawable marker = getResources().getDrawable( R.drawable.marker );
		GeoPoint geoPoint = new GeoPoint( (int)(school.latitude*1E6), (int)(school.longitude*1E6) );
		OverlayItem item = new OverlayItem(geoPoint, school.name, school.address);
		
		markerOverlays = new MapItemizedOverlay(marker, this);
		markerOverlays.addOverlay( item, school );
		
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

	@Override
	public boolean handleTap(Object data) {
		
		School school = (School)data;
		
		MapContextValues vals = new MapContextValues();
		vals.name = school.name;
		vals.address = school.address;
		vals.phoneNumber = school.phoneNumber;
		vals.emailAddress = school.email;
		vals.websiteUrl = school.url;
		vals.latitude = school.latitude;
		vals.longitude = school.longitude;
		
		Intent intent = new Intent(this, MapContextActivity.class);
		intent.putExtra(MapContextActivity.KEY, vals);
		startActivity(intent);
		
		return true;
	}
	
}
