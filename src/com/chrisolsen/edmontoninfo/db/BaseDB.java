package com.chrisolsen.edmontoninfo.db;

import com.chrisolsen.edmontoninfo.models.BaseModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class BaseDB extends SQLiteOpenHelper {

	// Standard Cols	
	public static final String  DATABASE_NAME = "edmontoninfo.db";
	public static final int 	VERSION = 1;
	
	public Cursor cursor;
	
	// Abstract methods
	protected abstract String getTableName();
	protected abstract String getColumnHackName();
	
	public BaseDB(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL( SchoolsDB.TABLE_CREATE );
		db.execSQL( PoliceStationsDB.TABLE_CREATE );
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop database edmontoninfo");
		onCreate(db);
		// if ( newVersion >= 2 )
		//     migrateToVersion_02();
	}
		
	// Lookups
	public Cursor findAll() {
		return findAll(null);
	}
	
	public Cursor findAll(String orderBy) {
		SQLiteDatabase db = getWritableDatabase();
		this.cursor = db.query(getTableName(), null, null, null, null, null, orderBy);
		return this.cursor;
	}
	
	public Cursor findBy(String column, String value) {
		SQLiteDatabase db = getWritableDatabase();
		this.cursor = db.query(getTableName(), 
							null, 
							column + " = " + value, 
							null, null, null, null);

		if ( this.cursor.moveToFirst() )
			return this.cursor;
		return null;
	}
	
	public Cursor findById(long id) {
		return findBy( "_id", Long.toString(id) );
	}
	
	// CRUD
	public long save(BaseModel model) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues vals = model.getContentValues();
		
		long id = model.id;
		if ( id != 0 )
			db.update(getTableName(), vals, "_id = " + Long.toString(id), null);
		else {
			vals.remove("_id");
			id = db.insert(getTableName(), getColumnHackName(), vals);
		}
		
		return id;
	}
	
	public boolean delete(BaseModel model) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(getTableName(), "_id = " + Long.toString(model.id), null);
		
		return false;
	}
	
	@Override
	public void close() {
		if ( this.cursor != null && !this.cursor.isClosed() )
			this.cursor.close();
		super.close();
	}
}
	