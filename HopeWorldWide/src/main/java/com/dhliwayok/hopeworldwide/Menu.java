package com.dhliwayok.hopeworldwide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Menu extends Activity {
	
	private HopeDbAdapter mydb; 
	private ImageButton bmiCalc;
	private ImageButton logout;
	private ImageButton sync;
	private ImageButton addChild;
	private MySharePref myPref;
	private ProgressDialog dialog = null;
    private String webLink= "http://team7.web44.net/sync/";
	//private boolean syncDone = false;
	BroadcastReceiver r;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
		myPref = new MySharePref(this);
		mydb= new HopeDbAdapter(this);
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLEAR_STACK");
		
		r = new ActivitiesBroadcastReceiver();
		registerReceiver(r, intentFilter);
		
		TextView view = (TextView) findViewById(R.id.textView1);
		TextView cName = (TextView) findViewById(R.id.textView2);
		view.setTextColor(Color.BLACK);
		view.setText("Welcome, "+myPref.getMyPref("Name"));
		cName.setText("Centre Name: "+myPref.getMyPref("CenterName"));
		
		bmiCalc =  (ImageButton) findViewById(R.id.calculatelogo);
		bmiCalc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					startActivity(new Intent(Menu.this, BMI_Activity.class));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		addChild = (ImageButton) findViewById(R.id.childLogo);
		addChild.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					startActivity(new Intent(Menu.this, AddChild.class));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		logout = (ImageButton) findViewById(R.id.logoutlogo);
		logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// set the title of the Alert Dialog
				alertDialogBuilder.setTitle("Logout");
				 // set dialog message
				alertDialogBuilder.setMessage("Are you sure you want to logout?").setCancelable(false).setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {// if yes is clicked, close
	         
					 try {
						 myPref.clearPref();
						 mydb.clearAll();
							startActivity(new Intent(Menu.this, LoginActivity.class));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				 }
			 }).setNegativeButton("No", new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface dialog,int id) {// if no is clicked, just close
					 // the dialog box and do nothing
					dialog.cancel();
				 }
				});
				
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				
			}
		});
		
		sync = (ImageButton) findViewById(R.id.synclogo);
		sync.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub					
				
				//if(bmi1 && bmi2)
					//mydb.clearAll();
				if(isOnline()){
				dialog = ProgressDialog.show(Menu.this, "", "Synchronizing...", true);
				new Sync().execute();
				//syncDone = true;
				}
				else{
					AlertDialog.Builder builder1 = new AlertDialog.Builder(Menu.this);
    				builder1.setTitle("Message");
    				builder1.setMessage("No Internet Connection \nDo not log out, try again later.");
    				builder1.setCancelable(true);
    				builder1.setNeutralButton(android.R.string.ok,
    				        new DialogInterface.OnClickListener() {
    				    public void onClick(DialogInterface dialog, int id) {
    				        dialog.cancel();
    				    }
    				});

    				AlertDialog alert11 = builder1.create();
    				alert11.show();
				
				}
				//mydb.close();
			}
		});
	}

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        MenuInflater myMenu = getMenuInflater();
        myMenu.inflate(R.menu.main, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(Menu.this, SettingsActivity.class));;
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    /* (non-Javadoc)
             * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
             */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			// set the title of the Alert Dialog
			alertDialogBuilder.setTitle("Exit Application?");
			 // set dialog message
			alertDialogBuilder.setMessage("Are you sure you want to exit!").setCancelable(false).setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {// if yes is clicked, close
                 // current activity
				//Intent intent = new Intent(Menu.this, MainActivity.class);
		        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        //intent.putExtra("EXIT", false);
		        //startActivity(intent);
				Intent broadcastIntent = new Intent();
				broadcastIntent.setAction("CLEAR_STACK");
				sendBroadcast(broadcastIntent);
			 }
		 }).setNegativeButton("No", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog,int id) {// if no is clicked, just close
				 // the dialog box and do nothing
				dialog.cancel();
			 }
			});
			
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			
	    }
		return super.onKeyDown(keyCode, event);
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
	
class ActivitiesBroadcastReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			finish();
		}
	}
	
