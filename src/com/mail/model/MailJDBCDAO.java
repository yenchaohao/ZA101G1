package com.mail.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.send.model.SendVO;

public class MailJDBCDAO implements Mail_interface {
	
	String  driver = "oracle.jdbc.driver.OracleDriver";
    String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String userid = "g1";
	String passwd = "za101g1";
	
	private static final String INSERT_STMT = 
			"INSERT INTO EX_MAIL (cmid,Member_id,empid,title,question,answer,a_date) VALUES(ex_mail_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = 
			"UPDATE EX_MAIL set Member_id=?,EMPID=?,TITLE=?,QUESTION=?,ANSWER=?,A_DATE=?,status=? where CMID = ?";
	private static final String DELETE_BY_TID = 
			"DELETE FROM EX_MAIL where CMID = ?";
	private static final String FIND_BY_TID = 
			"SELECT cmid,Member_id,empid,title,question,answer,q_date,a_date,status FROM EX_MAIL WHERE CMID = ?";
	private static final String GET_ALL_STMT = 
			"SELECT cmid,Member_id,empid,title,question,answer,q_date,a_date,status from ex_mail order by CMID";
	
	
	
	@Override
	public int insert(MailVO mailvo) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try{
				Class.forName(driver);
				con = DriverManager.getConnection(url,userid,passwd);
			
				pstmt = con.prepareStatement(INSERT_STMT);
				con.setAutoCommit(false);
				
				pstmt.setString(1, mailvo.getMember_id());
				pstmt.setString(2, mailvo.getEmpid());
				pstmt.setString(3, mailvo.getTitle());
				pstmt.setString(4, mailvo.getQuestion());
				pstmt.setString(5, mailvo.getAnswer());
				pstmt.setTimestamp(6, mailvo.getA_date());
				
				
				
				insertCount = pstmt.executeUpdate();
				con.commit();
				con.setAutoCommit(true);
			}catch(Exception ex){
			System.out.println(ex.getMessage());
			if(con!=null){
				try{
				System.out.println("Transaction begin, start rollback");
				con.rollback();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
		}finally{
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		
		return insertCount;
	}

	@Override
	public int update(MailVO mailvo) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try{
				Class.forName(driver);
				con = DriverManager.getConnection(url,userid,passwd);
			
				pstmt = con.prepareStatement(UPDATE);
				con.setAutoCommit(false);
				
				pstmt.setString(1, mailvo.getMember_id());
				pstmt.setString(2, mailvo.getEmpid());
				pstmt.setString(3, mailvo.getTitle());
				pstmt.setString(4, mailvo.getQuestion());
				pstmt.setString(5, mailvo.getAnswer());
				pstmt.setTimestamp(6, mailvo.getA_date());
				pstmt.setInt(7, mailvo.getStatus());
				pstmt.setInt(8,mailvo.getCmid());
				
				
				insertCount = pstmt.executeUpdate();
				con.commit();
				con.setAutoCommit(true);
			}catch(Exception ex){
			System.out.println(ex.getMessage());
			if(con!=null){
				try{
				System.out.println("Transaction begin, start rollback");
				con.rollback();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
		}finally{
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		
		return insertCount;
	}

	@Override
	public int deleteByCmid(Integer cmid) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try{
				Class.forName(driver);
				con = DriverManager.getConnection(url,userid,passwd);
			
				pstmt = con.prepareStatement(DELETE_BY_TID);
				con.setAutoCommit(false);
				
				pstmt.setInt(1, cmid);
				
				
				insertCount = pstmt.executeUpdate();
				con.commit();
				con.setAutoCommit(true);
			}catch(Exception ex){
			System.out.println(ex.getMessage());
			if(con!=null){
				try{
				System.out.println("Transaction begin, start rollback");
				con.rollback();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
		}finally{
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		
		return insertCount;
	}

	@Override
	public MailVO findByCmid(Integer cmid) {
		// TODO Auto-generated method stub
		MailVO mailvo = new MailVO();
		Connection con = null;
		PreparedStatement pstmt;
		try{
				Class.forName(driver);
				con = DriverManager.getConnection(url,userid,passwd);
				pstmt = con.prepareStatement(FIND_BY_TID);
				pstmt.setInt(1, cmid);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()){
					mailvo.setCmid(rs.getInt(1));
					mailvo.setMember_id(rs.getString(2));
					mailvo.setEmpid(rs.getString(3));
					mailvo.setTitle(rs.getString(4));
					mailvo.setQuestion(rs.getString(5));
					mailvo.setAnswer(rs.getString(6));
					mailvo.setQ_date(rs.getTimestamp(7));
					mailvo.setA_date(rs.getTimestamp(8));
					mailvo.setStatus(rs.getInt(9));
					}
				
				
			}catch(Exception ex){
			System.out.println(ex.getMessage());
		 }finally{
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		
		return mailvo;
	}

	@Override
	public List<MailVO> getAll() {
		// TODO Auto-generated method stub
		MailVO mailvo = null;
		List<MailVO> list = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement pstmt;
		try{
				Class.forName(driver);
				con = DriverManager.getConnection(url,userid,passwd);
				pstmt = con.prepareStatement(GET_ALL_STMT);
				
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()){
					mailvo = new MailVO();
					mailvo.setCmid(rs.getInt(1));
					mailvo.setMember_id(rs.getString(2));
					mailvo.setEmpid(rs.getString(3));
					mailvo.setTitle(rs.getString(4));
					mailvo.setQuestion(rs.getString(5));
					mailvo.setAnswer(rs.getString(6));
					mailvo.setQ_date(rs.getTimestamp(7));
					mailvo.setA_date(rs.getTimestamp(8));
					mailvo.setStatus(rs.getInt(9));
					list.add(mailvo);
					}
				
				
			}catch(Exception ex){
			System.out.println(ex.getMessage());
		 }finally{
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		
		return list;
	}

	@Override
	public List<MailVO> findByAnswerForReport(String answer) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
