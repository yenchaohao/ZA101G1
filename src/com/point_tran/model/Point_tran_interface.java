package com.point_tran.model;

import java.util.List;

public interface Point_tran_interface {
	public int insert(Point_tranVO point_tranVO);
    public int update(Point_tranVO point_tranVO);
    public int delete(Integer ptid);
    public Point_tranVO findByPrimaryKey(Integer ptid);
    public List<Point_tranVO> getAll();
}
