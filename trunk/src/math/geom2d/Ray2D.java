/* File Ray2D.java 
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

// Imports

/**
 * Ray, or half-line, defined from an origin and a direction vector.
 * It is composed of all points satisfying the parametric equation:<p>
 * <code>x(t) = x0+t*dx<code><br>
 * <code>y(t) = y0+t*dy<code></p> 
 * With <code>t<code> comprised between 0 and +INFINITY.
 */
public class Ray2D extends LineArc2D{


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
	public Ray2D(){
		this(0, 0, 1, 0);
	}

	/**
	 * Creates a new Ray2D, originating from <code>point1<\code>, and going in the
	 * direction of <code>point2<\code>.
	 */
	public Ray2D(Point2D point1, Point2D point2){
		this(point1.getX(), point1.getY(), 
			point2.getX()-point1.getX(), point2.getY()-point1.getY());
	}
	
	/**
	 * Creates a new Ray2D, originating from point <code>(x1,y1)<\code>, and going 
	 * in the direction defined by vector <code>(dx, dy)<\code>.
	 */
	public Ray2D(double x1, double y1, double dx, double dy){
		super(x1, y1, dx, dy, 0, Double.POSITIVE_INFINITY);
	}

	/**
	 * Creates a new Ray2D, originating from point <code>point<\code>, and going 
	 * in the direction defined by vector <code>(dx,dy)<\code>.
	 */
	public Ray2D(Point2D point, double dx, double dy){
		this(point.getX(), point.getY(), dx, dy);
	}

	/**
	 * Creates a new Ray2D, originating from point <code>point<\code>, and going 
	 * in the direction specified by <code>vector<\code>.
	 */
	public Ray2D(Point2D point, Vector2D vector){
		this(point.getX(), point.getY(), vector.getDx(), vector.getDy());
	}

	/**
	 * Creates a new Ray2D, originating from point <code>point<\code>, and going 
	 * in the direction specified by <code>angle<\code> (in radians).
	 */
	public Ray2D(Point2D point, double angle){
		this(point.getX(), point.getY(), Math.cos(angle), Math.sin(angle));
	}

	/**
	 * Creates a new Ray2D, originating from point <code>(x, y)<\code>, and going 
	 * in the direction specified by <code>angle<\code> (in radians).
	 */
	public Ray2D(double x, double y, double angle){
		this(x, y, Math.cos(angle), Math.sin(angle));
	}

	/** 
	 * Define a new Ray, with same characteristics as given object.
	 */
	public Ray2D(StraightObject2D line){
		this(line.x0, line.y0, line.dx, line.dy);
	}
	
	
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
		this.dx = vect.getDx();
		this.dy = vect.getDy();
	}

	// ===================================================================
	// accessors

	/** Always returns false, because a ray is not bounded.*/
	public boolean isBounded(){
		return false;
	}
	
	@Override
	public LineArc2D transform(AffineTransform2D trans){
		double[] tab = trans.getCoefficients();
		double x1 = x0*tab[0] + y0*tab[1] + tab[2];
		double y1 = x0*tab[3] + y0*tab[4] + tab[5];
		return new Ray2D(x1, y1, 
			dx*tab[0]+dy*tab[1], dx*tab[3]+dy*tab[4]);
	}
}