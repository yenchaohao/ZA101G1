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
    				errorMessage.add("���ӫ~�w�g�e�X�ШD");
    				RequestDispatcher view = req.getRequestDispatcher("/front/goods/goods_detail.jsp");
            		view.forward(req, res);
            		return;
    			}
    		}
   
    		 		
    		//�q�^���̪��b���h��o��^���̩ҵo�X���Ҧ��ШD
    		List<TranVO> resTran = new TranService().getOneByReqMember_id(res_member_id);
    		for(TranVO tran : resTran){
    			//�p�G�o�Q�n���쪺�F�� �P �o��ϥΪ̭n�e�X�h�����~�O�@�˪� �M �o�n�e�X�h���F�� �P �o��ϥΪ̷Q���쪺�F��O�@�˪�
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
        			//�p�G�ШD�̨S������
        			if(req_membervo.getHaving_p() < pending){
        				errorMessage.add("�z������޲z���w���ۦP������ШD�A���z���I�Ƥ���<br>"
        								+ "�Ц�<a href='"+req.getContextPath()+"/front/transaction/transactionCenter.jsp'>����޲z</a>�d�߬O�_���o�����"
        								+ "�ν��x���I�ƫ��~����");
        				RequestDispatcher view = req.getRequestDispatcher("/front/goods/goods_detail.jsp");
                		view.forward(req, res);
                		return;
        			//�p�G�^���̨S������ ������A3
        			}else if(res_membervo.getHaving_p() < pending){
        				//�����ШD�̪���
        				req_membervo.setHaving_p(req_membervo.getHaving_p() - pending);
        				req_membervo.setPending_p(req_membervo.getPending_p() + pending);
        				new MemberService().updateMemberByObject(req_membervo);
        				tran.setStatus(3);
        				tran.setRes_date( new Timestamp(Calendar.getInstance().getTimeInMillis()));
        				new TranService().updateTranByObject(tran);
        				errorMessage.add("����t�ﵲ�G:���]���ۦP���ШD,�������!<br>�w�q�֦��I�Ʀ���"+pending+"�I�ܦ����I��,");
        				errorMessage.add("�Ѿl�I��:"+(req_membervo.getHaving_p())+"<br>");
        				errorMessage.add("�Ц�<a href='"+req.getContextPath()+"/front/transaction/transactionCenter.jsp'>����޲z</a>�d�ݦ�������Ա� ");
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
        				
        				//��ۦP������R��
        				List<TranVO> unreply = new TranService().getUnreply();
        				//System.out.println(unreply.size());
        				
        				for(TranVO vo : unreply){
        					if(vo.getReq_gid() == res_gid || vo.getReq_gid() == res_gid || 
        						vo.getReq_gid() == req_gid || vo.getReq_gid() == req_gid
        						|| vo.getRes_gid() == res_gid || vo.getRes_gid() == res_gid || 
        						vo.getRes_gid() == req_gid || vo.getRes_gid() == req_gid
        							){
        					//	System.out.println("���@��");
        						vo.setStatus(2);
        						new TranService().updateTranByObject(vo);
        					}
        				}
        				
        				//�W�[�G�����e
              			new SendService().addSend(res_gid, res_member_id, tran.getTid());
        				new SendService().addSend(req_gid, req_member_id, tran.getTid());
        				
        				//�ӫ~���A�]��
        				res_goods.setGoods_status(3);
        				req_goods.setGoods_status(3);
        				new GoodsService().updateGoodsByObject(res_goods);
        				new GoodsService().updateGoodsByObject(req_goods);
        				//�|������
        				new MemberService().updateMemberByObject(req_membervo);
        				new MemberService().updateMemberByObject(res_membervo);
        				
        				errorMessage.add("����t�ﵲ�G:���]���ۦP���ШD,�������!<br>�w�q�֦��I�Ʀ���"+pending+"�I�ܦ����I��,");
        				errorMessage.add("�Ѿl�I��:"+(req_membervo.getHaving_p())+"<br>");
        				errorMessage.add("�Ц�<a href='"+req.getContextPath()+"/front/transaction/transactionCenter.jsp'>����޲z</a>�d�ݦ�������Ա� ");
        				RequestDispatcher view = req.getRequestDispatcher("/front/goods/goods_detail.jsp");
        	    		view.forward(req, res);
        	    		return;
        			}
        			
    			}
    		}
    		
    		
    		MemberVO membervo = new MemberService().getOneMemberByMemberId(req_member_id);
    		new TranService().addTran(res_member_id, res_gid, req_member_id, req_gid);
    		errorMessage.add("�w���\�e�X�ШD");
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
