package nl.utwente.di.bikedealer.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import nl.utwente.di.bikedealer.dao.BikeDao;
import nl.utwente.di.bikedealer.model.Bike;

public class BikeResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	String ID;
	public BikeResource(UriInfo uriInfo, Request request, String ID) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.ID = ID;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Bike getBikeDetails() {
		Bike bike = BikeDao.instance.getModel().get(ID);
		if (bike == null) {
			throw new RuntimeException("Bike " + ID + " not found.");
		}
		return bike;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	public void updateBike(Bike bike) {
		BikeDao.instance.getModel().put(bike.getID(), bike);
	}
	
	@DELETE
	public void deleteBike() {
		BikeDao.instance.getModel().remove(ID);
	}
}
