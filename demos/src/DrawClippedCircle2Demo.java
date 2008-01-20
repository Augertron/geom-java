/* file : DrawClippedCircle2Demo.java
 * 
 * Project : Euclide
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * Created on 1 avr. 2007
 *
 */

import java.awt.*;
import javax.swing.*;

import math.geom2d.*;


public class DrawClippedCircle2Demo extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	Circle2D circle = null;
	Box2D box = null;
	
	public DrawClippedCircle2Demo() {
		super();
		
		double x0 = 150;
		double y0 = 100;
		double r  = 50;
		circle = new Circle2D(x0, y0, r);
		
		box = new Box2D(50, 250, 50, 250);
	
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
	
		g2.setColor(Color.YELLOW);
		g2.fill(circle);
		
		g2.setColor(Color.BLUE);
		g2.draw(circle);

		g2.setColor(Color.BLUE);
		g2.draw(box);

		Curve2D clipped = box.clipCurve(circle);
		g2.setStroke(new BasicStroke(4.0f));
		g2.setColor(Color.RED);
		g2.draw(clipped);

		clipped = box.clipBoundary(circle);
		g2.setStroke(new BasicStroke(2.0f));
		g2.setColor(Color.CYAN);
		g2.fill(clipped);
		g2.setColor(Color.BLUE);
		g2.draw(clipped);
	}

	public final static void main(String[] args){
		System.out.println("should draw a circle");
		
		JPanel panel = new DrawClippedCircle2Demo();
		JFrame frame = new JFrame("Draw circle demo");
		frame.setContentPane(panel);
		frame.setSize(400, 300);
		frame.setVisible(true);
		
	}
}
