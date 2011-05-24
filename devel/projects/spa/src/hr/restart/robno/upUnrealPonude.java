/****license*****************************************************************
**   file: upUnrealPonude.java
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

import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Condition;
import hr.restart.baza.Stanje;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCpar;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.*;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;


public class upUnrealPonude extends raUpitFat {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  Util rut = Util.getUtil();
  Valid vl = Valid.getValid();
  
  JLabel jlDatum = new JLabel();
  JraTextField jraDatum = new JraTextField();
  JLabel jlTol = new JLabel();
  JraTextField jraTol = new JraTextField();
  
  JLabel jlCskl = new JLabel();
  JlrNavField jlrCskl = new JlrNavField();
  JlrNavField jlrNazskl = new JlrNavField();
  JraButton jbSelCskl = new JraButton();
  JLabel jlCorg = new JLabel();
  JlrNavField jlrCorg = new JlrNavField();
  JlrNavField jlrNaziv = new JlrNavField();
  JraButton jbSelCorg = new JraButton();
  jpCpar par = new jpCpar(350);
  
  XYLayout lay = new XYLayout(655, 135);
  JPanel pan = new JPanel(lay);
  
  StorageDataSet pons = new StorageDataSet();
  StorageDataSet tds = new StorageDataSet();
  
  private raNavAction rnvStop = new raNavAction("Poništi oznaèene ponude", 
      raImages.IMGSTOP, java.awt.event.KeyEvent.VK_F8) {
    public void actionPerformed(java.awt.event.ActionEvent e){
      ponisti();
    }
  };
  
  
  public upUnrealPonude() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    
    tds.setColumns(new Column[] {
        dM.createStringColumn("CSKL", "Skladište", 12),
        dM.createStringColumn("CORG", "Org. jedinica", 12),
        dM.createIntColumn("CPAR", "Partner"),
        dM.createTimestampColumn("DATUM", "Na dan"),
        dM.createIntColumn("TOL", "Tolerancija")
    });
    tds.open();
    
    pons.setColumns(new Column[] {
        doki.getDataModule().getColumn("CSKL").cloneColumn(),
        doki.getDataModule().getColumn("GOD").cloneColumn(),
        doki.getDataModule().getColumn("VRDOK").cloneColumn(),
        doki.getDataModule().getColumn("BRDOK").cloneColumn(),
        doki.getDataModule().getColumn("CPAR").cloneColumn(),
        doki.getDataModule().getColumn("DATDOK").cloneColumn(),
        doki.getDataModule().getColumn("DATDOSP").cloneColumn(),
        doki.getDataModule().getColumn("UIRAC").cloneColumn(),
        doki.getDataModule().getColumn("PARAM").cloneColumn(),
        dM.createIntColumn("NUMS", "Stavaka")
    });
    pons.open();
    pons.getColumn("PARAM").setVisible(0);
    pons.getColumn("DATDOSP").setCaption("Vrijedi do");
    pons.getColumn("CSKL").setWidth(9);
    
    jlDatum.setText("Na dan");
    jraDatum.setColumnName("DATUM");
    jraDatum.setDataSet(tds);
    jlTol.setText("Broj dodatnih dana važenja");
    jraTol.setColumnName("TOL");
    jraTol.setDataSet(tds);
    
    jlCskl.setText("Skladište");
    jlrCskl.setColumnName("CSKL");
    jlrCskl.setDataSet(tds);
    jlrCskl.setColNames(new String[] {"NAZSKL"});
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazskl});
    jlrCskl.setVisCols(new int[] {0,1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(rut.getSkladFromCorg());
    jlrCskl.setNavButton(jbSelCskl);

    jlrNazskl.setColumnName("NAZSKL");
    jlrNazskl.setNavProperties(jlrCskl);
    jlrNazskl.setSearchMode(1);
    
    jlCorg.setText("Org. jedinica");
    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(tds);
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0,1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String newk, String oldk) {
        jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);
    
    par.bind(tds);

    pan.add(jlCorg, new XYConstraints(15, 20, -1, -1));
    pan.add(jlrCorg, new XYConstraints(150, 20, 100, -1));
    pan.add(jlrNaziv, new XYConstraints(255, 20, 350, -1));
    pan.add(jbSelCorg, new XYConstraints(610, 20, 21, 21));

    pan.add(jlCskl, new XYConstraints(15, 45, -1, -1));
    pan.add(jlrCskl, new XYConstraints(150, 45, 100, -1));
    pan.add(jlrNazskl, new XYConstraints(255, 45, 350, -1));
    pan.add(jbSelCskl, new XYConstraints(610, 45, 21, 21));
    
    pan.add(par, new XYConstraints(0, 70, -1, -1));
        
    pan.add(jlDatum, new XYConstraints(15, 95, -1, -1));
    pan.add(jraDatum, new XYConstraints(150, 95, 100, -1));
    pan.add(jlTol, new XYConstraints(350, 95, -1, -1));
    pan.add(jraTol, new XYConstraints(555, 95, 50, -1));
    
    getJPTV().addTableModifier(new raTableColumnModifier("CPAR",
        new String[] {"CPAR", "NAZPAR"}, dM.getDataModule().getPartneri()));
    
    this.setJPan(pan);
  }

  public String navDoubleClickActionName() {
    return "Prikaz ponude";
  }

  int[] cc = {0,3,4,5,6,7,8};
  public int[] navVisibleColumns() {
    return cc;
  }
  
  void showDefaultValues() {
    rcc.EnabDisabAll(pan, true);
    this.getJPTV().clearDataSet();
    this.repaint();
    pons.empty();
    jraDatum.requestFocus();
  }

  public void componentShow() {
    tds.setTimestamp("DATUM", vl.getToday());
    tds.setInt("TOL", 0);
    showDefaultValues();
  }

  public void firstESC() {
    this.getJPTV().clearDataSet();
    removeNav();
    showDefaultValues();
  }
  
  protected void addNavBarOptions() {
    super.addNavBarOptions();
    getJPTV().getNavBar().addOption(rnvStop, 2);
  }
  
  protected void navbarremoval() {
    super.navbarremoval();
    if (getJPTV().getNavBar().contains(rnvStop))
      this.getJPTV().getNavBar().removeOption(rnvStop);
  }

  public void okPress() {
    Timestamp dan = ut.getFirstSecondOfDay(tds.getTimestamp("DATUM"));
    String year = vl.findYear(dan);
    Timestamp tol = ut.addDays(dan, -tds.getInt("TOL"));
    
    Condition mc = Condition.equal("VRDOK", "PON").
        and(Condition.till("DATDOK", dan)).
        and(Condition.where("DATDOSP", Condition.BEFORE, tol)).
        and(Condition.equal("STATIRA", "N"));
    
    if (tds.getString("CORG").length() > 0)
      mc = mc.and(Condition.equal("PARAM", "OJ")).
          and(Condition.equal("CSKL", tds.getString("CORG")));
    if (tds.getString("CSKL").length() > 0)
      mc = mc.and(Condition.equal("CSKL", tds)).
          and(Condition.where("PARAM", Condition.NOT_EQUAL, "OJ"));
    mc = mc.and(par.getCondition());
    
    //Condition dc = Condition.equal("REZKOL", "D");
    
    String q = "SELECT doki.cskl, doki.god, doki.vrdok, doki.brdok, " +
    		"MAX(doki.cpar) as cpar, MAX(doki.datdok) as datdok, " +
    		"MAX(doki.datdosp) as datdosp, MAX(doki.uirac) as uirac, " +
    		"MAX(doki.param) as param, COUNT(*) as nums FROM doki,stdoki " +
    		"WHERE " + rut.getDoc("doki", "stdoki") + " AND " + 
    		mc.qualified("doki") +
    		" GROUP BY doki.cskl, doki.god, doki.vrdok, doki.brdok";
    
    System.out.println("Query: " + q);
    pons.empty();
    fillScratchDataSet(pons, q);
    if (pons.rowCount() == 0) setNoDataAndReturnImmediately();
    
    pons.setTableName("unrealpon");
    pons.first();
    setDataSet(pons);
    getJPTV().installSelectionTracker(
        new String[] {"CSKL", "GOD", "BRDOK", "VRDOK"});
  }
  
  public void jptv_doubleClick() {
    raProcess.runChild(new Runnable() {
      public void run() {
        String type = pons.getString("PARAM");
        System.out.println("tip: "+ type);
        raMasterDetail md = null;
        if (type.equals("P")) {
          md = (raMasterDetail) raLoader.load("hr.restart.robno.raPON");
          presPON.getPres().showJpSelectDoc("PON", md, false);
        } else if (type.equals("OJ")) {
          md = (raMasterDetail) raLoader.load("hr.restart.robno.raPONOJ");
          presPONOJ.getPres().showJpSelectDoc("PON", md, false);
        } else if (type.equals("K")) {
          md = (raMasterDetail) raLoader.load("hr.restart.robno.raPONkup");
          presPONkup.getPres().showJpSelectDoc("PON", md, false);
        }
        raProcess.yield(md);
      }
    });
    
    final raMasterDetail md = (raMasterDetail) raProcess.getReturnValue();
    if (md == null) {
      JOptionPane.showMessageDialog(null, "Greška kod otvaranja dokumenta!",
          "Prikaz ponude", JOptionPane.WARNING_MESSAGE);
      return;
    }
     
    DataSet pres = md.getPreSelect().getSelRow();
    pres.setString("VRDOK", "PON");
    pres.setTimestamp("DATDOK-from", ut.getYearBegin(pons.getString("GOD"))); 
    pres.setTimestamp("DATDOK-to", ut.getYearEnd(pons.getString("GOD")));
    pres.setString("CSKL", pons.getString("CSKL"));
    ((jpPreselectDoc) md.getPreSelect()).memorize();
    md.showRecord(new String[] {
        pons.getString("CSKL"), "PON", pons.getString("GOD"),
          Integer.toString(pons.getInt("BRDOK"))});
  }
  
  void ponisti() {
    int pp = getJPTV().getSelectCount();
    if (pp == 0) {
      JOptionPane.showMessageDialog(getWindow(),
          "Nije odabrana nijedna ponuda!", "Poništavanje ponuda",
          JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (JOptionPane.showConfirmDialog(getWindow(), "Poništiti " +
        Aus.getNum(pp, "oznaèenu ponudu?", "oznaèene ponude?",
            "oznaèenih ponuda?"), "Poništavanje ponuda",
           JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
      return;
    
    
    raProcess.runChild(new Runnable() {
      public void run() {
        cancelPonProc();
      }
    });
    
    if (cpon > 0) {
      JOptionPane.showMessageDialog(getWindow(), 
          Aus.getNumDep(cstpon, "Poništena ", "Poništene ","Poništeno ") +
          Aus.getNum(cstpon, "stavka", "stavke", "stavaka") + " na " +
          Aus.getNum(cpon, "ponudi.", "ponude.", "ponuda."), "Poruka",
          JOptionPane.INFORMATION_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(getWindow(),
          "Nije poništena nijedna ponuda.", "Poruka",
          JOptionPane.INFORMATION_MESSAGE);
    }
  }
  
  int cpon, cstpon;
  void cancelPonProc() {
    cpon = cstpon = 0;
    DataSet ds = getJPTV().getSelectionView();
    try {
      for (ds.first(); ds.inBounds(); ds.next()) {
        checkClosing();
        cancelPon(Condition.whereAllEqual(rut.mkey, ds));
      }
    } finally {
      getJPTV().destroySelectionView();
      getJPTV().removeSelection();
    }
  }
  
  void cancelPon(Condition c) {
    DataSet ms = doki.getDataModule().getTempSet(c);
    ms.open();
    if (!ms.getString("STATIRA").equals("N")) return;
    
    String cskcol = "CSKL";
    if (ms.getString("PARAM").equals("OJ")) cskcol = "CSKLART";
    
    ++cpon;
    DataSet det = stdoki.getDataModule().getTempSet(c);
    det.open(); 
    
    for (det.first(); det.inBounds(); det.next())
      if (det.getString("REZKOL").equals("D")) {
        ++cstpon;
        DataSet st = Stanje.getDataModule().getTempSet(
            "CSKL GOD CART KOLREZ",
            Condition.equal("CSKL", det, cskcol).
            and(Condition.equal("GOD", det)).
            and(Condition.equal("CART", det)));
        st.open();
        if (st.rowCount() > 0) {
          Aus.sub(st, "KOLREZ", det, "KOL");
          st.saveChanges();
        }
      }
    
    ms.setString("STATIRA", "X");
    ms.saveChanges();
  }

  public boolean runFirstESC() {
    return getJPTV().getStorageDataSet() != null;
  }
}
