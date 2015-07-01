package com.credit.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class CreditVO implements Serializable {
	private Integer cid;
	private String memberA_id;
	private String memberB_id;
	private Integer status;
	private Integer tid;
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	public String getMemberA_id() {
		return memberA_id;
	}
	public void setMemberA_id(String memberA_id) {
		this.memberA_id = memberA_id;
	}
	public String getMemberB_id() {
		return memberB_id;
	}
	public void setMemberB_id(String memberB_id) {
		this.memberB_id = memberB_id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getTid() {
		return tid;
	}
	public void setTid(Integer tid) {
		this.tid = tid;
	}
	
	
	
}
