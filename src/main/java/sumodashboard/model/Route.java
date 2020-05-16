package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class Route {
    private String ID;
    private ArrayList<Edge> edges;

    public Route() {

    }

    public Route(String ID, ArrayList<Edge> edges) {
        this.ID = ID;
        this.edges = edges;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }
}
