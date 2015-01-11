package com.decmurphy.spx.servlet;

import com.decmurphy.spx.Globals;
import static com.decmurphy.spx.Globals.inputVars;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

/**
 *
 * @author declan
 */
public class InterfaceServlet extends HttpServlet {

	public static String resourcePath;
	public static String outputPath;
	public static String imagePath;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {

		resourcePath = getServletContext().getRealPath("/resource");
		outputPath = getServletContext().getRealPath("/output");
		imagePath = "/var/lib/tomcat8/webapps/ROOT/output";
		//imagePath = "/home/declan/NetBeansProjects/LaunchWebApp/web/output";

		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);

			try {
				switch (paramName) {

					case "flight_code":
						Globals.flightCode = paramValues[0];
						break;
					case "payload_mass":
						inputVars.setPayloadMass(Double.parseDouble(paramValues[0]));
						break;
					case "coast_level":
						Globals.coastMap = "/" + request.getSession().getId() + "_coast_" + paramValues[0] + ".txt";
						break;
					case "mei_time":
						inputVars.setMEITime(Double.parseDouble(paramValues[0]));
						break;
					case "launch_time":
						inputVars.setLaunchTime(Double.parseDouble(paramValues[0]));
						break;
					case "pitch_time":
						inputVars.setPitchTime(Double.parseDouble(paramValues[0]));
						break;
					case "meco_time":
						inputVars.setMECOTime(Double.parseDouble(paramValues[0]));
						break;
					case "fss_time":
						inputVars.setFSSTime(Double.parseDouble(paramValues[0]));
						break;
					case "sei_time":
						inputVars.setSEITime(Double.parseDouble(paramValues[0]));
						break;
					case "seco_time":
						inputVars.setSECOTime(Double.parseDouble(paramValues[0]));
						break;
					case "sss_time":
						inputVars.setSSSTime(Double.parseDouble(paramValues[0]));
						break;
					case "pitch":
						inputVars.setPitch(Double.parseDouble(paramValues[0]));
						break;
					case "yaw":
						inputVars.setYaw(Double.parseDouble(paramValues[0]));
						break;
					case "legs":
						inputVars.setLegs(paramValues[0].equalsIgnoreCase("YES"));
						break;

					default:
						System.out.println("Unrecognised time");
						break;

				}
			} catch (NumberFormatException e) {
			}
		}

		String site = "LoadingPage";
		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", site);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		doGet(request, response);
	}

}
