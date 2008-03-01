package math.geom2d.conic;

import java.util.*;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.ContinuousBoundary2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Polyline2D;
import math.geom2d.line.StraightObject2D;
import math.geom2d.polygon.Rectangle2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * Branch of an Hyperbola2D.
 */
public class HyperbolaBranch2D implements ContinuousBoundary2D, SmoothCurve2D{

	Hyperbola2D hyperbola = null;
	boolean positive = true;
	
	public HyperbolaBranch2D(Hyperbola2D hyperbola, boolean b){
		this.hyperbola = hyperbola;
		this.positive = b;
	}
	

	// ===================================================================
	// methods inherited from SmoothCurve2D interface

	/**
	 * Use formula given in
	 * <a href="http://mathworld.wolfram.com/Hyperbola.html">http://mathworld.wolfram.com/Hyperbola.html</a>
	 */
	public double getCurvature(double t) {
		double a = hyperbola.a;
		double b = hyperbola.b;
		double sih = Math.sinh(t);
		double coh = Math.sinh(t);
		return a*b/Math.pow(b*b*coh*coh + a*a*sih*sih, 3/2);
	}

	public Vector2D getTangent(double t) {
		double a = hyperbola.a;
		double b = hyperbola.b;
		double theta = hyperbola.theta;
		double dx, dy;
		if(positive){
			dx = a*Math.sinh(t);
			dy = b*Math.cosh(t);
		}else{
			dx = -a*Math.sinh(t);
			dy = -b*Math.cosh(t);
		}
		double cot = Math.cos(theta);
		double sit = Math.sin(theta);
		return new Vector2D(dx*cot-dy*sit, dx*sit+dy*cot);
	}
	
	
	// ===================================================================
	// methods inherited from Boundary2D interface

	/** 
	 * returns an instance of ArrayList<ContinuousBoundary2D> containing only
	 * <code>this</code>.
	 */
	public Collection<ContinuousBoundary2D> getBoundaryCurves() {
		ArrayList<ContinuousBoundary2D> list = new ArrayList<ContinuousBoundary2D>();
		list.add(this);
		return list;
	}

	// ===================================================================
	// methods inherited from OrientedCurve2D interface

	public double getSignedDistance(java.awt.geom.Point2D point) {
		double dist = this.getDistance(point);
		return this.isInside(point)? -dist : dist;
	}

	public double getSignedDistance(double x, double y) {
		return this.getSignedDistance(new Point2D(x, y));
	}

	public double getWindingAngle(java.awt.geom.Point2D point) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isInside(java.awt.geom.Point2D point) {
		if(hyperbola.isDirect()){
			if(hyperbola.isInside(point)) return true;
			double x = hyperbola.toLocal(new Point2D(point)).getX();
			return positive ? x<0 : x>0;
		}else{
			if(!hyperbola.isInside(point)) return false;
			double x = hyperbola.toLocal(new Point2D(point)).getX();
			return positive ? x>0 : x<0;
		}
	}


	// ===================================================================
	// methods inherited from ContinuousCurve2D interface

	/** 
	 * returns an instance of ArrayList<SmoothCurve2D> containing only
	 * <code>this</code>.
	 */
	public Collection<? extends SmoothCurve2D> getSmoothPieces() {
		ArrayList<SmoothCurve2D> list = new ArrayList<SmoothCurve2D>();
		list.add(this);
		return list;
	}

	/** return false, by definition of Hyperbola branch*/
	public boolean isClosed() {
		return false;
	}

	/**
	 * returns a hyperbola arc with -100<t<100 transformed into polyline.
	 */
	public Polyline2D getAsPolyline(int n) {
		return new HyperbolaBranchArc2D(this, -100, 100).getAsPolyline(n);
	}

