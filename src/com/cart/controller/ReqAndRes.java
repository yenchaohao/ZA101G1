package com.cart.controller;

import java.io.Serializable;

public class ReqAndRes implements Serializable{
	private Integer req_gid;
	private Integer res_gid;
	private String req_member_id;
	private String res_member_id;
	
	public String getReq_member_id() {
		return req_member_id;
	}
	public void setReq_member_id(String req_member_id) {
		this.req_member_id = req_member_id;
	}
	public String getRes_member_id() {
		return res_member_id;
	}
	public void setRes_member_id(String res_member_id) {
		this.res_member_id = res_member_id;
	}
	public Integer getReq_gid() {
		return req_gid;
	}
	public void setReq_gid(Integer req_gid) {
		this.req_gid = req_gid;
	}
	public Integer getRes_gid() {
		return res_gid;
	}
	public void setRes_gid(Integer res_gid) {
		this.res_gid = res_gid;
	}
	
}
