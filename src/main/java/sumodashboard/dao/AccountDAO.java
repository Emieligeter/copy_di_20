package sumodashboard.dao;

import java.sql.Connection;
import java.util.Date;

import sumodashboard.model.Account;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class  AccountDAO {
	private Connection connection;
	private SQLQueries sqlQueries;
	
	public AccountDAO() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Class org.postgresql.Driver not found in method SimulationDao.init(), check dependencies.");
		}
		
		startDBConnection();
		sqlQueries = new SQLQueries(connection);
	}
	
	//Start the connection to the database
	private void startDBConnection() {
		final String url = "jdbc:postgresql://bronto.ewi.utwente.nl:5432/dab_di19202b_333";
		final String username = "dab_di19202b_333";
		final String password = "zyU3/uAIyZgigF+A";
		
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.err.println("SQL Exception when starting connection to database:");
			System.err.println(e.getLocalizedMessage());
		}
	}
	
	public String getHashedPassword(String username)  throws SQLException{
		sqlQueries.getHashedPass.setString(1, username);
		ResultSet rs = sqlQueries.getHashedPass.executeQuery();
		rs.next();
		return rs.getString("password");
	}

	public void createNewUser(String username, String hashedPass, String email) throws SQLException{
		sqlQueries.createNewUser.setString(1, username);
		sqlQueries.createNewUser.setString(2, hashedPass);
		sqlQueries.createNewUser.setString(3, email);
		sqlQueries.createNewUser.setDate(4, new java.sql.Date(new Date().getTime()));
		sqlQueries.createNewUser.executeUpdate();
	}
	public String getUserByName(String username) throws SQLException {
		sqlQueries.getUserByName.setString(1, username);
		ResultSet rs = sqlQueries.getUserByName.executeQuery();
		rs.next();
		return rs.getString("username");
	}
}