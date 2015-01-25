
LaunchWebApp
============

A Java web app for the C Launch code I built previously. The C implementation has taken the back foot for this more user friendly version.

Java
============

Current code works but cannot implement mission-specific profiles. Current development build (see: profile branch) is attempting to generalise the process. Improvements include:
* Migrating to a Mission-centric problem, where a simulation must have a predetermined Launch Vehicle (LV), Payload, Profile and Launch Site.
* LVs will be modularised - built from stages and engines. Adding new LVs is seemless.
* Payloads will be divided into Satellites or Capsules (but all that really matters is the mass).
* Profiles can be dynamic until stage separation, where hard-coded course corrections take over.

TODO: unlimited dynamic course corrections on user interface (see: [here](http://www.sanwebe.com/2013/03/addremove-input-fields-dynamically-with-jquery)). This will also remove the need for a separate Profile class for each mission (woo!) but may be hard to store data in DB (aww).

Servlets
============

A user interface is built via Java Servlets. Home.java builds a dynamic html page (defined to be the welcome page in web.xml) where the user can choose a past SpaceX launch profile or build their own (soon). Posting the form sends the input data to the InterfaceServlet which parses and verifies the data and runs the simulation.

MySQL
============

The past launch profiles which get loaded up on the Home servlet are stored in a MySQL database which can be found in the mysql\_db folder. This method is future proof since we only need to add a row to a database for every new launch and the Home servlet automatically updates to contain it.

Output
============

The ExecuteLaunch servlet does the maths, builds and submits Gnuplot files, creating .png images of the trajectory. These images are displayed for the user in DisplayResults.

TODO: Find elegant solution for auto-removing irrelevant images/output telemetry. Repeating Shell script?
