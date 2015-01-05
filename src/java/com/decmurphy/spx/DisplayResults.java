package com.decmurphy.spx;

import com.decmurphy.spx.physics.Globals;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
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

    GnuplotFileBuilder gp_landing = null, gp_globe = null, gp_launch = null;

    try {
      gp_landing = new GnuplotFileBuilder(id, "landing");
      Process p1 = Runtime.getRuntime().exec("gnuplot " + gp_landing.getPath());
      p1.waitFor();

      gp_globe = new GnuplotFileBuilder(id, "globe");
      Process p2 = Runtime.getRuntime().exec("gnuplot " + gp_globe.getPath());
      p2.waitFor();

      gp_launch = new GnuplotFileBuilder(id, "launch");
      Process p3 = Runtime.getRuntime().exec("gnuplot " + gp_launch.getPath());
      p3.waitFor();
    }
    catch(InterruptedException e) {
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
            + "     <img class=\"first\" src=\"" + gp_landing.getImgPath() + "\" alt=\"first-stage-trajectory\"/>\n"
            + "     <img class=\"second\" src=\"" + gp_globe.getImgPath() + "\" alt=\"wide-view\"/>\n"
            + "     <img class=\"third\" src=\"" + gp_launch.getImgPath() + "\" alt=\"second-stage-trajectory\"/>\n"
            + "   </div>\n"
            + " </body>\n"
            + "</html>");
    
    request.getSession().invalidate();
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doGet(request, response);
  }

}
