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
		member.put("00", "申請中");
		member.put("01", "申請中駁回");
		member.put("10", "普通會員");
		member.put("11", "普通會員停權");
		member.put("20", "VIP會員");
		member.put("21", "VIP會員停權");
		context.setAttribute("member_status", member);
		
	}

}
