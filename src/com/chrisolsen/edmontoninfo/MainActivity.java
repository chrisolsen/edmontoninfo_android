package com.chrisolsen.edmontoninfo;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
    
	public static final int INDEX_SCHOOLS 					= 0;
	public static final int INDEX_POLICE_STATIONS 			= 1;
	public static final int INDEX_FIRE_STATIONS 			= 2;
	public static final int INDEX_COMMUNITY_LEAGUE_CENTRES 	= 3;
	public static final int INDEX_LIBRARIES 				= 4;
	public static final int INDEX_PARKS 					= 5;
	public static final int INDEX_REC_FACILITIES 			= 6;
	public static final int INDEX_FIELD_STATUS 				= 7;
	public static final int INDEX_CITY_EVENTS 				= 8;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		        
        String[] items = new String[] {
        	"Schools",
        	"Police Stations",
        	"Fire Stations",
        	"Community League Centres",
        	"Libraries",
        	"Parks",
        	"Rec Facilities",
        	"Field Status",
        	"City Events"
        };
        
        int[] icons = new int[] {
        	R.drawable.baseball,
        	R.drawable.police,
        	R.drawable.baseball,
        	R.drawable.baseball,
        	R.drawable.baseball,
        	R.drawable.baseball,
        	R.drawable.baseball,
        	R.drawable.baseball,
        	R.drawable.baseball
        };
        
        ListAdapter adapter = new ListAdapter(this, items, icons);
        setListAdapter(adapter);
        
        // remove borders
        ListView listView = getListView();
        listView.setDividerHeight(0);
    }
    
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	
    	Intent intent = null;
    	
    	switch(position) {
    	case INDEX_CITY_EVENTS:
    		intent = new Intent(MainActivity.this, CityEventsActivity.class);
    		break;
    	case INDEX_COMMUNITY_LEAGUE_CENTRES:
    		intent = new Intent(MainActivity.this, CommunityLeagueCentersActivity.class);
    		break;
    	case INDEX_FIELD_STATUS:
    		intent = new Intent(MainActivity.this, FieldStatusActivity.class);
    		break;
    		
    	case INDEX_FIRE_STATIONS:
    		intent = new Intent(MainActivity.this, FireStationsActivity.class);
    		break;
    		
    	case INDEX_LIBRARIES:
    		intent = new Intent(MainActivity.this, LibrariesActivity.class);
    		break;
    		
    	case INDEX_PARKS:
    		intent = new Intent(MainActivity.this, ParksActivity.class);
    		break;
    		
    	case INDEX_POLICE_STATIONS:
    		intent = new Intent(MainActivity.this, PoliceStationsActivity.class);
    		break;
    		
    	case INDEX_REC_FACILITIES:
    		intent = new Intent(MainActivity.this, RecFacilitiesActivity.class);
    		break;
    		
    	case INDEX_SCHOOLS:
    		intent = new Intent(MainActivity.this, SchoolsActivity.class);
    		break;
    	}
    	
    	if (intent != null)
    		startActivity(intent);
	}

	private class ListAdapter extends ArrayAdapter<String> {

    	private String[] items;
        private int[] icons;
    	
    	public ListAdapter(Context context, String[] items, int[] icons) {
			super(context, 1, items);
			
			this.items = items;
			this.icons = icons;
    	}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = convertView;
			
			if ( convertView == null ) {
				view = getLayoutInflater().inflate(R.layout.list_view_icon_row, null);
			}
			
			String itemVal 	= this.items[position];
			int iconVal		= this.icons[position];
			
			TextView itemView 	= (TextView)view.findViewById(R.id.list_view_image_text);
			ImageView iconView 	= (ImageView)view.findViewById(R.id.list_view_image_icon);
			
			itemView.setText( itemVal );
			iconView.setImageDrawable( getResources().getDrawable(iconVal));
			
			return view;
		}
    }
}