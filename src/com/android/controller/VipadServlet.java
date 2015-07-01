package com.android.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
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
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.vipad.model.VipadService;
import com.vipad.model.VipadVO;

public class VipadServlet extends HttpServlet {
	private void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String outStr = "";
		String action = req.getParameter("action");
		
		
		/** �Ӧ� SerialActivity �ɯŦ�VIP ********************************************/
		if ("upVip".equals(action)) {
			final int LACK_POINT = -1; // �z���I�Ƥ����A���x���I��
			final int ERROR = -9;
			try {
				/*************************** 1.�����ШD�Ѽ� ****************************************/
				String member_id = req.getParameter("member_id");
				/*************************** 2.�}�l�s�W��� ****************************************/
				MemberService memberSvc = new MemberService();
				MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
				Integer having_p = memberVO.getHaving_p() - 500;

				if (having_p < 0) {
					out.print(LACK_POINT);
					out.close();
					return;
				}

				memberVO.setHaving_p(having_p);
				memberVO.setMem_status(30);
				int updateCount = memberSvc.updateMemberByObject(memberVO);
				/***************************3.�d�ߧ���,�ǳ���� **************************************/
				
				out.print(updateCount);
				out.close();

			} catch (Exception e) {
				out.print(ERROR);
				out.close();
			}

		}
		
		/** �Ӧ� FavoriteFragment ��X�ڪ��̷R���ӫ~ ********************************************/
		if("getSix".equals(action)){
			try {		
				/*************************** 2.�}�l�d�߸�� ****************************************/
				VipadService vipadSvc = new VipadService();		
				GoodsService goodsSvc = new GoodsService();
				//����X�Ҧ�vipVO
				List<VipadVO> vipVOList = vipadSvc.getAllAlive();
				List<GoodsVO> goodsVOListTemp = new ArrayList<>();
				List<GoodsVO> goodsVOList = new ArrayList<>();

				//��X�Ҧ��bvipVOList�̭���goodsVO��igoodsVOListTemp
				for (VipadVO vipVOTemp : vipVOList) {
					int gid = vipVOTemp.getGid();
					GoodsVO goodsVO = goodsSvc.findGoodsByGid(gid);
					
					goodsVOListTemp.add(goodsVO);			
				}						
				
				//����goodsVOListTemp
				Collections.shuffle(goodsVOListTemp);

				//�p�GgoodsVOListTemp�p��6�ӴN�����ᵹgoodsVOList,�p�G�j��6�ӴN�]�j���0~5
				if (goodsVOListTemp.size() <= 6) {
					goodsVOList = goodsVOListTemp;
				} else {
					for (int i = 0; i < 6; i++) {
						GoodsVO goodsVO = goodsVOListTemp.get(i);
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
