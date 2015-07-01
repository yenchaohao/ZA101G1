package com.emp.model;

import java.util.List;
import java.util.Map;

import org.hibernate.*;

import com.tool.HibernateUtil;


public class EmpHibernateDAO implements EmpDAO_interface{
	
	private static final String GET_EMP_LOGIN = 
			"FROM EmpVO where empid = ? AND password = ? order by empid";
	private static final String GET_ALL_STMT =
			"FROM EmpVO order by empid";
	
	@Override
	public int insert(EmpVO empVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(empVO);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public String insertGetPrimaryKey(EmpVO empVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(empVO);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return empVO.getEmpid();
	}

	@Override
	public int update(EmpVO empVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(empVO);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public int delete(String empid) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			EmpVO empVO = (EmpVO) session.get(EmpVO.class, empid);
			session.delete(empVO);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return 1;
	}

	@Override
	public EmpVO findByPrimaryKey(String empid) {
		EmpVO empVO = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			empVO = (EmpVO) session.get(EmpVO.class, empid);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return empVO;
	}

	@Override
	public List<EmpVO> getAll() {
		List<EmpVO> list = null;
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
	public boolean loginEmp(String empid, String password) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		boolean result = false;
		List<EmpVO> list = null;
		try {
			session.beginTransaction();
			Query query = session.createQuery(GET_EMP_LOGIN);
			query.setParameter(0, empid);
			query.setParameter(1, password);
			list = query.list();
			if(list.get(0).getEmpid() != null){
				result = true;
			}
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		
		return result;
	}

	@Override
	public List<EmpVO> getAllComposite(Map<String, String[]> map) {
		List<EmpVO> list = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			String finalSQL = "from EmpVO "
			          + empUtil_CompositeQuery.get_WhereCondition(map)
			          + "order by empid";
			Query query = session.createQuery(finalSQL);
			list = query.list();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return list;
	}

}
