package com.chrisolsen.edmontoninfo.models;

import java.io.Serializable;

import com.chrisolsen.edmontoninfo.db.CommunityLeagueCentersDB;
import android.content.ContentValues;
import android.database.Cursor;

public class CommunityLeagueCenter extends BaseModel implements Serializable {

	private static final long serialVersionUID = -7382859815175743500L;
	
	public String name;
	public String address;
	public String phoneNumber;
	public String url;
	public double latitude;
	public double longitude;
	
	public CommunityLeagueCenter() {}
	
	public CommunityLeagueCenter(Cursor c) {
		this.id = c.getLong(CommunityLeagueCentersDB.CINDEX_ID);
		this.name = c.getString(CommunityLeagueCentersDB.CINDEX_NAME);
		this.address = c.getString(CommunityLeagueCentersDB.CINDEX_ADDRESS);
		this.phoneNumber = c.getString(CommunityLeagueCentersDB.CINDEX_PHONE);
		this.url = c.getString(CommunityLeagueCentersDB.CINDEX_URL);
		this.latitude = c.getDouble(CommunityLeagueCentersDB.CINDEX_LAT);
		this.longitude = c.getDouble(CommunityLeagueCentersDB.CINDEX_LNG);
	}
	
	@Override
	public ContentValues getContentValues() {
		ContentValues vals = new ContentValues();
		
		vals.put(CommunityLeagueCentersDB.CNAME_ID, id);
		vals.put(CommunityLeagueCentersDB.CNAME_NAME, name);
		vals.put(CommunityLeagueCentersDB.CNAME_ADDRESS, address);
		vals.put(CommunityLeagueCentersDB.CNAME_PHONE, phoneNumber);
		vals.put(CommunityLeagueCentersDB.CNAME_URL, url);
		vals.put(CommunityLeagueCentersDB.CNAME_LAT, latitude);
		vals.put(CommunityLeagueCentersDB.CNAME_LNG, longitude);
		
		return vals;
	}

	public static CommunityLeagueCenter[] convertToArray(Cursor c) {
		CommunityLeagueCenter[] centers = new CommunityLeagueCenter[c.getCount()];
		
		int i = 0;
		while (c.moveToNext()) {
			centers[i] = new CommunityLeagueCenter(c);
			i++;
		}
		
		return centers;
	}

}
