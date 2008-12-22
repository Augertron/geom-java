/* File InvertedRay2D.java 
 *
 * Project : Java Geometry Library
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
 */

// package
package math.geom2d.line;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Box2D;
import math.geom2d.UnboundedShapeException;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.transform.AffineTransform2D;

// Imports

/**
 * Inverted ray is defined from an origin and a direction vector.
 * It is composed of all points satisfying the parametric equation:<p>
 * <code>x(t) = x0+t*dx<code><br>
 * <code>y(t) = y0+t*dy<code></p> 
 * with <code>t<code> comprised between -INFINITY and 0.
 * This is complementary class to Ray2D.
 */
public class InvertedRay2D extends AbstractLine2D{

	// ===================================================================
	// constants	

	// ===================================================================
	// class variables
		
	// ===================================================================
	// constructors
	
	/**
	 * Empty constructor for Ray2D. Default is ray starting at origin, and 
	 * having a slope of 1*dx and 0*dy.
	 */
	public InvertedRay2D(){
		this(0, 0, 1, 0);
	}

	/**
	 * Creates a new Ray2D, originating from <code>point1<\code>, and going in the
	 * direction of <code>point2<\code>.
	 */
	public InvertedRay2D(Point2D point1, Point2D point2){
		this(point1.getX(), point1.getY(), 
			point2.getX()-point1.getX(), point2.getY()-point1.getY());
	}
	
	/**
	 * Creates a new Ray2D, originating from point <code>(x1,y1)<\code>, and going 
	 * in the direction defined by vector <code>(dx, dy)<\code>.
	 */
	public InvertedRay2D(double x1, double y1, double dx, double dy){
		super(x1, y1, dx, dy);
	}

	/**
	 * Creates a new Ray2D, originating from point <code>point<\code>, and going 
	 * in the direction defined by vector <code>(dx,dy)<\code>.
	 */
	public InvertedRay2D(Point2D point, double dx, double dy){
		this(point.getX(), point.getY(), dx, dy);
	}

	/**
	 * Creates a new Ray2D, originating from point <code>point<\code>, and going 
	 * in the direction specified by <code>vector<\code>.
	 */
	public InvertedRay2D(Point2D point, Vector2D vector){
		this(point.getX(), point.getY(), vector.getX(), vector.getY());
	}

	/**
	 * Creates a new Ray2D, originating from point <code>point<\code>, and going 
	 * in the direction specified by <code>angle<\code> (in radians).
	 */
	public InvertedRay2D(Point2D point, double angle){
		this(point.getX(), point.getY(), Math.cos(angle), Math.sin(angle));
	}

	/**
	 * Creates a new Ray2D, originating from point <code>(x, y)<\code>, and going 
	 * in the direction specified by <code>angle<\code> (in radians).
	 */
	public InvertedRay2D(double x, double y, double angle){
		this(x, y, Math.cos(angle), Math.sin(angle));
	}

	/** 
	 * Define a new Ray, with same characteristics as given object.
	 */
	public InvertedRay2D(LinearShape2D line){
		super(line);
	}
	
	
	// ===================================================================
	// methods specific to InvertedRay2D

	public void setRay(double x0, double y0, double dx, double dy){
		this.x0 = x0;
		this.y0 = y0;
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setRay(Point2D p1, Point2D p2){
		this.x0 = p1.getX();
		this.y0 = p1.getY();
		this.dx = p2.getX()-this.x0;
		this.dy = p2.getY()-this.y0;
	}

	public void setRay(Point2D point, Vector2D vect){
		this.x0 = point.getX();
		this.y0 = point.getY();
		this.dx = vect.getX();
		this.dy = vect.getY();
	}
	


	// ===================================================================
	// methods implementing the ContinuousCurve2D interface

	public Polyline2D getAsPolyline(int n) {
		throw new UnboundedShapeException();
	}

	/** Throws an infiniteShapeException */
	public GeneralPath appendPath(GeneralPath path) {
		throw new UnboundedShapeException();
	}
	
	/** Throws an infiniteShapeException */
	public java.awt.geom.GeneralPath getGeneralPath() {
		throw new UnboundedShapeException();
	}


	// ===================================================================
	// methods implementing the Curve2D interface

	public Point2D getFirstPoint() {
		return Point2D.INFINITY_POINT;
	}

	public Point2D getLastPoint() {
		return new Point2D(x0, y0);
	}

	public Point2D getPoint(double t) {
		t = Math.min(t, 0);
		return new Point2D(x0+t*dx, y0+t*dy);
	}

	public Collection<Point2D> getSingularPoints(){
		ArrayList<Point2D> list = new ArrayList<Point2D>(1);
		list.add(this.getLastPoint());
		return list;
	}
	
	public boolean isSingular(double pos) {
		return Math.abs(pos)<Shape2D.ACCURACY;
	}

	public double getT0() {
		return Double.NEGATIVE_INFINITY;
	}

	public double getT1() {
		return 0;
	}

	public Ray2D getReverseCurve() {
		return new Ray2D(x0, y0, -dx, -dy);
	}


	// ===================================================================
	// methods implementing the Shape2D interface

	/** Always returns false, because n inverted ray is not bounded.*/
	public boolean isBounded(){
		return false;
	}
	
	public boolean contains(double x, double y){
		if(!this.supportContains(x,y)) return false;
		double t = this.getPositionOnLine(x, y);
		return t<Shape2D.ACCURACY;
	}
	
	public Box2D getBoundingBox(){
		double t = Double.NEGATIVE_INFINITY;
		return new Box2D(x0+t*dx, x0, y0+t*dy, y0);
	}
	
	@Override
	public InvertedRay2D transform(AffineTransform2D trans){
		double[] tab = trans.getCoefficients();
		double x1 = x0*tab[0] + y0*tab[1] + tab[2];
		double y1 = x0*tab[3] + y0*tab[4] + tab[5];
		return new InvertedRay2D(x1, y1, 
			dx*tab[0]+dy*tab[1], dx*tab[3]+dy*tab[4]);
	}


	// ===================================================================
	// methods implementing the Shape interface

	public PathIterator getPathIterator(AffineTransform at) {
		throw new UnboundedShapeException();
	}

	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		throw new UnboundedShapeException();
	}

	/** Throws an infiniteShapeException */
	public void draw(Graphics2D g) {
		throw new UnboundedShapeException();
	}

	
	// ===================================================================
	// methods implementing the Shape interface

	public boolean equals(Object obj){
		if(!(obj instanceof InvertedRay2D)) return false;
		InvertedRay2D ray = (InvertedRay2D) obj;
		if(Math.abs(x0-ray.x0)>Shape2D.ACCURACY) return false;
		if(Math.abs(y0-ray.y0)>Shape2D.ACCURACY) return false;
		if(Math.abs(dx-ray.dx)>Shape2D.ACCURACY) return false;
		if(Math.abs(dy-ray.dy)>Shape2D.ACCURACY) return false;
		return true;
	}

}