package com.remessage.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class RemessageJDBCDAO implements Remessage_interface {

	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String userid = "g1";
	String passwd = "za101g1";
	private static final String INSERT_STMT = 
	"INSERT INTO EX_REMESSAGE (rid,member_id,mid,message) VALUES (EX_REMESSAGE_seq.NEXTVAL, ?, ?,?)";
	private static final String GET_ALL_STMT = 
	"SELECT rid,member_id,r_date,mid,message FROM EX_REMESSAGE order by rid";
	private static final String GET_BY_MID = 
	"SELECT rid,member_id,r_date,mid,message FROM EX_REMESSAGE where mid=?";
	private static final String GET_ONE_STMT = 
	"SELECT rid,member_id,r_date,mid,message FROM EX_REMESSAGE where rid = ?";
	private static final String DELETE = 
	"DELETE FROM EX_REMESSAGE where rid = ?";
	private static final String UPDATE = 
	"UPDATE EX_REMESSAGE set member_id=?, r_date=?,mid=?,message=? where rid = ?";

	@Override
	public void insert(RemessageVO remessageVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(INSERT_STMT);
			
			
			pstmt.setString(1, remessageVO.getMember_id());
			pstmt.setInt(2, remessageVO.getMid());
			pstmt.setString(3, remessageVO.getMessage());
			

			updateCount = pstmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);
			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			System.out.println(se.getMessage());
			if(con!=null){
				try{
				System.out.println("Transaction begin, start rollback");
				con.rollback();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
			
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	@Override
	public void update(RemessageVO remessageVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(UPDATE);

			
			pstmt.setString(1, remessageVO.getMember_id());
			pstmt.setTimestamp(2, remessageVO.getR_date());
			pstmt.setInt(3, remessageVO.getMid());
			pstmt.setString(4, remessageVO.getMessage());
			pstmt.setInt(5, remessageVO.getRid());

			updateCount = pstmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			System.out.println(se.getMessage());
			if(con!=null){
				try{
				System.out.println("Transaction begin, start rollback");
				con.rollback();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
			
			// Clean up JDBC resources
		}  finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	@Override
	public void delete(Integer rid) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setInt(1, rid);
			
			updateCount = pstmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);

			// Handle any driver errors
		}catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			System.out.println(se.getMessage());
			if(con!=null){
				try{
				System.out.println("Transaction begin, start rollback");
				con.rollback();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
			
			// Clean up JDBC resources
		}finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	@Override
	public RemessageVO findByPrimaryKey(Integer rid) {
		RemessageVO remessageVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setInt(1, rid);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// boardVo 也稱為 Domain objects
				remessageVO = new RemessageVO();
				remessageVO.setRid((rs.getInt("rid")));	
				remessageVO.setMember_id(rs.getString("member_id"));
				remessageVO.setR_date(rs.getTimestamp("r_date"));
				remessageVO.setMid(rs.getInt("mid"));
				remessageVO.setMessage(rs.getString("message"));

			}

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			System.out.println(se.getMessage());
		
			
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return remessageVO;
	}

	@Override
	public List<RemessageVO> getAll() {
		List<RemessageVO> list = new ArrayList<RemessageVO>();
		RemessageVO remessageVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
		 
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// boardVO 也稱為 Domain objects
				remessageVO = new RemessageVO();
				remessageVO.setRid((rs.getInt("rid")));	
				remessageVO.setMember_id(rs.getString("member_id"));
				remessageVO.setR_date(rs.getTimestamp("r_date"));
				remessageVO.setMid(rs.getInt("mid"));
				remessageVO.setMessage(rs.getString("message"));

				list.add(remessageVO); // Store the row in the vector
			}

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			System.out.println(se.getMessage());
		
			
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	@Override
	public List<RemessageVO> getByMid(Integer mid) {
		List<RemessageVO> list = new ArrayList<RemessageVO>();
		RemessageVO remessageVO = null;
	
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
	
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
		 
			pstmt = con.prepareStatement(GET_BY_MID);
			pstmt.setInt(1, mid);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
				// boardVO 也稱為 Domain objects
				remessageVO = new RemessageVO();
				remessageVO.setRid((rs.getInt("rid")));	
				remessageVO.setMember_id(rs.getString("member_id"));
				remessageVO.setR_date(rs.getTimestamp("r_date"));
				remessageVO.setMid(rs.getInt("mid"));
				remessageVO.setMessage(rs.getString("message"));
	
				list.add(remessageVO); // Store the row in the vector
			}
	
			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			System.out.println(se.getMessage());
		
			
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public static void main(String[] args) {
	
		RemessageJDBCDAO dao = new RemessageJDBCDAO();
	
			 //新增
//				RemessageVO remessageVO = new RemessageVO();
//				remessageVO.setMember_id("M1001");
//				
//				remessageVO.setMid(1010);
//				remessageVO.setMessage("內文10");
//				 int updateCount_insert = dao.insert(remessageVO);
//				 System.out.println(updateCount_insert);
					
	
			 //修改
//			RemessageVO remessageVO = new RemessageVO();
//			remessageVO.setRid(1004);
//			remessageVO.setMember_id("M1001");
//			java.util.Date date = new java.util.Date();
//			java.sql.Date update = new java.sql.Date(date.getTime());
//			remessageVO.setR_date(update);
//			remessageVO.setMid(1003);
//			remessageVO.setMessage("內文4");
//			
//			 int updateCount_update = dao.update(remessageVO);
//			 System.out.println(updateCount_update);
					
	
			 //刪除
//			 int updateCount_delete = dao.delete(1008);
//			 System.out.println(updateCount_delete);
	
			// 查詢
//			RemessageVO remessageVO = dao.findByPrimaryKey(1010);
//			System.out.print(remessageVO.getRid() + ",");
//			System.out.print(remessageVO.getMember_id() + ",");
//			System.out.print(remessageVO.getR_date() + ",");
//			System.out.print(remessageVO.getMid() + ",");
//			System.out.println(remessageVO.getMessage() + ",");
//	
//			System.out.println("---------------------");
	
			// 查詢
//			List<RemessageVO> list = dao.getAll();
//			for (RemessageVO remessageVO : list) {
//				System.out.print(remessageVO.getRid() + ",");
//				System.out.print(remessageVO.getMember_id() + ",");
//				System.out.print(remessageVO.getR_date() + ",");
//				System.out.print(remessageVO.getMid() + ",");
//				System.out.print(remessageVO.getMessage() + ",");
//	
//				System.out.println();
//			}
//			
		
		// 查詢同篇(mid)留言
//		List<RemessageVO> list = dao.getSame(1010);
//		for (RemessageVO remessageVO : list) {
//			System.out.print(remessageVO.getRid() + ",");
//			System.out.print(remessageVO.getMember_id() + ",");
//			System.out.print(remessageVO.getR_date() + ",");
//			System.out.print(remessageVO.getMid() + ",");
//			System.out.print(remessageVO.getMessage() + ",");
//			System.out.println();
//		}
	
		}
	
}
