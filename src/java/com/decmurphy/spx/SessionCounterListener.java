package com.decmurphy.spx;

import static com.decmurphy.spx.InterfaceServlet.imagePath;
import java.io.File;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionCounterListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {

		System.out.println("Deleting files in " + imagePath);
		for (File f : new File(imagePath).listFiles()) {
			if (f.getName().startsWith(se.getSession().getId())) {
				System.out.println("-- Deleting " + f.getName());
				try {
					f.delete();
				} catch(NullPointerException e) {
				}
			}
		}

	}
	
}
