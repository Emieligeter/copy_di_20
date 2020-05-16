package sumodashboard.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Route {
    private String edges;

    public Route() {

    }

    public Route(String edges) {
        this.edges = edges;
    }

    public String getEdges() {
        return edges;
    }

    @XmlAttribute
    public void setEdges(String edges) {
        this.edges = edges;
    }
}
