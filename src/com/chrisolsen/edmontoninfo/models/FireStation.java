package com.chrisolsen.edmontoninfo.models;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;

import com.chrisolsen.edmontoninfo.db.FireStationsDB;

public class FireStation extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = 1527878050564274601L;
	
	public String name;
	public String address;
	public String number;
	public double latitude;
	public double longitude;
	
	public FireStation() {}
	
	public FireStation(Cursor c) {
		this.id = c.getLong(FireStationsDB.CINDEX_ID);
		this.name = c.getString(FireStationsDB.CINDEX_NAME);
		this.address = c.getString(FireStationsDB.CINDEX_ADDRESS);
		this.number = c.getString(FireStationsDB.CINDEX_NUMBER);
		this.latitude = c.getDouble(FireStationsDB.CINDEX_LAT);
		this.longitude = c.getDouble(FireStationsDB.CINDEX_LNG);
	}
	
	@Override
	public ContentValues getContentValues() {
		ContentValues vals = new ContentValues();
		
		vals.put(FireStationsDB.CNAME_ID, id);
		vals.put(FireStationsDB.CNAME_NAME, name);
		vals.put(FireStationsDB.CNAME_ADDRESS, address);
		vals.put(FireStationsDB.CNAME_NUMBER, number);
		vals.put(FireStationsDB.CNAME_LAT, latitude);
		vals.put(FireStationsDB.CNAME_LNG, longitude);
		
		return vals;
	}

	public static FireStation[] convertToArray(Cursor c) {
		FireStation[] stations = new FireStation[c.getCount()];
		
		int i = 0;
		while (c.moveToNext()) {
			stations[i] = new FireStation(c);
			i++;
		}
		
		return stations;
	}

}
