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

	// totalSend (測試用)會在473行計算使用者總共要寄幾封(累計)
	int totalSend = 0;

	// 會呼叫MailService寄信的背景執行序 (寄一封信用一個執行序,所以用陣列裝執行序)
	private Thread[] mail_service;

	// 存地址的List
	private List<String> toList = new ArrayList<>();
	// 存主題的List
	private List<String> subjectList = new ArrayList<>();
	// 存內容的List
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

		// 設定回應字元編碼
		res.setContentType("text/html ; charset=UTF-8");
		// 設定請求字元編碼
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
						errorMessage.add("圖片上傳格式不正確. 格式必須為 JPG , JPEG , PNG");
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
				errorMessage.add("每項欄位都為必填欄位");
			}
			if (!password.equals(rePwd))
				errorMessage.add("密碼輸入錯誤");
			if(new TaiwanIdVerify().IDverification(id_no) == false)
				errorMessage.add("身份證字號錯誤");
			
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
					errorMessage.add("日期格式輸入錯誤");
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
				errorMessage.add("請上傳一張照片");
			}

			// 用來回傳到前一頁的物件
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
				errorMessage.add("此email已經有人使用");
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
		// 從show_all_member 傳過來的
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

				errorMessage.add("資料不正確,修改失敗: " + ex.getMessage());
				req.setAttribute("errorMessage", errorMessage);
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);

			}
		}
		// 從updateMember.jsp過來的
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
						errorMessage.add("圖片上傳格式不正確. 格式必須為 JPG , JPEG , PNG");
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
				errorMessage.add("欄位請確實填寫");
			}
			if (!password.equals(rePassword))
				errorMessage.add("密碼重複輸入不正確");

			// 如果其中一個格式錯誤,轉回上一頁
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
				// 初次使用fb登入的會員會進入membervo 是用 session member_id去找的,
				// 沒使用fb登入過的會員資料庫會找不到
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
								.add("使用Facebook帳號登入失敗,此信箱已被使用過.請使用交換網會員登入,或註冊交換網會員");
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
					errorMessage.add("新增失敗 請檢查資料是否正確");
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
			// currentSend 計算使用者當前要寄幾封信 (用來給執行序陣列知道要新增幾個執行序)
			int currentSend = 0;
			List<String> errorMessage = new LinkedList<String>();
			String requestURL = req.getParameter("requestURL");
			HttpSession session = req.getSession();
			int whichPage = 0;
			if (!requestURL
					.equals("/back/member/show_one_member_after_search.jsp"))
				whichPage = Integer.parseInt(req.getParameter("whichPage"));
			// 如果視查詢頁面送來的請求,要從session取出他查出來的結果,然後改狀態後再把改過的結果傳回原頁面
			List<MemberVO> list = null;
			if (requestURL.equals("/back/member/show_member_after_search.jsp")) {
				list = (List<MemberVO>) session.getAttribute("memberList");
			}

			// 計算出頁面上總共有幾筆會員資料
			int count = Integer.parseInt(req.getParameter("count"));
			// 依照幾筆就跑幾次迴圈
			for (int i = 0; i < count; i++) {
				// 如果該筆請求要變更的狀態不是空值且 也不等於0 (0代表沒有選擇任何改狀態的請求)
				if (req.getParameter("status" + i) != null
						&& Integer.parseInt(req.getParameter("status" + i)) != 0) {

					String status = req.getParameter("status" + i);
					String member_id = req.getParameter("member_id" + i);
					MemberVO membervo = new MemberService()
							.getOneMemberByMemberId(member_id);

					// 取得本機LOCALHOST
					InetAddress address = InetAddress.getLocalHost();
					// membervo.getMem_status() == 10 &&
					// Integer.parseInt(status) == 20
					// ...寄送email (會員狀態10是申請中,20是普通會員)
					if (membervo.getMem_status() == 10
							&& Integer.parseInt(status) == 20) {

						// 將會員的信箱地址存到list
						toList.add(membervo.getEmail());
						// 將信件的主題存到list
						subjectList.add("<二手交易所>你的會員申請已經通過");
						StringBuffer messageText = new StringBuffer();
						messageText.append("親愛的" + membervo.getMem_name()
								+ "你好 \n");
						messageText.append("歡迎成為<二手交易所>會員\n");
						/*
						 * InetAddress.getHostAddress() 可取得本機IP位址
						 * req.getServerPort() 本機連結阜(8081)
						 */
						messageText.append("請至:http://"
								+ address.getHostAddress() + ":"
								+ req.getServerPort()
								+ "/ZA101G1/front/index/index.jsp \n");
						messageText.append("登入並啟用你的帳號 \n");
						// 將信件的內容存到list
						contentList.add(messageText.toString());

					} else if (membervo.getMem_status() == 20
							&& Integer.parseInt(status) == 21
							|| membervo.getMem_status() == 30
							&& Integer.parseInt(status) == 31) {
						// 將會員的信箱地址存到list
						toList.add(membervo.getEmail());
						// 將信件的主題存到list
						subjectList.add("<二手交易所>你的會員資格已經停權");
						StringBuffer messageText = new StringBuffer();
						messageText.append("親愛的" + membervo.getMem_name()
								+ "你好 \n");
						messageText.append("你的會員資格已遭停權\n");
						/*
						 * InetAddress.getHostAddress() 可取得本機IP位址
						 * req.getServerPort() 本機連結阜(8081)
						 */
						messageText.append("請至:http://"
								+ address.getHostAddress() + ":"
								+ req.getServerPort()
								+ "/ZA101G1/front/index/index.jsp \n");
						messageText.append("查看 \n");
						// 將信件的內容存到list
						contentList.add(messageText.toString());

					} else if (membervo.getMem_status() == 21
							&& Integer.parseInt(status) == 20
							|| membervo.getMem_status() == 31
							&& Integer.parseInt(status) == 30) {
						// 將會員的信箱地址存到list
						toList.add(membervo.getEmail());
						// 將信件的主題存到list
						subjectList.add("<二手交易所>你的會員資格已經恢復");
						StringBuffer messageText = new StringBuffer();
						messageText.append("親愛的" + membervo.getMem_name()
								+ "你好 \n");
						messageText.append("你的會員資格已經恢復\n");
						/*
						 * InetAddress.getHostAddress() 可取得本機IP位址
						 * req.getServerPort() 本機連結阜(8081)
						 */
						messageText.append("請至:http://"
								+ address.getHostAddress() + ":"
								+ req.getServerPort()
								+ "/ZA101G1/front/index/index.jsp \n");
						messageText.append("查看 \n");
						// 將信件的內容存到list
						contentList.add(messageText.toString());

					} else if (membervo.getMem_status() == 10
							&& Integer.parseInt(status) == 11) {
						// 將會員的信箱地址存到list
						toList.add(membervo.getEmail());
						// 將信件的主題存到list
						subjectList.add("<二手交易所>申請加入會員失敗");
						StringBuffer messageText = new StringBuffer();
						messageText.append("親愛的" + membervo.getMem_name()
								+ "你好 \n");
						messageText.append("由於資格不符合,申請失敗\n");
						/*
						 * InetAddress.getHostAddress() 可取得本機IP位址
						 * req.getServerPort() 本機連結阜(8081)
						 */
						messageText.append("<交換網> http://"
								+ address.getHostAddress() + ":"
								+ req.getServerPort()
								+ "/ZA101G1/front/index/index.jsp \n");
						messageText.append("感謝你的申請 \n");
						// 將信件的內容存到list
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

					// 計算使用者寄信的數目
					currentSend++;
				}// 如果該筆請求要變更的狀態不是空值且 也不等於0 (0代表沒有選擇任何改狀態的請求)
			}

			// 如果有信件要寄的話 currentSend(計算要寄的信的次數)
			if (currentSend != 0) {
				System.out.println("還有 " + contentList.size() + " 封要寄 ");
				mail_service = new Thread[currentSend];
				for (int i = 0; i < mail_service.length; i++) {
					mail_service[i] = new Thread(this);
					mail_service[i].setPriority(Thread.MIN_PRIORITY);
					mail_service[i].start();
				}
				totalSend += currentSend;
				System.out.println("總共要寄總數: " + totalSend);
			}

			if (list != null && !list.isEmpty()) {
				session.setAttribute("memberList", list);
			}
			errorMessage.add("修改狀態成功");
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
				errorMessage.add("請輸入會員編號");
				req.setAttribute("errorMessage", errorMessage);
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;
			}
			MemberVO membervo = new MemberService()
					.getOneMemberByMemberId(member_id);
			if (membervo == null || membervo.getMember_id() == null) {
				errorMessage.add("查無此人");
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
				errorMessage.add("請輸入會員編號");
				req.setAttribute("errorMessage", errorMessage);
				RequestDispatcher view = req.getRequestDispatcher(requestURL);
				view.forward(req, res);
				return;
			}
			List<MemberVO> memberList = new MemberService().getSearchWish(req
					.getParameterMap());
			if (memberList == null || memberList.isEmpty()) {
				errorMessage.add("查無此人");
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
		// fb登入的請求
		if ("FBlogin".equals(action)) {
			List<String> errorMessage = new LinkedList<String>();
			req.setAttribute("errorMessage", errorMessage);
			String url = (String) req.getParameter("url");
			String location = (String) req.getSession()
					.getAttribute("location");
			// 取得fb會員資料
			String member_id = req.getParameter("fbId");
			String fbEmail = req.getParameter("fbEmail");
			String fbName = req.getParameter("fbName");

			// 如果資料正確
			if ((member_id != null && member_id.trim().length() != 0)
					|| (fbEmail != null && fbEmail.trim().length() != 0)
					|| (fbName != null && fbName.trim().length() != 0)) {
				MemberVO membervo = new MemberVO();
				membervo.setMember_id(member_id);

				// 用帳號找這個會員有沒有登入過
				MemberVO fbvo = new MemberService()
						.getOneMemberByMemberId(member_id);
				// 如果已經登入過
				if (fbvo != null) {
					HttpSession session = req.getSession();
					session.setAttribute("member_id", member_id);
					session.setAttribute("member", fbvo);
					if (location == null) {
						// 轉回原頁面
						RequestDispatcher view = req.getRequestDispatcher(url);
						view.forward(req, res);
						return;
					}

					if (location != null) {
						res.sendRedirect(location);
						return;
					}
				}
				// 沒登入過就新增一個member物件然後轉交到更新會員資料頁面
				membervo.setEmail(fbEmail);
				membervo.setMem_name(fbName);
				try {
					ServletContext context = req.getServletContext();
					// 因為fb傳回的資料沒有圖,從專案資料夾裡面抓圖
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
					// 會員圖片,給他一張fb預覽圖
					membervo.setPic(image);

				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}

				// 如果成功轉交到會員資料更新頁面,要求該會員輸入地址
				HttpSession session = req.getSession();
				
				session.setAttribute("member", membervo);
				//給更新頁面用的
				req.setAttribute("MemberVO", membervo);
				errorMessage
						.add("感謝您使用交換網,初次使用請填入您的基本資料與個人圖片更新,謝謝您的使用.<b>按下確認修改,登入程序才完成喔</b>");
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/facebookUpdateMember.jsp");
				view.forward(req, res);

			} else {
				errorMessage.add("使用Facebook帳號登入失敗.請使用交換網會員登入,或註冊交換網會員");
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
				errorMessage.add("請輸入帳號密碼");
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/member_login.jsp");
				view.forward(req, res);
				return;
			}

			MemberVO member = new MemberService().getOneMemberByEmail(email);

			if (member == null || member.getEmail() == null
					|| member.getMem_status() == 11) {
				errorMessage.add("帳號輸入錯誤");
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/member_login.jsp");
				view.forward(req, res);
				return;
			}

			if (!password.equals(member.getPassword())) {
				errorMessage.add("密碼輸入錯誤");
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/member_login.jsp");
				view.forward(req, res);
				return;
			}

			if (member.getMem_status() == 10) {
				errorMessage.add("此帳號還在審核中");
				RequestDispatcher view = req
						.getRequestDispatcher("/front/member/member_login.jsp");
				view.forward(req, res);
				return;
			} else if (member.getMem_status() == 21
					|| member.getMem_status() == 31) {
				errorMessage.add("此帳號已停權");
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

	// 信封的index
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

	// mailService.start()執行...
	@Override
	public void run() {
		sendMail();
		if (toList == null || toList.isEmpty()) {
			System.out.println("信件已經寄完");
			totalSend = 0;
		}
	}

	// //送信會有點累格 為了避免請求重復送出 執行完送信的方法後 停個2秒鐘等待信件寄送完成
	// try {
	// Thread.sleep(1500);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// //因為執行序無法重復使用,所以為了要讓使用者能夠一直發送信件,在執行序完成時,再重新生成一次

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
		String header = part.getHeader("content-disposition"); // 從前面第一個範例(版本1-基本測試)可得知此head的值
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
		// 年
		i[0] = Integer.parseInt(date.substring(6, date.length()));
		// 月
		i[1] = (Integer.parseInt(date.substring(0, 2)));
		if (i[1] > 1)
			i[1]--;
		// 日
		i[2] = Integer.parseInt(date.substring(3, 5));

		return i;
	}

}
