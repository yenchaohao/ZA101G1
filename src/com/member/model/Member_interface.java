package com.member.model;

import java.util.List;
import java.util.Map;



public interface Member_interface {
	public int insert(MemberVO membervo);
	public int insertForFacebook(MemberVO membervo); //only work on JNDI DAO
	public String insertGetPrimaryKey(MemberVO membervo);
	public int update(MemberVO membervo);
	public int deleteByMember_id(String member_id);
	public MemberVO findByMember_id(String memebr_id);
	public MemberVO findByEmail(String email);
	public List<MemberVO> getAll();
	public List<MemberVO> getAllAlive();
	public List<MemberVO> getAllWish();
	public List<MemberVO> getSerachWish(Map<String,String[]> map);
	public List<MemberVO> getMemberByMemberName(String mem_name);
	public boolean findByEmailAndPassword(String email,String pwd);

}
