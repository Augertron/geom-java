/* file : CurveSet2DTest.java
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
 * Created on 25 mars 2007
 *
 */
package math.geom2d.curve;

import junit.framework.TestCase;
import java.util.*;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.conic.CircleArc2D;


public class CurveSet2DTest extends TestCase {

	public void testGetPosition() {
		double r = 10;
		CircleArc2D arc1 = new CircleArc2D(0, 0, r, 5*Math.PI/3, 2*Math.PI/3);
		CircleArc2D arc2 = new CircleArc2D(r, 0, r, 2*Math.PI/3, 2*Math.PI/3);		
		CurveSet2D<CircleArc2D> set = new CurveSet2D<CircleArc2D>();
		set.addCurve(arc1);
		set.addCurve(arc2);
		
		assertEquals(set.getCurveNumber(), 2);
		assertEquals(set.getT0(), 0, 1e-14);
		assertEquals(set.getT1(), 3, 1e-14);
		
		double pos1 = .3;
		Point2D point1 = set.getPoint(pos1);
		assertEquals(set.getPosition(point1), pos1, 1e-10);

		pos1 = 1.2;
		point1 = set.getPoint(pos1);
		assertEquals(set.getPosition(point1), 1, 1e-10);

		pos1 = 1.6;
		point1 = set.getPoint(pos1);
		assertEquals(set.getPosition(point1), 2, 1e-10);

		pos1 = 2.3;
		point1 = set.getPoint(pos1);
		assertEquals(set.getPosition(point1), pos1, 1e-10);
	}
	
	public void testGetLocalPosition() {
		double r = 10;
		double extent = 2*Math.PI/3;
		CircleArc2D arc1 = new CircleArc2D(0, 0, r, 5*Math.PI/3, extent);
		CircleArc2D arc2 = new CircleArc2D(r, 0, r, 2*Math.PI/3, extent);	
		CurveSet2D<CircleArc2D> set = new CurveSet2D<CircleArc2D>();
		set.addCurve(arc1);
		set.addCurve(arc2);
		
		assertEquals(set.getLocalPosition(0), 0, Shape2D.ACCURACY);
		assertEquals(set.getLocalPosition(1), extent, Shape2D.ACCURACY);
		assertEquals(set.getLocalPosition(2), 0, Shape2D.ACCURACY);
		assertEquals(set.getLocalPosition(3), extent, Shape2D.ACCURACY);
	}

	public void testGetGlobalPosition() {
		double r = 10;
		double extent = 2*Math.PI/3;
		CircleArc2D arc1 = new CircleArc2D(0, 0, r, 5*Math.PI/3, extent);
		CircleArc2D arc2 = new CircleArc2D(r, 0, r, 2*Math.PI/3, extent);	
		CurveSet2D<CircleArc2D> set = new CurveSet2D<CircleArc2D>();
		set.addCurve(arc1);
		set.addCurve(arc2);
		
		assertEquals(set.getGlobalPosition(0, 0), 0, Shape2D.ACCURACY);
		assertEquals(set.getGlobalPosition(0, extent), 1, Shape2D.ACCURACY);
		assertEquals(set.getGlobalPosition(1, 0), 2, Shape2D.ACCURACY);
		assertEquals(set.getGlobalPosition(1, extent), 3, Shape2D.ACCURACY);
	}
	
	/*
	 * Test method for 'math.geom2d.CurveSet2D.getSubCurve(double, double)'
	 */
	public void testGetSubCurve() {
		double r = 10;
		CircleArc2D arc1 	= new CircleArc2D(0, 0, r, 5*Math.PI/3, 2*Math.PI/3);
		CircleArc2D arc2 	= new CircleArc2D(r, 0, r, 2*Math.PI/3, 2*Math.PI/3);
		CircleArc2D arc1h1 	= new CircleArc2D(0, 0, r, 0, 			Math.PI/3);
		CircleArc2D arc2h1 	= new CircleArc2D(r, 0, r, 2*Math.PI/3, Math.PI/3);
		CircleArc2D arc1h2 	= new CircleArc2D(0, 0, r, 5*Math.PI/3, Math.PI/3);
		CircleArc2D arc2h2 	= new CircleArc2D(r, 0, r, Math.PI, 	Math.PI/3);
		
		CurveSet2D<CircleArc2D> set = new CurveSet2D<CircleArc2D>();
		set.addCurve(arc1);
		set.addCurve(arc2);
		
		Curve2D sub1 = set.getSubCurve(0, 2);
		assertTrue(sub1 instanceof CurveSet2D);
		CurveSet2D<?> subset = (CurveSet2D<?>) sub1;
		assertEquals(subset.getCurveNumber(), 2);
		
		double pos0 = set.getPosition(new Point2D(r, 0));
		double pos1 = set.getPosition(new Point2D(0, 0));
		sub1 = set.getSubCurve(pos0, pos1);
		assertTrue(sub1 instanceof CurveSet2D);
		subset = (CurveSet2D<?>) sub1;
		assertEquals(subset.getCurveNumber(), 2);
		Iterator<?> iter = subset.getCurves().iterator();
		sub1 = (Curve2D) iter.next();
		assertTrue(arc1h1.equals(sub1));
		sub1 = (Curve2D) iter.next();
		assertTrue(arc2h1.equals(sub1));
		
		sub1 = set.getSubCurve(pos1, pos0);
		assertTrue(sub1 instanceof CurveSet2D);
		subset = (CurveSet2D<?>) sub1;
		assertEquals(subset.getCurveNumber(), 2);
		iter = subset.getCurves().iterator();
		sub1 = (Curve2D) iter.next();
		assertTrue(arc2h2.equals(sub1));
		sub1 = (Curve2D) iter.next();
		assertTrue(arc1h2.equals(sub1));
	}

}