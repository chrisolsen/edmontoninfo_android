package com.chrisolsen.edmontoninfo;

import com.chrisolsen.edmontoninfo.views.ImageTextButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

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
        b.bind(R.drawable.main_city_event_icon, "City Events", TAG_CITY_EVENTS, this);
        
        b = (ImageTextButton)findViewById(R.id.itb_community_league_centers);
        b.bind(R.drawable.main_community_league_center_icon, "Community\nLeague Centres", TAG_COMMUNITY_LEAGUE_CENTERS, this);
        
        b = (ImageTextButton)findViewById(R.id.itb_field_status);
        b.bind(R.drawable.main_field_status_icon, "Field Status", TAG_FIELD_STATUS, this);
        
        b = (ImageTextButton)findViewById(R.id.itb_fire_stations);
        b.bind(R.drawable.main_fire_station_icon, "Fire Stations", TAG_FIRE_STATIONS, this);
        
        b = (ImageTextButton)findViewById(R.id.itb_libraries);
        b.bind(R.drawable.main_library_icon, "Libraries", TAG_LIBRARIES, this);
        
        b = (ImageTextButton)findViewById(R.id.itb_parks);
        b.bind(R.drawable.main_park_icon, "Parks", TAG_PARKS, this);
        
        b = (ImageTextButton)findViewById(R.id.itb_police_stations);
        b.bind(R.drawable.main_police_station_icon, "Police Stations", TAG_POLICE_STATIONS, this);
      
        b = (ImageTextButton)findViewById(R.id.itb_rec_facilities);
        b.bind(R.drawable.main_rec_facility_icon, "Rec Facilities", TAG_REC_FACILITIES, this);
        
        b = (ImageTextButton)findViewById(R.id.itb_schools);
        b.bind(R.drawable.main_school_icon, "Schools", TAG_SCHOOLS, this);
    }
   
	public void onClick(View v) {
    	Intent intent = null;
    	int tag = new Integer(v.getTag().toString());
    	
    	switch( tag ) {
    	case TAG_CITY_EVENTS:
    		intent = new Intent(MainActivity.this, CityEventsActivity.class);
    		break;
    	case TAG_COMMUNITY_LEAGUE_CENTERS:
    		intent = new Intent(MainActivity.this, CommunityLeagueCentersActivity.class);
    		break;
    	case TAG_FIELD_STATUS:
    		intent = new Intent(MainActivity.this, FieldStatusActivity.class);
    		break;
    		
    	case TAG_FIRE_STATIONS:
    		intent = new Intent(MainActivity.this, FireStationsActivity.class);
    		break;
    		
    	case TAG_LIBRARIES:
    		intent = new Intent(MainActivity.this, LibrariesActivity.class);
    		break;

    	case TAG_PARKS:
    		intent = new Intent(MainActivity.this, ParksActivity.class);
    		break;
    		
    	case TAG_POLICE_STATIONS:
    		intent = new Intent(MainActivity.this, PoliceStationsActivity.class);
    		break;
    		
    	case TAG_REC_FACILITIES:
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