package hr.restart.help;

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.Valid;

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
  
  int unread = 0;
  
  public static void install(boolean receive) {
    if (inst == null) inst = new MsgDispatcher(receive);
    if (receive && inst != null) inst.checkMsg();
  }
  
  public static int getUnread() {
    return inst.unread;
  }
  
  protected MsgDispatcher(boolean receive) {
    if (!receive) return;
    try {
      String mt = frmParam.getParam("sisfun", "msgTimer", "2000",
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
    return inst.sendImpl(raUser.getInstance().getUser(), to, msg);
  }
  
  public static boolean send(String from, String to, String msg) {
    if (inst == null) return false;
    return inst.sendImpl(from, to, msg);
  }
  
  public static void refresh() {
  	Valid.getValid().execSQL("SELECT COUNT(*) FROM mesg WHERE dest='" +raUser.getInstance().getUser()+"' AND nova='D'");
  	inst.unread = Valid.getValid().getSetCount(Valid.getValid().RezSet, 0);
     
    raUserDialog.getInstance().updateMessageButton(false);
  	
  }
  
  int serial = 0;
  private boolean sendImpl(String from, String to, String msg) {
    if (++serial >= 1000) serial = 0;
    try {
    	PreparedStatement insert = dM.getDatabaseConnection().prepareStatement(
    			"INSERT INTO mesg(id, src, dest, datum, mtext) VALUES (?, ?, ?, ?, ?)");
      insert.setString(1, from+serial+":"+Aus.timeToString());
      insert.setString(2, from);
      insert.setString(3, to);
      insert.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
      insert.setString(5, msg);
      boolean ret = insert.executeUpdate() > 0;
     	Valid.getValid().runSQL("UPDATE useri SET param='NEW' WHERE cuser='" +to+"'");
      return ret;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
    
  void checkMsg() {
    System.out.println("Updating");
   	Valid.getValid().runSQL("UPDATE useri SET param='' WHERE cuser='" +raUser.getInstance().getUser()+"'");

   	Valid.getValid().execSQL("SELECT COUNT(*) FROM mesg WHERE dest='" +raUser.getInstance().getUser()+"' AND nova='D'");
  	inst.unread = Valid.getValid().getSetCount(Valid.getValid().RezSet, 0);
     
    raUserDialog.getInstance().updateMessageButton(true);
  }
}
