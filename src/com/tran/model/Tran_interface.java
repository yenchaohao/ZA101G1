package com.tran.model;

import java.util.List;

public interface Tran_interface {
	public void insert(TranVO tranvo);
	public void update(TranVO tranvo);
	public void deleteByTid(Integer tid);
	public TranVO findByTid(Integer tid);
	public List<TranVO> getAll();
	public List<TranVO> getUnreply();
	public List<TranVO> findByReqMember_id(String member_id);
	public List<TranVO> findByResMember_id(String member_id);
	public List<TranVO> findByReqMember_idUn(String member_id);
	public List<TranVO> findByResMember_idUn(String member_id);
	public List<TranVO> getAllByMember_idFinal(String member_id);
	public List<TranVO> getAllByMember_idFail(String member_id);
	public List<TranVO> getAllByMember_idWait(String member_id);
	public List<TranVO> getAllByMemberNoFail(String member_id);
	public List<TranVO> getAllAlive(); //µ¹showRanking.jsp¥Î
}
