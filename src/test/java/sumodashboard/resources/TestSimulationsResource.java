package sumodashboard.resources;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import sumodashboard.model.MetaData;

public class TestSimulationsResource {
	
	@Test
	public void testGetSimulations() throws Exception {
		Response response = new SimulationsResource().getSimulations();
		Assertions.assertEquals(200, response.getStatus());
		
		Object body = response.getEntity();
		Assertions.assertNotNull(body);
		Assertions.assertTrue(body instanceof ArrayList<?>);
		
		@SuppressWarnings("unchecked")
		ArrayList<MetaData> simulations = (ArrayList<MetaData>) body;
		Assertions.assertNotNull(simulations.get(0).getID());
	}
}
