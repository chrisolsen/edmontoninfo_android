package com.chrisolsen.edmontoninfo;

import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.chrisolsen.edmontoninfo.db.SchoolsDB;
import com.chrisolsen.edmontoninfo.maps.IMapMarkerTapDelegate;
import com.chrisolsen.edmontoninfo.maps.SchoolMarkerSet;
import com.chrisolsen.edmontoninfo.models.School;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class SchoolsMapActivity extends MapActivity implements IMapMarkerTapDelegate {

	public static final int ZOOM_LEVEL_FOR_DETAILS = 15;
	
	MapView mapView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schools_map);
		
		mapView = (MapView)findViewById( R.id.schools_map );
		
		// add overlays
		Resources res = getResources();
		List<Overlay> overlays = mapView.getOverlays();
		
		SchoolsDB db = new SchoolsDB(this);
		Cursor c = db.findAll( SchoolsDB.CNAME_NAME );
		School[] schools = School.convertToArray( c );
		db.close();
		
		Drawable marker = res.getDrawable( R.drawable.marker );
		SchoolMarkerSet schoolMarkers = new SchoolMarkerSet(marker, schools, this);
		
		overlays.add(schoolMarkers);
		
		// mapview controls
		mapView.setBuiltInZoomControls(true);
		
		// center on Edmonton
		Double cityLat = 53.5501 * 1E6;
		Double cityLng = -113.469 * 1E6;
		mapView.getController().animateTo( new GeoPoint(cityLat.intValue(), cityLng.intValue()) );
		mapView.getController().setZoom( 13 );
		mapView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int zoom = mapView.getZoomLevel();
				Log.d("TAG", "onTouch");
				if ( zoom > ZOOM_LEVEL_FOR_DETAILS )
					Toast.makeText(SchoolsMapActivity.this, "Select marker to view details", 500).show();
				else
					Toast.makeText(SchoolsMapActivity.this, "Zoom in to be able to view school details", 500).show();
				
				return false;
			}
		});
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
		i.putExtra(SchoolsDB.CNAME_ID, school.id);
		startActivity(i);
	}

}
