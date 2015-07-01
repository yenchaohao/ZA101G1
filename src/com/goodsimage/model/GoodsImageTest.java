package com.goodsimage.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class GoodsImageTest {

	static GoodsImageJDBCDAO goodsImageDAO = new GoodsImageJDBCDAO();
	static GoodsImageVO goodsImageVO = new GoodsImageVO();
	
	public static void main(String[] args) {
		
//		insert();
//		update(1033);
//		deleteByPic_number(1031);	
//		findByGid(1001);
	
	}

	public static void insert(){
		
		File file = new File("imageImport","ms.jpg");
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
			goodsImageVO.setPic(pic);	
			
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		
		
		
		goodsImageVO.setGid(1001);
		
		int updateCount_insert = goodsImageDAO.insert(goodsImageVO);
		System.out.print(updateCount_insert);
	}

	public static void update(int pic_number) {
		
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
		goodsImageVO.setPic(pic);	
		
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
			
	
		goodsImageVO.setGid(1002);
		goodsImageVO.setPic_number(pic_number);
		
		int updateCount_update = goodsImageDAO.update(goodsImageVO);
		System.out.print(updateCount_update);
	}
	
	public static void deleteByPic_number(int pic_number){
		int updateCount_delete = goodsImageDAO.deleteByPic_number(pic_number);
		System.out.print(updateCount_delete);
	}

	public static void findByGid(int gid){
		List<GoodsImageVO> goodsImageVOList = goodsImageDAO.findByGid(gid);
		for (GoodsImageVO goodsImageVO : goodsImageVOList) {
			System.out.print(goodsImageVO.getPic_number() + ",");
			System.out.print(goodsImageVO.getPic() + ",");
			System.out.print(goodsImageVO.getPic_number() + ",");
			System.out.print(goodsImageVO.getGid());
			System.out.println();
		}
		
	}
//	public static void findBypic_number(int pic_number){
//		List<GoodsImageVO> goodsImageVOList = goodsImageDAO.findByPicNumber(pic_number);
//		for (GoodsImageVO goodsImageVO : goodsImageVOList) {
//			System.out.print(goodsImageVO.getPic_number() + ",");
//			System.out.print(goodsImageVO.getPic() + ",");
//			System.out.print(goodsImageVO.getPic_number() + ",");
//			System.out.print(goodsImageVO.getGid());
//			System.out.println();
//		}
//		
//	}
}
