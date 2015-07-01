package com.post.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.send.model.SendVO;

public class PostDAO implements Post_interface {
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
			"INSERT INTO EX_POST (PID,EMPID,TITLE,POST,PIC) VALUES(ex_post_seq.NEXTVAL, ?, ?, ?, ?)";
	private static final String UPDATE = 
			"UPDATE EX_POST set EMPID=?,TITLE=?,POST=?,POSTDATE=default,PIC=? where PID = ?";
	private static final String DELETE_BY_PID = 
			"DELETE FROM EX_POST where PID = ?";
	private static final String FIND_BY_PID = 
			"SELECT PID,EMPID,TITLE,POST,POSTDATE,PIC FROM EX_POST WHERE PID = ?";
	private static final String GET_ALL_STMT = 
			"SELECT PID,EMPID,TITLE,POST,POSTDATE,PIC FROM EX_POST order by PID";
	private static final String GET_ALL_STMT_LATEST = 
			"SELECT PID,EMPID,TITLE,POST,POSTDATE,PIC FROM EX_POST order by POSTDATE DESC";
	private static final String GET_ALL_BY_EMPID = 
			"SELECT PID,EMPID,TITLE,POST,POSTDATE,PIC FROM EX_POST WHERE empid = ? order by PID";

	
	@Override
	public int insert(PostVO postvo) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try{
			    con = ds.getConnection();
			
				pstmt = con.prepareStatement(INSERT_STMT);
				con.setAutoCommit(false);
				
				pstmt.setString(1,postvo.getEmpid());
				pstmt.setString(2, postvo.getTitle());
				pstmt.setString(3,postvo.getPost());
				pstmt.setBytes(4, postvo.getPic());
				
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
	public int update(PostVO postvo) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try{
				con = ds.getConnection();
			
				pstmt = con.prepareStatement(UPDATE);
				con.setAutoCommit(false);
				
				pstmt.setString(1,postvo.getEmpid());
				pstmt.setString(2, postvo.getTitle());
				pstmt.setString(3,postvo.getPost());
				pstmt.setBytes(4, postvo.getPic());
				pstmt.setInt(5, postvo.getPid());
				
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
	public int deleteByPid(Integer pid) {
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try{
				con = ds.getConnection();
			
				pstmt = con.prepareStatement(DELETE_BY_PID);
				con.setAutoCommit(false);
				
				pstmt.setInt(1, pid);
				
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
	public PostVO findByPid(Integer pid) {
		// TODO Auto-generated method stub
		PostVO postvo = new PostVO();
		Connection con = null;
		PreparedStatement pstmt;
		try{	
				con = ds.getConnection();
				pstmt = con.prepareStatement(FIND_BY_PID);
				pstmt.setInt(1, pid);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()){
					postvo.setPid(rs.getInt(1));
					postvo.setEmpid(rs.getString(2));
					postvo.setTitle(rs.getString(3));
					postvo.setPost(rs.getString(4));
					postvo.setPostdate(rs.getTimestamp(5));
					postvo.setPic(rs.getBytes(6));
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
		
		
		return postvo;
	}

	@Override
	public List<PostVO> getAll() {
		// TODO Auto-generated method stub
		PostVO postvo = null;
		List<PostVO> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt;
		try{
				con = ds.getConnection();
				pstmt = con.prepareStatement(GET_ALL_STMT);
				
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()){
					postvo = new PostVO();
					postvo.setPid(rs.getInt(1));
					postvo.setEmpid(rs.getString(2));
					postvo.setTitle(rs.getString(3));
					postvo.setPost(rs.getString(4));
					postvo.setPostdate(rs.getTimestamp(5));
					postvo.setPic(rs.getBytes(6));
					list.add(postvo);
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
	public List<PostVO> getAllLatest() {
		// TODO Auto-generated method stub
				PostVO postvo = null;
				List<PostVO> list = new ArrayList<>();
				Connection con = null;
				PreparedStatement pstmt;
				try{
						con = ds.getConnection();
						pstmt = con.prepareStatement(GET_ALL_STMT_LATEST);
						
						ResultSet rs = pstmt.executeQuery();
						while(rs.next()){
							postvo = new PostVO();
							postvo.setPid(rs.getInt(1));
							postvo.setEmpid(rs.getString(2));
							postvo.setTitle(rs.getString(3));
							postvo.setPost(rs.getString(4));
							postvo.setPostdate(rs.getTimestamp(5));
							postvo.setPic(rs.getBytes(6));
							list.add(postvo);
							
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
	public List<PostVO> findByEmpid(String empid) {
		// TODO Auto-generated method stub
		PostVO postvo = null;
		List<PostVO> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt;
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_BY_EMPID);
			pstmt.setString(1, empid);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				postvo = new PostVO();
				postvo.setPid(rs.getInt(1));
				postvo.setEmpid(rs.getString(2));
				postvo.setTitle(rs.getString(3));
				postvo.setPost(rs.getString(4));
				postvo.setPostdate(rs.getTimestamp(5));
				postvo.setPic(rs.getBytes(6));
				list.add(postvo);
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

	
	
}
