package com.chrisolsen.edmontoninfo.maps;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MapMarkerList extends ItemizedOverlay<OverlayItem> {

	public static final int ZOOM_LEVEL_FOR_DETAILS = 15;
	
	private MapMarker[] markers;
	private OverlayItem[] overlayItems;
	private IMapMarkerTapDelegate delegate;
	
	public MapMarkerList(Drawable defaultMarker, MapMarker[] markers, IMapMarkerTapDelegate delegate) {
		super(boundCenterBottom( defaultMarker ));
		this.markers = markers;
		this.delegate = delegate;
		
		overlayItems = new OverlayItem[markers.length];
		MapMarker marker;
		for (int i = 0; i < markers.length; i++) {
			marker = markers[i];
			overlayItems[i] = new OverlayItem( marker.geoPoint, marker.name, marker.details );
		}
		
		populate();
	}

	@Override
	protected OverlayItem createItem(int index) {
		return overlayItems[index];
	}

	@Override
	public int size() {
		return markers.length;
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		// abort if too far out
		if ( mapView.getZoomLevel() < ZOOM_LEVEL_FOR_DETAILS ) {
			return super.onTap(p, mapView);
		}
		
		int allowance = 1000;
		int pLatE6 = p.getLatitudeE6();
		int pLngE6 = p.getLongitudeE6();
		
		int latDiff, lngDiff;
		
		for (MapMarker marker: markers) {
			latDiff = Math.abs( pLatE6 - marker.geoPoint.getLatitudeE6() );
			lngDiff = Math.abs( pLngE6 - marker.geoPoint.getLongitudeE6() );
		
			if ( (latDiff < allowance) && (lngDiff < allowance) ) {
				delegate.tap( marker.obj );
				return true;
			}
		}
		
		return super.onTap(p, mapView);
	}
}
