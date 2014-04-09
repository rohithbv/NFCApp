package com.example.nfcapp.activities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfcapp.R;

public class UserRegistrationActivity extends Activity {

	private String NFCText;
	private int trigger;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_registration);
		// Show the Up button in the action bar.
		//setupActionBar();
		
		Intent intent = getIntent();
		trigger = intent.getIntExtra(MainActivity.TRIGGER, -1);
		if (trigger == MainActivity.TAP_TRIGGER) {
			Toast.makeText(this, "Please register to proceed", Toast.LENGTH_SHORT).show();
			NFCText = new String(intent.getStringExtra(MainActivity.NFC_DATA));
		} else if (trigger == MainActivity.ERROR) {
			Toast.makeText(this, "ERROR: Please restart app", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			Toast.makeText(this, "Please register to proceed", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void registerUser(View view) {
		String username = new String(((EditText)findViewById(R.id.usernameField)).getText().toString());
		String password = new String(((EditText)findViewById(R.id.passwordField)).getText().toString());
		writeDataToFile(username, password);
		createPostRegistrationLayout();
		nextOperation();
		finish();
	}
	
	public void writeDataToFile(String username, String password){
		String filename = new String("NFCAppPrivateData");
		String text = new String(username + "\n" + password + "\n");
		FileOutputStream outputStream;
		
		try {
			outputStream = openFileOutput(filename, Context.MODE_APPEND);
			outputStream.write(text.getBytes(), 0, text.length());
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		filename = null;
		text = null;
	}
	
	public void createPostRegistrationLayout() {
		LinearLayout postRegistrationLayout = new LinearLayout(this);
		postRegistrationLayout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams postRegistrationLayoutParams = new LayoutParams();
		postRegistrationLayout.setLayoutParams(postRegistrationLayoutParams);
		
		TextView displayText = new TextView(this);
		displayText.setText("Registration Successful");
		displayText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
		displayText.setGravity(LinearLayout.HORIZONTAL | LinearLayout.VERTICAL);
		postRegistrationLayout.addView(displayText);
		setContentView(postRegistrationLayout);
	}
	
	public void nextOperation() {
		if (trigger == MainActivity.TAP_TRIGGER) {
			sendDataToServer();
		} else if (trigger == MainActivity.APP_OPEN_TRIGGER) {
			displayRoomStatus();
		}
	}
	
	public void sendDataToServer() {
		Intent intentToSend = new Intent(this, SendDataToServerActivity.class);
		intentToSend.putExtra(MainActivity.SEND_DATA_TO_SERVER, NFCText);
		startActivity(intentToSend);
		finish();
	}
	
	public void displayRoomStatus() {
		//Toast.makeText(this, "Displaying Room Status", Toast.LENGTH_SHORT).show();
		Intent intentToDisplay = new Intent(this, DisplayRoomStatusActivity.class);
		startActivity(intentToDisplay);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	/*private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_registration, menu);
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

}
