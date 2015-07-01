package com.group.model;

import java.util.List;

public interface Group_interface {
	public int insert(GroupVO groupVO);
	public int update(GroupVO groupVO);
	public int deleteByGroupid(Integer groupid);
	public GroupVO findByGroupid(Integer groupid);
	public List<GroupVO> getAll();
}
