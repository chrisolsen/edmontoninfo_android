package com.chrisolsen.edmontoninfo;

import java.util.ArrayList;

import com.chrisolsen.edmontoninfo.adapters.NameValue;
import com.chrisolsen.edmontoninfo.adapters.TableAdapter;
import com.chrisolsen.edmontoninfo.adapters.TableAdapter.RowLayout;
import com.chrisolsen.edmontoninfo.models.CityEvent;

import android.app.ListActivity;
import android.os.Bundle;

public class CityEventActivity extends ListActivity {

	public static final String DATA_KEY = "CityEventActivityDataKey";
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		CityEvent event = (CityEvent)getIntent().getSerializableExtra(DATA_KEY);
		
		ArrayList<NameValue> items = new ArrayList<NameValue>();
		if (event.name.trim() != "") 		
			items.add( new NameValue("Name", event.name) );
		if (event.location.trim() != "") 	
			items.add( new NameValue("Location", event.location) );
		if (event.note.trim() != "") 		
			items.add( new NameValue("Note", event.note.replace("\\n", "\n").replace("\\,", ",") ));
		if (event.startsAt != null) 		
			items.add( new NameValue("Start", event.getHumanizedStartsAt()) );
		if (event.endsAt != null) 	
			items.add( new NameValue("End", event.getHumanizedEndsAt()) );
		
		TableAdapter adapter = new TableAdapter(this, RowLayout.VERTICAL, items);
		
		setListAdapter(adapter);
	}
}
