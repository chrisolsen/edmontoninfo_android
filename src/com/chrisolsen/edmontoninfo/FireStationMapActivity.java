package com.chrisolsen.edmontoninfo;

import static com.chrisolsen.edmontoninfo.maps.MapHelper.*;
import static com.chrisolsen.edmontoninfo.Global.*;

import com.chrisolsen.edmontoninfo.db.FireStationsDB;
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

public class FireStationMapActivity extends MapActivity implements IMapDelegate {

	public static final String DATA_KEY = "datakey";

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.google_map);
		
		int zoom;
		GeoPoint mapCenter;
		FireStation[] stations;
		FireStation station = (FireStation)getIntent().getSerializableExtra(DATA_KEY);
		
		if (station != null) {
			setTitle(station.name);
			mapCenter = convertToGeoPoint(station.latitude, station.longitude);
			stations = new FireStation[] { station };
			zoom = 17;
		}
		else {
			FireStationsDB db = new FireStationsDB(this);
			stations = FireStation.convertToArray( db.findAll() );
			db.close();
			mapCenter = EDMONTON_CENTER;
			zoom = 11;
		}
		
		// add marker
		Drawable marker = getResources().getDrawable( R.drawable.marker );
		MapItemizedOverlay markerOverlays = new MapItemizedOverlay(marker, this);
		
		for (FireStation s: stations) {
			GeoPoint geoPoint = convertToGeoPoint(s.latitude, s.longitude);
			OverlayItem item = new OverlayItem(geoPoint, s.name, s.address);
			markerOverlays.addOverlay( item, s );
		}
		
		MapView mapView = (MapView)findViewById( R.id.mapview );
		mapView.getOverlays().add(markerOverlays);
		mapView.setBuiltInZoomControls(true);		
		mapView.getController().animateTo( mapCenter );
		mapView.getController().setZoom( zoom );
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
