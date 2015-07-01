package com.android.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
import com.serial.model.SerialService;
import com.serial.model.SerialVO;
import com.vipad.model.VipadService;
import com.vipad.model.VipadVO;

public class SerialServlet extends HttpServlet {
	private void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String outStr = "";
		String action = req.getParameter("action");
		
		/** 來自SerialActivity 點數儲值 ********************************************/
		if("use_serial".equals(action)){
			final int NO_MATCH = -1;
			final int ERROR = -9;
			try {		
				/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
				SerialVO serialVO = gson.fromJson(req.getParameter("serialVO"), SerialVO.class);
				String member_id = serialVO.getMember_id();
				String serial_number = serialVO.getSerial_number();
				/*************************** 2.開始查詢資料 ****************************************/
				SerialService serialSvc = new SerialService();
				MemberService memberSvc = new MemberService();
				//先找出所有serialVO
				List<SerialVO> serialVOlist = serialSvc.getAll();
				
				int money = 0;
				
				for(SerialVO serialVOTemp : serialVOlist){
					if(serialVOTemp.getSerial_number().equals(serial_number) && serialVOTemp.getMember_id() == null){
						money = serialVOTemp.getMoney();
						serialSvc.updateSerial(serial_number, money, member_id);
						
						//增加會員的點數
						MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
						memberVO.setHaving_p(memberVO.getHaving_p() + money);
						memberSvc.updateMemberByObject(memberVO);
						break;
					} 								
				}				

				out.print(money);
				out.close();
				/*************************** 其他可能的錯誤處理 ***********************************/
			} catch (Exception e) {
				out.print(ERROR);  
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