private class Sync extends AsyncTask{
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
			Menu.this.runOnUiThread(new Runnable() {

                public void run() {
                    // TODO Auto-generated method stub
                	AlertDialog.Builder builder1 = new AlertDialog.Builder(Menu.this);
    				builder1.setTitle("Message");
    				builder1.setMessage("Synchronization Complete");
    				builder1.setCancelable(true);
    				builder1.setNeutralButton(android.R.string.ok,
    				        new DialogInterface.OnClickListener() {
    				    public void onClick(DialogInterface dialog, int id) {
    				        dialog.cancel();
    				    }
    				});

    				AlertDialog alert11 = builder1.create();
    				alert11.show();
    				

                }
            });
			
		super.onPostExecute(result);
		
	}
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		if(syncBMI()){
			System.out.println("Sync successful");
		}
		
		if(syncChild_BMI()){
			System.out.println("Sync successful");
		}
		
		if(syncChild()){
			System.out.println("Sync successful");
		}
		dialog.dismiss();
		//syncDone = true;
		return null;
	}
	private boolean syncBMI(){
		
		Cursor cs;
		cs = mydb.getBMI();
		JSONArray jsonarray;
		JSONObject json ;// new JSONObject();
		boolean x = false;
		
		startManagingCursor(cs);  
	      if(cs!=null) 
	    	  //System.out.println(cs.getCount());
	      {  
	           cs.moveToFirst();  
	           jsonarray = new JSONArray();
	           int c = 0;
	           while (cs.isAfterLast() == false) {  
	           json = new JSONObject();  
	      try {  
	           json.put("BMI_id",cs.getString(cs.getColumnIndex("_BMI_id")));  
	           json.put("height",cs.getString(cs.getColumnIndex("Height")));   
	           json.put("weight",cs.getString(cs.getColumnIndex("Weight"))); 
	           json.put("score",cs.getString(cs.getColumnIndex("BMI_Score"))); 
	           jsonarray.put(json);  
	           cs.moveToNext();  
	      }  
	      catch (Exception e) {  
	           Log.d("Android", "JSON Error"+e);  
	      }  
	           }  
	           try {  
	           // Create a new HttpClient and Post Header  
	           HttpClient httpclient = new DefaultHttpClient();  
	           HttpPost httppost = new HttpPost(webLink+"myset.php");
	           // Post the data:  
	           StringEntity se = new StringEntity(jsonarray.toString());  
	           httppost.setEntity(se);  
	           httppost.setHeader("Accept", "application/json");  
	           httppost.setHeader("Content-type", "application/json");  
	           // Execute HTTP Post Request  
	           System.out.print(jsonarray.toString());  
	           HttpResponse response = httpclient.execute(httppost);  
	           // for JSON:  
	           if(response != null)  
	           {  
	                InputStream is = response.getEntity().getContent();  
	                BufferedReader reader = new BufferedReader(new InputStreamReader(is));  
	                StringBuilder sb = new StringBuilder();  
	                String line = null;  
	                try {  
	                     while ((line = reader.readLine()) != null) {  
	                     sb.append(line + "\n");  
	                     }  
	                     x = true;
	                } catch (IOException e) {  
	                     e.printStackTrace();  
	                     }   
	                finally {  
	                     try {  
	                          is.close();  
	                     } catch (IOException e) {  
	                                         e.printStackTrace();  
	                                    }  
	                               }  
	                          }  
	           }  
	           catch (ClientProtocolException e) {                 
	           } catch (IOException e) {   
	        }            
	      }  
	      cs.close();
	      
	      return x;
	      
	}

	private boolean syncChild_BMI(){
		
		Cursor cs;
		cs = mydb.getChildBMI();
		JSONArray jsonarray;
		JSONObject json;// new JSONObject();
		boolean x = false;
		
		startManagingCursor(cs);  
	      if(cs!=null)  
	    	  //System.out.println(cs.getCount());
	      {  
	           cs.moveToFirst();  
	           jsonarray = new JSONArray();  
	           while (cs.isAfterLast() == false) {  
	           json = new JSONObject();  
	      try {  
	    	  json.put("BMI_id",cs.getInt(cs.getColumnIndex("_BMI_id")));
	           json.put("child_id",cs.getInt(cs.getColumnIndex("_id")));   
	           json.put("centre_id",cs.getInt(cs.getColumnIndex("_Center_id"))); 
	           json.put("date",cs.getString(cs.getColumnIndex("Date")));  
	           jsonarray.put(json);  
	           cs.moveToNext();  
	      }  
	      catch (Exception e) {  
	           Log.d("Android", "JSON Error"+e);  
	      }  
	           }  
	           try {  
	           // Create a new HttpClient and Post Header  
	           HttpClient httpclient = new DefaultHttpClient();  
	           HttpPost httppost = new HttpPost(webLink+"myset_childBmi.php");
	           // Post the data:  
	           StringEntity se = new StringEntity(jsonarray.toString());  
	           httppost.setEntity(se);  
	           httppost.setHeader("Accept", "application/json");  
	           httppost.setHeader("Content-type", "application/json");  
	           // Execute HTTP Post Request  
	           System.out.print(jsonarray.toString());  
	           HttpResponse response = httpclient.execute(httppost);  
	           // for JSON:  
	           if(response != null)  
	           {  
	                InputStream is = response.getEntity().getContent();  
	                BufferedReader reader = new BufferedReader(new InputStreamReader(is));  
	                StringBuilder sb = new StringBuilder();  
	                String line = null;  
	                try {  
	                     while ((line = reader.readLine()) != null) {  
	                     sb.append(line + "\n");  
	                     }  
	                     x = true;
	                } catch (IOException e) {  
	                     e.printStackTrace();  
	                     }   
	                finally {  
	                     try {  
	                          is.close();  
	                     } catch (IOException e) {  
	                                         e.printStackTrace();  
	                                    }  
	                               }  
	                          }  
	           }  
	           catch (ClientProtocolException e) {                 
	           } catch (IOException e) {   
	        }            
	      }  
	      cs.close();
	      //mydb.close();
	      return x;
	      
	}
	
