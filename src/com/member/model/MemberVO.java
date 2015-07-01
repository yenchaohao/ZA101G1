package com.member.model;

import java.io.Serializable;
import java.sql.Date;

public class MemberVO implements Serializable {
	
	String member_id;
	String email;
	String mem_name;
	String password;
	String id_no;
	String tel;
	String address;
	Date birthday;
	Date joindate;
	byte[] pic;
	Integer credit;
	Integer having_p;
	Integer pending_p;
	Integer mem_status;
	String my_wish;
	
	public MemberVO(){}
	

	public MemberVO(String member_id,String email, String mem_name, String password,
			String id_no, String tel, String address, Date birthday,
			Date joindate, byte[] pic, Integer credit,
			Integer having_p, Integer pending_p, Integer mem_status,
			String my_wish) {
		super();
		this.member_id = member_id;
		this.email = email;
		this.mem_name = mem_name;
		this.password = password;
		this.id_no = id_no;
		this.tel = tel;
		this.address = address;
		this.birthday = birthday;
		this.joindate = joindate;
		this.pic = pic;
		this.credit = credit;
		this.having_p = having_p;
		this.pending_p = pending_p;
		this.mem_status = mem_status;
		this.my_wish = my_wish;
	}


	
	
	public String getMember_id() {
		return member_id;
	}


	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMem_name() {
		return mem_name;
	}

	public void setMem_name(String mem_name) {
		this.mem_name = mem_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId_no() {
		return id_no;
	}

	public void setId_no(String id_no) {
		this.id_no = id_no;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getJoindate() {
		return joindate;
	}

	public void setJoindate(Date joindate) {
		this.joindate = joindate;
	}

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}



	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public Integer getHaving_p() {
		return having_p;
	}

	public void setHaving_p(Integer having_p) {
		this.having_p = having_p;
	}

	public Integer getPending_p() {
		return pending_p;
	}

	public void setPending_p(Integer pending_p) {
		this.pending_p = pending_p;
	}

	public Integer getMem_status() {
		return mem_status;
	}

	public void setMem_status(Integer mem_status) {
		this.mem_status = mem_status;
	}

	public String getMy_wish() {
		return my_wish;
	}

	public void setMy_wish(String my_wish) {
		this.my_wish = my_wish;
	}
	

}
