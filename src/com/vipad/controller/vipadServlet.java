package com.vipad.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.vipad.model.VipadService;
import com.vipad.model.VipadVO;

/**
 * Servlet implementation class vipadServlet
 */
@WebServlet("/vipadServlet")
public class vipadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public vipadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	res.setContentType("text/html ; charset=UTF-8");
    	req.setCharacterEncoding("UTF-8");
    	
    	ServletContext context = req.getServletContext();
    	List<String> errorMessage = new LinkedList<String>(); //裝取錯誤訊息
		req.setAttribute("errorMessage", errorMessage);
    	
    	HttpSession session = req.getSession();
    	String member_id = (String)session.getAttribute("member_id");
    	
    	String action = req.getParameter("action");
    	
    	if("vipadArea".equals(action)){ //來自會員專區的VIP專區請求
    		
    		MemberService memberSvc = new MemberService();
    		MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
    		
    		if(memberVO.getMem_status() == 20){
    			errorMessage.add("您尚未升級成VIP會員，");
    		}
    		if (!errorMessage.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/vipad/vipadArea.jsp");
				failureView.forward(req, res);
				return;
			}
    		
    		String url = "/front/vipad/vipadArea.jsp";
			RequestDispatcher successView = req
					.getRequestDispatcher(url);
			successView.forward(req, res);
    	}
    	
    	if("upVip".equals(action)){ //來自vipadArea.jsp的請求
    		
    		MemberService memberSvc = new MemberService();
    		MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
    		Integer having_p = memberVO.getHaving_p()-500;
    		if(having_p < 0){
    			errorMessage.add("您的點數不足，請儲值點數");
    		}
    		if (!errorMessage.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/vipad/vipadArea.jsp");
				failureView.forward(req, res);
				return;
			}
    		memberVO.setHaving_p(having_p);
    		memberVO.setMem_status(30);
    		memberSvc.updateMemberByObject(memberVO);
    		
    		res.sendRedirect(req.getContextPath()+"/front/vipad/vipadArea.jsp"); //防止重新整理再次送出
//    		String url = "/front/vipad/vipadArea.jsp";
//			RequestDispatcher successView = req
//					.getRequestDispatcher(url);
//			successView.forward(req, res);
    	}
    	
    	if("addVipad".equals(action)){ //來自addVipad.jsp請求
    		
    		String[] boxs = req.getParameterValues("box");
    		
    		if(boxs == null){
    			errorMessage.add("請選擇一項商品新增廣告");
    		}
    		if (!errorMessage.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/vipad/addVipad.jsp");
				failureView.forward(req, res);
				return;
			}
    		VipadService vipadSvc = new VipadService();
    		List<VipadVO> vipadList = vipadSvc.getVipadByMember(member_id);
    		
    		if(vipadList.size() >= 5){
    			errorMessage.add("您的廣告排程已滿，排程中的廣告最多五筆");
    		}
    		
    		if (!errorMessage.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/vipad/addVipad.jsp");
				failureView.forward(req, res);
				return;
			}
    		
    		GoodsService goodsSvc = new GoodsService(); //為了顯示商品名稱
    		
    		boolean isRepeat = false;
    		int gid = 0;
    		for(String box : boxs){
    			gid = Integer.parseInt(box);
    			if(boxs.length > 5){
    				errorMessage.add("廣告一次最多新增五筆，請重新選擇");
    				break;
    			}
    			for(VipadVO vipadVO : vipadList){
    				if(vipadVO.getGid().equals(gid)){
    					errorMessage.add("『"+goodsSvc.findGoodsByGid(gid).getG_name()+"』此商品已加入廣告排程");
    					isRepeat = true;
    					break;
    				}
    			}
    		}
    		if (!errorMessage.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/vipad/addVipad.jsp");
				failureView.forward(req, res);
				return;
			}
    		List<VipadVO> vipadListNow = vipadSvc.getVipadByMember(member_id);
    		for(String box : boxs){
    			gid = Integer.parseInt(box);
	    		if(isRepeat == false){
	    			if(vipadList.size() == 0){
	    				vipadSvc.addVipad(gid, member_id);
	    				context.setAttribute("isAddVipad", 1);
	    			} else {
	    				vipadList = vipadSvc.getVipadByMember(member_id);
	    				if(vipadList.size() >= 5){
	    					errorMessage.add("您的廣告排程現有"+(5-(vipadList.size()-vipadListNow.size()))+"筆，已新增你選擇的前"+
	    									(vipadList.size()-vipadListNow.size())+"筆，現在廣告排程已滿，請至VIP廣告頁面查看");
	    					break;
	    				} else {
	    					vipadSvc.addVipad(gid, member_id);
	    					context.setAttribute("isAddVipad", 1);
	    				}
	    			}
	    		}
    		}
    		if (!errorMessage.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/vipad/addVipad.jsp");
				failureView.forward(req, res);
				return;
			}
    		
    		
    		String url = "/front/vipad/showVipad.jsp";
			RequestDispatcher successView = req
					.getRequestDispatcher(url);
			successView.forward(req, res);
    	}
    	
    	if("showVipad".equals(action)){ //來自vipadArea.jsp的請求
    		
    		
    		GoodsService goodsSvc = new GoodsService();
    		List<GoodsVO> goodsList = goodsSvc.findGoodsByMember_idAlive(member_id); //找出該會員上架商品
    		VipadService vipadSvc = new VipadService();
    		List<VipadVO> vipadList = vipadSvc.getVipadByMember(member_id); //找出該VIP會員的所有廣告
    		if(vipadList == null || vipadList.size() == 0){
    			errorMessage.add("廣告排程中並沒有廣告");
    		} else {
    			for(GoodsVO goodsVO : goodsList){
            		for(VipadVO vipadVO : vipadList){
            			if(goodsVO.getGid().equals(vipadVO.getGid())){
            				req.setAttribute("gid", goodsVO.getGid());
            			}
            		}
            	}
    		}
    		if (!errorMessage.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/vipad/showVipad.jsp");
				failureView.forward(req, res);
				return;
			}
    		
    		String url = "/front/vipad/showVipad.jsp";
			RequestDispatcher successView = req
					.getRequestDispatcher(url);
			successView.forward(req, res);
    	}
    	
    	if("delete".equals(action)){ //來自於showVipad.jsp的請求
    		
    		String[] boxs = req.getParameterValues("box");
    		
    		if(boxs == null){
    			errorMessage.add("請選擇刪除廣告選項");
    		}
    		
    		if (!errorMessage.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/vipad/showVipad.jsp");
				failureView.forward(req, res);
				return;
			}
    		
    		VipadService vipadSvc = new VipadService();
    		VipadVO vipadVO = new VipadVO();
    		int vid;
    		for(String box : boxs){
    			vid = Integer.parseInt(box);
    			vipadVO.setVid(vid);
    			vipadSvc.deleteVipad(vipadVO);
    			context.setAttribute("isAddVipad", 1);
    		}
    		String url = "/front/vipad/showVipad.jsp";
			RequestDispatcher successView = req
					.getRequestDispatcher(url);
			successView.forward(req, res);
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
