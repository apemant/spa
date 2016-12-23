/****license*****************************************************************
**   file: frmIntervencije.java
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
package hr.restart.zapod;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.StorageDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.Intervencije;
import hr.restart.baza.dM;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.XYPanel;
import hr.restart.swing.raInputDialog;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.swing.raTableValueModifier;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;


public class frmIntervencije extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  Util ut = Util.getUtil();
  
  XYPanel pan;
  
  raComboBox rcbTip = new raComboBox();
  
  JPanel jp = new JPanel(new BorderLayout());

  JraScrollPane vp = new JraScrollPane();
  
  JEditorPane opis = new JEditorPane() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
  };

  String cuser, corg;
  int cpar;
  Timestamp dfrom, dto;
  boolean allu, alls;
  
  
  raNavAction rnvFilter = new raNavAction("Filter", raImages.IMGZOOM, java.awt.event.KeyEvent.VK_F12) {
    public void actionPerformed(ActionEvent e) {
      setFilter();
    }
  };
  
  raNavAction rnvStatus = new raNavAction("Zatvori", raImages.IMGSENDMAIL, java.awt.event.KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      setStatus();
    }
  };

  public frmIntervencije() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(pan.getText("DATUM"), false);
    }
  }

  public void beforeShow() {
    if (raUser.getInstance().getUser().equals(cuser) && OrgStr.getKNJCORG(false).equals(corg)) return;
    
    cuser = raUser.getInstance().getUser();
    corg = OrgStr.getKNJCORG(false);
    dfrom = ut.addMonths(dto = vl.getToday(), -1);

    refilter();
  }
  
  public void refilter() {
    Condition c = Aus.getCorgCond().and(Condition.between("DATUM", dfrom, dto));
    if (!allu) c = c.and(Condition.equal("CUSER", cuser));
    if (!alls) c = c.and(Condition.diff("STATUS", "Z"));
    if (cpar > 0) c = c.and(Condition.equal("CPAR", cpar));
    Intervencije.getDataModule().setFilter(getRaQueryDataSet(), c);
    getRaQueryDataSet().open();
  }
  
  public void setStatus() {
    StorageDataSet ds = Aus.createSet("TRAJANJE");
    XYPanel xs = new XYPanel(ds);
    xs.label("Trajanje").text("TRAJANJE").expand();
    
    long mins = (vl.getToday().getTime() - getRaQueryDataSet().getTimestamp("DATUM").getTime()) / 1000 / 60;
    mins = (mins + 4) / 5 * 5;
    
    ds.setInt("TRAJANJE", (int) mins);
    
    raInputDialog sd = new raInputDialog();
    if (sd.show(getWindow(), xs, "Zatvaranje")) {
      getRaQueryDataSet().setInt("TRAJANJE", ds.getInt("TRAJANJE"));
      getRaQueryDataSet().setTimestamp("DATZ", vl.getToday());
      getRaQueryDataSet().setString("STATUS", "Z");
      getRaQueryDataSet().saveChanges();
      getJpTableView().fireTableDataChanged();
    }
  }
  
  public void setFilter() {
    StorageDataSet ds = Aus.createSet("Doki.CPAR @DATFROM @DATTO ALLU:1 ALLS:1");
    XYPanel xd = new XYPanel(ds);
    xd.label("Partner").nav(new String[] {"CPAR", "NAZPAR"}, new int[] {100,250}, dm.getPartneri()).nl();
    xd.label("Period (od - do)").text("DATFROM").text("DATTO").nl();
    xd.skip(150).check("Svi statusi", "ALLS").skip(105).check("Svi korisnici", "ALLU").nl().expand();
    if (cpar > 0) ds.setInt("CPAR", cpar);
    ds.setTimestamp("DATFROM", dfrom);
    ds.setTimestamp("DATTO", dto);
    ds.setString("ALLU", allu ? "D" : "N");
    ds.setString("ALLS", alls ? "D" : "N");
    
    raInputDialog fd = new raInputDialog();
    if (fd.show(getWindow(), xd, "Filter")) {
      if (!ds.isNull("CPAR") && ds.getInt("CPAR") > 0) 
        cpar = ds.getInt("CPAR");
      else cpar = 0;
      dfrom = ds.getTimestamp("DATFROM");
      dto = ds.getTimestamp("DATTO");
      allu = ds.getString("ALLU").equalsIgnoreCase("D");
      alls = ds.getString("ALLS").equalsIgnoreCase("D");
      refilter();
      getJpTableView().fireTableDataChanged();
      jeprazno();
    }
  }
  
  public void SetFokus(char mode) {
    if (mode == 'N') {
      opis.setText("");
      getRaQueryDataSet().setString("VRSTA", "O");
      rcbTip.findCombo();
      getRaQueryDataSet().setTimestamp("DATUM", vl.getToday());
      getRaQueryDataSet().setString("CORG", corg);
      getRaQueryDataSet().setString("CUSER", cuser);
      getRaQueryDataSet().setString("STATUS", "O");
      pan.getNav("CPAR").forceFocLost();
      pan.getNav("CPAR").requestFocusLater();
    } else if (mode == 'I') {
      opis.requestFocus();
      opis.setCaretPosition(opis.getDocument().getLength());
    }
  }
  
  public boolean Validacija(char mode) {
    if (vl.isEmpty(pan.getNav("CPAR")) || vl.isEmpty(pan.getText("DATUM")) || vl.isEmpty(pan.getText("NAZIV")))
      return false;
    
    getRaQueryDataSet().setString("OPIS", opis.getText());
    return true;
  }
  
  public boolean doBeforeSave(char mode) {
    if (mode == 'N') {
      getRaQueryDataSet().setInt("UID", Valid.getValid().findSeqInt("INTERV-int"));
    }
    return true;
  }
  
  public void raQueryDataSet_navigated(NavigationEvent e) {
    if (getRaQueryDataSet().rowCount() == 0) opis.setText("");
    else {
      String tx = getRaQueryDataSet().getString("OPIS");
      opis.setText(tx);
    }
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(Intervencije.getDataModule().copyDataSet());
    this.setVisibleCols(new int[] {1, 2, 3, 4, 5, 7, 8});
    
    rcbTip.setRaColumn("VRSTA");
    rcbTip.setRaDataSet(getRaQueryDataSet());
    rcbTip.setRaItems(new String[][] {
        {"Programska greška","B"},
        {"Zahtjev ispravka","I"},
        {"Pomoæ / pitanja","P"},
        {"Održavanje","M"},
        {"Prilagodba","U"},
        {"Ostalo","O"}
    });
    
    pan = new XYPanel(getRaQueryDataSet());
    pan.label("Datum").text("DATUM", 175).combo(rcbTip, 175).nl();
    pan.label("Partner").nav(new String[] {"CPAR", "NAZPAR"}, new int[] {100,250}, dm.getPartneri()).nl();
    pan.label("Kratki opis").text("NAZIV", 355).nl();
    pan.label("Opis i napomene").expand();
    
    vp.setPreferredSize(new Dimension(500, 200));
    vp.setViewportView(opis);
    
    jp.add(pan, BorderLayout.NORTH);
    jp.add(vp);
    
    setRaDetailPanel(jp);
    
    jpDetailView.remove(jScrollPaneDetail);
    jpDetailView.add(jp);
    
    getRaQueryDataSet().getColumn("DATUM").setDisplayMask("dd-MM-yyyy  HH:mm:ss");
    getRaQueryDataSet().getColumn("DATUM").setWidth(24);
    getRaQueryDataSet().getColumn("DATZ").setDisplayMask("dd-MM-yyyy  HH:mm:ss");
    getRaQueryDataSet().getColumn("DATZ").setWidth(24);
    
    getJpTableView().addTableModifier(new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, dm.getPartneri()));
    /*getJpTableView().addTableModifier(new raTableValueModifier("VRSTA", new String[] {"B", "I", "P", "M", "U", "O"}, 
        new String[] {"B - Programska greška", "I - Zahtjev ispravka", "P - Pomoæ / pitanja", 
        "M - Održavanje", "U - Prilagodba", "O - Ostalo"}));*/
    addOption(rnvFilter, 6, false);
    addOption(rnvStatus, 4, false);
  }
}
