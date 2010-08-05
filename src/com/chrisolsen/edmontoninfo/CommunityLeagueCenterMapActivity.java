package com.chrisolsen.edmontoninfo;

import com.chrisolsen.edmontoninfo.maps.IMapDelegate;
import com.chrisolsen.edmontoninfo.maps.MapContextActivity;
import com.chrisolsen.edmontoninfo.maps.MapContextValues;
import com.chrisolsen.edmontoninfo.maps.MapItemizedOverlay;
import com.chrisolsen.edmontoninfo.models.CommunityLeagueCenter;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class CommunityLeagueCenterMapActivity extends MapActivity implements IMapDelegate {

	public static final String DATA_KEY = "datakey";
	
	private CommunityLeagueCenter center;
	private MapItemizedOverlay markerOverlays;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView( R.layout.google_map );
		
		center = (CommunityLeagueCenter)getIntent().getSerializableExtra(DATA_KEY);
		setTitle(center.name);

		// add marker
		Drawable marker = getResources().getDrawable( R.drawable.marker );
		GeoPoint geoPoint = new GeoPoint( (int)(center.latitude*1E6), (int)(center.longitude*1E6) );
		OverlayItem item = new OverlayItem(geoPoint, center.name, center.address);
		
		markerOverlays = new MapItemizedOverlay(marker, this);
		markerOverlays.addOverlay( item, center );
		
		MapView mapView = (MapView)findViewById( R.id.mapview );
		mapView.getOverlays().add(markerOverlays);
		mapView.setBuiltInZoomControls(true);		
		mapView.getController().animateTo( geoPoint );
		mapView.getController().setZoom( 17 );
	}

	@Override
	public boolean handleTap(Object data) {
		
		CommunityLeagueCenter center = (CommunityLeagueCenter)data;
		MapContextValues vals = new MapContextValues();
		
		vals.name = center.name;
		vals.address = center.address;
		vals.phoneNumber = center.phoneNumber;
		vals.latitude = center.latitude;
		vals.longitude = center.longitude;
		
		Intent intent = new Intent(this, MapContextActivity.class);
		intent.putExtra(MapContextActivity.KEY, vals);
		startActivity(intent);
		
		return false;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
