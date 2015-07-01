package com.emp.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.authority.model.AuthorityService;
import com.authority.model.AuthorityVO;
import com.emp.model.EmpService;
import com.emp.model.EmpVO;
import com.mail.model.MailService;

import sun.text.resources.CollationData;

/**
 * Servlet implementation class EmpServlet
 */
@WebServlet("/EmpServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 5 * 5 * 1024 * 1024)
public class EmpServlet extends HttpServlet implements Runnable{
	private static final long serialVersionUID = 1L;
	//�}�Ҥ@�Ӱ����
	private Thread mail_service;
	//���H�H�H�c
	private String to = "";
	//�H��D��
	private String subject = "";
	//�H�󤺮e
	private String messageText = "";
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		//��l�ư�����
		mail_service = new Thread(this);
		mail_service.setPriority(Thread.MIN_PRIORITY);
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmpServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	
    	res.setContentType("text/html ; charset=UTF-8");
    	req.setCharacterEncoding("UTF-8");
    	
    	List<String> errorMsgs = new LinkedList<String>();//�˨����~�T��
    	req.setAttribute("errorMsgs", errorMsgs);
    	
    	String pic_name = "";
    	String action = req.getParameter("action");
    	
    	if("empLogin".equals(action)){ //�Ӧ۩�loginEmp.jsp���ШD
    		
    		String empid = req.getParameter("empid").trim();
    		String password = req.getParameter("password").trim();
    		/***********************1.�����ШD�Ѽ� - ��J�榡�����~�B�z*************************/
    		try {
				if(empid.isEmpty()){
					errorMsgs.add("�п�J�b��");
				}
				if(password.isEmpty()){
					errorMsgs.add("�п�J�K�X");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/emp/loginEmp.jsp");
					failureView.forward(req, res);
					return; //�{�����_
				}
				/***************************2.�}�l�t����***************************************/
				EmpVO empVO = new EmpVO();
				empVO.setEmpid(empid);
				
				EmpService empSvc = new EmpService();
				boolean login = empSvc.loginEmp(empid, password);
				EmpVO empVOstatus = empSvc.getOneEmp(empid);
				if(login==false){
					errorMsgs.add("�b���αK�X�����T");
				}else
				if(empVOstatus.getStatus()==0){ //�P�_���u�O�_�b¾
					errorMsgs.add("�ӭ��u�w��¾");
				}
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("empVO", empVO); // �t����J�榡���~��empVO����,�]�s�Jreq
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/emp/loginEmp.jsp");
					failureView.forward(req, res);
					return; //�{�����_
				}
				/***************************3.�t�粒��,�ǳ����(Send the Success view)*************/
				HttpSession session = req.getSession();
				session.setAttribute("empid", empid);
				
				AuthorityService authoritySvc = new AuthorityService();
				List<AuthorityVO> list = authoritySvc.getOneAid(empid);
				session.setAttribute("authority", list);
				
				//�ݬݦ��S�ӷ����� (�p�G���h���ɤ�)
				String location = (String) session.getAttribute("location");
				if(location != null){
					session.removeAttribute("location");
					res.sendRedirect(location);
					return;
				} else {
					res.sendRedirect(req.getContextPath()+"/back/index/index.jsp");
				}
//				String url = "/back/index/index.jsp";
//				RequestDispatcher successView = req
//						.getRequestDispatcher(url);
//				successView.forward(req, res);
			} catch (Exception e) {
				errorMsgs.add("�b���αK�X�����T");
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/emp/loginEmp.jsp");
				failureView.forward(req, res);
			}
    	}
    	
    	if("logout".equals(action)){ //�Ӧ۩�index.jsp���ШD
    		
    		HttpSession session = req.getSession();
    		session.removeAttribute("empid");
    		String url = "/back/emp/loginEmp.jsp";
			RequestDispatcher successView = req
					.getRequestDispatcher(url);
			successView.forward(req, res);
    	}
    	
    	if("get_one_emp".equals(action)){ //�Ӧ۩�select_page.jsp���ШD
    		
    		try {
				String empid = req.getParameter("empid").trim();
				/***********************1.�����ШD�Ѽ� - ��J�榡�����~�B�z*************************/
				if(empid.isEmpty()){
					errorMsgs.add("���u�s������ť�");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/emp/select_page.jsp");
					failureView.forward(req, res);
					return; //�{�����_
				}
				if(empid.codePointAt(0)!=69){
					errorMsgs.add("���u�s���Ĥ@�Ӧr�n�j�g'E'");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/emp/select_page.jsp");
					failureView.forward(req, res);
					return; //�{�����_
				}
				/***************************2.�}�l�d�߸��***************************************/
				EmpService empSvc = new EmpService();
				EmpVO empVO = empSvc.getOneEmp(empid);
				if(empVO == null){
					errorMsgs.add("�d�L�����");
				}
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("empVO", empVO);
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/emp/select_page.jsp");
					failureView.forward(req, res);
					return; //�{�����_
				}
				/***************************3.�d�ߧ���,�ǳ����(Send the Success view)*************/
				req.setAttribute("empVO", empVO);
				String url = "/back/emp/showOneEmp.jsp";
				RequestDispatcher successView = req
						.getRequestDispatcher(url);
				successView.forward(req, res);
			} catch (Exception e) {
				errorMsgs.add("�L�k���o���:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/emp/select_page.jsp");
				failureView.forward(req, res);
			}
    	}
    	
    	if("insertEmp".equals(action)){ //�Ӧ۩�addEmp.jsp���ШD
    		
//    		try {
				InputStream is = null;
				Collection<Part> parts = req.getParts(); //�˵۩Ҧ��ШD��KEY��
				for(Part part : parts){
					if("pic".equals(part.getName())){ //�P�ШD�ѼƤǰtKEY��
						if(getFileNameFromPart(part) != null 
								&& ("jpg".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 ||
								 "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 || "jpeg"
								.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0)){
							pic_name = getFileNameFromPart(part);
							is = part.getInputStream();
							break;
						} else {
							errorMsgs.add("�Ϥ��W�Ǯ榡�����T. �榡������ JPG , JPEG , PNG");
							break;
						}
					}
				}
				/***********************1.�����ШD�Ѽ� - ��J�榡�����~�B�z*************************/
				String ename = req.getParameter("ename").trim();
				String email = req.getParameter("email").trim();
				
				if(ename.isEmpty()){
					errorMsgs.add("���u�m�W���ର�ť�");
				}  
				//�üƲ��ͱK�X�A�Ĥ@�Ӥj�g�^��A�ĤG�Ӥp�g�^��A�᭱���0~9�üơA(�Ҧp:Aa99)
				String number = "";
				number = String.valueOf((int)Math.floor(Math.random()*10))
						   +String.valueOf((int)Math.floor(Math.random()*10));
				char A = (char) (Math.floor(Math.random()*26)+65);
				char a = (char) (Math.floor(Math.random()*26)+97);
				String password = A+""+a+""+number;
				
				if(email.isEmpty()){
					errorMsgs.add("E-mail���ର�ť�");
				}
				
				java.sql.Date hiredate = null;
				try {
					hiredate = java.sql.Date.valueOf(req.getParameter("hiredate"));
				} catch (Exception e) {
					hiredate = new java.sql.Date(System.currentTimeMillis());
					errorMsgs.add("�п�J���!");
				}
				
				Integer status = new Integer(1); //�w�]���u�b¾���A��1
				
				EmpVO empVO = new EmpVO();
				empVO.setEname(ename);
				empVO.setHiredate(hiredate);
				empVO.setEmail(email);
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("empVO", empVO); // �t����J�榡���~��empVO����,�]�s�Jreq
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/emp/addEmp.jsp");
					failureView.forward(req, res);
					return; //�{�����_
				}
				
				
				/***************************2.�}�l�s�W���***************************************/
				
				EmpService empSvc = new EmpService();
				String empPk = "";
				try {
					//�Ϥ��g�J
//					ByteArrayOutputStream baos = new ByteArrayOutputStream();
//					byte[] buffer = new byte[is.available()];
//					int len = 0;
//					while((len = is.read(buffer)) != -1){
//						baos.write(buffer,0,len);
//					}
//					baos.flush();
//					byte[] pic = baos.toByteArray();
					byte[] pic = new byte[is.available()]; //�@���g�J
					is.read(pic);
					is.close();
					empPk = empSvc.addEmpGetPrimaryKey(ename, password, hiredate, pic, email, status); //�s�W��ơA���o���u�b��
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//�H�X²�T
				to = empVO.getEmail();
				subject = "���u�b���P�K�X�q��";
				String empid = empPk;
				String passRandom = password;
				messageText = "Hello!�y"+ename+"�z�z�����u�b����:"+empid+"�A�K�X��:"+passRandom+"�A���԰O���b���K�X�C";
				//�ҰʭI�������
				mail_service.start();
				
				/***************************3.�d�ߧ���,�ǳ����(Send the Success view)*************/
				String url = "/back/emp/showAllEmp.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
				/***************************��L�i�઺���~�B�z*************************************/
//			} catch (Exception e) {
//				errorMsgs.add("�L�k���o���:" + e.getMessage());
//				RequestDispatcher failureView = req
//						.getRequestDispatcher("/back/emp/addEmp.jsp");
//				failureView.forward(req, res);
//			}
    	}
    	
    	if("update_For_One".equals(action)){ //�Ӧ�showAllEmp,jsp��showOneEmp.jsp���ШD 
    		try {
    			String requestURL = req.getParameter("requestURL"); //����showAllEmp.jsp��showOneEmp.jsp����e���|
    			String whichPage = req.getParameter("whichPage");  	//����showAllEmp.jsp����e����
				/***************************1.�����ШD�Ѽ�***************************************/
				String empid = req.getParameter("empid");
				/***************************2.�}�l�d�߸��***************************************/
				EmpService empSvc = new EmpService();
				EmpVO empVO = empSvc.getOneEmp(empid);
				/***************************3.�d�ߧ���,�ǳ����(Send the Success view)***********/
				req.setAttribute("empVO", empVO);
				String url = "/back/emp/updateEmp.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				/***************************��L�i�઺���~�B�z*************************************/
			} catch (Exception e) {
				errorMsgs.add("�L�k���o���:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/emp/updateEmp.jsp");
				failureView.forward(req, res);
			}
    	}
    	
    	if("updateEmp".equals(action)){ //�Ӧ�updateEmp,jsp���ШD
    			try {
    				//�Ϥ��B�z
					InputStream is = null;
					Collection<Part> parts = req.getParts(); //�˵۩Ҧ��ШD��KEY��
					for(Part part : parts){
						if("pic".equals(part.getName())){ //�P�ШD�ѼƤǰtKEY��
							if(getFileNameFromPart(part) != null 
									&& ("jpg".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 ||
									 "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 || "jpeg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0)){
								pic_name = getFileNameFromPart(part);
								is = part.getInputStream();
								break;
							} else if(getFileNameFromPart(part) == null) //�P�_�Y�L�W�ǹϤ�
								break;
							else {
								errorMsgs.add("�Ϥ��W�Ǯ榡�����T. �榡������ JPG , JPEG , PNG");
								break;
							}
						}
					}
					String requestURL = req.getParameter("requestURL"); //����showAllEmp.jsp��showOneEmp.jsp����e���|
					/***********************1.�����ШD�Ѽ� - ��J�榡�����~�B�z*************************/
					String empid = req.getParameter("empid").trim();
					String ename = req.getParameter("ename").trim();
					String password = req.getParameter("password").trim();
					String rePwd = req.getParameter("rePwd").trim();
					String email = req.getParameter("email").trim();
					Integer status = new Integer(req.getParameter("status").trim());
					
					if(ename.isEmpty()){
						errorMsgs.add("���u�m�W���ର�ť�");
					}  
					if(password.isEmpty() || rePwd.isEmpty()){
						errorMsgs.add("�K�X���ର�ť�");
					}
					if(!password.equals(rePwd)){
						errorMsgs.add("�K�X���ƿ�J���~");
					} 
					if(email.isEmpty()){
						errorMsgs.add("E-mail���ର�ť�");
					}
					
					EmpService empSvc = new EmpService();
					EmpVO empVO = empSvc.getOneEmp(empid);
					empVO.setEname(ename);
					empVO.setPassword(password);
					empVO.setEmail(email);
					empVO.setStatus(status);
					
					if (!errorMsgs.isEmpty()) {
						req.setAttribute("empVO", empVO); // �t����J�榡���~��empVO����,�]�s�Jreq
						RequestDispatcher failureView = req
								.getRequestDispatcher("/back/emp/updateEmp.jsp");
						failureView.forward(req, res);
						return; //�{�����_
					}
					/***************************2.�}�l�ק���***************************************/
					if(is != null){ //�P�_�S�W�ǹϤ�
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte[] buffer = new byte[is.available()];
						int len = 0;
						while((len = is.read(buffer)) != -1){
							baos.write(buffer,0,len);
						}
						baos.flush();
						byte[] pic = baos.toByteArray();
						baos.close();
						is.close();
						empVO.setPic(pic);
//						byte[] pic = new byte[is.available()]; //�@���g�J
//						empVO.setPic(pic);
					}
					empSvc.updateOneEmp(empVO);
					/***************************3.�ק粒��,�ǳ����(Send the Success view)***********/
					//�P�_���|�öǭ�
					if("/back/emp/showOneEmp.jsp".equals(requestURL)){
						req.setAttribute("empVO", empVO);
					}
					if("/back/emp/showEmpByQuery.jsp".equals(requestURL)){
						HttpSession session = req.getSession();
						HashMap<String, String[]> map = (HashMap<String, String[]>) session.getAttribute("map");
			    		List<EmpVO> list = empSvc.getAllComposite(map);
						req.setAttribute("empList_compositeQuery", list);
					}
					String url = requestURL;
					RequestDispatcher successView = req.getRequestDispatcher(url);
					successView.forward(req, res);
					/***************************��L�i�઺���~�B�z*************************************/
				} catch (Exception e) {
					errorMsgs.add("�L�k���o���:" + e.getMessage());
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/emp/updateEmp.jsp");
					failureView.forward(req, res);
				}
    	}
    	
    	if("emp_compositeQuery".equals(action)){
    		
    		HttpSession session = req.getSession();
    		Map<String, String[]> map = (Map<String, String[]>) session.getAttribute("map");
    		if(req.getParameter("whichPage") == null){
    			HashMap<String, String[]> map1 = (HashMap<String, String[]>)req.getParameterMap();
    			HashMap<String, String[]> map2 = new HashMap<String, String[]>();
    			map2 = (HashMap<String, String[]>)map1.clone();
    			session.setAttribute("map", map2);
    			map = (HashMap<String, String[]>)req.getParameterMap();
    		}
    		
    		EmpService empSvc = new EmpService();
    		List<EmpVO> list = empSvc.getAllComposite(map);
    		
    		req.setAttribute("empList_compositeQuery", list);
    		String url = "/back/emp/showEmpByQuery.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);
			successView.forward(req, res);
    		
    		
    	}
    	
