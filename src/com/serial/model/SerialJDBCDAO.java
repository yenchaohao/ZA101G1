package com.serial.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.desire.model.DesireVO;




public class SerialJDBCDAO implements Serial_interface{
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String userid = "g1";
	String passwd = "za101g1";
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

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(INSERT_STMT);
			String serial=null;
			
			List<SerialVO> list=getAll();
			Boolean isRepeat=null;
			do{
				//初始化
				serial=getRandomKey();
				isRepeat=false;
				//判斷有無重複
				for(SerialVO vo:list){
					if(serial.equals(vo.getSerial_number())){
						isRepeat=true;
						break;
					}
				}		
			}while(isRepeat==true);
			pstmt.setString(1, serial);
			pstmt.setInt(2, serialVO.getMoney());
			pstmt.setString(3, serialVO.getMember_id());

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

	public String getRandomKey() {
		int length=10;
		String serial="";
		String [] pwPool={
				"0","1","2","3","4","5","6","7","8","9",
		        "A","B","C","D","E","F","G","H","I","J",
		        "K","L","M","N","O","P","Q","R","S","T",
		        "U","V","W","X","Y","Z","a","b","c","d",
		        "e","f","g","h","i","j","k","l","m","n","o",
		        "p","q","r","s","t","u","v","w","x","y","z"
		};
		for(int i=0;i<length;i++){
			serial+=pwPool[(int) Math.floor(Math.random()*(pwPool.length))];		
		}
		return serial;
	}

	@Override
	public void update(SerialVO serialVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(UPDATE);

			
			pstmt.setString(3, serialVO.getSerial_number());
			pstmt.setInt(1, serialVO.getMoney());
			pstmt.setString(2, serialVO.getMember_id());
			

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
	public void delete(String serial_number) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			con.setAutoCommit(false); 
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setString(1, serial_number);
			
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
	}

	@Override
	public SerialVO findByPrimaryKey(String serial_number) {
		SerialVO serialVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			 
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

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			
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
		
		SerialJDBCDAO dao = new SerialJDBCDAO();
	
			 //新增
//				SerialVO serialVO = new SerialVO();
//				serialVO.setMoney(1000);
//				serialVO.setMember_id("M1001");			
//				int updateCount_insert = dao.insert(serialVO);
//				 System.out.println(updateCount_insert);
					
	
			 //修改
//			SerialVO serialVO = new SerialVO();
//			serialVO.setSerial_number("aULQLHhzES");
//			serialVO.setMoney(5000);
//			serialVO.setMember_id("M1006");
//		
//			
//			 int updateCount_update = dao.update(serialVO);
//			 System.out.println(updateCount_update);
				
	
			 //刪除
//			 int updateCount_delete = dao.delete("bDU3F67h2N");
//			 System.out.println(updateCount_delete);
	
			// 查詢
//			SerialVO serialVO = dao.findByPrimaryKey("aULQLHhzES");
//			System.out.print(serialVO.getSerial_number() + ",");
//			System.out.print(serialVO.getMoney() + ",");
//			System.out.print(serialVO.getMember_id() + ",");
			
	

	
			// 查詢
//			List<SerialVO> list = dao.getAll();
//			for (SerialVO serialVO : list) {
//				System.out.print(serialVO.getSerial_number() + ",");
//				System.out.print(serialVO.getMoney() + ",");
//				System.out.print(serialVO.getMember_id() + ",");
//				System.out.println();
//			}
		}
}
