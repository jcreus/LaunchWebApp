package com.decmurphy.spx.servlet;

import com.decmurphy.spx.mission.Mission;
import com.decmurphy.spx.mission.MissionBuilder;
import com.decmurphy.spx.util.Correction;
import com.decmurphy.utils.Globals;
import static com.decmurphy.utils.Globals.flightCode;
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
    imagePath = "/var/lib/tomcat8/webapps/LaunchWebApp/output";
		//imagePath = "/home/declan/NetBeansProjects/LaunchWebApp/web/output";

    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    String getId = UUID.randomUUID().toString();
    Mission mission = null;

    Enumeration paramNames = request.getParameterNames();
    while (paramNames.hasMoreElements()) {
      String paramName = (String) paramNames.nextElement();
      String[] paramValues = request.getParameterValues(paramName);

      if (!paramName.isEmpty()) {

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
					
					if(paramName.startsWith("correction")) paramName = "correction";

          switch (paramName) {

            case "payload_mass":
              double m;
              if ((m = Double.parseDouble(paramValues[0])) >= 0.0) {
                mission.Payload().setMass(m);
              }
              else {
                throw new IllegalArgumentException("Illegal value for Payload Mass. Must be non-negative");
              }
              break;

            case "mei1_time":
              mission.Profile()
									.addEvent("firstStageIgnition", Double.parseDouble(paramValues[0]))
									.addExtraInfo("stage", 0);
              break;
            case "launch_time":
              mission.Profile()
									.addEvent("releaseClamps", Double.parseDouble(paramValues[0]))
									.addExtraInfo("stage", 0);
              break;
            case "pitch_time":
              mission.Profile()
									.addEvent("pitchKick", Double.parseDouble(paramValues[0]))
									.addExtraInfo("stage", 0);
              break;
            case "pitch":
              mission.Profile()
									.getEvent("pitchKick")
									.addExtraInfo("pitch", Double.parseDouble(paramValues[0]));
              break;
            case "yaw":
              mission.Profile()
									.getEvent("pitchKick")
									.addExtraInfo("yaw", Double.parseDouble(paramValues[0]));
              break;
            case "meco1_time":
              mission.Profile()
									.addEvent("MECO1", Double.parseDouble(paramValues[0]))
									.addExtraInfo("stage", 0);
              break;
            case "fss_time":
              mission.Profile()
									.addEvent("firstStageSep", Double.parseDouble(paramValues[0]));
              break;
            case "sei1_time":
              mission.Profile()
									.addEvent("secondStageIgnition", Double.parseDouble(paramValues[0]))
									.addExtraInfo("stage", 1);
              break;

            case "mei2_time":
              mission.Profile()
									.addEvent("boost_start", Double.parseDouble(paramValues[0]));
              break;
            case "meco2_time":
              mission.Profile()
									.addEvent("boost_end", Double.parseDouble(paramValues[0]));
              break;
            case "mei3_time":
              mission.Profile()
									.addEvent("entry_start", Double.parseDouble(paramValues[0]));
              break;
            case "meco3_time":
              mission.Profile()
									.addEvent("entry_end", Double.parseDouble(paramValues[0]));
              break;
            case "mei4_time":
              mission.Profile()
									.addEvent("landing_start", Double.parseDouble(paramValues[0]));
              break;
						case "correction":
							try {
								mission.Profile()
										.addEvent("correction", Double.parseDouble(paramValues[2]))
										.addExtraInfo("stage", Integer.parseInt(paramValues[0]))
										.addCorrectionType("type", Correction.findCorrectionType(paramValues[1]))
										.addExtraInfo("param", Double.parseDouble(paramValues[3]));
								break;
							} catch(ArrayIndexOutOfBoundsException e) {
								
							}
							
            case "legs":
              mission.LaunchVehicle()
									.setLegs(paramValues[0].equalsIgnoreCase("YES"));
              break;
            default:
              break;

          }

        } catch (NumberFormatException e) {
        }
      }
    }

    request.getSession().setAttribute(getId, mission);
    response.sendRedirect("/LaunchWebApp/LoadingPage?id=" + getId);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doGet(request, response);
  }

}
