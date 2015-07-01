package com.serial.controller;

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

import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.serial.model.SerialService;
import com.serial.model.SerialVO;

/**
 * Servlet implementation class serial
 */
@WebServlet("/Serial")
public class Serial extends HttpServlet {
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
				// 設定回應字元編碼
				res.setContentType("text/html ; charset=UTF-8");
				// 設定請求字元編碼
				req.setCharacterEncoding("UTF-8");
				String action = req.getParameter("action");
				PrintWriter out = res.getWriter();
				
				
				if("new_serial".equals(action))  {
					List<String> errorMsgs = new LinkedList<String>();
					req.setAttribute("errorMsgs", errorMsgs);
					/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
					Integer quantity=0;
					try{
					   quantity=Integer.valueOf(req.getParameter("quantity"));
					}catch(Exception e){
						errorMsgs.add("數量請輸入正確格式");
					}
					Integer money=Integer.valueOf(req.getParameter("money"));
					if (!errorMsgs.isEmpty()) {
						//req.setAttribute("messageVO", messageVO);
						RequestDispatcher failureView = req
								.getRequestDispatcher("/back/serial/addSerial.jsp");
						failureView.forward(req, res);
						return;// 程式中斷
					}
					/*************************** 2.開始新增資料 *****************************************/
					SerialService seriSvc=new SerialService();
					for(int i=0;i<quantity;i++){
						seriSvc.addSerial(money);
					}
					/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
					String url = "/back/serial/listAllSerial.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
					successView.forward(req, res);
				}
				
				if("use_serial".equals(action)){
					List<String> errorMsgs = new LinkedList<String>();
					req.setAttribute("errorMsgs", errorMsgs);
					/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
					String member_id=req.getParameter("member_id");
					String serial_number=req.getParameter("serial_number");
					if (serial_number == null || (serial_number.trim()).length() == 0) {
						errorMsgs.add("請輸入序號");
					}
					if (!errorMsgs.isEmpty()) {			
						RequestDispatcher failureView = req
								.getRequestDispatcher("/front/point/storePoint.jsp");
						failureView.forward(req, res);
						return;// 程式中斷
					}
					/*************************** 2.開始儲存點數 *****************************************/
					SerialService seSvc=new SerialService();
					MemberService memSvc=new MemberService();
					List<SerialVO> list=seSvc.getAll();
					Integer money=null;
					Boolean isMatch=false;
					//判斷有無序號
					for(int i=0;i<list.size();i++){
						SerialVO seVO=list.get(i);
						if(serial_number.equals(seVO.getSerial_number())){
							//有序號但是已經有使用者
							if(seVO.getMember_id()!=null){
								errorMsgs.add("此序號已被使用");
								isMatch=true;
								break;
							}
								//有序號且沒被使用
								else{
								money=seVO.getMoney();
								seSvc.updateSerial(serial_number, money, member_id);
								isMatch=true;
								//增加會員的點數
								MemberVO memVO=memSvc.getOneMemberByMemberId(member_id);
								memVO.setHaving_p(memVO.getHaving_p()+money);
								memSvc.updateMemberByObject(memVO);
								break;
							}
						}
					}
					//判斷完畢，沒有match到的序號
					if(isMatch==false){
						errorMsgs.add("序號輸入錯誤");
					}
					if (!errorMsgs.isEmpty()) {			
						RequestDispatcher failureView = req
								.getRequestDispatcher("/front/point/storePoint.jsp");
						failureView.forward(req, res);
						return;// 程式中斷
					}
					/*************************** 3.儲存完成,準備轉交(Send the Success view) ***********/
					req.setAttribute("money", money);
					System.out.println(money);
					String url = "/front/point/storePoint.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
					successView.forward(req, res);
					
				}
	
	}

}
