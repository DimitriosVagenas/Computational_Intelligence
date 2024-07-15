
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

	private Cluster[] bestClusters;
	private int M;
	
    public Graph(Cluster[] clusters, int m) {
    	this.bestClusters = clusters; 
        this.M = m;
    }
    
    private void doDrawing(Graphics g) {
    	int metricY = 603/10;
    	int metricX = 602/4;
        Graphics2D g2d = (Graphics2D) g;
        FontRenderContext frc = g2d.getFontRenderContext();
        Font font1 = new Font("Serif", Font.PLAIN, 20);
        String numb;
        TextLayout tl;
        Color[] colors = {Color.BLUE, Color.red, Color.CYAN, Color.green, Color.MAGENTA, 
      		  Color.gray, Color.orange, Color.yellow, Color.pink, Color.LIGHT_GRAY, Color.BLACK, Color.DARK_GRAY , Color.BLUE };
        g2d.setPaint(Color.black);
        
        g2d.drawLine(100, 80, 100, 683);
        g2d.drawLine(100, 80, 702, 80);
        g2d.drawLine(702, 80, 702, 683);
        g2d.drawLine(702, 683, 100, 683);
        for (int i = 1; i < 10; i++) {
        	g2d.drawLine(100, 80 + metricY * i, 120, 80 + metricY * i);
        	g2d.drawLine(702, 80 + metricY * i, 682, 80 + metricY * i);
        	numb = 0.2 * i +"";
        	tl = new TextLayout(numb.substring(0, 3), font1, frc);
        	tl.draw(g2d, 70, 683 - metricY * i);
        }
        tl = new TextLayout("0", font1, frc);
        tl.draw(g2d, 85, 683);
        tl = new TextLayout("2", font1, frc);
        tl.draw(g2d, 85, 80);
        for (int i = 1; i < 4; i++) {
        	g2d.drawLine(100 + metricX * i, 80, 100 + metricX * i, 100);
        	g2d.drawLine(100 + metricX * i, 682, 100 + metricX * i, 662);
        	numb = 0.5 * i + "";
        	tl = new TextLayout(numb.substring(0, 3), font1, frc);
            tl.draw(g2d, 100 + metricX * i, 710);
        }
        tl = new TextLayout("0", font1, frc);
        tl.draw(g2d, 100, 710);
        tl = new TextLayout("2", font1, frc);
        tl.draw(g2d, 700, 710);
        
        double x;
        double y;
        
        for (int i = 0; i < M; i++) {
        	g2d.setPaint(colors[i]);
            for (Point point : bestClusters[i].getClusterPoints()){
            	 x = 100 + point.getX()*300 ;
                 y = 80 + 600 - point.getY()*300 ;
                 g2d.fill(new Ellipse2D.Double(x,y,6,6));
            }
        }
        
        for (int i = 0; i < M; i++) {
        	g2d.setPaint(Color.black);
            x = 100 + bestClusters[i].getCentroid().getX()*300 ;
            y = 80 + 600 - bestClusters[i].getCentroid().getY()*300 ;
            g2d.fill(createStar(x, y, 4, 15, 10, 0));
        }
        
}

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    private static Shape createStar(double centerX, double centerY,
            double innerRadius, double outerRadius, int numRays,
            double startAngleRad)
        {
            Path2D path = new Path2D.Double();
            double deltaAngleRad = Math.PI / numRays;
            for (int i = 0; i < numRays * 2; i++)
            {
                double angleRad = startAngleRad + i * deltaAngleRad;
                double ca = Math.cos(angleRad);
                double sa = Math.sin(angleRad);
                double relX = ca;
                double relY = sa;
                if ((i & 1) == 0)
                {
                    relX *= outerRadius;
                    relY *= outerRadius;
                }
                else
                {
                    relX *= innerRadius;
                    relY *= innerRadius;
                }
                if (i == 0)
                {
                    path.moveTo(centerX + relX, centerY + relY);
                }
                else
                {
                    path.lineTo(centerX + relX, centerY + relY);
                }
            }
            path.closePath();
            return path;
        }
    
  
}


