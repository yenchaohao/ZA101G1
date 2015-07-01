package com.authority_list.model;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.tool.HibernateUtil;

public class Authority_listHibernateDAO implements Authority_listDAO_interface{
	
	private static final String GET_ALL_STMT =
			"FROM Authority_listVO order by aid";

	@Override
	public int insert(Authority_listVO authority_listVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(authority_listVO);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public int update(Authority_listVO authority_listVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(authority_listVO);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public int delete(Integer aid) {
		Authority_listVO authority_listVO = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			authority_listVO = (Authority_listVO) session.get(Authority_listVO.class, aid);
			session.delete(authority_listVO);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public Authority_listVO findByPrimaryKey(Integer aid) {
		Authority_listVO authority_listVO = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			authority_listVO = (Authority_listVO) session.get(Authority_listVO.class, aid);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return authority_listVO;
	}

	@Override
	public List<Authority_listVO> getAll() {
		List<Authority_listVO> list = null;
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

}
