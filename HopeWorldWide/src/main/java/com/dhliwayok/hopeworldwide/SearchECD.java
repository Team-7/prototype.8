package com.dhliwayok.hopeworldwide;

import java.util.ArrayList;

import com.dhliwayok.hopeworldwide.BMI_Activity.ActivitiesBroadcastReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class SearchECD extends Activity {
	
	private ArrayList <String> theArray;
	private ArrayAdapter<String> adapter;
	private HopeDbAdapter myDb;
	private AutoCompleteTextView searchText;
	private TextView childView;
	private Button search;
	private String txt;
	private MyConnect connect;
	private static String CENTERNAME;
	private int centre;
	private MySharePref myPref;
	BroadcastReceiver r;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select);
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLEAR_STACK");
		
		r = new ActivitiesBroadcastReceiver();
		registerReceiver(r, intentFilter);
		myPref = new MySharePref(this);
		myDb = new HopeDbAdapter(this);
		connect = new MyConnect(myDb);
		
		searchText = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		search = (Button) findViewById(R.id.button1);
		childView = (TextView) findViewById(R.id.notFound);
		
		theArray = new ArrayList <String>();
		theArray=myDb.ECDNames();		
		adapter= new ArrayAdapter<String> (SearchECD.this,R.layout.dropdwn_textview ,theArray);
		// specify the layout
		searchText.setThreshold(2); // determine the number of character to enter before the guess
		searchText.setAdapter(adapter); // link the ArrayList to the Adapter
		
		
		search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(searchText.getText().toString().trim() != null){
				txt = searchText.getText().toString();
				
				if(!myDb.displayECDName(txt).equals("")){	//check name typed is in database
				
					//childView.setText(txt);
					CENTERNAME = myDb.displayECDName(txt);
				    centre = myDb.getCenter(txt);
				    myPref.saveMyPrefValue("CenterName", CENTERNAME);
				    new GetInfo().execute();
				    Intent mainIntent = new Intent(SearchECD.this, Menu.class);
	                SearchECD.this.startActivity(mainIntent);
	                SearchECD.this.finish();
				}
				else{
					childView.setText("No ECD found");

					}
				}else{
					childView.setText("Enter ECD name");
				}
				
				
			
			}
		});


	}

	
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {//if back button pressed, exit

		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			try {
				 myPref.clearPref();
					startActivity(new Intent(SearchECD.this, LoginActivity.class));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 //myDb.clearAll();
		}
		return super.onKeyDown(keyCode, event);
		
	}

	class ActivitiesBroadcastReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			finish();
		}
	}
	
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(r);
		super.onDestroy();
	}

	private class GetInfo extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			if(connect.parseJSON(centre))
				System.out.println("successful"+centre);
			
			if(connect.getBmi(centre))
				System.out.println("successful");
			
			if(connect.getChild_Bmi(centre))
				System.out.println("successful");
			return null;
		}
		
	}
	
}
