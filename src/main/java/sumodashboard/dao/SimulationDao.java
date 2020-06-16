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
	private SQLQueries sqlQueries;
	
	//Constructor sets up the connection to the database
	private SimulationDao() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Class org.postgresql.Driver not found in method SimulationDao.init(), check dependencies.");
		}
		
		//startDBConnection();
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
	
	//Get a List with all simulation metadata in the database
	public List<Simulation> getSimulations() throws SQLException {
		ResultSet rs = sqlQueries.getAllSimulationsQuery.executeQuery();
		
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
		sqlQueries.getSimulationQuery.setInt(1, simulation_id);
		
		ResultSet rs = sqlQueries.getSimulationQuery.executeQuery();
		
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
		sqlQueries.removeSimulationQuery.setInt(1, simulation_id);
		sqlQueries.removeSimulationQuery.setInt(2, simulation_id);
		sqlQueries.removeSimulationQuery.setInt(3, simulation_id);
		
		if (sqlQueries.removeSimulationQuery.executeUpdate() == 0) {
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

		sqlQueries.avgSpeedQuery.setInt(1, simulation_id);
		
		ResultSet resultSet = sqlQueries.avgSpeedQuery.executeQuery();
		
		List<GraphPoint> graphPoints = new ArrayList<>();

		while (resultSet.next()) {
			double timestamp = resultSet.getDouble("timestamp");
			double avgSpeed = resultSet.getDouble("avgSpeed");
			GraphPoint point = new GraphPoint(timestamp, avgSpeed);
			graphPoints.add(point);
		}

		return graphPoints;
	}
	
	//Get a list of all vehicles for a specified simulation.
	public List<String> getVehicleList(int simulation_id) throws IDNotFound, SQLException {
		System.out.println("getting the list @simDao");
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");

		sqlQueries.vehicleListQuery.setInt(1, simulation_id);
		
		ResultSet resultSet = sqlQueries.vehicleListQuery.executeQuery();
		System.out.println("We got the query back" + resultSet.toString());
		List<String> vehicles = new ArrayList<>();
		
		while (resultSet.next()) {
			String vehicle = resultSet.getString("vehicleid");
			vehicles.add(vehicle);
		}
		
		return vehicles;
		
	}
	
	//Get a list of datapoints for the speed of a single vehicle, over time. For a specified simulation and vehicle id.
	public List<GraphPoint> getVehicleSpeed(int simulation_id, String vehicle_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");

		sqlQueries.vehicleSpeedQuery.setInt(1, simulation_id);
		sqlQueries.vehicleSpeedQuery.setString(2, vehicle_id);
		
		ResultSet resultSet = sqlQueries.vehicleSpeedQuery.executeQuery();
		
		List<GraphPoint> graphPoints = new ArrayList<>();
		
		while (resultSet.next()) {
			double timeStamp = resultSet.getDouble("timestamp");
			double avgSpeed = resultSet.getDouble("vehicleSpeed");
			GraphPoint point = new GraphPoint(timeStamp, avgSpeed);
			graphPoints.add(point);
		}
		
		return graphPoints;
		
	}
	
	//Get a list of datapoints for the average speed of all vehicles, over time. For a specified simulation id.
		public List<GraphPoint> getAvgRouteLength(int simulation_id) throws SQLException, IDNotFound {
			if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");

			sqlQueries.avgRouteLengthQuery.setInt(1, simulation_id);
			
			ResultSet resultSet = sqlQueries.avgRouteLengthQuery.executeQuery();
			
			List<GraphPoint> graphPoints = new ArrayList<>();

			while (resultSet.next()) {
				double timestamp = resultSet.getDouble("timestamp");
				double avgSpeed = resultSet.getDouble("avgCount");
				GraphPoint point = new GraphPoint(timestamp, avgSpeed);
				graphPoints.add(point);
			}

			return graphPoints;
		}
	
	public void storeSimulation(Integer simId, String name, String description, Date date, File net, File routes, File config) throws Exception {
		sqlQueries.storeSimulationQuery.setInt(1, simId);
		sqlQueries.storeSimulationQuery.setString(2, name);
		sqlQueries.storeSimulationQuery.setString(3, date.toString());
		sqlQueries.storeSimulationQuery.setString(4, description);
		sqlQueries.storeSimulationQuery.setObject(5, convertFileToPGobject(net));
		sqlQueries.storeSimulationQuery.setObject(6, convertFileToPGobject(routes));
		sqlQueries.storeSimulationQuery.setObject(7, convertFileToPGobject(config));
		if(storeData) sqlQueries.storeSimulationQuery.executeUpdate();
	}
	
	public void storeState(Integer simId, Integer timeStamp, File stateFile) throws Exception {
		sqlQueries.storeStateQuery.setInt(1, simId);
		sqlQueries.storeStateQuery.setFloat(2, timeStamp);
		sqlQueries.storeStateQuery.setObject(3, convertFileToPGobject(stateFile));
		if(storeData) sqlQueries.storeStateQuery.executeUpdate();
		
	}
	
	public Integer getTagId(String tag) throws SQLException {
		sqlQueries.getTagIdQuery.setString(1, tag);
		ResultSet resultSet = sqlQueries.getTagIdQuery.executeQuery();
		if(resultSet.next()) {
			return resultSet.getInt(1);
		}
		return null;
	}

	public void storeTag(Integer tagId, String tag) throws SQLException {
		sqlQueries.storeTagQuery.setInt(1, tagId);
		sqlQueries.storeTagQuery.setString(2, tag);
		if(storeData) sqlQueries.storeTagQuery.executeUpdate();
		
	}

	public void storeSimTag(Integer tagId, int simId) throws SQLException {
		sqlQueries.storeSimTagQuery.setInt(1, tagId);
		sqlQueries.storeSimTagQuery.setInt(2, simId);
		if(storeData) sqlQueries.storeSimTagQuery.executeUpdate();
	}
	
	public boolean doesTagIdExist(int tagId) throws SQLException {
		sqlQueries.doesTagIdExistQuery.setInt(1, tagId);
		return sqlQueries.doesTagIdExistQuery.executeQuery().next();
	}

	public boolean doesSimIdExist(int simId) throws SQLException {
		sqlQueries.doesSimIdExistQuery.setInt(1, simId);
		return sqlQueries.doesSimIdExistQuery.executeQuery().next();
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
