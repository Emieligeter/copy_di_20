package sumodashboard.model;


import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Simulation {
	private String ID;
	private Date date;
	private String description;
	private Configuration configuration;
	private ArrayList<State> states;
	private ArrayList<String> tags;
	
	public Simulation() {
		
	}
	
	public Simulation(String ID, Date date, String description, Configuration configuration, ArrayList<State> states, ArrayList<String> tags) {
		super();
		
		this.ID = ID;
		this.date = date;
		this.description = description;
		this.configuration = configuration;
		this.states = states;
		this.tags = tags;
	}

	public String getID() {
		return ID;
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

	public void setID(String ID) {
		this.ID = ID;
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
}