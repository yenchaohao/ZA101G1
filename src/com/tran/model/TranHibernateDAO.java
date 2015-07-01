package com.tran.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.tool.HibernateUtil;

public class TranHibernateDAO implements Tran_interface{

	@Override
	public void insert(TranVO tranVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(tranVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(TranVO tranVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(tranVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void deleteByTid(Integer tid) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			TranVO tranVO=(TranVO) session.get(TranVO.class,tid);
			session.delete(tranVO);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public TranVO findByTid(Integer tid) {
		TranVO tranVO = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			tranVO = (TranVO) session.get(TranVO.class, tid);
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return tranVO;
	}

	@Override
	public List<TranVO> getAll() {
		List<TranVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from TranVO order by tid");
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	
	}

	@Override
	public List<TranVO> getUnreply() {
		List<TranVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from TranVO WHERE STATUS=0 order by TID");
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}

	@Override
	public List<TranVO> findByReqMember_id(String member_id) {
		List<TranVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from TranVO WHERE REQ_MEMBER_ID=? ORDER BY REQ_DATE");
			query.setParameter(0,member_id);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}

	@Override
	public List<TranVO> findByResMember_id(String member_id) {
		List<TranVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from TranVO WHERE RES_MEMBER_ID=? ORDER BY RES_DATE");
			query.setParameter(0,member_id);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}

	@Override
	public List<TranVO> findByReqMember_idUn(String member_id) {
		List<TranVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from TranVO WHERE REQ_MEMBER_ID=? AND STATUS = 0 ORDER BY REQ_DATE");
			query.setParameter(0,member_id);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}

	@Override
	public List<TranVO> findByResMember_idUn(String member_id) {
		List<TranVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from TranVO  WHERE RES_MEMBER_ID=? AND STATUS = 0 ORDER BY RES_DATE");
			query.setParameter(0,member_id);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}

	@Override
	public List<TranVO> getAllByMember_idFinal(String member_id) {
		List<TranVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from TranVO  WHERE (REQ_MEMBER_ID=? OR RES_MEMBER_ID=?) AND STATUS = 1 ORDER BY RES_DATE");
			query.setParameter(0,member_id);
			query.setParameter(1,member_id);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}
	
	@Override
	public List<TranVO> getAllByMember_idFail(String member_id) {
		List<TranVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from TranVO  WHERE (REQ_MEMBER_ID=? OR RES_MEMBER_ID=?) AND STATUS = 2 ORDER BY RES_DATE");
			query.setParameter(0,member_id);
			query.setParameter(1,member_id);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}
	
	@Override
	public List<TranVO> getAllByMember_idWait(String member_id) {
		List<TranVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from TranVO  WHERE (REQ_MEMBER_ID=? OR RES_MEMBER_ID=?) AND STATUS = 3 ORDER BY RES_DATE");
			query.setParameter(0,member_id);
			query.setParameter(1,member_id);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}
	
	@Override
	public List<TranVO> getAllByMemberNoFail(String member_id) {
		List<TranVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from TranVO  WHERE (REQ_MEMBER_ID=? OR RES_MEMBER_ID=?) AND (STATUS = 1 OR STATUS = 0) ORDER BY RES_DATE");
			query.setParameter(0,member_id);
			query.setParameter(1,member_id);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}

	@Override
	public List<TranVO> getAllAlive() {
		List<TranVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from TranVO WHERE STATUS=1 order by TID");
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException ex) {
			session.getTransaction().rollback();
			throw ex;
		}
		return list;
	}

}
