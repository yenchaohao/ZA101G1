package com.android.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.model.SendAndMemberBean;
import com.credit.model.CreditService;
import com.credit.model.CreditVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.send.model.SendDAO;
import com.send.model.SendService;
import com.send.model.SendVO;
import com.send.model.SendVOAssociations;
import com.tool.MailService;
import com.tran.model.TranService;
import com.tran.model.TranVO;

public class SendServlet extends HttpServlet implements Runnable {
	
	
	// 會呼叫MailService寄信的背景執行序
	private Thread mail_service;
	// 存地址的List
	private List<String> toList = new ArrayList<>();
	// 存主題的List
	private List<String> subjectList = new ArrayList<>();
	// 存內容的List
	private List<String> contentList = new ArrayList<>();

	// 初始化執行續
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		mail_service = new Thread(this);
		mail_service.setPriority(Thread.MIN_PRIORITY);
	}
	
	
	
	private void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String outStr = "";
		String action = req.getParameter("action");
		
		
		/** 來自TranActivity 依tid取得2筆sendVO *******************/
		if("findByTid".equals(action)){
			SendService sendSvc = new SendService();
			int tid = Integer.valueOf(req.getParameter("tid"));
			List<SendVO> sendVOList = sendSvc.getSendByTid(tid);
			
			outStr = gson.toJson(sendVOList);
			out.print(outStr);
			out.close();
		}
	
		/**目前未用 ---- 來自SendActivity 取得所有派送清單*******************/
		if("getAll".equals(action)){
			SendService sendSvc = new SendService();
			List<SendVO> sendVOList = sendSvc.getAll();
			
			outStr = gson.toJson(sendVOList);
			out.print(outStr);
			out.close();
		}
		
		/** 來自SendActivity 依派送狀態取得派送清單*************************/
		if("getAllByStatus".equals(action)){
			SendService sendSvc = new SendService();
			MemberService memberSvc = new MemberService();
			
			int status = Integer.valueOf(req.getParameter("status"));
			List<SendVO> sendVOList = sendSvc.getAllByStatus(status);
			List<SendAndMemberBean> sendAndMemberBeanList = new ArrayList<>();
			
			for(SendVO sendVO : sendVOList){
				String member_id = sendVO.getMember_id();
				MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
				sendAndMemberBeanList.add(new SendAndMemberBean(sendVO, memberVO));
			}
			
			outStr = gson.toJson(sendAndMemberBeanList);
			out.print(outStr);
			out.close();
		}

		/** 來自 SendActivity 掃描QRcode後 ********************************************/
		if("update".equals(action)){
			final int ADD_CREDIT = 2;  //雙方均送達且新增了一筆Credit資料,updateCount = 2
			final int FAIL_CREDIT = -1;  //雙方均送達但新增Credit資料失敗,updateCount = -1
			final int REPEAT = -2; //此筆sendVO已經被刷過status = 2
			int updateCount = 0;
			
			SendDAO dao = new SendDAO();
			SendVO sendVO = dao.findBySid(Integer.valueOf(req.getParameter("sid")));	
			
			//只要抓出來的這筆sendVO 的 status = 2就直接攔截轉交 
			if(sendVO.getStatus() == null || sendVO.getStatus() == 2){
				updateCount = REPEAT;
				outStr = String.valueOf(updateCount);
				out.print(outStr);
				out.close();
				return;
			}
			
			sendVO.setEnd_date((new Timestamp(new java.util.Date().getTime())));
			sendVO.setStatus(2);
			
			updateCount = dao.update(sendVO);  //只有一方送達,updateCount = 1
	
			//用掃描到的這個sendVO取出訂單編號(tid)去資料庫比對			
			int tid = sendVO.getTid();
			//未派送的 status = 1 
			List<SendVO> sendVOListUn = dao.findByTidUn(tid);
			//派送完成的 status = 2 
			List<SendVO> sendVOListFinal = dao.findByTidFinal(tid);
				
			//之後要用來insert評價的判斷
			if(sendVOListFinal.size() == 2 && sendVOListUn.size() == 0){
				TranService tranSvc = new TranService();
				TranVO tranVO = tranSvc.getOneTran(tid);
				String memberA_id = tranVO.getReq_member_id();
				String memberB_id = tranVO.getRes_member_id();
				int status = 0;
				CreditService creditSvc = new CreditService();
				CreditVO creditVO = creditSvc.addCredit(memberA_id, memberB_id, status, tid);
				if(creditVO != null){
					updateCount = ADD_CREDIT;  //雙方均送達且新增了一筆Credit資料,updateCount = 2
					
					// 取得本機LOCALHOST
					InetAddress address = InetAddress.getLocalHost();
					//寄出信件
					MemberService memberSvc = new MemberService();
					MemberVO memberVO_A = memberSvc.getOneMemberByMemberId(memberA_id);
					MemberVO memberVO_B = memberSvc.getOneMemberByMemberId(memberB_id);
					
					toList.add(memberVO_A.getEmail());
					toList.add(memberVO_B.getEmail());
					
//					toList.add("denevrove@gmail.com");
//					toList.add("reg368@gmail.com");
					
					subjectList.add("<二手交易所>此次交易已完成");
					subjectList.add("<二手交易所>此次交易已完成");
					
					StringBuffer messageTextA = new StringBuffer();
					messageTextA.append("親愛的會員你好 \n");
					messageTextA.append("此次交易已完成\n");
					messageTextA.append("請至:http://" + address.getHostAddress() + ":" + req.getServerPort()
							+ "/ZA101G1/front/index/index.jsp \n");
					messageTextA.append("替對方評價,以便系統歸還點數,謝謝 \n");
					
					StringBuffer messageTextB = new StringBuffer();
					messageTextB.append("親愛的的會員你好 \n");
					messageTextB.append("此次交易已完成\n");
					messageTextB.append("請至:http://" + address.getHostAddress() + ":" + req.getServerPort()
							+ "/ZA101G1/front/index/index.jsp \n");
					messageTextB.append("替對方評價,以便系統歸還點數,謝謝 \n");

					contentList.add(messageTextA.toString());
					contentList.add(messageTextB.toString());
					
					mail_service.start();
					
				} else {
					updateCount = FAIL_CREDIT;  //雙方均送達但新增Credit資料失敗,updateCount = -1
				}			
			} 
			
			// 啟動mail_service執行序 (會執行此類別 run method 裡面的程式碼)
			
			
			outStr = String.valueOf(updateCount);
			out.print(outStr);
			out.close();
			

		}
		
	}
	
	// mailService.start()執行...
	@Override
	public void run() {
		// TODO Auto-generated method stub
		MailService mailservice = new MailService();
		// 依照存入的會員地址大小去判定 要送幾封信
		for (int i = 0; i < toList.size(); i++) {
			System.out.print(toList.get(i));
			mailservice.sendMail(toList.get(i), subjectList.get(i),
					contentList.get(i));
			// 送信會有點累格 為了避免請求重復送出 執行完送信的方法後 停個2秒鐘等待信件寄送完成
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.print("ERROR");
			}
			// 因為執行序無法重復使用,所以為了要讓使用者能夠一直發送信件,在執行序完成時,再重新生成一次
		}
		mail_service = new Thread(this);
		mail_service.setPriority(Thread.MIN_PRIORITY);
		System.out.println("done");
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
