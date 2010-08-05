package com.chrisolsen.edmontoninfo.db;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chrisolsen.edmontoninfo.models.RecFacility;

import android.content.Context;
import android.util.Log;

public class RecFacilityDB extends BaseDB {

	public static final String TABLE_NAME = "rec_facilities";
	public static final String HACK_COLUMN = "name";

	public static final String CNAME_ID 		= "_id";
	public static final String CNAME_NAME 		= "name";
	public static final String CNAME_ADDRESS 	= "address";
	public static final String CNAME_TYPE 		= "type";
	public static final String CNAME_ENTITY_ID 	= "entityid";
	public static final String CNAME_PHONE 		= "phone_number";
	public static final String CNAME_URL 		= "url";
	public static final String CNAME_LATITUDE 	= "latitude";
	public static final String CNAME_LONGITUDE 	= "longitude";

	public static final int CINDEX_ID 			= 0;
	public static final int CINDEX_NAME 		= 1;
	public static final int CINDEX_ADDRESS 		= 2;
	public static final int CINDEX_TYPE 		= 3;
	public static final int CINDEX_ENTITY_ID 	= 4;
	public static final int CINDEX_PHONE 		= 5;
	public static final int CINDEX_LATITUDE 	= 6;
	public static final int CINDEX_LONGITUDE 	= 7;

	public static final String TABLE_CREATE = "create table " + 
		TABLE_NAME 		+ " (" + 
		CNAME_ID 		+ " integer primary key, " + 
		CNAME_NAME 		+ " text, " + 
		CNAME_ADDRESS 	+ " text, " + 
		CNAME_TYPE 		+ " text, " + 
		CNAME_ENTITY_ID + " text, " + 
		CNAME_PHONE 	+ " text, " + 
		CNAME_LATITUDE 	+ " real, " + 
		CNAME_LONGITUDE + " real )";

	public RecFacilityDB(Context context) {
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

	public ArrayList<RecFacility> convertFromJson(String rawJson) {
		try {
			ArrayList<RecFacility> facilities = new ArrayList<RecFacility>();
			JSONArray json = new JSONArray(rawJson);

			for (int i = 0; i < json.length(); i++) {

				JSONObject obj = json.getJSONObject(i);
				RecFacility r = new RecFacility();

				r.name 		= obj.getString(CNAME_NAME);
				r.address 	= obj.getString(CNAME_ADDRESS);
				r.type 		= obj.getString(CNAME_TYPE);
				r.phone 	= obj.getString(CNAME_PHONE);
				r.entityId 	= obj.getString(CNAME_ENTITY_ID);
				r.latitude 	= obj.getDouble(CNAME_LATITUDE);
				r.longitude = obj.getDouble(CNAME_LONGITUDE);

				facilities.add(r);
			}

			return facilities;
		} catch (JSONException e) {
			Log.e("JSON: Rec Facility import", e.getMessage());
		}

		return null;
	}

}
