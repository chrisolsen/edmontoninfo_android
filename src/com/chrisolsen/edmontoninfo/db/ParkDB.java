package com.chrisolsen.edmontoninfo.db;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class ParkDB extends BaseDB {

	public static final String TABLE_NAME = "parks";
	public static final String HACK_COLUMN = "name";
	
	public static final String CNAME_ID 		= "_id";
	public static final String CNAME_NAME 		= "name";
	public static final String CNAME_ADDRESS 	= "address";
	public static final String CNAME_ENTITY_ID 	= "entity_id";
	public static final String CNAME_LATITUDE 	= "latitude";
	public static final String CNAME_LONGITUDE 	= "longitude";
	
	public static final int CINDEX_ID			= 0;
	public static final int CINDEX_NAME			= 1;
	public static final int CINDEX_ADDRESS		= 2;
	public static final int CINDEX_ENTITY_ID	= 3;
	public static final int CINDEX_LATITUDE		= 4;
	public static final int CINDEX_LONGITUDE	= 5;
	
	public static final String TABLE_CREATE = "create table " +
		TABLE_NAME + "(" +
		CNAME_ID 		+ " integer primary key, " +
		CNAME_NAME 		+ " text default '', " +
		CNAME_ADDRESS	+ " text default '', " +
		CNAME_ENTITY_ID	+ " text default '', " +
		CNAME_LATITUDE	+ " real, " +
		CNAME_LONGITUDE + " real)";
	
	public ParkDB(Context context) {
		super(context);
	}
	
	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String getColumnHackName() {
		return HACK_COLUMN;
	}

	public ArrayList<Park> convertFromJson(String rawJson) {
		try {
			ArrayList<Park> parks = new ArrayList<Park>();
			JSONArray json = new JSONArray(rawJson);
			
			for (int i = 0; i < json.length(); i++) {
				
				JSONObject obj = json.getJSONObject(i);
				Park p = new Park();
				
				p.name = obj.getString(CNAME_NAME);
				p.address = obj.getString(CNAME_ADDRESS);
				p.entityId = obj.getString(CNAME_ENTITY_ID);
				p.latitude = obj.getDouble(CNAME_LATITUDE);
				p.longitude = obj.getDouble(CNAME_LONGITUDE);

				parks.add(p);
			}
			
			return parks;
		} catch (JSONException e) {
			Log.e("JSON: Park import", e.getMessage());
		}
		
		return null;
	}
}
