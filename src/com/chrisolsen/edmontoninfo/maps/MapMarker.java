package com.chrisolsen.edmontoninfo.maps;

import com.google.android.maps.GeoPoint;

public class MapMarker {
	public Object obj;
	public String name;
	public String details;
	public GeoPoint geoPoint;
	
	public MapMarker() {}
	
	public MapMarker(Object obj, String name, String details, double lat, double lng) {
		this.obj = obj;
		this.name = name;
		this.details = details;
		
		Double latE6 = lat * 1E6;
		Double lngE6 = lng * 1E6;
		this.geoPoint = new GeoPoint( latE6.intValue(), lngE6.intValue() );
	}
}
