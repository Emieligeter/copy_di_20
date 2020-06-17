package sumodashboard.resources;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import sumodashboard.dao.SimulationDao;
import sumodashboard.dao.SimulationDao.IDNotFound;
import sumodashboard.model.GraphPoint;
import sumodashboard.model.Simulation;

//Class responsible for handling all requests to /rest/simulations/id/{id}
public class SimulationResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	int ID;
	public SimulationResource(UriInfo uriInfo, Request request, int ID)  {
		this.uriInfo = uriInfo;
		this.request = request;
		this.ID = ID;
	}
	
	//Get all data for a simulation
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSimulation() {		
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
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSimulationMetadata(Simulation simulation) {		
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
	
	//Delete a simulation
	@DELETE
	public Response deleteSimulation() {
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
	
	@GET
	@Path("/edgefrequency")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEdgeFrequency() {
		String edgeID = uriInfo.getQueryParameters().getFirst("edge");
		if (edgeID == null) {
			return Response.status(400).entity("Please specifiy edge id using query parameter \"edge\"").build();
		}		
		try {
			List<GraphPoint> graphPoints = SimulationDao.instance.getEdgeAppearenceFrequency(ID, edgeID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get the appearence frequency for an edge:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}	
	}
	
	@GET
	@Path("/lanetransitingvehicles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLaneTransitingVehicles() {
		String laneID = uriInfo.getQueryParameters().getFirst("lane");
		if (laneID == null) {
			return Response.status(400).entity("Please specifiy lane id using query parameter \"lane\"").build();
		}		
		try {
			List<GraphPoint> graphPoints = SimulationDao.instance.getLaneTransitingVehicles(ID, laneID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get number of lane transiting vehicles for a lane:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}	
	}
	
	@GET
	@Path("/vehicleroutelength")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicleRouteLength() {
		String vehicleID = uriInfo.getQueryParameters().getFirst("vehicle");
		if (vehicleID == null) {
			return Response.status(400).entity("Please specifiy vehicle id using query parameter \"vehicle\"").build();
		}		
		try {
			List<GraphPoint> graphPoints = SimulationDao.instance.getVehicleRouteLength(ID, vehicleID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get route length for a vehicle:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	@GET
	@Path("/vehiclespeed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicleSpeed() {
		String vehicleID = uriInfo.getQueryParameters().getFirst("vehicle");	
		if (vehicleID == null) {
			return Response.status(400).entity("Please specifiy vehicle id using query parameter \"vehicle\"").build();
		}		
		try {
			List<GraphPoint> graphPoints = SimulationDao.instance.getVehicleSpeed(ID, vehicleID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get speed over time for a vehicle:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	@GET
	@Path("/vehiclespeedfactor")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicleSpeedFactor() {
		String vehicleID = uriInfo.getQueryParameters().getFirst("vehicle");
		if (vehicleID == null) {
			return Response.status(400).entity("Please specifiy vehicle id using query parameter \"vehicle\"").build();
		}		
		try {
			List<GraphPoint> graphPoints = SimulationDao.instance.getVehicleSpeedFactor(ID, vehicleID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get the speedFactor over time for a vehicle:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}	
	}
	
	@GET
	@Path("/avgroutelength")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAvgRouteLength() {
		try {
			List<GraphPoint> graphPoints = SimulationDao.instance.getAvgRouteLength(ID);
			return Response.status(200).entity(graphPoints).build();

		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get avg route length over time:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	@GET
	@Path("/avgspeed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAvgSpeed() {
		try {
			List<GraphPoint> graphPoints = SimulationDao.instance.getAverageSpeed(ID);
			return Response.status(200).entity(graphPoints).build();

		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get avg speed over time:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	@GET
	@Path("/avgspeedfactor")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAvgSpeedFactor() {
		try {
			List<GraphPoint> graphPoints = SimulationDao.instance.getAverageSpeedFactor(ID);
			return Response.status(200).entity(graphPoints).build();

		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get avg speedFactor over time:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	@GET
	@Path("/arrivedvehicles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArrivedVehicles() {
		try {
			List<GraphPoint> graphPoints = SimulationDao.instance.getCumNumArrivedVehicles(ID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get cumulative number of arrived vehicles over time:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	@GET
	@Path("/transferredvehicles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTransferredVehicles() {
		try {
			List<GraphPoint> graphPoints = SimulationDao.instance.getNumTransferredVehicles(ID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get number of transferred vehicles over time:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	@GET
	@Path("/runningvehicles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRunningVehicles() {
		try {
			List<GraphPoint> graphPoints = SimulationDao.instance.getNumRunningVehicles(ID);
			return Response.status(200).entity(graphPoints).build();
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get number of running vehicles over time:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	@GET
	@Path("/edgelist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEdgeList() {
		try {
			List<String> edges = SimulationDao.instance.getEdgeList(ID);
			return Response.status(200).entity(edges).build();
			
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get a edge list:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}	}
	
	@GET
	@Path("/lanelist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLaneList() {
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
	
	@GET
	@Path("/vehiclelist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicleList() {
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
	
	
	
	
}
