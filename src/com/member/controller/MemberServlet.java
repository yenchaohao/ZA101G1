package com.member.controller;

import org.json.*;

import com.tool.*;
import com.member.model.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.websocket.Session;

/**
 * Servlet implementation class RegisterController
 */

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 5 * 5 * 1024 * 1024)
public class MemberServlet extends HttpServlet implements Runnable {
	private static final long serialVersionUID = 1L;

	// totalSend (���ե�)�|�b473��p��ϥΪ��`�@�n�H�X��(�֭p)
	int totalSend = 0;

	// �|�I�sMailService�H�H���I������� (�H�@�ʫH�Τ@�Ӱ����,�ҥH�ΰ}�C�˰����)
	private Thread[] mail_service;

	// �s�a�}��List
	private List<String> toList = new ArrayList<>();
	// �s�D�D��List
	private List<String> subjectList = new ArrayList<>();
	// �s���e��List
	private List<String> contentList = new ArrayList<>();

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MemberServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	void process(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		// �]�w�^���r���s�X
		res.setContentType("text/html ; charset=UTF-8");
		// �]�w�ШD�r���s�X
		req.setCharacterEncoding("UTF-8");

		// ServletOutputStream out = res.getOutputStream();
		PrintWriter out = res.getWriter();
		String action = "";
		String picName = "";
		action = req.getParameter("action");

		if ("registerForm".equals(action)) {
			List<String> errorMessage = new LinkedList<String>();
			InputStream in = null;
			Collection<Part> parts = req.getParts();

			for (Part part : parts) {

				if ("pic".equals(part.getName())) {
					if (getFileNameFromPart(part) != null
							&& ("jpg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
									|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 || "jpeg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0)) {
						in = part.getInputStream();
						break;
					} else {
						errorMessage.add("�Ϥ��W�Ǯ榡�����T. �榡������ JPG , JPEG , PNG");
						break;
					}
				}

			}
			String email = req.getParameter("email").trim();
			String password = req.getParameter("password").trim();
			String rePwd = req.getParameter("rePwd").trim();
			String mem_name = req.getParameter("mem_name").trim();
			String id_no = req.getParameter("id_no").trim();
			String tel = req.getParameter("tel").trim();
			String address = req.getParameter("address").trim();
			String birthday = req.getParameter("birthday").trim();
			if (email.length() <= 0 || password.length() <= 0
					|| rePwd.length() <= 0 || mem_name.length() <= 0
					|| id_no.length() <= 0 || tel.length() <= 0
					|| address.length() <= 0 || birthday.length() <= 0) {
				errorMessage.add("�C����쳣���������");
			}
			if (!password.equals(rePwd))
				errorMessage.add("�K�X��J���~");
			if(new TaiwanIdVerify().IDverification(id_no) == false)
				errorMessage.add("�����Ҧr�����~");
			
			Date date = null;
			try {
				int[] birth = convertDate(birthday);
				Calendar calendar = new GregorianCalendar(birth[0], birth[1],
						birth[2]);
				date = new java.sql.Date(calendar.getTimeInMillis());
			} catch (Exception ex) {
				try {
					date = java.sql.Date.valueOf(birthday);
				} catch (Exception e) {
					errorMessage.add("����榡��J���~");
					birthday = "";
				}
			}
 
			byte[] image = null;
			if (in != null) {
				try {
					ByteArrayOutputStream byteout = new ByteArrayOutputStream();
					byte[] buffer = new byte[4 * 1024];
					int len = 0;
					while ((len = in.read(buffer)) != -1) {
						byteout.write(buffer, 0, len);
					}
					byteout.flush();

					image = byteout.toByteArray();

				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			} else {
				errorMessage.add("�ФW�Ǥ@�i�Ӥ�");
			}

			// �ΨӦ^�Ǩ�e�@��������
			MemberVO membervo = new MemberVO();
			membervo.setEmail(email);
			membervo.setMem_name(mem_name);
			membervo.setId_no(id_no);
			membervo.setTel(tel);
			membervo.setAddress(address);
			membervo.setBirthday(date);

			if (errorMessage.size() > 0) {
				req.setAttribute("MemberVO", membervo);
				req.setAttribute("errorMessage", errorMessage);
				RequestDispatcher error = req
						.getRequestDispatcher("/front/member/addMember.jsp");
				error.forward(req, res);
				return;
			}

			MemberService member_service = new MemberService();

			String member_id = member_service.addMemberGetPrimaryKey(email,
					mem_name, password, id_no, tel, address, date, image, null);
			if (member_id == null || member_id.trim().length() <= 0) {
				errorMessage.add("��email�w�g���H�ϥ�");
				req.setAttribute("MemberVO", membervo);
				req.setAttribute("errorMessage", errorMessage);
				RequestDispatcher error = req
						.getRequestDispatcher("/front/member/addMember.jsp");
				error.forward(req, res);
				return;
			}
			req.setAttribute("member_id", member_id);
			RequestDispatcher view = req
					.getRequestDispatcher("/front/member/success_register.jsp");
			view.forward(req, res);
		}
		// �qshow_all_member �ǹL�Ӫ�
		if ("update_member".equals(action)) {

			List<String> errorMessage = new LinkedList<String>();
			String requestURL = req.getParameter("requestURL").trim();

			try {
				String member_id = req.getParameter("member_id");
				MemberVO membervo = new MemberService()
						.getOneMemberByMemberId(member_id);
				req.setAttribute("MemberVO", membervo);
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/updateMember.jsp");
				view.forward(req, res);

			} catch (Exception ex) {

				errorMessage.add("��Ƥ����T,�ק異��: " + ex.getMessage());
				req.setAttribute("errorMessage", errorMessage);
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);

			}
		}
		// �qupdateMember.jsp�L�Ӫ�
		if ("update_confirm".equals(action)) {
			List<String> errorMessage = new LinkedList<String>();
			String member_id = req.getParameter("member_id");
			String requestURL = req.getParameter("requestURL");
			HttpSession session = req.getSession();

			MemberVO membervo = new MemberService()
					.getOneMemberByMemberId(member_id);

			byte[] image = null;
			InputStream in = null;
			Collection<Part> parts = req.getParts();

			for (Part part : parts) {

				if ("pic".equals(part.getName())) {
					if (getFileNameFromPart(part) != null
							&& ("jpg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
									|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 || "jpeg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0)) {
						in = part.getInputStream();
						break;
					} else if (getFileNameFromPart(part) == null)
						break;
					else {
						errorMessage.add("�Ϥ��W�Ǯ榡�����T. �榡������ JPG , JPEG , PNG");
						break;
					}
				}

			}

			if (in != null) {
				try {
					ByteArrayOutputStream byteout = new ByteArrayOutputStream();
					byte[] buffer = new byte[4 * 1024];
					int len = 0;
					while ((len = in.read(buffer)) != -1) {
						byteout.write(buffer, 0, len);
					}
					byteout.flush();
					image = byteout.toByteArray();
					membervo.setPic(image);
					byteout.close();

				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}

			String email = req.getParameter("email").trim();
			String password = req.getParameter("password").trim();
			String rePassword = req.getParameter("rePwd").trim();
			String tel = req.getParameter("tel").trim();
			String address = req.getParameter("address").trim();
			String my_wish = req.getParameter("my_wish").trim();
			if (email.length() <= 0 || password.length() <= 0
					|| tel.length() <= 0 || address.length() <= 0) {
				errorMessage.add("���нT���g");
			}
			if (!password.equals(rePassword))
				errorMessage.add("�K�X���ƿ�J�����T");

			// �p�G�䤤�@�Ӯ榡���~,��^�W�@��
			if (errorMessage.size() > 0) {
				MemberVO newmember = (MemberVO) session.getAttribute("member");
				newmember.setEmail(email);
				newmember.setTel(tel);
				newmember.setAddress(address);

				if (req.getParameter("my_wish") != null
						&& req.getParameter("my_wish").length() != 0) {
					newmember.setMy_wish(req.getParameter("groupid")
							+ req.getParameter("my_wish"));
				} else
					newmember.setMy_wish(null);

				newmember.setMember_id(member_id);
				req.setAttribute("MemberVO", newmember);
				req.setAttribute("errorMessage", errorMessage);
				//System.out.println(requestURL); 
				if ("/front/member/show_one_member.jsp".equals(requestURL)) {
					RequestDispatcher view = req
							.getRequestDispatcher("/front/member/updateMember.jsp");

					view.forward(req, res);
					return;
				}else{
					RequestDispatcher view = req
							.getRequestDispatcher("/front/member/facebookUpdateMember.jsp");

					view.forward(req, res);
					return;
				}

			} else {
				// �즸�ϥ�fb�n�J���|���|�i�Jmembervo �O�� session member_id�h�䪺,
				// �S�ϥ�fb�n�J�L���|����Ʈw�|�䤣��
				if (membervo == null) {
					membervo = (MemberVO) session.getAttribute("member");
					membervo.setEmail(email);
					membervo.setPassword(rePassword);
					membervo.setTel(tel);
					membervo.setAddress(address);
					if (req.getParameter("my_wish") != null
							&& req.getParameter("my_wish").length() != 0) {
						membervo.setMy_wish(req.getParameter("groupid")
								+ req.getParameter("my_wish"));
					} else
						membervo.setMy_wish(null);
					if (image != null)
						membervo.setPic(image);
					int insert = new MemberService()
							.addMemberForFaceBook(membervo);
					if (insert != 0) {
						session.setAttribute("member", membervo);
						session.setAttribute("member_id", membervo.getMember_id());
						RequestDispatcher view = req
								.getRequestDispatcher(requestURL);
						view.forward(req, res);
						return;
					} else {
						errorMessage
								.add("�ϥ�Facebook�b���n�J����,���H�c�w�Q�ϥιL.�ШϥΥ洫���|���n�J,�ε��U�洫���|��");
						req.setAttribute("errorMessage", errorMessage);
						RequestDispatcher view = req
								.getRequestDispatcher("/front/member/member_login.jsp");
						view.forward(req, res);
						return;
					}

				}
				membervo.setEmail(email);
				membervo.setPassword(rePassword);
				membervo.setTel(tel);
				membervo.setAddress(address);
				membervo.setMy_wish(my_wish);
				if (req.getParameter("my_wish") != null
						&& req.getParameter("my_wish").length() != 0) {
					membervo.setMy_wish(req.getParameter("groupid")
							+ req.getParameter("my_wish"));
				} else
					membervo.setMy_wish(null);

				if (image != null)
					membervo.setPic(image);

				int update = new MemberService().updateMemberByObject(membervo);
				if (update != 1) {
					errorMessage.add("�s�W���� ���ˬd��ƬO�_���T");
					req.setAttribute("MemberVO", membervo);
					req.setAttribute("errorMessage", errorMessage);
					RequestDispatcher view = req
							.getRequestDispatcher("/front/member/updateMember.jsp");
					view.forward(req, res);
					return;
				}

				req.setAttribute("MemberVO", membervo);
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;

			}
		}
		if ("ChangeStatusRequest".equals(action)) {
			// currentSend �p��ϥΪ̷�e�n�H�X�ʫH (�Ψӵ�����ǰ}�C���D�n�s�W�X�Ӱ����)
			int currentSend = 0;
			List<String> errorMessage = new LinkedList<String>();
			String requestURL = req.getParameter("requestURL");
			HttpSession session = req.getSession();
			int whichPage = 0;
			if (!requestURL
					.equals("/back/member/show_one_member_after_search.jsp"))
				whichPage = Integer.parseInt(req.getParameter("whichPage"));
			// �p�G���d�߭����e�Ӫ��ШD,�n�qsession���X�L�d�X�Ӫ����G,�M��窱�A��A���L�����G�Ǧ^�쭶��
			List<MemberVO> list = null;
			if (requestURL.equals("/back/member/show_member_after_search.jsp")) {
				list = (List<MemberVO>) session.getAttribute("memberList");
			}

			// �p��X�����W�`�@���X���|�����
			int count = Integer.parseInt(req.getParameter("count"));
			// �̷ӴX���N�]�X���j��
			for (int i = 0; i < count; i++) {
				// �p�G�ӵ��ШD�n�ܧ󪺪��A���O�ŭȥB �]������0 (0�N��S����ܥ���窱�A���ШD)
				if (req.getParameter("status" + i) != null
						&& Integer.parseInt(req.getParameter("status" + i)) != 0) {

					String status = req.getParameter("status" + i);
					String member_id = req.getParameter("member_id" + i);
					MemberVO membervo = new MemberService()
							.getOneMemberByMemberId(member_id);

					// ���o����LOCALHOST
					InetAddress address = InetAddress.getLocalHost();
					// membervo.getMem_status() == 10 &&
					// Integer.parseInt(status) == 20
					// ...�H�eemail (�|�����A10�O�ӽФ�,20�O���q�|��)
					if (membervo.getMem_status() == 10
							&& Integer.parseInt(status) == 20) {

						// �N�|�����H�c�a�}�s��list
						toList.add(membervo.getEmail());
						// �N�H�󪺥D�D�s��list
						subjectList.add("<�G������>�A���|���ӽФw�g�q�L");
						StringBuffer messageText = new StringBuffer();
						messageText.append("�˷R��" + membervo.getMem_name()
								+ "�A�n \n");
						messageText.append("�w�令��<�G������>�|��\n");
						/*
						 * InetAddress.getHostAddress() �i���o����IP��}
						 * req.getServerPort() �����s����(8081)
						 */
						messageText.append("�Ц�:http://"
								+ address.getHostAddress() + ":"
								+ req.getServerPort()
								+ "/ZA101G1/front/index/index.jsp \n");
						messageText.append("�n�J�ñҥΧA���b�� \n");
						// �N�H�󪺤��e�s��list
						contentList.add(messageText.toString());

					} else if (membervo.getMem_status() == 20
							&& Integer.parseInt(status) == 21
							|| membervo.getMem_status() == 30
							&& Integer.parseInt(status) == 31) {
						// �N�|�����H�c�a�}�s��list
						toList.add(membervo.getEmail());
						// �N�H�󪺥D�D�s��list
						subjectList.add("<�G������>�A���|�����w�g���v");
						StringBuffer messageText = new StringBuffer();
						messageText.append("�˷R��" + membervo.getMem_name()
								+ "�A�n \n");
						messageText.append("�A���|�����w�D���v\n");
						/*
						 * InetAddress.getHostAddress() �i���o����IP��}
						 * req.getServerPort() �����s����(8081)
						 */
						messageText.append("�Ц�:http://"
								+ address.getHostAddress() + ":"
								+ req.getServerPort()
								+ "/ZA101G1/front/index/index.jsp \n");
						messageText.append("�d�� \n");
						// �N�H�󪺤��e�s��list
						contentList.add(messageText.toString());

					} else if (membervo.getMem_status() == 21
							&& Integer.parseInt(status) == 20
							|| membervo.getMem_status() == 31
							&& Integer.parseInt(status) == 30) {
						// �N�|�����H�c�a�}�s��list
						toList.add(membervo.getEmail());
						// �N�H�󪺥D�D�s��list
						subjectList.add("<�G������>�A���|�����w�g��_");
						StringBuffer messageText = new StringBuffer();
						messageText.append("�˷R��" + membervo.getMem_name()
								+ "�A�n \n");
						messageText.append("�A���|�����w�g��_\n");
						/*
						 * InetAddress.getHostAddress() �i���o����IP��}
						 * req.getServerPort() �����s����(8081)
						 */
						messageText.append("�Ц�:http://"
								+ address.getHostAddress() + ":"
								+ req.getServerPort()
								+ "/ZA101G1/front/index/index.jsp \n");
						messageText.append("�d�� \n");
						// �N�H�󪺤��e�s��list
						contentList.add(messageText.toString());

					} else if (membervo.getMem_status() == 10
							&& Integer.parseInt(status) == 11) {
						// �N�|�����H�c�a�}�s��list
						toList.add(membervo.getEmail());
						// �N�H�󪺥D�D�s��list
						subjectList.add("<�G������>�ӽХ[�J�|������");
						StringBuffer messageText = new StringBuffer();
						messageText.append("�˷R��" + membervo.getMem_name()
								+ "�A�n \n");
						messageText.append("�ѩ��椣�ŦX,�ӽХ���\n");
						/*
						 * InetAddress.getHostAddress() �i���o����IP��}
						 * req.getServerPort() �����s����(8081)
						 */
						messageText.append("<�洫��> http://"
								+ address.getHostAddress() + ":"
								+ req.getServerPort()
								+ "/ZA101G1/front/index/index.jsp \n");
						messageText.append("�P�§A���ӽ� \n");
						// �N�H�󪺤��e�s��list
						contentList.add(messageText.toString());

					}
					membervo.setMem_status(Integer.parseInt(status));
					new MemberService().updateMemberByObject(membervo);

					if (list != null && !list.isEmpty()) {
						for (int y = 0; y < list.size(); y++) {
							if (list.get(y).getMember_id()
									.equals(membervo.getMember_id()))
								list.set(y, membervo);
						}
					}

					// �p��ϥΪ̱H�H���ƥ�
					currentSend++;
				}// �p�G�ӵ��ШD�n�ܧ󪺪��A���O�ŭȥB �]������0 (0�N��S����ܥ���窱�A���ШD)
			}

			// �p�G���H��n�H���� currentSend(�p��n�H���H������)
			if (currentSend != 0) {
				System.out.println("�٦� " + contentList.size() + " �ʭn�H ");
				mail_service = new Thread[currentSend];
				for (int i = 0; i < mail_service.length; i++) {
					mail_service[i] = new Thread(this);
					mail_service[i].setPriority(Thread.MIN_PRIORITY);
					mail_service[i].start();
				}
				totalSend += currentSend;
				System.out.println("�`�@�n�H�`��: " + totalSend);
			}

			if (list != null && !list.isEmpty()) {
				session.setAttribute("memberList", list);
			}
			errorMessage.add("�ק窱�A���\");
			req.setAttribute("whichPage", whichPage);

			RequestDispatcher view = req.getRequestDispatcher(requestURL);
			view.forward(req, res);
			return;
		}
		if ("searchMember".equals(action)) {
			List<String> errorMessage = new LinkedList<String>();
			String member_id = req.getParameter("member_id");
			String requestURL = req.getParameter("requestURL");
			if (member_id.length() <= 0) {
				errorMessage.add("�п�J�|���s��");
				req.setAttribute("errorMessage", errorMessage);
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;
			}
			MemberVO membervo = new MemberService()
					.getOneMemberByMemberId(member_id);
			if (membervo == null || membervo.getMember_id() == null) {
				errorMessage.add("�d�L���H");
				req.setAttribute("errorMessage", errorMessage);
				req.setAttribute("member_id", member_id);
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;
			}

			RequestDispatcher view = req
					.getRequestDispatcher("/back/member/show_one_member_after_search.jsp");
			view.forward(req, res);
			return;

		}
		if ("searchMemberByname".equals(action)) {
			List<String> errorMessage = new LinkedList<String>();
			String mem_name = req.getParameter("mem_name");
			String requestURL = req.getParameter("requestURL");
			if (mem_name.length() <= 0) {
				errorMessage.add("�п�J�|���s��");
				req.setAttribute("errorMessage", errorMessage);
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;
			}
			List<MemberVO> memberList = new MemberService().getSearchWish(req
					.getParameterMap());
			if (memberList == null || memberList.isEmpty()) {
				errorMessage.add("�d�L���H");
				req.setAttribute("errorMessage", errorMessage);
				req.setAttribute("mem_name", req.getParameter("mem_name"));
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;
			}
			HttpSession session = req.getSession();
			session.setAttribute("memberList", memberList);
			RequestDispatcher view = req
					.getRequestDispatcher("/back/member/show_member_after_search.jsp");
			view.forward(req, res);
			return;
		}
		// fb�n�J���ШD
		if ("FBlogin".equals(action)) {
			List<String> errorMessage = new LinkedList<String>();
			req.setAttribute("errorMessage", errorMessage);
			String url = (String) req.getParameter("url");
			String location = (String) req.getSession()
					.getAttribute("location");
			// ���ofb�|�����
			String member_id = req.getParameter("fbId");
			String fbEmail = req.getParameter("fbEmail");
			String fbName = req.getParameter("fbName");

			// �p�G��ƥ��T
			if ((member_id != null && member_id.trim().length() != 0)
					|| (fbEmail != null && fbEmail.trim().length() != 0)
					|| (fbName != null && fbName.trim().length() != 0)) {
				MemberVO membervo = new MemberVO();
				membervo.setMember_id(member_id);

				// �αb����o�ӷ|�����S���n�J�L
				MemberVO fbvo = new MemberService()
						.getOneMemberByMemberId(member_id);
				// �p�G�w�g�n�J�L
				if (fbvo != null) {
					HttpSession session = req.getSession();
					session.setAttribute("member_id", member_id);
					session.setAttribute("member", fbvo);
					if (location == null) {
						// ��^�쭶��
						RequestDispatcher view = req.getRequestDispatcher(url);
						view.forward(req, res);
						return;
					}

					if (location != null) {
						res.sendRedirect(location);
						return;
					}
				}
				// �S�n�J�L�N�s�W�@��member����M�������s�|����ƭ���
				membervo.setEmail(fbEmail);
				membervo.setMem_name(fbName);
				try {
					ServletContext context = req.getServletContext();
					// �]��fb�Ǧ^����ƨS����,�q�M�׸�Ƨ��̭����
					InputStream in = new FileInputStream(
							context.getRealPath("/images/fbuser.jpg"));
					ByteArrayOutputStream byteout = new ByteArrayOutputStream();
					byte[] buffer = new byte[4 * 1024];
					int len = 0;
					while ((len = in.read(buffer)) != -1) {
						byteout.write(buffer, 0, len);
					}
					byteout.flush();

					byte[] image = byteout.toByteArray();
					// �|���Ϥ�,���L�@�ifb�w����
					membervo.setPic(image);

				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}

				// �p�G���\����|����Ƨ�s����,�n�D�ӷ|����J�a�}
				HttpSession session = req.getSession();
				
				session.setAttribute("member", membervo);
				//����s�����Ϊ�
				req.setAttribute("MemberVO", membervo);
				errorMessage
						.add("�P�±z�ϥΥ洫��,�즸�ϥνж�J�z���򥻸�ƻP�ӤH�Ϥ���s,���±z���ϥ�.<b>���U�T�{�ק�,�n�J�{�Ǥ~������</b>");
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/facebookUpdateMember.jsp");
				view.forward(req, res);

			} else {
				errorMessage.add("�ϥ�Facebook�b���n�J����.�ШϥΥ洫���|���n�J,�ε��U�洫���|��");
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/member_login.jsp");
				view.forward(req, res);
				return;
			}

		}
		if ("login".equals(action)) {
			List<String> errorMessage = new LinkedList<String>();
			req.setAttribute("errorMessage", errorMessage);

			String url = (String) req.getParameter("url");
			String location = (String) req.getSession()
					.getAttribute("location");

			String email = req.getParameter("email");
			String password = req.getParameter("password");

			if (email.length() <= 0 || password.length() <= 0) {
				errorMessage.add("�п�J�b���K�X");
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/member_login.jsp");
				view.forward(req, res);
				return;
			}

			MemberVO member = new MemberService().getOneMemberByEmail(email);

			if (member == null || member.getEmail() == null
					|| member.getMem_status() == 11) {
				errorMessage.add("�b����J���~");
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/member_login.jsp");
				view.forward(req, res);
				return;
			}

			if (!password.equals(member.getPassword())) {
				errorMessage.add("�K�X��J���~");
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/member_login.jsp");
				view.forward(req, res);
				return;
			}

			if (member.getMem_status() == 10) {
				errorMessage.add("���b���٦b�f�֤�");
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/member_login.jsp");
				view.forward(req, res);
				return;
			} else if (member.getMem_status() == 21
					|| member.getMem_status() == 31) {
				errorMessage.add("���b���w���v");
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/member_login.jsp");
				view.forward(req, res);
				return;
			}
			MemberVO membervo = new MemberService().getOneMemberByEmail(email);
			String member_id = membervo.getMember_id();
			HttpSession session = req.getSession();
			session.setAttribute("member", membervo);
			session.setAttribute("member_id", member_id);

			if ("/ZA101G1/front/send/send_detail.jsp".equals(location)) {
				RequestDispatcher view = req
						.getRequestDispatcher("/front/transaction/transactionCenter.jsp");
				view.forward(req, res);
				return;
			}

			if (location == null) {
				RequestDispatcher view = req.getRequestDispatcher(url);
				view.forward(req, res);
				return;
			}

			if (location != null)
				res.sendRedirect(location);

			return;

		}
		if ("logout".equals(action)) {
			String requestURL = req.getParameter("requestURL");
			HttpSession session = req.getSession();
			String member_id = (String) session.getAttribute("member_id");
			String location = (String) session.getAttribute("location");
			if (location != null)
				session.removeAttribute("location");
			if (member_id != null)
				session.removeAttribute("member_id");
			session.removeAttribute("cart");
			RequestDispatcher view = req.getRequestDispatcher(requestURL);
			view.forward(req, res);
			return;

		}

	}

	// �H�ʪ�index
	int sendCount = 0;

	synchronized public void sendMail() {
		MailService mailservice = new MailService();
		mailservice.sendMail(toList.get(0), subjectList.get(0),
				contentList.get(0));

		toList.remove(0);
		subjectList.remove(0);
		contentList.remove(0);
		sendCount++;
		System.out.println("done " + sendCount + " mail");
	}

	// mailService.start()����...
	@Override
	public void run() {
		sendMail();
		if (toList == null || toList.isEmpty()) {
			System.out.println("�H��w�g�H��");
			totalSend = 0;
		}
	}

	// //�e�H�|���I�֮� ���F�קK�ШD���_�e�X ���槹�e�H����k�� ����2�������ݫH��H�e����
	// try {
	// Thread.sleep(1500);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// //�]������ǵL�k���_�ϥ�,�ҥH���F�n���ϥΪ̯���@���o�e�H��,�b����ǧ�����,�A���s�ͦ��@��

	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(req, res);
	}

	public String getFileNameFromPart(Part part) {
		String header = part.getHeader("content-disposition"); // �q�e���Ĥ@�ӽd��(����1-�򥻴���)�i�o����head����
		// System.out.println("part.getHeader('content-disposition'): " +
		// header);
		String filename = header.substring(header.lastIndexOf("=") + 2,
				header.length() - 1);
		// System.out.println(filename);
		if (filename.length() == 0) {
			return null;
		}
		return filename;
	}

	private String getFileFormat(String fileName) {
		int dotpos = fileName.indexOf('.');
		String format = fileName.substring(dotpos + 1);
		return format;
	}

	private int[] convertDate(String date) {
		int[] i = new int[3];
		// �~
		i[0] = Integer.parseInt(date.substring(6, date.length()));
		// ��
		i[1] = (Integer.parseInt(date.substring(0, 2)));
		if (i[1] > 1)
			i[1]--;
		// ��
		i[2] = Integer.parseInt(date.substring(3, 5));

		return i;
	}

}
