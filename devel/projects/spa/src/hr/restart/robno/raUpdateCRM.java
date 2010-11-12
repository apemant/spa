/****license*****************************************************************
**   file: raUpdateCRM.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.Partneri;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sk.PartnerCache;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jb.util.TriStateProperty;

public class raUpdateCRM {
  
  static boolean isWarning() {
    return frmParam.getParam("sisfun", "crmWarning", "D", 
    "Upozoriti ako maticni broj ne postoji u CRM-u (D,N)").equalsIgnoreCase("D");
  }
  
  static boolean isWarningCon() {
    return frmParam.getParam("sisfun", "crmWarningCon", "D", 
    "Upozoriti ako konekcija na CRM ne radi (D,N)").equalsIgnoreCase("D");
  }
  
  static void connError() {
    if (isWarningCon())
      JOptionPane.showMessageDialog(null, "Greška u konekciji na CRM!",
          "Greška", JOptionPane.ERROR_MESSAGE);
  }
  
  static String limit(String orig, int length) {
    if (orig == null) return "";
    if (orig.length() <= length) return orig;
    return orig.substring(0, length);
  }
  
  static DataSet getClientsByMB(Connection con, String mb) {
    StorageDataSet ds = null;
    try {
      ds = new StorageDataSet();
      ds.setColumns(new Column[] {
        dM.createStringColumn("UUID", 100),
        dM.createStringColumn("MB", "OIB", 40),
        dM.createStringColumn("NAME", "Ime klijenta", 150),
        dM.createStringColumn("CITY", "Grad", 40),
        dM.createStringColumn("STATUS", "Status", 1)
      });
      ds.open();
      ds.getColumn("UUID").setVisible(TriStateProperty.FALSE);
      ds.getColumn("MB").setWidth(15);
      ds.getColumn("NAME").setWidth(40);
      ds.getColumn("CITY").setWidth(20);
      ds.getColumn("STATUS").setWidth(10);
      
      Statement s = con.createStatement();
      ResultSet rs = s.executeQuery("SELECT uuid,subject_number,name,city,client_status_id " +
            "FROM client WHERE subject_number='"+mb+"'");
      while (rs.next()) {
        ds.insertRow(false);
        ds.setString("UUID", rs.getString("UUID"));
        ds.setString("MB", limit(rs.getString("subject_number"), 40));
        ds.setString("NAME", limit(rs.getString("NAME"), 150));
        ds.setString("CITY", limit(rs.getString("CITY"), 40));
        ds.setString("STATUS", limit(rs.getString("CLIENT_STATUS_ID"), 1));
        ds.post();
      }
      rs.close();
      s.close();
    } catch (Exception e) {
      e.printStackTrace();
      connError();
      return null;
    }
    
    if (ds != null && ds.rowCount() == 0) ds = null;
    
    if (ds == null && isWarning())
      JOptionPane.showMessageDialog(null, "Partner s odgovarajuæim matiènim brojem " +
            "ne postoji u CRM-u!", "Greška", JOptionPane.ERROR_MESSAGE);
      
    return ds;
  }  
  
  static void setStatus(Connection con, String uuid, String status) {
    try {
      Statement s = con.createStatement();
      s.executeUpdate("UPDATE client SET client_status_id='"+status+"' WHERE uuid='"+uuid+"'");
      System.out.println("UPDATED "+uuid+" TO "+status);
      s.close();
    } catch (Exception e) {
      e.printStackTrace();
      connError();
    }
  }
  
  static void closeConnection(Connection con) {
    try {
      con.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static boolean allowedSklad(String cskl) {
    String sklist = frmParam.getParam("sisfun", "crmUpdateSklad", "",
        "Popis skladišta relevantnih za ažuriranje CRM-a");
    if (sklist == null || sklist.trim().length() == 0) return true;
    String[] sl = new VarStr(sklist).splitTrimmed(',');
    for (int i = 0; i < sl.length; i++)
      if (cskl.equals(sl[i])) return true;
    return false;
  }

  public static void addRac(String cskl, int cpar) {
    if (!dM.getDataModule().isCRM()) return;
    if (!allowedSklad(cskl)) return;
    
    if (lookupData.getlookupData().raLocate(dM.getDataModule().getPartneri(), "CPAR", Integer.toString(cpar))) {
      String mb = dM.getDataModule().getPartneri().getString("OIB");
      
      Connection crc = dM.getDataModule().getCRMConnection();
      if (crc == null) {
        connError();
        return;
      }
      
      try {
        DataSet ds = getClientsByMB(crc, mb);
        if (ds == null) return;
        
        boolean allA = true;
        for (ds.first(); ds.inBounds(); ds.next())
          allA = allA && ("AFGHIK".indexOf(ds.getString("STATUS").toUpperCase()) >= 0);

        if (allA) return;
        
        ds.first();
        if (ds.rowCount() > 1) {
          if (JOptionPane.showConfirmDialog(null, new raMultiLineMessage("Postoji više " +
                "klijenata s istim OIB-om! Želite li ruèno izabrati klijenta " +
                "kojem se status treba prebaciti na A-kupac?"), "CRM", 
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
          String[] result = lookupData.getlookupData().lookUp(new JFrame(), ds,
              new String[] {"UUID"}, new String[] {""},
              new int[] {0,1,2,3});
          if (result == null) return;
          if (!lookupData.getlookupData().raLocate(ds, "UUID", result[0])) return;
        }

        String auto = frmParam.getParam("sisfun", "crmRacAuto", "D", 
            "Automatsko prebacivanja flaga kupaca u CRM (D,N)");
        if ((!auto.equalsIgnoreCase("D") || Partneri.getDataModule().getRowCount(Condition.equal("OIB", mb)) > 1) 
                && JOptionPane.showConfirmDialog(null, 
            new raMultiLineMessage("Prebaciti klijenta " + mb + " - " + 
                ds.getString("NAME").trim() + " u CRM-u na status A-kupac?"), "CRM", 
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

        setStatus(crc, ds.getString("UUID"), "A");
      } finally {
        closeConnection(crc);
      }
    }
  }
  
  public static void addPon(String cskl, int cpar) {
    if (!dM.getDataModule().isCRM()) return;
    if (!allowedSklad(cskl)) return;
    
    if (lookupData.getlookupData().raLocate(dM.getDataModule().getPartneri(), "CPAR", Integer.toString(cpar))) {
      String mb = dM.getDataModule().getPartneri().getString("OIB");
      
      Connection crc = dM.getDataModule().getCRMConnection();
      if (crc == null) {
        connError();
        return;
      }
      
      try {
        DataSet ds = getClientsByMB(crc, mb);
        if (ds == null) return;

        String auto = frmParam.getParam("sisfun", "crmPonAuto", "1", "Automatsko prebacivanja flaga na ponudu u CRM (0-rucno,1-ako nije kupac,2-uvijek)");

        boolean allOk = true;
        for (ds.first(); ds.inBounds(); ds.next())
          allOk = allOk && ("AEFGHIK".indexOf(ds.getString("STATUS").toUpperCase()) >= 0);

        if (allOk) return;
        
        int pn = Partneri.getDataModule().getRowCount(Condition.equal("OIB", mb));
        ds.first();
        if (ds.rowCount() > 1) {
          if (JOptionPane.showConfirmDialog(null, new raMultiLineMessage("Postoji više " +
                "klijenata s istim OIB-om! Želite li ruèno izabrati klijenta " +
                "kojem se status treba prebaciti na E-ponuda?"), "CRM", 
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
          String[] result = lookupData.getlookupData().lookUp(new JFrame(), ds,
              new String[] {"UUID"}, new String[] {""},
              new int[] {0,1,2,3});
          if (result == null) return;
          if (!lookupData.getlookupData().raLocate(ds, "UUID", result[0])) return;
        }
        
        if ((auto.equalsIgnoreCase("0") || pn > 1) && JOptionPane.showConfirmDialog(null, 
            new raMultiLineMessage("Prebaciti klijenta " + mb + " - " + 
                ds.getString("NAME").trim() + " u CRM-u na status E-ponuda?" +
                "\n(Trenutaèni status klijenta je "+ds.getString("STATUS")+")"), "CRM", 
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        
        if (auto.equalsIgnoreCase("1") && ds.rowCount() == 1 && pn == 1 &&
            ds.getString("STATUS").equalsIgnoreCase("A")) return;

        setStatus(crc, ds.getString("UUID"), "E");
      } finally {
        closeConnection(crc);
      }
    }
  }
  
  public static StorageDataSet updateSegmentation(DataSet src, PartnerCache pc) {
    
    StorageDataSet ret = null;
    
    Connection crc = dM.getDataModule().getCRMConnection();
    if (crc == null) return null;
    
    try {
      Map mbs = new HashMap();
      Statement s = crc.createStatement();
      ResultSet rs = s.executeQuery("SELECT " +
      		"uuid,subject_number,name,city,segmentation_id FROM client");
      while (rs.next()) {
        Client c = new Client(rs);
        if (c.mb.length() > 0) {
          Client oc = (Client) mbs.get(c.mb);
          if (oc != null) oc.same++;
          else mbs.put(c.mb, c);
        }
      }
      rs.close();
      s.close();
      
      ret = new StorageDataSet();
      ret.setColumns(new Column[] {
          dM.createStringColumn("MB", "OIB", 40),
          dM.createStringColumn("NAME", "Naziv kupca", 150),
          dM.createStringColumn("CITY", "Grad", 40),
          dM.createStringColumn("OPIS", "Akcija", 100),
          dM.createStringColumn("OLDS", "Status", 10),
          dM.createStringColumn("NEWS", "Novi status", 10)
      });
      ret.open();
      ret.getColumn("MB").setWidth(9);
      ret.getColumn("NAME").setWidth(30);
      ret.getColumn("CITY").setWidth(9);
      ret.getColumn("OPIS").setWidth(22);
      ret.getColumn("OLDS").setWidth(6);
      ret.getColumn("NEWS").setWidth(9);
      
      PreparedStatement ps = crc.prepareStatement(
          "UPDATE client SET segmentation_id = ? WHERE uuid = ?");
      
      for (src.first(); src.inBounds(); src.next()) {
        int cpar = src.getInt("CPAR");
        String mb = pc.getData(cpar).getOIB();
        Client c = (Client) mbs.get(mb);
        ret.insertRow(false);
        ret.setString("MB", mb);
        
        String ns = src.getString("SEG");
        int n = 0;
        if (ns.equals("M")) n = 3;
        else if (ns.equals("S")) n = 2;
        else if (ns.equals("V")) n = 1;
        if (n == 0) {
          ret.setString("NAME", pc.getNameNotNull(cpar));
          ret.setString("OPIS", "Preskoèen - status nepromijenjen");
          if (c != null && c.same == 1)
            ret.setString("OLDS", c.seg[c.olds]);
        } else if (c == null) {
          ret.setString("NAME", pc.getNameNotNull(cpar));
          ret.setString("OPIS", "Greška - nepostojeæi OIB");
        } else if (c.same > 1) {
          ret.setString("NAME", pc.getNameNotNull(cpar));
          ret.setString("OPIS", "Greška - " + c.same + " istih OIB-a");
        } else {
          ret.setString("NAME", c.name);
          ret.setString("CITY", c.city);
          ret.setString("OLDS", c.seg[c.olds]);
          ret.setString("NEWS", c.seg[n]);
          if (n == c.olds)
            ret.setString("OPIS", "Status nepromijenjen");
          else {
            ps.setInt(1, n);
            ps.setString(2, c.uuid);
            int u = ps.executeUpdate();
            ret.setString("OPIS", "Status ažuriran (" + u +")");
          }
        }
        ret.post();
      }
      ps.close();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return ret;
  }
  
  static class Client {
    public String uuid;
    public String mb;
    public String name;
    public String city;
    int olds;
    int same;
    public static final String[] seg = {"", "Veliki", "Srednji", "Mali"};
    public Client(ResultSet rs) throws SQLException {
      uuid = rs.getString("UUID").trim();
      mb = limit(rs.getString("subject_number"), 40).trim();
      name = limit(rs.getString("NAME"), 150).trim();
      city = limit(rs.getString("CITY"), 40).trim();
      olds = rs.getInt("segmentation_id");
      same = 1;
    }
  }
}
