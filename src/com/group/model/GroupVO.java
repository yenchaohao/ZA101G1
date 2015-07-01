package com.group.model;

import java.io.Serializable;
import java.sql.Date;

public class GroupVO implements Serializable {
	Integer groupid;
	String group_name;
    
    public GroupVO(){}

	public GroupVO(Integer groupid, String group_name) {
		super();
		this.groupid = groupid;
		this.group_name = group_name;
	}

	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}


}
