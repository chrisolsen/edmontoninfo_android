package com.chrisolsen.edmontoninfo.models;

import java.io.Serializable;

import com.chrisolsen.edmontoninfo.db.RecFacilityDB;

import android.content.ContentValues;
import android.database.Cursor;

public class RecFacility extends BaseModel implements Serializable {

	private static final long serialVersionUID = 8090085741056946946L;
	
	public String name;
	public String address;
	public String type;
	public String entityId;
	public String phone;
	public double latitude;
	public double longitude;

	public RecFacility() {}
	
	public RecFacility(Cursor c) {
		name 		= c.getString(RecFacilityDB.CINDEX_NAME);
		address 	= c.getString(RecFacilityDB.CINDEX_ADDRESS);
		type 		= c.getString(RecFacilityDB.CINDEX_TYPE);
		entityId 	= c.getString(RecFacilityDB.CINDEX_ENTITY_ID);
		phone 		= c.getString(RecFacilityDB.CINDEX_PHONE);
		latitude 	= c.getDouble(RecFacilityDB.CINDEX_LATITUDE);
		longitude 	= c.getDouble(RecFacilityDB.CINDEX_LONGITUDE);
	}

	@Override
	public ContentValues getContentValues() {
		ContentValues vals = new ContentValues();
		
		vals.put(RecFacilityDB.CNAME_NAME, name);
		vals.put(RecFacilityDB.CNAME_ADDRESS, address);
		vals.put(RecFacilityDB.CNAME_TYPE, type);
		vals.put(RecFacilityDB.CNAME_ENTITY_ID, entityId);
		vals.put(RecFacilityDB.CNAME_PHONE, phone);
		vals.put(RecFacilityDB.CNAME_LATITUDE, latitude);
		vals.put(RecFacilityDB.CNAME_LONGITUDE, longitude);
		
		return vals;
	}

}
