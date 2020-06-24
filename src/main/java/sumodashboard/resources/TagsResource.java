package sumodashboard.resources;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import sumodashboard.model.MetaData;
import sumodashboard.model.Simulation;

/**
 * Class responsible for all requests to /rest/tags
 */
@Path("/tags")
public class TagsResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ContainerRequestContext requestContext;
	
	/**
	 * Get metadata of all simulations
	 * @return response
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTags() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.FORBIDDEN).build();
		
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
	
	/**
	 * Create new tag
	 * @param tag name of the tag to create
	 * @return response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewTag(String tag) {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.FORBIDDEN).build();
		try {
			SimulationDao.instance.createTag(tag);
			return Response.status(200).build();
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get a simulation:\n" + e.getLocalizedMessage();
			return Response.status(500).entity(errorMsg).build();
		}
	}
}
