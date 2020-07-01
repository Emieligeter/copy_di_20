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
	
	/**
	 * Interface used for makeTransaction
	 * Will contain the code that has to be executed within the transaction
	 *
	 */
	public interface Transaction {
		Object execute() throws SQLException;
	}
	
	/**
	 * Make a transaction
	 * @param transaction an implementation of Transaction with the code that has to be ran
	 * @return a ResultSet containing the results of the query, or an Integer: how many
	 * 	rows were changed in an update
	 * @throws SQLException
	 */
	private Object makeTransaction(Transaction transaction) throws SQLException {
		try {
			try {
				Object result = transaction.execute();
				connection.commit();
				return result;
			} catch (SQLException e) {
				connection.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw e;
		}
	}
	
	/**
	 * Make a query in a transaction
	 * @param transaction an implementation of Transaction with the code that has to be ran
	 * @return a ResultSet containing the results of the query
	 * @throws SQLException
	 */
	public ResultSet doQuery(Transaction transaction) throws SQLException {
		return (ResultSet) makeTransaction(transaction);
	}
	
	/**
	 * Make an update in a transaction
	 * @param transaction an implementation of Transaction with the code that has to be ran
	 * @return an Integer: the number of rows that were changed in the update
	 * @throws SQLException
	 */
	public int doUpdate(Transaction transaction) throws SQLException {
		return (Integer) makeTransaction(transaction);
	}
	
	public SQLQueries getSqlQueries() {
		return sqlQueries;
	}
}
