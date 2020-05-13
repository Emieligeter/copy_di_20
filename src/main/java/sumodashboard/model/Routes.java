package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class Routes {
    private String ID;
    private ArrayList<Edge> edges;
    private ArrayList<Connection> connections;
    private ArrayList<Junction> junctions;

    public Routes() {

    }

    public Routes(String ID, ArrayList<Edge> edges, ArrayList<Connection> connections, ArrayList<Junction> junctions) {
        this.ID = ID;
        this.edges = edges;
        this.connections = connections;
        this.junctions = junctions;
    }

    public String getRoutesID() {
        return ID;
    }

    public void setRoutesID(String ID) {
        this.ID = ID;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }

    public ArrayList<Junction> getJunctions() {
        return junctions;
    }

    public void setJunctions(ArrayList<Junction> junctions) {
        this.junctions = junctions;
    }
}
