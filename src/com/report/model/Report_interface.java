package com.report.model;

import java.util.List;


public interface Report_interface {
	
	public int insert(ReportVO reportVO);
    public int update(ReportVO reportVO);
    public int delete(Integer rid);
    public ReportVO findByPrimaryKey(Integer rid);
    public List<ReportVO> getAll();
    public ReportVO findByGidAndMember_id(String member_id ,int gid);
    public List<ReportVO> findReportByGid(int gid);

}
