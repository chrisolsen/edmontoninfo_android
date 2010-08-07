package com.chrisolsen.edmontoninfo;

import static com.chrisolsen.edmontoninfo.Global.*;
import static com.chrisolsen.edmontoninfo.maps.MapHelper.*;

import com.chrisolsen.edmontoninfo.db.SchoolsDB;
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
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.google_map);
		
		int zoom;
		GeoPoint mapCenter;
		School[] schools;
		School school = (School)getIntent().getSerializableExtra(DATA_KEY);
		
		if (school != null) {
			setTitle(school.name);
			mapCenter = convertToGeoPoint(school.latitude, school.longitude);
			schools = new School[] { school };
			zoom = 17;
		}
		else {
			SchoolsDB db = new SchoolsDB(this);
			schools = School.convertToArray( db.findAll() );
			db.close();
			mapCenter = EDMONTON_CENTER;
			zoom = 11;
		}
		
		// add markers
		Drawable marker = getResources().getDrawable( R.drawable.marker );
		MapItemizedOverlay markerOverlays = new MapItemizedOverlay(marker, this);
		
		for (School s: schools) {
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
