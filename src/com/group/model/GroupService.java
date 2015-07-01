package com.group.model;

import java.util.List;

public class GroupService {
	
	private Group_interface dao;
	
	public GroupService(){
		dao = new GroupDAO();
	}
	
	public GroupVO addGroup(Integer groupid, String group_name){
		
		GroupVO groupVO = new GroupVO();
		
		groupVO.setGroupid(groupid);
		groupVO.setGroup_name(group_name);
		
		dao.insert(groupVO);
		
		return groupVO;
	}
	
	public GroupVO updateGroup(Integer groupid, String group_name){
		
		GroupVO groupVO = new GroupVO();
		
		groupVO.setGroupid(groupid);
		groupVO.setGroup_name(group_name);
		
		dao.update(groupVO);
		
		return groupVO;
	}
	
	public void deleteGroupByGroupid(Integer groupid){
		dao.deleteByGroupid(groupid);
	}
	
	public void findGroupByGroupid(Integer groupid){
		dao.findByGroupid(groupid);
	}
	
	public List<GroupVO> getAll(){
		return dao.getAll();
		
	}

}
