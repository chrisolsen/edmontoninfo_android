package com.chrisolsen.edmontoninfo;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.chrisolsen.edmontoninfo.db.SchoolsDB;
import com.chrisolsen.edmontoninfo.models.School;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;

public class SchoolsActivity extends TabActivity {

	protected ProgressDialog importDialog;
	protected ListView listView;
	protected SchoolsDB _db;
	
	private static final int DIALOG_IMPORT_ID = 0;
	
	// Handlers
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.schools);
		
		listView = (ListView)findViewById(android.R.id.list);
		listView.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View view, int position,
					long rowId) {
				
				Intent show = new Intent(SchoolsActivity.this, SchoolActivity.class);
				
				_db.cursor.moveToPosition( position );
				School school = new School( _db.cursor );
				show.putExtra( SchoolsDB.CNAME_ID, school.id);
				startActivity(show);
			}
		});
		
		setupTabs();
		bindSchools();
		
		if ( _db.cursor.getCount() == 0 )
			showDialog(DIALOG_IMPORT_ID);
	}
	
	/**
	 * Show custom dialogs
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_IMPORT_ID:
			importDialog = new ProgressDialog(SchoolsActivity.this);
			importDialog.setCancelable(false);
			importDialog.setMessage("Importing Schools...");
			importDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			
			ImportSchoolData thread = new ImportSchoolData();
			thread.execute( (Void[])null );	
			
			return importDialog;
		}
		
		return null;
	}

	/**
	 * Show the Schools and Map tabs
	 */
	private void setupTabs() {
		TabHost tabHost;
		TabHost.TabSpec specs;
		
		tabHost = getTabHost();
	
		// school list tab
		specs = tabHost.newTabSpec("list");
		specs.setContent(android.R.id.list);
		specs.setIndicator( "List", this.getResources().getDrawable(R.drawable.list) );
		tabHost.addTab(specs);
		
		// school map tab
		//	must create map through an intent to allow it to have it's own Activity
		Intent mapIntent = new Intent(this, SchoolsMapActivity.class);
		specs = tabHost.newTabSpec("map");
		specs.setContent(mapIntent);
		specs.setIndicator( "Map", this.getResources().getDrawable(R.drawable.map) );
		tabHost.addTab(specs);
		
		tabHost.setCurrentTab(0);
	}	
	
	/**
	 * Query and attach the schools to the listview
	 */
	private void bindSchools() {
		if ( _db == null )
			_db = new SchoolsDB(this);
		Cursor c = _db.findAll( SchoolsDB.CNAME_NAME );
		
		SimpleCursorAdapter ca = new SimpleCursorAdapter(SchoolsActivity.this, 
															R.layout.listview_row, 
															c, 
															new String[] { SchoolsDB.CNAME_NAME, SchoolsDB.CNAME_GRADE_LEVEL }, 
															new int[] {android.R.id.text1, android.R.id.text2});
		listView.setAdapter(ca);
	}
	
	/**
	 * Import Data Thread
	 * 	- pulls data from web service into the local database
	 */
	private class ImportSchoolData extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
		
			String url = getString(R.string.import_url_schools);
			
			HttpClient client = new DefaultHttpClient();
			HttpGet getter = new HttpGet(url);
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			SchoolsDB db = new SchoolsDB(SchoolsActivity.this);
			
			// list of existing school names
			Cursor c = db.findAll();
			ArrayList<String> names = new ArrayList<String>( c.getCount() );
			while ( c.moveToNext() ) {
				names.add( c.getString(SchoolsDB.CINDEX_NAME) );
			}
			
			try {
				String rawJson = client.execute(getter, handler);
				ArrayList<School> schools = db.convertFromJson(rawJson);
				School school;
				
				int schoolCount = schools.size();
				int savedSchoolCount = c.getCount();  // pre-existing schools from previous incomplete import
				
				for (int i = 0; i < schoolCount; i++) {
					school = schools.get(i);
					if ( !names.contains( school.name ) ) {
						db.save( school );
						savedSchoolCount++;
						publishProgress( new Integer(savedSchoolCount), new Integer(schoolCount) );
					}
				}
			}
			catch (ClientProtocolException ex) {
				Log.d("Import School Data", ex.getMessage());
			}
			catch (IOException ex) {
				Log.d("Import School Data", ex.getMessage());
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
			bindSchools();
		}
	}
}
