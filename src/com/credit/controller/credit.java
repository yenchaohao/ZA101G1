package com.credit.controller;

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

import com.credit.model.*;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.member.model.*;
import com.tran.model.*;


@WebServlet("/credit")
public class credit extends HttpServlet {
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
				
				if("gotoCredit".equals(action)){
					/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
					Integer tid=Integer.valueOf(req.getParameter("tid"));
					String member_id=req.getParameter("member_id");
					/*************************** 2.������ *****************************************/
					TranService tranSvc=new TranService();
					TranVO tranVO=tranSvc.getOneTran(tid);
					String given_member_id=null;
					GoodsVO goodVO=null;
					if(tranVO.getReq_member_id().equals(member_id)){
						given_member_id=tranVO.getRes_member_id();
						goodVO=new GoodsService().findGoodsByGid(tranVO.getRes_gid());
					}	
					else{
						given_member_id=tranVO.getReq_member_id();
						goodVO=new GoodsService().findGoodsByGid(tranVO.getReq_gid());
					}
					/*************************** 3.�s�W����,�ǳ����(Send the Success view) ***********/
					req.setAttribute("tranVO", tranVO);
					req.setAttribute("goodVO", goodVO);
					req.setAttribute("given_member_id",given_member_id);
					req.setAttribute("member_id",member_id);
					String url = "/front/credit/giveCredit.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
					successView.forward(req, res);
				}
				if("new_credit".equals(action)){
					List<String> errorMsgs = new LinkedList<String>();
					req.setAttribute("errorMsgs", errorMsgs);
					/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
					Integer creditValue=Integer.valueOf(req.getParameter("score"));
					String given_member_id=req.getParameter("given_member_id");
					String member_id=req.getParameter("member_id");
					Integer cid=Integer.valueOf(req.getParameter("cid"));
					Integer tid=Integer.valueOf(req.getParameter("tid"));
					
					/*************************** 2.�}�l��s���� *****************************************/
					MemberService memSvc = new MemberService();
					MemberVO given_memberVO = memSvc.getOneMemberByMemberId(given_member_id);
					creditValue+=given_memberVO.getCredit();
					given_memberVO.setCredit(creditValue);
					memSvc.updateMemberByObject(given_memberVO);
					/***************************3.��s�������A *****************************************/
					CreditService creSvc=new CreditService();
					CreditVO creVO=creSvc.getOneByCid(cid);
					if(creVO.getMemberA_id().equals(member_id)&&creVO.getStatus().equals(0)){
						creVO.setStatus(1);
					}else if(creVO.getMemberB_id().equals(member_id)&&creVO.getStatus().equals(0)){
						creVO.setStatus(2);
					}else{
						creVO.setStatus(3);
					}
					creSvc.updateCredit(creVO);
					/***************************4.�k���I�� *****************************************/
					TranService tranSvc=new TranService();
					MemberVO memberVO =memSvc.getOneMemberByMemberId(member_id);
					TranVO tranVO=tranSvc.getOneTran(tid);
					//���o����̤j������
					Integer res_gid=tranVO.getRes_gid();
					Integer req_gid=tranVO.getReq_gid();
					GoodsService goodsSvc=new GoodsService();
					GoodsVO res_good=goodsSvc.findGoodsByGid(res_gid);
					GoodsVO req_good=goodsSvc.findGoodsByGid(req_gid);
					Integer pendPoint=Math.max(res_good.getG_price(), req_good.getG_price());	
					memberVO.setPending_p(memberVO.getPending_p()-pendPoint);
					memberVO.setHaving_p(memberVO.getHaving_p()+pendPoint);
					memSvc.updateMemberByObject(memberVO);
					/***************************5.�s�W����,�ǳ����(Send the Success view) ***********/
					req.setAttribute("given_member_id", given_member_id);
					req.setAttribute("pendPoint", pendPoint);
					String url = "/front/credit/newCredit.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
					successView.forward(req, res);
				}
				
				
	}

}
