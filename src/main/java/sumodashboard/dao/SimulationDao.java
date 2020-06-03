package sumodashboard.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Writer;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sumodashboard.model.GraphPoint;
import sumodashboard.model.Simulation;

import javax.xml.bind.JAXBException;

public enum SimulationDao {
	instance;
	private boolean storeData = true;
	
	private Connection connection;
	
	private Map<String, Simulation> contentProvider = new HashMap<String, Simulation>();
	
	//Constructor sets up the connection to the database
	private SimulationDao() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Class org.postgresql.Driver not found in method SimulationDao.init(), check dependencies.");
		}
		
		startDBConnection();
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
	
	//Get a List with all simulation metadata in the database
	public List<Simulation> getSimulations() throws SQLException {
		PreparedStatement simQuery = connection.prepareStatement("" +
				"SELECT simid, name, date, description " +
				"FROM simulations");

		ResultSet rs = simQuery.executeQuery();
		
		List<Simulation> simulations = new ArrayList<>();
		while (rs.next()) {
			int ID = (Integer)rs.getObject("simid");
			String name = (String)rs.getObject("name");
			Date date = (Date)rs.getObject("date");
			String description = (String)rs.getObject("description");
			Simulation entry = new Simulation(ID, name, date, description, null, null, null);
			simulations.add(entry);
		}
		
		return simulations;		
	}
	
	//
	public List<GraphPoint> getAvgSpeedTime(int simulation_id) throws SQLException {
		//TODO fix query
		PreparedStatement dataQuery = connection.prepareStatement("" + 
				"SELECT timestamp, unnest(xpath('/snapshot/vehicle/@speed', state))::text::float " +
				"FROM states " +
				"WHERE simID = ? " +
				"ORDER BY timestamp");
		dataQuery.setInt(1, simulation_id);
		
		ResultSet resultSet = dataQuery.executeQuery();
		
		List<GraphPoint> graphPoints = new ArrayList<>();

		while (resultSet.next()) {
			double timestamp = (double)resultSet.getObject("timestamp");
			double avgSpeed = (double)resultSet.getObject("unnest");
			GraphPoint point = new GraphPoint(timestamp, avgSpeed);
			graphPoints.add(point);
		}

		return graphPoints;
	}
	
	public void storeSimulation(Integer simId, String name, String description, Date date, File net, File routes, File config) throws Exception {
		PreparedStatement dataQuery = connection.prepareStatement(
				"INSERT INTO simulations (simID, name, date, description, net, routes, config)" +
				"VALUES(?, ?, ?::date ,? ,? ,? ,?)");
		dataQuery.setInt(1, simId);
		dataQuery.setString(2, name);	
		dataQuery.setString(3, date.toString());
		dataQuery.setString(4, description);
		dataQuery.setSQLXML(5, convertFile(net));
		dataQuery.setSQLXML(6, convertFile(routes));
		dataQuery.setSQLXML(7, convertFile(config));		
		if(storeData) dataQuery.executeUpdate();
	}
	
	public void storeState(Integer simId, Integer timeStamp, File stateFile) throws Exception {
		PreparedStatement dataQuery = connection.prepareStatement(
				"INSERT INTO states (simID, timestamp, state)" +
				"VALUES(?, ?, ?)");
		dataQuery.setInt(1, simId);
		dataQuery.setFloat(2, timeStamp);
		dataQuery.setSQLXML(3, convertFile(stateFile));
		if(storeData) dataQuery.executeUpdate();
		
	}
	
	public Integer getTagId(String tag) throws SQLException {
		PreparedStatement dataQuery = connection.prepareStatement("" + 
				"SELECT tagid " +
				"FROM tags " +
				"WHERE value = ?");
		dataQuery.setString(1, tag);
		ResultSet resultSet = dataQuery.executeQuery();
		resultSet.next();
		resultSet.getInt(1);
		return null;
	}

	public void storeTag(Integer tagId, String tag) throws SQLException {
		PreparedStatement dataQuery = connection.prepareStatement(
				"INSERT INTO tags (tagId, value)" +
				"VALUES(?, ?)");
		dataQuery.setInt(1, tagId);
		dataQuery.setString(2, tag);
		if(storeData) dataQuery.executeUpdate();
		
	}

	public void storeSimTag(Integer tagId, int simId) throws SQLException {
		PreparedStatement dataQuery = connection.prepareStatement(
				"INSERT INTO simulation_tags (tagId, simId)" +
				"VALUES(?, ?)");
		dataQuery.setInt(1, tagId);
		dataQuery.setInt(2, simId);
		if(storeData) dataQuery.executeUpdate();
	}
	public boolean doesTagIdExist(int tagId) throws SQLException {
		PreparedStatement dataQuery = connection.prepareStatement("" + 
				"SELECT * " +
				"FROM tags t " +
				"WHERE tagid = ?");
		dataQuery.setInt(1, tagId);
		return dataQuery.executeQuery().next();
	}

	public boolean doesSimIdExist(int simId) throws SQLException {
		PreparedStatement dataQuery = connection.prepareStatement("" + 
				"SELECT * " +
				"FROM simulations " +
				"WHERE simid = ?");
		dataQuery.setInt(1, simId);
		return dataQuery.executeQuery().next();
	}
	
	
	
	
	
	
	public Map<String, Simulation> getModel() {
		return contentProvider;
	}
	
	private SQLXML convertFile(File file) throws Exception {
		SQLXML sqlxml = connection.createSQLXML();
		Writer out= sqlxml.setCharacterStream();
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = null;
		while((line = in.readLine()) != null) {
		    out.write(line);
		}
		return sqlxml;
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
