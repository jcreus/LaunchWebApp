package com.decmurphy.spx;

import com.decmurphy.spx.physics.Globals;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    Launch.main();

    response.setContentType("text/html");

    GnuplotFileBuilder gfb = new GnuplotFileBuilder(String.valueOf(System.currentTimeMillis()));
    try {
      Process p = Runtime.getRuntime().exec("gnuplot " + gfb.getPath());
      p.waitFor();
    } catch (InterruptedException ex) {
    }

    PrintWriter out = response.getWriter();
    String title = Globals.flightCode;
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
            + "     <img src=\"" + gfb.getImgPath() + "\" alt=\"first-stage-trajectory\">\n"
            + "   </div>\n"
            + " </body>\n"
            + "</html>");
    
    try {
      sleep(1000);
    } catch (InterruptedException ex) {
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doGet(request, response);
  }

}
