package com.post.model;

import java.util.List;

import com.mail.model.MailVO;

public class PostService {
		
	private Post_interface dao;
	private PostHibernateDAO hidao;
	
	public PostService(){
		dao = new PostDAO();
		hidao = new PostHibernateDAO();
	}
	
	public PostVO addPost(String empid, String title, String post){
		
		PostVO postvo = new PostVO();
		
		postvo.setEmpid(empid);
		postvo.setTitle(title);
		postvo.setPost(post);
		hidao.insert(postvo);
		return postvo;
		
	}
	
	public int addPostByObject(PostVO postvo){
		int  i = hidao.insert(postvo);
		return i;
	}
	
	public PostVO updatePost(String empid, String title, String post, Integer pid){
		
		PostVO postvo = new PostVO();
		postvo.setEmpid(empid);
		postvo.setTitle(title);
		postvo.setPost(post);
		postvo.setPid(pid);
		
		hidao.update(postvo);
		
		return postvo;
		
	}
	
	public int updatePostByObject(PostVO postvo){
		int  i = hidao.update(postvo);
		return i;
	}
	
	public void deletePostByPid(Integer pid){
		hidao.deleteByPid(pid);
	}
 	
	public PostVO getOnePost(Integer pid){
		return hidao.findByPid(pid);
	}
	
	public List<PostVO> getAll(){
		return hidao.getAll();
	}
	
	public List<PostVO> getAllLatest(){
		return hidao.getAllLatest();
	}
	
	public List<PostVO> findPostByEmpid(String empid){
		return hidao.findByEmpid(empid);
		
	}
	
	
}
