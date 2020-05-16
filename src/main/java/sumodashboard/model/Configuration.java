package sumodashboard.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="configuration")
public class Configuration {
    private TimeConfig timeConfig;
    private Net net;
    private Routes routes;

    public Configuration() {

    }

    public TimeConfig getTimeConfig() {
        return timeConfig;
    }

    @XmlElement
    public void setTimeConfig(TimeConfig timeConfig) {
        this.timeConfig = timeConfig;
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
