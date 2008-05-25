/* File AffineTransform2D.java 
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
package math.geom2d.transform;

// Imports
import math.geom2d.Shape2D;
import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.StraightObject2D;

/**
 * Base class for generic affine transforms in the plane. They include
 * rotations, translations, shears, homotheties, and combinations of these. 
 * Such transformations can be constructed by using coefficients
 * specification, or by creating specialized instances, by using static
 * methods.<p>
 */
public class AffineTransform2D implements Bijection2D{	

	// coefficients for x coordinate.
	protected double m00, m01, m02;
	
	// coefficients for y coordinate.
	protected double m10, m11, m12;

	
	// ===================================================================
	// static methods
	
	public final static AffineTransform2D createGlideReflection(
			StraightObject2D line, double distance){
		Vector2D vector = line.getVector().getNormalizedVector();
		Point2D origin = line.getOrigin();
		double dx = vector.getX();
		double dy = vector.getY();
		double x0 = origin.getX();
		double y0 = origin.getY();
		double delta = dx*dx + dy*dy;
		
		double tx = vector.getX()*distance;
		double ty = vector.getY()*distance;
		
		return new AffineTransform2D(
				(dx*dx - dy*dy)/delta,
				2*dx*dy/delta,
				2*dy*(dy*x0 - dx*y0)/delta + tx,
				2*dx*dy/delta,
				(dy*dy - dx*dx)/delta,
				2*dx*(dx*y0 - dy*x0)/delta + ty);
	}

	public final static AffineTransform2D createLineReflection(StraightObject2D line){
		Vector2D vector = line.getVector();
		Point2D origin = line.getOrigin();
		double dx = vector.getX();
		double dy = vector.getY();
		double x0 = origin.getX();
		double y0 = origin.getY();
		double delta = dx*dx + dy*dy;
		
		return new AffineTransform2D(
				(dx*dx - dy*dy)/delta,
				2*dx*dy/delta,
				2*dy*(dy*x0 - dx*y0)/delta,
				2*dx*dy/delta,
				(dy*dy - dx*dx)/delta,
				2*dx*(dx*y0 - dy*x0)/delta);
	}

	/**
	 * Return a rotation around the origin, with angle in radians.
	 */
	public final static AffineTransform2D createRotation(double angle){
		return AffineTransform2D.createRotation(0, 0, angle);
	}

	/**
	 * Return a rotation around the specified point, with angle in radians.
	 */
	public final static AffineTransform2D createRotation(Point2D center, double angle){
		return AffineTransform2D.createRotation(center.getX(), center.getY(), angle);
	}

	/**
	 * Return a rotation around the specified point, with angle in radians.
	 * If the angular distance of the angle with a multiple of PI/2 is lower
	 * than the threshold Shape2D.ACCURACY, the method assumes equality.
	 */
	public final static AffineTransform2D createRotation(double cx, double cy, double angle){
		angle = Angle2D.formatAngle(angle);
		
		// coefficients of parameters m00, m01, m10 and m11.
		double cot=1, sit=0;
		
		// special processing to detect angle close to multiple of PI/2.
		int k = (int) Math.round(angle*2/Math.PI);
		if(Math.abs(k*Math.PI/2 - angle)<Shape2D.ACCURACY){
			assert k>=0 : "k should be positive";
			assert k<5 : "k should be between 0 and 4";
			switch(k){
			case 0:cot=1; sit=0; break;
			case 1:cot=0; sit=1; break;
			case 2:cot=-1; sit=0; break;
			case 3:cot=0; sit=-1; break;
			case 4:cot=1; sit=0; break;	
			}
		}else{
			cot = Math.cos(angle);
			sit = Math.sin(angle);
		}
		
		// init coef of the new AffineTransform.
		return new AffineTransform2D(
				cot, -sit, (1-cot)*cx+sit*cy,
				sit,  cot, (1-cot)*cy-sit*cx);
	}

	/**
	 * Return a scaling by the given coefficients, centered on the origin.
	 */
	public final static AffineTransform2D createScaling(double sx, double sy){
		return AffineTransform2D.createScaling(new Point2D(0, 0), sx, sy);
	}

