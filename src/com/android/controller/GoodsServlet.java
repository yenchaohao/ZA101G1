package com.android.controller;

import com.android.model.*;
import com.android.tool.ImageUtil;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.*;
import javax.servlet.http.*;

import com.emp.model.EmpService;
import com.favorite.model.FavoriteService;
import com.favorite.model.FavoriteVO;
import com.goods.model.*;
import com.goodsimage.model.GoodsImageService;
import com.goodsimage.model.GoodsImageVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.member.model.MemberService;
import com.member.model.MemberVO;

public class GoodsServlet extends HttpServlet {
	private void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String outStr = "";
		String action = req.getParameter("action");
				
		/** 來自 OtherMemberActivity 取得其他會員的商品 ********************************************/		
		if("findOtherGoodsByMember_id".equals(action)){
			try {
				/*************************** 1.接收請求參數 ****************************************/		
				String member_id = req.getParameter("member_id");
				/*************************** 2.開始查詢資料 ****************************************/
				GoodsService goodsSvc = new GoodsService();	
				MemberService memberSvc = new MemberService();
				
				List<GoodsVO> goodsVOList = goodsSvc.findGoodsByMember_idAlive(member_id);
				MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
	
				GoodsListAndMemberBean goodsListAndMemberBean = new GoodsListAndMemberBean(goodsVOList, memberVO);
				
				/***************************3.查詢完成,準備轉交 **************************************/
				outStr = gson.toJson(goodsListAndMemberBean);		
				out.println(outStr);
				out.close();
				/*************************** 其他可能的錯誤處理 ***********************************/
			} catch (Exception e) {
				out.print("");  
				out.close();
			}				
		}
	
		/** 來自 MyGoodsActivity 商品上下架 ********************************************/
		if ("ChangeStatusRequest".equals(action)) {

			int NO_VIP_OVER = -1;
			int VIP_OVER = -2;
			int ERROR = -3;

			int updateCount = 0;

			// 此處判斷該會員所上架的商品數量 是否超過上限
			/*************************** 1.接收請求參數 ****************************************/
			// 取得請求的goodsVO
			GoodsVO goodsVO = gson.fromJson(req.getParameter("goodsVO"),GoodsVO.class);
			// 如果請求的goosdVO狀態是未上架 ==> 那就是要讓他上架
			try {
				if (goodsVO.getGoods_status() == 0) {

					// 取得memberVO
					String member_id = req.getParameter("member_id");
					MemberVO membervo = new MemberService().getOneMemberByMemberId(member_id);
					// 取得此會員所有上架商品
					List<GoodsVO> goodlist = new GoodsService().findGoodsByMember_idAlive(member_id);

					if (membervo.getMem_status() == 20 && goodlist.size() + 1 > 3) {
						updateCount = NO_VIP_OVER; // 欲上架的商品總數已超過普通會員的上架上限(3件),請重新選擇或加入VIP會員
						outStr = String.valueOf(updateCount);
						out.print(outStr);
						out.close();
						return;
					}
					if (membervo.getMem_status() == 30 && goodlist.size() + 1 > 10) {
						updateCount = VIP_OVER; // 欲上架的商品總數已超過VIP會員的上架上限(10件),請重新選擇
						outStr = String.valueOf(updateCount);
						out.print(outStr);
						out.close();
						return;
					}
					/*************************** 2.開始更新資料 ****************************************/
					java.util.Date du = new java.util.Date();
					Timestamp joindate = new Timestamp(du.getTime());

					goodsVO.setJoindate(joindate);
					Calendar cal = Calendar.getInstance();
					cal.setTime(joindate);
					cal.add(Calendar.DATE, 1);
					Timestamp quitdate = new Timestamp(cal.getTimeInMillis());
					goodsVO.setGoods_status(1);
					goodsVO.setQuitdate(quitdate);

				} else if (goodsVO.getGoods_status() == 1) {
					goodsVO.setGoods_status(0);
					goodsVO.setJoindate(null);
					goodsVO.setQuitdate(null);
				}
				
				updateCount = new GoodsService().updateGoodsByObject(goodsVO);
				/***************************3.新增完成,準備轉交 **************************************/
				outStr = String.valueOf(updateCount);
				out.print(outStr);
				out.close();
				
			/*************************** 其他可能的錯誤處理 ***********************************/
			} catch (Exception e) {
				updateCount = ERROR;
				outStr = String.valueOf(updateCount);
				out.print(outStr);
				out.close();
			}			
		}
			
