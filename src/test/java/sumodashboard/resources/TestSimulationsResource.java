package sumodashboard.resources;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sumodashboard.model.MetaData;

public class TestSimulationsResource {
	public SimulationsResource sr;
	
	@BeforeEach
	public void setUp() {
		this.sr = new SimulationsResource();
	}
	
	@Test
	public void testGetSimulations() throws Exception {
		Response response = sr.getSimulations();
		Assertions.assertEquals(response.getStatus(), 200);
		
		Object body = response.getEntity();
		Assertions.assertNotNull(body);
		Assertions.assertTrue(body instanceof ArrayList<?>);
		
		@SuppressWarnings("unchecked")
		ArrayList<MetaData> simulations = (ArrayList<MetaData>) body;
		Assertions.assertNotNull(simulations.get(0).getID());
	}
}
