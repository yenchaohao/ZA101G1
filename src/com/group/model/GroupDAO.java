package com.group.model;

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

public class GroupDAO implements Group_interface {

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


	private static final String INSERT_STMT = "INSERT INTO ex_group (groupid,group_name) VALUES (?,?)";
	private static final String UPDATE = "UPDATE ex_group set group_name=? where groupid = ?";
	private static final String DELETE_BY_GROUPID = "DELETE FROM ex_group where groupid = ?";
	private static final String GET_ONE_BY_GROUPID = "SELECT groupid,group_name FROM ex_group where groupid = ?";
	private static final String GET_ALL_STMT = "SELECT groupid,group_name FROM ex_group order by groupid";

	@Override
	public int insert(GroupVO groupVO) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(INSERT_STMT);
			con.setAutoCommit(false);

			pstmt.setInt(1, groupVO.getGroupid());
			pstmt.setString(2, groupVO.getGroup_name());

			insertCount = pstmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			if (con != null) {
				try {
					System.out.println("Transaction begin, start rollback");
					con.rollback();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return insertCount;
	}

	@Override
	public int update(GroupVO groupVO) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(UPDATE);
			con.setAutoCommit(false);

			pstmt.setString(1, groupVO.getGroup_name());
			pstmt.setInt(2, groupVO.getGroupid());

			insertCount = pstmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			if (con != null) {
				try {
					System.out.println("Transaction begin, start rollback");
					con.rollback();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return insertCount;
	}

	@Override
	public int deleteByGroupid(Integer groupid) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(DELETE_BY_GROUPID);
			con.setAutoCommit(false);

			pstmt.setInt(1, groupid);
			insertCount = pstmt.executeUpdate();

			con.commit();
			con.setAutoCommit(true);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			if (con != null) {
				try {
					System.out.println("Transaction begin, start rollback");
					con.rollback();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return insertCount;
	}

	@Override
	public GroupVO findByGroupid(Integer groupid) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GroupVO groupVO = null;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ONE_BY_GROUPID);

			pstmt.setInt(1, groupid);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				groupVO = new GroupVO();
				groupVO.setGroupid(rs.getInt(1));
				groupVO.setGroup_name(rs.getString(2));
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return groupVO;
	}

	@Override
	public List<GroupVO> getAll() {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GroupVO groupVO = null;
		List<GroupVO> groupVOList = new ArrayList<>();
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ALL_STMT);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				groupVO = new GroupVO();
				groupVO.setGroupid(rs.getInt(1));
				groupVO.setGroup_name(rs.getString(2));

				groupVOList.add(groupVO);

			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return groupVOList;
	}

}
