package com.chrisolsen.edmontoninfo;

import com.chrisolsen.edmontoninfo.db.SchoolsDB;
import com.chrisolsen.edmontoninfo.models.School;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.TextView;

public class SchoolActivity extends Activity {

	School school;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.school);
		
		// find school
		long id = getIntent().getLongExtra( SchoolsDB.CNAME_ID, 0 );
		SchoolsDB db = new SchoolsDB(this);
		Cursor c = db.findById(id);
		school = new School(c);
		db.close();
		
		// fill page
		TextView schoolName = (TextView)findViewById( R.id.school_name );
		TextView schoolAddress = (TextView)findViewById( R.id.school_address );
		TextView schoolPostalCode = (TextView)findViewById( R.id.school_postal_code );
		TextView schoolPrograms = (TextView)findViewById( R.id.school_programs );
		TextView schoolType = (TextView)findViewById( R.id.school_type );
		TextView schoolPhoneNumber = (TextView)findViewById( R.id.school_phone_number );
		TextView schoolFaxNumber = (TextView)findViewById( R.id.school_fax_number );
		TextView schoolContact = (TextView)findViewById( R.id.school_contact );
		TextView schoolGradeLevel = (TextView)findViewById( R.id.school_grade_level );
		TextView schoolUrl = (TextView)findViewById( R.id.school_url );
		TextView schoolWard = (TextView)findViewById( R.id.school_ward );
		
		schoolName.setText( school.name );
		schoolAddress.setText( school.address );
		schoolPostalCode.setText( school.postalCode );
		schoolPrograms.setText( school.programs );
		schoolType.setText( school.school_type );
		schoolPhoneNumber.setText( PhoneNumberUtils.formatNumber(school.phoneNumber) );
		schoolFaxNumber.setText( PhoneNumberUtils.formatNumber(school.faxNumber) );
		schoolContact.setText( school.email );
		schoolGradeLevel.setText( school.gradeLevel );
		schoolUrl.setText( school.url );
		schoolWard.setText( school.ward );
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.school_menu, menu);
		
		return super.onCreateOptionsMenu(menu);		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		Intent intent = null;
		String url;
		
		switch ( item.getItemId() ) {
		case R.id.school_menu_item_email:
			intent = new Intent( Intent.ACTION_SEND );
			intent.setType("plain/text");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] {school.email} );
			break;
			
		case R.id.school_menu_item_map:
			url = String.format("geo:0,0?q=%s,%s", school.address, "Edmonton,AB");
			intent = new Intent( Intent.ACTION_VIEW, Uri.parse(url) );
			break;
			
		case R.id.school_menu_item_phone:
			url = String.format("tel:%s", school.phoneNumber);
			intent = new Intent( Intent.ACTION_DIAL, Uri.parse(url) );
			break;
			
		case R.id.school_menu_item_streetview:
			url = String.format("google.streetview:cbll=%f,%f", school.latitude, school.longitude);
			intent = new Intent( Intent.ACTION_VIEW, Uri.parse(url) );
			break;
			
		case R.id.school_menu_item_website:
			url = String.format("http://%s", school.url);
			intent = new Intent( Intent.ACTION_VIEW, Uri.parse(url) );
			break;
		}
		
		if ( intent != null )
			startActivity( intent );
		
		return super.onOptionsItemSelected(item);
	}
}