package com.authority_list.model;

import java.io.Serializable;

public class Authority_listVO implements Serializable {
	private Integer aid;
	private String fname;
	public Integer getAid() {
		return aid;
	}
	public void setAid(Integer aid) {
		this.aid = aid;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	
}
