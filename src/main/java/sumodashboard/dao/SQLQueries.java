package sumodashboard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//Class for storing all SQL queries used in the DAO
public class SQLQueries {
	//Get all simulations
    public PreparedStatement getAllSimulationsQuery;
    //Get a single simulation by ID
    public PreparedStatement getSimulationQuery;
    //Remove a simulation by ID
    public PreparedStatement removeSimulationQuery;
    //Get the average speed of all vehicles over time
    public PreparedStatement avgSpeedQuery;
    //Get the speed of a specified vehicle over time
    public PreparedStatement vehicleSpeedQuery;
    //Get a list of all vehicles in a specified simulation
    public PreparedStatement vehicleListQuery;
    //Get the average route length over time
    public PreparedStatement avgRouteLengthQuery;
    //Store a new simulation in the database
    public PreparedStatement storeSimulationQuery;
    //Store a new state in the database
    public PreparedStatement storeStateQuery;
    //Get the id of a given tag
    public PreparedStatement getTagIdQuery;
    //Store a new tag in the database
    public PreparedStatement storeTagQuery;
    //Store a new connection between a simulation and a tag in the database
    public PreparedStatement storeSimTagQuery;
    //Check if a tag id exists
    public PreparedStatement doesTagIdExistQuery;
    //Check if a simulation id exists
    public PreparedStatement doesSimIdExistQuery;
    
    public PreparedStatement edgeAppearanceFrequencyQuery;
    public PreparedStatement numberOfLaneTransitingVehiclesQuery;
    public PreparedStatement vehicleRouteLengthQuery;
    public PreparedStatement vehicleSpeedQuery;
    public PreparedStatement vehicleSpeedFactorQuery;
    public PreparedStatement avgRouteLengthQuery;
    public PreparedStatement avgSpeedQuery;
    public PreparedStatement avgSpeedFactorQuery;
    public PreparedStatement cumulativeNumberOfArrivedVehiclesQuery;
    public PreparedStatement numberOfTransferredVehiclesQuery;
    public PreparedStatement numberOfRunningVehiclesQuery;
    
    public PreparedStatement edgeListQuery;
    public PreparedStatement laneListQuery;
    public PreparedStatement vehicleListQuery;    
    

