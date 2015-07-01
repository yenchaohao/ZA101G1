package com.post.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.emp.model.EmpService;
import com.emp.model.EmpVO;
import com.post.model.PostService;
import com.post.model.PostVO;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 5 * 5 * 1024 * 1024)
public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void process(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		res.setContentType("text/html ; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		
		String action = req.getParameter("action");
		
		if ("getOne_For_Display".equals(action)) { // 來自後端select_page.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
				String str = req.getParameter("pid");
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入公告編號");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/post/select_page.jsp");
					failureView.forward(req, res);
					return;// 程式中斷
				}

				Integer pid = null;
				try {
					pid = new Integer(str);
				} catch (Exception e) {
					errorMsgs.add("公告編號格式不正確");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/post/select_page.jsp");
					failureView.forward(req, res);
					return;// 程式中斷
				}

				/*************************** 2.開始查詢資料 *****************************************/
				PostService postSvc = new PostService();
				PostVO postVO = postSvc.getOnePost(pid);
				if (postVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/post/select_page.jsp");
					failureView.forward(req, res);
					return;// 程式中斷
				}

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
				req.setAttribute("postVO", postVO); // 資料庫取出的empVO物件,存入req
				String url = "/back/post/listOnePost.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交listOneEmp.jsp
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 *************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/post/select_page.jsp");
				failureView.forward(req, res);
			}
		}

		if ("getOne_For_Update".equals(action)) { // 來自listAllPost.jsp的請求 或
													// listPosts_ByEmpid.jsp 的請求

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String requestURL = req.getParameter("requestURL");
			String whichPage = req.getParameter("whichPage");

			try {
				/*************************** 1.接收請求參數 ***************************************/
				Integer pid = new Integer(req.getParameter("pid"));
				/*************************** 2.開始查詢資料 ***************************************/
				PostService postSvc = new PostService();
				PostVO postVO = postSvc.getOnePost(pid);
				/*************************** 3.查詢完成,準備轉交(Send the Success view) ***********/
				req.setAttribute("postVO", postVO);
				String url = "/back/post/update_post_input.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				/*************************** 其他可能的錯誤處理 *************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料時取出錯誤" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
			}

		}

		if ("update".equals(action)) { // 來自update_post_input.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String requestURL = req.getParameter("requestURL"); // 送出修改的來源網頁路徑:
																// 可能為【/emp/listAllEmp.jsp】
																// 或
																// 【/dept/listEmps_ByDeptno.jsp】
																// 或 【
																// /dept/listAllDept.jsp】

			try {
				/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
				Integer pid = new Integer(req.getParameter("pid").trim());
				String title = req.getParameter("title").trim();
				String post = req.getParameter("post").trim();
				String empid = req.getParameter("empid").trim();

				
				
				Collection<Part> parts = req.getParts();
				byte[] picByte = null;
				InputStream pic = null;
				for(Part part : parts){
					// 第一個if 先判斷有沒有上傳東西
					if ("pic".equals(part.getName())
							&& getFileNameFromPart(part) != null) {
						//如果有東西,再判斷是不是圖片
						if ("jpg".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
								|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
								|| "jpeg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0) {
							pic = part.getInputStream();

						}else{
							errorMsgs.add("上傳檔案格式必須為圖片(JPG,PNG,JPEG)");
						}

					}
				}
				if(pic != null){
					picByte =  InputStreamConvertByteArray(pic);
				}
				
				PostVO postVO = new PostService().getOnePost(pid);
				if(title != null && title.length() > 0 )
				postVO.setTitle(title);
				if(post != null && post.length() > 0)
				postVO.setPost(post);
				if(empid != null && empid.length() > 0)
				postVO.setEmpid(empid);
				if(picByte != null)
					postVO.setPic(picByte);
				
				
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("postVO", postVO); // 含有輸入格式錯誤的empVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/post/update_post_input.jsp");
					failureView.forward(req, res);
					return; // 程式中斷
				}

				/*************************** 2.開始修改資料 *****************************************/
				
				int update = new PostService().updatePostByObject(postVO);
				if(update != 1){
					errorMsgs.add("資料修改失敗,請再試一次或聯絡系統管理員");
					req.setAttribute("postVO", postVO); // 含有輸入格式錯誤的empVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/post/update_post_input.jsp");
					failureView.forward(req, res);
					return; // 程式中斷
				}
				/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
				if (requestURL.equals("/back/post/listPosts_ByEmpid.jsp")) {
					req.setAttribute("listPosts_ByEmpid",
							postVO);
				}
				String url = requestURL;
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交回送出修改的來源網頁
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 *************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/post/update_post_input.jsp");
				failureView.forward(req, res);
			}
		}

		if ("insert".equals(action)) { // 來自addEmp.jsp的請求
		
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/*********************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/
				String title = req.getParameter("title").trim();
				String post = req.getParameter("post").trim();
				String empid = req.getParameter("empid").trim();

				Collection<Part> parts = req.getParts();
				InputStream pic = null;
				for (Part part : parts) {
					// 第一個if 先判斷有沒有上傳東西
					if ("pic".equals(part.getName())
							&& getFileNameFromPart(part) != null) {
						//如果有東西,再判斷是不是圖片
						if ("jpg"
								.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
								|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
								|| "jpeg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0) {
							pic = part.getInputStream();

						}else{
							errorMsgs.add("上傳檔案格式必須為圖片(JPG,PNG,JPEG)");
						}

					}
				}
				
				byte[] picByte = null;
				if(pic != null){
					picByte = InputStreamConvertByteArray(pic);
				}
				
				
				
				PostVO postVO = new PostVO();
				postVO.setTitle(title);
				postVO.setPost(post);
				postVO.setEmpid(empid);
				postVO.setPic(picByte);

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("postVO", postVO); // 含有輸入格式錯誤的postVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/post/addPost.jsp");
					failureView.forward(req, res);
					return;
				}

				/*************************** 2.開始新增資料 ***************************************/
				PostService postSvc = new PostService();
				int  i = postSvc.addPostByObject(postVO);
				if(i==0){
					errorMsgs.add("新增公告失敗,請再試一次或聯絡系統管理員");
					req.setAttribute("postVO", postVO); // 含有輸入格式錯誤的postVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/post/addPost.jsp");
					failureView.forward(req, res);
					return;
				}

				/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
				String url = "/back/post/listAllPost.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/post/addPost.jsp");
				failureView.forward(req, res);
			}
		}

		if ("listPosts_ByEmpid".equals(action)) { // 來自select_pages.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/*************************** 1.接收請求參數 ***************************************/
				String empid = req.getParameter("empid");
				/*************************** 2.開始查詢資料 ***************************************/
				PostService postSvc = new PostService();
				List<PostVO> listPosts_ByEmpid = postSvc.findPostByEmpid(empid);
				/*************************** 3.查詢完成,準備轉交(Send the Success view) ***********/
				req.setAttribute("listPosts_ByEmpid", listPosts_ByEmpid);
				String url = "/back/post/listPosts_ByEmpid.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				/*************************** 其他可能的錯誤處理 *************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料時取出錯誤" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/post/select_page.jsp");
				failureView.forward(req, res);
			}

		}

		if ("delete".equals(action)) { // 來自listAllPost.jsp的請求 或
										// listPosts_ByEmpid.jsp 的請求

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			String requestURL = req.getParameter("requestURL"); // 送出刪除的來源網頁路徑:
																// 可能為【/emp/listAllEmp.jsp】
																// 或
																// 【/dept/listEmps_ByDeptno.jsp】
																// 或 【
																// /dept/listAllDept.jsp】

			try {
				/*************************** 1.接收請求參數 ***************************************/
				Integer pid = new Integer(req.getParameter("pid"));

				/*************************** 2.開始刪除資料 ***************************************/
				PostService postSvc = new PostService();
				PostVO postVO = postSvc.getOnePost(pid);
				postSvc.deletePostByPid(pid);

				/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
				if (requestURL.equals("/back/post/listPosts_ByEmpid.jsp")) {
					req.setAttribute("listPosts_ByEmpid",
							postSvc.findPostByEmpid(postVO.getEmpid())); // 資料庫取出的list物件,存入request
				}
				String url = requestURL;
				RequestDispatcher successView = req.getRequestDispatcher(url); // 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				errorMsgs.add("刪除資料失敗:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
			}
		}

	}

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
	
	private byte[] InputStreamConvertByteArray(InputStream in) {
		byte[] buffer = new byte[4 * 1024];
		try {
			ByteArrayOutputStream byteout = new ByteArrayOutputStream();
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				byteout.write(buffer, 0, len);
			}
			byteout.flush();

			return byteout.toByteArray();

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;

	}
}
