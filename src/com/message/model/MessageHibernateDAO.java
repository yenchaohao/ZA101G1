package com.message.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.remessage.model.RemessageVO;
import com.tool.HibernateUtil;

public class MessageHibernateDAO implements Message_interface {
	public void insert(MessageVO messageVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(messageVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}
	public void update(MessageVO messageVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(messageVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}
	@Override
	public void delete(Integer mid) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
//			RemessageVO remessageVO=new RemessageVO();
//			remessageVO.setMid(mid);
//			session.delete(remessageVO);
			Query query=session.createQuery("delete from RemessageVO where mid ="+mid+"");
			int updateCount=query.executeUpdate();
			MessageVO messageVO=(MessageVO) session.get(MessageVO.class,mid);
			session.delete(messageVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}
	@Override
	public MessageVO findByPrimaryKey(Integer mid) {
		MessageVO messageVO = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			messageVO = (MessageVO) session.get(MessageVO.class, mid);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return messageVO;
	}
	
	public List<MessageVO> getAll() {
		List<MessageVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from MessageVO order by mid");
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}
	@Override
	public int insertGetPK(MessageVO messageVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(messageVO);
			session.getTransaction().commit();
			return messageVO.getMid();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		
	}
	
	
	@Override
	public List<MessageVO> getAllLatest() {
		List<MessageVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from MessageVO  order by m_date DESC");
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}
	@Override
	public List<MessageVO> search(String title) {
		List<MessageVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from MessageVO  where title like '%"+
			title+"%' order by m_date DESC");
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}
}
