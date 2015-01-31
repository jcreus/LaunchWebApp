package com.decmurphy.spx.servlet;

import com.decmurphy.spx.Database;
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
public class Home extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {

		response.setContentType("text/html");
		Database db = new Database();

		String dbPropsFile = getServletContext().getRealPath("/db.properties");

		PrintWriter out = response.getWriter();
		String title = "Launch Simulator";
		String docType = "<!doctype html>\n";
		out.println(docType
						+ "<html>\n"
						+ "  <head>\n"
						+ "    <title>" + title + "</title>\n"
						+ "    <meta charset=\"UTF-8\">\n"
						+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
						+ "    <link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css\">\n"
						+ "    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>\n"
						+ "    <script src=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js\"></script>\n"
						+ "    <script src=\"js/scroll.js\"></script>\n"
						+ "    <script src=\"js/tabpane.js\"></script>\n"
						+ "    <script src=\"js/dynamic_fields.js\"></script>\n"
						+ "    <link rel=\"stylesheet\" href=\"css/style.css\" type=\"text/css\"/>\n"
						+ "    <link rel=\"stylesheet\" href=\"css/tabs.css\" type=\"text/css\"/>\n"
						+ "    <link rel=\"stylesheet\" href=\"css/button.css\" type=\"text/css\"/>\n"
						+ "	   <link rel=\"shortcut icon\" href=\"images/favicon.ico\" type=\"image/x-icon\">\n"
						+ "    <link rel=\"icon\" href=\"images/favicon-logo.png\" type=\"image/x-icon\">\n"
						+ "  </head>\n"
						+ "  <body id=\"home\">\n"
						+ "    <div class=\"bg\">\n"
						+ "      <img src=\"images/background.jpg\" alt=\"background\" />\n"
						+ "    </div>\n"
						+ "    <div id=\"container\">\n"
						+ "      <div class=\"tabbable\">\n"
						+ db.buildProfilesList(dbPropsFile)
						+ "      </div>\n"
						+ "    </div>\n"
						+ "    <div id=\"footer\">\n"
						+ "      Created by <a href=\"http://www.decmurphy.com\">Declan</a>, a.k.a /u/TheVehicleDestroyer, for the community at /r/SpaceX.<br>\n"
						+ "      I have no affiliation with SpaceX whatsoever. This app is purely for entertainment purposes."
						+ "    </div>\n"
						+ "  </body>\n"
						+ "</html>");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		doGet(request, response);
	}
}
