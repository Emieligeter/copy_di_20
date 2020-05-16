package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Connection {
    private Edge fromEdge;
    private Edge toEdge;
    private Lane fromLane;
    private Lane toLane;
    private Junction viaJunction;
    private char dir;
    private char state;

    public Connection() {

    }

    public Connection(Edge fromEdge, Edge toEdge, Lane fromLane, Lane toLane, char dir, char state) {
        this.fromEdge = fromEdge;
        this.toEdge = toEdge;
        this.fromLane = fromLane;
        this.toLane = toLane;
        this.dir = dir;
        this.state = state;
    }

    public Connection(Edge fromEdge, Edge toEdge, Lane fromLane, Lane toLane, Junction viaJunction, char dir, char state) {
        this.fromEdge = fromEdge;
        this.toEdge = toEdge;
        this.fromLane = fromLane;
        this.toLane = toLane;
        this.viaJunction = viaJunction;
        this.dir = dir;
        this.state = state;
    }

    public Edge getFromEdge() {
        return fromEdge;
    }

    public void setFromEdge(Edge fromEdge) {
        this.fromEdge = fromEdge;
    }

    public Edge getToEdge() {
        return toEdge;
    }

    public void setToEdge(Edge toEdge) {
        this.toEdge = toEdge;
    }

    public Lane getFromLane() {
        return fromLane;
    }

    public void setFromLane(Lane fromLane) {
        this.fromLane = fromLane;
    }

    public Lane getToLane() {
        return toLane;
    }

    public void setToLane(Lane toLane) {
        this.toLane = toLane;
    }

    public Junction getViaJunction() {
        return viaJunction;
    }

    public void setViaJunction(Junction viaJunction) {
        this.viaJunction = viaJunction;
    }

    public char getDir() {
        return dir;
    }

    public void setDir(char dir) {
        this.dir = dir;
    }

    public char getState() {
        return state;
    }

    public void setState(char state) {
        this.state = state;
    }
}
