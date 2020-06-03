package sumodashboard.model;

import java.util.ArrayList;
import java.util.Date;

public class MetaData {
	
	private String ID;
	private String name;
	private Date date;
	private String description;
	private ArrayList<String> tags;
	
	public String getID() {
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

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setID(String ID) {
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

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
}
