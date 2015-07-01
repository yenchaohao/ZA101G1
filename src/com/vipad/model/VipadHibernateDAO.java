package com.vipad.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.emp.model.EmpVO;
import com.favorite.model.FavoriteVO;
import com.tool.HibernateUtil;

public class VipadHibernateDAO implements VipadDAO_interface{
	
	private static final String GET_ALL_STMT =
			"FROM VipadVO order by vid";
	private static final String GET_ONE_VIPAD =
			"FROM VipadVO where gid = ?";
	private static final String GET_VIPAD_BY_MEMBER =
			"FROM VipadVO where status != 2 and member_id = ?";
	private static final String GET_VIPAD_BY_MEMBER_ALIVE =
			"FROM VipadVO where status = 1 and member_id = ?";
	private static final String GET_ALL_DELETE = 
			"FROM VipadVO where status = 2 order by vid";
	private static final String GET_ALL_STMT_ALIVE = 
			"FROM VipadVO where status = 1 order by vid";

	@Override
	public int insert(VipadVO vipadVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(vipadVO);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public int update(VipadVO vipadVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(vipadVO);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public int delete(VipadVO vipadVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.delete(vipadVO);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public VipadVO findByPrimaryKey(Integer vid) {
		VipadVO vipadVO = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			vipadVO = (VipadVO) session.get(VipadVO.class, vid);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return vipadVO;
	}

	@Override
	public List<VipadVO> getAll() {
		List<VipadVO> list = null;
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
	public List<VipadVO> getOneVipad(Integer gid) {
		List<VipadVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(GET_ONE_VIPAD);
			query.setParameter(0, gid);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return list;
	}

	@Override
	public List<VipadVO> getVipadByMember(String member_id) {
		List<VipadVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(GET_VIPAD_BY_MEMBER);
			query.setParameter(0, member_id);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return list;
	}

	@Override
	public List<VipadVO> getVipadByMemberAlive(String member_id) {
		List<VipadVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(GET_VIPAD_BY_MEMBER_ALIVE);
			query.setParameter(0, member_id);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return list;
	}

	@Override
	public List<VipadVO> getAllDelete() {
		List<VipadVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(GET_ALL_DELETE);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return list;
	}

	@Override
	public List<VipadVO> getAllAlive() {
		List<VipadVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(GET_ALL_STMT_ALIVE);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return list;
	}

	@Override
	public List<VipadHibernateVO> getAllAliveAssociations() {
		List<VipadHibernateVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("FROM VipadHibernateVO where status = 1 order by vid");
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return list;
	}

	@Override
	public List<VipadHibernateVO> getVipadByMemberAllAlive(String member_id) {
		List<VipadHibernateVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from VipadHibernateVO where (status = 1 or status = 0) and member_id = ? order by vid");
			query.setParameter(0, member_id);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return list;
	}

}
