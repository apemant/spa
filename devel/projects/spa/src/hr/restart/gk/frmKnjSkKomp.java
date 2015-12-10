/****license*****************************************************************
**   file: frmKnjSkKomp.java
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
import hr.restart.baza.Pokriveni;
import hr.restart.baza.Skstavke;
import hr.restart.baza.dM;
import hr.restart.sk.jpSelKonto;
import hr.restart.sk.raSaldaKonti;
import hr.restart.sk.raVrdokMatcher;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCpar;
import hr.restart.util.Aus;
import hr.restart.util.raCommonClass;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.Tecajevi;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmKnjSkKomp extends frmKnjizenje {

    raCommonClass rcc = raCommonClass.getraCommonClass();
    
    jpCpar jpp = new jpCpar(100, 265, false); /* {
    public void afterLookUp(boolean succ) {
    }
    protected void kupSelected() {
      super.kupSelected();
      if (raSaldaKonti.isDirect()) checkKupDob(true);
    }
    protected void dobSelected() {
      super.dobSelected();
      if (raSaldaKonti.isDirect()) checkKupDob(false);
    }
  }*/
  
  /*jpSelKonto jpk = new jpSelKonto(100, 265, true) {
    public void afterLookUp(boolean succ) {
      afterKonto(succ);
    }
  };*/
  
  jpSelKonto jpkup = new jpSelKonto(100, 265, false);
  jpSelKonto jpdob = new jpSelKonto(100, 265, false);
  
  JraTextField jraMaxKomp = new JraTextField();
    
    public frmKnjSkKomp() {
        dataSet.addColumn(dM.createIntColumn("CPAR", "Partner"));
        dataSet.addColumn(dM.createBigDecimalColumn("MAXKOMP", "Maksimalni iznos kompenzacije", 2));
                
        jpp.bind(dataSet);
        
        jpkup.bindOwn("Konto kupca");
        jpdob.bindOwn("Konto dobavljaèa");
                
        jraMaxKomp.setDataSet(dataSet);
        jraMaxKomp.setColumnName("MAXKOMP");
        
        JPanel ap = new JPanel(new XYLayout(540, 140));
        ap.add(jpp, new XYConstraints(0, 5, -1, -1));
        ap.add(jpkup, new XYConstraints(0, 30, -1, -1));
        ap.add(jpdob, new XYConstraints(0, 55, -1, -1));
        ap.add(new JLabel("Maksimalni iznos"), new XYConstraints(15, 80, -1, -1));
        ap.add(jraMaxKomp, new XYConstraints(150, 80, 100, -1));
        
        this.jp.add(ap,BorderLayout.CENTER);
    }
    
    public boolean Validacija() {
    //if (vl.isEmpty(jraMaxKomp)) return false;
    if (dataSet.getBigDecimal("MAXKOMP").signum() < 0) {
      jraMaxKomp.requestFocus();
        JOptionPane.showMessageDialog(this, "Pogrešan maksimalni iznos salda stavke!",
                "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    if (jpp.isEmpty()) {
      jpp.requestFocus();
      JOptionPane.showMessageDialog(this, "Obavezan unos partnera!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (jpkup.getKonto().length() == 0 || jpdob.getKonto().length() == 0) {
      if (jpkup.getKonto().length() == 0) jpkup.focusKonto();
      else jpdob.focusKonto();
      JOptionPane.showMessageDialog(this, "Obavezan unos konta!",
              "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
    

  Collection skstav = null;
  public boolean okPress() throws Exception {
    BigDecimal ms = dataSet.getBigDecimal("MAXKOMP");
    
    Condition cond = jpp.getCondition().and(Aus.getKnjigCond()).
            and(jpkup.getCondition().and(Condition.diff("ID", 0)).or(jpdob.getCondition().and(Condition.diff("IP", 0)))).
            and(Aus.getCurrGKDatumCond("DATDOK", dataSet.getTimestamp("DATUMDO"))).
            and(Condition.diff("SALDO", 0));
    
    String query = 
            "SELECT cpar, brojkonta, brojdok, corg, datdok, id, ip, saldo, " +
            "oznval, cskstavke FROM skstavke WHERE " + cond;
    System.out.println("query: "+query);
    StorageDataSet data = Skstavke.getDataModule().getScopedSet(
            "CPAR BROJKONTA BROJDOK CORG DATDOK ID IP SALDO OZNVAL CSKSTAVKE");
    ut.fillReadonlyData(data, query);
    
    data.setSort(new SortDescriptor(new String[] {"DATDOK"}));
    
    List st = new ArrayList();
    
    for (data.first(); data.inBounds(); data.next())
        if (raSaldaKonti.isDomVal(data))
            st.add(new LinkData(data));

    if (st.isEmpty()) {
      getKnjizenje().setErrorMessage("Nema podataka za knjiženje");
      throw new Exception("Nema podataka za knjiženje");
    }
    
    BigDecimal sid = Aus.zero0;
    BigDecimal sip = Aus.zero0;
    for (Iterator i = st.iterator(); i.hasNext(); ) {
      LinkData ld = (LinkData) i.next();
      sid = sid.add(ld.id);
      sip = sip.add(ld.ip);
    }
    
    if (ms.signum() == 0) ms = sid.max(sip);
    ms = ms.min(sid).min(sip);
    sid = sip = ms;
    
    System.out.println(sid + "  " + sip);
    
    if (ms.signum() == 0) {
      getKnjizenje().setErrorMessage("Nema podataka za knjiženje");
      throw new Exception("Nema podataka za knjiženje");
    }
    
    if (!getKnjizenje().startKnjizenje(this)) return false;
    getKnjizenje().setSKRacKnj(false);
    getKnjizenje().setInfoKeys(null);
    
    skstav = new ArrayList();
    StorageDataSet sds = null;
    for (Iterator i = st.iterator(); i.hasNext(); ) {
      LinkData ld = (LinkData) i.next();
      if (ld.id.signum() != 0 && sid.signum() == 0 || ld.ip.signum() != 0 && sip.signum() == 0) continue;
        
        sds = getKnjizenje().getNewStavka(ld.konto, ld.corg);
        if (sds != null) {
          
          if (ld.id.signum() != 0) {
            ld.id = ld.id.min(sid);
             getKnjizenje().setID(Aus.zero2);
             getKnjizenje().setIP(ld.id);
             sid = sid.subtract(ld.id);
          } else {
            ld.ip = ld.ip.min(sip);
             getKnjizenje().setID(ld.ip);
             getKnjizenje().setIP(Aus.zero2);
             sip = sip.subtract(ld.ip);
          }
          skstav.add(ld);
            
            sds.setTimestamp("DATDOK", ld.datdok);
            String cgk = getKnjizenje().getFNalozi().jpMaster.jpBrNal.getCNaloga() + 
                        "-" + getKnjizenje().getFNalozi().jpDetail.jpBrNal.rbs;
            ld.cgk = cgk;
            sds.setString("BROJDOK", ld.brojdok);
            sds.setInt("CPAR", ld.cpar);
            sds.setString("VRDOK", ld.id.signum() != 0 ? "OKK" : "OKD");
            sds.setString("OPIS", "Kompenzacija po raèunu - " + ld.brojdok);
            sds.setString("OZNVAL", Tecajevi.getDomOZNVAL());
            sds.setBigDecimal("TECAJ", Aus.one0);
            if (!getKnjizenje().saveStavka()) return false;
        }
    }
    
    return getKnjizenje().saveAll();
  }
  
  /*
   * Proknjizavanje temeljnice. SK dio se hendla posve ruèno, zbog specifiènog posla.
   */
  public boolean commitTransfer() {
    raVrdokMatcher vm = new raVrdokMatcher();
    QueryDataSet sk = Skstavke.getDataModule().getTempSet(Condition.nil);
    sk.open();
    QueryDataSet pok = Pokriveni.getDataModule().getTempSet(Condition.nil);
    pok.open();
    for (Iterator i = skstav.iterator(); i.hasNext(); ) {
      LinkData ld = (LinkData) i.next();

        sk.insertRow(false);
        sk.setString("KNJIG", OrgStr.getKNJCORG(false));
        sk.setString("VRDOK", ld.id.signum() != 0 ? "OKK" : "OKD");
        sk.setInt("CPAR", ld.cpar);
        sk.setString("BROJKONTA", ld.konto);
        sk.setString("BROJDOK", ld.cgk);
        sk.setTimestamp("DATDOK", dataSet.getTimestamp("DATUMKNJ"));
        sk.setTimestamp("DATUMKNJ", dataSet.getTimestamp("DATUMKNJ"));
        sk.setInt("BROJIZV", 0);
        sk.setString("CORG", ld.corg);
        sk.setString("OZNVAL", Tecajevi.getDomOZNVAL());
        sk.setBigDecimal("TECAJ", Aus.one0);
        if (ld.id.signum() != 0) {
            sk.setBigDecimal("ID", Aus.zero2);
            sk.setBigDecimal("IP", ld.id);
        } else {
            sk.setBigDecimal("ID", ld.ip);
            sk.setBigDecimal("IP", Aus.zero2);
        }
        sk.setBigDecimal("SALDO", ld.id.signum() != 0 ? ld.id : ld.ip);
        sk.setBigDecimal("SSALDO", sk.getBigDecimal("SALDO"));
        sk.setBigDecimal("PVID", sk.getBigDecimal("ID"));
        sk.setBigDecimal("PVIP", sk.getBigDecimal("IP"));
        sk.setBigDecimal("PVSALDO", sk.getBigDecimal("SALDO"));
        sk.setBigDecimal("PVSSALDO", sk.getBigDecimal("SSALDO"));
        sk.setString("CSKSTAVKE", raSaldaKonti.findCSK(sk));
        sk.setString("CGKSTAVKE", ld.cgk);
        
        QueryDataSet psk = Skstavke.getDataModule().getTempSet(
            Condition.equal("CSKSTAVKE", ld.cskstavke));
        psk.open();
        if (psk.rowCount() == 0) {
            System.out.println("Dokument broj "+ld.cgk+" od partnera "+
            String.valueOf(ld.cpar) + " je pokriven s nepostojeæim dokumentom");
            return false;
        }
        
        BigDecimal sal = sk.getBigDecimal("SALDO");
            
        vm.setStavka(psk);
        if (vm.getMatchSide().equals("cracuna") != vm.isRacunTip())
            sal = sal.negate();
            
        raSaldaKonti.matchIznos(psk, sk, pok, sal);
        raTransaction.saveChanges(psk);
    }
    
    raTransaction.saveChanges(sk);
    raTransaction.saveChanges(pok);
    return true;
  }
  
  /*private String skKey(LinkData ld) {
    return ld.konto + "-" + ld.cpar;
  }
  
  private String finKey(LinkData ld) {
    return ld.corg;
  }*/
  
  static class LinkData {
    String corg, konto, cskstavke, brojdok, cgk;
    int cpar;
    BigDecimal id, ip;
    Timestamp datdok;
    
    public LinkData(DataSet row) {
        cskstavke = row.getString("CSKSTAVKE");
        konto = row.getString("BROJKONTA");
        brojdok = row.getString("BROJDOK");
        datdok = new Timestamp(row.getTimestamp("DATDOK").getTime());
        corg = row.getString("CORG");
        cpar = row.getInt("CPAR");
        id = ip = Aus.zero2;
        if (row.getBigDecimal("ID").signum() != 0)
            id = row.getBigDecimal("SALDO");
        else ip = row.getBigDecimal("SALDO");
    }
  }
  
/*  static class Total {
    List links = new ArrayList();
    int cpar;
    String corg, konto, cgk;
    BigDecimal saldo = Aus.zero2;
    
    public Total(LinkData ld) {
        cpar = ld.cpar;
        corg = ld.corg;
        konto = ld.konto;
        add(ld);
    }
    
    public void add(LinkData ld) {
        links.add(ld.cskstavke);
        saldo = saldo.add(ld.id).subtract(ld.ip);
    }
  }*/
}
