package com.android.model;

import java.util.List;

import com.goods.model.GoodsVO;
import com.member.model.MemberVO;

public class GoodsListAndMemberBean {
    List<GoodsVO> goodsVOList;
    MemberVO memberVO;

    public GoodsListAndMemberBean(List<GoodsVO> goodsVOList, MemberVO memberVO) {
        this.goodsVOList = goodsVOList;
        this.memberVO = memberVO;
    }

    public List<GoodsVO> getGoodsVOList() {
        return goodsVOList;
    }

    public void setGoodsVOList(List<GoodsVO> goodsVOList) {
        this.goodsVOList = goodsVOList;
    }

    public MemberVO getMemberVO() {
        return memberVO;
    }

    public void setMemberVO(MemberVO memberVO) {
        this.memberVO = memberVO;
    }
}
