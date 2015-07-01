package com.report.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.report.model.*;
import com.goods.model.*;
import com.mail.model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ReportServlet
 */

public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReportServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void process(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		res.setContentType("text/html ; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession();
		PrintWriter out = res.getWriter();
		String action = req.getParameter("action");
		String url = req.getParameter("url");
		String reason = req.getParameter("reason");
		List<String> errorMessage = new LinkedList<String>();
		req.setAttribute("errorMessage", errorMessage);
		if ("addReport".equals(action)) {

			String member_id = (String) session.getAttribute("member_id");
			if (member_id == null || member_id.trim().length() == 0) {
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/member_login.jsp");
				view.forward(req, res);
				return;
			}

			int gid = Integer.parseInt(req.getParameter("gid"));
			ReportVO report = new ReportService().findByGidAndMember_id(
					member_id, gid);
			if (report != null) {
				errorMessage.add("���ӫ~�A�w�g���|�L�O,�P�§A���^��");
				RequestDispatcher view = req.getRequestDispatcher(url);
				view.forward(req, res);
				return;
			}

			ReportVO reportvo = new ReportVO();
			reportvo.setMember_id(member_id);
			reportvo.setGid(gid);
			int insert = new ReportService().addReport(gid, member_id);

			if (insert == 0) {
				errorMessage.add("��ƿ��~,���|����.���p���t�κ޲z��");
				RequestDispatcher view = req.getRequestDispatcher(url);
				view.forward(req, res);
				return;
			} else {
				try {
					MailVO mailvo = new MailVO();
					mailvo.setTitle("�ӫ~���|"); 
					mailvo.setQuestion(reason); //���|�z��
					mailvo.setMember_id(member_id); 
					mailvo.setAnswer(gid + ""); //�ӫ~�s��
					mailvo.setStatus(4); //���A4 = ���|
					new MailService().addMailByObject(mailvo);
					
				} catch (Exception ex) {
					errorMessage.add("��ƿ��~,���|����.���p���t�κ޲z��");
					RequestDispatcher view = req.getRequestDispatcher(url);
					view.forward(req, res);
					return;
				}
				errorMessage.add("���|���\,�洫���P�§A���^��");
				RequestDispatcher view = req.getRequestDispatcher(url);
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
