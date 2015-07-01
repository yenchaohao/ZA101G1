package com.tran.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goods.model.*;
import com.member.model.*;
import com.send.model.*;
import com.tran.model.*;

@WebServlet("/Transaction")
public class Transaction extends HttpServlet {
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html ; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		PrintWriter out = res.getWriter();


		if ("check_tran".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			Integer tid=Integer.valueOf(req.getParameter("tid"));
			String member_id=req.getParameter("member_id");
			MemberService memSvc=new MemberService();
			MemberVO memVO=memSvc.getOneMemberByMemberId(member_id);
			TranService tranSvc=new TranService();
			if(memVO.getHaving_p()<getMaxPrice(tranSvc.getOneTran(tid))){
				errorMsgs.add("點數不足");
			}
			if(!errorMsgs.isEmpty()){
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/transaction/receive.jsp");
				failureView.forward(req, res);
				return;// 程式中斷
			}
			
			//判斷req方有沒有足夠的點數
			String req_member_id=tranSvc.getOneTran(tid).getReq_member_id();
			MemberVO req_memVO=memSvc.getOneMemberByMemberId(req_member_id);
			if(req_memVO.getHaving_p()<getMaxPrice(tranSvc.getOneTran(tid))){//REQ沒有足夠點數 x-2
				/*************************** 2-2.更新交易狀態 *****************************************/
				TranVO half_tranVO=tranSvc.getOneTran(tid);
				java.util.Date date=new java.util.Date();
				Timestamp time=new Timestamp(date.getTime());
				tranSvc.updateTran(half_tranVO.getRes_member_id(), half_tranVO.getRes_gid(), half_tranVO.getReq_member_id()
						, half_tranVO.getReq_gid(), time, 3, tid);
				
				/*************************** 4-2.扣押res點數 *****************************************/
				//取得雙方最大的價錢	
				Integer pendPoint=getMaxPrice(half_tranVO);
				//開始扣押點數(增加扣除點數欄位，減少現有點數)
				payPoint(pendPoint, half_tranVO.getRes_member_id());
				/*************************** 7-2.準備轉交(Send the Success view) ***********/
				String url = "/front/transaction/receive.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
			}else{//雙方有足夠點數 x-1
				/*************************** 2-1.更新交易狀態 *****************************************/
				TranVO succ_tranVO=tranSvc.getOneTran(tid);
				java.util.Date date=new java.util.Date();
				Timestamp time=new Timestamp(date.getTime());
				tranSvc.updateTran(succ_tranVO.getRes_member_id(), succ_tranVO.getRes_gid(), succ_tranVO.getReq_member_id()
						, succ_tranVO.getReq_gid(), time, 1, tid);
				/*************************** 3-1.刪除其他請求 *****************************************/
				List <TranVO> list=tranSvc.getUnreply();
				
				for(TranVO tranVO:list){
					//同一個res_member的清單，跟成功交易相同的商品但是交易編號不同的，狀態改成交易失敗
					if( ((tranVO.getRes_gid().equals(succ_tranVO.getRes_gid())) && !(tranVO.getTid().equals(tid)))
							||(tranVO.getReq_gid().equals(succ_tranVO.getRes_gid())&& !(tranVO.getTid().equals(tid)))//res_member的物品 當成req拿去跟別人換的
							||(tranVO.getReq_gid().equals(succ_tranVO.getReq_gid())&& !(tranVO.getTid().equals(tid)))
							||(tranVO.getRes_gid().equals(succ_tranVO.getReq_gid())&& !(tranVO.getTid().equals(tid)))
							){			
						tranSvc.updateTran(tranVO.getRes_member_id(),tranVO.getRes_gid(), tranVO.getReq_member_id(),
								tranVO.getReq_gid(),time, 2,tranVO.getTid());
						//順便歸還點數
	//					Integer pendPoint=getMaxPrice(tranVO);
	//					MemberVO req_memberVO=memSvc.getOneMemberByMemberId(tranVO.getReq_member_id());
	//					req_memberVO.setPending_p(req_memberVO.getPending_p()-pendPoint);
	//					req_memberVO.setHaving_p(req_memberVO.getHaving_p()+pendPoint);
	//					memSvc.updateMemberByObject(req_memberVO);
	//					System.out.println("還了"+req_memberVO.getMem_name()+","+pendPoint+"元");
					}			
				}
					
				/*************************** 4-1.扣押雙方點數 *****************************************/
				//取得雙方最大的價錢	
				Integer pendPoint=getMaxPrice(succ_tranVO);
				//開始扣押點數(增加扣除點數欄位，減少現有點數)
				payPoint(pendPoint, succ_tranVO.getRes_member_id());
				payPoint(pendPoint,succ_tranVO.getReq_member_id());
			 
				/*************************** 5-1.新增派送訂單(2筆) *****************************************/
				Integer res_gid=succ_tranVO.getRes_gid();
				Integer req_gid=succ_tranVO.getReq_gid();
				GoodsService goodsSvc=new GoodsService();
				GoodsVO res_good=goodsSvc.findGoodsByGid(res_gid);
				GoodsVO req_good=goodsSvc.findGoodsByGid(req_gid);
				SendService sendSvc=new SendService();
				sendSvc.addSend(res_gid, succ_tranVO.getRes_member_id(), tid);
				sendSvc.addSend(req_gid, succ_tranVO.getReq_member_id(), tid);
				/*************************** 6-1.雙方商品下架*****************************************/
				res_good.setGoods_status(3);
				res_good.setQuitdate(time);
				req_good.setGoods_status(3);
				req_good.setQuitdate(time);
				goodsSvc.updateGoodsByObject(res_good);
				goodsSvc.updateGoodsByObject(req_good);
				/*************************** 7-1.準備轉交(Send the Success view) ***********/
				String url = "/front/transaction/receive.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); 
				successView.forward(req, res);
			}
		}
		if ("fillTran".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			Integer tid=Integer.valueOf(req.getParameter("tid"));
			String member_id=req.getParameter("member_id");
			MemberService memSvc=new MemberService();
			MemberVO memVO=memSvc.getOneMemberByMemberId(member_id);
			TranService tranSvc=new TranService();
			if(memVO.getHaving_p()<getMaxPrice(tranSvc.getOneTran(tid))){
				errorMsgs.add("點數不足");
			}
			if(!errorMsgs.isEmpty()){
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/transaction/transactionCenter.jsp");
				failureView.forward(req, res);
				return;// 程式中斷
			}
			/*************************** 2-3.更新交易狀態 *****************************************/
			TranVO succ_tranVO=tranSvc.getOneTran(tid);
			java.util.Date date=new java.util.Date();
			Timestamp time=new Timestamp(date.getTime());
			tranSvc.updateTran(succ_tranVO.getRes_member_id(), succ_tranVO.getRes_gid(), succ_tranVO.getReq_member_id()
					, succ_tranVO.getReq_gid(), time, 1, tid);
			/*************************** 3-3.刪除其他請求 *****************************************/
			List <TranVO> list=tranSvc.getAll();		
			for(TranVO tranVO:list){
				//同一個res_member的清單，跟成功交易相同的商品但是交易編號不同的，狀態改成交易失敗
				if( ((tranVO.getRes_gid().equals(succ_tranVO.getRes_gid())) && !(tranVO.getTid().equals(tid)))
						||(tranVO.getReq_gid().equals(succ_tranVO.getRes_gid())&& !(tranVO.getTid().equals(tid)))//res_member的物品 當成req拿去跟別人換的
						||(tranVO.getReq_gid().equals(succ_tranVO.getReq_gid())&& !(tranVO.getTid().equals(tid)))
						||(tranVO.getRes_gid().equals(succ_tranVO.getReq_gid())&& !(tranVO.getTid().equals(tid))) 
						){			
					tranSvc.updateTran(tranVO.getRes_member_id(),tranVO.getRes_gid(), tranVO.getReq_member_id(),
							tranVO.getReq_gid(),time, 2,tranVO.getTid());
					//歸還RES鎖定扣的點數
					Integer pendPoint=getMaxPrice(tranVO);
					returnPoint(pendPoint, tranVO.getRes_member_id());
				}			
			}
			/*************************** 4-3.扣押REQ方點數 *****************************************/
			//取得雙方最大的價錢	
			Integer pendPoint=getMaxPrice(succ_tranVO);
			//開始扣押點數(增加扣除點數欄位，減少現有點數)
			payPoint(pendPoint,succ_tranVO.getReq_member_id());
			/*************************** 5-3.新增派送訂單(2筆) *****************************************/
			Integer res_gid=succ_tranVO.getRes_gid();
			Integer req_gid=succ_tranVO.getReq_gid();
			GoodsService goodsSvc=new GoodsService();
			GoodsVO res_good=goodsSvc.findGoodsByGid(res_gid);
			GoodsVO req_good=goodsSvc.findGoodsByGid(req_gid);
			SendService sendSvc=new SendService();
			sendSvc.addSend(res_gid, succ_tranVO.getRes_member_id(), tid);
			sendSvc.addSend(req_gid, succ_tranVO.getReq_member_id(), tid);
			/*************************** 6-3.雙方商品下架*****************************************/
			res_good.setGoods_status(3);
			res_good.setQuitdate(time);
			req_good.setGoods_status(3);
			req_good.setQuitdate(time);
			goodsSvc.updateGoodsByObject(res_good);
			goodsSvc.updateGoodsByObject(req_good);
			/*************************** 7-3.準備轉交(Send the Success view) ***********/
			String url = "/front/transaction/transactionCenter.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); 
			successView.forward(req, res);
		}
		
