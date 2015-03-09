package com.decmurphy.spx;

import com.decmurphy.spx.util.Payload;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        sql.append("')");

        rs = con.prepareStatement(sql.toString()).executeQuery();
        if(/*rs exists*/true) continue;
        
        sql.setLength(0);
        
        sql.append("INSERT INTO launches VALUES (");
        sql.append("1");
        sql.append(",'2006-03-24'");
        sql.append(",'SpaceX'");
        sql.append(",0");
        sql.append(",'Falcon1'");
        sql.append(",0");
        sql.append(",'FalconSAT-2'");
        sql.append(",'FSAT'");
        sql.append(");");
        
      }
    } catch (IOException | ClassNotFoundException | SQLException e) {
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    
  }
  
}
