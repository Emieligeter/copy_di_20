package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Location {
    private String netOffset;
    private String convBoundary;
    private String origBoundary;
    private String projParameter;

    public Location() {

    }

    public Location(String netOffset, String convBoundary, String origBoundary, String projParameter) {
        this.netOffset = netOffset;
        this.convBoundary = convBoundary;
        this.origBoundary = origBoundary;
        this.projParameter = projParameter;
    }

    public String getNetOffset() {
        return netOffset;
    }

    public void setNetOffset(String netOffset) {
        this.netOffset = netOffset;
    }

    public String getConvBoundary() {
        return convBoundary;
    }

    public void setConvBoundary(String convBoundary) {
        this.convBoundary = convBoundary;
    }

    public String getOrigBoundary() {
        return origBoundary;
    }

    public void setOrigBoundary(String origBoundary) {
        this.origBoundary = origBoundary;
    }

    public String getProjParameter() {
        return projParameter;
    }

    public void setProjParameter(String projParameter) {
        this.projParameter = projParameter;
    }
}
