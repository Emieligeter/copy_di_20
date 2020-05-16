package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Vehicle {
    private int ID;
    private int depart;
    private Route route;

    public Vehicle() {

    }

    public Vehicle(int ID, String routeXmlID, int depart, int type_id, Route route) {
        this.ID = ID;
        this.depart = depart;
        this.route = route;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getDepart() {
        return depart;
    }

    public void setDepart(int depart) {
        this.depart = depart;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
