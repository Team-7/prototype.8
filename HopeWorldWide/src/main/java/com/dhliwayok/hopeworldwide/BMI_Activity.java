/**
 * 
 */
package com.dhliwayok.hopeworldwide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dhliwayok.hopeworldwide.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author KudaD
 *
 */
public class BMI_Activity extends Activity {
	
	private AutoCompleteTextView searchText;
	private EditText height;
	private EditText weight;
	private Button search;
	private Button calcBMI;
	private HopeDbAdapter myDb;
	private TextView childView;
	private TextView ageView;
	private TextView bmiView;
	private TextView currBMI;
	private TextView hyt;
	private TextView weit;
	private TextView hErr;
	private TextView wErr;
	private TextView dob;
	private TextView od;
	private TextView dateTaken;
	String txt;
	BroadcastReceiver r;
	private ArrayList <String> theArray;
	private ArrayAdapter<String> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bmi);
		

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLEAR_STACK");
		
		r = new ActivitiesBroadcastReceiver();
		registerReceiver(r, intentFilter);
		
		myDb = new HopeDbAdapter(this);
		
		
		searchText = (AutoCompleteTextView) findViewById(R.id.auto_names);
		search = (Button) findViewById(R.id.button2);
		hyt = (TextView) findViewById(R.id.textView1);
		weit = (TextView) findViewById(R.id.weight);
		childView = (TextView) findViewById(R.id.textView5);
		ageView = (TextView) findViewById(R.id.textView6);
		bmiView = (TextView) findViewById(R.id.textView7);
		calcBMI = (Button) findViewById(R.id.button1);
		height = (EditText) findViewById(R.id.heightvalue);
		weight = (EditText) findViewById(R.id.editText2);
		currBMI = (TextView) findViewById(R.id.textView9);
		hErr = (TextView) findViewById(R.id.textView12);
		wErr = (TextView) findViewById(R.id.textView11);
		dob = (TextView) findViewById(R.id.textView20);
		od = (TextView) findViewById(R.id.textView23);
		dateTaken = (TextView) findViewById(R.id.textView22);
		
		theArray = new ArrayList <String>();
		theArray=myDb.childNames();		
		adapter= new ArrayAdapter<String> (BMI_Activity.this,R.layout.dropdwn_textview ,theArray);
		// specify the layout
		searchText.setThreshold(2); // determine the number of character to enter before the guess
		searchText.setAdapter(adapter); // link the ArrayList to the Adapter
		
calcBMI.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		currBMI.setText("");
		hErr.setText("");
		wErr.setText("");
		hyt.setTextColor(Color.BLACK);
		weit.setTextColor(Color.BLACK);
		
		txt = searchText.getText().toString();
		if (!myDb.displayChildName(txt).equals("")) {
			if (!height.getText().toString().equals("")
					&& !weight.getText().toString().equals("")) {//values entered

				if (!height.getText().toString().equals("0")) {//check if height not equals zero

					if (!weight.getText().toString().equals("0")) {//check if height not equals zero

						final int h = Integer.parseInt(height.getText().toString());
						final double w = Double.parseDouble(weight.getText().toString());

						if (w < 100 && h < 250) {
							BMI myBmi = new BMI(h, w);
							
							final String date =myBmi.getDate();
							//System.out.println(date);
							
							final double score = myBmi.calculateBMI();
							//currBMI.setText("" + score);
							checkScore(score);
							final int myCentr = myDb.getChildCentre(txt);
							final int childId = Integer.parseInt(myDb.getChildId(txt));//gets searched child's id number
							
							if (!myDb.displayChildName(txt).equals("")) {
							
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BMI_Activity.this);
							// set the title of the Alert Dialog
							alertDialogBuilder.setTitle(R.string.bmiTitle);
							 // set dialog message
							alertDialogBuilder.setMessage("Confirm Height and Weight values \n"+"Height: "+h+" cm\nWeight: "+w+" kg").setCancelable(false).setPositiveButton(R.string.save,new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {// if yes is clicked, close
								

									if (myDb.insertBMI(h, w,score) > 0){ //stores bmi score, height, weight 
										Message.message(BMI_Activity.this, "BMI added successfully");
									}
									else
										Message.message(BMI_Activity.this,
												"BMI insert error");
									
									int row = myDb.checkBMIrow();//checks the highest bmi row number in the database 


									if (myDb.insertChildBMI(row, childId, myCentr, date) > 0){//links child to calculated bmi
										Message.message(BMI_Activity.this,
												"Child updated successfully");
										//myDb.close();
									}
									else
										Message.message(BMI_Activity.this,
												"Child update error");
							 }
							
						 }).setNegativeButton(R.string.edit, new DialogInterface.OnClickListener() {
							 public void onClick(DialogInterface dialog,int id) {// if no is clicked, just close
								 // the dialog box and do nothing
								dialog.cancel();
							 }
							});
							
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
							
							

							}
						} else {
							if(w > 100 ){
								weit.setTextColor(Color.RED);
								wErr.setTextColor(Color.RED);
								wErr.setText("greater than 100");
							}
							
							if(h > 250){
							hyt.setTextColor(Color.RED);

							hErr.setTextColor(Color.RED);
							hErr.setText("greater than 180");
							}
							
						}

					} else {
						
						weit.setTextColor(Color.RED);
						wErr.setTextColor(Color.RED);
						wErr.setText("Cannot be zero");
						
					
					}
				} else {
					
					hyt.setTextColor(Color.RED);
					hErr.setTextColor(Color.RED);
					hErr.setText("Cannot be zero");
					
				}
			} else if (height.getText().toString().equals("")) {//check if height field is empty
				hErr.setTextColor(Color.RED);
				hErr.setText("Enter height");
				hyt.setTextColor(Color.RED);
							
			} else if (weight.getText().toString().equals("")) {//check if weight field is empty
				wErr.setTextColor(Color.RED);
				wErr.setText("Enter weight");
				weit.setTextColor(Color.RED);
			}
		}
		else{
			if (!height.getText().toString().equals("")
					&& !weight.getText().toString().equals("")) {//values entered

				if (!height.getText().toString().equals("0")) {//check if height not equals zero

					if (!weight.getText().toString().equals("0")) {//check if height not equals zero

						int h = Integer.parseInt(height.getText().toString());
						double w = Double.parseDouble(weight.getText().toString());

						if (w < 100 && h < 250) {
							double myH = (h*h);
							
							double score= (w/myH)*10000;
							//currBMI.setText("" + BMI.round(score, 2, BigDecimal.ROUND_HALF_UP));
							checkScore(BMI.round(score, 2, BigDecimal.ROUND_HALF_UP));
						} else {
							if(w > 100 ){
								weit.setTextColor(Color.RED);
								wErr.setTextColor(Color.RED);
								wErr.setText("greater than 100");
							}
							
							if(h > 250){
							hyt.setTextColor(Color.RED);

							hErr.setTextColor(Color.RED);
							hErr.setText("greater than 180");
							}
							
						}

					} else {
						
						wErr.setTextColor(Color.RED);
						wErr.setText("Cannot be zero");
						weit.setTextColor(Color.RED);
					}
				} else {
					
					hErr.setTextColor(Color.RED);
					hErr.setText("Cannot be zero");
					hyt.setTextColor(Color.RED);
				}
			} else if (height.getText().toString().equals("")) {//check if height field is empty
				hErr.setTextColor(Color.RED);
				hErr.setText("Enter height");
				hyt.setTextColor(Color.RED);
			} else if (weight.getText().toString().equals("")) {//check if weight field is empty
				wErr.setTextColor(Color.RED);
				wErr.setText("Enter weight");
				weit.setTextColor(Color.RED);
			}
			
		}

		
		
		
	}
});		

