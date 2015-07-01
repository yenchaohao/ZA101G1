package com.goodsimage.model;

import java.io.Serializable;
import java.sql.Date;

public class GoodsImageVO implements Serializable {
	Integer pic_number;
	byte[] pic;
	Integer gid;
    
    public GoodsImageVO(){}

	public GoodsImageVO(Integer pic_number, byte[] pic, String pic_name,
			Integer gid) {
		super();
		this.pic_number = pic_number;
		this.pic = pic;
		this.gid = gid;
	}

	public Integer getPic_number() {
		return pic_number;
	}

	public void setPic_number(Integer pic_number) {
		this.pic_number = pic_number;
	}

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}


	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}
    
    
}
