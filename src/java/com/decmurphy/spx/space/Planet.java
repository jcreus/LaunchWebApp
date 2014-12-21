package com.decmurphy.spx.space;

import java.io.*;

public abstract class Planet {

  protected String name;
  protected double[] pos = new double[3];
  protected double radius;
  protected double mass;

  public Planet(String name, double x, double y, double z, double radius, double mass) {
    this.name = name;
    this.pos[0] = x;
    this.pos[1] = y;
    this.pos[2] = z;
    this.radius = radius;
    this.mass = mass;

    draw();
    System.out.printf(this.name + " created.\n");

  }

  protected void draw() {
    PrintWriter pw = null;

    try {
      File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
      File tempFile = new File(catalinaBase, "webapps/tmp");
      if (!tempFile.exists()) {
        tempFile.mkdirs();
      }
      File outputFile = new File(tempFile, "/" + name + ".output.txt");
      pw = new PrintWriter(new FileWriter(outputFile, false));

      double x, y, z;
      double theta, psi, dt = Math.PI / 36, dp = Math.PI / 36;

      for (theta = 0; theta < Math.PI; theta += dt) {
        z = radius * Math.cos(theta);
        for (psi = 0; psi < 2 * Math.PI; psi += dp) {
          x = radius * Math.sin(theta) * Math.sin(psi);
          y = radius * Math.sin(theta) * Math.cos(psi);

          pw.print(x * 1e-3 + "\t" + y * 1e-3 + "\t" + z * 1e-3 + "\n");
        }
        pw.print("\n");
      }
    } catch (IOException e) {
    } finally {
      if (pw != null) {
        pw.close();
      }
    }
  }
}
