package com.dkhs.geoalert;

import com.dkhs.geoalert.allert.Alarm;
import com.dkhs.geoalert.allert.AlarmManager;
import com.dkhs.geoalert.view.AlertView;

import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

public class AddAlarm extends Activity {
	
	public final static int POINT_IN_MAP_REQUEST = 5566;
	

	private AlarmManager man;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_alarm);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_alarm, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected Alarm isValid(View view){
		
		
		
    	EditText latitudeIn = (EditText) findViewById(R.id.latitudeTxt);
    	String latitude = latitudeIn.getText().toString();
    	if(!isTextSet(latitude)){
    		showFillFieldsToast();
    		return null;
    	}
    	if(!isGeoLocOk(latitude, true)){
    		showWrongGeoToast();
    		return null;
    	}
    	
    	EditText longtitudeIn = (EditText) findViewById(R.id.longtitudeTxt);
    	String longtitude = longtitudeIn.getText().toString();  	
    	if(!isTextSet(longtitude)){
    		showFillFieldsToast();
    		return null;
    	}
    	if(!isGeoLocOk(longtitude, false)){
    		showWrongGeoToast();
    		return null;
    	}	
    	
    	
    	Double latD = Double.parseDouble(latitude);
    	Double lonD = Double.parseDouble(longtitude);
    	
    	EditText radiusIn = (EditText) findViewById(R.id.radiusTxt);
    	
    	if(!isTextSet(radiusIn.getText().toString())){
    		showFillFieldsToast();
    		return null;
    	}
    	
    	Integer radius = Integer.parseInt(radiusIn.getText().toString());
    	
    	
    	EditText nameIn = (EditText) findViewById(R.id.nameTxt);
    	String name = nameIn.getText().toString();
    	if(!isTextSet(name)){
    		showFillFieldsToast();
    		return null;
    	}
    	
    	EditText reminderIn = (EditText) findViewById(R.id.reminderTxt);
    	String reminder= reminderIn.getText().toString();
    	
    	//Create Alarm
    	Alarm alarm = new Alarm();
    	alarm.setLatitude(latD);
    	alarm.setLongtitude(lonD);
    	alarm.setText(reminder);
    	alarm.setTitle(name);
    	alarm.setRadius(radius);
    	
    	return alarm;
	}
	
	public void showFillFieldsToast(){
		
		Context context = getApplicationContext();
		CharSequence text = getString(R.string.fillMandatoryFields);
		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	public void showWrongGeoToast(){
		Context context = getApplicationContext();
		CharSequence text = getString(R.string.fillGeoRightData);
		int duration = Toast.LENGTH_LONG;
		
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public boolean isTextSet(String text){
		if(text==null) return false;
		if(text.length()<1) return false;
		return true;
		
	}
	
	public boolean isGeoLocOk(String geoloc,boolean latitude){
		Double geo=-1.0; 
		try {
			geo=Double.parseDouble(geoloc);
		} catch (NumberFormatException e) {
			return false;
		}
		if(latitude){
			if(geo>90) return false;
			if(geo<-90) return false;
		}else{
			if(geo>180) return false;
			if(geo<-180) return false;
			
		}
		return true;
	}
	
	public void pointOnMap(View view){
		Intent mapIntent = new Intent(this.getApplicationContext(),MapActivity.class );
		
    	EditText latitudeIn = (EditText) findViewById(R.id.latitudeTxt);
    	String latitude = latitudeIn.getText().toString();
    	if(isTextSet(latitude) && isGeoLocOk(latitude, true)){
    		mapIntent.putExtra(MainActivity.LAT, Double.parseDouble(latitude));
    		
    	}
    	
    	EditText longtitudeIn = (EditText) findViewById(R.id.longtitudeTxt);
    	String longtitude = longtitudeIn.getText().toString();  	
    	if(isTextSet(longtitude) && isGeoLocOk(longtitude, false)){
    		mapIntent.putExtra(MainActivity.LON, Double.parseDouble(longtitude));
    	}	
    	
		
		startActivityForResult(mapIntent,POINT_IN_MAP_REQUEST);
	}
	
	public void setAllert(View view){
		

	    	
//	    	EditText latitudeIn = (EditText) findViewById(R.id.latitudeTxt);
//	    	String latitude = latitudeIn.getText().toString();
//	    	EditText longtitudeIn = (EditText) findViewById(R.id.longtitudeTxt);
//	    	String longtitude = longtitudeIn.getText().toString();  	
//	    	//intent.putExtra(LAT, latitude);
//	    	//intent.putExtra(LON, longtitude);
//	    	Double latD = Double.parseDouble(latitude);
//	    	Double lonD = Double.parseDouble(longtitude);
//	    	
//	    	EditText radiusIn = (EditText) findViewById(R.id.radiusTxt);
//	    	Integer radius = Integer.parseInt(radiusIn.getText().toString());
//	    	
//	    	EditText nameIn = (EditText) findViewById(R.id.nameTxt);
//	    	String name = nameIn.getText().toString();
//	    	
//	    	EditText reminderIn = (EditText) findViewById(R.id.reminderTxt);
//	    	String reminder= reminderIn.getText().toString();
	    	
	    	
	    	
	    	//Alarms are recognized by uniqueID!!!
	    	
			Alarm alarm = isValid(view);
			
			if(alarm==null) return;
		
			man=AlarmManager.getInstance(null, this);
			
	    	Intent intent = new Intent(this.getApplicationContext(), AlertActivity.class);
	    	
	    	int alarmId = man.getNxtUniqueID();
	    	
	    	alarm.setUniqueID(alarmId);
	    	
	    	intent.putExtra(MainActivity.ALARM_ID, alarmId);
	    	
	    	LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	    	
	    	//ask user if he would like to enable gps
	    	AlertDialog dial=showGPSEnableIfNecessary(locationManager);
	    	
	    	
	    	
	    	
	    	

	    	
	    	
	    	
	    	
	    	//important add alarm to manager to save and cancel 
	    	man.addAlarm(alarm);
	    	man.store(null, this.getApplicationContext());
	    	
	    	
	    	//creates an alarm view in the list
	    	//final LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayoutAlarms);

	    	//AlertView viewA = new AlertView (this,alarm,layout,this);
	    	
	    	//layout.addView(viewA);
	    	
	    	
	    	
//	    	final TextView textview = new TextView(this);
//	    	//textview.setWidth(LayoutParams.MATCH_PARENT);
//	    	//textview.setHeight(LayoutParams.WRAP_CONTENT);
//	    	textview.setTextSize(14);
//	    	textview.setText("lat:"+latD+"|lon:"+lonD);
//	    	textview.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					layout.removeView(textview);
//					
//				}
//			});
//	    	layout.addView(textview);
	    	
	    	
	    	//PendingIntent pi1=PendingIntent.getActivity(this, 1, intent, 0);
	    	
	    	//locationManager.addProximityAlert(latD, lonD, 500, -1, pi);
	    	
	    	//startActivity(intent);
	    	
	    	
	    	
	    	
	    	//adding pending intent
	    	PendingIntent pi=PendingIntent.getActivity(this.getApplicationContext(), alarmId, intent, 0);
	    	
	    	locationManager.addProximityAlert(alarm.getLatitude(), alarm.getLongtitude(), alarm.getRadius(), -1, pi);
	    	
	    	
	    	if(dial!=null && dial.isShowing()){
	    		dial.setOnDismissListener(new DialogInterface.OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						AddAlarm.this.finish();
						
					}
				});
	    	}else{
	    		this.finish();
	    	}
	    	
		}
	
	public void cancelOperation(View view){
		this.finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == POINT_IN_MAP_REQUEST) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	            // The user picked a contact.
	            // The Intent's data Uri identifies which contact was selected.

	            // Do something with the contact here (bigger example below)
	        	double lat=data.getDoubleExtra(MainActivity.LAT,0);
	        	double lon=data.getDoubleExtra(MainActivity.LON,0);
	        	//Toast.makeText(AddAlarm.this, "lat:"+lat+"\nlgn:"+lon, Toast.LENGTH_LONG).show();
	        	
	        	EditText latitudeIn = (EditText) findViewById(R.id.latitudeTxt);
	        	latitudeIn.setText(Double.toString(lat));

	        	
	        	EditText longtitudeIn = (EditText) findViewById(R.id.longtitudeTxt);
	        	longtitudeIn.setText(Double.toString(lon));
	        	
	        	
	        }else{
	        	Toast.makeText(AddAlarm.this, "location failed", Toast.LENGTH_LONG).show();
	        }
	    }
	}
	
	protected AlertDialog showGPSEnableIfNecessary(LocationManager locationManager){
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
            return dial;
    	}
    	return null;
	}
	
	public void enableLocationSettings() {
	    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    startActivity(settingsIntent);
	}
	

}