	/**
	 * Return a scaling by the given coefficients, centered on the given point.
	 */
	public final static AffineTransform2D createScaling(Point2D center, double sx, double sy){
		return new AffineTransform2D(sx, 0, (1-sx)*center.getX(), 0, sy, (1-sy)*center.getY());
	}

	/**
	 * Return a translation by the given vector.
	 */
	public final static AffineTransform2D createTranslation(Vector2D vect){
		return new AffineTransform2D(1, 0, vect.getX(), 0, 1, vect.getY());
	}
	
	/**
	 * Return a translation by the given vector.
	 */
	public final static AffineTransform2D createTranslation(double dx, double dy){
		return new AffineTransform2D(1, 0, dx, 0, 1, dy);
	}

	
	// ===================================================================
	// methods to identify transforms
	
	public final static boolean isIdentity(AffineTransform2D trans){
		double[] coefs = trans.getCoefficients();
		if(Math.abs(coefs[0]-1)>Shape2D.ACCURACY) return false;
		if(Math.abs(coefs[1])>Shape2D.ACCURACY) return false;
		if(Math.abs(coefs[2])>Shape2D.ACCURACY) return false;
		if(Math.abs(coefs[3])>Shape2D.ACCURACY) return false;
		if(Math.abs(coefs[4]-1)>Shape2D.ACCURACY) return false;
		if(Math.abs(coefs[5])>Shape2D.ACCURACY) return false;
		return true;
	}
	
	/**
	 * Check if the transform is direct, i.e. it preserves the orientation
	 * of transformed shapes.
	 * @return true if transform is direct.
	 */
	public final static boolean isDirect(AffineTransform2D trans){
		double[][] mat = trans.getAffineMatrix();
		return mat[0][0]*mat[1][1] - mat[0][1]*mat[1][0] > 0;
	}

	/**
	 * Check if the transform is an isometry, i.e. a compound of
	 * translation, rotation and reflection. Isometry keeps area of shapes
	 * unchanged, but can change orientation (directed or undirected).
	 * @return true in case of isometry.
	 */
	public final static boolean isIsometry(AffineTransform2D trans){
		double[][] mat = trans.getAffineMatrix();
		double det = mat[0][0]*mat[1][1] - mat[0][1]*mat[1][0]; 
		return Math.abs(Math.abs(det)-1)<Shape2D.ACCURACY;
	}

	/**
	 * Check if the transform is a motion, i.e. a compound of translations and
	 * rotation. Motion remains area and orientation (directed or undirected)
	 * of shapes unchanged.
	 * @return true in case of motion.
	 */
	public final static boolean isMotion(AffineTransform2D trans){
		double[][] mat = trans.getAffineMatrix();
		double det = mat[0][0]*mat[1][1] - mat[0][1]*mat[1][0]; 
		return Math.abs(det-1)<Shape2D.ACCURACY;
	}
	
	/**
	 * Check if the transform is an similarity, i.e. transformation which keeps
	 * unchanged the global shape, up to a scaling factor.
	 * @return true in case of similarity.
	 */
	public final static boolean isSimilarity(AffineTransform2D trans){
		double[][] mat = trans.getAffineMatrix();
		return Math.abs(mat[0][0]*mat[0][1] + mat[1][0]*mat[1][1])<
			Shape2D.ACCURACY;		
	}


	// ===================================================================
	// Constructors	
	
	/** Main constructor */
	public AffineTransform2D(){
		// init to identity matrix
		m00 = m11 = 1;
		m01 = m10 = 0;
		m02 = m12 = 0;
	}

	/** constructor by copy of an existing transform*/
	public AffineTransform2D(AffineTransform2D trans){
		double[][] mat = trans.getAffineMatrix();
		this.m00 = mat[0][0];
		this.m01 = mat[0][1];
		this.m02 = mat[0][2];
		this.m10 = mat[1][0];
		this.m11 = mat[1][1];
		this.m12 = mat[1][2];
	}

