package sumodashboard.dao;

import java.sql.Connection;
import java.util.Date;

import javax.security.sasl.AuthenticationException;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {
	private static DatabaseSetup db = DatabaseSetup.instance;
	
	public static String getHashedPassword(String username)  throws SQLException, AuthenticationException {
		ResultSet rs = db.doQuery(() -> {
			db.getSqlQueries().getHashedPass.setString(1, username);
			return db.getSqlQueries().getHashedPass.executeQuery();
		});
		
		if (!rs.next()) throw new AuthenticationException("Username not found");
		return rs.getString("password");
	}

	public static void createNewUser(String username, String hashedPass, String email) throws SQLException {
		db.doUpdate(() -> {
			db.getSqlQueries().createNewUser.setString(1, username);
			db.getSqlQueries().createNewUser.setString(2, hashedPass);
			db.getSqlQueries().createNewUser.setString(3, email);
			db.getSqlQueries().createNewUser.setDate(4, new java.sql.Date(new Date().getTime()));
			return db.getSqlQueries().createNewUser.executeUpdate();
		});	
	}
	
	public static synchronized String getUserByName(String username) throws SQLException {
		ResultSet rs = db.doQuery(() -> {
			db.getSqlQueries().getUserByName.setString(1, username);
			return db.getSqlQueries().getUserByName.executeQuery();
		});
		
		rs.next();
		return rs.getString("username");
	}
}