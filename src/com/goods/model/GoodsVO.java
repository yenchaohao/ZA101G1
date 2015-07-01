package com.goods.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class GoodsVO implements Serializable {
	private Integer gid;
	private Integer groupid;
	private String member_id;
	private String g_name;
	private String g_describe;
	private Integer g_price;
	private Integer g_level;
	private Integer g_hot;
	private Timestamp joindate;
	private Timestamp quitdate;
	private byte[] pic;
	private Integer goods_status;

	public GoodsVO() {
	}

	public GoodsVO(Integer gid, Integer groupid, String member_id,
			String g_name, String g_describe, Integer g_price, Integer g_level,
			Integer g_hot, Timestamp joindate, Timestamp quitdate, byte[] pic,
			Integer goods_status) {
		super();
		this.gid = gid;
		this.groupid = groupid;
		this.member_id = member_id;
		this.g_name = g_name;
		this.g_describe = g_describe;
		this.g_price = g_price;
		this.g_level = g_level;
		this.g_hot = g_hot;
		this.joindate = joindate;
		this.quitdate = quitdate;
		this.pic = pic;
		this.goods_status = goods_status;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getG_name() {
		return g_name;
	}

	public void setG_name(String g_name) {
		this.g_name = g_name;
	}

	public String getG_describe() {
		return g_describe;
	}

	public void setG_describe(String g_describe) {
		this.g_describe = g_describe;
	}

	public Integer getG_price() {
		return g_price;
	}

	public void setG_price(Integer g_price) {
		this.g_price = g_price;
	}

	public Integer getG_level() {
		return g_level;
	}

	public void setG_level(Integer g_level) {
		this.g_level = g_level;
	}

	public Integer getG_hot() {
		return g_hot;
	}

	public void setG_hot(Integer g_hot) {
		this.g_hot = g_hot;
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

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public Integer getGoods_status() {
		return goods_status;
	}

	public void setGoods_status(Integer goods_status) {
		this.goods_status = goods_status;
	}

}
