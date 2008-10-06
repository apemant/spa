/****license*****************************************************************
**   file: frmKarticeGKVal.java
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

import java.math.BigDecimal;
import java.sql.Date;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */


public class frmKarticeGKVal extends frmKarticeGK {

  QueryDataSet qdsVal = new QueryDataSet();
  Column colOznVal = new Column();
  public static String valutaOzn;
  static frmKarticeGKVal fKGK;
  hr.restart.zapod.jpGetValute jpVal = new hr.restart.zapod.jpGetValute();

  public frmKarticeGKVal() {
    super("");
    initializir();
  }

  public frmKarticeGKVal(boolean solo) {
    super(solo,0);
    initializir();
  }

//
//  public frmKarticeGKVal(boolean solo) {
//    super(solo,0);
//    initializir();
//  }
//

  private void initializir() {
  	dev = true;
    colOznVal.setCaption("Oznaka Valute");
    colOznVal.setColumnName("OZNVAL");
    colOznVal.setDataType(com.borland.dx.dataset.Variant.STRING);
    qdsVal.setColumns(new Column[] {colOznVal});
    qdsVal.open();
    jpVal.setBorderVisible(false);
    jpVal.setRaDataSet(qdsVal);
    jpVal.initJP('N');
    jpVal.xYLayout1.setWidth(570);
    jpVal.setAlwaysSelected(true);
    jpVal.add(jpVal.jtCVAL, new XYConstraints(200, 20, 50, -1));
    jpVal.add(jpVal.jtNAZVAL, new XYConstraints(255, 20, 285, -1));
    jpVal.add(jpVal.jbGetVal,  new XYConstraints(545, 20, 21, 21));
    this.getJPan().add(jpVal, new XYConstraints(0, 150, -1, -1));
  }

  public static frmKarticeGKVal getInstance() {
    return fKGK;
  }

  public boolean modifyOutSet() {
  	if (1==1) return true;
//    if (jpVal.jtOZNVAL.getText().equals(""))
//      {
//        rcc.EnabDisabAll(this.jpDetail, false);
//        JOptionPane.showConfirmDialog(this.jpVal,"Obavezan unos valute !","Greška",
//                                      JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//        rcc.setLabelLaF(this.jpVal.jtCVAL, true);
//        rcc.setLabelLaF(this.jpVal.jtOZNVAL, true);
//        rcc.setLabelLaF(this.jpVal.jtNAZVAL, true);
//        rcc.setLabelLaF(this.jpVal.jbGetVal, true);
//        jpVal.jtOZNVAL.requestFocus();
//        return false;
//      }
    BigDecimal tecaj = new BigDecimal(0);
    BigDecimal tecaj1 = new BigDecimal(0);
//    Date sDate;
    //Timestamp sDate;
    if(this.isPrikazSalda()) {//jcbSaldo.isSelected()) {
      if(stds.getString("SLJED").equals("D")) { //jrbDatDokum.isSelected()) {
//        sDate = new Date(outSet.getTimestamp("DATDOK").getTime());
        //sDate = outSet.getTimestamp("DATDOK");
        datdok = true;
      } else {
//        sDate = new Date(outSet.getTimestamp("DATUMKNJ").getTime());
        //sDate = outSet.getTimestamp("DATUMKNJ");
        datdok = false;
      }
    } else {
//      sDate = new Date(outSet.getTimestamp("DATDOK").getTime());
      //sDate = outSet.getTimestamp("DATDOK");
      datdok = true;
    }
    outSet.addColumn((Column)dm.getTecajevi().getColumn("TECSRED").clone());
    outSet.first();
    Date sDate = new Date(vl.getToday().getTime());
    tecaj = hr.restart.zapod.Tecajevi.getTecaj(sDate, qdsVal.getString("OZNVAL"));
    tecaj1 = hr.restart.zapod.Tecajevi.getTecaj1(sDate, qdsVal.getString("OZNVAL"));
    do {
      if (tecaj.equals(new BigDecimal(0))) {
//        rcc.EnabDisabAll(this.jpDetail, false);
//        JOptionPane.showConfirmDialog(this.jpVal,"Nema podataka za traženu valutu !","Upozorenje",
//                                      JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//        rcc.setLabelLaF(this.jpVal.jtCVAL, true);
//        rcc.setLabelLaF(this.jpVal.jtOZNVAL, true);
//        rcc.setLabelLaF(this.jpVal.jtNAZVAL, true);
//        rcc.setLabelLaF(this.jpVal.jbGetVal, true);
//        jpVal.jtOZNVAL.requestFocus();
        return false;
      }
      outSet.setBigDecimal("TECSRED", tecaj);
      outSet.setBigDecimal("IP", outSet.getBigDecimal("IP").divide(tecaj1, BigDecimal.ROUND_HALF_UP));
      outSet.setBigDecimal("ID", outSet.getBigDecimal("ID").divide(tecaj1, BigDecimal.ROUND_HALF_UP));
      outSet.next();
    } while(outSet.inBounds());
    //setReportType();
    getJPTV().requestFocus();
    outSet.getColumn("TECSRED").setVisible(0);
    valutaOzn = qdsVal.getString("OZNVAL");
    return true;
  }

