package com.chrisolsen.edmontoninfo.models;

import android.content.ContentValues;

public abstract class BaseModel {

	public long id;
	
	public abstract ContentValues getContentValues();
	
}
