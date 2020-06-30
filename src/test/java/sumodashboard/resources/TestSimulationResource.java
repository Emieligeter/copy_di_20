package sumodashboard.resources;

import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import sumodashboard.model.MetaData;
import sumodashboard.model.Simulation;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestSimulationResource {
	private static final String AUTHHEADER = "Bearer ZVXTyfmKXb7FxngTEAq2DHVmXZCxecJWTQLDsDnEce3dzhVK";
	int firstSimulationId;
	UriInfo uriInfo;
	Request request;
	ContainerRequestContext requestContext;
	
	@SuppressWarnings("unchecked")
	@BeforeAll
	public void setUp() {
		//Pretend to send request with ?vehicle=v5 and ?lane=e5 in URI
		uriInfo = Mockito.mock(UriInfo.class);
		request = Mockito.mock(Request.class);
		requestContext = Mockito.mock(ContainerRequestContext.class);
		Mockito.when(requestContext.getHeaderString("Authorization")).thenReturn(AUTHHEADER);
				
		//Get the ID of the first simulation
		ArrayList<MetaData> simulations = (ArrayList<MetaData>) new SimulationsResource(uriInfo, request, requestContext).getSimulations().getEntity();
		Assertions.assertNotNull(simulations);
		firstSimulationId = simulations.get(0).getID();
	}
	
	@Test
	public void testGetSimulation() {
		//Test with valid simulation id
		Response r1 = new SimulationResource(uriInfo, request, requestContext, firstSimulationId).getSimulation();
		Assertions.assertEquals(200, r1.getStatus());
		Assertions.assertTrue(r1.getEntity() instanceof Simulation);
		Assertions.assertEquals(firstSimulationId, ((Simulation) r1.getEntity()).getID());
		
		//Test with invalid simulation id
		Response r2 = new SimulationResource(uriInfo, request, requestContext, -5).getSimulation();
		Assertions.assertEquals(400, r2.getStatus());
	}
	
	@Test
	public void testUpdateSimulationMetadata() {
		SimulationResource simulationResource = new SimulationResource(uriInfo, request, requestContext, firstSimulationId); 
		//Get current metadata values
		Response r1 = simulationResource.getSimulation();
		Simulation initialState = (Simulation)r1.getEntity();
		Simulation newState = new Simulation(firstSimulationId, "xxx", "1980-08-08", "yyy", "zzz", "x, y, z", "{\"val\": \"x\"}", "{\"val\": \"y\"}", "{\"val\": \"z\"}");
		
		//update simulation correctly
		Response r2 = simulationResource.updateSimulationMetadata(newState);
		Assertions.assertEquals(200, r2.getStatus());
		
		//Check current simulation variables
		Response r3 = simulationResource.getSimulation();
		Assertions.assertEquals(newState, (Simulation) r3.getEntity());

		//update simulation with name override values
		Response r4 = simulationResource.updateSimulationMetadata(new Simulation(firstSimulationId, "xyz", null, null, null, null, null, null, null));
		Assertions.assertEquals(200, r4.getStatus());
		
		//Check current simulation variables
		Response r5 = simulationResource.getSimulation();
		newState.setName("xyz");
		Assertions.assertEquals(newState, (Simulation) r5.getEntity());
		
		//update metadata of invalid simulation id
		Response r6 = new SimulationResource(uriInfo, request, requestContext, -5).updateSimulationMetadata(newState);
		Assertions.assertEquals(400, r6.getStatus());
		
		//Restore old metadata
		simulationResource.updateSimulationMetadata(initialState);
	}
	
	@Test
	public void testDeleteSimulation() {
		Response wrongIdResponse = new SimulationResource(uriInfo, request, requestContext, -5).deleteSimulation();
		Assertions.assertEquals(400, wrongIdResponse.getStatus());
	}
	
	private void checkResponse(Response r, boolean IdExists) {
		if (IdExists) {
			Assertions.assertEquals(200, r.getStatus());
			Assertions.assertNotNull(r.getEntity());
		}
		else {
			Assertions.assertEquals(400, r.getStatus());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void checkAllStatistics(int simulationId, boolean IdExists) {
		SimulationResource simulationResource = new SimulationResource(uriInfo, request, requestContext, simulationId); 
		
		//Test methods without query parameters
		checkResponse(simulationResource.getAvgRouteLength(), IdExists);
		checkResponse(simulationResource.getAvgSpeed(), IdExists);
		checkResponse(simulationResource.getArrivedVehicles(), IdExists);
		checkResponse(simulationResource.getTransferredVehicles(), IdExists);
		checkResponse(simulationResource.getRunningVehicles(), IdExists);
		checkResponse(simulationResource.getEdgeAppearanceFrequencyInitialRoute(), IdExists);
		checkResponse(simulationResource.getInitRouteLengthPerVehicle(), IdExists);
		checkResponse(simulationResource.getSummaryStatistics(), IdExists);
		
		//Test list methods
		Response vehicleListResponse = simulationResource.getVehicleList();
		checkResponse(vehicleListResponse, IdExists);
		Response edgeListResponse = simulationResource.getEdgeList();
		checkResponse(edgeListResponse, IdExists);
		Response laneListResponse = simulationResource.getLaneList();
		checkResponse(laneListResponse, IdExists);
		
		if (IdExists) {
			//Generate correct query parameters
			MultivaluedMap<String, String> correctQueryParam = new MultivaluedHashMap<>();
			String firstVehicleId = ((ArrayList<String>) vehicleListResponse.getEntity()).get(0);
			String firstEdgeId = ((ArrayList<String>) edgeListResponse.getEntity()).get(0);
			String firstLaneId = ((ArrayList<String>) laneListResponse.getEntity()).get(0);
			correctQueryParam.put("vehicle", Arrays.asList(firstVehicleId));
			correctQueryParam.put("edge", Arrays.asList(firstEdgeId));
			correctQueryParam.put("lane", Arrays.asList(firstLaneId));
			Mockito.when(uriInfo.getQueryParameters()).thenReturn(correctQueryParam);	
		}
		else {
			Mockito.when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>());
		}
		
		//Test methods with query parameters
		checkResponse(simulationResource.getEdgeFrequency(), IdExists);
		checkResponse(simulationResource.getLaneTransitingVehicles(), IdExists);
		checkResponse(simulationResource.getVehicleRouteLength(), IdExists);
		checkResponse(simulationResource.getVehicleSpeed(), IdExists);
		checkResponse(simulationResource.getVehicleSpeedFactor(), IdExists);
	}
	
	@Test
	public void testAllStatistics() {
		checkAllStatistics(firstSimulationId, true);
	}
	
	@Test
	public void testAllStatisticsForWrongId() {
		checkAllStatistics(-5, false);
	}
	
	@Test
	public void testEmptyQueryParameter() {
		SimulationResource simulationResource = new SimulationResource(uriInfo, request, requestContext, firstSimulationId); 
		
		//Test methods with query parameters, but without giving any
		Mockito.when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>());
		checkResponse(simulationResource.getEdgeFrequency(), false);
		checkResponse(simulationResource.getLaneTransitingVehicles(), false);
		checkResponse(simulationResource.getVehicleRouteLength(), false);
		checkResponse(simulationResource.getVehicleSpeed(), false);
		checkResponse(simulationResource.getVehicleSpeedFactor(), false);
	}
}
