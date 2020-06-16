package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MetaData {
	public static final String TAGDELIMITER = ", ";
	
	private int ID;
	private String name;
	private String date;
	private String description;
	private String researcher;
	private String tags;
	
	public MetaData() {
		
	}
	
	public MetaData(int ID, String name, String date, String description, String researcher, String tags) {
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

	public String getTags() {
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

	public void setTags(String tags) {
		this.tags = tags;
	}
}
