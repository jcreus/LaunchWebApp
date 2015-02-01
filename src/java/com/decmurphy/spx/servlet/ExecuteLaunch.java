package com.decmurphy.spx.servlet;

import com.decmurphy.spx.Globals;
import com.decmurphy.spx.GnuplotFileBuilder;
import com.decmurphy.spx.Launch;
import com.decmurphy.spx.mission.Mission;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author declan
 */
public class ExecuteLaunch extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {

		String ID = request.getParameter("id");
		Mission mission = (Mission) request.getSession().getAttribute(ID);

		if (mission != null) {
			String[] args = {ID};
			Launch.execute(mission, args);
		}

		GnuplotFileBuilder gp_landing = null,
						gp_globe = null,
						gp_alt = null,
						gp_landing2 = null,
						gp_velocity = null,
						gp_phase = null,
            gp_profile = null,
            gp_q = null;

		try {
			gp_landing = new GnuplotFileBuilder(ID, "landing");
			Process p1 = Runtime.getRuntime().exec("gnuplot " + gp_landing.getPath());
			//Process p1 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_landing.getPath());
			p1.waitFor();

			gp_globe = new GnuplotFileBuilder(ID, "globe");
			Process p2 = Runtime.getRuntime().exec("gnuplot " + gp_globe.getPath());
			//Process p2 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_globe.getPath());
			p2.waitFor();

			gp_alt = new GnuplotFileBuilder(ID, mission, "altitude");
			Process p3 = Runtime.getRuntime().exec("gnuplot " + gp_alt.getPath());
			//Process p3 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_alt.getPath());
			p3.waitFor();

			gp_landing2 = new GnuplotFileBuilder(ID, "landing2");
			Process p4 = Runtime.getRuntime().exec("gnuplot " + gp_landing2.getPath());
			//Process p4 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_landing2.getPath());
			p4.waitFor();

			gp_velocity = new GnuplotFileBuilder(ID, mission, "velocity");
			Process p5 = Runtime.getRuntime().exec("gnuplot " + gp_velocity.getPath());
			//Process p5 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_velocity.getPath());
			p5.waitFor();

			gp_phase = new GnuplotFileBuilder(ID, "phase");
			Process p6 = Runtime.getRuntime().exec("gnuplot " + gp_phase.getPath());
			//Process p6 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_phase.getPath());
			p6.waitFor();
			
      gp_profile = new GnuplotFileBuilder(ID, mission, "profile");
			Process p7 = Runtime.getRuntime().exec("gnuplot " + gp_profile.getPath());
			//Process p7 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_profile.getPath());
			p7.waitFor();
			
      gp_q = new GnuplotFileBuilder(ID, "q");
			Process p8 = Runtime.getRuntime().exec("gnuplot " + gp_q.getPath());
			//Process p8 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_q.getPath());
			p8.waitFor();
      
		} catch (InterruptedException e) {
		}

		StringBuilder sb = new StringBuilder("<!doctype html>\n");
		sb.append("<html>\n"
						+ " <head>\n"
						+ "   <title>" + Globals.flightCode + " Results</title>\n"
						+ "   <meta charset=\"UTF-8\">\n"
						+ "   <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
						+ "   <link rel=\"stylesheet\" href=\"css/style.css\" type=\"text/css\"/>\n"
						+ "   <link rel=\"stylesheet\" href=\"css/tabs.css\" type=\"text/css\"/>\n"
						+ "   <link rel=\"stylesheet\" href=\"css/button.css\" type=\"text/css\"/>\n"
						+ "	   <link rel=\"shortcut icon\" href=\"images/favicon.ico\" type=\"image/x-icon\">\n"
						+ "    <link rel=\"icon\" href=\"images/favicon-logo.png\" type=\"image/x-icon\">\n"
						+ " </head>\n"
						+ " <body id=\"results\">\n"
						+ "   <div class=\"bg\">\n"
						+ "     <img src=\"images/background.jpg\" alt=\"background\" />\n"
						+ "   </div>\n"
						+ "   <div class=\"container\">\n"
						+ "			<table class=\"results\">\n"
						+ "				<tr>\n"
						+ "					<td><img src=\"" + gp_globe.getImgPath() + "\" alt=\"wide-view\"/></td>\n"
						+ "					<td><img src=\"" + gp_landing.getImgPath() + "\" alt=\"first-stage-trajectory\"/></td>\n"
						+ "					<td><img src=\"" + gp_alt.getImgPath() + "\" alt=\"altitude\"/></td>\n"
						+ "				</tr>\n"
						+ "				<tr>\n"
						+ "	        <td><img src=\"" + gp_phase.getImgPath() + "\" alt=\"phase-space\"/></td>\n"
						+ "					<td><img src=\"" + gp_landing2.getImgPath() + "\" alt=\"alt-first-stage-trajectory\"/></td>\n"
						+ "					<td><img src=\"" + gp_profile.getImgPath() + "\" alt=\"profile\"/></td>\n"
						+ "			  </tr>\n"
						+ "				<tr>\n"
						+ "		      <td><img src=\"" + gp_velocity.getImgPath() + "\" alt=\"velocity\"/></td>\n"
						+ "		      <td><img src=\"" + gp_q.getImgPath() + "\" alt=\"aeroPressure\"/></td>\n"
						+ "				</tr>\n"
						+ "			</table>\n"
						+ " </body>\n"
						+ "</html>");
		String body = sb.toString();

		request.getSession().setAttribute(ID, body);
		response.sendRedirect("/LaunchWebApp/DisplayResults?id=" + ID);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		doGet(request, response);
	}

}
