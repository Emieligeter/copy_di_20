package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VehicleState {
    private Vehicle vehicle;
    private double speedFactor;
    private String state;
    private double pos;
    private double speed;
    private double postlat;

    public VehicleState() {

    }

    public VehicleState(Vehicle vehicle, double speedFactor, String state, double pos, double speed, double postlat) {
        this.vehicle = vehicle;
        this.speedFactor = speedFactor;
        this.state = state;
        this.pos = pos;
        this.speed = speed;
        this.postlat = postlat;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public double getSpeedFactor() {
        return speedFactor;
    }

    public void setSpeedFactor(double speedFactor) {
        this.speedFactor = speedFactor;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getPos() {
        return pos;
    }

    public void setPos(double pos) {
        this.pos = pos;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getPostlat() {
        return postlat;
    }

    public void setPostlat(double postlat) {
        this.postlat = postlat;
    }
}
