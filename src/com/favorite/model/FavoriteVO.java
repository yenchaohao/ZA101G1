package com.favorite.model;

import java.io.Serializable;
import java.sql.Date;

public class FavoriteVO implements Serializable {
	private Integer fid;
	private Integer gid;
	private String member_id;
	private Date joindate;
	
	
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
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
	public Date getJoindate() {
		return joindate;
	}
	public void setJoindate(Date joindate) {
		this.joindate = joindate;
	}
	
}
