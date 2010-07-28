package com.chrisolsen.edmontoninfo;

import android.os.Bundle;

import com.chrisolsen.edmontoninfo.maps.SchoolItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class SchoolsMapActivity extends MapActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schools_map);
		
		// add overlays
		MapView mapView = (MapView)findViewById( R.id.schools_map );
		List<Overlay> overlays = 
		SchoolItemizedOverlay
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
