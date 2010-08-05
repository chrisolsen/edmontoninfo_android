package com.chrisolsen.edmontoninfo;

import static com.chrisolsen.edmontoninfo.Global.*;

import com.chrisolsen.edmontoninfo.maps.IMapDelegate;
import com.chrisolsen.edmontoninfo.maps.MapContextActivity;
import com.chrisolsen.edmontoninfo.maps.MapContextValues;
import com.chrisolsen.edmontoninfo.maps.MapItemizedOverlay;
import com.chrisolsen.edmontoninfo.models.FireStation;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

public class FireStationMapActivity extends MapActivity implements IMapDelegate {

	public static final String DATA_KEY = "datakey";
	
	private FireStation station;
	private MapItemizedOverlay markerOverlays;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.google_map);
		
		station = (FireStation)getIntent().getSerializableExtra(DATA_KEY);
		setTitle(station.name);

		Log.d(LOGD, "Name: " + station.name);
		Log.d(LOGD, "Address: " + station.address);
		Log.d(LOGD, "Num: " + station.number);
		
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
	public boolean handleTap(Object data) {
		FireStation station = (FireStation)data;
		
		MapContextValues vals = new MapContextValues();
		vals.name = String.format("#%s %s", station.number, station.name);
		vals.address = station.address;
		vals.latitude = station.latitude;
		vals.longitude = station.longitude;
		
		
		Intent intent = new Intent(this, MapContextActivity.class); 
		intent.putExtra(MapContextActivity.KEY, vals);
		startActivity(intent);
		
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
