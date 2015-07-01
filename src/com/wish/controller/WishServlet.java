package com.wish.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.member.model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class WishServlet
 */
@WebServlet("/WishServlet")
public class WishServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WishServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    private void process(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException{
    			res.setContentType("text/html ; charset=UTF-8");
    			req.setCharacterEncoding("UTF-8");
    			PrintWriter out = res.getWriter();
    			String action = req.getParameter("action");
    			if("wishSearch".equals(action)){
    			HttpSession session = req.getSession();
    			List<String> errorMessage = new LinkedList();
    			
    			
    			if(req.getParameter("my_wish").trim().length() == 0 && req.getParameter("groupid").trim().length() == 0 
    					&& req.getParameter("mem_name").trim().length() == 0){
    				errorMessage.add("請 選擇或輸入 查詢條件");
    				req.setAttribute("errorMessage", errorMessage);
    				List<MemberVO> listMember = new MemberService().getAllWish();
    				session.setAttribute("listMember", listMember);
    				RequestDispatcher view = req.getRequestDispatcher("/front/member/wish_after_search.jsp");
        			view.forward(req, res);
        			return;
    			}
    				
    			HashMap<String,String[]> map = new HashMap<String,String[]>();
    				
    			map.putAll(req.getParameterMap());
    				
    			List<MemberVO> listMember = new MemberService().getSearchWish(map);
    				
    			session.setAttribute("listMember", listMember);
    				
    			RequestDispatcher view = req.getRequestDispatcher("/front/member/wish_after_search.jsp");
    			view.forward(req, res);
    			return;
    				
    			}
    			
    }
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request,response);
	}

}
