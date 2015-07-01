package com.report.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.goods.model.GoodsVOHibernate;


public class ReportVOHibernate implements Serializable {
	
	Integer rid;
	GoodsVOHibernate goodsvo;
	String member_id;
	Timestamp r_date;


	public ReportVOHibernate(){}
	
	public ReportVOHibernate(Integer rid, String member_id, Timestamp r_date) {
		super();
		this.rid = rid;
		this.member_id = member_id;
		this.r_date = r_date;
	}

	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
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

	public GoodsVOHibernate getGoodsvo() {
		return goodsvo;
	}

	public void setGoodsvo(GoodsVOHibernate goodsvo) {
		this.goodsvo = goodsvo;
	}
	
	
	
	

}
