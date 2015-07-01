package com.member.model;

import org.hibernate.*;

import com.tool.HibernateUtil;

import java.util.List;
import java.util.Map;

public class MemberHibernateDAO implements Member_interface {

	@Override
	public int insert(MemberVO membervo) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			session.save(membervo);
			session.getTransaction().commit();
			return 1 ;
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return 0;
	}

	@Override
	public String insertGetPrimaryKey(MemberVO membervo) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			//System.out.println("hibernate insert");
			session.beginTransaction();
			session.save(membervo);
			session.getTransaction().commit();
			return membervo.getMember_id() ;
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return null;
	}

	@Override
	public int update(MemberVO membervo) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(membervo);
			session.getTransaction().commit();
			return 1;
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return 0;
	}

	@Override
	public int deleteByMember_id(String member_id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MemberVO findByMember_id(String memebr_id) {
		// TODO Auto-generated method stub
		MemberVO  membervo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			membervo = (MemberVO) session.get(MemberVO.class, memebr_id);
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return membervo;
	}

	@Override
	public MemberVO findByEmail(String email) {
		// SELECT * FROM EX_member WHERE email = ?
		MemberVO membervo = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from MemberVO where email= ?");
			query.setParameter(0, email);
			membervo = (MemberVO)query.list().get(0);
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return membervo;
	}

	@Override
	public List<MemberVO> getAll() {
		// SELECT * from ex_member order by member_id
		List<MemberVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from MemberVO order by joindate DESC");
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return list;
	}

	@Override
	public List<MemberVO> getAllAlive() {
		// SELECT * from ex_member where mem_status != 2 order by member_id
		List<MemberVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from MemberVO where mem_status = 20 or mem_status = 30  order by credit desc");
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return list;
	}

	@Override
	public boolean findByEmailAndPassword(String email, String pwd) {
		// SELECT * FROM EX_member WHERE email = ? and password = ?
		boolean isFind = false;
		List<MemberVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from MemberVO WHERE email = ? and password = ?  order by member_id");
			query.setParameter(0,email);
			query.setParameter(1, pwd);
			list = query.list();
			if(list.get(0).getMember_id() != null && list.get(0).getMember_id().length() > 0)
				isFind = true;
			
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return isFind;
	}

	@Override
	public List<MemberVO> getMemberByMemberName(String mem_name) {
		// TODO Auto-generated method stub
		List<MemberVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from MemberVO where upper(mem_name) like upper( ? ) ");
			query.setParameter(0, "%"+mem_name+"%" );
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return list;
	}

	@Override
	public List<MemberVO> getAllWish() {
		// TODO Auto-generated method stub
		List<MemberVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from MemberVO where ( mem_status = 20 or mem_status = 30 ) and  my_wish is not null order by member_id");
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return list;
	}

	@Override
	public List<MemberVO> getSerachWish(Map<String, String[]> map) {
		// TODO Auto-generated method stub
		List<MemberVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			//System.out.println(GetWishCondition.getWishCondition(map)); 
			if(GetWishCondition.getWishCondition(map) == null || GetWishCondition.getWishCondition(map).length() == 0)
				return list;
			Query query = session.createQuery("from MemberVO "+GetWishCondition.getWishCondition(map)+" order by member_id");
			//System.out.println("query:"+query.getQueryString());  
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			System.out.println(ex.getMessage());
			session.getTransaction().rollback();
		}
		return list;
	}

	@Override
	public int insertForFacebook(MemberVO membervo) {
		// TODO Auto-generated method stub
		return 0;
	}


	
	
}
