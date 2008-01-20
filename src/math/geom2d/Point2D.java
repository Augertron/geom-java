/* File Point2D.java 
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
package math.geom2d;

import java.util.Collection;

// Imports

/**
 * A point in the plane defined by its 2 cartesian coordinates x and y.
 * The class provides static methods to compute distance between two points.
 */
public class Point2D extends java.awt.geom.Point2D.Double implements Shape2D{


	// ===================================================================
	// constants
	
	private static final long serialVersionUID = 1L;


	/**
	 * The point located at the infinity. This point is virtual, it
	 * is contained in every inifinite shape, such as straight lines,
	 * hyperbolas and parabolas.
	 */
	public final static Point2D INFINITY_POINT = new Point2D(
		java.lang.Double.POSITIVE_INFINITY, 
		java.lang.Double.POSITIVE_INFINITY);
	
	// ===================================================================
	// class variables
	
	
	// ===================================================================
	// constructors
	
	/** construct a new Point2D at position (0,0). */
	public Point2D(){
		super(0, 0);
	}
	
	/** constructor with given position. */
	public Point2D(double x, double y){
		super(x, y);
	}
	
	/**
	 * Constructor from a java awt.geom Point2D, included for compatibility.
	 */
	public Point2D(java.awt.geom.Point2D point){
		super(point.getX(), point.getY());
	}

	/**
	 * Constructor from two java awt.geom Point2D, summing their coordinates. 
	 */
	public Point2D(java.awt.geom.Point2D point1, java.awt.geom.Point2D point2){
		super(point1.getX()+point2.getX(), point1.getY()+point2.getY());
	}
	
	/**
	 * Constructor from a java awt.geom Point2D, and two double. The (x,y)
	 *  coordinates are added to the coordinates of given point. 
	 */
	public Point2D(java.awt.geom.Point2D point1, double x, double y){
		super(point1.getX()+x, point1.getY()+y);
	}
	
	
	// ===================================================================
	// static functions

	/**
	 * create a new point from polar coordinates <code>rho</code> and
	 * <code>theta</code>.
	 */
	public final static Point2D createPolar(double rho, double theta){
		return new Point2D(rho*Math.cos(theta), rho*Math.sin(theta));
	}

	/**
	 * create a new point from polar coordinates <code>rho</code> and
	 * <code>theta</code>, from the given point.
	 */
	public final static Point2D createPolar(Point2D point, double rho, double theta){
		return new Point2D(point.getX()+rho*Math.cos(theta), point.getY()+rho*Math.sin(theta));
	}

	/**
	 * create a new point from polar coordinates <code>rho</code> and
	 * <code>theta</code>, from the position (x0,y0).
	 */
	public final static Point2D createPolar(double x0, double y0, double rho, double theta){
		return new Point2D(x0+rho*Math.cos(theta), y0+rho*Math.sin(theta));
	}

	
	/**
	 * get the dot product of the two points, defined by : <p>
	 * <code> x1*y2 + x2*y1</code> <p>
	 * Dot product is zero if the vectors defined by the 2 points are 
	 * orthogonal. It is positive if vectors are in the same direction, and
	 * negative if they are in opposite direction. 
	 * @deprecated: use Vector2D.dot() method instead
	 */
	@Deprecated
	public final static double dot(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2){
		return p1.getX()*p2.getX()+p1.getY()*p2.getY();
	}
	
	/**
	 * get the cross product of the two points, defined by : <p>
	 * <code> x1*y2 - x2*y1</code><p>
	 * cross product is zero for colinear vector. It is positive if angle
	 * between vector 1 and vector 2 is comprised between 0 and PI, and
	 * negative otherwise.
	 * @deprecated: use Vector2D.cross() method instead
	 */
	@Deprecated
	public final static double cross(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2){
		return p1.getX()*p2.getY()-p2.getX()*p1.getY();
	}
	
	public final static double getDistance(double x1, double y1, double x2, double y2){
		return Math.hypot(x2-x1, y2-y1); 
	}
	
