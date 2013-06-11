package com.dkhs.geoalert;

import com.dkhs.geoalert.allert.Alarm;
import com.dkhs.geoalert.allert.AlarmManager;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class AlertActivity extends Activity {
	
	Ringtone r=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
	            + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
	            + WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
	            + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		setContentView(R.layout.activity_alert);
		Intent intent = getIntent();
		//String lat=intent.getStringExtra(MainActivity.LAT);
		//String lon=intent.getStringExtra(MainActivity.LON);
		TextView txt = (TextView)findViewById(R.id.alertText);
		int alarmID=intent.getIntExtra(MainActivity.ALARM_ID, -1);
		if(alarmID==-1) txt.setText("kurwa nie odzyskalem pierdolonego ID!!");
		
		if(alarmID!=-1){
			Alarm a = AlarmManager.getInstance(null, this).getAlarm(alarmID);
			if(a!=null){
				
				txt.setText("name:"+a.getTitle()+"lat:"+a.getLatitude()+";\nlon:"+a.getLongtitude());
				//AlarmManager.getInstance(null, this).store(null, this);
				
				//play a default ringtone
				Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			     if(alarmSound == null){
			         // alert is null, using backup
			    	 alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			         if(alarmSound == null){  // I can't see this ever being null (as always have a default notification) but just incase
			             // alert backup is null, using 2nd backup
			        	 alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);               
			         }
			     }
				r = RingtoneManager.getRingtone(this, alarmSound);
				r.play();
			}
		}
	}

	public void stopAlarm(View view){
		System.out.print(true);
		if(r!=null && r.isPlaying()){
			r.stop();
		}
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alert, menu);
		return true;
	}

}
