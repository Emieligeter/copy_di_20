package sumodashboard.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VehicleState extends Vehicle {
    private String type;
    private double speedFactor;
    private String state;
    private double pos;
    private double speed;
    private double posLat;

    public VehicleState() {

    }

    public VehicleState(String ID, String routeXmlID, int depart, int type_id, Route route, String type, double speedFactor, String state, double pos, double speed, double posLat) {
        super(ID, routeXmlID, depart, type_id, route);
        this.type = type;
        this.speedFactor = speedFactor;
        this.state = state;
        this.pos = pos;
        this.speed = speed;
        this.posLat = posLat;
    }

    public String getType() {
        return type;
    }

    @XmlAttribute
    public void setType(String type) {
        this.type = type;
    }

    public double getSpeedFactor() {
        return speedFactor;
    }

    @XmlAttribute
    public void setSpeedFactor(double speedFactor) {
        this.speedFactor = speedFactor;
    }

    public String getState() {
        return state;
    }

    @XmlAttribute
    public void setState(String state) {
        this.state = state;
    }

    public double getPos() {
        return pos;
    }

    @XmlAttribute
    public void setPos(double pos) {
        this.pos = pos;
    }

    public double getSpeed() {
        return speed;
    }

    @XmlAttribute
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getPosLat() {
        return posLat;
    }

    @XmlAttribute
    public void setPosLat(double posLat) {
        this.posLat = posLat;
    }
}
