package com.decmurphy.spx;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author declan
 * This page isn't used at all at the moment - but hopefully I can figure out how to
 * integrate it someday! It looks cool as fuck though
 */
public class LoadingPage extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    String title = "Calculating trajectory...";
    String docType
            = "<!doctype html public \"-//w3c//dtd html 4.0 "
            + "transitional//en\">\n";
    out.println(docType
            + "<html>\n"
            + "<head>\n"
            + "<title>" + title + "</title>\n"
            + "<meta charset=\"UTF-8\">\n"
            + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
            + "<link rel=\"stylesheet\" href=\"" + request.getContextPath() + "/css/style.css\" type=\"text/css\"/>\n"
            + "</head>\n"
            + "<body>\n"
            + "<video autoplay loop poster=\"" + request.getContextPath() + "/images/background.jpg\" id=\"bgvid\">\n"
            + "<source src=\"" + request.getContextPath() + "/images/launch.webm\" type=\"video/webm\">\n"
            + "</video>\n");
    out.println("</body>\n</html>");

    String site = "DisplayResults";
    //response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
    //response.setHeader("Location", site);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doGet(request, response);
  }

}
