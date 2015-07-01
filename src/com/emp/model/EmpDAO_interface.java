package com.emp.model;

import java.util.List;
import java.util.Map;


public interface EmpDAO_interface {
	public int insert(EmpVO empVO);
	public String insertGetPrimaryKey(EmpVO empVO);
    public int update(EmpVO empVO);
    public int delete(String empid);
    public EmpVO findByPrimaryKey(String empid);
    public List<EmpVO> getAll();
    public boolean loginEmp(String empid, String password);
    //½Æ¦X¬d¸ß
    public List<EmpVO> getAllComposite(Map<String, String[]> map);
    
}
