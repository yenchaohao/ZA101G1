package com.android.model;

import java.util.List;

import com.goods.model.GoodsVO;
import com.goodsimage.model.GoodsImageVO;
import com.member.model.MemberVO;

public class GoodsAndMemberAndImgBean {
	GoodsVO goodsVO;
	List<GoodsImageVO> goodsImageVOList;
    MemberVO memberVO;
    
	public GoodsAndMemberAndImgBean(GoodsVO goodsVO, List<GoodsImageVO> goodsImageVOList, MemberVO memberVO) {
		super();
		this.goodsVO = goodsVO;
		this.goodsImageVOList = goodsImageVOList;
		this.memberVO = memberVO;
	}

	public GoodsVO getGoodsVO() {
		return goodsVO;
	}

	public void setGoodsVO(GoodsVO goodsVO) {
		this.goodsVO = goodsVO;
	}

	public List<GoodsImageVO> getGoodsImageVOList() {
		return goodsImageVOList;
	}

	public void setGoodsImageVOList(List<GoodsImageVO> goodsImageVOList) {
		this.goodsImageVOList = goodsImageVOList;
	}

	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}
	
}
