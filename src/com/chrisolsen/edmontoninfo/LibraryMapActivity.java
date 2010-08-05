package com.chrisolsen.edmontoninfo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.chrisolsen.edmontoninfo.maps.IMapDelegate;
import com.chrisolsen.edmontoninfo.maps.MapContextActivity;
import com.chrisolsen.edmontoninfo.maps.MapContextValues;
import com.chrisolsen.edmontoninfo.maps.MapItemizedOverlay;
import com.chrisolsen.edmontoninfo.models.Library;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class LibraryMapActivity extends MapActivity implements IMapDelegate {

	public static final String DATA_KEY = "library_data_key";
	private Library library;
	private MapItemizedOverlay markerOverlays;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.google_map);
		
		library = (Library)getIntent().getSerializableExtra(DATA_KEY);
		setTitle(library.branch);
		
		// add marker
		Drawable marker = getResources().getDrawable( R.drawable.marker );
		GeoPoint geoPoint = new GeoPoint( (int)(library.latitude*1E6), (int)(library.longitude*1E6) );
		OverlayItem item = new OverlayItem(geoPoint, library.branch, library.address);
		
		markerOverlays = new MapItemizedOverlay(marker, this);
		markerOverlays.addOverlay( item, library );
		
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
		Library lib = (Library)data;
		
		MapContextValues vals = new MapContextValues();
		vals.name = lib.branch;
		vals.address = lib.address;
		vals.phoneNumber = lib.phoneNumber;
		vals.websiteUrl = lib.url;
		vals.latitude = lib.latitude;
		vals.longitude = lib.longitude;
		
		Intent i = new Intent(this, MapContextActivity.class);
		i.putExtra(MapContextActivity.KEY, vals);
		
		startActivity(i);
		
		return true;
	}

}
