package com.message.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.remessage.model.RemessageService;
import com.remessage.model.RemessageVO;


public class MessageService {
	private MessageDAO dao;
	private MessageHibernateDAO hidao;
	List<RemessageVO> re_list;
	
	public MessageService() {
		dao = new MessageDAO();
		hidao = new MessageHibernateDAO();
	}
	public MessageVO addMessage(String member_id,String title) {

		MessageVO messageVO = new MessageVO();
		messageVO.setMember_id(member_id);
		messageVO.setTitle(title);
		hidao.insert(messageVO);

		return messageVO;
	}
	public int addMessageGetPK(String member_id,String title) {

		MessageVO messageVO = new MessageVO();
		messageVO.setMember_id(member_id);
		messageVO.setTitle(title);
		int mid=hidao.insertGetPK(messageVO);

		return mid;
	}
	public MessageVO updateMessage(Integer mid,String member_id,String title,java.sql.Timestamp m_date) {

		MessageVO messageVO = new MessageVO();

		messageVO.setMid(mid);
		messageVO.setMember_id(member_id);
		messageVO.setTitle(title);
		messageVO.setM_date(m_date);
		
		hidao.update(messageVO);

		return messageVO;
	}	
	public void deleteMessage(Integer mid) {
		hidao.delete(mid);
	}
	public MessageVO getOneEmp(Integer mid) {
		return hidao.findByPrimaryKey(mid);
	}
	public List<MessageVO> getAll() {
		return hidao.getAll();
	}
	public List<MessageVO> getAllLatest() {
		return hidao.getAllLatest();
	}
	public  List<MessageVO> search(String title) {
		return hidao.search(title);
	}
	public List<MessageVO> getAllOrderByRe() {
		List<MessageVO> list=hidao.getAllLatest();
		 RemessageService remsgSvc = new RemessageService();
		re_list = remsgSvc.getAll();
		Collections.sort(list, new Comparator<MessageVO>(){

			@Override
			public int compare(MessageVO vo1, MessageVO vo2) {
				//找到最後一個回覆
					
				RemessageVO reVO1=null;
				RemessageVO reVO2=null;
				for(int j=(re_list.size()-1);j>=0;j--){
					if(re_list.get(j).getMid().equals(vo1.getMid())){
						reVO1=re_list.get(j);						
						break;
					} 
				}
				for(int j=(re_list.size()-1);j>=0;j--){
					if(re_list.get(j).getMid().equals(vo2.getMid())){
						reVO2=re_list.get(j);						
						break;
					}
				}
				if(((reVO1.getR_date().getTime())-(reVO2.getR_date().getTime()))>0){
					return -1;				
				}else if(((reVO1.getR_date().getTime())-(reVO2.getR_date().getTime()))<0){
					return 1;
				}else{return 0;}
			}
			
		});
		return list;
	}
}
