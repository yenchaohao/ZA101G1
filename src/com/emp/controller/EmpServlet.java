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
	//開啟一個執行緒
	private Thread mail_service;
	//收信人信箱
	private String to = "";
	//信件主旨
	private String subject = "";
	//信件內容
	private String messageText = "";
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		//初始化執行續
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
    	
    	List<String> errorMsgs = new LinkedList<String>();//裝取錯誤訊息
    	req.setAttribute("errorMsgs", errorMsgs);
    	
    	String pic_name = "";
    	String action = req.getParameter("action");
    	
    	if("empLogin".equals(action)){ //來自於loginEmp.jsp的請求
    		
    		String empid = req.getParameter("empid").trim();
    		String password = req.getParameter("password").trim();
    		/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
    		try {
				if(empid.isEmpty()){
					errorMsgs.add("請輸入帳號");
				}
				if(password.isEmpty()){
					errorMsgs.add("請輸入密碼");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/emp/loginEmp.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				/***************************2.開始配對資料***************************************/
				EmpVO empVO = new EmpVO();
				empVO.setEmpid(empid);
				
				EmpService empSvc = new EmpService();
				boolean login = empSvc.loginEmp(empid, password);
				EmpVO empVOstatus = empSvc.getOneEmp(empid);
				if(login==false){
					errorMsgs.add("帳號或密碼不正確");
				}else
				if(empVOstatus.getStatus()==0){ //判斷員工是否在職
					errorMsgs.add("該員工已離職");
				}
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("empVO", empVO); // 含有輸入格式錯誤的empVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/emp/loginEmp.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				/***************************3.配對完成,準備轉交(Send the Success view)*************/
				HttpSession session = req.getSession();
				session.setAttribute("empid", empid);
				
				AuthorityService authoritySvc = new AuthorityService();
				List<AuthorityVO> list = authoritySvc.getOneAid(empid);
				session.setAttribute("authority", list);
				
				//看看有沒來源網頁 (如果有則重導之)
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
				errorMsgs.add("帳號或密碼不正確");
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/emp/loginEmp.jsp");
				failureView.forward(req, res);
			}
    	}
    	
    	if("logout".equals(action)){ //來自於index.jsp的請求
    		
    		HttpSession session = req.getSession();
    		session.removeAttribute("empid");
    		String url = "/back/emp/loginEmp.jsp";
			RequestDispatcher successView = req
					.getRequestDispatcher(url);
			successView.forward(req, res);
    	}
    	
    	if("get_one_emp".equals(action)){ //來自於select_page.jsp的請求
    		
    		try {
				String empid = req.getParameter("empid").trim();
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				if(empid.isEmpty()){
					errorMsgs.add("員工編號不能空白");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/emp/select_page.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				if(empid.codePointAt(0)!=69){
					errorMsgs.add("員工編號第一個字要大寫'E'");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/emp/select_page.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				/***************************2.開始查詢資料***************************************/
				EmpService empSvc = new EmpService();
				EmpVO empVO = empSvc.getOneEmp(empid);
				if(empVO == null){
					errorMsgs.add("查無此資料");
				}
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("empVO", empVO);
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/emp/select_page.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("empVO", empVO);
				String url = "/back/emp/showOneEmp.jsp";
				RequestDispatcher successView = req
						.getRequestDispatcher(url);
				successView.forward(req, res);
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/emp/select_page.jsp");
				failureView.forward(req, res);
			}
    	}
    	
    	if("insertEmp".equals(action)){ //來自於addEmp.jsp的請求
    		
//    		try {
				InputStream is = null;
				Collection<Part> parts = req.getParts(); //裝著所有請求的KEY值
				for(Part part : parts){
					if("pic".equals(part.getName())){ //與請求參數匹配KEY值
						if(getFileNameFromPart(part) != null 
								&& ("jpg".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 ||
								 "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 || "jpeg"
								.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0)){
							pic_name = getFileNameFromPart(part);
							is = part.getInputStream();
							break;
						} else {
							errorMsgs.add("圖片上傳格式不正確. 格式必須為 JPG , JPEG , PNG");
							break;
						}
					}
				}
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				String ename = req.getParameter("ename").trim();
				String email = req.getParameter("email").trim();
				
				if(ename.isEmpty()){
					errorMsgs.add("員工姓名不能為空白");
				}  
				//亂數產生密碼，第一個大寫英文，第二個小寫英文，後面兩個0~9亂數，(例如:Aa99)
				String number = "";
				number = String.valueOf((int)Math.floor(Math.random()*10))
						   +String.valueOf((int)Math.floor(Math.random()*10));
				char A = (char) (Math.floor(Math.random()*26)+65);
				char a = (char) (Math.floor(Math.random()*26)+97);
				String password = A+""+a+""+number;
				
				if(email.isEmpty()){
					errorMsgs.add("E-mail不能為空白");
				}
				
				java.sql.Date hiredate = null;
				try {
					hiredate = java.sql.Date.valueOf(req.getParameter("hiredate"));
				} catch (Exception e) {
					hiredate = new java.sql.Date(System.currentTimeMillis());
					errorMsgs.add("請輸入日期!");
				}
				
				Integer status = new Integer(1); //預設員工在職狀態為1
				
				EmpVO empVO = new EmpVO();
				empVO.setEname(ename);
				empVO.setHiredate(hiredate);
				empVO.setEmail(email);
				
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("empVO", empVO); // 含有輸入格式錯誤的empVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back/emp/addEmp.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				
				/***************************2.開始新增資料***************************************/
				
				EmpService empSvc = new EmpService();
				String empPk = "";
				try {
					//圖片寫入
//					ByteArrayOutputStream baos = new ByteArrayOutputStream();
//					byte[] buffer = new byte[is.available()];
//					int len = 0;
//					while((len = is.read(buffer)) != -1){
//						baos.write(buffer,0,len);
//					}
//					baos.flush();
//					byte[] pic = baos.toByteArray();
					byte[] pic = new byte[is.available()]; //一次寫入
					is.read(pic);
					is.close();
					empPk = empSvc.addEmpGetPrimaryKey(ename, password, hiredate, pic, email, status); //新增資料，取得員工帳號
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//寄出簡訊
				to = empVO.getEmail();
				subject = "員工帳號與密碼通知";
				String empid = empPk;
				String passRandom = password;
				messageText = "Hello!『"+ename+"』您的員工帳號為:"+empid+"，密碼為:"+passRandom+"，請謹記此帳號密碼。";
				//啟動背景執行緒
				mail_service.start();
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				String url = "/back/emp/showAllEmp.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理*************************************/
//			} catch (Exception e) {
//				errorMsgs.add("無法取得資料:" + e.getMessage());
//				RequestDispatcher failureView = req
//						.getRequestDispatcher("/back/emp/addEmp.jsp");
//				failureView.forward(req, res);
//			}
    	}
    	
    	if("update_For_One".equals(action)){ //來自showAllEmp,jsp或showOneEmp.jsp的請求 
    		try {
    			String requestURL = req.getParameter("requestURL"); //接收showAllEmp.jsp或showOneEmp.jsp的當前路徑
    			String whichPage = req.getParameter("whichPage");  	//接收showAllEmp.jsp的當前頁面
				/***************************1.接收請求參數***************************************/
				String empid = req.getParameter("empid");
				/***************************2.開始查詢資料***************************************/
				EmpService empSvc = new EmpService();
				EmpVO empVO = empSvc.getOneEmp(empid);
				/***************************3.查詢完成,準備轉交(Send the Success view)***********/
				req.setAttribute("empVO", empVO);
				String url = "/back/emp/updateEmp.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back/emp/updateEmp.jsp");
				failureView.forward(req, res);
			}
    	}
    	
    	if("updateEmp".equals(action)){ //來自updateEmp,jsp的請求
    			try {
    				//圖片處理
					InputStream is = null;
					Collection<Part> parts = req.getParts(); //裝著所有請求的KEY值
					for(Part part : parts){
						if("pic".equals(part.getName())){ //與請求參數匹配KEY值
							if(getFileNameFromPart(part) != null 
									&& ("jpg".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 ||
									 "png".compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0 || "jpeg"
									.compareToIgnoreCase(getFileFormat(getFileNameFromPart(part))) == 0)){
								pic_name = getFileNameFromPart(part);
								is = part.getInputStream();
								break;
							} else if(getFileNameFromPart(part) == null) //判斷若無上傳圖片
								break;
							else {
								errorMsgs.add("圖片上傳格式不正確. 格式必須為 JPG , JPEG , PNG");
								break;
							}
						}
					}
					String requestURL = req.getParameter("requestURL"); //接收showAllEmp.jsp或showOneEmp.jsp的當前路徑
					/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
					String empid = req.getParameter("empid").trim();
					String ename = req.getParameter("ename").trim();
					String password = req.getParameter("password").trim();
					String rePwd = req.getParameter("rePwd").trim();
					String email = req.getParameter("email").trim();
					Integer status = new Integer(req.getParameter("status").trim());
					
					if(ename.isEmpty()){
						errorMsgs.add("員工姓名不能為空白");
					}  
					if(password.isEmpty() || rePwd.isEmpty()){
						errorMsgs.add("密碼不能為空白");
					}
					if(!password.equals(rePwd)){
						errorMsgs.add("密碼重複輸入錯誤");
					} 
					if(email.isEmpty()){
						errorMsgs.add("E-mail不能為空白");
					}
					
					EmpService empSvc = new EmpService();
					EmpVO empVO = empSvc.getOneEmp(empid);
					empVO.setEname(ename);
					empVO.setPassword(password);
					empVO.setEmail(email);
					empVO.setStatus(status);
					
					if (!errorMsgs.isEmpty()) {
						req.setAttribute("empVO", empVO); // 含有輸入格式錯誤的empVO物件,也存入req
						RequestDispatcher failureView = req
								.getRequestDispatcher("/back/emp/updateEmp.jsp");
						failureView.forward(req, res);
						return; //程式中斷
					}
					/***************************2.開始修改資料***************************************/
					if(is != null){ //判斷沒上傳圖片
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
//						byte[] pic = new byte[is.available()]; //一次寫入
//						empVO.setPic(pic);
					}
					empSvc.updateOneEmp(empVO);
					/***************************3.修改完成,準備轉交(Send the Success view)***********/
					//判斷路徑並傳值
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
					/***************************其他可能的錯誤處理*************************************/
				} catch (Exception e) {
					errorMsgs.add("無法取得資料:" + e.getMessage());
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
    	
//    	if("deleteEmp".equals(action)){ //來自showAllEmp,jsp的請求
//    		try {
//				/***************************1.接收請求參數***************************************/
//				String empid = req.getParameter("empid");
//				/***************************2.開始刪除資料***************************************/
//				EmpService empSvc = new EmpService();
//				empSvc.deleteEmp(empid);
//				/***************************3.刪除完成,準備轉交(Send the Success view)***********/
//				String url = "/back/emp/showAllEmp.jsp";
//				RequestDispatcher successView = req.getRequestDispatcher(url);
//				successView.forward(req, res);
//				/***************************其他可能的錯誤處理*************************************/
//			} catch (Exception e) {
//				errorMsgs.add("無法取得資料:" + e.getMessage());
//				RequestDispatcher failureView = req
//						.getRequestDispatcher("/back/emp/showAllEmp.jsp");
//				failureView.forward(req, res);
//			}
//    	}
    }
    
    public String getFileNameFromPart(Part part){ //取出上傳的檔案名稱 (因為API未提供method,所以必須自行撰寫)
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
		System.out.println("寄信");
		System.out.println(to+"     "+subject+"      "+messageText);
		com.tool.MailService mailService = new com.tool.MailService();
		mailService.sendMail(to, subject, messageText);
		//因為執行續無法重複使用，所以為了能重複發信，在執行續完成時，再重新生成一次
		mail_service = new Thread(this);
		mail_service.setPriority(Thread.MIN_PRIORITY);
	}

}
