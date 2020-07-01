package sumodashboard.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public enum DatabaseSetup {
	instance;
	
	private Connection connection;
	private SQLQueries sqlQueries;
	
	private DatabaseSetup() {
		try {
			Class.forName("org.postgresql.Driver");
			startDBConnection();
		} catch (Exception e) {
			System.err.println("Class org.postgresql.Driver not found in method SimulationDao.init(), check dependencies.");
		}
		
		sqlQueries = new SQLQueries(connection);
	}
	
	/**
	 * Start the connection to the database
	 */
	private void startDBConnection() {
		final String url = "jdbc:postgresql://bronto.ewi.utwente.nl:5432/dab_di19202b_333";
		final String username = "dab_di19202b_333";
		final String password = "zyU3/uAIyZgigF+A";

		try {
			connection = DriverManager.getConnection(url, username, password);
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			System.err.println("SQL Exception when starting connection to database:");
			System.err.println(e.getLocalizedMessage());
		}
	}
	
	public interface Transaction {
		Object execute() throws SQLException;
	}
	
	private Object makeTransaction(Transaction transaction) throws SQLException {
		try {
			try {
				connection.setAutoCommit(false);
				Object result = transaction.execute();
				connection.commit();
				connection.setAutoCommit(true);
				return result;
			} catch (SQLException e) {
				connection.rollback();
				connection.setAutoCommit(true);
				throw e;
			}
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public ResultSet doQuery(Transaction transaction) throws SQLException {
		return (ResultSet) makeTransaction(transaction);
	}
	
	public int doUpdate(Transaction transaction) throws SQLException {
		return (Integer) makeTransaction(transaction);
	}
	
	public SQLQueries getSqlQueries() {
		return sqlQueries;
	}
}
