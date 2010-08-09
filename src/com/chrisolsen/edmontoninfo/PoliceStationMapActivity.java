package com.chrisolsen.edmontoninfo;

import static com.chrisolsen.edmontoninfo.Global.EDMONTON_CENTER;
import static com.chrisolsen.edmontoninfo.maps.MapHelper.convertToGeoPoint;

import com.chrisolsen.edmontoninfo.db.PoliceStationsDB;
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

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.google_map);
		
		int zoom;
		GeoPoint mapCenter;
		PoliceStation[] stations;
		PoliceStation station = (PoliceStation)getIntent().getSerializableExtra(POLICE_STATION);
		
		if (station != null) {
			setTitle(station.name);
			mapCenter = convertToGeoPoint(station.latitude, station.longitude);
			stations = new PoliceStation[] { station };
			zoom = 17;
		}
		else {
			PoliceStationsDB db = new PoliceStationsDB(this);
			stations = PoliceStation.convertToArray( db.getCursor(null, null) );
			db.close();
			mapCenter = EDMONTON_CENTER;
			zoom = 11;
		}
		
		// add markers
		Drawable marker = getResources().getDrawable( R.drawable.marker );
		MapItemizedOverlay markerOverlays = new MapItemizedOverlay(marker, this);
		
		for (PoliceStation s: stations) {
			GeoPoint geoPoint = convertToGeoPoint(s.latitude, s.longitude);
			OverlayItem item = new OverlayItem(geoPoint, s.name, s.address);
			markerOverlays.addOverlay( item, s );
		}
		
		// configure map
		MapView mapView = (MapView)findViewById( R.id.mapview );
		mapView.getOverlays().add(markerOverlays);
		mapView.setBuiltInZoomControls(true);		
		mapView.getController().animateTo( mapCenter );
		mapView.getController().setZoom( zoom );
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
