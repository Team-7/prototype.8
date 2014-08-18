package com.dhliwayok.hopeworldwide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

public class MyConnect extends AsyncTask {
	
	private HopeDbAdapter mydb;
    private String webLink= "http://team7.web44.net/sync/";
	
	
	public MyConnect(HopeDbAdapter mydb) {

		this.mydb = mydb;
	}


	public boolean parseJSON(int center)  
    {  
          
        String result = "";  
        boolean x = false;  
        InputStream is=null;  
        //http post  
        JSONObject json ;// new JSONObject();
	    	  //System.out.println(cs.getCount());
	      
			   JSONArray jsonarray = new JSONArray();
	           json = new JSONObject();  
	      try {  
	           json.put("Center",center); 
	           jsonarray.put(json);  
	      }  
	      catch (Exception e) {  
	           Log.d("Android", "JSON Error"+e);  
	      } 
        
        
        try{   
        	
                //ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
                //ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
        		StringEntity se = new StringEntity(jsonarray.toString());  
                HttpClient httpclient = new DefaultHttpClient();  
                HttpPost httppost = new HttpPost(webLink+"get_child.php");
                httppost.setEntity(se);  
                HttpResponse response = httpclient.execute(httppost);  
                HttpEntity entity = response.getEntity();  
                is = entity.getContent();  
        }catch(Exception e){  
                Log.e("log_tag", "Error in http connection "+e.toString());  
        }  
        //convert response to string  
        try{  
                  
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);  
                StringBuilder sb = new StringBuilder();  
                String line = null;  
                while ((line = reader.readLine()) != null) {  
                        sb.append(line + "\n");  
                }  
                is.close();  
           
                result=sb.toString();  
                Log.e("log_tag", result);
                  
        }catch(Exception e){  
                Log.e("log_tag", "Error converting result "+e.toString());  
        }  
        // </namevaluepair> </namevaluepair> 
         
         try{
        	 JSONArray jArray = new JSONArray(result);
        	  
             for(int i=0;i<jArray.length();i++){  
                 JSONObject json_data = jArray.getJSONObject(i);  
                 //String querylog= "insert or replace into table(a,b,c) values('"+json_data.getString("a")+"','"+json_data.getString("b")+"','"+json_data.getString("c")+"')";  
                 //Log.e("log_tag", json_data.getString("child_name"));
                 
                 if (mydb.insertChild(json_data.getInt("child_id"), json_data.getString("child_name"), json_data.getString("OD"), json_data.getString("Date_Of_Birth"),json_data.getString("Gender"), json_data.getString("Is_orphan"), json_data.getInt("centre_id"))> 0) 
                	 x = true;
             }
        	}
        catch (JSONException e){   
        Log.e("log_tag", "Error parsing data "+e.toString());  
        } 
	
