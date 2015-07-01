package com.message.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class MessageJDBCDAO implements Message_interface {

	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String userid = "g1";
	String passwd = "za101g1";
	private static final String INSERT_STMT = 
	"INSERT INTO EX_MESSAGE (mid,member_id,title) VALUES (EX_MESSAGE_seq.NEXTVAL, ?, ?)";
	private static final String GET_ALL_STMT = 
	"SELECT mid,member_id,m_date,title FROM EX_MESSAGE order by mid";
	private static final String GET_ONE_STMT = 
	"SELECT mid,member_id,m_date,title FROM EX_MESSAGE where mid = ?";
	private static final String DELETE_MSG = 
	"DELETE FROM EX_MESSAGE where mid = ?";
	private static final String DELETE_REMSG = 
	"DELETE FROM EX_REMESSAGE where mid = ?";
	private static final String UPDATE = 
	"UPDATE EX_MESSAGE set member_id=?, m_date=?,title=? where mid = ?";
	private static final String GET_ALL_STMT_LATEST = 
			"SELECT mid,member_id,m_date,title FROM EX_MESSAGE order by m_date DESC";

	@Override
	public void insert(MessageVO messageVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(INSERT_STMT);
			
			
			pstmt.setString(1, messageVO.getMember_id());
			
			pstmt.setString(2, messageVO.getTitle());
			
			

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
	public int insertGetPK(MessageVO messageVO) {
		int updateCount = 0;
		int mid = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			String [] cols={"mid"};
			pstmt = con.prepareStatement(INSERT_STMT,cols);
			
			
			pstmt.setString(1, messageVO.getMember_id());		
			pstmt.setString(2, messageVO.getTitle());
			
			updateCount = pstmt.executeUpdate();
			ResultSet rs=pstmt.getGeneratedKeys();
			//ResultSetMetaData rsmd=rs.getMetaData();
			if(rs.next()){
				 mid=rs.getInt(1);
			}
			
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
		return mid;
	}
	@Override
	public void update(MessageVO messageVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(UPDATE);

			
			pstmt.setString(1, messageVO.getMember_id());
			pstmt.setTimestamp(2, messageVO.getM_date());
			pstmt.setString(3, messageVO.getTitle());
			
			pstmt.setInt(4, messageVO.getMid());

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
	public void delete(Integer mid) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			//刪回覆
			pstmt = con.prepareStatement(DELETE_REMSG);	
			pstmt.setInt(1, mid);
			updateCount = pstmt.executeUpdate();
			
			//再刪主題
			pstmt = con.prepareStatement(DELETE_MSG);			
			pstmt.setInt(1, mid);
			pstmt.executeUpdate();
			
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
	public MessageVO findByPrimaryKey(Integer mid) {
		MessageVO messageVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setInt(1, mid);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// boardVo 也稱為 Domain objects
				messageVO = new MessageVO();
				messageVO.setMid((rs.getInt("mid")));	
				messageVO.setMember_id(rs.getString("member_id"));
				messageVO.setM_date(rs.getTimestamp("m_date"));
				messageVO.setTitle(rs.getString("title"));
				

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
		return messageVO;
	}

	@Override
	public List<MessageVO> getAll() {
		List<MessageVO> list = new ArrayList<MessageVO>();
		MessageVO messageVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// boardVO 也稱為 Domain objects
				messageVO = new MessageVO();
				messageVO.setMid((rs.getInt("mid")));	
				messageVO.setMember_id(rs.getString("member_id"));
				messageVO.setM_date(rs.getTimestamp("m_date"));
				messageVO.setTitle(rs.getString("title"));
				

				list.add(messageVO); // Store the row in the vector
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
	public List<MessageVO> getAllLatest() {
		// TODO Auto-generated method stub
				List<MessageVO> list = new ArrayList<MessageVO>();
				MessageVO messageVO = null;

				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;

				try {

					Class.forName(driver);
					con = DriverManager.getConnection(url, userid, passwd);
					con.setAutoCommit(false); 
					pstmt = con.prepareStatement(GET_ALL_STMT_LATEST);
					rs = pstmt.executeQuery();

					while (rs.next()) {
						// boardVO 也稱為 Domain objects
						messageVO = new MessageVO();
						messageVO.setMid((rs.getInt("mid")));	
						messageVO.setMember_id(rs.getString("member_id"));
						messageVO.setM_date(rs.getTimestamp("m_date"));
						messageVO.setTitle(rs.getString("title"));
						

						list.add(messageVO); // Store the row in the vector
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
	
		MessageJDBCDAO dao = new MessageJDBCDAO();
	
			 //新增
//				MessageVO messageVO = new MessageVO();
//				messageVO.setMember_id("M1001");
//				
//				messageVO.setTitle("標題4");
//
//				 int updateCount_insert = dao.insert(messageVO);
//				 System.out.println(updateCount_insert);
		 //新增取PK
//		MessageVO messageVO = new MessageVO();
//		messageVO.setMember_id("M1001");
//		
//		messageVO.setTitle("標題4");
//
//		 int pk = dao.insertGetPK(messageVO);
//		 System.out.println(pk);	
	
			 //修改
//			MessageVO messageVO = new MessageVO();
//			messageVO.setMid(1001);
//			messageVO.setMember_id("M1001");			
//			java.util.Date date = new java.util.Date();
//			java.sql.Date update = new java.sql.Date(date.getTime());
//			messageVO.setM_date(update);			
//			messageVO.setTitle("標題5");
//						
//			 int updateCount_update = dao.update(messageVO);
//			 System.out.println(updateCount_update);
					
	
			 //刪除
//			 int updateCount_delete = dao.delete(1003);
//			 System.out.println(updateCount_delete);
	
			// 查詢
//			MessageVO messageVO = dao.findByPrimaryKey(1001);
//			System.out.print(messageVO.getMid() + ",");
//			System.out.print(messageVO.getMember_id() + ",");
//			System.out.print(messageVO.getM_date().getTime() + ",");
//			System.out.print(messageVO.getTitle() + ",");
//			
//			
//			java.sql.Time time=new java.sql.Time(messageVO.getM_date().getTime());
//			java.sql.Date date=new java.sql.Date(messageVO.getM_date().getTime());
//			System.out.print(date+" "+time + ",");
	
			// 查詢
//			List<MessageVO> list = dao.getAll();
//			for (MessageVO messageVO : list) {
//				System.out.print(messageVO.getMid() + ",");
//				System.out.print(messageVO.getMember_id() + ",");
//				System.out.print(messageVO.getM_date() + ",");
//				System.out.print(messageVO.getTitle() + ",");
//
//	
//				System.out.println();
//			}
//			
		}
	@Override
	public List<MessageVO> search(String title) {
		List<MessageVO> list = new ArrayList<MessageVO>();
		MessageVO messageVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement("SELECT mid,member_id,m_date,title FROM EX_MESSAGE  where title like '%"+
					title+"%' order by m_date DESC");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// boardVO 也稱為 Domain objects
				messageVO = new MessageVO();
				messageVO.setMid((rs.getInt("mid")));	
				messageVO.setMember_id(rs.getString("member_id"));
				messageVO.setM_date(rs.getTimestamp("m_date"));
				messageVO.setTitle(rs.getString("title"));
				

				list.add(messageVO); // Store the row in the vector
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
	


	
}
