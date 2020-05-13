package nl.utwente.di.bikedealer.dao;

import java.util.HashMap;
import java.util.Map;

import nl.utwente.di.bikedealer.model.Bike;

public enum BikeDao {
	instance;
	
	private Map<String, Bike> contentProvider = new HashMap<String, Bike>();
	
	private BikeDao() {
		Bike bike = new Bike("1", "Piet", "green", "male");
		contentProvider.put("1", bike);
	}
	
	public Map<String, Bike> getModel() {
		return contentProvider;
	}
}
