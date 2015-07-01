package com.tool;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailService {
	
	/* 透過Gmail來完成信件傳送,需先至google帳戶設定 -> 登入 -> 安全性較低的應用程式存取權限選擇"開啟"  https://myaccount.google.com/ 
	        要在專案的lib 匯入 javax.mail.jar
	    
	 第一組google 帳戶: ZA101G1@gmail.com
	 		         密碼: za101g1za101		
	 */
	
	
	 public boolean sendMail(String to, String subject, String messageText){
		
		boolean isSend = false;
		
		try{
			
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			
			final String myGmail ="ZA101G1@gmail.com";
			final String myPwd ="za101g1za101";
			
			Session session = Session.getInstance(props,new Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(myGmail,myPwd);
				} 
			});
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myGmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			
			message.setSubject(subject);
			message.setText(messageText);
			Transport.send(message);
			
			isSend = true;
			return isSend;
			
			
			
		}catch(Exception ex){
			System.out.println("傳送失敗");
			System.out.println(ex.getMessage());
			//回傳0代表信件傳送失敗
			return isSend;
		}
	}
	
	
}
