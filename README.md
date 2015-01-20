
LaunchWebApp
============

A Java web app for the C Launch code I built previously. The C implementation has taken the back foot for this more user friendly version.

Java
============

Current code works but cannot implement mission-specific profiles. Current development build (see: profile branch) is attempting to generalise the process. Improvements include:
* Migrating to a Mission-centric problem, where a simulation must have a predetermined Launch Vehicle (LV), a Payload and a Profile.
* LVs will be modularised - built from stages and engines. Adding new LVs is seemless.
* Payloads will be divided into Satellites or Capsules.
* Profiles can be dynamic until stage separation, where hard-coded course corrections take over.

Servlets
============

A user interface is built via Java Servlets. Home.java builds a dynamic html page (defined to be the welcome page in web.xml) where the user can choose a past SpaceX launch profile or build their own. Posting the form sends the input data to InterfaceServlet.java which parses and runs the simulation.

MySQL
============

The past launch profiles which get loaded up on the Home servlet are stored in a MySQL database which can be found in the mysql\_db folder. This method is future proof since we only need to add a row to a database for every new launch and the Home servlet automatically updates to contain it.

Output
============

The DisplayResults servlet builds and submits Gnuplot files, creating .png images of the trajectory. These images are displayed for the user.

TODO: Find elegant solution for auto-removing irrelevant images/output telemetry
