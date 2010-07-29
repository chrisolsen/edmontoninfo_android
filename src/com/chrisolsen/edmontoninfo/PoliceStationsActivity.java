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
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

public class PoliceStationsActivity extends ListActivity {

	private static final int DIALOG_IMPORT_DATA = 0;
	private static final String DATA_URL = "http://edmontononrails.com/police_stations";
	
	private ProgressDialog importDialog;
	private PoliceStationsDB db = new PoliceStationsDB(this);
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		// bind police stations if they do exist
		if ( bindStations() == 0 )
			showDialog(DIALOG_IMPORT_DATA);
			
	}
	
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
	
	private int bindStations() {
		this.db = new PoliceStationsDB(this);
		Cursor c = db.findAll(PoliceStationsDB.CNAME_NAME);
		String[] from = new String[] { PoliceStationsDB.CNAME_NAME, PoliceStationsDB.CNAME_ADDRESS };
		int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listview_row, c, from, to);
		
		setListAdapter(adapter);
		
		return c.getCount();
	}
	
	private class ImportDataAsyncTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(DATA_URL);
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			try {
				PoliceStationsDB db = new PoliceStationsDB(PoliceStationsActivity.this);
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
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int percentComplete = values[0];
			importDialog.setMessage( String.format("%d%s Complete", percentComplete, "%") );
			super.onProgressUpdate(values);
		}
		
	}
}
