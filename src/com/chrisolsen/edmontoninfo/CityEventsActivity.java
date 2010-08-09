package com.chrisolsen.edmontoninfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.chrisolsen.edmontoninfo.db.CityEventDB.*;
import static com.chrisolsen.edmontoninfo.models.CityEvent.*;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import com.chrisolsen.edmontoninfo.db.CityEventDB;
import com.chrisolsen.edmontoninfo.models.CityEvent;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CityEventsActivity extends ListActivity {

	private static final int DIALOG_IMPORT_ID = 0;
	protected ProgressDialog dialog;
	
	private ArrayList<CityEvent> cityEvents;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		CityEventDB db = new CityEventDB(this);
		cityEvents = db.getList(CNAME_NAME);
		
		if ( cityEvents.size() == 0 )
			showDialog(DIALOG_IMPORT_ID);
		else
			bindData( cityEvents );
	}
	
	private void bindData(ArrayList<CityEvent> events) {
		Collections.sort(events);
		EventAdapter adapter = new EventAdapter(this, events);
		setListAdapter(adapter);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		
		if (id == DIALOG_IMPORT_ID) {
			dialog = new ProgressDialog(CityEventsActivity.this);
			dialog.setTitle("Importing City Events");
			dialog.setCancelable(false);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			
			ImportCityEvents importData = new ImportCityEvents();
			importData.execute( (Void[])null );
			
			return dialog;
		}
		
		return super.onCreateDialog(id);
	}

	private class ImportCityEvents extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			
			final String url = getString(R.string.import_url_city_events); 
			HttpGet getter = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			CityEventDB db = new CityEventDB(CityEventsActivity.this);
			
			try {
				String json = client.execute(getter, handler);
				ArrayList<CityEvent> events = db.convertFromJson(json);
				
				int eventCount = events.size();
				int currentIndex = 0;
				
				for (CityEvent e: events) {
					db.save(e);
					currentIndex++;
					
					publishProgress( new Integer(currentIndex), new Integer(eventCount) );	
				}
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int percentComplete = values[0] * 100 / values[1];
			dialog.setMessage(Integer.toString(percentComplete) + "% Complete");
		}
		
		@Override
		protected void onPostExecute(Void result) {
			dismissDialog(DIALOG_IMPORT_ID);
			
			CityEventDB db = new CityEventDB(CityEventsActivity.this);
			cityEvents = db.getList(CityEventDB.CNAME_STARTS_AT);

			bindData( cityEvents );
		}		
	}
	
	private class EventAdapter extends ArrayAdapter<CityEvent>  {

		private List<CityEvent> cityEvents;
		
		public EventAdapter(Context context, List<CityEvent> cityEvents) {
			super(context, R.layout.listview_row, cityEvents);
			this.cityEvents = cityEvents;
		}

		public View getView( int position, View convertView, ViewGroup parent) {
			
			View row = convertView;
			EventWrapper wrapper;
			CityEvent event = this.cityEvents.get(position);
			
			if (convertView == null) {
				row = getLayoutInflater().inflate(R.layout.listview_row, null);
				
				// textview refs for layout
				TextView header = (TextView)row.findViewById(android.R.id.text1);
				TextView details = (TextView)row.findViewById(android.R.id.text2);
				
				wrapper = new EventWrapper();
				wrapper.header = header;
				wrapper.details = details;
				row.setTag(wrapper);
			}
			else {
				wrapper = (EventWrapper)row.getTag();
			}
			
			int colorId = 0;
			switch (event.getStatus()) {
			case EVENT_ALMOST_OVER:
				colorId = R.color.event_almost_over;
				break;
			case EVENT_JUST_STARTED:
				colorId = R.color.event_just_started;
				break;
			case EVENT_OVER:
				colorId = R.color.event_over;
				break;
			case EVENT_UPCOMING:
				colorId = R.color.event_upcoming;
				break;
			}
			
			row.setBackgroundColor( getResources().getColor(colorId) );
			
			// set row displayed details
			wrapper.header.setText(event.name);
			wrapper.details.setText(event.getHappensAt());
			
			return row;
		}
	}
	
	private class EventWrapper {
		TextView header;
		TextView details;
	}
}
