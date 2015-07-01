package com.send.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.tran.model.TranVO;

public class SendDAO implements Send_interface {
	

	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/G1Local");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	/*
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/G1Amazon");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}*/
	
	private static final String INSERT_STMT = 
			"INSERT INTO EX_SEND (SID,GID,MEMBER_ID,TID) VALUES(ex_send_seq.NEXTVAL, ?, ?, ?)";
	private static final String UPDATE = 
			"UPDATE EX_SEND set GID=?,MEMBER_ID=?,START_DATE = ?,END_DATE=?,TID=?,STATUS=? where SID = ?";
	private static final String DELETE_BY_TID = 
			"DELETE FROM EX_SEND where SID = ?";
	private static final String FIND_BY_SID = 
			"SELECT SID,GID,MEMBER_ID,TID,START_DATE,END_DATE,STATUS  FROM EX_SEND WHERE SID = ?";	
	private static final String FIND_BY_TID = 
			"SELECT SID,GID,MEMBER_ID,TID,START_DATE,END_DATE,STATUS  FROM EX_SEND WHERE TID = ?";
	private static final String FIND_BY_TID_UN = 
			"SELECT SID,GID,MEMBER_ID,TID,START_DATE,END_DATE,STATUS  FROM EX_SEND WHERE status = 1 AND TID = ?";
	private static final String FIND_BY_TID_FINAL = 
			"SELECT SID,GID,MEMBER_ID,TID,START_DATE,END_DATE,STATUS  FROM EX_SEND WHERE status = 2 AND TID = ?";
	private static final String GET_ALL_STMT = 
			"SELECT SID,GID,MEMBER_ID,TID,START_DATE,END_DATE,STATUS FROM EX_SEND order by SID";
	private static final String GET_ALL_STMT_Alive = 
			"SELECT SID,GID,MEMBER_ID,TID,START_DATE,END_DATE,STATUS FROM EX_SEND where status != 3 order by SID";
	private static final String GET_ALL_BY_STATUS = 
			"SELECT SID,GID,MEMBER_ID,TID,START_DATE,END_DATE,STATUS FROM EX_SEND where status = ? order by SID";
	

