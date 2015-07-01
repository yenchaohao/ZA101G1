package com.point_tran.model;

import java.sql.Timestamp;
import java.util.List;


public class Point_tran_Service {
	private Point_tran_interface dao;

	public Point_tran_Service() {
		dao = new Point_tranDAO();
	}
	public Point_tranVO addPoint_tran(Integer status,String member_id) {

		Point_tranVO desireVO = new Point_tranVO();
		desireVO.setStatus(status);
		desireVO.setMember_id(member_id);		
		dao.insert(desireVO);

		return desireVO;
	}
	public Point_tranVO updatePoint_tran(Integer ptid,Integer status,String member_id,Timestamp quitDate) {

		Point_tranVO desireVO = new Point_tranVO();

		desireVO.setPtid(ptid);
		desireVO.setStatus(status);
		desireVO.setMember_id(member_id);
		desireVO.setQuitDate(quitDate);
		
		dao.update(desireVO);

		return desireVO;
	}	
	public void deletePoint_tran(Integer ptid) {
		dao.delete(ptid);
	}
	public Point_tranVO getOneEmp(Integer ptid) {
		return dao.findByPrimaryKey(ptid);
	}
	public List<Point_tranVO> getAll() {
		return dao.getAll();
	}
}
