package com.android.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.member.model.MemberDAO;
import com.member.model.MemberJDBCDAO;
import com.member.model.MemberService;
import com.member.model.MemberVO;

public class MemberServlet extends HttpServlet {
	private void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String outStr = "";
		String action = req.getParameter("action");
		
		
		/** �Ӧ�WishActivity �j�M ********************************************/
		if("wishSearch".equals(action)){
			/*************************** 1.�����ШD�Ѽ� ****************************************/
			//�o�̶ǹL�Ӫ��OMap��json�r��,�ҥH�n����json�r����^Map���A
			String req_map = req.getParameter("map");
			HashMap<String,String[]> map = new HashMap<String,String[]>();
					
			Type listType = new TypeToken<HashMap<String,String[]>>(){}.getType();
			map = gson.fromJson(req_map, listType);
			/*************************** 2.�}�l�s�W��� ****************************************/
			MemberService memberSvc = new MemberService();		
			List<MemberVO> memberVOList = new ArrayList<>();
			
			String[] groupid = map.get("groupid");
	        String[] mem_name = map.get("mem_name");
	        String[] my_wish = map.get("my_wish");

	        //�p�Gԣ���S��N����search����..groupid�|�ǪŦr��L��(���F�t�XgetWishCondition)���L�o����
			if(groupid[0].equals("") && mem_name[0].length() == 0 && my_wish[0].length() ==0){
				memberVOList = memberSvc.getAllWish();
			} else {
				memberVOList = memberSvc.getSearchWish(map);
			}
	
			outStr = gson.toJson(memberVOList);
			out.print(outStr);
			out.close();			
		}

		/** �Ӧ�WishActivity ��X�Ҧ�wish���O�ŭȪ��|�� ********************************************/
		
		if("getAllWish".equals(action)){
			MemberService memberSvc = new MemberService();
			List<MemberVO> memberVOList = memberSvc.getAllWish();
	
			outStr = gson.toJson(memberVOList);
			out.print(outStr);
			out.close();
		}
		
		
		/** �Ӧ� LoginDialogActivity �n�J�|�� ********************************************/
		if("isMember".equals(action)){
			int SUCCESS = 1;
			int EMAIL_ERROR = -1;     //�b����J���~
			int PASSWORD_ERROR = -2;  //�K�X��J���~
			int MEMBER_CHECK = -3;    //���b���٦b�f�֤�
			int MEMBER_SUSPEND = -4;  //���b���w���v
			int ERROR = -9;
			/*************************** 1.�����ШD�Ѽ� ****************************************/			
			String email = req.getParameter("email");
			String password = req.getParameter("password");
			/*************************** 2.�}�l�s�W��� ****************************************/
			MemberService memberSvc = new MemberService();
			MemberVO memberVO = memberSvc.getOneMemberByEmail(email);
			//�p�G�d���즹�|��,�^�� memberVO �d�L�h�^�ǰT���r��
			if(memberVO == null || memberVO.getEmail() == null || memberVO.getMem_status() == 11){
				outStr = "�b����J���~";
			} else if(!memberVO.getPassword().equals(password)){
				outStr = "�K�X��J���~";
			} else if(memberVO.getMem_status() == 10){
				outStr = "���b���٦b�f�֤�";
			} else if(memberVO.getMem_status() == 21 || memberVO.getMem_status() == 31){
				outStr = "���b���w���v";
			} else {
				outStr = gson.toJson(memberVO);
			}
			
			out.print(outStr);
			out.close();
		}
		
		if ("findByMember_id".equals(action)){
			MemberService memberSvc = new MemberService();
			String member_id = req.getParameter("member_id");
			MemberVO member = memberSvc.getOneMemberByMemberId(member_id);
			
			outStr = gson.toJson(member);
			out.print(outStr);
			out.close();
		}
		
		if ("isMemberIdExist".equals(action)) {
			MemberService memberSvc = new MemberService();
			String email = req.getParameter("email");
			MemberVO memberVO = memberSvc.getOneMemberByEmail(email);
			outStr = (memberVO.getEmail() != null) ? "true" : "false";
			out.print(outStr);
			out.close();
		}
		
		/** �Ӧ� CreateAccountActivity �[�J�|�� ********************************************/
		if ("insert".equals(action)) {
			try {
				/*************************** 1.�����ШD�Ѽ� ****************************************/
				MemberVO memberVO = gson.fromJson(req.getParameter("memberVO"), MemberVO.class);
				String email = memberVO.getEmail();
				String mem_name = memberVO.getMem_name();
				String password = memberVO.getPassword();
				String id_no = memberVO.getId_no();
				String tel = memberVO.getTel();
				String address = memberVO.getAddress();
				java.sql.Date birthday = memberVO.getBirthday();
				byte[] pic = memberVO.getPic();
				/*************************** 2.�}�l�s�W��� ****************************************/
				MemberService memberSvc = new MemberService();

				String member_id = memberSvc.addMemberGetPrimaryKey(email,mem_name, password, id_no, tel, address, birthday, pic,null);
				/***************************3.�s�W����,�ǳ���� **************************************/
				if(member_id != null){
					outStr = member_id;
				} else {
					outStr = "";
				}
				
				out.print(outStr);
				out.close();
			} catch (Exception e) {
				out.print(""); // �s�W����,�^�ǪŦr��
				out.close();
			}
		}
		
		/** �Ӧ� MyMemberActivity ��s�|����� ********************************************/
		if("update".equals(action)){
			int SUCCESS = 1;
			int REPEAT = -1;
			int ERROR = -9;
			try {
				/*************************** 1.�����ШD�Ѽ� ****************************************/
				MemberVO memberVO = gson.fromJson(req.getParameter("memberVO"), MemberVO.class);
				/*************************** 2.�}�l�s�W��� ****************************************/
				MemberService memberSvc = new MemberService();
				
				String member_id = memberVO.getMember_id();
				String email = memberVO.getEmail();
				//���h��Ʈw��X���쥻��email,�p�G��ǨӪ����P,��ܦ��|���n��ʥL���H�c
				if(!(memberSvc.getOneMemberByMemberId(member_id).getEmail()).equals(email)){
					if((memberSvc.getOneMemberByEmail(email)) != null){					
						out.print(REPEAT);
						out.close();
						return;
					}
				}
				
				outStr = String.valueOf(memberSvc.updateMemberByObject(memberVO));
				/***************************3.�s�W����,�ǳ���� **************************************/
				out.print(outStr);
				out.close();
			} catch (Exception e) {
				out.print(ERROR);  //�s�W����,�^��-9
				out.close();
			}		
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		process(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		process(req, res);
	}

}
