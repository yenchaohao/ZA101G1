package com.goods.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GoodsJDBCDAO implements Goods_interface {

	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String userid = "g1";
	String passwd = "za101g1";

	private static final String INSERT_STMT = "INSERT INTO ex_goods (gid,groupid,member_id,g_name,g_describe,g_price,g_level,g_hot,joindate,quitdate,pic,goods_status) VALUES (ex_goods_seq.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE = "UPDATE ex_goods set groupid=?,member_id=?,g_name=?,g_describe=?,g_price=?,g_level=?,g_hot=?,joindate=?,quitdate=?,pic=?,goods_status=? where gid = ?";
	private static final String DELETE_BY_GID = "DELETE FROM ex_goods where gid = ?";
	private static final String GET_ONE_BY_GID = "SELECT gid,groupid,member_id,g_name,g_describe,g_price,g_level,g_hot,joindate, quitdate,pic,goods_status FROM ex_goods where gid = ?";
	private static final String GET_ALL_BY_G_NAME = "SELECT gid,groupid,member_id,g_name,g_describe,g_price,g_level,g_hot, joindate, quitdate,pic,goods_status FROM ex_goods where goods_status != 2 and  g_name LIKE ?";
	private static final String GET_ALL_BY_G_NAMEAlive = "SELECT gid,groupid,member_id,g_name,g_describe,g_price,g_level,g_hot, joindate, quitdate,pic,goods_status FROM ex_goods where goods_status = 1 and  g_name LIKE ?";
	private static final String GET_ALL_BY_GROUP_ID = "SELECT gid,groupid,member_id,g_name,g_describe,g_price,g_level,g_hot, joindate, quitdate,pic,goods_status FROM ex_goods where goods_status != 2 and  groupid = ?";
	private static final String GET_ALL_BY_GROUP_IDAlive = "SELECT gid,groupid,member_id,g_name,g_describe,g_price,g_level,g_hot, joindate, quitdate,pic,goods_status FROM ex_goods where goods_status = 1 and  groupid = ?";	
	private static final String GET_ALL_BY_MID = "SELECT gid,groupid,member_id,g_name,g_describe,g_price,g_level,g_hot, joindate,quitdate,pic,goods_status FROM ex_goods where goods_status != 2 and  member_id = ?";	
	private static final String GET_ALL_BY_MID_Alive = "SELECT gid,groupid,member_id,g_name,g_describe,g_price,g_level,g_hot, joindate,quitdate,pic,goods_status FROM ex_goods where goods_status = 1 and  member_id = ?";
	private static final String GET_ALL_BY_MID_G_STATUS = "SELECT gid,groupid,member_id,g_name,g_describe,g_price,g_level,g_hot, joindate,quitdate,pic,goods_status FROM ex_goods where member_id = ? and goods_status = ? ";
	private static final String GET_ALL_STMT = "SELECT gid,groupid,member_id,g_name,g_describe,g_price,g_level,g_hot, joindate, quitdate,pic,goods_status FROM ex_goods order by gid";
	private static final String GET_ALLAlive_STMT = "SELECT gid,groupid,member_id,g_name,g_describe,g_price,g_level,g_hot, joindate, quitdate,pic,goods_status FROM ex_goods where goods_status = 1 order by gid";
	private static final String GET_ALL_BY_G_NAME_GROUP_IDAlive = "SELECT gid,groupid,member_id,g_name,g_describe,g_price,g_level,g_hot, joindate, quitdate,pic,goods_status FROM ex_goods where goods_status = 1 and g_name LIKE ? AND groupid = ?";

	@Override
	public int insert(GoodsVO goodsVO) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(INSERT_STMT);
			con.setAutoCommit(false);
			// groupid,member_id,g_name,g_describe,g_price,g_level,g_hot,joindate,quitdate,pic,goods_status
			pstmt.setInt(1, goodsVO.getGroupid());
			pstmt.setString(2, goodsVO.getMember_id());
			pstmt.setString(3, goodsVO.getG_name());
			pstmt.setString(4, goodsVO.getG_describe());
			pstmt.setInt(5, goodsVO.getG_price());
			pstmt.setInt(6, goodsVO.getG_level());
			pstmt.setInt(7, goodsVO.getG_hot());
			pstmt.setTimestamp(8, new Timestamp(new java.util.Date().getTime()));
			//pstmt.setDate(8, goodsVO.getJoindate());
			pstmt.setTimestamp(9, goodsVO.getQuitdate());

			byte[] pic = goodsVO.getPic();
			InputStream image = new ByteArrayInputStream(pic);
			pstmt.setBinaryStream(10, image);

			pstmt.setInt(11, goodsVO.getGoods_status());

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
	public int insertGetPrimaryKey(GoodsVO goodsVO) {
		// TODO Auto-generated method stub
		int pk = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			Class.forName(driver);
			String generatedColumns[] = {"gid"};
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT,generatedColumns);
			con.setAutoCommit(false);
			// groupid,member_id,g_name,g_describe,g_price,g_level,g_hot,joindate,quitdate,pic,goods_status
			pstmt.setInt(1, goodsVO.getGroupid());
			pstmt.setString(2, goodsVO.getMember_id());
			pstmt.setString(3, goodsVO.getG_name());
			pstmt.setString(4, goodsVO.getG_describe());
			pstmt.setInt(5, goodsVO.getG_price());
			pstmt.setInt(6, goodsVO.getG_level());
			pstmt.setInt(7, goodsVO.getG_hot());
			pstmt.setTimestamp(8, goodsVO.getJoindate());
			pstmt.setTimestamp(9, goodsVO.getQuitdate());

			
			pstmt.setBytes(10, goodsVO.getPic());

			pstmt.setInt(11, goodsVO.getGoods_status());

			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			while(rs.next()){
				pk = rs.getInt(1);
			}
			
			
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

		return pk;
	}
	
	@Override
	public int update(GoodsVO goodsVO) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(UPDATE);
			con.setAutoCommit(false);
			// groupid=?,member_id=?,g_name=?,g_describe=?,g_price=?,g_level=?,g_hot=?,joindate=?,quitdate=?,pic=?,goods_status=?
			// where gid = ?
			pstmt.setInt(1, goodsVO.getGroupid());
			pstmt.setString(2, goodsVO.getMember_id());
			pstmt.setString(3, goodsVO.getG_name());
			pstmt.setString(4, goodsVO.getG_describe());
			pstmt.setInt(5, goodsVO.getG_price());
			pstmt.setInt(6, goodsVO.getG_level());
			pstmt.setInt(7, goodsVO.getG_hot());
			pstmt.setTimestamp(8, goodsVO.getJoindate());
			pstmt.setTimestamp(9, goodsVO.getQuitdate());

			byte[] pic = goodsVO.getPic();
			InputStream image = new ByteArrayInputStream(pic);
			pstmt.setBinaryStream(10, image);

			pstmt.setInt(11, goodsVO.getGoods_status());
			pstmt.setInt(12, goodsVO.getGid());

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
	public int deleteByGid(Integer gid) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(DELETE_BY_GID);
			con.setAutoCommit(false);

			pstmt.setInt(1, gid);
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
	public GoodsVO findByGid(Integer gid) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GoodsVO goodsVO = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ONE_BY_GID);

			pstmt.setInt(1, gid);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				goodsVO = new GoodsVO();
				goodsVO.setGid(rs.getInt(1));
				goodsVO.setGroupid(rs.getInt(2));
				goodsVO.setMember_id(rs.getString(3));
				goodsVO.setG_name(rs.getString(4));
				goodsVO.setG_describe(rs.getString(5));
				goodsVO.setG_price(rs.getInt(6));
				goodsVO.setG_level(rs.getInt(7));
				goodsVO.setG_hot(rs.getInt(8));
				
				goodsVO.setJoindate(rs.getTimestamp(9));
				goodsVO.setQuitdate(rs.getTimestamp(10));
				goodsVO.setPic(rs.getBytes(11));
				goodsVO.setGoods_status(rs.getInt(12));
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
		return goodsVO;
	}

	@Override
	public List<GoodsVO> findByG_name(String g_name) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GoodsVO goodsVO = null;
		List<GoodsVO> goodsvoList = new ArrayList<>();
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALL_BY_G_NAME);

			pstmt.setString(1, "%" + g_name + "%");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				goodsVO = new GoodsVO();
				goodsVO.setGid(rs.getInt(1));
				goodsVO.setGroupid(rs.getInt(2));
				goodsVO.setMember_id(rs.getString(3));
				goodsVO.setG_name(rs.getString(4));
				goodsVO.setG_describe(rs.getString(5));
				goodsVO.setG_price(rs.getInt(6));
				goodsVO.setG_level(rs.getInt(7));
				goodsVO.setG_hot(rs.getInt(8));
				goodsVO.setJoindate(rs.getTimestamp(9));
				goodsVO.setQuitdate(rs.getTimestamp(10));
				goodsVO.setPic(rs.getBytes(11));
				goodsVO.setGoods_status(rs.getInt(12));
				goodsvoList.add(goodsVO);

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
		return goodsvoList;
	}

	@Override
	public List<GoodsVO> findByG_nameAlive(String g_name) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GoodsVO goodsVO = null;
		List<GoodsVO> goodsvoList = new ArrayList<>();
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALL_BY_G_NAMEAlive);

			pstmt.setString(1, "%" + g_name + "%");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				goodsVO = new GoodsVO();
				goodsVO.setGid(rs.getInt(1));
				goodsVO.setGroupid(rs.getInt(2));
				goodsVO.setMember_id(rs.getString(3));
				goodsVO.setG_name(rs.getString(4));
				goodsVO.setG_describe(rs.getString(5));
				goodsVO.setG_price(rs.getInt(6));
				goodsVO.setG_level(rs.getInt(7));
				goodsVO.setG_hot(rs.getInt(8));
				goodsVO.setJoindate(rs.getTimestamp(9));
				goodsVO.setQuitdate(rs.getTimestamp(10));
				goodsVO.setPic(rs.getBytes(11));
				goodsVO.setGoods_status(rs.getInt(12));
				goodsvoList.add(goodsVO);

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
		return goodsvoList;
	}
		
	@Override
	public List<GoodsVO> findByGroup_id(Integer group_id) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GoodsVO goodsVO = null;
		List<GoodsVO> goodsvoList = new ArrayList<>();
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALL_BY_GROUP_ID);

			pstmt.setInt(1, group_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				goodsVO = new GoodsVO();
				goodsVO.setGid(rs.getInt(1));
				goodsVO.setGroupid(rs.getInt(2));
				goodsVO.setMember_id(rs.getString(3));
				goodsVO.setG_name(rs.getString(4));
				goodsVO.setG_describe(rs.getString(5));
				goodsVO.setG_price(rs.getInt(6));
				goodsVO.setG_level(rs.getInt(7));
				goodsVO.setG_hot(rs.getInt(8));
				goodsVO.setJoindate(rs.getTimestamp(9));
				goodsVO.setQuitdate(rs.getTimestamp(10));
				goodsVO.setPic(rs.getBytes(11));
				goodsVO.setGoods_status(rs.getInt(12));
				goodsvoList.add(goodsVO);

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
		return goodsvoList;
	}

	@Override
	public List<GoodsVO> findByGroup_idAlive(Integer group_id) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GoodsVO goodsVO = null;
		List<GoodsVO> goodsvoList = new ArrayList<>();
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALL_BY_GROUP_IDAlive);

			pstmt.setInt(1, group_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				goodsVO = new GoodsVO();
				goodsVO.setGid(rs.getInt(1));
				goodsVO.setGroupid(rs.getInt(2));
				goodsVO.setMember_id(rs.getString(3));
				goodsVO.setG_name(rs.getString(4));
				goodsVO.setG_describe(rs.getString(5));
				goodsVO.setG_price(rs.getInt(6));
				goodsVO.setG_level(rs.getInt(7));
				goodsVO.setG_hot(rs.getInt(8));
				goodsVO.setJoindate(rs.getTimestamp(9));
				goodsVO.setQuitdate(rs.getTimestamp(10));
				goodsVO.setPic(rs.getBytes(11));
				goodsVO.setGoods_status(rs.getInt(12));
				goodsvoList.add(goodsVO);

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
		return goodsvoList;
	}
	
	@Override
	public List<GoodsVO> findByMember_id(String member_id) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GoodsVO goodsVO = null;
		List<GoodsVO> goodsvoList = new ArrayList<>();
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALL_BY_MID);

			pstmt.setString(1, member_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				goodsVO = new GoodsVO();
				goodsVO.setGid(rs.getInt(1));
				goodsVO.setGroupid(rs.getInt(2));
				goodsVO.setMember_id(rs.getString(3));
				goodsVO.setG_name(rs.getString(4));
				goodsVO.setG_describe(rs.getString(5));
				goodsVO.setG_price(rs.getInt(6));
				goodsVO.setG_level(rs.getInt(7));
				goodsVO.setG_hot(rs.getInt(8));
				goodsVO.setJoindate(rs.getTimestamp(9));
				goodsVO.setQuitdate(rs.getTimestamp(10));
				goodsVO.setPic(rs.getBytes(11));
				goodsVO.setGoods_status(rs.getInt(12));
				goodsvoList.add(goodsVO);

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
		return goodsvoList;
	}

	@Override
	public List<GoodsVO> findByMember_idAlive(String member_id) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GoodsVO goodsVO = null;
		List<GoodsVO> goodsvoList = new ArrayList<>();
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALL_BY_MID_Alive);

			pstmt.setString(1, member_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				goodsVO = new GoodsVO();
				goodsVO.setGid(rs.getInt(1));
				goodsVO.setGroupid(rs.getInt(2));
				goodsVO.setMember_id(rs.getString(3));
				goodsVO.setG_name(rs.getString(4));
				goodsVO.setG_describe(rs.getString(5));
				goodsVO.setG_price(rs.getInt(6));
				goodsVO.setG_level(rs.getInt(7));
				goodsVO.setG_hot(rs.getInt(8));
				goodsVO.setJoindate(rs.getTimestamp(9));
				goodsVO.setQuitdate(rs.getTimestamp(10));
				goodsVO.setPic(rs.getBytes(11));
				goodsVO.setGoods_status(rs.getInt(12));
				goodsvoList.add(goodsVO);

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
		return goodsvoList;
	}

	@Override
	public List<GoodsVO> findByMember_idG_status(String member_id, Integer goods_status) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GoodsVO goodsVO = null;
		List<GoodsVO> goodsvoList = new ArrayList<>();
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALL_BY_MID_G_STATUS);

			pstmt.setString(1, member_id);
			pstmt.setInt(2, goods_status);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				goodsVO = new GoodsVO();
				goodsVO.setGid(rs.getInt(1));
				goodsVO.setGroupid(rs.getInt(2));
				goodsVO.setMember_id(rs.getString(3));
				goodsVO.setG_name(rs.getString(4));
				goodsVO.setG_describe(rs.getString(5));
				goodsVO.setG_price(rs.getInt(6));
				goodsVO.setG_level(rs.getInt(7));
				goodsVO.setG_hot(rs.getInt(8));
				goodsVO.setJoindate(rs.getTimestamp(9));
				goodsVO.setQuitdate(rs.getTimestamp(10));
				goodsVO.setPic(rs.getBytes(11));
				goodsVO.setGoods_status(rs.getInt(12));
				goodsvoList.add(goodsVO);

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
		return goodsvoList;
	}
	
	@Override
	public List<GoodsVO> findByG_name_Group_idAlive(String g_name, Integer group_id) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GoodsVO goodsVO = null;
		List<GoodsVO> goodsvoList = new ArrayList<>();
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALL_BY_G_NAME_GROUP_IDAlive);

			pstmt.setString(1, "%" + g_name + "%");
			pstmt.setInt(2 , group_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				goodsVO = new GoodsVO();
				goodsVO.setGid(rs.getInt(1));
				goodsVO.setGroupid(rs.getInt(2));
				goodsVO.setMember_id(rs.getString(3));
				goodsVO.setG_name(rs.getString(4));
				goodsVO.setG_describe(rs.getString(5));
				goodsVO.setG_price(rs.getInt(6));
				goodsVO.setG_level(rs.getInt(7));
				goodsVO.setG_hot(rs.getInt(8));
				goodsVO.setJoindate(rs.getTimestamp(9));
				goodsVO.setQuitdate(rs.getTimestamp(10));
				goodsVO.setPic(rs.getBytes(11));
				goodsVO.setGoods_status(rs.getInt(12));
				goodsvoList.add(goodsVO);

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
		return goodsvoList;
	}
	
	@Override
	public List<GoodsVO> getAllOrderByG_hot() {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GoodsVO goodsVO = null;
		List<GoodsVO> goodsVOList = new ArrayList<>();
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALL_STMT);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				goodsVO = new GoodsVO();
				goodsVO.setGid(rs.getInt(1));
				goodsVO.setGroupid(rs.getInt(2));
				goodsVO.setMember_id(rs.getString(3));
				goodsVO.setG_name(rs.getString(4));
				goodsVO.setG_describe(rs.getString(5));
				goodsVO.setG_price(rs.getInt(6));
				goodsVO.setG_level(rs.getInt(7));
				goodsVO.setG_hot(rs.getInt(8));
				goodsVO.setJoindate(rs.getTimestamp(9));
				goodsVO.setQuitdate(rs.getTimestamp(10));
				goodsVO.setPic(rs.getBytes(11));
				goodsVO.setGoods_status(rs.getInt(12));
				goodsVOList.add(goodsVO);

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
		return goodsVOList;
	}

	@Override
	public List<GoodsVO> getAllOrderJoindate() {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GoodsVO goodsVO = null;
		List<GoodsVO> goodsVOList = new ArrayList<>();
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALLAlive_STMT);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				goodsVO = new GoodsVO();
				goodsVO.setGid(rs.getInt(1));
				goodsVO.setGroupid(rs.getInt(2));
				goodsVO.setMember_id(rs.getString(3));
				goodsVO.setG_name(rs.getString(4));
				goodsVO.setG_describe(rs.getString(5));
				goodsVO.setG_price(rs.getInt(6));
				goodsVO.setG_level(rs.getInt(7));
				goodsVO.setG_hot(rs.getInt(8));
				goodsVO.setJoindate(rs.getTimestamp(9));
				goodsVO.setQuitdate(rs.getTimestamp(10));
				goodsVO.setPic(rs.getBytes(11));
				goodsVO.setGoods_status(rs.getInt(12));
				goodsVOList.add(goodsVO);

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
		return goodsVOList;
	}

	@Override
	public List<GoodsVO> getAllComposite(Map<String, String[]> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GoodsVO> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GoodsVOHibernate> getAllAssociations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GoodsVO> getAllCompositeByMemberSearch(Map<String, String[]> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GoodsVO> findByMember_idAllAlive(String member_id) {
		// TODO Auto-generated method stub
		return null;
	}

	



	

	

	

}
