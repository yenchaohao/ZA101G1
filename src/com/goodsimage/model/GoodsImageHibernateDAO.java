package com.goodsimage.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.tool.HibernateUtil;

public class GoodsImageHibernateDAO implements GoodsImage_interface {

	@Override
	public int insert(GoodsImageVO goodsImageVO) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(goodsImageVO);
			session.getTransaction().commit();
			return 1;
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return 0;
	}

	@Override
	public int update(GoodsImageVO goodsImageVO) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(goodsImageVO);
			session.getTransaction().commit();
			return 1;
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return 0;
	}

	@Override
	public int deleteByPic_number(Integer pic_number) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<GoodsImageVO> findByGid(Integer gid) {
		// SELECT * FROM ex_goodsimage where gid = ?
		List<GoodsImageVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from GoodsImageVO where gid = ?");
			query.setParameter(0, gid);
			list = query.list();		
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return list;
	}

	@Override
	public GoodsImageVO findByPicNumber(Integer pic_number) {
		// TODO Auto-generated method stub
		GoodsImageVO goodsImageVO = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			Query query = session.createQuery("from GoodsImageVO where pic_number = ?");
			query.setParameter(0, pic_number);
			goodsImageVO = (GoodsImageVO)query.list().get(0);		
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.getTransaction().rollback();
		}
		return goodsImageVO;
	}

}
