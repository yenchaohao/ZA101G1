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
    	List<String> errorMessage = new LinkedList<String>(); //�˨����~�T��
		req.setAttribute("errorMessage", errorMessage);
    	
    	HttpSession session = req.getSession();
    	String member_id = (String)session.getAttribute("member_id");
    	
    	String action = req.getParameter("action");
    	
    	if("vipadArea".equals(action)){ //�Ӧ۷|���M�Ϫ�VIP�M�ϽШD
    		
    		MemberService memberSvc = new MemberService();
    		MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
    		
    		if(memberVO.getMem_status() == 20){
    			errorMessage.add("�z�|���ɯŦ�VIP�|���A");
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
    	
    	if("upVip".equals(action)){ //�Ӧ�vipadArea.jsp���ШD
    		
    		MemberService memberSvc = new MemberService();
    		MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
    		Integer having_p = memberVO.getHaving_p()-500;
    		if(having_p < 0){
    			errorMessage.add("�z���I�Ƥ����A���x���I��");
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
    		
    		res.sendRedirect(req.getContextPath()+"/front/vipad/vipadArea.jsp"); //����s��z�A���e�X
//    		String url = "/front/vipad/vipadArea.jsp";
//			RequestDispatcher successView = req
//					.getRequestDispatcher(url);
//			successView.forward(req, res);
    	}
    	
    	if("addVipad".equals(action)){ //�Ӧ�addVipad.jsp�ШD
    		
    		String[] boxs = req.getParameterValues("box");
    		
    		if(boxs == null){
    			errorMessage.add("�п�ܤ@���ӫ~�s�W�s�i");
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
    			errorMessage.add("�z���s�i�Ƶ{�w���A�Ƶ{�����s�i�̦h����");
    		}
    		
    		if (!errorMessage.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/vipad/addVipad.jsp");
				failureView.forward(req, res);
				return;
			}
    		
    		GoodsService goodsSvc = new GoodsService(); //���F��ܰӫ~�W��
    		
    		boolean isRepeat = false;
    		int gid = 0;
    		for(String box : boxs){
    			gid = Integer.parseInt(box);
    			if(boxs.length > 5){
    				errorMessage.add("�s�i�@���̦h�s�W�����A�Э��s���");
    				break;
    			}
    			for(VipadVO vipadVO : vipadList){
    				if(vipadVO.getGid().equals(gid)){
    					errorMessage.add("�y"+goodsSvc.findGoodsByGid(gid).getG_name()+"�z���ӫ~�w�[�J�s�i�Ƶ{");
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
	    					errorMessage.add("�z���s�i�Ƶ{�{��"+(5-(vipadList.size()-vipadListNow.size()))+"���A�w�s�W�A��ܪ��e"+
	    									(vipadList.size()-vipadListNow.size())+"���A�{�b�s�i�Ƶ{�w���A�Ц�VIP�s�i�����d��");
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
    	
    	if("showVipad".equals(action)){ //�Ӧ�vipadArea.jsp���ШD
    		
    		
    		GoodsService goodsSvc = new GoodsService();
    		List<GoodsVO> goodsList = goodsSvc.findGoodsByMember_idAlive(member_id); //��X�ӷ|���W�[�ӫ~
    		VipadService vipadSvc = new VipadService();
    		List<VipadVO> vipadList = vipadSvc.getVipadByMember(member_id); //��X��VIP�|�����Ҧ��s�i
    		if(vipadList == null || vipadList.size() == 0){
    			errorMessage.add("�s�i�Ƶ{���èS���s�i");
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
    	
    	if("delete".equals(action)){ //�Ӧ۩�showVipad.jsp���ШD
    		
    		String[] boxs = req.getParameterValues("box");
    		
    		if(boxs == null){
    			errorMessage.add("�п�ܧR���s�i�ﶵ");
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
