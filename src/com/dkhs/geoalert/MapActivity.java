package com.dkhs.geoalert;

import java.util.List;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity {

	public final static double NO_GEO = 99999;

	GoogleMap map;
	ProgressDialog progDailog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		// Show the Up button in the action bar.
		setupActionBar();

		double lat = getIntent().getDoubleExtra(MainActivity.LAT, NO_GEO);
		double lon = getIntent().getDoubleExtra(MainActivity.LON, NO_GEO);

		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);

		map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng arg0) {
				Intent ret = new Intent();
				ret.putExtra(MainActivity.LAT, arg0.latitude);
				ret.putExtra(MainActivity.LON, arg0.longitude);
				setResult(RESULT_OK, ret);
				finish();
			}
		});

		progDailog = new ProgressDialog(MapActivity.this);
		progDailog.setMessage(getString(R.string.loadingString));
		progDailog.setIndeterminate(false);
		progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDailog.setCancelable(true);

		LatLng coordinate = null;
		if (lat != NO_GEO && lon != NO_GEO) {
			coordinate = new LatLng(lat, lon);
			map.addMarker(new MarkerOptions().position(coordinate).title(
					getString(R.string.alertMapMarkerString)));
		} else {
			coordinate = getLastLocation();
		}
		if (coordinate != null) {
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(coordinate) // Sets the center of the map to
										// Mountain View
					.zoom(14) // Sets the zoom
					.bearing(0) // Sets the orientation of the camera to east
					.tilt(0) // Sets the tilt of the camera to 30 degrees
					.build(); // Creates a CameraPosition from the builder
			map.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		}
		Toast.makeText(MapActivity.this,
				getString(R.string.holdLocationOnMapToChoose),
				Toast.LENGTH_LONG).show();
	}

	public void searchMap(View view) {
		try {
			EditText addressSearchText = (EditText) findViewById(R.id.mapSearchText);
			String addressStr = addressSearchText.getText().toString();
			if (addressStr != null && addressStr.length() > 1) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(addressSearchText.getWindowToken(),
						0);

				new LoadingAddress().execute(addressStr);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
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
		getMenuInflater().inflate(R.menu.map, menu);
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

	private LatLng getLastLocation() {
		LatLng coordinate = null;
		Criteria criteria = new Criteria();
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		if (location == null)
			location = map.getMyLocation();
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		}
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			coordinate = new LatLng(lat, lng);
		}
		return coordinate;
	}

	class LoadingAddress extends AsyncTask<String, Void, List<Address>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progDailog.show();

		}

		@Override
		protected List<Address> doInBackground(String... location) {
			List<Address> ret = null;
			try {

				Geocoder geocoder = new Geocoder(MapActivity.this);
				ret = geocoder.getFromLocationName(location[0], 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ret;
		}

		@Override
		protected void onPostExecute(List<Address> results) {
			super.onPostExecute(results);
			progDailog.dismiss();
			if (results.size() == 0) {
				Toast.makeText(MapActivity.this,
						getString(R.string.mapSearchLocationNotFound),
						Toast.LENGTH_LONG).show();

			}

			if (map != null) {
				Address address = results.get(0);
				double lat = address.getLatitude();
				double lon = address.getLongitude();

				LatLng coordinate = new LatLng(lat, lon);
				map.addMarker(new MarkerOptions().position(coordinate).title(
						getString(R.string.alertMapMarkerString)));
				if (coordinate != null) {
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(coordinate) // Sets the center of the map to
												// Mountain View
							.zoom(14) // Sets the zoom
							.bearing(0) // Sets the orientation of the camera to
										// east
							.tilt(0) // Sets the tilt of the camera to 30
										// degrees
							.build(); // Creates a CameraPosition from the
										// builder
					map.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));
				}
			}
		}
	}

}
