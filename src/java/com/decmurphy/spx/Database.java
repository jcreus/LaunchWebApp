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
  private ResultSet rs, rs2, rs3;
  private PreparedStatement pst = null;

  public Database() {
  }

  public String buildProfilesList(String pathToPropertiesFile) {

    StringBuilder list = new StringBuilder("        <ul id=\"tabs\" class=\"nav nav-tabs\">\n");
    StringBuilder tabs = new StringBuilder("        <div class=\"tab-content\">\n");

    try {

			Properties props;
			try (FileInputStream in = new FileInputStream(pathToPropertiesFile)) {
				props = new Properties();
				props.load(in);
			}

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
      
      Statement st = con.createStatement();
      rs3 = st.executeQuery("SELECT COUNT(*) FROM profiles");
      rs3.next();
      int rowCount = rs3.getInt(1);
      System.out.println("RowCount = " + rowCount);

      while (rs.next() && rs2.next()) {
        
        Double d;

        int id = rs.getInt("launch_id");

        list.append("          <li").append(id == rowCount ? " class=\"active\"" : "").append("><a href=\"#tab").append(id).append("\" data-toggle=\"tab\">").append(rs2.getString("code")).append("</a></li>\n");

        tabs.append("          <div class=\"tab-pane").append(id == rowCount ? " in active" : "").append("\" id=\"tab").append(id).append("\">\n");
        tabs.append("            <form action=\"InterfaceServlet\" method=\"POST\">\n");
        tabs.append("              <input type=\"hidden\" name=\"flight_code\" value=\"").append(rs2.getString("code")).append("\"/>\n");
        tabs.append("              <table class=\"table_left\">\n");
        tabs.append("                <tr><td>1st Stage Ign </td><td> @ T</td><td><input type=\"text\" size=\"15\" name=\"mei1_time\"    value=\"").append((d=rs.getDouble("MEI_1"))==0.0?"":d).append("\"/>s</td></tr>\n");
        tabs.append("                <tr><td>Launch        </td><td> @ T</td><td><input type=\"text\" size=\"15\" name=\"launch_time\"  value=\"0.0\"/>s</td></tr>\n");
        tabs.append("                <tr><td>Pitck-Kick    </td><td> @ T</td><td><input type=\"text\" size=\"15\" name=\"pitch_time\"   value=\"").append((d=rs.getDouble("PitchKick"))==0.0?"":d).append("\"/>s</td></tr>\n");
        tabs.append("                <tr><td>-->Pitch      </td><td>    </td><td><input type=\"text\" size=\"15\" name=\"pitch\"        value=\"").append(rs.getDouble("Pitch")).append("\"/>rads</td></tr>\n");
        tabs.append("                <tr><td>-->Yaw        </td><td>    </td><td><input type=\"text\" size=\"15\" name=\"yaw\"          value=\"").append(rs.getDouble("Yaw")).append("\"/>rads</td></tr>\n");
        tabs.append("                <tr><td>MECO-1        </td><td> @ T</td><td><input type=\"text\" size=\"15\" name=\"meco1_time\"   value=\"").append((d=rs.getDouble("MECO_1"))==0.0?"":d).append("\"/>s</td></tr>\n");
        tabs.append("                <tr><td>1st Stage Sep </td><td> @ T</td><td><input type=\"text\" size=\"15\" name=\"fss_time\"     value=\"").append((d=rs.getDouble("StageSep"))==0.0?"":d).append("\"/>s</td></tr>\n");
        tabs.append("                <tr><td>2nd Stage Ign </td><td> @ T</td><td><input type=\"text\" size=\"15\" name=\"sei1_time\"    value=\"").append((d=rs.getDouble("SEI_1"))==0.0?"":d).append("\"/>s</td></tr>\n");
        tabs.append("                <tr><td>BoostBack On  </td><td> @ T</td><td><input type=\"text\" size=\"15\" name=\"mei2_time\"    value=\"").append((d=rs.getDouble("MEI_2"))==0.0?"":d).append("\"/>s</td></tr>\n");
        tabs.append("                <tr><td>BoostBack Off </td><td> @ T</td><td><input type=\"text\" size=\"15\" name=\"meco2_time\"   value=\"").append((d=rs.getDouble("MECO_2"))==0.0?"":d).append("\"/>s</td></tr>\n");
        tabs.append("                <tr><td>Re-Entry On   </td><td> @ T</td><td><input type=\"text\" size=\"15\" name=\"mei3_time\"    value=\"").append((d=rs.getDouble("MEI_3"))==0.0?"":d).append("\"/>s</td></tr>\n");
        tabs.append("                <tr><td>Re-Entry Off  </td><td> @ T</td><td><input type=\"text\" size=\"15\" name=\"meco3_time\"   value=\"").append((d=rs.getDouble("MECO_3"))==0.0?"":d).append("\"/>s</td></tr>\n");
        tabs.append("                <tr><td>Landing On    </td><td> @ T</td><td><input type=\"text\" size=\"15\" name=\"mei4_time\"    value=\"").append((d=rs.getDouble("MEI_4"))==0.0?"":d).append("\"/>s</td></tr>\n");
        tabs.append("              </table>\n");
				tabs.append("              <div class=\"table-right\">\n");
				tabs.append("                <table class=\"table_top\">\n");
				tabs.append("                  <tr><td>Payload Mass    </td><td></td><td><input type=\"text\" size=\"15\" name=\"payload_mass\" value=\"").append(rs.getInt("Mass")).append("\"/>kg</td><td rowspan=\"3\"><input class=\"launch_button\" type=\"submit\" value=\"LAUNCH\"/></td></tr>\n");
        tabs.append("                  <tr><td>Landing Legs    </td><td></td><td><input type=\"text\" size=\"15\" name=\"legs\"         value=\"").append(rs.getBoolean("Legs")? "Yes" : "No").append("\"/></td></tr>\n");
				tabs.append("                  <tr><td>Coriolis Effect </td><td></td><td><input type=\"text\" size=\"15\" name=\"coriolis\"     value=\"On\"/></td></tr>\n");
        tabs.append("                </table>\n");
//				tabs.append("				  			 <h3>Second Stage Course Corrections</h3><a class=\"add_field_button\" href=\"#\"><i class=\"glyphicon glyphicon-plus\"></i></a>");
//				tabs.append("			  				 <table class=\"table_bottom\">\n");
//				tabs.append("						  	 </table>\n");
				tabs.append("              </div>\n");
        tabs.append("            </form>\n");
        tabs.append("          </div>\n");
      }
    } catch (SQLException | ClassNotFoundException | FileNotFoundException ex) {
    } catch (IOException ex) {
    } finally {
      tabs.append("        </div>\n");

      list.append("        </ul>\n");
      list.append("        <a id=\"left-button\" href=\"javascript: void(0);\"><i class=\"glyphicon glyphicon-chevron-left\"></i></a>\n");
      list.append("        <a id=\"right-button\" href=\"javascript: void(0);\"><i class=\"glyphicon glyphicon-chevron-right\"></i></a>\n");
      list.append(tabs);

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

    return list.toString();
  }
}
