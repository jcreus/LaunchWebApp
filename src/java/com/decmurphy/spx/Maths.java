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
  
  public static void rotateX(double[] vector, double angle) {
    assert(vector.length==3);
    double[] tempVector = vector;
    
    vector[0] = 1*tempVector[0] + 0*tempVector[1]          + 0*tempVector[2];
    vector[1] = 0*tempVector[0] + cos(angle)*tempVector[1] - sin(angle)*tempVector[2];
    vector[2] = 0*tempVector[0] + sin(angle)*tempVector[1] + cos(angle)*tempVector[2];
  }
  
  public static void rotateY(double[] vector, double angle) {
    assert(vector.length==3);
    double[] tempVector = new double[]{vector[0], vector[1], vector[2]};
    
    vector[0] = cos(angle)*tempVector[0]  + 0*tempVector[1] + sin(angle)*tempVector[2];
    vector[1] = 0*tempVector[0]           + 1*tempVector[1] - 0*tempVector[2];
    vector[2] = -sin(angle)*tempVector[0] + 0*tempVector[1] + cos(angle)*tempVector[2];
  }
    
  public static void rotateZ(double[] vector, double angle) {
    assert(vector.length==3);
    double[] tempVector = vector;
    
    vector[0] = cos(angle)*tempVector[0] - sin(angle)*tempVector[1] + 0*tempVector[2];
    vector[1] = sin(angle)*tempVector[0] + cos(angle)*tempVector[1] + 0*tempVector[2];
    vector[2] = 0*tempVector[0]          + 0*tempVector[1]          + 1*tempVector[2];
  }
  
  public static void convertSphericalToCartesian(double[] sph, double[] cart) {
    assert(sph.length==2 && cart.length==3);
    
    cart[0] = sin(sph[0])*cos(sph[1]);
    cart[1] = sin(sph[0])*sin(sph[1]);
    cart[2] = cos(sph[0]);
    
  }
  
  public static void convertCartesianToSpherical(double[] cart, double[] sph) {
    assert(sph.length==2 && cart.length==3);

    sph[0] = atan2(cart[1], cart[0]);
    sph[1] = acos(cart[2]);
    
  }
  
  public static double magnitudeOf(double[] vector) {
    double sum=0.0;
    for(int i=0;i<vector.length;i++)
      sum += vector[i]*vector[i];
    return sqrt(sum);
  }
}
