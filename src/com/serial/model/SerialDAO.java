package com.serial.model;

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

public class SerialDAO implements Serial_interface{
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
	"INSERT INTO EX_SERIAL (serial_number,money,member_id) VALUES (?, ?, ?)";
	private static final String GET_ALL_STMT = 
	"SELECT serial_number,money,member_id FROM EX_SERIAL order by money";
	private static final String GET_ONE_STMT = 
	"SELECT serial_number,money,member_id FROM EX_SERIAL where serial_number = ?";
	private static final String DELETE = 
	"DELETE FROM EX_SERIAL where serial_number = ?";
	private static final String UPDATE = 
	"UPDATE EX_SERIAL set money=?,member_id=? where serial_number = ?";
	@Override
	public void insert(SerialVO serialVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, serialVO.getSerial_number());
			pstmt.setInt(2, serialVO.getMoney());
			pstmt.setString(3, serialVO.getMember_id());

			updateCount = pstmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);
			// Handle any driver errors
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
	public void update(SerialVO serialVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(UPDATE);

			
			pstmt.setString(3, serialVO.getSerial_number());
			pstmt.setInt(1, serialVO.getMoney());
			pstmt.setString(2, serialVO.getMember_id());
			

			updateCount = pstmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);

			// Handle any driver errors
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
	public void delete(String serial_number) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setString(1, serial_number);
			
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
	}

	@Override
	public SerialVO findByPrimaryKey(String serial_number) {
		SerialVO serialVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			 
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setString(1, serial_number);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// boardVo 也稱為 Domain objects
				serialVO = new SerialVO();
				serialVO.setSerial_number((rs.getString("serial_number")));
				serialVO.setMoney((rs.getInt("money")));
				serialVO.setMember_id(rs.getString("member_id"));
				

			}

			// Handle any driver errors
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
		return serialVO;
	}

	@Override
	public List<SerialVO> getAll() {
		List<SerialVO> list = new ArrayList<SerialVO>();
		SerialVO serialVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// boardVO 也稱為 Domain objects
				serialVO = new SerialVO();
				serialVO.setSerial_number((rs.getString("serial_number")));
				serialVO.setMoney((rs.getInt("money")));
				serialVO.setMember_id(rs.getString("member_id"));
				list.add(serialVO); // Store the row in the vector
			}

			// Handle any driver errors
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
