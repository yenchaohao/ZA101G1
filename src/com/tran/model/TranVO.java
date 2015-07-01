package com.tran.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TranVO implements Serializable {
	Integer tid;
	String res_member_id;
	Integer res_gid;
	String req_member_id;
	Integer req_gid;
	Timestamp res_date;
	Timestamp req_date;
	Integer status;

	public TranVO() {
	}

	public TranVO(Integer tid, String res_email, Integer res_gid,
			String req_email, Integer req_gid, Timestamp res_date,
			Timestamp req_date, Integer status) {
		super();
		this.tid = tid;
		this.res_member_id = res_email;
		this.res_gid = res_gid;
		this.req_member_id = req_email;
		this.req_gid = req_gid;
		this.res_date = res_date;
		this.req_date = req_date;
		this.status = status;
	}

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public String getRes_member_id() {
		return res_member_id;
	}

	public void setRes_member_id(String res_email) {
		this.res_member_id = res_email;
	}

	public Integer getRes_gid() {
		return res_gid;
	}

	public void setRes_gid(Integer res_gid) {
		this.res_gid = res_gid;
	}

	public String getReq_member_id() {
		return req_member_id;
	}

	public void setReq_member_id(String req_email) {
		this.req_member_id = req_email;
	}

	public Integer getReq_gid() {
		return req_gid;
	}

	public void setReq_gid(Integer req_gid) {
		this.req_gid = req_gid;
	}

	public Timestamp getRes_date() {
		return res_date;
	}

	public String getRes_date_str() {
		if (this.res_date != null) {
			java.util.Date date = new java.util.Date(res_date.getTime());
			String text = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(date);
			return text;
		} else
			return null;

	}

	public void setRes_date(Timestamp res_date) {
		this.res_date = res_date;
	}

	public Timestamp getReq_date() {
		return req_date;
	}

	public String getReq_date_str() {
		if (this.req_date != null) {
			java.util.Date date = new java.util.Date(req_date.getTime());
			String text = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(date);
			return text;
		} else
			return null;
	}

	public void setReq_date(Timestamp req_date) {
		this.req_date = req_date;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
