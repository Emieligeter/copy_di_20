package sumodashboard.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name="snapshot")
public class State {
    private double time;
    private String version;
    private ArrayList<RouteState> routeStates;
    private Delay delay;
    private ArrayList<VehicleType> vehicleTypes;
    private ArrayList<VehicleState> vehicleStates;
    private ArrayList<LaneState> laneStates;

    public State() {

    }

    public State(double time, String version, Delay delay, ArrayList<RouteState> routeStates, ArrayList<VehicleType> vehicleTypes, ArrayList<VehicleState> vehicleStates, ArrayList<LaneState> laneStates) {
        this.time = time;
        this.version = version;
        this.delay = delay;
        this.routeStates = routeStates;
        this.vehicleTypes = vehicleTypes;
        this.vehicleStates = vehicleStates;
        this.laneStates = laneStates;
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


    public Delay getDelay() {
        return delay;
    }

    @XmlElement
    public void setDelay(Delay delay) {
        this.delay = delay;
    }

    public ArrayList<RouteState> getRouteStates() {
        return routeStates;
    }

    @XmlElement(name="route", type=RouteState.class)
    public void setRouteStates(ArrayList<RouteState> routeStates) {
        this.routeStates = routeStates;
    }

    public ArrayList<VehicleType> getVehicleTypes() {
        return vehicleTypes;
    }

    @XmlElement(name="vType", type=VehicleType.class)
    public void setVehicleTypes(ArrayList<VehicleType> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    public ArrayList<VehicleState> getVehicleStates() {
        return vehicleStates;
    }

    @XmlElement(name="vehicle", type=VehicleState.class)
    public void setVehicleStates(ArrayList<VehicleState> vehicleStates) {
        this.vehicleStates = vehicleStates;
    }

    public ArrayList<LaneState> getLaneStates() {
        return laneStates;
    }

    @XmlElement(name="lane", type=LaneState.class)
    public void setLaneStates(ArrayList<LaneState> laneStates) {
        this.laneStates = laneStates;
    }
}
