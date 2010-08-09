package com.chrisolsen.edmontoninfo.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.chrisolsen.edmontoninfo.db.CityEventDB.*;
import static com.chrisolsen.edmontoninfo.Global.*;

import android.content.ContentValues;

public class CityEvent extends BaseModel implements Comparable<CityEvent> {

	public static final int EVENT_ALMOST_OVER 	= 0;
	public static final int EVENT_JUST_STARTED 	= 1;
	public static final int EVENT_UPCOMING 		= 2;
	public static final int EVENT_OVER 			= 3;
	
	public int gid;
	public String name;
	public String note;
	public String location;
	public Date startsAt;
	public Date	endsAt;
	
	public CityEvent() {}
	
	public String getHappensAt() {
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMM d");
		String start = formatter.format(startsAt);
		String end = formatter.format(endsAt);
		
		if ( start.equals(end) )
			return start;
		else
			return start + " - " + end;
	}
	
	public int getStatus() {
		long today = new Date().getTime();
		long start = startsAt.getTime();
		long end   = endsAt.getTime();
		
		if (start > today)
			return EVENT_UPCOMING;
		else if (end < today)
			return EVENT_OVER;
		
		long span  = end - start;
		boolean isHalfOver = ((end - today) / (span * 1.0f)) < 0.5f;
		
		return isHalfOver ? EVENT_ALMOST_OVER : EVENT_JUST_STARTED;
	}
	
	@Override
	public ContentValues getContentValues() {
		ContentValues vals = new ContentValues();
		
		SimpleDateFormat formatter = new SimpleDateFormat(ISO8601);
		
		vals.put(CNAME_ID, id);
		vals.put(CNAME_GID, gid);
		vals.put(CNAME_NAME, name);
		vals.put(CNAME_NOTE, note);
		vals.put(CNAME_LOCATION, location);
		vals.put(CNAME_STARTS_AT, formatter.format(startsAt));
		vals.put(CNAME_ENDS_AT, formatter.format(endsAt));
		
		return vals;
	}

	@Override
	public int compareTo(CityEvent another) {
		int diff = this.getStatus() - another.getStatus();
		
		// return order if they are not the same status level
		if (diff != 0)
			return diff;
		
		// sort based on date if equal status
		return this.startsAt.getTime() - another.startsAt.getTime() < 0 ? -1 : 1;
	}
}
