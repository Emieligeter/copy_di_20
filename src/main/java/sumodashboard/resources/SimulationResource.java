package sumodashboard.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import sumodashboard.dao.SimulationDao;
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
	@Produces(MediaType.APPLICATION_XML)
	public Simulation getSimulation() {
		Simulation simulation = SimulationDao.instance.getModel().get(ID);
		if (simulation == null) {
			throw new RuntimeException("Simulation " + ID + " not found.");
		}
		return simulation;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	public void updateSimulation(Simulation simulation) {
		SimulationDao.instance.getModel().put(simulation.getID(), simulation);
	}
	
	@DELETE
	public void deleteSimulation() {
		SimulationDao.instance.getModel().remove(ID);
	}
}
