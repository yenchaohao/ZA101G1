package TestZone;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.member.model.*;

public class MemberTest {

	static MemberJDBCDAO memberdao = new MemberJDBCDAO();
	static MemberVO membervo = new MemberVO();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		insert();

	}

	public static void insert() {
		membervo.setEmail("ksdllls@gmail.com");
		membervo.setMem_name("K");
		membervo.setId_no("K122222222");
		membervo.setTel("03-3333333");
		membervo.setAddress("NEW YORK , US");

		Calendar calendar = new GregorianCalendar(1988, 8, 8);
		Date date = new Date(calendar.getTimeInMillis());

		membervo.setBirthday(date);

		/*
		 * File�غc���Ĥ@��"images" �����O�U�������| (ZA101G1) �O�M�צW��
		 * D:\eclipseJavaEE\eclipse_WTP_workspace\ZA101G1\images
		 * (�]�N�O�������b�M�פ��ؤ@�Ӹ�Ƨ�,�Ϊ�����Ϥ��M��AFile�غc����������Ƨ��W�٩��ɦW(���Υ[�׽u)�N�i�HŪ������F)
		 * 
		 * "james.jpg" --> �O�Ϥ��ɦW(�bimages��Ƨ���)
		 */
//		File file = new File("images", "james.jpg");
//
//		/*
//		 * �N���Ū����james.jpg ��file���� ,�ন InputStream �M��A�� ByteArrayOutputStream ��
//		 * InputStram �ন byte[] �}�C�s�JVO �� (VO��byte[] ��^InputStream
//		 * �g�J��Ʈw�������bDAO����)
//		 */
//		try {
//			InputStream in = new FileInputStream(file);
//			ByteArrayOutputStream byteout = new ByteArrayOutputStream();
//			byte[] buffer = new byte[4 * 1024];
//			int len = 0;
//			while ((len = in.read(buffer)) != -1) {
//				byteout.write(buffer, 0, len);
//			}
//			byteout.flush();
//
//			byte[] image = byteout.toByteArray();
//			membervo.setPic(image);
//
//		} catch (Exception ex) {
//			System.out.println(ex.getMessage());
//		}
		
		membervo.setPic(null);

	
		membervo.setMy_wish("�ڷQ�Ȥj��");

		memberdao.insert(membervo);

	}

	public static void update() {
		membervo.setMember_id("M1011");
		membervo.setEmail("k@gmail.com");
		membervo.setMem_name("K");
		membervo.setPassword("123");
		membervo.setId_no("K122222222");
		membervo.setTel("03-3333333");
		membervo.setAddress("NEW YORK , US");

		Calendar calendar = new GregorianCalendar(1988, 8, 8);
		Date date = new Date(calendar.getTimeInMillis());

		membervo.setBirthday(date);

		/*
		 * File�غc���Ĥ@��"images" �����O�U�������| (ZA101G1) �O�M�צW��
		 * D:\eclipseJavaEE\eclipse_WTP_workspace\ZA101G1\images
		 * (�]�N�O�������b�M�פ��ؤ@�Ӹ�Ƨ�,�Ϊ�����Ϥ��M��AFile�غc����������Ƨ��W�٩��ɦW(���Υ[�׽u)�N�i�HŪ������F)
		 * 
		 * "james.jpg" --> �O�Ϥ��ɦW(�bimages��Ƨ���)
		 */
		File file = new File("images", "james.jpg");

		/*
		 * �N���Ū����james.jpg ��file���� ,�ন InputStream �M��A�� ByteArrayOutputStream ��
		 * InputStram �ন byte[] �}�C�s�JVO �� (VO��byte[] ��^InputStream
		 * �g�J��Ʈw�������bDAO����)
		 */
		try {
			InputStream in = new FileInputStream(file);
			ByteArrayOutputStream byteout = new ByteArrayOutputStream();
			byte[] buffer = new byte[4 * 1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				byteout.write(buffer, 0, len);
			}
			byteout.flush();

			byte[] image = byteout.toByteArray();
			membervo.setPic(image);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	
		membervo.setCredit(0);
		membervo.setHaving_p(0);
		membervo.setPending_p(0);
		membervo.setMem_status(0);
		membervo.setMy_wish("�ڷQ�Ȥj��");

		memberdao.update(membervo);
	}

	public static void delete() {
		int delete = memberdao.deleteByMember_id("M1011");
		System.out.println("delete" + delete + "row completed");
	}

	public static void findbymember_id(String id) {
		membervo = memberdao.findByMember_id(id);

		System.out.println(membervo.getMember_id());
		System.out.println(membervo.getEmail());
		System.out.println(membervo.getMem_name());
		System.out.println(membervo.getPassword());
		System.out.println(membervo.getId_no());
		System.out.println(membervo.getTel());
		System.out.println(membervo.getAddress());
		System.out.println(membervo.getBirthday());
		System.out.println(membervo.getJoindate());
		System.out.println(membervo.getPic().toString());
		
		System.out.println(membervo.getCredit());
		System.out.println(membervo.getHaving_p());
		System.out.println(membervo.getPending_p());
		System.out.println(membervo.getMem_status());
		System.out.println(membervo.getMy_wish());

	}

	public static void getAll() {

		List<MemberVO> list = memberdao.getAll();
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getMember_id());
			System.out.println(list.get(i).getEmail());
			System.out.println(list.get(i).getMem_name());
			System.out.println(list.get(i).getPassword());
			System.out.println(list.get(i).getId_no());
			System.out.println(list.get(i).getTel());
			System.out.println(list.get(i).getAddress());
			System.out.println(list.get(i).getBirthday());
			System.out.println(list.get(i).getJoindate());
			System.out.println(list.get(i).getPic().toString());
			
			System.out.println(list.get(i).getCredit());
			System.out.println(list.get(i).getHaving_p());
			System.out.println(list.get(i).getPending_p());
			System.out.println(list.get(i).getMem_status());
			System.out.println(list.get(i).getMy_wish());
		}
	}
}
