package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Configuration {
    private String ID;
    private double begin;
    private double end;
    private double step;
    private Net net;
    private Routes routes;

    public Configuration() {

    }

    public Configuration(String ID, double begin, double end, double step, Net net, Routes routes) {
        super();

        this.ID = ID;
        this.begin = begin;
        this.end = end;
        this.step = step;
        this.net = net;
        this.routes = routes;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getBegin() {
        return begin;
    }

    public void setBegin(double begin) {
        this.begin = begin;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public Net getNet() {
        return net;
    }

    public void setNet(Net net) {
        this.net = net;
    }

    public Routes getRoutes() {
        return routes;
    }

    public void setRoutes(Routes routes) {
        this.routes = routes;
    }
}
