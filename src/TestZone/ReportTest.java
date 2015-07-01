package TestZone;

import java.util.List;

import com.report.model.*;

public class ReportTest {

	static ReportJDBCDAO reportdao = new ReportJDBCDAO();
	static ReportVO reportvo = new ReportVO();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getALL();
	}

	public static void insert() {
		reportvo.setGid(1001);
		reportvo.setMember_id("M1001");
		int insert = reportdao.insert(reportvo);
		System.out.println("Success insert " + insert + " row");
	}

	public static void update() {
		reportvo.setGid(1002);
		reportvo.setMember_id("M1002");
		reportvo.setRid(1001);
		int insert = reportdao.update(reportvo);
		System.out.println("Success update " + insert + " row");
	}

	public static void delete() {

		int insert = reportdao.delete(1002);
		System.out.println("Success delete " + insert + " row");
	}

	public static void findbyrid(int rid) {

		reportvo = reportdao.findByPrimaryKey(rid);
		System.out.println(reportvo.getRid());
		System.out.println(reportvo.getGid());
		System.out.println(reportvo.getMember_id());
		System.out.println(reportvo.getR_date());

	}

	public static void getALL() {

		List<ReportVO> list = reportdao.getAll();
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getRid());
			System.out.println(list.get(i).getGid());
			System.out.println(list.get(i).getMember_id());
			System.out.println(list.get(i).getR_date());
		}
	}
}
