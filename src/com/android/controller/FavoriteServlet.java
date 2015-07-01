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
		
		
		/** �Ӧ� GoodsInfoActivity �R���ڪ��̷R ********************************************/
		if("deleteFavorite".equals(action)){
			final int SUCCESS = 1;
			final int ERROR = -9;
			int deleteCount = 0;
			try {
				/*************************** 1.�����ШD�Ѽ� ****************************************/		
				String member_id = req.getParameter("member_id");
				Integer gid = Integer.parseInt(req.getParameter("gid"));
				/*************************** 2.�}�l�d�߸�� ****************************************/
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
				/***************************3.�d�ߧ���,�ǳ���� **************************************/					
				outStr = gson.toJson(deleteCount);
				out.print(outStr);
				out.close();
				/*************************** ��L�i�઺���~�B�z ***********************************/
			} catch (Exception e) {
				out.print(ERROR);  
				out.close();
			}					
		}	
		
		
		
		/** �Ӧ� GoodsInfoActivity �P�_���ӫ~�O���O�w�g�Q�[�J�ڪ����� ********************************************/
		if("isFavorite".equals(action)){
			try {
				/*************************** 1.�����ШD�Ѽ� ****************************************/		
				String member_id = req.getParameter("member_id");
				Integer gid = Integer.parseInt(req.getParameter("gid"));
				/*************************** 2.�}�l�d�߸�� ****************************************/
				FavoriteService favoriteSvc = new FavoriteService();		
				
				boolean isFavorite = false;
				List<FavoriteVO> favoriteVOList = favoriteSvc.getAllByMember(member_id);

				for (FavoriteVO favoriteVO : favoriteVOList) {
					if(favoriteVO.getGid().equals(gid)){
						isFavorite = true;
						break;
					}		
				}										
				/***************************3.�d�ߧ���,�ǳ���� **************************************/					
				outStr = gson.toJson(isFavorite);
				out.print(outStr);
				out.close();
				/*************************** ��L�i�઺���~�B�z ***********************************/
			} catch (Exception e) {
				out.print(false);  
				out.close();
			}					
		}	
		
		
		/** �Ӧ� FavoriteFragment ��X�ڪ��̷R���ӫ~ ********************************************/
		if("getAllByMember".equals(action)){
			try {
				/*************************** 1.�����ШD�Ѽ� ****************************************/		
				String member_id = req.getParameter("member_id");
				/*************************** 2.�}�l�d�߸�� ****************************************/
				FavoriteService favoriteSvc = new FavoriteService();		
				GoodsService goodsSvc = new GoodsService();
				
				List<FavoriteVO> favoriteVOList = favoriteSvc.getAllByMember(member_id);
				List<GoodsVO> goodsVOList = new ArrayList<>();

				for (FavoriteVO favoriteVOTemp : favoriteVOList) {
					int gid = favoriteVOTemp.getGid();
					GoodsVO goodsVO = goodsSvc.findGoodsByGid(gid);
					//�p�G�ӫ~���O�W�[,���C�X
					if(goodsVO.getGoods_status() == 1){
						goodsVOList.add(goodsVO);
					}				
				}						
				/***************************3.�d�ߧ���,�ǳ���� **************************************/
				//�Y��
				for (int i = 0; i < goodsVOList.size(); i++) {
					GoodsVO goodsVO = goodsVOList.get(i);
					goodsVO.setPic(ImageUtil.shrink(goodsVO.getPic(), 150));
				}
								
				outStr = gson.toJson(goodsVOList);
				out.print(outStr);
				out.close();
				/*************************** ��L�i�઺���~�B�z ***********************************/
			} catch (Exception e) {
				out.print("");  
				out.close();
			}					
		}	
		
		/** �Ӧ� GoodsInfoActivity �[�J�ڪ��̷R ********************************************/
		if("insert".equals(action)){
			final int SUCCESS = 1;
			final int ERROR = -9;
			
			try {
				/*************************** 1.�����ШD�Ѽ� ****************************************/		
				FavoriteVO favoriteVO = gson.fromJson(req.getParameter("favoriteVO"), FavoriteVO.class);
				/*************************** 2.�}�l�s�W��� ****************************************/
				FavoriteService favoriteSvc = new FavoriteService();
				GoodsService goodsSvc = new GoodsService();
				int gid = favoriteVO.getGid();
				String member_id = favoriteVO.getMember_id();		

				favoriteSvc.addFavorite(gid, member_id);
				GoodsVO goodsVO = goodsSvc.findGoodsByGid(gid);
				goodsVO.setG_hot(goodsVO.getG_hot()+1);
				goodsSvc.updateGoodsByObject(goodsVO);
				context.setAttribute("isAddFavorite",1 );
							
				/***************************3.�s�W����,�ǳ���� **************************************/
				out.print(SUCCESS); //�s�W���\,�^��1
				out.close();
				/*************************** ��L�i�઺���~�B�z ***********************************/
			} catch (Exception e) {
				out.print(ERROR);  //�s�W����,�^��-9
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