		if ("del_tran".equals(action)){
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			Integer tid=Integer.valueOf(req.getParameter("tid"));
			/*************************** 2.更新交易狀態 *****************************************/
			TranService tranSvc=new TranService();
			TranVO del_tranVO=tranSvc.getOneTran(tid);
			java.util.Date date=new java.util.Date();
			Timestamp time=new Timestamp(date.getTime());
			del_tranVO.setRes_date(time);
			del_tranVO.setStatus(2);
			tranSvc.updateTranByObject(del_tranVO);
			/*************************** 3.歸還RES方點數 *****************************************/
			String member_id=req.getParameter("member_id");
			if(member_id!=null){
			//取得雙方最大的價錢			
				Integer pendPoint=getMaxPrice(del_tranVO);
			//開始歸還點數(減少扣除的點數，增加現有點數)
				returnPoint(pendPoint, del_tranVO.getRes_member_id());
			}
			/*************************** 4.準備轉交(Send the Success view) ***********/
			String source=req.getParameter("source");
			String url = source;
			RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
			successView.forward(req, res);
			
		}
		if ("regret".equals(action)){
			/*************************** 1.接收請求參數 **********************/
			Integer tid=Integer.valueOf(req.getParameter("tid"));
			/*************************** 2.取消派送 *****************************************/
			SendService sendSvc=new SendService();
			List<SendVO>sendList=sendSvc.getSendByTid(tid);
			for(SendVO sendVO:sendList){
				sendVO.setStatus(3);
				sendSvc.updateSendByObject(sendVO);
			}
			/*************************** 3.取消交易 *****************************************/
			TranService tranSvc=new TranService();
			TranVO tranVO=tranSvc.getOneTran(tid);
			tranVO.setStatus(2);
			tranSvc.updateTranByObject(tranVO);
			/*************************** 4.歸還雙方點數 *****************************************/
			//取得雙方最大的價錢
			Integer pendPoint=getMaxPrice(tranVO);
			//開始歸還點數(減少扣除的點數，增加現有點數)
			returnPoint(pendPoint, tranVO.getReq_member_id());
			returnPoint(pendPoint, tranVO.getRes_member_id());
			/*************************** ５.修改商品狀態 *****************************************/
			GoodsService goodsSvc=new GoodsService();
			GoodsVO req_good=goodsSvc.findGoodsByGid(tranVO.getReq_gid());
			GoodsVO res_good=goodsSvc.findGoodsByGid(tranVO.getRes_gid());
			res_good.setGoods_status(0);
			res_good.setJoindate(null);
			res_good.setQuitdate(null);
			req_good.setGoods_status(0);
			req_good.setJoindate(null);
			req_good.setQuitdate(null);
			goodsSvc.updateGoodsByObject(req_good);
			goodsSvc.updateGoodsByObject(res_good);
			/*************************** ６.準備轉交 *****************************************/
			String source=req.getParameter("source");
			String url = source;
			RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
			successView.forward(req, res);
			
		}
	}
	private Integer getMaxPrice(TranVO tranVO){
		Integer res_gid=tranVO.getRes_gid();
		Integer req_gid=tranVO.getReq_gid();
		GoodsService goodsSvc=new GoodsService();
		GoodsVO res_good=goodsSvc.findGoodsByGid(res_gid);
		GoodsVO req_good=goodsSvc.findGoodsByGid(req_gid);
		Integer pendPoint=Math.max(res_good.getG_price(), req_good.getG_price());
		return pendPoint;
	}
	private void payPoint(Integer pendPoint,String member_id){
		MemberService memSvc=new  MemberService();
		MemberVO memVO=memSvc.getOneMemberByMemberId(member_id);
		memVO.setPending_p(memVO.getPending_p()+pendPoint);
		memVO.setHaving_p(memVO.getHaving_p()-pendPoint);
		memSvc.updateMemberByObject(memVO);
	}
	private void returnPoint(Integer pendPoint,String member_id){
		MemberService memSvc=new  MemberService();
		MemberVO memVO=memSvc.getOneMemberByMemberId(member_id);
		memVO.setPending_p(memVO.getPending_p()-pendPoint);
		memVO.setHaving_p(memVO.getHaving_p()+pendPoint);
		memSvc.updateMemberByObject(memVO);
	}
}
