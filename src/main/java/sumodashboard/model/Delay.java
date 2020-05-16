package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Delay {
    private int number;
    private int begin;
    private int end;
    private double depart;
    private double time;

    public Delay() {

    }

    public Delay(int number, int begin, int end, double depart, double time) {
        this.number = number;
        this.begin = begin;
        this.end = end;
        this.depart = depart;
        this.time = time;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public double getDepart() {
        return depart;
    }

    public void setDepart(double depart) {
        this.depart = depart;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
