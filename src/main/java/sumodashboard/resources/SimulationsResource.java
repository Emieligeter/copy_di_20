package sumodashboard.resources;

import java.io.File;
import java.io.InputStream;
import java.net.http.HttpHeaders;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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
	@Context
	ContainerRequestContext requestContext;
	
	//Get metadata of all simulations
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSimulations() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.FORBIDDEN).build();
		
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
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.FORBIDDEN).build();

		//Read the inputstream and make a DTO object
		SumoFilesDTO dto = FileReadService.readInputStream(inputStream, bodyPart);
		inputStream.close();

		HashMap<String, File> files = dto.getFiles();
		TreeMap<Integer, File> stateFiles = dto.getStateFiles();
		MetaData meta = MetaDataIO.parseMetadata(files.get("metadata.txt")); //parse metadata into object
		SimulationDao simDao = SimulationDao.instance;
		
		//Generate a random id, if it exists generate a new one
		int simId = 0;
		do {
			simId = MetaDataIO.generateId(5);
		} while(simDao.doesSimIdExist(simId)) ;
		 
		//Store a simulation in 'simulation' table
		simDao.storeSimulation(
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
			simDao.storeState(simId, timeStamp, file);
		}
		
		//Check if tags exists, if not, create new one. Then add it to 'simulation_tag' table
		String tags = meta.getTags();
		MetaDataIO.addTagsToSimulation(simId, tags);
		
		// Delete files after use
		files.forEach((key, file) -> file.delete());
		stateFiles.forEach((key, file) -> file.delete());
		
		return Response.ok("Files uploaded successfully").build();
	}
	
	//Redirect all requests to /rest/simulations/id/{id}
	@Path("id/{simulation}")
	public SimulationResource getSimulation(@PathParam("simulation") int id) {
		return new SimulationResource(uriInfo, request, requestContext, id);
	}
}
