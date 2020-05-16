package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VehicleState extends Vehicle {
    private VehicleType vehicleType;
    private double speedFactor;
    private String state;
    private double pos;
    private double speed;
    private double posLat;

    public VehicleState() {

    }

    public VehicleState(int ID, String routeXmlID, int depart, int type_id, Route route, VehicleType vehicleType, double speedFactor, String state, double pos, double speed, double posLat) {
        super(ID, routeXmlID, depart, type_id, route);
        this.vehicleType = vehicleType;
        this.speedFactor = speedFactor;
        this.state = state;
        this.pos = pos;
        this.speed = speed;
        this.posLat = posLat;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
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

    public double getPosLat() {
        return posLat;
    }

    public void setPosLat(double posLat) {
        this.posLat = posLat;
    }
}
