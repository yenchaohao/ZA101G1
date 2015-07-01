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
    	
    	List<String> errorMsgs = new LinkedList<String>();//裝取錯誤訊息
    	req.setAttribute("errorMsgs", errorMsgs);
    	
    	String action = req.getParameter("action");
    	
    	if("showAuthority".equals(action)){ //來自showAllEmp,jsp的請求
    		try {
				/***********************1.接收請求參數*************************/
				String empid = req.getParameter("empid");
				/***************************2.開始查詢資料***************************************/
				AuthorityService authoritySvc = new AuthorityService();
				List<AuthorityVO> list = authoritySvc.getOneAid(empid);
				/***************************3.配對完成,準備轉交(Send the Success view)*************/
				req.setAttribute("empid", empid);
				req.setAttribute("showAuthority", list);
				String url = "/back/authority/showAuthority.jsp";
				RequestDispatcher successView = req
						.getRequestDispatcher(url);
				successView.forward(req, res);
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/authority/showAuthority.jsp");
				failureView.forward(req, res);
			}
    	}
    	
    	if("deleteAuthority".equals(action)){ //來自showAuthority.jsp的請求
    		try {
				/***********************1.接收請求參數*************************/
				String[] values = req.getParameterValues("box");
				if(values==null){
					errorMsgs.add("請勾選刪除項目");
				}
				String empid = req.getParameter("empid"); //接取showAuthority.jsp的req
				req.setAttribute("empid", empid);
				
				AuthorityService authoritySvc = new AuthorityService();
				List<AuthorityVO> list = null;
				
				if (!errorMsgs.isEmpty()) {
					list = authoritySvc.getOneAid(empid); //未選刪除的權限
					req.setAttribute("showAuthority", list);
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/authority/showAuthority.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				int aid = 0;
				/***************************2.開始刪除資料***************************************/
				for(String value : values){ //利用forEach一個一個把字串陣列轉成int送進資料庫
					aid = Integer.parseInt(value);
					authoritySvc.deleteAuthority(empid, aid);
				}
				/***************************3.配對完成,準備轉交(Send the Success view)*************/
				list = authoritySvc.getOneAid(empid); //已刪除完成的權限
				req.setAttribute("showAuthority", list);
				String url = "/back/authority/showAuthority.jsp";
				RequestDispatcher successView = req
						.getRequestDispatcher(url);
				successView.forward(req, res);
			} catch (NumberFormatException e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/authority/showAuthority.jsp");
				failureView.forward(req, res);
			}
    	}
    	
    	if("insertAuthority".equals(action)){ //來自addAuthority,file的請求
    		try {
				/***********************1.接收請求參數*************************/
				String[] values = req.getParameterValues("box");
				
				String empid = req.getParameter("empid"); //接取showAuthority.jsp的req
				req.setAttribute("empid", empid);
				AuthorityService authoritySvc = new AuthorityService();
				List<AuthorityVO> list = null;
				
				if(values==null){
					errorMsgs.add("請勾選新增項目");
				}
				
				if (!errorMsgs.isEmpty()) {
					list = authoritySvc.getOneAid(empid);
					req.setAttribute("showAuthority", list);
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/authority/showAuthority.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				int aid = 0;
				
				
				/***************************2.開始查詢資料***************************************/
				for(String value : values){ //利用forEach一個一個把字串陣列轉成int送進資料庫
					aid = Integer.parseInt(value);
					authoritySvc.addAuthority(empid, aid);
				}
				/***************************3.配對完成,準備轉交(Send the Success view)*************/
				list = authoritySvc.getOneAid(empid);
				req.setAttribute("showAuthority", list);
				String url = "/back/authority/showAuthority.jsp";
				RequestDispatcher successView = req
						.getRequestDispatcher(url);
				successView.forward(req, res);
			} catch (NumberFormatException e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
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
