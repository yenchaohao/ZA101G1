package com.cart.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.send.model.SendService;
import com.tran.model.TranService;
import com.tran.model.TranVO;

/**
 * Servlet implementation class cart
 */
@WebServlet("/cart")
public class cartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public cartServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	// �]�w�^���r���s�X
    	res.setContentType("text/html ; charset=UTF-8");
    	// �]�w�ШD�r���s�X
    	req.setCharacterEncoding("UTF-8");
    	
		List<String> errorMessage = new LinkedList<String>();
		req.setAttribute("errorMessage", errorMessage);
    	
    	HttpSession session = req.getSession();
    	String req_member_id = (String)session.getAttribute("member_id"); //��MemberServlet��login���|��session
    	
    	Vector<ReqAndRes> cartList = (Vector<ReqAndRes>) session.getAttribute("cart");
    	
    	String action = req.getParameter("action");
    	
    	if("addCart".equals(action)){ //�Ӧ�javascript_css/goods/cart.jsp���ШD
    		
			try {
				if(session.getAttribute("submit") != null){ //�P�_���s��z����session
					boolean match = false;
					
					String res_member_id = req.getParameter("res_member_id"); //�Q�ШD�誺member_id
					Integer res_gid = Integer.parseInt(req.getParameter("gid"));//�Q�ШD�誺gid
					Integer req_gid = null; //�ШD�誺gid
					try {
						req_gid = Integer.parseInt(req.getParameter("req_gid"));
					} catch (NumberFormatException e) {}
					
					
					ReqAndRes rar = new ReqAndRes();
					rar.setReq_gid(req_gid);
					rar.setRes_gid(res_gid);
					rar.setReq_member_id(req_member_id);
					rar.setRes_member_id(res_member_id);
					
					
					
					if(req_gid == null){
						errorMessage.add("�п�ܥ洫����");
					} else {
					
						//�s�W�Ĥ@���ܴ�����
						if(cartList == null){
							cartList = new Vector<ReqAndRes>();
							cartList.add(rar);
							
						} else {
							for(int i=0; i<cartList.size(); i++){
								Integer sgid = cartList.get(i).getRes_gid();
								Integer qgid = cartList.get(i).getReq_gid();
								//���p�s�W���@���P���������s���@�ˮ�
								if(rar.getRes_gid().equals(sgid) && rar.getReq_gid().equals(qgid)){
									errorMessage.add("<h3 style=color:red>���ӫ~�w��J������</h3>");
									match = true;
								}
							}
							if(cartList.size() == 5){
								errorMessage.add("<h3 style=color:red>�������w���A�вM��������</h3>");
							}  else
							//���p�s�W���@���P���������s�����@�ˮ�
							if(!match){
								cartList.add(rar);
							}
						}
					}
					if (!errorMessage.isEmpty()) {
						RequestDispatcher failureView = req
								.getRequestDispatcher("/front/goods/goods_detail.jsp");
						failureView.forward(req, res);
						return;
					}
					session.removeAttribute("submit"); //�������s��z������session
				}
				session.setAttribute("cart", cartList);//�⴫�������session
				errorMessage.add("<h3 style=color:red>���ӫ~�[�J���������\</h3>");
	    		String url = "/front/goods/goods_detail.jsp";
				RequestDispatcher successView = req
						.getRequestDispatcher(url);
				successView.forward(req, res);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	if("deleteCart".equals(action)){ //�Ӧ�showCart.jsp���ШD
    		
    		String[] box = req.getParameterValues("box");
    		
    		if(box == null){
    			errorMessage.add("<h3 style=color:red>�п�ܧR���ﶵ</h3>");
    		} else {
	    		//�R���������̭��ӫ~
	    		for(int i=box.length-1; i>=0; i--){
	    			cartList.remove(Integer.parseInt(box[i]));
	    		}
    		}
    		
    		if (!errorMessage.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/cart/showCart.jsp");
				failureView.forward(req, res);
				return;
			}
    		
    		if(cartList.size() == 0){
    			errorMessage.add("<h3 style=color:red>���������S���ӫ~</h3>");
    		}
    		String url = "/front/cart/showCart.jsp";
			RequestDispatcher successView = req
					.getRequestDispatcher(url);
			successView.forward(req, res);
    	}
    	
    	if("showCart".equals(action)){ //�Ӧ۩Ҧ�jsp�����W���������s��
    		
    		if(cartList == null || (cartList.size() == 0)){ 
    			errorMessage.add("<h3 style=color:red>���������S���ӫ~</h3>");
    		}
    		if (!errorMessage.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/cart/showCart.jsp");
				failureView.forward(req, res);
				return;
			}
    		
    		String url = "/front/cart/showCart.jsp";
			RequestDispatcher successView = req
					.getRequestDispatcher(url);
			successView.forward(req, res);
    	}
    	
    	if("sendTran".equals(action)){ //�Ӧ�showCart.jsp���ШD
    		
    		
    		//�s�W�洫�q�����
    		GoodsService goodsSvc = new GoodsService();
    		TranService tranSvc = new TranService();
    		List<TranVO> list = tranSvc.getOneByReqMember_id(req_member_id);
    		//���f�֥洫�q��gid���S���򴫪���gid�۲ŦX
    		for(int i=0; i<cartList.size(); i++){
		    	for(TranVO tranVO : list){
		    		if(tranVO.getRes_gid().equals(cartList.get(i).getRes_gid()) && tranVO.getReq_gid().equals(cartList.get(i).getReq_gid()) && ( tranVO.getStatus() == 0 || tranVO.getStatus() == 3)){ //�q��gid�P������gid�ۦP
		    			errorMessage.add("<h4 style=color:red>�z���ӫ~�y"+goodsSvc.findGoodsByGid(cartList.get(i).getReq_gid()).getG_name()+"�z�P�y"
		    							+goodsSvc.findGoodsByGid(cartList.get(i).getRes_gid()).getG_name()+"�z���ӫ~�w�e�X�ШD</h4>");
		    			break;
		    		} 
		    	}
    		}
    		
    		if (!errorMessage.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/cart/showCart.jsp");
				failureView.forward(req, res);
				return;
			}
    		
    		MemberService memberSvc = new MemberService();
    		//���o�ӷ|���I��
    		MemberVO req_memberVO = null;
    		//���o�Q�ШD�|���I��
    		MemberVO res_memberVO = null;
    		//�b���@���ݩ�ӷ|��������q��
    		List<TranVO> tran = tranSvc.getOneByReqMember_id(req_member_id);
    		
    		//��ӷ|���Ҧ����쪺�ШD������X�A���A�O0(��������߮�)
    		List<TranVO> resTran = tranSvc.getOneByResMember_idUn(req_member_id);
    		System.out.println(resTran.size());
    		
    		
    		
    		if(resTran.size() == 0){
    			for(int i=0; i<cartList.size(); i++){
    				String res_member_id = cartList.get(i).getRes_member_id();
		       		int res_gid = cartList.get(i).getRes_gid();
		       		int req_gid = cartList.get(i).getReq_gid();
			    	tranSvc.addTran(res_member_id, res_gid, req_member_id, req_gid);
    			}
    		}
    		
    		
    		//�^�����ɶ�
    		Timestamp time = new Timestamp(new java.util.Date().getTime());
    		//��w�I���`��
    		int total = 0;
    		//�P�_�T��
    		boolean error = false;
			   	for(TranVO tranVO : resTran){
			    	for(int i=cartList.size()-1; i>=0; i--){ //���ʪ���������q�榨�ߪ�����R��
			      		String res_member_id = cartList.get(i).getRes_member_id();
			       		int res_gid = cartList.get(i).getRes_gid();
			       		int req_gid = cartList.get(i).getReq_gid();
		    	   		if(tranVO.getRes_gid().equals(req_gid) && tranVO.getReq_gid().equals(res_gid)){ //����޲z���w�����q��A�q�椤��res_gid�Preq_gid�M
		    																						  //�q�椤��req_gid�Pres_gid�۲ŦX���ܡA�⪬�A�令1(�������)
		    				//���o�ӫ~�������
		    				GoodsVO res_goodsVO = goodsSvc.findGoodsByGid(res_gid);
		    	    		GoodsVO req_goodsVO = goodsSvc.findGoodsByGid(req_gid);
		    	    		//�ӫ~����
		    	       		int price = 0;
		    	    		//���p�Q�ШD���������ε���ШD������A�H�ШD����榩��
		    	    		if(res_goodsVO.getG_price() >= req_goodsVO.getG_price()){ 
		    	   				price = res_goodsVO.getG_price();
		    	   			} else {
		    	   				price = req_goodsVO.getG_price();
		    	   			}
		        			//���o�ӷ|�������I��
		   	    			req_memberVO = memberSvc.getOneMemberByMemberId(req_member_id);
		   	    			//���o�Q�ШD�|���Ҧ��I��
		   	    			res_memberVO = memberSvc.getOneMemberByMemberId(res_member_id);
		   					//�ӷ|�������I��
	    					int req_having_p = req_memberVO.getHaving_p();
	    					//�Q�ШD�|���I��
		    				int res_having_p = res_memberVO.getHaving_p();
		    				//�Q��w�I��
		    				int pending_p = price;
		   					//��w�I�ƥ[�`
		   					total += price;
		   					//�ӷ|�������I�Ʀ����ӫ~���椣����
		   					if(req_having_p - price < 0){
	    						errorMessage.add("�z������޲z���w���ۦP������ШD�A���z���I�Ƥ���<br>"
				    							+ "�Ц�<a href='"+req.getContextPath()+"/front/transaction/transactionCenter.jsp'>����޲z</a>�d�߬O�_���o�����"
				    							+ "�ν��x���I�ƫ��~����");
			    				RequestDispatcher view = req.getRequestDispatcher("/front/cart/showCart.jsp");
			            		view.forward(req, res);
				            	return;
		    				} else if(res_having_p - price < 0){ //�Q�ШD�|���I�Ʀ����ӫ~���椣����
		    					
		    					//�ӷ|���I�Ʀ����ӫ~����
		       					req_memberVO.setHaving_p(req_having_p - price);
		       					//�ӷ|����w�I�ơA�����X�쥻�Q�����I��
		        				req_memberVO.setPending_p(req_memberVO.getPending_p() + pending_p);
		        				//�ק���
		       					memberSvc.updateMemberByObject(req_memberVO);
		    					
		   						//����q�檬�A�令3(�I����w��)
		    					tranVO.setStatus(3);
		   						tranVO.setRes_date(time);
		   						tranSvc.updateTranByObject(tranVO);
		    						
		   						error = true;
		    						
		    				} else {
		    					//�ӷ|���I�Ʀ����ӫ~����
		        				req_memberVO.setHaving_p(req_having_p - price);
		        				//�ӷ|����w�I�ơA�����X�쥻�Q�����I��
		       					req_memberVO.setPending_p(req_memberVO.getPending_p() + pending_p);
		       					//�Q�ШD�|���I�Ʀ����ӫ~����
		       					res_memberVO.setHaving_p(res_having_p - price);
		       					//�Q�ШD�|����w�I�ơA�����X�쥻�Q�����I��
		       					res_memberVO.setPending_p(res_memberVO.getPending_p() + pending_p);
		       					//�ק���
		       					memberSvc.updateMemberByObject(req_memberVO);
		       					memberSvc.updateMemberByObject(res_memberVO);
	        					//�������q���窱�A�å[�J�^���ɶ�																		
		            			tranVO.setStatus(1);
		            			tranVO.setRes_date(time);
		            			tranSvc.updateTranByObject(tranVO);
		        					
		        				List<TranVO> unreply = tranSvc.getUnreply();
		        					
		        				for(TranVO vo : unreply){
		       						if(vo.getReq_gid().equals(req_gid) || vo.getRes_gid().equals(req_gid)
		       						|| vo.getReq_gid().equals(res_gid) || vo.getRes_gid().equals(res_gid)){
		       							//�Ҧ�����q�榳���o�ⶵ�ӫ~�����R��
		       							vo.setStatus(2);
		       							tranSvc.updateTranByObject(vo);
		       						}
		       					}
		       					//�s�W���e�ⵧ���
		       					new SendService().addSend(req_gid, req_member_id, tranVO.getTid());
		       					new SendService().addSend(res_gid, res_member_id, tranVO.getTid());
		       					//�������ӫ~���A
		       					req_goodsVO.setGoods_status(3);
		       					res_goodsVO.setGoods_status(3);
		       					goodsSvc.updateGoodsByObject(req_goodsVO);
		       					goodsSvc.updateGoodsByObject(res_goodsVO);
		        				
		       					error = true;
		   					}
		    				cartList.remove(i);
		    				errorMessage.add("<h4 style=color:red>�z���ӫ~�y"+goodsSvc.findGoodsByGid(tranVO.getRes_gid()).getG_name()+"�z���\�e�X����ШD</h4><br>");
		       				
		    			} 
		   			}
		   		}
    		//���X�w��������ߪ��q��(���A�O1��)�A�P�������t��
			List<TranVO> tranFinal = tranSvc.getAllByMember_idFinal(req_member_id);
			for(TranVO tranVO : tranFinal){
			  	for(int i=0; i<cartList.size(); i++){
			  		String res_member_id = cartList.get(i).getRes_member_id();
		       		int res_gid = cartList.get(i).getRes_gid();
		       		int req_gid = cartList.get(i).getReq_gid();
		       		//�������ӫ~���S���P����q�榨�ߪ��ӫ~�i�A�[�J����q�椤
		       		if(tranVO.getRes_gid() != res_gid && tranVO.getReq_gid() != res_gid
		       		&& tranVO.getRes_gid() != req_gid && tranVO.getReq_gid() != req_gid){
		       			tranSvc.addTran(res_member_id, res_gid, req_member_id, req_gid);
		       		}
			   	}
			}
			   		
			
    		//�s�W������������M��
    		session.removeAttribute("cart");
    		if(error == true){
    			errorMessage.add("����t�ﵲ�G:���]���ۦP���ШD,�������!<br>�w�q�֦��I�Ʀ���"+total+"�I�ܦ����I��,");
    			errorMessage.add("�Ѿl�I��:"+(req_memberVO.getHaving_p())+"<br>");
    			errorMessage.add("�Ц�<a href='"+req.getContextPath()+"/front/transaction/transactionCenter.jsp'>����޲z</a>�d�ݦ�������Ա�<br>");
    		}
    		
    		errorMessage.add("�Ҧ��ӫ~�w�����e�X�ШD");
    		String url = "/front/cart/showCart.jsp";
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
