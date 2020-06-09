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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import sumodashboard.dao.SimulationDao;
import sumodashboard.model.GraphPoint;
import sumodashboard.model.Simulation;

public class SimulationResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	String ID;
	public SimulationResource(UriInfo uriInfo, Request request, String ID) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.ID = ID;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSimulation() {
		int numericID;
		try {
			numericID = Integer.parseInt(ID);
		} catch (NumberFormatException e) {
			return Response.status(400).entity("Invalid ID, not a number.").build();
		}
		
		try {
			Simulation simulation = SimulationDao.instance.getSimulation(numericID);
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
	public Response getAvgSpeedTime() {
		System.out.println("getAvgSpeedTime() at /avgspeedtime in SimulationResource was reached.");
		int numericID;
		try {
			numericID = Integer.parseInt(ID);
		} catch (NumberFormatException e) {
			return Response.status(400).entity("Invalid ID, not a number.").build();
		}
		
		try {
			List<GraphPoint> graphPoints = SimulationDao.instance.getAvgSpeedTime(numericID);
			
			Response response;
			if (graphPoints.size() > 0) {
				response = Response.status(200).entity(graphPoints).build();
			}
			else {
				response = Response.status(400).entity("Invalid ID, does not exist").build();
			}
			return response;

		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get avg speed over time:\n" + e.getLocalizedMessage();
			Response response = Response.status(500).entity(errorMsg).build();
			return response;
		}
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSimulationMetadata(Simulation simulation) {
		int numericID;
		try {
			numericID = Integer.parseInt(ID);
		} catch (NumberFormatException e) {
			return Response.status(400).entity("Invalid ID, not a number.").build();
		}
		
		try {
			Response response;
			
			if (SimulationDao.instance.updateMetadata(numericID, simulation)) {
				response = Response.status(200).build();
			}
			else {
				response = Response.status(400).entity("Invalid ID, does not exist.").build();
			}
			
			return response;
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get a simulation:\n" + e.getLocalizedMessage();
			Response response = Response.status(500).entity(errorMsg).build();
			return response;
		}
	}
	
	@DELETE
	public Response deleteSimulation() {
		int numericID;
		try {
			numericID = Integer.parseInt(ID);
		} catch (NumberFormatException e) {
			return Response.status(400).entity("Invalid ID, not a number.").build();
		}
		
		try {
			Response response;
			
			if (SimulationDao.instance.removeSimulation(numericID)) {
				response = Response.status(200).build();
			}
			else {
				response = Response.status(400).entity("Invalid ID, does not exist.").build();
			}
			
			return response;
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get a simulation:\n" + e.getLocalizedMessage();
			Response response = Response.status(500).entity(errorMsg).build();
			return response;
		}
	}
}
