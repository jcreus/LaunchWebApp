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
						gp_aoa = null,
						gp_landing2 = null,
						gp_velocity = null,
						gp_phase = null;

		try {
			gp_landing = new GnuplotFileBuilder(ID, "landing");
			Process p1 = Runtime.getRuntime().exec("gnuplot " + gp_landing.getPath());
			p1.waitFor();

			gp_globe = new GnuplotFileBuilder(ID, "globe");
			Process p2 = Runtime.getRuntime().exec("gnuplot " + gp_globe.getPath());
			p2.waitFor();

			gp_alt = new GnuplotFileBuilder(ID, "altitude");
			Process p3 = Runtime.getRuntime().exec("gnuplot " + gp_alt.getPath());
			p3.waitFor();

			gp_landing2 = new GnuplotFileBuilder(ID, "landing2");
			Process p4 = Runtime.getRuntime().exec("gnuplot " + gp_landing2.getPath());
			p4.waitFor();

			gp_velocity = new GnuplotFileBuilder(ID, "velocity");
			Process p5 = Runtime.getRuntime().exec("gnuplot " + gp_velocity.getPath());
			p5.waitFor();

			gp_phase = new GnuplotFileBuilder(ID, "phase");
			Process p6 = Runtime.getRuntime().exec("gnuplot " + gp_phase.getPath());
			p6.waitFor();

			gp_aoa = new GnuplotFileBuilder(ID, "aoa");
			Process p7 = Runtime.getRuntime().exec("gnuplot " + gp_aoa.getPath());
			p7.waitFor();
		} catch (InterruptedException e) {
		}

		StringBuilder sb = new StringBuilder("<!doctype html>\n");
		sb.append("<html>\n"
						+ " <head>\n"
						+ "   <title>" + Globals.flightCode + " Results</title>\n"
						+ "   <meta charset=\"UTF-8\">\n"
						+ "   <meta name=\"viewport\" content=\"wIDth=device-wIDth, initial-scale=1.0\">\n"
						+ "   <link rel=\"stylesheet\" href=\"css/style.css\" type=\"text/css\"/>\n"
						+ "   <link rel=\"stylesheet\" href=\"css/tabs.css\" type=\"text/css\"/>\n"
						+ "   <link rel=\"stylesheet\" href=\"css/button.css\" type=\"text/css\"/>\n"
						+ "	   <link rel=\"shortcut icon\" href=\"images/favicon.ico\" type=\"image/x-icon\">\n"
						+ "    <link rel=\"icon\" href=\"images/favicon-logo.png\" type=\"image/x-icon\">\n"
						+ " </head>\n"
						+ " <body>\n"
						+ "   <div class=\"bg\">\n"
						+ "     <img src=\"images/background.jpg\" alt=\"background\" />\n"
						+ "   </div>\n"
						+ "   <div class=\"container\">\n"
						+ "     <div ID=\"row1\">\n"
						+ "       <img class=\"first\" src=\"" + gp_landing.getImgPath() + "\" alt=\"first-stage-trajectory\"/>\n"
						+ "       <img class=\"second\" src=\"" + gp_globe.getImgPath() + "\" alt=\"wide-view\"/>\n"
						+ "       <img class=\"third\" src=\"" + gp_alt.getImgPath() + "\" alt=\"altitude\"/>\n"
						+ "     </div>\n"
						+ "     <div ID=\"row2\">\n"
						+ "       <img class=\"fourth\" src=\"" + gp_landing2.getImgPath() + "\" alt=\"alt-first-stage-trajectory\"/>\n"
						+ "       <img class=\"fifth\" src=\"" + gp_velocity.getImgPath() + "\" alt=\"velocity\"/>\n"
						+ "       <img class=\"sixth\" src=\"" + gp_phase.getImgPath() + "\" alt=\"phase-space\"/>\n"
						+ "     </div>\n"
						+ "     <div ID=\"row3\">\n"
						+ "       <img class=\"seventh\" src=\"" + gp_aoa.getImgPath() + "\" alt=\"angle-of-attack\"/>\n"
						+ "       <img class=\"eigth\" src=\"" + "\" alt=\"\"/>\n"
						+ "       <img class=\"ninth\" src=\"" + "\" alt=\"\"/>\n"
						+ "     </div>\n"
						+ "   </div>\n"
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
