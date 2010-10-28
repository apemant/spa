package hr.restart.help;

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.swing.Timer;


public class MsgDispatcher implements ActionListener {
  
  private static MsgDispatcher inst;
  
  Connection con;
  PreparedStatement check;
  PreparedStatement update;
  PreparedStatement insert;
  PreparedStatement count;
  
  int unread = 0;
  
  public static void install() {
    if (inst == null) inst = new MsgDispatcher();
    if (inst != null) inst.checkMsg();
  }
  
  public static int getUnread() {
    return inst.unread;
  }
  
  protected MsgDispatcher() {
    try {
      String mt = frmParam.getParam("sisfun", "msgTimer", "10000",
          "Interval provjera poruka u milisekundama", true);
      int it = Aus.getNumber(mt);
      if (it <= 0) return;

      Timer tim = new Timer(it, this);
      tim.setRepeats(true);
      tim.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  void createStatements() {
    try {
      if (con != null && !con.isClosed()) return;
      con = dM.getTempConnection();
      check = con.prepareStatement("SELECT param FROM useri WHERE cuser = ? and param = 'NEW'");
      update = con.prepareStatement("UPDATE useri SET param = ? WHERE cuser = ?");
      insert = con.prepareStatement("INSERT INTO mesg(id, src, dest, datum, mtext) VALUES (?, ?, ?, ?, ?)");
      count = con.prepareStatement("SELECT count(*) FROM mesg WHERE nova='D' AND dest = ?");
    } catch (SQLException e) {
      con = null;
      e.printStackTrace();
    }
  }
  
  public void actionPerformed(ActionEvent a) {
    createStatements();
    if (con == null) return;
    try {
      check.setString(1, raUser.getInstance().getUser());
      ResultSet rs = check.executeQuery();
      if (rs.next()) checkMsg();
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public static boolean send(String to, String msg) {
    if (inst == null) return false;   
    return inst.send(raUser.getInstance().getUser(), to, msg);
  }
  
  public static boolean sendSys(String to, String msg) {
    if (inst == null) return false;
    return inst.send("SYSTEM", to, msg);
  }
  
  int serial = 0;
  private boolean send(String from, String to, String msg) {
    createStatements();
    if (con == null) return false;
    if (++serial >= 1000) serial = 0;
    try {  
      insert.setString(1, from+serial+":"+Aus.timeToString());
      insert.setString(2, from);
      insert.setString(3, to);
      insert.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
      insert.setString(5, msg);
      boolean ret = insert.executeUpdate() > 0;
      
      update.setString(1, "NEW");
      update.setString(2, raUser.getInstance().getUser());
      update.executeUpdate();
      return ret;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
    
  void checkMsg() {
    createStatements();
    if (con == null) return;
    System.out.println("Updating");
    try {
      update.setString(1, "");
      update.setString(2, raUser.getInstance().getUser());
      System.out.println("Updated " + update.executeUpdate());
      
      unread = 0;
      count.setString(1, raUser.getInstance().getUser());
      ResultSet rs = count.executeQuery();
      if (rs.next()) 
        unread = rs.getInt(1);
      rs.close();
      
      raUserDialog.getInstance().updateMessageButton();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
