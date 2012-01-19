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
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Angle2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.Curve2DUtils;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.PolyCurve2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.line.StraightLine2D;

import static java.lang.Math.*;


/**
 * A PolyOrientedCurve2D is a set of piecewise smooth curve arcs, such that the
 * end of a curve is the beginning of the next curve, and such that they do not
 * intersect nor self-intersect.
 * <p>
 * 
 * @author dlegland
 */
public class PolyOrientedCurve2D<T extends ContinuousOrientedCurve2D> extends
        PolyCurve2D<T> implements ContinuousOrientedCurve2D {

    // ===================================================================
    // static constructors

    /**
     * Static factory for creating a new PolyOrientedCurve2D from a collection of
     * curves.
     * @since 0.8.1
     */
    public static <T extends ContinuousOrientedCurve2D> PolyOrientedCurve2D<T>
    create(Collection<T> curves) {
    	return new PolyOrientedCurve2D<T>(curves);
    }
    
    /**
     * Static factory for creating a new PolyOrientedCurve2D from an array of
     * curves.
     * @since 0.8.1
     */
    public static <T extends ContinuousOrientedCurve2D> 
    PolyOrientedCurve2D<T> create(T... curves) {
    	return new PolyOrientedCurve2D<T>(curves);
    }

    /**
     * Static factory for creating a new PolyOrientedCurve2D from an array of
     * curves.
     * @since 0.8.1
     */
    public static <T extends ContinuousOrientedCurve2D> 
    PolyOrientedCurve2D<T> createClosed(T... curves) {
    	return new PolyOrientedCurve2D<T>(curves, true);
    }

    /**
     * Static factory for creating a new PolyOrientedCurve2D from a collection of
     * curves and a flag indicating if the curve is closed or not.
     * @since 0.9.0
     */
    public static <T extends ContinuousOrientedCurve2D> PolyOrientedCurve2D<T>
    create(Collection<T> curves, boolean closed) {
    	return new PolyOrientedCurve2D<T>(curves, closed);
    }
    
    /**
     * Static factory for creating a new PolyOrientedCurve2D from an array of
     * curves and a flag indicating if the curve is closed or not.
     * @since 0.9.0
     */
    public static <T extends ContinuousOrientedCurve2D> 
    PolyOrientedCurve2D<T> create(T[] curves, boolean closed) {
    	return new PolyOrientedCurve2D<T>(curves, closed);
    }

   
    // ===================================================================
    // Constructors

    public PolyOrientedCurve2D() {
        super();
    }

    public PolyOrientedCurve2D(int size) {
        super(size);
    }

    public PolyOrientedCurve2D(T... curves) {
        super(curves);
    }

    public PolyOrientedCurve2D(T[] curves, boolean closed) {
        super(curves, closed);
    }

    public PolyOrientedCurve2D(Collection<? extends T> curves) {
        super(curves);
    }

    public PolyOrientedCurve2D(Collection<? extends T> curves, boolean closed) {
        super(curves, closed);
    }

    
    // ===================================================================
    // Methods specific to PolyOrientedCurve2D

    public double getWindingAngle(Point2D point) {
        double angle = 0;
        for (OrientedCurve2D curve : this.curves)
            angle += curve.getWindingAngle(point);
        return angle;
    }

    public double getSignedDistance(Point2D p) {
        return getSignedDistance(p.getX(), p.getY());
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#getSignedDistance(math.geom2d.Point2D)
     */
    public double getSignedDistance(double x, double y) {
        double dist = this.getDistance(x, y);

        if (this.isInside(new Point2D(x, y)))
            dist = -dist;

        return dist;
    }

    private static Vector2D computeTangent(ContinuousCurve2D curve, double pos) {
        // For smooth curves, simply call the getTangent() method
        if (curve instanceof SmoothCurve2D)
            return ((SmoothCurve2D) curve).getTangent(pos);

        // Extract sub curve and recursively call this method on the sub curve
        if (curve instanceof CurveSet2D<?>) {
            CurveSet2D<?> curveSet = (CurveSet2D<?>) curve;
            double pos2 = curveSet.getLocalPosition(pos);
            Curve2D subCurve = curveSet.getChildCurve(pos);
            return computeTangent((ContinuousCurve2D) subCurve, pos2);
        }

        throw new IllegalArgumentException(
        		"Unknown type of curve: should be either continuous or curveset");
    }

    /**
     * Determines if the given point lies within the domain bounded by this curve.
     */
    public boolean isInside(Point2D point) {
//    	double alpha = Math.abs(this.getWindingAngle(point));
//    	alpha = Math.min(alpha, 4 * PI - alpha);
//    	return alpha > PI ;
        double pos = this.project(point);

        if (!this.isSingular(pos)) {
            // Simply call the method isInside on the child curve
            return this.getChildCurve(pos).isInside(point);
        }
        
        // number of curves
        int n = this.getCurveNumber();

        // vertex index and position
        int i = this.getCurveIndex(pos);
        if (pos / 2 - i > .25)
        	i++;

        // Test case of point equal to last position
        if (round(pos) == 2 * n - 1) {
        	pos = 0;
        	i = 0;
        }

        Point2D vertex = this.getPoint(pos);

        // indices of previous and next curves
        int iPrev = i > 0 ? i - 1 : n - 1;
        int iNext = i;

        // previous and next curves
        T prev = this.curves.get(iPrev);
        T next = this.curves.get(iNext);

        // tangent vectors of the 2 neighbor curves
        Vector2D v1 = computeTangent(prev, prev.getT1());
        Vector2D v2 = computeTangent(next, next.getT0());

        // compute on which side of each ray the test point lies
        boolean in1 = new StraightLine2D(vertex, v1).isInside(point);
        boolean in2 = new StraightLine2D(vertex, v2).isInside(point);

        // check if angle is acute or obtuse
        if (Angle2D.getAngle(v1, v2) < PI) {
        	return in1 && in2;
        } else {
        	return in1 || in2;
        }
    }

    @Override
    public PolyOrientedCurve2D<? extends ContinuousOrientedCurve2D> getReverseCurve() {
        ContinuousOrientedCurve2D[] curves2 = 
        	new ContinuousOrientedCurve2D[curves.size()];
        int n = curves.size();
        for (int i = 0; i < n; i++)
            curves2[i] = curves.get(n-1-i).getReverseCurve();
        return new PolyOrientedCurve2D<ContinuousOrientedCurve2D>(curves2);
    }

    /**
     * Return an instance of PolyOrientedCurve2D.
     */
    @Override
    public PolyOrientedCurve2D<? extends ContinuousOrientedCurve2D> getSubCurve(
            double t0, double t1) {
        PolyCurve2D<?> set = super.getSubCurve(t0, t1);
        PolyOrientedCurve2D<ContinuousOrientedCurve2D> subCurve = 
        	new PolyOrientedCurve2D<ContinuousOrientedCurve2D>();
        subCurve.setClosed(false);

        // convert to PolySmoothCurve by adding curves.
        for (Curve2D curve : set.getCurves())
            subCurve.addCurve((ContinuousOrientedCurve2D) curve);

        return subCurve;
    }

    /**
     * Clip the PolyCurve2D by a box. The result is an instance of CurveSet2D<ContinuousOrientedCurve2D>,
     * which contains only instances of ContinuousOrientedCurve2D. If the
     * PolyCurve2D is not clipped, the result is an instance of CurveSet2D<ContinuousOrientedCurve2D>
     * which contains 0 curves.
     */
    @Override
    public CurveSet2D<? extends ContinuousOrientedCurve2D> clip(Box2D box) {
        // Clip the curve
        CurveSet2D<? extends Curve2D> set = Curve2DUtils.clipCurve(this, box);

        // Stores the result in appropriate structure
        int n = set.getCurveNumber();
        CurveArray2D<ContinuousOrientedCurve2D> result = 
        	new CurveArray2D<ContinuousOrientedCurve2D>(n);

        // convert the result
        for (Curve2D curve : set.getCurves()) {
            if (curve instanceof ContinuousOrientedCurve2D)
                result.addCurve((ContinuousOrientedCurve2D) curve);
        }
        return result;
    }

    @Override
    public PolyOrientedCurve2D<?> transform(AffineTransform2D trans) {
        PolyOrientedCurve2D<ContinuousOrientedCurve2D> result = 
        	new PolyOrientedCurve2D<ContinuousOrientedCurve2D>();
        for (ContinuousOrientedCurve2D curve : curves)
            result.addCurve(curve.transform(trans));
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        // check class
        if (!(obj instanceof CurveSet2D<?>))
            return false;
        // call superclass method
        return super.equals(obj);
    }

}
