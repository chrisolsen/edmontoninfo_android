package com.chrisolsen.edmontoninfo;

import java.util.List;

import com.chrisolsen.edmontoninfo.db.CommunityLeagueCentersDB;
import com.chrisolsen.edmontoninfo.maps.IMapMarkerTapDelegate;
import com.chrisolsen.edmontoninfo.maps.MapMarker;
import com.chrisolsen.edmontoninfo.maps.MapMarkerList;
import com.chrisolsen.edmontoninfo.models.CommunityLeagueCenter;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class CommunityLeagueCentersMapActivity extends MapActivity implements IMapMarkerTapDelegate {

	MapView mapView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_map);
		
		// mapview controls
		mapView = (MapView)findViewById( R.id.google_map );
		List<Overlay> overlays = mapView.getOverlays();
		mapView.setBuiltInZoomControls(true);
		
		MapMarker[] markers;
		
		// show one center location or all of them?
		if ( getIntent().hasExtra("center") ) {
			CommunityLeagueCenter center = (CommunityLeagueCenter)getIntent().getSerializableExtra("center");
			markers = new MapMarker[1];
			markers[0] = new MapMarker( center, center.name, "", center.latitude, center.longitude);
			
			Double lat = center.latitude * 1E6;
			Double lng = center.longitude * 1E6;
			mapView.getController().animateTo( new GeoPoint(lat.intValue(), lng.intValue()) );
			mapView.getController().setZoom( 15 );
		}
		else {
			// center on Edmonton
			Double cityLat = Double.valueOf( getString(R.string.edmonton_latitude_1e6) );
			Double cityLng = Double.valueOf( getString(R.string.edmonton_longitude_1e6) );
			int zoom = Integer.valueOf( getString(R.string.default_map_zoom) );
			
			mapView.getController().animateTo( new GeoPoint(cityLat.intValue(), cityLng.intValue()) );
			mapView.getController().setZoom( zoom );
			
			// overlays
			CommunityLeagueCentersDB db = new CommunityLeagueCentersDB(this);
			Cursor c = db.findAll(CommunityLeagueCentersDB.CNAME_NAME);
			CommunityLeagueCenter[] centers = CommunityLeagueCenter.convertToArray(c);
			db.close();
			markers = new MapMarker[centers.length];
			
			CommunityLeagueCenter center;
			for (int i=0; i<centers.length; i++) {
				center = centers[i];
				markers[i] = new MapMarker( center, center.name, "", center.latitude, center.longitude);
			}
		}
		
		Resources res = getResources();
		Drawable marker = res.getDrawable( R.drawable.marker );
		MapMarkerList stationMarkers = new MapMarkerList(marker, markers, this);
		overlays.add(stationMarkers);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * IMapMarkerTapDelegate methods
	 */
	@Override
	public void tap(Object obj) {
		// do nothing
	}
}
