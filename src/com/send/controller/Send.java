package com.send.controller;

import com.send.model.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Send
 */
@WebServlet("/Send")
public class Send extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Send() {
		super();
		// TODO Auto-generated constructor stub
	}

	private void process(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		// 設定回應字元編碼
		res.setContentType("text/html ; charset=UTF-8");
		// 設定請求字元編碼
		req.setCharacterEncoding("UTF-8");
		PrintWriter out = res.getWriter();
		String action = req.getParameter("action");
		String requestURL = req.getParameter("requestURL");
		List<String> errorMessage = new LinkedList<>();
		HttpSession session = req.getSession();
		
		if ("ChangeStatusRequest".equals(action)) {
			int count = Integer.parseInt(req.getParameter("count"));
			for (int i = 0; i < count; i++) {
				if (i % 2 != 0 && i != 0) {
					SendVO sendvo1 = new SendService().getOneSend(Integer
							.parseInt(req.getParameter("sid" + i)));
					SendVO sendvo2 = new SendService().getOneSend(Integer
							.parseInt(req.getParameter("sid" + (i - 1))));
					int status = Integer.parseInt(req.getParameter("status"
							+ (i - 1)));
					if (status == 0 || status == 3) {
						sendvo1.setStart_date(null);
						sendvo1.setEnd_date(null);
						sendvo2.setStart_date(null);
						sendvo2.setEnd_date(null);
						sendvo1.setStatus(status);
						sendvo2.setStatus(status);
						new SendService().updateSendByObject(sendvo1);
						new SendService().updateSendByObject(sendvo2);
					} else if (status == 1) {
						Calendar cal = Calendar.getInstance();
						Timestamp time = new Timestamp(cal.getTimeInMillis());
						System.out.println("開始派送");
						sendvo1.setStart_date(time);
						sendvo1.setEnd_date(null);
						sendvo2.setStart_date(time);
						sendvo2.setEnd_date(null);
						sendvo1.setStatus(status);
						sendvo2.setStatus(status);
						new SendService().updateSendByObject(sendvo1);
						new SendService().updateSendByObject(sendvo2);
					}
				}// if (i % 2 != 0 && i != 0)
			}// for
			RequestDispatcher view = req.getRequestDispatcher(requestURL);
			view.forward(req, res);
			return;

		}// if changeStatus action
		
		if("search".equals(action)){
			Map<String,String[]> map = req.getParameterMap();
			
			if((req.getParameter("sid") != null && req.getParameter("sid").trim().length() != 0) ||
			   (req.getParameter("mem_address") != null && req.getParameter("mem_address").trim().length() != 0) ||
			   (req.getParameter("mem_name") != null && req.getParameter("mem_name").trim().length() != 0) ||
			   (req.getParameter("status") != null && req.getParameter("status").trim().length() != 0)
					){
				
				List<SendVOAssociations> sendList = new SendService().getAllByCompositeQuery(map);
				
				if(sendList.size() == 0){
					errorMessage.add("查詢不到任何資料喔,請重新輸入");
					req.setAttribute("errorMessage", errorMessage);
					RequestDispatcher view = req.getRequestDispatcher(requestURL);
					view.forward(req, res);
					return;
				}
				
				session.setAttribute("sendList", sendList);
				RequestDispatcher view = req.getRequestDispatcher("/back/send/send_after_search.jsp");
				view.forward(req, res);
				return;
			
			}else{
				errorMessage.add("請輸入查詢關鍵字");
				req.setAttribute("errorMessage", errorMessage);
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;
			}
		}
		
		
	
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request, response);
	}

}
