package com.mail.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mail.model.*;
import com.member.model.*;

@WebServlet("/Mail")
public class Mail extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html ; charset=UTF-8");
		// �]�w�ШD�r���s�X
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		if ("addQuestion".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			String title=req.getParameter("title");
			String question=req.getParameter("question");
//			String title=new String(tit.getBytes("ISO-8859-1"),"UTF-8");
//			String question=new String(ques.getBytes("ISO-8859-1"),"UTF-8");
			String member_id=req.getParameter("member_id");
			
			if(question==null||question.trim().length()==0){
				errorMsgs.add("�п�J���D");
			}
			if(title.equals("�п��")){
				errorMsgs.add("�п�ܰ��D����");		
			}
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/customerCenter/contact.jsp");
				failureView.forward(req, res);
				return;// �{�����_
			}
			/*************************** 2.�}�l�s�W��� *****************************************/
			
			MailService mailSvc=new MailService();
			mailSvc.addMail(member_id,  title, question);
			/*************************** 3.�s�W����,�ǳ����(Send the Success view) ***********/
			String url = "/front/index/index.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
			successView.forward(req, res);
		}
		if ("answerMail".equals(action)){
			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			Integer cmid=Integer.valueOf(req.getParameter("cmid"));
			/*************************** 2.�}�l���o��� *****************************************/
			MailService mailSvc=new MailService();
			MailVO mailVO=mailSvc.getOneMail(cmid);
			/*************************** 3.�ǳ����(Send the Success view) ***********/
			req.setAttribute("mailVO", mailVO);
			String url = "/back/mail/answerMail.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
			successView.forward(req, res);
		}
		
		if ("answered".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			String answer=req.getParameter("answer");
			String empid=req.getParameter("empid");
			Integer cmid=Integer.valueOf(req.getParameter("cmid"));
			MailService mailSvc=new MailService();
			MailVO mailVO=mailSvc.getOneMail(cmid);
			if(answer.trim().length()==0){
				errorMsgs.add("�п�J�^�Ф��e");
			}
			
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("mailVO", mailVO);
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/mail/answerMail.jsp");
				failureView.forward(req, res);
				return;// �{�����_
			}
			/*************************** 2.�}�l��s��� *****************************************/
			mailSvc.updateMail(mailVO.getMember_id(), empid, mailVO.getTitle(), mailVO.getQuestion(), answer, 1, cmid);
			/*************************** 3.�H�e�^�� *****************************************/
			MemberService memSvc=new MemberService();
			MemberVO memVO=memSvc.getOneMemberByMemberId(mailVO.getMember_id());
			 String to = memVO.getEmail();
		     
		     String subject = "EX-CHANGE���D�^��";

		     String messageText = "���A�����D:"+mailVO.getQuestion()+"\n��H�U�^��:\n"+answer+"\n\n\n�P�±z�Q�Υ�����"; 
		     sendMail(to, subject, messageText);
			/*************************** 4.²�T�q�� *****************************************/
			Send se = new Send();	
		 	String[] tel ={memVO.getTel().replace("-","")};
		 	String message =memVO.getMem_name()+"�z�n:�z�󥻺���EX-CHANGE�����D�w�g�^�_�A�q�Ьd���z��email";
		 	se.sendMessage(tel , message);
			/*************************** 5.�ǳ����(Send the Success view) ***********/
			
			String url = "/back/mail/checkMail.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
			successView.forward(req, res);
		}
	}
	public void sendMail(String to, String subject, String messageText) {
		
		   try {
			   // �]�w�ϥ�SSL�s�u�� Gmail smtp Server
			   Properties props = new Properties();
			   props.put("mail.smtp.host", "smtp.gmail.com");
			   props.put("mail.smtp.socketFactory.port", "465");
			   props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			   props.put("mail.smtp.auth", "true");
			   props.put("mail.smtp.port", "465");

	       // ���]�w gmail ���b�� & �K�X (�N�ǥѧA��Gmail�ӶǰeEmail)
	       // �����NmyGmail���i�w���ʸ��C�����ε{���s���v�j���}
		     final String myGmail = "ZA101G1@gmail.com";
		     final String myGmail_password = "za101g1za101";
			   Session session = Session.getInstance(props, new Authenticator() {
				   protected PasswordAuthentication getPasswordAuthentication() {
					   return new PasswordAuthentication(myGmail, myGmail_password);
				   }
			   });

			   Message message = new MimeMessage(session);
			   message.setFrom(new InternetAddress(myGmail));
			   message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
			  
			   //�]�w�H�����D��  
			   message.setSubject(subject);
			   //�]�w�H�������e 
			   message.setText(messageText);

			   Transport.send(message);
			   System.out.println("�ǰe���\!");
	     }catch (MessagingException e){
		     System.out.println("�ǰe����!");
		     e.printStackTrace();
	     }
	   }	
}
