package com.android.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.model.GoodsAndMemberAndImgBean;
import com.android.model.TranAllBean;
import com.android.model.TranAllDataBean;
import com.favorite.model.FavoriteDAO;
import com.favorite.model.FavoriteService;
import com.favorite.model.FavoriteVO;
import com.favorite.model.Favorite_interface;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.send.model.SendService;
import com.tran.model.TranService;
import com.tran.model.TranVO;

public class TranServlet extends HttpServlet {
	private void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String outStr = "";
		String action = req.getParameter("action");
		
		/** 送出交換請求****************************************************/
		if("tranRequest".equals(action)){
			int SUCCESS = 1;  //成功送出請求
			int REQ_SUCCESS = 2; //對方也有相同的請求,交易成立,但先請求的一方點數不足
			int RES_SUCCESS = 3; //對方也有相同的請求,交易成立
			int IS_REPEAT = -1;  //此商品已經送出請求
			int LACK_POINT = -2; //交易管理中已有相同的交易請求,點數不足

			TranVO tranVO = gson.fromJson(req.getParameter("tranVO"),TranVO.class);
			//請求方資料
			int req_gid = tranVO.getReq_gid();
			String req_member_id = tranVO.getReq_member_id();
			//回應方資料
			int res_gid = tranVO.getRes_gid();
			String res_member_id = tranVO.getRes_member_id();
			//檢查此物品是否已經交換中
			List<TranVO> tranVOList = new TranService().getOneByReqMember_id(req_member_id);
			for(TranVO check_tranVO : tranVOList){
				if(check_tranVO.getReq_gid() == req_gid && check_tranVO.getRes_gid() == res_gid && (check_tranVO.getStatus() == 0 || check_tranVO.getStatus() == 3)){
					outStr = String.valueOf(IS_REPEAT);  //此商品已經送出請求
					out.print(outStr);
					out.close();
					return;
				}
			}		
			
			//從回應者的帳號去找這位回應者所發出的所有請求
			List<TranVO> resTran = new TranService().getOneByReqMember_id(res_member_id);
			for(TranVO tran : resTran){
				//如果她想要換到的東西 與 這位使用者要送出去的物品是一樣的 和 她要送出去的東西 與 這位使用者想換到的東西是一樣的(當雙方互送相同請求時)
				if (tran.getRes_gid() == req_gid && tran.getReq_gid() == res_gid && tran.getStatus() == 0) {
					MemberVO req_memberVO = new MemberService().getOneMemberByMemberId(req_member_id);
					MemberVO res_memberVO = new MemberService().getOneMemberByMemberId(res_member_id);
					GoodsVO res_goodsVO = new GoodsService().findGoodsByGid(res_gid);
					GoodsVO req_goodsVO = new GoodsService().findGoodsByGid(req_gid);
					int pending = 0;
					// 判斷請求的商品與被請求的商品哪一個價錢比較貴,取貴的值存到pending
					if (req_goodsVO.getG_price() >= res_goodsVO.getG_price()) {
						pending = req_goodsVO.getG_price();
					} else {
						pending = res_goodsVO.getG_price();
					}
					
					//如果請求者(後送出的人)沒錢的話
					if(req_memberVO.getHaving_p() < pending){
						outStr = String.valueOf(LACK_POINT);  //交易管理中已有相同的交易請求,點數不足
						out.print(outStr);
						out.close();
						return;
						
					//如果回應者沒錢的話 交易狀態3
					} else if(res_memberVO.getHaving_p() < pending){
						//先扣請求者的錢
						req_memberVO.setHaving_p(req_memberVO.getHaving_p() - pending);
						req_memberVO.setPending_p(req_memberVO.getPending_p() + pending);
						new MemberService().updateMemberByObject(req_memberVO);
						tran.setStatus(3);
        				tran.setRes_date( new Timestamp(Calendar.getInstance().getTimeInMillis()));
        				new TranService().updateTranByObject(tran);
        				outStr = String.valueOf(REQ_SUCCESS);  //對方也有相同的請求,交易成立,但先請求的一方點數不足
						out.print(outStr);
						out.close();
						return;
					} else {
						req_memberVO.setHaving_p(req_memberVO.getHaving_p() - pending);
						req_memberVO.setPending_p(req_memberVO.getPending_p() + pending);
						res_memberVO.setHaving_p(res_memberVO.getHaving_p() - pending);
						res_memberVO.setPending_p(res_memberVO.getPending_p() + pending);
        				tran.setStatus(1);
        				new TranService().updateTranByObject(tran);
        				
        				//把相同交易的刪掉(找出所有STATUS=0的tranVO)
        				List<TranVO> unreply = new TranService().getUnreply();
        				
        				for(TranVO vo : unreply){
        					if(vo.getReq_gid() == res_gid || vo.getReq_gid() == res_gid || 
        						vo.getReq_gid() == req_gid || vo.getReq_gid() == req_gid
        						|| vo.getRes_gid() == res_gid || vo.getRes_gid() == res_gid || 
        						vo.getRes_gid() == req_gid || vo.getRes_gid() == req_gid
        							){
        						vo.setStatus(2);
        						new TranService().updateTranByObject(vo);
        					}
        				}
        				
        				//增加二筆派送
              			new SendService().addSend(res_gid, res_member_id, tran.getTid());
        				new SendService().addSend(req_gid, req_member_id, tran.getTid());
        				//商品狀態也改
        				res_goodsVO.setGoods_status(3);
        				req_goodsVO.setGoods_status(3);
        				new GoodsService().updateGoodsByObject(res_goodsVO);
        				new GoodsService().updateGoodsByObject(req_goodsVO);
        				//會員扣錢
        				new MemberService().updateMemberByObject(req_memberVO);
        				new MemberService().updateMemberByObject(res_memberVO);
        				
        				outStr = String.valueOf(RES_SUCCESS);  //對方也有相同的請求,交易成立
						out.print(outStr);
						out.close();
						return;
						
					}
				}
			}
			
			new TranService().addTran(res_member_id, res_gid, req_member_id, req_gid);
			outStr = String.valueOf(SUCCESS);  //成功送出請求
			out.print(outStr);
			out.close();
			return;
		}
		
