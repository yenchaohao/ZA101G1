package com.message.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.message.model.*;
import com.remessage.model.RemessageService;

@WebServlet("/newMessage")
public class message extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		// 設定回應字元編碼
		res.setContentType("text/html ; charset=UTF-8");
		// 設定請求字元編碼
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		PrintWriter out = res.getWriter();
		if ("new_message".equals(action)) {
			List<String> new_errorMsgs = new LinkedList<String>();
			req.setAttribute("new_errorMsgs", new_errorMsgs);
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			try {
				String member_id = req.getParameter("member_id");
				String title = req.getParameter("title");

				if (title == null || (title.trim()).length() == 0) {
					new_errorMsgs.add("請輸入標題");
				}
				// Send the use back to the form, if there were errors

				String message = req.getParameter("message");
				if (message == null || (message.trim()).length() == 0) {
					new_errorMsgs.add("請輸入內容");
				}

				MessageVO messageVO = new MessageVO();
				messageVO.setTitle(title);
				
				// Send the use back to the form, if there were errors
				if (!new_errorMsgs.isEmpty()) {
					req.setAttribute("messageVO", messageVO);
					req.setAttribute("message", message);
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front/message/listAllMessageTitle.jsp");
					failureView.forward(req, res);
					return;// 程式中斷
				}
				/*************************** 2.開始新增資料 *****************************************/
				MessageService msgSvc = new MessageService();
				Integer mid = msgSvc.addMessageGetPK(member_id, title);
				RemessageService remsgSvc = new RemessageService();
				remsgSvc.addRemessage(member_id, mid, message);
				/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
				// req.setAttribute("messageVO", messageVO);
				String url = "/front/message/listAllMessageTitle.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
				successView.forward(req, res);
			} catch (Exception e) {
				new_errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/message/listAllMessageTitle.jsp");
				failureView.forward(req, res);
			}
		}
		if("del_msg".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			Integer mid=Integer.valueOf(req.getParameter("mid"));	
			String source=req.getParameter("source");
			/***************************2.開始刪除資料*****************************************/
			MessageService msgSvc=new MessageService();
			msgSvc.deleteMessage(mid);
			/***************************3.新增完成,準備轉交(Send the Success view)***********/
			req.setAttribute("mid", mid); 
			String url=null;
			if("/back/message/searchTitle.jsp".equals(source))
			 url = "/back/message/listAllMessageTitle.jsp";
			else if("/back/message/listAllMessageTitle.jsp".equals(source))
				 url = "/back/message/listAllMessageTitle.jsp";
			
			RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
			successView.forward(req, res);
			
		}
		if("search".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			String source=req.getParameter("source");
			String title=req.getParameter("title");
			if(title==null||title.trim().length()==0){
				errorMsgs.add("請輸入查詢內容");
			}
			if (!errorMsgs.isEmpty()) {
				
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/message/listAllMessageTitle.jsp");
				failureView.forward(req, res);
				return;// 程式中斷
			}
			/***************************2.開始查詢資料*****************************************/
			MessageService msgSvc=new MessageService();
			List <MessageVO> list =msgSvc.search(title);
			if(list.isEmpty()){
				errorMsgs.add("查無資料");
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/message/listAllMessageTitle.jsp");
				failureView.forward(req, res);
				return;// 程式中斷
			}
			/***************************3.查詢完成,準備轉交(Send the Success view)***********/
			req.setAttribute("list", list); 
			req.setAttribute("title", title); 
			String url=null;
			if("/front/message/listAllMessageTitle.jsp".equals(source))
			 url = "/front/message/searchTitle.jsp";
			else if("/back/message/listAllMessageTitle.jsp".equals(source))
				 url = "/back/message/searchTitle.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
			successView.forward(req, res);
		}
	}

}
