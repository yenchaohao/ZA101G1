package com.android.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.tool.ImageUtil;
import com.favorite.model.FavoriteDAO;
import com.favorite.model.FavoriteService;
import com.favorite.model.FavoriteVO;
import com.favorite.model.Favorite_interface;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FavoriteServlet extends HttpServlet {
	private void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();
		ServletContext context = req.getServletContext();
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String outStr = "";
		String action = req.getParameter("action");
		
		
		/** 來自 GoodsInfoActivity 刪除我的最愛 ********************************************/
		if("deleteFavorite".equals(action)){
			final int SUCCESS = 1;
			final int ERROR = -9;
			int deleteCount = 0;
			try {
				/*************************** 1.接收請求參數 ****************************************/		
				String member_id = req.getParameter("member_id");
				Integer gid = Integer.parseInt(req.getParameter("gid"));
				/*************************** 2.開始查詢資料 ****************************************/
				FavoriteService favoriteSvc = new FavoriteService();		
				GoodsService goodsSvc = new GoodsService();
				
				List<FavoriteVO> favoriteVOList = favoriteSvc.getAllByMember(member_id);

				for (FavoriteVO favoriteVO : favoriteVOList) {
					if(favoriteVO.getGid().equals(gid)){
						favoriteSvc.deleteFavorite(favoriteVO.getFid());
						deleteCount = SUCCESS;
						GoodsVO goodsVO = goodsSvc.findGoodsByGid(gid);
						goodsVO.setG_hot(goodsVO.getG_hot()-1);
						goodsSvc.updateGoodsByObject(goodsVO);
						break;
					}		
				}
				context.setAttribute("isAddFavorite",1 );
				/***************************3.查詢完成,準備轉交 **************************************/					
				outStr = gson.toJson(deleteCount);
				out.print(outStr);
				out.close();
				/*************************** 其他可能的錯誤處理 ***********************************/
			} catch (Exception e) {
				out.print(ERROR);  
				out.close();
			}					
		}	
		
		
		
		/** 來自 GoodsInfoActivity 判斷此商品是不是已經被加入我的收藏 ********************************************/
		if("isFavorite".equals(action)){
			try {
				/*************************** 1.接收請求參數 ****************************************/		
				String member_id = req.getParameter("member_id");
				Integer gid = Integer.parseInt(req.getParameter("gid"));
				/*************************** 2.開始查詢資料 ****************************************/
				FavoriteService favoriteSvc = new FavoriteService();		
				
				boolean isFavorite = false;
				List<FavoriteVO> favoriteVOList = favoriteSvc.getAllByMember(member_id);

				for (FavoriteVO favoriteVO : favoriteVOList) {
					if(favoriteVO.getGid().equals(gid)){
						isFavorite = true;
						break;
					}		
				}										
				/***************************3.查詢完成,準備轉交 **************************************/					
				outStr = gson.toJson(isFavorite);
				out.print(outStr);
				out.close();
				/*************************** 其他可能的錯誤處理 ***********************************/
			} catch (Exception e) {
				out.print(false);  
				out.close();
			}					
		}	
		
		
		/** 來自 FavoriteFragment 找出我的最愛的商品 ********************************************/
		if("getAllByMember".equals(action)){
			try {
				/*************************** 1.接收請求參數 ****************************************/		
				String member_id = req.getParameter("member_id");
				/*************************** 2.開始查詢資料 ****************************************/
				FavoriteService favoriteSvc = new FavoriteService();		
				GoodsService goodsSvc = new GoodsService();
				
				List<FavoriteVO> favoriteVOList = favoriteSvc.getAllByMember(member_id);
				List<GoodsVO> goodsVOList = new ArrayList<>();

				for (FavoriteVO favoriteVOTemp : favoriteVOList) {
					int gid = favoriteVOTemp.getGid();
					GoodsVO goodsVO = goodsSvc.findGoodsByGid(gid);
					//如果商品不是上架,不列出
					if(goodsVO.getGoods_status() == 1){
						goodsVOList.add(goodsVO);
					}				
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
		
		/** 來自 GoodsInfoActivity 加入我的最愛 ********************************************/
		if("insert".equals(action)){
			final int SUCCESS = 1;
			final int ERROR = -9;
			
			try {
				/*************************** 1.接收請求參數 ****************************************/		
				FavoriteVO favoriteVO = gson.fromJson(req.getParameter("favoriteVO"), FavoriteVO.class);
				/*************************** 2.開始新增資料 ****************************************/
				FavoriteService favoriteSvc = new FavoriteService();
				GoodsService goodsSvc = new GoodsService();
				int gid = favoriteVO.getGid();
				String member_id = favoriteVO.getMember_id();		

				favoriteSvc.addFavorite(gid, member_id);
				GoodsVO goodsVO = goodsSvc.findGoodsByGid(gid);
				goodsVO.setG_hot(goodsVO.getG_hot()+1);
				goodsSvc.updateGoodsByObject(goodsVO);
				context.setAttribute("isAddFavorite",1 );
							
				/***************************3.新增完成,準備轉交 **************************************/
				out.print(SUCCESS); //新增成功,回傳1
				out.close();
				/*************************** 其他可能的錯誤處理 ***********************************/
			} catch (Exception e) {
				out.print(ERROR);  //新增失敗,回傳-9
				out.close();
			}				
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
