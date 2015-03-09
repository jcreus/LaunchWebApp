package com.decmurphy.spx;

import com.decmurphy.spx.config.LaunchVehicleConfig;
import com.decmurphy.spx.exceptions.LaunchVehicleException;
import com.decmurphy.spx.util.LaunchVehicle;
import com.decmurphy.spx.util.Payload;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author dmurphy
 */
public class InitialiseEnvironment implements ServletContextListener {
  
  private Connection con = null;
  private ResultSet rs;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    
    try {
    Properties props;
			try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
				props = new Properties();
				props.load(in);
			}

      String url = props.getProperty("dbUrl");
      String user = props.getProperty("dbUser");
      String password = props.getProperty("dbPass");

      Class.forName("com.mysql.jdbc.Driver");
      con = DriverManager.getConnection(url, user, password);

      for (Payload p : Payload.values()) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT EXISTS(SELECT 1 FROM launches WHERE code='");
        sql.append(String.valueOf(p));
        sql.append("');");

        rs = con.prepareStatement(sql.toString()).executeQuery();
        if(/*rs exists*/true) continue;
        
        sql.setLength(0);
				
				LaunchVehicle lv = LaunchVehicleConfig
						.getLaunchVehicle(String.valueOf(p))
						.getLaunchVehicleType();
        
        sql.append("INSERT INTO launches VALUES (");
        sql.append(p.ordinal());
        sql.append(", ''"); // Date
        sql.append(", 'SpaceX'"); // Provider
        sql.append(", 0"); // Provider ID
        sql.append(", '").append(lv.getLaunchVehicleName()).append("'"); // Launch vehicle
        sql.append(", ").append(lv.ordinal()); // Launch vehicle ID
        sql.append(", '").append(p.getPayloadName()).append("'");  // FalconSat-2
        sql.append(", '").append(String.valueOf(p)).append("'");   // FSAT
        sql.append(");");

        rs = con.prepareStatement(sql.toString()).executeQuery();
				
				sql.setLength(0);
				
				sql.append("INSERT INTO profiles VALUES (");

      }
    } catch (IOException | ClassNotFoundException | SQLException e) {
    } catch (LaunchVehicleException e) {
		}
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    
  }
  
}
