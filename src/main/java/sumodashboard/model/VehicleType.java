package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VehicleType {
    private int ID;
    private String type;

    public VehicleType() {

    }

    public VehicleType(int ID, String type) {
        this.ID = ID;
        this.type = type;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
