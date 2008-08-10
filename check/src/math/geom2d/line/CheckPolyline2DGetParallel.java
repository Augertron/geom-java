/**
 * 
 */
package math.geom2d.line;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.curve.Curve2D;

/**
 * @author dlegland
 *
 */
public class CheckPolyline2DGetParallel extends JPanel{
	private static final long serialVersionUID = 1L;

	Polyline2D polyline;
	Curve2D parallel;
	
	public CheckPolyline2DGetParallel(){
		
		polyline = new Polyline2D(new Point2D[]{
				new Point2D(50, 50),
				new Point2D(100, 50),
				new Point2D(100, 100),
				new Point2D(150, 100),
				new Point2D(50, 200) });
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLACK);
		g2.draw(polyline);
		
		
		g2.setColor(Color.BLUE);

		parallel = Polyline2DUtils.createParallel(polyline, 30);
		g2.draw(parallel);
		
		parallel = Polyline2DUtils.createParallel(polyline, -20);
		g2.draw(parallel);
	}
	
	public final static void main(String[] args){
		System.out.println("draw wedges");
		
		JPanel panel = new CheckPolyline2DGetParallel();
		JFrame frame = new JFrame("Draw parallel polyline");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);		
	}
}
