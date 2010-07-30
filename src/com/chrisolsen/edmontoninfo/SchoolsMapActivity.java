package com.chrisolsen.edmontoninfo;

import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.chrisolsen.edmontoninfo.db.SchoolsDB;
import com.chrisolsen.edmontoninfo.maps.IMapMarkerTapDelegate;
import com.chrisolsen.edmontoninfo.maps.MapMarkerList;
import com.chrisolsen.edmontoninfo.maps.MapMarker;
import com.chrisolsen.edmontoninfo.models.School;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class SchoolsMapActivity extends MapActivity implements IMapMarkerTapDelegate {

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
		mapView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int zoom = mapView.getZoomLevel();
				if ( zoom > MapMarkerList.ZOOM_LEVEL_FOR_DETAILS )
					Toast.makeText(SchoolsMapActivity.this, "Select marker to view details", 500).show();
				else
					Toast.makeText(SchoolsMapActivity.this, "Zoom in to be able to view school details", 500).show();
				
				return false;
			}
		});
		
		// add school overlays
		SchoolsDB db = new SchoolsDB(this);
		Cursor c = db.findAll( SchoolsDB.CNAME_NAME );
		School[] schools = School.convertToArray( c );
		db.close();
		
		MapMarker[] mapMarkers = new MapMarker[schools.length];
		School s;
		for (int i=0; i<schools.length; i++) {
			s = schools[i];
			mapMarkers[i] = new MapMarker( s, s.name, s.address, s.latitude, s.longitude );
		}

		Resources res = getResources();
		Drawable marker = res.getDrawable( R.drawable.marker );
		MapMarkerList schoolMarkers = new MapMarkerList(marker, mapMarkers, this);
		
		overlays.add(schoolMarkers);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * IMapMarkerTapDelegate methods
	 */
	public void tap(Object obj) {
		School school = (School)obj;
		Intent i = new Intent(this, SchoolActivity.class);
		i.putExtra("school", school);
		startActivity(i);
	}

}
