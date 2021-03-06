package com.decmurphy.spx.servlet;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {

		String ID = request.getParameter("id");
		String body = (String) request.getSession().getAttribute(ID);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
    
		if (body != null && !body.isEmpty()) {
			out.println(body);
		} else {
			out.println("Empty body");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		doGet(request, response);
	}
}
