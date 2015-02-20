package com.decmurphy.spx;

import Jama.Matrix;
import static com.decmurphy.spx.Globals.earthAngVel;
import static com.decmurphy.spx.Globals.simTime;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

/**
 *
 * @author declan
 */
public class Maths {

  public static class CartesianCoordinates {

    private double[] coords = null;

    public CartesianCoordinates() {
      this(0,0,0);
    }    
		public CartesianCoordinates(double[] x) {
      this(x[0], x[1], x[2]);
    }
    public CartesianCoordinates(double x, double y, double z) {
      coords = new double[]{x, y, z};
    }
    
    public double get(int i) {
      return coords[i];
    }

    public SphericalCoordinates convertToSpherical() {
      return new SphericalCoordinates(
        magnitudeOf(coords),
        acos(coords[2] / magnitudeOf(coords)),
        atan2(coords[1], coords[0])
      );
    }

    public void rotateX(double angle) {
      double[] tempVector = new double[]{coords[0], coords[1], coords[2]};

      coords[0] = 1*tempVector[0] + 0*tempVector[1]          + 0*tempVector[2];
      coords[1] = 0*tempVector[0] + cos(angle)*tempVector[1] - sin(angle)*tempVector[2];
      coords[2] = 0*tempVector[0] + sin(angle)*tempVector[1] + cos(angle)*tempVector[2];
    }

    public void rotateY(double angle) {
      double[] tempVector = new double[]{coords[0], coords[1], coords[2]};

      coords[0] =  cos(angle)*tempVector[0] + 0*tempVector[1] + sin(angle)*tempVector[2];
      coords[1] =  0*tempVector[0]          + 1*tempVector[1] + 0*tempVector[2];
      coords[2] = -sin(angle)*tempVector[0] + 0*tempVector[1] + cos(angle)*tempVector[2];
    }

    public void rotateZ(double angle) {
      double[] tempVector = new double[]{coords[0], coords[1], coords[2]};

      coords[0] = cos(angle)*tempVector[0] - sin(angle)*tempVector[1] + 0*tempVector[2];
      coords[1] = sin(angle)*tempVector[0] + cos(angle)*tempVector[1] + 0*tempVector[2];
      coords[2] = 0*tempVector[0]          + 0*tempVector[1]          + 1*tempVector[2];
    }
    
    public double[] getValues() {
      return coords;
    }
    
    public void setValues(double[] arr) {
      assert(arr.length==coords.length);
      coords = arr;
    }
    
    public int length() {
      return coords.length;
    }
  }

  public static class SphericalCoordinates {

    private double[] coords = null;

    public SphericalCoordinates() {
      this(0,0,0);
    }
		public SphericalCoordinates(double[] x) {
			coords = new double[]{x[0], x[1], x[2]};
		}		
    public SphericalCoordinates(double r, double theta, double phi) {
      coords = new double[]{r, theta, phi};
    }
    
    public double get(int i) {
      return coords[i];
    }

    public CartesianCoordinates convertToCartesian() {
      return new CartesianCoordinates(
        coords[0]*sin(coords[1])*cos(coords[2]),
        coords[0]*sin(coords[1])*sin(coords[2]),
        coords[0]*cos(coords[1])
      );
    }
        
    public double[] getValues() {
      return coords;
    }
    
    public void setValues(double[] arr) {
      assert(arr.length==coords.length);
      coords = arr;
    }
    
    public int length() {
      return coords.length;
    }
  }

  public static double magnitudeOf(double[] vector) {
    double sum = 0.0;
    for (int i = 0; i < vector.length; i++) {
      sum += vector[i] * vector[i];
    }
    return sqrt(sum);
  }
	
	public static class CartesianVelocity {
		
		private double[] coords = null;
				
		public CartesianVelocity() {
			coords = new double[]{0, 0, 0};
		}
		public CartesianVelocity(double[] x) {
			coords = new double[]{x[0], x[1], x[2]};
		}		
		public CartesianVelocity(double x, double y, double z) {
			coords = new double[]{x, y, z};
		}
		
		public double[] getValues() {
			return coords;
		}
		
		public void setValues(double x, double y, double z) {
			coords[0] = x;
			coords[1] = y;
			coords[2] = z;
		}
		
		public SphericalVelocity convertToSpherical(CartesianCoordinates pos) {
			
			double[] car = pos.getValues();
			
			double Cx = 1/magnitudeOf(car);
			double Cy = Cx/sqrt(car[0]*car[0] + car[1]*car[1]);
			double Cz = Cx;
			
			double[][] arr = {{Cx*car[0],        Cx*car[1],        Cx*car[2]},
												{Cy*car[0]*car[2], Cy*car[1]*car[2], -Cy*(car[0]*car[0] + car[1]*car[1])},
												{-Cz*car[1],       Cz*car[0],        0}};
		
			Matrix C2SV = new Matrix(arr);
			Matrix carVel = new Matrix(coords, 1);
			
			return new SphericalVelocity(C2SV.times(carVel).getColumnPackedCopy());
		}
	}
	
	public static class SphericalVelocity {
		
		private double[] coords = null;
		
		public SphericalVelocity() {
			coords = new double[]{0, 0, 0};
		}
		public SphericalVelocity(double[] x) {
			coords = new double[]{x[0], x[1], x[2]};
		}		
		public SphericalVelocity(double r, double theta, double phi) {
			coords = new double[]{r, theta, phi};
		}
		
		public double[] getValues() {
			return coords;
		}
		
		public CartesianVelocity convertToCartesian(SphericalCoordinates pos) {
			
			double[] sph = pos.getValues();
			
			double Kx = cos(sph[1])*cos(sph[2]);
			double Ky = Kx;
			double Kz = cos(sph[1]);
			
			double[][] arr = {{Kx*tan(sph[1]),             Kx*sph[0],              -Kx*sph[0]*tan(sph[1])*tan(sph[2])},
												{Ky*tan(sph[1])*tan(sph[2]), Kx*sph[0]*tan(sph[2]),  Kx*sph[0]*tan(sph[1])},
												{Kz*1,                       -Kz*sph[0]*tan(sph[1]), 0}};
		
			Matrix S2CV = new Matrix(arr);
			Matrix sphVel = new Matrix(coords, 1);
			
			return new CartesianVelocity(S2CV.times(sphVel).getColumnPackedCopy());
		}
		
		public SphericalVelocity rotateEarth() {
			coords[2] -= earthAngVel*simTime;
			return this;
		}

	}

}
