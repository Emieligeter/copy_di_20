package sumodashboard.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sumodashboard.model.Simulation;

import javax.xml.bind.JAXBException;

public enum SimulationDao {
	instance;
	
	private Connection connection;
	
	private Map<String, Simulation> contentProvider = new HashMap<String, Simulation>();
	
	private SimulationDao() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Class org.postgresql.Driver not found in method SimulationDao.init(), check dependencies.");
		}
		
		startDBConnection();
	}
	
	private void startDBConnection() {
		final String url = "jdbc:postgresql://bronto.ewi.utwente.nl:5432/dab_di19202b_333";
		final String username = "dab_di19202b_333";
		final String password = "zyU3/uAIyZgigF+A";
		
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.err.print("SQL Exception when starting connection to database: ");
			System.err.println(e.getLocalizedMessage());
		}
	}
	
	public ResultSet getSimulations() throws SQLException {
		PreparedStatement simQuery = connection.prepareStatement("" +
				"SELECT metadata.*" + 
				"FROM metadata");
		
		ResultSet res = simQuery.executeQuery();
		
		return res;		
	}
	
	public Map<String, Simulation> getModel() {
		return contentProvider;
	}
	
	public static void main(String[] arg) {
		try {
			System.out.println(SimulationDao.instance.getSimulations());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
