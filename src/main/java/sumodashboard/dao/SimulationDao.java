package sumodashboard.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sumodashboard.model.MetaData;
import sumodashboard.model.Simulation;

import org.json.JSONObject;
import org.json.XML;

import org.postgresql.util.PGobject;

//Class used for all communication with the database
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
		startDBConnection();
		sqlQueries = new SQLQueries(connection);
	}
	
	//Start the connection to the database
	private void startDBConnection() {
//		new dblogin();
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
	public List<MetaData> getSimulations() throws SQLException {
		ResultSet rs = sqlQueries.getAllSimulationsQuery.executeQuery();
		
		List<MetaData> simulations = new ArrayList<>();
		while (rs.next()) {
			int ID = rs.getInt("simid");
			String name = rs.getString("name");
			String date = rs.getDate("date").toString();
			String description = rs.getString("description");
			String researcher = rs.getString("researcher");
			String tags = rs.getString("tags");
			MetaData entry = new MetaData(ID, name, date, description, researcher, tags);
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
		String tags = rs.getString("tags");
		String net = rs.getString("net");
		String routes = rs.getString("routes");
		String config = rs.getString("config");
		Simulation result = new Simulation(ID, name, date, description, researcher, tags, net, routes, config);
		
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
	//TODO: improve this code
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
		
		if (simulation.getTags() != null) {			
			//Remove existing tags
			sqlQueries.removeAllSimulationTagsQuery.setInt(1, simulation_id);
			sqlQueries.removeAllSimulationTagsQuery.execute();
			
			//Add tags specified
			MetaDataIO.addTagsToSimulation(simulation_id, simulation.getTags());
		}
	}
	
	//Get a list of datapoints for the edge appearence frequency, over time. For a specified simulation and edge id.
		public Map<Double, Double> getEdgeAppearenceFrequency(int simulation_id, String edge_id) throws SQLException, IDNotFound {
			if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
			sqlQueries.edgeAppearanceFrequencyQuery.setInt(2, simulation_id);
			sqlQueries.edgeAppearanceFrequencyQuery.setString(1, edge_id);
			ResultSet resultSet = sqlQueries.edgeAppearanceFrequencyQuery.executeQuery();
			Map<Double, Double> graphPoints = new HashMap<Double, Double>();
			while (resultSet.next()) {
				double timeStamp = resultSet.getDouble("timestamp");
				double number = resultSet.getDouble("edgeFrequency");
				graphPoints.put(timeStamp, number);
			}
			return graphPoints;
		}
	
	//Get a list of all edges for a specified simulation.
	public List<String> getEdgeList(int simulation_id) throws IDNotFound, SQLException {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		sqlQueries.edgeListQuery.setInt(1, simulation_id);
		ResultSet resultSet = sqlQueries.edgeListQuery.executeQuery();
		List<String> edges = new ArrayList<>();
		while (resultSet.next()) {
			String edge = resultSet.getString("edge");
			edges.add(edge);
		}
		return edges;
	}
	
	//Get a list of datapoints for the lane transiting vehicles, over time. For a specified simulation and lane id.
	public Map<Double, Double> getLaneTransitingVehicles(int simulation_id, String lane_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		sqlQueries.numberOfLaneTransitingVehiclesQuery.setInt(1, simulation_id);
		sqlQueries.numberOfLaneTransitingVehiclesQuery.setString(2, lane_id);
		ResultSet resultSet = sqlQueries.numberOfLaneTransitingVehiclesQuery.executeQuery();
		Map<Double, Double> graphPoints = new HashMap<Double, Double>();
		while (resultSet.next()) {
			double timeStamp = resultSet.getDouble("timestamp");
			double number = resultSet.getDouble("vehicleCount");
			graphPoints.put(timeStamp, number);
		}
		return graphPoints;
	}
	
	//Get a list of all lanes for a specified simulation.
	public List<String> getLaneList(int simulation_id) throws IDNotFound, SQLException {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		sqlQueries.laneListQuery.setInt(1, simulation_id);
		ResultSet resultSet = sqlQueries.laneListQuery.executeQuery();
		List<String> lanes = new ArrayList<>();
		while (resultSet.next()) {
			String lane = resultSet.getString("lane_id");
			lanes.add(lane);
		}
		return lanes;
	}
	
	//Get a list of all vehicles for a specified simulation.
	public List<String> getVehicleList(int simulation_id) throws IDNotFound, SQLException {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		sqlQueries.vehicleListQuery.setInt(1, simulation_id);
		ResultSet resultSet = sqlQueries.vehicleListQuery.executeQuery();
		List<String> vehicles = new ArrayList<>();
		while (resultSet.next()) {
			String vehicle = resultSet.getString("vehicleid");
			vehicles.add(vehicle);
		}
		return vehicles;
	}
	
	//Get a list of datapoints for the route length of a single vehicle, over time. For a specified simulation and vehicle id.
	public Map<Double, Double> getVehicleRouteLength(int simulation_id, String vehicle_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		sqlQueries.vehicleRouteLengthQuery.setInt(1, simulation_id);
		sqlQueries.vehicleRouteLengthQuery.setString(2, vehicle_id);
		ResultSet resultSet = sqlQueries.vehicleRouteLengthQuery.executeQuery();
		Map<Double, Double> graphPoints = new HashMap<Double, Double>();
		while (resultSet.next()) {
			double timeStamp = resultSet.getDouble("timestamp");
			double length = resultSet.getDouble("routeLength");
			graphPoints.put(timeStamp, length);
		}
		return graphPoints;
	}
	
	//Get a list of datapoints for the speed of a single vehicle, over time. For a specified simulation and vehicle id.
	public Map<Double, Double> getVehicleSpeed(int simulation_id, String vehicle_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		sqlQueries.vehicleSpeedQuery.setInt(1, simulation_id);
		sqlQueries.vehicleSpeedQuery.setString(2, vehicle_id);
		ResultSet resultSet = sqlQueries.vehicleSpeedQuery.executeQuery();
		HashMap<Double, Double> graphPoints = new HashMap<Double, Double>();
		while (resultSet.next()) {
			double timeStamp = resultSet.getDouble("timestamp");
			double speed = resultSet.getDouble("vehicleSpeed");
			graphPoints.put(timeStamp, speed);
		}
		return graphPoints;
	}
	
	//Get a list of datapoints for the speedfactor of a single vehicle, over time. For a specified simulation and vehicle id.
	public Map<Double, Double> getVehicleSpeedFactor(int simulation_id, String vehicle_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		sqlQueries.vehicleSpeedFactorQuery.setInt(1, simulation_id);
		sqlQueries.vehicleSpeedFactorQuery.setString(2, vehicle_id);
		ResultSet resultSet = sqlQueries.vehicleSpeedFactorQuery.executeQuery();
		Map<Double, Double> graphPoints = new HashMap<Double, Double>();
		while (resultSet.next()) {
			double timeStamp = resultSet.getDouble("timestamp");
			double vehicleSpeedFactor = resultSet.getDouble("vehicleSpeedFactor");
			graphPoints.put(timeStamp, vehicleSpeedFactor);
		}
		return graphPoints;
	}
	
	//Get a list of datapoints for the average route length of all vehicles, over time. For a specified simulation id.
	public Map<Double, Double> getAvgRouteLength(int simulation_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		sqlQueries.avgRouteLengthQuery.setInt(1, simulation_id);
		ResultSet resultSet = sqlQueries.avgRouteLengthQuery.executeQuery();
		Map<Double, Double> graphPoints = new HashMap<Double, Double>();
		while (resultSet.next()) {
			double timestamp = resultSet.getDouble("timestamp");
			double avgRouteLength = resultSet.getDouble("avgCount");
			graphPoints.put(timestamp, avgRouteLength);
		}
		return graphPoints;
	}
	
	//Get a list of datapoints for the average speed of all vehicles, over time. For a specified simulation id.
	public Map<Double, Double> getAverageSpeed(int simulation_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		sqlQueries.avgSpeedQuery.setInt(1, simulation_id);
		ResultSet resultSet = sqlQueries.avgSpeedQuery.executeQuery();
		Map<Double, Double> graphPoints = new HashMap<Double, Double>();
		while (resultSet.next()) {
			double timestamp = resultSet.getDouble("timestamp");
			double avgSpeed = resultSet.getDouble("avgSpeed");
			graphPoints.put(timestamp, avgSpeed);
		}
		return graphPoints;
	}
	
	//Get a list of datapoints for the average speedFactor of all vehicles, over time. For a specified simulation id.
	public Map<Double, Double> getAverageSpeedFactor(int simulation_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		sqlQueries.avgSpeedFactorQuery.setInt(1, simulation_id);
		ResultSet resultSet = sqlQueries.avgSpeedFactorQuery.executeQuery();
		Map<Double, Double> graphPoints = new HashMap<Double, Double>();
		while (resultSet.next()) {
			double timestamp = resultSet.getDouble("timestamp");
			double avgSpeedFactor = resultSet.getDouble("avgSpeedFactor");
			graphPoints.put(timestamp, avgSpeedFactor);
		}
		return graphPoints;
	}
	
	//Get a list of datapoints for the cumulative number of arrived vehicles, over time. For a specified simulation id.
	public Map<Double, Double> getCumNumArrivedVehicles(int simulation_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		sqlQueries.cumulativeNumberOfArrivedVehiclesQuery.setInt(1, simulation_id);
		ResultSet resultSet = sqlQueries.cumulativeNumberOfArrivedVehiclesQuery.executeQuery();
		Map<Double, Double> graphPoints = new HashMap<Double, Double>();
		while (resultSet.next()) {
			double timestamp = resultSet.getDouble("timestamp");
			double numberArrivedVehicles = resultSet.getDouble("cumulativeNumberOfArrivedVehicles");
			graphPoints.put(timestamp, numberArrivedVehicles);
		}
		return graphPoints;
	}
	
	//Get a list of datapoints for the number of transferred vehicles, over time. For a specified simulation id.
	public Map<Double, Double> getNumTransferredVehicles(int simulation_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		sqlQueries.numberOfTransferredVehiclesQuery.setInt(1, simulation_id);
		ResultSet resultSet = sqlQueries.numberOfTransferredVehiclesQuery.executeQuery();
		Map<Double, Double> graphPoints = new HashMap<Double, Double>();
		while (resultSet.next()) {
			double timestamp = resultSet.getDouble("timestamp");
			double numberTransferredVehicles = resultSet.getDouble("numberOfTransferredVehicles");
			graphPoints.put(timestamp, numberTransferredVehicles);
		}
<<<<<<< HEAD

		public List<String> summaryStats(int simulation_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");

		sqlQueries.summaryStats.setInt(1, simulation_id);

		ResultSet resultSet = sqlQueries.summaryStats.executeQuery();

		List<String> summaryStats = new ArrayList<>();

		while (resultSet.next()) {
			double timestamp = resultSet.getDouble("timestamp");
			String summary_statistics = resultSet.getString("summary_statistics");
			String timeStamp = timestamp + "";
			String sumstat = timeStamp + " " + summary_statistics;
		}

		return summaryStats;
=======
		return graphPoints;
>>>>>>> 7c55499c7c611e260e00e4260a4f658bee810a3e
	}
	
	//Get a list of datapoints for the number of running vehicles, over time. For a specified simulation id.
	public Map<Double, Double> getNumRunningVehicles(int simulation_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		sqlQueries.numberOfRunningVehiclesQuery.setInt(1, simulation_id);
		ResultSet resultSet = sqlQueries.numberOfRunningVehiclesQuery.executeQuery();
		Map<Double, Double> graphPoints = new HashMap<Double, Double>();
		while (resultSet.next()) {
			double timestamp = resultSet.getDouble("timestamp");
			double numberRunningVehicles = resultSet.getDouble("numberOfRunningVehicles");
			graphPoints.put(timestamp, numberRunningVehicles);
		}
		return graphPoints;
	}
	
	//Get information about all edges: how often they appear in the initial routes
	public Map<String, Integer> getEdgeAppearanceFrequencyInitialRoute(int simulation_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		sqlQueries.edgeAppearanceFrequencyInitialRouteQuery.setInt(1, simulation_id);
		ResultSet resultSet = sqlQueries.edgeAppearanceFrequencyInitialRouteQuery.executeQuery();
		Map<String, Integer> dataPoints = new HashMap<>();
		while (resultSet.next()) {
			String edge = resultSet.getString("edge");
			int edgeFrequency = resultSet.getInt("edgeFrequency");
			dataPoints.put(edge, edgeFrequency);
		}
		return dataPoints;
	}
	
	//Store a simulation in the database
	public void storeSimulation(Integer simId, String name, String description, String date, File net, File routes, File config) throws SQLException, IOException {
		sqlQueries.storeSimulationQuery.setInt(1, simId);
		sqlQueries.storeSimulationQuery.setString(2, name);
		sqlQueries.storeSimulationQuery.setString(3, date);
		sqlQueries.storeSimulationQuery.setString(4, description);
		sqlQueries.storeSimulationQuery.setObject(5, convertFileToPGobject(net));
		sqlQueries.storeSimulationQuery.setObject(6, convertFileToPGobject(routes));
		sqlQueries.storeSimulationQuery.setObject(7, convertFileToPGobject(config));
		if(storeData) sqlQueries.storeSimulationQuery.executeUpdate();
	}
	
	//Store a state file in the database
	public void storeState(Integer simId, Integer timeStamp, File stateFile) throws SQLException, IOException {
		sqlQueries.storeStateQuery.setInt(1, simId);
		sqlQueries.storeStateQuery.setFloat(2, timeStamp);
		sqlQueries.storeStateQuery.setObject(3, convertFileToPGobject(stateFile));
		if(storeData) sqlQueries.storeStateQuery.executeUpdate();
		
	}
	
	//Get the id for a given tag
	public Integer getTagId(String tag) throws SQLException {
		sqlQueries.getTagIdQuery.setString(1, tag);
		ResultSet resultSet = sqlQueries.getTagIdQuery.executeQuery();
		if(resultSet.next()) {
			return resultSet.getInt(1);
		}
		return null;
	}
	
	//Store a new tag in the database by specified tag id
	public void storeTag(Integer tagId, String tag) throws SQLException {
		sqlQueries.storeTagQuery.setInt(1, tagId);
		sqlQueries.storeTagQuery.setString(2, tag);
		if(storeData) sqlQueries.storeTagQuery.executeUpdate();
		
	}
	
	//Store a new connection between a simulation and a tag in the database
	public void storeSimTag(Integer tagId, int simId) throws SQLException {
		sqlQueries.storeSimTagQuery.setInt(1, tagId);
		sqlQueries.storeSimTagQuery.setInt(2, simId);
		if(storeData) sqlQueries.storeSimTagQuery.executeUpdate();
	}
	
	//Check if a tag id exists
	public boolean doesTagIdExist(int tagId) throws SQLException {
		sqlQueries.doesTagIdExistQuery.setInt(1, tagId);
		return sqlQueries.doesTagIdExistQuery.executeQuery().next();
	}
	
	//Check if a simulation id exists
	public boolean doesSimIdExist(int simId) throws SQLException {
		sqlQueries.doesSimIdExistQuery.setInt(1, simId);
		return sqlQueries.doesSimIdExistQuery.executeQuery().next();
	}
	
	//Get all existing tags
	public List<String> getTags() throws SQLException {
		ResultSet rs = sqlQueries.getAllTagsQuery.executeQuery();	
		List<String> tags = new ArrayList<>();
		while (rs.next()) {
			String tag = rs.getString("tags");
			tags.add(tag);
		}
		return tags;
	}
	
	//Remove all tags from a simulation
	public void removeAllSimulationTags(int simId) throws SQLException {
		sqlQueries.removeAllSimulationTagsQuery.setInt(1, simId);
		sqlQueries.removeAllSimulationTagsQuery.execute();
	}
	
	//Convert an uploaded XML file to a JSON file for storing the the database
	public PGobject convertFileToPGobject(File file) throws IOException, SQLException {
		String xmlString = "";
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = null;
		while((line = in.readLine()) != null) {
		    xmlString += line;
		}
		in.close();
		
		JSONObject jsonObj = XML.toJSONObject(xmlString);
		PGobject result = new PGobject();
		result.setType("json");
		result.setValue(jsonObj.toString());
		return result;
	}
	
	//Exception that gets thrown if a specified id is not found
	public class IDNotFound extends Exception {
		private static final long serialVersionUID = 4280320385143360167L;
		
		public IDNotFound(String msg) {
			super(msg);
		}
	}
}

