package com.chrisolsen.edmontoninfo.models;

import java.io.Serializable;

import com.chrisolsen.edmontoninfo.db.LibraryDB;
import android.content.ContentValues;
import android.database.Cursor;

public class Library extends BaseModel implements Serializable {

	private static final long serialVersionUID = -9177889326246337400L;
	
	public String branch;
	public String phoneNumber;
	public String address;
	public String url;
	public double latitude;
	public double longitude;
	
	public Library() {}

	public Library(Cursor c) {
		this.branch 		= c.getString(LibraryDB.CINDEX_BRANCH);
		this.phoneNumber 	= c.getString(LibraryDB.CINDEX_PHONE);
		this.address 		= c.getString(LibraryDB.CINDEX_ADDRESS);
		this.url 			= c.getString(LibraryDB.CINDEX_URL);
		this.latitude 		= c.getDouble(LibraryDB.CINDEX_LATITUDE);
		this.longitude 		= c.getDouble(LibraryDB.CINDEX_LONGITUDE);
	}
	
	@Override
	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		
		values.put(LibraryDB.CNAME_BRANCH, this.branch);
		values.put(LibraryDB.CNAME_PHONE, this.phoneNumber);
		values.put(LibraryDB.CNAME_ADDRESS, this.address);
		values.put(LibraryDB.CNAME_URL, this.url);
		values.put(LibraryDB.CNAME_LATITUDE, this.latitude);
		values.put(LibraryDB.CNAME_LONGITUDE, this.longitude);
		
		return values;
	}
	
	public static Library[] convertToArray( Cursor c) {
		Library[] libraries = new Library[c.getCount()];
		int index = 0;

		while ( c.moveToNext() ) {
			libraries[index] = new Library(c);
			index++;
		}
		
		return libraries;
	}
}
