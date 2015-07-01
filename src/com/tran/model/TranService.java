package com.tran.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;



public class TranService {
	
private TranDAO tran;
private TranHibernateDAO hitran;
	
	public TranService(){
		tran = new TranDAO();
		hitran = new TranHibernateDAO();
	}
	
	public TranVO addTran(String res_member_id,int  res_gid,String req_member_id,int req_gid){
		TranVO tranvo = new TranVO();
		tranvo.setRes_member_id(res_member_id);
        tranvo.setRes_gid(res_gid);
        tranvo.setReq_member_id(req_member_id);
        tranvo.setReq_gid(req_gid);
       
		hitran.insert(tranvo);
		return tranvo;
	}
	
	public TranVO updateTran(String res_member_id,int  res_gid,String req_member_id,int req_gid,Timestamp res_date,int status,int tid){
		
		TranVO tranvo = new TranVO();
		tranvo.setRes_member_id(res_member_id);
		tranvo.setRes_gid(res_gid);
		tranvo.setReq_member_id(req_member_id);
		tranvo.setReq_gid(req_gid);
		tranvo.setRes_date(res_date);
		tranvo.setStatus(status);
		tranvo.setTid(tid);
		tranvo.setReq_date(getOneTran(tid).getReq_date());
		hitran.update(tranvo);
		
		return tranvo;
	}
public TranVO updateTranByObject(TranVO tranVO){
	
		hitran.update(tranVO);
		
		return tranVO;
	}
	
	
	public void deleteTran(Integer tid){
		hitran.deleteByTid(tid);
	}
 	
	public TranVO getOneTran(Integer tid){
		return hitran.findByTid(tid);
	}
	public List<TranVO> getOneByReqMember_id(String member_id){
		return hitran.findByReqMember_id(member_id);
	}
	public List<TranVO> getOneByResMember_id(String member_id){
		return hitran.findByResMember_id(member_id);
	}
	public List<TranVO> getAll(){
		return hitran.getAll();
	}
	
	public List<TranVO> getUnreply(){
		return hitran.getUnreply();
	}
	
	public List<TranVO> getOneByReqMember_idUn(String member_id){
		return hitran.findByReqMember_idUn(member_id);
	}

	public List<TranVO> getOneByResMember_idUn(String member_id){
		return hitran.findByResMember_idUn(member_id);
	}
	
	public List<TranVO> getAllByMember_idFinal(String member_id){
		return hitran.getAllByMember_idFinal(member_id);
	}
	public List<TranVO> getAllByMember_idFail(String member_id){
		return hitran.getAllByMember_idFail(member_id);
	}
	public List<TranVO> getAllByMember_idWait(String member_id){
		return hitran.getAllByMember_idWait(member_id);
	}
	public List<TranVO> getAllByMemberNoFail(String member_id){
		return hitran.getAllByMemberNoFail(member_id);
	}
	public List<TranVO> getAllAlive(){
		return hitran.getAllAlive();
	}
}
