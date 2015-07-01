package com.goodsimage.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.goods.model.GoodsVO;

public class GoodsImageJDBCDAO implements GoodsImage_interface {

	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String userid = "g1";
	String passwd = "za101g1";

	private static final String INSERT_STMT = "INSERT INTO ex_goodsimage (pic_number,pic,gid) VALUES (ex_goodsimage_seq.NEXTVAL,?,?)";
	private static final String UPDATE = "UPDATE ex_goodsimage set pic=?,gid=? where pic_number = ?";
	private static final String DELETE_BY_PIC_NUMBER = "DELETE FROM ex_goodsimage where pic_number = ?";
	private static final String GET_BY_GID = "SELECT pic_number,pic,gid FROM ex_goodsimage where gid = ?";
	private static final String GET_BY_PIC_NUMBER = "SELECT pic_number,pic,pic_name,gid FROM ex_goodsimage where pic_number = ?";

	@Override
	public int insert(GoodsImageVO goodsImageVO) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(INSERT_STMT);
			con.setAutoCommit(false);
			// pic_number,pic,pic_name,gid

			byte[] pic = goodsImageVO.getPic();
			InputStream image = new ByteArrayInputStream(pic);
			pstmt.setBinaryStream(1, image);

			pstmt.setInt(2, goodsImageVO.getGid());

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
	public int update(GoodsImageVO goodsImageVO) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(UPDATE);
			con.setAutoCommit(false);

			byte[] pic = goodsImageVO.getPic();
			InputStream image = new ByteArrayInputStream(pic);
			pstmt.setBinaryStream(1, image);

			pstmt.setInt(2, goodsImageVO.getGid());
			pstmt.setInt(3, goodsImageVO.getPic_number());

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
	public int deleteByPic_number(Integer pic_number) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(DELETE_BY_PIC_NUMBER);
			con.setAutoCommit(false);

			pstmt.setInt(1, pic_number);
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
	public List<GoodsImageVO> findByGid(Integer gid) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GoodsImageVO goodsImageVO = null;
		List<GoodsImageVO> goodsImageVOList = new ArrayList<>();
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_BY_GID);

			pstmt.setInt(1, gid);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				goodsImageVO = new GoodsImageVO();
				goodsImageVO.setPic_number(rs.getInt(1));

				InputStream is = rs.getBinaryStream(2);
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nRead = 0;
				byte[] data = new byte[4 * 1024];
				while ((nRead = is.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
				}
				goodsImageVO.setPic(buffer.toByteArray());
				buffer.close();

				goodsImageVO.setGid(rs.getInt(3));

				goodsImageVOList.add(goodsImageVO);
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
		return goodsImageVOList;
	}

	@Override
	public GoodsImageVO findByPicNumber(Integer pic_number) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		GoodsImageVO goodsImageVO = new GoodsImageVO();;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_BY_PIC_NUMBER);

			pstmt.setInt(1, pic_number);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				goodsImageVO.setPic_number(rs.getInt(1));
				goodsImageVO.setPic(rs.getBytes(2));
				goodsImageVO.setGid(rs.getInt(3));

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
		return goodsImageVO;
	}

}
