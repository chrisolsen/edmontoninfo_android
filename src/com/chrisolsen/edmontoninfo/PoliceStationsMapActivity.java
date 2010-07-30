package com.chrisolsen.edmontoninfo;

import java.util.List;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.chrisolsen.edmontoninfo.db.PoliceStationsDB;
import com.chrisolsen.edmontoninfo.maps.IMapMarkerTapDelegate;
import com.chrisolsen.edmontoninfo.maps.MapMarker;
import com.chrisolsen.edmontoninfo.maps.MapMarkerList;
import com.chrisolsen.edmontoninfo.models.PoliceStation;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class PoliceStationsMapActivity extends MapActivity implements IMapMarkerTapDelegate {

	MapView mapView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_map);
		
		// mapview controls
		mapView = (MapView)findViewById( R.id.google_map );
		List<Overlay> overlays = mapView.getOverlays();
		mapView.setBuiltInZoomControls(true);
		
		// center on Edmonton
		Double cityLat = Double.valueOf( getString(R.string.edmonton_latitude_1e6) );
		Double cityLng = Double.valueOf( getString(R.string.edmonton_longitude_1e6) );
		int zoom = Integer.valueOf( getString(R.string.default_map_zoom) );
		
		mapView.getController().animateTo( new GeoPoint(cityLat.intValue(), cityLng.intValue()) );
		mapView.getController().setZoom( zoom );
		
		// police station overlays
		PoliceStationsDB db = new PoliceStationsDB(this);
		Cursor c = db.findAll(PoliceStationsDB.CNAME_NAME);
		PoliceStation[] stations = PoliceStation.convertToArray(c);
		db.close();
		MapMarker[] markers = new MapMarker[stations.length];
		
		PoliceStation ps;
		for (int i=0; i<stations.length; i++) {
			ps = stations[i];
			markers[i] = new MapMarker( ps, ps.name, ps.address, ps.latitude, ps.longitude);
		}
		
		Resources res = getResources();
		Drawable marker = res.getDrawable( R.drawable.marker );
		MapMarkerList stationMarkers = new MapMarkerList(marker, markers, this);
		overlays.add(stationMarkers);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
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
