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

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

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
		Cursor c = db.findAll(FireStationsDB.CNAME_NAME);
		String[] from = new String[] { FireStationsDB.CNAME_NAME, FireStationsDB.CNAME_ADDRESS };
		int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listview_row, c, from, to);
		
		ListView listView = getListView();
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				db.cursor.moveToPosition(position);
				FireStation station = new FireStation(db.cursor);
				Intent intent = new Intent( FireStationsActivity.this, FireStationMapActivity.class );
				intent.putExtra(FireStationMapActivity.DATA_KEY, station);
				startActivity(intent);
			}
		});
		
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
			HttpGet request = new HttpGet( getString(R.string.import_url_fire_stations) );
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			FireStationsDB db = new FireStationsDB(FireStationsActivity.this);
			
			try {
				String rawJSON = client.execute(request, handler);
				ArrayList<FireStation> stations = db.convertFromJSON(rawJSON);
				
				int totalCount = stations.size();
				int currentIndex = 0;
				
				for (FireStation station: stations) {
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
