package com.remessage.model;

import java.util.List;

public interface Remessage_interface {
	public void insert(RemessageVO remessageVO);
    public void update(RemessageVO remessageVO);
    public void delete(Integer rid);
    public RemessageVO findByPrimaryKey(Integer rid);
    public List<RemessageVO> getAll();
    public List<RemessageVO> getByMid(Integer mid);
}
