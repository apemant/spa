/****license*****************************************************************
**   file: frmRadniNalog.java
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
import hr.restart.baza.Rnser;
import hr.restart.baza.Skupart;
import hr.restart.baza.Stanje;
import hr.restart.baza.VTRnl;
import hr.restart.baza.dM;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.raUser;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavBar;
import hr.restart.util.raTransaction;
import hr.restart.util.sysoutTEST;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public abstract class frmRadniNalog extends raMasterDetail {

  sysoutTEST sys = new sysoutTEST(false);
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  Rbr rbr = Rbr.getRbr();
  lookupData ld = lookupData.getlookupData();
  raDateUtil rdu = raDateUtil.getraDateUtil();

  String status, corg;
  Timestamp dfrom, dto;

  short oldRbr;
  int oldRbsid;

  rajpBrDok header;

  boolean needDetail;
  protected boolean forceTotalExpand = false;

  protected void copyPreselectValues() {
    corg = getPreSelect().getSelRow().getString("CSKL");
    status = getPreSelect().getSelRow().getString("STATUS");
    dfrom = getPreSelect().getSelRow().getTimestamp("DATDOK-from");
    dto = getPreSelect().getSelRow().getTimestamp("DATDOK-to");
  }

  protected void initNewMaster() {
    this.getMasterSet().setString("CUSEROTVORIO", raUser.getInstance().getUser());
    this.getMasterSet().setString("VRDOK", "RNL");
    this.getMasterSet().setString("CSKL", corg);
    this.getMasterSet().setTimestamp("DATDOK", ut.getToday(dfrom, dto));
    this.getMasterSet().setString("STATUS", "P");    
  }

//  protected void getBrojDokumenta(com.borland.dx.dataset.DataSet ds) {
//    Integer Broj;
//    this.getMasterSet().setString("GOD",vl.findYear(getMasterSet().getTimestamp("DATDOK")));
//    Broj=vl.findSeqInteger(ds.getString("CSKL")+ds.getString("VRDOK")+ds.getString("GOD"), false);
//    ds.setInt("BRDOK",Broj.intValue());
//  }

  protected abstract void setIzmjenaEntry();

  public void EntryPointMaster(char mode) {
    if (mode == 'I') {
      setIzmjenaEntry();
    } else {
      initNewMaster();
    }
    header.SetDefTextDOK(mode);
  }

  protected void ValidacijaNoviMaster() {
//    getBrojDokumenta(this.getMasterSet());


  }

  public boolean doBeforeSaveMaster(char mode) {
    if (mode == 'N') {
      hr.restart.robno.Util.getUtil().getBrojDokumenta(this.getMasterSet());
      this.getMasterSet().setString("CRADNAL", "RNL-"+this.getMasterSet().getString("CSKL")+"/"+
                                  this.getMasterSet().getString("GOD")+"-"+this.getMasterSet().getInt("BRDOK"));
      needDetail = this.getMasterSet().getString("CSKUPART").length() == 0;
      if (!needDetail) fillStavke();
    }
    return true;
  }


  public boolean doWithSaveMaster(char mode) {
    if (mode == 'B') {
      hr.restart.robno.Util.getUtil().delSeq(delstr, true);
    }
    return true;
  }

  String delstr;
  public boolean DeleteCheckMaster() {
    DataSet ds = getMasterSet();
    delstr = rut.getSeqString(ds);
    return hr.restart.robno.Util.getUtil().checkSeq(delstr, String.valueOf(ds.getInt("BRDOK")));
  }

  public boolean isNewDetailNeeded() {
    return needDetail;
  }

  protected void initNewDetail(DataSet ds) {
    ds.setString("VRDOK", "RNL");
    ds.setString("CRADNAL", this.getMasterSet().getString("CRADNAL"));
    ds.setString("CSKL", this.getMasterSet().getString("CSKL"));
    ds.setString("GOD", this.getMasterSet().getString("GOD"));
    ds.setInt("BRDOK", this.getMasterSet().getInt("BRDOK"));
  }

  protected void fillStavke() {
    int rbr = 0;
    if (!getDetailSet().isOpen()) {
      stdoki.getDataModule().setFilter(getDetailSet(), "1=0");
      getDetailSet().open();
    }
    BigDecimal mult = getMasterSet().getBigDecimal("MULT");
    if (mult.signum() <= 0) mult = Aus.one0;
    
    QueryDataSet skup = Skupart.getDataModule().setFilter(
        new QueryDataSet(), "cskupart = '"+this.getMasterSet().getString("CSKUPART")+"'");
    skup.open();
    QueryDataSet rns = Rnser.getDataModule().getTempSet("1=0");
    rns.open();
    QueryDataSet vti = VTRnl.getDataModule().getTempSet("1=0");
    vti.open();
    for (skup.first(); skup.inBounds(); skup.next()) {
//      System.out.println(vl.RezSet);
      if (true) {
          //Aut.getAut().artTipa(skup.getInt("CART"), "PMU")) {
        //System.out.println(vl.RezSet);
        this.getDetailSet().insertRow(false);
        initNewDetail(this.getDetailSet());
        this.getDetailSet().setShort("RBR", (short) ++rbr);
        this.getDetailSet().setInt("RBSID", rbr);
        Aut.getAut().copyArtFields(this.getDetailSet(), skup);
        //System.out.println("master "+this.getMasterSet().getString("CRADNAL"));
        //System.out.println("detail "+this.getDetailSet().getString("CRADNAL"));
//        this.getDetailSet().setString("CRADNAL", this.getMasterSet().getString("CRADNAL"));
        this.getDetailSet().setBigDecimal("KOL", skup.getBigDecimal("KOL").multiply(mult).setScale(3, BigDecimal.ROUND_HALF_UP));
        addVtrnl(vti);
        addRnser(rns);
        getDetailSet().setString("ID_STAVKA",
            raControlDocs.getKey(getDetailSet(), new String[] { "cskl",
                "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
      }
    }
    raTransaction.saveChanges(vti);
    raTransaction.saveChanges(rns);
    raTransaction.saveChanges(this.getDetailSet());
  }

  protected void ValidacijaNoviDetail() {
    this.getDetailSet().setInt("RBSID", rbr.getRbsID(this.getDetailSet()));
    this.getDetailSet().setShort("RBR", rbr.vrati_rbr("STDOKI",this.getMasterSet().getString("CSKL"),
        this.getMasterSet().getString("VRDOK"),this.getMasterSet().getString("GOD"),
        this.getMasterSet().getInt("BRDOK")));
  }

  public void masterSet_navigated(com.borland.dx.dataset.NavigationEvent e) {
    if (raDetail.isShowing()) checkActions();
    else checkMasterActions();
  }

  public void detailSet_navigated(com.borland.dx.dataset.NavigationEvent e) {
    checkActions();
  }

  public void checkActions() {
    if (!getDetailSet().isOpen()) return;
    boolean enaba = this.getMasterSet().getString("STATUS").trim().equals("P");
    boolean enabde = enaba && this.getDetailSet().getString("STATUS").trim().equals("N");

    raDetail.getNavBar().getStandardOption(raNavBar.ACTION_UPDATE).setEnabled(enabde);
    raDetail.getNavBar().getStandardOption(raNavBar.ACTION_DELETE).setEnabled(enabde);
    raDetail.getNavBar().getStandardOption(raNavBar.ACTION_ADD).setEnabled(enaba);
    raDetail.getNavBar().getStandardOption(raNavBar.ACTION_TOGGLE_TABLE).setEnabled(enaba);
  }

  public void checkMasterActions() {}

  public boolean DeleteCheckDetail() {
    oldRbr = this.getDetailSet().getShort("RBR");
    oldRbsid = this.getDetailSet().getInt("RBSID");
    if (!this.getDetailSet().getString("STATUS").trim().equals("N")) {
      JOptionPane.showMessageDialog(raDetail.getWindow(),
        "Ne mogu se brisati stavke po kojima su izdani drugi dokumenti !","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (checkIzdDetailRow(false)) {
      JOptionPane.showMessageDialog(raDetail.getWindow(),
          "Stavka je izdana i ne može se brisati!","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  
  void addRnser(QueryDataSet rns) {
    short rbr = 0;
    
    DataSet ds = Aut.getAut().expandArt(getDetailSet().getInt("CART"),getDetailSet().getBigDecimal("KOL"),false);
    for (ds.first(); ds.inBounds(); ds.next()) {
      if (!raVart.isStanje(ds.getInt("CART"))) {
        rns.insertRow(false);
        rns.setString("CRADNAL", getMasterSet().getString("CRADNAL"));
        rns.setInt("RBSID", getDetailSet().getInt("RBSID"));
        rns.setShort("RBR", ++rbr);
        Aut.getAut().copyArtFields(rns, ds);
        Aus.set(rns, "KOL", ds);
        if (ld.raLocate(dm.getArtikli(), "CART1", ds.getString("CART1"))) {
          Aus.set(rns, "ZC", dm.getArtikli(), "NC");
          Aus.mul(rns, "VRI", "ZC", "KOL");
        }
      }
    }
  }

  void addVtrnl(QueryDataSet vti) {
    DataSet skl = Util.getSkladFromCorg();
    int art = Stanje.getDataModule().getRowCount(Condition.in("CSKL", skl).
        and(Condition.equal("CART", getDetailSet())).
        and(Condition.equal("GOD", getMasterSet())));
        
    if (art > 0 || raVart.isStanje(getDetailSet().getInt("CART"))) {
        //Aut.getAut().artTipa(getDetailSet().getInt("CART"), "PRM")) {      
      DataSet exp = Aut.getAut().expandArt(getDetailSet(),
          getMasterSet().getString("SERPR").equalsIgnoreCase("S") || forceTotalExpand);
      exp.open();
      for (exp.first(); exp.inBounds(); exp.next()) {    
        if (raVart.isStanje(exp.getInt("CART"))) {
            // Aut.getAut().artTipa(exp.getInt("CART"), "PRM")) {
          vti.insertRow(false);
          vti.setString("CRADNAL", getMasterSet().getString("CRADNAL"));
          vti.setInt("RBSRNL", getDetailSet().getInt("RBSID"));
          vti.setString("BRANCH", exp.getString("BRANCH"));
          vti.setInt("RBSIZD", -1);
        }
      }
    }
  }
  
  public boolean doBeforeSaveDetail(char mode) {
    if (mode == 'N') {
      getDetailSet().setString("ID_STAVKA",
          raControlDocs.getKey(getDetailSet(), new String[] { "cskl",
              "vrdok", "god", "brdok", "rbsid" }, "stdoki"));     
    }
    if (mode == 'N') {
      QueryDataSet vti = VTRnl.getDataModule().getTempSet("1=0");
      vti.open();
      addVtrnl(vti);
      raTransaction.saveChanges(vti);
    }
    return true;
  }

  public boolean doWithSaveDetail(char mode) {
    if (mode == 'B') {
      vl.recountDataSet(raDetail, "RBR", oldRbr, false);
      raTransaction.saveChanges(getDetailSet());
      // vezna tablica za izdatnice
      try {
        raTransaction.runSQL("DELETE FROM VTRnl WHERE "+Condition.equal("CRADNAL",
            getMasterSet().getString("CRADNAL")).and(Condition.equal("RBSRNL", oldRbsid)));
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
      try {
        raTransaction.runSQL("DELETE FROM Rnser WHERE "+Condition.equal("CRADNAL",
            getMasterSet().getString("CRADNAL")).and(Condition.equal("RBSID", oldRbsid)));
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    } else if (mode == 'N') {
      QueryDataSet rns = Rnser.getDataModule().getTempSet("1=0");
      rns.open();
      addRnser(rns);
      raTransaction.saveChanges(rns);
    }
    return true;
  }

  private void Potvrdi() {
    if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(raMaster.getWindow(),
          "Želite li potvrditi radni nalog " + this.getMasterSet().getString("CRADNAL") + "?",
          "Potvrda", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) return;
    this.getMasterSet().setString("CUSEROBRAC", raUser.getInstance().getUser());
    this.getMasterSet().setString("STATUS", "O");
    this.getMasterSet().setTimestamp("DATUMO", vl.getToday());
    this.getMasterSet().saveChanges();
    raMaster.getJpTableView().fireTableDataChanged();
    JOptionPane.showMessageDialog(raMaster.getWindow(), "Potvrda obavljena!", "Poruka",
                                  JOptionPane.INFORMATION_MESSAGE);
  }

  private void Ponisti() {
    if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(raMaster.getWindow(),
            "Želite li poništiti potvrdu radnog naloga " + this.getMasterSet().getString("CRADNAL") + "?",
            "Potvrda", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) return;
    this.getMasterSet().setString("CUSEROBRAC", "");
    this.getMasterSet().setString("STATUS", "P");
    this.getMasterSet().saveChanges();
    raMaster.getJpTableView().fireTableDataChanged();
    JOptionPane.showMessageDialog(raMaster.getWindow(), "Potvrda poništena!", "Poruka",
                                  JOptionPane.INFORMATION_MESSAGE);
  }

  private void Zatvori() {
    if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(raMaster.getWindow(),
          "Želite li zatvoriti radni nalog " + this.getMasterSet().getString("CRADNAL") + "?",
          "Zatvaranje", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) return;

    this.getMasterSet().setString("STATUS", "Z");
    this.getMasterSet().setTimestamp("DATUMZ", vl.getToday());
    this.getMasterSet().saveChanges();
    raMaster.getJpTableView().fireTableDataChanged();
    JOptionPane.showMessageDialog(raMaster.getWindow(), "Radni nalog zatvoren!", "Poruka",
                                  JOptionPane.INFORMATION_MESSAGE);
  }

  private void Otvori() {
    if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(raMaster.getWindow(),
          "Želite li otvoriti radni nalog " + this.getMasterSet().getString("CRADNAL") + "?",
          "Zatvaranje", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) return;
    
    this.getMasterSet().setString("STATUS", 
        (getMasterSet().getString("CFAKTURE").length() > 0) ? "F" : "O");

    this.getMasterSet().saveChanges();
    raMaster.getJpTableView().fireTableDataChanged();
    JOptionPane.showMessageDialog(raMaster.getWindow(), "Radni nalog otvoren!", "Poruka",
                                  JOptionPane.INFORMATION_MESSAGE);
  }

  protected boolean isAutomaticIZD() {
    return false;
  }

  protected void transferToIZD() {}

  private void AutomaticIZD(boolean fakt) {
    int num = 0;
    VarStr list = new VarStr();
    DataSet ds = this.getDetailSet();
    for (ds.first(); ds.inBounds(); ds.next())
      if (!checkIzdDetailRow(true)) {
        ++num;
        list.append(ds.getShort("RBR")).append(" (").
            append(ds.getString("NAZART")).append("), ");
      }
    if (num == 0) {
      if (fakt) Zatvori();
      else Potvrdi();
    } else {
      list.insert(0, num == 1 ? "Stavka br. " : "Stavke br. ");
      list.chop(2).append(num == 1 ? " nije izdana" : " nisu izdane").
        append("! Želite li napraviti izdatnicu?");
      int ch = JOptionPane.showConfirmDialog(raMaster.getWindow(),
          new raMultiLineMessage(list.toString(), SwingConstants.LEFT, 80), "Prijenos",
          JOptionPane.YES_NO_OPTION);
      if (ch == JOptionPane.YES_OPTION) transferToIZD();
    }
  }

  protected boolean checkIzdDetailRow(boolean izd) {
    DataSet ds = VTRnl.getDataModule().getTempSet(Condition.equal("CRADNAL",
        getMasterSet().getString("CRADNAL")).and(Condition.equal("RBSRNL",
        getDetailSet().getInt("RBSID"))));
    ds.open();
    for (ds.first(); ds.inBounds(); ds.next())
      if ((ds.getInt("RBSIZD") < 0 && izd) || 
          (ds.getInt("RBSIZD") > 0 && !izd)) return !izd;
    return izd;
  }

  protected void Obradi() {
    this.getMasterSet().refetchRow(getMasterSet());
    String st = this.getMasterSet().getString("STATUS");
    if (st.equals("P") || st.equals("F")) {
      this.refilterDetailSet();
      DataSet ds = this.getDetailSet();
      ds.refresh();
      if (!isAutomaticIZD()) {
        for (ds.first(); ds.inBounds(); ds.next())
          if (!checkIzdDetailRow(true)) {
            /*if (ds.rowCount() > 1) {
              JOptionPane.showMessageDialog(raMaster.getWindow(), "Stavka br. "+
              ds.getShort("RBR") + " (" + ds.getString("NAZART") +
              ") nije izdana!", "Greška", JOptionPane.ERROR_MESSAGE);
              return;
            }*/
            if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Stavka br. "+
                  ds.getShort("RBR") + " (" + ds.getString("NAZART") +
                  ") nije izdana! \nOznaèiti preostale stavke normativa kao nevažne?", "Greška", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) != JOptionPane.YES_OPTION) return;
            DataSet vt = VTRnl.getDataModule().getTempSet(Condition.equal("CRADNAL",
                getMasterSet().getString("CRADNAL")).and(Condition.equal("RBSRNL",
                getDetailSet().getInt("RBSID"))));
            vt.open();
            for (vt.first(); vt.inBounds(); vt.next()) {
              if (vt.getInt("RBSIZD") < 0)
                vt.setInt("RBSIZD", 0);
            }
            vt.saveChanges();
          }
        if (st.equals("P")) Potvrdi();
        else Zatvori();
      } else AutomaticIZD(st.equals("F"));
    } else if (st.equals("O")) {
      boolean hasn = false, hasz = false;
      short rbr= 0;
      String nazart = "";
      this.refilterDetailSet();
      DataSet ds = this.getDetailSet();
      ds.refresh();
      for (ds.first(); ds.inBounds(); ds.next())
        if (ds.getString("STATUS").equals("Z")) hasz = true;
        else if (!hasn) {
          hasn = true;
          rbr = ds.getShort("RBR");
          nazart = ds.getString("NAZART");
        }
      if (!hasz) {
        if (!raUser.getInstance().isSuper() && !raUser.getInstance().getUser().equals(getMasterSet().getString("CUSEROBRAC"))) {
          JOptionPane.showMessageDialog(raMaster.getWindow(), "Radni nalog je potvrdio drugi korisnik!", "Greška",
                                        JOptionPane.ERROR_MESSAGE);
        } else Ponisti();
      } else {
        if (hasn) {
          JOptionPane.showMessageDialog(raMaster.getWindow(), "Stavka br. "+rbr+
             " ("+nazart+") nije zadovoljena!", "Greška", JOptionPane.ERROR_MESSAGE);
          return;
        }
        Zatvori();
      }
    } else Otvori();
  }

  public frmRadniNalog() {
  }
}

