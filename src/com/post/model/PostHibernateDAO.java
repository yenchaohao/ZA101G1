package com.post.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.tool.HibernateUtil;

public class PostHibernateDAO implements Post_interface {
	
	private static final String GET_ALL_STMT = 
			"FROM PostVO order by pid";
	private static final String GET_ALL_STMT_LATEST = 
			"FROM PostVO order by postdate desc";
	private static final String GET_ALL_BY_EMPID = 
			"FROM PostVO WHERE empid = ? order by pid";
	
	
	@Override
	public int insert(PostVO postvo) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(postvo);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public int update(PostVO postvo) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(postvo);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public int deleteByPid(Integer pid) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			PostVO postVO = (PostVO) session.get(PostVO.class, pid);
			session.delete(postVO);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}

		return 1;
	}

	@Override
	public PostVO findByPid(Integer pid) {
		// TODO Auto-generated method stub
		PostVO postVO = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			postVO = (PostVO) session.get(PostVO.class, pid);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}

		return postVO;
	}

	@Override
	public List<PostVO> getAll() {
		// TODO Auto-generated method stub
		List<PostVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(GET_ALL_STMT);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}

		return list;
	}

	@Override
	public List<PostVO> getAllLatest() {
		// TODO Auto-generated method stub
		List<PostVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(GET_ALL_STMT_LATEST);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}

		return list;
	}
	
	
	@Override
	public List<PostVO> findByEmpid(String empid) {
		// TODO Auto-generated method stub
		List<PostVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(GET_ALL_BY_EMPID);
			query.setParameter(0, empid);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}

		return list;
	}

	
	
}
