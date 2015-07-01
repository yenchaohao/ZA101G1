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
		member.put(10, "�ӽФ�");
		member.put(11, "�ӽФ���^");
		member.put(20, "���q�|��");
		member.put(21, "���q�|�����v");
		member.put(30, "VIP�|��");
		member.put(31, "VIP�|�����v");
		context.setAttribute("member_status", member);
		 
		HashMap<Integer,String> goods_level = new HashMap<Integer,String>();
		goods_level.put(0, "�D�`�n");
		goods_level.put(1, "�n");
		goods_level.put(2, "���q");
		context.setAttribute("goods_level", goods_level);
		
		HashMap<Integer,String> goods_status = new HashMap<Integer,String>();
		goods_status.put(0, "���W�[");
		goods_status.put(1, "�W�[��");
		goods_status.put(2, "�w�R��");
		goods_status.put(3, "�w�������");
		context.setAttribute("goods_status", goods_status);
		
		HashMap<Integer, String> emp_status = new HashMap<Integer, String>();
		emp_status.put(1, "�b¾");
    	emp_status.put(0, "��¾");
    	context.setAttribute("emp_status", emp_status);
    	
    	HashMap<Integer, String> tran_status = new HashMap<Integer, String>();
		tran_status.put(0, "���^�Ъ����");
    	tran_status.put(1, "�������");
    	tran_status.put(2, "�������");
    	tran_status.put(3, "�����w��(�I�ڬݸԱ�)");
    	context.setAttribute("tran_status", tran_status);
    	
    	HashMap<Integer, String> send_status = new HashMap<Integer, String>();
    	send_status.put(0, "�����e");
    	send_status.put(1, "���e��");
    	send_status.put(2, "���e����");
    	send_status.put(3, "���e����");
    	context.setAttribute("send_status", send_status);
    	
    	HashMap<Integer, String> vipad_status = new HashMap<Integer, String>();
    	vipad_status.put(0, "���W�[");
    	vipad_status.put(1, "�W�[��");
    	vipad_status.put(2, "�w�R��");
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
