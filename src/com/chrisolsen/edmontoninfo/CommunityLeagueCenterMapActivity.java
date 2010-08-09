package com.chrisolsen.edmontoninfo;

import static com.chrisolsen.edmontoninfo.maps.MapHelper.*;
import static com.chrisolsen.edmontoninfo.Global.*;

import com.chrisolsen.edmontoninfo.db.CommunityLeagueCentersDB;
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

	public static final String TAG = "Community League Center Map Activity";
	public static final String DATA_KEY = "datakey";
	
	private MapItemizedOverlay markerOverlays;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView( R.layout.google_map );
		
		int zoom;
		GeoPoint focus;
		CommunityLeagueCenter[] centers;
		CommunityLeagueCentersDB db = new CommunityLeagueCentersDB(this);
		
		MapView mapView = (MapView)findViewById( R.id.mapview );
		CommunityLeagueCenter center = (CommunityLeagueCenter)getIntent().getSerializableExtra(DATA_KEY);
		
		if ( center != null ) {
			centers = new CommunityLeagueCenter[] { center };
			setTitle(center.name);
			focus = convertToGeoPoint(center.latitude, center.longitude);
			zoom = 17;
		}
		else {
			centers = CommunityLeagueCenter.convertToArray( db.getCursor(null, null) );
			db.close();
			focus = EDMONTON_CENTER;
			zoom = 11;
		}
		
		// add markers
		Drawable marker = getResources().getDrawable( R.drawable.marker );
		markerOverlays = new MapItemizedOverlay(marker, this);
		
		for ( CommunityLeagueCenter c: centers ) {
			GeoPoint geoPoint = convertToGeoPoint(c.latitude, c.longitude);
			OverlayItem item = new OverlayItem(geoPoint, c.name, c.address);
			markerOverlays.addOverlay( item, c );
		}
		
		mapView.getOverlays().add(markerOverlays);
		mapView.setBuiltInZoomControls(true);
		mapView.getController().setZoom( zoom );
		mapView.getController().animateTo( focus );

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
