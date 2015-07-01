package com.vipad.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class VipadJDBCDAO implements VipadDAO_interface {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:XE";
	String userid = "g1";
	String passwd = "za101g1";

	private static final String INSERT_STMT = 
		"INSERT INTO ex_vipad (vid,member_id,gid,joindate,quitdate,status) VALUES (ex_vipad_seq.NEXTVAL, ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT vid,member_id,gid,joindate,quitdate,status FROM ex_vipad order by vid";
	private static final String GET_ONE_STMT = 
		"SELECT vid,member_id,gid,joindate,quitdate,status FROM ex_vipad where vid = ?";
	private static final String DELETE = 
		"DELETE FROM ex_vipad where vid = ?";
	private static final String UPDATE = 
		"UPDATE ex_vipad set gid=?, member_id=?, joindate=?, quitdate=?, status=? where vid = ?";
	private static final String GET_ONE_VIPAD =
			"SELECT vid,member_id,gid,joindate,quitdate,status FROM ex_vipad where gid = ?";
	private static final String GET_VIPAD_BY_MEMBER =
			"SELECT vid,member_id,gid,joindate,quitdate,status FROM ex_vipad where member_id = ?";

	@Override
	public int insert(VipadVO vipadVO) {

		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);
			
			con.setAutoCommit(false); //在JDBC裡面預設AutoCommit為true，假如要rollback，要把預設改為false
			
			pstmt.setInt(1, vipadVO.getGid());
			pstmt.setString(2, vipadVO.getMember_id());
			pstmt.setTimestamp(3, vipadVO.getJoindate());
			pstmt.setTimestamp(4, vipadVO.getQuitdate());
			pstmt.setInt(5, vipadVO.getStatus());

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
	public int update(VipadVO vipadVO) {

		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			con.setAutoCommit(false); //在JDBC裡面預設AutoCommit為true，假如要rollback，要把預設改為false
			
			pstmt.setInt(1, vipadVO.getGid());
			pstmt.setString(2, vipadVO.getMember_id());
			pstmt.setTimestamp(3, vipadVO.getJoindate());
			pstmt.setTimestamp(4, vipadVO.getQuitdate());
			pstmt.setInt(5, vipadVO.getStatus());
			pstmt.setInt(6, vipadVO.getVid());

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
	public int delete(VipadVO vipadVO) {

		int deleteCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);
			
			con.setAutoCommit(false); //在JDBC裡面預設AutoCommit為true，假如要rollback，要把預設改為false

			pstmt.setInt(1, vipadVO.getVid());

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
	public VipadVO findByPrimaryKey(Integer vid) {

		VipadVO vipadVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setInt(1, vid);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				vipadVO = new VipadVO();
				vipadVO.setVid(rs.getInt("vid"));
				vipadVO.setMember_id(rs.getString("member_id"));
				vipadVO.setGid(rs.getInt("gid"));
				vipadVO.setJoindate(rs.getTimestamp("joindate"));
				vipadVO.setQuitdate(rs.getTimestamp("quitdate"));
				vipadVO.setStatus(rs.getInt("status"));

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
		return vipadVO;
	}

	@Override
	public List<VipadVO> getAll() {
		List<VipadVO> list = new ArrayList<VipadVO>();
		VipadVO vipadVO = null;

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
				vipadVO = new VipadVO();
				vipadVO.setVid(rs.getInt("vid"));
				vipadVO.setMember_id(rs.getString("member_id"));
				vipadVO.setGid(rs.getInt("gid"));
				vipadVO.setJoindate(rs.getTimestamp("joindate"));
				vipadVO.setQuitdate(rs.getTimestamp("quitdate"));
				vipadVO.setStatus(rs.getInt("status"));

				list.add(vipadVO); // Store the row in the list
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
	
	@Override
	public List<VipadVO> getOneVipad(Integer gid) {
		List<VipadVO> list = new ArrayList<VipadVO>();
		VipadVO vipadVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_VIPAD);
			pstmt.setInt(1, gid);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				vipadVO = new VipadVO();
				vipadVO.setVid(rs.getInt("vid"));
				vipadVO.setMember_id(rs.getString("member_id"));
				vipadVO.setGid(rs.getInt("gid"));
				vipadVO.setJoindate(rs.getTimestamp("joindate"));
				vipadVO.setQuitdate(rs.getTimestamp("quitdate"));
				vipadVO.setStatus(rs.getInt("status"));

				list.add(vipadVO); // Store the row in the list
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
	
	@Override
	public List<VipadVO> getVipadByMember(String member_id) {
		List<VipadVO> list = new ArrayList<VipadVO>();
		VipadVO vipadVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_VIPAD_BY_MEMBER);
			pstmt.setString(1, member_id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				vipadVO = new VipadVO();
				vipadVO.setVid(rs.getInt("vid"));
				vipadVO.setMember_id(rs.getString("member_id"));
				vipadVO.setGid(rs.getInt("gid"));
				vipadVO.setJoindate(rs.getTimestamp("joindate"));
				vipadVO.setQuitdate(rs.getTimestamp("quitdate"));
				vipadVO.setStatus(rs.getInt("status"));

				list.add(vipadVO); // Store the row in the list
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

		VipadJDBCDAO dao = new VipadJDBCDAO();

//		// 新增
//		VipadVO vipadVO1 = new VipadVO();
//		vipadVO1.setGid(1001);
//		vipadVO1.setJoindate(java.sql.Date.valueOf("2015-05-01"));
//		vipadVO1.setQuitdate(java.sql.Date.valueOf("2015-05-02"));
//		vipadVO1.setStatus(0);
//		dao.insert(vipadVO1);
//
//		// 修改
//		VipadVO vipadVO2 = new VipadVO();
//		vipadVO2.setVid(1001);
//		vipadVO2.setGid(1003);
//		vipadVO2.setJoindate(java.sql.Date.valueOf("2015-05-03"));
//		vipadVO2.setQuitdate(java.sql.Date.valueOf("2015-05-04"));
//		vipadVO2.setStatus(1);
//		dao.update(vipadVO2);

//		// 刪除
//		dao.delete(1003);

//		// 查詢
//		VipadVO vipadVO3 = dao.findByPrimaryKey(1001);
//		System.out.print(vipadVO3.getVid() + ",");
//		System.out.print(vipadVO3.getGid() + ",");
//		System.out.print(vipadVO3.getJoindate() + ",");
//		System.out.print(vipadVO3.getQuitdate() + ",");
//		System.out.println(vipadVO3.getStatus());
//		System.out.println("---------------------");
//
		// 查詢
		List<VipadVO> list = dao.getAll();
		for (VipadVO aVipad : list) {
			System.out.print(aVipad.getVid() + ",");
			System.out.print(aVipad.getGid() + ",");
			System.out.print(aVipad.getJoindate() + ",");
			System.out.print(aVipad.getQuitdate() + ",");
			System.out.print(aVipad.getStatus());
			System.out.println();
		}
	}

	@Override
	public List<VipadVO> getVipadByMemberAlive(String member_id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<VipadVO> getAllDelete() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VipadVO> getAllAlive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VipadHibernateVO> getAllAliveAssociations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VipadHibernateVO> getVipadByMemberAllAlive(String member_id) {
		// TODO Auto-generated method stub
		return null;
	}


	

	
}
