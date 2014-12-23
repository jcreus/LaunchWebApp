
LaunchWebApp
============

A Java web app for the C Launch code I built previously. The C implementation has taken the back foot for this more user friendly version.

Java
============

The code consists mainly of objects which make up a rocket. A Builder design pattern might be a good idea for building specific LVs.
Most of the interesting stuff happens in the Stage class, (the physics happens in Stage.leapfrogStep, telemetry is output in Stage.outputFile, etc)

Servlets
============

A user interface is built via Java Servlets. Home.java builds a dynamic html page (defined to be the welcome page in web.xml) where the user can choose a past SpaceX launch profile or build their own. Posting the form sends the input data to InterfaceServlet.java which parses and runs the simulation.

TODO: Scroll bar of launch payloads over input form. Have ~5 visible at a time

MySQL
============

The past launch profiles which get loaded up on the Home servlet are stored in a MySQL database which can be found in the mysql\_db folder. This method is future proof since we only need to add a row to a database for every new launch and the Home servlet automatically updates to contain it.

Output
============

The current big problem. The DisplayResults servlet builds and submits Gnuplot files, creating .png images of the trajectory. These images are created successfully (find all output files in build/web/output, by the way) but displaying them on a webpage is a different matter. Something to do with this whole thing running on a *web*-server, not a *file*-server.

Answers on a postcard please.
