package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Vehicle {
    private int ID;
    private int depart;
    private int type_id;
    private VehicleType vehicleType;
    private Routes routes;

    public Vehicle() {

    }

    public Vehicle(int ID, String routeXmlID, int depart, int type_id, VehicleType vehicleType, Routes routes) {
        this.ID = ID;
        this.depart = depart;
        this.type_id = type_id;
        this.vehicleType = vehicleType;
        this.routes = routes;
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

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public sumodashboard.model.Routes getRoutes() {
        return routes;
    }

    public void setRoutes(sumodashboard.model.Routes routes) {
        this.routes = routes;
    }
}
