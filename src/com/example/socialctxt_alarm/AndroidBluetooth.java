package com.example.socialctxt_alarm;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AndroidBluetooth extends Activity {

	private static final int REQUEST_ENABLE_BT = 1;
	ListView listDevicesFound, listContacts;
	Button btnScanDevice;
	TextView stateBluetooth;
	BluetoothAdapter bluetoothAdapter;
	Context c;
	BluetoothViewAdaptor frnd = null;
	ArrayAdapter<String> btArrayAdapter, contactArrayAdapter;
	ArrayList<String> btdev, phnnum;
	String phone_num, bt_adrs;
	int cnt = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btdev = new ArrayList<String>();
		phnnum = new ArrayList<String>();
		btnScanDevice = (Button) findViewById(R.id.scandevice);
		stateBluetooth = (TextView) findViewById(R.id.bluetoothstate);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		listDevicesFound = (ListView) findViewById(R.id.devicesfound);
		btArrayAdapter = new ArrayAdapter<String>(AndroidBluetooth.this,
				android.R.layout.simple_list_item_1);
		listDevicesFound.setAdapter(btArrayAdapter);
		CheckBlueToothState();
		btnScanDevice.setOnClickListener(btnScanDeviceOnClickListener);
		registerReceiver(ActionFoundReceiver, new IntentFilter(
				BluetoothDevice.ACTION_FOUND));
		c = this.getBaseContext();
		listDevicesFound.setOnItemClickListener(listDevOnClickListener);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(ActionFoundReceiver);
	}

	private void CheckBlueToothState() {
		if (bluetoothAdapter == null) {
			stateBluetooth.setText("Bluetooth NOT support");
		} else {
			if (bluetoothAdapter.isEnabled()) {
				if (bluetoothAdapter.isDiscovering()) {
					stateBluetooth
							.setText("Bluetooth is currently in device discovery process.");
				} else {
					stateBluetooth.setText("Bluetooth is Enabled.");
					btnScanDevice.setEnabled(true);
				}
			} else {
				stateBluetooth.setText("Bluetooth is NOT Enabled!");
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	private Button.OnClickListener btnScanDeviceOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			btArrayAdapter.clear();
			bluetoothAdapter.startDiscovery();
		}
	};

	private ListView.OnItemClickListener listDevOnClickListener = new ListView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
			bt_adrs = btArrayAdapter.getItem(i);
			String arr[] = bt_adrs.split(",");
			btdev.add(cnt, arr[1]);
			setContentView(R.layout.bluetooth_dev_map);
			listContacts = (ListView) findViewById(R.id.BTdevicesList);
			frnd = new BluetoothViewAdaptor(c);
			listContacts.setAdapter(frnd);
			listContacts.setOnItemClickListener(contactsOnClickListener);
		}
	};

	private ListView.OnItemClickListener contactsOnClickListener = new ListView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String name = frnd.names.get(arg2);

			for (int i = 0; i < frnd.names.size(); i++) {
				if (frnd.names.get(i).equalsIgnoreCase(name)) {
					phone_num = frnd.phn_num.get(i);
					phnnum.add(cnt, phone_num);
					cnt++;
					break;
				}
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			// CheckBlueToothState();
		}
	}

	private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				btArrayAdapter
						.add(device.getName() + "," + device.getAddress());
				btArrayAdapter.notifyDataSetChanged();
			}
		}
	};
}