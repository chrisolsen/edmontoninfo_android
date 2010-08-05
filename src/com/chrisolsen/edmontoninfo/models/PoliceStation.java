package com.chrisolsen.edmontoninfo.models;

import java.io.Serializable;

import com.chrisolsen.edmontoninfo.db.PoliceStationsDB;
import android.content.ContentValues;
import android.database.Cursor;

public class PoliceStation extends BaseModel implements Serializable {

	private static final long serialVersionUID = -2295934244978710994L;
	
	public String name;
	public String address;
	public String phoneNumber;
	public String url;
	public double latitude;
	public double longitude;
	
	public PoliceStation() {}
	
	public PoliceStation(Cursor c) {
		this.id = c.getLong(PoliceStationsDB.CINDEX_ID);
		this.name = c.getString(PoliceStationsDB.CINDEX_NAME);
		this.address = c.getString(PoliceStationsDB.CINDEX_ADDRESS);
		this.phoneNumber = c.getString(PoliceStationsDB.CINDEX_PHONE);
		this.url = c.getString(PoliceStationsDB.CINDEX_URL);
		this.latitude = c.getDouble(PoliceStationsDB.CINDEX_LAT);
		this.longitude = c.getDouble(PoliceStationsDB.CINDEX_LNG);
	}
	
	@Override
	public ContentValues getContentValues() {
		ContentValues vals = new ContentValues();
		
		vals.put(PoliceStationsDB.CNAME_ID, id);
		vals.put(PoliceStationsDB.CNAME_NAME, name);
		vals.put(PoliceStationsDB.CNAME_ADDRESS, address);
		vals.put(PoliceStationsDB.CNAME_PHONE, phoneNumber);
		vals.put(PoliceStationsDB.CNAME_URL, url);
		vals.put(PoliceStationsDB.CNAME_LAT, latitude);
		vals.put(PoliceStationsDB.CNAME_LNG, longitude);
		
		return vals;
	}

	public static PoliceStation[] convertToArray(Cursor c) {
		PoliceStation[] stations = new PoliceStation[c.getCount()];
		
		int i = 0;
		while (c.moveToNext()) {
			stations[i] = new PoliceStation(c);
			i++;
		}
		
		return stations;
	}

}
