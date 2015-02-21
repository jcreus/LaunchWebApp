package com.decmurphy.spx.space;

import static com.decmurphy.spx.servlet.InterfaceServlet.outputPath;
import static com.decmurphy.spx.servlet.InterfaceServlet.resourcePath;
import com.decmurphy.utils.Globals;
import static com.decmurphy.utils.Globals.massOfEarth;
import static com.decmurphy.utils.Globals.radiusOfEarth;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

/**
 *
 * @author declan
 */
public final class Earth extends Planet {

	private String id;

	public Earth(double x, double y, double z) {
		this(x, y, z, "");
	}

	public Earth(double x, double y, double z, String id) {
		super("Earth", x, y, z, id, radiusOfEarth, massOfEarth);
		this.id = id;
		draw();
		drawHazard();
	}

	@Override
	protected void draw() {

		PrintWriter pw = null;

		try {

			File outputFile = new File(outputPath, "/" + id + "_" + name + ".output.txt");
			pw = new PrintWriter(new FileWriter(outputFile, false));

			double x, y, z;
			double theta, psi, dt = PI/36, dp = PI/36;

			for (theta=0; theta<PI; theta+=dt) {
				z = radius*cos(theta);
				for (psi=0; psi<2*PI; psi+=dp) {
					x = radius*sin(theta)*cos(psi);
					y = radius*sin(theta)*sin(psi);

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

	protected void drawHazard() {
		PrintWriter pw = null;
		BufferedReader br;

		double theta, psi;
		double R = radiusOfEarth * 1e-3;

		try {
			String line;
			File outputFile = new File(outputPath, "/" + id + "_hazard.output.txt");
			pw = new PrintWriter(new FileWriter(outputFile, false));

			File hazardFile = new File(resourcePath, "/" + Globals.flightCode + ".hazard.txt");
			br = new BufferedReader(new FileReader(hazardFile));

			while ((line = br.readLine()) != null) {
				if (!line.isEmpty()) {
					String[] parts = line.split("\\s+");

					psi = toRadians(Double.parseDouble(parts[0]));
					theta = toRadians(90 - Double.parseDouble(parts[1]));
					pw.print(R*sin(theta)*cos(psi) + "\t" + R*sin(theta)*sin(psi) + "\t" + R*cos(theta) + "\n");
				} else {
					pw.print("\n");
				}
			}
		} catch (IOException e) {
		} finally {
			if (pw != null) {
				pw.close();
			}
			System.out.printf("Hazard map drawn.\n");
		}
	}

}
