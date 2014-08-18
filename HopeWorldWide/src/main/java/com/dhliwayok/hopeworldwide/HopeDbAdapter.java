package com.dhliwayok.hopeworldwide;


import java.sql.Date;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HopeDbAdapter {//Adapter Class to help use database

		private HopeHelper hopeHelper;
		private SQLiteDatabase db;
		private MySharePref myPref;
		
		
		public HopeDbAdapter(Context context){
			
			hopeHelper = new HopeHelper(context);
			myPref = new MySharePref(context);
		}
		
		//public synchronized SQLiteDatabase open() throws SQLException {

	        //if(db ==null){
	            //db = hopeHelper.getWritableDatabase();
	        //}

	        //return db;
	    //}
		
		
		//Login method, compares username and password
		public boolean Login(String username, String password) throws SQLException {
		db = hopeHelper.getWritableDatabase();// returns a writable db 
		String [] columns = {HopeHelper.USERNAME, HopeHelper.PASSWORD};// Columns that we actually need to log in  
		Cursor myCursor = db.query(HopeHelper.TABLE2, columns, HopeHelper.USERNAME+" = '"+username+"'", null, null, null, null);
		// query that looks up the username that is in the db 		
		String pass= "";
		if (myCursor != null) {
			
			while(myCursor.moveToNext()){ // move the cursor to the next row 
			int index = myCursor.getColumnIndex(HopeHelper.PASSWORD);
			pass = myCursor.getString(index);
			}
			
			if(pass.equals(password))	
				return true;
				
			}
		myCursor.close();
		db.close();
		return false;
		}
		
		public String LoginName(String username, String password) throws SQLException {
			db = hopeHelper.getWritableDatabase();// returns a writable db 
			String [] columns = {HopeHelper.USERNAME, HopeHelper.PASSWORD, HopeHelper.W_NAME};// Columns that we actually need to log in  
			Cursor myCursor = db.query(HopeHelper.TABLE2, columns, HopeHelper.USERNAME+" = '"+username+"'", null, null, null, null);
			// query that looks up the username that is in the db 		
			String pass= "";
			String name = "";
			if (myCursor != null) {
				
				while(myCursor.moveToNext()){ // move the cursor to the next row 
				int index = myCursor.getColumnIndex(HopeHelper.PASSWORD);
				int index2 = myCursor.getColumnIndex(HopeHelper.W_NAME);
				pass = myCursor.getString(index);
				name = myCursor.getString(index2);
				}
				
				if(pass.equals(password))	
					return name;
					
				}
			myCursor.close();
			db.close();
			return name;
			}
		
		public long insertChild(int id, String name, String od, String dateOfBirth, String gender, String isOrphan, int cntr){//inserts a child record in the database
		
			db = hopeHelper.getWritableDatabase();			
			ContentValues myValues = new ContentValues();//raw queries can have problems 
			myValues.put(HopeHelper.KEY_ID, id);
			myValues.put(HopeHelper.C_NAME, name);
			myValues.put(HopeHelper.OD, od);
			myValues.put(HopeHelper.DATE_OF_BIRTH, dateOfBirth);
			myValues.put(HopeHelper.GENDER, gender);
			myValues.put(HopeHelper.IS_ORPHAN, isOrphan);
			myValues.put(HopeHelper.CENTRE_ID, cntr);
			long myId = db.insert(HopeHelper.TABLE1, null, myValues);
			db.close();
			return myId;
		}

    public long updateChild(int id, String name, String od, String dateOfBirth, String gender, String isOrphan, int cntr){//inserts a child record in the database

        db = hopeHelper.getWritableDatabase();
        ContentValues myValues = new ContentValues();//raw queries can have problems
        //myValues.put(HopeHelper.KEY_ID, id);
        myValues.put(HopeHelper.C_NAME, name);
        myValues.put(HopeHelper.OD, od);
        myValues.put(HopeHelper.DATE_OF_BIRTH, dateOfBirth);
        myValues.put(HopeHelper.GENDER, gender);
        myValues.put(HopeHelper.IS_ORPHAN, isOrphan);
        myValues.put(HopeHelper.CENTRE_ID, cntr);

        long myId = db.update(HopeHelper.TABLE1, myValues, HopeHelper.KEY_ID+"="+id,null);
        db.close();
        return myId;
    }
		
		public long insertNewChild(String name, String od, String dateOfBirth, String gender, String isOrphan, int cntr){//inserts a child record in the database with no id number
			
			db = hopeHelper.getWritableDatabase();			
			ContentValues myValues = new ContentValues();//raw queries can have problems 
			myValues.put(HopeHelper.C_NAME, name);
			myValues.put(HopeHelper.OD, od);
			myValues.put(HopeHelper.DATE_OF_BIRTH, dateOfBirth);
			myValues.put(HopeHelper.GENDER, gender);
			myValues.put(HopeHelper.IS_ORPHAN, isOrphan);
			myValues.put(HopeHelper.CENTRE_ID, cntr);
			long myId = db.insert(HopeHelper.TABLE6, null, myValues);
			db.close();
			return myId;
		}
		
		public long insertCommunityWorker(String username, String password, String name){//insert a community worker in the database
			
			db = hopeHelper.getWritableDatabase();
			ContentValues myValues = new ContentValues();
			myValues.put(HopeHelper.USERNAME, username);
			myValues.put(HopeHelper.PASSWORD, password);
			myValues.put(HopeHelper.W_NAME, name);
			long myId = db.insert(HopeHelper.TABLE2, null, myValues);
			db.close();
			return myId;
		}
		
public long insertECD(int centerId, String cName, String location){//inserts community center 
			
			db = hopeHelper.getWritableDatabase();
			ContentValues myValues = new ContentValues();
			myValues.put(HopeHelper.CENTRE_ID, centerId);
			myValues.put(HopeHelper.CENTRE_NAME, cName);
			myValues.put(HopeHelper.LOCATION, location);
			long myId = db.insert(HopeHelper.TABLE3, null, myValues);
			db.close();
			return myId;
			
		}

public long insertBMI(int id, int height, double weight, double score){//inserts a BMI record in database
	
	db = hopeHelper.getWritableDatabase();
	
	ContentValues myValues = new ContentValues();
	myValues.put(HopeHelper.BMI_ID, id);
	myValues.put(HopeHelper.HEIGHT, height);
	myValues.put(HopeHelper.WEIGHT, weight);
	myValues.put(HopeHelper.SCORE, score);
	long myId = db.insert(HopeHelper.TABLE4, null, myValues);
	db.close();
	return myId;
}

public long insertBMI(int height, double weight, double score){//inserts a BMI record in database
	
	db = hopeHelper.getWritableDatabase();
	String [] columns = {HopeHelper.BMI_ID, HopeHelper.SCORE};
	Cursor myCursor = db.query(HopeHelper.TABLE4, columns,null, null, null, null, null);
	int theName = 0;
	long myId =0;
	
	ContentValues myValues = new ContentValues();
	
	if(myCursor.getCount() > 0){
		
		myValues.put(HopeHelper.HEIGHT, height);
		myValues.put(HopeHelper.WEIGHT, weight);
		myValues.put(HopeHelper.SCORE, score);
		myId = db.insert(HopeHelper.TABLE4, null, myValues);
	}
	else{
		
		if(getCenter(myPref.getMyPref("CenterName"))>0){
		 int num = getCenter(myPref.getMyPref("CenterName"))*10000;
		 int id = num+1;
			myId = insertBMI(id, height, weight, score);
			System.out.println("Number: "+ id);
		}
		
	
	}
	//System.out.println(date);
	db.close();
	
	return myId;
}

public long insertChildBMI( int BMI_id, int child_id, int cnter_id, String date){//links child to a bmi record (association)
	
	db = hopeHelper.getWritableDatabase();
	ContentValues myValues = new ContentValues();
	myValues.put(HopeHelper.BMI_ID, BMI_id);
	myValues.put(HopeHelper.KEY_ID, child_id);
	myValues.put(HopeHelper.CENTRE_ID, cnter_id);
	myValues.put(HopeHelper.DATE, date);
	long myId = db.insert(HopeHelper.TABLE5, null, myValues);
	db.close();
	return myId;
}

public int checkBMIrow(){//checks the number of rows in the BMI Table
	
	db = hopeHelper.getWritableDatabase();
	String [] columns = {HopeHelper.BMI_ID, HopeHelper.SCORE};
	Cursor myCursor = db.query(HopeHelper.TABLE4, columns,null, null, null, null, null);
	int theName = 0;
	
	if(myCursor.moveToLast()){
		int index1 = myCursor.getColumnIndex(HopeHelper.BMI_ID);
		theName = myCursor.getInt(index1);
	}
	//while(myCursor.moveToNext()){
		//int index1 = myCursor.getColumnIndex(HopeHelper.CENTRE_ID);
		
		//theName = myCursor.getInt(index1);

		//buffer.append(theName);
	//}
	//System.out.println(theName);
	myCursor.close();
	db.close();
	return theName;
	
}

public boolean updateChild(String name, int id){//Updates a child by adding an id
	
	db = hopeHelper.getWritableDatabase();
	ContentValues myValues = new ContentValues();
	myValues.put(HopeHelper.BMI_ID, id);
	long myId = db.update(HopeHelper.TABLE1, myValues,HopeHelper.C_NAME+" = '"+name+"'", null);
	
	if(myId>0)
		return true;
	else			
		return false;
	
}

public int getChildCentre(String name){
	
	db = hopeHelper.getWritableDatabase();
	String [] columns = {HopeHelper.C_NAME, HopeHelper.OD, HopeHelper.DATE_OF_BIRTH, HopeHelper.GENDER, HopeHelper.CENTRE_ID};
	Cursor myCursor = db.query(HopeHelper.TABLE1, columns, HopeHelper.C_NAME+" = '"+name+"'", null, null, null, null);
	int theName = 0;
	
	while(myCursor.moveToNext()){
		int index1 = myCursor.getColumnIndex(HopeHelper.CENTRE_ID);
		
		theName = myCursor.getInt(index1);

		//buffer.append(theName);
	}
	//myCursor.close();
	return theName;
}

public int getCenter(String ecdname){
	
	db = hopeHelper.getWritableDatabase();
	String [] columns = {HopeHelper.CENTRE_NAME, HopeHelper.CENTRE_ID};
	Cursor myCursor = db.query(HopeHelper.TABLE3, columns, HopeHelper.CENTRE_NAME+" = '"+ecdname+"'", null, null, null, null);
	int theName = 0;
	
	while(myCursor.moveToNext()){
		int index1 = myCursor.getColumnIndex(HopeHelper.CENTRE_ID);
		
		theName = myCursor.getInt(index1);

		//buffer.append(theName);
	}
	//myCursor.close();
	return theName;
}



public String displayChildDetails(String name){//displays all child details (excluding BMI information)
	
	db = hopeHelper.getWritableDatabase();
	String [] columns = {HopeHelper.C_NAME, HopeHelper.OD, HopeHelper.DATE_OF_BIRTH, HopeHelper.GENDER, HopeHelper.CENTRE_ID};
	Cursor myCursor = db.query(HopeHelper.TABLE1, columns, HopeHelper.C_NAME+" = '"+name+"'", null, null, null, null);
	StringBuffer buffer = new StringBuffer();
	
	while(myCursor.moveToNext()){
		int index1 = myCursor.getColumnIndex(HopeHelper.C_NAME);
		int index2 = myCursor.getColumnIndex(HopeHelper.OD);
		int index3 = myCursor.getColumnIndex(HopeHelper.DATE_OF_BIRTH);
		int index4 = myCursor.getColumnIndex(HopeHelper.GENDER);
		int index5 = myCursor.getColumnIndex(HopeHelper.CENTRE_ID);
		
		String theName = myCursor.getString(index1);
		String od = myCursor.getString(index2);
		String date = myCursor.getString(index3);
		String sex = myCursor.getString(index4);
		int centr = myCursor.getInt(index4);
		buffer.append(theName+" "+od+" "+date+" "+sex+" "+centr+" \n");
	}
	//myCursor.close();
	return buffer.toString();
}

public String displayChildName(String name){//checks if a name exists and if t does it returns the name
	
	db = hopeHelper.getWritableDatabase();
	String [] columns = {HopeHelper.C_NAME, HopeHelper.OD, HopeHelper.DATE_OF_BIRTH, HopeHelper.GENDER};
	Cursor myCursor = db.query(HopeHelper.TABLE1, columns, HopeHelper.C_NAME+" = '"+name+"'", null, null, null, null);
	StringBuffer buffer = new StringBuffer();
	
	while(myCursor.moveToNext()){
		int index1 = myCursor.getColumnIndex(HopeHelper.C_NAME);
		
		String theName = myCursor.getString(index1);

		buffer.append(theName);
	}
	//myCursor.close();
	return buffer.toString();
	
}

public String displayECDName(String name){//checks if a name exists and if t does it returns the name
	
	db = hopeHelper.getWritableDatabase();
	String [] columns = {HopeHelper.CENTRE_ID, HopeHelper.CENTRE_NAME, HopeHelper.LOCATION};
	Cursor myCursor = db.query(HopeHelper.TABLE3, columns, HopeHelper.CENTRE_NAME +" = '"+name+"'", null, null, null, null);
	StringBuffer buffer = new StringBuffer();
	
	while(myCursor.moveToNext()){
		int index1 = myCursor.getColumnIndex(HopeHelper.CENTRE_NAME);
		
		String theName = myCursor.getString(index1);

		buffer.append(theName);
	}
	//myCursor.close();
	return buffer.toString();
	
}

public String displayChildAge(String name){//calculates child age from date of birth
	
	db = hopeHelper.getWritableDatabase();
	String [] columns = {HopeHelper.C_NAME, HopeHelper.OD, HopeHelper.DATE_OF_BIRTH, HopeHelper.GENDER};
	Cursor myCursor = db.query(HopeHelper.TABLE1, columns, HopeHelper.C_NAME+" = '"+name+"'", null, null, null, null);
	//StringBuffer buffer = new StringBuffer();
	String date = "";
	while(myCursor.moveToNext()){
		int index3 = myCursor.getColumnIndex(HopeHelper.DATE_OF_BIRTH);
		
		date = myCursor.getString(index3);
		
	}
	Cursor miCursor = db.rawQuery("SELECT "+HopeHelper.DATE_OF_BIRTH+", round((SELECT julianday('now') - julianday('"+date+"'))/365, 1)FROM "+HopeHelper.TABLE1+";",null);
	String age = "";
	while(miCursor.moveToNext()){
		age = miCursor.getString(1);
		//buffer.append(age);
	}
	miCursor.close();
	String mon = age.substring(age.length()-1);
	String yer = age.split("\\.")[0];
	return yer + " years " + mon +" months";//buffer.toString();
}

public String getDOB(String name){//calculates child age from date of birth
	
	db = hopeHelper.getWritableDatabase();
	String [] columns = {HopeHelper.C_NAME, HopeHelper.OD, HopeHelper.DATE_OF_BIRTH, HopeHelper.GENDER};
	Cursor myCursor = db.query(HopeHelper.TABLE1, columns, HopeHelper.C_NAME+" = '"+name+"'", null, null, null, null);
	//StringBuffer buffer = new StringBuffer();
	String date = "";
	while(myCursor.moveToNext()){
		int index3 = myCursor.getColumnIndex(HopeHelper.DATE_OF_BIRTH);
		
		date = myCursor.getString(index3);
		
	}
	return date;
}

public String getChildId(String name){// searches child by name and returns the child id
	db = hopeHelper.getWritableDatabase();
	String [] columns = {HopeHelper.C_NAME, HopeHelper.KEY_ID};
	Cursor myCursor = db.query(HopeHelper.TABLE1, columns, HopeHelper.C_NAME+" = '"+name+"'", null, null, null, null);
	
	String ID = "";
	
	while(myCursor.moveToNext()){
		int index5 = myCursor.getColumnIndex(HopeHelper.KEY_ID);
		ID = myCursor.getString(index5);
		//buffer.append(bmi);
	}
	myCursor.close();
	return ID;
}

public String getChildOd(String name){// searches child by name and returns the odeama
	db = hopeHelper.getWritableDatabase();
	String [] columns = {HopeHelper.C_NAME, HopeHelper.OD};
	Cursor myCursor = db.query(HopeHelper.TABLE1, columns, HopeHelper.C_NAME+" = '"+name+"'", null, null, null, null);
	
	String OD = "";
	
	while(myCursor.moveToNext()){
		int index5 = myCursor.getColumnIndex(HopeHelper.OD);
		OD = myCursor.getString(index5);
		//buffer.append(bmi);
	}
	myCursor.close();
	return OD;
}

public String displayChildBMI(String name){//searches child by name and returns bmi information
	
	db = hopeHelper.getWritableDatabase();
	StringBuffer buffer = new StringBuffer();
	String ID = getChildId(name);
	String a ="";
	if(!ID.equals("")){
		
		String [] c = {HopeHelper.BMI_ID};
		Cursor b= db.query(HopeHelper.TABLE5, c, HopeHelper.KEY_ID+" = '"+ID+"'", null, null, null, null);

		while(b.moveToNext()){
			int index5 = b.getColumnIndex(HopeHelper.BMI_ID);
			a = b.getString(index5);
			//buffer.append(bmi);
		}
	}
	
	String result = "";
	if(a.equals("0"))
		result = "";
	else{
		String [] bmiColumns = {HopeHelper.SCORE, HopeHelper.BMI_ID};
		Cursor miCursor = db.query(HopeHelper.TABLE4, bmiColumns, HopeHelper.BMI_ID+" = '"+a+"'", null, null, null, null);
	
		while(miCursor.moveToNext()){
			int index = miCursor.getColumnIndex(HopeHelper.SCORE);
			result = miCursor.getString(index);
			buffer.append(result);
		}
		miCursor.close();
		db.close();
	}
	
	return buffer.toString();
}		

public String bmiDateTaken(String name){//searches child by name and returns bmi information(date taken)
	
	db = hopeHelper.getWritableDatabase();
	StringBuffer buffer = new StringBuffer();
	String ID = getChildId(name);
	String a ="";
	if(!ID.equals("")){
		
		String [] c = {HopeHelper.BMI_ID, };
		Cursor b= db.query(HopeHelper.TABLE5, c, HopeHelper.KEY_ID+" = '"+ID+"'", null, null, null, null);

		while(b.moveToNext()){
			int index5 = b.getColumnIndex(HopeHelper.BMI_ID);
			a = b.getString(index5);
			//buffer.append(bmi);
		}
	}
	
	String result = "";
	if(a.equals("0"))
		result = "";
	else{
		String [] bmiColumns = {HopeHelper.DATE, HopeHelper.BMI_ID};
		Cursor miCursor = db.query(HopeHelper.TABLE5, bmiColumns, HopeHelper.BMI_ID+" = '"+a+"'", null, null, null, null);
	
		while(miCursor.moveToNext()){
			int index = miCursor.getColumnIndex(HopeHelper.DATE);
			result = miCursor.getString(index);
			buffer.append(result);
		}
		miCursor.close();
		db.close();
	}
	
	return buffer.toString();
}	

public ArrayList <String> childNames(){
	
	
	 // db = hopeHelper.getWritableDatabase();
	  db = hopeHelper.getReadableDatabase();
	  ArrayList<String> myNames = new ArrayList<String>();
	  String selectQuery = "SELECT * FROM " + HopeHelper.TABLE1+";";
	  
	  Cursor cursor = db.rawQuery(selectQuery,null);

	  if (cursor.moveToFirst()) 
	  {
	    do {
	        String names = cursor.getString(cursor.getColumnIndex(HopeHelper.C_NAME));
	        myNames.add(names);
	      } 
	      while (cursor.moveToNext());
	    //cursor.close();
	    db.close();
	  }

	  return myNames;	
}      

public ArrayList <String> ECDNames(){
	
	
	 // db = hopeHelper.getWritableDatabase();
	  db = hopeHelper.getReadableDatabase();
	  ArrayList<String> myNames = new ArrayList<String>();
	  String selectQuery = "SELECT * FROM " + HopeHelper.TABLE3+";";
	  
	  Cursor cursor = db.rawQuery(selectQuery,null);

	  if (cursor.moveToFirst()) 
	  {
	    do {
	        String names = cursor.getString(cursor.getColumnIndex(HopeHelper.CENTRE_NAME));
	        myNames.add(names);
	      } 
	      while (cursor.moveToNext());
	  }
	    cursor.close();
	  
	    db.close();
	  

	  return myNames;	
} 


public Cursor getBMI(){
	
	 db = hopeHelper.getWritableDatabase();
	 String selectQuery = "SELECT * FROM " + HopeHelper.TABLE4+";";
	 Cursor cursor = db.rawQuery(selectQuery,null);
	
	  return cursor;
}

public Cursor getChildBMI(){
	
	 db = hopeHelper.getWritableDatabase();
	 String selectQuery = "SELECT * FROM " + HopeHelper.TABLE5+";";
	 Cursor cursor = db.rawQuery(selectQuery,null);
	
	  return cursor;
}

public Cursor getChild(){
	
	 db = hopeHelper.getWritableDatabase();
	 String selectQuery = "SELECT * FROM " + HopeHelper.TABLE6+";";
	 Cursor cursor = db.rawQuery(selectQuery,null);
	
	  return cursor;
}

public void clearAll(){
	db = hopeHelper.getWritableDatabase();
	try{
		db.execSQL("DROP TABLE IF EXISTS "+HopeHelper.TABLE5);
		db.execSQL("DROP TABLE IF EXISTS "+HopeHelper.TABLE4);
		db.execSQL("DELETE FROM "+HopeHelper.TABLE2);
		db.execSQL("DELETE FROM "+HopeHelper.TABLE1);
		db.execSQL("DELETE FROM "+HopeHelper.TABLE6);
		db.execSQL("DELETE FROM "+HopeHelper.TABLE3);
		db.execSQL(HopeHelper.CREATE_TABLE4);
		db.execSQL(HopeHelper.CREATE_TABLE5);
	}catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	db.close();
	
}



	
		
	
	static class HopeHelper extends SQLiteOpenHelper{//Database class (Schema)
		
		//DATABASE NAME
		private static final String DATABASE_NAME = "myDb";
		//DATABASE VERSION
		private static final int DATABASE_VERSION = 12;
		//TABLE NAMES
		private static final String TABLE1 = "CHILD";
		private static final String TABLE2 = "COMMUNITY_WORKER";
		private static final String TABLE3 = "ECD";
		private static final String TABLE4 = "BMI";
		private static final String TABLE5 = "CHILD_BMI";
		private static final String TABLE6 = "NEW_CHILD";
		
		//TABLE CHILD COLUMN NAMES
		private static final String KEY_ID = "_id";
		private static final String C_NAME = "Name";
		private static final String OD = "Odeama";
		private static final String IS_ORPHAN = "Is_orphan";
		private static final String DATE_OF_BIRTH = "Date_of_birth";
		private static final String GENDER = "Gender";
		
		//TABLE COMMUNITY WORKER COLUMN NAMES
		private static final String USERNAME = "_Username";
		private static final String PASSWORD = "Password";
		private static final String W_NAME = "Name";
		
		//TABLE COMMUNITY CENTRE COLUMN NAMES
		private static final String CENTRE_ID = "_Center_id";
		private static final String CENTRE_NAME = "Centre_name";
		private static final String LOCATION = "Location";
		
		//TABLE BMI COLUMN NAMES
		private static final String BMI_ID = "_BMI_id";
		private static final String DATE = "Date";
		private static final String HEIGHT = "Height";
		private static final String WEIGHT = "Weight";
		private static final String SCORE = "BMI_Score";
		
		//CREATE TABLE STATEMENTS
		//CREATE TABLE TABLE1 (_id INTEGER PRIMARY KEY,NAME VARCHAR(255));
		private static final String CREATE_TABLE1 = "CREATE TABLE "+ TABLE1+" ("+KEY_ID+" INTEGER (6) PRIMARY KEY, "+C_NAME+" VARCHAR (20), "+
				OD+" CHAR(1), "+DATE_OF_BIRTH+" DATE, "+GENDER+" CHAR(1), "+IS_ORPHAN+" CHAR(1), "+ CENTRE_ID+" INTEGER (3), FOREIGN KEY ("+ CENTRE_ID+") REFERENCES "+ TABLE3+" (" + CENTRE_ID+"));";
		
		private static final String CREATE_TABLE2 = "CREATE TABLE "+ TABLE2+" ("+USERNAME+" VARCHAR (10) PRIMARY KEY, "+ PASSWORD + " VARCHAR (10), "+
				 W_NAME + " VARCHAR (20));";
		
		private static final String CREATE_TABLE3 = "CREATE TABLE "+ TABLE3+" ("+CENTRE_ID + " INTEGER (3) PRIMARY KEY, "+ CENTRE_NAME + " VARCHAR (20), "+
				LOCATION + " VARCHAR (20));";
		
		private static final String CREATE_TABLE4 = "CREATE TABLE "+ TABLE4+" ("+ BMI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
				HEIGHT + " INTEGER (3), "+WEIGHT+" DECIMAL (5,2), "+ SCORE+ " DECIMAL (5,2));";
		
		private static final String CREATE_TABLE5 = "CREATE TABLE "+ TABLE5+" ("+ BMI_ID + " INTEGER (5) NOT NULL REFERENCES "+ TABLE4+"("+ BMI_ID+"), "
		+ KEY_ID +" INTEGER (6) NOT NULL REFERENCES "+ TABLE1+"("+ KEY_ID+"), "+ CENTRE_ID +" INTEGER (3) NOT NULL REFERENCES "+ TABLE3+"("+ CENTRE_ID+"), "+ DATE + " VARCHAR (10), PRIMARY KEY ("+KEY_ID+","+BMI_ID+"));";
		
		private static final String CREATE_TABLE6 = "CREATE TABLE "+ TABLE6+" ("+C_NAME+" VARCHAR (20) PRIMARY KEY, "+ OD+" CHAR(1), "+DATE_OF_BIRTH+" DATE, "+GENDER+" CHAR(1), "+IS_ORPHAN+" CHAR(1), "+ CENTRE_ID+" INTEGER (3), FOREIGN KEY ("+ CENTRE_ID+") REFERENCES "+ TABLE3+" (" + CENTRE_ID+"));";
		
		private Context context;
		
		public HopeHelper (Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
			try {
				db.execSQL(CREATE_TABLE1);
				db.execSQL(CREATE_TABLE2);
				db.execSQL(CREATE_TABLE3);
				db.execSQL(CREATE_TABLE4);
				db.execSQL(CREATE_TABLE5);
				db.execSQL(CREATE_TABLE6);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				Message.message(context, ""+e);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			// on upgrade drop older tables
	        try {
	        	db.execSQL("DROP TABLE IF EXISTS " + TABLE3);
				db.execSQL("DROP TABLE IF EXISTS " + TABLE1);
				db.execSQL("DROP TABLE IF EXISTS " + TABLE6);
				db.execSQL("DROP TABLE IF EXISTS " + TABLE2);
				db.execSQL("DROP TABLE IF EXISTS " + TABLE4);
				db.execSQL("DROP TABLE IF EXISTS " + TABLE5);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        // create new tables
	        onCreate(db);
		}

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#onOpen(android.database.sqlite.SQLiteDatabase)
		 */
		@Override
		public void onOpen(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			super.onOpen(db);
			if (!db.isReadOnly()) {
		        // Enable foreign key constraints
		        db.execSQL("PRAGMA foreign_keys=ON;");
		    }
		}

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#close()
		 */
		@Override
		public synchronized void close() {
			// TODO Auto-generated method stub
			//super.close();
		}
		
		
		
	}

	
	
}
