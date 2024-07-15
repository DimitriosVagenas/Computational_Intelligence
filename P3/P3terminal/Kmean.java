import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.Random;

public class Kmean {

	private static int M = 3;
	private static String data = "";
	private static String[] arrOfStr= new String[1200];
	private static Point[] points = new Point[1200];
	private static Cluster[] theTop = new Cluster[M];
	private static Cluster[] theCurrent = new Cluster[M];
	
	public static void main(String[] args) {
		/*try {
			dataGenerator();
			FileWriter fileWriter = new FileWriter("DATA/" + "data" + ".txt");
			
			fileWriter.write(data);
			fileWriter.close();
			
		}catch(Exception e ) {
			
			JOptionPane.showMessageDialog(null, e+"");
			
		}*/
		
		//Data reading
		try {
          String content = Files.readString(Paths.get("DATA/" + "data" + ".txt"));
          arrOfStr = content.split("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i;
		double x, y;
		for (i = 0; i < 1200; i++) {
			String[] xy = arrOfStr[i].split(" ");
			x = Double.valueOf(xy[0]);
			y = Double.valueOf(xy[1]);
			Point point = new Point(x, y);
			points[i] = point;
		}
		
		for (i = 0; i < 6; i++) {
			M = i*2 + 3;
			runAll3_13();
		}
	}
	
	public static void runAll3_13() {	
		double clusteringError = 0;
		double bestClusteringError = 1000;
		int i;
		for (i = 0; i < 20; i++) {
			clusteringError= callKmean();
			if (clusteringError < bestClusteringError) {
				bestClusteringError = clusteringError;
				theTop = theCurrent;
			}
		}
		JFrame frame =new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Graph(theTop, M));
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        String title = "Points " + M + " Clusters";
        frame.setTitle(title);
		System.out.println(M + " Clusters with error: " + bestClusteringError  );
	}
	
	
	
	public static double callKmean() {
		Cluster[] clusters = new Cluster[M];
		Cluster[] bestClusters = new Cluster[M];
		
		Random centroidPicker = new Random();
		int randomPoint; 
		int i;
		for (i = 0; i < M; i++) {
			
			randomPoint = centroidPicker.nextInt(1199);
			Cluster randomCluster = new Cluster(i, points[randomPoint]);
			clusters[i] = randomCluster;
		}
		double clusteringError = 0;
		bestClusters = kMean(clusters);
		for (i = 0; i < 1200; i++) {
			clusteringError += points[i].getDinstanceFromCentroid();
		}
		theCurrent = bestClusters;
		
		
		//Plotting
		
		return clusteringError;
	}
	
	
	private static Cluster[] kMean(Cluster[] currentClusters) {
		
		int finished = 1;
		int counter = 0;
		while (finished == 1)  {
			currentClusters = Clustering(currentClusters);
			Cluster[] newClusters = newClusters(currentClusters);
			finished = 0;
			for (int j = 0; j < M; j++) {
				if ((newClusters[j].getCentroid().getX() != currentClusters[j].getCentroid().getX()) || (newClusters[j].getCentroid().getY() != currentClusters[j].getCentroid().getY()) )  {
					finished = 1;
				}
			}
			currentClusters = newClusters;
			counter++;
			if (counter==40000) {
				break;
			}
		}
		currentClusters = Clustering(currentClusters);
		return currentClusters;
	}
	
	
	 private static double distance(Point p, Point centroid) {
		 return Math.sqrt(Math.pow((centroid.getY() - p.getY()), 2) + Math.pow((centroid.getX() - p.getX()), 2));
	 }
	 
	 private static Cluster[] Clustering(Cluster[] clusters) {
		 double minimumDistance=100;
		 int clusterId=-1;
		 for (int i = 0; i < 1200; i++) {
			 minimumDistance=100;
			 for (int j = 0; j < M; j++) {
				 double distance = distance(points[i], clusters[j].getCentroid());
				 
				 
				 if (minimumDistance > distance) {								
					 minimumDistance = distance;								   
					 clusterId = j;
				 }
			 }
			 points[i].setClusterId(clusterId);
			 points[i].setDinstanceFromCentroid(minimumDistance);
			 clusters[clusterId].addPoint(points[i]);
			 
		 }
		 
		
		 return clusters;
	 }
	
	 private static Cluster[] newClusters(Cluster[] clusters) {
		 Cluster[] newClusters = new Cluster[M];
		 Point newCentroid;
		 for (int j = 0; j < M; j++) {
			 newCentroid = calculateNewCentroid(clusters[j].getClusterPoints());
			// System.out.println("" + newCentroid.getX() + "     "+ newCentroid.getY());
			 newClusters[j] = new Cluster(j, newCentroid);
		 }
		 return newClusters;
	 }
	 
	 private static Point calculateNewCentroid(ArrayList<Point> clusterPoints) {
	
		 double x = 0;
		 double y = 0;
		 
		 for (Point point : clusterPoints) {
			 x += point.getX();
			 y += point.getY();
		 }
		 x = x / clusterPoints.size();
		 y = y / clusterPoints.size();
		 return new Point(x,y);
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 private static void dataGenerator() {
			int i;
			for (i = 0; i < 150; i++) {
				data += pointGenerator(0.75, 1.25, 0.75, 1.25) + "\n";
			}
			for (i = 150; i < 300; i++) {
				data += pointGenerator(0, 0.5, 0, 0.5) + "\n";
			}
			for (i = 300; i < 450; i++) {
				data += pointGenerator(0, 0.5, 1.5, 2) + "\n";
			}
			for (i = 450; i < 600; i++) {
				data += pointGenerator(1.5, 2, 0, 0.5) + "\n";
			}
			for (i = 600; i < 750; i++) {
				data += pointGenerator(1.5, 2, 1.5, 2) + "\n";
			}
			for (i = 750; i < 825; i++) {
				data += pointGenerator(0.6, 0.8, 0, 0.4) + "\n";
			}
			for (i = 825; i < 900; i++) {
				data += pointGenerator(0.6, 0.8, 1.6, 2) + "\n";
			}
			for (i = 900; i < 975; i++) {
				data += pointGenerator(1.2, 1.4, 0, 0.4) + "\n";
			}
			for (i = 975; i < 1050; i++) {
				data += pointGenerator(1.2, 1.4, 1.6, 2) + "\n";
			}
			for (i = 1050; i < 1200; i++) {
				data += pointGenerator(0, 2, 0, 2) + "\n";
			}
			
		}

		private static String pointGenerator(double minx, double maxx,double miny, double maxy) {
			Random r = new Random();
			double x = minx + (maxx - minx) * r.nextDouble();
			double y = miny + (maxy - miny) * r.nextDouble();
			return String.valueOf(x)+" "+String.valueOf(y);
		}
		

}



