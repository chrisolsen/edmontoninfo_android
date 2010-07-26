package com.chrisolsen.edmontoninfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        bindButtons();
    }
    
    private void bindButtons() {
    	Button btnSchool = (Button)findViewById(R.id.btn_schools); 
    	
    	btnSchool.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, SchoolsActivity.class);
				startActivity(i);
			}
		});
    }
    
}