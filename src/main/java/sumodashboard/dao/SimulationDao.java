package sumodashboard.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
				"SELECT metadata.* " +
				"FROM metadata");

		ResultSet res = simQuery.executeQuery();

		return res;
	}
	
	public ResultSet getAvgSpeedTime(String simulation_id) throws SQLException {
		PreparedStatement dataQuery = connection.prepareStatement("" + 
				"SELECT state.timestep, AVG(vehicle_state.speed) " +
				"FROM vehicle_state, state " +
				"WHERE vehicle_state.state_id = state.state_id " +
				"GROUP BY state.timestep " +
				"ORDER BY state.timestep ");
		
		ResultSet resultSet = dataQuery.executeQuery();
		return resultSet;
	}
	
	/* Query for storing in db as tables
	public void setMetaData(String id, String name, Date date, String description, ArrayList<String> tags) throws SQLException {
		PreparedStatement dataQuery = connection.prepareStatement(""
				+ "INSERT INTO metadata VALUES "
				+ "(?, ?::date, ?, ?, ?)");
		String[] tagsies = (String[]) tags.toArray();
		dataQuery.setString(1, id);
		dataQuery.setString(2, date.toString());
		dataQuery.setString(3, description);
		dataQuery.setString(4, name);
		dataQuery.setArray(5, tagsies);
		System.out.println("YEEHAW");
	} */
	
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
