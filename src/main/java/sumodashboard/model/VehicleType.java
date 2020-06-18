package sumodashboard.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VehicleType {
    private String ID;
    private String vClass;

    public VehicleType() {

    }

    public VehicleType(String ID, String vClass) {
        this.ID = ID;
        this.vClass = vClass;
    }

    public String getID() {
        return ID;
    }

    @XmlAttribute
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return vClass;
    }

    @XmlAttribute(name="vClass")
    public void setType(String vClass) {
        this.vClass = vClass;
    }
}