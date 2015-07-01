package com.group.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class GroupTest {

	static GroupJDBCDAO groupDAO = new GroupJDBCDAO();
	static GroupVO groupVO = new GroupVO();
	
	public static void main(String[] args) {
		
//		insert();
//		update(7);
//		deleteByGroupid(7);
//		findByGroupid(3);
//		getAll();
		
	}

	public static void insert(){
		groupVO.setGroupid(7);
		groupVO.setGroup_name("不知是啥類別");
		
		int updateCount_insert = groupDAO.insert(groupVO);
		System.out.print(updateCount_insert);
	}

	public static void update(int groupid) {
		groupVO.setGroup_name("啥類別123");
		groupVO.setGroupid(groupid);
		
		int updateCount_update = groupDAO.update(groupVO);
		System.out.print(updateCount_update);
	}
	
	public static void deleteByGroupid(Integer groupid){
		int updateCount_delete = groupDAO.deleteByGroupid(groupid);
		System.out.print(updateCount_delete);
	}
	
	public static void findByGroupid(Integer groupid){
		groupVO = groupDAO.findByGroupid(groupid);
		System.out.print(groupVO.getGroupid() + ",");
		System.out.print(groupVO.getGroup_name());	
		System.out.println();
	}
	
	public static void getAll(){
		List<GroupVO> groupVOList = groupDAO.getAll();
		for (GroupVO groupVO : groupVOList) {
			System.out.print(groupVO.getGroupid() + ",");
			System.out.print(groupVO.getGroup_name());
			System.out.println();
		}
		
	}
}
