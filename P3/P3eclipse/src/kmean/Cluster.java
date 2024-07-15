package kmean;

import java.util.ArrayList;

public class Cluster {
	private int id;
	private Point centroid;
	private ArrayList<Point> clusterPoints = new ArrayList<Point>();
	
	public Cluster(int id, Point centroid) {
		this.id = id;
		this.centroid = centroid;	
	}
	
	public Point getCentroid() {
		return this.centroid;
	}
	
	public void setCentroid(Point centroid) {
		this.centroid = centroid ;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void addPoint(Point point) {
		this.clusterPoints.add(point);
	}
	
	public ArrayList<Point> getClusterPoints() {
		return this.clusterPoints;
	}
	
}
