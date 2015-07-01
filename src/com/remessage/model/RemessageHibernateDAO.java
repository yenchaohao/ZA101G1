package com.remessage.model;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import com.tool.HibernateUtil;

public class RemessageHibernateDAO implements Remessage_interface {

	@Override
	public void insert(RemessageVO remessageVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(remessageVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(RemessageVO remessageVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(remessageVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void delete(Integer rid) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			RemessageVO remessageVO=(RemessageVO) session.get(RemessageVO.class,rid);
			session.delete(remessageVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public RemessageVO findByPrimaryKey(Integer rid) {
		RemessageVO remessageVO = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			remessageVO = (RemessageVO) session.get(RemessageVO.class, rid);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return remessageVO;
	}

	@Override
	public List<RemessageVO> getAll() {
		List<RemessageVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from RemessageVO order by rid");
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}
	

	@Override
	public List<RemessageVO> getByMid(Integer mid) {
		List<RemessageVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from RemessageVO where mid=? order by r_date");
			query.setParameter(0,mid);			
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}

}
