/****license*****************************************************************
**   file: frmIzvodi.java
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
package hr.restart.gk;

import hr.restart.baza.Condition;
import hr.restart.baza.Gkstavkerad;
import hr.restart.baza.Izvodi;
import hr.restart.baza.Partneri;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sk.raMatchDialog;
import hr.restart.sk.raSaldaKonti;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raAdditionalLookupFilter;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raKeyAction;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;
import hr.restart.zapod.raKonta;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmIzvodi extends raMasterDetail {
  private static final int[] visibleColsDetailIzvodi = new int[] { 4, 5, 11, 12, 13, 18, 19 };
  private static frmIzvodi fIzv;

  raCommonClass rcc = raCommonClass.getraCommonClass();

  dM dm = dM.getDataModule();

  Valid vl = Valid.getValid();

  jpIzvodiMaster jpMaster;

  jpIzvodiDetail jpDetail;

  JPanel jpMessage;

  JCheckBox jcbPokriv = new JCheckBox("Pokrivanje", true);

  JLabel jlMessage = new JLabel();

  StorageDataSet knjizenjeSet = new StorageDataSet();

  raKonta rKon;

  String oznval = "";

  String promet = "";

  String kontoprometa = "";

  String ziro = "";

  String idizvod = "";

  boolean devizni = false;

  boolean devind = false;

  raKnjizenje knjizenje = null;

  private java.math.BigDecimal nula = new java.math.BigDecimal(0);
  
  // stavka prilikom pokrivanja. Ako se promijeni neki od bitnih
  // , treba ponistiti pokrivanje.
  private DataRow saveRow;
  private int vrdokSel = -1;
  private String[] checkCols = {"CPAR", "BROJKONTA", "ID", "IP", "DEVID", "DEVIP"};
  

  raCommonClass rCC = raCommonClass.getraCommonClass();

  raMatchDialog match = new raMatchDialog();

  private raNavAction rnvObrada = new raNavAction("Obrada", raImages.IMGHISTORY,
      KeyEvent.VK_F10) {
    public void actionPerformed(ActionEvent e) {
      obradaIzvoda();
    }
  };

  //  presIzvodi pres;
  public frmIzvodi() {
    try {
      jbInit();
      fIzv = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static frmIzvodi getFrmIzvodi() {
    if (fIzv == null)
      fIzv = new frmIzvodi();
    return fIzv;
  }

  boolean getZiroParams() {
    ziro = getPreSelect().getSelRow().getString("ZIRO");
    if (((presIzvodi) getPreSelect()).posZiro()) {
      oznval = dm.getZirorn().getString("OZNVAL");
      promet = dm.getZirorn().getString("PROMET");
      kontoprometa = dm.getZirorn().getString("BROJKONTA");
      idizvod = ziro.concat("/").concat(oznval);
      devizni = !oznval.equals(hr.restart.zapod.Tecajevi.getDomOZNVAL());
      devind = dm.getZirorn().getString("DEV").equals("D");
      return true;
    } else
      return false;
  }

  void setTecajVisible() {
    jpDetail.jraTecaj.setVisible(devizni);
    jpDetail.jraTecaj.setEnabled(devizni);
    jpDetail.jlaTecaj.setVisible(devizni);
    jpDetail.jpDev.setVisible(devizni);
  }
  public void Funkcija_ispisa_detail() {
    raDetail.getJpTableView().enableEvents(false);
    posNalozi();
    getZiroParams();
    raDetail.getRepRunner().clearAllCustomReports();
    // raDetail.getRepRunner().addReport("hr.restart.gk.repIzvod", "Ispis izvoda",
    //        2); cuda radi po tablici
    getKnjizenje().getFNalozi().fillReports(raDetail, true);
    super.Funkcija_ispisa_detail();
    raDetail.getJpTableView().enableEvents(true);
  }
  public void Funkcija_ispisa_master() {
    if (!raDetail.isShowing()) {
      changeDetailViewStatus(getMasterSet().getString("STATUS"));
    }
    posNalozi();
    getZiroParams();
    raMaster.getRepRunner().clearAllCustomReports();
    raMaster.getRepRunner().addReport("hr.restart.gk.repIzvod", "Ispis izvoda",
        2);
    raMaster.getRepRunner().addReport("hr.restart.gk.repIzvodi",
        "Ispis zaglavlja izvoda", 2);
    // Ispisi sa temeljnice
    getKnjizenje().getFNalozi().fillReports(raMaster, true);
    //raMaster.getRepRunner().removeReport("hr.restart.gk.repNalozi");
    //    raMaster.getRepRunner().addReport("hr.restart.gk.repNalog","Ispis naloga
    // - temeljnice",5);
    super.Funkcija_ispisa_master();
  }
  
  public void detailSet_navigated(NavigationEvent e) {
    if (raDetail.getMode() == 'B' && getDetailSet().getRowCount() > 0 &&
        !getMasterSet().getString("STATUS").equalsIgnoreCase("K") &&
        getDetailSet().getString("BROJKONTA").length() > 0) {
      lastOPIS = getDetailSet().getString("OPIS");
      lastVRDOK = getDetailSet().getString("VRDOK");
      lastPok = getDetailSet().getString("POKRIVENO");
      lastCpar = jpDetail.jlrCpar.getText();
      lastKonto = getDetailSet().getString("BROJKONTA");
      lastCorg = getDetailSet().getString("CORG");
    }
  }

  public void masterSet_navigated(NavigationEvent ev) {
    if (raDetail.isShowing())
      return;
    posNalozi();
    frmNalozi.handleRnvActionsWithStatus_masterSet_navigated(this, isLastIzvod());
/*    if (!getMasterSet().getString("STATUS").equals("K")) {
      raMaster.setEnabledNavAction(rnvObrada,true);
    }*/
  }

  /**
   * @return
   */
  private boolean isLastIzvod() {
    boolean nemaKontroluLastIzvod = frmParam.getParam("gk", "chnotlastizv", "N", "Dozvoliti izmjenu izvoda koji nije zadnji")
    	.equals("D");
    if (nemaKontroluLastIzvod) return true;
    return (getMasterSet().getInt("BROJIZV") == 
      (gkQuerys.getNextBrojIzv(getMasterSet().getString("ZIRO"), getMasterSet().getString("GOD"))-1));
  }

  public void EntryPointMaster(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    getZiroParams();
    if (mode != 'N')
      posNalozi();
    if (mode == 'N') {
      changeDetailViewStatus("N");
      getPreSelect().copySelValues();
      JraTextField jtPresDatTo = ((presIzvodi) getPreSelect()).jraDatumto;
      if (frmParam.getParam("gk","izvNoDate","N","Da li da nudi datume pri unosu novog izvoda").equals("N")) {
        vl.copyValue(jtPresDatTo, jpMaster.jraDatum);
        vl.copyValue(jtPresDatTo, jpMaster.jraDatumknj);
      }
//      String god = Util.getUtil().getYear(jpMaster.datknjset.getTimestamp("DATUMKNJ"));
      String god = Util.getUtil().getYear(jtPresDatTo.getDataSet().getTimestamp(jtPresDatTo.getColumnName())); 
      int brojizv = gkQuerys.getNextBrojIzv(ziro, god);
      getMasterSet().setInt("BROJIZV", brojizv);
      if (brojizv > 1 && promet.equals("D"))
        getMasterSet().setBigDecimal("PRETHSTANJE",
            gkQuerys.getPrethStanje(ziro, god, brojizv - 1));
    } else if (mode == 'I') {
      rcc.setLabelLaF(jpMaster.jraBrojizv, false);
      int brojizv = getMasterSet().getInt("BROJIZV");
      if (brojizv > 1 && promet.equals("D"))
        getMasterSet().setBigDecimal("PRETHSTANJE",
            gkQuerys.getPrethStanje(ziro, getMasterSet().getString("GOD"), brojizv - 1));
    }
    if (mode != 'B') {
      if (getMasterSet().getInt("BROJIZV") == 1) {
        rcc.setLabelLaF(jpMaster.jraPrethstanje, true);
      } else {
        rcc.setLabelLaF(jpMaster.jraPrethstanje, false);
      }
    }
    rcc.setLabelLaF(jpMaster.jraNovostanje, promet.equals("D"));
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      focLostIdIp();
      jpMaster.jraBrojstavki.requestFocus();
    } else if (mode == 'I') {
      jpMaster.jraBrojstavki.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    
    if (vl.isEmpty(jpMaster.jraDatum))
      return false;
    if (vl.isEmpty(jpMaster.jraDatumknj))
      return false;
    if (vl.isEmpty(jpMaster.jraBrojizv))
      return false;
    String god_d = Util.getUtil().getYear(jpMaster.jraDatum.getDataSet().getTimestamp(jpMaster.jraDatum.getColumnName()));
    String god_k = Util.getUtil().getYear(jpMaster.jraDatumknj.getDataSet().getTimestamp(jpMaster.jraDatumknj.getColumnName()));
    if (!god_d.equals(god_k)) {
      JOptionPane.showMessageDialog(jpMaster, 
          "Datum izvoda i datum knjiženja nisu u istoj godini!","Greška",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (!dontCheckAnything()) {
      if (vl.isEmpty(jpMaster.jraBrojstavki))
        return false;
    }
    getZiroParams();
    if (promet.equals("D")) {
      if (!validacijaPromet())
        return false;
    } else {
      if (!validacijaFake())
        return false;
    }
    if (mode == 'N') {
      
    } else {
      updStatus(false);
    }
    return true;
  }

  public boolean doBeforeSaveMaster(char mode) {
    if (mode == 'N') {      
      if (!addNewNalog())
        return false;
      getMasterSet().setString("CNALOGA",
          knjizenje.getFNalozi().jpMaster.jpBrNal.getCNaloga());
      getMasterSet().setString("STATUS", "N");
      getMasterSet().setString("GOD", 
          Util.getUtil().getYear(jpMaster.datknjset.getTimestamp("DATUMKNJ")));
    }
    return true;
  }

  boolean validacijaPromet() {
    if (!dontCheckAnything()) {
      if (vl.chkIsEmpty(jpMaster.jraId) && vl.chkIsEmpty(jpMaster.jraIp)) {
        JOptionPane
            .showMessageDialog(
                jpMaster,
                "Potrebno je unijeti ili ukupni dugovni ili ukupni potražni iznos!",
                "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    if (!checkIznosi())
      return false;
    return true;
  }

  boolean validacijaFake() {
    boolean balans = getMasterSet().getBigDecimal("ID").compareTo(
        getMasterSet().getBigDecimal("IP")) == 0;
    if (!balans) {
      JOptionPane
          .showMessageDialog(
              jpMaster,
              "S obzirom da se stavka prometa ne kreira automatski, izvod bi trebao biti u balansu (dugovni iznos = potražni iznos)!",
              "Greška", JOptionPane.ERROR_MESSAGE);
    }
    return balans;
  }

  BigDecimal getExpectedNovoStanje() {
    return getMasterSet().getBigDecimal("IP").add(
        getMasterSet().getBigDecimal("ID").negate()).add(
        getMasterSet().getBigDecimal("PRETHSTANJE"));
  }

  boolean checkIznosi() {
    //novostanje = ip-id+prethstanje
    BigDecimal novostanje = getMasterSet().getBigDecimal("NOVOSTANJE");
    BigDecimal ip_id_ps = getExpectedNovoStanje();
    boolean chk = novostanje.compareTo(ip_id_ps) == 0;
    if (!chk)
      JOptionPane.showMessageDialog(jpMaster, new raMultiLineMessage(
              "Uneseni iznosi ne odgovaraju! Novo stanje je " + novostanje.toString()
                  + ", a razlika ukupnog potražnog iznosa i ukupunog dugovnog iznosa " +
                  "sa pribrojenim prethodnim stanjem je "
                  + ip_id_ps.toString() + ". Ti iznosi moraju biti jednaki!"),
              "Greška", JOptionPane.ERROR_MESSAGE);
    return chk;
  }

  raKnjizenje getKnjizenje() {
    fillKnjizenjeSet();
    if (knjizenje == null)
      knjizenje = new raKnjizenje(knjizenjeSet);
    return knjizenje;
  }

  boolean addNewNalog() {
    // kasnije koristi member knjizenje
    try {
      if (getKnjizenje().startKnjizenje()) {
        raTransaction.saveChanges(dm.getNalozi());
        return true;
      } else
        return false;
    } catch (Exception ex) {
      return false;
    }
  }

  void focLostIdIp() {
    getMasterSet().setBigDecimal("NOVOSTANJE", getExpectedNovoStanje());
  }

  private String cnaloga = "";

  void posNalozi() {
    if (getMasterSet().rowCount() == 0)
      return;
    String newCnaloga = getMasterSet().getString("CNALOGA");
    if (cnaloga.equals(newCnaloga))
      return;
    cnaloga = newCnaloga;
    boolean succ = getKnjizenje().posNalozi(cnaloga);
    if (!succ)
      cnaloga = "";
    else {
      jpMaster.datknjset.setTimestamp("DATUMKNJ", knjizenjeSet
          .getTimestamp("DATUMKNJ"));
      knjizenje.getFNalozi().jpDetail.jpBrNal.initJP(knjizenje.getFNalozi()
          .getMasterSet());
    }
  }

  public void refilterDetailSet() {
    //super.refilterDetailSet();
    posNalozi();
    setNaloziUI();
    setTitleDetail();
    changeDetailViewStatus(getMasterSet().getString("STATUS"));//tu se zove
                                                               // refilterDetailSet
  }
  
  /*public boolean ValidacijaPrijeIzlazaDetail() {
    inClosing = true;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        inClosing = false;
      }
    });
    return true;
  }*/

  String oldstatus = "X";

  private void changeDetailViewStatus(String newstatus) {
    if (newstatus.equals("K")) {
      setDetailSet(getStavkeProknjizenogIzvoda());
      setKnjizenjeFNaloziDetailSet();
      //      if (!oldstatus.equals("K")) {//ako prije nije bio K treba
      // reinicijalizirati tablicu
      setVisibleColsDetail(new int[] { 4, 5, 9, 10, 11, 12 });
        if (raDetail.isShowing()) {
          raDetail.getNavBar().getColBean().initialize();
        } else raDetail.forceColInitOnShow();
        raDetail.setEditEnabled(false);
      //      }
    } else {
      setDetailSet(dm.getGkstavkerad());
      super.refilterDetailSet();
      setKnjizenjeFNaloziDetailSet();
      //      if (oldstatus.equals("K")) {//ako je prije bio K treba
      // reinicijalizirati tablicu

      setVisibleColsDetail(visibleColsDetailIzvodi);
        if (raDetail.isShowing()) {
          raDetail.getNavBar().getColBean().initialize();
        } else raDetail.forceColInitOnShow();
        setDetailEditEnabled(isLastIzvod());
    }
    oldstatus = newstatus;
    raDetail.rebindRaDetailPanel();
  }

  private void setKnjizenjeFNaloziDetailSet() {
    if (knjizenje != null && knjizenje.getFNalozi()!=null) {
      knjizenje.getFNalozi().setDetailSet(getDetailSet());
    }
  }

  private void setDetailEditEnabled(boolean enable) {
    raDetail.setEditEnabled(enable);
    if (!enable) raDetail.setEnabledNavAction(
        raDetail.getNavBar().getStandardOption(raNavBar.ACTION_UPDATE),true);
  }

  private QueryDataSet getStavkeProknjizenogIzvoda() {
    String cnalogaIzv = getMasterSet().getString("CNALOGA");
    QueryDataSet retSet = hr.restart.baza.Gkstavke.getDataModule()
        .getTempSet(Condition.equal("CNALOGA", cnalogaIzv));
    retSet.open();
    return retSet;
  }

  void setTitleDetail() {
    setNaslovDetail("Izvod ".concat(" br. ") + getMasterSet().getInt("BROJIZV")
        + " - ".concat(idizvod));
  }

  void setNaloziUI() {
    if (!knjizenje.getFNalozi().raMaster.isVisible())
      return;
    knjizenje.getFNalozi().raMaster.getJpTableView().fireTableDataChanged();
    knjizenje.getFNalozi().setTitleMaster();
  }

  void setDefaultTecaj() {
    if (devizni) {
      getDetailSet().setBigDecimal(
          "TECAJ",
          hr.restart.zapod.Tecajevi.getTecaj(getDetailSet().getTimestamp(
              "DATDOK"), oznval));
    } else {
      getDetailSet().setBigDecimal("TECAJ", new BigDecimal(1));
    }
  }

  String lastVRDOK = "IPL", lastPok = "N";

  String lastOPIS = "";

  public void EntryPointDetail(char mode) {
    if (mode == 'N') {
      knjizenje.getFNalozi().newStavka();
      knjizenje.getFNalozi().jpDetail.jpBrNal.noviBrojStavke();
      getDetailSet().setTimestamp("DATDOK",
          getMasterSet().getTimestamp("DATUM"));
      //      getDetailSet().setString("VRDOK", "KUP");
      jpDetail.kcGroup.getJlrBROJKONTA().emptyTextFields();
      jpDetail.jlrCpar.emptyTextFields();
      jpDetail.kcGroup.setEnabledKonto(true);
      jpDetail.kcGroup.setEnabledCorg(false);
      setIdIp(false, false);
      setDefaultTecaj();
      //values before
      if (frmParam.getParam("gk", "lvIzvod", "D",
          "Prenijeti opis na slij.novu stavku izvoda").equals("D")) {
        getDetailSet().setString("OPIS", lastOPIS);
      }
      getDetailSet().setString("VRDOK", lastVRDOK);
      getDetailSet().setString("POKRIVENO", lastPok);
      addSaldaKontaFilter();
    } else if (mode == 'I') {
      knjizenje.getFNalozi().loadStavka();
      knjizenje.getFNalozi().jpDetail.jpBrNal.initJP(getDetailSet().getInt(
          "RBS"));
      jpDetail.kcGroup.setEnabledKonto(false);
      jpDetail.kcGroup.setScrKonto(getDetailSet().getString("BROJKONTA"));
      setVRDOK();
      addSaldaKontaFilter();
    }
    findComboVrdok();
    getZiroParams();
    entryPointDetailDone = true;
  }
  
  public void afterSetModeDetail(char oldMod, char newMod) {
    if (newMod == 'B') rmvSaldaKontaFilter();
  }
  
  private boolean entryPointDetailDone = false;

  public void SetFokusDetail(char mode) {
    rCC.setLabelLaF(jpDetail.jtHor1, false);
    rCC.setLabelLaF(jpDetail.jtVer1, false);
    if (!entryPointDetailDone)
      EntryPointDetail(mode);
    if (mode == 'N') {
      match.init(getDetailSet(), 'N', oznval, devind && !devizni);
      saveRow = null;
      if (jpDetail.rcVRDOK.getSelectedIndex() != 2)
        jpDetail.getFirstFocusableComponent().requestFocus();
      else {
        //if (frmParam.getParam("gk", "lvIzvod", "D", "Prenijeti opis i vrstu
        // dokumenta na slij.novu stavku izvoda").equals("D")) {
        if (getDetailSet().getString("OPIS").equals("")) {
          jpDetail.jraOpis.requestFocus();
        } else {
          jpDetail.kcGroup.getJlrBROJKONTA().requestFocus();
        }
      }
    } else if (mode == 'I') {
      match.init(getDetailSet(), 'I', oznval, devind && !devizni);
      saveRow = new DataRow(getDetailSet(), checkCols);
      vrdokSel = jpDetail.rcVRDOK.getSelectedIndex();
      dM.copyColumns(getDetailSet(), saveRow, checkCols);
      if (isSkDokum()) {
        jpDetail.getFirstFocusableComponent().requestFocus();
      } else {
        jpDetail.jraDatdok.requestFocus();
      }
      if (!isLastIzvod()) {
        rcc.setLabelLaF(jpDetail.jraId,false);
        rcc.setLabelLaF(jpDetail.jraIp,false);
        rcc.setLabelLaF(jpDetail.jraPvid,false);
        rcc.setLabelLaF(jpDetail.jraPvip,false);
      }
    } else {
      findComboVrdok();
    }
    jpDetail.jpDevI.setFokus(devind && !devizni ? 'I' : mode);
    if (mode != 'B') checkCopyEnabled();
    entryPointDetailDone = false;
  }
  
  private boolean rowChanged() {
    if (saveRow == null) return false;
    String id = devind ? "DEVID" : "ID";
    String ip = devind ? "DEVIP" : "IP";
    return saveRow.getBigDecimal(id).compareTo(getDetailSet().getBigDecimal(id)) != 0 ||
           saveRow.getBigDecimal(ip).compareTo(getDetailSet().getBigDecimal(ip)) != 0 ||
           vrdokSel != jpDetail.rcVRDOK.getSelectedIndex() ||
           !saveRow.getString("BROJKONTA").equals(getDetailSet().getString("BROJKONTA")) 
           || saveRow.getInt("CPAR") != getDetailSet().getInt("CPAR");
  }

  private String delPokQuery;

  public boolean DeleteCheckDetail() {
    knjizenje.getFNalozi().loadStavka();
    // ab.f pokrivanje
    delPokQuery = match.getDeleteQuery(getDetailSet());
    return true;
  }

  public void AfterDeleteDetail() {
    knjizenje.getFNalozi().delStavka();
    updStatus();
    // ab.f brisanje eventualnog radnog pokrivanja
    if (delPokQuery != null)
      vl.runSQL(delPokQuery);
  }

  public boolean DeleteCheckMaster() {
    if (getMasterSet().getString("STATUS").equals("K")) {
      JOptionPane.showMessageDialog(raMaster,
          "Brisanje proknjiženog izvoda nije moguæe!", "Greška",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    int lastrbrIzv = gkQuerys.getNextBrojIzv(getMasterSet().getString("ZIRO"), 
        getMasterSet().getString("GOD")) - 1;
//    System.out.println("lastrbrIzv = " + lastrbrIzv);
//    System.out.println("getMasterSet().BROJIZV = "
//        + getMasterSet().getInt("BROJIZV"));
    if (getMasterSet().getInt("BROJIZV") != lastrbrIzv) {
      JOptionPane.showMessageDialog(raMaster,
          "Moguæe je brisati samo zadnji izvod!", "Greška",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    knjizenje.getFNalozi().jpMaster.jpBrNal.initJP(knjizenje.getFNalozi()
        .getMasterSet());
    isLastNalog = knjizenje.getFNalozi().jpMaster.jpBrNal.isLastNalog();
//    System.out.println("isLast? = " + isLastNalog);
    if (!isLastNalog) {
      if (askObr("Zaglavlje izvoda nije moguæe obrisati! Obrisati stavke?",
          false) == JOptionPane.YES_OPTION) {
        delAllDetailSet();
      }
      return false;
    }
    return true; //deleteIzvodovNalog() u before ili afterdelete;
  }

  public boolean PorukaDeleteMaster() {
    return raMaster
        .PorukaDelete("Brisanjem izvoda obrisat æe se i njegove stavke! Nastaviti?");
  }

  public void AfterDeleteMaster() {
    deleteIzvodovNalog();
  }

  boolean isLastNalog = false;

  void deleteIzvodovNalog() {
    if (isLastNalog) {
      knjizenje.getFNalozi().raMaster.LegalDelete(false, false);
    }
  }

  //  void postStavkaNaloga() {
  //
  //    knjizenje.getFNalozi().prepareForSaveStavka();
  //
  //    knjizenje.getFNalozi().updStavka();
  //
  //  }
  boolean nullit = false;

  private boolean isDuplicateBrojdok() {
    if (frmParam.getParam("gk", "unqBRDStIzvod", "D", "Provjera duplikata broja dokumenta kod snimanja svake stavke salda konti izvoda (D/N)")
        .equalsIgnoreCase("N")) return false;
    if (nullit) {
      nullit = false;
      getDetailSet().setAssignedNull("KNJIG");
    }
    if (Gkstavkerad.getDataModule().getRowCount(
        Condition.whereAllEqual(raKnjizenjeSK.skKeyNames, getDetailSet())) > 0) {
      nullit = true;
      jpDetail.jraBrojdok.requestFocus();
      JOptionPane
          .showMessageDialog(
              raDetail.getWindow(),
              "Uplata s istim brojem za istog partnera veæ postoji na ovom izvodu!",
              "Greška", JOptionPane.ERROR_MESSAGE);
      return true;
    } else
      return false;
  }
  
  public void checkCopyEnabled() {
    jpDetail.jbCopyVals.setEnabled(lastCpar != null && raDetail.getMode() == 'N' &&
        jpDetail.kcGroup.getJlrBROJKONTA().isEnabled());
  }
  
  public void copyPrevious() {
    if (lastCpar == null) return;
    getDetailSet().setString("VRDOK", lastVRDOK);
    getDetailSet().setString("POKRIVENO", lastPok);
    findComboVrdok();
    jpDetail.jlrCpar.setText(lastCpar);
    jpDetail.jlrCpar.forceFocLost();
    jpDetail.kcGroup.getJlrBROJKONTA().setText(lastKonto);
    jpDetail.kcGroup.getJlrBROJKONTA().forceFocLost();
    jpDetail.kcGroup.getJlrCORG().setText(lastCorg);
    jpDetail.kcGroup.getJlrCORG().forceFocLost();
    getDetailSet().setString("OPIS", lastOPIS);
    checkCopyEnabled();
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jpDetail.kcGroup.getJlrBROJKONTA()) ||
        vl.isEmpty(jpDetail.kcGroup.getJlrCORG())) {
      return false;
    }
    if (isSkDokum())
      if (vl.isEmpty(jpDetail.jlrCpar))
        return false;
    if (isSkDokum())
      if (vl.isEmpty(jpDetail.jraBrojdok))
        return false;
    getDetailSet().setString("POKRIVENO", "N");
    if (jpDetail.rcVRDOK.getSelectedIndex() == 0) {
      getDetailSet().setString("VRDOK", 
          raKonta.isKupac(getDetailSet().getString("BROJKONTA")) ? "UPL" : "IPL");
    } else if (jpDetail.rcVRDOK.getSelectedIndex() == 1) {
      getDetailSet().setString("VRDOK", 
          raKonta.isKupac(getDetailSet().getString("BROJKONTA")) ? "OKK" : "OKD");
    } else if (jpDetail.rcVRDOK.getSelectedIndex() == 3) {
      getDetailSet().setString("VRDOK", 
          raKonta.isKupac(getDetailSet().getString("BROJKONTA")) ? "OKK" : "OKD");
      getDetailSet().setString("POKRIVENO", "K");
    } else
      getDetailSet().setString("VRDOK", "");
    knjizenje.getFNalozi().prepareForSaveStavka();
    if (mode == 'N') {
      if (isSkDokum()) {
        nullit = false;
        if (isDuplicateBrojdok()) {
          nullit = false;
          return false;
        }
      }
      // hendlanje pokrivanja (ab.f)
      vl.execSQL("SELECT MAX(rbsrac) as maxr FROM gkstavkerad WHERE cnaloga='"
          + getMasterSet().getString("CNALOGA") + "'");
      vl.RezSet.open();
      getDetailSet().setInt("RBSRAC", vl.RezSet.getInt("MAXR") + 1);
    } else if (mode == 'I') {
    }
    if (!valiDateIDIP()) return false;
    lastOPIS = getDetailSet().getString("OPIS");
    lastVRDOK = getDetailSet().getString("VRDOK");
    lastPok = getDetailSet().getString("POKRIVENO");
    lastCpar = jpDetail.jlrCpar.getText();
    lastKonto = jpDetail.kcGroup.getJlrBROJKONTA().getText();
    lastCorg = jpDetail.kcGroup.getJlrCORG().getText();
    if (!jpDetail.jpDevI.validate(mode)) return false;
    
    //  ako se promijenila stavka, ponisti pokrivanje
    if (rowChanged() && match.getPokriveni() != null) 
      match.getPokriveni().deleteAllRows();

    saveRow = null;
    return true;
    //    postStavkaNaloga();
    //return true;
  }

  /**
   * Kontrolira da li je upisan id ILI ip
   * @return
   */
  private boolean valiDateIDIP() {
    BigDecimal id = getDetailSet().getBigDecimal("ID");
    BigDecimal ip = getDetailSet().getBigDecimal("IP");    
    String messg = "";
    if (id.signum() == 0 && ip.signum() == 0) {
      messg = messg + "Nije unesen niti jedan iznos! ";
    }
    if (promet.equals("D") || jpDetail.rcVRDOK.getSelectedIndex() != 2) {
      if (id.signum() != 0 && ip.signum() != 0) {
        messg = messg + "Smije biti unesen ili dugovni ili potražni iznos! ";
      }
    }
    if ((id.signum() < 0 || ip.signum() < 0) &&
        jpDetail.rcVRDOK.getSelectedIndex() == 0) {
      messg = "Uplata ne može biti negativna! (Izaberite vrstu dokumenta K.O.)";
    }
    if (!messg.equals("")) {
      JOptionPane.showMessageDialog(jpDetail,messg, "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public boolean doWithSaveDetail(char mode) {
    if (mode != 'B') {
      knjizenje.getFNalozi().updStavka();
    }
    return true;
  }

  private boolean changeDatumKnjizenjaMade = false;

  public boolean doWithSaveMaster(char mode) {
    changeDatumKnjizenjaMade = false;
    if (mode == 'I') {
      if (knjizenje.getFNalozi().getMasterSet().getTimestamp("DATUMKNJ")
          .compareTo(jpMaster.datknjset.getTimestamp("DATUMKNJ")) != 0) {
//        System.out.println("idem u changeDatumKnjizenja");
        String ret = frmNalozi.changeDatumKnjizenja(getMasterSet().getString(
            "CNALOGA"), jpMaster.datknjset.getTimestamp("DATUMKNJ"), true);
        if (!ret.equals("")) {
//          System.out.println("changeDatumKnjizenja: " + ret);
          return false;
        }
        changeDatumKnjizenjaMade = true;
      }
    }
    return true;
  }

  public void AfterAfterSaveMaster(char mode) {
    if (changeDatumKnjizenjaMade)
      getDetailSet().refresh();
    super.AfterAfterSaveMaster(mode);
  }

  //  public void AfterSaveFailedDetail(char mode) {
  //    if (mode != 'B') {
  //      knjizenje.getFNalozi().getMasterSet().refresh();
  //      cnaloga="";
  //      posNalozi();
  //    }
  //  }
  private void findComboVrdok() {
    if (getMasterSet().getString("STATUS").equals("K"))
      return;
    String vd = getDetailSet().getString("VRDOK");
    if (getDetailSet().getString("POKRIVENO").equals("K"))
      jpDetail.rcVRDOK.setSelectedIndex(3);
    else if (vd.equals("IPL") || vd.equals("UPL") || vd.equals("KUP"))
      jpDetail.rcVRDOK.setSelectedIndex(0);
    else if (vd.equals("OKK") || vd.equals("OKD") || vd.equals("KOB"))
      jpDetail.rcVRDOK.setSelectedIndex(1);
    else
      jpDetail.rcVRDOK.setSelectedIndex(2);
  }

  public void AfterSaveDetail(char mode) {
    knjizenje.getFNalozi().getMasterSet().saveChanges();
    updStatus();
    // ab.f - snimanje eventualnog radnog pokrivanja b
    match.saveChanges(mode);
  }

  void updStatus() {
    updStatus(true);
  }
	boolean isNegateFixEnabled() {
	  return frmParam.getParam("gk", "izvnegatefix","D","Ako se pojavi negativni iznos kod izvoda da li da ga prebaci na drugu stranu u pozitivu")
	  .equals("D");
	}

  BigDecimal[] getSumIDIP(DataSet ds) {
    BigDecimal stvID = new BigDecimal(0);
    BigDecimal stvIP = new BigDecimal(0);
    ds.enableDataSetEvents(false);
    for (ds.first(); ds.inBounds(); ds.next()) {
      if (ds.getString("VRDOK").startsWith("OK") && isNegateFixEnabled()) {
        if (ds.getBigDecimal("ID").signum() < 0) {
          stvIP = stvIP.add(ds.getBigDecimal("ID").negate());
        } else if (ds.getBigDecimal("IP").signum() < 0) {
          stvID = stvID.add(ds.getBigDecimal("IP").negate());
        } else {
          stvID = stvID.add(ds.getBigDecimal("ID"));
          stvIP = stvIP.add(ds.getBigDecimal("IP"));
        }
      } else {
        stvID = stvID.add(ds.getBigDecimal("ID"));
        stvIP = stvIP.add(ds.getBigDecimal("IP"));
      }
    }
    ds.enableDataSetEvents(true);
    return new BigDecimal[] { stvID, stvIP };
  }

  void updStatus(boolean saveChanges) {
    if (!raDetail.isVisible()) {
//      System.out.println("refiltering for updating status ...");
      refilterDetailSet();
    }
    raDetail.getJpTableView().Zbrajalo();
    String oldStatus = getMasterSet().getString("STATUS");
    if (oldStatus.equals("K"))
      return;
    if (getDetailSet().getRowCount() == 0)
      return;
    /* OKD i OKP ako idu u negativu prebaciti na drugu stranu u pozitivu */
    //BigDecimal stvID = raDetail.getJpTableView().getSum("ID");
    //BigDecimal stvIP = raDetail.getJpTableView().getSum("IP");
    BigDecimal[] stvIDIP = getSumIDIP(getDetailSet());
    BigDecimal stvID = stvIDIP[0];
    BigDecimal stvIP = stvIDIP[1];
    BigDecimal izvID = getMasterSet().getBigDecimal("ID");
    BigDecimal izvIP = getMasterSet().getBigDecimal("IP");
    String newStatus = oldStatus;
//	System.out.println("stvID " + stvID);
//    System.out.println("stvIP " + stvIP);
//    System.out.println("izvID " + izvID);
//    System.out.println("izvIP " + izvIP);
//    System.out.println("getMasterSet().getInt(BROJSTAVKI) "
//        + getMasterSet().getInt("BROJSTAVKI"));
//    System.out.println("getDetailSet().getRowCount() "
//        + getDetailSet().getRowCount());
    if (dontCheckAnything()) {
      getMasterSet().setInt("BROJSTAVKI", getDetailSet().getRowCount());
      getMasterSet().setBigDecimal("ID", izvID = stvID);
      getMasterSet().setBigDecimal("IP", izvIP = stvIP);
      focLostIdIp();
      oldStatus = "";
    }
    if (stvID.compareTo(izvID) == 0 && stvIP.compareTo(izvIP) == 0
        && getMasterSet().getInt("BROJSTAVKI") == getDetailSet().getRowCount()) {
      newStatus = "S";
    } else {
      newStatus = "N";
    }
    if (promet.equals("D")) {
      if (Aus.sum("ID", getDetailSet(), "POKRIVENO", "K").compareTo(
          Aus.sum("IP", getDetailSet(), "POKRIVENO", "K")) != 0)
        newStatus = "N";
    }
    if (!promet.equals("D")) {
      if (stvID.compareTo(stvIP) != 0)
        newStatus = "N";
    }
    if (!oldStatus.equals(newStatus)) {
      getMasterSet().setString("STATUS", newStatus);
      if (saveChanges) {
        getMasterSet().saveChanges();
        raMaster.getJpTableView().fireTableDataChanged();
      }
    }
  }

  public static boolean dontCheckAnything() {
    return frmParam.getParam("gk", "izvodCheck", "D",
        "Kontrolirati broj stavaka i sume pri unosu izvoda (D/N)").equals("N");
  }

  String lastCpar, lastKonto, lastCorg;
  public void beforeShowDetail() {
    //
    lastVRDOK = "IPL";
    lastCpar = null;
  }

  public void beforeShowMaster() {
    getZiroParams();
    setTecajVisible();
    setDevIznosVisible();
    setNaslovMaster("Izvodi - " + idizvod);
    oldstatus = "X";
  }

  private void setDevIznosVisible() {
    boolean devivisible = !devizni && devind;
    jpDetail.jpDevI.setVisible(devivisible);
    jpDetail.lay.setHeight(devind ? 310 : 280);
  }
  
  /*private void setDevButtonVisible() {
    boolean butvis = !devizni && !devind;
    jpDetail.jbDeviz.setVisible(butvis);
    if (butvis) {
      jpDetail.lay.setHeight(300);
    }
  }*/

  //  public void tabStateChangedDetail(int idx) {
  //  //tako da moze istovremeno brijati u Nalozima
  //    if (idx == 0) rmvSaldaKontaFilter();
  //    else if (idx == 1) addSaldaKontaFilter();
  //  }
  //  public boolean ValidacijaPrijeIzlazaDetail() {
  //  //za svaki slucaj
  //    rmvSaldaKontaFilter();
  //    return true;
  //  }
  boolean isSkDokum() {
    if (getDetailSet().hasColumn("VRDOK") == null)
      return false;
    String vrdok = getDetailSet().getString("VRDOK");
    return !vrdok.equals("");
  }

  private void initKnjizenjeSet() {
    Column cCVRNAL = (Column) dm.getNalozi().getColumn("CVRNAL").clone();
    Column cDATUMKNJ = (Column) dm.getNalozi().getColumn("DATUMKNJ").clone();
    knjizenjeSet.setColumns(new Column[] { cCVRNAL, cDATUMKNJ });
  }

  private void fillKnjizenjeSet() {
    knjizenjeSet.empty();
    knjizenjeSet.open();
    knjizenjeSet.insertRow(false);
    if (!((presIzvodi) getPreSelect()).findCVRNAL())
      return;
    String cvrnal = dm.getZirorn().getString("CVRNAL");
    java.sql.Timestamp datumknj = jpMaster.datknjset.getTimestamp("DATUMKNJ");
    knjizenjeSet.setString("CVRNAL", cvrnal);
    knjizenjeSet.setTimestamp("DATUMKNJ", datumknj);
    knjizenjeSet.post();
  }

  void setScrKonto(String brKonta) {
    if (isKO()) {
      setIdIp(true, true);//ako je obavijest o knjizenju oslobodi oba iznosa za unos
    } else {
	    if (raKonta.isSaldak(brKonta)) {
	      setIdIp(raKonta.isDobavljac(brKonta), raKonta.isKupac(brKonta));
	    } else {
	      setIdIp(raKonta.isDugovni(brKonta), raKonta.isPotrazni(brKonta));
	    }
    }
    checkCopyEnabled();
  }

  /**
   * @return radi li se o knjiznoj obavijesti (odabran index 1 na rcVRDOK)
   */
  private boolean isKO() {
    return jpDetail.rcVRDOK.getSelectedIndex() == 1;
  }

  void setIdIp(boolean enid, boolean enip) {
    rcc.setLabelLaF(jpDetail.jraId, enid);
    rcc.setLabelLaF(jpDetail.jraIp, enip);
    rcc.setLabelLaF(jpDetail.jraPvid, enid);
    rcc.setLabelLaF(jpDetail.jraPvip, enip);
    if (enip)
      jpDetail.kcGroup.setNextComponent(jpDetail.jraIp);
    if (enid)
      jpDetail.kcGroup.setNextComponent(jpDetail.jraId);
  }

  void setVRDOK() {
    jpDetail.kcGroup.getJlrBROJKONTA().getRaDataSet().refilter();
    rcc.setLabelLaF(jpDetail.jlrZr, isSkDokum() || isPartnerOnFinKonta());
    rcc.setLabelLaF(jpDetail.jlrCpar, isSkDokum() || isPartnerOnFinKonta());
    rcc.setLabelLaF(jpDetail.jlrNazpar, isSkDokum() || isPartnerOnFinKonta());
    rcc.setLabelLaF(jpDetail.jraBrojdok, isSkDokum());
    rcc.setLabelLaF(jpDetail.jraExtbrojdok, isSkDokum());
    rcc.setLabelLaF(jpDetail.jbSelZr, isSkDokum() || isPartnerOnFinKonta());
  }

  /**
   * @return
   */
  private boolean isPartnerOnFinKonta() {
    return frmParam.getParam("gk","cpar-fkonta","N","Da li se unosi partner na financijska konta kod izvoda (D/N)?").equals("D");
  }

  void setOpis(boolean always) {
    if (vl.chkIsEmpty(jpDetail.jraBrojdok))
      return;
    if (!always && !vl.chkIsEmpty(jpDetail.jraOpis))
      return;
    String vrdok = getDetailSet().getString("VRDOK");
    if (vrdok.equals(""))
      return;
    String brojdok = getDetailSet().getString("BROJDOK");
    String opis = "";
    if (jpDetail.rcVRDOK.getSelectedIndex() == 3)
      opis = "Kompenzacija po ".concat(brojdok);
    else if (vrdok.equals("KOB") || vrdok.equals("OKK") || vrdok.equals("OKD")) {
      opis = "Knjižna obavijest za ".concat(brojdok);
    } else if (vrdok.equals("KUP") || vrdok.equals("UPL")
        || vrdok.equals("IPL")) {
      opis = "Uplata ".concat(brojdok);
    }
    getDetailSet().setString("OPIS", opis);
  }

  void addSaldaKontaFilter() {
    jpDetail.kcGroup.getJlrBROJKONTA().setAdditionalLookupFilter(skFilter);
    jpDetail.kcGroup.getJlrNAZIVKONTA().setAdditionalLookupFilter(skFilter);
  }

  void rmvSaldaKontaFilter() {
    jpDetail.kcGroup.getJlrBROJKONTA().setAdditionalLookupFilter(null);
    jpDetail.kcGroup.getJlrNAZIVKONTA().setAdditionalLookupFilter(null);
  }

  void addZiroFilter() {
    if (isZiroReq()) {
	    jpDetail.jlrCpar.setAdditionalLookupFilter(ziroFilter);
	    jpDetail.jlrNazpar.setAdditionalLookupFilter(ziroFilter);
	    jpDetail.jlrZr.setAdditionalLookupFilter(ziroFilter);
    }
  }

  void detEscape_action() {
    if (raDetail.getMode() != 'B'
        && !jpDetail.kcGroup.getJlrBROJKONTA().isEnabled()) {
      jpDetail.kcGroup.clrBROJKONTA();
      jpDetail.kcGroup.setEnabledKonto(true);
      jpDetail.kcGroup.setEnabledCorg(false);
      setIdIp(false, false);
      getDetailSet().setBigDecimal("ID", nula);
      getDetailSet().setBigDecimal("IP", nula);
      getDetailSet().setBigDecimal("PVID", nula);
      getDetailSet().setBigDecimal("PVIP", nula);
      jpDetail.kcGroup.getJlrBROJKONTA().requestFocus();
      checkCopyEnabled();
    } else {
      raDetail.getOKpanel().jPrekid_actionPerformed();
    }
  }
  
  void calcTecaj(String dev, String kn) {
    if (getDetailSet().getBigDecimal(dev).signum() != 0 &&
        getDetailSet().getBigDecimal(kn).signum() != 0) {
      BigDecimal jedval = raSaldaKonti.getJedVal(oznval);
      getDetailSet().setBigDecimal("TECAJ", 
          getDetailSet().getBigDecimal(kn).multiply(jedval).divide(
              getDetailSet().getBigDecimal(dev), 6, BigDecimal.ROUND_HALF_UP));
    }
  }

  public boolean validateSelectionMaster(ReadRow row) {
    if (!row.getString("STATUS").equals("S")) {
      JOptionPane.showMessageDialog(raMaster,
          "Moguæe je odabrati samo izvode spremne za obradu!", "Greška",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  void masovnaObradaIzvoda() {
    if (raMaster.getSelectionTracker().countSelected() == 0) {
      JOptionPane.showMessageDialog(raMaster,
          "Nema odabranih izvoda za obradu! Odaberite ih tipkom ENTER!",
          "Obavijest", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    getZiroParams();
    if (askObr("Obraditi sve neobraðene i odabrane izvode na ekranu ?", true) != JOptionPane.YES_OPTION)
      return;
    boolean pokriv = jcbPokriv.isSelected();
    //    raMaster.getJpTableView().enableEvents(false);
    int lastRow = getMasterSet().getRow();
    raMaster.getJpTableView().enableEvents(false);
    getMasterSet().first();
    do {
      String status = getMasterSet().getString("STATUS");
      if (raMaster.getSelectionTracker().isSelected(getMasterSet())
          && status.equals("S")) {
        refilterDetailSet();
        lastRow = getMasterSet().getRow();
        //        if (!raObrIzvoda.getRaObrIzvoda().obradaIzvoda(this,pokriv)) {
        if (!izvodProcess(pokriv)) {
          JOptionPane.showMessageDialog(raMaster, "Izvod " + ziro + " / "
              + oznval + " - " + getMasterSet().getInt("BROJIZV")
              + " neuspješno obraðen! Prekidam!",
              "Obrada svih neobraðenih izvoda", JOptionPane.ERROR_MESSAGE);
          break;
        }
        raMaster.getJpTableView().fireTableDataChanged();
      }
    } while (getMasterSet().next());
    raMaster.getSelectionTracker().clearSelection();
    getMasterSet().goToRow(lastRow);
    raMaster.getJpTableView().enableEvents(true);
  }

  void obradaIzvoda() {
    raMaster.getSelectionTracker().removeFromSelection(getMasterSet());
    String status = getMasterSet().getString("STATUS");
    if (status.equals("S")) {
      if (askObrada()) {
        obradiIzvod();
      }
    } else if (status.equals("K")) {
      if (askPonisti()) {
        ponistiIzvod();
      }
    }
    raMaster.getJpTableView().fireTableDataChanged();
    changeDetailViewStatus(getMasterSet().getString("STATUS"));//ai 04.05.05
  }

  void obradiIzvod() {
    refilterDetailSet();
    //    if
    // (raObrIzvoda.getRaObrIzvoda().obradaIzvoda(this,jcbPokriv.isSelected()))
    // {
    if (!izvodProcess(jcbPokriv.isSelected())) {
      //JOptionPane.showMessageDialog(raMaster.getWindow(),"Obrada izvoda
      // uspješno završena!","Obrada izvoda",JOptionPane.INFORMATION_MESSAGE);
      //} else {
      JOptionPane.showMessageDialog(raMaster.getWindow(),
          "Obrada izvoda neuspješna!", "Obrada izvoda",
          JOptionPane.ERROR_MESSAGE);
    }
    if (raKnjizenjeSK.getRaKnjizenjeSK().hasErrors()) {
      raKnjizenjeSK.getRaKnjizenjeSK().showErrors();
    }
  }

  private boolean izvodProcessResult = true;

  boolean izvodProcess(final boolean pokriv) {
    String mess = "Obrada izvoda " + ziro + " / " + oznval + " - "
        + getMasterSet().getInt("BROJIZV");
    java.lang.Runnable izvodRunner = new Runnable() {
      public void run() {
        izvodProcessResult = raObrIzvoda.getRaObrIzvoda().obradaIzvoda(
            frmIzvodi.this, pokriv);
        if (!izvodProcessResult)
          frmIzvodi.this.getDetailSet().refresh();
      }
    };
    raProcess.runChild(raMaster, "Obrada izvoda", mess, izvodRunner);
    return izvodProcessResult;
  }

  public boolean ValidacijaPrijeIzlazaMaster() {
    //    ((frmGK)frmGK.getStartFrame()).closeIzvod();
    cnaloga = "";
    frmGK.getFrmGK().closeIzvod();
    return true;
  }

  void ponistiIzvod() {
    raObrNaloga.getRaObrNaloga().hasSkStavke(getMasterSet().getString("CNALOGA")); //{kontrola ima li skstavaka koji imaju cgkstavke like cnaloga% + parametar?
    Thread t = 
      new Thread() {
        public void run() {
          if (getKnjizenje().getFNalozi().obrNaloga_inThread(true, false, false, false)) {
//          azuriraj status na izvodu
            vl.runSQL("UPDATE gkstavkerad SET BROJIZV = " + getMasterSet().getInt("BROJIZV")+
                    " where cnaloga='"+getMasterSet().getString("CNALOGA")+"'");
            getMasterSet().setString("STATUS","S");
            getMasterSet().saveChanges();
            raMaster.getJpTableView().fireTableDataChanged();
          }
        }
      };
      t.start();
      
 /*   } else {
      JOptionPane.showMessageDialog(raMaster.getWindow(),
          "Rasknjižavanje izvoda nije moguæe!", "Obrada izvoda",
          JOptionPane.ERROR_MESSAGE);
    }*/
  }

  boolean askObrada() {
    getZiroParams();
    int odg = askObr("Obraditi izvod " + ziro + " / " + oznval + " - "
        + getMasterSet().getInt("BROJIZV") + "?", true);
    return odg == JOptionPane.YES_OPTION;
  }

  boolean askPonisti() {
    int odg = askObr("Izvod " + ziro + " / " + oznval + " - "
        + getMasterSet().getInt("BROJIZV")
        + " je obra\u0111en! Poništiti obradu izvoda?", false);
    return odg == JOptionPane.YES_OPTION;
  }

  int askObr(String txt, boolean showpanel) {
    Object mess;
    if (showpanel) {
      if (jpMessage == null)
        initJpMessage();
      jlMessage.setText(txt);
      mess = jpMessage;
    } else {
      mess = txt;
    }
    return JOptionPane.showConfirmDialog(raMaster.getWindow(), mess, "Obrada",
        JOptionPane.YES_NO_OPTION);
  }

  void initJpMessage() {
    jpMessage = new JPanel(new BorderLayout());
    jpMessage.add(jlMessage, BorderLayout.CENTER);
    jpMessage.add(jcbPokriv, BorderLayout.SOUTH);
    //    jpMessage.setPreferredSize(new Dimension(-1,50));
  }

  private void jbInit() throws Exception {
    initKnjizenjeSet();
    this.setMasterSet(dm.getIzvodi());
    Izvodi.getDataModule().setFilter("1=0");
    this.setVisibleColsMaster(new int[] { 2, 5, 6, 8 });
    this.setMasterKey(new String[] { "CNALOGA", "BROJIZV" });
    jpMaster = new jpIzvodiMaster(this);
    this.setJPanelMaster(jpMaster);
    this.setDetailSet(dm.getGkstavkerad());
    Gkstavkerad.getDataModule().setFilter("1=0");
    this.setVisibleColsDetail(visibleColsDetailIzvodi);
    this.setDetailKey(new String[] { "CNALOGA", "BROJIZV", "RBS" });
    jpDetail = new jpIzvodiDetail(this);
    this.setJPanelDetail(jpDetail);
    this.set_kum_detail(true);
    this.stozbrojiti_detail(new String[] { "ID", "IP" });
    setMasterDeleteMode(raMasterDetail.DELDETAIL);
    raMaster.getJpTableView().addTableModifier(new gkStatusColorModifier());
    raDetail.getJpTableView().addTableModifier(
        new hr.restart.swing.raTableColumnModifier("CPAR", new String[] {
            "CPAR", "NAZPAR" }, dm.getPartneri()));
    raDetail.getJpTableView().addTableModifier(
        new hr.restart.swing.raTableColumnModifier("VRDOK",
            new String[] { "NAZDOK" }, dm.getVrdokum()) {
          public int getMaxModifiedTextLength() {
            return 15;
          }

          public void replaceValues() {
            super.replaceValues();
            if (getComponentText().equals(""))
              setComponentText("Financijski");
            else {
              tableDs.getVariant("POKRIVENO", getRow(), shared);
              if (shared.getString().equals("K"))
                setComponentText("Kompenzacija");
            }
          }
        });
    raDetail.getJpTableView().addTableModifier(
        new hr.restart.swing.raTableModifier() {
          Variant v = new Variant();

          public boolean doModify() {
            if (oldstatus.equals("K"))
              return false;
            if (!((JraTable2) getTable()).getDataSetColumn(getColumn())
                .getColumnName().equals("CPAR"))
              return false;
            getDetailSet().getVariant("VRDOK", getRow(), v);
            return v.getString().equals("");
          }

          public void modify() {
            getDetailSet().getVariant("OPIS", getRow(), v);
            if (renderComponent instanceof JLabel)
              ((JLabel) renderComponent).setText("Opis: ".concat(v.toString()));
          }
        });
    raDetail.addKeyAction(new raKeyAction(KeyEvent.VK_ESCAPE, "") {
      public void keyAction() {
        detEscape_action();
      }
    });
    raDetail.addKeyAction(new raKeyAction(KeyEvent.VK_F7, "Kopiraj prethodni") {
      public void keyAction() {
        if (jpDetail.jbCopyVals.isEnabled())
          copyPrevious();
      }
    });
    raDetail.addKeyAction(new raKeyAction(KeyEvent.VK_F6, "Pokrivanje") {
      public void keyAction() {
        if (jpDetail.jlrCpar.getText().length() == 0) {
          jpDetail.jlrCpar.requestFocus();
          JOptionPane.showMessageDialog(raDetail.getWindow(),
              "Obavezan unos PARTNER!", "Greška", JOptionPane.ERROR_MESSAGE);
          return;
        }
        if (!jpDetail.jraBrojdok.maskCheck())
          return;
        if (!jpDetail.jraId.maskCheck() || !jpDetail.jraIp.maskCheck())
          return;
        getDetailSet().setString("POKRIVENO", 
            jpDetail.rcVRDOK.getSelectedIndex() == 3 ? "K" : "N");
        checkPokrivanje();
      }
    });
    raDetail.addOption(new raNavAction("Pokrivanje", raImages.IMGALIGNJUSTIFY,
        KeyEvent.VK_F6) {
      public void actionPerformed(ActionEvent e) {
        if (getDetailSet().getString("VRDOK").length() == 0) {
          JOptionPane.showMessageDialog(raDetail.getWindow(),
              "Stavka nije salda konti!", "Greška", JOptionPane.ERROR_MESSAGE);
          return;
        }
        if ("K".equalsIgnoreCase(getMasterSet().getString("STATUS"))) {
          JOptionPane.showMessageDialog(raDetail.getWindow(),
              "Izvod je proknjižen!", "Greška", JOptionPane.ERROR_MESSAGE);
          return;
        }
        match.init(getDetailSet(), 'I', oznval, devind && !devizni);
        saveRow = new DataRow(getDetailSet(), checkCols);
        vrdokSel = jpDetail.rcVRDOK.getSelectedIndex();
        dM.copyColumns(getDetailSet(), saveRow, checkCols);
        checkPokrivanje();
        if (match.isAccepted())
          match.saveChanges('I');
      }
    }, 5, true);
    raDetail.addOption(new raNavAction("Dodaj iz datoteke", raImages.IMGOPEN, KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        if (getMasterSet().getString("STATUS").equals("K") || getDetailSet().getRowCount() > 0) return;
        IzvodFromFile izf = new IzvodFromFile('I') {
          protected void commitTransfer() {
            commitFromFile(this);
          }
        };
        izf.showInSet();
      }
    }, 6, false);
    raMaster.addOption(rnvObrada, 5, false);
    raMaster.addOption(new raNavAction("Obradi sve odabrane",
        raImages.IMGMOVIE, KeyEvent.VK_F10, KeyEvent.SHIFT_MASK) {
      public void actionPerformed(ActionEvent e) {
        masovnaObradaIzvoda();
      }
    }, 6, false);
    addZiroFilter();
    raMaster.installSelectionTracker("BROJIZV");
  }

  raAdditionalLookupFilter skFilter = new raAdditionalLookupFilter() {
    public boolean isRow(ReadRow row) {
      String saldak = row.getString("SALDAK");
      boolean isSaldak = saldak.equals("D") || saldak.equals("K");
      boolean isRow = isSkDokum() ? isSaldak : !isSaldak;
      return isRow;
    }
  };

  raAdditionalLookupFilter ziroFilter = new raAdditionalLookupFilter() {
    public boolean isRow(ReadRow row) {
      return oznval.equals(row.getString("OZNVAL"));
    }
  };

  void checkPokrivanje() {
    // ako se promijenila stavka, ponisti pokrivanje
    if (rowChanged())
      match.getPokriveni().deleteAllRows();
    
    match.show(raDetail.getWindow());
    if (!match.isAccepted()) {
      match.getPokriveni().deleteAllRows();
      return;
    }
    if (raDetail.getMode() == 'N') {
      DataSet st = match.getFirstSelectedRow();
      if (st == null)
        return;
      //      if (jpDetail.jraBrojdok.getText().length() == 0) {
      if (match.getPokriveni().rowCount() == 1) {
        getDetailSet().setString("BROJDOK",
            match.getFirstSelectedRow().getString("BROJDOK"));
        getDetailSet().setString("EXTBRDOK",
            match.getFirstSelectedRow().getString("EXTBRDOK"));
        boolean always = "D".equals(frmParam.getParam("gk", "setOpisForced", "N",
            "Forsirati generiranje opisa kod pokrivanja na izvodu (D,N)"));
        setOpis(always);
      }
      //      }
      if (jpDetail.kcGroup.getJlrBROJKONTA().getText().length() == 0) {
        jpDetail.kcGroup.getJlrBROJKONTA().setText(st.getString("BROJKONTA"));
        jpDetail.kcGroup.getJlrBROJKONTA().forceFocLost();
      }
      jpDetail.kcGroup.getJlrCORG().setText(st.getString("CORG"));
      jpDetail.kcGroup.getJlrCORG().forceFocLost();

      if (raSaldaKonti.isDomVal(st)) {
        if (getDetailSet().getBigDecimal("IP").signum() == 0
            && getDetailSet().getBigDecimal("ID").signum() == 0) {
          getDetailSet().setBigDecimal(
              raKonta.isDobavljac(st.getString("BROJKONTA")) ? "ID" : "IP",
              match.getTotalPokriveno());
        }
        getDetailSet().setString("OZNVAL", "");
        getDetailSet().setBigDecimal("DEVID", Aus.zero2);
        getDetailSet().setBigDecimal("DEVIP", Aus.zero2);
        getDetailSet().setBigDecimal("TECAJ", Aus.zero0);
        jpDetail.jpDevI.fillValues();
      } else {
        getDetailSet().setString("OZNVAL", st.getString("OZNVAL"));
        if (getDetailSet().getBigDecimal("IP").signum() == 0
            && getDetailSet().getBigDecimal("ID").signum() == 0) {
          getDetailSet().setBigDecimal(
              raKonta.isDobavljac(st.getString("BROJKONTA")) ? "ID" : "IP",
              match.getTotalPokrivenoPv());
          getDetailSet().setBigDecimal(
              raKonta.isDobavljac(st.getString("BROJKONTA")) ? "DEVID" : "DEVIP",
              match.getTotalPokriveno());
          jpDetail.jpDevI.fillValues();
          jpDetail.jpDevI.setPVval(match.getTotalPokrivenoPv());
        }
      }
      if (match.getPokriveni().rowCount() != 1)
        jpDetail.jraBrojdok.requestFocusLater();
      else jpDetail.jraOpis.requestFocusLater();
    }
    if (saveRow == null)
      saveRow = new DataRow(getDetailSet(), checkCols);
    vrdokSel = jpDetail.rcVRDOK.getSelectedIndex();
    dM.copyColumns(getDetailSet(), saveRow, checkCols);
  }

  private boolean isZiroReq() {
    return frmParam.getParam("gk","zrreqizv","D","Da li se na unosu izvoda na dohvatu partnera pojavljuju samo partneri sa unesenim žiro raèunom")
    .equals("D");
  }
  
  QueryDataSet getParIzvodSet() {
    if (isZiroReq()) {
      return gkQuerys.getParZiro();
    } else {
      return dm.getPartneri();
    }
  }

  /**
   * @return
   */
  String getZiroColumn() {
    return isZiroReq()?"ZIRO":"ZR";
  }
  
  int[] getParVisCols() {
    if (isZiroReq()) {
      return new int[] { 0, 3, 4 };
    } else {
      return new int[] { 0, 1, 2 };
    }
  }

  void bsh() {
    String in = "";
    QueryDataSet
    //in = "";
    qds = Util.getNewQueryDataSet("SELECT distinct cnaloga FROM Gkstavke, Konta WHERE konta.saldak in ('D','K') and gkstavke.brojkonta = konta.brojkonta and gkstavke.god='2005' and gkstavke.cvrnal!='00'",true);
    for (qds.first(); qds.inBounds(); qds.next()) {
      if (!hr.restart.gk.raObrNaloga.getRaObrNaloga().hasSkStavke(qds.getString("CNALOGA"))) {
        in = in + "'"+qds.getString("CNALOGA")+"',";
      }
    }
    
    System.out.println("SELECT * FROM gkstavkerad where CNALOGA in ("+
        in.substring(0,in.length()-1)+")");
  }
  //  public void detailViewShownDetail(char mod)
  //  {
  //    jpDetail.setPanelsVisible(1);
  //  }
  private void commitFromFile(IzvodFromFile izf) {
    QueryDataSet gks = izf.convertToGkStavke();
    QueryDataSet cpartneri = Partneri.getDataModule().getTempSet();
    cpartneri.open();
    posNalozi();
    BigDecimal tid = Aus.zero2, tip = Aus.zero2;
    int brojstavki = 0;//getDetailSet().getRowCount();
    int error = 0;
    for (gks.first(); gks.inBounds(); gks.next()) {
      int rbs = gks.getInt("RBS");
      //KNJIG, GOD, CVRNAL, RBR, DATUMKNJ, (DATDOSP=DATDOK), BROJIZV, GODMJ, TECAJ=1.0, CNALOGA
//      gks.setString("KNJIG", getMasterSet().getString("KNJIG"));
//      gks.setString("GOD", getMasterSet().getString("GOD"));
//      gks.setString("CVRNAL", knjizenjeSet.getString("CVRNAL"));
//      gks.setInt("RBR", getKnjizenje().getFNalozi().getMasterSet().getInt("RBR"));
//      gks.setTimestamp("DATUMKNJ", knjizenjeSet.getTimestamp("DATUMKNJ"));
//      
      knjizenje.getFNalozi().prepareForSaveStavka(gks);
      gks.setTimestamp("DATDOSP", gks.getTimestamp("DATDOK"));
      gks.setInt("BROJIZV", getMasterSet().getInt("BROJIZV"));
      gks.setBigDecimal("TECAJ", Aus.one0);
      gks.setString("CNALOGA", getMasterSet().getString("CNALOGA"));
      gks.setString("POKRIVENO", "N");
      gks.setInt("RBS", rbs);
      tid = tid.add(gks.getBigDecimal("ID"));
      tip = tip.add(gks.getBigDecimal("IP"));
      //check
      if (raKonta.isSaldak(gks.getString("BROJKONTA"))) {
        if (!lookupData.getlookupData().raLocate(cpartneri, "CPAR", gks.getInt("CPAR")+"")) {
          error++;
        }
        if (gks.getString("BROJDOK").trim().length() == 0) error ++;
        if (gks.getString("VRDOK").trim().length() == 0) error ++;
      }
      gks.post();
      brojstavki ++;
      System.out.println(gks);
    }
    if (error > 0) {
      JOptionPane.showMessageDialog(null, "Postoji "+error+" nepotpunih podataka. Molim ispravite ih dodavajuæi žiro raèune partnerima ili veze poziv - konto ili žiro - konto");
      return;
    }
    /** @TODO azurirati izvod, nalog i sve u transakciji s: */
    getMasterSet().setInt("BROJSTAVKI", brojstavki);
    getMasterSet().setBigDecimal("ID", tid);
    getMasterSet().setBigDecimal("IP", tip);
    getMasterSet().setBigDecimal("NOVOSTANJE", getExpectedNovoStanje());

    getKnjizenje().getFNalozi().getMasterSet().setBigDecimal("ID", tid);
    getKnjizenje().getFNalozi().getMasterSet().setBigDecimal("IP", tip);
    getKnjizenje().getFNalozi().getMasterSet().setBigDecimal("SALDO", tid.subtract(tip));
    
    raTransaction.saveChangesInTransaction(new QueryDataSet[] {gks, getMasterSet(), getKnjizenje().getFNalozi().getMasterSet()});
    updStatus();
    //raDetail.refreshTable();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        raDetail.getJpTableView().fireTableDataChanged();
        raMaster.getJpTableView().fireTableDataChanged();
      }
    });
  }
}