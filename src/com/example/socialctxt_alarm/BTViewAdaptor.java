package com.example.socialctxt_alarm;


import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BTViewAdaptor extends BaseAdapter {

	private BluetoothDevicesMapper btdevicesMapper;
	public ArrayList<String> names = new ArrayList<String>();

	public BTViewAdaptor(BluetoothDevicesMapper btdevicesMapper, ArrayList<String> names1) {
		this.btdevicesMapper = btdevicesMapper;
		for (int i = 0; i < names1.size(); i++)
			names.add(names1.get(i));
	}

	public int getCount() {
		return names.size();
	}

	public Object getItem(int position) {
		return names.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = new TextView(btdevicesMapper);
		textView.setText(names.get(position));
		textView.setClickable(true);
		return textView;
	}
}