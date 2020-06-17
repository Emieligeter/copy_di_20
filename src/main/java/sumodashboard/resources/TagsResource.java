package sumodashboard.resources;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import sumodashboard.dao.SimulationDao;
import sumodashboard.model.MetaData;

//Class responsible for all requests to /rest/tags
@Path("/tags")
public class TagsResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	//Get metadata of all simulations
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSimulations() {
		try {			
			List<String> tags = SimulationDao.instance.getTags();
			
			Response response = Response.status(200).entity(tags).build();
			return response;
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get tags:\n" + e.getLocalizedMessage();
			Response response = Response.status(500).entity(errorMsg).build();
			return response;
		}
	}
}
