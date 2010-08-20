package com.chrisolsen.edmontoninfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import static com.chrisolsen.edmontoninfo.Global.*;
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CityEventsActivity extends ListActivity {

	private static final int DIALOG_IMPORT_ID = 0;
	private static final String PREFS = "cityeventprefs";
	private static final String LAST_SYNC_DATE = "last_city_event_sync_date";
	
	protected ProgressDialog dialog;
	
	private ArrayList<CityEvent> cityEvents;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		CityEventDB db = new CityEventDB(this);
		cityEvents = db.getList(null, CNAME_NAME);
		
		if ( cityEvents.size() == 0 )
			showDialog(DIALOG_IMPORT_ID);
		else {
			bindData( cityEvents );
			syncData();
		}
	}
	
	/**
	 * Bind data to listview
	 * @param events
	 */
	private void bindData(ArrayList<CityEvent> events) {
		Collections.sort(events);
		EventAdapter adapter = new EventAdapter(this, events);
		setListAdapter(adapter);
	}
	
	/**
	 * Gets the most recently posted events
	 */
	private void syncData() {
		SyncCityEvents sync = new SyncCityEvents();
		sync.execute( (Void[])null );
	}
	
	/**
	 * List item selection
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//super.onListItemClick(l, v, position, id);
		CityEvent event = this.cityEvents.get(position);
		Intent intent = new Intent(this, CityEventActivity.class);
		intent.putExtra(CityEventActivity.DATA_KEY, event);
		startActivity(intent);
	}

	/**
	 * Show data import progress
	 */
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
	
	private class SyncCityEvents extends AsyncTask<Void, Void, Void> {

		private ArrayList<CityEvent> newEvents;
		
		@Override
		protected Void doInBackground(Void... params) {
			
			SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
			String lastSyncTimeStamp = prefs.getString(LAST_SYNC_DATE, "");
			
			final String url = getString(R.string.import_url_city_events_sync) + "/" + lastSyncTimeStamp;
			
			HttpGet getter = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			CityEventDB db = new CityEventDB(CityEventsActivity.this);
			Cursor cursor = null;
			
			try {
				String json = client.execute(getter, handler);
				newEvents = db.convertFromJson(json);
				
				for (CityEvent e: newEvents) {
					// if event already exists set id attr to allow for an update rather than create
					cursor = db.getCursor(CNAME_GID + " = " + e.gid, null);
					if (cursor.getCount() > 0 && cursor.moveToFirst())
						e.id = cursor.getLong(CINDEX_ID);
					
					db.save(e);
				}
				
				// save new timestamp
				SharedPreferences.Editor prefsEditor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
				SimpleDateFormat formatter = new SimpleDateFormat(ISO8601);
				formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
				
				lastSyncTimeStamp = formatter.format(new Date());
				
				prefsEditor.putString(LAST_SYNC_DATE, lastSyncTimeStamp);
				prefsEditor.commit();
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				db.close();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			setProgressBarIndeterminateVisibility(true);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			if (newEvents.size() > 0) {
				cityEvents.addAll(0, newEvents);
				Collections.sort(cityEvents);
				((BaseAdapter) getListAdapter()).notifyDataSetChanged();
			}

			setProgressBarIndeterminateVisibility(false);
		}
	}

	/**
	 * Background thread to import events
	 */
	private class ImportCityEvents extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			
			final String url = getString(R.string.import_url_city_events); 
			HttpGet getter = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			CityEventDB db = new CityEventDB(CityEventsActivity.this);
			int eventCount = 0;
			
			try {
				String json = client.execute(getter, handler);
				ArrayList<CityEvent> events = db.convertFromJson(json);
				
				eventCount = events.size();
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
			finally {
				db.close();
			}
			
			return eventCount;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int percentComplete = values[0] * 100 / values[1];
			dialog.setMessage(Integer.toString(percentComplete) + "% Complete");
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			
			dismissDialog(DIALOG_IMPORT_ID);
			
			if ( result == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(CityEventsActivity.this);
				builder
					.setNegativeButton("Close", null)
					.setMessage("Unable to connect to the server")
					.show();
			}
			else {
				CityEventDB db = new CityEventDB(CityEventsActivity.this);
				cityEvents = db.getList(null, CityEventDB.CNAME_STARTS_AT);
	
				bindData( cityEvents );
				
				// save timestamp to allow for later syncing of new events			
				SharedPreferences.Editor prefs = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
				SimpleDateFormat formatter = new SimpleDateFormat(ISO8601);
				formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
				String stamp = formatter.format(new Date());
				
				prefs.putString( LAST_SYNC_DATE, stamp );
				prefs.commit();
			}
		}		
	}
	
	/**
	 * Adapter to allow formatting of event start and end dates and
	 * color coding based on the event priority
	 */
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
			
			// event priority colors
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
	
	/**
	 * Wrapper to save the finding of views
	 */
	private class EventWrapper {
		TextView header;
		TextView details;
	}
}
