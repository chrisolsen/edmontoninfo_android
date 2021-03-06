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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SchoolsActivity extends ListActivity {

	protected ProgressDialog importDialog;
	protected ListView listView;
	protected SchoolsDB _db;
	
	private static final int DIALOG_IMPORT_ID = 0;
	
	// Handlers
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		bindSchools();
		
		if ( _db.cursor.getCount() == 0 )
			showDialog(DIALOG_IMPORT_ID);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		_db.cursor.moveToPosition( position - 1 );
		School school = new School( _db.cursor );

		Intent intent = new Intent(SchoolsActivity.this, SchoolMapActivity.class);
		intent.putExtra(SchoolMapActivity.DATA_KEY, school);
		startActivity(intent);
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
	 * Query and attach the schools to the listview
	 */
	private void bindSchools() {
		if ( _db == null )
			_db = new SchoolsDB(this);
		Cursor c = _db.getCursor( null, SchoolsDB.CNAME_NAME );
		
		SimpleCursorAdapter ca = new SimpleCursorAdapter(SchoolsActivity.this, 
															R.layout.listview_row, 
															c, 
															new String[] { SchoolsDB.CNAME_NAME, SchoolsDB.CNAME_GRADE_LEVEL }, 
															new int[] {android.R.id.text1, android.R.id.text2});
		
		if (c.getCount() > 0) {
			View header = getLayoutInflater().inflate(R.layout.listview_header, null);
			TextView viewMap = (TextView)header.findViewById(R.id.listview_header_text);
			viewMap.setText( String.format("View Map (%d Schools)", c.getCount()) );
			
			viewMap.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(SchoolsActivity.this, SchoolMapActivity.class);
					startActivity(intent);
				}
			});
			
			getListView().addHeaderView(header);
			getListView().addFooterView(header);
			getListView().setAdapter(ca);
		}
	}
	
	/**
	 * Import Data Thread
	 * 	- pulls data from web service into the local database
	 */
	private class ImportSchoolData extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
		
			String url = getString(R.string.import_url_schools);
			
			HttpClient client = new DefaultHttpClient();
			HttpGet getter = new HttpGet(url);
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			SchoolsDB db = new SchoolsDB(SchoolsActivity.this);
			int schoolCount = 0;
			
			// list of existing school names
			Cursor c = db.getCursor(null, null);
			ArrayList<String> names = new ArrayList<String>( c.getCount() );
			while ( c.moveToNext() ) {
				names.add( c.getString(SchoolsDB.CINDEX_NAME) );
			}
			
			try {
				String rawJson = client.execute(getter, handler);
				ArrayList<School> schools = db.convertFromJson(rawJson);
				School school;
				
				schoolCount = schools.size();
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
			
			return schoolCount;
		}
		
		@Override 
		protected void onProgressUpdate(Integer... status) {
			int countComplete = status[0];
			int totalCount = status[1];
			int progress = (countComplete * 100) / totalCount;
			
			importDialog.setMessage( String.valueOf(progress) + "% Complete" );
		}
		
		@Override 
		protected void onPostExecute(Integer result) {
			dismissDialog(DIALOG_IMPORT_ID);
			
			if ( result == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(SchoolsActivity.this);
				builder
					.setNegativeButton("Close", null)
					.setMessage("Unable to connect to the server")
					.show();
			}
			else
			bindSchools();
		}
	}
}
