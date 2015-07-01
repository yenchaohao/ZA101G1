package com.desire.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



public class DesireDAO implements Desire_interface {

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
	"INSERT INTO EX_DESIRE (did,groupId,member_id) VALUES (EX_DESIRE_seq.NEXTVAL, ?, ?)";
	private static final String GET_ALL_STMT = 
	"SELECT did,groupId,member_id,joindate FROM EX_DESIRE order by did";
	private static final String GET_ONE_STMT = 
	"SELECT did,groupId,member_id,joindate FROM EX_DESIRE where did = ?";
	private static final String DELETE = 
	"DELETE FROM EX_DESIRE where did = ?";
	private static final String UPDATE = 
	"UPDATE EX_DESIRE set groupId=?,member_id=?, joindate=? where did = ?";

	@Override
	public int insert(DesireVO desireVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(INSERT_STMT);
			
			
			pstmt.setInt(1, desireVO.getGroupId());
			pstmt.setString(2, desireVO.getMember_id());
			
			

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
	public int update(DesireVO desireVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(UPDATE);

			
			pstmt.setInt(1, desireVO.getGroupId());
			pstmt.setString(2, desireVO.getMember_id());
			pstmt.setDate(3, desireVO.getJoinDate());
			pstmt.setInt(4, desireVO.getDid());

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
	public int delete(Integer did) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setInt(1, did);
			
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
	public DesireVO findByPrimaryKey(Integer did) {
		DesireVO desireVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			 
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setInt(1, did);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// boardVo 也稱為 Domain objects
				desireVO = new DesireVO();
				desireVO.setDid((rs.getInt("did")));
				desireVO.setGroupId((rs.getInt("groupId")));
				desireVO.setMember_id(rs.getString("member_id"));
				desireVO.setJoinDate(rs.getDate("joindate"));

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
		return desireVO;
	}

	@Override
	public List<DesireVO> getAll() {
		List<DesireVO> list = new ArrayList<DesireVO>();
		DesireVO desireVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// boardVO 也稱為 Domain objects
				desireVO = new DesireVO();
				desireVO.setDid((rs.getInt("did")));
				desireVO.setGroupId((rs.getInt("groupId")));
				desireVO.setMember_id(rs.getString("member_id"));
				desireVO.setJoinDate(rs.getDate("joindate"));

				list.add(desireVO); // Store the row in the vector
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
	
		DesireDAO dao = new DesireDAO();
	
			 //新增
//				DesireVO desireVO = new DesireVO();
//				desireVO.setGroupId(4);
//				desireVO.setMember_id("M1001");
//				
//				int updateCount_insert = dao.insert(desireVO);
//				 System.out.println(updateCount_insert);
					
	
			 //修改
//			DesireVO desireVO = new DesireVO();
//			desireVO.setDid(1001);
//			desireVO.setGroupId(2);
//			desireVO.setMember_id("M1001");
//			java.util.Date date = new java.util.Date();
//			java.sql.Date update = new java.sql.Date(date.getTime());
//			desireVO.setJoinDate(update);
////			
//			 int updateCount_update = dao.update(desireVO);
//			 System.out.println(updateCount_update);
//					
	
			 //刪除
//			 int updateCount_delete = dao.delete(1005);
//			 System.out.println(updateCount_delete);
	
			// 查詢
//			DesireVO desireVO = dao.findByPrimaryKey(1001);
//			System.out.print(desireVO.getDid() + ",");
//			System.out.print(desireVO.getGroupId() + ",");
//			System.out.print(desireVO.getMember_id() + ",");
//			System.out.print(desireVO.getJoinDate() + ",");
//	
//			System.out.println("---------------------");
	
			// 查詢
			List<DesireVO> list = dao.getAll();
			for (DesireVO desireVO : list) {
				System.out.print(desireVO.getDid() + ",");
				System.out.print(desireVO.getGroupId() + ",");
				System.out.print(desireVO.getMember_id() + ",");
				System.out.print(desireVO.getJoinDate() + ",");
	
				System.out.println();
			}
		}
	
}
