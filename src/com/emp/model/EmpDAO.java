package com.emp.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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



public class EmpDAO implements EmpDAO_interface{
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
		"INSERT INTO ex_emp (empid,ename,password,hiredate,pic,email,status) VALUES (concat('E',ex_emp_seq.NEXTVAL), ?, ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT empid,ename,password,hiredate,pic,email,status FROM ex_emp order by empid";
	private static final String GET_ONE_STMT = 
		"SELECT empid,ename,password,hiredate,pic,email,status FROM ex_emp where empid = ?";
	private static final String DELETE = 
		"DELETE FROM ex_emp where empid = ?";
	private static final String UPDATE = 
		"UPDATE ex_emp set ename=?, password=?, hiredate=?, pic=?, email=?, status=? where empid = ?";
	private static final String GET_EMP_LOGIN = 
		"SELECT empid, password, status FROM ex_emp where empid = ? AND password = ?";

	@Override
	public int insert(EmpVO empVO) {
		
		int insertCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			
			
			con.setAutoCommit(false); //在JDBC裡面預設AutoCommit為true，假如要rollback，要把預設改為false
			
			pstmt.setString(1, empVO.getEname());
			pstmt.setString(2, empVO.getPassword());
			pstmt.setDate(3, empVO.getHiredate());
			//把存在VO的byte[]轉InputStream寫進資料庫，因為setBinaryStream只能放InputStream
			byte[] pic = empVO.getPic();
			InputStream images = new ByteArrayInputStream(pic);
			pstmt.setBinaryStream(4, images);
			pstmt.setString(5, empVO.getEmail());
			pstmt.setInt(6, empVO.getStatus());

			insertCount = pstmt.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true); //把AutoCommit為true

			// Handle any driver errors
		}catch (SQLException se) {
			if(con!=null){
				try{
				System.out.println("Transaction begin, start rollback");
				con.rollback();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return insertCount;

	}
	
	@Override
	public String insertGetPrimaryKey(EmpVO empVO) {
		String pk = "";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			String generatedColumns[] = {"empid"};
			pstmt = con.prepareStatement(INSERT_STMT,generatedColumns);
			
			
			
			con.setAutoCommit(false); //在JDBC裡面預設AutoCommit為true，假如要rollback，要把預設改為false
			
			pstmt.setString(1, empVO.getEname());
			pstmt.setString(2, empVO.getPassword());
			pstmt.setDate(3, empVO.getHiredate());
			//把存在VO的byte[]轉InputStream寫進資料庫，因為setBinaryStream只能放InputStream
			byte[] pic = empVO.getPic();
			InputStream images = new ByteArrayInputStream(pic);
			pstmt.setBinaryStream(4, images);
			pstmt.setString(5, empVO.getEmail());
			pstmt.setInt(6, empVO.getStatus());

			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			while(rs.next()){
				pk = rs.getString(1);
			}
			
			con.commit();
			con.setAutoCommit(true); //把AutoCommit為true

			// Handle any driver errors
		}catch (SQLException se) {
			if(con!=null){
				try{
				System.out.println("Transaction begin, start rollback");
				con.rollback();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return pk;
	}

	@Override
	public int update(EmpVO empVO) {
		
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			con.setAutoCommit(false); //在JDBC裡面預設AutoCommit為true，假如要rollback，要把預設改為false
			
			//"UPDATE ex_emp set ename=?, password=?, hiredate=?, pic=?, pic_name=?, status=? where empid = ?"
			pstmt.setString(1, empVO.getEname());
			pstmt.setString(2, empVO.getPassword());
			pstmt.setDate(3, empVO.getHiredate());
			
			byte[] pic = empVO.getPic();
			InputStream images = new ByteArrayInputStream(pic);
			pstmt.setBinaryStream(4, images);
			
			pstmt.setString(5, empVO.getEmail());
			pstmt.setInt(6, empVO.getStatus());
			pstmt.setString(7, empVO.getEmpid());
			
			updateCount = pstmt.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true); //把AutoCommit為true

			// Handle any driver errors
		}catch (SQLException se) {
			if(con!=null){
				try{
				System.out.println("Transaction begin, start rollback");
				con.rollback();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	public int delete(String empid) {
		
		int deleteCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);
			
			con.setAutoCommit(false); //在JDBC裡面預設AutoCommit為true，假如要rollback，要把預設改為false

			pstmt.setString(1, empid);

			deleteCount = pstmt.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true); //把AutoCommit為true

			// Handle any driver errors
		}catch (SQLException se) {
			if(con!=null){
				try{
				System.out.println("Transaction begin, start rollback");
				con.rollback();
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return deleteCount;

	}

	@Override
	public EmpVO findByPrimaryKey(String empid) {

		EmpVO empVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);
			//"SELECT empid,ename,password,hiredate,pic,pic_name,status FROM ex_emp where empid = ?"

			pstmt.setString(1, empid);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				empVO = new EmpVO();
				empVO.setEmpid(rs.getString("empid"));
				empVO.setEname(rs.getString("ename"));
				empVO.setPassword(rs.getString("password"));
				empVO.setHiredate(rs.getDate("hiredate"));
				empVO.setPic(rs.getBytes("pic"));
				empVO.setEmail(rs.getString("email"));
				empVO.setStatus(rs.getInt("status"));

			}

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return empVO;
	}

	@Override
	public List<EmpVO> getAll() {
		List<EmpVO> list = new ArrayList<EmpVO>();
		EmpVO empVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			//"SELECT empid,ename,password,hiredate,pic,pic_name,status FROM ex_emp order by empid"
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				empVO = new EmpVO();
				empVO.setEmpid(rs.getString("empid"));
				empVO.setEname(rs.getString("ename"));
				empVO.setPassword(rs.getString("password"));
				empVO.setHiredate(rs.getDate("hiredate"));
				empVO.setPic(rs.getBytes("pic"));
				empVO.setEmail(rs.getString("email"));
				empVO.setStatus(rs.getInt("status"));

				list.add(empVO); // Store the row in the list
			}

			// Handle any driver errors
		}catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	public boolean loginEmp(String empid, String password) {
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean result = false;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_EMP_LOGIN);
			//"SELECT empid, password FROM ex_emp where empid = ? AND password = ?"
			
			pstmt.setString(1, empid);
			pstmt.setString(2, password);
			
			rs = pstmt.executeQuery();
			String emp_id = null;
			while(rs.next()){
				emp_id = rs.getString("empid");
				if(emp_id != null){ //判斷帳號密碼是否正確
					result = true;
				}
			}
			
			// Handle any driver errors
		}catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return result;
	}
	
	@Override
	public List<EmpVO> getAllComposite(Map<String, String[]> map) {
		List<EmpVO> list = new ArrayList<EmpVO>();
		EmpVO empVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			String finalSQL = "select * from ex_emp "
			          + empUtil_CompositeQuery.get_WhereCondition(map)
			          + "order by empid";
			pstmt = con.prepareStatement(finalSQL);
			System.out.println("●●finalSQL(by DAO) = "+finalSQL);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				empVO = new EmpVO();
				empVO.setEmpid(rs.getString("empid"));
				empVO.setEname(rs.getString("ename"));
				empVO.setPassword(rs.getString("password"));
				empVO.setHiredate(rs.getDate("hiredate"));
				empVO.setPic(rs.getBytes("pic"));
				empVO.setEmail(rs.getString("email"));
				empVO.setStatus(rs.getInt("status"));

				list.add(empVO); // Store the row in the list
			}

			// Handle any driver errors
		}catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		
