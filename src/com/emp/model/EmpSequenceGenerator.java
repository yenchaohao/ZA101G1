package com.emp.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class EmpSequenceGenerator implements IdentifierGenerator{
	
	private static DataSource ds = null;

	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/G1Local");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {

		String prefix = "E";
		String empid = null;
		Connection con;
		try {
			con = ds.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT ex_emp_seq.NEXTVAL as nextval FROM DUAL");
			while(rs.next()){
				int nextval = rs.getInt("nextval");
				empid = prefix + nextval;
			}
			con.close();
		} catch (SQLException e) {
			throw new HibernateException("Unable to generate Sequence");
		}
		return empid;
	}

}
