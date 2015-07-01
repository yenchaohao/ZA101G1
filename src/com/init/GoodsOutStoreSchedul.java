package com.init;

import com.member.model.*;
import com.tool.MailService;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.goods.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Schedul
 */
@WebServlet(urlPatterns = { "/Schedul" }, loadOnStartup = 1)
public class GoodsOutStoreSchedul extends HttpServlet implements Runnable {
	private static final long serialVersionUID = 1L;

	//�P�_�O�_���H��ݭn�H�X(�O�_���ӫ~�U�[)
	boolean isSend = false;
	//�H�H�������
	Thread mail_service;
	// �s�a�}��List
	private List<String> toList = new ArrayList<>();
	// �s�D�D��List
	private List<String> subjectList = new ArrayList<>();
	// �s���e��List
	private List<String> contentList = new ArrayList<>();
	//���Ƶ{���D�{�b���ɶ�,�M�ӫ~���U�[������p��p�G�W�L�N�U�[
	Calendar cal = Calendar.getInstance();
	//�]�w�Ƶ{��timer
	Timer timer;
	//�^�����ϥΪ����ϥΪ̪��D�ӫ~����ɭԳQ�U�[
	Timestamp stamp;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();

		mail_service = new Thread(this);
		mail_service.setPriority(Thread.MIN_PRIORITY);

		// timertask �Ƶ{��
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<GoodsVO> list = new GoodsService().getAllAlive();
				// currentDate���o��e���ɶ�
				Calendar cal = Calendar.getInstance();
				for (GoodsVO goods : list) {
				
					if (goods.getQuitdate() != null) {
						// �p�G�ثe���ɶ� �p�� �U�[���
						if (cal.getTimeInMillis() > goods.getQuitdate()
								.getTime()) {
							//�o��P�_�p�G�n�H���H�w�g�ֿn��5�ʴN���X�j�� (�@���H�X���H���n�Ӧh�ȼv�T�į�)
							if(toList.size() > 5){
								isSend = true;
								break;
							}
							
							//���o�{�b�ɶ�
							Calendar current = Calendar.getInstance();
							stamp = new Timestamp(current.getTimeInMillis());
							String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(stamp.getTime())); 
							//���o�Ӱӫ~�֦��̪���T
							MemberVO membervo = new MemberService()
									.getOneMemberByMemberId(goods
											.getMember_id());
							
							toList.add(membervo.getEmail());
							subjectList.add("�A���ӫ~: " + goods.getG_name()
									+ "�w�g�U�[");
							contentList.add("�A���ӫ~: " + goods.getG_name()
									+ " �s��: " + goods.getGid() + " �w�g��:"
									+ time + "�U�[");
							goods.setGoods_status(0);
							new GoodsService().updateGoodsByObject(goods);
							//System.out.println("�w��ӫ~");
							isSend = true;
						}
					}
				}
				// �}�l�H�H
				if (isSend)
					mail_service.start();
				//after sending resume boolean
				isSend = false;
			}
		};

		timer = new Timer();
		// �Ѯv�d�� 1*60*60*1000 (��)*(��)*(��)*(�@��) ��*�@�� = 10 ��
		timer.scheduleAtFixedRate(task, cal.getTime(), 10 * 1000);
		// System.out.println("�w�إ߱Ʀ�");

	}

	@Override
	public void run() {
		/*��Ƶ{������åB�M��
		  �b�H�H���ɭԬ��F�קK�Ƶ{�P�ɤ]�b�i��,�|�~��h��Ʈw��O�_���U�[�ӫ~�i�ӥh����toList���j�p�ӳy���n�H���H�P�U�[�ӫ~���P�B,�ҥH�b�H�H�ɥ���Ƶ{�M�ŵ���
		  �ݱH�H����,�Ƶ{�A�~�����
		*/
		timer.purge();
		timer.cancel();
	
		MailService mailservice = new MailService();
		// TODO Auto-generated method stub
		for (int i = 0; i < toList.size(); i++) {
			//System.out.println("�H�H"+i);
			mailservice.sendMail(toList.get(i), subjectList.get(i),
					contentList.get(i));
			//�p�G���W�L�@�ʭn�H����
			if (i > 0) {
				//�H�H�s�����ݭn�ɶ� ���Ӥ@�����קK�y�����_�H�X
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		/* �H�U�H�H�����}�l�^�_�Ƶ{�M����Ǫ��ʧ@....
		 
		 mail_service�b�}�ҷs������� ,�H�ѤU�@���ϥ�*/
		mail_service = new Thread(this);
		mail_service.setPriority(Thread.MIN_PRIORITY);
		//�N���X�M��
		toList.clear();
		subjectList.clear();
		contentList.clear();
		//�b�ؤ@�Ӥ@�Ҥ@�˷s��task���Ƶ{ (���i�H��init() �ئn�� �|���~)
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// System.out.println("�w����Ƶ{" + (count++));
				// TODO Auto-generated method stub
				List<GoodsVO> list = new GoodsService().getAllAlive();
				// currentDate���o��e���ɶ�
				Calendar cal = Calendar.getInstance();
				for (GoodsVO goods : list) {
					// �p�G�ثe���ɶ� �p�� �U�[���
					if (goods.getQuitdate() != null) {
						if (cal.getTimeInMillis() > goods.getQuitdate()
								.getTime()) {

							Calendar current = Calendar.getInstance();
							stamp = new Timestamp(current.getTimeInMillis());
							String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(stamp.getTime())); 
							
							MemberVO membervo = new MemberService()
									.getOneMemberByMemberId(goods
											.getMember_id());
							toList.add(membervo.getEmail());
							subjectList.add("�A���ӫ~: " + goods.getG_name()
									+ "�w�g�U�[");
							contentList.add("�A���ӫ~: " + goods.getG_name()
									+ " �s��: " + goods.getGid() + " �w�g��:"
									+ time + "�U�[");
							goods.setGoods_status(0);
							new GoodsService().updateGoodsByObject(goods);
							System.out.println("�w�H�H"+goods.getMember_id()+"�M�U�[�ӫ~"+goods.getG_name());
							isSend = true;
						}
					}
				}
				// �}�l�H�H
				if (isSend)
					mail_service.start();
				//after sending resume boolean
				isSend = false;
			}
		};
		
		//�طs���Ƶ{ �õ��U�s��task
		timer = new Timer();
		timer.schedule(task, cal.getTime(), 10 * 1000);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
		timer.cancel();
	}

	public GoodsOutStoreSchedul() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
