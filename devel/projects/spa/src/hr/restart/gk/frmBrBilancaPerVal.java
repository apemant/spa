/****license*****************************************************************
**   file: frmBrBilancaPerVal.java
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

public class frmBrBilancaPerVal extends frmBrutoBilanca {
  private static frmBrBilancaPerVal fBVal;
  hr.restart.zapod.jpGetValute jpVal = new hr.restart.zapod.jpGetValute();
  QueryDataSet qdsVal = new QueryDataSet();

//  public static frmBrBilancaPerVal frmBilVal() {
//    if(fBVal == null)
//      fBVal = new frmBrBilancaPerVal();
//    return fBVal;
//  }

//  static frmBrutoBilanca fbbpv;

  public frmBrBilancaPerVal() {
    super("tricky :)");
    try {
      jbInit();
      fBVal = this;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  public static frmBrutoBilanca getInstance() {
    System.out.println("GETING INSTANCE OF frmBrBilancaPerVal");
    if (fBVal == null) {
      fBVal = new frmBrBilancaPerVal();
    }
    return fBVal;
  }

  public void componentShow() {
    initialValues();
  }

  public void showDefaultValues(){
    super.showDefaultValues();
    jpVal.jtCVAL.setText("");
    jpVal.jtCVAL.emptyTextFields();
  }

  private void jbInit() throws Exception {
    qdsVal.setColumns(new Column[] {dm.createStringColumn("OZNVAL","Oznaka valute",3)});

    layDetail.setHeight(458);

    this.getJPan().add(jpVal, new XYConstraints(0, 145, -1, -1));


    jpVal.xYLayout1.setWidth(570);
    jpVal.setAlwaysSelected(true);
    jpVal.add(jpVal.jtCVAL, new XYConstraints(200, 20, 50, -1));
    jpVal.add(jpVal.jtNAZVAL, new XYConstraints(255, 20, 285, -1));
    jpVal.add(jpVal.jbGetVal,  new XYConstraints(545, 20, 21, 21));
    this.getJPan().add(jpVal, new XYConstraints(0, 150, -1, -1));

//    jpVal.jcbValuta.setText("Po valuti");
    jpVal.setRaDataSet(qdsVal);
    jpVal.initJP('N');
    jpVal.setAlwaysSelected(true);
  }

  public boolean Validacija(){
    if (vl.isEmpty(jpVal.jtOZNVAL)) return false;
    return super.Validacija();
  }

  public boolean noValuta;

  public void okPress(){
    noValuta = false;
    super.okPress();
    killAllReports();
    addingReport(kontoPanel.jlrKontoBroj.getText().equals(""));
  }

  public QueryDataSet getValutaSet(QueryDataSet olDS){
    val = qdsVal.getString("OZNVAL");
    olDS.first();
    BigDecimal tecaj = hr.restart.zapod.Tecajevi.getTecaj(vl.getToday(), qdsVal.getString("OZNVAL"));
    do {
      try {
        olDS.setBigDecimal("IP", olDS.getBigDecimal("IP").divide(tecaj, BigDecimal.ROUND_HALF_UP));
        olDS.setBigDecimal("ID", olDS.getBigDecimal("ID").divide(tecaj, BigDecimal.ROUND_HALF_UP));
        olDS.setBigDecimal("POCIP", olDS.getBigDecimal("POCIP").divide(tecaj, BigDecimal.ROUND_HALF_UP));
        olDS.setBigDecimal("POCID", olDS.getBigDecimal("POCID").divide(tecaj, BigDecimal.ROUND_HALF_UP));
      }
      catch (Exception ex) {
        noValuta =  true;
        setNoDataAndReturnImmediately();
      }
    } while (olDS.next());
    return olDS;
  }

  public String getNoDataMessage(){
    if (noValuta) return "Nema podataka za traženu valutu!";
    return "Nema podataka koji zadovoljavaju tražene uvjete!";
  }

  public boolean runFirstESC() {
    return super.runFirstESC();
  }

  public void firstESC() {
    super.firstESC();
  }

  static QueryDataSet tabQDS = new QueryDataSet();
  static QueryDataSet repQDS = new QueryDataSet();

  public static QueryDataSet getTableDS() {
    return tabQDS;
  }

  public QueryDataSet getRepDS() {
    return repDS2;
  }

  public String sValuta = jpVal.jtNAZVAL.getText();

  public void jptv_doubleClick() {
    if (!doubleClicked)
      kontoPanel.setNoLookup(true);
    doubleClicked = true;
//    System.out.println("\n"+this.getJPTV().getDataSet().getString("BROJKONTA"));
    if (this.getJPTV().getDataSet().getString("BROJKONTA").length() <4) {
      ll.addLast(this.kontoPanel.jlrKontoBroj.getText().trim());
      pl.addLast(this.getJPTV().getDataSet().getRow()+"");
      kontoPanel.jlrKontoBroj.setText(this.getJPTV().getDataSet().getString("BROJKONTA"));
      kontoPanel.jlrKontoBroj.forceFocLost();
      kontoPanel.setcORG(corgRemember);
      this.getJPTV().enableEvents(false);
      okPress();
      this.getJPTV().getDataSet().first();
      this.getJPTV().enableEvents(true);
    }
  }


  protected void addingReport(boolean rekapitulacija){
    this.addReport("hr.restart.gk.repBrutoBilancaRP2", "hr.restart.gk.repBrBilancaVal", "BrutoBilancaRP2", "Bruto bilanca - Bez naziva - PS - Promet - Saldo");
    this.addReport("hr.restart.gk.repBrutoBilancaRP3", "hr.restart.gk.repBrBilancaVal", "BrutoBilancaRP3", "Bruto bilanca - Bez naziva - Uk. promet - Saldo");
    this.addReport("hr.restart.gk.repBrutoBilancaRP4", "hr.restart.gk.repBrBilancaVal", "BrutoBilancaRP4", "Bruto bilanca - Bez naziva - PS - Promet - Uk. saldo");
    this.addReport("hr.restart.gk.repBrutoBilancaRP1", "hr.restart.gk.repBrBilancaVal", "BrutoBilancaRP1", "Bruto bilanca - Sa nazivom - Uk. promet - Saldo ");
    this.addReport("hr.restart.gk.repBrutoBilancaRP5", "hr.restart.gk.repBrBilancaVal", "BrutoBilancaRP5", "Bruto bilanca - Sa nazivom - PS - Promet - Uk. saldo - 3 reda");
    this.addReport("hr.restart.gk.repBrutoBilancaRP6", "hr.restart.gk.repBrBilancaVal", "BrutoBilancaRP6", "Bruto bilanca - Sa nazivom konta - Saldo");
    if(rekapitulacija){
      this.addReport("hr.restart.gk.repBrutoBilancaRP7", "hr.restart.gk.repBrBilancaRekapitulacijaVal", "BrutoBilancaRP7", "Rekapitulacija - PS - Promet - Saldo");
      this.addReport("hr.restart.gk.repBrutoBilancaRP8", "hr.restart.gk.repBrBilancaRekapitulacijaVal", "BrutoBilancaRP8", "Rekapitulacija - Uk. promet - Saldo");
      this.addReport("hr.restart.gk.repBrutoBilancaRP9", "hr.restart.gk.repBrBilancaRekapitulacijaVal", "BrutoBilancaRP9", "Rekapitulacija - PS - Promet - Uk. saldo - 3 reda");
//      this.addReport("hr.restart.gk.repBrutoBilancaRP9", "hr.restart.gk.repBrutoBilancaRP9", "BrutoBilancaRP9", "Rekapitulacija - PS - Promet - Uk. saldo - 3 reda");
    }
  }
//  public String getVALUTA(){
//    return val;
//  }

}