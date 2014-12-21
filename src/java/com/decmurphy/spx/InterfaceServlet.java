package com.decmurphy.spx;

import com.decmurphy.spx.physics.Globals;
import static com.decmurphy.spx.physics.Globals.profile;
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

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    resourcePath = getServletContext().getRealPath("/resource");
    outputPath = getServletContext().getRealPath("/output");
    
    System.out.println("Path: " + resourcePath);
    response.setContentType("text/html");

    PrintWriter out = response.getWriter();
    String title = "Reading All Form Parameters";
    String docType
            = "<!doctype html public \"-//w3c//dtd html 4.0 "
            + "transitional//en\">\n";
    out.println(docType
            + "<html>\n"
            + "<head><title>" + title + "</title></head>\n"
            + "<body bgcolor=\"#f0f0f0\">\n"
            + "<h1 align=\"center\">" + title + "</h1>\n"
            + "<table width=\"100%\" border=\"1\" align=\"center\">\n"
            + "<tr bgcolor=\"#949494\">\n"
            + "<th>Param Name</th><th>Param Value(s)</th>\n"
            + "</tr>\n");

    Enumeration paramNames = request.getParameterNames();

    while (paramNames.hasMoreElements()) {
      String paramName = (String) paramNames.nextElement();
      out.print("<tr><td>" + paramName + "</td>\n<td>");
      String[] paramValues = request.getParameterValues(paramName);

      // Read single valued data
      if (paramValues.length == 1) {
        String paramValue = paramValues[0];
        if (paramValue.length() == 0) {
          out.println("<i>No Value</i>");
        } else {
          out.println(paramValue);
        }
      } else {
        // Read multiple valued data
        out.println("<ul>");
        for (String paramValue : paramValues) {
          out.println("<li>" + paramValue);
        }
        out.println("</ul>");
      }

      try {
        switch (paramName) {

          case "flight_code":
            Globals.flightCode = paramValues[0];
            break;
          case "mei_time":
            profile.setMEITime(Double.parseDouble(paramValues[0]));
            break;
          case "launch_time":
            profile.setLaunchTime(Double.parseDouble(paramValues[0]));
            break;
          case "pitch_time":
            profile.setPitchTime(Double.parseDouble(paramValues[0]));
            break;
          case "meco_time":
            profile.setMECOTime(Double.parseDouble(paramValues[0]));
            break;
          case "fss_time":
            profile.setFSSTime(Double.parseDouble(paramValues[0]));
            break;
          case "sei_time":
            profile.setSEITime(Double.parseDouble(paramValues[0]));
            break;
          case "seco_time":
            profile.setSECOTime(Double.parseDouble(paramValues[0]));
            break;
          case "sss_time":
            profile.setSSSTime(Double.parseDouble(paramValues[0]));
            break;
          case "pitch":
            profile.setPitch(Double.parseDouble(paramValues[0]));
            break;
          case "yaw":
            profile.setYaw(Double.parseDouble(paramValues[0]));
            break;

          default:
            System.out.println("Unrecognised time");
            break;

        }
      } catch (NumberFormatException e) {
      }
    }
    out.println("</tr>\n</table>\n</body></html>");

    Launch.main();

    String site = "DisplayResults";
    response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
    response.setHeader("Location", site);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doGet(request, response);
  }

}
