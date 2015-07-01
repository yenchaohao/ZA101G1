package com.mail.model;

import java.util.List;



public interface Mail_interface {
	public int insert(MailVO mailvo);
	public int update(MailVO mailvo);
	public int deleteByCmid(Integer cmid);
	public MailVO findByCmid(Integer cmid);
	public List<MailVO> getAll();
	public List<MailVO> findByAnswerForReport(String answer);
}
