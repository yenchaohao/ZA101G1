package com.authority.model;

import java.io.Serializable;

public class AuthorityVO implements Serializable {
	private String empid;
	private Integer aid;
	public Integer getAid() {
		return aid;
	}
	public void setAid(Integer aid) {
		this.aid = aid;
	}
	public String getEmpid() {
		return empid;
	}
	public void setEmpid(String empid) {
		this.empid = empid;
	}
	
}
