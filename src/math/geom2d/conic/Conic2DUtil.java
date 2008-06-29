/**
 * 
 */
package math.geom2d.conic;

import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * Generic class providing utilities for conics, especially for reducing a conic.
 * @author dlegland
 */
public class Conic2DUtil {

	public final static Conic2D reduceConic(double[] coefs){
		if(coefs.length<6){
			System.err.println("Conic2DUtil.reduceConic: must provide 6 coefficients");
			return null;
		}
		boolean debug = false;
		
		// precision for tests
		double eps = Shape2D.ACCURACY;

		// Extract coefficients
		double a = coefs[0]; double b = coefs[1]; double c = coefs[2];
		double d = coefs[3]; double e = coefs[4]; double f = coefs[5];

		
		// Transform the generic conic into a conic symmetric with respect to
		// one of the basis axes.
		
		// coefficients of transformed conic;
		double a1, b1, c1, d1, e1, f1;
		
		double theta0=0;
		// Check if b is zero
		if(Math.abs(b)<eps){
			// Simply keep the same coefficients
			a1 = a;	b1 = b; c1 = c;	d1 = d;	e1 = e;	f1 = f;
			theta0 = 0;
		}else{
			// determine rotation angle (between 0 and PI/2).
			if (Math.abs(a-c)<eps)
				theta0 = Math.PI/4;			// conic symmetric wrt diagonal
			else
				theta0 = Angle2D.formatAngle(Math.atan2(b, a-c)/2);
			
			// computation shortcuts
			double cot 	= Math.cos(theta0);
			double sit 	= Math.sin(theta0);
			double co2t = Math.cos(2*theta0);
			double si2t = Math.sin(2*theta0);
			double cot2 = cot*cot;
			double sit2 = sit*sit;
			
			// Compute coefficients of rotated conic
			a1 = a*cot2 + b*sit*cot + c*sit2;
			b1 = si2t*(c-a) + b*co2t; // should be equal to zero
			c1 = a*sit2 - b*sit*cot + c*cot2;
			d1 = d*cot + e*sit;
			e1 = -d*sit + e*cot;
			f1 = f;
		}
		
		// small control on the value of b1
		if(Math.abs(b1)>eps){
			System.err.println("Conic2DUtil.reduceConic: conic was not correctly transformed");
		}
		
		// Case of a parabola
		if(Math.abs(a1)<eps){
			// case of a1 close to 0 -> parabola parallel to horizontal axis
			if(debug)
				System.out.println("horizontal parabola");
			
			// compute reduced coefficients
			double c2 = -c1/d1;
			double e2 = -e1/d1;
			double f2 = -f1/d1;
			
			// vertex of parabola
			double xs = -e2/c2;
			double ys = -(e2*e2 - c2*f2)/c2;
			
			// create and return result
			return new Parabola2D(xs, ys, c2, theta0-Math.PI/2);
			
		}else if(Math.abs(c1)<eps){
			// Case of c1 close to 0 -> parabola parallel to vertical axis
			if(debug)
				System.out.println("vertical parabola");
			
			// compute reduced coefficients
			double a2 = -a1/e1;
			double d2 = -d1/e1;
			double f2 = -f1/e1;
			
			// vertex of parabola
			double xs = -d2/a2;
			double ys = -(d2*d2 - a2*f2)/a2;
			
			// create and return result
			return new Parabola2D(xs, ys, a2, theta0);
		}

		// Remaining cases: ellipse or hyperbola
		
		// compute coordinate of conic center
		Point2D center = new Point2D(-d1/(2*a1), -e1/(2*c1));
		center = center.transform(AffineTransform2D.createRotation(theta0));
		
		// length of semi axes
		double num = (c1*d1*d1 + a1*e1*e1 - 4*a1*c1*f1)/(4*a1*c1);
		double at = num/a1;
		double bt = num/c1;
		
		if(at<0 && bt<0){
			System.out.println("empty set");
		}
		
		// Case of an ellipse
		if(at>0 && bt>0){
			if(debug)
				System.out.println("ellipse");
			if(at>bt)
				return new Ellipse2D(center,
						Math.sqrt(at), Math.sqrt(bt), theta0);
			else
				return new Ellipse2D(center, 
						Math.sqrt(bt), Math.sqrt(at), theta0+Math.PI/2);
			
				
		}
		
		// remaining cas is the hyperbola
		return null;
	}
	
	/**
	 * Transform a conic centered around the origin, by dropping the
	 * translation part of the transform. The array must be contains at
	 * least 3 elements. If it contains 3 elements, the 3 remaining elements
	 * are supposed to be 0, 0, and -1 in that order.
	 * @param coefs an array of double with at least 3 coefficients
	 * @param trans an affine transform
	 * @return an array of double with as many elements as the input array
	 */
	public final static double[] transformCentered(double[] coefs, AffineTransform2D trans){
		// Extract transform coefficients
		double[][] mat = trans.getAffineMatrix();
		double a = mat[0][0];
		double b = mat[1][0];
		double c = mat[0][1];
		double d = mat[1][1];
		
		// Extract first conic coefficients
		double A = coefs[0];
		double B = coefs[1];
		double C = coefs[2];
		
		// compute matrix determinant
		double delta = a*d-b*c;
		delta = delta*delta;
		
		double A2 = (A*d*d + C*b*b - B*b*d)/delta;
		double B2 = (B*(a*d+b*c) - 2*(A*c*d+C*a*b))/delta;
		double C2 = (A*c*c + C*a*a - B*a*c)/delta;
		
		// return only 3 parameters if needed
		if(coefs.length==3) return new double[]{A2, B2, C2};
		
		// Compute other coefficients
		double D = coefs[3];
		double E = coefs[4];
		double F = coefs[5];
		double D2 = D*d-E*b;
		double E2 = E*a-D*c;
		return new double[]{A2, B2, C2, D2, E2, F};
	}
}
