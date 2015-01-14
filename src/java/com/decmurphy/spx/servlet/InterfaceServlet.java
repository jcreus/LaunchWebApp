package com.decmurphy.spx.servlet;

import com.decmurphy.spx.Globals;
import static com.decmurphy.spx.Globals.flightCode;
import static com.decmurphy.spx.Globals.mission;
import com.decmurphy.spx.mission.MissionBuilder;
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

			switch (paramName) {

				case "flight_code":
					Globals.flightCode = paramValues[0];
					MissionBuilder mb = new MissionBuilder();
					mission = mb.createMission(flightCode);
					break;
				default:
					break;
			}

			try {
				switch (paramName) {

					case "coast_level":	Globals.coastMap = "/" + request.getSession().getId() + "_coast_" + paramValues[0] + ".txt"; break;
					case "payload_mass":
						double m;
						if((m = Double.parseDouble(paramValues[0])) > 0) {
							mission.Payload().setMass(m);
						}
						break;
						
					case "mei_time":	mission.Profile().addEvent("firstStageIgnition", Double.parseDouble(paramValues[0])).addExtraInfo("stage", 0); break;
					case "launch_time":	mission.Profile().addEvent("releaseClamps", Double.parseDouble(paramValues[0])).addExtraInfo("stage", 0); break;
					case "pitch_time":	mission.Profile().addEvent("pitchKick", Double.parseDouble(paramValues[0])).addExtraInfo("stage", 0); break;
					case "pitch":		mission.Profile().getEvent("pitchKick").addExtraInfo("pitch", Double.parseDouble(paramValues[0])).addExtraInfo("stage", 0); break;
					case "yaw":			mission.Profile().getEvent("pitchKick").addExtraInfo("yaw", Double.parseDouble(paramValues[0])).addExtraInfo("stage", 0); break;
					case "meco_time":	mission.Profile().addEvent("MECO1", Double.parseDouble(paramValues[0])).addExtraInfo("stage", 0); break;
						
					case "fss_time":	mission.Profile().addEvent("firstStageSep", Double.parseDouble(paramValues[0])); break;
						
					case "sei_time":	mission.Profile().addEvent("secondStageIgnition", Double.parseDouble(paramValues[0])).addExtraInfo("stage", 1);	break;
					case "seco_time":	mission.Profile().addEvent("SECO1", Double.parseDouble(paramValues[0])).addExtraInfo("stage", 1); break;
					case "sss_time":	mission.Profile().addEvent("secondStageSep", Double.parseDouble(paramValues[0])).addExtraInfo("stage", 1); break;
						
					case "legs":		mission.LaunchVehicle().setLegs(paramValues[0].equalsIgnoreCase("YES")); break;
					default: break;

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
