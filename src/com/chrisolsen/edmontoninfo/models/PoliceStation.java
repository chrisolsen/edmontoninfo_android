package com.chrisolsen.edmontoninfo.models;

import com.chrisolsen.edmontoninfo.db.PoliceStationsDB;

import android.content.ContentValues;
import android.database.Cursor;

public class PoliceStation extends BaseModel {

	public String name;
	public String address;
	public double latitude;
	public double longitude;
	
	public PoliceStation() {}
	
	public PoliceStation(Cursor c) {
		this.id = c.getLong(PoliceStationsDB.CINDEX_ID);
		this.name = c.getString(PoliceStationsDB.CINDEX_NAME);
		this.address = c.getString(PoliceStationsDB.CINDEX_ADDRESS);
		this.latitude = c.getDouble(PoliceStationsDB.CINDEX_LAT);
		this.longitude = c.getDouble(PoliceStationsDB.CINDEX_LNG);
	}
	
	@Override
	public ContentValues getContentValues() {
		ContentValues vals = new ContentValues();
		
		vals.put(PoliceStationsDB.CNAME_ID, id);
		vals.put(PoliceStationsDB.CNAME_NAME, name);
		vals.put(PoliceStationsDB.CNAME_ADDRESS, address);
		vals.put(PoliceStationsDB.CNAME_LAT, latitude);
		vals.put(PoliceStationsDB.CNAME_LNG, longitude);
		
		return vals;
	}

}
