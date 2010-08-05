package com.chrisolsen.edmontoninfo.maps;

import java.util.ArrayList;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> items; 
	private ArrayList<Object> dataItems;
	private IMapDelegate delegate;
	
	public MapItemizedOverlay(Drawable marker, IMapDelegate delegate) {
		super(boundCenterBottom(marker));
		
		items = new ArrayList<OverlayItem>();
		dataItems = new ArrayList<Object>();
		
		this.delegate = delegate;
		
		populate();
	}

	public void addOverlay(OverlayItem item, Object data) {
		items.add(item);
		dataItems.add(data);
		populate();
	}

	@Override
	public boolean onTap(int index) {
		return delegate.handleTap( dataItems.get(index) );
	}

	@Override
	public int size() {
		return items.size();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return items.get(i);
	}

}
