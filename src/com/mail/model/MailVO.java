package com.mail.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class MailVO implements Serializable {
	Integer cmid;
	String member_id;
	String empid;
	String title;
	String question;
	String answer;
	Timestamp q_date;
	Timestamp a_date;
	Integer status;

	public MailVO() {

	}

	public MailVO(Integer cmid, String email, String empid, String title,
			String question, String answer, Timestamp q_date, Timestamp a_date,
			Integer status) {
		super();
		this.cmid = cmid;
		this.member_id = email;
		this.empid = empid;
		this.title = title;
		this.question = question;
		this.answer = answer;
		this.q_date = q_date;
		this.a_date = a_date;
		this.status = status;
	}

	public Integer getCmid() {
		return cmid;
	}

	public void setCmid(Integer cmid) {
		this.cmid = cmid;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String email) {
		this.member_id = email;
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

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Timestamp getQ_date() {
		return q_date;
	}

	public String getQ_date_str() {
		if (q_date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date_str = df.format(q_date);
			return date_str;
		} else
			return null;
	}

	public void setQ_date(Timestamp q_date) {
		this.q_date = q_date;
	}

	public Timestamp getA_date() {
		return a_date;
	}

	public String getA_date_str() {
		if (a_date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date_str = df.format(a_date);
			return date_str;
		}else
			return null;
	}

	public void setA_date(Timestamp a_date) {
		this.a_date = a_date;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
