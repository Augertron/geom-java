/* file : CheckRotateParabola2D.java
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

package math.geom2d.conic;

import java.awt.*;
import javax.swing.*;

import math.geom2d.*;
import math.geom2d.conic.Parabola2D;
import math.geom2d.transform.AffineTransform2D;


public class CheckRotateParabola2D extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	double x0 = 150;
	double y0 = 150;
	double a  = .1;

	Parabola2D parabola = null;
	Box2D box = null;
	
	public CheckRotateParabola2D() {
		super();
		
		parabola = new Parabola2D(x0, y0+30, a, 0);
		
		box = new Box2D(50, 250, 50, 250);
	
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
			

		g2.setColor(Color.BLACK);
		g2.draw(box.getAsRectangle());

		for(int i=0; i<8; i++){
			double theta = i*Math.PI*2/8;
			AffineTransform2D rot = AffineTransform2D.createRotation(x0, y0, theta);
			Parabola2D rotated = parabola.transform(rot);

			g2.setColor(Color.CYAN);
			rotated.getDomain().clip(box).fill(g2);
			g2.setColor(Color.BLUE);
			rotated.clip(box).draw(g2);
		}
		
		// Draw parabola origin
		Point2D p1 = parabola.getPoint(0);
		p1.draw(g2, 4);
	}

	public final static void main(String[] args){
		System.out.println("should draw a parabola");
		
		JPanel panel = new CheckRotateParabola2D();
		JFrame frame = new JFrame("Check rotations of parabola");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
		
	}
}