package nl.utwente.di.bikedealer.model;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Bike {
	private String ID;
	private String ownerName;
	private String colour;
	private String gender;
	
	public Bike() {
		
	}
	
	public Bike(String ID, String ownerName, String colour, String gender) {
		super();
		
		this.ID = ID;
		this.ownerName = ownerName;
		this.colour = colour;
		this.gender = gender;
	}
	
	public String getID() {
		return ID;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public String getColour() {
		return colour;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setID(String ID) {
		this.ID = ID;		
	}
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public void setColour(String colour) {
		this.colour = colour;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}

  
} 