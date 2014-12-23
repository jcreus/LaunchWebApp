/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decmurphy.spx;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 *
 * @author declan
 */
public class Database {

  private Connection con = null;
  private ResultSet rs, rs2 = null;
  private PreparedStatement pst = null;

  public Database() {
  }

  public String buildProfilesList(String pathToPropertiesFile) {

    StringBuilder sb = new StringBuilder("<ul class=\"tabs\">\n");

    try {
            
      Properties props = new Properties();
      FileInputStream in = new FileInputStream(pathToPropertiesFile);
      props.load(in);
      in.close();

      String url = props.getProperty("dbUrl");
      String user = props.getProperty("dbUser");
      String password = props.getProperty("dbPass");
            
      Class.forName("com.mysql.jdbc.Driver");
      con = DriverManager.getConnection(url, user, password);
      
      pst = con.prepareStatement("select * from profiles");
      rs = pst.executeQuery();
      pst = con.prepareStatement(
              "select launches.code from launches "
              + "inner join profiles "
              + "on launches.launch_id=profiles.launch_id");
      rs2 = pst.executeQuery();

      while (rs.next() && rs2.next()) {

        int id = rs.getInt("launch_id");

        sb.append("<li>\n");
        sb.append("  <input type=\"radio\" name=\"tabs\" id=\"tab").append(id).append("\" checked />\n");
        sb.append("  <label for=\"tab").append(id).append("\">").append(rs2.getString("code")).append("</label>\n");
        sb.append("  <div id=\"tab-content").append(id).append("\" class=\"tab-content\">\n");
        sb.append("    <form action=\"InterfaceServlet\" method=\"POST\">\n");
        sb.append("      <input type=\"hidden\" name=\"flight_code\" value=\"").append(rs2.getString("code")).append("\"/>\n");
        sb.append("      <table>\n");
        sb.append("        <tr><td>Payload Mass</td> <td>    </td><td><input type=\"text\" name=\"payload_mass\" value=\"").append(rs.getInt("Mass")).append("\"/>kg  </td><td><input type=\"radio\" name=\"coast_level\" value=\"coarse\" />Coarse <br /></td></tr>\n");
        sb.append("        <tr><td>Landing Legs</td> <td>    </td><td><input type=\"text\" name=\"legs\" value=\"").append(rs.getBoolean("Legs")).append("\"          />    </td><td><input type=\"radio\" name=\"coast_level\" value=\"medium\" />Medium <br /></td></tr>\n");
        sb.append("        <tr><td>1st Stage Ign</td><td> @ T</td><td><input type=\"text\" name=\"mei_time\" value=\"").append(rs.getDouble("MEI_1")).append("\"    />s   </td><td><input type=\"radio\" name=\"coast_level\" value=\"fine\" checked/>Fine <br /></td></tr>\n");
        sb.append("        <tr><td>Launch</td>       <td> @ T</td><td><input type=\"text\" name=\"launch_time\" value=\"0.0\"  />s   </td><td rowspan=\"8\"><input class=\"launch_button\" type=\"submit\" value=\"LAUNCH\"/></td></tr>\n");
        sb.append("        <tr><td>Pitck-Kick</td>   <td> @ T</td><td><input type=\"text\" name=\"pitch_time\" value=\"").append(rs.getDouble("PitchKick")).append("\"   />s   </td></tr>\n");
        sb.append("        <tr><td></td>             <td>    </td><td><input type=\"text\" name=\"pitch\" value=\"").append(rs.getDouble("Pitch")).append("\"      />rads x\n");
        sb.append("                                                   <input type=\"text\" name=\"yaw\" value=\"").append(rs.getDouble("Yaw")).append("\"        />rads</td></tr>\n");
        sb.append("        <tr><td>MECO</td>         <td> @ T</td><td><input type=\"text\" name=\"meco_time\" value=\"").append(rs.getDouble("MECO_1")).append("\"  />s   </td></tr>\n");
        sb.append("        <tr><td>1st Stage Sep</td><td> @ T</td><td><input type=\"text\" name=\"fss_time\" value=\"").append(rs.getDouble("StageSep")).append("\"   />s   </td></tr>\n");
        sb.append("        <tr><td>2nd Stage Ign</td><td> @ T</td><td><input type=\"text\" name=\"sei_time\" value=\"").append(rs.getDouble("SEI_1")).append("\"   />s   </td></tr>\n");
        sb.append("        <tr><td>SECO</td>         <td> @ T</td><td><input type=\"text\" name=\"seco_time\" value=\"\"       />s   </td></tr>\n");
        sb.append("        <tr><td>2nd Stage Sep</td><td> @ T</td><td><input type=\"text\" name=\"sss_time\" value=\"\"        />s   </td></tr>\n");
        sb.append("      </table>\n");
        sb.append("    </form>\n");
        sb.append("  </div>\n");
        sb.append("</li>\n");
      }
    } catch (SQLException | ClassNotFoundException | FileNotFoundException ex) {
    } catch (IOException ex) {
    } finally {
      sb.append("</ul>\n");
      try {
        if (con != null) {
          con.close();
        }
        if (pst != null) {
          pst.close();
        }
        if (rs != null) {
          rs.close();
        }
        if (rs2 != null) {
          rs2.close();
        }
      } catch (SQLException ex) {
      }
    }

    return sb.toString();
  }
}
