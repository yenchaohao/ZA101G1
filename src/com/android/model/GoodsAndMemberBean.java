package com.android.model;

import com.goods.model.GoodsVO;
import com.member.model.MemberVO;

public class GoodsAndMemberBean {
    GoodsVO goodsVO;
    MemberVO memberVO;

    public GoodsAndMemberBean(GoodsVO goodsVO, MemberVO memberVO) {
        this.goodsVO = goodsVO;
        this.memberVO = memberVO;
    }

    public GoodsVO getGoodsVO() {
        return goodsVO;
    }

    public void setGoodsVO(GoodsVO goodsVO) {
        this.goodsVO = goodsVO;
    }

    public MemberVO getMemberVO() {
        return memberVO;
    }

    public void setMemberVO(MemberVO memberVO) {
        this.memberVO = memberVO;
    }
}
