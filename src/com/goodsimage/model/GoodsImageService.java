package com.goodsimage.model;

import java.util.List;

public class GoodsImageService {

	private GoodsImage_interface dao;
	private GoodsImage_interface daoHibernate;

	public GoodsImageService() {
		dao = new GoodsImageDAO();
		daoHibernate = new GoodsImageHibernateDAO();
	}

	public GoodsImageVO addGoodsImage(byte[] pic, Integer gid) {
		
		GoodsImageVO goodsImageVO = new GoodsImageVO();
		
		goodsImageVO.setPic(pic);
		goodsImageVO.setGid(gid);
		
		daoHibernate.insert(goodsImageVO);

		return goodsImageVO;
	}
	
	public GoodsImageVO updateGoodsImage(Integer pic_number, byte[] pic, 
			Integer gid){
		
		GoodsImageVO goodsImageVO = new GoodsImageVO();
		
		goodsImageVO.setPic_number(pic_number);
		goodsImageVO.setPic(pic);
	    goodsImageVO.setGid(gid);
	    daoHibernate.update(goodsImageVO);
		
		return goodsImageVO;
	}
	
	public GoodsImageVO updateGoodsImageByObject(GoodsImageVO goodImageVO){
		daoHibernate.update(goodImageVO);
		
		return goodImageVO;
	}
	
	public void deleteGoodsImageByPic_number(Integer pic_number){
		dao.deleteByPic_number(pic_number);
	}
	
	public List<GoodsImageVO> findGoodsImageByGid(Integer gid){
		return daoHibernate.findByGid(gid);
	}
	public GoodsImageVO findGoodsImageByPicNumber(Integer pic_number){
		return daoHibernate.findByPicNumber(pic_number);
	}

}
