package hr.restart.util;

import hr.restart.baza.Condition;
import hr.restart.baza.SEQ;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.robno.Aut;
import hr.restart.robno.Util;
import hr.restart.robno.raControlDocs;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raPilot;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.borland.dx.sql.dataset.QueryDataSet;


public class Imports {

  static JFileChooser fc = new JFileChooser(new File("."));
  static {
    FileFilter ff = new raFileFilter("EDI XML datoteke (*.xml)");
    fc.addChoosableFileFilter(ff);
    fc.setFileFilter(ff);
  }
  
  private Imports() {
    // static class
  }
  
  private static hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  
  private static String corgHUO;
  private static String corgHRZ;
  private static String corgANT;
  private static String cartHUO;
  private static String cartHRZ;
  private static String cartANT;
  
  public static void hjk() {
    raPilot.stopDelayWindow();
    
    corgHUO = frmParam.getParam("rac", "huoCorg", "", "OJ za import ra�una HUO");
    corgHRZ = frmParam.getParam("rac", "hrzCorg", "", "OJ za import ra�una HRZ");
    corgANT = frmParam.getParam("rac", "antCorg", "", "OJ za import ra�una anticipacije");
    cartHUO = frmParam.getParam("rac", "huoArt", "", "Artikl za import ra�una HUO");
    cartHRZ = frmParam.getParam("rac", "hrzArt", "", "Artikl za import ra�una HUO");
    cartANT = frmParam.getParam("rac", "antArt", "", "Artikl za import ra�una anticipacije");
    if (corgHUO.length() == 0 || corgHRZ.length() == 0 || corgANT.length() == 0 ||  
        cartHUO.length() == 0 || cartHRZ.length() == 0 || cartANT.length() == 0) {
      JOptionPane.showMessageDialog(null, "Nisu postavljeni parametri importa!", "Gre�ka", JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    if (fc.showOpenDialog(null) != fc.APPROVE_OPTION) return;
    
    raProcess.runChild(new Runnable() {
      public void run() {
        raProcess.setMessage("U�itavanje xml datoteke...", false);
        SAXBuilder builder = new SAXBuilder();
        try {
          Document doc = builder.build(fc.getSelectedFile());
          raProcess.yield(doc);
        } catch (JDOMException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        raProcess.fail();
      }
    });
    if (raProcess.isInterrupted()) return;
    if (raProcess.isFailed())
      JOptionPane.showMessageDialog(null, "Do�lo je do gre�ke kod u�itavanja xml datoteke!", "Gre�ka", JOptionPane.ERROR_MESSAGE);
    
    load((Document) raProcess.getReturnValue());
  }
  
  private static void load(Document doc) {
    Element root = doc.getRootElement();
    if (!root.getName().equals("Racuni")) {
      JOptionPane.showMessageDialog(null, "Pogre�na xml datoteka (root)!", "Gre�ka", JOptionPane.ERROR_MESSAGE);
      return;
    }
    final List rac = root.getChildren("Racun");
    if (rac.size() == 0) {
      JOptionPane.showMessageDialog(null, "Pogre�na xml datoteka (nema ra�una)!", "Gre�ka", JOptionPane.ERROR_MESSAGE);
      return;
    }
    raProcess.runChild(new Runnable() {
      public void run() {
        raProcess.setMessage("Punjenje ra�una...", false);
        
        String frac = ((Element) rac.iterator().next()).getChildTextTrim("RacunBroj");
        if (doki.getDataModule().getRowCount(Condition.equal("BRDOKIZ", frac)) > 0)
          raProcess.fail();
        
        QueryDataSet zag = doki.getDataModule().openEmptySet();
        QueryDataSet st = stdoki.getDataModule().openEmptySet();
        QueryDataSet huo = SEQ.getDataModule().openEmptySet();
        QueryDataSet hrz = SEQ.getDataModule().openEmptySet();
        QueryDataSet ant = SEQ.getDataModule().openEmptySet();
        for (Iterator i = rac.iterator(); i.hasNext(); ) 
          load((Element) i.next(), zag, st, huo, hrz, ant);
          
        raProcess.setMessage("Spremanje ra�una...", false);
        if (!raTransaction.saveChangesInTransaction(new QueryDataSet[] {zag, st, huo, hrz, ant}))
          raProcess.fail();
      }
    });
    if (raProcess.isInterrupted()) return;
    if (raProcess.isCompleted()) {
      JOptionPane.showMessageDialog(null, "U�itano " + rac.size() + " ra�una.", "Import", JOptionPane.INFORMATION_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(null, "Do�lo je do gre�ke kod prijenosa!", "Gre�ka", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  static void load(Element rac, QueryDataSet zag, QueryDataSet st, QueryDataSet huo, QueryDataSet hrz, QueryDataSet ant) {
    raProcess.checkClosing();
    Timestamp dat = Timestamp.valueOf(rac.getChildTextTrim("DatumRacuna").replace('T', ' '));
    String god = Valid.getValid().findYear(dat);
    String cart = "";
    
    Timestamp now = new Timestamp(System.currentTimeMillis());
    Timestamp morn = ut.getFirstSecondOfDay(now);
    long diff = now.getTime() - morn.getTime();
    
    Timestamp sys = new Timestamp(ut.getFirstSecondOfDay(dat).getTime() + diff);
    
    zag.insertRow(false);
    zag.setTimestamp("SYSDAT", sys);
    zag.setTimestamp("DATDOK", dat);
    zag.setTimestamp("DVO", dat);
    zag.setShort("DDOSP", (short) 14);
    zag.setTimestamp("DATDOSP", hr.restart.util.Util.getUtil().addDays(dat, 14));
    zag.setString("GOD", god);
    zag.setString("VRDOK", "RAC");
    
    String typ = rac.getChildTextTrim("VrstaRacuna");  
    if (typ.equals("Ra�un HUO")) {
      if (huo.rowCount() == 0) {
        String opis = corgHUO + "RAC" + god;
        SEQ.getDataModule().setFilter(huo, Condition.equal("OPIS", opis));
        huo.open();
        if (huo.rowCount() == 0) {
          huo.insertRow(false);
          huo.setString("OPIS", opis);
          huo.setInt("BROJ", 0);
        }
      }
      cart = cartHUO;
      zag.setString("CSKL", corgHUO);
      zag.setString("CORG", corgHUO);
      huo.setInt("BROJ", huo.getInt("BROJ") + 1);
      zag.setInt("BRDOK", huo.getInt("BROJ"));
    } else if (typ.equals("Ra�un HRZ")) {
      if (hrz.rowCount() == 0) {
        String opis = corgHRZ + "RAC" + god;
        SEQ.getDataModule().setFilter(hrz, Condition.equal("OPIS", opis));
        hrz.open();
        if (hrz.rowCount() == 0) {
          hrz.insertRow(false);
          hrz.setString("OPIS", opis);
          hrz.setInt("BROJ", 0);
        }
      }
      cart = cartHRZ;
      zag.setString("CSKL", corgHRZ);
      zag.setString("CORG", corgHRZ);
      hrz.setInt("BROJ", hrz.getInt("BROJ") + 1);
      zag.setInt("BRDOK", hrz.getInt("BROJ"));
    } else if (typ.equals("Anticip")) {
      if (ant.rowCount() == 0) {
        String opis = corgANT + "RAC" + god;
        SEQ.getDataModule().setFilter(ant, Condition.equal("OPIS", opis));
        ant.open();
        if (ant.rowCount() == 0) {
          ant.insertRow(false);
          ant.setString("OPIS", opis);
          ant.setInt("BROJ", 0);
        }
      }
      cart = cartANT;
      zag.setString("CSKL", corgANT);
      zag.setString("CORG", corgANT);
      ant.setInt("BROJ", ant.getInt("BROJ") + 1);
      zag.setInt("BRDOK", ant.getInt("BROJ"));
    } else {
      System.out.println("Invalid type: " + typ);
      return;
    }
    
    if (lookupData.getlookupData().raLocate(dM.getDataModule().getPartneri(), "OIB", rac.getChildTextTrim("OIB"))) 
      zag.setInt("CPAR", dM.getDataModule().getPartneri().getInt("CPAR"));
    
    zag.setString("PNBZ2", rac.getChildTextTrim("RacunBroj"));
    zag.setString("BRDOKIZ", rac.getChildTextTrim("RacunBroj"));
    zag.setString("OPIS", "Poziv na broj " + rac.getChildTextTrim("PozivNaBroj"));
    
    int rbr = 0;
    lookupData.getlookupData().raLocate(dM.getDataModule().getArtikli(), "CART", cart);
    List stav = rac.getChildren("Stavka");
    for (Iterator i = stav.iterator(); i.hasNext(); ) {
      Element est = (Element) i.next();
      st.insertRow(false);
      dM.copyColumns(zag, st, Util.mkey);
      st.setInt("RBSID", ++rbr);
      st.setShort("RBR", (short) rbr);
      Aut.getAut().copyArtFields(st, dM.getDataModule().getArtikli());
      String nazart = est.getChildTextTrim("OpisStavke");
      if (nazart.length() > st.getColumn("NAZART").getPrecision())
        nazart = nazart.substring(0, st.getColumn("NAZART").getPrecision());
      st.setString("NAZART", nazart);
      st.setBigDecimal("KOL", Aus.one0);
      BigDecimal val = Aus.getDecNumber(est.getChildTextTrim("IznosStavke"));
      st.setBigDecimal("FC", val);
      st.setBigDecimal("FVC", val);
      st.setBigDecimal("FMC", val);
      st.setBigDecimal("INETO", val);
      st.setBigDecimal("IPRODBP", val);
      st.setBigDecimal("IPRODSP", val);
      st.setString("ID_STAVKA", raControlDocs.getKey(st, Util.dkey, "stdoki"));
    }
  }
}
