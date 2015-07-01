package com.android.model;

import com.goods.model.*;
import com.tran.model.TranVO;

public class TranAllDataBean {

    TranVO tranVO;
    byte[] myGoodsPic, otherGoodsPic;
    String myG_name, otherG_name, otherMem_name;
    int myG_price, otherG_price;
    
	public TranAllDataBean(TranVO tranVO, byte[] myGoodsPic,
			byte[] otherGoodsPic, String myG_name, String otherG_name,
			String otherMem_name, int myG_price, int otherG_price) {
		super();
		this.tranVO = tranVO;
		this.myGoodsPic = myGoodsPic;
		this.otherGoodsPic = otherGoodsPic;
		this.myG_name = myG_name;
		this.otherG_name = otherG_name;
		this.otherMem_name = otherMem_name;
		this.myG_price = myG_price;
		this.otherG_price = otherG_price;
	}

	public TranVO getTranVO() {
		return tranVO;
	}

	public void setTranVO(TranVO tranVO) {
		this.tranVO = tranVO;
	}

	public byte[] getMyGoodsPic() {
		return myGoodsPic;
	}

	public void setMyGoodsPic(byte[] myGoodsPic) {
		this.myGoodsPic = myGoodsPic;
	}

	public byte[] getOtherGoodsPic() {
		return otherGoodsPic;
	}

	public void setOtherGoodsPic(byte[] otherGoodsPic) {
		this.otherGoodsPic = otherGoodsPic;
	}

	public String getMyG_name() {
		return myG_name;
	}

	public void setMyG_name(String myG_name) {
		this.myG_name = myG_name;
	}

	public String getOtherG_name() {
		return otherG_name;
	}

	public void setOtherG_name(String otherG_name) {
		this.otherG_name = otherG_name;
	}

	public String getOtherMem_name() {
		return otherMem_name;
	}

	public void setOtherMem_name(String otherMem_name) {
		this.otherMem_name = otherMem_name;
	}

	public int getMyG_price() {
		return myG_price;
	}

	public void setMyG_price(int myG_price) {
		this.myG_price = myG_price;
	}

	public int getOtherG_price() {
		return otherG_price;
	}

	public void setOtherG_price(int otherG_price) {
		this.otherG_price = otherG_price;
	}


}

