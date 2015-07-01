package com.send.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class SendVO implements Serializable {
	
	Integer sid;
	Integer gid;
	String member_id;
	Integer tid;
	Timestamp start_date;
	Timestamp end_date;
	Integer status;
	
	public SendVO(){
		
	}

	
	
	
	public SendVO(Integer sid, Integer gid, String email, Integer tid,
			Timestamp start_date, Timestamp end_date, Integer status) {
		super();
		this.sid = sid;
		this.gid = gid;
		this.member_id = email;
		this.tid = tid;
		this.start_date = start_date;
		this.end_date = end_date;
		this.status = status;
	}




	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String email) {
		this.member_id = email;
	}

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public Timestamp getStart_date() {
		return start_date;
	}
	
	public String getStart_date_format(){
		if(start_date != null)
			 return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(this.start_date.getTime()));
			else
				return null;
	}
	
	public void setStart_date(Timestamp start_date) {
		this.start_date = start_date;
	}

	public Timestamp getEnd_date() {
		return end_date;
	}
	
	public String getEnd_date_format(){
		if(end_date != null)
			 return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(this.end_date.getTime()));
			else
				return null;
	}
	
	public void setEnd_date(Timestamp end_date) {
		this.end_date = end_date;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	

}
