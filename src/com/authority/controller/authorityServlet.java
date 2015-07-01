package com.authority.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.authority.model.AuthorityService;
import com.authority.model.AuthorityVO;
import com.emp.model.EmpService;
import com.sun.org.apache.bcel.internal.generic.ATHROW;

/**
 * Servlet implementation class authorityServlet
 */
@WebServlet("/authorityServlet")
public class authorityServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public authorityServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	res.setContentType("text/html ; charset=UTF-8");
    	req.setCharacterEncoding("UTF-8");
    	
    	List<String> errorMsgs = new LinkedList<String>();//�˨����~�T��
    	req.setAttribute("errorMsgs", errorMsgs);
    	
    	String action = req.getParameter("action");
    	
    	if("showAuthority".equals(action)){ //�Ӧ�showAllEmp,jsp���ШD
    		try {
				/***********************1.�����ШD�Ѽ�*************************/
				String empid = req.getParameter("empid");
				/***************************2.�}�l�d�߸��***************************************/
				AuthorityService authoritySvc = new AuthorityService();
				List<AuthorityVO> list = authoritySvc.getOneAid(empid);
				/***************************3.�t�粒��,�ǳ����(Send the Success view)*************/
				req.setAttribute("empid", empid);
				req.setAttribute("showAuthority", list);
				String url = "/back/authority/showAuthority.jsp";
				RequestDispatcher successView = req
						.getRequestDispatcher(url);
				successView.forward(req, res);
			} catch (Exception e) {
				errorMsgs.add("�L�k���o���:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/authority/showAuthority.jsp");
				failureView.forward(req, res);
			}
    	}
    	
    	if("deleteAuthority".equals(action)){ //�Ӧ�showAuthority.jsp���ШD
    		try {
				/***********************1.�����ШD�Ѽ�*************************/
				String[] values = req.getParameterValues("box");
				if(values==null){
					errorMsgs.add("�ФĿ�R������");
				}
				String empid = req.getParameter("empid"); //����showAuthority.jsp��req
				req.setAttribute("empid", empid);
				
				AuthorityService authoritySvc = new AuthorityService();
				List<AuthorityVO> list = null;
				
				if (!errorMsgs.isEmpty()) {
					list = authoritySvc.getOneAid(empid); //����R�����v��
					req.setAttribute("showAuthority", list);
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/authority/showAuthority.jsp");
					failureView.forward(req, res);
					return; //�{�����_
				}
				int aid = 0;
				/***************************2.�}�l�R�����***************************************/
				for(String value : values){ //�Q��forEach�@�Ӥ@�ӧ�r��}�C�নint�e�i��Ʈw
					aid = Integer.parseInt(value);
					authoritySvc.deleteAuthority(empid, aid);
				}
				/***************************3.�t�粒��,�ǳ����(Send the Success view)*************/
				list = authoritySvc.getOneAid(empid); //�w�R���������v��
				req.setAttribute("showAuthority", list);
				String url = "/back/authority/showAuthority.jsp";
				RequestDispatcher successView = req
						.getRequestDispatcher(url);
				successView.forward(req, res);
			} catch (NumberFormatException e) {
				errorMsgs.add("�L�k���o���:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/authority/showAuthority.jsp");
				failureView.forward(req, res);
			}
    	}
    	
    	if("insertAuthority".equals(action)){ //�Ӧ�addAuthority,file���ШD
    		try {
				/***********************1.�����ШD�Ѽ�*************************/
				String[] values = req.getParameterValues("box");
				
				String empid = req.getParameter("empid"); //����showAuthority.jsp��req
				req.setAttribute("empid", empid);
				AuthorityService authoritySvc = new AuthorityService();
				List<AuthorityVO> list = null;
				
				if(values==null){
					errorMsgs.add("�ФĿ�s�W����");
				}
				
				if (!errorMsgs.isEmpty()) {
					list = authoritySvc.getOneAid(empid);
					req.setAttribute("showAuthority", list);
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/authority/showAuthority.jsp");
					failureView.forward(req, res);
					return; //�{�����_
				}
				int aid = 0;
				
				
				/***************************2.�}�l�d�߸��***************************************/
				for(String value : values){ //�Q��forEach�@�Ӥ@�ӧ�r��}�C�নint�e�i��Ʈw
					aid = Integer.parseInt(value);
					authoritySvc.addAuthority(empid, aid);
				}
				/***************************3.�t�粒��,�ǳ����(Send the Success view)*************/
				list = authoritySvc.getOneAid(empid);
				req.setAttribute("showAuthority", list);
				String url = "/back/authority/showAuthority.jsp";
				RequestDispatcher successView = req
						.getRequestDispatcher(url);
				successView.forward(req, res);
			} catch (NumberFormatException e) {
				errorMsgs.add("�L�k���o���:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/authority/showAuthority.jsp");
				failureView.forward(req, res);
			}
    		
    	}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(req, res);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(req, res);
	}

}
