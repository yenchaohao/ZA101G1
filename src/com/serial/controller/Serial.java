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
				// �]�w�^���r���s�X
				res.setContentType("text/html ; charset=UTF-8");
				// �]�w�ШD�r���s�X
				req.setCharacterEncoding("UTF-8");
				String action = req.getParameter("action");
				PrintWriter out = res.getWriter();
				
				
				if("new_serial".equals(action))  {
					List<String> errorMsgs = new LinkedList<String>();
					req.setAttribute("errorMsgs", errorMsgs);
					/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
					Integer quantity=0;
					try{
					   quantity=Integer.valueOf(req.getParameter("quantity"));
					}catch(Exception e){
						errorMsgs.add("�ƶq�п�J���T�榡");
					}
					Integer money=Integer.valueOf(req.getParameter("money"));
					if (!errorMsgs.isEmpty()) {
						//req.setAttribute("messageVO", messageVO);
						RequestDispatcher failureView = req
								.getRequestDispatcher("/back/serial/addSerial.jsp");
						failureView.forward(req, res);
						return;// �{�����_
					}
					/*************************** 2.�}�l�s�W��� *****************************************/
					SerialService seriSvc=new SerialService();
					for(int i=0;i<quantity;i++){
						seriSvc.addSerial(money);
					}
					/*************************** 3.�s�W����,�ǳ����(Send the Success view) ***********/
					String url = "/back/serial/listAllSerial.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
					successView.forward(req, res);
				}
				
				if("use_serial".equals(action)){
					List<String> errorMsgs = new LinkedList<String>();
					req.setAttribute("errorMsgs", errorMsgs);
					/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
					String member_id=req.getParameter("member_id");
					String serial_number=req.getParameter("serial_number");
					if (serial_number == null || (serial_number.trim()).length() == 0) {
						errorMsgs.add("�п�J�Ǹ�");
					}
					if (!errorMsgs.isEmpty()) {			
						RequestDispatcher failureView = req
								.getRequestDispatcher("/front/point/storePoint.jsp");
						failureView.forward(req, res);
						return;// �{�����_
					}
					/*************************** 2.�}�l�x�s�I�� *****************************************/
					SerialService seSvc=new SerialService();
					MemberService memSvc=new MemberService();
					List<SerialVO> list=seSvc.getAll();
					Integer money=null;
					Boolean isMatch=false;
					//�P�_���L�Ǹ�
					for(int i=0;i<list.size();i++){
						SerialVO seVO=list.get(i);
						if(serial_number.equals(seVO.getSerial_number())){
							//���Ǹ����O�w�g���ϥΪ�
							if(seVO.getMember_id()!=null){
								errorMsgs.add("���Ǹ��w�Q�ϥ�");
								isMatch=true;
								break;
							}
								//���Ǹ��B�S�Q�ϥ�
								else{
								money=seVO.getMoney();
								seSvc.updateSerial(serial_number, money, member_id);
								isMatch=true;
								//�W�[�|�����I��
								MemberVO memVO=memSvc.getOneMemberByMemberId(member_id);
								memVO.setHaving_p(memVO.getHaving_p()+money);
								memSvc.updateMemberByObject(memVO);
								break;
							}
						}
					}
					//�P�_�����A�S��match�쪺�Ǹ�
					if(isMatch==false){
						errorMsgs.add("�Ǹ���J���~");
					}
					if (!errorMsgs.isEmpty()) {			
						RequestDispatcher failureView = req
								.getRequestDispatcher("/front/point/storePoint.jsp");
						failureView.forward(req, res);
						return;// �{�����_
					}
					/*************************** 3.�x�s����,�ǳ����(Send the Success view) ***********/
					req.setAttribute("money", money);
					System.out.println(money);
					String url = "/front/point/storePoint.jsp";
					RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
					successView.forward(req, res);
					
				}
	
	}

}
