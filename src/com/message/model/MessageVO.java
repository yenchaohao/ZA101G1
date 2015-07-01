package com.message.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MessageVO implements Serializable {
	private Integer mid;
	private String member_id;
	private String title;
	private Timestamp m_date;
	private Integer m_status;
	public Integer getM_status() {
		return m_status;
	}
	public void setM_status(Integer m_status) {
		this.m_status = m_status;
	}
	public Integer getMid() {
		return mid;
	}
	public void setMid(Integer mid) {
		this.mid = mid;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String email) {
		this.member_id = email;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Timestamp getM_date() {
		return m_date;
	}
	
	public String getM_date_str() {
		java.util.Date date=new  java.util.Date(m_date.getTime());	
		String time=formatDateTime(date);
		return time;
		
	}
	public void setM_date(Timestamp m_date) {
		this.m_date = m_date;
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
