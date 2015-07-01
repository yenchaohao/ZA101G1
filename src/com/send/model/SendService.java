package com.send.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;



public class SendService {
	
	private Send_interface send;
	private Send_interface sendHibernate;
	
	
	public SendService(){
		send = new SendDAO();
		sendHibernate = new SendHibernateDAO();
	}
	
	public SendVO addSend(int gid,String member_id,int tid){
		SendVO sendvo = new SendVO();
		sendvo.setGid(gid);
		sendvo.setMember_id(member_id);
		sendvo.setTid(tid);
		sendHibernate.insert(sendvo);
		
		return sendvo;
	}
	
	public SendVO updateSend(int gid,String member_id,Timestamp end_date,int tid,int status,int sid){
		
		SendVO sendvo = new SendVO();
		sendvo.setGid(gid);
		sendvo.setMember_id(member_id);
		sendvo.setEnd_date(end_date);
		sendvo.setTid(tid);
		sendvo.setStatus(status);
		sendvo.setSid(sid);
		
		sendHibernate.update(sendvo);
		
		return sendvo;
	}
	public SendVO updateSendByObject(SendVO sendvo){
		
		sendHibernate.update(sendvo);
		
		return sendvo;
	}
	
	public void deleteSend(Integer sid){
		send.deleteBySid(sid);
	}
 	
	public SendVO getOneSend(Integer sid){
		return sendHibernate.findBySid(sid);
	}
	public List<SendVO> getAll(){
		return sendHibernate.getAll();
	}
	public List<SendVO> getAllAlive(){
		return sendHibernate.getAllAlive();
	}
	

	public List<SendVO> getAllByStatus(Integer status){
		return sendHibernate.getAllByStatus(status);
	}
	
	public List<SendVOAssociations> getAllByStatusAssociations(Integer status){
		return sendHibernate.getAllByStatusAssociations(status);
	}
	
	public List<SendVOAssociations> getAllByAddressKeyAssociations(String address){
		return sendHibernate.getAllByAddressKeyAssociations(address);
	}
	
	public List<SendVOAssociations> getAllByCompositeQuery(Map<String,String[]> map){
		return sendHibernate.CompositeQuery(map);
	}
	
	public List<SendVO> getSendByTid(Integer tid){
		return sendHibernate.findByTid(tid);
	}

	public List<SendVO> getSendByTidUn(Integer tid){
		return sendHibernate.findByTidUn(tid);
	}
	
	public List<SendVO> getSendByTidFinal(Integer tid){
		return sendHibernate.findByTidFinal(tid);
	}
}
