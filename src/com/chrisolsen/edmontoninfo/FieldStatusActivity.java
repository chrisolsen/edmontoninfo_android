package com.chrisolsen.edmontoninfo;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.chrisolsen.edmontoninfo.Global.*;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FieldStatusActivity extends ListActivity {

	private static final int DIALOG_IMPORT_ID = 0;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		showDialog(DIALOG_IMPORT_ID);
	}
	
	private void bindData(ArrayList<FieldState> fieldStates) {
		
		TableAdapter adapter = new TableAdapter(this, fieldStates); 
		
		View header = getLayoutInflater().inflate(R.layout.listview_header, null);
		TextView headerText = (TextView)header.findViewById(R.id.listview_header_text);
		
		SimpleDateFormat formatter = new SimpleDateFormat("E MMM d '@' hh:mm a");
		String formattedDate = formatter.format(fieldStates.get(0).updatedAt);
		
		headerText.setText(formattedDate);
		getListView().setClickable(false);
		getListView().addHeaderView(header);
		setListAdapter(adapter);
	}

	/**
	 * Show custom dialogs
	 */
	protected Dialog onCreateDialog(int id) {
		
		switch (id) {
		case DIALOG_IMPORT_ID:
			ProgressDialog importDialog = new ProgressDialog(FieldStatusActivity.this);
			importDialog.setCancelable(false);
			importDialog.setMessage("Getting latest field status...");
			importDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			
			FieldStatusUpdate thread = new FieldStatusUpdate();
			thread.execute( (Void[])null );	
			
			return importDialog;
		}
		
		return null;
	}
	
	private class FieldStatusUpdate extends AsyncTask<Void, Void, ArrayList<FieldState> > {

		@Override
		protected ArrayList<FieldState> doInBackground(Void... params) {
			
			String url = getString(R.string.import_url_field_status);
			
			HttpClient client = new DefaultHttpClient();
			HttpGet getter = new HttpGet(url);
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			ArrayList<FieldState> states = new ArrayList<FieldState>();
			
			try {
				String rawJson = client.execute(getter, handler);
				
				// convert from json
				JSONArray json = new JSONArray(rawJson);
				
				for (int i = 0; i < json.length(); i++) {
					JSONObject obj = json.getJSONObject(i);
					FieldState state = new FieldState();
					
					SimpleDateFormat formatter = new SimpleDateFormat(ISO8601);
					
					state.field 	= obj.getString("name");
					state.status 	= obj.getString("state");
					state.updatedAt = formatter.parse( obj.getString("created_at") );

					states.add(state);
				}
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			return states;
		}
		
		@Override 
		protected void onPostExecute(ArrayList<FieldState> states) {
			dismissDialog(DIALOG_IMPORT_ID);
			bindData(states);
		}
		
	}
	
	private class FieldState {
		public String field;
		public String status;
		public Date updatedAt;
	}
	
	public class TableAdapter extends ArrayAdapter<FieldState> {

		LayoutInflater inflater;
		ArrayList<FieldState> fieldStates;
		
		public TableAdapter(Context context, ArrayList<FieldState> fieldStates) {
			super(context, R.layout.table_row, fieldStates);
			this.fieldStates = fieldStates;
			this.inflater = getLayoutInflater();
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View row = convertView;
			
			if (convertView == null)
				row = inflater.inflate(R.layout.table_row, null);
			
			TextView label = (TextView)row.findViewById( android.R.id.text1 );
			TextView value = (TextView)row.findViewById( android.R.id.text2 );
			
			FieldState item = this.fieldStates.get(position);
			label.setText( item.field );
			value.setText( item.status );
			
			return row;
		}
	}
	
}
