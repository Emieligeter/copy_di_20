package sumodashboard.model;


import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Simulation {
	private int ID;
	private String name;
	private Date date;
	private String description;
	private Configuration configuration;
	private ArrayList<State> states;
	private ArrayList<String> tags;
	
	private String net;
	private String routes;
	private String config;
	
	public Simulation() {
		
	}
	
	public Simulation(int ID, String name, Date date, String description) {
		super();
		this.ID = ID;
		this.name = name;
		this.date = date;
		this.description = description;
	}
	
	public Simulation(int ID, String name, Date date, String description, String net, String routes, String config) {
		super();
		this.ID = ID;
		this.name = name;
		this.date = date;
		this.description = description;
		this.net = net;
		this.routes = routes;
		this.config = config;
	}
	
	public Simulation(String metadataPath, String netPath, String routesPath, String configPath) {
		
	}

	public int getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}

	public Date getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public ArrayList<State> getStates() {
		return states;
	}
	
	public ArrayList<String> getTags() {
		return tags;
	}

	public void setID(int ID) {
		this.ID = ID;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setStates(ArrayList<State> states) {
		this.states = states;
	}
	
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public String getNet() {
		return net;
	}

	public void setNet(String net) {
		this.net = net;
	}

	public String getRoutes() {
		return routes;
	}

	public void setRoutes(String routes) {
		this.routes = routes;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}
}