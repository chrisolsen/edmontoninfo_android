package com.chrisolsen.edmontoninfo;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.chrisolsen.edmontoninfo.db.CommunityLeagueCentersDB;
import com.chrisolsen.edmontoninfo.models.CommunityLeagueCenter;

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
import android.widget.TextView;

public class CommunityLeagueCentersActivity extends ListActivity {

	private static final int DIALOG_IMPORT_DATA = 0;
	
	private ProgressDialog importDialog;
	private CommunityLeagueCentersDB db = new CommunityLeagueCentersDB(this);
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		setContentView(R.layout.list);
		setTitle("Community League Centres");
		
		// bind centers if they do exist
		if ( bindItems() == 0 )
			showDialog(DIALOG_IMPORT_DATA);
	}

	/**
	 * Show the progress dialog when importing the data
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_IMPORT_DATA:
			importDialog = new ProgressDialog(CommunityLeagueCentersActivity.this);
			importDialog.setCancelable(false);
			importDialog.setMessage("Importing Community League Centers...");
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
	 * 	Center count
	 */
	private int bindItems() {
		this.db = new CommunityLeagueCentersDB(this);
		Cursor c = db.findAll(CommunityLeagueCentersDB.CNAME_NAME);
		String[] from = new String[] { CommunityLeagueCentersDB.CNAME_NAME, CommunityLeagueCentersDB.CNAME_ADDRESS };
		int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listview_row, c, from, to);
		
		ListView listView = getListView();
		if (c.getCount() > 0) {
			View header = getLayoutInflater().inflate(R.layout.listview_header, null);
			TextView viewMap = (TextView)header.findViewById(R.id.listview_header_text);
			viewMap.setText( String.format("View Map (%d Centres)", c.getCount()) );
			
			viewMap.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent( CommunityLeagueCentersActivity.this, CommunityLeagueCenterMapActivity.class );
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
					CommunityLeagueCenter center = new CommunityLeagueCenter(db.cursor);
					
					Intent intent = new Intent( CommunityLeagueCentersActivity.this, CommunityLeagueCenterMapActivity.class );
					intent.putExtra(CommunityLeagueCenterMapActivity.DATA_KEY, center);
					
					startActivity(intent);
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
			HttpGet request = new HttpGet( getString(R.string.import_url_community_league_centers) );
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			CommunityLeagueCentersDB db = new CommunityLeagueCentersDB(CommunityLeagueCentersActivity.this);
			
			try {
				String rawJSON = client.execute(request, handler);
				ArrayList<CommunityLeagueCenter> centers = db.convertFromJSON(rawJSON);
				
				int totalCount = centers.size();
				int currentIndex = 0;
				
				for (CommunityLeagueCenter center: centers) {
					db.save(center);
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
			bindItems();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int percentComplete = values[0];
			importDialog.setMessage( String.format("%d%s Complete", percentComplete, "%") );
		}
	}
}
