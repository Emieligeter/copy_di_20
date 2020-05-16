package sumodashboard.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Vehicle {
    private String ID;
    private double depart;
    private Route route;

    public Vehicle() {

    }

    public Vehicle(String ID, String routeXmlID, double depart, int type_id, Route route) {
        this.ID = ID;
        this.depart = depart;
        this.route = route;
    }

    public String getID() {
        return ID;
    }

    @XmlAttribute
    public void setID(String ID) {
        this.ID = ID;
    }

    public double getDepart() {
        return depart;
    }

    @XmlAttribute
    public void setDepart(double depart) {
        this.depart = depart;
    }

    public Route getRoute() {
        return route;
    }

    @XmlElement
    public void setRoute(Route route) {
        this.route = route;
    }
}
