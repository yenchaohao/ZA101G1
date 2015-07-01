package com.favorite.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class FavoriteJDBCDAO implements Favorite_interface {

	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String userid = "g1";
	String passwd = "za101g1";
	private static final String INSERT_STMT = 
	"INSERT INTO EX_FAVORITE (fid,gid,member_id) VALUES (EX_FAVORITE_seq.NEXTVAL, ?, ?)";
	private static final String GET_ALL_STMT = 
	"SELECT fid,gid,member_id,joindate FROM EX_FAVORITE order by fid";
	private static final String GET_ONE_STMT = 
	"SELECT fid,gid,member_id,joindate FROM EX_FAVORITE where fid = ?";
	private static final String DELETE = 
	"DELETE FROM EX_FAVORITE where fid = ?";
	private static final String UPDATE = 
	"UPDATE EX_FAVORITE set gid=?,member_id=?, joindate=? where fid = ?";

	@Override
	public int insert(FavoriteVO favoriteVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(INSERT_STMT);
			
			
			pstmt.setInt(1, favoriteVO.getGid());
			pstmt.setString(2, favoriteVO.getMember_id());
			
			

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
		return updateCount;
	}

	@Override
	public int update(FavoriteVO favoriteVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(UPDATE);

			
			pstmt.setInt(1, favoriteVO.getGid());
			pstmt.setString(2, favoriteVO.getMember_id());
			pstmt.setDate(3, favoriteVO.getJoindate());
			pstmt.setInt(4, favoriteVO.getFid());

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
		return updateCount;
	}

	@Override
	public int delete(Integer fid) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setInt(1, fid);
			
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
		return updateCount;
	}

	@Override
	public FavoriteVO findByPrimaryKey(Integer fid) {
		FavoriteVO favoriteVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setInt(1, fid);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// boardVo 也稱為 Domain objects
				favoriteVO = new FavoriteVO();
				favoriteVO.setFid((rs.getInt("fid")));
				favoriteVO.setGid((rs.getInt("gid")));
				favoriteVO.setMember_id(rs.getString("member_id"));
				favoriteVO.setJoindate(rs.getDate("joindate"));

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
		return favoriteVO;
	}

	@Override
	public List<FavoriteVO> getAll() {
		List<FavoriteVO> list = new ArrayList<FavoriteVO>();
		FavoriteVO favoriteVO = null;

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
				favoriteVO = new FavoriteVO();
				favoriteVO.setFid((rs.getInt("fid")));
				favoriteVO.setGid((rs.getInt("gid")));
				favoriteVO.setMember_id(rs.getString("member_id"));
				favoriteVO.setJoindate(rs.getDate("joindate"));

				list.add(favoriteVO); // Store the row in the vector
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
	
		FavoriteJDBCDAO dao = new FavoriteJDBCDAO();
	
			// 新增
//				FavoriteVO favoriteVO = new FavoriteVO();
//				favoriteVO.setGid(1006);
//				favoriteVO.setMember_id("M1001");
//			
//				 int updateCount_insert = dao.insert(favoriteVO);
//				 System.out.println(updateCount_insert);
					
	
			 //修改
//			FavoriteVO favoriteVO = new FavoriteVO();
//			favoriteVO.setFid(1001);
//			favoriteVO.setGid(1010);
//			favoriteVO.setMember_id("M1001");
//			java.util.Date date = new java.util.Date();
//			java.sql.Date update = new java.sql.Date(date.getTime());
//			favoriteVO.setJoinDate(update);
//			
//			 int updateCount_update = dao.update(favoriteVO);
//			 System.out.println(updateCount_update);
					
	
			 //刪除
//			 int updateCount_delete = dao.delete(1018);
//			 System.out.println(updateCount_delete);
	
			// 查詢
//			FavoriteVO favoriteVO = dao.findByPrimaryKey(1001);
//			System.out.print(favoriteVO.getFid() + ",");
//			System.out.print(favoriteVO.getGid() + ",");
//			System.out.print(favoriteVO.getMember_id() + ",");
//			System.out.print(favoriteVO.getJoinDate() + ",");
//	
//			System.out.println("---------------------");
	
			// 查詢
//			List<FavoriteVO> list = dao.getAll();
//			for (FavoriteVO favoriteVO : list) {
//				System.out.print(favoriteVO.getFid() + ",");
//				System.out.print(favoriteVO.getGid() + ",");
//				System.out.print(favoriteVO.getMember_id() + ",");
//				System.out.print(favoriteVO.getJoinDate() + ",");
//	
//				System.out.println();
//			}
		}

	@Override
	public List<FavoriteVO> getAllByMember(String member_id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