		/** 查看我送出的請求 ***********************************************************************/
		if("findMyReq".equals(action)){
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			String member_id = req.getParameter("member_id");  //我的member_id
			/*************************** 2.更新交易狀態 *****************************************/
			TranService tranSvc = new TranService();
			List<TranVO> tranVOList = tranSvc.getOneByReqMember_idUn(member_id);  //我是請求方找到的
			
			List<TranAllBean> tranAllBeanList = new ArrayList<>();
			
			for(TranVO check_tranVO : tranVOList){
				String req_member_id = check_tranVO.getReq_member_id();
				int req_gid = check_tranVO.getReq_gid();
				MemberVO req_memberVO = new MemberService().getOneMemberByMemberId(req_member_id);
				GoodsVO req_goodsVO = new GoodsService().findGoodsByGid(req_gid);
				
				String res_member_id = check_tranVO.getRes_member_id();
				int res_gid = check_tranVO.getRes_gid();
				MemberVO res_memberVO = new MemberService().getOneMemberByMemberId(res_member_id);
				GoodsVO res_goodsVO = new GoodsService().findGoodsByGid(res_gid);
				
				TranAllBean tranAllBean = new TranAllBean(check_tranVO, req_goodsVO, req_memberVO, res_goodsVO, res_memberVO);
				tranAllBeanList.add(tranAllBean);
			}
					
			outStr = gson.toJson(tranAllBeanList);		
			out.println(outStr);
			out.close();

		}
		
		/** 查看我要回覆的請求 ***********************************************************************/
		if("findMyRes".equals(action)){
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			String member_id = req.getParameter("member_id");  //我的member_id
			/*************************** 2.更新交易狀態 *****************************************/
			TranService tranSvc = new TranService();
			List<TranVO> tranVOList = tranSvc.getOneByResMember_idUn(member_id);  //我是res回覆方找到的
			
			List<TranAllBean> tranAllBeanList = new ArrayList<>();
			
			for(TranVO check_tranVO : tranVOList){
				String req_member_id = check_tranVO.getReq_member_id();
				int req_gid = check_tranVO.getReq_gid();
				MemberVO req_memberVO = new MemberService().getOneMemberByMemberId(req_member_id);
				GoodsVO req_goodsVO = new GoodsService().findGoodsByGid(req_gid);
				
				String res_member_id = check_tranVO.getRes_member_id();
				int res_gid = check_tranVO.getRes_gid();
				MemberVO res_memberVO = new MemberService().getOneMemberByMemberId(res_member_id);
				GoodsVO res_goodsVO = new GoodsService().findGoodsByGid(res_gid);
				
				TranAllBean tranAllBean = new TranAllBean(check_tranVO, req_goodsVO, req_memberVO, res_goodsVO, res_memberVO);
				tranAllBeanList.add(tranAllBean);
			}
					
			outStr = gson.toJson(tranAllBeanList);		
			out.println(outStr);
			out.close();
		}
		
