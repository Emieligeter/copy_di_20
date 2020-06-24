package sumodashboard.resources;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import sumodashboard.model.MetaData;
import sumodashboard.model.Simulation;

public class TestSimulationsResource {
	AuthenticationResource auth = new AuthenticationResource();
	@Test
	public void testGetSimulations() throws Exception {
		Client client = ClientBuilder.newClient();
		String token ="";// = auth.createToken("user1" , 8000);
		NewCookie cookie = new NewCookie("session-id",token , "/", null, null, 300, false, false);
		Response response =  client.target("http://localhost:8080/sumo-dashboard/rest/simulations")
				.request(MediaType.APPLICATION_JSON)
				.cookie(cookie)
				.get();
		
		//Response response = new SimulationsResource().getSimulations();
		Assertions.assertEquals(200, response.getStatus());
		
		Object body = response.getEntity();
		Assertions.assertNotNull(body);
		Assertions.assertTrue(body instanceof ArrayList<?>);
		
		@SuppressWarnings("unchecked")
		ArrayList<MetaData> simulations = (ArrayList<MetaData>) body;
		Assertions.assertNotNull(simulations.get(0).getID());
	}
	
	@Test
	public void testUploadSimulations() throws Exception {
		File zipFile;
		Path path = Paths.get("/src/test/java/sumodashboard/resources/sumoFiles/sumo");
		System.out.println(path.toString());
	}
}
