package com.init;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

public class InitServletContext implements ServletContextListener {
	
	ServletContext context;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		context = event.getServletContext(); 
		HashMap<String,String> member = new HashMap<String,String>();
		member.put("00", "�ӽФ�");
		member.put("01", "�ӽФ���^");
		member.put("10", "���q�|��");
		member.put("11", "���q�|�����v");
		member.put("20", "VIP�|��");
		member.put("21", "VIP�|�����v");
		context.setAttribute("member_status", member);
		
	}

}
