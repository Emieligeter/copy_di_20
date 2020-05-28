package sumodashboard.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GraphPoint {
	private double xValue;
	private double yValue;

	public GraphPoint() {
		
	}
	
	public GraphPoint(double x, double y) {
		this.xValue = x;
		this.yValue = y;
	}
	
	public double getXValue() {
		return xValue;
	}
	
	public void setXValue(double value) {
		this.xValue = value;
	}
	
	public double getYValue() {
		return yValue;
	}
	
	public void setYValue(double value) {
		this.yValue = value;
	}
}
