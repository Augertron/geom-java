/* file : ClosedPolyline2D.java
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
 * Created on 16 avr. 2007
 *
 */
package math.geom2d.line;

import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.curve.ContinuousBoundary2D;
import math.geom2d.transform.AffineTransform2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Extends Polyline2D, by considering the last point is connected to the first one.
 * A ClosedPolyline2D can be used as boundary for Polygons.
 * @author dlegland
 */
public class ClosedPolyline2D extends Polyline2D implements
		ContinuousBoundary2D {

	public ClosedPolyline2D() {
		super();
	}

	public ClosedPolyline2D(Point2D initialPoint) {
		super(initialPoint);
	}

	public ClosedPolyline2D(Point2D[] points) {
		super(points);
	}

	public ClosedPolyline2D(double[] xcoords, double[] ycoords) {
		super(xcoords, ycoords);
	}

	public ClosedPolyline2D(Collection<? extends Point2D> points) {
		super(points);
	}


	
	// ===================================================================
	// Methods specific to ClosedPolyline2D
	
	/**
	 * Computes area of the polyline, by returning the absolute value of the
	 * signed area.
	 */
	public double getArea(){
		return Math.abs(this.getSignedArea());
	}
	
	/**
	 * Computes the signed area of the polyline. Algorithm is taken from page:
	 * <a href="http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/">
	 * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/</a>.
	 * Signed are is positive if polyline is oriented counter-clockwise, and
	 * negative otherwise. Result is wrong if polyline is self-intersecting.
	 * @return the signed area of the polyline.
	 */
	public double getSignedArea(){
		double area = 0;
		Point2D prev = this.points.get(this.points.size()-1);
		Point2D point;
		for(int i=0; i<points.size(); i++){
			point = this.points.get(i);
			area += prev.getX()*point.getY() - prev.getY()*point.getX();
			prev = point;
		}
		return area /= 2;
	}
	
	// ===================================================================
	// Methods specific to Polyline2D

	/**
	 * return an array of LineSegment2D. The number of edges is the sazme as 
	 * the number of vertices.
	 * @return the edges of the polyline
	 */
	public Collection<LineSegment2D> getEdges(){
		int n = points.size();
		ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>();
		
		if(n<2)	return edges;
		
		for(int i=0; i<n-1; i++)
			edges.add(new LineSegment2D(points.get(i), points.get(i+1)));
		edges.add(new LineSegment2D(points.get(n-1), points.get(0)));
		return edges;
	}

	
	// ===================================================================
	// Methods inherited from Boundary2D
	
	public Collection<ContinuousBoundary2D> getBoundaryCurves(){
		ArrayList<ContinuousBoundary2D> list = new ArrayList<ContinuousBoundary2D>(1);
		list.add(this);
		return list;
	}

	
	// ===================================================================
	// Methods inherited from interface OrientedCurve2D
	
	/* (non-Javadoc)
	 * @see math.geom2d.OrientedCurve2D#getSignedDistance(double, double)
	 */
	public double getSignedDistance(double x, double y) {
		double minDist = Double.POSITIVE_INFINITY;
		double dist = Double.POSITIVE_INFINITY;
		
		for(LineSegment2D edge : this.getEdges()){
			dist = edge.getSignedDistance(x, y);
			if(Math.abs(dist)<Math.abs(minDist))
				minDist = dist;
		}		
		return minDist;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.OrientedCurve2D#getSignedDistance(java.awt.geom.Point2D)
	 */
	public double getSignedDistance(java.awt.geom.Point2D point) {
		return getSignedDistance(point.getX(), point.getY());
	}

	/* (non-Javadoc)
	 * @see math.geom2d.OrientedCurve2D#getWindingAngle(java.awt.geom.Point2D)
	 */
	public double getWindingAngle(java.awt.geom.Point2D point) {
		double angle = 0;
		for(LineSegment2D edge : this.getEdges())
			angle += edge.getWindingAngle(point);		
		return angle;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.OrientedCurve2D#isInside(java.awt.geom.Point2D)
	 */
	public boolean isInside(java.awt.geom.Point2D point) {

		Point2D p1 = this.getPoint(points.size()-1);
		Point2D p2;
		double alpha;
		double angle = 0;
		
		for (int i=0; i<this.points.size(); i++) {
			p2 = (Point2D) this.points.get(i);
			alpha = Angle2D.getAngle(p1, point, p2);
			if(alpha>Math.PI) alpha -=Math.PI*2;
			angle += alpha;
//		      p1.h = polygon[i].h - p.h;
//		      p1.v = polygon[i].v - p.v;
//		      p2.h = polygon[(i+1)%n].h - p.h;
//		      p2.v = polygon[(i+1)%n].v - p.v;
//		      angle += Angle2D(p1.h,p1.v,p2.h,p2.v);
			p1 = p2;
		}
		return angle > 0;			
	}

	// ===================================================================
	// Methods inherited from interface ContinuousCurve2D

	/**
	 * return true, by definition.
	 */
	public boolean isClosed() {
		return true;
	}

	
	// ===================================================================
	// Methods inherited from interface Curve2D


	/* (non-Javadoc)
	 * @see math.geom2d.Curve2D#getPoint(double, math.geom2d.Point2D)
	 */
	/**
	 * return point from position as double. Position t can be from 0 to n, 
	 * with n equal to the number of vertices of the polyline.
	 */
	public math.geom2d.Point2D getPoint(double t, math.geom2d.Point2D point) {
		if(point==null) point = new Point2D();
		
		// format position to stay between limits
		double t0 	= this.getT0();
		double t1 	= this.getT1();		
		t = Math.max(Math.min(t, t1), t0);
		
		int n		= points.size();

		// index of vertex before point
		int ind0 	= (int) Math.floor(t+Shape2D.ACCURACY);
		double tl = t-ind0;

		if(ind0==n) ind0 = 0;
		Point2D p0 	= points.get(ind0);
		
		// check if equal to a vertex
		if(Math.abs(t-ind0)<Shape2D.ACCURACY){
			point.setLocation(p0);
			return point;
		}		
		
		// index of vertex after point
		int ind1	= ind0+1;
		if(ind1==n) ind1 = 0;
		Point2D p1 	= points.get(ind1);
		
		// position on line;

		double x0 = p0.getX();
		double y0 = p0.getY();
		double dx = p1.getX()-x0;
		double dy = p1.getY()-y0;
		
		point.setLocation(x0+tl*dx, y0+tl*dy);
		return point;
	}

	/**
	 * returns 0.
	 */
	public double getT0(){
		return 0;
	}
	
	/**
	 * return the number of points in the polyline.
	 */
	public double getT1(){
		return points.size();
	}
	
	/**
	 * return the first point of the polyline.
	 */
	public Point2D getFirstPoint(){
		if(points.size()==0) return null;
		return points.get(0);
	}

	/**
	 * return the first point, as this is the same as the last point.
	 */
	public Point2D getLastPoint(){
		if(points.size()==0) return null;
		return points.get(0);
	}
	
	/**
	 * Returns the closed polyline with same points taken in reverse order. 
	 * The first points is still the same. Points of reverse curve are the
	 * same as the original curve (same pointers).
	 */
	public ClosedPolyline2D getReverseCurve(){
		Point2D[] points2 = new Point2D[points.size()];
		int n=points.size();
		if(n>0){
			points2[0] = points.get(0);
			for(int i=1; i<n; i++)
				points2[i] = points.get(n-i);
		}
		return new ClosedPolyline2D(points2);
	}

	/** 
	 * Return an instance of Polyline2D. If t1 is lower than t0, the returned
	 * Polyline contains the origin of the curve. 
	 */
	public Polyline2D getSubCurve(double t0, double t1){
		// code adapted from CurveSet2D
		
		Polyline2D res = new Polyline2D();
		
		// number of points in the polyline
		int indMax = (int) this.getT1();
		
		// format to ensure t is between T0 and T1
		t0 = Math.min(Math.max(t0, 0), indMax);
		t1 = Math.min(Math.max(t1, 0), indMax);

		// find curves index
		int ind0 = (int) Math.floor(t0+Shape2D.ACCURACY);
		int ind1 = (int) Math.floor(t1+Shape2D.ACCURACY);
		
		// need to subdivide only one line segment
		if(ind0==ind1 && t0<t1){
			// extract limit points
			res.addPoint(this.getPoint(t0));
			res.addPoint(this.getPoint(t1));
			// return result
			return res;
		}		

		// add the point corresponding to t0
		res.addPoint(this.getPoint(t0));
		
		if(ind1>ind0){
			// add all the whole points between the 2 cuts
			for(int n=ind0+1; n<=ind1; n++)
				res.addPoint(points.get(n));
		}else{
			// add all points until the end of the set
			for(int n=ind0+1; n<indMax; n++)
				res.addPoint(points.get(n));
			
			// add all points from the beginning of the set
			for(int n=0; n<=ind1; n++)
				res.addPoint(points.get(n));
		}
		
		// add the last point
		res.addPoint(this.getPoint(t1));

		// return the curve set
		return res;
	}


	// ===================================================================
	// Methods inherited from interface Shape2D

	/**
	 * Return the transformed shape, as a ClosePolyline2D.
	 */
	public ClosedPolyline2D transform(AffineTransform2D trans) {
		Point2D[] pts = new Point2D[points.size()];
		for(int i=0; i<points.size(); i++)
			pts[i] = trans.transform(points.get(i), pts[i]);
		return new ClosedPolyline2D(pts);
	}
	
//	/**
//	 * Return clipped shape, as a BoundarySet2D.
//	 */
//	public CurveSet2D<? extends Curve2D> getClippedShape(Box2D box) {
//		return box.clipBoundary(this);
//	}

	// ===================================================================
	// Methods inherited from Shape interface
	
	/* (non-Javadoc)
	 * @see math.geom2d.ContinuousCurve2D#appendPath(java.awt.geom.GeneralPath)
	 */
	public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path) {
		
		if(points.size()<2) return path;
		
		Point2D p0 = this.getFirstPoint();
		path.moveTo((float)p0.getX(), (float)p0.getY());
			
		Iterator<Point2D> iter = points.iterator();		
		
		// process each curve
		while(iter.hasNext()){
			Point2D point = (Point2D) iter.next();			
			path.lineTo((float)point.getX(), (float)point.getY());
		}
		path.lineTo((float)p0.getX(), (float)p0.getY());
				
		return path;
	}
	
	/** 
	 * Return a general path iterator.
	 */
	public java.awt.geom.GeneralPath getGeneralPath(){
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		if(points.size()<2) return path;
		
		// get point iterator
		Iterator<Point2D> iter = points.iterator();

		// move to first point
		Point2D point = iter.next();
		path.moveTo((float)(point.getX()), (float)(point.getY()));
		
		// line to each other point
		while(iter.hasNext()){
			point = iter.next();
			path.lineTo((float)(point.getX()), (float)(point.getY()));
		}
		
		// closes the path
		path.closePath();
		
		return path;
	}
	
	/** 
	 * Return pathiterator for this polyline.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans){
		return this.getGeneralPath().getPathIterator(trans);
	}

	/**
	 * Return pathiterator for this polyline.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans, double flatness){
		return this.getGeneralPath().getPathIterator(trans, flatness);
	}
}