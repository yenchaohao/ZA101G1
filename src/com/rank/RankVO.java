package com.rank;

import java.io.Serializable;
import java.util.Map;

import com.member.model.MemberVO;


public class RankVO implements Comparable<RankVO>{
	
	private MemberVO memberVO;
	private Integer count;
	
	public RankVO(MemberVO memberVO, Integer count) {
		super();
		this.memberVO = memberVO;
		this.count = count;
	}
	
	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public int compareTo(RankVO rank) {
		return rank.count.compareTo(this.count);
	}
	
	
}
