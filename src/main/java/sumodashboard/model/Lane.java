package sumodashboard.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "lane")
public class Lane {
    private String ID;
    private int index;
    private double speed;
    private double length;
    private String shape;

    public Lane() {

    }

    public Lane(String ID, int index, double speed, double length, String shape) {
        this.ID = ID;
        this.index = index;
        this.speed = speed;
        this.length = length;
        this.shape = shape;
    }

    public String getID() {
        return ID;
    }

    @XmlAttribute
    public void setID(String ID) {
        this.ID = ID;
    }
    
    public int getIndex() {
    	return index;
    }
    
    @XmlAttribute
    public void setIndex(int index) {
    	this.index = index;
    }

    public double getSpeed() {
        return speed;
    }

    @XmlAttribute
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getLength() {
        return length;
    }

    @XmlAttribute
    public void setLength(double length) {
        this.length = length;
    }

    public String getShape() {
        return shape;
    }

    @XmlAttribute
    public void setShape(String shape) {
        this.shape = shape;
    }
}
