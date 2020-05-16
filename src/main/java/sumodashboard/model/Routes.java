package sumodashboard.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class Routes {
    private ArrayList<Vehicle> vehicles;

    public Routes() {

    }

    public Routes(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }
    
    @XmlElement(name = "vehicle", type = Vehicle.class)
    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
