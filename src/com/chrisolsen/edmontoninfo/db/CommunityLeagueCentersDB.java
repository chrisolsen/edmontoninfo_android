package com.chrisolsen.edmontoninfo.db;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chrisolsen.edmontoninfo.models.CommunityLeagueCenter;

import android.content.Context;
import android.util.Log;

public class CommunityLeagueCentersDB extends BaseDB {

	public static final String CNAME_ID 		= "_id";
	public static final String CNAME_NAME 		= "name";
	public static final String CNAME_ADDRESS 	= "address";
	public static final String CNAME_PHONE 		= "phone_number";
	public static final String CNAME_URL 		= "url";
	public static final String CNAME_LAT 		= "latitude";
	public static final String CNAME_LNG 		= "longitude";
	
	
	public static final int CINDEX_ID 		= 0;
	public static final int CINDEX_NAME 	= 1;
	public static final int CINDEX_ADDRESS 	= 2;
	public static final int CINDEX_PHONE 	= 3;
	public static final int CINDEX_URL 		= 4;
	public static final int CINDEX_LAT 		= 5;
	public static final int CINDEX_LNG 		= 6;
	
	public static final String TABLE_CREATE = "create table community_league_centers (" +
			CNAME_ID 		+ " integer primary key," +
			CNAME_NAME 		+ " text," +
			CNAME_ADDRESS 	+ " text," +
			CNAME_PHONE 	+ " text," +
			CNAME_URL 		+ " text," +
			CNAME_LAT 		+ " real," +
			CNAME_LNG 		+ " real )";
	
	public CommunityLeagueCentersDB(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return "community_league_centers";
	}

	@Override
	protected String getColumnHackName() {
		return "name";
	}
	
	public ArrayList<CommunityLeagueCenter> convertFromJSON(String rawJSON) {
		
		try {
			JSONArray jsonArr = new JSONArray(rawJSON);
			ArrayList<CommunityLeagueCenter> stations = new ArrayList<CommunityLeagueCenter>( jsonArr.length() );
			
			for (int i=0; i<jsonArr.length(); i++) {
				JSONObject jObj = jsonArr.getJSONObject(i);
				CommunityLeagueCenter station = new CommunityLeagueCenter();
				
				station.name 		= jObj.getString(CommunityLeagueCentersDB.CNAME_NAME);
				station.address 	= jObj.getString(CommunityLeagueCentersDB.CNAME_ADDRESS);
				station.phoneNumber = jObj.getString(CommunityLeagueCentersDB.CNAME_PHONE);
				station.url 		= jObj.getString(CommunityLeagueCentersDB.CNAME_URL);
				station.latitude 	= jObj.getDouble(CommunityLeagueCentersDB.CNAME_LAT);
				station.longitude 	= jObj.getDouble(CommunityLeagueCentersDB.CNAME_LNG);
				
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
