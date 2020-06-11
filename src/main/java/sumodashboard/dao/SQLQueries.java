package sumodashboard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLQueries {
    public PreparedStatement getAllSimulationsQuery;
    public PreparedStatement getSimulationQuery;
    public PreparedStatement removeSimulationQuery;
    public PreparedStatement avgSpeedQuery;
    public PreparedStatement vehicleSpeedQuery;
    public PreparedStatement avgRouteLengthQuery;
    public PreparedStatement storeSimulationQuery;
    public PreparedStatement storeStateQuery;
    public PreparedStatement getTagIdQuery;
    public PreparedStatement storeTagQuery;
    public PreparedStatement storeSimTagQuery;
    public PreparedStatement doesTagIdExistQuery;
    public PreparedStatement doesSimIdExistQuery;

    public SQLQueries(Connection connection) {
        try {
            getAllSimulationsQuery = connection.prepareStatement("" +
                    "SELECT simid, name, date, description, researcher " +
                    "FROM project.simulations");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            getSimulationQuery = connection.prepareStatement("" +
                    "SELECT simid, name, date, description, researcher, net, routes, config " +
                    "FROM project.simulations " +
                    "WHERE simid = ?");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            removeSimulationQuery = connection.prepareStatement(""
                    + "DELETE FROM project.states "
                    + "WHERE simid = ?; "
                    + "DELETE FROM project.simulation_tags "
                    + "WHERE simid = ?; "
                    + "DELETE FROM project.simulations "
                    + "WHERE simid = ?; ");
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
                    "	FROM project.states " +
                    "	WHERE json_typeof(state -> 'snapshot' -> 'route') = 'object' " +
                    "UNION " +
                    "	SELECT simid, timestamp, json_array_elements(state -> 'snapshot' -> 'route') ->> 'edges' AS route " +
                    "	FROM project.states " +
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
                    "INSERT INTO project.simulations (simID, name, date, description, net, routes, config)" +
                            "VALUES(?, ?, ?::date ,? ,? ,? ,?)");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            storeStateQuery = connection.prepareStatement(
                    "INSERT INTO project.states (simID, timestamp, state)" +
                            "VALUES(?, ?, ?)");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            getTagIdQuery = connection.prepareStatement("" +
                    "SELECT tagid " +
                    "FROM project.tags " +
                    "WHERE value = ?");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            storeTagQuery = connection.prepareStatement(
                    "INSERT INTO project.tags (tagId, value)" +
                            "VALUES(?, ?)");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            storeSimTagQuery = connection.prepareStatement(
                    "INSERT INTO project.simulation_tags (tagId, simId)" +
                            "VALUES(?, ?)");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            doesTagIdExistQuery = connection.prepareStatement("" +
                    "SELECT * " +
                    "FROM project.tags t " +
                    "WHERE tagid = ?");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }

        try {
            doesSimIdExistQuery = connection.prepareStatement("" +
                    "SELECT * " +
                    "FROM project.simulations " +
                    "WHERE simid = ?");
        } catch (SQLException e) {
            System.err.println("Couldn't prepare statement: ");
            e.printStackTrace();
        }
    }
}
