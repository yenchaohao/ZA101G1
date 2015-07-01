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
    	// 設定回應字元編碼
    	res.setContentType("text/html ; charset=UTF-8");
    	// 設定請求字元編碼
    	req.setCharacterEncoding("UTF-8");
    	
		List<String> errorMessage = new LinkedList<String>();
		req.setAttribute("errorMessage", errorMessage);
    	
    	HttpSession session = req.getSession();
    	String req_member_id = (String)session.getAttribute("member_id"); //接MemberServlet的login的會員session
    	
    	Vector<ReqAndRes> cartList = (Vector<ReqAndRes>) session.getAttribute("cart");
    	
    	String action = req.getParameter("action");
    	
    	if("addCart".equals(action)){ //來自javascript_css/goods/cart.jsp的請求
    		
			try {
				if(session.getAttribute("submit") != null){ //判斷重新整理頁面session
					boolean match = false;
					
					String res_member_id = req.getParameter("res_member_id"); //被請求方的member_id
					Integer res_gid = Integer.parseInt(req.getParameter("gid"));//被請求方的gid
					Integer req_gid = null; //請求方的gid
					try {
						req_gid = Integer.parseInt(req.getParameter("req_gid"));
					} catch (NumberFormatException e) {}
					
					
					ReqAndRes rar = new ReqAndRes();
					rar.setReq_gid(req_gid);
					rar.setRes_gid(res_gid);
					rar.setReq_member_id(req_member_id);
					rar.setRes_member_id(res_member_id);
					
					
					
					if(req_gid == null){
						errorMessage.add("請選擇交換項目");
					} else {
					
						//新增第一筆至換物車
						if(cartList == null){
							cartList = new Vector<ReqAndRes>();
							cartList.add(rar);
							
						} else {
							for(int i=0; i<cartList.size(); i++){
								Integer sgid = cartList.get(i).getRes_gid();
								Integer qgid = cartList.get(i).getReq_gid();
								//假如新增的一筆與換物車的編號一樣時
								if(rar.getRes_gid().equals(sgid) && rar.getReq_gid().equals(qgid)){
									errorMessage.add("<h3 style=color:red>此商品已交入換物車</h3>");
									match = true;
								}
							}
							if(cartList.size() == 5){
								errorMessage.add("<h3 style=color:red>換物車已滿，請清除換物車</h3>");
							}  else
							//假如新增的一筆與換物車的編號不一樣時
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
					session.removeAttribute("submit"); //移除重新整理頁面的session
				}
				session.setAttribute("cart", cartList);//把換物車放到session
				errorMessage.add("<h3 style=color:red>此商品加入換物車成功</h3>");
	    		String url = "/front/goods/goods_detail.jsp";
				RequestDispatcher successView = req
						.getRequestDispatcher(url);
				successView.forward(req, res);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	if("deleteCart".equals(action)){ //來自showCart.jsp的請求
    		
    		String[] box = req.getParameterValues("box");
    		
    		if(box == null){
    			errorMessage.add("<h3 style=color:red>請選擇刪除選項</h3>");
    		} else {
	    		//刪除換物車裡面商品
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
    			errorMessage.add("<h3 style=color:red>換物車內沒有商品</h3>");
    		}
    		String url = "/front/cart/showCart.jsp";
			RequestDispatcher successView = req
					.getRequestDispatcher(url);
			successView.forward(req, res);
    	}
    	
    	if("showCart".equals(action)){ //來自所有jsp網頁上換物車的連結
    		
    		if(cartList == null || (cartList.size() == 0)){ 
    			errorMessage.add("<h3 style=color:red>換物車內沒有商品</h3>");
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
    	
    	if("sendTran".equals(action)){ //來自showCart.jsp的請求
    		
    		
    		//新增交換訂單明細
    		GoodsService goodsSvc = new GoodsService();
    		TranService tranSvc = new TranService();
    		List<TranVO> list = tranSvc.getOneByReqMember_id(req_member_id);
    		//先審核交換訂單gid有沒有跟換物車gid相符合
    		for(int i=0; i<cartList.size(); i++){
		    	for(TranVO tranVO : list){
		    		if(tranVO.getRes_gid().equals(cartList.get(i).getRes_gid()) && tranVO.getReq_gid().equals(cartList.get(i).getReq_gid()) && ( tranVO.getStatus() == 0 || tranVO.getStatus() == 3)){ //訂單gid與換物車gid相同
		    			errorMessage.add("<h4 style=color:red>您的商品『"+goodsSvc.findGoodsByGid(cartList.get(i).getReq_gid()).getG_name()+"』與『"
		    							+goodsSvc.findGoodsByGid(cartList.get(i).getRes_gid()).getG_name()+"』此商品已送出請求</h4>");
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
    		//取得該會員點數
    		MemberVO req_memberVO = null;
    		//取得被請求會員點數
    		MemberVO res_memberVO = null;
    		//在撈一次屬於該會員的交易訂單
    		List<TranVO> tran = tranSvc.getOneByReqMember_id(req_member_id);
    		
    		//把該會員所有收到的請求交易撈出，狀態是0(未交易成立時)
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
    		
    		
    		//回應的時間
    		Timestamp time = new Timestamp(new java.util.Date().getTime());
    		//鎖定點數總數
    		int total = 0;
    		//判斷訊息
    		boolean error = false;
			   	for(TranVO tranVO : resTran){
			    	for(int i=cartList.size()-1; i>=0; i--){ //把購物車有交易訂單成立的交易刪除
			      		String res_member_id = cartList.get(i).getRes_member_id();
			       		int res_gid = cartList.get(i).getRes_gid();
			       		int req_gid = cartList.get(i).getReq_gid();
		    	   		if(tranVO.getRes_gid().equals(req_gid) && tranVO.getReq_gid().equals(res_gid)){ //交易管理中已有此訂單，訂單中的res_gid與req_gid和
		    																						  //訂單中的req_gid與res_gid相符合的話，把狀態改成1(交易成立)
		    				//取得商品雙方價錢
		    				GoodsVO res_goodsVO = goodsSvc.findGoodsByGid(res_gid);
		    	    		GoodsVO req_goodsVO = goodsSvc.findGoodsByGid(req_gid);
		    	    		//商品價格
		    	       		int price = 0;
		    	    		//假如被請求方價錢高於或等於請求方價錢，以請求方價格扣押
		    	    		if(res_goodsVO.getG_price() >= req_goodsVO.getG_price()){ 
		    	   				price = res_goodsVO.getG_price();
		    	   			} else {
		    	   				price = req_goodsVO.getG_price();
		    	   			}
		        			//取得該會員本身點數
		   	    			req_memberVO = memberSvc.getOneMemberByMemberId(req_member_id);
		   	    			//取得被請求會員所有點數
		   	    			res_memberVO = memberSvc.getOneMemberByMemberId(res_member_id);
		   					//該會員本身點數
	    					int req_having_p = req_memberVO.getHaving_p();
	    					//被請求會員點數
		    				int res_having_p = res_memberVO.getHaving_p();
		    				//被鎖定點數
		    				int pending_p = price;
		   					//鎖定點數加總
		   					total += price;
		   					//該會員本身點數扣除商品價格不足時
		   					if(req_having_p - price < 0){
	    						errorMessage.add("您的交易管理中已有相同的交易請求，但您的點數不足<br>"
				    							+ "請至<a href='"+req.getContextPath()+"/front/transaction/transactionCenter.jsp'>交易管理</a>查詢是否有這筆交易"
				    							+ "或請儲值點數後繼續交易");
			    				RequestDispatcher view = req.getRequestDispatcher("/front/cart/showCart.jsp");
			            		view.forward(req, res);
				            	return;
		    				} else if(res_having_p - price < 0){ //被請求會員點數扣除商品價格不足時
		    					
		    					//該會員點數扣除商品價格
		       					req_memberVO.setHaving_p(req_having_p - price);
		       					//該會員鎖定點數，先撈出原本被扣除點數
		        				req_memberVO.setPending_p(req_memberVO.getPending_p() + pending_p);
		        				//修改資料
		       					memberSvc.updateMemberByObject(req_memberVO);
		    					
		   						//交易訂單狀態改成3(點數鎖定中)
		    					tranVO.setStatus(3);
		   						tranVO.setRes_date(time);
		   						tranSvc.updateTranByObject(tranVO);
		    						
		   						error = true;
		    						
		    				} else {
		    					//該會員點數扣除商品價格
		        				req_memberVO.setHaving_p(req_having_p - price);
		        				//該會員鎖定點數，先撈出原本被扣除點數
		       					req_memberVO.setPending_p(req_memberVO.getPending_p() + pending_p);
		       					//被請求會員點數扣除商品價格
		       					res_memberVO.setHaving_p(res_having_p - price);
		       					//被請求會員鎖定點數，先撈出原本被扣除點數
		       					res_memberVO.setPending_p(res_memberVO.getPending_p() + pending_p);
		       					//修改資料
		       					memberSvc.updateMemberByObject(req_memberVO);
		       					memberSvc.updateMemberByObject(res_memberVO);
	        					//把當筆交易訂單更改狀態並加入回應時間																		
		            			tranVO.setStatus(1);
		            			tranVO.setRes_date(time);
		            			tranSvc.updateTranByObject(tranVO);
		        					
		        				List<TranVO> unreply = tranSvc.getUnreply();
		        					
		        				for(TranVO vo : unreply){
		       						if(vo.getReq_gid().equals(req_gid) || vo.getRes_gid().equals(req_gid)
		       						|| vo.getReq_gid().equals(res_gid) || vo.getRes_gid().equals(res_gid)){
		       							//所有交易訂單有關這兩項商品全部刪除
		       							vo.setStatus(2);
		       							tranSvc.updateTranByObject(vo);
		       						}
		       					}
		       					//新增派送兩筆資料
		       					new SendService().addSend(req_gid, req_member_id, tranVO.getTid());
		       					new SendService().addSend(res_gid, res_member_id, tranVO.getTid());
		       					//更改雙方商品狀態
		       					req_goodsVO.setGoods_status(3);
		       					res_goodsVO.setGoods_status(3);
		       					goodsSvc.updateGoodsByObject(req_goodsVO);
		       					goodsSvc.updateGoodsByObject(res_goodsVO);
		        				
		       					error = true;
		   					}
		    				cartList.remove(i);
		    				errorMessage.add("<h4 style=color:red>您的商品『"+goodsSvc.findGoodsByGid(tranVO.getRes_gid()).getG_name()+"』成功送出交易請求</h4><br>");
		       				
		    			} 
		   			}
		   		}
    		//取出已有交易成立的訂單(狀態是1的)，與換物車配對
			List<TranVO> tranFinal = tranSvc.getAllByMember_idFinal(req_member_id);
			for(TranVO tranVO : tranFinal){
			  	for(int i=0; i<cartList.size(); i++){
			  		String res_member_id = cartList.get(i).getRes_member_id();
		       		int res_gid = cartList.get(i).getRes_gid();
		       		int req_gid = cartList.get(i).getReq_gid();
		       		//換物車商品中沒有與交易訂單成立的商品可再加入交易訂單中
		       		if(tranVO.getRes_gid() != res_gid && tranVO.getReq_gid() != res_gid
		       		&& tranVO.getRes_gid() != req_gid && tranVO.getReq_gid() != req_gid){
		       			tranSvc.addTran(res_member_id, res_gid, req_member_id, req_gid);
		       		}
			   	}
			}
			   		
			
    		//新增結束把易物車清空
    		session.removeAttribute("cart");
    		if(error == true){
    			errorMessage.add("交易配對結果:對方也有相同的請求,交易成立!<br>已從擁有點數扣除"+total+"點至扣押點數,");
    			errorMessage.add("剩餘點數:"+(req_memberVO.getHaving_p())+"<br>");
    			errorMessage.add("請至<a href='"+req.getContextPath()+"/front/transaction/transactionCenter.jsp'>交易管理</a>查看此筆交易詳情<br>");
    		}
    		
    		errorMessage.add("所有商品已全部送出請求");
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