	public final static double getDistance(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2){
		return getDistance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	
	/**
	 * test if the three points are colinear.
	 * @return true if three points lie on the same line.
	 */
	public final static boolean isColinear(
			java.awt.geom.Point2D p1, 
			java.awt.geom.Point2D p2, 
			java.awt.geom.Point2D p3){	
		//TODO: makes the method more robust
		double dx1, dx2, dy1, dy2;
		dx1 = p2.getX()-p1.getX();
		dy1 = p2.getY()-p1.getY();
		dx2 = p3.getX()-p1.getX();
		dy2 = p3.getY()-p1.getY();

		// test if the two lines are parallel
		return Math.abs(dx1*dy2 - dy1*dx2)<Shape2D.ACCURACY;
	}
	
	public final static Point2D midPoint(
			java.awt.geom.Point2D p1, java.awt.geom.Point2D p2){
		return new Point2D((p1.getX()+p2.getX())/2, (p1.getY()+p2.getY())/2); 
	}

	/**
	 * Computes the centroid, or center of mass, of an array of points.
	 * @param points an array of points
	 * @return the centroid of the points
	 */
	public final static Point2D centroid(java.awt.geom.Point2D[] points){
		int n=points.length;
		double sx = 0, sy=0;
		for(int i=0; i<n; i++){
			sx += points[i].getX();
			sy += points[i].getY();
		}
		return new Point2D(sx/n, sy/n); 
	}
	
	/**
	 * Computes the centroid, or center of mass, of a collection of points.
	 * @param points a collection of points
	 * @return the centroid of the points
	 */
	public final static Point2D centroid(Collection<? extends Point2D> points){
		int n = points.size();
		double sx = 0, sy=0;
		for(Point2D point : points){
			sx += point.getX();
			sy += point.getY();
		}
		return new Point2D(sx/n, sy/n); 
	}
	
	/**
	 * Compute the centroid of three points.
	 * @param pt1 the first point 
	 * @param pt2 the second point 
	 * @param pt3 the third point 
	 * @return the centroid of the 3 points
	 */
	public final static Point2D centroid(
			java.awt.geom.Point2D pt1, java.awt.geom.Point2D pt2, java.awt.geom.Point2D pt3){
		return new Point2D(
			(pt1.getX() + pt2.getX() + pt3.getX())/3, 
			(pt1.getY() + pt2.getY() + pt3.getY())/3
		 ); 
	}
	
	
	// ===================================================================
	// accessors

	/**
	 * Compute the distance between this and the point <code>point</code>.
	 */
	public double getDistance(java.awt.geom.Point2D point){
		return getDistance(point.getX(), point.getY());
	}
	
	/**
	 * Compute the distance between current point and point with coordinate 
	 * <code>(x,y)</code>. Uses the <code>Math.hypot()</code> function for 
	 * better robustness than simple square root.
	 */
	public double getDistance(double x, double y){
		return Math.hypot(getX()-x, getY()-y);
	}

	/** 
	 * Convert point to an integer version. Coordinates are rounded to the 
	 * nearest integer.
	 * @return an instance of java.awt.Point
	 */
	public java.awt.Point getAsInt(){
		return new java.awt.Point((int)x, (int)y);
	}
	
	/** 
	 * Convert point to an double version. 
	 */
	public java.awt.geom.Point2D.Double getAsDouble(){
		return new java.awt.geom.Point2D.Double(x, y);
	}
	
	/** 
	 * Convert point to a float version. Coordinates are rounded to the 
	 * nearest float.
	 */
	public java.awt.geom.Point2D.Float getAsFloat(){
		return new java.awt.geom.Point2D.Float((float)x, (float)y);
	}
	
	// ===================================================================
	// modifiers

	/** 
	 * Set location specified as polar coordinate : distance from origin +
	 * angle with horizontal.
	 */
	public void setPolarLocation(double rho, double theta){
		x = rho*Math.cos(theta);
		y = rho*Math.sin(theta);
	}
	
	/**
	 * Set location at distance 'rho' from given point, and making an
	 * angle 'theta' with horizontal.
	 */
	public void setPolarLocation(java.awt.geom.Point2D point, double rho, double theta){
		x = point.getX()+rho*Math.cos(theta);
		y = point.getY()+rho*Math.sin(theta);
	}

	// ===================================================================
	// general methods

	/**
	 * Returns true if the point is bounded. A point is unbounded if at least
	 * one of its coordinates is infinite or NaN.
	 * @return true if both coordinates of the point are finite 
	 */
	public boolean isBounded(){
		if(java.lang.Double.isInfinite(this.x)) return false;
		if(java.lang.Double.isInfinite(this.y)) return false;
		if(java.lang.Double.isNaN(this.x)) return false;
		if(java.lang.Double.isNaN(this.y)) return false;
		return true;
	}

	/**
	 * return true if the two points are equal.
	 */
	public boolean contains(double x, double y){
		return this.equals(new Point2D(x, y));
	}

	/**
	 * return true if the two points are equal.
	 */
	public boolean contains(java.awt.geom.Point2D p){
		return this.equals(p);
	}

	/**
	 * Always return false.
	 */
	public boolean contains(double x, double y, double w, double h){
		return false;
	}

	/**
	 * Always return false.
	 */
	public boolean contains(java.awt.geom.Rectangle2D r){
		return false;
	}

	/**
	 * Two points are consided equal if their euclidean distance is less
	 * than Shape2D.ACCURACY.
	 */
	public boolean equals(Object obj){
		if(!(obj instanceof java.awt.geom.Point2D)) return false;
		java.awt.geom.Point2D p = (java.awt.geom.Point2D) obj;
		return this.distance(p.getX(), p.getY())<Shape2D.ACCURACY;
	}
	
	public Point2D plus(java.awt.geom.Point2D p){
		return new Point2D(p.getX()+x, p.getY()+y);
	}
	
	public Point2D minus(java.awt.geom.Point2D p){
		return new Point2D(x-p.getX(), y-p.getY());
	}
	
	
	/**
	 * Return bounding box of the shape.
	 */
	public java.awt.Rectangle getBounds(){
		return this.getBoundingBox().getAsAWTRectangle();
	}
	
	/**
	 * Return more precise bounds for the shape.
	 */
	public java.awt.geom.Rectangle2D getBounds2D(){
		return this.getBoundingBox().getAsAWTRectangle2D();
	}

	/** Return null*/
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform t){
		return null;
	}

