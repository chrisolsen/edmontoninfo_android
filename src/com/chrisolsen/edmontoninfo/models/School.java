package com.chrisolsen.edmontoninfo.models;

import com.chrisolsen.edmontoninfo.db.SchoolsDB;

import android.content.ContentValues;
import android.database.Cursor;

public class School extends BaseModel {
	
	public String address;
	public String email;
	public String faxNumber;
	public String entityId;
	public String gradeLevel;
	public String name;
	public String phoneNumber;
	public String postalCode;
	public String programs;
	public String timestamp;
	public String school_type;
	public String ward;
	public String url;
	public double latitude;
	public double longitude;
	
	public School() {};
	
	public School(Cursor c) {
		
		this.id = c.getLong( SchoolsDB.CINDEX_ID );
		this.address = c.getString(SchoolsDB.CINDEX_ADDRESS);
		this.email = c.getString(SchoolsDB.CINDEX_EMAIL);
		this.entityId = c.getString(SchoolsDB.CINDEX_ENTITY_ID);
		this.faxNumber = c.getString(SchoolsDB.CINDEX_FAX_NUMBER);
		this.gradeLevel = c.getString(SchoolsDB.CINDEX_GRADE_LEVEL);
		this.latitude = c.getDouble(SchoolsDB.CINDEX_LAT);
		this.longitude = c.getDouble(SchoolsDB.CINDEX_LNG);
		this.name = c.getString(SchoolsDB.CINDEX_NAME);
		this.phoneNumber = c.getString(SchoolsDB.CINDEX_PHONE_NUMBER);
		this.postalCode = c.getString(SchoolsDB.CINDEX_POSTAL_CODE);
		this.programs = c.getString(SchoolsDB.CINDEX_PROGRAMS);
		this.timestamp = c.getString(SchoolsDB.CINDEX_TIMESTAMP);
		this.school_type = c.getString(SchoolsDB.CINDEX_TYPE);
		this.url = c.getString(SchoolsDB.CINDEX_URL);
		this.ward = c.getString(SchoolsDB.CINDEX_WARD);
	}
	
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		
		values.put(SchoolsDB.CNAME_ID, this.id);
		values.put(SchoolsDB.CNAME_ADDRESS, this.address);
		values.put(SchoolsDB.CNAME_EMAIL, this.email);
		values.put(SchoolsDB.CNAME_ENTITY_ID, this.entityId);
		values.put(SchoolsDB.CNAME_FAX_NUMBER, this.faxNumber);
		values.put(SchoolsDB.CNAME_GRADE_LEVEL, this.gradeLevel);
		values.put(SchoolsDB.CNAME_LAT, this.latitude);
		values.put(SchoolsDB.CNAME_LNG, this.longitude);
		values.put(SchoolsDB.CNAME_NAME, this.name);
		values.put(SchoolsDB.CNAME_PHONE_NUMBER, this.phoneNumber);
		values.put(SchoolsDB.CNAME_POSTAL_CODE, this.postalCode);
		values.put(SchoolsDB.CNAME_PROGRAMS, this.programs);
		values.put(SchoolsDB.CNAME_TIMESTAMP, this.timestamp);
		values.put(SchoolsDB.CNAME_TYPE, this.school_type);
		values.put(SchoolsDB.CNAME_URL, this.url);
		values.put(SchoolsDB.CNAME_WARD, this.ward);

		return values;
	}
}
