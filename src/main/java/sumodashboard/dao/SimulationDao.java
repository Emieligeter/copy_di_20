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
import java.util.Random;
import java.util.UUID;

import sumodashboard.model.GraphPoint;
import sumodashboard.model.Simulation;

import javax.xml.bind.JAXBException;

import org.json.JSONObject;
import org.json.XML;

import org.postgresql.util.PGobject;


public enum SimulationDao {
	instance;
	private boolean storeData = true;
	
	private Connection connection;
	
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
				"FROM project.simulations");

		ResultSet rs = simQuery.executeQuery();
		
		List<Simulation> simulations = new ArrayList<>();
		while (rs.next()) {
			int ID = rs.getInt("simid");
			String name = rs.getString("name");
			Date date = rs.getDate("date");
			String description = rs.getString("description");
			Simulation entry = new Simulation(ID, name, date, description);
			simulations.add(entry);
		}
		
		return simulations;		
	}
	
	//Get one simulation by id
	public Simulation getSimulation(int simulation_id) throws SQLException {
		PreparedStatement simQuery = connection.prepareStatement("" +
				"SELECT simid, name, date, description, net, routes, config " + 
				"FROM project.simulations " +
				"WHERE simid = ?");
		simQuery.setInt(1, simulation_id);
		
		ResultSet rs = simQuery.executeQuery();
		
		rs.next();
		int ID = rs.getInt("simid");
		String name = rs.getString("name");
		Date date = rs.getDate("date");
		String description = rs.getString("description");
		String net = rs.getString("net");
		String routes = rs.getString("routes");
		String config = rs.getString("config");
		Simulation result = new Simulation(ID, name, date, description, net, routes, config);
		
		return result;
	}
	
	//
	public List<GraphPoint> getAvgSpeedTime(int simulation_id) throws SQLException {
		//TODO fix query
		PreparedStatement dataQuery = connection.prepareStatement("" + 
				"SELECT timestamp, unnest(xpath('/snapshot/vehicle/@speed', state))::text::float " +
				"FROM project.states " +
				"WHERE simID = ? " +
				"ORDER BY timestamp");
		dataQuery.setInt(1, simulation_id);
		
		ResultSet resultSet = dataQuery.executeQuery();
		
		List<GraphPoint> graphPoints = new ArrayList<>();

		while (resultSet.next()) {
			double timestamp = resultSet.getDouble("timestamp");
			double avgSpeed = resultSet.getDouble("unnest");
			GraphPoint point = new GraphPoint(timestamp, avgSpeed);
			graphPoints.add(point);
		}

		return graphPoints;
	}
	
	public void storeSimulation(Integer simId, String name, String description, Date date, File net, File routes, File config) throws Exception {
		PreparedStatement dataQuery = connection.prepareStatement(
				"INSERT INTO project.simulations (simID, name, date, description, net, routes, config)" +
				"VALUES(?, ?, ?::date ,? ,? ,? ,?)");
		dataQuery.setInt(1, simId);
		dataQuery.setString(2, name);	
		dataQuery.setString(3, date.toString());
		dataQuery.setString(4, description);
		dataQuery.setObject(5, convertFileToPGobject(net));
		dataQuery.setObject(6, convertFileToPGobject(routes));
		dataQuery.setObject(7, convertFileToPGobject(config));		
		if(storeData) dataQuery.executeUpdate();
	}
	
	public void storeState(Integer simId, Integer timeStamp, File stateFile) throws Exception {
		PreparedStatement dataQuery = connection.prepareStatement(
				"INSERT INTO project.states (simID, timestamp, state)" +
				"VALUES(?, ?, ?)");
		dataQuery.setInt(1, simId);
		dataQuery.setFloat(2, timeStamp);
		dataQuery.setObject(3, convertFileToPGobject(stateFile));
		if(storeData) dataQuery.executeUpdate();
		
	}
	
	public Integer getTagId(String tag) throws SQLException {
		PreparedStatement dataQuery = connection.prepareStatement("" + 
				"SELECT tagid " +
				"FROM project.tags " +
				"WHERE value = ?");
		dataQuery.setString(1, tag);
		ResultSet resultSet = dataQuery.executeQuery();
		if(resultSet.next()) {
			return resultSet.getInt(1);
		}
		return null;
	}

	public void storeTag(Integer tagId, String tag) throws SQLException {
		PreparedStatement dataQuery = connection.prepareStatement(
				"INSERT INTO project.tags (tagId, value)" +
				"VALUES(?, ?)");
		dataQuery.setInt(1, tagId);
		dataQuery.setString(2, tag);
		if(storeData) dataQuery.executeUpdate();
		
	}

	public void storeSimTag(Integer tagId, int simId) throws SQLException {
		PreparedStatement dataQuery = connection.prepareStatement(
				"INSERT INTO project.simulation_tags (tagId, simId)" +
				"VALUES(?, ?)");
		dataQuery.setInt(1, tagId);
		dataQuery.setInt(2, simId);
		if(storeData) dataQuery.executeUpdate();
	}
	
	public boolean doesTagIdExist(int tagId) throws SQLException {
		PreparedStatement dataQuery = connection.prepareStatement("" + 
				"SELECT * " +
				"FROM project.tags t " +
				"WHERE tagid = ?");
		dataQuery.setInt(1, tagId);
		return dataQuery.executeQuery().next();
	}

	public boolean doesSimIdExist(int simId) throws SQLException {
		PreparedStatement dataQuery = connection.prepareStatement("" + 
				"SELECT * " +
				"FROM project.simulations " +
				"WHERE simid = ?");
		dataQuery.setInt(1, simId);
		return dataQuery.executeQuery().next();
	}
	
	
	//Generates a random ID of size 'length', never starting with a 0 
	public int generateId(int length) {
		Random random = new Random();
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString().substring(0, length-1);
		str = String.valueOf(random.nextInt(9) +1) + str.replaceAll("[^0-9.]", String.valueOf((random).nextInt(9) +1));
		System.out.println("generated id = " + Integer.parseInt(str));
		return Integer.parseInt(str);
	}
	
	
	private PGobject convertFileToPGobject(File file) throws Exception {
		/*SQLXML sqlxml = connection.createSQLXML();
		Writer out= sqlxml.setCharacterStream();
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = null;
		while((line = in.readLine()) != null) {
		    out.write(line);
		}
		return sqlxml;*/
		
		String xmlString = "";
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = null;
		while((line = in.readLine()) != null) {
		    xmlString += line;
		}
		//System.out.println(xmlString);
		JSONObject jsonObj = XML.toJSONObject(xmlString);
		//System.out.println(jsonObj.toString());
		PGobject result = new PGobject();
		result.setType("json");
		result.setValue(jsonObj.toString());
		return result;
	}

	

}
