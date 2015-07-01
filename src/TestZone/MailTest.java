package TestZone;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mail.model.*;

public class MailTest {
	
	static MailJDBCDAO MailDAO = new MailJDBCDAO();
	static MailVO MailVO = new MailVO();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getAll();

	}
	public static void insert() {
		MailVO.setMember_id("M1001");
		MailVO.setTitle("特強的網站");
		MailVO.setQuestion("但還差我一點 (賈伯斯)");
		
		int insert = MailDAO.insert(MailVO);
		System.out.println("Success insert " + insert + " row");
	}
	public static void update() {
		MailVO.setMember_id("M1001");
		MailVO.setEmpid("E1001");
		MailVO.setTitle("特強的網站");
		MailVO.setAnswer("大神請指教");
		MailVO.setQuestion("但還差我一點 (賈伯斯)");
		java.util.Date du = new java.util.Date();
		Timestamp date = new Timestamp(du.getTime());
		MailVO.setA_date(date);
		MailVO.setStatus(1);
		MailVO.setCmid(1004);
		
		int insert = MailDAO.update(MailVO);
		System.out.println("Success update " + insert + " row");
	}
	public static void delete() {
		
		
		int insert = MailDAO.deleteByCmid(1004);
		System.out.println("Success delete " + insert + " row");
	}
	public static void findbyid(int cmid) {
		MailVO mailvo = MailDAO.findByCmid(cmid);
		System.out.println(mailvo.getCmid());
		System.out.println(mailvo.getMember_id());
		System.out.println(mailvo.getEmpid());
		System.out.println(mailvo.getQuestion());
		System.out.println(mailvo.getAnswer());
		System.out.println(mailvo.getA_date());
		System.out.println(mailvo.getQ_date());
		System.out.println(mailvo.getStatus());
		
		
		
	}
	public static void getAll() {
		List<MailVO> list = MailDAO.getAll();
		for(int i = 0 ;i<list.size() ; i++){
		System.out.println(list.get(i).getCmid());
		System.out.println(list.get(i).getMember_id());
		System.out.println(list.get(i).getEmpid());
		System.out.println(list.get(i).getQuestion());
		System.out.println(list.get(i).getAnswer());
		System.out.println(list.get(i).getA_date());
		System.out.println(list.get(i).getQ_date());
		System.out.println(list.get(i).getStatus());
		}
		
		
	}
	

}
