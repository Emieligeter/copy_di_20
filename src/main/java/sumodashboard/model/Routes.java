package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class Routes {
    private int ID;
    private ArrayList<Vehicle> vehicles;

    public Routes() {

    }

    public Routes(int ID, ArrayList<Vehicle> vehicles) {
        this.ID = ID;
        this.vehicles = vehicles;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
