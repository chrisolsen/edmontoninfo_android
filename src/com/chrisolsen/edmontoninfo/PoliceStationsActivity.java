package com.chrisolsen.edmontoninfo;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.chrisolsen.edmontoninfo.db.PoliceStationsDB;
import com.chrisolsen.edmontoninfo.models.PoliceStation;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PoliceStationsActivity extends ListActivity {

	private static final long serialVersionUID = -6655391771139565324L;

	private static final int DIALOG_IMPORT_DATA = 0;
	
	private ProgressDialog importDialog;
	private PoliceStationsDB db = new PoliceStationsDB(this);
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		// bind police stations if they do exist
		if ( bindStations() == 0 )
			showDialog(DIALOG_IMPORT_DATA);
	}

	/**
	 * Show the progress dialog when importing the data
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_IMPORT_DATA:
			importDialog = new ProgressDialog(PoliceStationsActivity.this);
			importDialog.setCancelable(false);
			importDialog.setMessage("Importing Police Stations...");
			importDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			
			ImportDataAsyncTask importThread = new ImportDataAsyncTask();
			importThread.execute( (Void[])null );
			
			return importDialog;
		}
		
		return null;
	}
	
	/**
	 * Bind police stations and return the count of existing stations
	 * @return
	 * 	Police station count
	 */
	private int bindStations() {
		this.db = new PoliceStationsDB(this);
		Cursor c = db.findAll(PoliceStationsDB.CNAME_NAME);
		String[] from = new String[] { PoliceStationsDB.CNAME_NAME, PoliceStationsDB.CNAME_ADDRESS };
		int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listview_row, c, from, to);
		
		ListView listView = getListView();
		
		// bind header and footer
		if (c.getCount() > 0) {
			View header = getLayoutInflater().inflate(R.layout.listview_header, null);
			TextView viewMap = (TextView)header.findViewById(R.id.listview_header_text);
			viewMap.setText( String.format("View Map (%d Stations)", c.getCount()) );
			
			viewMap.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(PoliceStationsActivity.this, PoliceStationMapActivity.class);
					startActivity(intent);
				}
			});
			
			listView.addHeaderView(header);
			listView.addFooterView(header);
			listView.setAdapter(adapter);
			
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
					db.cursor.moveToPosition(position);
					PoliceStation station = new PoliceStation(db.cursor);
					
					Intent i = new Intent(PoliceStationsActivity.this, PoliceStationMapActivity.class);
					i.putExtra(PoliceStationMapActivity.POLICE_STATION, station);
					
					startActivity(i);
				}
			});
		}
		
		return c.getCount();
	}
	
	/**
	 * Import the external data in background thread
	 * @author chris
	 *
	 */
	private class ImportDataAsyncTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet( getString(R.string.import_url_police_stations) );
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			PoliceStationsDB db = new PoliceStationsDB(PoliceStationsActivity.this);
			
			try {
				String rawJSON = client.execute(request, handler);
				ArrayList<PoliceStation> stations = db.convertFromJSON(rawJSON);
				
				int totalCount = stations.size();
				int currentIndex = 0;
				
				for (PoliceStation station: stations) {
					db.save(station);
					currentIndex++;
					publishProgress( new Integer(currentIndex * 100 / totalCount) );
				}
				
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
		protected void onPostExecute(Void result) {
			dismissDialog( DIALOG_IMPORT_DATA );
			bindStations();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int percentComplete = values[0];
			importDialog.setMessage( String.format("%d%s Complete", percentComplete, "%") );
		}
		
	}
}
