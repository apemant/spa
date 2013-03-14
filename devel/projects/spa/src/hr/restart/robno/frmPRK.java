/****license*****************************************************************
**   file: frmPRK.java
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

import hr.restart.baza.Artrans;
import hr.restart.baza.Condition;
import hr.restart.baza.Stdoku;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raColors;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.LinkClass;
import hr.restart.util.MasterDetailChooser;
import hr.restart.util.VarStr;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raTransaction;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

public class frmPRK extends frmUlazTemplate implements IZavtrHandler {
  hr.restart.robno.jpUlazMaster jpMaster = new hr.restart.robno.jpUlazMaster(this);
  hr.restart.robno.jpUlazDetail jpDetail = new hr.restart.robno.jpUlazDetail(this);
  
  frmVTZtr zt = new frmVTZtr();  // (ab.f)
  frmVTZtrstav zts = new frmVTZtrstav();
	{
		dm.getDokuPRK().open();
		dm.getStdokuPRK().open();
	}
	
   QueryDataSet rotStavkaNew = null;
   QueryDataSet rotStavkaOld = null;
   
   QueryDataSet transStavka = null;
   
   
   raKalkulBDDoc rKD = new raKalkulBDDoc();
   LinkClass lc = LinkClass.getLinkClass();
   
   boolean allowRecalc = frmParam.getParam("robno","recalcPRK","N",
     "Omoguæiti rekalkulaciju primke sa stavkama (D/N)").equals("D");
   
   raNavAction rnvRecalc = new raNavAction("Rekalkulacija stavki",
       raImages.IMGPREFERENCES, java.awt.event.KeyEvent.VK_F7) {
     public void actionPerformed(ActionEvent e) {
       recalcStavke();
     }
   };

   
  public jpUlazDetail getDetailPanel() {
		return jpDetail;
	}

	public jpUlazMaster getMasterPanel() {
		return jpMaster;
	}

	public frmVTZtrstav getZavtrDetail() {
		return zts;
	}

	public frmVTZtr getZavtrMaster() {
		return zt;
	}
	


  public frmPRK() {
    prSTAT='P';
    vrDok="PRK";
    masterTitle="Primke - kalkulacije";
    detailTitle="Stavke primke - kalkulacije";
    jpp=presPRK.getPres();
    setJPanelMaster(jpMaster);
    setJPanelDetail(jpDetail);
    setMasterSet(dm.getDokuPRK());
    setDetailSet(dm.getStdokuPRK());
    zt.setMasterFrame(this);
    zts.setMasterFrame(this);
    jpMaster.setDataSet(getMasterSet());
    jpDetail.setDataSet(getDetailSet(), getMasterSet());
    
    raDetail.addOption(rnvKartica, 4, false);
    if (allowRecalc) raDetail.addOption(rnvRecalc, 4, true);
    
//    raMaster.getRepRunner().addReport("hr.restart.robno.repPriKalk","Primka - kolièinska",2);
//    raDetail.getRepRunner().addReport("hr.restart.robno.repPriKalk","Primka - kolièinska",2);
//    raMaster.getRepRunner().addReport("hr.restart.robno.repPriKalkExtendedVersion","Primka - vrijednosna",2);
//    raDetail.getRepRunner().addReport("hr.restart.robno.repPriKalkExtendedVersion","Primka - vrijednosna",2);
//    raMaster.getRepRunner().addReport("hr.restart.robno.repPrkProvider","Primka - kalkulacija",2);
//    raDetail.getRepRunner().addReport("hr.restart.robno.repPrkProvider","Primka - kalkulacija",2);
    
    raMaster.getRepRunner().addReport("hr.restart.robno.repPrkKol","hr.restart.robno.repPrkProvider","PriKalk", "Primka - kolièinska");
    raDetail.getRepRunner().addReport("hr.restart.robno.repPrkKol","hr.restart.robno.repPrkProvider","PriKalk","Primka - kolièinska");
    raMaster.getRepRunner().addReport("hr.restart.robno.repPrkVri","hr.restart.robno.repPrkProvider","PriKalkExtendedVersion","Primka - vrijednosna");
    raDetail.getRepRunner().addReport("hr.restart.robno.repPrkVri","hr.restart.robno.repPrkProvider","PriKalkExtendedVersion","Primka - vrijednosna");
    raMaster.getRepRunner().addReport("hr.restart.robno.repPrkKAl","hr.restart.robno.repPrkProvider","PriKalkMegablastVersion","Primka - kalkulacija");
    raDetail.getRepRunner().addReport("hr.restart.robno.repPrkKAl","hr.restart.robno.repPrkProvider","PriKalkMegablastVersion","Primka - kalkulacija");
    raMaster.getRepRunner().addReport("hr.restart.robno.repPrkKal","hr.restart.robno.repPrkProvider","PriKalkFakeInab","Primka - kalkulacija, nabavna");
    raDetail.getRepRunner().addReport("hr.restart.robno.repPrkKal","hr.restart.robno.repPrkProvider","PriKalkFakeInab","Primka - kalkulacija, nabavna");

    raMaster.getRepRunner().addReport("hr.restart.robno.repPrkNiv","hr.restart.robno.repPrkNivProvider","Nivel","Poravnanje - nivelacija");
    raDetail.getRepRunner().addReport("hr.restart.robno.repPrkNiv","hr.restart.robno.repPrkNivProvider","Nivel","Poravnanje - nivelacija");
    
//    raMaster.getRepRunner().addReport("hr.restart.robno.repNivel", "Formatirani ispis", 2);
    
    jpMaster.jpGetVal.setRaDataSet(getMasterSet());
  }
  
  public void beforeShowMaster() {
    super.beforeShowMaster();
    
    jpDetail.setTransEnabled(isTranzit || isNar);
    jpDetail.setTransOpis(isTranzit);
  }
  
  public boolean doWithSaveMaster(char mode) {
    //if (mode == 'N' || mode == 'B' || (mode == 'I' && enableZT))
      try {
        zt.saveChanges(getMasterSet().getString("CSHZT").equals("YES") ? mode : 'B');
      } catch (Exception e) {
        return false;
      }
    return super.doWithSaveMaster(mode);
  }
  public boolean doWithSaveDetail(char mode) {
    if (getMasterSet().getString("CSHZT").equals("YES"))
      try {
        zts.saveChanges(mode);
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }

    if (!super.doWithSaveDetail(mode)) return false;
    //if (isTranzit) return updateRotStavka(mode);
    return true;
  }

  private String[] vkey = {"CSKL", "VRDOK", "GOD", "BRDOK", "RBSID"};
  
  private void findOldStavka(char mode) {
    if (mode == 'I' || mode == 'B') {
      VarStr veza = new VarStr(getDetailSet().getString("VEZA"));
      if (veza.length() > 0 && veza.countOccurences('-') >= 4) {
        rotStavkaOld = stdoki.getDataModule().getTempSet(
            Condition.whereAllEqual(vkey, veza.splitTrimmed('-')));
        rotStavkaOld.open();
        if (rotStavkaOld.rowCount() != 1) {
          new Throwable("Greška kod prijenosa ROT->PRK").printStackTrace();
          rotStavkaOld = null;
        }
      }
    }
  }
  
  
  private void recalcStavke() {
    
    if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(jpDetail,
        "Rekalkulirati sve stavke s teèajem i troškovima?", "Rekalkulacija", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.WARNING_MESSAGE)) return;
    
    for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next()) {
      findOldValues('I');
      getDetailSet().setBigDecimal("PZT", getMasterSet().getBigDecimal("UPZT"));
      getDetailPanel().kalkulacija(1);
      isFind = findSTANJE();
      findIZAD();
      updateStanje('I');
      raTransaction.saveChangesInTransaction(new QueryDataSet[] {
                      getDetailSet(), stanjeSet});
    }
    
    raDetail.getJpTableView().fireTableDataChanged();
    JOptionPane.showMessageDialog(jpDetail, 
        "Rekalkulacija završena.",
        "Rekalkulacija", JOptionPane.ERROR_MESSAGE);
  }
  
  private void findTransStavka(char mode) {
    if (mode == 'I' || mode == 'B') {
      transStavka = Artrans.getDataModule().getTempSet(
          Condition.equal("DEST", getDetailSet(), "ID_STAVKA"));
      transStavka.open();
      if (transStavka.rowCount() != 1) {
        transStavka = rotStavkaOld = null;
        return;
      }
      rotStavkaOld = stdoki.getDataModule().getTempSet(
          Condition.equal("ID_STAVKA", transStavka, "SRC"));
      rotStavkaOld.open();
      if (rotStavkaOld.rowCount() != 1) {
        new Throwable("Greška kod prijenosa NDO->PRK").printStackTrace();
        transStavka = rotStavkaOld = null;
      }
    }
  }
  
  boolean updateRotStavka(char mode) {
    try {
      System.out.println("updateRotStavka:");
      System.out.println(rotStavkaOld);
      System.out.println(rotStavkaNew);
      System.out.println(stanjeSet);
      // mogucnosti:
      // 1. stavka nije prebacena iz ROT-a ni prije ni sad
      if (rotStavkaOld == null && rotStavkaNew == null) return true;
      
      // 2. stavka je od prije prebacena, a sad se brise
      if (mode == 'B') return removeOldLink();
      
      // 2. stavka je od prije prebacena, a sad se samo mijenja kalkulacija
      if (rotStavkaNew == null) return updateRotStavka(rotStavkaOld);
      
      // 3. stavka je povezana s ROT stavkom, a prije nije bila
      //  (nevezano radi li se o unosu nove ili izmjeni stare, nepovezane stavke)
      if (rotStavkaOld == null) return updateRotStavka(rotStavkaNew);
      
      // 4. stavka je povezana opet s istom stavkom kao i prije. Svodi se na 2/3
      String diff = dM.compareColumns(rotStavkaNew, rotStavkaOld, vkey);
      if (diff == null) return updateRotStavka(rotStavkaOld);
      
      // 5. stavka je povezana s novom, a prije je bila s nekom drugom
      removeOldLink();
      updateRotStavka(rotStavkaNew);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  
  private boolean removeOldLink() {
    if (!isTranzit) return removeOldLinkNdo();
    rotStavkaOld.setString("VEZA", "");
    rotStavkaOld.setString("STATUS", "N");
    QueryDataSet master = doki.getDataModule().getTempSet(
        Condition.whereAllEqual(Util.mkey, rotStavkaOld));
    master.open();
    if (master.getRowCount() == 1) {
      master.setString("STATIRA", "N");
      raTransaction.saveChanges(master);
    } else new Throwable("Greška kod prijenosa ROT->PRK").printStackTrace();
    raTransaction.saveChanges(rotStavkaOld);
    return true;
  }
  
  private boolean removeOldLinkNdo() {
    Aus.sub(rotStavkaOld, "KOL2", transStavka, "KOL");
    rotStavkaOld.setString("STATUS", 
        Aus.comp(rotStavkaOld, "KOL", "KOL2") == 0 ? "P" : "N");
    raTransaction.saveChanges(rotStavkaOld);
    if (rotStavkaOld.getString("VEZA").length() > 0) {
      QueryDataSet zah = stdoki.getDataModule().getTempSet(
          Condition.equal("ID_STAVKA", rotStavkaOld.getString("VEZA")));
      zah.open();
      if (zah.rowCount() == 1) {
        Aus.set(zah, "KOL2", rotStavkaOld);
        raTransaction.saveChanges(zah);
      }
    }
    transStavka.deleteRow();
    raTransaction.saveChanges(transStavka);
    return true;
  }
  
  private boolean updateRotStavka(QueryDataSet stavka) {
    if (!isTranzit) return updateNdoStavka(stavka);
    
    rKD.stavka.Init();
    rKD.stavkaold.Init();
    rKD.stanje.Init();
    lc.TransferFromDB2Class(stavka, rKD.stavkaold);
    lc.TransferFromDB2Class(stavka, rKD.stavka);
    lc.TransferFromDB2Class(stanjeSet, rKD.stanje);
    rKD.setVrzal(getMasterSet().getString("CSKL"));
    if (getDetailSet().getBigDecimal("KOL").compareTo(rKD.stavka.kol) == 0)
      rKD.pureCopySkladPartFrom(getDetailSet());
    else rKD.pureKalkSkladPartFrom(getDetailSet());
    rKD.KalkulacijaStanje("ROT");
    lc.TransferFromClass2DB(stavka, rKD.stavka);
    lc.TransferFromClass2DB(stanjeSet, rKD.stanje);
    stavka.setString("STATUS", "P");
    stavka.setString("VEZA", getDetailSet().getString("ID_STAVKA"));
    getDetailSet().setString("VEZA", stavka.getString("ID_STAVKA"));
    DataSet nonp = stdoki.getDataModule().getTempSet(Condition.whereAllEqual(
        Util.mkey, stavka).and(Condition.equal("STATUS", "N")));
    nonp.open();
    if (nonp.rowCount() == 0 || (nonp.rowCount() == 1 &&
        dM.compareColumns(stavka, nonp, vkey) == null)) {
      // postavi status mastera na prenesen
      QueryDataSet master = doki.getDataModule().getTempSet(
          Condition.whereAllEqual(Util.mkey, stavka));
      master.open();
      if (master.getRowCount() == 1) {
        master.setString("STATIRA", "P");
        raTransaction.saveChanges(master);
      } else new Throwable("Greška kod prijenosa ROT->PRK").printStackTrace();
    }

    raTransaction.saveChanges(stavka);
    raTransaction.saveChanges(stanjeSet);
    raTransaction.saveChanges(getDetailSet());
    return true;
  }
  
  private boolean updateNdoStavka(QueryDataSet stavka) {
    QueryDataSet link = Artrans.getDataModule().getTempSet("1=0");
    link.open();
    link.setString("UUID", Aus.timeToString());
    link.setString("SRC", stavka.getString("ID_STAVKA"));
    link.setString("DEST", getDetailSet().getString("ID_STAVKA"));
    link.setBigDecimal("KOL", getDetailSet().getBigDecimal("KOL"));
    Aus.add(stavka, "KOL2", link, "KOL");
    stavka.setString("STATUS", Aus.comp(stavka, "KOL", "KOL2") <= 0 ? "P" : "N");
    if (stavka.getString("VEZA").length() > 0) {
      QueryDataSet zah = stdoki.getDataModule().getTempSet(
          Condition.equal("ID_STAVKA", stavka.getString("VEZA")));
      zah.open();
      if (zah.rowCount() == 1) {
        Aus.set(zah, "KOL2", stavka);
        raTransaction.saveChanges(zah);
      }
    }
    raTransaction.saveChanges(link);
    raTransaction.saveChanges(stavka);
    return true;
  }
  
  public void EntryPointMaster(char mode) {
    super.EntryPointMaster(mode);
    if (mode == 'N') jpMaster.jtabs.setSelectedIndex(0);
  }
  public void SetFokusMaster(char mode) {
  	super.SetFokusMaster(mode);
    boolean detailExist = Stdoku.getDataModule().getRowCount(Condition.whereAllEqual(
        new String[] {"CSKL","GOD","VRDOK","BRDOK"}, getMasterSet())) > 0;    
    if (mode == 'I' && detailExist && !allowRecalc)
      jpMaster.jpGetVal.setValutaEditable(false);
    else jpMaster.jpGetVal.setValutaEditable(true);

    if (mode=='N') {
      getMasterSet().setTimestamp("DATDOK", vl.getPresToday(presPRK.getPres().getSelRow()));
      getMasterSet().setTimestamp("DVO", vl.getPresToday(presPRK.getPres().getSelRow()));
      presPRK.getPres().copySelValues();
    }
    enableZT = (mode == 'N' || (mode == 'I' && !detailExist));
    zt.needsRefresh();
    jpMaster.initPanel(mode);
    if (allowRecalc && mode == 'I') {
      jpMaster.rcc.setLabelLaF(jpMaster.jtfDEVIZN, true);
      jpMaster.rcc.setLabelLaF(jpMaster.jtfUINAB, true);
      jpMaster.rcc.setLabelLaF(jpMaster.jtfUIZT, true);
      jpMaster.rcc.setLabelLaF(jpMaster.jtfUPZT, true);
    }
  }
  public void afterSetModeMaster(char oldm, char newm) {
    super.afterSetModeMaster(oldm, newm);
    if (newm == 'B') zt.kill();
  }
  public void afterSetModeDetail(char oldm, char newm) {
    super.afterSetModeDetail(oldm, newm);
    if (newm == 'B') zts.kill();
  }
  public void SetFokusDetail(char mode) {
    super.SetFokusDetail(mode);
    if (mode=='N') {
//      getDetailSet().setBigDecimal("PZT", rdUtil.getUtil().getPrimka_ZT(jpMaster.getCSHZT()));
      getDetailSet().setBigDecimal("PZT", getMasterSet().getBigDecimal("UPZT")); // (ab.f)
      jpDetail.rpcart.SetDefFocus();
//      jpDetail.rpcart.setCART();
    }
//    if (mode != 'B') zt.loadZT(false);
    jpDetail.findVirtualFields(mode);
    if (getMasterSet().getString("CSHZT").equals("YES")) {
      zts.needsRefresh(mode);
//      jpDetail.checkZT();
    }
  }
  public void EntryPointDetail(char mode) {
    super.EntryPointDetail(mode);
    if (mode == 'I') jpDetail.rpcart.EnabDisab(false);
    if (isTranzit && jpDetail.trans.isVisible())
      jpDetail.rcc.setLabelLaF(jpDetail.trans, mode == 'N');
      
    rotStavkaNew = rotStavkaOld = transStavka = null;
    jpDetail.disableDefFields();
//    jpDetail.setCustomZT(getMasterSet().getString("CSHZT").equals("YES"));
  }
  public boolean ValidacijaMaster(char mode) {
    /*getMasterSet().setBigDecimal("TECAJ",jpMaster.jpGetVal.getTecajUI());*/
    if (hr.restart.sisfun.frmParam.getParam("robno","reqRacUlaz","N","Obavezan ulaz raèuna i ulaznog dokumenta na PRK-u (D/N)").equals("D")) {
      if (vl.isEmpty(jpMaster.jtfBRRAC))
        return false;
      if (vl.isEmpty(jpMaster.jpBRDOKUL.jtfBRDOK))
        return false;
    }
    if (zt.isShowing()) zt.rnvExit_action();
    if (enableZT) getMasterSet().setString("CSHZT", jpMaster.jcbZT.isSelected() ? "YES" : "");
    if (vl.isEmpty(jpMaster.jrfCPAR))
      return false;
    if (!super.ValidacijaMaster(mode)) return false;
    zt.prepareSave();
    return true;
  }
  public void AfterCancelMaster() {
    zt.kill();
  }
  public void AfterCancelDetail() {
    zts.kill();
  }
  public boolean DeleteCheckMaster() {
    zt.prepareSave();
    return super.DeleteCheckMaster();
  }
  public boolean DeleteCheckDetail() {
    zts.prepareSave();
    if (isTranzit) findOldStavka('B');
    else if (isNar) findTransStavka('B');
    return super.DeleteCheckDetail();
  }

  public boolean ValidacijaDetail(char mode) {
    if (zts.isShowing()) zts.rnvExit_action();
    if (vl.isEmpty(jpDetail.jtfKOL))
      return false;
    if (vl.isEmpty(jpDetail.jtfDC))
      return false;
    if (vl.isEmpty(jpDetail.jtfVC))
      return false;
    if (vl.isEmpty(jpDetail.jtfMC))
      return false;
    if (mode == 'N') super.saveDobArt();
    if (!dlgSerBrojevi.getdlgSerBrojevi().findSB(jpDetail.rpcart, getDetailSet(), 'U', mode)) {
      return false;
    }
    if (!super.ValidacijaDetail(mode)) return false;
    if (rotStavkaNew != null && isTranzit) {
      rotStavkaNew.refresh();
      if (getDetailSet().getBigDecimal("KOL").compareTo(
          rotStavkaNew.getBigDecimal("KOL")) != 0) {
        if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(jpDetail,
            "Kolièina na primci se razlikuje od kolièine na raèunu!" +
            " Nastaviti ipak?", "Upozorenje", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE)) return false;
      }
      if (!rotStavkaNew.getString("STATUS").equalsIgnoreCase("N")) {
        JOptionPane.showMessageDialog(jpDetail, 
            "Odabrana stavka raèuna je veæ ažurirana s prijašnjim ulazom!",
            "Prijenos", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    if (isTranzit) findOldStavka(mode);
    else if (isNar) findTransStavka(mode);
    if (rotStavkaNew == null && rotStavkaOld != null && isTranzit) {
      if (getDetailSet().getBigDecimal("KOL").compareTo(
          rotStavkaOld.getBigDecimal("KOL")) != 0) {
        if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(jpDetail,
            "Kolièina na primci se razlikuje od kolièine na raèunu!" +
            " Nastaviti ipak?", "Upozorenje", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE)) return false;
      }
    }
    zts.prepareSave();
    return true;
  }
  public void AfterSaveDetail(char mode) {
    super.AfterSaveDetail(mode);
  }
  public boolean ValDPEscapeDetail(char mode) {
    if (mode=='N') {
      if (jpDetail.rpcart.getCART().trim().length() == 0)
        return true;
      
      getDetailSet().setBigDecimal("DC", main.nul);
      getDetailSet().setBigDecimal("PRAB", main.nul);
//        getDetailSet().setBigDecimal("PZT", main.nul);
      getDetailSet().setBigDecimal("PMAR", main.nul);
      getDetailSet().setBigDecimal("VC", main.nul);
      getDetailSet().setBigDecimal("MC", main.nul);
      getDetailSet().setBigDecimal("KOL", main.nul);
      getDetailSet().setBigDecimal("MAR", main.nul);
      jpDetail.tds.setBigDecimal("POR", main.nul);
      jpDetail.kalkulacija(0);
      jpDetail.disableUnosFields(true, 'P');
      if (isTranzit && jpDetail.trans.isVisible())
        jpDetail.rcc.setLabelLaF(jpDetail.trans, true);
      rotStavkaNew = rotStavkaOld = transStavka = null;
      jpDetail.rpcart.setCART();
      jpDetail.findSTANJE(' ');
      jpDetail.rpcart.SetDefFocus();
      return false;
    }

    return true;
  }

  public boolean ValidacijaPrijeIzlazaDetail() {
    if (!super.ValidacijaPrijeIzlazaDetail()) return false;
    if (!getMasterSet().getString("CSHZT").equals("YES")) return true;
    zts.kill();
    return checkZavtr();
  }

  protected void tranzitDohvat() {
    if (!isTranzit) {
      ndoDohvat();
      return;
    }
    
    String art = jpDetail.rpcart.getCART().trim();
    boolean haveArt = art.length() > 0;
    
    boolean fullRac = frmParam.getParam("robno", "dohvatROT", "D", 
        "Prikazati kalkulirane stavke raèuna kod dohvata na primci (D,N)", true).
          equalsIgnoreCase("D");
    
    String dod = "";
    if (haveArt) dod = " AND stdoki.cart=" + art;
    else if (!fullRac) dod = " AND stdoki.status='N'";

    String[] mcols = {"CUSER", "CSKL", "VRDOK", "GOD", 
        "BRDOK", "CPAR", "DATDOK", "OPIS", "PNBZ2"};
    
    String[] dcols = {"RBR", "CART", "CART1", "BC", "NAZART", 
        "JM", "KOL", "FVC", "FMC", "STATUS"};
    
    String q = "SELECT doki.cuser, doki.cskl, doki.vrdok, doki.god, " +
    		"doki.brdok, doki.cpar, doki.datdok, doki.opis, doki.pnbz2, " +
    		"stdoki.rbr, stdoki.cart, stdoki.cart1, stdoki.bc, " +
    		"stdoki.nazart, stdoki.jm, stdoki.kol, stdoki.fvc, " +
    		"stdoki.fmc, stdoki.status FROM doki,stdoki WHERE " +
    		Util.getUtil().getDoc("doki", "stdoki") + 
    		" AND doki.cskl='" + getMasterSet().getString("CSKL") +
    		"' AND doki.god='" + getMasterSet().getString("GOD") +
    		"' AND doki.vrdok='ROT' AND doki.statira='N'" + dod;
    System.out.println(q);
    		
    QueryDataSet doh = new QueryDataSet();
    Aus.setFilter(doh, q);
    List cols = new ArrayList();
    for (int i = 0; i < mcols.length; i++)
      cols.add(doki.getDataModule().getColumn(mcols[i]).clone());
    for (int i = 0; i < dcols.length; i++)
      cols.add(stdoki.getDataModule().getColumn(dcols[i]).clone());
    doh.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+
        MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
    doh.setColumns((Column[]) cols.toArray(new Column[cols.size()]));
    doh.open();
    if (doh.rowCount() == 0) {
      JOptionPane.showMessageDialog(jpDetail, 
          "Na skladištu nema nepovezanih raèuna u ovoj godini!",
          "Prijenos", JOptionPane.ERROR_MESSAGE);
      return;
    }

    doh.setSort(new SortDescriptor(new String[] {"DATDOK", "RBR"}));
    
    MasterDetailChooser mdc = new MasterDetailChooser(doh, 
        haveArt ? "prk-dohvat-rot-art" : "prk-dohvat-rot", 
            mcols, dcols, new int[] {2, 4, 5, 6, 9,
            Aut.getAut().getCARTdependable(10, 11, 12), 13,14,15,16});
    mdc.addModifier(new raTableColumnModifier("CPAR",
         new String[] { "CPAR", "NAZPAR" }, dm.getPartneri()));
    mdc.addModifier(new StatusColorModifier(mdc.isTurboTable()));
    
    if (mdc.show(raDetail.getWindow(), 
        "Dohvat stavke raèuna za povezivanje")) {
      if (!doh.getString("STATUS").equals("N")) {
        JOptionPane.showMessageDialog(jpDetail, 
            "Odabrana stavka je veæ ažurirana s prijašnjim ulazom!",
            "Prijenos", JOptionPane.ERROR_MESSAGE);
        return;
      }
      rotStavkaNew = stdoki.getDataModule().getTempSet(
          Condition.whereAllEqual(Util.dkey, doh));
      rotStavkaNew.open();
      if (haveArt) afterGetArtikl();
      else jpDetail.rpcart.setCART(doh.getInt("CART"));
    }
  }
  
  protected void ndoDohvat() {
    String art = jpDetail.rpcart.getCART().trim();
    boolean haveArt = art.length() > 0;
    
    String dod = "";
    if (haveArt) dod = " AND stdoki.cart=" + art;

    String[] mcols = {"CUSER", "CSKL", "VRDOK", "GOD", 
        "BRDOK", "CPAR", "DATDOK", "OPIS", "PNBZ2"};
    
    String[] dcols = {"RBR", "CART", "CART1", "BC", "NAZART", 
        "JM", "KOL", "KOL2", "STATUS"};
    
    String q = "SELECT doki.cuser, doki.cskl, doki.vrdok, doki.god, " +
            "doki.brdok, doki.cpar, doki.datdok, doki.opis, doki.pnbz2, " +
            "stdoki.rbr, stdoki.cart, stdoki.cart1, stdoki.bc, " +
            "stdoki.nazart, stdoki.jm, stdoki.kol, stdoki.kol2, stdoki.status " +
            "FROM doki,stdoki WHERE " +
            Util.getUtil().getDoc("doki", "stdoki") + 
            " AND doki.cpar=" + getMasterSet().getInt("CPAR") +
            " AND doki.god='" + getMasterSet().getString("GOD") +
            "' AND doki.vrdok='NDO' AND doki.statira='N'" + dod +
            " AND stdoki.status='N'";
    System.out.println(q);
            
    QueryDataSet doh = new QueryDataSet();
    Aus.setFilter(doh, q);
    List cols = new ArrayList();
    for (int i = 0; i < mcols.length; i++)
      cols.add(doki.getDataModule().getColumn(mcols[i]).clone());
    for (int i = 0; i < dcols.length; i++)
      cols.add(stdoki.getDataModule().getColumn(dcols[i]).clone());
    doh.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+
        MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
    doh.setColumns((Column[]) cols.toArray(new Column[cols.size()]));
    doh.open();
    if (doh.rowCount() == 0) {
      JOptionPane.showMessageDialog(jpDetail, 
          "Za ovog partnera nema nepovezanih narudžbenica u ovoj godini!",
          "Prijenos", JOptionPane.ERROR_MESSAGE);
      return;
    }
    doh.getColumn("KOL2").setCaption("Isporuèeno");

    doh.setSort(new SortDescriptor(new String[] 
             {"DATDOK", "CSKL", "BRDOK", "RBR"}));
    
    MasterDetailChooser mdc = new MasterDetailChooser(doh, 
        haveArt ? "prk-dohvat-ndo-art" : "prk-dohvat-ndo", 
            mcols, dcols, new int[] {2, 4, 6, 9,
            Aut.getAut().getCARTdependable(10, 11, 12), 13,14,15,16});
    mdc.addModifier(new raTableColumnModifier("CPAR",
         new String[] { "CPAR", "NAZPAR" }, dm.getPartneri()));
    mdc.addModifier(new StatusColorModifier(mdc.isTurboTable()));
    
    if (mdc.show(raDetail.getWindow(), 
        "Dohvat stavke narudžbenice za povezivanje")) {
      if (!doh.getString("STATUS").equals("N")) {
        JOptionPane.showMessageDialog(jpDetail, 
            "Odabrana stavka je veæ povezana s prijašnjim ulazom!",
            "Prijenos", JOptionPane.ERROR_MESSAGE);
        return;
      }
      rotStavkaNew = stdoki.getDataModule().getTempSet(
          Condition.whereAllEqual(Util.dkey, doh));
      rotStavkaNew.open();
      if (haveArt) afterGetArtikl();
      else jpDetail.rpcart.setCART(doh.getInt("CART"));
    }
  }
  
  public void afterGetArtikl() {
    if (rotStavkaNew != null) {
      if (isTranzit) {
        jpDetail.oldValue = "";
        getDetailSet().setBigDecimal("KOL", rotStavkaNew.getBigDecimal("KOL"));
        jpDetail.jtfKOL.maskCheck();
        getDetailSet().setBigDecimal("VC", rotStavkaNew.getBigDecimal("FVC"));
        jpDetail.jtfVC.maskCheck();
        if (frmParam.getParam("robno", "focAfterPRKdoh", "1", "Fokusirati koje polje " +
        		"nakon dohvata stavke raèuna na primci (1-DC,2-IDOB)", true).
        		equals("1"))
          jpDetail.jtfDC.requestFocusLater();
        else jpDetail.jtfIDOB.requestFocusLater();
      } else {
        jpDetail.oldValue = "";
        getDetailSet().setBigDecimal("KOL", 
            rotStavkaNew.getBigDecimal("KOL").
            subtract(rotStavkaNew.getBigDecimal("KOL2")));
        jpDetail.jtfKOL.maskCheck();
        getDetailSet().setBigDecimal("DC", rotStavkaNew.getBigDecimal("NC"));
        jpDetail.jtfDC.maskCheck();
        getDetailSet().setBigDecimal("PRAB", rotStavkaNew.getBigDecimal("UPRAB"));
        jpDetail.jtfPRAB.maskCheck();
        jpDetail.jtfKOL.requestFocusLater();
      }
    }
  }
  
  static class StatusColorModifier extends raTableModifier {
    Variant shared = new Variant();
    HashSet dset = new HashSet(Arrays.asList(new String[]
       {"RBR", "CART", "CART1", "BC", "NAZART", 
        "JM", "KOL", "FVC", "FMC", "STATUS"}));
    
    boolean turboTable;
    
    public StatusColorModifier(boolean turboTable) {
      this.turboTable = turboTable;
    }

    public boolean doModify() {
      if (getTable() instanceof JraTable2) {
        JraTable2 tab = (JraTable2) getTable();
        if (tab.getDataSet().getRowCount() > 0 && 
            tab.getDataSet().hasColumn("STATUS") != null) {
          if (turboTable && !dset.contains(tab.getDataSetColumn(getColumn()).
              getColumnName().toUpperCase())) return false;
          tab.getDataSet().getVariant("STATUS", this.getRow(), shared);
          return !shared.getString().equals("N");
        }
      }
      return false;
    }
    
    public void modify() {
      JComponent jRenderComp = (JComponent) renderComponent;
      if (isSelected()) {
        jRenderComp.setBackground(raColors.green);
        jRenderComp.setForeground(Color.black);
      } else {
        //jRenderComp.setBackground(getTable().getBackground());
        jRenderComp.setForeground(Color.green.darker().darker());
      }
    }
  }
}
