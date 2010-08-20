package com.chrisolsen.edmontoninfo;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.chrisolsen.edmontoninfo.db.FireStationsDB;
import com.chrisolsen.edmontoninfo.models.FireStation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class FireStationsActivity extends ListActivity {
	
private static final int DIALOG_IMPORT_DATA = 0;
	
	private ProgressDialog importDialog;
	private FireStationsDB db = new FireStationsDB(this);
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		// bind fire stations if they do exist
		if ( bindStations() == 0 )
			showDialog(DIALOG_IMPORT_DATA);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		db.cursor.moveToPosition(position - 1);
		FireStation station = new FireStation(db.cursor);
		
		Intent intent = new Intent( FireStationsActivity.this, FireStationMapActivity.class );
		intent.putExtra(FireStationMapActivity.DATA_KEY, station);
		startActivity(intent);
	}

	/**
	 * Show the progress dialog when importing the data
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_IMPORT_DATA:
			importDialog = new ProgressDialog(FireStationsActivity.this);
			importDialog.setCancelable(false);
			importDialog.setMessage("Importing Fire Stations...");
			importDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			
			ImportDataAsyncTask importThread = new ImportDataAsyncTask();
			importThread.execute( (Void[])null );
			
			return importDialog;
		}
		
		return null;
	}
	
	
	/**
	 * Bind fire stations and return the count of existing stations
	 * @return
	 * 	Fire station count
	 */
	private int bindStations() {
		this.db = new FireStationsDB(this);
		Cursor c = db.getCursor(null, FireStationsDB.CNAME_NAME);
		String[] from = new String[] { FireStationsDB.CNAME_NAME, FireStationsDB.CNAME_ADDRESS };
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
					Intent intent = new Intent(FireStationsActivity.this, FireStationMapActivity.class);
					startActivity(intent);
				}
			});
			
			listView.addHeaderView(header);
			listView.addFooterView(header);
			listView.setAdapter(adapter);
		}
		
		return c.getCount();
	}
	
	/**
	 * Import the external data in background thread
	 * @author chris
	 *
	 */
	private class ImportDataAsyncTask extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected Integer doInBackground(Void... arg0) {
			
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet( getString(R.string.import_url_fire_stations) );
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			FireStationsDB db = new FireStationsDB(FireStationsActivity.this);
			int count = 0;
			
			try {
				String rawJSON = client.execute(request, handler);
				ArrayList<FireStation> stations = db.convertFromJSON(rawJSON);
				
				int totalCount = stations.size();
				
				for (FireStation station: stations) {
					db.save(station);
					count++;
					publishProgress( new Integer(count * 100 / totalCount) );
				}
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				db.close();
			}
			
			return count;
		}

		@Override
		protected void onPostExecute(Integer result) {
			dismissDialog( DIALOG_IMPORT_DATA );
			
			if ( result == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(FireStationsActivity.this);
				builder
					.setNegativeButton("Close", null)
					.setMessage("Unable to connect to the server")
					.show();
			}
			else
				bindStations();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int percentComplete = values[0];
			importDialog.setMessage( String.format("%d%s Complete", percentComplete, "%") );
		}
		
	}
}
