package com.point_tran.model;

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



public class Point_tranDAO implements Point_tran_interface {

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
	"INSERT INTO EX_POINT_TRAN (ptid,status,member_id) VALUES (EX_POINT_TRAN_seq.NEXTVAL, ?, ?)";
	private static final String GET_ALL_STMT = 
	"SELECT ptid,status,member_id,quitdate FROM EX_POINT_TRAN order by ptid";
	private static final String GET_ONE_STMT = 
	"SELECT ptid,status,member_id,quitdate FROM EX_POINT_TRAN where ptid = ?";
	private static final String DELETE = 
	"DELETE FROM EX_POINT_TRAN where ptid = ?";
	private static final String UPDATE = 
	"UPDATE EX_POINT_TRAN set status=?,member_id=?, quitdate=? where ptid = ?";

	@Override
	public int insert(Point_tranVO point_tranVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(INSERT_STMT);
			
			
			pstmt.setInt(1, point_tranVO.getStatus());
			pstmt.setString(2, point_tranVO.getMember_id());
			
			

			updateCount = pstmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);
			// Handle any driver errors
		}catch (SQLException se) {
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
		return updateCount;
	}

	@Override
	public int update(Point_tranVO point_tranVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(UPDATE);

			
			pstmt.setInt(1, point_tranVO.getStatus());
			pstmt.setString(2, point_tranVO.getMember_id());
			pstmt.setTimestamp(3, point_tranVO.getQuitDate());
			pstmt.setInt(4, point_tranVO.getPtid());

			updateCount = pstmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);

			// Handle any driver errors
		}catch (SQLException se) {
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
		return updateCount;
	}

	@Override
	public int delete(Integer ptid) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setInt(1, ptid);
			
			updateCount = pstmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);


			// Handle any driver errors
		}catch (SQLException se) {
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
		return updateCount;
	}

	@Override
	public Point_tranVO findByPrimaryKey(Integer ptid) {
		Point_tranVO point_tranVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setInt(1, ptid);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// boardVo 也稱為 Domain objects
				point_tranVO = new Point_tranVO();
				point_tranVO.setPtid((rs.getInt("ptid")));
				point_tranVO.setStatus((rs.getInt("status")));
				point_tranVO.setMember_id(rs.getString("member_id"));
				point_tranVO.setQuitDate(rs.getTimestamp("quitdate"));

			}

			// Handle any driver errors
		}catch (SQLException se) {
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
		return point_tranVO;
	}

	@Override
	public List<Point_tranVO> getAll() {
		List<Point_tranVO> list = new ArrayList<Point_tranVO>();
		Point_tranVO point_tranVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// boardVO 也稱為 Domain objects
				point_tranVO = new Point_tranVO();
				point_tranVO.setPtid((rs.getInt("ptid")));
				point_tranVO.setStatus((rs.getInt("status")));
				point_tranVO.setMember_id(rs.getString("member_id"));
				point_tranVO.setQuitDate(rs.getTimestamp("quitdate"));

				list.add(point_tranVO); // Store the row in the vector
			}

			// Handle any driver errors
		}catch (SQLException se) {
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
	
		Point_tranDAO dao = new Point_tranDAO();
	
			 //新增
//				Point_tranVO point_tranVO = new Point_tranVO();
//				point_tranVO.setStatus(0);
//				point_tranVO.setMember_id("M1001");
//				
//				 int updateCount_insert = dao.insert(point_tranVO);
//				 System.out.println(updateCount_insert);
					
	
			 //修改
//			Point_tranVO point_tranVO = new Point_tranVO();
//			point_tranVO.setPtid(1001);
//			point_tranVO.setStatus(1);
//			point_tranVO.setMember_id("M1001");
//			java.util.Date date = new java.util.Date();
//			java.sql.Date update = new java.sql.Date(date.getTime());
//			point_tranVO.setQuitDate(update);
//			
//			 int updateCount_update = dao.update(point_tranVO);
//			 System.out.println(updateCount_update);
					
	
			 //刪除
//			 int updateCount_delete = dao.delete(1004);
//			 System.out.println(updateCount_delete);
	
			// 查詢
//			Point_tranVO point_tranVO = dao.findByPrimaryKey(1001);
//			System.out.print(point_tranVO.getPtid() + ",");
//			System.out.print(point_tranVO.getStatus() + ",");
//			System.out.print(point_tranVO.getMember_id() + ",");
//			System.out.print(point_tranVO.getQuitDate() + ",");
//	
//			System.out.println("---------------------");
	
			// 查詢
//			List<Point_tranVO> list = dao.getAll();
//			for (Point_tranVO point_tranVO : list) {
//				System.out.print(point_tranVO.getPtid() + ",");
//				System.out.print(point_tranVO.getStatus() + ",");
//				System.out.print(point_tranVO.getMember_id() + ",");
//				System.out.print(point_tranVO.getQuitDate() + ",");
//	
//				System.out.println();
//			}
		}
	
}
