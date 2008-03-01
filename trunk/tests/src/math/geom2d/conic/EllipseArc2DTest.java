/* file : EllipseArc2DTest.java
 * 
 * Project : geometry
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
 * Created on 7 mars 2007
 *
 */
package math.geom2d.conic;

import junit.framework.TestCase;

public class EllipseArc2DTest extends TestCase {

	/**
	 * Create ellipse arcs with various constructors, and test if they are
	 * equal between them.
	 */
	public void testEllipseArc2D() {
		// direct ellipse
		EllipseArc2D arc0 = new EllipseArc2D();
		Ellipse2D ellipse = new Ellipse2D(0, 0, 1, 1, 0, true);
		EllipseArc2D arc1 = new EllipseArc2D(ellipse, 0, Math.PI/2);
		EllipseArc2D arc2 = new EllipseArc2D(ellipse, 0, Math.PI/2, true);
		EllipseArc2D arc3 = new EllipseArc2D(0, 0, 1, 1, 0, 0, Math.PI/2);
		EllipseArc2D arc4 = new EllipseArc2D(0, 0, 1, 1, 0, 0, Math.PI/2, true);
		assertTrue(arc1.equals(arc0));
		assertTrue(arc1.equals(arc2));
		assertTrue(arc1.equals(arc3));
		assertTrue(arc1.equals(arc4));

		// direct ellipse, with different angles
		ellipse = new Ellipse2D(0, 0, 1, 1, 0, true);
		arc1 = new EllipseArc2D(ellipse, Math.PI/2, Math.PI/2);
		arc2 = new EllipseArc2D(ellipse, Math.PI/2, Math.PI, true);
		arc3 = new EllipseArc2D(0, 0, 1, 1, 0, Math.PI/2, Math.PI/2);
		arc4 = new EllipseArc2D(0, 0, 1, 1, 0, Math.PI/2, Math.PI, true);
		assertTrue(arc1.equals(arc2));
		assertTrue(arc1.equals(arc3));
		assertTrue(arc1.equals(arc4));
		
		// indirect ellipse, with different angles
		ellipse = new Ellipse2D(0, 0, 1, 1, 0, true);
		arc1 = new EllipseArc2D(ellipse, Math.PI/2, -Math.PI/2);
		arc2 = new EllipseArc2D(ellipse, Math.PI/2, 2*Math.PI, false);
		arc3 = new EllipseArc2D(0, 0, 1, 1, 0, Math.PI/2, -Math.PI/2);
		arc4 = new EllipseArc2D(0, 0, 1, 1, 0, Math.PI/2, 2*Math.PI, false);
		assertTrue(arc1.equals(arc2));
		assertTrue(arc1.equals(arc3));
		assertTrue(arc1.equals(arc4));
	}

}