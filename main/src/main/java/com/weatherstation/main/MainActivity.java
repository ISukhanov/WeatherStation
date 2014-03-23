package com.weatherstation.main;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity
	implements NavigationDrawerFragment.NavigationDrawerCallbacks, SensorEventListener
{

	private SensorManager mSensorManager;
	private Sensor mTemperature;
	private Sensor mHumidity;
	private Sensor mPressure;

	@Override
	public void onSensorChanged(SensorEvent sensorEvent)
	{
		TextView sensorGraph;
		switch (sensorEvent.sensor.getType())
		{
			case Sensor.TYPE_AMBIENT_TEMPERATURE:
				sensorGraph = (TextView) findViewById(R.id.temperature);
				break;
			case Sensor.TYPE_RELATIVE_HUMIDITY:
				sensorGraph = (TextView) findViewById(R.id.humidity);
				break;
			case Sensor.TYPE_PRESSURE:
				sensorGraph = (TextView) findViewById(R.id.pressure);
				break;
			default:
				sensorGraph = null;
		}
		if (sensorGraph != null)
			sensorGraph.setText(Float.toString(sensorEvent.values[0]));

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i)
	{

	}

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment)
			getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		mHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(
			R.id.navigation_drawer,
			(DrawerLayout) findViewById(R.id.drawer_layout)
		                               );
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mHumidity, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position)
	{
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
		               .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
		               .commit();
	}

	public String getSectionString(int number)
	{
		String res;
		switch (number)
		{
			case 1:
				res = getString(R.string.main);
				break;
			case 2:
				res = getString(R.string.forecast);
				break;
			default:
				res = "";
		}
		return res;
	}

	public void onSectionAttached(int number)
	{
		mTitle = this.getSectionString(number);
	}

	public void restoreActionBar()
	{
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if (!mNavigationDrawerFragment.isDrawerOpen())
		{
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment
	{
		/**
		 * The fragment argument representing the section number for this fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber)
		{
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment()
		{
		}

		@Override
		public View onCreateView(
			LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState
		                        )
		{

			int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
			int fragment;
			switch (sectionNumber)
			{
				case 1:
				default:
					fragment = R.layout.fragment_main;
					break;
				case 2:
					fragment = R.layout.fragment_forecast;
					break;
			}

			View rootView = inflater.inflate(fragment, container, false);
//			TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//			textView.setText(Integer.toString());
			return rootView;
		}

		@Override
		public void onAttach(Activity activity)
		{
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(
				getArguments().getInt(ARG_SECTION_NUMBER)
			                                           );
		}
	}

}
