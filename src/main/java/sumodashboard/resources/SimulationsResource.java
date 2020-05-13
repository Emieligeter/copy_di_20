package sumodashboard.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import sumodashboard.dao.SimulationDao;
import sumodashboard.model.Simulation;

@Path("/simulations")
public class SimulationsResource {
	@Context
    UriInfo uriInfo;
    @Context
    Request request;
	  
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Simulation> getSimulations() {
		List<Simulation> simulations = new ArrayList<Simulation>();
		
		for (Simulation bike: SimulationDao.instance.getModel().values()) {			
			simulations.add(bike);
		}

		return simulations;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public void createSimulation(Simulation simulation) {
		SimulationDao.instance.getModel().put(simulation.getID(), simulation);
	}
	
	
	@Path("{simulation}")
	public SimulationResource getSimulation(@PathParam("simulation") String id) {
		return new SimulationResource(uriInfo, request, id);
	}
}
