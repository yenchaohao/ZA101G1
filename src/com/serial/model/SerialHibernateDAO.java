package com.serial.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import com.tool.HibernateUtil;

public class SerialHibernateDAO  implements Serial_interface{
	
	
	public void insert(SerialVO serialVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(serialVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}
	public void update(SerialVO serialVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(serialVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}
	public void delete(String serial_number) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			SerialVO serialVO=(SerialVO) session.get(SerialVO.class,serial_number);
			session.delete(serialVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}
	
	public SerialVO findByPrimaryKey(String serial_number) {
		SerialVO serialVO = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			serialVO = (SerialVO) session.get(SerialVO.class, serial_number);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return serialVO;
	}
	
	public List<SerialVO> getAll() {
		List<SerialVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from SerialVO order by money");
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}
}
