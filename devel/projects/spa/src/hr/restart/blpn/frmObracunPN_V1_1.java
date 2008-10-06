/****license*****************************************************************
**   file: frmObracunPN_V1_1.java
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
package hr.restart.blpn;

import hr.restart.baza.Stavkepn;
import hr.restart.baza.dM;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raKeyAction;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;
import hr.restart.util.raTransaction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmObracunPN_V1_1 extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  sgStuff ss = sgStuff.getStugg();
  Util ut = Util.getUtil();

  jpPrijavaPN jpMaster;
  jpObracunPNDetail_V1_1 jpDetail;

  StorageDataSet ukupnjaci = new StorageDataSet();
  StorageDataSet satiOdlDol = new StorageDataSet();
  QueryDataSet iznosi;
  QueryDataSet masterSet  = new QueryDataSet();
  QueryDataSet detailSet  = new QueryDataSet();

  short tmpRBS;

  int dani;
  int sati;
  int pozicijaUredu = -3;

  BigDecimal tmpTroskovi;
  BigDecimal iznosZaKorekciju;

  String previousTab = "";
  String oznval;
  String tmpVal="";

  Column satOdl = new Column();
  Column satDol = new Column();
  Column minOdl = new Column();
  Column minDol = new Column();

  static frmObracunPN_V1_1 fopnNV;

  raNavAction toRemove0 = raMaster.getNavBar().getNavContainer().getNavActions()[0];
  raNavAction toRemove1 = raMaster.getNavBar().getNavContainer().getNavActions()[1];
  raNavAction toRemove2 = raMaster.getNavBar().getNavContainer().getNavActions()[2];

  raNavAction rnvObracunGumbic = new raNavAction("Obra\u010Dun/poništavanje obraèuna putnog naloga", raImages.IMGIMPORT, KeyEvent.VK_F10) {
    public void actionPerformed(ActionEvent e) {
      obracunNaloga();
      dM.getSynchronizer().markAsDirty("PutniNalog_Radnici");
    }
  };

  raNavAction rnvArhiviranjeGumbic = new raNavAction("Arhiviranje putnog naloga", raImages.IMGHISTORY, KeyEvent.VK_F11) {
    public void actionPerformed(ActionEvent e) {
      arhiviranjeNaloga();
      dM.getSynchronizer().markAsDirty("PutniNalog_Radnici");
    }
  };

  raKeyAction rkaDetEscape = new raKeyAction(KeyEvent.VK_ESCAPE,"Ponovni izbor stavke troška") {
    public void keyAction() {
      detEscape_action();
    }
  };

  raKeyAction rkaDetF5 = new raKeyAction(KeyEvent.VK_F5,"Unos dnevnica") {
    public void keyAction() {
      posljediceJP01();
    }
  };

  raKeyAction rkaDetF6 = new raKeyAction(KeyEvent.VK_F6,"Unos prijevoznih troškova") {
    public void keyAction() {
      posljediceJP02();
    }
  };

  raKeyAction rkaDetF7 = new raKeyAction(KeyEvent.VK_F7,"Unos troškova no\u0107enja") {
    public void keyAction() {
      posljediceJP03();
    }
  };

  raKeyAction rkaDetF8 = new raKeyAction(KeyEvent.VK_F8,"Unos ostalih troškova") {
    public void keyAction() {
      posljediceJP04();
    }
  };

  public static frmObracunPN_V1_1 getInstance(){
    return fopnNV;
  }

  public frmObracunPN_V1_1() {
    super(2,1);
    try {
      fopnNV = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  
    
  public void EntryPointDetail(char mode){
    rcc.EnabDisabAll(jpDetail.jpDetail00, false);
    if (mode == 'N') {
      if (this.getMasterSet().getString("STATUS").equals("O")){
        JOptionPane.showMessageDialog(getJPanelMaster(),
                                      new hr.restart.swing.raMultiLineMessage(new String[] {"Putni nalog je ve\u0107 obra\u0111en","Nemogu\u0107a je daljnja izmjena"}),
                                      "Upozorenje!",
                                      JOptionPane.ERROR_MESSAGE);
      }
      if(!jpDetail.jTabed.isEnabledAt(0)) jpDetail.jTabed.setEnabledAt(0, true);
      jpDetail.jTabed.setSelectedIndex(0);
      disableAll();

      this.getDetailSet().setString("CZEMLJE", this.getMasterSet().getString("CZEMLJE"));
      setValuta();

      prepareIznQDS(this.getMasterSet().getString("CZEMLJE"));

      rcc.setLabelLaF(jpDetail.jbGetDnevnice, true);
      rcc.setLabelLaF(jpDetail.jbGetPutTrosk, true);
      rcc.setLabelLaF(jpDetail.jbGetNocenja, true);
      rcc.setLabelLaF(jpDetail.jbGetOstalo, true);
      
      enabDisabUppMid(true);
//      rcc.setLabelLaF(jpDetail.jraDatObr, false);
    }
    if (mode == 'I') {

      int stoSadOtvorit = Integer.parseInt(this.getDetailSet().getString("CSKL"));
      enabDisabUppMid(false);

      switch (stoSadOtvorit) {
        case 2:
          jpDetail.addD01();
          getVrimenaFromDetail();
          if(!jpDetail.jTabed.isEnabledAt(1)) jpDetail.jTabed.setEnabledAt(1, true);
          jpDetail.jTabed.setSelectedIndex(1);
          disableAll();
          rcc.setLabelLaF(jpDetail.jraBrSati, false);
          rcc.setLabelLaF(jpDetail.jraBrojDNK, false);
          break;
        case 3:
          jpDetail.addD02();
          if(!jpDetail.jTabed.isEnabledAt(2)) jpDetail.jTabed.setEnabledAt(2, true);
          jpDetail.jTabed.setSelectedIndex(2);
          disableAll();
          break;
        case 4:
          if(!jpDetail.jTabed.isEnabledAt(3)) jpDetail.jTabed.setEnabledAt(3, true);
          jpDetail.jTabed.setSelectedIndex(3);
          disableAll();
          break;
        case 5:
          if(!jpDetail.jTabed.isEnabledAt(4)) jpDetail.jTabed.setEnabledAt(4, true);
          jpDetail.jTabed.setSelectedIndex(4);
          disableAll();
          break;
      }
    }
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {

      this.getDetailSet().setString("CZEMLJE", this.getMasterSet().getString("CZEMLJE"));
      setValuta();

//      jpDetail.jlrCzemlje.requestFocus();
//      jpDetail.jlrCzemlje.forceFocLost();

      jpDetail.jraDatObr.requestFocus();
      jpDetail.jraDatObr.selectAll();
    } else if (mode == 'I') {
      int stoSadOtvorit = Integer.parseInt(this.getDetailSet().getString("CSKL"));

      switch (stoSadOtvorit) {
        case 2:
          jpDetail.jraDatOdl.requestFocus();
          jpDetail.jraDatOdl.selectAll();
          break;
        case 3:
          jpDetail.jraOdmj.requestFocus();
          jpDetail.jraOdmj.selectAll();
          break;
        case 4:
          jpDetail.jraBrojDNK.requestFocus();
          jpDetail.jraBrojDNK.selectAll();
          break;
        case 5:
          jpDetail.jraIznos.requestFocus();
          jpDetail.jraIznos.selectAll();
          break;
      }
      iznosZaKorekciju = this.getDetailSet().getBigDecimal("PVIZNOS");
    }
  }

  public void beforeShowMaster(){
    manageObradaButton();
    manageArhivaButton();
  }

  public void beforeShowDetail(){
    this.setNaslovDetail("Stavke obra\u010Duna putnog naloga broj ".concat(this.getMasterSet().getString("CPN")));
    if (getMasterSet().isAssignedNull("DATOBR") || getMasterSet().isUnassignedNull("DATOBR")) {
      this.getMasterSet().setTimestamp("DATOBR", getPreSelect().getSelRow().getTimestamp("DATUMODL-to"));
    }
    changeUkupnoSet();
    if (this.getMasterSet().getString("STATUS").equals("O") || this.getMasterSet().getString("STATUS").equals("I")){
      raDetail.setEnabledNavAction(raDetail.getNavBar().getNavContainer().getNavActions()[0],false);
      raDetail.setEnabledNavAction(raDetail.getNavBar().getNavContainer().getNavActions()[1],false);
      raDetail.setEnabledNavAction(raDetail.getNavBar().getNavContainer().getNavActions()[2],false);
    } else {
      raDetail.setEnabledNavAction(raDetail.getNavBar().getNavContainer().getNavActions()[0],true);
      raDetail.setEnabledNavAction(raDetail.getNavBar().getNavContainer().getNavActions()[1],true);
      raDetail.setEnabledNavAction(raDetail.getNavBar().getNavContainer().getNavActions()[2],true);
    }
  }

  public boolean ValidacijaDetail(char mode) {
    if (jpDetail.jTabed.getSelectedIndex() == 0) {
      raDetail.getOKpanel().jPrekid_actionPerformed();
      return false;
    }
    if (mode == 'I'){
      if(!jpDetail.jTabed.isEnabledAt(0)) jpDetail.jTabed.setEnabledAt(0, true);
      jpDetail.jTabed.setSelectedIndex(0);
      disableAll();
    }

    if (mode == 'N'){
      tmpVal = jpDetail.jlrVal.getText();
    }

    switch (jpDetail.jTabed.getSelectedIndex()){
      case 1: this.getDetailSet().setString("CSKL", "2");
        if (vl.isEmpty(jpDetail.jlrStavka)) {
          jpDetail.jlrStavka.requestFocus();
          return false;
        }
        break;
      case 2: this.getDetailSet().setString("CSKL", "3");
        if (vl.isEmpty(jpDetail.jlrStavka)) {
          jpDetail.jlrStavka.requestFocus();
          return false;
        }
        break;
      case 3: this.getDetailSet().setString("CSKL", "4");
        if (vl.isEmpty(jpDetail.jlrStavka)) {
          jpDetail.jlrStavka.requestFocus();
          return false;
        }
        break;
      case 4: this.getDetailSet().setString("CSKL", "5");
        if (vl.isEmpty(jpDetail.jlrStavka)) {
          jpDetail.jlrStavka.requestFocus();
          return false;
        }
        break;
    }
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(this.getDetailSet());

    if (!isDomestic(this.getDetailSet().getString("CZEMLJE"))) {
      String valuta = ss.getOZNVAL(this.getDetailSet().getString("CZEMLJE"));
      if (!valuta.equals(this.getDetailSet().getString("OZNVAL"))){
        valuta = this.getDetailSet().getString("OZNVAL");
      }
      this.getDetailSet().setBigDecimal("TECAJ",ss.getTECAJ(valuta,this.getMasterSet().getTimestamp("DATOBR")));
      this.getDetailSet().setBigDecimal("PVIZNOS", ss.currrencyConverterToKN(this.getDetailSet().getBigDecimal("IZNOS"),
          valuta, this.getMasterSet().getTimestamp("DATOBR")));
    } else {
      String valuta = ss.getOZNVAL(this.getDetailSet().getString("CZEMLJE"));
      if (!valuta.equals(this.getDetailSet().getString("OZNVAL"))){
        valuta = this.getDetailSet().getString("OZNVAL");
        this.getDetailSet().setBigDecimal("TECAJ",ss.getTECAJ(valuta,this.getMasterSet().getTimestamp("DATOBR")));
        this.getDetailSet().setBigDecimal("PVIZNOS",
            ss.currrencyConverterToKN(this.getDetailSet().getBigDecimal("IZNOS"),
            valuta,
            this.getMasterSet().getTimestamp("DATOBR")));
      } else {
        this.getDetailSet().setBigDecimal("TECAJ", new BigDecimal(1));
        this.getDetailSet().setBigDecimal("PVIZNOS", this.getDetailSet().getBigDecimal("IZNOS"));
      }
    }
//    syst.prn(this.getDetailSet());
//    if (mode == 'N') ss.updatePutniNalogObracunUnos(this.getDetailSet().getString("CPN"), this.getDetailSet().getBigDecimal("PVIZNOS"));
//    else ss.updatePutniNalogObracunIzmjena(this.getDetailSet().getString("CPN"), iznosZaKorekciju, this.getDetailSet().getBigDecimal("PVIZNOS"));
    return true;
  }

  protected boolean isDomestic(String czem){
    oznval = ss.getOZNVAL(czem);
    if (oznval.equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())) return true;
    return false;
  }

  public void AfterCancelDetail(){
    previousTab = "";
    if(!jpDetail.jTabed.isEnabledAt(0)) jpDetail.jTabed.setEnabledAt(0, true);
    jpDetail.jTabed.setSelectedIndex(0);
    disableAll();
  }

  public boolean DeleteCheckDetail(){
    /** @todo samo tako :) */
    try {
      tmpRBS = this.getDetailSet().getShort("RBS");
      tmpTroskovi = this.getDetailSet().getBigDecimal("PVIZNOS");
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    return true;
  }

  public void AfterDeleteDetail(){
    try {
      vl.recountDataSet(raDetail, "RBS", tmpRBS);
      ss.updatePutniNalogObracunBrisanjeStavke(this.getMasterSet().getString("CPN"), tmpTroskovi);
      changeUkupnoSet();
    }
    catch (Exception ex) {
    }
  }

