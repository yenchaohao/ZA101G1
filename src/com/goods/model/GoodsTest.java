package com.goods.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class GoodsTest {

	static GoodsJDBCDAO goodsDAO = new GoodsJDBCDAO();
	static GoodsVO goodsVO = new GoodsVO();
	
	public static void main(String[] args) {
		
 //   insert();
//		update(1016);
//		deleteByGid(1016);
	//	findByGid(1015);
//		findByG_name("1");
//		findByMember_id("M1001");
//		findByGroup_id(0);
//		getAll();
    findByG_name_Group_idAlive("C",1);
    
	}

	public static void insert(){
		goodsVO.setGroupid(1);
		goodsVO.setMember_id("M1001");
		goodsVO.setG_name("商品名稱");
		goodsVO.setG_describe("商品敘述");
		goodsVO.setG_price(9999);
		goodsVO.setG_level(0);
		goodsVO.setG_hot(0);		
		
		java.util.Date du = new java.util.Date();
		Timestamp date = new Timestamp(du.getTime());
		
		goodsVO.setJoindate(date);
		
		goodsVO.setPic(null);
			
		goodsVO.setGoods_status(0);
		
	int pk = goodsDAO.insertGetPrimaryKey(goodsVO);
		System.out.print(pk);
	}

	public static void update(int gid) {
		goodsVO.setGroupid(2);
		goodsVO.setMember_id("M1002");
		goodsVO.setG_name("商品名稱2");
		goodsVO.setG_describe("商品敘述2");
		goodsVO.setG_price(1999);
		goodsVO.setG_level(1);
		goodsVO.setG_hot(1);
		
		java.util.Date du = new java.util.Date();
		Timestamp date = new Timestamp(du.getTime());
		
		goodsVO.setJoindate(date);
		
		File file = new File("imageImport","t2k_t.jpg");
				
		try{
		InputStream in = new FileInputStream(file);
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		byte[] buffer = new byte[4*1024];
		int len = 0;
		while((len = in.read(buffer)) != -1){
			byteout.write(buffer,0,len);
		}
		
		byte[] pic = byteout.toByteArray();
		byteout.close();
		goodsVO.setPic(pic);	
		
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
			
		goodsVO.setGoods_status(1);
		goodsVO.setGid(gid);
		
		int updateCount_update = goodsDAO.update(goodsVO);
		System.out.print(updateCount_update);
	}
	
	public static void deleteByGid(int gid){
		int updateCount_delete = goodsDAO.deleteByGid(gid);
		System.out.print(updateCount_delete);
	}
	
	public static void findByGid(Integer gid){
		goodsVO = goodsDAO.findByGid(gid);
		System.out.print(goodsVO.getGid() + ",");
		System.out.print(goodsVO.getGroupid() + ",");
		System.out.print(goodsVO.getMember_id() + ",");
		System.out.print(goodsVO.getG_name() + ",");
		System.out.print(goodsVO.getG_describe() + ",");
		System.out.print(goodsVO.getG_price() + ",");
		System.out.print(goodsVO.getG_level() + ",");
		System.out.print(goodsVO.getG_hot() + ",");
		System.out.print(goodsVO.getJoindate() + ",");
		System.out.print(goodsVO.getQuitdate() + ",");
		System.out.print(goodsVO.getPic() + ",");	
		System.out.print(goodsVO.getGoods_status());
		System.out.println();
	}
	
	public static void findByG_name(String g_name){
		List<GoodsVO> goodsvoList = goodsDAO.findByG_name(g_name);
		for (GoodsVO goods : goodsvoList) {
			System.out.print(goods.getGid() + ",");
			System.out.print(goods.getGroupid() + ",");
			System.out.print(goods.getMember_id() + ",");
			System.out.print(goods.getG_name() + ",");
			System.out.print(goods.getG_describe() + ",");
			System.out.print(goods.getG_price() + ",");
			System.out.print(goods.getG_level() + ",");
			System.out.print(goods.getG_hot() + ",");
			System.out.print(goods.getJoindate() + ",");
			System.out.print(goods.getQuitdate() + ",");
			System.out.print(goods.getPic() + ",");	
			System.out.print(goods.getGoods_status());
			System.out.println();
		}
	}
	
	public static void findByMember_id(String member_id){
		List<GoodsVO> goodsvoList = goodsDAO.findByMember_id(member_id);
		for (GoodsVO goods : goodsvoList) {
			System.out.print(goods.getGid() + ",");
			System.out.print(goods.getGroupid() + ",");
			System.out.print(goods.getMember_id() + ",");
			System.out.print(goods.getG_name() + ",");
			System.out.print(goods.getG_describe() + ",");
			System.out.print(goods.getG_price() + ",");
			System.out.print(goods.getG_level() + ",");
			System.out.print(goods.getG_hot() + ",");
			System.out.print(goods.getJoindate() + ",");
			System.out.print(goods.getQuitdate() + ",");
			System.out.print(goods.getPic() + ",");	
			System.out.print(goods.getGoods_status());
			System.out.println();
		}		
	}
	
	public static void findByGroup_id(Integer group_id){
		List<GoodsVO> goodsvoList = goodsDAO.findByGroup_id(group_id);
		for (GoodsVO goods : goodsvoList) {
			System.out.print(goods.getGid() + ",");
			System.out.print(goods.getGroupid() + ",");
			System.out.print(goods.getMember_id() + ",");
			System.out.print(goods.getG_name() + ",");
			System.out.print(goods.getG_describe() + ",");
			System.out.print(goods.getG_price() + ",");
			System.out.print(goods.getG_level() + ",");
			System.out.print(goods.getG_hot() + ",");
			System.out.print(goods.getJoindate() + ",");
			System.out.print(goods.getQuitdate() + ",");
			System.out.print(goods.getPic() + ",");	
			System.out.print(goods.getGoods_status());
			System.out.println();
		}
	}
	
	public static void getAll(){
		List<GoodsVO> goodsvoList = goodsDAO.getAllOrderByG_hot();
		for (GoodsVO goods : goodsvoList) {
			System.out.print(goods.getGid() + ",");
			System.out.print(goods.getGroupid() + ",");
			System.out.print(goods.getMember_id() + ",");
			System.out.print(goods.getG_name() + ",");
			System.out.print(goods.getG_describe() + ",");
			System.out.print(goods.getG_price() + ",");
			System.out.print(goods.getG_level() + ",");
			System.out.print(goods.getG_hot() + ",");
			System.out.print(goods.getJoindate() + ",");
			System.out.print(goods.getQuitdate() + ",");
			System.out.print(goods.getPic() + ",");	
			System.out.print(goods.getGoods_status());
			System.out.println();
		}		
	}
	
	public static void findByG_name_Group_idAlive(String g_name, Integer group_id){
		List<GoodsVO> goodsvoList = goodsDAO.findByG_name_Group_idAlive(g_name, group_id);
		for (GoodsVO goods : goodsvoList) {
			System.out.print(goods.getGid() + ",");
			System.out.print(goods.getGroupid() + ",");
			System.out.print(goods.getMember_id() + ",");
			System.out.print(goods.getG_name() + ",");
			System.out.print(goods.getG_describe() + ",");
			System.out.print(goods.getG_price() + ",");
			System.out.print(goods.getG_level() + ",");
			System.out.print(goods.getG_hot() + ",");
			System.out.print(goods.getJoindate() + ",");
			System.out.print(goods.getQuitdate() + ",");
			System.out.print(goods.getPic() + ",");	
			System.out.print(goods.getGoods_status());
			System.out.println();
		}		
	}
	
}
