package com.desire.model;

import java.io.Serializable;
import java.sql.Date;

public class DesireVO implements Serializable {
	private Integer did;
	private Integer groupId;
	private String member_id;
	private Date joinDate;
	public Integer getDid() {
		return did;
	}
	public void setDid(Integer did) {
		this.did = did;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String email) {
		this.member_id = email;
	}
	public Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	
	
	
	
}
