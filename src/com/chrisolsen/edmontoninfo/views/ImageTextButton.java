package com.chrisolsen.edmontoninfo.views;

import com.chrisolsen.edmontoninfo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageTextButton extends RelativeLayout {

	public ImageView	image;
	public TextView 	text;
	public int 			tag;
	
	public ImageTextButton(Context context) {
		super(context);
		bindControls();
	}

	public ImageTextButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		bindControls();
	}

	private void bindControls() {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.image_text_button, this, true);
		
		text = (TextView)findViewById(R.id.image_text_button_text);
		text.setTag(tag);
		
		image = (ImageView)findViewById(R.id.image_text_button_image);
		image.setTag(tag);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		text.setOnClickListener(l);
		image.setOnClickListener(l);
	}
	
}