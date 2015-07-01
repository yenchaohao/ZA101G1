package com.remessage.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RemessageVO implements Serializable {
	private Integer rid;
	private String member_id;
	private Integer mid;
	private String message;
	private Timestamp r_date;
	private Integer r_status;
	public Integer getRid() {
		return rid;
	}
	public Integer getR_status() {
		return r_status;
	}
	public void setR_status(Integer r_status) {
		this.r_status = r_status;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String email) {
		this.member_id = email;
	}
	public Integer getMid() {
		return mid;
	}
	public void setMid(Integer mid) {
		this.mid = mid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Timestamp getR_date() {
		return r_date;
	}
	public String getR_date_str() {
		java.util.Date date=new  java.util.Date(r_date.getTime());
		String time=formatDateTime(date);
		return time;
	}
	public void setR_date(Timestamp r_date) {
		this.r_date = r_date;
	}
	
	private String formatDateTime(java.util.Date date) {
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date_str=df.format(date);
		Calendar current= Calendar.getInstance(); 
		
		Calendar today= Calendar.getInstance(); //今天
		  today.set( Calendar.HOUR_OF_DAY, 0);  
	      today.set( Calendar.MINUTE, 0);  
	      today.set(Calendar.SECOND, 0);  
	      
	      Calendar yesterday = Calendar.getInstance();    //昨天           
	        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));  
	        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));  
	        yesterday.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)-1);  
	        yesterday.set( Calendar.HOUR_OF_DAY, 0);  
	        yesterday.set( Calendar.MINUTE, 0);  
	        yesterday.set(Calendar.SECOND, 0);  
	        
	        current.setTime(date);  
	        if(current.after(today)){
	        	return "今天  "+date_str.split(" ")[1];
	        }else if(current.before(today) && current.after(yesterday)){
	        	return "昨天  "+date_str.split(" ")[1];
	        }else{
	        	return date_str.substring(date_str.indexOf("-")+1,date_str.length());
	        }
	}
}
