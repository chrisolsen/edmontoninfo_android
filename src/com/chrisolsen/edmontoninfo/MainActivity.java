package com.chrisolsen.edmontoninfo;

import com.chrisolsen.edmontoninfo.views.ImageTextButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener {
    	
    private static final int TAG_SCHOOLS 					= 0;
	private static final int TAG_REC_FACILITIES 			= 1;
	private static final int TAG_POLICE_STATIONS 			= 2;
	private static final int TAG_PARKS 						= 3;
	private static final int TAG_LIBRARIES 					= 4;
	private static final int TAG_FIRE_STATIONS 				= 5;
	private static final int TAG_FIELD_STATUS 				= 6;
	private static final int TAG_COMMUNITY_LEAGUE_CENTERS 	= 7;
	private static final int TAG_CITY_EVENTS 				= 8;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ImageTextButton b;
        
        b = (ImageTextButton)findViewById(R.id.itb_city_events);
        b.image.setBackgroundResource(R.drawable.main_city_event_icon);
        b.text.setText("City Events");
        b.tag = TAG_CITY_EVENTS;
        b.setOnClickListener(this);
        
        b = (ImageTextButton)findViewById(R.id.itb_community_league_centers);
        b.image.setBackgroundResource(R.drawable.main_community_league_center_icon);
        b.text.setText("Community\nLeague Centres");
        b.tag = TAG_COMMUNITY_LEAGUE_CENTERS;
        b.setOnClickListener(this);
        
        b = (ImageTextButton)findViewById(R.id.itb_field_status);
        b.image.setBackgroundResource(R.drawable.main_field_status_icon);
        b.text.setText("Field Status");
        b.tag = TAG_FIELD_STATUS;
        b.setOnClickListener(this);
        
        b = (ImageTextButton)findViewById(R.id.itb_fire_stations);
        b.image.setBackgroundResource(R.drawable.main_fire_station_icon);
        b.text.setText("Fire Stations");
        b.tag = TAG_FIRE_STATIONS;
        b.setOnClickListener(this);
        
        b = (ImageTextButton)findViewById(R.id.itb_libraries);
        b.image.setBackgroundResource(R.drawable.main_library_icon);
        b.text.setText("Libraries");
        b.tag = TAG_LIBRARIES;
        b.setOnClickListener(this);
        
        b = (ImageTextButton)findViewById(R.id.itb_parks);
        b.image.setBackgroundResource(R.drawable.main_park_icon);
        b.text.setText("Parks");
        b.tag = TAG_PARKS;
        b.setOnClickListener(this);
        
        b = (ImageTextButton)findViewById(R.id.itb_police_stations);
        b.image.setBackgroundResource(R.drawable.main_police_station_icon);
        b.text.setText("Police Stations");
        b.tag = TAG_POLICE_STATIONS;
        b.setOnClickListener(this);
        
        b = (ImageTextButton)findViewById(R.id.itb_rec_facilities);
        b.image.setBackgroundResource(R.drawable.main_rec_facility_icon);
        b.text.setText("Rec Facilities");
        b.tag = TAG_REC_FACILITIES;
        b.setOnClickListener(this);
        
        b = (ImageTextButton)findViewById(R.id.itb_schools);
        b.image.setBackgroundResource(R.drawable.main_school_icon);
        b.text.setText("Schools");
        b.tag = TAG_SCHOOLS;
        b.setOnClickListener(this);
    }
   
	public void onClick(View v) {
    	Intent intent = null;
    	int tag = new Integer(v.getTag().toString());
    	
    	switch( tag ) {
    	case R.id.itb_city_events:
    		intent = new Intent(MainActivity.this, CityEventsActivity.class);
    		break;
    	case R.id.itb_community_league_centers:
    		intent = new Intent(MainActivity.this, CommunityLeagueCentersActivity.class);
    		break;
    	case R.id.itb_field_status:
    		intent = new Intent(MainActivity.this, FieldStatusActivity.class);
    		break;
    		
    	case R.id.itb_fire_stations:
    		intent = new Intent(MainActivity.this, FireStationsActivity.class);
    		break;
    		
    	case R.id.itb_libraries:
    		intent = new Intent(MainActivity.this, LibrariesActivity.class);
    		break;

    	case R.id.itb_parks:
    		intent = new Intent(MainActivity.this, ParksActivity.class);
    		break;
    		
    	case R.id.itb_police_stations:
    		intent = new Intent(MainActivity.this, PoliceStationsActivity.class);
    		break;
    		
    	case R.id.itb_rec_facilities:
    		intent = new Intent(MainActivity.this, RecFacilitiesActivity.class);
    		break;
    		
    	case TAG_SCHOOLS:
    		intent = new Intent(MainActivity.this, SchoolsActivity.class);
    		break;
    	default:
    		Log.d("Invalid Parent", tag + "");
    	}
    	
    	if (intent != null)
    		startActivity(intent);
	}

}