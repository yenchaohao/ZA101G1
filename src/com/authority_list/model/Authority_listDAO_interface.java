package com.authority_list.model;

import java.util.List;


public interface Authority_listDAO_interface {
	public int insert(Authority_listVO authority_listVO);
    public int update(Authority_listVO authority_listVO);
    public int delete(Integer aid);
    public Authority_listVO findByPrimaryKey(Integer aid);
    public List<Authority_listVO> getAll();
}
