package hr.restart.help;

import hr.restart.baza.Condition;
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
import java.sql.Statement;
import java.sql.Timestamp;

import javax.swing.Timer;


public class MsgDispatcher implements ActionListener {
  
  private static MsgDispatcher inst;
  
  Connection con;
  Statement last;
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
      tim.setInitialDelay(10000);
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
      check = con.prepareStatement("SELECT alarm FROM MesgStatus WHERE cuser = ? AND alarm <= ? AND nova = 'D'");
    } catch (SQLException e) {
      con = null;
      //e.printStackTrace();
    }
  }
    
  public void actionPerformed(ActionEvent a) {
    createStatements();
    if (con == null) return;
    try {
      check.setString(1, raUser.getInstance().getUser());
      check.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      ResultSet rs = check.executeQuery();
      boolean have = rs.next();
      rs.close();
      if (have) checkMsg();
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
  
  public synchronized static boolean sendOut(String from, String to, String msg) {
    if (inst == null) return false;
    return inst.sendOutImpl(from, to, null, msg);
  }
  
  public synchronized static boolean sendOut(String from, String to, Timestamp when, String msg) {
    if (inst == null) return false;
    return inst.sendOutImpl(from, to, when, msg);
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
    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	PreparedStatement insert = dM.getDatabaseConnection().prepareStatement(
    			"INSERT INTO mesg(id, src, dest, datum, mtext) VALUES (?, ?, ?, ?, ?)");
      insert.setString(1, from+serial+":"+Aus.timeToString());
      insert.setString(2, from);
      insert.setString(3, to);
      insert.setTimestamp(4, now);
      insert.setString(5, msg);
      boolean ret = insert.executeUpdate() > 0;
      insert.close();
      Statement update = dM.getDatabaseConnection().createStatement();
      int upd = update.executeUpdate("UPDATE MesgStatus SET nova='D', alarm='" + now + "' WHERE cuser='" + to + "'");
      update.close();
      if (upd < 1) {
      	update = dM.getDatabaseConnection().createStatement();
      	update.executeUpdate("INSERT INTO MesgStatus(cuser, nova, alarm) VALUES ('" + to + "', 'D', '" + now + "')");
      	update.close();
      }
      return ret;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
    
  private boolean sendOutImpl(String from, String to, Timestamp when, String msg) {
    if (++serial >= 1000) serial = 0;
    try {
    	String stat = when == null ? "D" : "X";
    	if (when == null) when = new Timestamp(System.currentTimeMillis());
    	Connection oc = dM.getTempConnection();
    	PreparedStatement insert = oc.prepareStatement(
    			"INSERT INTO mesg(id, nova, src, dest, datum, mtext) VALUES (?, ?, ?, ?, ?, ?)");
      insert.setString(1, from+serial+":"+Aus.timeToString());
      insert.setString(2, stat);
      insert.setString(3, from);
      insert.setString(4, to);
      insert.setTimestamp(5, when);
      insert.setString(6, msg);
      boolean ret = insert.executeUpdate() > 0;
      insert.close();
      Statement update = oc.createStatement();
      ResultSet dat = update.executeQuery("SELECT datum FROM mesg WHERE nova='X' ORDER BY datum");
      if (dat.next() && dat.getTimestamp(1).before(when)) when = dat.getTimestamp(1);
      update.close();      
      update = oc.createStatement();
      int upd = update.executeUpdate("UPDATE MesgStatus SET nova='D', alarm='" + when + "' WHERE cuser='" + to + "'");
      update.close();
      if (upd < 1) {
      	update = oc.createStatement();
      	update.executeUpdate("INSERT INTO MesgStatus(cuser, nova, alarm) VALUES ('" + to + "', 'D', '" + when + "')");
      	update.close();
      }
      return ret;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
  
  Statement create() throws SQLException {
  	if (last != null) last.close();
  	return last = con.createStatement();
  }
  
  void close() throws SQLException {
  	if (last != null) last.close();
  	last = null;
  }
    
  void checkMsg() {
    System.out.println("Updating");
    createStatements();
    if (con == null) return;
    try {
    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	Condition cu = Condition.equal("DEST", raUser.getInstance().getUser());
    	Condition ct = Condition.till("DATUM", now);
    	create().executeUpdate("UPDATE mesg SET nova='D' WHERE "+ cu.and(ct) + " AND nova='X'");
    	ResultSet dat = create().executeQuery("SELECT datum FROM mesg WHERE nova='X' ORDER BY datum");
    	Timestamp next = dat.next() ? dat.getTimestamp(1) : null;
    	String set = next == null ? "nova='N'" : "nova='D', alarm='" + next + "'";

    	create().executeUpdate("UPDATE MesgStatus SET " + set + " WHERE cuser='" +raUser.getInstance().getUser()+"'");
			ResultSet rs = create().executeQuery("SELECT COUNT(*) FROM mesg WHERE " + cu + " AND nova='D'");
			if (rs.next()) {
				inst.unread = (int) rs.getLong(1);
				raUserDialog.getInstance().updateMessageButton(true);
			}
			close();
		} catch (SQLException e) {
			con = null;
			e.printStackTrace();
		}
  }
}
