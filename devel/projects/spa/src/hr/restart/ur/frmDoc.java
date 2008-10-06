/****license*****************************************************************
**   file: frmDoc.java
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
package hr.restart.ur;

import hr.restart.baza.Condition;
import hr.restart.baza.Urdok;
import hr.restart.baza.Urshist;
import hr.restart.baza.Urstat;
import hr.restart.baza.Urvrdok;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Aus;
import hr.restart.util.ImageLoad;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;
import hr.restart.util.raTransaction;
import hr.restart.util.startFrame;
import hr.restart.zapod.OrgStr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class frmDoc extends raMatPodaci {
  dM dm = dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  Valid vl = Valid.getValid();
  Util ut = Util.getUtil();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  
  jpDetailDoc jpDetail;
  static frmDoc inst;
  
  presDoc pres;
  
  String vrdok, god;
  Timestamp dfrom, dto;
  Condition counter;
  
  boolean racs;
  String[] oldCols = {"RBR", "CPAR",  "BROJDOK", "STATUS", "IZNOS", "OZNVAL"};
  String[] checkRacCols = {"RBR", "CPAR", "BROJDOK", "IZNOS", "OZNVAL"};
  String[] dupCheckCols = {"KNJIG", "CPAR", "VRDOK", "BROJDOK"};
  String[] editCheckCols = {"CPAR", "BROJDOK"};
  DataRow oldVals;
  
  raNavAction rnvImgLoad = new raNavAction("Skenirani dokument", raImages.IMGMOVIE, KeyEvent.VK_F6){
    public void actionPerformed(ActionEvent e) {
      rnvImgLoad_actionPerformed(e);
    }
    
  };
  public frmDoc() {
    super();
    try {
      inst = this;
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public String getAdditionalSaveName() {
    return "-" + pres.getSelRow().getString("VRDOK");
  }
  
  public void beforeShow() {
    vrdok = pres.getSelRow().getString("VRDOK");
    dfrom = pres.getSelRow().getTimestamp("DATPRI-from");
    dto = pres.getSelRow().getTimestamp("DATPRI-to");
    god = pres.getSelRow().getString("GOD");
    DataSet vrdoks = Urvrdok.getDataModule().getQueryDataSet();
    ld.raLocate(vrdoks, "VRDOK", vrdok);
    counter = Condition.in("VRDOK", Urvrdok.getDataModule().getTempSet(
          Condition.equal("CBROJ", vrdoks.getInt("CBROJ"))));
    counter = Aus.getKnjigCond().and(counter).and(Condition.equal("GOD", god));
    
    jpDetail.setVrdok(vrdok);
    setTitle("Dokumenti tipa - " + vrdoks.getString("NAZIV") + "  od "+
        Aus.formatTimestamp(dfrom) + " do " + Aus.formatTimestamp(dto) + getGodinaSfx());
    
    racs = "URA".equals(vrdok) || "IRA".equals(vrdok);
  }
  
  private String getGodinaSfx() {
    if (vl.findYear(dfrom).equals(vl.findYear(dto)) && god.equals(vl.findYear(dfrom))) return "";
    return " (za godinu " + god + ".)";
  }
  
  public static frmDoc getInstance() {
    return inst;
  }
  
  public void EntryPoint(char mode) {
    if (mode == 'I') {
      if (racs) {
        jpDetail.jpgetval.setValutaEditable(false);
        jpDetail.jpgetval.initJP('I');
      }
    }
    if (racs) {
      rcc.setLabelLaF(jpDetail.jlrSig, false);
      rcc.setLabelLaF(jpDetail.jlrIme, false);
      rcc.setLabelLaF(jpDetail.jlrPrezime, false);
      rcc.setLabelLaF(jpDetail.jbSelSig, false);
    }
  }
  
  public void SetFokus(char mode) {
    if (racs && mode != 'I') {
      jpDetail.jpgetval.setValutaEditable(mode == 'N');
      jpDetail.jpgetval.initJP(mode);
    }
    if (mode == 'B') {
      if (jpDetail.jbDoh != null) jpDetail.jbDoh.setEnabled(true);
      if (jpDetail.jbDoh2 != null) jpDetail.jbDoh2.setEnabled(true);
      if (jpDetail.jbDoh3 != null) jpDetail.jbDoh3.setEnabled(true);
    }
    if (mode == 'N') {
      jpDetail.jlrIme.setText("");
      jpDetail.jlrPrezime.setText("");
      jpDetail.jpp.clear();
      Timestamp td = ut.getToday(dfrom, dto);
      jpDetail.jpo.setCorg(OrgStr.getKNJCORG(false));
      getRaQueryDataSet().setString("CUSER", raUser.getInstance().getUser());
      getRaQueryDataSet().setTimestamp("DATDOK", td);
      getRaQueryDataSet().setTimestamp("DATPRI", td);
      getRaQueryDataSet().setString("KNJIG", OrgStr.getKNJCORG(false));
      getRaQueryDataSet().setString("VRDOK", vrdok);
      getRaQueryDataSet().setInt("RBR", findNextUR());
      getRaQueryDataSet().setString("GOD", god);
      jpDetail.initStatus();
      if (racs) jpDetail.jpp.focusCparLater();
      else jpDetail.jlrSig.requestFocusLater();
    } else if (mode == 'I'){
      oldVals = new DataRow(getRaQueryDataSet(), oldCols);
      dM.copyColumns(getRaQueryDataSet(), oldVals, oldCols);
      jpDetail.jraOpis.requestFocusLater();
    }
    jpDetail.setEdit(mode != 'B');
  }
  
  public void afterSetMode(char bef, char aft) {
    if (aft == 'B') {
      jpDetail.setEdit(false);
      if (racs) jpDetail.jpgetval.disableDohvat();
    }
  }

  private int findNextUR() {
    DataSet nur = Util.getNewQueryDataSet("SELECT MAX(rbr) as maxbroj FROM urdok WHERE " + counter);
    return nur.getInt("MAXBROJ") + 1;
  }
  
  public void rnvUpdate_action() {

    if (checkUserEdit()) super.rnvUpdate_action();

  }

  public void rnvDelete_action() {

    if (checkUserEdit()) super.rnvDelete_action();

  }

  private boolean checkUserEdit() {
    if (checkUser()) return true;
    
    if (showUserCheckMsg()) {
      startFrame.changeUser();
      return checkUserEdit();
    }
    return false;
  }
  
  private boolean checkUser() {
    try {
      if (raUser.getInstance().isSuper()) return true; // superuser
      if (raUser.getInstance().getUser().equals(
          getRaQueryDataSet().getString("CUSER"))) return true;
      return false;
    } catch (Exception ex) {
      return false;
    }
  }
  
  public boolean showUserCheckMsg() {
    String uCMess = "Dokument je izradio drugi korisnik. Moguæ je samo pregled !";
    String[] ops = {"OK","Promjena korisnika"};
    return JOptionPane.showOptionDialog(getWindow(), uCMess, "Sistem",
      JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, ops, ops[0]) == 1;
  }

  private boolean autoinc;
  public boolean Validacija(char mode) {
    autoinc = false;
    if (vl.isEmpty(jpDetail.jraBroj) ||
        !jpDetail.jpo.Validacija() || vl.isEmpty(jpDetail.jraBrojdok)
        || vl.isEmpty(jpDetail.jraDatdok) || vl.isEmpty(jpDetail.jraDatpri)) return false;
    if (racs && !jpDetail.jpp.Validacija()) return false;
    
    //if (!pres.validateEntry()) {
    Timestamp dpri = getRaQueryDataSet().getTimestamp("DATPRI");
    if (dpri.before(ut.getFirstSecondOfDay(dfrom)) || 
        dpri.after(ut.getLastSecondOfDay(dto))) {
      jpDetail.jraDatpri.requestFocus();
      JOptionPane.showMessageDialog(getWindow(),
        "Uneseni podaci ne odgovaraju zadanim kriterijima u predselekciji",
        "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    if (racs) {
      if (!Aus.checkDatAndDosp(jpDetail.jraDatdok, jpDetail.jraDatdosp)) return false;
      int brdokDuplicates = Urdok.getDataModule().getRowCount(
          Condition.whereAllEqual(dupCheckCols, getRaQueryDataSet()));
      if ((mode == 'N' || !dM.rowsEqual(oldVals, getRaQueryDataSet(), editCheckCols))
          && brdokDuplicates > 0) {
        jpDetail.jraBrojdok.requestFocus();
        if (JOptionPane.showConfirmDialog(jpDetail, "Raèun istog broja za ovog partnera " +
                "veæ postoji. Spremiti dokument?", "Upozorenje", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) !=
              JOptionPane.OK_OPTION) return false;
      }
    } else if (!Aus.checkSanityRange(jpDetail.jraDatdok)) return false;

    if (mode == 'N' && getRaQueryDataSet().getInt("RBR") != findNextUR()) {
      jpDetail.jraBroj.requestFocus();
      int response = JOptionPane.showConfirmDialog(jpDetail, "Interni broj nije u slijedu. " +
            "Želite li da se automatski poveæa?", "Upozorenje", 
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
      if (response == JOptionPane.CANCEL_OPTION) return false;
      if (response == JOptionPane.YES_OPTION) autoinc = true;
    }
    if (mode == 'I' && getRaQueryDataSet().getString("STATKNJ").equals("D")) {
      if (!dM.rowsEqual(oldVals, getRaQueryDataSet(), checkRacCols)) {
        jpDetail.jraBrojdok.requestFocus();
        JOptionPane.showMessageDialog(this.getWindow(), "Raèun je proknjižen " +
                "u financijsko knjigovodstvo! Izmjena nije moguæa.", 
            "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    if (mode == 'I' && getRaQueryDataSet().getInt("RBR") != oldVals.getInt("RBR")) {
      jpDetail.jraBroj.requestFocus();
      if (JOptionPane.showConfirmDialog(jpDetail, "Interni broj je promijenjen! " +
            "Spremiti promjene?", "Upozorenje", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) !=
              JOptionPane.OK_OPTION) return false;
    }
    return true;
  }
  
  private String delUuid;
  public boolean DeleteCheck() {
    if (getRaQueryDataSet().getString("STATKNJ").equals("D")) {
      JOptionPane.showMessageDialog(this.getWindow(), "Ne mogu se brisati dokumenti " +
            "koji su proknjiženi u financijsko knjigovodstvo!", 
            "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    delUuid = getRaQueryDataSet().getString("UUID");
    return true;
  }
  
  public boolean doBeforeSave(char mode) {
    if (autoinc && mode == 'N') {
      getRaQueryDataSet().setInt("RBR", findNextUR());
    }
    if (mode == 'N') {
      long tim = System.currentTimeMillis();
      StringBuffer hex = new StringBuffer();
      char[] hexdigits = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
      for (int i = 0; i < 16; i++, tim >>>= 4)
        hex.insert(0, hexdigits[(int) tim & 15]);
      getRaQueryDataSet().setString("UUID", hex.toString());
      saveStatusHistory();
    }
    if (mode == 'I' && !oldVals.getString("STATUS").
        equals(getRaQueryDataSet().getString("STATUS"))) {
      saveStatusHistory();
    }
    return true;
  }
  
  public boolean doWithSave(char mode) {
    if (mode == 'B') {
      try {
        raTransaction.runSQL("DELETE FROM urshist WHERE uuid = '" + delUuid + "'");
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }
  
  private void saveStatusHistory() {
    QueryDataSet hist = Urshist.getDataModule().getTempSet("1=0");
    hist.open();
    hist.insertRow(false);
    hist.setString("UUID", getRaQueryDataSet().getString("UUID"));
    hist.setTimestamp("DATUM", ut.getCurrentDatabaseTime());
    hist.setString("STATUS", getRaQueryDataSet().getString("STATUS"));
    hist.setString("CUSER", raUser.getInstance().getUser());
    raTransaction.saveChanges(hist);
  }
  
  public void datdokChanged() {
    partnerChanged();
    if (racs) jpDetail.jpgetval.setTecajDate(getRaQueryDataSet().getTimestamp("DATDOK"));
  }
  
  public void invokeLink(String col) {
    if (getMode() == 'B') showLink(col);
    else selectLink(col);
  }
  
  void showLink(String col) {
    String hlink = getRaQueryDataSet().getString(col).trim();
    if (hlink.length() == 0) return;
    
    String invoker = frmParam.getParam("sisfun", "invoker", "explorer", 
        "Naredba za prikazivanje dokumenata", true);
    boolean quote = frmParam.getParam("sisfun", "invokerQuoted", "D", 
        "Uokviriti datoteku navodnicima (D,N)", true).equalsIgnoreCase("D");
    
    final String comm = invoker + (quote ? " \"" : " ") + hlink + (quote ? "\"" : "");
    
    new Thread() {
      public void run() {
        try {
          Runtime.getRuntime().exec(comm);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }.start();
  }
  
  JFileChooser linkCh = new JFileChooser();
  void selectLink(String col) {
    if (linkCh.showOpenDialog(getWindow()) == JFileChooser.APPROVE_OPTION) {
      File f = linkCh.getSelectedFile();
      if (!f.exists()) {
        JOptionPane.showMessageDialog(getWindow(), "Datoteka ne postoji!",
            "Greška", JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (f.isDirectory()) {
        JOptionPane.showMessageDialog(getWindow(), "Odabrana datoteka je direktorij!",
            "Greška", JOptionPane.ERROR_MESSAGE);
        return;
      }
      try {
        getRaQueryDataSet().setString(col, f.toURL().toString());
      } catch (IOException e) {
        JOptionPane.showMessageDialog(getWindow(), "Neispravna datoteka!",
            "Greška", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
  
  public void partnerChanged() {
    if (!racs || getMode() != 'N') return;
    if (lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR",
        String.valueOf(this.getRaQueryDataSet().getInt("CPAR")))) {
        this.getRaQueryDataSet().setTimestamp("DATDOSP", new Timestamp(
           hr.restart.robno.raDateUtil.getraDateUtil().addDate(
             this.getRaQueryDataSet().getTimestamp("DATDOK"), dm.getPartneri().getShort("DOSP")
           ).getTime()));
    } else getRaQueryDataSet().setTimestamp("DATDOSP", getRaQueryDataSet().getTimestamp("DATDOK")); 
  }
  
  private void jbInit() throws Exception {
    setRaQueryDataSet(Urdok.getDataModule().getQueryDataSet());
    setVisibleCols(new int[] {1,2,3,4,5,6,7});
    setTitle("Dokumenti");
    jpDetail = new jpDetailDoc(this);
    setRaDetailPanel(jpDetail);
    jpDetail.BindComponents(this.getRaQueryDataSet());
    getJpTableView().getColumnsBean().setSaveSettings(true);
    getJpTableView().getColumnsBean().setSaveName(getClass().getName());
    this.setSort(new String[]{"RBR"});
    
    removeRnvCopyCurr();
    this.getNavBar().removeStandardOption(raNavBar.ACTION_TOGGLE_TABLE);
    this.getJpTableView().addTableModifier(new raSkStatusColorModifier());
    this.getJpTableView().addTableModifier(
      new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, dm.getPartneri()));
    this.getJpTableView().addTableModifier(
        new raTableColumnModifier("STATUS", new String[] {"OPIS"}, 
            Urstat.getDataModule().getQueryDataSet()));
    
    pres = new presDoc();
    
    this.addOption(new raNavAction("Predselekcija", raImages.IMGZOOM, KeyEvent.VK_F12) {
      public void actionPerformed(ActionEvent e) {
        pres.showPreselect(frmDoc.this, "Dokumenti urudžbenog zapisnika");
      }
    }, 4, false);
    this.addOption(rnvImgLoad,5);
  }
  void  rnvImgLoad_actionPerformed(ActionEvent e) {
    ImageLoad imgload= new ImageLoad();
    imgload.Img(this.getJframe(), "urdok", getRaQueryDataSet().getString("UUID"),"Skenirani dokument "+getRaQueryDataSet().getString("BROJDOK"));
   }
}
