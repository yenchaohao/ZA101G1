package com.android.model;

import com.goods.model.GoodsVO;
import com.member.model.MemberVO;
import com.tran.model.TranVO;

public class TranAllBean {
	
	TranVO tranVO;
	GoodsVO reqGoodsVO;
	MemberVO reqMemberVO;
	GoodsVO resGoodsVO;
	MemberVO resMemberVO;
	
	public TranAllBean(TranVO tranVO, GoodsVO reqGoodsVO, MemberVO reqMemberVO,
			GoodsVO resGoodsVO, MemberVO resMemberVO) {
		super();
		this.tranVO = tranVO;
		this.reqGoodsVO = reqGoodsVO;
		this.reqMemberVO = reqMemberVO;
		this.resGoodsVO = resGoodsVO;
		this.resMemberVO = resMemberVO;
	}

	public TranVO getTranVO() {
		return tranVO;
	}

	public void setTranVO(TranVO tranVO) {
		this.tranVO = tranVO;
	}

	public GoodsVO getReqGoodsVO() {
		return reqGoodsVO;
	}

	public void setReqGoodsVO(GoodsVO reqGoodsVO) {
		this.reqGoodsVO = reqGoodsVO;
	}

	public MemberVO getReqMemberVO() {
		return reqMemberVO;
	}

	public void setReqMemberVO(MemberVO reqMemberVO) {
		this.reqMemberVO = reqMemberVO;
	}

	public GoodsVO getResGoodsVO() {
		return resGoodsVO;
	}

	public void setResGoodsVO(GoodsVO resGoodsVO) {
		this.resGoodsVO = resGoodsVO;
	}

	public MemberVO getResMemberVO() {
		return resMemberVO;
	}

	public void setResMemberVO(MemberVO resMemberVO) {
		this.resMemberVO = resMemberVO;
	}
	
	

}
