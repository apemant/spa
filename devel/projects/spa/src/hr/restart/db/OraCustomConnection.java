package hr.restart.db;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

import com.borland.dx.sql.dataset.Database;

public class OraCustomConnection extends CustomConnection {

  private String url, user, pass;

  private OraCustomConnection() {
  }
  
  public OraCustomConnection(String _url, String _user, String _pass) {
    url = _url; //"jdbc:oracle:thin:@//192.168.1.162:1521/XE"
    user = _user; //"abssolute"
    pass = _pass; //"abssolute
  }

  public Database connect() throws SQLException {
    System.out.println("Connecting ...");
    OracleDataSource ods = new OracleDataSource();
    ods.setURL(url);
    ods.setUser(user);
    ods.setPassword(pass);
    Connection conn = ods.getConnection();
    Database dbor=new Database();
    dbor.setJdbcConnection(conn);
    System.out.println("Connected! :) ");
    return dbor;
  }

}
