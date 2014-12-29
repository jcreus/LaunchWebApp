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
 */
public class Home extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    response.setContentType("text/html");
    Database db = new Database();
    
    String dbPropsFile = getServletContext().getRealPath("/db.properties");

    PrintWriter out = response.getWriter();
    String title = "Launch Sim - WebApp development";
    String docType = "<!doctype html>\n";
    out.println(docType
            + "<html>\n"
            + " <head>\n"
            + "   <title>" + title + "</title>\n"
            + "   <meta charset=\"UTF-8\">\n"
            + "   <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
            
            + "   <link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css\">\n"
            + "   <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>\n"
            + "   <script src=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js\"></script>\n"
            
            + "   <link rel=\"stylesheet\" href=\"" + request.getContextPath() + "/css/style.css\" type=\"text/css\"/>\n"
            + "   <link rel=\"stylesheet\" href=\"" + request.getContextPath() + "/css/tabs.css\" type=\"text/css\"/>\n"
            + "   <link rel=\"stylesheet\" href=\"" + request.getContextPath() + "/css/button.css\" type=\"text/css\"/>\n"
            + " </head>\n"
            + " <body>\n"
            + "   <div class=\"bg\">\n"
            + "     <img src=\"images/background.jpg\" alt=\"background\" />\n"
            + "   </div>\n"
            + "   <div id=\"container\">\n"
            +       db.buildProfilesList(dbPropsFile)
            + "   </div>\n"
            + " </body>\n"
            + "</html>");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doGet(request, response);
  }
}
