package com.tool;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class MailListener
 *
 */
@WebListener
public class MailListener implements ServletContextAttributeListener {

	public MailListener() {
		// TODO Auto-generated constructor stub
	}

	public void attributeAdded(ServletContextAttributeEvent event) {
		ServletContext context = event.getServletContext();
		if (context.getAttribute("Sending") != null) {
			
			String sendStatus = (String)context.getAttribute("Sending");
			
		}
	}

	public void attributeRemoved(ServletContextAttributeEvent event) {
		// TODO Auto-generated method stub
	}

	public void attributeReplaced(ServletContextAttributeEvent event) {
		// TODO Auto-generated method stub
		ServletContext context = event.getServletContext();
		if (context.getAttribute("isSend") != null
				&& context.getAttribute("memberName") != null) {
			boolean isSend = (Boolean) context.getAttribute("isSend");
			String member_name = (String) context.getAttribute("memberName");
			if (isSend == true) {
				System.out.println(member_name + "信件已經送出");
			} else
				System.out.println(member_name + "信件送出失敗");
		}
	}

}
