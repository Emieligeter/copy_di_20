package sumodashboard.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Junction {
    private String ID;
    private String type;
    private double x;
    private double y;
    private String incLanes;
    private String intLanes;
    private String shape;

    public Junction() {

    }

    public Junction(String ID, String type, double x, double y, String incLanes, String intLanes, String shape) {
        this.ID = ID;
        this.type = type;
        this.x = x;
        this.y = y;
        this.incLanes = incLanes;
        this.intLanes = intLanes;
        this.shape = shape;
    }

    public String getID() {
        return ID;
    }

    @XmlAttribute
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    @XmlAttribute
    public void setType(String type) {
        this.type = type;
    }

    public double getX() {
        return x;
    }

    @XmlAttribute
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    @XmlAttribute
    public void setY(double y) {
        this.y = y;
    }

    public String getIncLanes() {
        return incLanes;
    }

    @XmlAttribute
    public void setIncLanes(String incLanes) {
        this.incLanes = incLanes;
    }

    public String getIntLanes() {
        return intLanes;
    }

    @XmlAttribute
    public void setIntLanes(String intLanes) {
        this.intLanes = intLanes;
    }

    public String getShape() {
        return shape;
    }

    @XmlAttribute
    public void setShape(String shape) {
        this.shape = shape;
    }
}
