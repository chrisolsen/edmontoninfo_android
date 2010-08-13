package com.chrisolsen.edmontoninfo.views;

import com.chrisolsen.edmontoninfo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageTextButton extends RelativeLayout {

	public ImageTextButton(Context context) {
		super(context);
	}

	public ImageTextButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void bind(int imageId, String text, int tag, OnClickListener l) {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.image_text_button, this, true);
		
		TextView textView = (TextView)findViewById(R.id.image_text_button_text);
		textView.setTag(tag);
		textView.setText(text);
		textView.setOnClickListener(l);
		
		ImageView imageView = (ImageView)findViewById(R.id.image_text_button_image);
		imageView.setTag(tag);
		imageView.setBackgroundResource(imageId);
		imageView.setOnClickListener(l);
	}
}