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
				
		/** �Ӧ� OtherMemberActivity ���o��L�|�����ӫ~ ********************************************/		
		if("findOtherGoodsByMember_id".equals(action)){
			try {
				/*************************** 1.�����ШD�Ѽ� ****************************************/		
				String member_id = req.getParameter("member_id");
				/*************************** 2.�}�l�d�߸�� ****************************************/
				GoodsService goodsSvc = new GoodsService();	
				MemberService memberSvc = new MemberService();
				
				List<GoodsVO> goodsVOList = goodsSvc.findGoodsByMember_idAlive(member_id);
				MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
	
				GoodsListAndMemberBean goodsListAndMemberBean = new GoodsListAndMemberBean(goodsVOList, memberVO);
				
				/***************************3.�d�ߧ���,�ǳ���� **************************************/
				outStr = gson.toJson(goodsListAndMemberBean);		
				out.println(outStr);
				out.close();
				/*************************** ��L�i�઺���~�B�z ***********************************/
			} catch (Exception e) {
				out.print("");  
				out.close();
			}				
		}
	
		/** �Ӧ� MyGoodsActivity �ӫ~�W�U�[ ********************************************/
		if ("ChangeStatusRequest".equals(action)) {

			int NO_VIP_OVER = -1;
			int VIP_OVER = -2;
			int ERROR = -3;

			int updateCount = 0;

			// ���B�P�_�ӷ|���ҤW�[���ӫ~�ƶq �O�_�W�L�W��
			/*************************** 1.�����ШD�Ѽ� ****************************************/
			// ���o�ШD��goodsVO
			GoodsVO goodsVO = gson.fromJson(req.getParameter("goodsVO"),GoodsVO.class);
			// �p�G�ШD��goosdVO���A�O���W�[ ==> ���N�O�n���L�W�[
			try {
				if (goodsVO.getGoods_status() == 0) {

					// ���omemberVO
					String member_id = req.getParameter("member_id");
					MemberVO membervo = new MemberService().getOneMemberByMemberId(member_id);
					// ���o���|���Ҧ��W�[�ӫ~
					List<GoodsVO> goodlist = new GoodsService().findGoodsByMember_idAlive(member_id);

					if (membervo.getMem_status() == 20 && goodlist.size() + 1 > 3) {
						updateCount = NO_VIP_OVER; // ���W�[���ӫ~�`�Ƥw�W�L���q�|�����W�[�W��(3��),�Э��s��ܩΥ[�JVIP�|��
						outStr = String.valueOf(updateCount);
						out.print(outStr);
						out.close();
						return;
					}
					if (membervo.getMem_status() == 30 && goodlist.size() + 1 > 10) {
						updateCount = VIP_OVER; // ���W�[���ӫ~�`�Ƥw�W�LVIP�|�����W�[�W��(10��),�Э��s���
						outStr = String.valueOf(updateCount);
						out.print(outStr);
						out.close();
						return;
					}
					/*************************** 2.�}�l��s��� ****************************************/
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
				/***************************3.�s�W����,�ǳ���� **************************************/
				outStr = String.valueOf(updateCount);
				out.print(outStr);
				out.close();
				
			/*************************** ��L�i�઺���~�B�z ***********************************/
			} catch (Exception e) {
				updateCount = ERROR;
				outStr = String.valueOf(updateCount);
				out.print(outStr);
				out.close();
			}			
		}
			
		/** �ثe���ϥ� *** �Ӧ� MyGoodsActivity ��ڪ��Ҧ��ӫ~(���]�t�w�R��(status!=2)�M�w�������(status!=3)) ********/
		if ("findByMember_id".equals(action)) {
			try {
				/*************************** 1.�����ШD�Ѽ� ****************************************/
				String member_id = req.getParameter("member_id");
				/*************************** 2.�}�l�d�߸�� ****************************************/
				GoodsService goodsSvc = new GoodsService();
				List<GoodsVO> goodsVOList = goodsSvc.findGoodsByMember_id(member_id);
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
		
		/** �Ӧ� MyGoodsActivity ��ڪ��ӫ~(���� status=-1 ;���W�[ status=0 ; �W�[�� status=1) ********/
		if("findByMember_idG_status".equals(action)){
			try {
				/*************************** 1.�����ШD�Ѽ� ****************************************/
				String member_id = req.getParameter("member_id");
				Integer goods_status = new Integer(req.getParameter("goods_status"));
				/*************************** 2.�}�l�d�߸�� ****************************************/
				GoodsService goodsSvc = new GoodsService();
				List<GoodsVO> goodsVOList = new ArrayList<>();
				
				if(goods_status == -1){
					//����Xstatus!=2���ӫ~,�A��status = 3���簣
					List<GoodsVO> goodsVOListTemp = goodsSvc.findGoodsByMember_id(member_id);
					for(GoodsVO goodsVO : goodsVOListTemp){
						if(goodsVO.getGoods_status() != 3){
							goodsVOList.add(goodsVO);
						}			
					}
					
				//���F�t�Xspinner������,��goods_status == 2��,��
				} else if (goods_status == 2){
					goodsVOList = goodsSvc.findGoodsByMember_idG_status(member_id, goods_status + 1);
				} else {
					goodsVOList = goodsSvc.findGoodsByMember_idG_status(member_id, goods_status);
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
		
		/** �Ӧ� GoodsInfoActivity ��ӫ~�Ա��η|����Ƥΰӫ~�Ϥ�*************************************/
		if("goodsInfo".equals(action)){
			try {
				/*************************** 1.�����ШD�Ѽ� ****************************************/
				Integer gid = Integer.parseInt(req.getParameter("gid"));
				/*************************** 2.�}�l�d�߸�� ****************************************/
				GoodsService goodsSvc = new GoodsService();
				GoodsImageService goodsImageSvc = new GoodsImageService();
				MemberService memberSvc = new MemberService();
				
				GoodsVO goodsVO = goodsSvc.findGoodsByGid(gid);
				List<GoodsImageVO> goodsImageVOList = goodsImageSvc.findGoodsImageByGid(gid);
				String member_id = goodsVO.getMember_id();
				MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);							
								
				// �N�Ҧ��Ӥ��Y�p
				for (int i = 0; i < goodsImageVOList.size(); i++) {
					GoodsImageVO goodsImageVO = goodsImageVOList.get(i);
					goodsImageVO.setPic(ImageUtil.shrink(goodsImageVO.getPic(), 500));
				}
				GoodsAndMemberAndImgBean goodsAndMemberAndImgBean = new GoodsAndMemberAndImgBean(goodsVO, goodsImageVOList, memberVO);	
				/***************************3.�d�ߧ���,�ǳ���� **************************************/				
				outStr = gson.toJson(goodsAndMemberAndImgBean);		
				out.println(outStr);
				out.close();
			} catch (Exception e) {
				out.print("");  
				out.close();
			}				
		}
		
		/** �ثe���ϥ� *** �Ӧ� GoodsInfoActivity ��ӫ~�Ա��η|����� *************************************/
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
		
		
		/** �Ӧ� GoodsFragment �j�M�ӫ~�Τ��� ) *********************************************/
		if ("searchGoods".equals(action) || "ChangePage".equals(action)) {
			GoodsService goodsSvc = new GoodsService();
			// �q��Ʈw���X�Ӫ��Ҧ��ӫ~
			List<GoodsVO> allGoodsVOList = null;
			// �^����client��list,�@�����6��
			List<GoodsVO> responselist = new ArrayList<>();
			// ���o�ҽk�j�M�r��
			String g_name = req.getParameter("g_name");
			// ���o�ШD�ӫ~���Oid
			int groupid = Integer.parseInt(req.getParameter("groupid"));

			// �̷����O���o�ӫ~�s�J"List<GoodsVO> list" 0:���� 1:�A�� 2:�B�� 3:3c
			if (groupid == 0 && g_name.length() == 0){
				allGoodsVOList = goodsSvc.getAllAlive();
			} else if (groupid == 0 && g_name.length() > 0){
				allGoodsVOList = goodsSvc.findByG_nameAlive(g_name);
			} else {
				allGoodsVOList = goodsSvc.findByG_nameGroupidAlive(g_name, groupid);
			}
				
			if ("searchGoods".equals(action)) {
				// �p�G�ӫ~�ƶq�W�L6 , ���o�e��6����ܦb�Ĥ@��
				if (allGoodsVOList.size() >= 6) {
					for (int i = 0; i < 6; i++) {
						responselist.add(allGoodsVOList.get(i));
					}
					// �p�G�ӫ~����6,�������
				} else {
					for (int i = 0; i < allGoodsVOList.size(); i++) {
						responselist.add(allGoodsVOList.get(i));
					}
				}
			} else if ("ChangePage".equals(action)) {
				// ���o�ثe�ϥΪ̩Ҧb������
				int currentPage = Integer.parseInt(req.getParameter("currentPage"));
				// �̷ӨϥΪ̩Ҧb������,��X�n�q�ĴX���ӫ~�}�l����� (start �_�I)
				int start = 6 * (currentPage - 1);
				// �P�_�ϥΪ̥ثe�������O�_�n��ܶW�L6�Ӱӫ~
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
			// �N�Ҧ��Ӥ��Y�p
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
