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

public class TableAdapter extends ArrayAdapter<NameValue> {

	private LayoutInflater inflater;
	private List<NameValue> nameValues;
	private RowLayout rowLayout;
	
	public static enum RowLayout {VERTICAL, HORIZONTAL};
	
	public TableAdapter(Context context, RowLayout rowLayout, List<NameValue> nameValues) {
		super(context, R.layout.table_row, nameValues);
		this.nameValues = nameValues;
		this.inflater = (LayoutInflater)context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		this.rowLayout = rowLayout;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		int layout = rowLayout == RowLayout.HORIZONTAL ? R.layout.table_row : R.layout.table_stacked;

		if (convertView == null)
			row = inflater.inflate(layout, null);
		
		TextView label = (TextView)row.findViewById( android.R.id.text1 );
		TextView value = (TextView)row.findViewById( android.R.id.text2 );
		
		NameValue item = this.nameValues.get(position);
		label.setText( item.name );
		value.setText( item.value );
		
		return row;
	}
}