private boolean syncChild(){
		
		Cursor cs;
		cs = mydb.getChild();
		JSONArray jsonarray;
		JSONObject json;// new JSONObject();
		boolean x = false;
		
		startManagingCursor(cs);  
	      if(cs!=null)  
	    	  //System.out.println(cs.getCount());
	      {  
	           cs.moveToFirst();  
	           jsonarray = new JSONArray();  
	           while (cs.isAfterLast() == false) {  
	           json = new JSONObject();  
	      try {  
	    	  json.put("child_name",cs.getString(cs.getColumnIndex("Name")));
	    	  json.put("OD",cs.getString(cs.getColumnIndex("Odeama")));
	    	  json.put("Date_Of_Birth",cs.getString(cs.getColumnIndex("Date_of_birth")));
	    	  json.put("Gender",cs.getString(cs.getColumnIndex("Gender")));  
	    	  json.put("Is_orphan",cs.getString(cs.getColumnIndex("Is_orphan"))); 
	    	  json.put("centre_id",cs.getInt(cs.getColumnIndex("_Center_id"))); 
	           jsonarray.put(json);  
	           cs.moveToNext();  
	      }  
	      catch (Exception e) {  
	           Log.d("Android", "JSON Error"+e);  
	      }  
	           }  
	           try {  
	           // Create a new HttpClient and Post Header  
	           HttpClient httpclient = new DefaultHttpClient();  
	           HttpPost httppost = new HttpPost(webLink+"myset_child.php");
	           // Post the data:  
	           StringEntity se = new StringEntity(jsonarray.toString());  
	           httppost.setEntity(se);  
	           httppost.setHeader("Accept", "application/json");  
	           httppost.setHeader("Content-type", "application/json");  
	           // Execute HTTP Post Request  
	           System.out.print(jsonarray.toString());  
	           HttpResponse response = httpclient.execute(httppost);  
	           // for JSON:  
	           if(response != null)  
	           {  
	                InputStream is = response.getEntity().getContent();  
	                BufferedReader reader = new BufferedReader(new InputStreamReader(is));  
	                StringBuilder sb = new StringBuilder();  
	                String line = null;  
	                try {  
	                     while ((line = reader.readLine()) != null) {  
	                     sb.append(line + "\n");  
	                     }  
	                     x = true;
	                } catch (IOException e) {  
	                     e.printStackTrace();  
	                     }   
	                finally {  
	                     try {  
	                          is.close();  
	                     } catch (IOException e) {  
	                                         e.printStackTrace();  
	                                    }  
	                               }  
	                          }  
	           }  
	           catch (ClientProtocolException e) {                 
	           } catch (IOException e) {   
	        }            
	      }  
	      cs.close();
	      //mydb.close();
	      return x;
	      
	}
}


@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	unregisterReceiver(r);
	super.onDestroy();
}



}