//  public boolean doWithSaveDetail(char mode){
//    this.getMasterSet().setTimestamp("DATOBR", vl.getToday());
//    return raTransaction.saveChangesInTransaction(new QueryDataSet[] {this.getMasterSet()});
//  }

  public void AfterSaveDetail(char mode){
    changeUkupnoSet();
    if (raDetail.getMode()=='N' && jpDetail.jTabed.getSelectedIndex() != 0) {
      rcc.EnabDisabAll(jpDetail.jpMidel, true);
      jpDetail.jlrVal.setText(tmpVal);
      jpDetail.jTabed.setEnabledAt(0, true);
      jpDetail.jTabed.setSelectedIndex(0);
      lookupData.getlookupData().raLocate(ukupnjaci, "VALUTA", tmpVal);
      disableAll();

      jpDetail.jlrCzemlje.requestFocus();
      jpDetail.jlrCzemlje.selectAll();
    }
System.out.println("AfterSaveDetail.DATOBR = "+getMasterSet().getTimestamp("DATOBR"));
    if (mode == 'N') ss.updatePutniNalogObracunUnos(this.getDetailSet().getString("CPN"), this.getDetailSet().getBigDecimal("PVIZNOS"), getMasterSet().getTimestamp("DATOBR"));
    else ss.updatePutniNalogObracunIzmjena(this.getDetailSet().getString("CPN"), iznosZaKorekciju, this.getDetailSet().getBigDecimal("PVIZNOS"), getMasterSet().getTimestamp("DATOBR"));
  }

  public void detailSet_navigated(com.borland.dx.dataset.NavigationEvent e){
    navigationDogadjaj();
  }


  public void masterSet_navigated(com.borland.dx.dataset.NavigationEvent e){
    if (this.getMasterSet().getRow() != pozicijaUredu){
      pozicijaUredu = this.getMasterSet().getRow();
      this.setNaslovDetail("Stavke obra\u010Duna putnog naloga broj ".concat(this.getMasterSet().getString("CPN")));
      renewUkupnoSet();
      manageObradaButton();
      manageArhivaButton();
    }
  }

  private void navigationDogadjaj() {
    String currTab;
    currTab = this.getDetailSet().getString("CSKL");
    if (!currTab.equals(previousTab)){
      previousTab = currTab;
      try {
        int ditektTab = Integer.parseInt(this.getDetailSet().getString("CSKL"));
        int inejblTab = 0;
        switch (ditektTab) {
          case 2:
            inejblTab = 1;
            getVrimenaFromDetail();
            jpDetail.addD01();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                jpDetail.jlrStavka.forceFocLost();
              }
            });
            break;
          case 3:
            inejblTab = 2;
            jpDetail.addD02();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                jpDetail.jlrSifPrSredstva.forceFocLost();
                jpDetail.jlrStavka.forceFocLost();
              }
            });
            break;
          case 4:
            inejblTab = 3;
            jpDetail.addD03();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                jpDetail.jlrStavka.forceFocLost();
              }
            });
            break;
          case 5:
            inejblTab = 4;
            jpDetail.addD04();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                jpDetail.jlrStavka.forceFocLost();
              }
            });
            break;
        }
        jpDetail.jTabed.setEnabledAt(inejblTab, true);
        jpDetail.jTabed.setSelectedIndex(inejblTab);
        disableRest(0);
      } catch (NumberFormatException ex) {}
    }
  }

  protected void jbInit() throws Exception {
//    System.out.println(vl.countAllTables());
    this.setMasterSet(dm.getPutniNalog());
    this.setNaslovMaster("Obra\u010Dun putnog naloga");
    this.setVisibleColsMaster(new int[] {1, 3, 4, 5, 12, 18});
    this.setMasterKey(new String[] {"KNJIG", "GODINA", "BROJ", "INDPUTA"});
    jpMaster = new jpPrijavaPN(this.raMaster);
    this.setJPanelMaster(jpMaster);

    this.setDetailSet(dm.getStavkepn());
    this.setVisibleColsDetail(new int[] {3, 5, 7, 10});
    this.setDetailKey(new String[] {"KNJIG", "GODINA", "BROJ", "INDPUTA", "RBS"});
    jpDetail = new jpObracunPNDetail_V1_1(this);
    this.setJPanelDetail(jpDetail);

    raMaster.getJpTableView().addTableModifier(
        new hr.restart.swing.raTableColumnModifier(
        "CRADNIK",
        new String[] {"CRADNIK","IME","PREZIME"},
        dm.getRadnici()
        ));
    raDetail.getJpTableView().addTableModifier(
        new hr.restart.swing.raTableColumnModifier(
        "CSKL",
        new String[] {"CVRSK","OPISVRSK"},
        new String[] {"CSKL"},
        new String[] {"CVRSK"},
        ss.getVrshemekZaPN()
        ));
//    this.raMaster.getRepRunner().clearAllReports();
    this.raMaster.getRepRunner().addReport("hr.restart.blpn.repPutniRacun","Putni ra\u010Dun",2);
    this.raMaster.getRepRunner().addReport("hr.restart.blpn.repPutniRacunPV","Putni ra\u010Dun - protuvrijednost",2);
    this.raDetail.getRepRunner().addReport("hr.restart.blpn.repPutniRacun","Putni ra\u010Dun",2);
    this.raDetail.getRepRunner().addReport("hr.restart.blpn.repPutniRacunPV","Putni ra\u010Dun - protuvrijednost",2);
    setTimeStorige();
    setSplashStorige();
    raMaster.getNavBar().getNavContainer().remove(toRemove0);
    raMaster.getNavBar().getNavContainer().remove(toRemove1);
    raMaster.getNavBar().getNavContainer().remove(toRemove2);
    this.raMaster.addOption(rnvObracunGumbic, 2);
    this.raMaster.addOption(rnvArhiviranjeGumbic, 3);
    raDetail.addKeyAction(rkaDetEscape);
    raDetail.addKeyAction(rkaDetF5);
    raDetail.addKeyAction(rkaDetF6);
    raDetail.addKeyAction(rkaDetF7);
    raDetail.addKeyAction(rkaDetF8);
//    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
//      public void knjigChanged(String oldKnjig, String newKnjig) {
////        System.out.println("knjigovodstvo schangano!");
////        jpDetail.jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
//      };
//    });
  }

  public QueryDataSet getRepQDS_stavke(){
    refilterDetailSet();
    detailSet.setSort(new SortDescriptor(new String[] {"OZNVAL"}));
    return detailSet;
  }

  public QueryDataSet getRepQDS_zaglavlje(){
    return masterSet;
  }

  public DataSet getRepQDS_ukupno(){
    ukupnjaci.setSort(new SortDescriptor(new String[] {"VALUTA"}));
    return ukupnjaci;
  }

  protected void setSplashStorige(){
    ukupnjaci.setColumns(new Column[] {
      dm.createBigDecimalColumn("UKUDNEV"),
      dm.createBigDecimalColumn("UKUDNEVPV"),
      dm.createBigDecimalColumn("UKUPUTR"),
      dm.createBigDecimalColumn("UKUPUTRPV"),
      dm.createBigDecimalColumn("UKUNOCE"),
      dm.createBigDecimalColumn("UKUNOCEPV"),
      dm.createBigDecimalColumn("UKUREST"),
      dm.createBigDecimalColumn("UKURESTPV"),
      dm.createBigDecimalColumn("UKUSUKU"),
      dm.createBigDecimalColumn("UKUSUKUPV"),
      dm.createStringColumn("VALUTA", 3)
    });
  }

  protected void setTimeStorige(){
    satOdl = dm.createIntColumn("SATODL", "Vrijeme odlaska - sati");
    satOdl.setDisplayMask("00");

    satDol = dm.createIntColumn("SATDOL", "Vrijeme dolaska - sati");
    satDol.setDisplayMask("00");

    minOdl = dm.createIntColumn("MINODL", "Vrijeme odlaska - minuta");
    minOdl.setDisplayMask("00");

    minDol = dm.createIntColumn("MINDOL", "Vrijeme odlaska - minuta");
    minDol.setDisplayMask("00");

    satiOdlDol.setColumns(new Column[] { satOdl,satDol,minOdl,minDol});
  }

  protected void changeUkupnoSet(){
    try {
      ukupnjaci.first();
      boolean imaVal = false; // = lookupData.getlookupData().raLocate(ukupnjaci, "VALUTA", this.getDetailSet().getString("OZNVAL"));
      //false;
      do {
        if (ukupnjaci.getString("VALUTA").equals(this.getDetailSet().getString("OZNVAL"))) {
          imaVal = true;
          setInfoPanel();
          break;
        }
      } while (ukupnjaci.next());

      if (!imaVal){
        ukupnjaci.insertRow(false);
        setInfoPanel();
      }
    }
    catch (Exception ex) {
    }
  }

  protected void setInfoPanel(){
    try {
      String knjig = this.getMasterSet().getString("KNJIG");
      short god = this.getMasterSet().getShort("GODINA");
      int broj = this.getMasterSet().getInt("BROJ");
      String valuta = this.getDetailSet().getString("OZNVAL");
      String indputa = this.getMasterSet().getString("INDPUTA");

      if (!ukupnjaci.isOpen()) ukupnjaci.open();

      ukupnjaci.setString("VALUTA", valuta);
      ukupnjaci.setBigDecimal("UKUDNEV", ss.getUkupnoDnevnice(knjig, god, broj, valuta, indputa) );
      ukupnjaci.setBigDecimal("UKUPUTR", ss.getUkupnoPutTros(knjig, god, broj, valuta, indputa)  );
      ukupnjaci.setBigDecimal("UKUNOCE", ss.getUkupnoNocenja(knjig, god, broj, valuta, indputa)  );
      ukupnjaci.setBigDecimal("UKUREST", ss.getUkupnoOstalo(knjig, god, broj, valuta, indputa)   );
      ukupnjaci.setBigDecimal("UKUSUKU", ss.getUkupnoSveukupno(knjig, god, broj, valuta, indputa));

//      System.out.println("valuta " + valuta);

      if (!valuta.equals("")){
        ukupnjaci.setBigDecimal("UKUDNEVPV", ss.currrencyConverterToKN(ukupnjaci.getBigDecimal("UKUDNEV"), valuta, getMasterSet().getTimestamp("DATOBR")));
        ukupnjaci.setBigDecimal("UKUPUTRPV", ss.currrencyConverterToKN(ukupnjaci.getBigDecimal("UKUPUTR"), valuta, getMasterSet().getTimestamp("DATOBR")));
        ukupnjaci.setBigDecimal("UKUNOCEPV", ss.currrencyConverterToKN(ukupnjaci.getBigDecimal("UKUNOCE"), valuta, getMasterSet().getTimestamp("DATOBR")));
        ukupnjaci.setBigDecimal("UKURESTPV", ss.currrencyConverterToKN(ukupnjaci.getBigDecimal("UKUREST"), valuta, getMasterSet().getTimestamp("DATOBR")));
        ukupnjaci.setBigDecimal("UKUSUKUPV", ss.currrencyConverterToKN(ukupnjaci.getBigDecimal("UKUSUKU"), valuta, getMasterSet().getTimestamp("DATOBR")));
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void calculateSate(){
    setVrimenaOdlDol(satiOdlDol.getInt("SATODL"),satiOdlDol.getInt("MINODL"), satiOdlDol.getInt("SATDOL"), satiOdlDol.getInt("MINDOL"));
    dateDiff(this.getDetailSet().getTimestamp("DATUMDOL"),
             this.getDetailSet().getTimestamp("DATUMODL"));
    int brSati = getDana()*24+getSati();
    this.getDetailSet().setInt("BROJSATI", brSati);
    calculateDnevnice();
  }

  protected void getVrimenaFromDetail(){
    satiOdlDol.setInt("SATODL",getSat(this.getDetailSet().getShort("VRIJODL")));
    satiOdlDol.setInt("MINODL",getMin(this.getDetailSet().getShort("VRIJODL")));
    satiOdlDol.setInt("SATDOL",getSat(this.getDetailSet().getShort("VRIJDOL")));
    satiOdlDol.setInt("MINDOL",getMin(this.getDetailSet().getShort("VRIJDOL")));
  }

  protected void setVrimenaOdlDol(int hOdl, int mOdl, int hDol, int mDol){
    String hmOdl = hOdl + ":" + mOdl;
    String hmDol = hDol + ":" + mDol;

    Timestamp dodl = Timestamp.valueOf(this.getDetailSet().getTimestamp("DATUMODL").toString().substring(0,11).concat(hmOdl).concat(":00.0"));
    Timestamp ddol = Timestamp.valueOf(this.getDetailSet().getTimestamp("DATUMDOL").toString().substring(0,11).concat(hmDol).concat(":00.0"));

    this.getDetailSet().setTimestamp("DATUMODL", dodl);
    this.getDetailSet().setTimestamp("DATUMDOL", ddol);

    this.getDetailSet().setShort("VRIJODL", getVrime(hOdl,mOdl));
    this.getDetailSet().setShort("VRIJDOL", getVrime(hDol,mDol));
  }

  public void dateDiff(Timestamp t1, Timestamp t2) {
    int diff = (int)(t1.getTime()-t2.getTime());
    dani = diff/1000/3600/24;
    sati = diff/1000/3600 - dani*24;
  }
  public int getDana(){
    return dani;
  }
  public int getSati(){
    return sati;
  }

  public int getSat(short vrime){
    try {
      String vri = Short.toString(vrime);
      String sat = vri.substring(0,(vri.length()-2));
      return Integer.parseInt(sat);
    } catch (Exception e) {
      return 0;
    }
  }

  public int getMin(short vrime){
    try {
      String vri = Short.toString(vrime);
      String min = vri.substring((vri.length()-2),vri.length());
      return Integer.parseInt(min);
    } catch (Exception e) {
      return 0;
    }
  }

  public boolean validSat(int sat){
    if (sat < 0 || sat >23) return false;
    return true;
  }

  public boolean validMin(int min){
    if (min<0 || min>59) return false;
    return true;
  }

  public void calculateDnevnice(){
    int brdnevnica = this.getDetailSet().getInt("BROJSATI") / 24;
    BigDecimal brDnev = new BigDecimal(brdnevnica);

    int ostataksati = this.getDetailSet().getInt("BROJSATI") - brdnevnica * 24;
    if (ostataksati >= 12) brDnev = brDnev.add(new BigDecimal(1));
    else if (ostataksati >= 8) brDnev = brDnev.add(new BigDecimal(0.5));

    this.getDetailSet().setBigDecimal("BROJDNK", brDnev);
    prepareIznQDS(this.getMasterSet().getString("CZEMLJE"));
    this.getDetailSet().setBigDecimal("IZNOS", (iznosi.getBigDecimal("DNEVNICA")).multiply(brDnev));
    rcc.setLabelLaF(jpDetail.jraBrojDNK, false);
    rcc.setLabelLaF(jpDetail.jraBrSati, false);
  }

  public void calculateIznosDnev(){
    try {
      BigDecimal unoDnevnica = iznosi.getBigDecimal("DNEVNICA");
      BigDecimal bd = this.getDetailSet().getBigDecimal("BROJDNK");
      BigDecimal iznos = unoDnevnica.multiply(bd);
      this.getDetailSet().setBigDecimal("IZNOS", iznos);
    }
    catch (Exception ex) {
    }
  }

  private short getVrime(int sat, int min){
    String saat = Integer.toString(sat);
    String miin = Integer.toString(min);
    if(min <10) miin = "0".concat(miin);
    String sve = saat.concat(miin);
    return Short.parseShort(sve);
  }

  protected void prepareIznQDS(String czem){

    iznosi = new QueryDataSet();
    iznosi.setColumns(dm.getZemlje().cloneColumns());
    iznosi = ss.DNKL(czem);

    if (!iznosi.getString("OZNVAL").equals(this.getDetailSet().getString("OZNVAL"))){
      iznosi.setBigDecimal("DNEVNICA", ss.currencyConverter(iznosi.getBigDecimal("DNEVNICA"),
          iznosi.getString("OZNVAL"),
          this.getDetailSet().getString("OZNVAL"),
          this.getMasterSet().getTimestamp("DATOBR")));
      iznosi.setBigDecimal("NOCENJE", ss.currencyConverter(iznosi.getBigDecimal("NOCENJE"),
          iznosi.getString("OZNVAL"),
          this.getDetailSet().getString("OZNVAL"),
          this.getMasterSet().getTimestamp("DATOBR")));
      iznosi.setBigDecimal("LOCO", ss.currencyConverter(iznosi.getBigDecimal("LOCO"),
          iznosi.getString("OZNVAL"),
          this.getDetailSet().getString("OZNVAL"),
          this.getMasterSet().getTimestamp("DATOBR")));
      iznosi.setBigDecimal("LITBENZ", ss.currencyConverter(iznosi.getBigDecimal("LITBENZ"),
          iznosi.getString("OZNVAL"),
          this.getDetailSet().getString("OZNVAL"),
          this.getMasterSet().getTimestamp("DATOBR")));
    }

    jpDetail.jraJednaDnev.setColumnName("DNEVNICA");
    jpDetail.jraJednaDnev.setDataSet(iznosi);
  }

  protected void enabDisabUppMid(boolean oculi){
    rcc.EnabDisabAll(jpDetail.jpUpper, oculi);
    rcc.EnabDisabAll(jpDetail.jpMidel, oculi);
  }

  protected void disableAll(){
    disableRest(6);
  }

  protected void disableRest(int except){
    for (int i=0; i<5; i++){
      if (i != except) {
        if (i != jpDetail.jTabed.getSelectedIndex()) jpDetail.jTabed.setEnabledAt(i, false);
      }
    }
  }

  protected void setValuta(){
    this.getDetailSet().setString("OZNVAL", ss.getOZNVAL(this.getMasterSet().getString("CZEMLJE")));
  }

  protected void prepareAndGo() {
    prepareKey();
    prepareVrimenaOdlDol();
    prepareAllSame();
  }

  private void prepareKey() {
    String knjig = this.getMasterSet().getString("KNJIG");
    String indputa = this.getMasterSet().getString("INDPUTA");
    short godina = this.getMasterSet().getShort("GODINA");
    int broj = this.getMasterSet().getInt("BROJ");
    this.getDetailSet().setString("KNJIG", knjig);
    this.getDetailSet().setShort("GODINA", godina);
    this.getDetailSet().setInt("BROJ", broj);
    this.getDetailSet().setString("INDPUTA", indputa);
    this.getDetailSet().setShort("RBS", ss.getNextRBSstavkepn(knjig, godina, broj, indputa));
  }

  private void prepareVrimenaOdlDol() {

    this.getDetailSet().setTimestamp("DATUMODL", this.getMasterSet().getTimestamp("DATUMODL"));
    this.getDetailSet().setTimestamp("DATUMDOL", hr.restart.util.Util.getUtil().addDays(this.getMasterSet().getTimestamp("DATUMODL"),(int)this.getMasterSet().getShort("TRAJANJE")-1));
    setVrimenaOdlDol(8,0,20,0);

    this.getDetailSet().setShort("VRIJODL", (short)800);
    this.getDetailSet().setShort("VRIJDOL", (short)2000);

    getVrimenaFromDetail();
  }

  private void prepareAllSame(){
    this.getDetailSet().setString("VRDOK","PN");
    this.getDetailSet().setString("CPN", this.getMasterSet().getString("CPN"));
  }

  private void setPrijevoznoSredstvo(){
    this.getDetailSet().setString("CPRIJSRED", this.getMasterSet().getString("CPRIJSRED"));
    jpDetail.jlrSifPrSredstva.requestFocus();
    jpDetail.jlrSifPrSredstva.forceFocLost();
  }

  public void setBrojKmLitBenz(){
    BigDecimal brojKm = this.getDetailSet().getBigDecimal("BROJDNK");
    prepareIznQDS(this.getMasterSet().getString("CZEMLJE"));
    this.getDetailSet().setBigDecimal("IZNOS", (iznosi.getBigDecimal("LOCO")).multiply(brojKm));
  }

  public void setBrojNocenja(String brn){
    BigDecimal brNocenja;
    if (brn.equals("")) brNocenja = new BigDecimal(this.getMasterSet().getShort("TRAJANJE")).subtract(new BigDecimal(1));
    else brNocenja = this.getDetailSet().getBigDecimal("BROJDNK");

    this.getDetailSet().setBigDecimal("BROJDNK", brNocenja);
    prepareIznQDS(this.getMasterSet().getString("CZEMLJE"));
    this.getDetailSet().setBigDecimal("IZNOS", (iznosi.getBigDecimal("NOCENJE")).multiply(brNocenja));
  }

  protected void posljediceJP01() {
    jpDetail.addD01();
    prepareAndGo();
    enabDisabUppMid(false);
    calculateSate();
    jpDetail.jTabed.setEnabledAt(1, true);
    jpDetail.jTabed.setSelectedIndex(1);
    disableAll();
    if(raDetail.getMode() == 'N') {
      jpDetail.jlrStavka.emptyTextFields();
    }
    jpDetail.jraDatOdl.requestFocus();
  }

  protected void posljediceJP02() {
    jpDetail.addD02();
    prepareAndGo();
    enabDisabUppMid(false);
    jpDetail.jTabed.setEnabledAt(2, true);
    jpDetail.jTabed.setSelectedIndex(2);
    disableAll();
    setPrijevoznoSredstvo();
    getDetailSet().setBigDecimal("BROJDNK", new BigDecimal(0));
    getDetailSet().setBigDecimal("IZNOS", new BigDecimal(0));
    if(raDetail.getMode() == 'N') {
      jpDetail.jlrStavka.emptyTextFields();
    }
    jpDetail.jraOdmj.requestFocus();
  }

  protected void posljediceJP03() {
    jpDetail.addD03();
    prepareAndGo();
    enabDisabUppMid(false);
    setBrojNocenja("");
    jpDetail.jTabed.setEnabledAt(3, true);
    jpDetail.jTabed.setSelectedIndex(3);
    disableAll();
    if(raDetail.getMode() == 'N') {
      jpDetail.jlrStavka.emptyTextFields();
    }
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jpDetail.jraBrojDNK.requestFocus();
      }
    });
  }

  protected void posljediceJP04() {
    jpDetail.addD04();
    prepareAndGo();
    enabDisabUppMid(false);
    getDetailSet().setBigDecimal("IZNOS", new BigDecimal(0));
    jpDetail.jTabed.setEnabledAt(4, true);
    jpDetail.jTabed.setSelectedIndex(4);
    disableAll();
    if(raDetail.getMode() == 'N') {
      jpDetail.jlrStavka.emptyTextFields();
    }
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jpDetail.jraIznos.requestFocus();
      }
    });
  }

  private void manageObradaButton() {
    if (/*this.getMasterSet().getString("STATUS").equals("O") || */this.getMasterSet().getString("STATUS").equals("I")
        || this.getMasterSet().rowCount() == 0){
      raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[2],false);
    } else{
      raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[2],true);
    }
  }

  private void manageArhivaButton() {
    if (this.getMasterSet().getString("STATUS").equals("P") || this.getMasterSet().getString("STATUS").equals("A") || this.getMasterSet().getString("STATUS").equals("O")
        || this.getMasterSet().rowCount() == 0){
      raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[3],false);
    } else{
      raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[3],true);
    }
  }

  void renewUkupnoSet(){
    int row = this.getDetailSet().getRow();
    try {
      if (!ukupnjaci.isOpen()) ukupnjaci.open();

      ukupnjaci.deleteAllRows();

      this.getDetailSet().first();
      String oznval="";
      do {
        if (!this.getDetailSet().getString("OZNVAL").equals(oznval)){
          changeUkupnoSet();
          oznval = this.getDetailSet().getString("OZNVAL");
        }
      } while (this.getDetailSet().next());
      this.getDetailSet().goToRow(row);
    }
    catch (Exception ex) {
    }
  }

  public void Funkcija_ispisa_master(){
    refilterDetailSet();
    renewUkupnoSet();
    Aus.refilter(masterSet, ss.getMasterSetPN(this.getMasterSet().getString("CPN")));
    Aus.setFilter(detailSet, ss.getDetailSetPN(this.getMasterSet().getString("CPN")));    
    detailSet.setColumns(dM.getDataModule().getStavkepn().cloneColumns());
    detailSet.open();
    super.Funkcija_ispisa_master();
  }

  public void Funkcija_ispisa_detail(){
  	Aus.refilter(masterSet, ss.getMasterSetPN(this.getMasterSet().getString("CPN")));
    Aus.setFilter(detailSet, ss.getDetailSetPN(this.getMasterSet().getString("CPN")));    
    detailSet.setColumns(dM.getDataModule().getStavkepn().cloneColumns());
    detailSet.open();
    super.Funkcija_ispisa_detail();
  }

  void checkAkont(){
    this.refilterDetailSet();

    String cpn = this.getDetailSet().getString("CPN");
    String qstr = "select s.oznval as oznval, s.izdatak as izdatak, s.pvizdatak as pvizdatak from stavblag s where s.cpn='" +
                  cpn + "' and s.stavka ='1' and s.cskl = '6' " +
                " UNION " +
                "select a.oznval as oznval, a.izdatak as izdatak, a.pvizdatak as pvizdatak from stavkeblarh a where a.cpn='" +
                  cpn + "' and a.stavka ='1' and a.cskl = '6' " +
                  		"order by 1";

    /** @todo ovo ispod */

    /*
        E sad...
        Provjerit kako stvari stoje kad se akontacija ne isplati u valuti zemlje!!!
    */

    System.out.println("qstr : " + qstr);

//    vl.execSQL(qstr);
//    StorageDataSet uplaceno = vl.RezSet;

    StorageDataSet uplaceno = (StorageDataSet)Util.getNewQueryDataSet(qstr);

    HashMap hm = new HashMap();

    uplaceno.open();

//    if(lookupData.getlookupData().raLocate(uplaceno, "OZNVAL", uplaceno.getString("OZNVAL")))

    if(uplaceno.rowCount() != 0){
      this.getDetailSet().first();
      do {
        if (hm.containsKey(uplaceno.getString("OZNVAL"))){
          BigDecimal velju = (BigDecimal)hm.get(uplaceno.getString("OZNVAL"));
          velju = velju.add(uplaceno.getBigDecimal("IZDATAK"));
          hm.put(uplaceno.getString("OZNVAL"), velju);
        } else {
          hm.put(uplaceno.getString("OZNVAL"), uplaceno.getBigDecimal("IZDATAK"));
        }
      } while (uplaceno.next());
    } else {
      ss.updatePutniNalogAkontacija(this.getDetailSet().getString("CPN"), new BigDecimal(0));
    }

    if (!this.getDetailSet().isEmpty()){

      this.getDetailSet().first();

      do {
//        System.out.println("ima li valute u hm-u " + hm.containsKey(this.getDetailSet().getString("OZNVAL")));
        if (hm.containsKey(this.getDetailSet().getString("OZNVAL"))){

          BigDecimal tlaka = (BigDecimal)hm.get(this.getDetailSet().getString("OZNVAL"));

          if (tlaka.compareTo(this.getDetailSet().getBigDecimal("IZNOS")) >= 0){
            tlaka = tlaka.subtract(this.getDetailSet().getBigDecimal("IZNOS"));
            hm.put(this.getDetailSet().getString("OZNVAL"), tlaka);

            if (new BigDecimal(hm.get(this.getDetailSet().getString("OZNVAL")).toString()).compareTo(new BigDecimal(0)) == 0){
              hm.remove(this.getDetailSet().getString("OZNVAL"));
            }

            this.getDetailSet().setString("ISPL", "T");
          }
        }
      } while (this.getDetailSet().next());

      raTransaction.saveChanges(this.getDetailSet());

      if (ss.brojNeisplacenihStavkiPN(cpn) == 0 && hm.isEmpty()){
        JOptionPane.showMessageDialog(this.raMaster.getWindow(),
                                      new hr.restart.swing.raMultiLineMessage(new String[] {"Putni nalog je ispla\u0107en"}),
                                      "Obavjest", JOptionPane.INFORMATION_MESSAGE);
        ss.calculateTecRazlika(cpn);
        ss.setPutninalogIsplacen(cpn);
        this.raMaster.getRaQueryDataSet().refresh();
      }
    } /*else {
//      System.out.println("diteljset iz empty");
       //@todo something else
    }*/
    manageArhivaButton();
    manageObradaButton();
  }

  void detEscape_action() {
    if (raDetail.getMode()=='N' && jpDetail.jTabed.getSelectedIndex() != 0) {
      rcc.EnabDisabAll(jpDetail.jpMidel, true);
      jpDetail.jlrCzemlje.requestFocus();
    } else {
      raDetail.getOKpanel().jPrekid_actionPerformed();
    }
    jpDetail.jTabed.setEnabledAt(0, true);
    jpDetail.jTabed.setSelectedIndex(0);
    disableAll();
  }

  void obracunNaloga(){
//      System.out.println("Potrebita provjera da li postoje stavke...");
//      
//      System.out.println(this.getMasterSet().getString("CORG"));
//      System.out.println(this.getMasterSet().getShort("GODINA"));
//      System.out.println(this.getMasterSet().getInt("BROJ"));
//      System.out.println(this.getMasterSet().getString("INDPUTA"));
      
  	if (this.getMasterSet().getString("STATUS").equalsIgnoreCase("O")){
  	    
  	    String flag;
  	    
  	    if (lookupData.getlookupData().raLocate(dm.getStavblag(),"CPN",this.getMasterSet().getString("CPN"))){
  	        System.out.println("PN je akontiran");
  	        flag = "A";
  	    } else flag = "P";
  	    
  	    int areUshurePonistavanje = JOptionPane.showConfirmDialog(this.raMaster.getWindow(),
            new raMultiLineMessage(new String[] {"Putni nalog je obraðen","Želite li poništiti obradu?"}),
            "Pozor!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
  	    
  	    if (areUshurePonistavanje == JOptionPane.YES_OPTION){
  	        this.getMasterSet().setString("STATUS", flag);
  	        raTransaction.saveChanges(this.getMasterSet());
  	        raMaster.getJpTableView().fireTableDataChanged();
  	        return;
  	    } else return;
  	}
    
    QueryDataSet checkItOut = Stavkepn.getDataModule().getFilteredDataSet(" knjig='"+this.getMasterSet().getString("CORG")+"' "+
        																						" and godina ="+this.getMasterSet().getShort("GODINA")+" "+
        																						" and broj = "+this.getMasterSet().getInt("BROJ")+" "+
        																						" and indputa = '"+this.getMasterSet().getString("INDPUTA")+"'");
    checkItOut.open();
    if (checkItOut.isEmpty()) {
      JOptionPane.showMessageDialog(this.raMaster.getWindow(), new hr.restart.swing.raMultiLineMessage(new String[]{"Nema stavki", "obrada nije moguæa"}), "Pozor!", JOptionPane.ERROR_MESSAGE);
      return;
    }
      
  	int areUshure = JOptionPane.showConfirmDialog(this.raMaster.getWindow(),
        new raMultiLineMessage(new String[] {"Ova akcija obra\u010Dunava putni nalog","Da li to stvarno želite?"}),
        "Pozor!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (areUshure == JOptionPane.NO_OPTION || areUshure == JOptionPane.CANCEL_OPTION) return;


    this.getMasterSet().setString("STATUS", "O");
    raTransaction.saveChanges(this.getMasterSet());

    checkAkont();

    raDetail.setEnabledNavAction(raDetail.getNavBar().getNavContainer().getNavActions()[0],false);
    raDetail.setEnabledNavAction(raDetail.getNavBar().getNavContainer().getNavActions()[1],false);
    ss.updatePutniNalogObracunObracun(this.getDetailSet().getString("CPN"));
    raMaster.getJpTableView().fireTableDataChanged();
  }

  private void arhiviranjeNaloga(){

    int areUshure = JOptionPane.showConfirmDialog(this.raMaster.getWindow(),
        new hr.restart.swing.raMultiLineMessage(new String[] {"Ova akcija arhivira putni nalog","Da li to stvarno želite?"}),
        "Pozor!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (areUshure == JOptionPane.NO_OPTION) return;


    if (!this.getMasterSet().getString("STATUS").equals("I")){
      JOptionPane.showMessageDialog(this.raMaster.getWindow(),
                                    new hr.restart.swing.raMultiLineMessage(new String[] {"Putni nalog nije ispla\u0107en","arhiviranje nije mogu\u0107e"}),
                                    "Pozor!", JOptionPane.ERROR_MESSAGE);
      return;
    }

    try {
      String[] koloneMaster = new String[]
        {"LOKK", "AKTIV", "KNJIG", "GODINA", "BROJ", "CPN", "CRADNIK", "DATUMODL",
        "DATOBR", "TRAJANJE", "RAZLOGPUTA", "MJESTA", "CPRIJSRED", "INDPUTA", "STATUS", "AKONTACIJA", "TROSKOVI",
        "RAZLIKA", "UPLRAZLIKA", "TECRAZLIKA", "CZEMLJE", "CORG"};

      String[] koloneDitelj = new String[]
        {"LOKK","AKTIV","KNJIG","GODINA","BROJ", "RBS","STAVKA","CSKL","VRDOK",
        "CZEMLJE","IZNOS","PVIZNOS","TECAJ","OZNVAL", "ODMJ","DOMJ","CPRIJSRED",
        "BROJDNK","DATUMODL","DATUMDOL","VRIJODL","VRIJDOL", "BROJSATI","INDPUTA",
        "CPN","ISPL"};

      QueryDataSet dummyDetail = hr.restart.baza.Stavpnarh.getDataModule().getTempSet("1=0");
//      ut.getNewQueryDataSet("select * from stavpnarh where 0=1");
      QueryDataSet dummyMaster = hr.restart.baza.Putnalarh.getDataModule().getTempSet("1=0");
//      ut.getNewQueryDataSet("select * from putnalarh where 0=1");

      dummyDetail.open();
      dummyMaster.open();

      dummyMaster.insertRow(false);
      DataSet.copyTo(koloneMaster, this.getMasterSet(), koloneMaster, dummyMaster);
      
//      dummyMaster.setBigDecimal("TROSKOVI",dummyMaster.getBigDecimal("UPLRAZLIKA").negate());//WTF!?
//      dummyMaster.setBigDecimal("RAZLIKA",dummyMaster.getBigDecimal("UPLRAZLIKA"));

      refilterDetailSet();
      this.getDetailSet().first();

      do {
        dummyDetail.insertRow(false);
        DataSet.copyTo(koloneDitelj, this.getDetailSet(), koloneDitelj, dummyDetail);
      } while (this.getDetailSet().next());

      raMaster.getJpTableView().fireTableDataChanged();
      this.getDetailSet().deleteAllRows();
      this.getMasterSet().deleteRow();
      raTransaction.saveChangesInTransaction(new QueryDataSet[] {this.getMasterSet(), dummyMaster,
                                                                 this.getDetailSet(), dummyDetail});
      raMaster.getJpTableView().fireTableDataChanged();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void knjigIsChanged(){
    this.getPreSelect().getSelRow().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG());
    this.getPreSelect().doSelect();
    this.raMaster.refreshTable();
  }
}