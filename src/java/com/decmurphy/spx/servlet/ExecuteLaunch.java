package com.decmurphy.spx.servlet;

import com.decmurphy.spx.ProcessLaunch;
import com.decmurphy.spx.mission.Mission;
import com.decmurphy.utils.Globals;
import com.decmurphy.utils.GnuplotFileBuilder;
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
      String[] args = {};
      mission.setMissionId(ID);
      ProcessLaunch.execute(mission, args);
		}

		GnuplotFileBuilder gp_landing = null,
						gp_globe = null,
						gp_alt1 = null,
            gp_alt2 = null,
						gp_landing2 = null,
						gp_velocity1 = null,
						gp_velocity2 = null,
						gp_phase1 = null,
						gp_phase2 = null,
            gp_profile1 = null,
            gp_profile2 = null,
            gp_q = null,
						gp_prop = null;
		
		try {
			gp_landing = new GnuplotFileBuilder(ID, "landing");
			Process p1 = Runtime.getRuntime().exec("gnuplot " + gp_landing.getPath());
			//Process p1 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_landing.getPath());
			p1.waitFor();

			gp_globe = new GnuplotFileBuilder(ID, "globe");
			Process p2 = Runtime.getRuntime().exec("gnuplot " + gp_globe.getPath());
			//Process p2 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_globe.getPath());
			p2.waitFor();

			gp_alt1 = new GnuplotFileBuilder(ID, mission, "altitude1");
			Process p3 = Runtime.getRuntime().exec("gnuplot " + gp_alt1.getPath());
			//Process p3 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_alt1.getPath());
			p3.waitFor();

			gp_landing2 = new GnuplotFileBuilder(ID, "landing2");
			Process p4 = Runtime.getRuntime().exec("gnuplot " + gp_landing2.getPath());
			//Process p4 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_landing2.getPath());
			p4.waitFor();

			gp_velocity1 = new GnuplotFileBuilder(ID, mission, "velocity1");
			Process p5 = Runtime.getRuntime().exec("gnuplot " + gp_velocity1.getPath());
			//Process p5 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_velocity1.getPath());
			p5.waitFor();

			gp_phase1 = new GnuplotFileBuilder(ID, "phase1");
			Process p6 = Runtime.getRuntime().exec("gnuplot " + gp_phase1.getPath());
			//Process p6 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_phase1.getPath());
			p6.waitFor();
			
      gp_profile1 = new GnuplotFileBuilder(ID, mission, "profile1");
			Process p7 = Runtime.getRuntime().exec("gnuplot " + gp_profile1.getPath());
			//Process p7 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_profile1.getPath());
			p7.waitFor();
			
      gp_q = new GnuplotFileBuilder(ID, "q");
			Process p8 = Runtime.getRuntime().exec("gnuplot " + gp_q.getPath());
			//Process p8 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_q.getPath());
			p8.waitFor();

			gp_alt2 = new GnuplotFileBuilder(ID, mission, "altitude2");
			Process p9 = Runtime.getRuntime().exec("gnuplot " + gp_alt2.getPath());
			//Process p9 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_alt.getPath());
			p9.waitFor();

			gp_velocity2 = new GnuplotFileBuilder(ID, mission, "velocity2");
			Process p10 = Runtime.getRuntime().exec("gnuplot " + gp_velocity2.getPath());
			//Process p10 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_velocity.getPath());
			p10.waitFor();

			gp_phase2 = new GnuplotFileBuilder(ID, "phase2");
			Process p11 = Runtime.getRuntime().exec("gnuplot " + gp_phase2.getPath());
			//Process p11 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_phase2.getPath());
			p11.waitFor();
			
      gp_profile2 = new GnuplotFileBuilder(ID, mission, "profile2");
			Process p12 = Runtime.getRuntime().exec("gnuplot " + gp_profile2.getPath());
			//Process p12 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_profile2.getPath());
			p12.waitFor();
			
      gp_prop = new GnuplotFileBuilder(ID, mission, "prop");
			Process p13 = Runtime.getRuntime().exec("gnuplot " + gp_prop.getPath());
			//Process p13 = Runtime.getRuntime().exec("C:\\cygwin64\\bin\\gnuplot.exe " + gp_prop.getPath());
			p13.waitFor();			
      
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
						+ "     <table class=\"results\">\n"
						+ "	      <tr>\n"
						+ "         <td><img src=\"" + gp_globe.getImgPath() + "\" alt=\"wide-view\"/></td>\n"
						+ "	        <td><img src=\"" + gp_alt1.getImgPath() + "\" alt=\"altitude1\"/></td>\n"
						+ "	        <td><img src=\"" + gp_alt2.getImgPath() + "\" alt=\"altitude2\"/></td>\n"
						+ "	      </tr>\n"
						+ "       <tr>\n"
						+ "	        <td><img src=\"" + gp_landing.getImgPath() + "\" alt=\"first-stage-trajectory\"/></td>\n"
						+ "	        <td><img src=\"" + gp_landing2.getImgPath() + "\" alt=\"alt-first-stage-trajectory\"/></td>\n"
						+ "	        <td><img src=\"" + gp_profile1.getImgPath() + "\" alt=\"profile1\"/></td>\n"
						+ "	      </tr>\n"
						+ "	      <tr>\n"
						+ "	        <td><img src=\"" + gp_velocity1.getImgPath() + "\" alt=\"velocity1\"/></td>\n"
						+ "         <td><img src=\"" + gp_velocity2.getImgPath() + "\" alt=\"velocity2\"/></td>\n"
						+ "	        <td><img src=\"" + gp_prop.getImgPath() + "\" alt=\"prop\"/></td>\n"
						+ "	      </tr>\n"
						+ "	      <tr>\n"
						+ "         <td><img src=\"" + gp_q.getImgPath() + "\" alt=\"aero-pressure\"/></td>\n"
						+ "         <td><img src=\"" + gp_phase1.getImgPath() + "\" alt=\"phase-space1\"/></td>\n"
						+ "         <td><img src=\"" + gp_phase2.getImgPath() + "\" alt=\"phase-space2\"/></td>\n"
						+ "	      </tr>\n"
						+ "	    </table>\n"
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
