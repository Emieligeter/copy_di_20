package sumodashboard.resources;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import sumodashboard.dao.SimulationDao;
import sumodashboard.model.Simulation;
import sumodashboard.services.FileReadService;

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

		for (Simulation bike : SimulationDao.instance.getModel().values()) {
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

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadZippedFile(@FormDataParam("uploadFile") InputStream inputStream,
			@FormDataParam("uploadFile") FormDataBodyPart bodyPart) throws Exception {
		List<String> fileList = new ArrayList<String>();
		for (BodyPart part : bodyPart.getParent().getBodyParts()) {
			ContentDisposition meta = part.getContentDisposition();
			if(meta.getFileName().length() > 3) fileList.add('\n' + meta.getFileName());
		}
		FileReadService fileReadService = new FileReadService();
		fileReadService.readInputStream(inputStream, bodyPart, fileList);
		return Response.ok("File uploaded successfully : " + fileList.toString()).build();
	}
	
	@Path("id/{simulation}")
	public SimulationResource getSimulation(@PathParam("simulation") String id) {
		return new SimulationResource(uriInfo, request, id);
	}
}
