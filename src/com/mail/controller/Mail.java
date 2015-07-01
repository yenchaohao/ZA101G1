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
		// 設定請求字元編碼
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		if ("addQuestion".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			String title=req.getParameter("title");
			String question=req.getParameter("question");
//			String title=new String(tit.getBytes("ISO-8859-1"),"UTF-8");
//			String question=new String(ques.getBytes("ISO-8859-1"),"UTF-8");
			String member_id=req.getParameter("member_id");
			
			if(question==null||question.trim().length()==0){
				errorMsgs.add("請輸入問題");
			}
			if(title.equals("請選擇")){
				errorMsgs.add("請選擇問題類型");		
			}
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front/customerCenter/contact.jsp");
				failureView.forward(req, res);
				return;// 程式中斷
			}
			/*************************** 2.開始新增資料 *****************************************/
			
			MailService mailSvc=new MailService();
			mailSvc.addMail(member_id,  title, question);
			/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
			String url = "/front/index/index.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
			successView.forward(req, res);
		}
		if ("answerMail".equals(action)){
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			Integer cmid=Integer.valueOf(req.getParameter("cmid"));
			/*************************** 2.開始取得資料 *****************************************/
			MailService mailSvc=new MailService();
			MailVO mailVO=mailSvc.getOneMail(cmid);
			/*************************** 3.準備轉交(Send the Success view) ***********/
			req.setAttribute("mailVO", mailVO);
			String url = "/back/mail/answerMail.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
			successView.forward(req, res);
		}
		
		if ("answered".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			String answer=req.getParameter("answer");
			String empid=req.getParameter("empid");
			Integer cmid=Integer.valueOf(req.getParameter("cmid"));
			MailService mailSvc=new MailService();
			MailVO mailVO=mailSvc.getOneMail(cmid);
			if(answer.trim().length()==0){
				errorMsgs.add("請輸入回覆內容");
			}
			
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("mailVO", mailVO);
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/mail/answerMail.jsp");
				failureView.forward(req, res);
				return;// 程式中斷
			}
			/*************************** 2.開始更新資料 *****************************************/
			mailSvc.updateMail(mailVO.getMember_id(), empid, mailVO.getTitle(), mailVO.getQuestion(), answer, 1, cmid);
			/*************************** 3.寄送回覆 *****************************************/
			MemberService memSvc=new MemberService();
			MemberVO memVO=memSvc.getOneMemberByMemberId(mailVO.getMember_id());
			 String to = memVO.getEmail();
		     
		     String subject = "EX-CHANGE問題回覆";

		     String messageText = "對於你的問題:"+mailVO.getQuestion()+"\n於以下回覆:\n"+answer+"\n\n\n感謝您利用本網站"; 
		     sendMail(to, subject, messageText);
			/*************************** 4.簡訊通知 *****************************************/
			Send se = new Send();	
		 	String[] tel ={memVO.getTel().replace("-","")};
		 	String message =memVO.getMem_name()+"您好:您於本網站EX-CHANGE的問題已經回復，敬請查收您的email";
		 	se.sendMessage(tel , message);
			/*************************** 5.準備轉交(Send the Success view) ***********/
			
			String url = "/back/mail/checkMail.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
			successView.forward(req, res);
		}
	}
	public void sendMail(String to, String subject, String messageText) {
		
		   try {
			   // 設定使用SSL連線至 Gmail smtp Server
			   Properties props = new Properties();
			   props.put("mail.smtp.host", "smtp.gmail.com");
			   props.put("mail.smtp.socketFactory.port", "465");
			   props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			   props.put("mail.smtp.auth", "true");
			   props.put("mail.smtp.port", "465");

	       // ●設定 gmail 的帳號 & 密碼 (將藉由你的Gmail來傳送Email)
	       // ●須將myGmail的【安全性較低的應用程式存取權】打開
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
			  
			   //設定信中的主旨  
			   message.setSubject(subject);
			   //設定信中的內容 
			   message.setText(messageText);

			   Transport.send(message);
			   System.out.println("傳送成功!");
	     }catch (MessagingException e){
		     System.out.println("傳送失敗!");
		     e.printStackTrace();
	     }
	   }	
}
