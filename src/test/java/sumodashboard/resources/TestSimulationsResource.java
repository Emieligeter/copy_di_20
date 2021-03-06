package sumodashboard.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import sumodashboard.model.MetaData;

public class TestSimulationsResource {
	private static final String AUTHHEADER = "Bearer ZVXTyfmKXb7FxngTEAq2DHVmXZCxecJWTQLDsDnEce3dzhVK";
	int firstSimulationId;
	static UriInfo uriInfo;
	static Request request;
	static ContainerRequestContext requestContext;
	
	@BeforeAll
	public static void setUp() {
		//Pretend to send request with ?vehicle=v5 and ?lane=e5 in URI
		uriInfo = Mockito.mock(UriInfo.class);
		request = Mockito.mock(Request.class);
		requestContext = Mockito.mock(ContainerRequestContext.class);
		Mockito.when(requestContext.getHeaderString("Authorization")).thenReturn(AUTHHEADER);
	}
	
	@Test
	public void testGetSimulations() throws Exception {		
		Response response = new SimulationsResource(uriInfo,request, requestContext).getSimulations();
		Assertions.assertEquals(200, response.getStatus());
		
		Object body = response.getEntity();
		Assertions.assertNotNull(body);
		Assertions.assertTrue(body instanceof ArrayList<?>);
		
		@SuppressWarnings("unchecked")
		ArrayList<MetaData> simulations = (ArrayList<MetaData>) body;
		Assertions.assertNotNull(simulations.get(0).getID());
	}
	
	@Test
	public void testUploadSimulationsZipFile() throws Exception {
		
		//Test zip file

		URL url = getClass().getClassLoader().getResource("sumo file 1.zip");
		File zipFile = Paths.get(url.toURI()).toFile();
	
		InputStream zipStream = new FileInputStream(zipFile);
		
		FormDataMultiPart multiPart = new FormDataMultiPart();
		FormDataBodyPart filePart = new FormDataBodyPart();
		FormDataContentDisposition content =   FormDataContentDisposition.name("test").fileName(zipFile.getName()).build();

		filePart.setContentDisposition(content);
		multiPart.bodyPart(filePart);
		FormDataBodyPart bodyPart = (new FormDataBodyPart());
		bodyPart.setParent(multiPart);
		SimulationsResource simRes = new SimulationsResource(uriInfo, request, requestContext);
		simRes.setStoreData(false);
		Response res = simRes.uploadFiles(zipStream, bodyPart );
		System.out.println(res.getStatus());
		simRes.setStoreData(true);
	}
}
