package com.chrisolsen.edmontoninfo.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.chrisolsen.edmontoninfo.Global.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.chrisolsen.edmontoninfo.models.CityEvent;

public class CityEventDB extends BaseDB {

	public CityEventDB(Context context) {
		super(context);
	}

	public static final String TABLE_NAME 	= "city_events";
	public static final String HACK_COLUMN 	= "name";
	
	public static final String CNAME_ID 		= "_id";
	public static final String CNAME_GID		= "id";
	public static final String CNAME_NAME 		= "name";
	public static final String CNAME_LOCATION 	= "location";
	public static final String CNAME_NOTE 		= "note";
	public static final String CNAME_STARTS_AT 	= "starts_at";
	public static final String CNAME_ENDS_AT 	= "ends_at";
	public static final String CNAME_CREATED_AT = "created_at";
	public static final String CNAME_UPDATED_AT = "updated_at";
	
	public static final int CINDEX_ID 			= 0;
	public static final int CINDEX_GID 			= 1;
	public static final int CINDEX_NAME 		= 2;
	public static final int CINDEX_LOCATION 	= 3;
	public static final int CINDEX_NOTE 		= 4;
	public static final int CINDEX_STARTS_AT 	= 5;
	public static final int CINDEX_ENDS_AT 		= 6;
	public static final int CINDEX_CREATED_AT 	= 7;
	public static final int CINDEX_UPDATED_AT 	= 8;
	
	
	public static final String TABLE_CREATE = "create table " +
		TABLE_NAME 			+ " (" +
		CNAME_ID 			+ " integer primary key, " +
		CNAME_GID 			+ " integer, " +
		CNAME_NAME 			+ " text, " +
		CNAME_LOCATION 		+ " text, " +
		CNAME_NOTE 			+ " text, " +
		CNAME_STARTS_AT 	+ " numeric, " +
		CNAME_ENDS_AT   	+ " numeric, " +
		CNAME_CREATED_AT 	+ " numeric, " +
		CNAME_UPDATED_AT 	+ " numeric )";
	
	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String getColumnHackName() {
		return HACK_COLUMN;
	}
	
	public ArrayList<CityEvent> getList(String where, String orderBy) {
		
		Cursor c = this.getCursor(where, orderBy);
		ArrayList<CityEvent> list = new ArrayList<CityEvent>();
		SimpleDateFormat formatter = new SimpleDateFormat(ISO8601);
		
		while(c.moveToNext()) {
			try {
				CityEvent e = new CityEvent();
				e.id 		= c.getLong(CINDEX_ID);
				e.gid 		= c.getInt(CINDEX_GID);
				e.name 		= c.getString(CINDEX_NAME);
				e.note 		= c.getString(CINDEX_NOTE);
				e.location 	= c.getString(CINDEX_LOCATION);
				e.startsAt 	= formatter.parse( c.getString(CINDEX_STARTS_AT) );
				e.endsAt 	= formatter.parse( c.getString(CINDEX_ENDS_AT) );
				e.createdAt = formatter.parse( c.getString(CINDEX_CREATED_AT) );
				e.updatedAt = formatter.parse( c.getString(CINDEX_UPDATED_AT) );
				
				list.add(e);
				
			} catch (ParseException e1) {
				// do nothing -> bad data
				e1.printStackTrace();
			}
		}
		
		close();
		
		return list;
	}

	public ArrayList<CityEvent> convertFromJson(String rawJson) {
		try {
			ArrayList<CityEvent> events = new ArrayList<CityEvent>();
			JSONArray jsonArr = new JSONArray(rawJson);
			
			for (int i = 0; i < jsonArr.length(); i++) {
				
				JSONObject obj = jsonArr.getJSONObject(i);
				CityEvent e = new CityEvent();
				
				SimpleDateFormat formatter = new SimpleDateFormat(ISO8601); // 2010-08-06T15:00:03+00:00

				// ignore event if unable to parse dates
				try {
					e.gid 		= obj.getInt(CNAME_GID);
					e.name 		= obj.getString(CNAME_NAME);
					e.note 		= obj.getString(CNAME_NOTE);
					e.location 	= obj.getString(CNAME_LOCATION);
					e.startsAt 	= formatter.parse( obj.getString(CNAME_STARTS_AT) );
					e.endsAt 	= formatter.parse( obj.getString(CNAME_ENDS_AT) );
					e.createdAt = formatter.parse( obj.getString(CNAME_CREATED_AT) );
					e.updatedAt = formatter.parse( obj.getString(CNAME_UPDATED_AT) );
					
					// fixes
					if (e.note == "null") e.note = "";
					if (e.location == "null") e.location = "";
					
					e.location = e.location.replace("\\", "");
					
					events.add(e);
				} 
				catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			
			return events;
			
		} catch (JSONException e) {
			Log.e("JSON: City Event import", e.getMessage());
		}
		
		return null;
	}
}
