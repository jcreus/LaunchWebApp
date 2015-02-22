package com.decmurphy.utils;

import Jama.Matrix;
import static com.decmurphy.utils.Globals.earthAngVel;
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
			assert(x.length==3);
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

			double[][] arr = {{1, 0         ,  0         },
                        {0, cos(angle), -sin(angle)},
                        {0, sin(angle),  cos(angle)}};
			
			Matrix rX = new Matrix(arr);
			Matrix v = new Matrix(coords, 1).transpose();
			Matrix V = rX.times(v);

			for(int i=0;i<coords.length;i++)
				coords[i] = V.get(i,0);
    }

    public void rotateY(double angle) {

			double[][] arr = {{ cos(angle), 0, sin(angle)},
				                { 0         , 1, 0         }, 
                        {-sin(angle), 0, cos(angle)}};
			
			Matrix rY = new Matrix(arr);
			Matrix v = new Matrix(coords, 1).transpose();
			Matrix V = rY.times(v);

			for(int i=0;i<coords.length;i++)
				coords[i] = V.get(i,0);
    }

    public void rotateZ(double angle) {

			double[][] arr = {{cos(angle), -sin(angle), 0},
                        {sin(angle),  cos(angle), 0},
                        {0         ,  0         , 1}};
			
			Matrix rZ = new Matrix(arr);
			Matrix v = new Matrix(coords, 1).transpose();
			Matrix V = rZ.times(v);

			for(int i=0;i<coords.length;i++)
				coords[i] = V.get(i,0);
    }
    
    public double[] getValues() {
      return coords;
    }
  }

  public static class SphericalCoordinates {

    private double[] coords = null;

    public SphericalCoordinates() {
      this(0,0,0);
    }
		public SphericalCoordinates(double[] x) {
			this(x[0], x[1], x[2]);
			assert(x.length==3);
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
			this(0, 0, 0);
		}
		public CartesianVelocity(double[] x) {
			this(x[0], x[1], x[2]);
			assert(x.length==3);
		}		
		public CartesianVelocity(double x, double y, double z) {
			coords = new double[]{x, y, z};
		}
		
		public double[] getValues() {
			return coords;
		}
		
		public SphericalVelocity convertToSpherical(CartesianCoordinates pos) {
			
			double[] car = pos.getValues();
			
			double Cx = 1/magnitudeOf(car);
			double Cy = Cx*Cx/sqrt(car[0]*car[0] + car[1]*car[1]);
			double Cz = -1/(car[0]*car[0] + car[1]*car[1]);
			
			/*
				DiagMatrix C = {Cx, Cy, Cz};
			
				double[][] arr = {
					{car[0],        car[1],         car[2]},
					{car[0]*car[2], car[1]*car[2], -(car[0]*car[0] + car[1]*car[1])},
					{car[1],       -car[0],         0}
				};
		
				Matrix C2SV = DiagMatrix.times(new Matrix(arr));
			*/
			
			double[][] arr = {
				{Cx*car[0],        Cx*car[1],         Cx*car[2]},
        {Cy*car[0]*car[2], Cy*car[1]*car[2], -Cy*(car[0]*car[0] + car[1]*car[1])},
        {Cz*car[1],       -Cz*car[0],         0}
			};
		
			Matrix C2SV = new Matrix(arr);
			Matrix carVel = new Matrix(coords, 1);
      Matrix res = C2SV.times(carVel.transpose());
			
			return new SphericalVelocity(res.get(0,0), res.get(1,0), res.get(2,0));
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
			
			/*
				DiagMatrix K = {Kx, Ky, Kz};		
			
				double[][] arr = {
					{tan(sph[1]),             sph[0],            -sph[0]*tan(sph[1])*tan(sph[2])},
					{tan(sph[1])*tan(sph[2]), sph[0]*tan(sph[2]), sph[0]*tan(sph[1])},
					{1,                      -sph[0]*tan(sph[1]), 0}
				};
		
				Matrix S2CV = DiagMatrix.times(new Matrix(arr));
			*/
			
			double[][] arr = {
				{Kx*tan(sph[1]),             Kx*sph[0],            -Kx*sph[0]*tan(sph[1])*tan(sph[2])},
				{Ky*tan(sph[1])*tan(sph[2]), Ky*sph[0]*tan(sph[2]), Ky*sph[0]*tan(sph[1])},
				{Kz*1,                      -Kz*sph[0]*tan(sph[1]), 0}
			};
		
			Matrix S2CV = new Matrix(arr);
			Matrix sphVel = new Matrix(coords, 1);
			Matrix res = S2CV.times(sphVel.transpose());
			return new CartesianVelocity(res.get(0,0), res.get(1,0), res.get(2,0));
		}
		
		public SphericalVelocity rotateEarth() {
			coords[2] -= earthAngVel;
			return this;
		}

	}

}
