package com.credit.model;

import java.util.List;

public interface Credit_interface {
	public void insert(CreditVO creditVO);
    public void update(CreditVO creditVO);
    public void delete(Integer cid);
    public CreditVO findByPrimaryKey(Integer cid);
    public List<CreditVO> getAll();
}
