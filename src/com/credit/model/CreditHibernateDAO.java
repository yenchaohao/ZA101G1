package com.credit.model;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import com.tool.HibernateUtil;

public class CreditHibernateDAO implements Credit_interface{

	@Override
	public void insert(CreditVO creditVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
		session.beginTransaction();
		session.saveOrUpdate(creditVO);
		session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(CreditVO creditVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
		session.beginTransaction();
		session.saveOrUpdate(creditVO);
		session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void delete(Integer cid) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			CreditVO creditVO=(CreditVO) session.get(CreditVO.class,cid);
			session.delete(creditVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public CreditVO findByPrimaryKey(Integer cid) {
		CreditVO creditVO = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			creditVO = (CreditVO) session.get(CreditVO.class, cid);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return creditVO;
	}

	@Override
	public List<CreditVO> getAll() {
		List<CreditVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from CreditVO order by cid");
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}

}
