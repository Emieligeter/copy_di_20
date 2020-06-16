package sumodashboard.model;

import java.util.ArrayList;
import java.util.Date;

public class MetaData {
	
	private int ID;
	private String name;
	private String date;
	private String description;
	private String researcher;
	private ArrayList<String> tags;
	
	public MetaData() {
		
	}
	
	public MetaData(int ID, String name, String date, String description, String researcher, ArrayList<String> tags) {
		super();
		this.ID = ID;
		this.name = name;
		this.date = date;
		this.description = description;
		this.researcher = researcher;
		this.tags = tags;
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
	
	public String getResearcher() {
		return researcher;
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

	public void setDate(String date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setResearcher(String researcher) {
		this.researcher = researcher;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
}
