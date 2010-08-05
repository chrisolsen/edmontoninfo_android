package com.chrisolsen.edmontoninfo.db;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;

import com.chrisolsen.edmontoninfo.models.BaseModel;

public class Park extends BaseModel implements Serializable {

	private static final long serialVersionUID = 207430581256690178L;
	
	public String name;
	public String address;
	public String entityId;
	public double latitude;
	public double longitude;
	
	public Park() {}
	
	public Park(Cursor c) {
		this.name 		= c.getString(ParkDB.CINDEX_NAME);
		this.address 	= c.getString(ParkDB.CINDEX_ADDRESS);
		this.entityId 	= c.getString(ParkDB.CINDEX_ENTITY_ID);
		this.latitude 	= c.getDouble(ParkDB.CINDEX_LATITUDE);
		this.longitude 	= c.getDouble(ParkDB.CINDEX_LONGITUDE);
	}
	
	@Override
	public ContentValues getContentValues() {
		ContentValues vals = new ContentValues();
		
		vals.put(ParkDB.CNAME_NAME, this.name);
		vals.put(ParkDB.CNAME_ADDRESS, this.address);
		vals.put(ParkDB.CNAME_ENTITY_ID, this.entityId);
		vals.put(ParkDB.CNAME_LATITUDE, this.latitude);
		vals.put(ParkDB.CNAME_LONGITUDE, this.longitude);
		
		return vals;
	}

}
