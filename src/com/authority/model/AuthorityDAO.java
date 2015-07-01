package com.authority.model;

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



public class AuthorityDAO implements AuthorityDAO_interface{
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
		"INSERT INTO ex_authority (empid,aid) VALUES (?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT empid,aid FROM ex_authority order by empid";
	private static final String GET_ONE_STMT = 
		"SELECT empid,aid FROM ex_authority where empid = ?";
	private static final String DELETE = 
		"DELETE FROM ex_authority where empid = ? AND aid = ?";
	private static final String UPDATE = 
		"UPDATE ex_authority set aid=? where empid = ?";
	private static final String GET_ONE_AID =
		"SELECT empid,aid FROM ex_authority where empid = ? order by empid";

	@Override
	public int insert(AuthorityVO authorityVO) {

		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			con.setAutoCommit(false); //在JDBC裡面預設AutoCommit為true，假如要rollback，要把預設改為false
			
			pstmt.setString(1, authorityVO.getEmpid());
			pstmt.setInt(2, authorityVO.getAid());
		
			insertCount = pstmt.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true); //把AutoCommit為true

			// Handle any driver errors
		}catch (SQLException se) {
			if(con!=null){
				try{
				System.out.println("Transaction begin, start rollback");
				con.rollback();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return insertCount;

	}

	@Override
	public int update(AuthorityVO authorityVO) {

		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			con.setAutoCommit(false); //在JDBC裡面預設AutoCommit為true，假如要rollback，要把預設改為false
			
			pstmt.setInt(1, authorityVO.getAid());
			pstmt.setString(2, authorityVO.getEmpid());
			
			updateCount = pstmt.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true); //把AutoCommit為true

			// Handle any driver errors
		}catch (SQLException se) {
			if(con!=null){
				try{
				System.out.println("Transaction begin, start rollback");
				con.rollback();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return updateCount;

	}

	@Override
	public int delete(String empid, Integer aid) {

		int deleteCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);
			
			con.setAutoCommit(false); //在JDBC裡面預設AutoCommit為true，假如要rollback，要把預設改為false

			pstmt.setString(1, empid);
			pstmt.setInt(2, aid);

			deleteCount = pstmt.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true); //把AutoCommit為true

			// Handle any driver errors
		}catch (SQLException se) {
			if(con!=null){
				try{
				System.out.println("Transaction begin, start rollback");
				con.rollback();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return deleteCount;

	}

	@Override
	public AuthorityVO findByPrimaryKey(String empid) {

		AuthorityVO authorityVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, empid);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				authorityVO = new AuthorityVO();
				authorityVO.setEmpid(rs.getString("empid"));
				authorityVO.setAid(rs.getInt("aid"));
			}

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return authorityVO;
	}

	@Override
	public List<AuthorityVO> getAll() {
		List<AuthorityVO> list = new ArrayList<AuthorityVO>();
		AuthorityVO authorityVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				authorityVO = new AuthorityVO();
				authorityVO.setEmpid(rs.getString("empid"));
				authorityVO.setAid(rs.getInt("aid"));

				list.add(authorityVO); // Store the row in the list
			}

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	public List<AuthorityVO> getOneAid(String empid) {
		List<AuthorityVO> list = new ArrayList<AuthorityVO>();
		AuthorityVO authorityVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_AID);
			
			pstmt.setString(1, empid);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				authorityVO = new AuthorityVO();
				authorityVO.setEmpid(rs.getString("empid"));
				authorityVO.setAid(rs.getInt("aid"));

				list.add(authorityVO); // Store the row in the list
			}

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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

		AuthorityDAO dao = new AuthorityDAO();

//		// 新增
//		AuthorityVO authorityVO1 = new AuthorityVO();
//		authorityVO1.setEmpid("E1003");
//		authorityVO1.setAid(0);
//		dao.insert(authorityVO1);
//
//		// 修改
//		AuthorityVO authorityVO2 = new AuthorityVO();
//		authorityVO2.setEmpid("E1003");
//		authorityVO2.setAid(5);
//		dao.update(authorityVO2);
//
//		// 刪除
//		dao.delete("E1003");
//
		// 查詢
//		AuthorityVO authorityVO3 = dao.findByPrimaryKey("E1003");
//		System.out.print(authorityVO3.getEmpid() + ",");
//		System.out.println(authorityVO3.getAid());
//		System.out.println("---------------------");

		// 查詢
//		List<AuthorityVO> list = dao.getAll();
//		for (AuthorityVO aAuthority : list) {
//			System.out.print(aAuthority.getEmpid() + ",");
//			System.out.print(aAuthority.getAid());
//			System.out.println();
//		}
	}
}