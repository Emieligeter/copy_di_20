package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class LaneState {
    private Lane lane;
    private int ID;
    private ArrayList<Vehicle> vehicles;

    public LaneState() {

    }

    public LaneState(Lane lane, int ID, ArrayList<Vehicle> vehicles) {
        this.lane = lane;
        this.ID = ID;
        this.vehicles = vehicles;
    }

    public Lane getLane() {
        return lane;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
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
