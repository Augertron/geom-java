/*
 * File : LineSegment2DTest.java
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
 * author : Legland
 * Created on 29 d�c. 2003
 */
package math.geom2d;

import junit.framework.TestCase;

/**
 * @author Legland
 */
public class LineSegment2DTest extends TestCase {

	/**
	 * Constructor for Edge2DTest.
	 * @param arg0
	 */
	public LineSegment2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(LineSegment2DTest.class);
	}

	/*
	 * Test for double getDistance(Point2D)
	 */
	public void testGetDistanceDoubleDouble() {
		
		// basic
		LineSegment2D edge = new LineSegment2D(1, 1, 4, 3);
		
		assertEquals(edge.getDistance(1, 1), 0, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2.5, 2), 0, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(4, 3), 0, Shape2D.ACCURACY);
	
		double d1 = Math.sqrt(13)/2;
		assertEquals(edge.getDistance(-.5, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2, -.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(3.5, .5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(5, 1.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(5.5, 4), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(3, 4.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(1.5, 3.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(0, 2.5), d1, Shape2D.ACCURACY);

		double d2 = Math.sqrt(26)/2;
		assertEquals(edge.getDistance(0.5, -1.5), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(-1.5, 1.5), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(6.5, 2.5), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(4.5, 5.5), d2, Shape2D.ACCURACY);
		
		// horizontal edge
		edge = new LineSegment2D(1, 1, 4, 1);
		assertEquals(edge.getDistance(1, 1), 0, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2.5, 1), 0, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(4, 1), 0, Shape2D.ACCURACY);
		
		d1 = 1;
		assertEquals(edge.getDistance(0, 1), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(5, 1), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(1, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2.5, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(4, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(1, 2), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2.5, 2), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(4, 2), d1, Shape2D.ACCURACY);
		
		d2=Math.sqrt(2);
		assertEquals(edge.getDistance(0, 0), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(0, 2), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(5, 0), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(5, 2), d2, Shape2D.ACCURACY);
		
		// vertical edge
		edge = new LineSegment2D(1, 1, 1, 4);
		assertEquals(edge.getDistance(1, 1), 0, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(1, 2.5), 0, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(1, 4), 0, Shape2D.ACCURACY);
		
		assertEquals(edge.getDistance(1, 0), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(1, 5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(0, 1), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(0, 2.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(0, 4), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2, 1), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2, 2.5), d1, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2, 4), d1, Shape2D.ACCURACY);

		assertEquals(edge.getDistance(0, 0), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2, 0), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(2, 5), d2, Shape2D.ACCURACY);
		assertEquals(edge.getDistance(0, 5), d2, Shape2D.ACCURACY);
	}

	/*
	 * Test for double getSignedDistance(Point2D)
	 */
	public void testGetSignedDistancePoint2D() {
	}

	public void testGetEdgeAngleLineSegment2DLineSegment2D(){
		Point2D p1, p2, p3;
		p1 = new Point2D(0, 0);
		p2 = new Point2D(50, 50);
		p3 = new Point2D(100, 50);
		
		LineSegment2D edge1d = new LineSegment2D(p1, p2);
		LineSegment2D edge1i = new LineSegment2D(p2, p1);
		LineSegment2D edge2d = new LineSegment2D(p2, p3);
		LineSegment2D edge2i = new LineSegment2D(p3, p2);
		
		assertEquals(LineSegment2D.getEdgeAngle(edge1d, edge2d), Math.PI*3/4, Shape2D.ACCURACY);
		assertEquals(LineSegment2D.getEdgeAngle(edge1d, edge2i), Math.PI*3/4, Shape2D.ACCURACY);
		assertEquals(LineSegment2D.getEdgeAngle(edge1i, edge2d), Math.PI*3/4, Shape2D.ACCURACY);
		assertEquals(LineSegment2D.getEdgeAngle(edge1i, edge2i), Math.PI*3/4, Shape2D.ACCURACY);

		assertEquals(LineSegment2D.getEdgeAngle(edge2d, edge1d), Math.PI*5/4, Shape2D.ACCURACY);
		assertEquals(LineSegment2D.getEdgeAngle(edge2d, edge1i), Math.PI*5/4, Shape2D.ACCURACY);
		assertEquals(LineSegment2D.getEdgeAngle(edge2i, edge1d), Math.PI*5/4, Shape2D.ACCURACY);
		assertEquals(LineSegment2D.getEdgeAngle(edge2i, edge1i), Math.PI*5/4, Shape2D.ACCURACY);
	}
	
	/*
	 * Test for Point2D getIntersection(StraightObject2D)
	 */
	public void testGetIntersectionStraightObject2D(){
		LineSegment2D edge1 = new LineSegment2D(1, 1, 3, 2);
		LineSegment2D edge2 = new LineSegment2D(1, 1, 0, 4);
		assertTrue(edge1.getIntersection(edge2).equals(new Point2D(1, 1)));
		assertTrue(edge2.getIntersection(edge1).equals(new Point2D(1, 1)));
		
		LineSegment2D edge3 = new LineSegment2D(3, 2, 0, 4);
		assertTrue(edge1.getIntersection(edge3).equals(new Point2D(3, 2)));
		assertTrue(edge3.getIntersection(edge1).equals(new Point2D(3, 2)));
		assertTrue(edge2.getIntersection(edge3).equals(new Point2D(0, 4)));
		assertTrue(edge3.getIntersection(edge2).equals(new Point2D(0, 4)));
		
		LineSegment2D edge4 = new LineSegment2D(0, 0, 5, 1);
		assertEquals(edge1.getIntersection(edge4), null);
		assertEquals(edge2.getIntersection(edge4), null);
		assertEquals(edge3.getIntersection(edge4), null);
		
		edge1 = new LineSegment2D(1, 1, 5, 5);
		edge2 = new LineSegment2D(1, 5, 5, 1);
		assertTrue(edge1.getIntersection(edge2).equals(new Point2D(3, 3)));
		assertTrue(edge2.getIntersection(edge1).equals(new Point2D(3, 3)));
	}

	/*
	 * Test for boolean contains(double, double)
	 */
	public void testContainsdoubledouble() {
		LineSegment2D edge;
		
		// diagonal edge
		edge = new LineSegment2D(1, 1, 3, 2);
		assertTrue(edge.contains(1, 1));
		assertTrue(edge.contains(3, 2));
		assertTrue(edge.contains(2, 1.5));
		assertTrue(!edge.contains(0, 0));
		assertTrue(!edge.contains(-1, 0));
		assertTrue(!edge.contains(5, 3));
		
		// horizontal edge
		edge = new LineSegment2D(1, 1, 3, 1);
		assertTrue(edge.contains(1, 1));
		assertTrue(edge.contains(3, 1));
		assertTrue(edge.contains(2, 1));
		assertTrue(!edge.contains(0, 0));
		assertTrue(!edge.contains(0, 1));
		assertTrue(!edge.contains(4, 1));

		// vertical edge
		edge = new LineSegment2D(1, 1, 1, 3);
		assertTrue(edge.contains(1, 1));
		assertTrue(edge.contains(1, 3));
		assertTrue(edge.contains(1, 2));
		assertTrue(!edge.contains(0, 0));
		assertTrue(!edge.contains(1, 0));
		assertTrue(!edge.contains(1, 4));
	}

	public void testGetBounds2D() {
		LineSegment2D edge;		

		// diagonal edge
		edge = new LineSegment2D(1, 1, 3, 2);
		HRectangle2D rect = (HRectangle2D) edge.getBounds2D();
		assertTrue(rect.equals(new HRectangle2D(1, 1, 2, 1)));
	}

	public void testEqualsLineSegment2D(){
		LineSegment2D edge1 = new LineSegment2D(1, 2, 3, 4);
		assertTrue(edge1.equals(edge1));
		LineSegment2D edge2 = new LineSegment2D(3, 4, 1, 2);
		assertTrue(edge1.equals(edge2));
		assertTrue(edge2.equals(edge1));
		
		LineSegment2D edge3 = new LineSegment2D(1, 4, 3, 2);
		assertTrue(!edge1.equals(edge3));
		assertTrue(!edge2.equals(edge3));
		assertTrue(!edge3.equals(edge1));
		assertTrue(!edge3.equals(edge2));
		
		LineSegment2D edge4 = new LineSegment2D(1, 2, 3, 2);
		assertTrue(!edge1.equals(edge4));
		assertTrue(!edge2.equals(edge4));
		assertTrue(!edge4.equals(edge1));
		assertTrue(!edge4.equals(edge2));		
	}

	public void testGetViewAnglePoint2D(){
		
		Point2D p1 = new Point2D(1, 1);
		Point2D p2 = new Point2D(3, 1);
		Point2D p3 = new Point2D(1, 1);
		Point2D p4 = new Point2D(1, 3);
		
		LineSegment2D edge1 = new LineSegment2D(2, 0, 2, 2);
		assertEquals(edge1.getWindingAngle(p1), Math.PI/2, 1e-14);
		assertEquals(edge1.getWindingAngle(p2), -Math.PI/2, 1e-14);
		
		LineSegment2D edge2 = new LineSegment2D(2, 2, 2, 0);
		assertEquals(edge2.getWindingAngle(p1), -Math.PI/2, 1e-14);
		assertEquals(edge2.getWindingAngle(p2), Math.PI/2, 1e-14);

		LineSegment2D edge3 = new LineSegment2D(0, 2, 2, 2);
		assertEquals(edge3.getWindingAngle(p3), -Math.PI/2, 1e-14);
		assertEquals(edge3.getWindingAngle(p4), Math.PI/2, 1e-14);

		LineSegment2D edge4 = new LineSegment2D(2, 2, 0, 2);
		assertEquals(edge4.getWindingAngle(p3), Math.PI/2, 1e-14);
		assertEquals(edge4.getWindingAngle(p4), -Math.PI/2, 1e-14);
		
	}
}
