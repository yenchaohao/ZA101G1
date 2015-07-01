package com.send.model;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.goods.model.GoodsUtil_CompositeQuery;
import com.tool.HibernateUtil;

public class SendHibernateDAO implements Send_interface {

	@Override
	public int insert(SendVO sendvo) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(sendvo);
			session.getTransaction().commit();
			return 1;
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return 0;
	}

	@Override
	public int update(SendVO sendvo) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(sendvo);
			session.getTransaction().commit();
			return 1;
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return 0;
	}

	@Override
	public int deleteBySid(Integer sid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SendVO findBySid(Integer sid) {
		// SELECT *  FROM EX_SEND WHERE SID = ?
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		SendVO sendvo = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from SendVO where sid = ?");
			query.setParameter(0, sid);
			sendvo = (SendVO)query.list().get(0);
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		
		return sendvo;
	}

	@Override
	public List<SendVO> findByTid(Integer tid) {
		// SELECT *  FROM EX_SEND WHERE TID = ?
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<SendVO> list = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from SendVO where tid = ?");
			query.setParameter(0, tid);
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		
		return list;
	}

	@Override
	public List<SendVO> findByTidUn(Integer tid) {
		// SELECT *  FROM EX_SEND WHERE status = 1 AND TID = ?
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<SendVO> list = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from SendVO WHERE status = 1 AND TID = ?");
			query.setParameter(0, tid);
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		
		return list;
	}

	@Override
	public List<SendVO> findByTidFinal(Integer tid) {
		// SELECT *  FROM EX_SEND WHERE status = 2 AND TID = ?
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<SendVO> list = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from SendVO WHERE status = 2 AND TID = ?");
			query.setParameter(0, tid);
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		
		return list;
	}

	@Override
	public List<SendVO> getAll() {
		// SELECT * EX_SEND order by SID
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<SendVO> list = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from SendVO order by SID");
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		
		return list;
	}

	@Override
	public List<SendVO> getAllAlive() {
		// SELECT * FROM EX_SEND where status != 3 order by SID
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<SendVO> list = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from SendVO where status != 3 order by SID desc ");
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		
		return list;
	}

	@Override
	public List<SendVO> getAllByStatus(Integer status) {
		// SELECT *FROM EX_SEND where status = ? order by SID
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<SendVO> list = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from SendVO where status = ? order by SID");
			query.setParameter(0, status);
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		
		return list;
	}

	@Override
	public List<SendVOAssociations> getAllByStatusAssociations(Integer status) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<SendVOAssociations> list = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from SendVOAssociations where status = ? order by SID");
			query.setParameter(0, status);
			//System.out.println(query.getQueryString());
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		
		return list;
	}

	@Override
	public List<SendVOAssociations> getAllByAddressKeyAssociations(
			String address) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<SendVOAssociations> list = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from SendVOAssociations where membervo.address like ? order by SID");
			query.setParameter(0, "%"+address+"%");
			//System.out.println(query.getQueryString()); 
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			System.out.println(ex.getMessage());
			session.getTransaction().rollback();
		}
		
		return list;
	}

	@Override
	public List<SendVOAssociations> CompositeQuery(Map<String, String[]> map) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<SendVOAssociations> list = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from SendVOAssociations "+SendUtil_CompositeQuery.getWhereCondition(map)+" and status != 3  order by SID");
			//System.out.println(query.getQueryString()); 
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			System.out.println(ex.getMessage());
			session.getTransaction().rollback();
		}
		
		return list;
	}

}
