package com.chrisolsen.edmontoninfo;

import static com.chrisolsen.edmontoninfo.Global.EDMONTON_CENTER;
import static com.chrisolsen.edmontoninfo.maps.MapHelper.convertToGeoPoint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.chrisolsen.edmontoninfo.db.Park;
import com.chrisolsen.edmontoninfo.db.ParkDB;
import com.chrisolsen.edmontoninfo.maps.IMapDelegate;
import com.chrisolsen.edmontoninfo.maps.MapContextActivity;
import com.chrisolsen.edmontoninfo.maps.MapContextValues;
import com.chrisolsen.edmontoninfo.maps.MapItemizedOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class ParkMapActivity extends MapActivity implements IMapDelegate {

	public static final String DATA_KEY = "park_data_key";
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.google_map);
		
		int zoom;
		GeoPoint mapCenter;
		Park[] parks;
		Park park = (Park)getIntent().getSerializableExtra(DATA_KEY);
		
		if (park != null) {
			setTitle(park.name);
			mapCenter = convertToGeoPoint(park.latitude, park.longitude);
			parks = new Park[] { park };
			zoom = 17;
		}
		else {
			ParkDB db = new ParkDB(this);
			parks = Park.convertToArray( db.getCursor(null, null) );
			db.close();
			mapCenter = EDMONTON_CENTER;
			zoom = 11;
		}
		
		// add markers
		Drawable marker = getResources().getDrawable( R.drawable.marker );
		MapItemizedOverlay markerOverlays = new MapItemizedOverlay(marker, this);
		
		for (Park p: parks) {
			GeoPoint geoPoint = convertToGeoPoint(p.latitude, p.longitude);
			OverlayItem item = new OverlayItem(geoPoint, p.name, p.address);
			markerOverlays.addOverlay( item, p );
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
		Park park = (Park)data;
		
		MapContextValues vals = new MapContextValues();
		vals.name = park.name;
		vals.latitude = park.latitude;
		vals.longitude = park.longitude;
		
		Intent i = new Intent(this, MapContextActivity.class);
		i.putExtra(MapContextActivity.KEY, vals);
		
		startActivity(i);
		
		return true;
	}
}
