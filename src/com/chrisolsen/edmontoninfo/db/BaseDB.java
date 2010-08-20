package com.chrisolsen.edmontoninfo.db;

import com.chrisolsen.edmontoninfo.models.BaseModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BaseDB extends SQLiteOpenHelper {

	public static final String  DATABASE_NAME = "edmontoninfo.db";
	public static final String 	ID = "_id";
	public static final int 	VERSION = 1;
	
	public Cursor cursor;
	
	protected abstract String getTableName();
	protected abstract String getColumnHackName();
	
	public BaseDB(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL( SchoolsDB.TABLE_CREATE );
		db.execSQL( PoliceStationsDB.TABLE_CREATE );
		db.execSQL( FireStationsDB.TABLE_CREATE );
		db.execSQL( CommunityLeagueCentersDB.TABLE_CREATE );
		db.execSQL( LibraryDB.TABLE_CREATE );
		db.execSQL( ParkDB.TABLE_CREATE );
		db.execSQL( RecFacilityDB.TABLE_CREATE );
		db.execSQL( CityEventDB.TABLE_CREATE );
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop database edmontoninfo");
		onCreate(db);
		// if ( newVersion >= 2 )
		//     migrateToVersion_02();
	}
		
	// Lookups
	public Cursor getCursor(String where, String orderBy) {
		//Log.d("SQL", "select * from " + getTableName() + " where " + where);
		
		SQLiteDatabase db = getWritableDatabase();
		this.cursor = db.query(getTableName(), null, where, null, null, null, orderBy);
		return this.cursor;
	}

	public Cursor getCursor(long id) {
		return getCursor( ID + "" + Long.toString(id) , null);
	}
	
	// CRUD
	public long save(BaseModel model) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues vals = model.getContentValues();
		
		long id = model.id;
		if ( id != 0 ) {
			//Log.d("SQL", "update " + getTableName() + " where id = " + model.id);
			
			db.update(getTableName(), vals, ID + " = " + Long.toString(id), null);
		}
		else {
			//Log.d("SQL", "insert into " + getTableName());
			
			vals.remove(ID);
			id = db.insert(getTableName(), getColumnHackName(), vals);
		}
		
		return id;
	}
	
	public boolean delete(BaseModel model) {
		//Log.d("SQL", "delete from " + getTableName() + " where " + model.id);
		
		SQLiteDatabase db = getWritableDatabase();
		db.delete(getTableName(), ID + " = " + Long.toString(model.id), null);
		
		return false;
	}
	
	@Override
	public void close() {
		if ( this.cursor != null && !this.cursor.isClosed() )
			this.cursor.close();
		super.close();
	}
}
	