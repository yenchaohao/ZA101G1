package com.emp.model;

import java.sql.Date;
import java.util.List;
import java.util.Map;


public class EmpService {
	
	private EmpDAO_interface dao;
	
	public EmpService() {
		dao = new EmpHibernateDAO();
	}
	
	public EmpVO addEmp(String ename, String password, Date hiredate, byte[] pic, String email, Integer status) {

		EmpVO empVO = new EmpVO();

		empVO.setEname(ename);
		empVO.setPassword(password);
		empVO.setHiredate(hiredate);
		empVO.setPic(pic);
		empVO.setEmail(email);
		empVO.setStatus(status);
		dao.insert(empVO);

		return empVO;
	}
	
	public String addEmpGetPrimaryKey(String ename, String password, Date hiredate, byte[] pic, String email, Integer status) {

		EmpVO empVO = new EmpVO();

		empVO.setEname(ename);
		empVO.setPassword(password);
		empVO.setHiredate(hiredate);
		empVO.setPic(pic);
		empVO.setEmail(email);
		empVO.setStatus(status);
		String pk = dao.insertGetPrimaryKey(empVO);

		return pk;
	}

	public EmpVO updateEmp(String empid, String ename, String password, Date hiredate, byte[] pic, String email, Integer status) {

		EmpVO empVO = new EmpVO();
		
		empVO.setEmpid(empid);
		empVO.setEname(ename);
		empVO.setPassword(password);
		empVO.setHiredate(hiredate);
		empVO.setPic(pic);
		empVO.setEmail(email);
		empVO.setStatus(status);
		dao.update(empVO);

		return empVO;
	}
	
	public Integer updateOneEmp(EmpVO empVO){
		int i = dao.update(empVO);
		return i;
	}
	
	public boolean loginEmp(String empid, String password){
		return dao.loginEmp(empid, password);
	}
	
	public void deleteEmp(String empid) {
		dao.delete(empid);
	}

	public EmpVO getOneEmp(String empid) {
		return dao.findByPrimaryKey(empid);
	}

	public List<EmpVO> getAll() {
		return dao.getAll();
	} 
	
	public List<EmpVO>  getAllComposite(Map<String, String[]> map){
		return dao.getAllComposite(map);
	}
}
