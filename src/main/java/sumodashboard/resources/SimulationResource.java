package sumodashboard.resources;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import sumodashboard.dao.SimulationDao;
import sumodashboard.dao.SimulationDao.IDNotFound;
import sumodashboard.model.Simulation;

/**
 * Class responsible for handling all requests to /rest/simulations/id/{id}
 */
public class SimulationResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ContainerRequestContext requestContext;
	
	int ID;
	public SimulationResource(UriInfo uriInfo, Request request, ContainerRequestContext requestContext, int ID)  {
		this.uriInfo = uriInfo;
		this.request = request;
		this.requestContext = requestContext;
		this.ID = ID;
	}
	
	/**
	 * Get all data for a specified simulation
	 * @return response
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSimulation() {	
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		try {
			Simulation simulation = SimulationDao.instance.getSimulation(ID);
			Response response;
			
			if (simulation == null) {
				response = Response.status(400).entity("Invalid ID, does not exist").build();
			}
			else {
				response = Response.status(200).entity(simulation).build();
			}
			
			return response;
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get a simulation:\n" + e.getLocalizedMessage();
			Response response = Response.status(500).entity(errorMsg).build();
			return response;
		}
	}
	
	/**
	 * Update the simulation metadata
	 * @param simulation a simulation with the new data
	 * @return response
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSimulationMetadata(Simulation simulation) {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		try {
			SimulationDao.instance.updateMetadata(ID, simulation);
			return Response.status(200).build();

		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get a simulation:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	/**
	 * Delete a simulation
	 * @return response
	 */
	@DELETE
	public Response deleteSimulation() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		try {
			SimulationDao.instance.removeSimulation(ID);
			return Response.status(200).build();
			
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get a simulation:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	/**
	 * Get the edge appearance frequency over time
	 * @return response
	 */
	@GET
	@Path("/edgefrequency")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEdgeFrequency() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		String edgeID = uriInfo.getQueryParameters().getFirst("edge");
		if (edgeID == null) {
			return Response.status(400).entity("Please specifiy edge id using query parameter \"edge\"").build();
		}		
		try {
			Map<Double, Double> graphPoints = SimulationDao.instance.getEdgeAppearenceFrequency(ID, edgeID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get the appearence frequency for an edge:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}	
	}
	
	/**
	 * Get the number of lane transiting vehicles over time
	 * @return response
	 */
	@GET
	@Path("/lanetransitingvehicles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLaneTransitingVehicles() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		String laneID = uriInfo.getQueryParameters().getFirst("lane");
		if (laneID == null) {
			return Response.status(400).entity("Please specifiy lane id using query parameter \"lane\"").build();
		}		
		try {
			Map<Double, Double> graphPoints = SimulationDao.instance.getLaneTransitingVehicles(ID, laneID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get number of lane transiting vehicles for a lane:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}	
	}
	
	/**
	 * Get the route length for a specified vehicle over time
	 * @return response
	 */
	@GET
	@Path("/vehicleroutelength")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicleRouteLength() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		String vehicleID = uriInfo.getQueryParameters().getFirst("vehicle");
		if (vehicleID == null) {
			return Response.status(400).entity("Please specifiy vehicle id using query parameter \"vehicle\"").build();
		}		
		try {
			Map<Double, Double> graphPoints = SimulationDao.instance.getVehicleRouteLength(ID, vehicleID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get route length for a vehicle:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	/**
	 * Get the speed for a specified vehicle over time
	 * @return response
	 */
	@GET
	@Path("/vehiclespeed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicleSpeed() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		String vehicleID = uriInfo.getQueryParameters().getFirst("vehicle");	
		if (vehicleID == null) {
			return Response.status(400).entity("Please specifiy vehicle id using query parameter \"vehicle\"").build();
		}		
		try {
			Map<Double, Double> graphPoints = SimulationDao.instance.getVehicleSpeed(ID, vehicleID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get speed over time for a vehicle:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	/**
	 * Get the speed factor for a specified vehicle over time
	 * @return response
	 */
	@GET
	@Path("/vehiclespeedfactor")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicleSpeedFactor() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		String vehicleID = uriInfo.getQueryParameters().getFirst("vehicle");
		if (vehicleID == null) {
			return Response.status(400).entity("Please specifiy vehicle id using query parameter \"vehicle\"").build();
		}		
		try {
			Map<Double, Double> graphPoints = SimulationDao.instance.getVehicleSpeedFactor(ID, vehicleID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get the speedFactor over time for a vehicle:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}	
	}
	
	/**
	 * Get the average route length of all vehicles over time
	 * @return response
	 */
	@GET
	@Path("/avgroutelength")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAvgRouteLength() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		try {
			Map<Double, Double> graphPoints = SimulationDao.instance.getAvgRouteLength(ID);
			return Response.status(200).entity(graphPoints).build();

		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get avg route length over time:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	/**
	 * Get the average speed of all vehicles over time
	 * @return response
	 */
	@GET
	@Path("/avgspeed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAvgSpeed() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		try {
			Map<Double, Double> graphPoints = SimulationDao.instance.getAverageSpeed(ID);
			return Response.status(200).entity(graphPoints).build();

		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get avg speed over time:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	/**
	 * Get the average speed factor of all vehicles over time
	 * @return response
	 */
	@GET
	@Path("/avgspeedfactor")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAvgSpeedFactor() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		try {
			Map<Double, Double> graphPoints = SimulationDao.instance.getAverageSpeedFactor(ID);
			return Response.status(200).entity(graphPoints).build();

		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get avg speedFactor over time:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	/**
	 * Get the cumulative number of arrived vehicles over time
	 * @return response
	 */
	@GET
	@Path("/arrivedvehicles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArrivedVehicles() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		try {
			Map<Double, Double> graphPoints = SimulationDao.instance.getCumNumArrivedVehicles(ID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get cumulative number of arrived vehicles over time:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	/**
	 * Get the number of transferred vehicles over time
	 * @return response
	 */
	@GET
	@Path("/transferredvehicles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTransferredVehicles() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		try {
			Map<Double, Double> graphPoints = SimulationDao.instance.getNumTransferredVehicles(ID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get number of transferred vehicles over time:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	/**
	 * Get the number of running vehicles over time
	 * @return response
	 */
	@GET
	@Path("/runningvehicles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRunningVehicles() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		try {
			Map<Double, Double> graphPoints = SimulationDao.instance.getNumRunningVehicles(ID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get number of running vehicles over time:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	/**
	 * Get a list of all edges that appear in a route in a specified simulation
	 * @return response
	 */
	@GET
	@Path("/edgelist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEdgeList() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		try {
			List<String> edges = SimulationDao.instance.getEdgeList(ID);
			return Response.status(200).entity(edges).build();
			
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get a edge list:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}	}
	
	/**
	 * Get a list of all lanes that appear in a simulation
	 * @return response
	 */
	@GET
	@Path("/lanelist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLaneList() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		try {
			List<String> lanes = SimulationDao.instance.getLaneList(ID);
			return Response.status(200).entity(lanes).build();
			
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get a lane list:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	/**
	 * Get a list of all vehicles that appear in a simulation
	 * @return response
	 */
	@GET
	@Path("/vehiclelist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicleList() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		try {
			List<String> vehicles = SimulationDao.instance.getVehicleList(ID);
			return Response.status(200).entity(vehicles).build();
			
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get a vehicle list:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	/**
	 * Get information about all edges: how often they appear in the initial routes
	 * @return response
	 */
	@GET
	@Path("/edgefrequencyinitial")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEdgeAppearanceFrequencyInitialRoute() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		try {
			Map<String, Integer> dataPoints = SimulationDao.instance.getEdgeAppearanceFrequencyInitialRoute(ID);
			return Response.status(200).entity(dataPoints).build();
			
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get the initial edge frequency per edge:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	/**
	 * Get information about all vehicles: length of their initial routes
	 * @return response
	 */
	@GET
	@Path("/routelengthinitial")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInitRouteLengthPerVehicle() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.FORBIDDEN).build();
		try {
			Map<String, Integer> dataPoints = SimulationDao.instance.getInitRouteLengthVehicle(ID);
			return Response.status(200).entity(dataPoints).build();
			
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get the inital route length per vehicle:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	
}
