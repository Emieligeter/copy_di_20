package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class Edge {
    private String edge;
    private ArrayList<Lane> lanes;
    private Junction fromJunction;
    private Junction toJunction;

    public Edge() {

    }

    public Edge(String edge, ArrayList<Lane> lanes, Junction fromJunction, Junction toJunction) {
        this.edge = edge;
        this.lanes = lanes;
        this.fromJunction = fromJunction;
        this.toJunction = toJunction;
    }

    public String getEdge() {
        return edge;
    }

    public void setEdge(String edge) {
        this.edge = edge;
    }

    public ArrayList<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(ArrayList<Lane> lanes) {
        this.lanes = lanes;
    }

    public Junction getFromJunction() {
        return fromJunction;
    }

    public void setFromJunction(Junction fromJunction) {
        this.fromJunction = fromJunction;
    }

    public Junction getToJunction() {
        return toJunction;
    }

    public void setToJunction(Junction toJunction) {
        this.toJunction = toJunction;
    }
}
