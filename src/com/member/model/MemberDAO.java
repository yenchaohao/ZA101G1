package com.member.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MemberDAO implements Member_interface {

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


	private static final String INSERT_STMT = "INSERT INTO EX_MEMBER (member_id,email,mem_name,password,id_no,tel,address,birthday,pic,my_wish) VALUES(concat('M',ex_member_seq.nextval),?,?,?,?,?,?,?,?,?)";
	private static final String INSERT_STMT_FACEBOOK = "INSERT INTO EX_MEMBER (member_id,email,mem_name,tel,address,pic,mem_status) VALUES(?,?,?,?,?,?,20)"; 
	private static final String UPDATE = "UPDATE EX_MEMBER set EMAIL=?,MEM_NAME=?,PASSWORD=?,ID_NO=?,TEL=?,ADDRESS=?,BIRTHDAY=?,PIC=?,CREDIT=?,HAVING_P=?,PENDING_P=?,MEM_STATUS=?,MY_WISH=? where member_id = ?";
	private static final String DELETE_BY_MEMBER_ID = "DELETE FROM EX_MEMBER where MEMBER_ID = ?";
	private static final String FIND_BY_MEMBER_ID = "SELECT member_id,email,mem_name,password,id_no,tel,address,birthday,joindate,pic,credit,having_p,pending_p,mem_status,my_wish FROM EX_member WHERE member_id = ?";
	private static final String GET_ALL_STMT = 
			"SELECT member_id,email,mem_name,password,id_no,tel,address,birthday,joindate,pic,credit,having_p,pending_p,mem_status,my_wish from ex_member order by member_id";
	private static final String FIND_BY_EMAIL = 
			"SELECT member_id,email,mem_name,password,id_no,tel,address,birthday,joindate,pic,credit,having_p,pending_p,mem_status,my_wish FROM EX_member WHERE email = ?";
	private static final String GET_ALLAlive_STMT = 
			"SELECT member_id,email,mem_name,password,id_no,tel,address,birthday,joindate,pic,credit,having_p,pending_p,mem_status,my_wish from ex_member where mem_status != 2 order by member_id";
	private static final String FIND_BY_EMAIL_AND_PASSWORD = 
			"SELECT member_id,email,mem_name,password,id_no,tel,address,birthday,joindate,pic,credit,having_p,pending_p,mem_status,my_wish FROM EX_member WHERE email = ? and password = ?";
	
	
	@Override
	public int insert(MemberVO membervo) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(INSERT_STMT);
			con.setAutoCommit(false);

			// "INSERT INTO EX_MEMBER (email,mem_name,password,id_no,tel,address,birthday,pic,pic_name,my_wish) VALUES(?,?,?,?,?,?,?,?,?,?)";
			pstmt.setString(1, membervo.getEmail());
			pstmt.setString(2, membervo.getMem_name());
			pstmt.setString(3, membervo.getPassword());
			pstmt.setString(4, membervo.getId_no());
			pstmt.setString(5, membervo.getTel());
			pstmt.setString(6, membervo.getAddress());
			pstmt.setDate(7, membervo.getBirthday());

			// 把存在vo裡面的byte陣列 轉成InputStream 寫進資料庫 (因為"pstmt.setBinaryStream"
			// 方法只接收InputStream)
			
			pstmt.setBytes(8, membervo.getPic());

			pstmt.setString(9, membervo.getMy_wish());

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
	public int insertForFacebook(MemberVO membervo) {
		// TODO Auto-generated method stub
				int insertCount = 0;
				Connection con = null;
				PreparedStatement pstmt;
				try {
					con = ds.getConnection();

					pstmt = con.prepareStatement(INSERT_STMT_FACEBOOK);
					con.setAutoCommit(false);

					//INSERT INTO EX_MEMBER (member_id,email,mem_name,tel,address,pic,mem_status) VALUES(?,?,?,?,?,?,20)"
					pstmt.setString(1, membervo.getMember_id());
					pstmt.setString(2, membervo.getEmail());
					pstmt.setString(3, membervo.getMem_name());
					pstmt.setString(4, membervo.getTel());
					pstmt.setString(5, membervo.getAddress());
					pstmt.setBytes(6, membervo.getPic());

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
	
	
	public String insertGetPrimaryKey(MemberVO membervo){
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		String pk = "";
		try {
			String generatedColumns[]={"member_id"};
			con = ds.getConnection();

			pstmt = con.prepareStatement(INSERT_STMT,generatedColumns);
			con.setAutoCommit(false);

			// "INSERT INTO EX_MEMBER (email,mem_name,password,id_no,tel,address,birthday,pic,pic_name,my_wish) VALUES(?,?,?,?,?,?,?,?,?,?)";
			pstmt.setString(1, membervo.getEmail());
			pstmt.setString(2, membervo.getMem_name());
			pstmt.setString(3, membervo.getPassword());
			pstmt.setString(4, membervo.getId_no());
			pstmt.setString(5, membervo.getTel());
			pstmt.setString(6, membervo.getAddress());
			pstmt.setDate(7, membervo.getBirthday());

			// 把存在vo裡面的byte陣列 轉成InputStream 寫進資料庫 (因為"pstmt.setBinaryStream"
			// 方法只接收InputStream)
			
			pstmt.setBytes(8, membervo.getPic());

			pstmt.setString(9, membervo.getMy_wish());

			insertCount = pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			while(rs.next()){
				pk = rs.getString(1);
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
	public int update(MemberVO membervo) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(UPDATE);
			con.setAutoCommit(false);

			// set
			// EMAIL=?,MEM_NAME=?,PASSWORD=?,ID_NO=?,TEL=?,ADDRESS=?,BIRTHDAY=?,PIC=?,PIC_NAME=?,CREDIT=?,HAVING_P=?,PENDING_P=?,MEM_STATUS=?,MY_WISH=?
			// where member_id = ?";
			pstmt.setString(1, membervo.getEmail());
			pstmt.setString(2, membervo.getMem_name());
			pstmt.setString(3, membervo.getPassword());
			pstmt.setString(4, membervo.getId_no());
			pstmt.setString(5, membervo.getTel());
			pstmt.setString(6, membervo.getAddress());
			pstmt.setDate(7, membervo.getBirthday());

			// 把存在vo裡面的byte陣列 轉成InputStream 寫進資料庫 (因為"pstmt.setBinaryStream"
			// 方法只接收InputStream)
			pstmt.setBytes(8, membervo.getPic());
			
			pstmt.setInt(9, membervo.getCredit());
			pstmt.setInt(10, membervo.getHaving_p());
			pstmt.setInt(11, membervo.getPending_p());
			pstmt.setInt(12, membervo.getMem_status());
			pstmt.setString(13, membervo.getMy_wish());
			pstmt.setString(14, membervo.getMember_id());

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
	public int deleteByMember_id(String member_id) {
		// TODO Auto-generated method stub
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(DELETE_BY_MEMBER_ID);
			con.setAutoCommit(false);

			pstmt.setString(1, member_id);

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
	public MemberVO findByMember_id(String member_id) {
		// TODO Auto-generated method stub

		Connection con = null;
		PreparedStatement pstmt;
		MemberVO membervo = null;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(FIND_BY_MEMBER_ID);
			pstmt.setString(1, member_id);
			
			ResultSet rs = pstmt.executeQuery();

			// "SELECT member_id,email,mem_name,password,id_no,tel,address,birthday,joinday,pic,pic_name,credit,having_p,pending_p,mem_status,my_wish, FROM EX_member WHERE member_id = ? ";
			membervo = new MemberVO();
			while (rs.next()) {
				membervo.setMember_id(rs.getString(1));
				membervo.setEmail(rs.getString(2));
				membervo.setMem_name(rs.getString(3));
				membervo.setPassword(rs.getString(4));
				membervo.setId_no(rs.getString(5));
				membervo.setTel(rs.getString(6));
				membervo.setAddress(rs.getString(7));
				membervo.setBirthday(rs.getDate(8));
				membervo.setJoindate(rs.getDate(9));
				membervo.setPic(rs.getBytes(10));
				
				membervo.setCredit(rs.getInt(11));
				membervo.setHaving_p(rs.getInt(12));
				membervo.setPending_p(rs.getInt(13));
				membervo.setMem_status(rs.getInt(14));
				membervo.setMy_wish(rs.getString(15));
			}

		} catch (Exception ex) {
			System.out.println("Exception: "+ex.getMessage());

		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return membervo;
	}

	@Override
	public List<MemberVO> getAll() {
		// TODO Auto-generated method stub
		List<MemberVO> list = new ArrayList();
		Connection con = null;
		PreparedStatement pstmt;
		MemberVO membervo = null;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ALL_STMT);
			
			ResultSet rs = pstmt.executeQuery();

			// "SELECT member_id,email,mem_name,password,id_no,tel,address,birthday,joinday,pic,pic_name,credit,having_p,pending_p,mem_status,my_wish, FROM EX_member WHERE member_id = ? ";
			
			while (rs.next()) {
				membervo = new MemberVO();
				membervo.setMember_id(rs.getString(1));
				membervo.setEmail(rs.getString(2));
				membervo.setMem_name(rs.getString(3));
				membervo.setPassword(rs.getString(4));
				membervo.setId_no(rs.getString(5));
				membervo.setTel(rs.getString(6));
				membervo.setAddress(rs.getString(7));
				membervo.setBirthday(rs.getDate(8));
				membervo.setJoindate(rs.getDate(9));
				membervo.setPic(rs.getBytes(10));
				
				membervo.setCredit(rs.getInt(11));
				membervo.setHaving_p(rs.getInt(12));
				membervo.setPending_p(rs.getInt(13));
				membervo.setMem_status(rs.getInt(14));
				membervo.setMy_wish(rs.getString(15));
				list.add(membervo);
			}

		} catch (Exception ex) {
			System.out.println("Exception: "+ex.getMessage());

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
	public MemberVO findByEmail(String email) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt;
		MemberVO membervo = null;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(FIND_BY_EMAIL);
			pstmt.setString(1, email);
			
			ResultSet rs = pstmt.executeQuery();

			// "SELECT member_id,email,mem_name,password,id_no,tel,address,birthday,joinday,pic,pic_name,credit,having_p,pending_p,mem_status,my_wish, FROM EX_member WHERE member_id = ? ";
			membervo = new MemberVO();
			while (rs.next()) {
				membervo.setMember_id(rs.getString(1));
				membervo.setEmail(rs.getString(2));
				membervo.setMem_name(rs.getString(3));
				membervo.setPassword(rs.getString(4));
				membervo.setId_no(rs.getString(5));
				membervo.setTel(rs.getString(6));
				membervo.setAddress(rs.getString(7));
				membervo.setBirthday(rs.getDate(8));
				membervo.setJoindate(rs.getDate(9));
				membervo.setPic(rs.getBytes(10));
				
				membervo.setCredit(rs.getInt(11));
				membervo.setHaving_p(rs.getInt(12));
				membervo.setPending_p(rs.getInt(13));
				membervo.setMem_status(rs.getInt(14));
				membervo.setMy_wish(rs.getString(15));
			}

		} catch (Exception ex) {
			System.out.println("Exception: "+ex.getMessage());

		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return membervo;
	}

	@Override
	public List<MemberVO> getAllAlive() {
		// TODO Auto-generated method stub
				List<MemberVO> list = new ArrayList();
				Connection con = null;
				PreparedStatement pstmt;
				MemberVO membervo = null;
				try {
					con = ds.getConnection();

					pstmt = con.prepareStatement(GET_ALLAlive_STMT);
					
					ResultSet rs = pstmt.executeQuery();

					// "SELECT member_id,email,mem_name,password,id_no,tel,address,birthday,joinday,pic,pic_name,credit,having_p,pending_p,mem_status,my_wish, FROM EX_member WHERE member_id = ? ";
					
					while (rs.next()) {
						membervo = new MemberVO();
						membervo.setMember_id(rs.getString(1));
						membervo.setEmail(rs.getString(2));
						membervo.setMem_name(rs.getString(3));
						membervo.setPassword(rs.getString(4));
						membervo.setId_no(rs.getString(5));
						membervo.setTel(rs.getString(6));
						membervo.setAddress(rs.getString(7));
						membervo.setBirthday(rs.getDate(8));
						membervo.setJoindate(rs.getDate(9));
						membervo.setPic(rs.getBytes(10));
						
						membervo.setCredit(rs.getInt(11));
						membervo.setHaving_p(rs.getInt(12));
						membervo.setPending_p(rs.getInt(13));
						membervo.setMem_status(rs.getInt(14));
						membervo.setMy_wish(rs.getString(15));
						list.add(membervo);
					}

				} catch (Exception ex) {
					System.out.println("Exception: "+ex.getMessage());

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

	public boolean findByEmailAndPassword(String email,String pwd){
		Connection con = null;
		PreparedStatement pstmt;
		MemberVO membervo = null;
		boolean result = false;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(FIND_BY_EMAIL_AND_PASSWORD);
			pstmt.setString(1, email);
			pstmt.setString(2, pwd);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
			String member_id = rs.getString(1);
			if(member_id != null)
				result = true;
			}
			
			
			

		} catch (Exception ex) {
			System.out.println("Exception: "+ex.getMessage());

		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return result;
		
	}

	@Override
	public List<MemberVO> getMemberByMemberName(String mem_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MemberVO> getAllWish() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MemberVO> getSerachWish(Map<String, String[]> map) {
		// TODO Auto-generated method stub
		return null;
	}

}
