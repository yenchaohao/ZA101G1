package com.remessage.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.message.model.*;
import com.remessage.model.*;


/**
 * Servlet implementation class remessage
 */
@WebServlet("/remessage")
public class remessage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 設定回應字元編碼
		res.setContentType("text/html ; charset=UTF-8");
		// 設定請求字元編碼
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		PrintWriter out = res.getWriter();
	
		if("new_remessage".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try{
			String remember_id=req.getParameter("remember_id");
			Integer mid=Integer.valueOf(req.getParameter("mid"));		
			
			
			String remessage=req.getParameter("remessage");
			if (remessage == null || (remessage.trim()).length() == 0) {
				errorMsgs.add("請輸入內容");
			}
			if (!errorMsgs.isEmpty()) {
				
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/remessage/listAllRemessage.jsp");
				failureView.forward(req, res);
				return;//程式中斷
			}
			/***************************2.開始新增資料*****************************************/
			RemessageService msgSvc=new RemessageService();
			RemessageVO remessageVO=msgSvc.addRemessage(remember_id, mid, remessage);
			/***************************3.新增完成,準備轉交(Send the Success view)***********/
			//req.setAttribute("messageVO", messageVO); 
			String url = "/front/remessage/listAllRemessage.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
			successView.forward(req, res);
			}catch (Exception e){
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/remessage/listAllRemessage.jsp");
				failureView.forward(req, res);
			}
		}
		
		if("del_remessage".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			Integer mid=Integer.valueOf(req.getParameter("mid"));	
			Integer rid=Integer.valueOf(req.getParameter("rid"));	
			/***************************2.開始刪除資料*****************************************/
			RemessageService msgSvc=new RemessageService();
			msgSvc.deleteRemessage(rid);
			/***************************3.新增完成,準備轉交(Send the Success view)***********/
			req.setAttribute("mid", mid); 
			String url = "/front/remessage/listAllRemessage.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
			successView.forward(req, res);
			
		}
		if("alter_remessage".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			Integer rid=Integer.valueOf(req.getParameter("rid"));	
			/***************************2.開始取得資料*****************************************/
			RemessageService remsgSvc=new  RemessageService();
			RemessageVO remsg=remsgSvc.getOneEmp(rid);
			/***************************3.取得完成,準備轉交(Send the Success view)***********/
			req.setAttribute("remsg", remsg); 
			String url = "/front/remessage/alterRemessage.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
			successView.forward(req, res);
		}
		
		if("finishAlter".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			Integer rid=Integer.valueOf(req.getParameter("rid"));	
			String message=req.getParameter("message");
			if(message.trim().length()==0||message==null){
				errorMsgs.add("請輸入內容");
			}
			if(!errorMsgs.isEmpty()){
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/remessage/alterRemessage.jsp");
				failureView.forward(req, res);
				return;// 程式中斷
			}
			/***************************2.開始修改資料*****************************************/
			RemessageService remsgSvc=new  RemessageService();
			RemessageVO remsgVO=remsgSvc.getOneEmp(rid);			
			remsgSvc.updateRemessage(rid, remsgVO.getMember_id(), remsgVO.getMid(), message, remsgVO.getR_date());
			/***************************3.取得完成,準備轉交(Send the Success view)***********/
			String url = "/front/remessage/listAllRemessage.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
			successView.forward(req, res);
		
		}
	}

}
