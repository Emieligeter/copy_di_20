package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Simulation {
	private int ID;
	private String name;
	private String date;
	private String description;
	private String researcher;
	private String tags;
	
	private String net;
	private String routes;
	private String config;
	
	public Simulation() {
		
	}
	
	public Simulation(int ID, String name, String date, String description, String researcher, String tags, String net, String routes, String config) {
		super();
		this.ID = ID;
		this.name = name;
		this.date = date;
		this.description = description;
		this.researcher = researcher;
		this.tags = tags;
		this.net = net;
		this.routes = routes;
		this.config = config;
	}

	public int getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}

	public String getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public void setID(int ID) {
		this.ID = ID;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	public String getResearcher() {
		return researcher;
	}
	
	public void setResearcher(String researcher) {
		this.researcher = researcher;
	}
	
	public String getTags() {
		return tags;
	}
	
	public void setTags(String tags) {
		this.tags = tags;
	}
}