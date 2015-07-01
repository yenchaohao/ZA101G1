package com.vipad.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.goods.model.GoodsVO;

public class VipadHibernateVO implements Serializable{
	
	private Integer vid;
	private String member_id;
	private GoodsVO goodsVO;
	private Timestamp joindate;
	private Timestamp quitdate;
	private Integer status;
	
	public Integer getVid() {
		return vid;
	}
	public void setVid(Integer vid) {
		this.vid = vid;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	
	public GoodsVO getGoodsVO() {
		return goodsVO;
	}
	public void setGoodsVO(GoodsVO goodsVO) {
		this.goodsVO = goodsVO;
	}
	
	public Timestamp getJoindate() {
		return joindate;
	}

	public String getJoindateFormat() {
		if(joindate != null)
		 return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(this.joindate.getTime()));
		else
			return null;
	}

	public void setJoindate(Timestamp joindate) {
		this.joindate = joindate;
	}

	public Timestamp getQuitdate() {
		return quitdate;
	}
	
	public String getQuitdateFormat() {
		if(quitdate != null)
		 return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(this.quitdate.getTime()));
		else 
			return null;
	}
	
	public void setQuitdate(Timestamp quitdate) {
		this.quitdate = quitdate;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
