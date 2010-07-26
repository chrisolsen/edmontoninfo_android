package com.chrisolsen.edmontoninfo.db;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chrisolsen.edmontoninfo.models.School;

import android.content.Context;
import android.util.Log;

public class SchoolsDB extends BaseDB {
		
	public SchoolsDB(Context context) {
		super(context);
	}
	
	protected String getTableName() {
		return "schools";
	}
	
	protected String getColumnHackName() {
		return SchoolsDB.CNAME_NAME;
	}

	// Column Names
	public static final String CNAME_ID = "_id";
	public static final String CNAME_ADDRESS = "address";	
	public static final String CNAME_EMAIL = "email_address";
	public static final String CNAME_ENTITY_ID = "entityid";
	public static final String CNAME_FAX_NUMBER = "fax_number";
	public static final String CNAME_GRADE_LEVEL = "grade_level";
	public static final String CNAME_LAT = "latitude";
	public static final String CNAME_LNG = "longitude";
	public static final String CNAME_NAME = "school_name";
	public static final String CNAME_PHONE_NUMBER = "phone_number";
	public static final String CNAME_POSTAL_CODE = "postal_code";
	public static final String CNAME_PROGRAMS = "programs";
	public static final String CNAME_TIMESTAMP = "timestamp";
	public static final String CNAME_TYPE = "school_type";
	public static final String CNAME_URL = "website";
	public static final String CNAME_WARD = "ward";
	
	// Column Indices
	public static final int CINDEX_ID = 0;
	public static final int CINDEX_ADDRESS = 1;	
	public static final int CINDEX_EMAIL = 2;
	public static final int CINDEX_ENTITY_ID = 3;
	public static final int CINDEX_FAX_NUMBER = 4;
	public static final int CINDEX_GRADE_LEVEL = 5;
	public static final int CINDEX_LAT = 6;
	public static final int CINDEX_LNG = 7;
	public static final int CINDEX_NAME = 8;
	public static final int CINDEX_PHONE_NUMBER = 9;
	public static final int CINDEX_POSTAL_CODE = 10;
	public static final int CINDEX_PROGRAMS = 11;
	public static final int CINDEX_TIMESTAMP = 12;
	public static final int CINDEX_TYPE = 13;
	public static final int CINDEX_URL = 14;
	public static final int CINDEX_WARD = 15;
	
	public static final String CREATE_TABLE = "create table schools (" +
			CNAME_ID 			+ " integer primary key, " +
			CNAME_ADDRESS 		+ " text default '', " +
			CNAME_EMAIL 		+ " text default '', " +
			CNAME_ENTITY_ID 	+ " text default '', " +
			CNAME_FAX_NUMBER 	+ " text default '', " +
			CNAME_GRADE_LEVEL 	+ " text default '', " +
			CNAME_LAT 			+ " real default '', " +
			CNAME_LNG 			+ " real default '', " +
			CNAME_NAME 			+ " text default '', " +
			CNAME_PHONE_NUMBER	+ " text default '', " +
			CNAME_POSTAL_CODE 	+ " text default '', " +
			CNAME_PROGRAMS 		+ " text default '', " +
			CNAME_TIMESTAMP 	+ " text default '', " +
			CNAME_TYPE 			+ " text default '', " +
			CNAME_URL 			+ " text default '', " +
			CNAME_WARD 			+ " text default ''" + ")";
	
	public ArrayList<School> convertFromJson(String rawJson) {
		try {
			ArrayList<School> schools = new ArrayList<School>();
			JSONArray jsonSchools = new JSONArray(rawJson);
			JSONObject obj;
			School s;
			
			for (int i = 0; i < jsonSchools.length(); i++) {
				obj = jsonSchools.getJSONObject(i);
				s = new School();
				s.address = obj.getString(CNAME_ADDRESS);
				s.email = obj.getString(CNAME_EMAIL);
				s.entityId = obj.getString(CNAME_ENTITY_ID);
				s.faxNumber = obj.getString(CNAME_FAX_NUMBER);
				s.gradeLevel = obj.getString(CNAME_GRADE_LEVEL);
				s.name = obj.getString(CNAME_NAME);
				s.phoneNumber = obj.getString(CNAME_PHONE_NUMBER);
				s.postalCode = obj.getString(CNAME_POSTAL_CODE);
				s.programs = obj.getString(CNAME_PROGRAMS);
				s.timestamp = obj.getString(CNAME_TIMESTAMP);
				s.school_type = obj.getString(CNAME_TYPE);
				s.url = obj.getString(CNAME_URL);
				s.ward = obj.getString(CNAME_WARD);
				s.latitude = obj.getDouble(CNAME_LAT);
				s.longitude = obj.getDouble(CNAME_LNG);
				
				schools.add(s);
			}
			
			return schools;
		} catch (JSONException e) {
			Log.e("JSON: School import", e.getMessage());
		}
		
		return null;
	}
}
