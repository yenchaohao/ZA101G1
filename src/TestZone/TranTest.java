package TestZone;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import com.tran.model.*;


public class TranTest {

//	static TranJDBCDAO trandao = new TranJDBCDAO();
//	static TranVO tranvo = new TranVO();
//	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		
//		getAll();
//		
//	
//		
//	}
//	public static void insert(){
//		
//		tranvo.setRes_member_id("M1001");
//		tranvo.setReq_member_id("M1002");
//		tranvo.setRes_gid(1001);
//		tranvo.setReq_gid(1002);
//		
//		int insert = trandao.insert(tranvo);
//		System.out.println("Insert "+insert+" row completed. ");
//	}
//	public static void update(){
//		
//		tranvo.setRes_member_id("M1001");
//		tranvo.setReq_member_id("M1002");
//		tranvo.setRes_gid(1001);
//		tranvo.setReq_gid(1002);
//		tranvo.setStatus(2);
//		tranvo.setTid(1002);
//		//java.util.Date dateutil = new java.util.Date();
//		//Date sqldate = new Date(dateutil.getTime());
//		//tranvo.setRes_date(sqldate);
//		
//		
//		
//		int update = trandao.update(tranvo);
//		System.out.println("Update "+update+" row completed. ");
//	}
//		public static void delete(){
//		
//		
//		int update = trandao.deleteByTid(1001);
//		System.out.println("Delete "+update+" row completed. ");
//	}
//		public static void findbytid(int i){
//			tranvo = trandao.findByTid(i);
//			System.out.println(tranvo.getReq_member_id());
//			System.out.println(tranvo.getRes_member_id());
//			System.out.println(tranvo.getReq_gid());
//			System.out.println(tranvo.getRes_gid());
//			System.out.println(tranvo.getRes_date());
//			System.out.println(tranvo.getReq_date());
//			System.out.println(tranvo.getStatus());
//			
//			
//		}
//		
//		public static void getAll(){
//			List<TranVO> list = trandao.getAll();
//			for(int i = 0 ; i < list.size() ; i++){
//			System.out.println(list.get(i).getReq_member_id());
//			System.out.println(list.get(i).getRes_member_id());
//			System.out.println(list.get(i).getReq_gid());
//			System.out.println(list.get(i).getRes_gid());
//			System.out.println(list.get(i).getRes_date());
//			System.out.println(list.get(i).getReq_date());
//			System.out.println(list.get(i).getStatus());
//			}
//		}
	

}
