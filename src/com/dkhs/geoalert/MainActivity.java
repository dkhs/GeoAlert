package com.dkhs.geoalert;

import java.util.List;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.dkhs.geoalert.allert.Alarm;
import com.dkhs.geoalert.allert.AlarmManager;
import com.dkhs.geoalert.view.AlertView;

public class MainActivity extends FragmentActivity implements AlarmIntentManager {

	public static final String LAT = "com.dkhs.geoalert.LATITUDE.id";
	public static final String LON = "com.dkhs.geoalert.LONGTITUDE.id";
	public static final String ALARM_ID = "com.dkhs.geoalert.geoalert.id";
	
	
	private AlarmManager man;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public MainActivity() {
		super();
	}
	
	
	public void addAllert(View view){
		
		Intent intent = new Intent(this, AddAlarm.class);
		startActivity(intent);
	}
	
//	public void setAllert(View view){
//		
////    	Intent intent = new Intent(this, AlertActivity.class);
////    	
////    	LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
////    	//ask user if he would like to enable gps
////    	showGPSEnableIfNecessary(locationManager);
////    	
////    	EditText latitudeIn = (EditText) findViewById(R.id.latitudeInput);
////    	String latitude = latitudeIn.getText().toString();
////    	EditText longtitudeIn = (EditText) findViewById(R.id.longtitudeEdit);
////    	String longtitude = longtitudeIn.getText().toString();  	
////    	intent.putExtra(LAT, latitude);
////    	intent.putExtra(LON, longtitude);
////    	Double latD = Double.parseDouble(latitude);
////    	Double lonD = Double.parseDouble(longtitude);
//    	
//    	
//    	
////    	//Alarms are recognized by uniqueID!!!
////    	int alarmId = man.getNxtUniqueID();
////    	intent.putExtra(ALARM_ID, alarmId);
////    	
////
////    	
////    	
////    	
////    	
////    	//dummy now
////    	String title ="title";
////    	String text= "text";
////    	
////    	//Create Alarm
////    	Alarm alarm = new Alarm();
////    	alarm.setLatitude(latD);
////    	alarm.setLongtitude(lonD);
////    	alarm.setText(text);
////    	alarm.setTitle(title);
////    	alarm.setUniqueID(alarmId);
////    	
////    	
////    	
////    	//important add alarm to manager to save and cancel 
////    	man.addAlarm(alarm);
//    	
//    	
//    	//creates an alarm view in the list
//    	final LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayoutAlarms);
//
//    	AlertView viewA = new AlertView (this,alarm,layout,this);
//    	
//    	layout.addView(viewA);
//    	
//    	
//    	
////    	final TextView textview = new TextView(this);
////    	//textview.setWidth(LayoutParams.MATCH_PARENT);
////    	//textview.setHeight(LayoutParams.WRAP_CONTENT);
////    	textview.setTextSize(14);
////    	textview.setText("lat:"+latD+"|lon:"+lonD);
////    	textview.setOnClickListener(new View.OnClickListener() {
////			
////			@Override
////			public void onClick(View v) {
////				layout.removeView(textview);
////				
////			}
////		});
////    	layout.addView(textview);
//    	
//    	
//    	//PendingIntent pi1=PendingIntent.getActivity(this, 1, intent, 0);
//    	
//    	//locationManager.addProximityAlert(latD, lonD, 500, -1, pi);
//    	
//    	//startActivity(intent);
//    	
//    	
//    	
//    	
//    	
//    	//adding pending intent
//    	PendingIntent pi=PendingIntent.getActivity(this, alarmId, intent, 0);
//    	
//    	locationManager.addProximityAlert(latD, lonD, 20, -1, pi);
//    	
//	}
	
	public void removeAlarmAndPendingIntent(Alarm a){
		//remove pending intent
		Intent intent = new Intent(this, AlertActivity.class);
		PendingIntent pi=PendingIntent.getActivity(this, a.getUniqueID(), intent, 0);
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeProximityAlert(pi);
		//remove alarm from manager
		man.removeAlarm(a);
	}
	
	
	protected void showGPSEnableIfNecessary(LocationManager locationManager){
    	final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	
    	if (!gpsEnabled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.enableGPS)
                   .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                    	   enableLocationSettings();
                       }
                   })
                   .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                    	   dialog.dismiss();
                       }
                   });
            // Create the AlertDialog object and return it
            AlertDialog dial=builder.create();
            dial.show();
    	}
	}

	
	public void enableLocationSettings() {
	    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    startActivity(settingsIntent);
	}
	
	
	
	
	@Override
	protected void onPause() {
		super.onPause();
	}





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		man=AlarmManager.getInstance(savedInstanceState,this);
		rebuildView();
	}
	
	
	
	

	@Override
	protected void onResume() {	
		super.onResume();
		man=AlarmManager.getInstance(null,this);
		LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayoutAlarms);
		layout.removeAllViews();
		rebuildView();
	}





	private void rebuildView() {
		List<Alarm> alarms = man.getAlarms();
		final LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayoutAlarms);
		for(Alarm alarm : alarms){
	    	AlertView viewA = new AlertView (this,alarm,layout,this);
	    	layout.addView(viewA);
		}
		
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(man==null) man=AlarmManager.getInstance(savedInstanceState, this);
		
	}



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		man.store(outState,this);
	}

}
