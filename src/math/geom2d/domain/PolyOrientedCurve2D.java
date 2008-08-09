/* file : PolyOrientedCurve2D.java
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
 * Created on 1 mai 2006
 *
 */
package math.geom2d.domain;

// Imports
import math.geom2d.Box2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.Curve2DUtils;
import math.geom2d.curve.PolyCurve2D;
import math.geom2d.transform.AffineTransform2D;

import java.util.*;

/**
 * A PolyOrientedCurve2D is a set of piecewise smooth curve arcs, such that the
 * end of a curve is the beginning of the next curve, and such that they do
 * not intersect nor self-intersect.<p>
 * @author dlegland
 */
public class PolyOrientedCurve2D<T extends ContinuousOrientedCurve2D> 
		extends PolyCurve2D<T>
		implements ContinuousOrientedCurve2D {
	
	public PolyOrientedCurve2D(){
		super();
	}
	
	public PolyOrientedCurve2D(T[] curves){
		super(curves);
	}

	public PolyOrientedCurve2D(Collection<? extends T> curves) {
		super(curves);
	}

	
	public double getWindingAngle(java.awt.geom.Point2D point) {
		double angle=0;
		for(OrientedCurve2D curve : this.curves)
			angle += curve.getWindingAngle(point);
		return angle;
	}

	public double getSignedDistance(java.awt.geom.Point2D p){
		return getSignedDistance(p.getX(), p.getY());
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#getSignedDistance(math.geom2d.Point2D)
	 */
	public double getSignedDistance(double x, double y) {
		double minDist = Double.POSITIVE_INFINITY;
		double dist = Double.POSITIVE_INFINITY;
		
		for(OrientedCurve2D curve : this.getCurves()){
			dist = curve.getSignedDistance(x, y);
			if(Math.abs(dist)<Math.abs(minDist))
				minDist = dist;
		}		
		return minDist;
	}
	
	public boolean isInside(java.awt.geom.Point2D point){
		return this.getSignedDistance(point.getX(), point.getY())<0;
	}
	
	public PolyOrientedCurve2D<? extends ContinuousOrientedCurve2D> getReverseCurve(){
		ContinuousOrientedCurve2D[] curves2 = 
			new ContinuousOrientedCurve2D[curves.size()];
		int n=curves.size();
		for(int i=0; i<n; i++)
			curves2[i] = curves.get(n-1-i).getReverseCurve();
		return new PolyOrientedCurve2D<ContinuousOrientedCurve2D>(curves2);
	}

	/** 
	 * Return an instance of PolyOrientedCurve2D. 
	 */
	public PolyOrientedCurve2D <? extends ContinuousOrientedCurve2D>
			getSubCurve(double t0, double t1){
		PolyCurve2D<?> set = (PolyCurve2D<?>) super.getSubCurve(t0, t1);
		PolyOrientedCurve2D<ContinuousOrientedCurve2D> subCurve = 
			new PolyOrientedCurve2D<ContinuousOrientedCurve2D>();
		subCurve.setClosed(false);
		
		// convert to PolySmoothCurve by adding curves.
		for(Curve2D curve : set.getCurves())
			subCurve.addCurve((ContinuousOrientedCurve2D) curve);
		
		return subCurve;
	}
	
	/**
	 * Clip the PolyCurve2D by a box. The result is an instance of
	 * CurveSet2D<ContinuousOrientedCurve2D>, which 
	 * contains only instances of ContinuousOrientedCurve2D. 
	 * If the PolyCurve2D is not clipped, the result is an instance of
	 * CurveSet2D<ContinuousOrientedCurve2D> which contains 0 curves.
	 */
	@Override
	public CurveSet2D<? extends ContinuousOrientedCurve2D> clip(Box2D box) {
		// Clip the curve
		CurveSet2D<Curve2D> set = Curve2DUtils.clipCurve(this, box);
		
		// Stores the result in appropriate structure
		CurveSet2D<ContinuousOrientedCurve2D> result =
			new CurveSet2D<ContinuousOrientedCurve2D> ();
		
		// convert the result
		for(Curve2D curve : set.getCurves()){
			if (curve instanceof ContinuousOrientedCurve2D)
				result.addCurve((ContinuousOrientedCurve2D) curve);
		}
		return result;
	}

	
	public PolyOrientedCurve2D<?> transform(AffineTransform2D trans) {
		PolyOrientedCurve2D<ContinuousOrientedCurve2D> result = 
			new PolyOrientedCurve2D<ContinuousOrientedCurve2D>();
		for(ContinuousOrientedCurve2D curve : curves)
			result.addCurve(curve.transform(trans));
		return result;
	}
}