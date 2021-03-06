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
		member.put(10, "ビ出い");
		member.put(11, "ビ出い脂�^");
		member.put(20, "感�q�|��");
		member.put(21, "感�q�|��葦�v");
		member.put(30, "VIP�|��");
		member.put(31, "VIP�|��葦�v");
		context.setAttribute("member_status", member);
		 
		HashMap<Integer,String> goods_level = new HashMap<Integer,String>();
		goods_level.put(0, "�D�`�n");
		goods_level.put(1, "�n");
		goods_level.put(2, "感�q");
		context.setAttribute("goods_level", goods_level);
		
		HashMap<Integer,String> goods_status = new HashMap<Integer,String>();
		goods_status.put(0, "ゼ�W�[");
		goods_status.put(1, "�W�[い");
		goods_status.put(2, "�w�R娃");
		goods_status.put(3, "�wЧΘユ��");
		context.setAttribute("goods_status", goods_status);
		
		HashMap<Integer, String> emp_status = new HashMap<Integer, String>();
		emp_status.put(1, "�b他");
    	emp_status.put(0, "託他");
    	context.setAttribute("emp_status", emp_status);
    	
    	HashMap<Integer, String> tran_status = new HashMap<Integer, String>();
		tran_status.put(0, "ゼ�^対�坤罘�");
    	tran_status.put(1, "ユ��Θミ");
    	tran_status.put(2, "ユ������");
    	tran_status.put(3, "ユ��題�wい(�Iи�欷埀�)");
    	context.setAttribute("tran_status", tran_status);
    	
    	HashMap<Integer, String> send_status = new HashMap<Integer, String>();
    	send_status.put(0, "ゼ�０e");
    	send_status.put(1, "�０eい");
    	send_status.put(2, "�０eЧΘ");
    	send_status.put(3, "�０e����");
    	context.setAttribute("send_status", send_status);
    	
    	HashMap<Integer, String> vipad_status = new HashMap<Integer, String>();
    	vipad_status.put(0, "ゼ�W�[");
    	vipad_status.put(1, "�W�[い");
    	vipad_status.put(2, "�w�R娃");
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
