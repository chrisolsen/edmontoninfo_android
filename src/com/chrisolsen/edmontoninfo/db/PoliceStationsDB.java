package com.chrisolsen.edmontoninfo.db;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chrisolsen.edmontoninfo.models.PoliceStation;

import android.content.Context;
import android.util.Log;

public class PoliceStationsDB extends BaseDB {

	public static final String CNAME_ID 		= "_id";
	public static final String CNAME_NAME 		= "name";
	public static final String CNAME_PHONE 		= "phone_number";
	public static final String CNAME_URL 		= "url";
	public static final String CNAME_LNG 		= "longitude";
	public static final String CNAME_LAT 		= "latitude";
	public static final String CNAME_ADDRESS 	= "address";
	
	public static final int CINDEX_ID 		= 0;
	public static final int CINDEX_NAME 	= 1;
	public static final int CINDEX_PHONE 	= 2;
	public static final int CINDEX_URL 		= 3;
	public static final int CINDEX_LAT 		= 4;
	public static final int CINDEX_LNG 		= 5;
	public static final int CINDEX_ADDRESS 	= 6;
	
	public static final String TABLE_CREATE = "create table police_stations (" +
		CNAME_ID 		+ " integer primary key, " +
		CNAME_NAME 		+ " text default '', " +
		CNAME_PHONE 	+ " text default '', " +
		CNAME_URL 		+ " text default '', " +
		CNAME_LAT 		+ " real default '', " +
		CNAME_LNG		+ " real default '', " +
		CNAME_ADDRESS 	+ " text default '' )";
	
	public PoliceStationsDB(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return "police_stations";
	}

	@Override
	protected String getColumnHackName() {
		return "name";
	}
	
	public ArrayList<PoliceStation> convertFromJSON(String rawJSON) {
		
		try {
			JSONArray jsonArr = new JSONArray(rawJSON);
			ArrayList<PoliceStation> stations = new ArrayList<PoliceStation>( jsonArr.length() );
			
			for (int i=0; i<jsonArr.length(); i++) {
				JSONObject jObj = jsonArr.getJSONObject(i);
				PoliceStation station = new PoliceStation();
				
				station.name = jObj.getString(PoliceStationsDB.CNAME_NAME);
				station.address = jObj.getString(PoliceStationsDB.CNAME_ADDRESS);
				station.phoneNumber = jObj.getString(PoliceStationsDB.CNAME_PHONE);
				station.url = jObj.getString(PoliceStationsDB.CNAME_URL);
				station.latitude = jObj.getDouble(PoliceStationsDB.CNAME_LAT);
				station.longitude = jObj.getDouble(PoliceStationsDB.CNAME_LNG);
				
				stations.add(station);
			}
			
			return stations;
			
		} catch (JSONException e) {
			Log.e("JSON: School import", e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e) {
			Log.e("JSON: School import", e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}

}
