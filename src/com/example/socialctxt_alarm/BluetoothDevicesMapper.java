package com.example.socialctxt_alarm;

import java.util.ArrayList;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.maps.MapActivity;

public class BluetoothDevicesMapper extends MapActivity {

	private BluetoothAdapter mBtAdapter;
	ArrayList<String> names1 = new ArrayList<String>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		mBtAdapter.startDiscovery();

		setContentView(R.layout.bluetooth_dev_map);
		ListView list = (ListView) findViewById(R.id.BTdevicesList);
		list.setAdapter(new BTViewAdaptor(this, names1));
	}
	
	public void onDestroy() {
	    if (mReceiver != null) {
	        this.unregisterReceiver(mReceiver);
	    }
	    super.onDestroy();
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				ArrayList<String> mArrayAdapter = new ArrayList<String>();
				// Add the name and address to an array adapter to show in a
				// ListView
				mArrayAdapter
						.add(device.getName() + "\n" + device.getAddress());
				names1.add(device.getAddress());
			}
		}
	};

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}