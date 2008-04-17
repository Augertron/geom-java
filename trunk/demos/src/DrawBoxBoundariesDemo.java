/* file : DrawBoxBoundariesDemo.java
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

import math.geom2d.Box2D;
import math.geom2d.polygon.HRectangle2D;
import math.geom2d.transform.AffineTransform2D;


public class DrawBoxBoundariesDemo extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	Box2D[][] boxes;	
	
	public DrawBoxBoundariesDemo() {
		super();
		
		double[] x0 = {20, 20, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
		double[] x1 = {30, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 30};
		double[] y0 = {20, 20, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
		double[] y1 = {30, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 30};
		
		boxes = new Box2D[4][4];
		for(int x=0; x<4; x++)
			for(int y=0; y<4; y++)
				boxes[y][x] = new Box2D(x0[x], x1[x], y0[y], y1[y]);
		
		this.setBackground(Color.WHITE);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		Graphics2D g2 = (Graphics2D) g;

		int x0 = 10;
		int y0 = 10;
		int sx = 2;
		int sy = 2;
		int w  = 50;
		int h  = 50;
		
		
		AffineTransform2D user2Display = AffineTransform2D.createScaling(sx, sy);
		user2Display = AffineTransform2D.createTranslation(x0, y0).compose(user2Display);
		
		Box2D clippingBox = new Box2D(10, 40, 10, 40);
		
		g2.setColor(Color.WHITE);
		g2.fillRect(x0, y0, w*sx*4, h*sy*4);
		
		g2.setColor(Color.BLACK);
		g2.draw(new HRectangle2D(0, 0, 4*w, 4*h).transform(user2Display));
		g2.draw(new HRectangle2D(w, 0, 2*w, 4*h).transform(user2Display));
		g2.draw(new HRectangle2D(w, 0, 1*w, 4*h).transform(user2Display));
		g2.draw(new HRectangle2D(0, h, 4*w, 2*h).transform(user2Display));
		g2.draw(new HRectangle2D(0, h, 4*w, 1*h).transform(user2Display));
		
		g2.setColor(Color.BLUE);
		Box2D box;
		AffineTransform2D tra;
		
		Box2D clip;
		HRectangle2D rect;
		for(int y=0; y<4; y++)
			for(int x=0; x<4; x++){
				box = boxes[y][x];
				clip = box.intersection(clippingBox);
				rect = clip.getAsRectangle();
				tra = AffineTransform2D.createTranslation(x*w, y*h);
				g2.setColor(Color.CYAN);
				g2.fill(rect.transform(tra).transform(user2Display));
				g2.setColor(Color.BLUE);
				g2.draw(rect.transform(tra).transform(user2Display));
				g2.setColor(Color.LIGHT_GRAY);
				g2.draw(clippingBox.getAsRectangle().transform(tra).transform(user2Display));
			}
		
//		Shape2D bnd;
//		for(int y=0; y<4; y++)
//			for(int x=0; x<4; x++){
//				box = boxes[y][x];
//				tra = AffineTransform2D.createTranslation(x*w, y*h);
//
//				g2.setColor(Color.GRAY);
//				g2.draw(boxes[0][0].getAsRectangle().transform(tra).transform(user2Display));
//				g2.setColor(Color.BLUE);
//				g2.draw(box.getBoundary().clip(clippingBox).transform(tra).transform(user2Display));
//				g2.setColor(Color.GRAY);
//				g2.draw(clippingBox.getAsRectangle().transform(tra).transform(user2Display));
//			}
	}

	public final static void main(String[] args){
		JPanel panel = new DrawBoxBoundariesDemo();
		JFrame frame = new JFrame("Draw box boundaries demo");
		frame.setContentPane(panel);
		frame.setSize(500, 500);
		frame.setVisible(true);
		
	}
}
