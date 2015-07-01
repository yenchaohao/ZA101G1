package com.report.model;

import java.util.List;

import com.tool.HibernateUtil;

import org.hibernate.*;

public class ReportHibernateDAO implements Report_interface {

	@Override
	public int insert(ReportVO reportVO) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(reportVO);
			session.getTransaction().commit();
			return reportVO.getRid();
		}catch(RuntimeException ex){
			session.beginTransaction().rollback();
		} 
		return 0;
	}
	

	@Override
	public int update(ReportVO reportVO) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(reportVO);
			session.getTransaction().commit();
			return 1;
		}catch(RuntimeException ex){
			session.beginTransaction().rollback();
		}
		return 0;
	}

	@Override
	public int delete(Integer rid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ReportVO findByPrimaryKey(Integer rid) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		ReportVO reportvo = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from ReportVO where rid = ? ");
			query.setParameter(0,rid);
			reportvo = (ReportVO)query.list().get(0);
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.beginTransaction().rollback();
		}
		return reportvo;
	}

	@Override
	public List<ReportVO> getAll() {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<ReportVO> list = null;
		try{
			session.beginTransaction();
			System.out.println("getAll()");
			Query query = session.createQuery("from ReportVO where rid = ? order by rid");
			list = query.list();
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.beginTransaction().rollback();
		}
		return list;
	}

	@Override
	public ReportVO findByGidAndMember_id(String member_id, int gid) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		ReportVO reportvo = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from ReportVO where gid = ? and member_id = ? ");
			query.setParameter(0,gid);
			query.setParameter(1,member_id);
			reportvo = (ReportVO)query.list().get(0);
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.beginTransaction().rollback();
		}
		return reportvo;
	}

	@Override
	public List<ReportVO> findReportByGid(int gid) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<ReportVO> list = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from ReportVO where gid = ?");
			query.setParameter(0,gid);
			list = query.list();	
			session.getTransaction().commit();
		}catch(RuntimeException ex){
			session.beginTransaction().rollback();
		}
		return list;
	}

}
