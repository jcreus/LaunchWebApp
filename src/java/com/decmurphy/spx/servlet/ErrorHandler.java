package com.decmurphy.spx.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ErrorHandler extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");

    if (servletName == null) {
      servletName = "Unknown";
    }

    String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");

    if (requestUri == null) {
      requestUri = "Unknown";
    }

    // Set response content type
    response.setContentType("text/html");

    PrintWriter out = response.getWriter();
    String title = "Error/Exception Information";
    String docType = "<!doctype html>\n";
    out.println(docType
            + "<html>\n"
            + " <head>\n"
            + "   <title>" + title + "</title>\n"
            + " </head>\n"
            + " <body bgcolor=\"#f0f0f0\">\n");

    if (throwable == null && statusCode == null) {
      out.println("   <h2>Error information is missing</h2>");
      out.println("   Please return to the <a href=\"" + response.encodeURL("http://localhost:8080/") + "\">Home Page</a>.");
    } else if (statusCode != null) {
      out.println("   The status code : " + statusCode);
    } else {
      out.println("   <h2>Error information</h2>");
      out.println("   Servlet Name : " + servletName + "</br></br>");
      out.println("   Exception Type : " + throwable.getClass().getName() + "</br></br>");
      out.println("   The request URI: " + requestUri + "<br><br>");
      out.println("   The exception message: " + throwable.getMessage());
    }
    out.println(" </body>");
    out.println("</html>");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

}
