package com.decmurphy.spx.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author declan
 */
public class LoadingPage extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		
		Random rand = new Random();
		int numImages = 6;	// number of .webm files in web/images/
		int r = rand.nextInt(numImages) + 1;

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Calculating trajectory...";
		String docType = "<!doctype html>\n";
		out.println(docType
						+ "<html>\n"
						+ " <head>\n"
						+ "   <title>" + title + "</title>\n"
						+ "   <meta charset=\"UTF-8\">\n"
						+ "   <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
						+ "   <meta http-equiv=\"refresh\" content=\"1;URL='ExecuteLaunch?id=" + request.getParameter("id") + "'\" />"
						+ "   <link rel=\"stylesheet\" href=\"css/style.css\" type=\"text/css\"/>\n"
						+ "	  <link rel=\"shortcut icon\" href=\"images/favicon.ico\" type=\"image/x-icon\">\n"
						+ "   <link rel=\"icon\" href=\"images/favicon-logo.png\" type=\"image/x-icon\">\n"
						+ " </head>\n"
						+ " <body>\n"
						+ "   <video autoplay loop poster=\"images/background.jpg\" id=\"bgvid\">\n"
						+ "     <source type=\"video/webm\" src=\"images/launch" + r + ".webm\"/>\n"
						+ "   </video>\n"
						+ " </body>\n"
						+ "</html>");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		doGet(request, response);
	}

}
