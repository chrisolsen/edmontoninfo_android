package com.chrisolsen.edmontoninfo;

import android.app.ListActivity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	private static final int MENU_ABOUT = 99;
	
	private static final int TAG_CITY_EVENTS 				= 0;
	private static final int TAG_SCHOOLS 					= 1;
	private static final int TAG_REC_FACILITIES 			= 2;
	private static final int TAG_POLICE_STATIONS 			= 3;
	private static final int TAG_PARKS 						= 4;
	private static final int TAG_LIBRARIES 					= 5;
	private static final int TAG_FIRE_STATIONS 				= 6;
	private static final int TAG_FIELD_STATUS 				= 7;
	private static final int TAG_COMMUNITY_LEAGUE_CENTERS 	= 8;
	
	private static final String[] items = new String[] {
		"City Events",
		"Schools",
		"Rec Facilities",
		"Police Stations",
		"Fire Stations",
		"Parks",
		"Libraries",
		"Field Status",
		"Community League Centres"
	};
	
	private static final int[] imageIds = new int[] {
		R.drawable.event,
		R.drawable.books_and_apple,
		R.drawable.rec_facility,
		R.drawable.police,
		R.drawable.fire_helmet,
		R.drawable.trees,
		R.drawable.books,
		R.drawable.baseball,
		R.drawable.house
	};
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		ListAdapter adapter = new ListAdapter(this);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	
		Intent intent = null;
		
		switch( position ) {
    	case TAG_CITY_EVENTS:
    		intent = new Intent(this, CityEventsActivity.class);
    		break;
    		
    	case TAG_COMMUNITY_LEAGUE_CENTERS:
    		intent = new Intent(this, CommunityLeagueCentersActivity.class);
    		break;
    		
    	case TAG_FIELD_STATUS:
    		intent = new Intent(this, FieldStatusActivity.class);
    		break;
    		
    	case TAG_FIRE_STATIONS:
    		intent = new Intent(this, FireStationsActivity.class);
    		break;
    		
    	case TAG_LIBRARIES:
    		intent = new Intent(this, LibrariesActivity.class);
    		break;

    	case TAG_PARKS:
    		intent = new Intent(this, ParksActivity.class);
    		break;
    		
    	case TAG_POLICE_STATIONS:
    		intent = new Intent(this, PoliceStationsActivity.class);
    		break;
    		
    	case TAG_REC_FACILITIES:
    		intent = new Intent(this, RecFacilitiesActivity.class);
    		break;
    		
    	case TAG_SCHOOLS:
    		intent = new Intent(this, SchoolsActivity.class);
    		break;
		}
    		
    	if ( intent != null )
    		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		super.onCreateOptionsMenu(menu);

		MenuItem menuItem = menu.add(Menu.NONE, MENU_ABOUT, Menu.NONE, R.string.about);
		menuItem.setIcon(R.drawable.about);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch ( item.getItemId() ) {
		case MENU_ABOUT:
			Intent i = new Intent(MainActivity.this, AboutActivity.class);
			startActivity(i);
			break;
		}
		return true;
	}

	private class ListAdapter extends ArrayAdapter<String> {

		public ListAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_1, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		
			View row = convertView;
			if ( row == null ) {
				LayoutInflater inflater = (LayoutInflater)getSystemService(Service.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.listview_row_icon, null);
			}
			
			TextView textView 	= (TextView)row.findViewById( android.R.id.text1 );
			ImageView imageView = (ImageView)row.findViewById( android.R.id.icon );
			
			textView.setText( items[position] );
			imageView.setBackgroundResource( imageIds[position] );
			
			return row;
		}
		
	}
}
