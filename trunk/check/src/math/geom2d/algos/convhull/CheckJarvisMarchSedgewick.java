/* file : CheckJarvisMarchSedgewick.java
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

package math.geom2d.algos.convhull;

import java.awt.*;
import javax.swing.*;

import math.geom2d.*;
import math.geom2d.conic.Circle2D;
import math.geom2d.polygon.Polygon2D;

/**
 * Check the transformation of Ellipses by affine transforms.
 * @author dlegland
 *
 */
public class CheckJarvisMarchSedgewick extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	PointSet2D points;
	Polygon2D hull;

	
	public CheckJarvisMarchSedgewick() {
		super();
		
		// Point coordinate are multiplied by 10 for better drawing
		points = new PointSet2D(new Point2D[]{
				new Point2D(30, 90), 
				new Point2D(110, 10), 
				new Point2D(60, 80), 
				new Point2D(40, 30), 
				new Point2D(50, 150), 
				new Point2D(80, 110), 
				new Point2D(10, 60), 
				new Point2D(70, 40), 
				new Point2D(90, 70), 
				new Point2D(140, 50), 
				new Point2D(100, 130), 
				new Point2D(160, 140), 
				new Point2D(150, 20), 
				new Point2D(130, 160), 
				new Point2D(30, 120), 
				new Point2D(120, 100)});
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLACK);
		for(Point2D point : points){
			g2.fill(new Circle2D(point, 2));
		}
		
		Polygon2D hull = new JarvisMarch().convexHull(points);
		g2.setColor(Color.BLUE);
		g2.draw(hull);
	}

	public final static void main(String[] args){
		System.out.println("Check convex hull by Jarvis march");
		
		JPanel panel = new CheckJarvisMarchSedgewick();
		JFrame frame = new JFrame("Convex hull by Jarvis march");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
		
	}
}
