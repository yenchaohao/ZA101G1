package com.desire.model;

import java.util.List;

public interface Desire_interface {
	public int insert(DesireVO desireVO);
    public int update(DesireVO desireVO);
    public int delete(Integer did);
    public DesireVO findByPrimaryKey(Integer did);
    public List<DesireVO> getAll();
}
