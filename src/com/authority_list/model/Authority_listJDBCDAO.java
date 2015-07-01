package com.authority_list.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.authority.model.AuthorityJDBCDAO;


public class Authority_listJDBCDAO implements Authority_listDAO_interface{
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:XE";
	String userid = "g1";
	String passwd = "za101g1";

	private static final String INSERT_STMT = 
		"INSERT INTO ex_authority_list (aid,fname) VALUES (?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT aid,fname FROM ex_authority_list order by aid";
	private static final String GET_ONE_STMT = 
		"SELECT aid,fname FROM ex_authority_list where aid = ?";
	private static final String DELETE = 
		"DELETE FROM ex_authority_list where aid = ?";
	private static final String UPDATE = 
		"UPDATE ex_authority_list set fname=? where aid = ?";

	@Override
	public int insert(Authority_listVO authority_listVO) {
		
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);
			
			con.setAutoCommit(false); //在JDBC裡面預設AutoCommit為true，假如要rollback，要把預設改為false
			
			pstmt.setInt(1, authority_listVO.getAid());
			pstmt.setString(2, authority_listVO.getFname());

			insertCount = pstmt.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true); //把AutoCommit為true

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
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
	public int update(Authority_listVO authority_listVO) {

		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			con.setAutoCommit(false); //在JDBC裡面預設AutoCommit為true，假如要rollback，要把預設改為false
			
			pstmt.setString(1, authority_listVO.getFname());
			pstmt.setInt(2, authority_listVO.getAid());

			updateCount = pstmt.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true); //把AutoCommit為true

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
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
	public int delete(Integer aid) {

		int deleteCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);
			
			con.setAutoCommit(false); //在JDBC裡面預設AutoCommit為true，假如要rollback，要把預設改為false

			pstmt.setInt(1, aid);

			deleteCount = pstmt.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true); //把AutoCommit為true

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
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
	public Authority_listVO findByPrimaryKey(Integer aid) {

		Authority_listVO authority_listVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setInt(1, aid);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				authority_listVO = new Authority_listVO();
				authority_listVO.setAid(rs.getInt("aid"));
				authority_listVO.setFname(rs.getString("fname"));
			}

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
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
		return authority_listVO;
	}

	@Override
	public List<Authority_listVO> getAll() {
		List<Authority_listVO> list = new ArrayList<Authority_listVO>();
		Authority_listVO authority_listVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				authority_listVO = new Authority_listVO();
				authority_listVO.setAid(rs.getInt("aid"));
				authority_listVO.setFname(rs.getString("fname"));

				list.add(authority_listVO); // Store the row in the list
			}

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
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

		Authority_listJDBCDAO dao = new Authority_listJDBCDAO();

//		// 新增
//		Authority_listVO authority_listVO1 = new Authority_listVO();
//		authority_listVO1.setAid(8);
//		authority_listVO1.setFname("人事管理");
//		dao.insert(authority_listVO1);
//
//		// 修改
//		Authority_listVO authority_listVO2 = new Authority_listVO();
//		authority_listVO2.setAid(8);
//		authority_listVO2.setFname("業務管理");
//		dao.update(authority_listVO2);
//
//		// 刪除
//		dao.delete(8);
//
		// 查詢
//		Authority_listVO authority_listVO3 = dao.findByPrimaryKey(7);
//		System.out.print(authority_listVO3.getAid() + ",");
//		System.out.println(authority_listVO3.getFname());
//		System.out.println("---------------------");
//
		// 查詢
		List<Authority_listVO> list = dao.getAll();
		for (Authority_listVO aAuthority_list : list) {
			System.out.print(aAuthority_list.getAid() + ",");
			System.out.print(aAuthority_list.getFname());
			System.out.println();
		}
	}
}
