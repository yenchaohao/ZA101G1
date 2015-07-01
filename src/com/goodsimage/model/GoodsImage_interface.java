package com.goodsimage.model;

import java.util.List;

public interface GoodsImage_interface {
	public int insert(GoodsImageVO goodsImageVO);
	public int update(GoodsImageVO goodsImageVO);
	public int deleteByPic_number(Integer pic_number);
	public List<GoodsImageVO> findByGid(Integer gid);
	public GoodsImageVO findByPicNumber(Integer pic_number);
}
