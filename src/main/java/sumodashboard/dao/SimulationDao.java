package sumodashboard.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sumodashboard.model.Simulation;

import javax.xml.bind.JAXBException;

public enum SimulationDao {
	instance;
	
	private Map<String, Simulation> contentProvider = new HashMap<String, Simulation>();
	
	private SimulationDao() {
		Simulation sim = null;
		try {
			sim = new Simulation("Test", new Date(), "Example 2",
					ParseXML.parseConfigFromFiles("C:\\Users\\Reijer\\Downloads\\SUMO example 2\\net.net.xml", "C:\\Users\\Reijer\\Downloads\\SUMO example 2\\simulation.sumocfg", "C:\\Users\\Reijer\\Downloads\\SUMO example 2\\routes.rou.xml"),
					null, null);
			contentProvider.put("Test", sim);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		Simulation sim2 = new Simulation("Test2", new Date(), "Another empty sim", null, null, null);
		contentProvider.put("Test2", sim2);
	}
	
	public Map<String, Simulation> getModel() {
		return contentProvider;
	}
}
