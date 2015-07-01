package com.tran.model;

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

public class TranDAO implements Tran_interface {

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

	private static final String INSERT_STMT = "INSERT INTO EX_TRAN (TID,RES_MEMBER_ID,RES_GID,REQ_MEMBER_ID,REQ_GID) VALUES(ex_tran_seq.NEXTVAL, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE EX_TRAN set RES_MEMBER_ID=?,RES_GID=?,REQ_MEMBER_ID=?,REQ_GID=?, RES_DATE= ?,STATUS=? where Tid = ?";
	private static final String DELETE_BY_TID = "DELETE FROM EX_TRAN where Tid = ?";
	private static final String FIND_BY_TID = "SELECT TID,RES_MEMBER_ID,RES_GID,REQ_MEMBER_ID,REQ_GID,RES_DATE,REQ_DATE,STATUS FROM EX_TRAN WHERE TID = ?";
	private static final String GET_ALL_STMT = "SELECT TID,RES_MEMBER_ID,RES_GID,REQ_MEMBER_ID,REQ_GID,RES_DATE,REQ_DATE,STATUS FROM EX_TRAN order by TID";
	private static final String GET_UNREPLY = "SELECT TID,RES_MEMBER_ID,RES_GID,REQ_MEMBER_ID,REQ_GID,RES_DATE,REQ_DATE,STATUS FROM EX_TRAN WHERE STATUS=0 order by TID";
	private static final String GET_ONE_BY_REQ_MEMBER_ID="SELECT * FROM EX_TRAN WHERE REQ_MEMBER_ID=? ORDER BY REQ_DATE ";
	private static final String GET_ONE_BY_RES_MEMBER_ID="SELECT * FROM EX_TRAN WHERE RES_MEMBER_ID=? ORDER BY RES_DATE ";
	private static final String GET_ONE_BY_REQ_MEMBER_ID_UN="SELECT * FROM EX_TRAN WHERE REQ_MEMBER_ID=? AND STATUS = 0 ORDER BY REQ_DATE ";
	private static final String GET_ONE_BY_RES_MEMBER_ID_UN="SELECT * FROM EX_TRAN WHERE RES_MEMBER_ID=? AND STATUS = 0 ORDER BY RES_DATE ";
	private static final String GET_ALL_BY_MEMBER_ID_FINAL="SELECT * FROM EX_TRAN WHERE (REQ_MEMBER_ID=? OR RES_MEMBER_ID=?) AND STATUS = 1 ORDER BY RES_DATE";

