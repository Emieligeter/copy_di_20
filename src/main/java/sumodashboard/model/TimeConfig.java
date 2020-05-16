package sumodashboard.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="time")
public class TimeConfig {
    private int begin;
    private int end;
    private double stepLength;

    public TimeConfig() {

    }

    public TimeConfig(int begin, int end, double stepLength) {
        this.begin = begin;
        this.end = end;
        this.stepLength = stepLength;
    }

    public int getBegin() {
        return begin;
    }

    @XmlElement
    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    @XmlElement
    public void setEnd(int end) {
        this.end = end;
    }

    public double getStepLength() {
        return stepLength;
    }

    @XmlElement
    public void setStepLength(double stepLength) {
        this.stepLength = stepLength;
    }
}
