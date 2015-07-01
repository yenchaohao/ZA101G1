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
		
		/** �e�X�洫�ШD****************************************************/
		if("tranRequest".equals(action)){
			int SUCCESS = 1;  //���\�e�X�ШD
			int REQ_SUCCESS = 2; //���]���ۦP���ШD,�������,�����ШD���@���I�Ƥ���
			int RES_SUCCESS = 3; //���]���ۦP���ШD,�������
			int IS_REPEAT = -1;  //���ӫ~�w�g�e�X�ШD
			int LACK_POINT = -2; //����޲z���w���ۦP������ШD,�I�Ƥ���

			TranVO tranVO = gson.fromJson(req.getParameter("tranVO"),TranVO.class);
			//�ШD����
			int req_gid = tranVO.getReq_gid();
			String req_member_id = tranVO.getReq_member_id();
			//�^������
			int res_gid = tranVO.getRes_gid();
			String res_member_id = tranVO.getRes_member_id();
			//�ˬd�����~�O�_�w�g�洫��
			List<TranVO> tranVOList = new TranService().getOneByReqMember_id(req_member_id);
			for(TranVO check_tranVO : tranVOList){
				if(check_tranVO.getReq_gid() == req_gid && check_tranVO.getRes_gid() == res_gid && (check_tranVO.getStatus() == 0 || check_tranVO.getStatus() == 3)){
					outStr = String.valueOf(IS_REPEAT);  //���ӫ~�w�g�e�X�ШD
					out.print(outStr);
					out.close();
					return;
				}
			}		
			
			//�q�^���̪��b���h��o��^���̩ҵo�X���Ҧ��ШD
			List<TranVO> resTran = new TranService().getOneByReqMember_id(res_member_id);
			for(TranVO tran : resTran){
				//�p�G�o�Q�n���쪺�F�� �P �o��ϥΪ̭n�e�X�h�����~�O�@�˪� �M �o�n�e�X�h���F�� �P �o��ϥΪ̷Q���쪺�F��O�@�˪�(�����褬�e�ۦP�ШD��)
				if (tran.getRes_gid() == req_gid && tran.getReq_gid() == res_gid && tran.getStatus() == 0) {
					MemberVO req_memberVO = new MemberService().getOneMemberByMemberId(req_member_id);
					MemberVO res_memberVO = new MemberService().getOneMemberByMemberId(res_member_id);
					GoodsVO res_goodsVO = new GoodsService().findGoodsByGid(res_gid);
					GoodsVO req_goodsVO = new GoodsService().findGoodsByGid(req_gid);
					int pending = 0;
					// �P�_�ШD���ӫ~�P�Q�ШD���ӫ~���@�ӻ�������Q,���Q���Ȧs��pending
					if (req_goodsVO.getG_price() >= res_goodsVO.getG_price()) {
						pending = req_goodsVO.getG_price();
					} else {
						pending = res_goodsVO.getG_price();
					}
					
					//�p�G�ШD��(��e�X���H)�S������
					if(req_memberVO.getHaving_p() < pending){
						outStr = String.valueOf(LACK_POINT);  //����޲z���w���ۦP������ШD,�I�Ƥ���
						out.print(outStr);
						out.close();
						return;
						
					//�p�G�^���̨S������ ������A3
					} else if(res_memberVO.getHaving_p() < pending){
						//�����ШD�̪���
						req_memberVO.setHaving_p(req_memberVO.getHaving_p() - pending);
						req_memberVO.setPending_p(req_memberVO.getPending_p() + pending);
						new MemberService().updateMemberByObject(req_memberVO);
						tran.setStatus(3);
        				tran.setRes_date( new Timestamp(Calendar.getInstance().getTimeInMillis()));
        				new TranService().updateTranByObject(tran);
        				outStr = String.valueOf(REQ_SUCCESS);  //���]���ۦP���ШD,�������,�����ШD���@���I�Ƥ���
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
        				
        				//��ۦP������R��(��X�Ҧ�STATUS=0��tranVO)
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
        				
        				//�W�[�G�����e
              			new SendService().addSend(res_gid, res_member_id, tran.getTid());
        				new SendService().addSend(req_gid, req_member_id, tran.getTid());
        				//�ӫ~���A�]��
        				res_goodsVO.setGoods_status(3);
        				req_goodsVO.setGoods_status(3);
        				new GoodsService().updateGoodsByObject(res_goodsVO);
        				new GoodsService().updateGoodsByObject(req_goodsVO);
        				//�|������
        				new MemberService().updateMemberByObject(req_memberVO);
        				new MemberService().updateMemberByObject(res_memberVO);
        				
        				outStr = String.valueOf(RES_SUCCESS);  //���]���ۦP���ШD,�������
						out.print(outStr);
						out.close();
						return;
						
					}
				}
			}
			
			new TranService().addTran(res_member_id, res_gid, req_member_id, req_gid);
			outStr = String.valueOf(SUCCESS);  //���\�e�X�ШD
			out.print(outStr);
			out.close();
			return;
		}
		
		/** �d�ݧڰe�X���ШD ***********************************************************************/
		if("findMyReq".equals(action)){
			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			String member_id = req.getParameter("member_id");  //�ڪ�member_id
			/*************************** 2.��s������A *****************************************/
			TranService tranSvc = new TranService();
			List<TranVO> tranVOList = tranSvc.getOneByReqMember_idUn(member_id);  //�ڬO�ШD���쪺
			
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
		
		/** �d�ݧڭn�^�Ъ��ШD ***********************************************************************/
		if("findMyRes".equals(action)){
			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			String member_id = req.getParameter("member_id");  //�ڪ�member_id
			/*************************** 2.��s������A *****************************************/
			TranService tranSvc = new TranService();
			List<TranVO> tranVOList = tranSvc.getOneByResMember_idUn(member_id);  //�ڬOres�^�Ф��쪺
			
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
		
		/** �d�ݤw�g���ߪ���� ***********************************************************************/
		if("findMyTranFinal".equals(action)){
			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			String member_id = req.getParameter("member_id");  //�ڪ�member_id
			/*************************** 2.��s������A *****************************************/
			TranService tranSvc = new TranService();
			List<TranVO> tranVOList = tranSvc.getAllByMember_idFinal(member_id);
			
			List<TranAllBean> tranAllBeanList = new ArrayList<>();
			
			for(TranVO check_tranVO : tranVOList){
				String req_member_id = check_tranVO.getReq_member_id();  //�����o�M�D�誺member_id,�ΨӸ�ۤv��member_id���
				String my_member_id, other_member_id;
				int my_gid, other_gid;
				
				if(req_member_id.equals(member_id)){  					//�p�G�ڬO�ШD��
					my_member_id = check_tranVO.getReq_member_id();
					my_gid = check_tranVO.getReq_gid();					
					other_member_id = check_tranVO.getRes_member_id();
					other_gid = check_tranVO.getRes_gid();
				} else {												//�ڬO�^�_��
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
		
		
		/** �d�ݵ��ݤ������ ***********************************************************************/
		if("findMyTranWait".equals(action)){
			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			String member_id = req.getParameter("member_id");  //�ڪ�member_id
			/*************************** 2.��s������A *****************************************/
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
		
		
		
		
		/** �^�Х洫�ШD****************************************************/
		if ("check_tran".equals(action)){
			int REQ_SUCCESS = 2; //�������,�����ШD���@���I�Ƥ���
			int SUCCESS = 1;  	 //�������
			int LACK_POINT = -2; //�I�Ƥ���
			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			Integer tid = Integer.valueOf(req.getParameter("tid"));  //������ߪ�����s��
			String member_id = req.getParameter("member_id");
			
			MemberService memberSvc = new MemberService();
			MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
			TranService tranSvc = new TranService();
			if(memberVO.getHaving_p() < getMaxPrice(tranSvc.getOneTran(tid))){
				outStr = String.valueOf(LACK_POINT);  //�I�Ƥ���
				out.print(outStr);
				out.close();
				return;
			}
			
			//�P�_req�観�S���������I��
			String req_member_id=tranSvc.getOneTran(tid).getReq_member_id();
			MemberVO req_memVO = memberSvc.getOneMemberByMemberId(req_member_id);
			if(req_memVO.getHaving_p() < getMaxPrice(tranSvc.getOneTran(tid))){
				/*************************** 2.��s������A *****************************************/
				TranVO half_tranVO = tranSvc.getOneTran(tid);
				java.util.Date date = new java.util.Date();
				Timestamp time = new Timestamp(date.getTime());
				tranSvc.updateTran(half_tranVO.getRes_member_id(), half_tranVO.getRes_gid(), half_tranVO.getReq_member_id()
						, half_tranVO.getReq_gid(), time, 3, tid);
				/*************************** 4-2.����res�I�� *****************************************/
				//���o����̤j������	
				Integer pendPoint = getMaxPrice(half_tranVO);
				//�}�l�����I��(�W�[�����I�����A��ֲ{���I��)
				payPoint(pendPoint, half_tranVO.getRes_member_id());
				/*************************** 7-2.�ǳ����(Send the Success view) ***********/
				outStr = String.valueOf(REQ_SUCCESS);  //�������,���ШD���I�Ƥ���
				out.print(outStr);
				out.close();
				return;
			} else {
				/*************************** 2.��s������A *****************************************/
				TranVO succ_tranVO = tranSvc.getOneTran(tid);
				java.util.Date date = new java.util.Date();
				Timestamp time=new Timestamp(date.getTime());
				tranSvc.updateTran(succ_tranVO.getRes_member_id(), succ_tranVO.getRes_gid(), succ_tranVO.getReq_member_id()
						, succ_tranVO.getReq_gid(), time, 1, tid);
				/*************************** 3-1.�R����L�ШD *****************************************/
				List <TranVO> list = tranSvc.getUnreply();
				for(TranVO tranVO:list){
					//�P�@��res_member���M��A�򦨥\����ۦP���ӫ~���O����s�����P���A���A�令�������
					if( ((tranVO.getRes_gid().equals(succ_tranVO.getRes_gid())) && !(tranVO.getTid().equals(tid)))
							||(tranVO.getReq_gid().equals(succ_tranVO.getRes_gid())&& !(tranVO.getTid().equals(tid)))//res_member�����~ ��req���h��O�H����
							||(tranVO.getReq_gid().equals(succ_tranVO.getReq_gid())&& !(tranVO.getTid().equals(tid)))
							||(tranVO.getRes_gid().equals(succ_tranVO.getReq_gid())&& !(tranVO.getTid().equals(tid)))
							){			
						tranSvc.updateTran(tranVO.getRes_member_id(),tranVO.getRes_gid(), tranVO.getReq_member_id(),
								tranVO.getReq_gid(),time, 2,tranVO.getTid());

					}			
				}
				
				/*************************** 4-1.���������I�� *****************************************/
				//���o����̤j������	
				Integer pendPoint=getMaxPrice(succ_tranVO);
				//�}�l�����I��(�W�[�����I�����A��ֲ{���I��)
				payPoint(pendPoint, succ_tranVO.getRes_member_id());
				payPoint(pendPoint,succ_tranVO.getReq_member_id());
				/*************************** 5-1.�s�W���e�q��(2��) *****************************************/
				Integer res_gid=succ_tranVO.getRes_gid();
				Integer req_gid=succ_tranVO.getReq_gid();
				GoodsService goodsSvc=new GoodsService();
				GoodsVO res_good=goodsSvc.findGoodsByGid(res_gid);
				GoodsVO req_good=goodsSvc.findGoodsByGid(req_gid);
				SendService sendSvc=new SendService();
				sendSvc.addSend(res_gid, succ_tranVO.getRes_member_id(), tid);
				sendSvc.addSend(req_gid, succ_tranVO.getReq_member_id(), tid);
				/*************************** 6-1.����ӫ~�U�[*****************************************/
				res_good.setGoods_status(3);
				res_good.setQuitdate(time);
				req_good.setGoods_status(3);
				req_good.setQuitdate(time);
				goodsSvc.updateGoodsByObject(res_good);
				goodsSvc.updateGoodsByObject(req_good);
				/*************************** 7-1.�ǳ����(Send the Success view) ***********/
				outStr = String.valueOf(SUCCESS);
				out.print(outStr);
				out.close();
				return;
			}			
		}
		
		
		/** �A���^�Х洫�ШD****************************************************/
		if ("fillTran".equals(action)){
			int SUCCESS = 1;  	 //�������
			int LACK_POINT = -2; //�I�Ƥ���
			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			Integer tid = Integer.valueOf(req.getParameter("tid"));  //������ߪ�����s��
			String member_id = req.getParameter("member_id");
			
			MemberService memberSvc = new MemberService();
			MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
			TranService tranSvc = new TranService();
			if(memberVO.getHaving_p() < getMaxPrice(tranSvc.getOneTran(tid))){
				outStr = String.valueOf(LACK_POINT);  //�I�Ƥ���
				out.print(outStr);
				out.close();
				return;
			}
			
			/*************************** 2.��s������A *****************************************/
				TranVO succ_tranVO = tranSvc.getOneTran(tid);
				java.util.Date date = new java.util.Date();
				Timestamp time=new Timestamp(date.getTime());
				tranSvc.updateTran(succ_tranVO.getRes_member_id(), succ_tranVO.getRes_gid(), succ_tranVO.getReq_member_id()
						, succ_tranVO.getReq_gid(), time, 1, tid);
			/*************************** 3-1.�R����L�ШD *****************************************/
				List <TranVO> list = tranSvc.getUnreply();
				for(TranVO tranVO:list){
					//�P�@��res_member���M��A�򦨥\����ۦP���ӫ~���O����s�����P���A���A�令�������
					if( ((tranVO.getRes_gid().equals(succ_tranVO.getRes_gid())) && !(tranVO.getTid().equals(tid)))
							||(tranVO.getReq_gid().equals(succ_tranVO.getRes_gid())&& !(tranVO.getTid().equals(tid)))//res_member�����~ ��req���h��O�H����
							||(tranVO.getReq_gid().equals(succ_tranVO.getReq_gid())&& !(tranVO.getTid().equals(tid)))
							||(tranVO.getRes_gid().equals(succ_tranVO.getReq_gid())&& !(tranVO.getTid().equals(tid)))
							){			
						tranSvc.updateTran(tranVO.getRes_member_id(),tranVO.getRes_gid(), tranVO.getReq_member_id(),
								tranVO.getReq_gid(),time, 2,tranVO.getTid());
					}			
				}
				/*************************** 4-1.����REQ���I�� *****************************************/
				//���o����̤j������	
				Integer pendPoint=getMaxPrice(succ_tranVO);
				//�}�l�����I��(�W�[�����I�����A��ֲ{���I��)
				payPoint(pendPoint,succ_tranVO.getReq_member_id());
				/*************************** 5-1.�s�W���e�q��(2��) *****************************************/
				Integer res_gid=succ_tranVO.getRes_gid();
				Integer req_gid=succ_tranVO.getReq_gid();
				GoodsService goodsSvc=new GoodsService();
				GoodsVO res_good=goodsSvc.findGoodsByGid(res_gid);
				GoodsVO req_good=goodsSvc.findGoodsByGid(req_gid);
				SendService sendSvc=new SendService();
				sendSvc.addSend(res_gid, succ_tranVO.getRes_member_id(), tid);
				sendSvc.addSend(req_gid, succ_tranVO.getReq_member_id(), tid);
				/*************************** 6-1.����ӫ~�U�[*****************************************/
				res_good.setGoods_status(3);
				res_good.setQuitdate(time);
				req_good.setGoods_status(3);
				req_good.setQuitdate(time);
				goodsSvc.updateGoodsByObject(res_good);
				goodsSvc.updateGoodsByObject(req_good);
				/*************************** 7-1.�ǳ����(Send the Success view) ***********/
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
