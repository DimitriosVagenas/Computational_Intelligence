package twolevels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import javax.swing.JPanel;

class Graph extends JPanel {

	private static final long serialVersionUID = 1L;

	private Point[] points;
	
    public Graph(Point[] point) {
    	this.points = point; 
    }
    
    private void doDrawing(Graphics g) {
    	int metricY = 603/10;
    	int metricX = 602/4;
        Graphics2D g2d = (Graphics2D) g;
        FontRenderContext frc = g2d.getFontRenderContext();
        Font font1 = new Font("Serif", Font.PLAIN, 20);
        String numb;
        TextLayout tl;
        Color[] colors = { Color.CYAN, Color.green,  Color.MAGENTA, Color.red, Color.BLUE,   
      		  Color.gray, Color.orange, Color.yellow, Color.pink, Color.LIGHT_GRAY, Color.BLACK, Color.DARK_GRAY , Color.BLUE };
        g2d.setPaint(Color.black);
        
        g2d.drawLine(100, 80, 100, 683);
        g2d.drawLine(100, 80, 702, 80);
        g2d.drawLine(702, 80, 702, 683);
        g2d.drawLine(702, 683, 100, 683);
        for (int i = 1; i < 10; i++) {
        	g2d.drawLine(100, 80 + metricY * i, 120, 80 + metricY * i);
        	g2d.drawLine(702, 80 + metricY * i, 682, 80 + metricY * i);
        	int number = -10 + 2 * i;
        	if (number<0) {
        		tl = new TextLayout("-0."+ (-number), font1, frc);
            	tl.draw(g2d, 65, 683 - metricY * i);
        	}
        	else {
        		tl = new TextLayout("0."+number, font1, frc);
            	tl.draw(g2d, 70, 683 - metricY * i);
        	}
        	
        }
        tl = new TextLayout("-1", font1, frc);
        tl.draw(g2d, 80, 683);
        tl = new TextLayout("1", font1, frc);
        tl.draw(g2d, 85, 80);
        for (int i = 1; i < 4; i++) {
        	g2d.drawLine(100 + metricX * i, 80, 100 + metricX * i, 100);
        	g2d.drawLine(100 + metricX * i, 682, 100 + metricX * i, 662);
        	double number = -1 + 0.5 * i;
        	numb = number + "";
        	if (number<0) {
        		tl = new TextLayout(numb.substring(0, 4), font1, frc);
                tl.draw(g2d, 100 + metricX * i, 700);
        	}
        	else {
        		tl = new TextLayout(numb.substring(0, 3), font1, frc);
                tl.draw(g2d, 100 + metricX * i, 700);
        	}
        	
        }
        tl = new TextLayout("-1", font1, frc);
        tl.draw(g2d, 100, 700);
        tl = new TextLayout("1", font1, frc);
        tl.draw(g2d, 700, 700);
        
        double x;
        double y;
        	
        for (int j = 0; j < 4000; j++){
          	 x = 400 + points[j].getX()*300 ;
          	 y = 80 + 300 - points[j].getY()*300 ;
          	 g2d.setPaint(colors[points[j].getClusterId()]);
          	 g2d.fill(new Ellipse2D.Double(x,y,4.5,4.5));
        }
}

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }
}
