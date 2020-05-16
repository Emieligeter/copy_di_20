package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class Lane {
    private String ID;
    private int index;
    private double speed;
    private double length;
    private ArrayList<Integer> shape;

    public Lane() {

    }

    public Lane(String ID, double speed, double length, ArrayList<Integer> shape) {
        this.ID = ID;
        this.speed = speed;
        this.length = length;
        this.shape = shape;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public ArrayList<Integer> getShape() {
        return shape;
    }

    public void setShape(ArrayList<Integer> shape) {
        this.shape = shape;
    }
}
