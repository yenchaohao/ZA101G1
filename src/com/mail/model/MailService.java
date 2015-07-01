package com.mail.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class MailService {
	
	private MailDAO mail;
	private MailHibernateDAO himail;
	
	public MailService(){
		mail = new MailDAO();
		himail = new MailHibernateDAO();
		
	}
	
	public MailVO addMail(String email,  String title,
			String question){
		MailVO mailvo = new MailVO();
		
		mailvo.setMember_id(email);
		mailvo.setTitle(title);
		mailvo.setQuestion(question);
		mailvo.setStatus(0);
		himail.insert(mailvo);
		return mailvo;
	}
	
	public MailVO addMailByObject(MailVO mailvo){
		himail.insert(mailvo);
		return mailvo;
	}
	
	public MailVO updateMail(String member_id, String empid, String title,
			String question, String answer, 
			Integer status,Integer cmid){
		
		java.util.Date date=new java.util.Date();
		Timestamp a_date=new Timestamp(date.getTime());
		
		MailVO mailvo = new MailVO();	
		mailvo.setMember_id(member_id);
		mailvo.setEmpid(empid);
		mailvo.setTitle(title);
		mailvo.setQuestion(question);
		mailvo.setAnswer(answer);
		mailvo.setA_date(a_date);
		mailvo.setQ_date(getOneMail(cmid).getQ_date());
		mailvo.setStatus(status);
		mailvo.setCmid(cmid);
		
		himail.update(mailvo);
		
		return mailvo;
	}
	
	public void deleteMail(Integer cmid){
		himail.deleteByCmid(cmid);
	}
 	
	public MailVO getOneMail(Integer cmid){
		return himail.findByCmid(cmid);
	}
	public List<MailVO> getAll(){
		return himail.getAll();
	}
	//找檢舉商品不是真的找
	public List<MailVO> findByAnswerForReport(String answer){
		return himail.findByAnswerForReport(answer);
	}
}