//		insert();
	//	update();
//		delete();
	//	findByPrimaryKey();
		getAllEmp();


	}
	static EmpVO empVO;
	static EmpDAO dao = new EmpDAO();
	//新增
	public static void insert(){
		empVO = new EmpVO();
		empVO.setEname("許家瑞");
		empVO.setPassword("E13");
		empVO.setHiredate(java.sql.Date.valueOf("2015-05-02"));
		//新增圖片，把圖片從InputStream轉成byte[]
		File pic = new File("images","E10.jpg");
		try {
			InputStream is = new FileInputStream(pic);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[is.available()];
			int len = 0;
			while((len = is.read(buffer)) != -1){
				baos.write(buffer,0,len);
			}
			baos.flush();
			byte[] image = baos.toByteArray();
			empVO.setPic(image); //把圖片放入
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		empVO.setStatus(1);
		dao.insert(empVO);
	}
	//修改
	public static void update(){
		empVO = new EmpVO();
		empVO.setEmpid("E1011");
		empVO.setEname("李白");
		empVO.setPassword("E12");
		empVO.setHiredate(java.sql.Date.valueOf("2015-05-04"));
		//修改圖片，把圖片從InputStream轉成byte[]
		File pic = new File("images","E2.jpg");
		try {
			InputStream is = new FileInputStream(pic);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[is.available()];
			int len = 0;
			while((len = is.read(buffer)) != -1){
				baos.write(buffer,0,len);
			}
			baos.flush();
			byte[] image = baos.toByteArray();
			empVO.setPic(image); //把圖片放入
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		empVO.setStatus(1);
		dao.update(empVO);
	}
	//刪除
	public static void delete(){
		dao.delete("E1011");
	}
	//查詢單項
	public static void findByPrimaryKey(){
		empVO = dao.findByPrimaryKey("E1011");
		System.out.print(empVO.getEmpid()+",");
		System.out.print(empVO.getEname()+",");
		System.out.print(empVO.getPassword()+",");
		System.out.print(empVO.getHiredate()+",");
		System.out.print(empVO.getPic()+",");
		System.out.println(empVO.getStatus());
		System.out.println("----------------------------");
	}
	//查詢全部
	public static void getAllEmp(){
		List<EmpVO> list = dao.getAll();
		for(EmpVO aempVO : list){
			System.out.print(aempVO.getEmpid()+",");
			System.out.print(aempVO.getEname()+",");
			System.out.print(aempVO.getPassword()+",");
			System.out.print(aempVO.getHiredate()+",");
			System.out.print(aempVO.getPic()+",");
			System.out.println(aempVO.getStatus());
		}
		
	}

	
}