	/** 
	 * return a circle enclosing the point, which radius is the specified flatness.
	 */
	public java.awt.geom.PathIterator getPathIterator(
			java.awt.geom.AffineTransform t, double flatness){
		return null;
	}

	/**
	 * Tests if the Point2D intersects the interior of a specified rectangular area.
	 */
	public boolean intersects(double x, double y, double w, double h){
		return( getX()>=x && getX()<=x+w && getY()>=y && getY()<=y+h);
	}

	/**
	 * Tests if the Point2D intersects the interior of a specified rectangle2D.
	 */
	public boolean intersects(java.awt.geom.Rectangle2D r){
		return( getX()>=r.getX() && getX()<=r.getX()+r.getWidth() && 
				getY()>=r.getY() && getY()<=r.getY()+r.getHeight() );
	}

	/**
	 * Returns either the point itself, or the shape EMPTY_SET, depending on
	 * whether the point lies inside the specified box.
	 */
	public Shape2D getClippedShape(Box2D box){
		if(x<box.getMinX()) return Shape2D.EMPTY_SET;
		if(y<box.getMinY()) return Shape2D.EMPTY_SET;
		if(x>box.getMaxX()) return Shape2D.EMPTY_SET;
		if(y>box.getMaxY()) return Shape2D.EMPTY_SET;
		
		return this;
	}
	
	/**
	 * Returns either the point itself, or the shape EMPTY_SET, depending on
	 * whether the point lies inside the specified box.
	 */
	public Shape2D clip(Box2D box){
		if(x<box.getMinX()) return Shape2D.EMPTY_SET;
		if(y<box.getMinY()) return Shape2D.EMPTY_SET;
		if(x>box.getMaxX()) return Shape2D.EMPTY_SET;
		if(y>box.getMaxY()) return Shape2D.EMPTY_SET;
		
		return this;
	}
	
	public Box2D getBoundingBox(){
		return new Box2D(getX()-.5, getX()+.5, getY()-.5, getY()+.5);
	}
	
	public Shape2D getTransformedShape(AffineTransform2D trans){
		double[] tab = trans.getCoefficients();
		return new Point2D(
			x*tab[0] + y*tab[1] + tab[2],
			x*tab[3] + y*tab[4] + tab[5] );
	}
	
	public Point2D transform(AffineTransform2D trans){
		double[] tab = trans.getCoefficients();
		return new Point2D(
			x*tab[0] + y*tab[1] + tab[2],
			x*tab[3] + y*tab[4] + tab[5] );
	}
}