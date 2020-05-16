package sumodashboard.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name="snapshot")
public class State {
    private int ID;
    private double time;
    private String version;
    private ArrayList<Route> routeStates;
    private Delay delay;
    private ArrayList<VehicleType> vehicleTypes;
    private ArrayList<VehicleState> vehicleStates;
    private ArrayList<LaneState> laneStates;

    public State() {

    }

    public State(int ID, double time, String version, ArrayList<Route> routeStates, ArrayList<VehicleType> vehicleTypes, ArrayList<VehicleState> vehicleStates, ArrayList<LaneState> laneStates) {
        this.ID = ID;
        this.time = time;
        this.version = version;
        this.routeStates = routeStates;
        this.vehicleTypes = vehicleTypes;
        this.vehicleStates = vehicleStates;
        this.laneStates = laneStates;
    }

    public int getID() {
        return ID;
    }

    @XmlAttribute
    public void setID(int ID) {
        this.ID = ID;
    }

    public double getTime() {
        return time;
    }

    @XmlAttribute
    public void setTime(double time) {
        this.time = time;
    }

    public String getVersion() {
        return version;
    }

    @XmlAttribute
    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<Route> getRouteStates() {
        return routeStates;
    }

    public void setRouteStates(ArrayList<Route> routeStates) {
        this.routeStates = routeStates;
    }

    public ArrayList<VehicleType> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(ArrayList<VehicleType> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    public ArrayList<VehicleState> getVehicleStates() {
        return vehicleStates;
    }

    public void setVehicleStates(ArrayList<VehicleState> vehicleStates) {
        this.vehicleStates = vehicleStates;
    }

    public ArrayList<LaneState> getLaneStates() {
        return laneStates;
    }

    public void setLaneStates(ArrayList<LaneState> laneStates) {
        this.laneStates = laneStates;
    }
}
