package com.dhliwayok.hopeworldwide;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class LoginActivity extends Activity {
	Button btnLogin;
	Button btnLinkToRegister;
	EditText username;
	EditText password;
	TextView loginErrorMsg;
	HopeDbAdapter mydb;
	private String name;
	private MySharePref mypref;
	private MyConnect connect;
	private static int center;
	BroadcastReceiver r;
	
	
	// JSON Response node names
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login2);
		
		mypref = new MySharePref(this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLEAR_STACK");
		
		r = new ActivitiesBroadcastReceiver();
		registerReceiver(r, intentFilter);

		// Importing all assets like buttons, text fields
		username = (EditText) findViewById(R.id.loginEmail);
		password = (EditText) findViewById(R.id.loginPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);
		mydb = new HopeDbAdapter(this);
		connect = new MyConnect(mydb);
	
		if(mypref.checkKey("username") && mypref.checkKey("password")){
			try {
				startActivity(new Intent(LoginActivity.this, Menu.class));
				name = mypref.getMyPref("Name");
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String usename = username.getText().toString();
				String passWord = password.getText().toString();
				//UserFunctions userFunction = new UserFunctions();
				Log.d("Button", "Login");

				if (username.length() > 0 && password.length() > 0) {
					try {

						if (mydb.Login(usename, passWord)) {
							mypref.saveMyPrefValue("username", usename);
							mypref.saveMyPrefValue("password", passWord);
							//Message.message(LoginActivity.this, "Successfully Logged In");
							try {
								startActivity(new Intent(LoginActivity.this, SearchECD.class));
								name = mydb.LoginName(usename, passWord);
								mypref.saveMyPrefValue("Name", name);
								//center = mydb.getCenter(name);
								
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						} else {
							//Message.message(LoginActivity.this,"Invalid username or password");
							loginErrorMsg.setTextColor(Color.RED);
							loginErrorMsg.setText("Invalid username or password");
					}

					} catch (Exception e) {
						//Message.message(LoginActivity.this,"Some problem occurred"+e); 
						loginErrorMsg.setTextColor(Color.RED);
						loginErrorMsg.setText("Some problem occurred"+e);
					}
				} else {
					//Message.message(LoginActivity.this,"Username or Password is empty");
					loginErrorMsg.setTextColor(Color.RED);
					loginErrorMsg.setText("Empty Username or Password");
					//JSONObject json = userFunction.loginUser(email, password);

					// check for login response
				}
			}

		});
		

	}		// Link to Register Screen
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {//if back button pressed, exit

		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction("CLEAR_STACK");
			sendBroadcast(broadcastIntent);
			mypref.clearPref();
			mydb.clearAll();
			}
		return super.onKeyDown(keyCode, event);
	}
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(r);
		super.onDestroy();
	}



	class ActivitiesBroadcastReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			finish();
		}
	}
	
	
	
	
	
	
	
}




