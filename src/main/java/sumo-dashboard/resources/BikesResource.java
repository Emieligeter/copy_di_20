package nl.utwente.di.bikedealer.resources;

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

import nl.utwente.di.bikedealer.dao.BikeDao;
import nl.utwente.di.bikedealer.model.Bike;

@Path("/bikes")
public class BikesResource {
	@Context
    UriInfo uriInfo;
    @Context
    Request request;
	  
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Bike> getBikes() {
		List<Bike> bikes = new ArrayList<Bike>();
		String colorFilter = uriInfo.getQueryParameters().getFirst("color");
		String genderFilter = uriInfo.getQueryParameters().getFirst("gender");
		
		for (Bike bike: BikeDao.instance.getModel().values()) {
			if (colorFilter != null && !colorFilter.contentEquals(bike.getColour())) continue;
			if (genderFilter != null && !genderFilter.contentEquals(bike.getGender())) continue;
			
			bikes.add(bike);
		}

		return bikes;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public void createBike(Bike bike) {
		BikeDao.instance.getModel().put(bike.getID(), bike);
	}
	
	
	@Path("{bike}")
	public BikeResource getBike(@PathParam("bike") String id) {
		return new BikeResource(uriInfo, request, id);
	}
}
