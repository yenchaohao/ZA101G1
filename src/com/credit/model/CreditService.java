package com.credit.model;

import java.sql.Timestamp;
import java.util.List;


public class CreditService {
	private Credit_interface dao;

	public CreditService() {
		dao = new CreditDAO();
	}
	public CreditVO addCredit(String memberA_id,String memberB_id,Integer status,Integer tid) {

		CreditVO creditVO = new CreditVO();
		creditVO.setMemberA_id(memberA_id);		
		creditVO.setMemberB_id(memberB_id);		
		creditVO.setStatus(status);
		creditVO.setTid(tid);
		dao.insert(creditVO);

		return creditVO;
	}
	public CreditVO updateCredit(CreditVO creditVO) {
		
		dao.update(creditVO);

		return creditVO;
	}	
	public void deleteCredit(Integer cid) {
		dao.delete(cid);
	}
	public CreditVO getOneByCid(Integer cid) {
		return dao.findByPrimaryKey(cid);
	}
	public List<CreditVO> getAll() {
		return dao.getAll();
	}
}
