package com.chrisolsen.edmontoninfo;

import com.chrisolsen.edmontoninfo.maps.IMapDelegate;
import com.chrisolsen.edmontoninfo.maps.MapContextActivity;
import com.chrisolsen.edmontoninfo.maps.MapContextValues;
import com.chrisolsen.edmontoninfo.maps.MapItemizedOverlay;
import com.chrisolsen.edmontoninfo.models.PoliceStation;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class PoliceStationMapActivity extends MapActivity implements IMapDelegate {

	public static final String POLICE_STATION = "police_station";	

	MapItemizedOverlay markerOverlays;
	PoliceStation station;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.google_map);
		
		station = (PoliceStation)getIntent().getSerializableExtra(POLICE_STATION);
		setTitle(station.name);
		
		// add marker
		Drawable marker = getResources().getDrawable( R.drawable.marker );
		GeoPoint geoPoint = new GeoPoint( (int)(station.latitude*1E6), (int)(station.longitude*1E6) );
		OverlayItem item = new OverlayItem(geoPoint, station.name, station.address);
		
		markerOverlays = new MapItemizedOverlay(marker, this);
		markerOverlays.addOverlay( item, station );
		
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
		PoliceStation station = (PoliceStation)data;
		
		MapContextValues vals = new MapContextValues();
		vals.name = station.name;
		vals.address = station.address;
		vals.phoneNumber = station.phoneNumber;
		vals.websiteUrl = station.url;
		vals.latitude = station.latitude;
		vals.longitude = station.longitude;
		
		Intent intent = new Intent(this, MapContextActivity.class);
		intent.putExtra(MapContextActivity.KEY, vals);
		startActivity(intent);
		
		return true;
	}	

}