  protected void selectReport() {
    //this.getRepRunner().clearAllReports();
    this.killAllReports();

    /*if (this.isPrikazSalda()) //jcbSaldo.isSelected())
      this.addReport("hr.restart.gk.repKarticeGKVal_Saldirana", "Ispis izvještaja konto kartica u valuti - saldo", 5);
    else*/
    this.addReport("hr.restart.gk.repKarticeGKVal", "hr.restart.gk.repKarticeGKVal", "KarticeGKVal2", "Ispis izvještaja konto kartica");
  }

  public void firstESC(){
    /*if(!jpVal.jtOZNVAL.isEnabled()) {
      rcc.setLabelLaF(this.jpVal.jtCVAL, true);
      rcc.setLabelLaF(this.jpVal.jtOZNVAL, true);
      rcc.setLabelLaF(this.jpVal.jtNAZVAL, true);
      rcc.setLabelLaF(this.jpVal.jbGetVal, true);
      getJPTV().setDataSet(null);
      jpVal.jtOZNVAL.requestFocus();
    } else*/ if(!jtfPocDatum.isEnabled()) {
    	
    	rcc.setLabelLaF(this.jpVal.jtCVAL, true);
      rcc.setLabelLaF(this.jpVal.jtOZNVAL, true);
      rcc.setLabelLaF(this.jpVal.jtNAZVAL, true);
      rcc.setLabelLaF(this.jpVal.jbGetVal, true);
      getJPTV().setDataSet(null);
    	
//      rcc.setLabelLaF(jtfPocDatum, true);
//      rcc.setLabelLaF(jtfZavDatum, true);
//      rcc.setLabelLaF(this.rcbPrivremenost, true);
//      rcc.setLabelLaF(this.rcbSaldo, true);
      qdsVal.setString("OZNVAL","");
      jpVal.jtOZNVAL.forceFocLost();
      stds.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().findFirstDayOfYear());
      stds.setTimestamp("zavDatum", hr.restart.util.Util.getUtil().getLastDayOfMonth());
// //      this.jcbPrivremeni.setSelected(false);
//      setPrivremeno("0");
//      setSaldo("N");
// //      this.jcbSaldo.setSelected(false);
// //      buttonGroup1.setSelected(jrbDatKnjiz);
//      setSljed("K");
//      rcc.setLabelLaF(rcbSljed,true);
// //      if(this.isPrikazSalda()) /*jcbSaldo.isSelected())*/ //rcc.EnabDisabAll(this.jPanel1, true);
//      jtfPocDatum.requestFocus();
//    } else if(!kontoPanel.jlrKontoBroj.isEnabled()) {
      rcc.setLabelLaF(this.jtfPocDatum, true);
      rcc.setLabelLaF(this.jtfZavDatum, true);
      rcc.setLabelLaF(this.rcbPrivremenost, true);
      rcc.setLabelLaF(this.rcbSaldo, true);
      rcc.setLabelLaF(kontoPanel.jlrKontoNaziv, true);
      rcc.setLabelLaF(kontoPanel.jlrKontoBroj, true);
      rcc.setLabelLaF(kontoPanel.jbSelBrKon, true);
      rcc.setLabelLaF(rcbSljed,true);
      kontoPanel.jlrKontoBroj_lookup();
      if (kontoPanel.jlrCorg.isEnabled()) kontoPanel.jlrCorg.requestFocus();
      else kontoPanel.jlrKontoBroj.requestFocus();
    }
    else if(kontoPanel.jlrCorg.hasFocus()) {
      rcc.setLabelLaF(kontoPanel.jlrKontoNaziv, true);
      rcc.setLabelLaF(kontoPanel.jlrKontoBroj, true);
      rcc.setLabelLaF(kontoPanel.jbSelBrKon, true);
      kontoPanel.jlrKontoBroj.requestFocus();
    } else if (!kontoPanel.jlrKontoBroj.getText().equals("")){
      kontoPanel.setcORG(hr.restart.zapod.OrgStr.getKNJCORG());
      kontoPanel.jlrKontoBroj.setText("");
      kontoPanel.jlrKontoBroj.forceFocLost();
      kontoPanel.jlrKontoBroj.requestFocus();
    }
  }

  public boolean runFirstESC() {
//    if(!jpVal.jtOZNVAL.isEnabled() || !kontoPanel.jlrKontoBroj.isEnabled()
//       || !this.jtfPocDatum.isEnabled() || kontoPanel.jlrCorg.hasFocus()
//       || !kontoPanel.jlrKontoBroj.getText().equals(""))
//      return true;
//    else
//      return false;
    return (!jpVal.jtOZNVAL.isEnabled()
            || !kontoPanel.jlrKontoBroj.isEnabled()
            || !this.jtfPocDatum.isEnabled()
            || kontoPanel.jlrCorg.hasFocus()
            || !kontoPanel.jlrKontoBroj.getText().equals(""));
  }

  public void componentShow() {
    rcc.EnabDisabAll(jpDetail, true);
    super.componentShow();
    fKGK = this;
  }
  
  public void initialValues() {
    super.initialValues();
    qdsVal.setString("OZNVAL","");
    jpVal.jtOZNVAL.forceFocLost();
  }
}