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
		// �]�w�^���r���s�X
		res.setContentType("text/html ; charset=UTF-8");
		// �]�w�ШD�r���s�X
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
				errorMsgs.add("�п�J���e");
			}
			if (!errorMsgs.isEmpty()) {
				
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/remessage/listAllRemessage.jsp");
				failureView.forward(req, res);
				return;//�{�����_
			}
			/***************************2.�}�l�s�W���*****************************************/
			RemessageService msgSvc=new RemessageService();
			RemessageVO remessageVO=msgSvc.addRemessage(remember_id, mid, remessage);
			/***************************3.�s�W����,�ǳ����(Send the Success view)***********/
			//req.setAttribute("messageVO", messageVO); 
			String url = "/front/remessage/listAllRemessage.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
			successView.forward(req, res);
			}catch (Exception e){
				errorMsgs.add("�L�k���o���:" + e.getMessage());
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
			/***************************2.�}�l�R�����*****************************************/
			RemessageService msgSvc=new RemessageService();
			msgSvc.deleteRemessage(rid);
			/***************************3.�s�W����,�ǳ����(Send the Success view)***********/
			req.setAttribute("mid", mid); 
			String url = "/front/remessage/listAllRemessage.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
			successView.forward(req, res);
			
		}
		if("alter_remessage".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			Integer rid=Integer.valueOf(req.getParameter("rid"));	
			/***************************2.�}�l���o���*****************************************/
			RemessageService remsgSvc=new  RemessageService();
			RemessageVO remsg=remsgSvc.getOneEmp(rid);
			/***************************3.���o����,�ǳ����(Send the Success view)***********/
			req.setAttribute("remsg", remsg); 
			String url = "/front/remessage/alterRemessage.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
			successView.forward(req, res);
		}
		
		if("finishAlter".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			Integer rid=Integer.valueOf(req.getParameter("rid"));	
			String message=req.getParameter("message");
			if(message.trim().length()==0||message==null){
				errorMsgs.add("�п�J���e");
			}
			if(!errorMsgs.isEmpty()){
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/remessage/alterRemessage.jsp");
				failureView.forward(req, res);
				return;// �{�����_
			}
			/***************************2.�}�l�ק���*****************************************/
			RemessageService remsgSvc=new  RemessageService();
			RemessageVO remsgVO=remsgSvc.getOneEmp(rid);			
			remsgSvc.updateRemessage(rid, remsgVO.getMember_id(), remsgVO.getMid(), message, remsgVO.getR_date());
			/***************************3.���o����,�ǳ����(Send the Success view)***********/
			String url = "/front/remessage/listAllRemessage.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
			successView.forward(req, res);
		
		}
	}

}
