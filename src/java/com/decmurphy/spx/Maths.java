package com.decmurphy.spx;

import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

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
}
