package com.member.model;

import java.sql.Date;
import java.util.List;
import java.util.Map;



public class MemberService {
	
	private Member_interface member;
	private Member_interface hibernateMember;
	
	
	
	public MemberService(){
		member = new MemberDAO();
		hibernateMember = new MemberHibernateDAO();
	}
	
	public int addMemberForFaceBook(MemberVO membervo){
		return member.insertForFacebook(membervo);
	}

	
	public MemberVO addMember(String email,String mem_name,String password,String id_no,String tel,String address,Date birthday,byte[] pic,String my_wish){
		MemberVO membervo = new MemberVO();
		membervo.setEmail(email);
		membervo.setMem_name(mem_name);
		membervo.setPassword(password);
		membervo.setId_no(id_no);
		membervo.setTel(tel);
		membervo.setAddress(address);
		membervo.setBirthday(birthday);
		membervo.setPic(pic);
		
		membervo.setMy_wish(my_wish);
		
		hibernateMember.insert(membervo);
		
		return membervo; 
	}
	public String addMemberGetPrimaryKey(String email,String mem_name,String password,String id_no,String tel,String address,Date birthday,byte[] pic,String my_wish){
		MemberVO membervo = new MemberVO();
		membervo.setEmail(email);
		membervo.setMem_name(mem_name);
		membervo.setPassword(password);
		membervo.setId_no(id_no);
		membervo.setTel(tel);
		membervo.setAddress(address);
		membervo.setBirthday(birthday);
		membervo.setPic(pic);
		 
		membervo.setMy_wish(my_wish);
		
		String pk = hibernateMember.insertGetPrimaryKey(membervo);
		
		return pk;
	}
	
	public MemberVO updateMember(String email,String mem_name,String password,String id_no,String tel,String address,Date birthday,byte[] pic,String my_wish,String member_id){
		
		MemberVO membervo = new MemberVO();
		membervo.setEmail(email);
		membervo.setMem_name(mem_name);
		membervo.setPassword(password);
		membervo.setId_no(id_no);
		membervo.setTel(tel);
		membervo.setAddress(address);
		membervo.setBirthday(birthday);
		membervo.setPic(pic);
		
		membervo.setMy_wish(my_wish);
		membervo.setMember_id(member_id);
		hibernateMember.update(membervo);
		
		return membervo;
	}
	
	public Integer updateMemberByObject(MemberVO membervo){
		int i = hibernateMember.update(membervo);
		return i;
	}
	
	public void deleteMember(String member_id){
		member.deleteByMember_id(member_id);
	}
 	
	public MemberVO getOneMemberByMemberId(String member_id){
		return hibernateMember.findByMember_id(member_id);
	}
	
	public MemberVO getOneMemberByEmail(String email){
		return hibernateMember.findByEmail(email);
	}
	public List<MemberVO> getAll(){
		return hibernateMember.getAll();
	}
	public List<MemberVO> getAllAlive(){
		return hibernateMember.getAllAlive();
	}
	
	public boolean MemberVerify(String email,String pwd){
		return hibernateMember.findByEmailAndPassword(email, pwd);
	}
	public List<MemberVO> getMemberByMemberName(String mem_name){
		return hibernateMember.getMemberByMemberName(mem_name);
	}
	
	
	public List<MemberVO> getAllWish(){
		return hibernateMember.getAllWish();
	}
	
	public List<MemberVO> getSearchWish(Map<String,String[]> map){
		return hibernateMember.getSerachWish(map);
	}

	
}
