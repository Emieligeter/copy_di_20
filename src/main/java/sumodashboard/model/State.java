package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class State {
    private int md_key;
    private String data;
    private double timestep;
    private VehicleState vehicleState;
    private RouteState routeState;
    private LaneState laneState;

    public State() {

    }

    public State(int md_key, String data, double timestep, VehicleState vehicleState, RouteState routeState, LaneState laneState) {
        super();

        this.md_key = md_key;
        this.data = data;
        this.timestep = timestep;
        this.vehicleState = vehicleState;
        this.routeState = routeState;
        this.laneState = laneState;
    }

    public int getMd_key() {
        return md_key;
    }

    public void setMd_key(int md_key) {
        this.md_key = md_key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getTimestep() {
        return timestep;
    }

    public void setTimestep(double timestep) {
        this.timestep = timestep;
    }

    public VehicleState getVehicleState() {
        return vehicleState;
    }

    public void setVehicleState(VehicleState vehicleState) {
        this.vehicleState = vehicleState;
    }

    public RouteState getRouteState() {
        return routeState;
    }

    public void setRouteState(RouteState routeState) {
        this.routeState = routeState;
    }

    public LaneState getLaneState() {
        return laneState;
    }

    public void setLaneState(LaneState laneState) {
        this.laneState = laneState;
    }
}
