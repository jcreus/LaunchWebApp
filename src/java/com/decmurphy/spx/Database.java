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

    StringBuilder sb = new StringBuilder("      <div class=\"tabbable\">\n"
            + "        <ul id=\"tabs\" class=\"nav nav-tabs\">\n");
    StringBuilder sb2 = new StringBuilder("        <div class=\"tab-content\">\n");

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

      pst = con.prepareStatement("SELECT * FROM profiles");
      rs = pst.executeQuery();
      pst = con.prepareStatement(
              "SELECT launches.code FROM launches "
              + "INNER JOIN profiles "
              + "ON launches.launch_id=profiles.launch_id");
      rs2 = pst.executeQuery();

      while (rs.next() && rs2.next()) {

        int id = rs.getInt("launch_id");

        sb.append("          <li").append(id == 1 ? " class=\"active\"" : "").append("><a href=\"#tab").append(id).append("\" data-toggle=\"tab\">").append(rs2.getString("code")).append("</a></li>\n");

        sb2.append("          <div class=\"tab-pane").append(id == 1 ? " in active" : "").append("\" id=\"tab").append(id).append("\">\n");
        sb2.append("            <form action=\"InterfaceServlet\" method=\"POST\">\n");
        sb2.append("              <input type=\"hidden\" name=\"flight_code\" value=\"").append(rs2.getString("code")).append("\"/>\n");
        sb2.append("              <table>\n");
        sb2.append("                <tr><td>Payload Mass</td> <td>    </td><td><input type=\"text\" name=\"payload_mass\" value=\"").append(rs.getInt("Mass")).append("\"/>kg  </td><td><input type=\"radio\" name=\"coast_level\" value=\"coarse\" />Coarse <br /></td></tr>\n");
        sb2.append("                <tr><td>Landing Legs</td> <td>    </td><td><input type=\"text\" name=\"legs\" value=\"").append(rs.getBoolean("Legs") ? "Yes" : "No").append("\"          />    </td><td><input type=\"radio\" name=\"coast_level\" value=\"medium\" />Medium <br /></td></tr>\n");
        sb2.append("                <tr><td>1st Stage Ign</td><td> @ T</td><td><input type=\"text\" name=\"mei_time\" value=\"").append(rs.getDouble("MEI_1")).append("\"    />s   </td><td><input type=\"radio\" name=\"coast_level\" value=\"fine\" checked/>Fine <br /></td></tr>\n");
        sb2.append("                <tr><td>Launch</td>       <td> @ T</td><td><input type=\"text\" name=\"launch_time\" value=\"0.0\"  />s   </td><td rowspan=\"8\"><input class=\"launch_button\" type=\"submit\" value=\"LAUNCH\"/></td></tr>\n");
        sb2.append("                <tr><td>Pitck-Kick</td>   <td> @ T</td><td><input type=\"text\" name=\"pitch_time\" value=\"").append(rs.getDouble("PitchKick")).append("\"   />s   </td></tr>\n");
        sb2.append("                <tr><td></td>             <td>    </td><td><input type=\"text\" name=\"pitch\" value=\"").append(rs.getDouble("Pitch")).append("\"      />rads x\n");
        sb2.append("                                                           <input type=\"text\" name=\"yaw\" value=\"").append(rs.getDouble("Yaw")).append("\"        />rads</td></tr>\n");
        sb2.append("                <tr><td>MECO</td>         <td> @ T</td><td><input type=\"text\" name=\"meco_time\" value=\"").append(rs.getDouble("MECO_1")).append("\"  />s   </td></tr>\n");
        sb2.append("                <tr><td>1st Stage Sep</td><td> @ T</td><td><input type=\"text\" name=\"fss_time\" value=\"").append(rs.getDouble("StageSep")).append("\"   />s   </td></tr>\n");
        sb2.append("                <tr><td>2nd Stage Ign</td><td> @ T</td><td><input type=\"text\" name=\"sei_time\" value=\"").append(rs.getDouble("SEI_1")).append("\"   />s   </td></tr>\n");
        sb2.append("                <tr><td>SECO</td>         <td> @ T</td><td><input type=\"text\" name=\"seco_time\" value=\"\"       />s   </td></tr>\n");
        sb2.append("                <tr><td>2nd Stage Sep</td><td> @ T</td><td><input type=\"text\" name=\"sss_time\" value=\"\"        />s   </td></tr>\n");
        sb2.append("              </table>\n");
        sb2.append("            </form>\n");
        sb2.append("          </div>\n");
      }
    } catch (SQLException | ClassNotFoundException | FileNotFoundException ex) {
    } catch (IOException ex) {
    } finally {
      sb.append("        </ul>\n");
      sb.append("        <a id=\"left-button\" href=\"javascript: void(0);\"><i class=\"glyphicon glyphicon-chevron-left\"></i></a>\n");
      sb.append("        <a id=\"right-button\" href=\"javascript: void(0);\"><i class=\"glyphicon glyphicon-chevron-right\"></i></a>\n");
      sb.append(sb2);
      sb.append("        </div>\n      </div>\n");

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