         return x;
    }
	
	public boolean getCommunityWorker()  
    {  
          
        String result = "";  
        boolean x = false;  
        InputStream is=null;  
        //http post  
        try{   
        	
                //ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  

                HttpClient httpclient = new DefaultHttpClient();  
                HttpPost httppost = new HttpPost(webLink+"get_cw.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));  
                HttpResponse response = httpclient.execute(httppost);  
                HttpEntity entity = response.getEntity();  
                is = entity.getContent();  
        }catch(Exception e){  
                Log.e("log_tag", "Error in http connection "+e.toString());  
        }  
        //convert response to string  
        try{  
                  
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);  
                StringBuilder sb = new StringBuilder();  
                String line = null;  
                while ((line = reader.readLine()) != null) {  
                        sb.append(line + "\n");  
                }  
                is.close();  
           
                result=sb.toString();  
                Log.e("log_tag", result);
                  
        }catch(Exception e){  
                Log.e("log_tag", "Error converting result "+e.toString());  
        }  
        // </namevaluepair> </namevaluepair> 
         
         try{
        	 JSONArray jArray = new JSONArray(result);
        	  
             for(int i=0;i<jArray.length();i++){  
                 JSONObject json_data = jArray.getJSONObject(i);  
                 //String querylog= "insert or replace into table(a,b,c) values('"+json_data.getString("a")+"','"+json_data.getString("b")+"','"+json_data.getString("c")+"')";  
                 //Log.e("log_tag", json_data.getString("child_name"));
                 
                 if (mydb.insertCommunityWorker(json_data.getString("username"), json_data.getString("password"), json_data.getString("name"))> 0) 
                	 x = true;
             }
        	}
        catch (JSONException e){   
        Log.e("log_tag", "Error parsing data "+e.toString());  
        } 
	
         return x;
    }
	
	public boolean getChild_Bmi(int center)  
    {  
          
        String result = "";  
        boolean x = false;  
        InputStream is=null;  
        //http post  
        
        JSONObject json ;// new JSONObject();
  	  //System.out.println(cs.getCount());
    
		  JSONArray jsonarray = new JSONArray();
         json = new JSONObject();  
    try {  
         json.put("Center",center);    
         jsonarray.put(json);  
    }  
    catch (Exception e) {  
         Log.d("Android", "JSON Error"+e);  
    } 
    
        try{   
        	
                //ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
                //ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
        		StringEntity se = new StringEntity(jsonarray.toString()); 
                HttpClient httpclient = new DefaultHttpClient();  
                HttpPost httppost = new HttpPost(webLink+"get_mychildBmi.php");
                httppost.setEntity(se);  
                HttpResponse response = httpclient.execute(httppost);  
                HttpEntity entity = response.getEntity();  
                is = entity.getContent();  
        }catch(Exception e){  
                Log.e("log_tag", "Error in http connection "+e.toString());  
        }  
        //convert response to string  
        try{  
                  
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);  
                StringBuilder sb = new StringBuilder();  
                String line = null;  
                while ((line = reader.readLine()) != null) {  
                        sb.append(line + "\n");  
                }  
                is.close();  
           
                result=sb.toString();  
                Log.e("log_tag", result);
                  
        }catch(Exception e){  
                Log.e("log_tag", "Error converting result "+e.toString());  
        }  
        // </namevaluepair> </namevaluepair> 
         
         try{
        	 JSONArray jArray = new JSONArray(result);
        	  
             for(int i=0;i<jArray.length();i++){  
                 JSONObject json_data = jArray.getJSONObject(i);  
                 //String querylog= "insert or replace into table(a,b,c) values('"+json_data.getString("a")+"','"+json_data.getString("b")+"','"+json_data.getString("c")+"')";  
                 //Log.e("log_tag", json_data.getString("child_name"));
                 
                 if (mydb.insertChildBMI(json_data.getInt("BMI_id"), json_data.getInt("child_id"), json_data.getInt("centre_id"), json_data.getString("date"))> 0) 
                	 x = true;
             }
        	}
        catch (JSONException e){   
        Log.e("log_tag", "Error parsing data "+e.toString());  
        } 
	
         return x;
    }

	public boolean getBmi(int center)  
    {  
          
        String result = "";  
        boolean x = false;  
        InputStream is=null;  
        //http post 
        
        JSONObject json ;// new JSONObject();
    	  //System.out.println(cs.getCount());
      
  		   JSONArray jsonarray = new JSONArray();
           json = new JSONObject();  
      try {  
           json.put("Center",center);    
           jsonarray.put(json);  
      }  
      catch (Exception e) {  
           Log.d("Android", "JSON Error"+e);  
      } 
        try{   
        	
                //ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
               // ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
        	    StringEntity se = new StringEntity(jsonarray.toString()); 
                HttpClient httpclient = new DefaultHttpClient();  
                HttpPost httppost = new HttpPost(webLink+"get_mybmi.php");
                httppost.setEntity(se);  
                HttpResponse response = httpclient.execute(httppost);  
                HttpEntity entity = response.getEntity();  
                is = entity.getContent();  
        }catch(Exception e){  
                Log.e("log_tag", "Error in http connection "+e.toString());  
        }  
        //convert response to string  
        try{  
                  
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);  
                StringBuilder sb = new StringBuilder();  
                String line = null;  
                while ((line = reader.readLine()) != null) {  
                        sb.append(line + "\n");  
                }  
                is.close();  
           
                result=sb.toString();  
                Log.e("log_tag", result);
                  
        }catch(Exception e){  
                Log.e("log_tag", "Error converting result "+e.toString());  
        }  
        // </namevaluepair> </namevaluepair> 
         
         try{
        	 JSONArray jArray = new JSONArray(result);
        	  
             for(int i=0;i<jArray.length();i++){  
                 JSONObject json_data = jArray.getJSONObject(i);  
                 //String querylog= "insert or replace into table(a,b,c) values('"+json_data.getString("a")+"','"+json_data.getString("b")+"','"+json_data.getString("c")+"')";  
                 //Log.e("log_tag", json_data.getString("child_name"));
                 
                 if (mydb.insertBMI(json_data.getInt("BMI_id"), json_data.getInt("height"), json_data.getDouble("weight"), json_data.getDouble("score"))> 0) 
                	 x = true;
             }
        	}
        catch (JSONException e){   
        Log.e("log_tag", "Error parsing data "+e.toString());  
        } 
	
         return x;
    }

	public boolean getECD()  
    {  
          
        String result = "";  
        boolean x = false;  
        InputStream is=null;  
        //http post  
        try{   
        	
                //ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  

                HttpClient httpclient = new DefaultHttpClient();  
                HttpPost httppost = new HttpPost(webLink+"get_cc.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));  
                HttpResponse response = httpclient.execute(httppost);  
                HttpEntity entity = response.getEntity();  
                is = entity.getContent();  
        }catch(Exception e){  
                Log.e("log_tag", "Error in http connection "+e.toString());  
        }  
        //convert response to string  
        try{  
                  
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);  
                StringBuilder sb = new StringBuilder();  
                String line = null;  
                while ((line = reader.readLine()) != null) {  
                        sb.append(line + "\n");  
                }  
                is.close();  
           
                result=sb.toString();  
                Log.e("log_tag", result);
                  
        }catch(Exception e){  
                Log.e("log_tag", "Error converting result "+e.toString());  
        }  
        // </namevaluepair> </namevaluepair> 
         
         try{
        	 JSONArray jArray = new JSONArray(result);
        	  
             for(int i=0;i<jArray.length();i++){  
                 JSONObject json_data = jArray.getJSONObject(i);  
                 //String querylog= "insert or replace into table(a,b,c) values('"+json_data.getString("a")+"','"+json_data.getString("b")+"','"+json_data.getString("c")+"')";  
                 //Log.e("log_tag", json_data.getString("child_name"));
                 
                 if (mydb.insertECD(json_data.getInt("centre_id"), json_data.getString("centre_name"), json_data.getString("location"))> 0) 
                	 x = true;
                 //mydb.close();
             }
        	}
        catch (JSONException e){   
        Log.e("log_tag", "Error parsing data "+e.toString());  
        } 
	
         return x;
    }


	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		 if(getECD())
			 System.out.println("ECD success");
		 
		//if(parseJSON())
			//System.out.println("Child success");
		
		if(getCommunityWorker())
			System.out.println("Cw success");
		
		//if(getBmi())
			//System.out.println("BMi success");
		
	//	if(getChild_Bmi())
			//System.out.println("BMi_child success");
		//mydb.close();
		return null;
	}

}
