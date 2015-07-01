package com.favorite.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.emp.model.EmpVO;
import com.tool.HibernateUtil;

public class FavoriteHibernateDAO implements Favorite_interface{
	
	private static final String GET_ALL_STMT =
			"FROM FavoriteVO order by fid";
	private static final String GET_ALL_BY_MEMBER = 
			"FROM FavoriteVO where member_id = ? order by fid";

	@Override
	public int insert(FavoriteVO favoriteVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(favoriteVO);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public int update(FavoriteVO favoriteVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(favoriteVO);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public int delete(Integer fid) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			FavoriteVO favoriteVO = (FavoriteVO) session.get(FavoriteVO.class, fid);
			session.delete(favoriteVO);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public FavoriteVO findByPrimaryKey(Integer fid) {
		FavoriteVO favoriteVO = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			favoriteVO = (FavoriteVO) session.get(FavoriteVO.class, fid);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return favoriteVO;
	}

	@Override
	public List<FavoriteVO> getAll() {
		List<FavoriteVO> list = null;
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
	public List<FavoriteVO> getAllByMember(String member_id) {
		List<FavoriteVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(GET_ALL_BY_MEMBER);
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
