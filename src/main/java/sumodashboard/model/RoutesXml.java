package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class RoutesXml {
    private int ID;
    private ArrayList<Vehicle> vehicles;
    private Routes routes;

    public RoutesXml() {

    }

    public RoutesXml(int ID, ArrayList<Vehicle> vehicles, Routes routes) {
        this.ID = ID;
        this.vehicles = vehicles;
        this.routes = routes;
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

    public Routes getRoutes() {
        return routes;
    }

    public void setRoutes(Routes routes) {
        this.routes = routes;
    }
}
