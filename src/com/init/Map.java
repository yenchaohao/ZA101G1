package com.init;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Map
 */
@WebServlet(urlPatterns={"/Map"}, loadOnStartup = 1)
public class Map extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	ServletContext context;
	
    @Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		//System.out.println("12");
		context = this.getServletContext();
		HashMap<Integer,String> member = new HashMap<Integer,String>();
		member.put(10, "申請中");
		member.put(11, "申請中駁回");
		member.put(20, "普通會員");
		member.put(21, "普通會員停權");
		member.put(30, "VIP會員");
		member.put(31, "VIP會員停權");
		context.setAttribute("member_status", member);
		 
		HashMap<Integer,String> goods_level = new HashMap<Integer,String>();
		goods_level.put(0, "非常好");
		goods_level.put(1, "好");
		goods_level.put(2, "普通");
		context.setAttribute("goods_level", goods_level);
		
		HashMap<Integer,String> goods_status = new HashMap<Integer,String>();
		goods_status.put(0, "未上架");
		goods_status.put(1, "上架中");
		goods_status.put(2, "已刪除");
		goods_status.put(3, "已完成交易");
		context.setAttribute("goods_status", goods_status);
		
		HashMap<Integer, String> emp_status = new HashMap<Integer, String>();
		emp_status.put(1, "在職");
    	emp_status.put(0, "離職");
    	context.setAttribute("emp_status", emp_status);
    	
    	HashMap<Integer, String> tran_status = new HashMap<Integer, String>();
		tran_status.put(0, "未回覆的交易");
    	tran_status.put(1, "交易成立");
    	tran_status.put(2, "交易取消");
    	tran_status.put(3, "交易鎖定中(點我看詳情)");
    	context.setAttribute("tran_status", tran_status);
    	
    	HashMap<Integer, String> send_status = new HashMap<Integer, String>();
    	send_status.put(0, "未派送");
    	send_status.put(1, "派送中");
    	send_status.put(2, "派送完成");
    	send_status.put(3, "派送取消");
    	context.setAttribute("send_status", send_status);
    	
    	HashMap<Integer, String> vipad_status = new HashMap<Integer, String>();
    	vipad_status.put(0, "未上架");
    	vipad_status.put(1, "上架中");
    	vipad_status.put(2, "已刪除");
    	context.setAttribute("vipad_status", vipad_status);
    }

	/**
     * @see HttpServlet#HttpServlet()
     */
    public Map() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
