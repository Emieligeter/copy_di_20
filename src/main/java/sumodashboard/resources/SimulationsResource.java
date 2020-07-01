package sumodashboard.resources;

import java.io.File;
import java.io.InputStream;
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

/**
 * Class responsible for all requests to /rest/simulations
 */
@Path("/simulations")
public class SimulationsResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	ContainerRequestContext requestContext;
	
	//Used to disable storage when testing
	private boolean storeData = true;

	public SimulationsResource() {
	}
	
	/**
	 * Constructor used for testing purposes
	 */
	public SimulationsResource(UriInfo uriInfo, Request request, ContainerRequestContext requestContext) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.requestContext = requestContext;
	}
	
	/**
	 * Get metadata of all simulations
	 * @return Response
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSimulations() {
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();
		
		try {			
			List<MetaData> simulations = SimulationDao.getSimulations();
			
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
		if (!AuthenticationResource.isAuthorized(requestContext)) return Response.status(Response.Status.UNAUTHORIZED).build();

		//Read the inputstream and make a DTO object
		SumoFilesDTO dto = FileReadService.readInputStream(inputStream, bodyPart);
		inputStream.close();

		HashMap<String, File> files = dto.getFiles();
		TreeMap<Integer, File> stateFiles = dto.getStateFiles();
		MetaData meta = MetaDataIO.parseMetadata(files.get("metadata.txt")); //parse metadata into object
		
		//Generate a random id, if it exists generate a new one
		int simId = 0;
		do {
			simId = MetaDataIO.generateId(5);
		} while(SimulationDao.doesSimIdExist(simId)) ;
		 
		//Store a simulation in 'simulation' table
		SimulationDao.storeSimulation(
				simId, meta.getName(), 
				meta.getDescription(), 
				meta.getDate(),
				files.get("net.net.xml"), 
				files.get("routes.rou.xml"), 
				files.get("simulation.sumocfg"),
				storeData);

		//Store all state files in 'states' table
		for (Map.Entry<Integer, File> sf : stateFiles.entrySet()) {
			Integer timeStamp = sf.getKey();
			File file = sf.getValue();
			if(storeData)SimulationDao.storeState(simId, timeStamp, file, storeData);
		}
		
		//Check if tags exists, if not, create new one. Then add it to 'simulation_tag' table
		String tags = meta.getTags();
		if(storeData)MetaDataIO.addTagsToSimulation(simId, tags);
		
		// Delete files after use
		files.forEach((key, file) -> file.delete());
		stateFiles.forEach((key, file) -> file.delete());
		
		return Response.ok("Files uploaded successfully").build();
	}
	
	/**
	 * Redirect all requests to /rest/simulations/id/{id}
	 * @param id simulation id given in the url
	 * @return instance of simulationResource
	 */
	@Path("id/{simulation}")
	public SimulationResource getSimulation(@PathParam("simulation") int id) {
		return new SimulationResource(uriInfo, request, requestContext, id);
	}
	
	/**
	 * Disable / enable storing of data during methods. Used for testing purposes.
	 * @param storeData
	 */
	public void setStoreData(boolean storeData) {
		this.storeData = storeData;
	}
}
