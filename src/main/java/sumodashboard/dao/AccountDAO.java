package sumodashboard.dao;

import java.util.Date;

import javax.security.sasl.AuthenticationException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDao {
	private static DatabaseSetup db = DatabaseSetup.instance;
	
	/**
	 * Get the hashed password of a user
	 * @param username
	 * @return the hash of the password
	 * @throws SQLException
	 * @throws AuthenticationException if the user is not found
	 */
	public static String getHashedPassword(String username)  throws SQLException, AuthenticationException {
		ResultSet rs = db.doQuery(() -> {
			db.getSqlQueries().getHashedPass.setString(1, username);
			return db.getSqlQueries().getHashedPass.executeQuery();
		});
		
		if (!rs.next()) throw new AuthenticationException("Username not found");
		return rs.getString("password");
	}
	
	/**
	 * Create a new user
	 * @param username 
	 * @param hashedPass
	 * @param email
	 * @throws SQLException
	 */
	public static void createNewUser(String username, String hashedPass, String email) throws SQLException {
		db.doUpdate(() -> {
			db.getSqlQueries().createNewUser.setString(1, username);
			db.getSqlQueries().createNewUser.setString(2, hashedPass);
			db.getSqlQueries().createNewUser.setString(3, email);
			db.getSqlQueries().createNewUser.setDate(4, new java.sql.Date(new Date().getTime()));
			return db.getSqlQueries().createNewUser.executeUpdate();
		});	
	}
	
	/**
	 * Get a username from the database if it exists
	 * @param username
	 * @return the username if it exists
	 * @throws SQLException if the username does not exist
	 */
	public static synchronized String getUserByName(String username) throws SQLException {
		ResultSet rs = db.doQuery(() -> {
			db.getSqlQueries().getUserByName.setString(1, username);
			return db.getSqlQueries().getUserByName.executeQuery();
		});
		
		rs.next();
		return rs.getString("username");
	}
}