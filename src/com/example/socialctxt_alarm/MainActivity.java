package com.example.socialctxt_alarm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class MainActivity extends Activity {

	Button btnRegister, btnContinue, btnbtctxt, btntraffctxt,
			btntraff_Srchctxt, btntraff_setctxt, btntraff_backctxt,
			btnMainCancelAlrm, btnMainSnoozeAlrm, btnMainTrafficCancelAlrm,
			btnMainTrafficSnoozeAlrm;
	EditText mEdit;
	private PendingIntent trafficPendingIntent;

	static int snoozer = 0;
	static PendingIntent pendingIntentAlarmContext,
			pendingIntentTrafficAlarmContext;

	private static final int REQUEST_ENABLE_BT = 1;

	ListView listDevicesFound, listContacts;
	Button btnScanDevice;
	TextView stateBluetooth;
	BluetoothAdapter bluetoothAdapter;
	Context c;
	BluetoothViewAdaptor btdevices = null;
	ArrayAdapter<String> btArrayAdapter, contactArrayAdapter;
	static ArrayList<String> btdev = new ArrayList<String>();
	static ArrayList<String> phnnum = new ArrayList<String>();
	static ArrayList<String> temp_btArrayAdap = new ArrayList<String>();
	String phone_num, bt_adrs, phn;
	static int cnt = 0;
	static String src, dest, timeBusSrch;
	static String later_reqd_bus_number, later_reqd_bus_departure;
	static Calendar c_reset;
	static long reqd_reset_alarm = 0, time_diff_hrs = 0, time_diff_sec = 0,
			buffer_time_to_get_ready = 0;
	static int set;
	static long user_set_time_new;
	static boolean flag_traffic = false;
	static boolean flag_bluetooth = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		setContentView(R.layout.main_application);

		btnbtctxt = (Button) findViewById(R.id.btprox);
		btnbtctxt.setOnClickListener(btnbtctxtOnClickListener);

		btntraffctxt = (Button) findViewById(R.id.traffctxt);
		btntraffctxt.setOnClickListener(btntraffctxtOnClickListener);

		btnMainCancelAlrm = (Button) findViewById(R.id.cancelButton);
		btnMainCancelAlrm.setOnClickListener(btnMainCancelAlrmOnClickListener);

		btnMainSnoozeAlrm = (Button) findViewById(R.id.snoozeButton);
		btnMainSnoozeAlrm.setOnClickListener(btnMainSnoozeAlrmOnClickListener);

	}

	private Button.OnClickListener btnbtctxtOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			//

			setContentView(R.layout.firstscreen);

			btnRegister = (Button) findViewById(R.id.register);
			btnContinue = (Button) findViewById(R.id.Continue);

			btnRegister.setOnClickListener(btnRegisterOnClickListener);
			btnContinue.setOnClickListener(btnContinueOnClickListener);

			Button buttonBack = (Button) findViewById(R.id.backbutton_firstscreen);

			buttonBack.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View arg0) {

					setContentView(R.layout.main_application);
					btnbtctxt = (Button) findViewById(R.id.btprox);
					btnbtctxt.setOnClickListener(btnbtctxtOnClickListener);

					btntraffctxt = (Button) findViewById(R.id.traffctxt);
					btntraffctxt
							.setOnClickListener(btntraffctxtOnClickListener);

					btnMainCancelAlrm = (Button) findViewById(R.id.cancelButton);
					btnMainCancelAlrm
							.setOnClickListener(btnMainCancelAlrmOnClickListener);

					btnMainSnoozeAlrm = (Button) findViewById(R.id.snoozeButton);
					btnMainSnoozeAlrm
							.setOnClickListener(btnMainSnoozeAlrmOnClickListener);

				}
			});

		}
	};

	private Button.OnClickListener btntraffctxtOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			//

			setContentView(R.layout.traff_param);

			btntraff_Srchctxt = (Button) findViewById(R.id.srchButton);

			TimePicker tp1 = (TimePicker) findViewById(R.id.timePicker_traffparam);
			tp1.setIs24HourView(true);

			btntraff_Srchctxt.setOnClickListener(btntraff_SrchOnClickListener);

			Button btnbacktraff_Srchctxt = (Button) findViewById(R.id.backbuttontraff);
			btnbacktraff_Srchctxt
					.setOnClickListener(btnbacktraff_SrchOnClickListener);
		}
	};

	private Button.OnClickListener btnMainCancelAlrmOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			//

			if (MyAlarmService.r1 != null)
				MyAlarmService.r1.stop();

			snoozer = 0;

			if (flag_bluetooth) {

				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.cancel(pendingIntentAlarmContext);
				flag_bluetooth = false;

			}

			if (flag_traffic) {

				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.cancel(pendingIntentTrafficAlarmContext);
				flag_traffic = false;
			}

			Toast.makeText(MainActivity.this, "Alarm Cancelled",
					Toast.LENGTH_LONG).show();

		}
	};

	private Button.OnClickListener btnbacktraff_SrchOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			//
			setContentView(R.layout.main_application);
			btnbtctxt = (Button) findViewById(R.id.btprox);
			btnbtctxt.setOnClickListener(btnbtctxtOnClickListener);

			btntraffctxt = (Button) findViewById(R.id.traffctxt);
			btntraffctxt.setOnClickListener(btntraffctxtOnClickListener);

			btnMainCancelAlrm = (Button) findViewById(R.id.cancelButton);
			btnMainCancelAlrm
					.setOnClickListener(btnMainCancelAlrmOnClickListener);

			btnMainSnoozeAlrm = (Button) findViewById(R.id.snoozeButton);
			btnMainSnoozeAlrm
					.setOnClickListener(btnMainSnoozeAlrmOnClickListener);

		}
	};

	private Button.OnClickListener btnMainSnoozeAlrmOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			//
			if (flag_bluetooth == false && flag_traffic == false) {
				Toast.makeText(MainActivity.this, "No Alarm Set for Snoozing",
						Toast.LENGTH_LONG).show();
				return;
			}

			if (MyAlarmService.r1 == null) {
				Toast.makeText(MainActivity.this,
						"Alarm Not yet ringing to Snooze", Toast.LENGTH_LONG)
						.show();
				return;
			}

			if (!MyAlarmService.r1.isPlaying()) {
				Toast.makeText(MainActivity.this,
						"Alarm Not yet ringing to Snooze", Toast.LENGTH_LONG)
						.show();
				return;
			}

			if (MyAlarmService.r1 != null)
				MyAlarmService.r1.stop();
			Intent myIntent = new Intent(MainActivity.this,
					MyAlarmService.class);

			if (flag_bluetooth)
				pendingIntentAlarmContext = PendingIntent.getService(
						MainActivity.this, 0, myIntent, 0);

			if (flag_traffic)
				pendingIntentTrafficAlarmContext = PendingIntent.getService(
						MainActivity.this, 0, myIntent, 0);

			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.SECOND, 10);

			if (flag_bluetooth)
				alarmManager.set(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), pendingIntentAlarmContext);

			if (flag_traffic)
				alarmManager.set(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(),
						pendingIntentTrafficAlarmContext);

			snoozer++;

			// as per the flag set in the bluetoth code and snooze code
			if (flag_bluetooth == true) {
				if (snoozer == 1) {

					findDeviceNum();
				}

				if (snoozer == 5) {

					snoozer = 0;
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(phn, null,
							"Please wake me up. I have an exam", null, null); // un-comment
																				// the
																				// code

					/*
					 * System.out.println("SMS sent to " + phn);
					 * System.out.println("SMS is sent");
					 */
				}
			}
			Toast.makeText(MainActivity.this, "Alarm Snoozed",
					Toast.LENGTH_LONG).show();

		}
	};

	private Button.OnClickListener btntraff_SrchOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {

			mEdit = (EditText) findViewById(R.id.srcText);
			src = mEdit.getText().toString();

			mEdit = (EditText) findViewById(R.id.destText);
			dest = mEdit.getText().toString();

			TimePicker tp1 = null;
			tp1 = (TimePicker) findViewById(R.id.timePicker_traffparam);
			timeBusSrch = tp1.getCurrentHour().toString() + "-"
					+ tp1.getCurrentMinute().toString();
			setContentView(R.layout.dispbusinptime);
			tp1 = (TimePicker) findViewById(R.id.timepicker_dispbus);
			tp1.setIs24HourView(true);

			TextView mEdit1;
			mEdit1 = (TextView) findViewById(R.id.busInfoText);
			mEdit1.setTextSize(15);

			transitSystemInformation.getBusDetails(src, dest, timeBusSrch);
			if (transitSystemInformation.bus_info.get("bus_info") != null) {
				mEdit1.setText("\n\n"
						+ transitSystemInformation.bus_info.get("bus_info")
						+ "\nBus Number:"
						+ transitSystemInformation.bus_info.get("bus_number")
						+ "\nDeparture Time:"
						+ transitSystemInformation.bus_info
								.get("departure_time")
						+ "\nArrival Time:"
						+ transitSystemInformation.bus_info.get("arrival_time")
						+ "\nDuration Time:"
						+ transitSystemInformation.bus_info
								.get("duration_time"));
				later_reqd_bus_number = transitSystemInformation.bus_info
						.get("bus_number");
				later_reqd_bus_departure = transitSystemInformation.bus_info
						.get("departure_time_value");

			} else {
				mEdit1.setText("\n\n" + "Bus not Found");
			}

			btntraff_setctxt = (Button) findViewById(R.id.setbutton);
			btntraff_setctxt.setOnClickListener(btntraff_setOnClickListener);

			btntraff_backctxt = (Button) findViewById(R.id.backbutton);
			btntraff_backctxt.setOnClickListener(btntraff_backOnClickListener);

		}
	};

	private Button.OnClickListener btntraff_setOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {

			if (transitSystemInformation.bus_info.isEmpty()) {
				Toast.makeText(MainActivity.this,
						"Alarm cannot be set as no Bus is found.",
						Toast.LENGTH_LONG).show();
				return;
			}

			// set the alarm based on @+id/editText1
			Intent myIntent = new Intent(MainActivity.this,
					MyAlarmService.class);

			pendingIntentTrafficAlarmContext = PendingIntent.getService(
					MainActivity.this, 0, myIntent, 0);

			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

			final TimePicker tp = (TimePicker) findViewById(R.id.timepicker_dispbus);
			int hrs = tp.getCurrentHour();
			int min = tp.getCurrentMinute();

			// =======================================================================
			Calendar calendar = Calendar.getInstance();
			int hrs1 = calendar.get(Calendar.HOUR_OF_DAY);
			int min1 = calendar.get(Calendar.MINUTE);
			int sec1 = calendar.get(Calendar.SECOND);
			String time1 = hrs + ":" + min + ":00";
			String time2 = hrs1 + ":" + min1 + ":00";

			Calendar jhol = Calendar.getInstance();
			jhol.set(Calendar.HOUR_OF_DAY, hrs);
			jhol.set(Calendar.MINUTE, min);
			user_set_time_new = jhol.getTimeInMillis() / 1000;

			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			long difference = 0;
			try {
				Date date1 = format.parse(time1);
				Date date2 = format.parse(time2);

				difference = date1.getTime() - date2.getTime();
				difference = difference / 1000;
				difference = difference - sec1;

				if (difference < 0) {

					time2 = hrs1 + ":" + min1 + ":" + sec1;
					date2 = format.parse(time2);

					String time_new = "23:59:00";
					SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");

					Date date_new = format1.parse(time_new);

					difference = date_new.getTime() - date2.getTime();
					time_new = "00:00:00";
					date_new = format1.parse(time_new);

					difference = difference
							+ (date1.getTime() - date_new.getTime());

					difference = difference / 1000;
					difference = difference + 60;
				}

			} catch (Exception e) {
			}
			// ======================================================================

			time_diff_sec = difference;
			time_diff_hrs = difference / 3600;

			buffer_time_to_get_ready = Long
					.parseLong(transitSystemInformation.bus_info
							.get("departure_time_value"))
					- user_set_time_new;

			int set = (int) difference;

			calendar.setTimeInMillis(System.currentTimeMillis());

			calendar.add(Calendar.SECOND, set);

			alarmManager.set(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(),
					pendingIntentTrafficAlarmContext);

			flag_traffic = true;

			Toast.makeText(MainActivity.this, "Alarm Started",
					Toast.LENGTH_LONG).show();

			// =================================================================
			// ===============================

			Intent trafficIntent = new Intent(MainActivity.this,
					TrafficService.class);

			trafficPendingIntent = PendingIntent.getService(MainActivity.this,
					0, trafficIntent, 0);

			AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
			Calendar calendarTraffic = Calendar.getInstance();
			calendarTraffic.setTimeInMillis(System.currentTimeMillis());

			calendarTraffic.add(Calendar.SECOND, 1);
			alarmManager1.set(AlarmManager.RTC_WAKEUP,
					calendarTraffic.getTimeInMillis(), trafficPendingIntent);
			// ================================

		}

	};

	private Button.OnClickListener btntraff_backOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			//
			setContentView(R.layout.traff_param);

			btntraff_Srchctxt = (Button) findViewById(R.id.srchButton);

			btntraff_Srchctxt.setOnClickListener(btntraff_SrchOnClickListener);

			Button btnbacktraff_Srchctxt = (Button) findViewById(R.id.backbuttontraff);
			btnbacktraff_Srchctxt
					.setOnClickListener(btnbacktraff_SrchOnClickListener);
			TimePicker tp1 = (TimePicker) findViewById(R.id.timePicker_traffparam);
			tp1.setIs24HourView(true);

		}
	};

	private Button.OnClickListener btnRegisterOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			//

			setContentView(R.layout.main);
			btnScanDevice = (Button) findViewById(R.id.scandevice);

			stateBluetooth = (TextView) findViewById(R.id.bluetoothstate);
			bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

			listDevicesFound = (ListView) findViewById(R.id.devicesfound);
			btArrayAdapter = new ArrayAdapter<String>(MainActivity.this,
					android.R.layout.simple_list_item_1);
			listDevicesFound.setAdapter(btArrayAdapter);

			CheckBlueToothState();

			btnScanDevice.setOnClickListener(btnScanDeviceOnClickListener);

			registerReceiver(ActionFoundReceiver, new IntentFilter(
					BluetoothDevice.ACTION_FOUND));

			c = getBaseContext();
			listDevicesFound.setOnItemClickListener(listDevOnClickListener);

			Button buttonBack = (Button) findViewById(R.id.backbutton_scandev);

			buttonBack.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View arg0) {

					setContentView(R.layout.firstscreen);

					btnRegister = (Button) findViewById(R.id.register);
					btnContinue = (Button) findViewById(R.id.Continue);

					btnRegister.setOnClickListener(btnRegisterOnClickListener);
					btnContinue.setOnClickListener(btnContinueOnClickListener);

					Button buttonBack1 = (Button) findViewById(R.id.backbutton_firstscreen);

					buttonBack1
							.setOnClickListener(new Button.OnClickListener() {

								@Override
								public void onClick(View arg0) {

									setContentView(R.layout.main_application);
									btnbtctxt = (Button) findViewById(R.id.btprox);
									btnbtctxt
											.setOnClickListener(btnbtctxtOnClickListener);

									btntraffctxt = (Button) findViewById(R.id.traffctxt);
									btntraffctxt
											.setOnClickListener(btntraffctxtOnClickListener);

									btnMainCancelAlrm = (Button) findViewById(R.id.cancelButton);
									btnMainCancelAlrm
											.setOnClickListener(btnMainCancelAlrmOnClickListener);

									btnMainSnoozeAlrm = (Button) findViewById(R.id.snoozeButton);
									btnMainSnoozeAlrm
											.setOnClickListener(btnMainSnoozeAlrmOnClickListener);

								}
							});

				}
			});

		}
	};

	protected void onDestroy() {
		//
		super.onDestroy();
		try {
			unregisterReceiver(ActionFoundReceiver);
		} catch (Exception e) {
			System.out.println("Unregistered");
		}

	}

	private void CheckBlueToothState() {
		if (bluetoothAdapter == null) {
			stateBluetooth.setText("Bluetooth NOT support");
		} else {
			if (bluetoothAdapter.isEnabled()) {
				if (bluetoothAdapter.isDiscovering()) {
					btArrayAdapter.clear();
					temp_btArrayAdap.clear();
					stateBluetooth
							.setText("Bluetooth is currently in device discovery process.");
				} else {
					btArrayAdapter.clear();
					temp_btArrayAdap.clear();
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
			//
			btArrayAdapter.clear();
			bluetoothAdapter.startDiscovery();
		}
	};

	private ListView.OnItemClickListener listDevOnClickListener = new ListView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
			//

			bt_adrs = btArrayAdapter.getItem(i);
			String arr[] = bt_adrs.split(",");
			btdev.add(cnt, arr[1]);

			setContentView(R.layout.bluetooth_dev_map);

			listContacts = (ListView) findViewById(R.id.BTdevicesList);
			btdevices = new BluetoothViewAdaptor(c);
			listContacts.setAdapter(btdevices);

			listContacts.setOnItemClickListener(contactsOnClickListener);
		}
	};

	private ListView.OnItemClickListener contactsOnClickListener = new ListView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {

			String name = btdevices.names.get(position);

			for (int i = 0; i < btdevices.names.size(); i++) {
				if (btdevices.names.get(i).equalsIgnoreCase(name)) {
					phone_num = btdevices.phn_num.get(i);
					phnnum.add(cnt, phone_num);
					cnt++;
					break;
				}
			}

			/*
			 * for (int i = 0; i < btdev.size(); i++) {
			 * System.out.println("bt: " + btdev.get(i)+ "phn num"+
			 * phnnum.get(i)); }
			 * 
			 * for (int i = 0; i < phnnum.size(); i++) {
			 * System.out.println("phn: " + phnnum.get(i)); }
			 */
			setContentView(R.layout.firstscreen);

			btnRegister = (Button) findViewById(R.id.register);
			btnContinue = (Button) findViewById(R.id.Continue);

			btnRegister.setOnClickListener(btnRegisterOnClickListener);
			btnContinue.setOnClickListener(btnContinueOnClickListener);

			Button buttonBack1 = (Button) findViewById(R.id.backbutton_firstscreen);

			buttonBack1.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View arg0) {

					setContentView(R.layout.main_application);
					btnbtctxt = (Button) findViewById(R.id.btprox);
					btnbtctxt.setOnClickListener(btnbtctxtOnClickListener);

					btntraffctxt = (Button) findViewById(R.id.traffctxt);
					btntraffctxt
							.setOnClickListener(btntraffctxtOnClickListener);

					btnMainCancelAlrm = (Button) findViewById(R.id.cancelButton);
					btnMainCancelAlrm
							.setOnClickListener(btnMainCancelAlrmOnClickListener);

					btnMainSnoozeAlrm = (Button) findViewById(R.id.snoozeButton);
					btnMainSnoozeAlrm
							.setOnClickListener(btnMainSnoozeAlrmOnClickListener);

				}
			});

		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//
		if (requestCode == REQUEST_ENABLE_BT) {
			// CheckBlueToothState();
		}
	}

	private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			//
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				String dname = device.getName() + "," + device.getAddress();
				if (!temp_btArrayAdap.contains(dname)) {
					btArrayAdapter.add(device.getName() + ","
							+ device.getAddress());
					temp_btArrayAdap.add(dname);
					btArrayAdapter.notifyDataSetChanged();
				}

			}
		}
	};

	private Button.OnClickListener btnContinueOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			//

			setContentView(R.layout.activity_android_alarm_service);
			try {
				final TimePicker tp = (TimePicker) findViewById(R.id.timepicker_alarmsvc);
				tp.setIs24HourView(true);
			} catch (Exception e) {
				System.out.println("Exception on 24 hour set");
			}
			Button buttonStart = (Button) findViewById(R.id.startalarm);
			Button buttonCancel = (Button) findViewById(R.id.cancelalarm);
			Button buttonSnooze = (Button) findViewById(R.id.snooze);
			Button buttonBack = (Button) findViewById(R.id.backbutton_alrm);

			buttonStart.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View arg0) {

					Intent myIntent = new Intent(MainActivity.this,
							MyAlarmService.class);

					pendingIntentAlarmContext = PendingIntent.getService(
							MainActivity.this, 0, myIntent, 0);

					AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

					final TimePicker tp = (TimePicker) findViewById(R.id.timepicker_alarmsvc);
					int hrs = tp.getCurrentHour();
					int min = tp.getCurrentMinute();
					c_reset = Calendar.getInstance();
					c_reset.set(Calendar.HOUR_OF_DAY, hrs);
					c_reset.set(Calendar.MINUTE, min);

					// =======================================================================
					Calendar calendar = Calendar.getInstance();
					int hrs1 = calendar.get(Calendar.HOUR_OF_DAY);
					int min1 = calendar.get(Calendar.MINUTE);
					int sec1 = calendar.get(Calendar.SECOND);

					String time1 = hrs + ":" + min + ":00";
					String time2 = hrs1 + ":" + min1 + ":00";

					// to check whether the alarm is set from bluetooth or
					// traffic context app

					SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
					long difference = 0;
					try {
						Date date1 = format.parse(time1);
						Date date2 = format.parse(time2);

						difference = date1.getTime() - date2.getTime();
						difference = difference / 1000;
						difference = difference - sec1;

						if (difference < 0) {

							time2 = hrs1 + ":" + min1 + ":" + sec1;
							date2 = format.parse(time2);

							String time_new = "23:59:00";
							SimpleDateFormat format1 = new SimpleDateFormat(
									"HH:mm:ss");

							Date date_new = format1.parse(time_new);

							difference = date_new.getTime() - date2.getTime();
							time_new = "00:00:00";
							date_new = format1.parse(time_new);

							difference = difference
									+ (date1.getTime() - date_new.getTime());

							difference = difference / 1000;
							difference = difference + 60;
						}

					} catch (Exception e) {
						System.out.println("Exception thrown");
						e.printStackTrace();
					}

					reqd_reset_alarm = (c_reset.getTimeInMillis() / 1000)
							- sec1;
					time_diff_sec = difference;
					time_diff_hrs = difference / 3600;
					// ======================================================================

					int set = (int) difference;

					calendar.setTimeInMillis(System.currentTimeMillis());

					calendar.add(Calendar.SECOND, set);

					alarmManager.set(AlarmManager.RTC_WAKEUP,
							calendar.getTimeInMillis(),
							pendingIntentAlarmContext);

					flag_bluetooth = true;

					Toast.makeText(MainActivity.this, "Alarm Started",
							Toast.LENGTH_LONG).show();

				}
			});

			// ===================================================================================================================

			buttonCancel.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View arg0) {

					if (MyAlarmService.r1 != null)
						MyAlarmService.r1.stop();

					snoozer = 0;

					AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
					alarmManager.cancel(pendingIntentAlarmContext);
					// Tell the user about what we did.
					Toast.makeText(MainActivity.this, "Alarm Cancelled",
							Toast.LENGTH_LONG).show();

				}
			});

			// ==================================================================================================================

			buttonSnooze.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View arg0) {

					if (flag_bluetooth == false) {
						Toast.makeText(MainActivity.this,
								"No Alarm Set for Snoozing", Toast.LENGTH_LONG)
								.show();
						return;
					}

					if (MyAlarmService.r1 == null) {
						Toast.makeText(MainActivity.this,
								"Alarm Not yet ringing to Snooze",
								Toast.LENGTH_LONG).show();
						return;
					}

					if (!MyAlarmService.r1.isPlaying()) {
						Toast.makeText(MainActivity.this,
								"Alarm Not yet ringing to Snooze",
								Toast.LENGTH_LONG).show();
						return;
					}

					if (MyAlarmService.r1 != null)
						MyAlarmService.r1.stop();

					Intent myIntent = new Intent(MainActivity.this,
							MyAlarmService.class);
					pendingIntentAlarmContext = PendingIntent.getService(
							MainActivity.this, 0, myIntent, 0);
					AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(System.currentTimeMillis());
					calendar.add(Calendar.SECOND, 10);
					alarmManager.set(AlarmManager.RTC_WAKEUP,
							calendar.getTimeInMillis(),
							pendingIntentAlarmContext);
					snoozer++;

					if (snoozer == 1) {

						findDeviceNum();
					}

					if (snoozer == 5) {

						// TODO Uncomment SMS sending code and Send SMS
						snoozer = 0;
						SmsManager smsManager = SmsManager.getDefault();
						smsManager.sendTextMessage(phn, null,
								"Please wake me up. I have an exam today.",
								null, null);
						/*
						 * System.out.println("SMS sent to " + phn);
						 * System.out.println("SMS is sent");
						 */
					}

					Toast.makeText(MainActivity.this, "Alarm Snoozed",
							Toast.LENGTH_LONG).show();

				}
			});

			// ===================================================================================================================
			buttonBack.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View arg0) {

					setContentView(R.layout.firstscreen);
					btnRegister = (Button) findViewById(R.id.register);
					btnContinue = (Button) findViewById(R.id.Continue);

					btnRegister.setOnClickListener(btnRegisterOnClickListener);
					btnContinue.setOnClickListener(btnContinueOnClickListener);

					Button buttonBack1 = (Button) findViewById(R.id.backbutton_firstscreen);

					buttonBack1
							.setOnClickListener(new Button.OnClickListener() {

								@Override
								public void onClick(View arg0) {

									setContentView(R.layout.main_application);
									btnbtctxt = (Button) findViewById(R.id.btprox);
									btnbtctxt
											.setOnClickListener(btnbtctxtOnClickListener);

									btntraffctxt = (Button) findViewById(R.id.traffctxt);
									btntraffctxt
											.setOnClickListener(btntraffctxtOnClickListener);

									btnMainCancelAlrm = (Button) findViewById(R.id.cancelButton);
									btnMainCancelAlrm
											.setOnClickListener(btnMainCancelAlrmOnClickListener);

									btnMainSnoozeAlrm = (Button) findViewById(R.id.snoozeButton);
									btnMainSnoozeAlrm
											.setOnClickListener(btnMainSnoozeAlrmOnClickListener);

								}
							});

				}
			});

		}
	};

	public void findDeviceNum() {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		registerReceiver(ActionFoundReceiver1, new IntentFilter(
				BluetoothDevice.ACTION_FOUND));
		bluetoothAdapter.startDiscovery();
	}

	private final BroadcastReceiver ActionFoundReceiver1 = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			//
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				for (int i = 0; i < btdev.size(); i++) {
					if (btdev.get(i).equalsIgnoreCase(device.getAddress())) {
						phn = phnnum.get(i);
					}
				}
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
