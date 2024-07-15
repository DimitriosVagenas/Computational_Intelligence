
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PerceptronTwoLevels {

	
	private static double n = 0.000002;
	private static int B = 1;
	private static int h1 = 15;
	private static int h2 = 15;
	private static double katwfli = 0.01;
	private static boolean pickRelu = true;

	
	private static String data = "";
	private static int tes = 4000/B;
	private static String[] arrOfStr= new String[tes];
	private static Point[] points = new Point[tes];
	private static double[][] newInputs;
	private static double[][] newInputs1;
	private static double[][] newInputs2;
	private static double[][] newInputs3;
	private static double[][] newOutputs;
	private static int ClusterNumber = 4;
	private static double[][] weight1;
	private static double[][] weight2;
	private static double[][] weight3;
	private static double[][] errorOl1 = new double[2+1][h1];
	private static double[][] errorOl2 = new double[h1+1][h2];
	private static double[][] errorOl3 = new double[h2+1][ClusterNumber];
	private static double[][] deltaExodus = new double[tes][ClusterNumber];
	private static double[][] deltaProExodus = new double[tes][h2];
	private static double[][] deltaProProExodus = new double[tes][h1];
	
	
	
	
	public static void main(String[] args) {
		readData("learning", tes);
		//showGraph();
		newInputs = new double[tes][2];
		for (int i = 0; i < tes; i++) {
			newInputs[i][0] = points[i].getX(); 
			newInputs[i][1] = points[i].getY(); 	
		}
		learn();
		
		
		/*for (int i = 0; i < tes; i++) {
			
			for (int j = 0; j < ClusterNumber; j++) {
				System.out.println(newInputs3[i][j] +" " + j + " " + i);
			}
		}*/
	}
	private static void learn() {
		weight1 = createWeights(newInputs[0].length + 1, h1);
		weight2 = createWeights(h1 + 1, h2);
		weight3 = createWeights(h2 + 1, ClusterNumber);
		int t = 1;
		int y = 0;
		double sfalmaPre = 10;
		double sfalma = 0;
		while(y == 0) {
			sfalma = 0;
			
			cleaner();
			newInputs1 = forwardPass(newInputs, h1, weight1, tes);
			newInputs2 = forwardPass(newInputs1, h2, weight2, tes);
			newInputs3 = forwardPass(newInputs2, ClusterNumber, weight3,tes);
			backProp();
			updateWeights();
			for (int i = 0; i < tes; i++) {
				for (int j = 0; j < ClusterNumber; j++) {					
					sfalma += Math.pow(deltaExodus[i][j],2);
				}
			}
			for (int i = 0; i < tes; i++) {
				for (int j = 0; j < h2; j++) {					
					sfalma += Math.pow(deltaProExodus[i][j],2);
				}
			}
			for (int i = 0; i < tes; i++) {
				for (int j = 0; j < h1; j++) {					
					sfalma += Math.pow(deltaProProExodus[i][j],2);
				}
			}
			sfalma /= 2;
			t++;
			System.out.println(sfalma + " " + t);
			if (t>700) {
				if (Math.abs(sfalma-sfalmaPre) < katwfli) {
					y = 1;
				}
			}
			sfalmaPre = sfalma;
			
			
		}
		System.out.println();
		System.out.println();
		System.out.println();
		int counter = 0;
		for (int i = 0; i < tes; i++) {
			double max=0;
			int teamj=0;
			for (int j = 0  ; j < 4; j++) {
				if(newInputs3[i][j] > max){
					
					max = newInputs3[i][j];
					teamj = j+1;
				}
				
			}
			if(points[i].getClusterId() == teamj ) {
				counter++;
			}
			
		}
		System.out.println(counter);
		
		points = new Point[4000];
		readData("checking", 4000);
		//showGraph();
		newInputs = new double[4000][2];
		for (int i = 0; i < 4000; i++) {
			newInputs[i][0] = points[i].getX(); 
			newInputs[i][1] = points[i].getY(); 	
		}
		newInputs1 = forwardPass(newInputs, h1, weight1, 4000);
		newInputs2 = forwardPass(newInputs1, h2, weight2, 4000);
		newInputs3 = forwardPass(newInputs2, ClusterNumber, weight3, 4000);
		for (int i = 0; i < 4000; i++) {
			double max=0;
			int teamj=0;
			for (int j = 0  ; j < 4; j++) {
				if(newInputs3[i][j] > max){
					max = newInputs3[i][j];
					teamj = j+1;
				}
				
			}
			
			points[i].setClusterId(teamj);
			
		}
		showGraph();	
		
		
	}
	
	private static void updateWeights(){
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < h1; j++) {
				weight1[i][j] = weight1[i][j] - errorOl1[i][j]*n;
			}
		}
		
		for (int i = 0; i < h1 + 1; i++) {
			for (int j = 0; j < h2; j++) {		
				weight2[i][j] = weight2[i][j] - errorOl2[i][j]*n;
			}
		}
		for (int i = 0; i < h2 + 1; i++) {
			for (int j = 0; j < ClusterNumber; j++) {
				weight3[i][j] = weight3[i][j] - errorOl3[i][j]*n;
			}
		}
		
		
	}
	
	
	private static void cleaner(){
		errorOl1 = new double[3][h1];
		errorOl2 = new double[h1+1][h2];
		errorOl3 = new double[h2+1][ClusterNumber];
	}
		
	
	private static void backProp(){
		deltaExodus = calculateExodusError();
		deltaProExodus = calculateLevelError(deltaExodus, h2, ClusterNumber, weight3, newInputs2);
		deltaProProExodus = calculateLevelError(deltaProExodus, h1, h2, weight2, newInputs1);
		//deltaEsodus = calculateLevelError(deltaProProExodus, 2, h1, weight1);
		
		
		
		for (int k = 0; k < tes; k++) {
			double[][] paragogos = new double[h2+1][ClusterNumber];
			for (int i = 1; i < h2 + 1; i++) {
				for (int j = 0; j < ClusterNumber; j++) {
					paragogos[i][j] = deltaExodus[k][j]*newInputs2[k][i-1];
					errorOl3[i][j] += paragogos[i][j];
				}
			}
		}
		for (int k = 0; k < tes; k++) {
			for (int j = 0; j < ClusterNumber; j++) {
				errorOl3[0][j] += deltaExodus[k][j];
			}
		}
		
		
		
		for (int k = 0; k < tes; k++) {
			double[][] paragogos = new double[h1+1][h2];
			for (int i = 1; i < h1 + 1; i++) {
				for (int j = 0; j < h2; j++) {
					paragogos[i][j] = deltaProExodus[k][j]*newInputs1[k][i-1];
					errorOl2[i][j] += paragogos[i][j];
				}
			}
		}
		for (int k = 0; k < tes; k++) {
			for (int j = 0; j < h2; j++) {
				errorOl2[0][j] += deltaProExodus[k][j];
			}
		}
		
		for (int k = 0; k < tes; k++) {
			double[][] paragogos = new double[3][h1];
			for (int i = 1; i < 3; i++) {
				for (int j = 0; j < h1; j++) {
					paragogos[i][j] = deltaProProExodus[k][j]*newInputs[k][i-1];
					errorOl1[i][j] += paragogos[i][j];
				}
			}
		}
		for (int k = 0; k < tes; k++) {
			for (int j = 0; j < h1; j++) {
				errorOl1[0][j] += deltaProProExodus[k][j];
			}
		}
	}			
	
	private static double[][] calculateExodusError(){
		double[][] deltaExodus = new double[tes][ClusterNumber];
		for (int i = 0; i < tes; i++) {
			for (int j = 0; j < ClusterNumber; j++) {
				if (points[i].getClusterId() - 1 == j) {
					if (pickRelu) {
						deltaExodus[i][j] = newInputs3[i][j] - 1;
				
					}
					else {
						deltaExodus[i][j] = newInputs3[i][j] * (1 - newInputs3[i][j]) * (newInputs3[i][j] - 1);
						
					}
				}
				else {
					if (pickRelu) {
						deltaExodus[i][j] = newInputs3[i][j] - 0;
					}
					else {
						deltaExodus[i][j] = newInputs3[i][j] * (1 - newInputs3[i][j]) * (newInputs3[i][j] + 1);
						
					}
					
				}
			}
			
		}
		
		return deltaExodus;
		
	}
	
	private static double[][] calculateLevelError(double[][] deltaPreviousLevel, int neuronsIn, int neuronsOut, double[][] weight, double[][] yi){
		double[][] deltaLevel = new double[tes][neuronsIn];
		for (int i = 0; i < tes; i++) {
			for (int j = 0; j < neuronsIn; j++) {
				deltaLevel[i][j] = 0;
				for (int k = 0; k < neuronsOut; k++) {
					if (pickRelu) { 
						deltaLevel[i][j] +=  deltaPreviousLevel[i][k] * weight[j+1][k];
					}
					else {
						deltaLevel[i][j] += (1 - yi[i][j] * yi[i][j]) * deltaPreviousLevel[i][k] * weight[j+1][k];
					}
				}
			}
		}
		return deltaLevel;
	}
	
	
	public static double[][] forwardPass(double[][] input, int outputSize, double[][] weight, int team) {
		newOutputs = new double[team][outputSize];
		double u;
		for (int i = 0; i < team; i++) {
			for (int j = 0  ; j < outputSize; j++) {
				u = 0;
				for (int k = 0; k < input[0].length ; k++) {
					u += input[i][k] * weight[k+1][j];
				}
				u += weight[0][j];
				
				if (pickRelu) {
					u = relu(u);
				}
				else {
					u = tanH(u);
				}
				newOutputs[i][j] = u;
			}
		}
		return newOutputs;
	}
	
	public static double[][] createWeights(int input, int output) {
		
		double[][] weight = new double[input][output];
		
		for (int j = 0; j < output; j++) {
			weight[0][j] = 0;
		}
		for (int i = 1; i < input; i++) {
			for (int j = 0; j < output; j++) {
				weight[i][j] = randomGenerator(-1, 1);
			}
		}
		return weight;
	}
	
	
	public static double relu(double number) {
		double relu = 0;
		if (number > 0 ) {
			relu = number;
		}	
		return relu;
	}
	
	public static double tanH(double number) {
		return Math.tanh(number);
	}
	
	
	
	private static void readData(String file, int number) {	
		try {
			String content = Files.readString(Paths.get( file + ".txt"));
			arrOfStr = content.split("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i;
		double x, y;
		for (i = 0; i < number; i++) {
			String[] xy = arrOfStr[i].split(" ");
			x = Double.valueOf(xy[0]);
			y = Double.valueOf(xy[1]);
			Point point = new Point(x, y);
			point.setClusterId(Integer.parseInt(xy[2]));
			points[i] = point;
		}
	}
	
	private static void showGraph() {
		JFrame frame =new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new Graph(points));
		frame.setSize(800, 800);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		String title = "Points";
		frame.setTitle(title);
	
	}
	
		
	private static void generateNewData() {
		try {
			dataGenerator();
			FileWriter fileWriter = new FileWriter( "learning" + ".txt");
			
			fileWriter.write(data);
			fileWriter.close();
	
		}catch(Exception e ) {
		
			JOptionPane.showMessageDialog(null, e+"");
	
		}
	}
	
	
	private static double distance(Point p, double x, double y) {
		 return Math.pow((y - p.getY()), 2) + Math.pow((x - p.getX()), 2);
	 }
	
	private static double distance2(double x1, double y1 , double x, double y) {
		 return Math.pow((y - y1), 2) + Math.pow((x - x1), 2);
	 }

	private static void dataGenerator() {
		int i;
		for (i = 0; i < tes; i++) {
			data += pointGenerator(-1, 1, -1, 1) + "\n";
		}
	}
	
	private static double randomGenerator(double min, double max) {
		Random r = new Random();
		double x = min + (max - min) * r.nextDouble();
		return x;
	}
	
	
	private static String pointGenerator(double minx, double maxx,double miny, double maxy) {
		String shmeio = "";
		Random r = new Random();
		int error1 = r.nextInt(100);
		int error;
		double x = minx + (maxx - minx) * r.nextDouble();
		double y = miny + (maxy - miny) * r.nextDouble();
		if ((distance2(x,y,0.5,0.5)<0.16)||(distance2(x,y,-0.5,-0.5)<0.16)) {
			shmeio = String.valueOf(x)+" "+String.valueOf(y) ;
			if (error1 > 10) {
				shmeio += " " + 1;
			}
			else {
				error = r.nextInt(3);
				shmeio += " " + (error+1);
			}
		}
		else if ((distance2(x,y,0.5,-0.5)<0.16)||(distance2(x,y,-0.5,0.5)<0.16)) {
			shmeio = String.valueOf(x)+" "+String.valueOf(y);
			if (error1 > 10) {
				shmeio += " " + 2;
			}
			else {
				error = r.nextInt(3);
				if (error == 3) {
					shmeio += " " + 4;
				}
				else if (error == 2){
					shmeio += " " + 3;
				}
				else {
					shmeio += " " + 1;
				}
			}
		}
		else if (x*y>0) {
			shmeio = String.valueOf(x)+" "+String.valueOf(y);
			if (error1 > 10) {
				shmeio += " " + 3;
			}
			else {
				error = r.nextInt(3);
				if (error == 3) {
					shmeio += " " + 4;
				}
				else if (error == 2){
					shmeio += " " + 2;
				}
				else {
					shmeio += " " + 1;
				}
			}
		}
		else {
			shmeio = String.valueOf(x)+" "+String.valueOf(y);
			if (error1 > 10) {
				shmeio += " " + 4;
			}
			else {
				error = r.nextInt(3);
				shmeio += " " + (4 - error);
			}
		}
		return shmeio;
	}
	
	
}

