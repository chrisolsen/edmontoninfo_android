package com.chrisolsen.edmontoninfo;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.chrisolsen.edmontoninfo.db.RecFacilityDB;
import com.chrisolsen.edmontoninfo.models.RecFacility;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class RecFacilitiesActivity extends ListActivity {
	private static final int DIALOG_IMPORT_ID = 0;
	private RecFacilityDB _db;
	private ProgressDialog importDialog;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		getListView().setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View view, int position,
					long rowId) {
			
				_db.cursor.moveToPosition(position);
				RecFacility rec = new RecFacility(_db.cursor);
				
				Intent intent = new Intent(RecFacilitiesActivity.this, RecFacilityMapActivity.class);
				intent.putExtra(RecFacilityMapActivity.DATA_KEY, rec);
				startActivity(intent);
			}
		});
		
		bindData();
		
		if ( _db.cursor.getCount() == 0 )
			showDialog(DIALOG_IMPORT_ID);
	}
	
	/**
	 * Show custom dialogs
	 */
	protected Dialog onCreateDialog(int id) {
		
		switch (id) {
		case DIALOG_IMPORT_ID:
			importDialog = new ProgressDialog(RecFacilitiesActivity.this);
			importDialog.setCancelable(false);
			importDialog.setMessage("Importing Rec Facilities...");
			importDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			
			ImportData thread = new ImportData();
			thread.execute( (Void[])null );	
			
			return importDialog;
		}
		
		return null;
	}
	
	/**
	 * Query and attach the schools to the listview
	 */
	private void bindData() {
		if ( _db == null )
			_db = new RecFacilityDB(this);
		Cursor c = _db.findAll( RecFacilityDB.CNAME_NAME );
		
		SimpleCursorAdapter ca = new SimpleCursorAdapter(RecFacilitiesActivity.this, 
															R.layout.listview_row, 
															c, 
															new String[] { RecFacilityDB.CNAME_NAME, RecFacilityDB.CNAME_ADDRESS }, 
															new int[] {android.R.id.text1, android.R.id.text2});
		getListView().setAdapter(ca);
	}
	
	/**
	 * Import Data Thread
	 * 	- pulls data from web service into the local database
	 */
	private class ImportData extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
		
			String url = getString(R.string.import_url_rec_facilities);
			
			HttpClient client = new DefaultHttpClient();
			HttpGet getter = new HttpGet(url);
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			RecFacilityDB db = new RecFacilityDB(RecFacilitiesActivity.this);
			
			// list of existing school names
			Cursor c = db.findAll();
			ArrayList<String> names = new ArrayList<String>( c.getCount() );
			while ( c.moveToNext() ) {
				names.add( c.getString(RecFacilityDB.CINDEX_ENTITY_ID) );
			}
			
			try {
				String rawJson = client.execute(getter, handler);
				ArrayList<RecFacility> facilities = db.convertFromJson(rawJson);
				RecFacility recFacility;
				
				int count = facilities.size();
				int savedCount = c.getCount();  // pre-existing schools from previous incomplete import
				
				for (int i = 0; i < count; i++) {
					recFacility = facilities.get(i);
					if ( !names.contains( recFacility.entityId ) ) {
						db.save( recFacility );
						savedCount++;
						publishProgress( new Integer(savedCount), new Integer(count) );
					}
				}
			}
			catch (ClientProtocolException ex) {
				Log.d("Import Rec Facility Data", ex.getMessage());
			}
			catch (IOException ex) {
				Log.d("Import Rec Facility Data", ex.getMessage());
			}
			finally {
				db.close();
			}
			
			return null;
		}
		
		@Override 
		protected void onProgressUpdate(Integer... status) {
			int countComplete = status[0];
			int totalCount = status[1];
			int progress = (countComplete * 100) / totalCount;
			
			importDialog.setMessage( String.format("%d%s Complete", progress, "%") );
		}
		
		@Override 
		protected void onPostExecute(Void result) {
			dismissDialog(DIALOG_IMPORT_ID);
			bindData();
		}
	}
}
