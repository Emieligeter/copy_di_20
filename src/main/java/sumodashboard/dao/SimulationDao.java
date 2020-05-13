package sumodashboard.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sumodashboard.model.Simulation;

public enum SimulationDao {
	instance;
	
	private Map<String, Simulation> contentProvider = new HashMap<String, Simulation>();
	
	private SimulationDao() {
		Simulation sim = new Simulation("Test", new Date(), "An empty sim", null, null, null);
		contentProvider.put("Test", sim);
		Simulation sim2 = new Simulation("Test2", new Date(), "Another empty sim", null, null, null);
		contentProvider.put("Test2", sim2);
	}
	
	public Map<String, Simulation> getModel() {
		return contentProvider;
	}
}
