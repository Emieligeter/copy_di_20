package sumodashboard.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Priority;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NameBinding;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Priorities;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import sumodashboard.dao.MetaDataIO;
import sumodashboard.dao.SimulationDao;
import sumodashboard.model.MetaData;
import sumodashboard.model.SumoFilesDTO;
import sumodashboard.services.FileReadService;

//Class responsible for all requests to /rest/simulations
@Path("/simulations")
public class SimulationsResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@NameBinding
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.METHOD})
	public @interface Secured { }
	
	@Secured
	@Provider
	@Priority(Priorities.AUTHENTICATION)
	public class AuthenticationFilter implements ContainerRequestFilter {

	    private static final String REALM = "example";
	    private static final String AUTHENTICATION_SCHEME = "Bearer";

	    @Override
	    public void filter(ContainerRequestContext requestContext) throws IOException {
	    	System.out.println("Test");
	    	//^This never gets called for some reason

	        // Get the Authorization header from the request
	        String authorizationHeader =
	                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

	        // Validate the Authorization header
	        if (!isTokenBasedAuthentication(authorizationHeader)) {
	            abortWithUnauthorized(requestContext);
	            return;
	        }

	        // Extract the token from the Authorization header
	        String token = authorizationHeader
	                            .substring(AUTHENTICATION_SCHEME.length()).trim();

	        try {

	            // Validate the token
	            validateToken(token);

	        } catch (Exception e) {
	            abortWithUnauthorized(requestContext);
	        }
	    }

	    private boolean isTokenBasedAuthentication(String authorizationHeader) {

	        // Check if the Authorization header is valid
	        // It must not be null and must be prefixed with "Bearer" plus a whitespace
	        // The authentication scheme comparison must be case-insensitive
	        return authorizationHeader != null && authorizationHeader.toLowerCase()
	                    .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
	    }

	    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

	        // Abort the filter chain with a 401 status code response
	        // The WWW-Authenticate header is sent along with the response
	        requestContext.abortWith(
	                Response.status(Response.Status.UNAUTHORIZED)
	                        .header(HttpHeaders.WWW_AUTHENTICATE, 
	                                AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
	                        .build());
	    }

	    private void validateToken(String token) throws Exception {
	        // Check if the token was issued by the server and if it's not expired
	        // Throw an Exception if the token is invalid
	    	if (!token.equals("test")) throw new Exception();
	    }
	}
	
	//Get metadata of all simulations
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSimulations() {
		try {			
			List<MetaData> simulations = SimulationDao.instance.getSimulations();
			
			Response response = Response.status(200).entity(simulations).build();
			return response;
		} catch (SQLException e) {
			String errorMsg = "SQL Exception when trying to get simulations:\n" + e.getLocalizedMessage();
			Response response = Response.status(500).entity(errorMsg).build();
			return response;
		}
	}
	
	/**
	 * Receives an {@link InputStream} and {@link FormDataBodyPart} and returns a {@link Response}. 
	 * The {@link InputStream} is parsed to {@link File}s and the files are stored in the database through the {@link SimulationDao}.
	 * The files are deleted here after all reading has been done.
	 * @param {@link InputStream} 
	 * @param {@link FormDataBodyPart} 
	 * @return {@link Response} Response
	 * @throws {@link Exception}
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFiles(@FormDataParam("uploadFile") InputStream inputStream,
			@FormDataParam("uploadFile") FormDataBodyPart bodyPart) throws Exception {

		//Read the inputstream and make a DTO object
		SumoFilesDTO dto = FileReadService.readInputStream(inputStream, bodyPart);
		inputStream.close();

		HashMap<String, File> files = dto.getFiles();
		TreeMap<Integer, File> stateFiles = dto.getStateFiles();
		MetaData meta = MetaDataIO.parseMetadata(files.get("metadata.txt")); //parse metadata into object
		SimulationDao SimDao = SimulationDao.instance;
		
		//Generate a random id, if it exists generate a new one
		int simId = 0;
		do {simId = MetaDataIO.generateId(5);
		}while(SimDao.doesSimIdExist(simId)) ;
		 
		//Store a simulation in 'simulation' table
		SimDao.storeSimulation(
				simId, meta.getName(), 
				meta.getDescription(), 
				meta.getDate(),
				files.get("net.net.xml"), 
				files.get("routes.rou.xml"), 
				files.get("simulation.sumocfg"));

		//Store all state files in 'states' table
		for (Map.Entry<Integer, File> sf : stateFiles.entrySet()) {
			Integer timeStamp = sf.getKey();
			File file = sf.getValue();
			SimDao.storeState(simId, timeStamp, file);
		}
		
		//Check if tags exists, if not, create new one. Then add it to 'simulation_tag' table
		String tags = meta.getTags();
		for(String tag : tags.split(MetaData.TAGDELIMITER)) {
			Integer tagId = SimDao.getTagId(tag);
			System.out.println("tag: " + tag + " , tagId: " + tagId);
			if(tagId == null) {
				tagId = 0;
				do 
					tagId = MetaDataIO.generateId(4);
				while(SimDao.doesTagIdExist(tagId));
				System.out.println("generated tagId: " + tagId);
				SimDao.storeTag(tagId, tag);
			} 
			SimDao.storeSimTag(tagId, simId);
		}
		// Delete files after use
		files.forEach((key, file) -> file.delete());
		stateFiles.forEach((key, file) -> file.delete());
		
		return Response.ok("Files uploaded successfully").build();
	}
	
	//Redirect all requests to /rest/simulations/id/{id}
	@Path("id/{simulation}")
	public SimulationResource getSimulation(@PathParam("simulation") int id) {
		return new SimulationResource(uriInfo, request, id);
	}
}
