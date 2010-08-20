package com.chrisolsen.edmontoninfo.maps;

import com.chrisolsen.edmontoninfo.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MapContextActivity extends Activity implements OnClickListener {

	public static final String KEY = "mapContextValues";
	private MapContextValues values;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.map_context);
		
		this.values = (MapContextValues)getIntent().getExtras().getSerializable(KEY);
		this.setTitle(values.name);
		
		// bind controls
		View b;
		
		// email
		b = findViewById( R.id.map_context_email );
		b.setOnClickListener(this);
		if ( values.emailAddress == null || values.emailAddress == "" )
			b.setVisibility(View.GONE);
		
		// website
		b = findViewById( R.id.map_context_website );
		b.setOnClickListener(this);
		if ( values.websiteUrl == null )
			b.setVisibility(View.GONE);
		
		// phone
		b = findViewById( R.id.map_context_phone );
		b.setOnClickListener(this);
		if ( values.phoneNumber == null )
			b.setVisibility(View.GONE);
		
		// streetview
		b = findViewById( R.id.map_context_streetview );
		b.setOnClickListener(this);
		if ( values.latitude == 0d || values.longitude == 0d)
			b.setVisibility(View.GONE);
		
		// address
		TextView address = (TextView)findViewById( R.id.map_context_address );
		if ( values.address == null )
			address.setVisibility(View.GONE);
		else
			address.setText( values.address );
	}

	@Override
	public void onClick(View v) {
		
		String url;
		Intent intent = null;
		
		switch(v.getId()) {
		case R.id.map_context_email:
			intent = new Intent( Intent.ACTION_SEND );
			intent.setType("plain/text");
			intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{values.emailAddress});
			break;
			
		case R.id.map_context_phone:
			intent = new Intent( Intent.ACTION_DIAL, Uri.parse("tel:" + values.phoneNumber) );
			break;
			
		case R.id.map_context_streetview:
			url = String.format("google.streetview:cbll=%f,%f", values.latitude, values.longitude);
			intent = new Intent( Intent.ACTION_VIEW, Uri.parse(url) );
			break;
			
		case R.id.map_context_website:
			String urlPrefix = values.websiteUrl.startsWith("http") ? "" : "http://";
			url = urlPrefix + values.websiteUrl;
			intent = new Intent( Intent.ACTION_VIEW, Uri.parse(url) );
			break;
		}
		
		if (intent != null)
			startActivity(intent);
	}
}
