package kmean;

public class Point {

	private double x;
	private double y;
	private int clusterId;
	private double dinstanceFromCentroid;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		this.clusterId = -1;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public int getClusterId() {
		return this.clusterId;
	}
	
	public void setClusterId(int newClusterId) {
		this.clusterId = newClusterId;
	}
	
	public double getDinstanceFromCentroid() {
		return this.dinstanceFromCentroid;
	}
	
	public void setDinstanceFromCentroid(double newDinstanceFromCentroid) {
		this.dinstanceFromCentroid = newDinstanceFromCentroid;
	}
	
}