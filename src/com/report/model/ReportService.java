package com.report.model;

import java.util.List;

public class ReportService {
	
	private Report_interface dao;
	private Report_interface hibernatedao;
	
	public ReportService(){ 
		dao = new ReportDAO();
		hibernatedao = new ReportHibernateDAO();
	}
	
	public int addReport(Integer gid, String member_id){
		
		ReportVO reportVO = new ReportVO();
		
		reportVO.setGid(gid);
		reportVO.setMember_id(member_id);
		int i = hibernatedao.insert(reportVO);
		return i;	
	}
	
	public ReportVO updateReport(Integer rid, Integer gid, String member_id){
		
		ReportVO reportVO = new ReportVO();
		
		reportVO.setRid(rid);
		reportVO.setGid(gid);
		reportVO.setMember_id(member_id);
		
		hibernatedao.update(reportVO);
		
		return reportVO;
	}
	
	public void deleteReport(Integer rid){
		dao.delete(rid);
	}
	
	public ReportVO findReportByPrimaryKey(Integer rid){
		return hibernatedao.findByPrimaryKey(rid);
	}
	
	public List<ReportVO> getAll(){
		return hibernatedao.getAll();
	}
	 
	public ReportVO findByGidAndMember_id(String member_id ,int gid){
		return hibernatedao.findByGidAndMember_id(member_id, gid);
	}
	
	public  List<ReportVO> findReportByGid(int gid){
		return hibernatedao.findReportByGid(gid);
	}

}