		/** 查看已經成立的交易 ***********************************************************************/
		if("findMyTranFinal".equals(action)){
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			String member_id = req.getParameter("member_id");  //我的member_id
			/*************************** 2.更新交易狀態 *****************************************/
			TranService tranSvc = new TranService();
			List<TranVO> tranVOList = tranSvc.getAllByMember_idFinal(member_id);
			
			List<TranAllBean> tranAllBeanList = new ArrayList<>();
			
			for(TranVO check_tranVO : tranVOList){
				String req_member_id = check_tranVO.getReq_member_id();  //先取得清求方的member_id,用來跟自己的member_id比對
				String my_member_id, other_member_id;
				int my_gid, other_gid;
				
				if(req_member_id.equals(member_id)){  					//如果我是請求方
					my_member_id = check_tranVO.getReq_member_id();
					my_gid = check_tranVO.getReq_gid();					
					other_member_id = check_tranVO.getRes_member_id();
					other_gid = check_tranVO.getRes_gid();
				} else {												//我是回復方
					my_member_id = check_tranVO.getRes_member_id();
					my_gid = check_tranVO.getRes_gid();									
					other_member_id = check_tranVO.getReq_member_id();
					other_gid = check_tranVO.getReq_gid();				
				}
				
				MemberVO my_memberVO = new MemberService().getOneMemberByMemberId(my_member_id);
				GoodsVO my_goodsVO = new GoodsService().findGoodsByGid(my_gid);				
				MemberVO other_memberVO = new MemberService().getOneMemberByMemberId(other_member_id);
				GoodsVO other_goodsVO = new GoodsService().findGoodsByGid(other_gid);
				
				TranAllBean tranAllBean = new TranAllBean(check_tranVO, my_goodsVO, my_memberVO, other_goodsVO, other_memberVO);
				tranAllBeanList.add(tranAllBean);
			}
					
			outStr = gson.toJson(tranAllBeanList);		
			out.println(outStr);
			out.close();
		}
		
		
		/** 查看等待中的交易 ***********************************************************************/
		if("findMyTranWait".equals(action)){
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			String member_id = req.getParameter("member_id");  //我的member_id
			/*************************** 2.更新交易狀態 *****************************************/
			TranService tranSvc = new TranService();
			List<TranVO> tranVOList = tranSvc.getAllByMember_idWait(member_id);
			
			List<TranAllBean> tranAllBeanList = new ArrayList<>();
			
			for(TranVO check_tranVO : tranVOList){
				String req_member_id = check_tranVO.getReq_member_id();
				int req_gid = check_tranVO.getReq_gid();
				MemberVO req_memberVO = new MemberService().getOneMemberByMemberId(req_member_id);
				GoodsVO req_goodsVO = new GoodsService().findGoodsByGid(req_gid);
				
				String res_member_id = check_tranVO.getRes_member_id();
				int res_gid = check_tranVO.getRes_gid();
				MemberVO res_memberVO = new MemberService().getOneMemberByMemberId(res_member_id);
				GoodsVO res_goodsVO = new GoodsService().findGoodsByGid(res_gid);
				
				TranAllBean tranAllBean = new TranAllBean(check_tranVO, req_goodsVO, req_memberVO, res_goodsVO, res_memberVO);
				tranAllBeanList.add(tranAllBean);
			}
					
			outStr = gson.toJson(tranAllBeanList);		
			out.println(outStr);
			out.close();
		}
		
		
		
		
		/** 回覆交換請求****************************************************/
		if ("check_tran".equals(action)){
			int REQ_SUCCESS = 2; //交易成立,但先請求的一方點數不足
			int SUCCESS = 1;  	 //交易成立
			int LACK_POINT = -2; //點數不足
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			Integer tid = Integer.valueOf(req.getParameter("tid"));  //交易成立的交易編號
			String member_id = req.getParameter("member_id");
			
			MemberService memberSvc = new MemberService();
			MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
			TranService tranSvc = new TranService();
			if(memberVO.getHaving_p() < getMaxPrice(tranSvc.getOneTran(tid))){
				outStr = String.valueOf(LACK_POINT);  //點數不足
				out.print(outStr);
				out.close();
				return;
			}
			
			//判斷req方有沒有足夠的點數
			String req_member_id=tranSvc.getOneTran(tid).getReq_member_id();
			MemberVO req_memVO = memberSvc.getOneMemberByMemberId(req_member_id);
			if(req_memVO.getHaving_p() < getMaxPrice(tranSvc.getOneTran(tid))){
				/*************************** 2.更新交易狀態 *****************************************/
				TranVO half_tranVO = tranSvc.getOneTran(tid);
				java.util.Date date = new java.util.Date();
				Timestamp time = new Timestamp(date.getTime());
				tranSvc.updateTran(half_tranVO.getRes_member_id(), half_tranVO.getRes_gid(), half_tranVO.getReq_member_id()
						, half_tranVO.getReq_gid(), time, 3, tid);
				/*************************** 4-2.扣押res點數 *****************************************/
				//取得雙方最大的價錢	
				Integer pendPoint = getMaxPrice(half_tranVO);
				//開始扣押點數(增加扣除點數欄位，減少現有點數)
				payPoint(pendPoint, half_tranVO.getRes_member_id());
				/*************************** 7-2.準備轉交(Send the Success view) ***********/
				outStr = String.valueOf(REQ_SUCCESS);  //交易成立,但請求方點數不足
				out.print(outStr);
				out.close();
				return;
			} else {
				/*************************** 2.更新交易狀態 *****************************************/
				TranVO succ_tranVO = tranSvc.getOneTran(tid);
				java.util.Date date = new java.util.Date();
				Timestamp time=new Timestamp(date.getTime());
				tranSvc.updateTran(succ_tranVO.getRes_member_id(), succ_tranVO.getRes_gid(), succ_tranVO.getReq_member_id()
						, succ_tranVO.getReq_gid(), time, 1, tid);
				/*************************** 3-1.刪除其他請求 *****************************************/
				List <TranVO> list = tranSvc.getUnreply();
				for(TranVO tranVO:list){
					//同一個res_member的清單，跟成功交易相同的商品但是交易編號不同的，狀態改成交易失敗
					if( ((tranVO.getRes_gid().equals(succ_tranVO.getRes_gid())) && !(tranVO.getTid().equals(tid)))
							||(tranVO.getReq_gid().equals(succ_tranVO.getRes_gid())&& !(tranVO.getTid().equals(tid)))//res_member的物品 當成req拿去跟別人換的
							||(tranVO.getReq_gid().equals(succ_tranVO.getReq_gid())&& !(tranVO.getTid().equals(tid)))
							||(tranVO.getRes_gid().equals(succ_tranVO.getReq_gid())&& !(tranVO.getTid().equals(tid)))
							){			
						tranSvc.updateTran(tranVO.getRes_member_id(),tranVO.getRes_gid(), tranVO.getReq_member_id(),
								tranVO.getReq_gid(),time, 2,tranVO.getTid());

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
				outStr = String.valueOf(SUCCESS);
				out.print(outStr);
				out.close();
				return;
			}			
		}
		
		
		/** 再次回覆交換請求****************************************************/
		if ("fillTran".equals(action)){
			int SUCCESS = 1;  	 //交易成立
			int LACK_POINT = -2; //點數不足
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			Integer tid = Integer.valueOf(req.getParameter("tid"));  //交易成立的交易編號
			String member_id = req.getParameter("member_id");
			
			MemberService memberSvc = new MemberService();
			MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
			TranService tranSvc = new TranService();
			if(memberVO.getHaving_p() < getMaxPrice(tranSvc.getOneTran(tid))){
				outStr = String.valueOf(LACK_POINT);  //點數不足
				out.print(outStr);
				out.close();
				return;
			}
			
			/*************************** 2.更新交易狀態 *****************************************/
				TranVO succ_tranVO = tranSvc.getOneTran(tid);
				java.util.Date date = new java.util.Date();
				Timestamp time=new Timestamp(date.getTime());
				tranSvc.updateTran(succ_tranVO.getRes_member_id(), succ_tranVO.getRes_gid(), succ_tranVO.getReq_member_id()
						, succ_tranVO.getReq_gid(), time, 1, tid);
			/*************************** 3-1.刪除其他請求 *****************************************/
				List <TranVO> list = tranSvc.getUnreply();
				for(TranVO tranVO:list){
					//同一個res_member的清單，跟成功交易相同的商品但是交易編號不同的，狀態改成交易失敗
					if( ((tranVO.getRes_gid().equals(succ_tranVO.getRes_gid())) && !(tranVO.getTid().equals(tid)))
							||(tranVO.getReq_gid().equals(succ_tranVO.getRes_gid())&& !(tranVO.getTid().equals(tid)))//res_member的物品 當成req拿去跟別人換的
							||(tranVO.getReq_gid().equals(succ_tranVO.getReq_gid())&& !(tranVO.getTid().equals(tid)))
							||(tranVO.getRes_gid().equals(succ_tranVO.getReq_gid())&& !(tranVO.getTid().equals(tid)))
							){			
						tranSvc.updateTran(tranVO.getRes_member_id(),tranVO.getRes_gid(), tranVO.getReq_member_id(),
								tranVO.getReq_gid(),time, 2,tranVO.getTid());
					}			
				}
				/*************************** 4-1.扣押REQ方點數 *****************************************/
				//取得雙方最大的價錢	
				Integer pendPoint=getMaxPrice(succ_tranVO);
				//開始扣押點數(增加扣除點數欄位，減少現有點數)
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
				outStr = String.valueOf(SUCCESS);
				out.print(outStr);
				out.close();
				return;		
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
	
	
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		process(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		process(req, res);
	}
}