	public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path) {
		return new HyperbolaBranchArc2D(this, -100, 100).appendPath(path);
	}


	// ===================================================================
	// methods inherited from Curve2D interface

	public Point2D getFirstPoint() {
		return Point2D.INFINITY_POINT;
	}

	public Point2D getLastPoint() {
		return Point2D.INFINITY_POINT;
	}

	public Point2D getPoint(double t) {
		return this.getPoint(t, new Point2D());
	}

	public Point2D getPoint(double t, Point2D point) {
		if(positive)
			point.setLocation(Math.cosh(t), Math.sinh(t));
		else
			point.setLocation(-Math.cosh(t), -Math.sinh(t));
		return hyperbola.toGlobal(point);
	}

	public double getPosition(Point2D point) {
		point = hyperbola.toLocal(point);
		double y = this.positive ? point.getY() : - point.getY();
		return Math.log(y + Math.hypot(y*y, 1));
	}

	public double project(Point2D point) {
		point = hyperbola.toLocal(point);
		double y = this.positive ? point.getY() : - point.getY();
		return Math.log(y + Math.hypot(y*y, 1));
	}

	public HyperbolaBranch2D getReverseCurve() {
		Hyperbola2D hyper2 = new Hyperbola2D(hyperbola.xc, hyperbola.yc, 
				hyperbola.a, hyperbola.b, hyperbola.theta, !hyperbola.direct);
		return new HyperbolaBranch2D(hyper2, positive);
	}

	public Collection<ContinuousCurve2D> getContinuousCurves() {
		ArrayList<ContinuousCurve2D> list = new ArrayList<ContinuousCurve2D>(1);
		list.add(this);
		return list;
	}

	/** 
	 * returns an instance of HyprbolaBranchArc2D initialized with
	 * <code>this</code>.
	 */ 
	public HyperbolaBranchArc2D getSubCurve(double t0, double t1) {
		return new HyperbolaBranchArc2D(this, t0, t1);
	}

	/** returns Double.NEGATIVE_INFINITY.*/
	public double getT0() {
		return Double.NEGATIVE_INFINITY;
	}

	/** returns Double.POSITIVE_INFINITY.*/
	public double getT1() {
		return Double.POSITIVE_INFINITY;
	}

	public Collection<Point2D> getIntersections(StraightObject2D line) {
		// TODO Auto-generated method stub
		return new ArrayList<Point2D>();
	}

	/** Returns a bounding box with infinite bounds in every direction */
	public Box2D getBoundingBox() {
		return new Box2D(
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	/**
	 * Clip the circle arc by a box. The result is an instance of
	 * CurveSet2D<HyperbolaBranchArc2D>, which 
	 * contains only instances of HyperbolaBranchArc2D. If the conic arc is not
	 * clipped, the result is an instance of
	 * CurveSet2D<HyperbolaBranchArc2D> which contains 0 curves.
	 */
	public CurveSet2D<? extends HyperbolaBranchArc2D> clip(Box2D box) {
		// Clip the curve
		CurveSet2D<SmoothCurve2D> set = box.clipSmoothCurve(this);
		
		// Stores the result in appropriate structure
		CurveSet2D<HyperbolaBranchArc2D> result =
			new CurveSet2D<HyperbolaBranchArc2D> ();
		
		// convert the result
		for(Curve2D curve : set.getCurves()){
			if (curve instanceof HyperbolaBranchArc2D)
				result.addCurve((HyperbolaBranchArc2D) curve);
		}
		return result;
	}


	public double getDistance(java.awt.geom.Point2D p) {
		return this.getDistance(p.getX(), p.getY());
	}

	public double getDistance(double x, double y) {
		// TODO Auto-generated method stub
		return 0;
	}

	/** return false, as an hyperbola branch is never bounded.*/
	public boolean isBounded() {
		return false;
	}

	public HyperbolaBranch2D transform(AffineTransform2D trans) {
		return new HyperbolaBranch2D(this.hyperbola.transform(trans), this.positive);
	}

	
	// ===================================================================
	// methods inherited from Shape interface

	public boolean contains(java.awt.geom.Point2D point) {
		return this.contains(point.getX(), point.getY());
	}

	public boolean contains(double x, double y) {
		if(!hyperbola.contains(x, y)) return false;
		Point2D point = hyperbola.toLocal(new Point2D(x, y));
		return point.getX()>0;
	}

	/** return false, as an hyperbola branch can not contain a rectangle.*/
	public boolean contains(java.awt.geom.Rectangle2D rect) {
		return false;
	}

	/** return false, as an hyperbola branch can not contain a rectangle.*/
	public boolean contains(double xr, double yr, double wr, double hr) {
		return false;
	}

	public boolean intersects(java.awt.geom.Rectangle2D rect) {
		return this.intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	/**
	 * Returns true if one of the edges of the rectangle intersects the curve.
	 */
	public boolean intersects(double xr, double yr, double wr, double hr) {
		for(LineSegment2D edge : new Rectangle2D(xr, yr, wr, hr).getEdges())
			if(this.getIntersections(edge).size()>0) 
				return true;
		return false;
	}

	public java.awt.Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public java.awt.geom.Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * compute path of a HyperbolaBranchArc2D with -100<t<100.
	 */
	public java.awt.geom.GeneralPath getGeneralPath(){
		return new HyperbolaBranchArc2D(this, -100, 100).getGeneralPath();
	}
	
	/**
	 * return path iterator of a HyperbolaBranchArc2D with -100<t<100.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans) {
		return getGeneralPath().getPathIterator(trans);
	}

	/**
	 * return path iterator of a HyperbolaBranchArc2D with -100<t<100.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans, double flatness) {
		return getGeneralPath().getPathIterator(trans, flatness);
	}
}