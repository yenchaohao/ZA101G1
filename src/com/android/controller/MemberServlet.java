package com.android.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.member.model.MemberDAO;
import com.member.model.MemberJDBCDAO;
import com.member.model.MemberService;
import com.member.model.MemberVO;

public class MemberServlet extends HttpServlet {
	private void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String outStr = "";
		String action = req.getParameter("action");
		
		
		/** 來自WishActivity 搜尋 ********************************************/
		if("wishSearch".equals(action)){
			/*************************** 1.接收請求參數 ****************************************/
			//這裡傳過來的是Map的json字串,所以要先把json字串轉回Map型態
			String req_map = req.getParameter("map");
			HashMap<String,String[]> map = new HashMap<String,String[]>();
					
			Type listType = new TypeToken<HashMap<String,String[]>>(){}.getType();
			map = gson.fromJson(req_map, listType);
			/*************************** 2.開始新增資料 ****************************************/
			MemberService memberSvc = new MemberService();		
			List<MemberVO> memberVOList = new ArrayList<>();
			
			String[] groupid = map.get("groupid");
	        String[] mem_name = map.get("mem_name");
	        String[] my_wish = map.get("my_wish");

	        //如果啥都沒選就直接search的話..groupid會傳空字串過來(為了配合getWishCondition)的過濾機制
			if(groupid[0].equals("") && mem_name[0].length() == 0 && my_wish[0].length() ==0){
				memberVOList = memberSvc.getAllWish();
			} else {
				memberVOList = memberSvc.getSearchWish(map);
			}
	
			outStr = gson.toJson(memberVOList);
			out.print(outStr);
			out.close();			
		}

		/** 來自WishActivity 找出所有wish不是空值的會員 ********************************************/
		
		if("getAllWish".equals(action)){
			MemberService memberSvc = new MemberService();
			List<MemberVO> memberVOList = memberSvc.getAllWish();
	
			outStr = gson.toJson(memberVOList);
			out.print(outStr);
			out.close();
		}
		
		
		/** 來自 LoginDialogActivity 登入會員 ********************************************/
		if("isMember".equals(action)){
			int SUCCESS = 1;
			int EMAIL_ERROR = -1;     //帳號輸入錯誤
			int PASSWORD_ERROR = -2;  //密碼輸入錯誤
			int MEMBER_CHECK = -3;    //此帳號還在審核中
			int MEMBER_SUSPEND = -4;  //此帳號已停權
			int ERROR = -9;
			/*************************** 1.接收請求參數 ****************************************/			
			String email = req.getParameter("email");
			String password = req.getParameter("password");
			/*************************** 2.開始新增資料 ****************************************/
			MemberService memberSvc = new MemberService();
			MemberVO memberVO = memberSvc.getOneMemberByEmail(email);
			//如果查的到此會員,回傳 memberVO 查無則回傳訊息字串
			if(memberVO == null || memberVO.getEmail() == null || memberVO.getMem_status() == 11){
				outStr = "帳號輸入錯誤";
			} else if(!memberVO.getPassword().equals(password)){
				outStr = "密碼輸入錯誤";
			} else if(memberVO.getMem_status() == 10){
				outStr = "此帳號還在審核中";
			} else if(memberVO.getMem_status() == 21 || memberVO.getMem_status() == 31){
				outStr = "此帳號已停權";
			} else {
				outStr = gson.toJson(memberVO);
			}
			
			out.print(outStr);
			out.close();
		}
		
		if ("findByMember_id".equals(action)){
			MemberService memberSvc = new MemberService();
			String member_id = req.getParameter("member_id");
			MemberVO member = memberSvc.getOneMemberByMemberId(member_id);
			
			outStr = gson.toJson(member);
			out.print(outStr);
			out.close();
		}
		
		if ("isMemberIdExist".equals(action)) {
			MemberService memberSvc = new MemberService();
			String email = req.getParameter("email");
			MemberVO memberVO = memberSvc.getOneMemberByEmail(email);
			outStr = (memberVO.getEmail() != null) ? "true" : "false";
			out.print(outStr);
			out.close();
		}
		
		/** 來自 CreateAccountActivity 加入會員 ********************************************/
		if ("insert".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ****************************************/
				MemberVO memberVO = gson.fromJson(req.getParameter("memberVO"), MemberVO.class);
				String email = memberVO.getEmail();
				String mem_name = memberVO.getMem_name();
				String password = memberVO.getPassword();
				String id_no = memberVO.getId_no();
				String tel = memberVO.getTel();
				String address = memberVO.getAddress();
				java.sql.Date birthday = memberVO.getBirthday();
				byte[] pic = memberVO.getPic();
				/*************************** 2.開始新增資料 ****************************************/
				MemberService memberSvc = new MemberService();

				String member_id = memberSvc.addMemberGetPrimaryKey(email,mem_name, password, id_no, tel, address, birthday, pic,null);
				/***************************3.新增完成,準備轉交 **************************************/
				if(member_id != null){
					outStr = member_id;
				} else {
					outStr = "";
				}
				
				out.print(outStr);
				out.close();
			} catch (Exception e) {
				out.print(""); // 新增失敗,回傳空字串
				out.close();
			}
		}
		
		/** 來自 MyMemberActivity 更新會員資料 ********************************************/
		if("update".equals(action)){
			int SUCCESS = 1;
			int REPEAT = -1;
			int ERROR = -9;
			try {
				/*************************** 1.接收請求參數 ****************************************/
				MemberVO memberVO = gson.fromJson(req.getParameter("memberVO"), MemberVO.class);
				/*************************** 2.開始新增資料 ****************************************/
				MemberService memberSvc = new MemberService();
				
				String member_id = memberVO.getMember_id();
				String email = memberVO.getEmail();
				//先去資料庫找出它原本的email,如果跟傳來的不同,表示此會員要更動他的信箱
				if(!(memberSvc.getOneMemberByMemberId(member_id).getEmail()).equals(email)){
					if((memberSvc.getOneMemberByEmail(email)) != null){					
						out.print(REPEAT);
						out.close();
						return;
					}
				}
				
				outStr = String.valueOf(memberSvc.updateMemberByObject(memberVO));
				/***************************3.新增完成,準備轉交 **************************************/
				out.print(outStr);
				out.close();
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
