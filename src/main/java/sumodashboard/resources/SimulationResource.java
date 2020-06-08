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
	
	//TODO
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public Simulation getSimulation() {
//		Simulation simulation = SimulationDao.instance.getModel().get(ID);
//		if (simulation == null) {
//			throw new RuntimeException("Simulation " + ID + " not found.");
//		}
//		return simulation;
//	}
	
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
			
			Response response = Response.status(200).entity(graphPoints).build();
			return response;

		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get avg speed over time:\n" + e.getLocalizedMessage();
			Response response = Response.status(500).entity(errorMsg).build();
			return response;
		}
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	public void updateSimulation(Simulation simulation) {
		//SimulationDao.instance.getModel().put(simulation.getID(), simulation);
	}
	
	@DELETE
	public void deleteSimulation() {
		//TODO
		//SimulationDao.instance.getModel().remove(ID);
	}
}
