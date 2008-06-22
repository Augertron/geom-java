/**
 * 
 */
package math.geom2d.curve;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.domain.Boundary2DUtil;
import math.geom2d.domain.BoundarySet2D;

/**
 * @author dlegland
 *
 */
public class CheckDrawCurveSet2D extends JPanel{
	private static final long serialVersionUID = 1L;

	BoundarySet2D<Circle2D> circleSet = new BoundarySet2D<Circle2D>();
	
	public CheckDrawCurveSet2D(){
		circleSet.addCurve(new Circle2D(50, 50, 40));
		circleSet.addCurve(new Circle2D(150, 50, 40));
		circleSet.addCurve(new Circle2D(100, 140, 50));
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setStroke(new BasicStroke(2.0f));
	
		Box2D box = new Box2D(0, 200, 0, 200);
		Curve2D clipped =  Boundary2DUtil.clipBoundary(circleSet, box);

		g2.setColor(Color.CYAN);
		g2.fill(clipped);
		g2.setColor(Color.BLUE);
		g2.draw(clipped);
	}
	
	public final static void main(String[] args){
		System.out.println("draw a curve set");
		
		JPanel panel = new CheckDrawCurveSet2D();
		JFrame frame = new JFrame("Draw curve set demo");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
		
	}
}
