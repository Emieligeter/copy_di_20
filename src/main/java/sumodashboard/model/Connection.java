package sumodashboard.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Connection {
    private String from;
    private String to;
    private String fromLane;
    private String toLane;
    private String via;
    private String dir;
    private String state;

    public Connection() {

    }

    public Connection(String from, String to, String fromLane, String toLane, String dir, String state) {
        this.from = from;
        this.to = to;
        this.fromLane = fromLane;
        this.toLane = toLane;
        this.dir = dir;
        this.state = state;
    }

    public Connection(String from, String to, String fromLane, String toLane, String via, String dir, String state) {
        this.from = from;
        this.to = to;
        this.fromLane = fromLane;
        this.toLane = toLane;
        this.via = via;
        this.dir = dir;
        this.state = state;
    }

    public String getFrom() {
        return from;
    }

    @XmlAttribute
    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    @XmlAttribute
    public void setTo(String to) {
        this.to = to;
    }

    public String getFromLane() {
        return fromLane;
    }

    @XmlAttribute
    public void setFromLane(String fromLane) {
        this.fromLane = fromLane;
    }

    public String getToLane() {
        return toLane;
    }

    @XmlAttribute
    public void setToLane(String toLane) {
        this.toLane = toLane;
    }

    public String getVia() {
        return via;
    }

    @XmlAttribute
    public void setVia(String via) {
        this.via = via;
    }

    public String getDir() {
        return dir;
    }

    @XmlAttribute
    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getState() {
        return state;
    }

    @XmlAttribute
    public void setState(String state) {
        this.state = state;
    }
}
