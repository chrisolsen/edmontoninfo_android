package com.chrisolsen.edmontoninfo;

import static com.chrisolsen.edmontoninfo.Global.EDMONTON_CENTER;
import static com.chrisolsen.edmontoninfo.maps.MapHelper.convertToGeoPoint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.chrisolsen.edmontoninfo.db.LibraryDB;
import com.chrisolsen.edmontoninfo.maps.IMapDelegate;
import com.chrisolsen.edmontoninfo.maps.MapContextActivity;
import com.chrisolsen.edmontoninfo.maps.MapContextValues;
import com.chrisolsen.edmontoninfo.maps.MapItemizedOverlay;
import com.chrisolsen.edmontoninfo.models.Library;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class LibraryMapActivity extends MapActivity implements IMapDelegate {

	public static final String DATA_KEY = "library_data_key";
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.google_map);
		
		int zoom;
		GeoPoint mapCenter;
		Library[] libraries;
		Library library = (Library)getIntent().getSerializableExtra(DATA_KEY);
		
		if (library != null) {
			setTitle(library.branch);
			mapCenter = convertToGeoPoint(library.latitude, library.longitude);
			libraries = new Library[] { library };
			zoom = 17;
		}
		else {
			LibraryDB db = new LibraryDB(this);
			libraries = Library.convertToArray( db.findAll() );
			db.close();
			mapCenter = EDMONTON_CENTER;
			zoom = 11;
		}
		
		// add markers
		Drawable marker = getResources().getDrawable( R.drawable.marker );
		MapItemizedOverlay markerOverlays = new MapItemizedOverlay(marker, this);
		
		for (Library l: libraries) {
			GeoPoint geoPoint = convertToGeoPoint(l.latitude, l.longitude);
			OverlayItem item = new OverlayItem(geoPoint, l.branch, l.address);
			markerOverlays.addOverlay( item, l );
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleTap(Object data) {
		Library lib = (Library)data;
		
		MapContextValues vals = new MapContextValues();
		vals.name = lib.branch;
		vals.address = lib.address;
		vals.phoneNumber = lib.phoneNumber;
		vals.websiteUrl = lib.url;
		vals.latitude = lib.latitude;
		vals.longitude = lib.longitude;
		
		Intent i = new Intent(this, MapContextActivity.class);
		i.putExtra(MapContextActivity.KEY, vals);
		
		startActivity(i);
		
		return true;
	}

}
