package sumodashboard.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class LaneState {
    private String ID;
    private Vehicles vehicles;

    public LaneState() {

    }

    public LaneState(String ID, Vehicles vehicles) {
        this.ID = ID;
        this.vehicles = vehicles;
    }

    public String getID() {
        return ID;
    }

    @XmlAttribute
    public void setID(String ID) {
        this.ID = ID;
    }

    public Vehicles getVehicles() {
        return vehicles;
    }

    @XmlElement
    public void setVehicles(Vehicles vehicles) {
        this.vehicles = vehicles;
    }
}
