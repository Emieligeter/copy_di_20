package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class Net {
    private int ID;
    private String version;
    private int junctionCornerDetail;
    private double limitTurnSpeed;
    private Location location;
    private ArrayList<Edge> edges;
    private ArrayList<Junction> junctions;
    private ArrayList<Connection> connections;

    public Net() {

    }

    public Net(int ID, String version, int junctionCornerDetail, double limitTurnSpeed, Location location, ArrayList<Edge> edges, ArrayList<Junction> junctions, ArrayList<Connection> connections) {
        this.ID = ID;
        this.version = version;
        this.junctionCornerDetail = junctionCornerDetail;
        this.limitTurnSpeed = limitTurnSpeed;
        this.location = location;
        this.edges = edges;
        this.junctions = junctions;
        this.connections = connections;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getJunctionCornerDetail() {
        return junctionCornerDetail;
    }

    public void setJunctionCornerDetail(int junctionCornerDetail) {
        this.junctionCornerDetail = junctionCornerDetail;
    }

    public double getLimitTurnSpeed() {
        return limitTurnSpeed;
    }

    public void setLimitTurnSpeed(double limitTurnSpeed) {
        this.limitTurnSpeed = limitTurnSpeed;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public ArrayList<Junction> getJunctions() {
        return junctions;
    }

    public void setJunctions(ArrayList<Junction> junctions) {
        this.junctions = junctions;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }
}
