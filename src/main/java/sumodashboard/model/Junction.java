package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class Junction {
    private String ID;
    private String type;
    private double x;
    private double y;
    private ArrayList<Lane> lanes;
    private ArrayList<Double> shape;

    public Junction() {

    }

    public Junction(String ID, String type, double x, double y, ArrayList<Lane> lanes, ArrayList<Double> shape) {
        this.ID = ID;
        this.type = type;
        this.x = x;
        this.y = y;
        this.lanes = lanes;
        this.shape = shape;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public ArrayList<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(ArrayList<Lane> lanes) {
        this.lanes = lanes;
    }

    public ArrayList<Double> getShape() {
        return shape;
    }

    public void setShape(ArrayList<Double> shape) {
        this.shape = shape;
    }
}
