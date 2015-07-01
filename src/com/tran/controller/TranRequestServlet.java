package com.tran.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.goods.model.*;
import com.member.model.*;
import com.tran.model.*;
import com.send.model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TranRequestServlet
 */
@WebServlet("/TranRequestServlet")
public class TranRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public TranRequestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    private void process(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
    	res.setContentType("text/html ; charset=UTF-8 "); 
    	res.setCharacterEncoding("UTF-8");
    	PrintWriter out = res.getWriter();
    	String action = req.getParameter("action");
    	List<String> errorMessage = new LinkedList<String>();
    	req.setAttribute("errorMessage", errorMessage);
    	if("tranRequest".equals(action)){
    		boolean isRepeat = false;
    		int res_gid = Integer.parseInt(req.getParameter("gid"));
    		String res_member_id = req.getParameter("res_member_id");
    		int req_gid = Integer.parseInt(req.getParameter("req_gid"));
    		String req_member_id = (String)req.getSession().getAttribute("member_id");
    		
    	
    		
    		List<TranVO> listTran = new TranService().getOneByReqMember_id(req_member_id);
    		for(TranVO tran : listTran){
    			if(tran.getRes_gid() == res_gid && tran.getReq_gid() == req_gid && (tran.getStatus() == 0 || tran.getStatus() == 3)){
    				errorMessage.add("此商品已經送出請求");
    				RequestDispatcher view = req.getRequestDispatcher("/front/goods/goods_detail.jsp");
            		view.forward(req, res);
            		return;
    			}
    		}
   
    		 		
    		//從回應者的帳號去找這位回應者所發出的所有請求
    		List<TranVO> resTran = new TranService().getOneByReqMember_id(res_member_id);
    		for(TranVO tran : resTran){
    			//如果她想要換到的東西 與 這位使用者要送出去的物品是一樣的 和 她要送出去的東西 與 這位使用者想換到的東西是一樣的
    			if(tran.getRes_gid() == req_gid && tran.getReq_gid() == res_gid && tran.getStatus() == 0){
    				MemberVO req_membervo = new MemberService().getOneMemberByMemberId(req_member_id);
    				MemberVO res_membervo = new MemberService().getOneMemberByMemberId(res_member_id);
    				GoodsVO res_goods = new GoodsService().findGoodsByGid(res_gid);
        			GoodsVO req_goods = new GoodsService().findGoodsByGid(req_gid);
        			int pending = 0;
        			if(res_goods.getG_price() >= req_goods.getG_price())
        				pending = res_goods.getG_price();
        			else
        				pending = req_goods.getG_price();
        			//如果請求者沒錢的話
        			if(req_membervo.getHaving_p() < pending){
        				errorMessage.add("您的交易管理中已有相同的交易請求，但您的點數不足<br>"
        								+ "請至<a href='"+req.getContextPath()+"/front/transaction/transactionCenter.jsp'>交易管理</a>查詢是否有這筆交易"
        								+ "或請儲值點數後繼續交易");
        				RequestDispatcher view = req.getRequestDispatcher("/front/goods/goods_detail.jsp");
                		view.forward(req, res);
                		return;
        			//如果回應者沒錢的話 交易狀態3
        			}else if(res_membervo.getHaving_p() < pending){
        				//先扣請求者的錢
        				req_membervo.setHaving_p(req_membervo.getHaving_p() - pending);
        				req_membervo.setPending_p(req_membervo.getPending_p() + pending);
        				new MemberService().updateMemberByObject(req_membervo);
        				tran.setStatus(3);
        				tran.setRes_date( new Timestamp(Calendar.getInstance().getTimeInMillis()));
        				new TranService().updateTranByObject(tran);
        				errorMessage.add("交易配對結果:對方也有相同的請求,交易成立!<br>已從擁有點數扣除"+pending+"點至扣押點數,");
        				errorMessage.add("剩餘點數:"+(req_membervo.getHaving_p())+"<br>");
        				errorMessage.add("請至<a href='"+req.getContextPath()+"/front/transaction/transactionCenter.jsp'>交易管理</a>查看此筆交易詳情 ");
        				RequestDispatcher view = req.getRequestDispatcher("/front/goods/goods_detail.jsp");
        	    		view.forward(req, res);
        	    		return;
        			}else{
        				req_membervo.setHaving_p(req_membervo.getHaving_p() - pending);
        				req_membervo.setPending_p(req_membervo.getPending_p() + pending);
        				res_membervo.setHaving_p(res_membervo.getHaving_p() - pending);
        				res_membervo.setPending_p(res_membervo.getPending_p() + pending);
        				tran.setStatus(1);
        				new TranService().updateTranByObject(tran);
        				
        				//把相同交易的刪掉
        				List<TranVO> unreply = new TranService().getUnreply();
        				//System.out.println(unreply.size());
        				
        				for(TranVO vo : unreply){
        					if(vo.getReq_gid() == res_gid || vo.getReq_gid() == res_gid || 
        						vo.getReq_gid() == req_gid || vo.getReq_gid() == req_gid
        						|| vo.getRes_gid() == res_gid || vo.getRes_gid() == res_gid || 
        						vo.getRes_gid() == req_gid || vo.getRes_gid() == req_gid
        							){
        					//	System.out.println("有一樣");
        						vo.setStatus(2);
        						new TranService().updateTranByObject(vo);
        					}
        				}
        				
        				//增加二筆派送
              			new SendService().addSend(res_gid, res_member_id, tran.getTid());
        				new SendService().addSend(req_gid, req_member_id, tran.getTid());
        				
        				//商品狀態也改
        				res_goods.setGoods_status(3);
        				req_goods.setGoods_status(3);
        				new GoodsService().updateGoodsByObject(res_goods);
        				new GoodsService().updateGoodsByObject(req_goods);
        				//會員扣錢
        				new MemberService().updateMemberByObject(req_membervo);
        				new MemberService().updateMemberByObject(res_membervo);
        				
        				errorMessage.add("交易配對結果:對方也有相同的請求,交易成立!<br>已從擁有點數扣除"+pending+"點至扣押點數,");
        				errorMessage.add("剩餘點數:"+(req_membervo.getHaving_p())+"<br>");
        				errorMessage.add("請至<a href='"+req.getContextPath()+"/front/transaction/transactionCenter.jsp'>交易管理</a>查看此筆交易詳情 ");
        				RequestDispatcher view = req.getRequestDispatcher("/front/goods/goods_detail.jsp");
        	    		view.forward(req, res);
        	    		return;
        			}
        			
    			}
    		}
    		
    		
    		MemberVO membervo = new MemberService().getOneMemberByMemberId(req_member_id);
    		new TranService().addTran(res_member_id, res_gid, req_member_id, req_gid);
    		errorMessage.add("已成功送出請求");
    		RequestDispatcher view = req.getRequestDispatcher("/front/goods/goods_detail.jsp");
    	    view.forward(req, res);
    	    return;
    			
    		
    			
    		
    		
    		
    		
    		
    	}
    	
    	
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request,response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request,response);
	}

}
