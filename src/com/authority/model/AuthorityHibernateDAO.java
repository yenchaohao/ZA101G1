package com.authority.model;


import java.util.List;

import org.hibernate.*;

import com.tool.HibernateUtil;

public class AuthorityHibernateDAO implements AuthorityDAO_interface{
	
	private static final String GET_ONE_AID =
			"FROM AuthorityVO where empid = ? order by empid";
	private static final String DELETE = 
			"FROM AuthorityVO where empid = ? AND aid = ?";
	private static final String GET_ALL_STMT =
			"FROM AuthorityVO order by empid";

	@Override
	public int insert(AuthorityVO authorityVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(authorityVO);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public int update(AuthorityVO authorityVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(authorityVO);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public int delete(String empid, Integer aid) {
		List<AuthorityVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(DELETE);
			query.setParameter(0, empid);
			query.setParameter(1, aid);
			list = query.list();
			for(AuthorityVO authorityVO : list){
				session.delete(authorityVO);
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public AuthorityVO findByPrimaryKey(String empid) {
		AuthorityVO authorityVO = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			authorityVO = (AuthorityVO) session.get(AuthorityVO.class, empid);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return authorityVO;
	}

	@Override
	public List<AuthorityVO> getAll() {
		List<AuthorityVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(GET_ALL_STMT);
			list = query.list();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return list;
	}

	@Override
	public List<AuthorityVO> getOneAid(String empid) {
		List<AuthorityVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(GET_ONE_AID);
			query.setParameter(0, empid);
			list = query.list();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return list;
	}

}
