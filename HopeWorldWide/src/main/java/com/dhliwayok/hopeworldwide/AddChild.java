package com.dhliwayok.hopeworldwide;

import java.util.Calendar;




import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AddChild extends Activity {

	private Button newDate;
	private Button save;
	private TextView viewDate;
	private TextView dateErr;
	private EditText childName;
	private RadioGroup radioSexGroup;
	private RadioButton radioSexButton;
	private RadioGroup radioOdeamaGroup;
	private RadioButton radioOdeamaButton;
	private RadioGroup radioOrphanGroup;
	private RadioButton radioOrphanButton;
	private MySharePref myPref;
	private HopeDbAdapter myDb;
	private TextView childerr;
	private int centre;
	private int year;
	private int month;
	private int day;
	BroadcastReceiver r;
	static final int DATE_DIALOG_ID = 999;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addchild);

		setCurrentDateOnView();
		addListenerOnButton();
		
		myPref = new MySharePref(this);
		myDb = new HopeDbAdapter(this);
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLEAR_STACK");
		
		r = new ActivitiesBroadcastReceiver();
		registerReceiver(r, intentFilter);
		
		centre = myDb.getCenter(myPref.getMyPref("CenterName"));
		radioOdeamaGroup = (RadioGroup) findViewById(R.id.radioOdeama);
		radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
		radioOrphanGroup =(RadioGroup) findViewById(R.id.radioOrphan);
		save = (Button) findViewById(R.id.button2);
		childName = (EditText) findViewById(R.id.editText1);
		childerr = (TextView) findViewById(R.id.textView19);
		dateErr = (TextView) findViewById(R.id.textView31);
		
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String name = childName.getText().toString();
				String dateOfbirth = viewDate.getText().toString();
				
				 // get selected radio button from radioGroup
				int mySex = radioSexGroup.getCheckedRadioButtonId();
	 
				// find the radiobutton by returned id
			        radioSexButton = (RadioButton) findViewById(mySex);
			    String gender = ((String) radioSexButton.getText()).substring(0, 1);
			    //System.out.println(gender);
			 // get selected radio button from radioGroup
				int myOD = radioOdeamaGroup.getCheckedRadioButtonId();
	 
				// find the radiobutton by returned id
			        radioOdeamaButton = (RadioButton) findViewById(myOD);
			    String odeama = ((String) radioOdeamaButton.getText()).substring(0, 1);
			    
			 // get selected radio button from radioGroup
				int myOrphan = radioOrphanGroup.getCheckedRadioButtonId();
	 
				// find the radiobutton by returned id
			        radioOrphanButton = (RadioButton) findViewById(myOrphan);
			    String orphan = ((String) radioOrphanButton.getText()).substring(0, 1);
			    //System.out.println("Orphan: "+orphan);
			    
			    if(!name.equals("") ){
			    	if(dateOfbirth.charAt(0) != ' '){
			    		if(checkName(name)){
			    		if( (myDb.insertNewChild(name, odeama, dateOfbirth, gender,orphan, centre))> 0){
			    			Message.message(AddChild.this, "Child added successfully");	
			    			childName.setText("");
			    			dateErr.setText(" ");
			    			childerr.setText(" ");
			    		}
			    	
			    	else
			    		Message.message(AddChild.this, "Child not added. Try again");	
			    		}else{
			    			childerr.setTextColor(Color.RED);
					    	childerr.setText("Child name cannot contain a number");
			    		}
			    			
			    	}else{
			    		dateErr.setTextColor(Color.RED);
			    		dateErr.setText("Child has to be at least one year old");
			    	}
			    }
			    else{
			    
			    	childerr.setTextColor(Color.RED);
			    	childerr.setText("Enter Child Name");
			    }
			}
		});
	}
	
	private boolean checkName(String name){
		boolean myName = true;
		
		for(int i =0; i<name.length();i++){
			if(name.charAt(i)== '1'||name.charAt(i)== '2'||name.charAt(i)=='3'||name.charAt(i)== '4'||name.charAt(i)== '5'||name.charAt(i)== '6'||name.charAt(i)== '7'||name.charAt(i)== '8'||name.charAt(i)== '9'||name.charAt(i)== '0'){
				myName = false;
			}
		}
		
		return myName;
	}
	
	
	public void setCurrentDateOnView() {
		 
		viewDate = (TextView) findViewById(R.id.textView4);
		//dpResult = (DatePicker) findViewById(R.id.dpResult);
 
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR)-2;
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
 
		// set current date into textview
		viewDate.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "));
			// Month is 0 based, just add 1
			//.append(month + 1).append("-").append(day).append("-")
			//.append(year).append(" "));
 
		// set current date into datepicker
		//dpResult.init(year, month, day, null);
	}
	
	public void addListenerOnButton() {
		 
		newDate = (Button) findViewById(R.id.button1);
 
		newDate.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View v) {
 
				showDialog(DATE_DIALOG_ID);
 
			}
 
		});
 
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
		   // set date picker as current date
		   return new DatePickerDialog(this, datePickerListener, 
                         year, month,day);
		}
		return null;
	}
 
	private DatePickerDialog.OnDateSetListener datePickerListener 
                = new DatePickerDialog.OnDateSetListener() {
 
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
 
			final Calendar c = Calendar.getInstance();
			int myYear = c.get(Calendar.YEAR);
			// set selected date into textview
			if(year < myYear){
				viewDate.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "));
				viewDate.setTextColor(Color.BLACK);
			}
				else{
					viewDate.setTextColor(Color.RED);
				viewDate.setText(" "+new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "));
			}
			// set selected date into datepicker also
			//dpResult.init(year, month, day, null);
 
		}
	};
	
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

    