search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				height.setText("");
				weight.setText("");//set all the fields and views to blank
				currBMI.setText("");
				hErr.setText("");
				wErr.setText("");
				dob.setText("");
				od.setText("");
				dateTaken.setText("");
				
				txt = searchText.getText().toString();
				if(!myDb.displayChildName(txt).equals("")){	//check name typed is in database
					childView.setText(txt);
					
					if(!myDb.displayChildAge(txt).equals("")){//check for child's age
						
						String age = myDb.displayChildAge(txt);
							ageView.setText(age);
							
						if(!myDb.getDOB(txt).equals(""))
							dob.setText(myDb.getDOB(txt));
						}
						else
							ageView.setText("child has no age");
					
						if(!myDb.getChildOd(txt).equals("")){
							
							String x = myDb.getChildOd(txt);
							if(x.equals("Y")){
								od.setTextColor(Color.RED);
								od.setText("Yes");
							}
							else{
								od.setTextColor(Color.BLACK);
								od.setText("No");
							}
						}
					
					if(!myDb.displayChildBMI(txt).equals("")){
						String bmi = myDb.displayChildBMI(txt);//get child bmi information
						double w = Double.parseDouble(bmi);
						checkPrBMI(w);
						dateTaken.setText(myDb.bmiDateTaken(txt));
						//bmiView.setText(bmi);
					}
					else{
						bmiView.setTextColor(Color.RED);
						bmiView.setText("No previous BMI");
					}
				}
				else{
					childView.setText("No child found");
					ageView.setText("");
					bmiView.setText("");
					currBMI.setText("");
					dob.setText("");
					od.setText("");
					dateTaken.setText("");
					}
			
					
			}
		});
		
	}
		
	
private void checkScore(double score){//checks which category bmi score belongs to 
	
	if(score > 0 && score < 19)
	{  					
		currBMI.setTextColor(Color.RED);	
		currBMI.setText(score+" Under Weight");
	}
	if(score >=19 && score < 24)
	{ 			
		currBMI.setTextColor(Color.BLUE);
		currBMI.setText(score+" Normal");
	}
	
	if(score >=24 && score < 30)
	{  				
		currBMI.setTextColor(Color.RED);
		currBMI.setText(score+" Over Weight");
	}
	
	if(score >=30 && score <=40)
	{  				
		currBMI.setTextColor(Color.RED);
		currBMI.setText(score+" Obese");
	}
	
	if(score > 40)
	{  				
		currBMI.setTextColor(Color.RED);
		currBMI.setText(score+" Morbidly Obese");
	}	
}


private void checkPrBMI(double score){//checks which category bmi score belongs to     new
	
	if(score > 0 && score < 19)
	{  					
		bmiView.setTextColor(Color.RED);
		bmiView.setText(score+ "   Under Weight");						
	}
	if(score >=19 && score < 24)
	{ 			
		bmiView.setTextColor(Color.BLUE);
		bmiView.setText(score+ "   Normal");							
	}
	
	if(score >=24 && score < 30)
	{  				
		bmiView.setTextColor(Color.RED);
		bmiView.setText(score+ "  Over Weight");						
	}
	
	if(score >=30 && score <=40)
	{  			
		bmiView.setTextColor(Color.RED);
		bmiView.setText(score+ "   Obese");						
	}
	
	if(score > 40)
	{  				
		bmiView.setTextColor(Color.RED);
		bmiView.setText(score+ "   Morbidly Obese");
	}	
}

class ActivitiesBroadcastReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		finish();
	}
}

@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	unregisterReceiver(r);
	super.onDestroy();
}




}