	@Override
	public int insert(SendVO sendvo) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try{
				con = ds.getConnection();
			
				pstmt = con.prepareStatement(INSERT_STMT);
				con.setAutoCommit(false);
				pstmt.setInt(1, sendvo.getGid());
				pstmt.setString(2,sendvo.getMember_id());
				pstmt.setInt(3,sendvo.getTid());
				
				
				
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
	public int update(SendVO sendvo) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try{
				con = ds.getConnection();
				pstmt = con.prepareStatement(UPDATE);
				con.setAutoCommit(false);
				pstmt.setInt(1, sendvo.getGid());
				pstmt.setString(2,sendvo.getMember_id());
				pstmt.setTimestamp(3, sendvo.getStart_date());
				pstmt.setTimestamp(4, sendvo.getEnd_date());
				pstmt.setInt(5,sendvo.getTid());
				pstmt.setInt(6, sendvo.getStatus());
				pstmt.setInt(7, sendvo.getSid());
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
	public int deleteBySid(Integer sid) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try{
				con = ds.getConnection();
				pstmt = con.prepareStatement(DELETE_BY_TID);
				con.setAutoCommit(false);
				
				pstmt.setInt(1, sid);
				
				
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
	public SendVO findBySid(Integer sid) {
		// TODO Auto-generated method stub
		SendVO sendvo = new SendVO();
		Connection con = null;
		PreparedStatement pstmt;
		try{
				con = ds.getConnection();
				pstmt = con.prepareStatement(FIND_BY_SID);
				pstmt.setInt(1, sid);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()){
					sendvo.setSid(rs.getInt(1));
					sendvo.setGid(rs.getInt(2));
					sendvo.setMember_id(rs.getString(3));
					sendvo.setTid(rs.getInt(4));
					sendvo.setStart_date(rs.getTimestamp(5));
					sendvo.setEnd_date(rs.getTimestamp(6));
					sendvo.setStatus(rs.getInt(7));
					
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
		
		
		return sendvo;
		
	}

	@Override
	public List<SendVO> findByTid(Integer tid) {
		// TODO Auto-generated method stub
				SendVO sendvo = null;
				Connection con = null;
				List<SendVO> list = new ArrayList<>();
				PreparedStatement pstmt;
				try{
						con = ds.getConnection();
						pstmt = con.prepareStatement(FIND_BY_TID);
						pstmt.setInt(1, tid);
						ResultSet rs = pstmt.executeQuery();
						while(rs.next()){
							sendvo = new SendVO();
							sendvo.setSid(rs.getInt(1));
							sendvo.setGid(rs.getInt(2));
							sendvo.setMember_id(rs.getString(3));
							
							sendvo.setTid(rs.getInt(4));
							sendvo.setStart_date(rs.getTimestamp(5));
							sendvo.setEnd_date(rs.getTimestamp(6));
							sendvo.setStatus(rs.getInt(7));
							list.add(sendvo);
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
	public List<SendVO> findByTidUn(Integer tid) {
		// TODO Auto-generated method stub
		SendVO sendvo = null;
		Connection con = null;
		List<SendVO> list = new ArrayList<>();
		PreparedStatement pstmt;
		try{
				con = ds.getConnection();
				pstmt = con.prepareStatement(FIND_BY_TID_UN);
				pstmt.setInt(1, tid);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()){
					sendvo = new SendVO();
					sendvo.setSid(rs.getInt(1));
					sendvo.setGid(rs.getInt(2));
					sendvo.setMember_id(rs.getString(3));
					
					sendvo.setTid(rs.getInt(4));
					sendvo.setStart_date(rs.getTimestamp(5));
					sendvo.setEnd_date(rs.getTimestamp(6));
					sendvo.setStatus(rs.getInt(7));
					list.add(sendvo);
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
	public List<SendVO> findByTidFinal(Integer tid) {
		// TODO Auto-generated method stub
		SendVO sendvo = null;
		Connection con = null;
		List<SendVO> list = new ArrayList<>();
		PreparedStatement pstmt;
		try{
				con = ds.getConnection();
				pstmt = con.prepareStatement(FIND_BY_TID_FINAL);
				pstmt.setInt(1, tid);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()){
					sendvo = new SendVO();
					sendvo.setSid(rs.getInt(1));
					sendvo.setGid(rs.getInt(2));
					sendvo.setMember_id(rs.getString(3));
					
					sendvo.setTid(rs.getInt(4));
					sendvo.setStart_date(rs.getTimestamp(5));
					sendvo.setEnd_date(rs.getTimestamp(6));
					sendvo.setStatus(rs.getInt(7));
					list.add(sendvo);
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
	public List<SendVO> getAll() {
		// TODO Auto-generated method stub
		SendVO sendvo = null;
		List<SendVO> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt;
		try{
				con = ds.getConnection();
				pstmt = con.prepareStatement(GET_ALL_STMT);
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()){
					sendvo = new SendVO();
					sendvo.setSid(rs.getInt(1));
					sendvo.setGid(rs.getInt(2));
					sendvo.setMember_id(rs.getString(3));
					sendvo.setTid(rs.getInt(4));
					sendvo.setStart_date(rs.getTimestamp(5));
					sendvo.setEnd_date(rs.getTimestamp(6));
					sendvo.setStatus(rs.getInt(7));
					list.add(sendvo);
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
	public List<SendVO> getAllAlive() {
		// TODO Auto-generated method stub
		SendVO sendvo = null;
		List<SendVO> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt;
		try{
				con = ds.getConnection();
				pstmt = con.prepareStatement(GET_ALL_STMT_Alive);
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()){
					sendvo = new SendVO();
					sendvo.setSid(rs.getInt(1));
					sendvo.setGid(rs.getInt(2));
					sendvo.setMember_id(rs.getString(3));
					sendvo.setTid(rs.getInt(4));
					sendvo.setStart_date(rs.getTimestamp(5));
					sendvo.setEnd_date(rs.getTimestamp(6));
					sendvo.setStatus(rs.getInt(7));
					list.add(sendvo);
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
	public List<SendVO> getAllByStatus(Integer status) {
		// TODO Auto-generated method stub
		SendVO sendvo = null;
		List<SendVO> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt;
		try{
				con = ds.getConnection();
				pstmt = con.prepareStatement(GET_ALL_BY_STATUS);
				pstmt.setInt(1, status);
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()){
					sendvo = new SendVO();
					sendvo.setSid(rs.getInt(1));
					sendvo.setGid(rs.getInt(2));
					sendvo.setMember_id(rs.getString(3));
					sendvo.setTid(rs.getInt(4));
					sendvo.setStart_date(rs.getTimestamp(5));
					sendvo.setEnd_date(rs.getTimestamp(6));
					sendvo.setStatus(rs.getInt(7));
					list.add(sendvo);
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
	public List<SendVOAssociations> getAllByStatusAssociations(Integer status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SendVOAssociations> getAllByAddressKeyAssociations(
			String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SendVOAssociations> CompositeQuery(Map<String, String[]> map) {
		// TODO Auto-generated method stub
		return null;
	}



	

}
