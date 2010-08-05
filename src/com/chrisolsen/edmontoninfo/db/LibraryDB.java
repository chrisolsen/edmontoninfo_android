package com.chrisolsen.edmontoninfo.db;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chrisolsen.edmontoninfo.models.Library;
import android.content.Context;
import android.util.Log;

public class LibraryDB extends BaseDB {
	
	private static final String TABLE_NAME 	= "libraries";
	private static final String HACK_COLUMN = "branch";
	
	public static final String CNAME_ID 		= "_id";
	public static final String CNAME_BRANCH 	= "branch";
	public static final String CNAME_PHONE 		= "phone_number";
	public static final String CNAME_ADDRESS 	= "address";
	public static final String CNAME_URL 		= "url";
	public static final String CNAME_LATITUDE	= "latitude";
	public static final String CNAME_LONGITUDE	= "longitude";
	
	public static final int CINDEX_ID 		= 0;
	public static final int CINDEX_BRANCH 	= 1;
	public static final int CINDEX_PHONE 	= 2;
	public static final int CINDEX_ADDRESS 	= 3;
	public static final int CINDEX_URL 		= 4;
	public static final int CINDEX_LATITUDE = 5;
	public static final int CINDEX_LONGITUDE= 6;

	public static final String TABLE_CREATE = "create table " +
			TABLE_NAME + "(" +
			CNAME_ID 		+ " integer primary key, " +
			CNAME_BRANCH 	+ " text default '', " +
			CNAME_PHONE 	+ " text default '', " +
			CNAME_ADDRESS	+ " text default '', " +
			CNAME_URL		+ " text default '', " +
			CNAME_LATITUDE	+ " real, " +
			CNAME_LONGITUDE + " real)";
			
	
	public LibraryDB(Context context) {
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

	public ArrayList<Library> convertFromJson(String rawJson) {
		try {
			ArrayList<Library> libraries = new ArrayList<Library>();
			JSONArray json = new JSONArray(rawJson);
			
			for (int i = 0; i < json.length(); i++) {
				
				JSONObject obj = json.getJSONObject(i);
				Library	l = new Library();
				
				l.address = obj.getString(CNAME_ADDRESS);
				l.branch = obj.getString(CNAME_BRANCH);
				l.phoneNumber = obj.getString(CNAME_PHONE);
				l.url = obj.getString(CNAME_URL);
				l.latitude = obj.getDouble(CNAME_LATITUDE);
				l.longitude = obj.getDouble(CNAME_LONGITUDE);

				libraries.add(l);
			}
			
			return libraries;
		} catch (JSONException e) {
			Log.e("JSON: Library import", e.getMessage());
		}
		
		return null;
	}
}
