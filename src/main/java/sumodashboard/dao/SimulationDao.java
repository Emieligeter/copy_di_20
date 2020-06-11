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
				"SELECT simid, name, date, description, researcher " +
				"FROM project.simulations");

		ResultSet rs = simQuery.executeQuery();
		
		List<Simulation> simulations = new ArrayList<>();
		while (rs.next()) {
			int ID = rs.getInt("simid");
			String name = rs.getString("name");
			String date = rs.getDate("date").toString();
			String description = rs.getString("description");
			String researcher = rs.getString("researcher");
			Simulation entry = new Simulation(ID, name, date, description, researcher);
			simulations.add(entry);
		}
		
		return simulations;		
	}
	
	//Get one simulation by id
	public Simulation getSimulation(int simulation_id) throws SQLException {
		PreparedStatement simQuery = connection.prepareStatement("" +
				"SELECT simid, name, date, description, researcher, net, routes, config " + 
				"FROM project.simulations " +
				"WHERE simid = ?");
		simQuery.setInt(1, simulation_id);
		
		ResultSet rs = simQuery.executeQuery();
		
		if (!rs.next()) {
			return null;
		}
		
		int ID = rs.getInt("simid");
		String name = rs.getString("name");
		String date = rs.getDate("date").toString();
		String description = rs.getString("description");
		String researcher = rs.getString("researcher");
		String net = rs.getString("net");
		String routes = rs.getString("routes");
		String config = rs.getString("config");
		Simulation result = new Simulation(ID, name, date, description, researcher, net, routes, config);
		
		return result;
	}
	
	//Remove one simulation by id
	public void removeSimulation(int simulation_id) throws SQLException, IDNotFound {
		PreparedStatement remQuery = connection.prepareStatement(""
				+ "DELETE FROM project.states "
				+ "WHERE simid = ?; "
				+ "DELETE FROM project.simulation_tags "
				+ "WHERE simid = ?; "
				+ "DELETE FROM project.simulations "
				+ "WHERE simid = ?; ");
		remQuery.setInt(1, simulation_id);
		remQuery.setInt(2, simulation_id);
		remQuery.setInt(3, simulation_id);
		
		if (remQuery.executeUpdate() == 0) {
			throw new IDNotFound("Could not find simulation id: " + simulation_id);
		}
	}
	
	//Updates metadata of specified simulation, with the specified fields of the simulation object
	public void updateMetadata(int simulation_id, Simulation simulation) throws SQLException, IDNotFound {
		StringBuilder query = new StringBuilder("UPDATE project.simulations SET ");
		if (simulation.getName() != null) query.append("name = '" + simulation.getName() + "', ");
		if (simulation.getDate() != null) query.append("date = '" + simulation.getDate() + "', ");
		if (simulation.getDescription() != null) query.append("description = '" + simulation.getDescription() + "', ");
		if (simulation.getResearcher() != null) query.append("researcher = '" + simulation.getResearcher() + "', ");
		query.delete(query.length()-2, query.length());
		query.append(" WHERE simid = ?");
		
		PreparedStatement update = connection.prepareStatement(query.toString());
		update.setInt(1, simulation_id);
		
		if (update.executeUpdate() == 0) {
			throw new IDNotFound("Could not find simulation id: " + simulation_id);
		}
	}
	
	//Get a list of datapoints for the average speed of all vehicles, over time. For a specified simulation id.
	public List<GraphPoint> getAverageSpeed(int simulation_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		
		PreparedStatement dataQuery = connection.prepareStatement("" + 
				"SELECT simid, timestamp, avgSpeed " + 
				"FROM " + 
				"	( " + 
				"		SELECT simid, timestamp, (state -> 'snapshot' -> 'vehicle' ->> 'speed')::float as avgSpeed " + 
				"		FROM project.states	 " + 
				"		WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'object' " + 
				"	UNION " + 
				"		SELECT simid, timestamp, avg(vehicleSpeed) AS avgSpeed " + 
				"		FROM " + 
				"			( " + 
				"			SELECT simid, timestamp, (json_array_elements(state -> 'snapshot' -> 'vehicle') ->> 'speed')::float as vehicleSpeed " + 
				"			FROM project.states " + 
				"			WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'array' " + 
				"			GROUP BY simid, timestamp " + 
				"                        ) q1 " + 
				"                GROUP BY simid, timestamp " + 
				"	) avgSpeeds " + 
				"WHERE simid = ? " +
				"ORDER BY simid, timestamp ASC");
		dataQuery.setInt(1, simulation_id);
		
		ResultSet resultSet = dataQuery.executeQuery();
		
		List<GraphPoint> graphPoints = new ArrayList<>();

		while (resultSet.next()) {
			double timestamp = resultSet.getDouble("timestamp");
			double avgSpeed = resultSet.getDouble("avgSpeed");
			GraphPoint point = new GraphPoint(timestamp, avgSpeed);
			graphPoints.add(point);
		}

		return graphPoints;
	}
	
	//Get a list of datapoints for the speed of a single vehicle, over time. For a specified simulation and vehicle id.
	public List<GraphPoint> getVehicleSpeed(int simulation_id, String vehicle_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		
		PreparedStatement dataQuery = connection.prepareStatement("" +
				"SELECT simid, timestamp, vehicleSpeed, vehicle_id " + 
				"FROM " + 
				"	( " + 
				"		SELECT simid, timestamp, (state -> 'snapshot' -> 'vehicle' ->> 'speed')::float as vehicleSpeed,  " + 
				"		(state -> 'snapshot' -> 'vehicle' ->> 'id')::text as vehicle_id " + 
				"		FROM project.states " + 
				"		WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'object' " + 
				"	UNION " + 
				"		SELECT simid, timestamp, (json_array_elements(state -> 'snapshot' -> 'vehicle') ->> 'speed')::float as vehicleSpeed, (json_array_elements(state -> 'snapshot' -> 'vehicle') ->> 'id')::text as vehicle_id " + 
				"		FROM project.states " + 
				"		WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'array' " + 
				"		GROUP BY simid, vehicle_id, timestamp, vehicleSpeed " + 
				"	) q1 " + 
				"WHERE simid = ? AND vehicle_id = ? " +
				"ORDER BY simid, vehicle_id, timestamp ASC ");
		dataQuery.setInt(1, simulation_id);
		dataQuery.setString(2, vehicle_id);
		
		System.out.println(vehicle_id);
		
		ResultSet resultSet = dataQuery.executeQuery();
		
		List<GraphPoint> graphPoints = new ArrayList<>();
		
		while (resultSet.next()) {
			double timeStamp = resultSet.getDouble("timestamp");
			double avgSpeed = resultSet.getDouble("vehicleSpeed");
			GraphPoint point = new GraphPoint(timeStamp, avgSpeed);
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

	
	public class IDNotFound extends Exception {
		private static final long serialVersionUID = 4280320385143360167L;
		
		public IDNotFound(String msg) {
			super(msg);
		}
	}
}
