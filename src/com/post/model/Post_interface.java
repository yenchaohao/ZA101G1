package com.post.model;

import java.util.List;

import com.send.model.SendVO;

public interface Post_interface {
	public int insert(PostVO postvo);
	public int update(PostVO postvo);
	public int deleteByPid(Integer pid);
	public PostVO findByPid(Integer pid);
	public List<PostVO> findByEmpid(String empid);
	public List<PostVO> getAll();
	public List<PostVO> getAllLatest();
	
}
