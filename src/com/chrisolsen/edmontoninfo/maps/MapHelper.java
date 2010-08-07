package com.chrisolsen.edmontoninfo.maps;

import com.google.android.maps.GeoPoint;

public class MapHelper {

	public static GeoPoint convertToGeoPoint(double lat, double lng) {
		return new GeoPoint( (int)(lat*1E6), (int)(lng*1E6) );
	}
	
}
