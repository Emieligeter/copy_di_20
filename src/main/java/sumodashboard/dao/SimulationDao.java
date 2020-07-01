package sumodashboard.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

/**
 * Class used for all communication with the database
 */
public class SimulationDao {
	private static DatabaseSetup db = DatabaseSetup.instance;
	
	/**
	 * Get a List with all simulation metadata in the database
	 * @return List<MetaData>
	 * @throws SQLException database is not reachable
	 */
	public static List<MetaData> getSimulations() throws SQLException {
		ResultSet rs = db.doQuery(() -> {
			return db.getSqlQueries().getAllSimulationsQuery.executeQuery();
		});
		
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
	
	/**
	 * Get one simulation by id
	 * @param simulation_id simulation id (int)
	 * @return simulation for given id
	 * @throws SQLException database is not reachable
	 */
	public static Simulation getSimulation(int simulation_id) throws SQLException {
		ResultSet rs = db.doQuery(() -> {
			db.getSqlQueries().getSimulationQuery.setInt(1, simulation_id);
			return db.getSqlQueries().getSimulationQuery.executeQuery();
		});
		
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
	
	/**
	 * Remove one simulation by id
	 * @param simulation_id simulation id (int)
	 * @throws SQLException database is not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static void removeSimulation(int simulation_id) throws SQLException, IDNotFound {
		int result = db.doUpdate(() -> {
			db.getSqlQueries().removeSimulationQuery.setInt(1, simulation_id);
			db.getSqlQueries().removeSimulationQuery.setInt(2, simulation_id);
			db.getSqlQueries().removeSimulationQuery.setInt(3, simulation_id);
			return db.getSqlQueries().removeSimulationQuery.executeUpdate();
		});

		if (result == 0) {
			throw new IDNotFound("Could not find simulation id: " + simulation_id);
		}		
	}
	
	/**
	 * Updates metadata of specified simulation, with the specified fields of the simulation object
	 * @param simulation_id simulation id (int)
	 * @param simulation simulation
	 * @throws SQLException database is not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static void updateMetadata(int simulation_id, Simulation simulation) throws SQLException, IDNotFound {
		int result = db.doUpdate(() -> {
			db.getSqlQueries().updateSimulationQuery.setString(1, simulation.getName());
			db.getSqlQueries().updateSimulationQuery.setString(2, simulation.getDate());
			db.getSqlQueries().updateSimulationQuery.setString(3, simulation.getDescription());
			db.getSqlQueries().updateSimulationQuery.setString(4, simulation.getResearcher());
			db.getSqlQueries().updateSimulationQuery.setString(5, simulation.getNet());
			db.getSqlQueries().updateSimulationQuery.setString(6, simulation.getRoutes());
			db.getSqlQueries().updateSimulationQuery.setString(7, simulation.getConfig());
			db.getSqlQueries().updateSimulationQuery.setInt(8, simulation_id);
			return db.getSqlQueries().updateSimulationQuery.executeUpdate();
		});
		
		if (result == 0) {
			throw new IDNotFound("Could not find simulation id: " + simulation_id);
		}
		
		if (simulation.getTags() != null) {			
			//Remove existing tags
			db.doUpdate(() -> {
				db.getSqlQueries().removeAllSimulationTagsQuery.setInt(1, simulation_id);
				return db.getSqlQueries().removeAllSimulationTagsQuery.executeUpdate();
			});
			
			//Add tags specified
			MetaDataIO.addTagsToSimulation(simulation_id, simulation.getTags());
		}
	}
	
	/**
	 * Get a list of datapoints for a statistic, over time. For a specified simulation id.
	 * @param simulation_id simulation id (int)
	 * @param statistic the name of the values column as returned by the sql query
	 * @param query the query that will be used for the request
	 * @return Map<Double timestamp, Double value>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	private static Map<Double, Double> getStats(int simulation_id, String statistic, PreparedStatement query) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		
		ResultSet rs = db.doQuery(() -> {
			query.setInt(1, simulation_id);
			return query.executeQuery();
		});		
		
		Map<Double, Double> graphPoints = new HashMap<Double, Double>();
		while (rs.next()) {
			double timestamp = rs.getDouble("timestamp");
			double value = rs.getDouble(statistic);
			graphPoints.put(timestamp, value);
		}
		return graphPoints;
	}
	
	/**
	 * Get a list of datapoints for the average route length of all vehicles, over time. For a specified simulation id.
	 * @param simulation_id simulation id (int)
	 * @return Map<Double timestamp, Double avgRouteLength>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<Double, Double> getAvgRouteLength(int simulation_id) throws SQLException, IDNotFound {
		return getStats(simulation_id, "avgCount", db.getSqlQueries().avgRouteLengthQuery);
	}
	
	/**
	 * Get a list of datapoints for the average speed of all vehicles, over time. For a specified simulation id.
	 * @param simulation_id simulation id (int)
	 * @return Map<Double timestamp, Double avgSpeed>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<Double, Double> getAverageSpeed(int simulation_id) throws SQLException, IDNotFound {
		return getStats(simulation_id, "avgSpeed", db.getSqlQueries().avgSpeedQuery);
	}
	
	/**
	 * Get a list of datapoints for the average speedFactor of all vehicles, over time. For a specified simulation id.
	 * @param simulation_id simulation id (int)
	 * @return Map<Double timestamp, Double avgSpeedFactor>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<Double, Double> getAverageSpeedFactor(int simulation_id) throws SQLException, IDNotFound {
		return getStats(simulation_id, "avgSpeedFactor", db.getSqlQueries().avgSpeedFactorQuery);
	}
	
	/**
	 * Get a list of datapoints for the cumulative number of arrived vehicles, over time. For a specified simulation id.
	 * @param simulation_id simulation id (int)
	 * @return Map<Double timestamp, Double cumulativeNumberOfArrivedVehicles>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<Double, Double> getCumNumArrivedVehicles(int simulation_id) throws SQLException, IDNotFound {
		return getStats(simulation_id, "cumulativeNumberOfArrivedVehicles", db.getSqlQueries().cumulativeNumberOfArrivedVehiclesQuery);
	}
	
	/**
	 * Get a list of datapoints for the number of transferred vehicles, over time. For a specified simulation id.
	 * @param simulation_id simulation id (int)
	 * @return Map<Double timestamp, Double numberOfTransferredVehicles>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<Double, Double> getNumTransferredVehicles(int simulation_id) throws SQLException, IDNotFound {
		return getStats(simulation_id, "numberOfTransferredVehicles", db.getSqlQueries().numberOfTransferredVehiclesQuery);
	}
	
	/**
	 * Get a list of datapoints for the number of running vehicles, over time. For a specified simulation id.
	 * @param simulation_id simulation id (int)
	 * @return Map<Double timestamp, Double numberOfRunningVehicles>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<Double, Double> getNumRunningVehicles(int simulation_id) throws SQLException, IDNotFound {
		return getStats(simulation_id, "numberOfRunningVehicles", db.getSqlQueries().numberOfRunningVehiclesQuery);
	}
	
	/**
	 * Get a list of datapoints for the a statistic that requires a parameter, over time. For a specified simulation and parameter id.
	 * @param simulation_id simulation id
	 * @param statistic the name of the values column as returned by the sql query
	 * @param param_id the specified value of the parameter
	 * @param query the query that will be used for the request
	 * @param simIdFirst is true if the simulationId appears in the preparedStatement
	 * 	before the statistic value
	 * @return Map<Double timestamp, Double value>
	 * @throws SQLException database is not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	private static Map<Double, Double> getStatsWithParam(int simulation_id, String statistic, String param_id, PreparedStatement query, boolean simIdFirst) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		
		ResultSet rs = db.doQuery(() -> {
			if (simIdFirst) {
				query.setInt(1, simulation_id);
				query.setString(2, param_id);
			}
			else {
				query.setString(1, param_id);
				query.setInt(2, simulation_id);
			}
			
			return query.executeQuery();
		});
		
		Map<Double, Double> graphPoints = new HashMap<Double, Double>();
		while (rs.next()) {
			double timeStamp = rs.getDouble("timestamp");
			double value = rs.getDouble(statistic);
			graphPoints.put(timeStamp, value);
		}
		return graphPoints;
	}
	
	/**
	 * Get a list of datapoints for the edge appearence frequency, over time. For a specified simulation and edge id.
	 * @param simulation_id simulation id (int)
	 * @param edge_id edge id
	 * @return Map<Double timestamp, Double edgeFrequency>
	 * @throws SQLException database is not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<Double, Double> getEdgeAppearenceFrequency(int simulation_id, String edge_id) throws SQLException, IDNotFound {
		return getStatsWithParam(simulation_id, "edgeFrequency", edge_id, db.getSqlQueries().edgeAppearanceFrequencyQuery, false);
	}
	
	/**
	 * Get a list of datapoints for the lane transiting vehicles, over time. For a specified simulation and lane id.
	 * @param simulation_id simulation id (int)
	 * @param lane_id lane id
	 * @return Map<Double timestamp, Double vehicleCount>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<Double, Double> getLaneTransitingVehicles(int simulation_id, String lane_id) throws SQLException, IDNotFound {
		return getStatsWithParam(simulation_id, "vehicleCount", lane_id, db.getSqlQueries().numberOfLaneTransitingVehiclesQuery, true);
	}
	
	/**
	 * Get a list of datapoints for the route length of a single vehicle, over time. For a specified simulation and vehicle id.
	 * @param simulation_id simulation id (int)
	 * @param vehicle_id vehicle id
	 * @return Map<Double timestamp, Double routeLength>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<Double, Double> getVehicleRouteLength(int simulation_id, String vehicle_id) throws SQLException, IDNotFound {
		return getStatsWithParam(simulation_id, "routeLength", vehicle_id, db.getSqlQueries().vehicleRouteLengthQuery, true);
	}
	
	/**
	 * Get a list of datapoints for the speed of a single vehicle, over time. For a specified simulation and vehicle id.
	 * @param simulation_id simulation id (int)
	 * @param vehicle_id vehicle id
	 * @return Map<Double timestamp, Double vehicleSpeed>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<Double, Double> getVehicleSpeed(int simulation_id, String vehicle_id) throws SQLException, IDNotFound {
		return getStatsWithParam(simulation_id, "vehicleSpeed", vehicle_id, db.getSqlQueries().vehicleSpeedQuery, true);
	}
	
	/**
	 * Get a list of datapoints for the speedfactor of a single vehicle, over time. For a specified simulation and vehicle id.
	 * @param simulation_id simulation id (int)
	 * @param vehicle_id vehicle id
	 * @return Map<Double timestamp, Double vehicleSpeedFactor>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<Double, Double> getVehicleSpeedFactor(int simulation_id, String vehicle_id) throws SQLException, IDNotFound {
		return getStatsWithParam(simulation_id, "vehicleSpeedFactor", vehicle_id, db.getSqlQueries().vehicleSpeedFactorQuery, true);
	}
	
	/**
	 * Get a list of labeled datapoints for a statistic. For a specified simulation id.
	 * @param simulation_id simulation id (int)
	 * @param label the name of the labels column as returned by the sql query
	 * @param statistic the name of the values column as returned by the sql query
	 * @param query the query that will be used for the request
	 * @return Map<String label, Integer value>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	private static Map<String, Integer> getLabeledStats(int simulation_id, String label, String statistic, PreparedStatement query) throws SQLException, IDNotFound  {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		
		ResultSet rs = db.doQuery(() -> {		
			query.setInt(1, simulation_id);
			return query.executeQuery();
		});
		
		Map<String, Integer> dataPoints = new HashMap<>();
		while (rs.next()) {
			String labelValue = rs.getString(label);
			int num = rs.getInt(statistic);
			dataPoints.put(labelValue, num);
		}
		return dataPoints;
	}
	
	/**
	 * Get information about all edges: how often they appear in the initial routes
	 * @param simulation_id simulation id (int)
	 * @return Map<String edge_id, Integer edgeFrequency>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<String, Integer> getEdgeAppearanceFrequencyInitialRoute(int simulation_id) throws SQLException, IDNotFound {
		return getLabeledStats(simulation_id, "edge", "edgeFrequency", db.getSqlQueries().edgeAppearanceFrequencyInitialRouteQuery);
	}
	
	/**
	 * Get information about all vehicles: route length of initial routes
	 * @param simulation_id simulation id (int)
	 * @return Map<String vehicle_id, Integer routeLength>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<String, Integer> getInitRouteLengthVehicle(int simulation_id) throws SQLException, IDNotFound {
		return getLabeledStats(simulation_id, "vehicleId", "routeLength", db.getSqlQueries().initialRouteLengthPerVehicleQuery);
	}	
	
	/**
	 * Get information about all timestamps: number of running and arrived vehicles
	 * @param simulation_id simulation id (int)
	 * @param timestamp timestamp (double)
	 * @return Map<String vehicle_id, Integer routeLength>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<String, Integer> getRunningVsArrivedVehicles(int simulation_id, String timestamp) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		double numericTimestamp;
		try {
			numericTimestamp = Double.parseDouble(timestamp);
		} catch (NumberFormatException e) {
			throw new IDNotFound("Timestamp: " + timestamp + " is not a number!");
		}
		ResultSet rs = db.doQuery(() -> {
			db.getSqlQueries().runningVsArrivedVehiclesQuery.setInt(1, simulation_id);
			db.getSqlQueries().runningVsArrivedVehiclesQuery.setDouble(2, numericTimestamp);
			return db.getSqlQueries().runningVsArrivedVehiclesQuery.executeQuery();
		});
		Map<String, Integer> dataPoints = new HashMap<>();
		rs.next();
		int numRunning = rs.getInt("numberOfRunningVehicles");
		int numArrived = rs.getInt("cumulativeNumberOfArrivedVehicles");
		dataPoints.put("numberOfRunningVehicles", numRunning);
		dataPoints.put("cumulativeNumberOfArrivedVehicles", numArrived);
		return dataPoints; 
	}	
	
	/**
	 * Get a list of strings. For a specified simulation id.
	 * @param simulation_id simulation id (int)
	 * @param listName the name of the values column as returned by the sql query
	 * @param query the query that will be used for the request
	 * @return List<String>
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	private static List<String> getList(int simulation_id, String listName, PreparedStatement query) throws IDNotFound, SQLException {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		
		ResultSet rs = db.doQuery(() -> {		
			query.setInt(1, simulation_id);
			return query.executeQuery();
		});
		
		List<String> values = new ArrayList<>();
		while (rs.next()) {
			String value = rs.getString(listName);
			values.add(value);
		}
		return values;
	}
	
	/**
	 * Get a list of all edges for a specified simulation.
	 * @param simulation_id simulation id (int)
	 * @return List<String edge_id>
	 * @throws IDNotFound simulation id does not exist
	 * @throws SQLException database not reachable
	 */
	public static List<String> getEdgeList(int simulation_id) throws IDNotFound, SQLException {
		return getList(simulation_id, "edge", db.getSqlQueries().edgeListQuery);
	}
	
	/**
	 * Get a list of all lanes for a specified simulation.
	 * @param simulation_id simulation id (int)
	 * @return List<String lane_id>
	 * @throws IDNotFound simulation id does not exist
	 * @throws SQLException database not reachable
	 */
	public static List<String> getLaneList(int simulation_id) throws IDNotFound, SQLException {
		return getList(simulation_id, "lane_id", db.getSqlQueries().laneListQuery);
	}
	
	/**
	 * Get a list of all vehicles for a specified simulation.
	 * @param simulation_id simulation id (int)
	 * @return List<String vehicle_id>
	 * @throws IDNotFound simulation id does not exist
	 * @throws SQLException database not reachable
	 */
	public static List<String> getVehicleList(int simulation_id) throws IDNotFound, SQLException {
		return getList(simulation_id, "vehicleid", db.getSqlQueries().vehicleListQuery);
	}
	
	/**
	 * Get a list of all timestamps for a specified simulation.
	 * @param simulation_id simulation id (int)
	 * @return List<String timestamp>
	 * @throws IDNotFound simulation id does not exist
	 * @throws SQLException database not reachable
	 */
	public static List<String> getTimestampList(int simulation_id) throws IDNotFound, SQLException {
		return getList(simulation_id, "timestamp", db.getSqlQueries().timestampListQuery);
	}
	
	/**
	 * Get the summary statistics: number of vehicles, edges and junctions
	 * @param simulation_id simulation id
	 * @return Map<String label, Integer number>
	 * @throws SQLException database is not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static Map<String, Integer> getSummaryStatistics(int simulation_id) throws SQLException, IDNotFound {
		if (!doesSimIdExist(simulation_id)) throw new IDNotFound("Simulation ID: " + simulation_id + " not found");
		
		ResultSet rs = db.doQuery(() -> {
			db.getSqlQueries().summaryStatistics.setInt(1, simulation_id);
			return db.getSqlQueries().summaryStatistics.executeQuery();
		});
		
		Map<String, Integer> summaryStatistics = new HashMap<>();
		while (rs.next()) {
			int vehicles = rs.getInt("vehicles");
			int edges = rs.getInt("alledges");
			int junctions = rs.getInt("junction");
			summaryStatistics.put("vehicles", vehicles);
			summaryStatistics.put("edges", edges);
			summaryStatistics.put("junctions", junctions);
		}
		return summaryStatistics;
	}
	
	/**
	 * Store a simulation in the database
	 * @param simId simulation id (int)
	 * @param name simulation name
	 * @param description simulation description
	 * @param date simulation date (String)
	 * @param net xml file containing simulation net
	 * @param routes xml file containing simulation routes
	 * @param config xml file containing simulation configuration
	 * @throws SQLException database not reachable
	 * @throws IOException conversion failed
	 */
	public static void storeSimulation(Integer simId, String name, String description, String date, File net, File routes, File config, boolean storeData) throws SQLException, IOException {
		PGobject netObj = convertFileToPGobject(net);
		PGobject routesObj = convertFileToPGobject(routes);
		PGobject configObj = convertFileToPGobject(config);
		db.doUpdate(() -> {
			db.getSqlQueries().storeSimulationQuery.setInt(1, simId);
			db.getSqlQueries().storeSimulationQuery.setString(2, name);
			db.getSqlQueries().storeSimulationQuery.setString(3, date);
			db.getSqlQueries().storeSimulationQuery.setString(4, description);
			db.getSqlQueries().storeSimulationQuery.setObject(5, netObj);
			db.getSqlQueries().storeSimulationQuery.setObject(6, routesObj);
			db.getSqlQueries().storeSimulationQuery.setObject(7, configObj);
			return db.getSqlQueries().storeSimulationQuery.executeUpdate();
		});
	}
	
	/**
	 * Store a state file in the database
	 * @param simId simulation id (int)
	 * @param timeStamp timestamp
	 * @param stateFile xml file containing the state corresponding to the timestamp
	 * @throws SQLException database not reachable
	 * @throws IOException conversion failed
	 */
	public static void storeState(Integer simId, Integer timeStamp, File stateFile, boolean storeData) throws SQLException, IOException {
		PGobject stateFileObj = convertFileToPGobject(stateFile);
		db.doUpdate(() -> {
			db.getSqlQueries().storeStateQuery.setInt(1, simId);
			db.getSqlQueries().storeStateQuery.setFloat(2, timeStamp);
			db.getSqlQueries().storeStateQuery.setObject(3, stateFileObj);
			return db.getSqlQueries().storeStateQuery.executeUpdate();
		});	
	}

	
	/**
	 * Get the id for a given tag
	 * @param tag tag (String)
	 * @return Integer tag_id
	 * @throws SQLException database not reachable
	 */
	public static Integer getTagId(String tag) throws SQLException {
		ResultSet rs = db.doQuery(() -> {
			db.getSqlQueries().getTagIdQuery.setString(1, tag);
			return db.getSqlQueries().getTagIdQuery.executeQuery();
		});
		
		if (rs.next()) {
			return rs.getInt(1);
		}
		return null;
	}
	
	/**
	 * Create a new tag in the database
	 * @param tag tag name
	 * @throws SQLException database not reachable
	 */
	public static void createTag(String tag) throws SQLException {
		int tagId;
		do {
			tagId = MetaDataIO.generateId(4);
		} while (doesTagIdExist(tagId));
		System.out.println("generated tagId: " + tagId);
		storeTag(tagId, tag);
	}
	
	/**
	 * Store a new tag in the database by specified tag id
	 * @param tagId tag_id (Integer)
	 * @param tag tag (String)
	 * @throws SQLException database not reachable
	 */
	public static void storeTag(Integer tagId, String tag) throws SQLException {
		db.doUpdate(() -> {
			db.getSqlQueries().storeTagQuery.setInt(1, tagId);
			db.getSqlQueries().storeTagQuery.setString(2, tag);
			return db.getSqlQueries().storeTagQuery.executeUpdate();
		});		
	}
	
	/**
	 * Store a new connection between a simulation and a tag in the database
	 * @param tagId tag_id (Integer)
	 * @param simId simulation id (int)
	 * @throws SQLException database not reachable
	 */
	public static void storeSimTag(Integer tagId, int simId) throws SQLException {
		db.doUpdate(() -> {
			db.getSqlQueries().storeSimTagQuery.setInt(1, tagId);
			db.getSqlQueries().storeSimTagQuery.setInt(2, simId);
			return db.getSqlQueries().storeSimTagQuery.executeUpdate();
		});
	}
	
	/**
	 * Check if a tag id exists
	 * @param tagId tag_id (int)
	 * @return boolean
	 * @throws SQLException database not reachable
	 */
	public static boolean doesTagIdExist(int tagId) throws SQLException {
		ResultSet rs = db.doQuery(() -> {
			db.getSqlQueries().doesTagIdExistQuery.setInt(1, tagId);
			return db.getSqlQueries().doesTagIdExistQuery.executeQuery();
		});
		return (rs.next());
	}
	
	/**
	 * Check if a simulation id exists
	 * @param simId simulation id (int)
	 * @return boolean
	 * @throws SQLException database not reachable
	 */
	public static boolean doesSimIdExist(int simId) throws SQLException {
		ResultSet rs = db.doQuery(() -> {
			db.getSqlQueries().doesSimIdExistQuery.setInt(1, simId);
			return db.getSqlQueries().doesSimIdExistQuery.executeQuery();
		});
		return (rs.next());
	}
	
	/**
	 * Get all existing tags
	 * @return List<String tag>
	 * @throws SQLException database not reachable
	 */
	public static List<String> getTags() throws SQLException {
		ResultSet rs = db.doQuery(() -> {
			return db.getSqlQueries().getAllTagsQuery.executeQuery();
		});
		
		List<String> tags = new ArrayList<>();
		while (rs.next()) {
			String tag = rs.getString("tags");
			tags.add(tag);
		}
		return tags;
	}
	
	/**
	 * Remove all tags from a simulation
	 * @param simId simulation id (int)
	 * @throws SQLException database not reachable
	 * @throws IDNotFound simulation id does not exist
	 */
	public static void removeAllSimulationTags(int simId) throws SQLException, IDNotFound {
		int result = db.doUpdate(() -> {
			db.getSqlQueries().removeAllSimulationTagsQuery.setInt(1, simId);
			return db.getSqlQueries().removeAllSimulationTagsQuery.executeUpdate();
		});
		
		if (result == 0) {
			throw new IDNotFound("Could not find simulation id: " + simId);
		}
	}
	/**
	 * Convert an uploaded XML file to a JSON file for storing the the database
	 * @param file xml file
	 * @return PGobject with type JSON and value the xml file in json format
	 * @throws IOException conversion failed
	 * @throws SQLException database not reachable
	 */
	public static PGobject convertFileToPGobject(File file) throws IOException, SQLException {
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
	
	/**
	 * Exception that gets thrown if a specified id is not found
	 */
	public static class IDNotFound extends Exception {
		private static final long serialVersionUID = 4280320385143360167L;
		
		public IDNotFound(String msg) {
			super(msg);
		}
	}
}