//    	if("deleteEmp".equals(action)){ //�Ӧ�showAllEmp,jsp���ШD
//    		try {
//				/***************************1.�����ШD�Ѽ�***************************************/
//				String empid = req.getParameter("empid");
//				/***************************2.�}�l�R�����***************************************/
//				EmpService empSvc = new EmpService();
//				empSvc.deleteEmp(empid);
//				/***************************3.�R������,�ǳ����(Send the Success view)***********/
//				String url = "/back/emp/showAllEmp.jsp";
//				RequestDispatcher successView = req.getRequestDispatcher(url);
//				successView.forward(req, res);
//				/***************************��L�i�઺���~�B�z*************************************/
//			} catch (Exception e) {
//				errorMsgs.add("�L�k���o���:" + e.getMessage());
//				RequestDispatcher failureView = req
//						.getRequestDispatcher("/back/emp/showAllEmp.jsp");
//				failureView.forward(req, res);
//			}
//    	}
    }
    
    public String getFileNameFromPart(Part part){ //���X�W�Ǫ��ɮצW�� (�]��API������method,�ҥH�����ۦ漶�g)
    	String header = part.getHeader("content-disposition");
    	String filename = header.substring(header.lastIndexOf("=") + 2, header.length() - 1);
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
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(req, res);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(req, res);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("�H�H");
		System.out.println(to+"     "+subject+"      "+messageText);
		com.tool.MailService mailService = new com.tool.MailService();
		mailService.sendMail(to, subject, messageText);
		//�]��������L�k���ƨϥΡA�ҥH���F�୫�Ƶo�H�A�b�����򧹦��ɡA�A���s�ͦ��@��
		mail_service = new Thread(this);
		mail_service.setPriority(Thread.MIN_PRIORITY);
	}

}
