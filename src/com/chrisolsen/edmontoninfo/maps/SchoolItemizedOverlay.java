package com.chrisolsen.edmontoninfo.maps;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.chrisolsen.edmontoninfo.models.School;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class SchoolItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<School> schools;
	private ArrayList<OverlayItem> overlayItems;
	
	public SchoolItemizedOverlay(Drawable defaultMarker, ArrayList<School> schools) {
		super(defaultMarker);
		this.schools = schools;
		populate();
	}

	@Override
	protected OverlayItem createItem(int index) {
		OverlayItem item;
		
		if ( overlayItems.size() > 0 ) {
			item = overlayItems.get( index );
			if ( item != null )
				return item;
		}
		
		School school = schools.get(index);
		item = new OverlayItem( school.getGeoPoint(), school.name, school.address );
		overlayItems.add(index, item);
		
		return item;
	}

	@Override
	public int size() {
		return schools.size();
	}
	
}
