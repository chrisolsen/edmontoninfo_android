package com.chrisolsen.edmontoninfo.adapters;

import java.util.List;

import com.chrisolsen.edmontoninfo.R;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TableAdapter extends ArrayAdapter<TableAdapter.NameValue> {

	LayoutInflater inflater;
	List<TableAdapter.NameValue> nameValues;
	
	public TableAdapter(Context context, List<TableAdapter.NameValue> nameValues) {
		super(context, R.layout.table_row, nameValues);
		this.nameValues = nameValues;
		this.inflater = (LayoutInflater)context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		
		if (convertView == null)
			row = inflater.inflate(R.layout.table_row, null);
		
		TextView label = (TextView)row.findViewById( android.R.id.text1 );
		TextView value = (TextView)row.findViewById( android.R.id.text2 );
		
		NameValue item = this.nameValues.get(position);
		label.setText( item.name );
		value.setText( item.value );
		
		return row;
	}

	public class NameValue {
		public String name;
		public String value;
		
		public NameValue(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}
}
