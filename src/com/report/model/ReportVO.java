package com.report.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class ReportVO implements Serializable {
	
	Integer rid;
	Integer gid;
	String member_id;
	Timestamp r_date;
	
	public ReportVO(){}
	
	public ReportVO(Integer rid, Integer gid, String member_id, Timestamp r_date) {
		super();
		this.rid = rid;
		this.gid = gid;
		this.member_id = member_id;
		this.r_date = r_date;
	}

	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
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
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public Timestamp getR_date() {
		return r_date;
	}
	public void setR_date(Timestamp r_date) {
		this.r_date = r_date;
	}
	
	
	
	

}
