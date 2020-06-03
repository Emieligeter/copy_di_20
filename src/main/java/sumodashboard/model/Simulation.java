package sumodashboard.model;


import java.io.File;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

import org.junit.platform.commons.util.StringUtils;

@XmlRootElement
public class Simulation {
	private int ID;
	private String name;
	private Date date;
	private String description;
	private Configuration configuration;
	private ArrayList<State> states;
	private ArrayList<String> tags;
	
	public Simulation() {
		
	}
	
	public Simulation(int ID, String name, Date date, String description, Configuration configuration, ArrayList<State> states, ArrayList<String> tags) {
		super();
		this.ID = ID;
		this.name = name;
		this.date = date;
		this.description = description;
		this.configuration = configuration;
		this.states = states;
		this.tags = tags;
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
}