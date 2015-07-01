package com.authority_list.model;

import java.util.List;


public class Authority_listService {
	
	private Authority_listDAO_interface dao;
	
	public Authority_listService() {
		dao = new Authority_listHibernateDAO();
	}
	
	public  Authority_listVO addAuthority_list(Integer aid, String fname) {

		Authority_listVO authority_listVO = new Authority_listVO();

		authority_listVO.setAid(aid);
		authority_listVO.setFname(fname);
		dao.insert(authority_listVO);

		return authority_listVO;
	}

	public Authority_listVO updateAuthority_list(Integer aid, String fname) {

		Authority_listVO authority_listVO = new Authority_listVO();

		authority_listVO.setAid(aid);
		authority_listVO.setFname(fname);
		dao.update(authority_listVO);

		return authority_listVO;
	}

	public void deleteAuthority_list(Integer aid) {
		dao.delete(aid);
	}

	public Authority_listVO getOneAuthority_list(Integer aid) {
		return dao.findByPrimaryKey(aid);
	}

	public List<Authority_listVO> getAll() {
		return dao.getAll();
	} 
}
