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
		
		if ("getOne_For_Display".equals(action)) { // �Ӧ۫��select_page.jsp���ШD

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
				String str = req.getParameter("pid");
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("�п�J���i�s��");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/post/select_page.jsp");
					failureView.forward(req, res);
					return;// �{�����_
				}

				Integer pid = null;
				try {
					pid = new Integer(str);
				} catch (Exception e) {
					errorMsgs.add("���i�s���榡�����T");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/post/select_page.jsp");
					failureView.forward(req, res);
					return;// �{�����_
				}

				/*************************** 2.�}�l�d�߸�� *****************************************/
				PostService postSvc = new PostService();
				PostVO postVO = postSvc.getOnePost(pid);
				if (postVO == null) {
					errorMsgs.add("�d�L���");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/post/select_page.jsp");
					failureView.forward(req, res);
					return;// �{�����_
				}

				/*************************** 3.�d�ߧ���,�ǳ����(Send the Success view) *************/
				req.setAttribute("postVO", postVO); // ��Ʈw���X��empVO����,�s�Jreq
				String url = "/back/post/listOnePost.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // ���\���listOneEmp.jsp
				successView.forward(req, res);

				/*************************** ��L�i�઺���~�B�z *************************************/
			} catch (Exception e) {
				errorMsgs.add("�L�k���o���:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/post/select_page.jsp");
				failureView.forward(req, res);
			}
		}

		if ("getOne_For_Update".equals(action)) { // �Ӧ�listAllPost.jsp���ШD ��
													// listPosts_ByEmpid.jsp ���ШD

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String requestURL = req.getParameter("requestURL");
			String whichPage = req.getParameter("whichPage");

			try {
				/*************************** 1.�����ШD�Ѽ� ***************************************/
				Integer pid = new Integer(req.getParameter("pid"));
				/*************************** 2.�}�l�d�߸�� ***************************************/
				PostService postSvc = new PostService();
				PostVO postVO = postSvc.getOnePost(pid);
				/*************************** 3.�d�ߧ���,�ǳ����(Send the Success view) ***********/
				req.setAttribute("postVO", postVO);
				String url = "/back/post/update_post_input.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				/*************************** ��L�i�઺���~�B�z *************************************/
			} catch (Exception e) {
				errorMsgs.add("�ק��Ʈɨ��X���~" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
			}

		}

		if ("update".equals(action)) { // �Ӧ�update_post_input.jsp���ШD

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String requestURL = req.getParameter("requestURL"); // �e�X�ק諸�ӷ��������|:
																// �i�ର�i/emp/listAllEmp.jsp�j
																// ��
																// �i/dept/listEmps_ByDeptno.jsp�j
																// �� �i
																// /dept/listAllDept.jsp�j

			try {
				/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
				Integer pid = new Integer(req.getParameter("pid").trim());
				String title = req.getParameter("title").trim();
				String post = req.getParameter("post").trim();
				String empid = req.getParameter("empid").trim();

				
				
				Collection<Part> parts = req.getParts();
				byte[] picByte = null;
				InputStream pic = null;
				for(Part part : parts){
					// �Ĥ@��if ���P�_���S���W�ǪF��
					if ("pic".equals(part.getName())
							&& getFileNameFromPart(part) != null) {
						//�p�G���F��,�A�P�_�O���O�Ϥ�
						if ("jpg".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
								|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
								|| "jpeg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0) {
							pic = part.getInputStream();

						}else{
							errorMsgs.add("�W���ɮ׮榡�������Ϥ�(JPG,PNG,JPEG)");
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
					req.setAttribute("postVO", postVO); // �t����J�榡���~��empVO����,�]�s�Jreq
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/post/update_post_input.jsp");
					failureView.forward(req, res);
					return; // �{�����_
				}

				/*************************** 2.�}�l�ק��� *****************************************/
				
				int update = new PostService().updatePostByObject(postVO);
				if(update != 1){
					errorMsgs.add("��ƭק異��,�ЦA�դ@�����p���t�κ޲z��");
					req.setAttribute("postVO", postVO); // �t����J�榡���~��empVO����,�]�s�Jreq
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/post/update_post_input.jsp");
					failureView.forward(req, res);
					return; // �{�����_
				}
				/*************************** 3.�ק粒��,�ǳ����(Send the Success view) *************/
				if (requestURL.equals("/back/post/listPosts_ByEmpid.jsp")) {
					req.setAttribute("listPosts_ByEmpid",
							postVO);
				}
				String url = requestURL;
				RequestDispatcher successView = req.getRequestDispatcher(url); // �ק令�\��,���^�e�X�ק諸�ӷ�����
				successView.forward(req, res);

				/*************************** ��L�i�઺���~�B�z *************************************/
			} catch (Exception e) {
				errorMsgs.add("�ק��ƥ���:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/post/update_post_input.jsp");
				failureView.forward(req, res);
			}
		}

		if ("insert".equals(action)) { // �Ӧ�addEmp.jsp���ШD
		
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/*********************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z *************************/
				String title = req.getParameter("title").trim();
				String post = req.getParameter("post").trim();
				String empid = req.getParameter("empid").trim();

				Collection<Part> parts = req.getParts();
				InputStream pic = null;
				for (Part part : parts) {
					// �Ĥ@��if ���P�_���S���W�ǪF��
					if ("pic".equals(part.getName())
							&& getFileNameFromPart(part) != null) {
						//�p�G���F��,�A�P�_�O���O�Ϥ�
						if ("jpg"
								.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
								|| "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0
								|| "jpeg"
										.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0) {
							pic = part.getInputStream();

						}else{
							errorMsgs.add("�W���ɮ׮榡�������Ϥ�(JPG,PNG,JPEG)");
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
					req.setAttribute("postVO", postVO); // �t����J�榡���~��postVO����,�]�s�Jreq
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/post/addPost.jsp");
					failureView.forward(req, res);
					return;
				}

				/*************************** 2.�}�l�s�W��� ***************************************/
				PostService postSvc = new PostService();
				int  i = postSvc.addPostByObject(postVO);
				if(i==0){
					errorMsgs.add("�s�W���i����,�ЦA�դ@�����p���t�κ޲z��");
					req.setAttribute("postVO", postVO); // �t����J�榡���~��postVO����,�]�s�Jreq
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/post/addPost.jsp");
					failureView.forward(req, res);
					return;
				}

				/*************************** 3.�s�W����,�ǳ����(Send the Success view) ***********/
				String url = "/back/post/listAllPost.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
				successView.forward(req, res);

				/*************************** ��L�i�઺���~�B�z **********************************/
			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/post/addPost.jsp");
				failureView.forward(req, res);
			}
		}

		if ("listPosts_ByEmpid".equals(action)) { // �Ӧ�select_pages.jsp���ШD

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/*************************** 1.�����ШD�Ѽ� ***************************************/
				String empid = req.getParameter("empid");
				/*************************** 2.�}�l�d�߸�� ***************************************/
				PostService postSvc = new PostService();
				List<PostVO> listPosts_ByEmpid = postSvc.findPostByEmpid(empid);
				/*************************** 3.�d�ߧ���,�ǳ����(Send the Success view) ***********/
				req.setAttribute("listPosts_ByEmpid", listPosts_ByEmpid);
				String url = "/back/post/listPosts_ByEmpid.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				/*************************** ��L�i�઺���~�B�z *************************************/
			} catch (Exception e) {
				errorMsgs.add("�ק��Ʈɨ��X���~" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/post/select_page.jsp");
				failureView.forward(req, res);
			}

		}

		if ("delete".equals(action)) { // �Ӧ�listAllPost.jsp���ШD ��
										// listPosts_ByEmpid.jsp ���ШD

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			String requestURL = req.getParameter("requestURL"); // �e�X�R�����ӷ��������|:
																// �i�ର�i/emp/listAllEmp.jsp�j
																// ��
																// �i/dept/listEmps_ByDeptno.jsp�j
																// �� �i
																// /dept/listAllDept.jsp�j

			try {
				/*************************** 1.�����ШD�Ѽ� ***************************************/
				Integer pid = new Integer(req.getParameter("pid"));

				/*************************** 2.�}�l�R����� ***************************************/
				PostService postSvc = new PostService();
				PostVO postVO = postSvc.getOnePost(pid);
				postSvc.deletePostByPid(pid);

				/*************************** 3.�R������,�ǳ����(Send the Success view) ***********/
				if (requestURL.equals("/back/post/listPosts_ByEmpid.jsp")) {
					req.setAttribute("listPosts_ByEmpid",
							postSvc.findPostByEmpid(postVO.getEmpid())); // ��Ʈw���X��list����,�s�Jrequest
				}
				String url = requestURL;
				RequestDispatcher successView = req.getRequestDispatcher(url); // �R�����\��,���^�e�X�R�����ӷ�����
				successView.forward(req, res);

				/*************************** ��L�i�઺���~�B�z **********************************/
			} catch (Exception e) {
				errorMsgs.add("�R����ƥ���:" + e.getMessage());
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
