package sumodashboard.resources;

import java.sql.SQLException;
import java.util.ArrayList;
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
				response = Response.status(400).entity("Invalid ID, does not exist: " + ID).build();
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
			String errorMsg = "SQL Exception when trying to update a simulation:\n" + e.getLocalizedMessage();
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
			String errorMsg = "SQL Exception when trying to delete a simulation:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	/**
	 * Interface used for passing a method as an argument to getStats()
	 */
	private interface StatsRequest {
		Map<Double, Double> request() throws IDNotFound, SQLException;
	}
	
	/**
	 * Get a statistic without a parameter
	 * @param paramType the type of parameter required for the request
	 * @param requestFunction ParameterizedStatsRequest class implementing the request to the Dao
	 * @return response
	 */
	private Response getStats(StatsRequest requestFunction) {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		
		try {
			Map<Double, Double> graphPoints = requestFunction.request();
			return Response.status(200).entity(graphPoints).build();

		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception:\n" + e.getLocalizedMessage();
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
		return getStats(() -> {
			return SimulationDao.instance.getAvgRouteLength(ID);
		});
	}
	
	/**
	 * Get the average speed of all vehicles over time
	 * @return response
	 */
	@GET
	@Path("/avgspeed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAvgSpeed() {
		return getStats(() -> {
			return SimulationDao.instance.getAverageSpeed(ID);
		});
	}
	
	/**
	 * Get the average speed factor of all vehicles over time
	 * @return response
	 */
	@GET
	@Path("/avgspeedfactor")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAvgSpeedFactor() {
		return getStats(() -> {
			return SimulationDao.instance.getAverageSpeedFactor(ID);
		});
	}
	
	/**
	 * Get the cumulative number of arrived vehicles over time
	 * @return response
	 */
	@GET
	@Path("/arrivedvehicles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArrivedVehicles() {
		return getStats(() -> {
			return SimulationDao.instance.getCumNumArrivedVehicles(ID);
		});
	}
	
	/**
	 * Get the number of transferred vehicles over time
	 * @return response
	 */
	@GET
	@Path("/transferredvehicles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTransferredVehicles() {
		return getStats(() -> {
			return SimulationDao.instance.getNumTransferredVehicles(ID);
		});
	}
	
	/**
	 * Get the number of running vehicles over time
	 * @return response
	 */
	@GET
	@Path("/runningvehicles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRunningVehicles() {
		return getStats(() -> {
			return SimulationDao.instance.getNumRunningVehicles(ID);
		});
	}
	
	/**
	 * Interface used for passing a method as an argument to getStatsWithParam()
	 */
	private interface ParameterizedStatsRequest {
		Map<Double, Double> request(String paramID) throws IDNotFound, SQLException;
	}
	
	/**
	 * Get a statistic with parameter
	 * @param paramType the type of parameter required for the request
	 * @param requestFunction ParameterizedStatsRequest class implementing the request to the Dao
	 * @return response
	 */
	private Response getStatsWithParam(String paramType, ParameterizedStatsRequest requestFunction) {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		
		String paramID = uriInfo.getQueryParameters().getFirst(paramType);
		if (paramID == null) {
			return Response.status(400).entity("Please specifiy " + paramType + " id using query parameter \"" + paramType + "\"").build();
		}		
		
		return getStats(() -> {
			return requestFunction.request(paramID);
		});
	}
	
	/**
	 * Get the edge appearance frequency over time
	 * @return response
	 */
	@GET
	@Path("/edgefrequency")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEdgeFrequency() {
		ParameterizedStatsRequest requestFunction = (paramID) -> {
			return SimulationDao.instance.getEdgeAppearenceFrequency(ID, paramID);
		};
		return getStatsWithParam("edge", requestFunction);
	}
	
	/**
	 * Get the number of lane transiting vehicles over time
	 * @return response
	 */
	@GET
	@Path("/lanetransitingvehicles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLaneTransitingVehicles() {
		ParameterizedStatsRequest requestFunction = (paramID) -> {
			return SimulationDao.instance.getLaneTransitingVehicles(ID, paramID);
		};
		return getStatsWithParam("lane", requestFunction);	
	}
	
	/**
	 * Get the route length for a specified vehicle over time
	 * @return response
	 */
	@GET
	@Path("/vehicleroutelength")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicleRouteLength() {
		ParameterizedStatsRequest requestFunction = (paramID) -> {
			return SimulationDao.instance.getVehicleRouteLength(ID, paramID);
		};
		return getStatsWithParam("vehicle", requestFunction);
	}
	
	/**
	 * Get the speed for a specified vehicle over time
	 * @return response
	 */
	@GET
	@Path("/vehiclespeed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicleSpeed() {
		ParameterizedStatsRequest requestFunction = (paramID) -> {
			return SimulationDao.instance.getVehicleSpeed(ID, paramID);
		};
		return getStatsWithParam("vehicle", requestFunction);
	}
	
	/**
	 * Get the speed factor for a specified vehicle over time
	 * @return response
	 */
	@GET
	@Path("/vehiclespeedfactor")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicleSpeedFactor() {
		ParameterizedStatsRequest requestFunction = (paramID) -> {
			return SimulationDao.instance.getVehicleSpeedFactor(ID, paramID);
		};
		return getStatsWithParam("vehicle", requestFunction);
	}
	
	/**
	 * Interface used for passing a method as an argument to getStats()
	 */
	private interface LabeledStatsRequest {
		Map<String, Integer> request() throws IDNotFound, SQLException;
	}
	
	/**
	 * Get a labeled statistic without a parameter
	 * @param requestFunction StatsRequest class implementing the request to the Dao
	 * @return response
	 */
	private Response getLabeledStats(LabeledStatsRequest requestFunction) {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		
		try {
			Map<String, Integer> dataPoints = requestFunction.request();
			return Response.status(200).entity(dataPoints).build();

		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception:\n" + e.getLocalizedMessage();
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
		return getLabeledStats(() -> {
			return SimulationDao.instance.getEdgeAppearanceFrequencyInitialRoute(ID);
		});
	}
	
	/**
	 * Get information about all vehicles: length of their initial routes
	 * @return response
	 */
	@GET
	@Path("/routelengthinitial")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInitRouteLengthPerVehicle() {
		return getLabeledStats(() -> {
			return SimulationDao.instance.getInitRouteLengthVehicle(ID);
		});
	}
	
	/**
	 * Interface used for passing a method as an argument to getStats()
	 */
	private interface ListRequest {
		List<String> request() throws IDNotFound, SQLException;
	}
	
	/**
	 * Get a labeled statistic without a parameter
	 * @param requestFunction StatsRequest class implementing the request to the Dao
	 * @return response
	 */
	private Response getList(ListRequest requestFunction) {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		
		try {
			List<String> values = requestFunction.request();
			return Response.status(200).entity(values).build();

		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception:\n" + e.getLocalizedMessage();
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
		return getList(() -> {
			return SimulationDao.instance.getEdgeList(ID);
		});
	}
	
	/**
	 * Get a list of all lanes that appear in a simulation
	 * @return response
	 */
	@GET
	@Path("/lanelist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLaneList() {
		return getList(() -> {
			return SimulationDao.instance.getLaneList(ID);
		});
	}
	
	/**
	 * Get a list of all vehicles that appear in a simulation
	 * @return response
	 */
	@GET
	@Path("/vehiclelist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicleList() {
		return getList(() -> {
			return SimulationDao.instance.getVehicleList(ID);
		});
	}
	
	/**
	 * Get the summary statistics: number of vehicles, edges and junctions
	 * @return response
	 */
	@GET
	@Path("/summarystatistics")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSummaryStatistics() {
		return getLabeledStats(() -> {
			return SimulationDao.instance.getSummaryStatistics(ID);
		});
	}
}
