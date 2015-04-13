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
  
  private static String corgHUO;
  private static String corgHRZ;
  private static String cartHUO;
  private static String cartHRZ;
  
  public static void hjk() {
    raPilot.stopDelayWindow();
    
    corgHUO = frmParam.getParam("rac", "huoCorg", "", "OJ za import raèuna HUO");
    corgHRZ = frmParam.getParam("rac", "hrzCorg", "", "OJ za import raèuna HRZ");
    cartHUO = frmParam.getParam("rac", "huoArt", "", "Artikl za import raèuna HUO");
    cartHRZ = frmParam.getParam("rac", "hrzArt", "", "Artikl za import raèuna HUO");
    if (corgHUO.length() == 0 || corgHRZ.length() == 0 || cartHUO.length() == 0 || cartHRZ.length() == 0) {
      JOptionPane.showMessageDialog(null, "Nisu postavljeni parametri importa!", "Greška", JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    if (fc.showOpenDialog(null) != fc.APPROVE_OPTION) return;
    
    raProcess.runChild(new Runnable() {
      public void run() {
        raProcess.setMessage("Uèitavanje xml datoteke...", false);
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
      JOptionPane.showMessageDialog(null, "Došlo je do greške kod uèitavanja xml datoteke!", "Greška", JOptionPane.ERROR_MESSAGE);
    
    load((Document) raProcess.getReturnValue());
  }
  
  private static void load(Document doc) {
    Element root = doc.getRootElement();
    if (!root.getName().equals("Racuni")) {
      JOptionPane.showMessageDialog(null, "Pogrešna xml datoteka (root)!", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    final List rac = root.getChildren("Racun");
    if (rac.size() == 0) {
      JOptionPane.showMessageDialog(null, "Pogrešna xml datoteka (nema raèuna)!", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    raProcess.runChild(new Runnable() {
      public void run() {
        raProcess.setMessage("Punjenje raèuna...", false);
        
        String frac = ((Element) rac.iterator().next()).getChildTextTrim("RacunBroj");
        if (doki.getDataModule().getRowCount(Condition.equal("BRDOKIZ", frac)) > 0)
          raProcess.fail();
        
        QueryDataSet zag = doki.getDataModule().openEmptySet();
        QueryDataSet st = stdoki.getDataModule().openEmptySet();
        QueryDataSet huo = SEQ.getDataModule().openEmptySet();
        QueryDataSet hrz = SEQ.getDataModule().openEmptySet();
        for (Iterator i = rac.iterator(); i.hasNext(); ) 
          load((Element) i.next(), zag, st, huo, hrz);
          
        raProcess.setMessage("Spremanje raèuna...", false);
        if (!raTransaction.saveChangesInTransaction(new QueryDataSet[] {zag, st, huo, hrz}))
          raProcess.fail();
      }
    });
    if (raProcess.isInterrupted()) return;
    if (raProcess.isCompleted()) {
      JOptionPane.showMessageDialog(null, "Uèitano " + rac.size() + " raèuna.", "Import", JOptionPane.INFORMATION_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(null, "Došlo je do greške kod prijenosa!", "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  private static void load(Element rac, QueryDataSet zag, QueryDataSet st, QueryDataSet huo, QueryDataSet hrz) {
    raProcess.checkClosing();
    Timestamp dat = Timestamp.valueOf(rac.getChildTextTrim("DatumRacuna").replace('T', ' '));
    String god = Valid.getValid().findYear(dat);
    String cart = "";
    
    zag.insertRow(false);
    zag.setTimestamp("SYSDAT", dat);
    zag.setTimestamp("DATDOK", dat);
    zag.setTimestamp("DVO", dat);
    zag.setShort("DDOSP", (short) 14);
    zag.setTimestamp("DATDOSP", hr.restart.util.Util.getUtil().addDays(dat, 14));
    zag.setString("GOD", god);
    zag.setString("VRDOK", "RAC");
    
    String typ = rac.getChildTextTrim("VrstaRacuna");  
    if (typ.equals("Raèun HUO")) {
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
    } else if (typ.equals("Raèun HRZ")) {
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
      st.setString("NAZART", est.getChildTextTrim("OpisStavke"));
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
