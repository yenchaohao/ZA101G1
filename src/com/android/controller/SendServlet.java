package com.android.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.model.SendAndMemberBean;
import com.credit.model.CreditService;
import com.credit.model.CreditVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.send.model.SendDAO;
import com.send.model.SendService;
import com.send.model.SendVO;
import com.send.model.SendVOAssociations;
import com.tool.MailService;
import com.tran.model.TranService;
import com.tran.model.TranVO;

public class SendServlet extends HttpServlet implements Runnable {
	
	
	// �|�I�sMailService�H�H���I�������
	private Thread mail_service;
	// �s�a�}��List
	private List<String> toList = new ArrayList<>();
	// �s�D�D��List
	private List<String> subjectList = new ArrayList<>();
	// �s���e��List
	private List<String> contentList = new ArrayList<>();

	// ��l�ư�����
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		mail_service = new Thread(this);
		mail_service.setPriority(Thread.MIN_PRIORITY);
	}
	
	
	
	private void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter out = res.getWriter();

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String outStr = "";
		String action = req.getParameter("action");
		
		
		/** �Ӧ�TranActivity ��tid���o2��sendVO *******************/
		if("findByTid".equals(action)){
			SendService sendSvc = new SendService();
			int tid = Integer.valueOf(req.getParameter("tid"));
			List<SendVO> sendVOList = sendSvc.getSendByTid(tid);
			
			outStr = gson.toJson(sendVOList);
			out.print(outStr);
			out.close();
		}
	
		/**�ثe���� ---- �Ӧ�SendActivity ���o�Ҧ����e�M��*******************/
		if("getAll".equals(action)){
			SendService sendSvc = new SendService();
			List<SendVO> sendVOList = sendSvc.getAll();
			
			outStr = gson.toJson(sendVOList);
			out.print(outStr);
			out.close();
		}
		
		/** �Ӧ�SendActivity �̬��e���A���o���e�M��*************************/
		if("getAllByStatus".equals(action)){
			SendService sendSvc = new SendService();
			MemberService memberSvc = new MemberService();
			
			int status = Integer.valueOf(req.getParameter("status"));
			List<SendVO> sendVOList = sendSvc.getAllByStatus(status);
			List<SendAndMemberBean> sendAndMemberBeanList = new ArrayList<>();
			
			for(SendVO sendVO : sendVOList){
				String member_id = sendVO.getMember_id();
				MemberVO memberVO = memberSvc.getOneMemberByMemberId(member_id);
				sendAndMemberBeanList.add(new SendAndMemberBean(sendVO, memberVO));
			}
			
			outStr = gson.toJson(sendAndMemberBeanList);
			out.print(outStr);
			out.close();
		}

		/** �Ӧ� SendActivity ���yQRcode�� ********************************************/
		if("update".equals(action)){
			final int ADD_CREDIT = 2;  //���觡�e�F�B�s�W�F�@��Credit���,updateCount = 2
			final int FAIL_CREDIT = -1;  //���觡�e�F���s�WCredit��ƥ���,updateCount = -1
			final int REPEAT = -2; //����sendVO�w�g�Q��Lstatus = 2
			int updateCount = 0;
			
			SendDAO dao = new SendDAO();
			SendVO sendVO = dao.findBySid(Integer.valueOf(req.getParameter("sid")));	
			
			//�u�n��X�Ӫ��o��sendVO �� status = 2�N�����d�I��� 
			if(sendVO.getStatus() == null || sendVO.getStatus() == 2){
				updateCount = REPEAT;
				outStr = String.valueOf(updateCount);
				out.print(outStr);
				out.close();
				return;
			}
			
			sendVO.setEnd_date((new Timestamp(new java.util.Date().getTime())));
			sendVO.setStatus(2);
			
			updateCount = dao.update(sendVO);  //�u���@��e�F,updateCount = 1
	
			//�α��y�쪺�o��sendVO���X�q��s��(tid)�h��Ʈw���			
			int tid = sendVO.getTid();
			//�����e�� status = 1 
			List<SendVO> sendVOListUn = dao.findByTidUn(tid);
			//���e������ status = 2 
			List<SendVO> sendVOListFinal = dao.findByTidFinal(tid);
				
			//����n�Ψ�insert�������P�_
			if(sendVOListFinal.size() == 2 && sendVOListUn.size() == 0){
				TranService tranSvc = new TranService();
				TranVO tranVO = tranSvc.getOneTran(tid);
				String memberA_id = tranVO.getReq_member_id();
				String memberB_id = tranVO.getRes_member_id();
				int status = 0;
				CreditService creditSvc = new CreditService();
				CreditVO creditVO = creditSvc.addCredit(memberA_id, memberB_id, status, tid);
				if(creditVO != null){
					updateCount = ADD_CREDIT;  //���觡�e�F�B�s�W�F�@��Credit���,updateCount = 2
					
					// ���o����LOCALHOST
					InetAddress address = InetAddress.getLocalHost();
					//�H�X�H��
					MemberService memberSvc = new MemberService();
					MemberVO memberVO_A = memberSvc.getOneMemberByMemberId(memberA_id);
					MemberVO memberVO_B = memberSvc.getOneMemberByMemberId(memberB_id);
					
					toList.add(memberVO_A.getEmail());
					toList.add(memberVO_B.getEmail());
					
//					toList.add("denevrove@gmail.com");
//					toList.add("reg368@gmail.com");
					
					subjectList.add("<�G������>��������w����");
					subjectList.add("<�G������>��������w����");
					
					StringBuffer messageTextA = new StringBuffer();
					messageTextA.append("�˷R���|���A�n \n");
					messageTextA.append("��������w����\n");
					messageTextA.append("�Ц�:http://" + address.getHostAddress() + ":" + req.getServerPort()
							+ "/ZA101G1/front/index/index.jsp \n");
					messageTextA.append("��������,�H�K�t���k���I��,���� \n");
					
					StringBuffer messageTextB = new StringBuffer();
					messageTextB.append("�˷R�����|���A�n \n");
					messageTextB.append("��������w����\n");
					messageTextB.append("�Ц�:http://" + address.getHostAddress() + ":" + req.getServerPort()
							+ "/ZA101G1/front/index/index.jsp \n");
					messageTextB.append("��������,�H�K�t���k���I��,���� \n");

					contentList.add(messageTextA.toString());
					contentList.add(messageTextB.toString());
					
					mail_service.start();
					
				} else {
					updateCount = FAIL_CREDIT;  //���觡�e�F���s�WCredit��ƥ���,updateCount = -1
				}			
			} 
			
			// �Ұ�mail_service����� (�|���榹���O run method �̭����{���X)
			
			
			outStr = String.valueOf(updateCount);
			out.print(outStr);
			out.close();
			

		}
		
	}
	
	// mailService.start()����...
	@Override
	public void run() {
		// TODO Auto-generated method stub
		MailService mailservice = new MailService();
		// �̷Ӧs�J���|���a�}�j�p�h�P�w �n�e�X�ʫH
		for (int i = 0; i < toList.size(); i++) {
			System.out.print(toList.get(i));
			mailservice.sendMail(toList.get(i), subjectList.get(i),
					contentList.get(i));
			// �e�H�|���I�֮� ���F�קK�ШD���_�e�X ���槹�e�H����k�� ����2�������ݫH��H�e����
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.print("ERROR");
			}
			// �]������ǵL�k���_�ϥ�,�ҥH���F�n���ϥΪ̯���@���o�e�H��,�b����ǧ�����,�A���s�ͦ��@��
		}
		mail_service = new Thread(this);
		mail_service.setPriority(Thread.MIN_PRIORITY);
		System.out.println("done");
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