	public AffineTransform2D(double[] coefs){
		if(coefs.length==4){
			m00 = coefs[0];
			m01 = coefs[1];
			m10 = coefs[2];
			m11 = coefs[3];
		}else{
			m00 = coefs[0];
			m01 = coefs[1];
			m02 = coefs[2];
			m10 = coefs[3];
			m11 = coefs[4];
			m12 = coefs[5];
		}
	}
	
	public AffineTransform2D(double xx, double yx, double tx, double xy, double yy, double ty){
		m00 = xx;
		m01 = yx;
		m02 = tx;
		m10 = xy;
		m11 = yy;
		m12 = ty;
	}


	// ===================================================================
	// implementations of AffineTransform2D methods
	
	/**
	 * Returns coefficients of the transform. Result is an array of 6 double.
	 */
	public double[] getCoefficients(){
		double[] tab = {m00, m01, m02, m10, m11, m12};		
		return tab;
	}	

	public double[][] getAffineMatrix(){
		double[][] tab = new double[][]{
				new double[]{m00, m01, m02}, 
				new double[]{m10, m11, m12}
		};
		return tab;
	}

	public AffineTransform2D getInverseTransform(){
		double det = m00*m11 - m10*m01;
		// TODO: manage case of transforms with determinant=0
		return new AffineTransform2D(
			m11/det, -m01/det, (m01*m12-m02*m11)/det, 
			-m10/det, m00/det, (m02*m10-m00*m12)/det);
	}
	
	public AffineTransform2D compose(AffineTransform2D that){
		double[][] m2 = that.getAffineMatrix();
		double n00 = this.m00*m2[0][0] + this.m01*m2[1][0];
		double n01 = this.m00*m2[0][1] + this.m01*m2[1][1];
		double n02 = this.m00*m2[0][2] + this.m01*m2[1][2] + this.m02;
		double n10 = this.m10*m2[0][0] + this.m11*m2[1][0];
		double n11 = this.m10*m2[0][1] + this.m11*m2[1][1];
		double n12 = this.m10*m2[0][2] + this.m11*m2[1][2] + this.m12;
		return new AffineTransform2D(n00, n01, n02, n10, n11, n12); 
	}
	
	
	public boolean isSimilarity(){
		return AffineTransform2D.isSimilarity(this);
	}
	
	public boolean isMotion(){
		return AffineTransform2D.isMotion(this);
	}
	
	public boolean isIsometry(){
		return AffineTransform2D.isIsometry(this);
	}
	
	public boolean isDirect(){
		return AffineTransform2D.isDirect(this);
	}
	
	public boolean isIdentity(){
		return AffineTransform2D.isIdentity(this);
	}
	
	// ===================================================================
	// implementations of Transform2D methods

	public Point2D[] transform(java.awt.geom.Point2D[] src, Point2D[] dst){
		if(dst==null)
			dst = new Point2D[src.length];
		if(dst[0]==null)
			for(int i=0; i<src.length; i++)
				dst[i]=new Point2D();
		
		double coef[] = getCoefficients();
		
		for(int i=0; i<src.length; i++)
			dst[i].setLocation(new Point2D(
				src[i].getX()*coef[0] + src[i].getY()*coef[1] + coef[2],
				src[i].getX()*coef[3] + src[i].getY()*coef[4] + coef[5]));
		return dst;
	}
	
	public Point2D transform(java.awt.geom.Point2D src) {
		return transform(src, new Point2D());
	}
	
	public Point2D transform(java.awt.geom.Point2D src, Point2D dst){
		double coef[] = getCoefficients();
		if (dst==null)dst = new Point2D();	
		dst.setLocation(src.getX()*coef[0] + src.getY()*coef[1] + coef[2],
					  	src.getX()*coef[3] + src.getY()*coef[4] + coef[5]);
		return dst;
	}	
	
	public boolean equals(Object obj){
		if(!(obj instanceof AffineTransform2D)) return false;
		
		double[] tab1 = this.getCoefficients();
		double[] tab2 = ((AffineTransform2D) obj).getCoefficients();

		for(int i=0; i<6; i++)
			if(Math.abs(tab1[i]-tab2[i])>Shape2D.ACCURACY) return false;

		return true;
	}
}