		/** 目前未使用 *** 來自 MyGoodsActivity 找我的所有商品(不包含已刪除(status!=2)和已完成交易(status!=3)) ********/
		if ("findByMember_id".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ****************************************/
				String member_id = req.getParameter("member_id");
				/*************************** 2.開始查詢資料 ****************************************/
				GoodsService goodsSvc = new GoodsService();
				List<GoodsVO> goodsVOList = goodsSvc.findGoodsByMember_id(member_id);
				/***************************3.查詢完成,準備轉交 **************************************/
				//縮圖
				for (int i = 0; i < goodsVOList.size(); i++) {
					GoodsVO goodsVO = goodsVOList.get(i);
					goodsVO.setPic(ImageUtil.shrink(goodsVO.getPic(), 150));
				}
								
				outStr = gson.toJson(goodsVOList);
				out.print(outStr);
				out.close();
				/*************************** 其他可能的錯誤處理 ***********************************/
			} catch (Exception e) {
				out.print("");  
				out.close();
			}		
		}
		
		/** 來自 MyGoodsActivity 找我的商品(全部 status=-1 ;未上架 status=0 ; 上架中 status=1) ********/
		if("findByMember_idG_status".equals(action)){
			try {
				/*************************** 1.接收請求參數 ****************************************/
				String member_id = req.getParameter("member_id");
				Integer goods_status = new Integer(req.getParameter("goods_status"));
				/*************************** 2.開始查詢資料 ****************************************/
				GoodsService goodsSvc = new GoodsService();
				List<GoodsVO> goodsVOList = new ArrayList<>();
				
				if(goods_status == -1){
					//先找出status!=2的商品,再把status = 3的剔除
					List<GoodsVO> goodsVOListTemp = goodsSvc.findGoodsByMember_id(member_id);
					for(GoodsVO goodsVO : goodsVOListTemp){
						if(goodsVO.getGoods_status() != 3){
							goodsVOList.add(goodsVO);
						}			
					}
					
				//為了配合spinner的順序,當goods_status == 2時,找
				} else if (goods_status == 2){
					goodsVOList = goodsSvc.findGoodsByMember_idG_status(member_id, goods_status + 1);
				} else {
					goodsVOList = goodsSvc.findGoodsByMember_idG_status(member_id, goods_status);
				}				
				/***************************3.查詢完成,準備轉交 **************************************/
				//縮圖
				for (int i = 0; i < goodsVOList.size(); i++) {
					GoodsVO goodsVO = goodsVOList.get(i);
					goodsVO.setPic(ImageUtil.shrink(goodsVO.getPic(), 150));
				}
				
				outStr = gson.toJson(goodsVOList);
				out.print(outStr);
				out.close();
				/*************************** 其他可能的錯誤處理 ***********************************/
			} catch (Exception e) {
				out.print("");  
				out.close();
			}			
		}
		
		/** 來自 GoodsInfoActivity 找商品詳情及會員資料及商品圖片*************************************/
		if("goodsInfo".equals(action)){
			try {
				/*************************** 1.接收請求參數 ****************************************/
				Integer gid = Integer.parseInt(req.getParameter("gid"));
				/*************************** 2.開始查詢資料 ****************************************/
				GoodsService goodsSvc = new GoodsService();
				GoodsImageService goodsImageSvc = new GoodsImageService();
				MemberService memberSvc = new MemberService();
				
				GoodsVO goodsVO = goodsSvc.findGoodsByGid(gid);
				List<GoodsImageVO> goodsImageVOList = goodsImageSvc.findGoodsImageByGid(gid);
				String member_id = goodsVO.getMember_id();
				MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);							
								
				// 將所有照片縮小
				for (int i = 0; i < goodsImageVOList.size(); i++) {
					GoodsImageVO goodsImageVO = goodsImageVOList.get(i);
					goodsImageVO.setPic(ImageUtil.shrink(goodsImageVO.getPic(), 500));
				}
				GoodsAndMemberAndImgBean goodsAndMemberAndImgBean = new GoodsAndMemberAndImgBean(goodsVO, goodsImageVOList, memberVO);	
				/***************************3.查詢完成,準備轉交 **************************************/				
				outStr = gson.toJson(goodsAndMemberAndImgBean);		
				out.println(outStr);
				out.close();
			} catch (Exception e) {
				out.print("");  
				out.close();
			}				
		}
		
		/** 目前未使用 *** 來自 GoodsInfoActivity 找商品詳情及會員資料 *************************************/
		if("aaa".equals(action)){
			GoodsService goodsSvc = new GoodsService();
			MemberService memberSvc = new MemberService();
			int gid = Integer.parseInt(req.getParameter("gid"));
			GoodsVO goodsVO = goodsSvc.findGoodsByGid(gid);
			String member_id = goodsVO.getMember_id();
			MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
			
			GoodsAndMemberBean goodsAndMemberBean = new GoodsAndMemberBean(goodsVO, memberVO);		
			outStr = gson.toJson(goodsAndMemberBean);		
			out.println(outStr);
			out.close();
		}
		
		
		/** 來自 GoodsFragment 搜尋商品及分頁 ) *********************************************/
		if ("searchGoods".equals(action) || "ChangePage".equals(action)) {
			GoodsService goodsSvc = new GoodsService();
			// 從資料庫撈出來的所有商品
			List<GoodsVO> allGoodsVOList = null;
			// 回應給client的list,一次顯示6個
			List<GoodsVO> responselist = new ArrayList<>();
			// 取得模糊搜尋字串
			String g_name = req.getParameter("g_name");
			// 取得請求商品類別id
			int groupid = Integer.parseInt(req.getParameter("groupid"));

			// 依照類別取得商品存入"List<GoodsVO> list" 0:全部 1:服飾 2:運動 3:3c
			if (groupid == 0 && g_name.length() == 0){
				allGoodsVOList = goodsSvc.getAllAlive();
			} else if (groupid == 0 && g_name.length() > 0){
				allGoodsVOList = goodsSvc.findByG_nameAlive(g_name);
			} else {
				allGoodsVOList = goodsSvc.findByG_nameGroupidAlive(g_name, groupid);
			}
				
			if ("searchGoods".equals(action)) {
				// 如果商品數量超過6 , 取得前六6筆顯示在第一頁
				if (allGoodsVOList.size() >= 6) {
					for (int i = 0; i < 6; i++) {
						responselist.add(allGoodsVOList.get(i));
					}
					// 如果商品不足6,直接顯示
				} else {
					for (int i = 0; i < allGoodsVOList.size(); i++) {
						responselist.add(allGoodsVOList.get(i));
					}
				}
			} else if ("ChangePage".equals(action)) {
				// 取得目前使用者所在的頁面
				int currentPage = Integer.parseInt(req.getParameter("currentPage"));
				// 依照使用者所在的頁面,算出要從第幾筆商品開始取資料 (start 起點)
				int start = 6 * (currentPage - 1);
				// 判斷使用者目前的頁面是否要顯示超過6個商品
				if (allGoodsVOList.size() - start > 6) {
					for (int i = start; i < start + 6; i++) {
						responselist.add(allGoodsVOList.get(i));
					}
				} else {
					for (int i = start; i < allGoodsVOList.size(); i++) {
						responselist.add(allGoodsVOList.get(i));
					}
				}
			}
			// 將所有照片縮小
			for (int i = 0; i < allGoodsVOList.size(); i++) {
				GoodsVO goodsVO = allGoodsVOList.get(i);
				goodsVO.setPic(ImageUtil.shrink(goodsVO.getPic(), 200));
			}
			GoodsResponseBean goodsBean = new GoodsResponseBean(responselist,allGoodsVOList.size());
			outStr = gson.toJson(goodsBean);
			out.println(outStr);
			out.close();
			
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		process(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		process(req, res);
	}

}
