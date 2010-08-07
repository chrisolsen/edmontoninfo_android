package com.chrisolsen.edmontoninfo;

import static com.chrisolsen.edmontoninfo.Global.EDMONTON_CENTER;
import static com.chrisolsen.edmontoninfo.maps.MapHelper.convertToGeoPoint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.chrisolsen.edmontoninfo.db.RecFacilityDB;
import com.chrisolsen.edmontoninfo.maps.IMapDelegate;
import com.chrisolsen.edmontoninfo.maps.MapContextActivity;
import com.chrisolsen.edmontoninfo.maps.MapContextValues;
import com.chrisolsen.edmontoninfo.maps.MapItemizedOverlay;
import com.chrisolsen.edmontoninfo.models.RecFacility;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class RecFacilityMapActivity extends MapActivity implements IMapDelegate {

	public static final String DATA_KEY = "rec_facilities_key";	

	MapItemizedOverlay markerOverlays;
	RecFacility facility;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.google_map);
		
		int zoom;
		GeoPoint mapCenter;
		RecFacility[] facs;
		RecFacility fac = (RecFacility)getIntent().getSerializableExtra(DATA_KEY);
		
		if (fac != null) {
			setTitle(fac.name);
			mapCenter = convertToGeoPoint(fac.latitude, fac.longitude);
			facs = new RecFacility[] { fac };
			zoom = 17;
		}
		else {
			RecFacilityDB db = new RecFacilityDB(this);
			facs = RecFacility.convertToArray( db.findAll() );
			db.close();
			mapCenter = EDMONTON_CENTER;
			zoom = 11;
		}
		
		// add markers
		Drawable marker = getResources().getDrawable( R.drawable.marker );
		MapItemizedOverlay markerOverlays = new MapItemizedOverlay(marker, this);
		
		for (RecFacility f: facs) {
			GeoPoint geoPoint = convertToGeoPoint(f.latitude, f.longitude);
			OverlayItem item = new OverlayItem(geoPoint, f.name, f.address);
			markerOverlays.addOverlay( item, f );
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
		RecFacility facility = (RecFacility)data;
		
		MapContextValues vals = new MapContextValues();
		vals.name = facility.name;
		vals.address = facility.address;
		vals.phoneNumber = facility.phone;
		vals.latitude = facility.latitude;
		vals.longitude = facility.longitude;
		
		Intent intent = new Intent(this, MapContextActivity.class);
		intent.putExtra(MapContextActivity.KEY, vals);
		startActivity(intent);
		
		return true;
	}	

}
