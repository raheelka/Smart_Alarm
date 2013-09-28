package com.example.socialctxt_alarm;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

public class MyAlarmService extends Service {

	static Ringtone r1;

	@Override
	public void onCreate() {
		Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG)
				.show();
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG)
				.show();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Uri notification = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
				notification);
		r1 = r;
		r.play();
		Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG)
				.show();
		return super.onUnbind(intent);
	}

}