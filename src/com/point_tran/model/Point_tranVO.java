package com.point_tran.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Point_tranVO implements Serializable {
	private Integer ptid;
	private Integer status;
	private String member_id;
	private Timestamp quitDate;
	public Integer getPtid() {
		return ptid;
	}
	public void setPtid(Integer ptid) {
		this.ptid = ptid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String email) {
		this.member_id = email;
	}
	public Timestamp getQuitDate() {
		return quitDate;
	}
	public void setQuitDate(Timestamp quitDate) {
		this.quitDate = quitDate;
	}

	
}