    public SQLQueries(Connection connection) {
    	final String schemaName = "project";
        try {
            getAllSimulationsQuery = connection.prepareStatement("" +
                    "SELECT sim.simid, sim.name, sim.date, sim.description, sim.researcher, STRING_AGG(tags.value, ', ') AS tags " + 
                    "FROM project.tags, project.simulations sim, project.simulation_tags st " + 
                    "WHERE sim.simid = st.simid " + 
                    "AND st.tagid = tags.tagid " + 
                    "GROUP BY sim.simid");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            getSimulationQuery = connection.prepareStatement("" +
            		"SELECT sim.simid, sim.name, sim.date, sim.description, sim.researcher, STRING_AGG(tags.value, ', ') AS tags, sim.net, sim.routes, sim.config " +
                    "FROM project.tags, project.simulations sim, project.simulation_tags st " +
                    "WHERE sim.simid = st.simid " + 
                    "AND st.tagid = tags.tagid " + 
                    "AND sim.simid = ? " +
                    "GROUP BY sim.simid");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            removeSimulationQuery = connection.prepareStatement(""
                    + "DELETE FROM " + schemaName + ".states "
                    + "WHERE simid = ?; "
                    + "DELETE FROM " + schemaName + ".simulation_tags "
                    + "WHERE simid = ?; "
                    + "DELETE FROM " + schemaName + ".simulations "
                    + "WHERE simid = ?; ");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }
        
        try {
            edgeAppearanceFrequencyQuery = connection.prepareStatement("" +
            		"SELECT timestamp, array_length(string_to_array(concat(string_agg(edgeRoute, ' '), ' ~'), ? ), 1) - 1 AS edgeFrequency " +
            		"FROM	( " +
            		"	SELECT simid, timestamp, (state -> 'snapshot' -> 'route' ->> 'edges') AS edgeRoute " +
            		"	FROM " + schemaName + ".states " +
            		"	WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'object' " +
            		"	AND (state -> 'snapshot' -> 'route' -> 'id')::text = (state -> 'snapshot' -> 'vehicle' -> 'route')::text " +
            		"UNION " +
            		"	SELECT a.simid, a.timestamp, (routeElem ->> 'edges') AS edgeRoute " +
            		"	FROM ( " +
            		"		SELECT simid, timestamp, json_array_elements(state -> 'snapshot' -> 'vehicle') AS vehicleElem " +
            		"		FROM " + schemaName + ".states " +
            		"		WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'array' " +
            		"		) a, " +
            		"		(SELECT simid, timestamp, json_array_elements(state -> 'snapshot' -> 'route') AS routeElem " +
            		"		FROM " + schemaName + ".states " +
            		"		WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'array' " +
            		"		) b " +
            		"	WHERE (routeElem ->> 'id')::text = (vehicleElem ->> 'route')::text " +
            		"	AND a.simid = b.simid " +
            		"	AND a.timestamp = b.timestamp " +
            		") routeLengths " +
            		"WHERE simid = ? " +
            		"GROUP BY timestamp " +
            		"ORDER BY timestamp ASC"
                    );
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }
        
        try {
            edgeListQuery = connection.prepareStatement("" +
            		"SELECT DISTINCT unnest(string_to_array(json_array_elements(routes -> 'routes' -> 'vehicle') -> 'route' ->> 'edges', ' ')) AS edge " +
            		"FROM " + schemaName + ".simulations " +
            		"WHERE simid = ? " +
            		"ORDER BY edge"
                    );
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            numberOfLaneTransitingVehiclesQuery = connection.prepareStatement("" +
            		"SELECT simid, timestamp, LENGTH(vehicles)-LENGTH(REPLACE(vehicles,'v','')) AS vehicleCount " +
            		"FROM ( " +
            		"	SELECT simid, timestamp, lane ->> 'id' AS lane_id, lane -> 'vehicles' ->> 'value' AS vehicles " +
            		"	FROM ( " +
            		"		SELECT simid, timestamp, json_array_elements(state -> 'snapshot' -> 'lane') AS lane " +
            		"		FROM " + schemaName + ".states " +
            		"		WHERE simid = ? " +
            		"		) lanes " +
            		") vehicles " +
            		"WHERE lane_id = ? " +
            		"ORDER BY timestamp "
                    );
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }
        
        try {
            laneListQuery = connection.prepareStatement("" +
            		"SELECT json_array_elements(net -> 'net' -> 'edge') -> 'lane' ->> 'id' AS lane_id " +
            		"FROM " + schemaName + ".simulations " +
            		"WHERE simid = ?"
                    );
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }
        
        try {
            vehicleRouteLengthQuery = connection.prepareStatement("" +
            		"SELECT timestamp, (LENGTH(edgeRoute)-LENGTH(REPLACE(edgeRoute,'e',''))) AS routeLength " +
            		"FROM " +
            		"	( " +
            		"	SELECT simid, timestamp, (state -> 'snapshot' -> 'vehicle' ->> 'id') AS vehicle_id, (state -> 'snapshot' -> 'route' ->> 'edges') AS edgeRoute " +
            		"	FROM " + schemaName + ".states " +
            		"	WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'object' " +
            		"	AND (state -> 'snapshot' -> 'route' -> 'id')::text = (state -> 'snapshot' -> 'vehicle' -> 'route')::text " +
            		"UNION " +
            		"	SELECT a.simid, a.timestamp, (vehicleElem ->> 'id') as vehicle_id, (routeElem ->> 'edges') AS edgeRoute " +
            		"	FROM ( "  +
            		"		SELECT simid, timestamp, json_array_elements(state -> 'snapshot' -> 'vehicle') AS vehicleElem " +
            		"		FROM " + schemaName + ".states " +
            		"		WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'array' " +
            		"		) a, " +
            		"		(SELECT simid, timestamp, json_array_elements(state -> 'snapshot' -> 'route') AS routeElem " +
            		"		FROM " + schemaName + ".states " +
            		"		WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'array' " +
            		"		) b " +
            		"	WHERE (routeElem ->> 'id')::text = (vehicleElem ->> 'route')::text " +
            		"	AND a.simid = b.simid " +
            		"	AND a.timestamp = b.timestamp " +
            		") routeLengths " +
            		"WHERE simid = ? " +
            		"AND vehicle_id = ? " +
            		"ORDER BY timestamp ASC"
                   );
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }
        
        try {
            avgSpeedQuery = connection.prepareStatement("" +
                    "SELECT simid, timestamp, avgSpeed " +
                    "FROM " +
                    "	( " +
                    "		SELECT simid, timestamp, (state -> 'snapshot' -> 'vehicle' ->> 'speed')::float as avgSpeed " +
                    "		FROM " + schemaName + ".states	 " +
                    "		WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'object' " +
                    "	UNION " +
                    "		SELECT simid, timestamp, avg(vehicleSpeed) AS avgSpeed " +
                    "		FROM " +
                    "			( " +
                    "			SELECT simid, timestamp, (json_array_elements(state -> 'snapshot' -> 'vehicle') ->> 'speed')::float as vehicleSpeed " +
                    "			FROM " + schemaName + ".states " +
                    "			WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'array' " +
                    "			GROUP BY simid, timestamp " +
                    "                        ) q1 " +
                    "                GROUP BY simid, timestamp " +
                    "	) avgSpeeds " +
                    "WHERE simid = ? " +
                    "ORDER BY simid, timestamp ASC");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            vehicleSpeedQuery = connection.prepareStatement("" +
                    "SELECT simid, timestamp, vehicleSpeed, vehicle_id " +
                    "FROM " +
                    "	( " +
                    "		SELECT simid, timestamp, (state -> 'snapshot' -> 'vehicle' ->> 'speed')::float as vehicleSpeed,  " +
                    "		(state -> 'snapshot' -> 'vehicle' ->> 'id')::text as vehicle_id " +
                    "		FROM " + schemaName + ".states " +
                    "		WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'object' " +
                    "	UNION " +
                    "		SELECT simid, timestamp, (json_array_elements(state -> 'snapshot' -> 'vehicle') ->> 'speed')::float as vehicleSpeed, (json_array_elements(state -> 'snapshot' -> 'vehicle') ->> 'id')::text as vehicle_id " +
                    "		FROM " + schemaName + ".states " +
                    "		WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'array' " +
                    "		GROUP BY simid, vehicle_id, timestamp, vehicleSpeed " +
                    "	) q1 " +
                    "WHERE simid = ? AND vehicle_id = ? " +
                    "ORDER BY simid, vehicle_id, timestamp ASC ");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            vehicleListQuery = connection.prepareStatement("" +
            		"SELECT simid, (json_array_elements(routes -> 'routes' -> 'vehicle') ->> 'id') AS vehicleid " +
            		"FROM " + schemaName + ".simulations " +
            		"WHERE simid = ?"
            		);
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }
        
        
        try {
            avgRouteLengthQuery = connection.prepareStatement("" +
                    "SELECT timestamp, AVG(LENGTH(route)-LENGTH(REPLACE(route,'e',''))) AS avgCount " +
                    "FROM  " +
                    "	( " +
                    "	SELECT simid, timestamp, state -> 'snapshot' -> 'route' ->> 'edges' AS route " +
                    "	FROM " + schemaName + ".states " +
                    "	WHERE json_typeof(state -> 'snapshot' -> 'route') = 'object' " +
                    "UNION " +
                    "	SELECT simid, timestamp, json_array_elements(state -> 'snapshot' -> 'route') ->> 'edges' AS route " +
                    "	FROM " + schemaName + ".states " +
                    "	WHERE json_typeof(state -> 'snapshot' -> 'route') = 'array' " +
                    "        ) thing " +
                    "WHERE simid = ? " +
                    "GROUP BY timestamp " +
                    "ORDER BY timestamp ASC ");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            storeSimulationQuery = connection.prepareStatement(
                    "INSERT INTO " + schemaName + ".simulations (simID, name, date, description, net, routes, config)" +
                            "VALUES(?, ?, ?::date ,? ,? ,? ,?)");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            storeStateQuery = connection.prepareStatement(
                    "INSERT INTO " + schemaName + ".states (simID, timestamp, state)" +
                            "VALUES(?, ?, ?)");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            getTagIdQuery = connection.prepareStatement("" +
                    "SELECT tagid " +
                    "FROM " + schemaName + ".tags " +
                    "WHERE value = ?");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            storeTagQuery = connection.prepareStatement(
                    "INSERT INTO " + schemaName + ".tags (tagId, value)" +
                            "VALUES(?, ?)");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            storeSimTagQuery = connection.prepareStatement(
                    "INSERT INTO " + schemaName + ".simulation_tags (tagId, simId)" +
                            "VALUES(?, ?)");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            doesTagIdExistQuery = connection.prepareStatement("" +
                    "SELECT * " +
                    "FROM " + schemaName + ".tags t " +
                    "WHERE tagid = ?");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            doesSimIdExistQuery = connection.prepareStatement("" +
                    "SELECT * " +
                    "FROM " + schemaName + ".simulations " +
                    "WHERE simid = ?");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }
        
        try {
            avgSpeedFactorQuery = connection.prepareStatement("" +
            		"SELECT simid, timestamp, avgSpeedFactor " +
            		"FROM " +
            		"	(" +
            		"		SELECT simid, timestamp, (state -> 'snapshot' -> 'vehicle' ->> 'speedFactor')::float as avgSpeedFactor " +
            		"		FROM " + schemaName + ".states " +
            		"		WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'object' " +
            		"	UNION " +
            		"		SELECT simid, timestamp, avg(vehicleSpeedFactor) AS avgSpeedFactor " +
            		"		FROM " +
            		"			(" +
            		"			SELECT simid, timestamp, (json_array_elements(state -> 'snapshot' -> 'vehicle') ->> 'speedFactor')::float AS vehiclespeedFactor " + 
            		"			FROM " + schemaName + ".states " +
            		"			WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'array' " +
            		"			GROUP BY simid, timestamp " +
            		"                       ) vehicleSpeedFactors " +
            		"                GROUP BY simid, timestamp " +
            		"	) avgspeedFactors " +
            		"WHERE simid = ? " +
            		"ORDER BY simid, timestamp ASC"
            		);
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }
        
        try {
            vehicleSpeedFactorQuery = connection.prepareStatement("" +
            		"SELECT simid, timestamp, vehicleSpeedFactor, vehicle_id " +
            		"FROM " +
            		"	(" +
            		"		SELECT simid, timestamp, (state -> 'snapshot' -> 'vehicle' ->> 'speedFactor')::float as vehicleSpeedFactor, (state -> 'snapshot' -> 'vehicle' ->> 'id')::text as vehicle_id " +
            		"		FROM " + schemaName + ".states " +
            		"		WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'object' " +
            		"	UNION " +
            		"		SELECT simid, timestamp, (json_array_elements(state -> 'snapshot' -> 'vehicle') ->> 'speedFactor')::float as vehicleSpeedFactor, (json_array_elements(state -> 'snapshot' -> 'vehicle') ->> 'id')::text AS vehicle_id " +
            		"		FROM " + schemaName + ".states " +
            		"		WHERE json_typeof(state -> 'snapshot' -> 'vehicle') = 'array' " +
            		"		GROUP BY simid, vehicle_id, timestamp, vehicleSpeedFactor " +
            		"	) vehicleSpeedFactors " +
            		"WHERE simid = ? " +
            		"AND vehicle_id = ? " +
            		"ORDER BY simid, vehicle_id, timestamp ASC"
            		);
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }
        
        try {
            cumulativeNumberOfArrivedVehiclesQuery = connection.prepareStatement("" +
            		"SELECT simid, timestamp, (state -> 'snapshot' -> 'delay' ->> 'end') AS cumulativeNumberOfArrivedVehicles " +
            		"FROM " + schemaName + ".states " +
            		"WHERE simid = ? " +
            		"ORDER BY simid, timestamp ASC"
            		);
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }
        
        try {
            numberOfTransferredVehiclesQuery = connection.prepareStatement("" +
            		"SELECT simid, timestamp, numberOfTransferredVehicles " +
            		"FROM " +
            		"	(" +
            		"	SELECT simid, timestamp, json_array_length(state -> 'snapshot' -> 'vehicleTransfer') AS numberOfTransferredVehicles " +
            		"	FROM " + schemaName + ".states " +
            		"	WHERE json_typeof(state -> 'snapshot' -> 'vehicleTransfer') = 'array' " +
            		"UNION " +
            		"	SELECT simid, timestamp, 1 AS numberOfTransferredVehicles " +
            		"	FROM " + schemaName + ".states " +
            		"	WHERE json_typeof(state -> 'snapshot' -> 'vehicleTransfer') = 'object' " +
            		"UNION " +
            		"	SELECT simid, timestamp, 0 AS numberOfTransferredVehicles " +
            		"	FROM " + schemaName + ".states " +
            		"	WHERE (state -> 'snapshot' ->> 'vehicleTransfer') IS NULL " +
            		") numbersOfTransferredVehicles " +
            		"WHERE simid = ? " +
            		"ORDER BY simid, timestamp ASC"
            		);
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }
        
        try {
            numberOfRunningVehiclesQuery = connection.prepareStatement("" +
            		"SELECT simid, timestamp, (state -> 'snapshot' -> 'delay' ->> 'number') AS numberOfRunningVehicles " +
            		"FROM " + schemaName + ".states " +
            		"WHERE simid = ? " +
            		"ORDER BY simid, timestamp ASC"
            		);
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

    }
}
