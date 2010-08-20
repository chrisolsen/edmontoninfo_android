package com.chrisolsen.edmontoninfo;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.chrisolsen.edmontoninfo.db.LibraryDB;
import com.chrisolsen.edmontoninfo.models.Library;

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

public class LibrariesActivity extends ListActivity {

	private static final int DIALOG_IMPORT_ID = 0;
	private LibraryDB _db;
	private ProgressDialog importDialog;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		bindData();
		
		if ( _db.cursor.getCount() == 0 )
			showDialog(DIALOG_IMPORT_ID);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		_db.cursor.moveToPosition(position - 1);
		Library lib = new Library(_db.cursor);
		
		Intent intent = new Intent(LibrariesActivity.this, LibraryMapActivity.class);
		intent.putExtra(LibraryMapActivity.DATA_KEY, lib);
		startActivity(intent);
	}

	/**
	 * Show custom dialogs
	 */
	protected Dialog onCreateDialog(int id) {
		
		switch (id) {
		case DIALOG_IMPORT_ID:
			importDialog = new ProgressDialog(LibrariesActivity.this);
			importDialog.setCancelable(false);
			importDialog.setMessage("Importing Libraries...");
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
			_db = new LibraryDB(this);
		Cursor c = _db.getCursor( null, LibraryDB.CNAME_BRANCH );
		
		SimpleCursorAdapter ca = new SimpleCursorAdapter(LibrariesActivity.this, 
															R.layout.listview_row, 
															c, 
															new String[] { LibraryDB.CNAME_BRANCH, LibraryDB.CNAME_ADDRESS }, 
															new int[] {android.R.id.text1, android.R.id.text2});
		
		// bind header and footer
		if (c.getCount() > 0) {
			View header = getLayoutInflater().inflate(R.layout.listview_header, null);
			TextView viewMap = (TextView)header.findViewById(R.id.listview_header_text);
			viewMap.setText( String.format("View Map (%d Libraries)", c.getCount()) );
			
			viewMap.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LibrariesActivity.this, LibraryMapActivity.class);
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
	private class ImportData extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
		
			String url = getString(R.string.import_url_libraries);
			
			HttpClient client = new DefaultHttpClient();
			HttpGet getter = new HttpGet(url);
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			LibraryDB db = new LibraryDB(LibrariesActivity.this);
			int count = 0;
			
			// list of existing school names
			Cursor c = db.getCursor(null, null);
			ArrayList<String> names = new ArrayList<String>( c.getCount() );
			while ( c.moveToNext() ) {
				names.add( c.getString(LibraryDB.CINDEX_BRANCH) );
			}
			
			try {
				String rawJson = client.execute(getter, handler);
				ArrayList<Library> libs = db.convertFromJson(rawJson);
				Library lib;
				
				count = libs.size();
				int savedCount = c.getCount();  // pre-existing schools from previous incomplete import
				
				for (int i = 0; i < count; i++) {
					lib = libs.get(i);
					if ( !names.contains( lib.branch ) ) {
						db.save( lib );
						savedCount++;
						publishProgress( new Integer(savedCount), new Integer(count) );
					}
				}
			}
			catch (ClientProtocolException ex) {
				Log.d("Import Library Data", ex.getMessage());
			}
			catch (IOException ex) {
				Log.d("Import Library Data", ex.getMessage());
			}
			finally {
				db.close();
			}
			
			return count;
		}
		
		@Override 
		protected void onProgressUpdate(Integer... status) {
			int countComplete = status[0];
			int totalCount = status[1];
			int progress = (countComplete * 100) / totalCount;
			
			importDialog.setMessage( String.format("%d%s Complete", progress, "%") );
		}
		
		@Override 
		protected void onPostExecute(Integer result) {
			dismissDialog(DIALOG_IMPORT_ID);
			
			if ( result == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(LibrariesActivity.this);
				builder
					.setNegativeButton("Close", null)
					.setMessage("Unable to connect to the server")
					.show();
			}
			else
				bindData();
		}
	}
	
}
