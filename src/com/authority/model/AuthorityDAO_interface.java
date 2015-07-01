package com.authority.model;

import java.util.List;


public interface AuthorityDAO_interface {
	public int insert(AuthorityVO authorityVO);
    public int update(AuthorityVO authorityVO);
    public int delete(String empid, Integer aid);
    public AuthorityVO findByPrimaryKey(String empid);
    public List<AuthorityVO> getAll();
    public List<AuthorityVO> getOneAid(String empid);
}
