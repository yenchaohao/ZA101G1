package com.emp.model;

import java.io.Serializable;
import java.sql.Date;

public class EmpVO implements Serializable{
	private String empid;
	private String ename;
	private String password;
	private Date hiredate;
	private byte[] pic;
	private String email;
	private Integer status;
	
	public EmpVO(){}

	public EmpVO(String empid, String ename, String password, Date hiredate,
			byte[] pic, String email, Integer status) {
		super();
		this.empid = empid;
		this.ename = ename;
		this.password = password;
		this.hiredate = hiredate;
		this.pic = pic;
		this.email = email;
		this.status = status;
	}

	public String getEmpid() {
		return empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getHiredate() {
		return hiredate;
	}

	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}

