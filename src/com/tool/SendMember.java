package com.tool;

import java.io.Serializable;

public class SendMember implements Serializable {

	String address;
	String memberImage;
	String mem_name;
	String goodsImage;
	String goods_name;
	
	public SendMember(){}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMemberImage() {
		return memberImage;
	}
	public void setMemberImage(String memberImage) {
		this.memberImage = memberImage;
	} 
	public String getMem_name() {
		return mem_name;
	}
	public void setMem_name(String mem_name) {
		this.mem_name = mem_name;
	}
	public String getGoodsImage() {
		return goodsImage;
	}
	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	
	
	
}
