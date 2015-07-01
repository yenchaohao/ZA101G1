package com.member.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.tool.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.tool.HibernateUtil;

public class MemberSequenceGenerator implements IdentifierGenerator {

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
		String member_id = "";
		Connection connection;
		try {
			connection = ds.getConnection();
			PreparedStatement pstmt = connection
					.prepareStatement("SELECT ex_member_seq.nextval as next from dual");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("next");
				//System.out.println("sequence: " + id);
				member_id = "M" + id;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return member_id;
	}

}
