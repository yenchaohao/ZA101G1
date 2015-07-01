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
		// �]�w�^���r���s�X
		res.setContentType("text/html ; charset=UTF-8");
		// �]�w�ШD�r���s�X
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		PrintWriter out = res.getWriter();
		if ("new_message".equals(action)) {
			List<String> new_errorMsgs = new LinkedList<String>();
			req.setAttribute("new_errorMsgs", new_errorMsgs);
			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			try {
				String member_id = req.getParameter("member_id");
				String title = req.getParameter("title");

				if (title == null || (title.trim()).length() == 0) {
					new_errorMsgs.add("�п�J���D");
				}
				// Send the use back to the form, if there were errors

				String message = req.getParameter("message");
				if (message == null || (message.trim()).length() == 0) {
					new_errorMsgs.add("�п�J���e");
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
					return;// �{�����_
				}
				/*************************** 2.�}�l�s�W��� *****************************************/
				MessageService msgSvc = new MessageService();
				Integer mid = msgSvc.addMessageGetPK(member_id, title);
				RemessageService remsgSvc = new RemessageService();
				remsgSvc.addRemessage(member_id, mid, message);
				/*************************** 3.�s�W����,�ǳ����(Send the Success view) ***********/
				// req.setAttribute("messageVO", messageVO);
				String url = "/front/message/listAllMessageTitle.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
				successView.forward(req, res);
			} catch (Exception e) {
				new_errorMsgs.add("�L�k���o���:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/message/listAllMessageTitle.jsp");
				failureView.forward(req, res);
			}
		}
		if("del_msg".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			Integer mid=Integer.valueOf(req.getParameter("mid"));	
			String source=req.getParameter("source");
			/***************************2.�}�l�R�����*****************************************/
			MessageService msgSvc=new MessageService();
			msgSvc.deleteMessage(mid);
			/***************************3.�s�W����,�ǳ����(Send the Success view)***********/
			req.setAttribute("mid", mid); 
			String url=null;
			if("/back/message/searchTitle.jsp".equals(source))
			 url = "/back/message/listAllMessageTitle.jsp";
			else if("/back/message/listAllMessageTitle.jsp".equals(source))
				 url = "/back/message/listAllMessageTitle.jsp";
			
			RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
			successView.forward(req, res);
			
		}
		if("search".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			String source=req.getParameter("source");
			String title=req.getParameter("title");
			if(title==null||title.trim().length()==0){
				errorMsgs.add("�п�J�d�ߤ��e");
			}
			if (!errorMsgs.isEmpty()) {
				
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/message/listAllMessageTitle.jsp");
				failureView.forward(req, res);
				return;// �{�����_
			}
			/***************************2.�}�l�d�߸��*****************************************/
			MessageService msgSvc=new MessageService();
			List <MessageVO> list =msgSvc.search(title);
			if(list.isEmpty()){
				errorMsgs.add("�d�L���");
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/message/listAllMessageTitle.jsp");
				failureView.forward(req, res);
				return;// �{�����_
			}
			/***************************3.�d�ߧ���,�ǳ����(Send the Success view)***********/
			req.setAttribute("list", list); 
			req.setAttribute("title", title); 
			String url=null;
			if("/front/message/listAllMessageTitle.jsp".equals(source))
			 url = "/front/message/searchTitle.jsp";
			else if("/back/message/listAllMessageTitle.jsp".equals(source))
				 url = "/back/message/searchTitle.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
			successView.forward(req, res);
		}
	}

}
