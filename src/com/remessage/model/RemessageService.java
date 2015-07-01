package com.remessage.model;

import java.util.List;


public class RemessageService {
	private RemessageDAO dao;
	private RemessageHibernateDAO hidao;

	public RemessageService() {
		dao = new RemessageDAO();
		hidao = new RemessageHibernateDAO();
	}
	public RemessageVO addRemessage(String member_id,Integer mid,String message) {

		RemessageVO messageVO = new RemessageVO();
		messageVO.setMember_id(member_id);
		messageVO.setMid(mid);
		messageVO.setMessage(message);		
		hidao.insert(messageVO);

		return messageVO;
	}
	public RemessageVO updateRemessage(Integer rid,String member_id,Integer mid,String message,java.sql.Timestamp r_date) {

		RemessageVO messageVO = new RemessageVO();

		messageVO.setRid(rid);
		messageVO.setMember_id(member_id);
		messageVO.setMid(mid);
		messageVO.setMessage(message);
		messageVO.setR_date(r_date);
		
		hidao.update(messageVO);

		return messageVO;
	}	
	public void deleteRemessage(Integer rid) {
		hidao.delete(rid);
	}
	public RemessageVO getOneEmp(Integer rid) {
		return hidao.findByPrimaryKey(rid);
	}
	public List<RemessageVO> getAll() {
		return hidao.getAll();
	}
	public List<RemessageVO> getByMid(Integer mid) {
		return hidao.getByMid(mid);
	}
}
