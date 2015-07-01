package com.android.model;

import com.member.model.MemberVO;
import com.send.model.SendVO;

public class SendAndMemberBean {
    SendVO sendVO;
    MemberVO memberVO;

    public SendAndMemberBean(SendVO sendVO, MemberVO memberVO) {
        this.sendVO = sendVO;
        this.memberVO = memberVO;
    }

    public SendVO getSendVO() {
        return sendVO;
    }

    public void setSendVO(SendVO sendVO) {
        this.sendVO = sendVO;
    }

    public MemberVO getMemberVO() {
        return memberVO;
    }

    public void setMemberVO(MemberVO memberVO) {
        this.memberVO = memberVO;
    }
}
