package com.report.model;

import java.sql.Connection;
import java.sql.Date;
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

public class ReportDAO implements Report_interface {

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

	private static final String INSERT_STMT = "INSERT INTO EX_REPORT (rid,gid,member_id) VALUES (ex_report_seq.NEXTVAL, ?, ?)";
	private static final String GET_ALL_STMT = "SELECT rid,gid,member_id,r_date FROM EX_report order by rid";
	private static final String GET_ONE_STMT = "SELECT rid,gid,member_id,r_date FROM EX_report where rid = ?";
	private static final String DELETE = "DELETE FROM EX_report where rid = ?";
	private static final String UPDATE = "UPDATE EX_report set gid=?,member_id=?,r_date=? where rid = ?";

	@Override
	public int insert(ReportVO reportVO) {
		// TODO Auto-generated method stub
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setInt(1, reportVO.getGid());
			pstmt.setString(2, reportVO.getMember_id());

			updateCount = pstmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);
			// Handle any driver errors
		}catch (SQLException se) {
			System.out.println(se.getMessage());
			if (con != null) {
				try {
					System.out.println("Transaction begin, start rollback");
					con.rollback();
				} catch (Exception e) {
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
	public int update(ReportVO reportVO) {
		// TODO Auto-generated method stub
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setInt(1, reportVO.getGid());
			pstmt.setString(2, reportVO.getMember_id());
			java.util.Date date = new java.util.Date();
			java.sql.Date update = new java.sql.Date(date.getTime());
			pstmt.setDate(3, update);
			pstmt.setInt(4, reportVO.getRid());
			updateCount = pstmt.executeUpdate();

			con.commit();
			con.setAutoCommit(true);
			// Handle any driver errors
		}catch (SQLException se) {
			System.out.println(se.getMessage());
			if (con != null) {
				try {
					System.out.println("Transaction begin, start rollback");
					con.rollback();
				} catch (Exception e) {
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
	public int delete(Integer rid) {
		// TODO Auto-generated method stub
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setInt(1, rid);
			updateCount = pstmt.executeUpdate();

			con.commit();
			con.setAutoCommit(true);
			// Handle any driver errors
		}catch (SQLException se) {
			System.out.println(se.getMessage());
			if (con != null) {
				try {
					System.out.println("Transaction begin, start rollback");
					con.rollback();
				} catch (Exception e) {
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
	public ReportVO findByPrimaryKey(Integer rid) {
		// TODO Auto-generated method stub

		Connection con = null;
		PreparedStatement pstmt = null;
		ReportVO reportvo = new ReportVO();

		try {

			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ONE_STMT);
			pstmt.setInt(1, rid);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				reportvo.setRid(rs.getInt(1));
				reportvo.setGid(rs.getInt(2));
				reportvo.setMember_id(rs.getString(3));
				reportvo.setR_date(rs.getTimestamp(4));
			}

			// Handle any driver errors
		}catch (SQLException se) {
			System.out.println(se.getMessage());

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
		return reportvo;
	}

	@Override
	public List<ReportVO> getAll() {
		// TODO Auto-generated method stub

		Connection con = null;
		PreparedStatement pstmt = null;
		ReportVO reportvo = null;
		List<ReportVO> list = new ArrayList<>();

		try {

			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ALL_STMT);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				reportvo = new ReportVO();
				reportvo.setRid(rs.getInt(1));
				reportvo.setGid(rs.getInt(2));
				reportvo.setMember_id(rs.getString(3));
				reportvo.setR_date(rs.getTimestamp(4));
				list.add(reportvo);
			}

			// Handle any driver errors
		}catch (SQLException se) {
			System.out.println(se.getMessage());

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
		return list;
	}

	@Override
	public ReportVO findByGidAndMember_id(String member_id, int gid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReportVO> findReportByGid(int gid) {
		// TODO Auto-generated method stub
		return null;
	}

}
