package com.credit.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class CreditJDBCDAO implements Credit_interface {

	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String userid = "g1";
	String passwd = "za101g1";
	private static final String INSERT_STMT = 
	"INSERT INTO EX_CREDIT (cid,memberA_id,memberB_id,status,tid) VALUES (EX_CREDIT_seq.NEXTVAL, ?, ?,?,?)";
	private static final String GET_ALL_STMT = 
	"SELECT cid,memberA_id,memberB_id,status,tid FROM EX_CREDIT order by cid";
	private static final String GET_ONE_STMT = 
	"SELECT cid,memberA_id,memberB_id,status,tid FROM EX_CREDIT where cid = ?";
	private static final String DELETE = 
	"DELETE FROM EX_CREDIT where cid = ?";
	private static final String UPDATE = 
	"UPDATE EX_CREDIT set memberA_id=?,memberB_id=?, status=?,tid=? where cid = ?";

	@Override
	public void insert(CreditVO creditVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(INSERT_STMT);
			
			
			pstmt.setString(1, creditVO.getMemberA_id());
			pstmt.setString(2, creditVO.getMemberB_id());
			pstmt.setInt(3, creditVO.getStatus());
			pstmt.setInt(4, creditVO.getTid());
			
			

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
	public void update(CreditVO creditVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(UPDATE);

			
			pstmt.setString(1, creditVO.getMemberA_id());
			pstmt.setString(2, creditVO.getMemberB_id());
			pstmt.setInt(3, creditVO.getStatus());
			pstmt.setInt(4, creditVO.getTid());
			pstmt.setInt(5, creditVO.getCid());

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
	public void delete(Integer cid) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setInt(1, cid);
			
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
	public CreditVO findByPrimaryKey(Integer cid) {
		CreditVO creditVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setInt(1, cid);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// boardVo 也稱為 Domain objects
				creditVO = new CreditVO();
				creditVO.setCid((rs.getInt("cid")));
				creditVO.setMemberA_id(rs.getString("memberA_id"));
				creditVO.setMemberB_id(rs.getString("memberB_id"));
				creditVO.setStatus((rs.getInt("status")));
				creditVO.setTid((rs.getInt("tid")));

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
		return creditVO;
	}

	@Override
	public List<CreditVO> getAll() {
		List<CreditVO> list = new ArrayList<CreditVO>();
		CreditVO creditVO = null;

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
				creditVO = new CreditVO();
				creditVO.setCid((rs.getInt("cid")));
				creditVO.setMemberA_id(rs.getString("memberA_id"));
				creditVO.setMemberB_id(rs.getString("memberB_id"));
				creditVO.setStatus((rs.getInt("status")));
				creditVO.setTid((rs.getInt("tid")));

				list.add(creditVO); // Store the row in the vector
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
	
		CreditJDBCDAO dao = new CreditJDBCDAO();
	
			 //新增
//				CreditVO creditVO = new CreditVO();
//				creditVO.setMemberA_id("M1008");
//				creditVO.setMemberB_id("M1006");
//				creditVO.setStatus(0);
//				creditVO.setTid(1004);
//				
//				 int updateCount_insert = dao.insert(creditVO);
//				 System.out.println(updateCount_insert);
					
	
			 //修改
//			Point_tranVO creditVO = new Point_tranVO();
//			creditVO.setPtid(1001);
//			creditVO.setStatus(1);
//			creditVO.setMember_id("M1001");
//			java.util.Date date = new java.util.Date();
//			java.sql.Date update = new java.sql.Date(date.getTime());
//			creditVO.setQuitDate(update);
//			
//			 int updateCount_update = dao.update(creditVO);
//			 System.out.println(updateCount_update);
					
	
			 //刪除
//			 int updateCount_delete = dao.delete(1004);
//			 System.out.println(updateCount_delete);
	
			// 查詢
//			Point_tranVO creditVO = dao.findByPrimaryKey(1001);
//			System.out.print(creditVO.getPtid() + ",");
//			System.out.print(creditVO.getStatus() + ",");
//			System.out.print(creditVO.getMember_id() + ",");
//			System.out.print(creditVO.getQuitDate() + ",");
//	
//			System.out.println("---------------------");
	
			// 查詢
//			List<Point_tranVO> list = dao.getAll();
//			for (Point_tranVO creditVO : list) {
//				System.out.print(creditVO.getPtid() + ",");
//				System.out.print(creditVO.getStatus() + ",");
//				System.out.print(creditVO.getMember_id() + ",");
//				System.out.print(creditVO.getQuitDate() + ",");
//	
//				System.out.println();
//			}
		}
	
}
