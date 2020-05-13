package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Configuration {
    private String ID;
    private double begin;
    private double end;
    private double step;
    private Net net;
    private RoutesXml routesXml;

    public Configuration() {

    }

    public Configuration(String ID, double begin, double end, double step, Net net, RoutesXml routesXml) {
        super();

        this.ID = ID;
        this.begin = begin;
        this.end = end;
        this.step = step;
        this.net = net;
        this.routesXml = routesXml;
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

    public RoutesXml getRoutesXml() {
        return routesXml;
    }

    public void setRoutesXml(RoutesXml routesXml) {
        this.routesXml = routesXml;
    }
}
