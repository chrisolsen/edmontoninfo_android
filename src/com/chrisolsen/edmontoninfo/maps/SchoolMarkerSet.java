package com.chrisolsen.edmontoninfo.maps;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.chrisolsen.edmontoninfo.models.School;
import com.chrisolsen.edmontoninfo.SchoolsMapActivity;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;


public class SchoolMarkerSet extends ItemizedOverlay<OverlayItem> {

	private School[] schools;
	private OverlayItem[] overlayItems;
	private IMapMarkerTapDelegate delegate;
	
	public SchoolMarkerSet(Drawable defaultMarker, School[] schools, IMapMarkerTapDelegate delegate) {
		super(boundCenterBottom( defaultMarker ));
		this.schools = schools;
		this.delegate = delegate;
		
		overlayItems = new OverlayItem[schools.length];
		School school;
		for (int i = 0; i < schools.length; i++) {
			school = schools[i];
			overlayItems[i] = new OverlayItem( school.getGeoPoint(), school.name, school.address );
		}
		
		populate();
	}

	@Override
	protected OverlayItem createItem(int index) {
		return overlayItems[index];
	}

	@Override
	public int size() {
		return schools.length;
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		// abort if too far out
		if ( mapView.getZoomLevel() < SchoolsMapActivity.ZOOM_LEVEL_FOR_DETAILS ) {
			Log.d("SchoolMarkerSet.onTap", String.format("%d", mapView.getZoomLevel()) );
			return super.onTap(p, mapView);
		}
		
		int allowance = 1000;
		int pLatE6 = p.getLatitudeE6();
		int pLngE6 = p.getLongitudeE6();
		
		int latDiff, lngDiff;
		
		for (School school: schools) {
			latDiff = Math.abs( pLatE6 - school.getGeoPoint().getLatitudeE6() );
			lngDiff = Math.abs( pLngE6 - school.getGeoPoint().getLongitudeE6() );
		
			if ( (latDiff < allowance) && (lngDiff < allowance) ) {
				delegate.tap( school );
				return true;
			}
		}
		
		return super.onTap(p, mapView);
	}

}
