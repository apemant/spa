package hr.restart.sisfun;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.Intervencije;
import hr.restart.util.Aus;
import hr.restart.util.Util;


public class raVersionCheck {

  public static void entry() {    
    Connection con = Aus.getMainConnection();
    if (con == null) return;
    
    try {
      checkAndPrompt(con);
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("OVER CHECK");
  }
  
  private static void checkAndPrompt(Connection con) throws Exception {
    //if (1==1) return;
    ResultSet par = con.createStatement().executeQuery("SELECT * FROM parametri WHERE app='sisfun' AND param='expcheck'");
    if (!par.next()) return;
    
    Timestamp now = Aus.getNow();
    int days = 60;
    Timestamp then = Util.getUtil().addDays(now, days);
    ResultSet dat = con.createStatement().executeQuery("SELECT * FROM intervencije WHERE uid=0");
    if (!dat.next()) {
      PreparedStatement ps = con.prepareStatement("INSERT INTO intervencije(uid, datum, datz, trajanje) VALUES (0, ?, ?, ?)");
      ps.setTimestamp(1, now);
      ps.setTimestamp(2, then);
      ps.setInt(3, 60);
      ps.execute();
    } else {
      then = dat.getTimestamp("DATZ");
      days = dat.getInt("TRAJANJE");
    }
    
    if (now.before(then)) return;
    
    int curr = (int) ((System.currentTimeMillis() % 100000) * 157 % 9000) + 1000;
    
    String s = JOptionPane.showInputDialog(null, "Verzija programa je istekla. \nZa nastavak korištenja unesite kljuè.\n"+
        "Kljuè možete zatražiti na tel. +38513705176 ili na info@rest-art.hr \n" +
        "("+curr+")", "Potrebna aktivacija programa", JOptionPane.INFORMATION_MESSAGE);
    
    if (s == null || !s.equals(Aus.simpleCode(curr)+"")) System.exit(0);
    
    PreparedStatement ps = con.prepareStatement("UPDATE intervencije SET datum=?, datz=? WHERE uid=0");
    ps.setTimestamp(1, now);
    ps.setTimestamp(2, Util.getUtil().addDays(now, days));
    ps.execute();
    JOptionPane.showMessageDialog(null, "Program je aktiviran, hvala na razumijevanju.");
  }
  
  public static void install() {
    int curr = (int) ((System.currentTimeMillis() % 100000) * 157 % 9000) + 1000;
    
    String s = JOptionPane.showInputDialog(null, "Unesite passcode (" + curr + ")", "Provjera verzije", JOptionPane.INFORMATION_MESSAGE);
    if (s == null || !s.equals(Aus.simpleCode(curr)+"")) return;
    
    DataSet ds = Intervencije.getDataModule().openTempSet(Condition.equal("UID", 0));
    if (ds.rowCount() == 0) {
      ds.insertRow(false);
      ds.setInt("UID", 0);
    }
    ds.setTimestamp("DATUM", Aus.getNow());
    String dn = JOptionPane.showInputDialog(null, "Broj dana do isteka (do sada " + ds.getInt("TRAJANJE") + ")?", 
        "Istek verzije", JOptionPane.INFORMATION_MESSAGE);
    if (dn == null) return;
    
    if (dn.equals("0")) {
      ds.deleteAllRows();
      ds.saveChanges();
    } else {
      if (dn.length() > 0 && Aus.getNumber(dn) > 0)
        ds.setInt("TRAJANJE", Aus.getNumber(dn));
      
      ds.setTimestamp("DATZ", Util.getUtil().addDays(ds.getTimestamp("DATUM"), ds.getInt("TRAJANJE")));
      ds.saveChanges();
    }
    JOptionPane.showMessageDialog(null, "Verzija produžena do " + Aus.formatTimestamp(ds.getTimestamp("DATZ")));
  }
}
