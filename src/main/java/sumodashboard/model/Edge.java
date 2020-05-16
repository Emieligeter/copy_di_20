package sumodashboard.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

@XmlRootElement(name = "edge")
public class Edge {
    private String ID;
    private String function;
    private ArrayList<Lane> lanes;
    private String fromJunction;
    private String toJunction;
    private int priority;

    public Edge() {

    }

    public Edge(String ID, String function, ArrayList<Lane> lanes) {
        this.ID = ID;
        this.function = function;
        this.lanes = lanes;
    }

    public Edge(String ID, ArrayList<Lane> lanes, String fromJunction, String toJunction, int priority) {
        this.ID = ID;
        this.lanes = lanes;
        this.fromJunction = fromJunction;
        this.toJunction = toJunction;
        this.priority = priority;
    }

    public String getID() {
        return ID;
    }

    @XmlAttribute
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFunction() {
        return function;
    }

    @XmlAttribute
    public void setFunction(String function) {
        this.function = function;
    }

    public ArrayList<Lane> getLanes() {
        return lanes;
    }

    @XmlElement(name = "lane", type = Lane.class)
    public void setLanes(ArrayList<Lane> lanes) {
        this.lanes = lanes;
    }

    public String getFromJunction() {
        return fromJunction;
    }

    @XmlAttribute
    public void setFromJunction(String fromJunction) {
        this.fromJunction = fromJunction;
    }

    public String getToJunction() {
        return toJunction;
    }

    @XmlAttribute
    public void setToJunction(String toJunction) {
        this.toJunction = toJunction;
    }

    public int getPriority() {
        return priority;
    }

    @XmlAttribute
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
