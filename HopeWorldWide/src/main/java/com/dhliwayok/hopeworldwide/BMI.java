package com.dhliwayok.hopeworldwide;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BMI {
	
	private String date;
	private int height;
	private double weight;
	private double bmi_score;
	
	public BMI(int height, double weight){
	
		this.height = height;
		this.weight = weight;
		this.date = getSysDate();
	}
	
	public BMI(String date, int height, double weight){
		
		this.date = date;
		this.height = height;
		this.weight = weight;
	}
	
	public double calculateBMI(){
		
		
		double h = (height*height);
		
		bmi_score = (weight/h)*10000;
		
		return round(bmi_score, 2, BigDecimal.ROUND_HALF_UP);
		
	}

	public String getDate() {
		return date;
	}

	public String getSysDate(){
		
		Calendar c = Calendar.getInstance();
		//System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = df.format(c.getTime());
		return formattedDate;
		
	}
	
	public static double round(double unrounded, int precision, int roundingMode)
	{
	    BigDecimal bd = new BigDecimal(unrounded);
	    BigDecimal rounded = bd.setScale(precision, roundingMode);
	    return rounded.doubleValue();
	}
	
	public void setDate(String date) {
		this.date = date;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getBmi_score() {
		return bmi_score;
	}


	@Override
	public String toString() {
		return "BMI [date=" + date + ", height=" + height + ", weight="
				+ weight + ", bmi_score=" + bmi_score + "]";
	}
	
	

}
