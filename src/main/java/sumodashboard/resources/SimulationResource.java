package sumodashboard.resources;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import sumodashboard.dao.SimulationDao;
import sumodashboard.dao.SimulationDao.IDNotFound;
import sumodashboard.model.GraphPoint;
import sumodashboard.model.Simulation;

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
	
	@GET
	@Path("/avgspeedtime")
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
	@Path("/vehiclelist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicleList() {
		System.out.println("getting the list @simResource");
		try {
			List<String> vehicles = SimulationDao.instance.getVehicleList(ID);
			System.out.println("returning the response @simResource");
			return Response.status(200).entity(vehicles).build();
			
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get a vehicle list:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
	
	@GET
	@Path("/vehiclespeed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicleSpeed() {
		String vehicleID = uriInfo.getQueryParameters().getFirst("vehicle");		
		
		try {
			List<GraphPoint> graphPoints = SimulationDao.instance.getVehicleSpeed(ID, vehicleID);
			return Response.status(200).entity(graphPoints).build();
			
		} catch (IDNotFound i) {
			return Response.status(400).entity(i.getMessage()).build();
			
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get avg speed over time for a vehicle:\n" + e.getLocalizedMessage();
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
			String errorMsg = "SQL Exception when trying to get avg speed over time:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
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
}
