package com.chrisolsen.edmontoninfo.maps;

import com.chrisolsen.edmontoninfo.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MapContextActivity extends Activity {

	public static final String KEY = "mapContextValues";
	private MapContextValues values;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.map_context);
		
		this.values = (MapContextValues)getIntent().getExtras().getSerializable(KEY);
		this.setTitle(values.name);
		
		// bind controls
		bindAddress();
		bindWebsite();
		bindPhone();
		bindEmail();
		bindStreetView();
	}

	private void bindAddress() {
		TextView address = (TextView)findViewById( R.id.map_context_address );
		if ( values.address == null )
			address.setVisibility(View.GONE);
		else
			address.setText( values.address );
	}
	
	private void bindWebsite() {
		LinearLayout row = (LinearLayout)findViewById( R.id.map_context_website );
		if ( values.websiteUrl == null )
			row.setVisibility(View.GONE);
		else {
			row.setClickable(true);
			row.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String urlPrefix = values.websiteUrl.startsWith("http") ? "" : "http://";
					String url = urlPrefix + values.websiteUrl;
					Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(url) );
					startActivity(intent);
				}
			});
		}
	}
	
	private void bindPhone() {
		LinearLayout row = (LinearLayout)findViewById( R.id.map_context_phone );
		if ( values.phoneNumber == null )
			row.setVisibility(View.GONE);
		else {
			row.setClickable(true);
			row.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent( Intent.ACTION_DIAL, Uri.parse("tel:" + values.phoneNumber) );
					startActivity(intent);
				}
			});
		}
	}
	
	private void bindEmail() {
		LinearLayout row = (LinearLayout)findViewById( R.id.map_context_email );
		if ( values.emailAddress == null )
			row.setVisibility(View.GONE);
		else {
			row.setClickable(true);
			row.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent( Intent.ACTION_SEND );
					intent.setType("plain/text");
					intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{values.emailAddress});
					 
					startActivity(intent);
				}
			});
		}
	}
	
	private void bindStreetView() {
		LinearLayout row = (LinearLayout)findViewById( R.id.map_context_streetview );
		if ( values.latitude == 0d || values.longitude == 0d)
			row.setVisibility(View.GONE);
		else {
			row.setClickable(true);
			row.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = String.format("google.streetview:cbll=%f,%f", values.latitude, values.longitude);
					Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(url) );
					startActivity(intent);
				}
			});
		}
	}
}
