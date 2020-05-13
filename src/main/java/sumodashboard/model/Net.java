package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Net {
    private int ID;
    private String version;
    private int junctionCornerDetail;
    private int limitTurnSpeed;
    private String location;

    public Net() {

    }

    public Net(int ID, String version, int junctionCornerDetail, int limitTurnSpeed, String location) {
        this.ID = ID;
        this.version = version;
        this.junctionCornerDetail = junctionCornerDetail;
        this.limitTurnSpeed = limitTurnSpeed;
        this.location = location;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getJunctionCornerDetail() {
        return junctionCornerDetail;
    }

    public void setJunctionCornerDetail(int junctionCornerDetail) {
        this.junctionCornerDetail = junctionCornerDetail;
    }

    public int getLimitTurnSpeed() {
        return limitTurnSpeed;
    }

    public void setLimitTurnSpeed(int limitTurnSpeed) {
        this.limitTurnSpeed = limitTurnSpeed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
