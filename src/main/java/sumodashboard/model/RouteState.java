package sumodashboard.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RouteState extends Route {
    private String ID;
    private int state;

    public RouteState() {

    }

    public RouteState(String edges, String ID, int state) {
        super(edges);
        this.ID = ID;
        this.state = state;
    }

    public String getID() {
        return ID;
    }

    @XmlAttribute
    public void setID(String ID) {
        this.ID = ID;
    }

    public int getState() {
        return state;
    }

    @XmlAttribute
    public void setState(int state) {
        this.state = state;
    }
}
