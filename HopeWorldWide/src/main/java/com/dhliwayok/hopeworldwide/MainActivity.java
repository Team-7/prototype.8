package com.dhliwayok.hopeworldwide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private HopeDbAdapter mydb;  
	private MyConnect connect;
	BroadcastReceiver r;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLEAR_STACK");
		
		r = new ActivitiesBroadcastReceiver();
		registerReceiver(r, intentFilter);
		
		 mydb= new HopeDbAdapter(this);
		 connect = new MyConnect(mydb);
		 //if(isOnline())
			 connect.execute();
		 //mydb.close();
		 
		// Message.message(this, mydb.displayChildDetails("Kalamba Timba"));
		 
		
		 
		
try {
	new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	if(isOnline()){
	                final Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
	                MainActivity.this.startActivity(mainIntent);
	                MainActivity.this.finish();
	            	}else{
	            		AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
	    				builder1.setTitle("Message");
	    				builder1.setMessage("Internet Connection Required to start application \nPress Ok to exit");
	    				builder1.setCancelable(true);
	    				builder1.setNeutralButton(android.R.string.ok,
	    				        new DialogInterface.OnClickListener() {
	    				    public void onClick(DialogInterface dialog, int id) {
	    				    	Intent broadcastIntent = new Intent();
	    						broadcastIntent.setAction("CLEAR_STACK");
	    						sendBroadcast(broadcastIntent);
	    				    }
	    				});

	    				AlertDialog alert11 = builder1.create();
	    				alert11.show();
	            		
	            	}
	            }
	        }, 7000);//langing page with picture runs for 7seconds
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

		 
	
	}
	
		



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	class ActivitiesBroadcastReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			finish();
		}
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}





	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(r);
		super.onDestroy();
	}
	
	


}


