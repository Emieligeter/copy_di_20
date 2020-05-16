package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class State {
    private int ID;
    private double timestep;
    private ArrayList<Route> routeStates;
    private ArrayList<VehicleType> vehicleTypes;
    private ArrayList<VehicleState> vehicleStates;
    private ArrayList<LaneState> laneStates;

    public State() {

    }

    public State(int ID, double timestep, ArrayList<Route> routeStates, ArrayList<VehicleType> vehicleTypes, ArrayList<VehicleState> vehicleStates, ArrayList<LaneState> laneStates) {
        this.ID = ID;
        this.timestep = timestep;
        this.routeStates = routeStates;
        this.vehicleTypes = vehicleTypes;
        this.vehicleStates = vehicleStates;
        this.laneStates = laneStates;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getTimestep() {
        return timestep;
    }

    public void setTimestep(double timestep) {
        this.timestep = timestep;
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
