package com.chrisolsen.edmontoninfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        
        bindButtons();
    }
    
    private void bindButtons() {
    	// Schools
    	Button btnSchool = (Button)findViewById(R.id.btn_schools);
    	btnSchool.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, SchoolsActivity.class);
				startActivity(i);
			}
		});
    	
    	// Police
    	Button btnPoliceStations = (Button)findViewById(R.id.btn_police_stations);
    	btnPoliceStations.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, PoliceStationsActivity.class);
				startActivity(i);
			}
		});
    	
    	// Fire
    	Button btnFireStations = (Button)findViewById(R.id.btn_fire_stations);
    	btnFireStations.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, FireStationsActivity.class);
				startActivity(i);
			}
		});
    	
    	// Community Leauge Centres
    	Button btnCommunityLeagueCenters = (Button)findViewById(R.id.btn_community_league_centers);
    	btnCommunityLeagueCenters.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, CommunityLeagueCentersActivity.class);
				startActivity(i);
			}
		});
    	
    	// Libraries
    	Button btnLibraries = (Button)findViewById(R.id.btn_libraries);
    	btnLibraries.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, LibrariesActivity.class);
				startActivity(i);
			}
		});
    	
    	// Parks
    	Button btnParks = (Button)findViewById(R.id.btn_parks);
    	btnParks.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, ParksActivity.class);
				startActivity(i);
			}
		});
    	
    	// Rec Facilities
    	Button btnRecFac = (Button)findViewById(R.id.btn_rec_facilities);
    	btnRecFac.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, RecFacilitiesActivity.class);
				startActivity(i);
			}
		});
    	
    	// Field Status
    	Button btnFieldStatus = (Button)findViewById(R.id.btn_field_status);
    	btnFieldStatus.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, FieldStatusActivity.class);
				startActivity(i);
			}
		});
    	
    	// City Events
    	Button btnCityEvents = (Button)findViewById(R.id.btn_city_events);
    	btnCityEvents.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, CityEventsActivity.class);
				startActivity(i);
			}
		});
    }
    
}