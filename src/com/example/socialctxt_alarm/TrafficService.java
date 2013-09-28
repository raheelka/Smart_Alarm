package com.example.socialctxt_alarm;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class TrafficService extends Service {

	static int call_count = 0;
	static int skipper = 0;
	static transitSystemInformation traffic_context;

	@Override
	public void onCreate() {
		Toast.makeText(this, "Traffic Service Create", Toast.LENGTH_LONG)
				.show();

	}

	@Override
	public IBinder onBind(Intent intent) {
		Toast.makeText(this, "Traffic Service.onBind()", Toast.LENGTH_LONG)
				.show();
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "TrafficService.onDestroy()", Toast.LENGTH_LONG)
				.show();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		long current_system_time = System.currentTimeMillis() / 1000;
		if (MainActivity.user_set_time_new > current_system_time) {
			transitSystemInformation tsi = new transitSystemInformation();
			long alarm_reset_time = tsi
					.check_transit_system_and_update_alarm(transitSystemInformation.user_set_time);
			if (alarm_reset_time > 0) {
				Intent myIntent = new Intent(this, MyAlarmService.class);

				MainActivity.pendingIntentTrafficAlarmContext = PendingIntent
						.getService(this, 0, myIntent, 0);

				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

				int set = (int) alarm_reset_time;

				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());

				calendar.add(Calendar.SECOND, set);
				MainActivity.user_set_time_new = System.currentTimeMillis()
						/ 1000 + set;

				alarmManager.set(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(),
						MainActivity.pendingIntentTrafficAlarmContext);
			}

			Toast.makeText(this, "Traffic Service Started", Toast.LENGTH_LONG)
					.show();

			Intent latestintent = new Intent(this, this.getClass());
			PendingIntent reqdPendingIntent = PendingIntent.getService(this, 0,
					latestintent, 0);

			Calendar calendarTraffic = Calendar.getInstance();
			calendarTraffic.setTimeInMillis(System.currentTimeMillis());

			calendarTraffic.add(Calendar.SECOND, 3600);

			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					calendarTraffic.getTimeInMillis(), reqdPendingIntent);
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {

		Toast.makeText(this, "TrafficService.onUnbind()", Toast.LENGTH_LONG)
				.show();
		return super.onUnbind(intent);
	}

}