	@Override
	public void insert(TranVO tranvo) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(INSERT_STMT);
			con.setAutoCommit(false);
			pstmt.setString(1, tranvo.getRes_member_id());
			pstmt.setInt(2, tranvo.getRes_gid());
			pstmt.setString(3, tranvo.getReq_member_id());
			pstmt.setInt(4, tranvo.getReq_gid());
			

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

	}

	@Override
	public void update(TranVO tranvo) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		int UpdateCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(UPDATE);
			con.setAutoCommit(false);
			pstmt.setString(1, tranvo.getRes_member_id());
			pstmt.setInt(2, tranvo.getRes_gid());
			pstmt.setString(3, tranvo.getReq_member_id());
			pstmt.setInt(4, tranvo.getReq_gid());
			pstmt.setTimestamp(5, tranvo.getRes_date());
			pstmt.setInt(6, tranvo.getStatus());
			pstmt.setInt(7, tranvo.getTid());
			UpdateCount = pstmt.executeUpdate();
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


	}

	@Override
	public void deleteByTid(Integer tid) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(DELETE_BY_TID);
			con.setAutoCommit(false);
			pstmt.setInt(1, tid);
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


	}

	@Override
	public TranVO findByTid(Integer tid) {
		// TODO Auto-generated method stub
		TranVO tranvo = new TranVO();
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_TID);
			pstmt.setInt(1, tid);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				tranvo.setTid(rs.getInt(1));
				tranvo.setRes_member_id(rs.getString(2));
				tranvo.setRes_gid(rs.getInt(3));
				tranvo.setReq_member_id(rs.getString(4));
				tranvo.setReq_gid(rs.getInt(5));
				tranvo.setRes_date(rs.getTimestamp(6));
				tranvo.setReq_date(rs.getTimestamp(7));
				tranvo.setStatus(rs.getInt(8));
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

		return tranvo;

	}

	@Override
	public List<TranVO> getAll() {
		// TODO Auto-generated method stub
		List<TranVO> list = new ArrayList<>();
		TranVO tranvo = null;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				tranvo = new TranVO();
				tranvo.setTid(rs.getInt(1));
				tranvo.setRes_member_id(rs.getString(2));
				tranvo.setRes_gid(rs.getInt(3));
				tranvo.setReq_member_id(rs.getString(4));
				tranvo.setReq_gid(rs.getInt(5));
				tranvo.setRes_date(rs.getTimestamp(6));
				tranvo.setReq_date(rs.getTimestamp(7));
				tranvo.setStatus(rs.getInt(8));
				list.add(tranvo);
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

		return list;

	}

	public List<TranVO> findByReqMember_id(String member_id){
		Connection con = null;
		PreparedStatement pstmt;
		TranVO tranVO=null;
		List<TranVO> list=new ArrayList<>();
		try {
			con = ds.getConnection();
	
			pstmt = con.prepareStatement(GET_ONE_BY_REQ_MEMBER_ID);
	
			pstmt.setString(1, member_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				tranVO = new TranVO();
				tranVO.setTid(rs.getInt(1));
				tranVO.setRes_member_id(rs.getString(2));
				tranVO.setRes_gid(rs.getInt(3));
				tranVO.setReq_member_id(rs.getString(4));
				tranVO.setReq_gid(rs.getInt(5));
				tranVO.setRes_date(rs.getTimestamp(6));
				tranVO.setReq_date(rs.getTimestamp(7));
				tranVO.setStatus(rs.getInt(8));
				
				list.add(tranVO);
	
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
		return list;
	}

	public List<TranVO> findByResMember_id(String member_id){
		Connection con = null;
		PreparedStatement pstmt;
		TranVO tranVO=null;
		List<TranVO> list=new ArrayList<>();
		try {
			con = ds.getConnection();
	
			pstmt = con.prepareStatement(GET_ONE_BY_RES_MEMBER_ID);
	
			pstmt.setString(1, member_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				tranVO = new TranVO();
				tranVO.setTid(rs.getInt(1));
				tranVO.setRes_member_id(rs.getString(2));
				tranVO.setRes_gid(rs.getInt(3));
				tranVO.setReq_member_id(rs.getString(4));
				tranVO.setReq_gid(rs.getInt(5));
				tranVO.setRes_date(rs.getTimestamp(6));
				tranVO.setReq_date(rs.getTimestamp(7));
				tranVO.setStatus(rs.getInt(8));
				
				list.add(tranVO);
	
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
		return list;
	}

	@Override
	public List<TranVO> getUnreply() {
		List<TranVO> list = new ArrayList<>();
		TranVO tranvo = null;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_UNREPLY);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				tranvo = new TranVO();
				tranvo.setTid(rs.getInt(1));
				tranvo.setRes_member_id(rs.getString(2));
				tranvo.setRes_gid(rs.getInt(3));
				tranvo.setReq_member_id(rs.getString(4));
				tranvo.setReq_gid(rs.getInt(5));
				tranvo.setRes_date(rs.getTimestamp(6));
				tranvo.setReq_date(rs.getTimestamp(7));
				tranvo.setStatus(rs.getInt(8));
				list.add(tranvo);
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

		return list;
	}
	
	public List<TranVO> findByReqMember_idUn(String member_id){
		Connection con = null;
		PreparedStatement pstmt;
		TranVO tranVO=null;
		List<TranVO> list=new ArrayList<>();
		try {
			con = ds.getConnection();
	
			pstmt = con.prepareStatement(GET_ONE_BY_REQ_MEMBER_ID_UN);
	
			pstmt.setString(1, member_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				tranVO = new TranVO();
				tranVO.setTid(rs.getInt(1));
				tranVO.setRes_member_id(rs.getString(2));
				tranVO.setRes_gid(rs.getInt(3));
				tranVO.setReq_member_id(rs.getString(4));
				tranVO.setReq_gid(rs.getInt(5));
				tranVO.setRes_date(rs.getTimestamp(6));
				tranVO.setReq_date(rs.getTimestamp(7));
				tranVO.setStatus(rs.getInt(8));
				
				list.add(tranVO);
	
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
		return list;
	}

	public List<TranVO> findByResMember_idUn(String member_id){
		Connection con = null;
		PreparedStatement pstmt;
		TranVO tranVO=null;
		List<TranVO> list=new ArrayList<>();
		try {
			con = ds.getConnection();
	
			pstmt = con.prepareStatement(GET_ONE_BY_RES_MEMBER_ID_UN);
	
			pstmt.setString(1, member_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				tranVO = new TranVO();
				tranVO.setTid(rs.getInt(1));
				tranVO.setRes_member_id(rs.getString(2));
				tranVO.setRes_gid(rs.getInt(3));
				tranVO.setReq_member_id(rs.getString(4));
				tranVO.setReq_gid(rs.getInt(5));
				tranVO.setRes_date(rs.getTimestamp(6));
				tranVO.setReq_date(rs.getTimestamp(7));
				tranVO.setStatus(rs.getInt(8));
				
				list.add(tranVO);
	
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
		return list;
	}

	
	@Override
	public List<TranVO> getAllByMember_idFinal(String member_id) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		TranVO tranVO=null;
		List<TranVO> list=new ArrayList<>();
		try {
			con = ds.getConnection();
	
			pstmt = con.prepareStatement(GET_ALL_BY_MEMBER_ID_FINAL);
	
			pstmt.setString(1, member_id);
			pstmt.setString(2, member_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				tranVO = new TranVO();
				tranVO.setTid(rs.getInt(1));
				tranVO.setRes_member_id(rs.getString(2));
				tranVO.setRes_gid(rs.getInt(3));
				tranVO.setReq_member_id(rs.getString(4));
				tranVO.setReq_gid(rs.getInt(5));
				tranVO.setRes_date(rs.getTimestamp(6));
				tranVO.setReq_date(rs.getTimestamp(7));
				tranVO.setStatus(rs.getInt(8));
				
				list.add(tranVO);
	
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
		return list;
	}

	@Override
	public List<TranVO> getAllByMember_idFail(String member_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TranVO> getAllByMember_idWait(String member_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TranVO> getAllByMemberNoFail(String member_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TranVO> getAllAlive() {
		// TODO Auto-generated method stub
		return null;
	}

}
