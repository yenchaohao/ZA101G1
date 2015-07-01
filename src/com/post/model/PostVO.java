package com.post.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class PostVO implements Serializable {
	Integer pid;
	String empid;
	String title;
	String post;
	Timestamp postdate;
	byte[] pic;
	
	public PostVO(){
		
	}
	
	public PostVO(Integer pid, String empid, String title, String post,
			Timestamp postdate, byte[] pic) {
		super();
		this.pid = pid;
		this.empid = empid;
		this.title = title;
		this.post = post;
		this.postdate = postdate;
		this.pic = pic;
	}

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getEmpid() {
		return empid;
	}
	public void setEmpid(String empid) {
		this.empid = empid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public Timestamp getPostdate() {
		return postdate;
	}
	public void setPostdate(Timestamp postdate) {
		this.postdate = postdate;
	}
	
	

}
