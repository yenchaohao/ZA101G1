package com.authority.model;

import java.util.List;


public class AuthorityService {
	
	private AuthorityDAO_interface dao;
	
	public AuthorityService() {
		dao = new AuthorityHibernateDAO();
	}
	
	public AuthorityVO addAuthority(String empid, Integer aid) {

		AuthorityVO authorityVO = new AuthorityVO();

		authorityVO.setEmpid(empid);
		authorityVO.setAid(aid);
		dao.insert(authorityVO);

		return authorityVO;
	}

	public AuthorityVO updateAuthority(String empid, Integer aid) {

		AuthorityVO authorityVO = new AuthorityVO();

		authorityVO.setEmpid(empid);
		authorityVO.setAid(aid);
		dao.update(authorityVO);

		return authorityVO;
	}

	public void deleteAuthority(String empid, Integer aid) {
		dao.delete(empid,aid);
	}

	public AuthorityVO getOneAuthority(String empid) {
		return dao.findByPrimaryKey(empid);
	}

	public List<AuthorityVO> getAll() {
		return dao.getAll();
	} 
	
	public List<AuthorityVO> getOneAid(String empid) {
		return dao.getOneAid(empid);
	}
}
