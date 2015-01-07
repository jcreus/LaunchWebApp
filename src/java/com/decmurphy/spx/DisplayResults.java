package com.decmurphy.spx;

import com.decmurphy.spx.physics.Globals;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author declan
 */
public class DisplayResults extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {

		String id = request.getSession().getId();
		String[] args = {id};
		Launch.main(args);

		GnuplotFileBuilder gp_landing = null,
						gp_globe = null,
						gp_alt = null,
						gp_landing2 = null,
						gp_velocity = null,
						gp_phase = null;

		try {
			gp_landing = new GnuplotFileBuilder(id, "landing");
			Process p1 = Runtime.getRuntime().exec("gnuplot " + gp_landing.getPath());
			p1.waitFor();

			gp_globe = new GnuplotFileBuilder(id, "globe");
			Process p2 = Runtime.getRuntime().exec("gnuplot " + gp_globe.getPath());
			p2.waitFor();

			gp_alt = new GnuplotFileBuilder(id, "altitude");
			Process p3 = Runtime.getRuntime().exec("gnuplot " + gp_alt.getPath());
			p3.waitFor();

			gp_landing2 = new GnuplotFileBuilder(id, "landing2");
			Process p4 = Runtime.getRuntime().exec("gnuplot " + gp_landing2.getPath());
			p4.waitFor();

			gp_velocity = new GnuplotFileBuilder(id, "velocity");
			Process p5 = Runtime.getRuntime().exec("gnuplot " + gp_velocity.getPath());
			p5.waitFor();

			gp_phase = new GnuplotFileBuilder(id, "phase");
			Process p6 = Runtime.getRuntime().exec("gnuplot " + gp_phase.getPath());
			p6.waitFor();
		} catch (InterruptedException e) {
		}

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = Globals.flightCode + " Results";
		String docType = "<!doctype html>\n";

		out.println(docType
						+ "<html>\n"
						+ " <head>\n"
						+ "   <title>" + title + "</title>\n"
						+ "   <meta charset=\"UTF-8\">\n"
						+ "   <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
						+ "   <link rel=\"stylesheet\" href=\"css/style.css\" type=\"text/css\"/>\n"
						+ "   <link rel=\"stylesheet\" href=\"css/tabs.css\" type=\"text/css\"/>\n"
						+ "   <link rel=\"stylesheet\" href=\"css/button.css\" type=\"text/css\"/>\n"
						+ " </head>\n"
						+ " <body>\n"
						+ "   <div class=\"bg\">\n"
						+ "     <img src=\"images/background.jpg\" alt=\"background\" />\n"
						+ "   </div>\n"
						+ "   <div class=\"container\">\n"
						+ "     <div id=\"row1\">\n"
						+ "       <img class=\"first\" src=\"" + gp_landing.getImgPath() + "\" alt=\"first-stage-trajectory\"/>\n"
						+ "       <img class=\"second\" src=\"" + gp_globe.getImgPath() + "\" alt=\"wide-view\"/>\n"
						+ "       <img class=\"third\" src=\"" + gp_alt.getImgPath() + "\" alt=\"altitude\"/>\n"
						+ "     </div>\n"
						+ "     <div id=\"row2\">\n"
						+ "       <img class=\"fourth\" src=\"" + gp_landing2.getImgPath() + "\" alt=\"alt-first-stage-trajectory\"/>\n"
						+ "       <img class=\"fifth\" src=\"" + gp_velocity.getImgPath() + "\" alt=\"velocity\"/>\n"
						+ "       <img class=\"sixth\" src=\"" + gp_phase.getImgPath() + "\" alt=\"phase-space\"/>\n"
						+ "     </div>\n"
						+ "   </div>\n"
						+ " </body>\n"
						+ "</html>");
/*
		System.out.println("Calling invalidate()\n");
		request.getSession().invalidate();
		System.out.println("Called invalidate()\n");
*/
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		doGet(request, response);
	}

}
