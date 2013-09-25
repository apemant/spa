/****license*****************************************************************
**   file: frmPDV.java
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
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.IzvjPDV;
import hr.restart.baza.StIzvjPDV;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;
import hr.restart.util.sysoutTEST;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import hr.restart.util.VarStr;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmPDV extends raUpitLite {

  Util ut = Util.getUtil();
  Valid vl = Valid.getValid();
  raIzvjPDV rip = raIzvjPDV.getInstance();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  boolean pdvK = false;

  String vezaSkUi = "skstavke.KNJIG = uistavke.KNJIG " +
                    "and skstavke.CPAR = uistavke.CPAR " +
                    "and skstavke.VRDOK = uistavke.VRDOK " +
                    "and skstavke.BROJDOK = uistavke.BROJDOK ";

  JPanel mainPanel = new JPanel();
  JPanel datePanel = new JPanel();
  raJpanelPdv panelPDV = new raJpanelPdv(this);
  raJpanelPdv_K panelPDV_K = new raJpanelPdv_K(this);

  StorageDataSet stds = new StorageDataSet();
  StorageDataSet reportSet = new StorageDataSet();
 
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  JLabel jlPer = new JLabel("Razdoblje");
  JraTextField jraPoctDat = new JraTextField();
  JraTextField jraKrajDat = new JraTextField();
  XYLayout xYlay = new XYLayout();
  BorderLayout bLay = new BorderLayout();
  boolean finalIspis;
  JraButton jbSwitchPdv_PdvK = new JraButton();

  static frmPDV fpdv;

  public frmPDV() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    fpdv = this;
  }

  public static frmPDV getInstance(){
    if (fpdv == null) fpdv = new frmPDV();
    return fpdv;
  }

  public void componentShow() {
    defaultValues();
//    System.out.println("SQL : " + getSQL());
  }

  private String getSQL() {
    String sql = "select uistavke.*,skstavke.ssaldo from skstavke,uistavke where "+
                 vezaSkUi + "and skstavke.datpri between '" + Util.getUtil().getFirstSecondOfDay(stds.getTimestamp("DATUMOD")) +
                 "' and '" + Util.getUtil().getLastSecondOfDay(stds.getTimestamp("DATUMDO")) + "' and skstavke.knjig = '" + hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG() + "'";
    return sql;
  }

  boolean calc = false;
  public void okPress() {
//    switchPDV13();
    if (isEU13()) {
      killAllReports();
      try {
        Class.forName("hr.restart.sk.repPDVDisk");
        this.addReport("hr.restart.sk.repPDVDisk", "Datoteka PDV za e-poreznu");
      } catch (Exception e) {
      }
//      switchPDV13();
      finalIspis = true;
    } else {
      killAllReports();
      addPDVreports();
      calc = calcPPDV();
  //    System.out.println("getSQL : " + getSQL());
      reportSet.enableDataSetEvents(false);
      StorageDataSet tmpSet = rip.napuniPDVSet(ut.getNewQueryDataSet(getSQL()));
      String pfx = askVersionPDV();
      processReportSet(tmpSet, pfx);
  //    sysoutTEST syst = new sysoutTEST(false);
  //    syst.prn(reportSet);
    }
  }

  private boolean isEU13() {
    Calendar c = Calendar.getInstance();
    c.set(2013, 5, 1);
    long time13 = c.getTimeInMillis();
    if (stds.getTimestamp("DATUMOD").after(Util.getUtil().getLastDayOfMonth(new Timestamp(time13)))) {
      System.err.println("OP OP OPAAA dolazi EUropaaa");
      return eu13warn();
    }
    return false;
  }
  private boolean eu13warn() {
    JOptionPane.showMessageDialog(getWindow(), "Obrazac PDV i PDV-K za razdoblje nakon 01.07.2013. moguæe je izraditi preko opcije izbornika 'Obrasci za poreznu upravu'");
    return false;
  }

  private String askVersionPDV() {
//    int answ = JOptionPane.showOptionDialog(this, "Odaberite verziju obrasca za ispis","Obrazac PDV",
//        JOptionPane.DEFAULT_OPTION, JOptisonPane.QUESTION_MESSAGE,
//        null, new String[] {"za 2006 i dalje", "za 2005 i ranije"},"za 2006 i dalje");
//    if (answ == 1) return "05";
    if (stds.getTimestamp("DATUMDO").after(Util.getUtil().getYearEnd("2005"))) {
      return "";
    } else {
      return "05";
    }
  }

  public void afterOKPress(){
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        frmPDV.this.requestFocus();
        getOKPanel().jBOK.requestFocus();
        rcc.setLabelLaF(jraPoctDat,false);
        rcc.setLabelLaF(jraKrajDat,false);
        rcc.setLabelLaF(jbSwitchPdv_PdvK, !isEU13());//true
        panelPDV.disableDownwards(true);
        panelPDV_K.disableDownwards(true);
        reportSet.enableDataSetEvents(true);
        if (finalIspis && !pdvK){
          panelPDV.I1.requestFocus();
          panelPDV.I1.selectAll();
        } else if (finalIspis && !pdvK){
          panelPDV_K.VII11.requestFocus();
          panelPDV_K.VII11.selectAll();
        }
      }
    });
  }
  
  void processReportSet(StorageDataSet tmpSet){
    processReportSet(tmpSet, "");
  }
  
  void processReportSet(StorageDataSet tmpSet, String pfx){
    if (!reportSet.open()){
      reportSet.deleteAllRows();
    }
    reportSet.insertRow(false);
    for (int i = 0; i < tmpSet.getColumnCount(); i++) {
      System.out.println("reportSet Column COL"+(i)+" = " + tmpSet.getColumn(i).getCaption()+" = " +tmpSet.getBigDecimal(i));

      if (tmpSet.getColumn(i).getCaption().equals(pfx+"I.1.")){
        reportSet.setBigDecimal("BEZ_POREZA",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_I",reportSet.getBigDecimal("UKUPNO_I").add(tmpSet.getBigDecimal(i)));
        suma_I_II(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"I.2.1.")){
        reportSet.setBigDecimal("TUZEMNE",tmpSet.getBigDecimal(i));//IZVOZNE!!!
        reportSet.setBigDecimal("UKUPNO_I",reportSet.getBigDecimal("UKUPNO_I").add(tmpSet.getBigDecimal(i)));
        reportSet.setBigDecimal("UKUPNO_I2",reportSet.getBigDecimal("UKUPNO_I2").add(tmpSet.getBigDecimal(i)));
        suma_I_II(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"I.2.2.")){
        reportSet.setBigDecimal("PRIJEVOZ",tmpSet.getBigDecimal(i));//TUZEMNE!!!
        reportSet.setBigDecimal("UKUPNO_I",reportSet.getBigDecimal("UKUPNO_I").add(tmpSet.getBigDecimal(i)));
        reportSet.setBigDecimal("UKUPNO_I2",reportSet.getBigDecimal("UKUPNO_I2").add(tmpSet.getBigDecimal(i)));
        suma_I_II(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"I.2.3.")){
        reportSet.setBigDecimal("IZVOZNE",tmpSet.getBigDecimal(i));//TUZEMNE!!!
        reportSet.setBigDecimal("UKUPNO_I",reportSet.getBigDecimal("UKUPNO_I").add(tmpSet.getBigDecimal(i)));
        reportSet.setBigDecimal("UKUPNO_I2",reportSet.getBigDecimal("UKUPNO_I2").add(tmpSet.getBigDecimal(i)));
        suma_I_II(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"I.2.4.")){
        reportSet.setBigDecimal("OSTALO_I24",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_I",reportSet.getBigDecimal("UKUPNO_I").add(tmpSet.getBigDecimal(i)));
        reportSet.setBigDecimal("UKUPNO_I2",reportSet.getBigDecimal("UKUPNO_I2").add(tmpSet.getBigDecimal(i)));
        suma_I_II(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"I.3.")){
        reportSet.setBigDecimal("STOPA_NULA",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_I",reportSet.getBigDecimal("UKUPNO_I").add(tmpSet.getBigDecimal(i)));
//#ai        reportSet.setBigDecimal("UKUPNO_I2",reportSet.getBigDecimal("UKUPNO_I2").add(tmpSet.getBigDecimal(i)));
        suma_I_II(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"II.1.v")){
        reportSet.setBigDecimal("IZDANI_RACUNI_V",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_II_V",reportSet.getBigDecimal("UKUPNO_II_V").add(tmpSet.getBigDecimal(i)));
        suma_I_II(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"II.1.p")){
        reportSet.setBigDecimal("IZDANI_RACUNI_P",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_II_P",reportSet.getBigDecimal("UKUPNO_II_P").add(tmpSet.getBigDecimal(i)));
        add_II_IIIP(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"II.2.v")){
        reportSet.setBigDecimal("NEZARACUNANE_V",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_II_V",reportSet.getBigDecimal("UKUPNO_II_V").add(tmpSet.getBigDecimal(i)));
        suma_I_II(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"II.2.p")){
        reportSet.setBigDecimal("NEZARACUNANE_P",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_II_P",reportSet.getBigDecimal("UKUPNO_II_P").add(tmpSet.getBigDecimal(i)));
        add_II_IIIP(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"II.3.v")){
        reportSet.setBigDecimal("R23_V",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_II_V",reportSet.getBigDecimal("UKUPNO_II_V").add(tmpSet.getBigDecimal(i)));
        suma_I_II(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"II.3.p")){
        reportSet.setBigDecimal("R23_P",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_II_P",reportSet.getBigDecimal("UKUPNO_II_P").add(tmpSet.getBigDecimal(i)));
        add_II_IIIP(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"II.4.v")){
        reportSet.setBigDecimal("VLASTITA_POT_V",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_II_V",reportSet.getBigDecimal("UKUPNO_II_V").add(tmpSet.getBigDecimal(i)));
        suma_I_II(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"II.4.p")){
        reportSet.setBigDecimal("VLASTITA_POT_P",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_II_P",reportSet.getBigDecimal("UKUPNO_II_P").add(tmpSet.getBigDecimal(i)));
        add_II_IIIP(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"II.5.v")){
        reportSet.setBigDecimal("NENAP_IZVOZ_V",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_II_V",reportSet.getBigDecimal("UKUPNO_II_V").add(tmpSet.getBigDecimal(i)));
        suma_I_II(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"II.5.p")){
        reportSet.setBigDecimal("NENAP_IZVOZ_P",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_II_P",reportSet.getBigDecimal("UKUPNO_II_P").add(tmpSet.getBigDecimal(i)));
        add_II_IIIP(tmpSet.getBigDecimal(i));
        continue;
      /*} else if (tmpSet.getColumn(i).getCaption().equals(pfx+"II.5.v")){
        reportSet.setBigDecimal("NAK_OSL_IZV_V",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_II_V",reportSet.getBigDecimal("UKUPNO_II_V").add(tmpSet.getBigDecimal(i)));
        suma_I_II(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"II.5.p")){
        reportSet.setBigDecimal("NAK_OSL_IZV_P",tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_II_P",reportSet.getBigDecimal("UKUPNO_II_P").add(tmpSet.getBigDecimal(i)));
        add_II_IIIP(tmpSet.getBigDecimal(i));
        continue;*/
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.1.v")){
        reportSet.setBigDecimal("PPOR_PR_RAC_V", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_V",reportSet.getBigDecimal("UKUPNO_III_V").add(tmpSet.getBigDecimal(i)));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.1.p")){
        reportSet.setBigDecimal("PPOR_PR_RAC_P", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_P",reportSet.getBigDecimal("UKUPNO_III_P").add(tmpSet.getBigDecimal(i)));
        subtract_II_IIIP(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.2.v")){
        reportSet.setBigDecimal("PL_PPOR_UVOZ_V", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_V",reportSet.getBigDecimal("UKUPNO_III_V").add(tmpSet.getBigDecimal(i)));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.2.p")){
        reportSet.setBigDecimal("PL_PPOR_UVOZ_P", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_P",reportSet.getBigDecimal("UKUPNO_III_P").add(tmpSet.getBigDecimal(i)));
        subtract_II_IIIP(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.3.v")){
        reportSet.setBigDecimal("PL_PPOR_USLUGE_V", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_V",reportSet.getBigDecimal("UKUPNO_III_V").add(tmpSet.getBigDecimal(i)));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.3.p")){
        reportSet.setBigDecimal("PL_PPOR_USLUGE_P", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_P",reportSet.getBigDecimal("UKUPNO_III_P").add(tmpSet.getBigDecimal(i)));
        subtract_II_IIIP(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.6.") 
          || tmpSet.getColumn(i).getCaption().equals(pfx+"III.8.")){
        reportSet.setBigDecimal("ISPRAVCI_PPORA", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_P",reportSet.getBigDecimal("UKUPNO_III_P").add(tmpSet.getBigDecimal(i)));
        subtract_II_IIIP(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.4.p")){
        reportSet.setBigDecimal("III4P", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_P",reportSet.getBigDecimal("UKUPNO_III_P").add(tmpSet.getBigDecimal(i)));
        subtract_II_IIIP(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.4.v")){
        reportSet.setBigDecimal("III4V", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_V",reportSet.getBigDecimal("UKUPNO_III_V").add(tmpSet.getBigDecimal(i)));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.5.p")){
        reportSet.setBigDecimal("III5P", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_P",reportSet.getBigDecimal("UKUPNO_III_P").add(tmpSet.getBigDecimal(i)));
        subtract_II_IIIP(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.5.v")){
        reportSet.setBigDecimal("III5V", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_V",reportSet.getBigDecimal("UKUPNO_III_V").add(tmpSet.getBigDecimal(i)));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.6.p")){
        reportSet.setBigDecimal("III6P", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_P",reportSet.getBigDecimal("UKUPNO_III_P").add(tmpSet.getBigDecimal(i)));
        subtract_II_IIIP(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.6.v")){
        reportSet.setBigDecimal("III6V", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_V",reportSet.getBigDecimal("UKUPNO_III_V").add(tmpSet.getBigDecimal(i)));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.7.p")){
        reportSet.setBigDecimal("III7P", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_P",reportSet.getBigDecimal("UKUPNO_III_P").add(tmpSet.getBigDecimal(i)));
        subtract_II_IIIP(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"III.7.v")){
        reportSet.setBigDecimal("III7V", tmpSet.getBigDecimal(i));
        reportSet.setBigDecimal("UKUPNO_III_V",reportSet.getBigDecimal("UKUPNO_III_V").add(tmpSet.getBigDecimal(i)));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"VII.1.1")){
        reportSet.setBigDecimal("NAB_NEK_ISPOR", tmpSet.getBigDecimal(i));
        suma_VII1(tmpSet.getBigDecimal(i));
        suma_VII(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"VII.1.2.")){
        reportSet.setBigDecimal("PRO_NEK_PRIM", tmpSet.getBigDecimal(i));
        suma_VII1(tmpSet.getBigDecimal(i));
        suma_VII(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"VII.1.3.")){
        reportSet.setBigDecimal("NAB_OSOB_VOZIL", tmpSet.getBigDecimal(i));
        suma_VII1(tmpSet.getBigDecimal(i));
        suma_VII(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"VII.1.4.")){
        reportSet.setBigDecimal("PRO_OSOB_VOZIL", tmpSet.getBigDecimal(i));
        suma_VII1(tmpSet.getBigDecimal(i));
        suma_VII(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"VII.1.5.")){
        reportSet.setBigDecimal("NAB_RAB_VOZIL", tmpSet.getBigDecimal(i));
        suma_VII1(tmpSet.getBigDecimal(i));
        suma_VII(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"VII.1.6.")){
        reportSet.setBigDecimal("NAB_OSTALO", tmpSet.getBigDecimal(i));
        suma_VII1(tmpSet.getBigDecimal(i));
        suma_VII(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"VII.1.7.")){
        reportSet.setBigDecimal("PRO_OSTALO", tmpSet.getBigDecimal(i));
        suma_VII1(tmpSet.getBigDecimal(i));
        suma_VII(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"VII.2.")){
        reportSet.setBigDecimal("OTUDJ_STJEC", tmpSet.getBigDecimal(i));
        suma_VII(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"VII.3.")){
        reportSet.setBigDecimal("VII3", tmpSet.getBigDecimal(i));
        suma_VII(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"VII.4.")){
        reportSet.setBigDecimal("VII4", tmpSet.getBigDecimal(i));
        suma_VII(tmpSet.getBigDecimal(i));
        continue;
      } else if (tmpSet.getColumn(i).getCaption().equals(pfx+"VII.5.")){
        reportSet.setBigDecimal("VII5", tmpSet.getBigDecimal(i));
        suma_VII(tmpSet.getBigDecimal(i));
        continue;
      }
    }
    reportSet.setBigDecimal("RAZLIKA", (reportSet.getBigDecimal("POR_OBV").subtract(reportSet.getBigDecimal("PO_PRETHOD_OBR"))));
    finalIspis = true;
    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
    st.prn(reportSet);
    changeIcon(1);
  }

  void suma_I_II(BigDecimal iznos){
    reportSet.setBigDecimal("UKUPNO_I_II",reportSet.getBigDecimal("UKUPNO_I_II").add(iznos));
  }

  void suma_VII(BigDecimal iznos){
//    reportSet.setBigDecimal("UKUPNO_VII",reportSet.getBigDecimal("UKUPNO_VII").add(iznos));
  }

  void suma_VII1(BigDecimal iznos){
    reportSet.setBigDecimal("UKUPNO_VII1",reportSet.getBigDecimal("UKUPNO_VII1").add(iznos));
  }

  void add_II_IIIP(BigDecimal iznos){
    reportSet.setBigDecimal("POR_OBV", reportSet.getBigDecimal("POR_OBV").add(iznos));
  }

  void subtract_II_IIIP(BigDecimal iznos){
    reportSet.setBigDecimal("POR_OBV", reportSet.getBigDecimal("POR_OBV").subtract(iznos));
  }
  boolean calcPPDV() {
    return (frmParam.getParam("sk", "calcPPDV", "N", "Automatski izracun iznosa na PDV i PDV-k obrascu",true ).equals("D"));
  }
  public void focusLostEv(String source){
    if (!calc) return;
    reportSet.setBigDecimal("UKUPNO_II_V", (
        reportSet.getBigDecimal("IZDANI_RACUNI_V").add(reportSet.getBigDecimal("NEZARACUNANE_V")).add(
        reportSet.getBigDecimal("VLASTITA_POT_V")).add(reportSet.getBigDecimal("NENAP_IZVOZ_V")).add(
        reportSet.getBigDecimal("NAK_OSL_IZV_V"))));
    reportSet.setBigDecimal("UKUPNO_II_P", (
        reportSet.getBigDecimal("IZDANI_RACUNI_P").add(reportSet.getBigDecimal("NEZARACUNANE_P")).add(
        reportSet.getBigDecimal("VLASTITA_POT_P")).add(reportSet.getBigDecimal("NENAP_IZVOZ_P")).add(
        reportSet.getBigDecimal("NAK_OSL_IZV_P"))));

    reportSet.setBigDecimal("UKUPNO_III_V", (
        reportSet.getBigDecimal("PPOR_PR_RAC_V").add(reportSet.getBigDecimal("PL_PPOR_UVOZ_V")).add(
        reportSet.getBigDecimal("PL_PPOR_USLUGE_V"))));
    reportSet.setBigDecimal("UKUPNO_III_P", (
        reportSet.getBigDecimal("PPOR_PR_RAC_P").add(reportSet.getBigDecimal("PL_PPOR_UVOZ_P")).add(
        reportSet.getBigDecimal("PL_PPOR_USLUGE_P")).add(reportSet.getBigDecimal("ISPRAVCI_PPORA"))));

    reportSet.setBigDecimal("UKUPNO_I2",(reportSet.getBigDecimal("TUZEMNE").add(reportSet.getBigDecimal("IZVOZNE"))));
    reportSet.setBigDecimal("UKUPNO_I",(reportSet.getBigDecimal("UKUPNO_I2").add(reportSet.getBigDecimal("BEZ_POREZA")).add(
        reportSet.getBigDecimal("STOPA_NULA"))));
    
    reportSet.setBigDecimal("UKUPNO_I_II",(reportSet.getBigDecimal("UKUPNO_I")/*.add(reportSet.getBigDecimal("UKUPNO_I2"))*/.add(
        reportSet.getBigDecimal("UKUPNO_II_V"))));

    reportSet.setBigDecimal("POR_OBV", (reportSet.getBigDecimal("UKUPNO_II_P").subtract(reportSet.getBigDecimal("UKUPNO_III_P"))));
//    reportSet.setBigDecimal("RAZLIKA", reportSet.getBigDecimal("POR_OBV"));
    reportSet.setBigDecimal("RAZLIKA", reportSet.getBigDecimal("POR_OBV").add(reportSet.getBigDecimal("PO_PRETHOD_OBR")));
  }

  public void focusLostEvK(){
    if (!calc) return;
    reportSet.setBigDecimal("UKUPNO_VII1", reportSet.getBigDecimal("NAB_NEK_ISPOR").add(reportSet.getBigDecimal("PRO_NEK_PRIM").add(
        reportSet.getBigDecimal("NAB_OSOB_VOZIL").add(reportSet.getBigDecimal("PRO_OSOB_VOZIL").add(
        reportSet.getBigDecimal("NAB_RAB_VOZIL").add(reportSet.getBigDecimal("NAB_OSTALO").add(
        reportSet.getBigDecimal("PRO_OSTALO"))))))));
//    reportSet.setBigDecimal("UKUPNO_VII", reportSet.getBigDecimal("UKUPNO_VII1")
//        .add(reportSet.getBigDecimal("OTUDJ_STJEC"))
//        );
  }
  
  public void focusLostV(){
    if (!calc) return;
//    System.out.println("PPO - " + reportSet.getBigDecimal("PO_PRETHOD_OBR"));
    reportSet.setBigDecimal("RAZLIKA", reportSet.getBigDecimal("POR_OBV").add(reportSet.getBigDecimal("PO_PRETHOD_OBR")));
  }

  public void firstESC() {
    rcc.setLabelLaF(jbSwitchPdv_PdvK, false);
    panelPDV_K.clearNapomena();
    rcc.setLabelLaF(jraPoctDat,true);
    rcc.setLabelLaF(jraKrajDat,true);
    panelPDV.disableDownwards(false);
    panelPDV_K.disableDownwards(false);
    if (pdvK){
      switchButton();
    }
    try {
      reportSet.deleteAllRows();
      reportSet.insertRow(false);
//      panelPDV.rebindFields();
//      panelPDV_K.rebindFields();
      jraPoctDat.requestFocus();
      if (finalIspis){
        changeIcon(2);
        finalIspis = false;
      }
    }
    catch (Exception ex) {
//      System.out.println("Exception : " + ex.getMessage());
//      System.out.println("Comment by RADT : NEVAZNO!!!!");
    }
  }

  public boolean runFirstESC() {
    if (jraPoctDat.isEnabled())
      return false;
    return true;
  }

  void defaultValues(){
    rcc.setLabelLaF(jbSwitchPdv_PdvK, false);
    panelPDV_K.clearNapomena();
    // ???
//    panelPDV.rebindFields();
//    panelPDV_K.rebindFields();
    // ???
    panelPDV.disableDownwards(false);
    panelPDV_K.disableDownwards(false);
    stds.setTimestamp("DATUMOD", ut.getFirstDayOfMonth(ut.addMonths(vl.getToday(), -1)));
    stds.setTimestamp("DATUMDO", ut.getLastDayOfMonth(ut.addMonths(vl.getToday(), -1)));
    // <TEST>
//    stds.setTimestamp("DATUMOD", java.sql.Timestamp.valueOf("2002-01-01 00:00:00.0"));
//    stds.setTimestamp("DATUMDO", java.sql.Timestamp.valueOf("2002-12-31 23:59:59.9"));
    // </TEST>
    finalIspis = false;
    if (panelPDV13 != null) {
      mainPanel.remove(panelPDV13);
      mainPanel.add(panelPDV);
      pdvK = false;
    }
    panelPDV.rebindFields();
    panelPDV_K.rebindFields();
  }

  public boolean isIspis() {
    return finalIspis;
  }

  public boolean ispisNow() {
    return false;
  }

  private void jbInit() throws Exception {
    datePanel.setLayout(xYlay);
    mainPanel.setLayout(bLay);
    xYlay.setWidth(700);
    xYlay.setHeight(50);
    formatReportSet();
    stds.setColumns(new Column[] {
      dm.createTimestampColumn("DATUMOD"),
      dm.createTimestampColumn("DATUMDO")
    });

    jraPoctDat.setDataSet(stds);
    jraPoctDat.setColumnName("DATUMOD");

    jraKrajDat.setDataSet(stds);
    jraKrajDat.setColumnName("DATUMDO");

    setJPan(mainPanel);
    jbSwitchPdv_PdvK.setToolTipText("F6");
    jbSwitchPdv_PdvK.setText("Obrazac PDV-K");
    jbSwitchPdv_PdvK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbSwitchPdv_PdvK_actionPerformed(e);
      }
    });
    datePanel.add(jlPer, new XYConstraints(15,15,-1,-1));
    datePanel.add(jraPoctDat, new XYConstraints(150, 15, 100, -1));
    datePanel.add(jraKrajDat, new XYConstraints(255, 15, 100, -1));
    datePanel.add(jbSwitchPdv_PdvK, new XYConstraints(360, 15, 180, 21));
    mainPanel.add(datePanel, BorderLayout.NORTH);
    mainPanel.add(panelPDV, BorderLayout.CENTER);
//    this.addReport("hr.restart.sk.repPrijavaPDV", "Obrazac PDV", 2);
//    this.addReport("hr.restart.sk.repPrijavaPDV_K", "Obrazac PDV-K", 2);
//    try {
//      Class.forName("hr.restart.sk.repPDVDisk");
//      this.addReport("hr.restart.sk.repPDVDisk", "Datoteka PDV za e-poreznu");
//    } catch (Exception e) {
//    }
    addPDVreports();
  }

  private void addPDVreports() {
    this.addJasper("hr.restart.sk.repPrijavaPDVj","hr.restart.sk.repPrijavaPDV_K","pdv25.jrxml","Obrazac PDV od 01.03.2012 (25%)");
    this.addJasper("hr.restart.sk.repPrijavaPDVj","hr.restart.sk.repPrijavaPDV_K","pdv10.jrxml","Obrazac PDV 2010");
    this.addJasper("hr.restart.sk.repPrijavaPDVj","hr.restart.sk.repPrijavaPDV_K","pdv09.jrxml","Obrazac PDV 2009");
    this.addReport("hr.restart.sk.repPrijavaPDV","hr.restart.sk.repPrijavaPDV_K","PrijavaPDV06","Obrazac PDV 22%");
    this.addJasper("hr.restart.sk.repPrijavaPDV_Kj","hr.restart.sk.repPrijavaPDV_K","pdvk12.jrxml","Obrazac PDV-K 2012");
    this.addJasper("hr.restart.sk.repPrijavaPDV_Kj","hr.restart.sk.repPrijavaPDV_K","pdvk10.jrxml","Obrazac PDV-K 2010");
    this.addJasper("hr.restart.sk.repPrijavaPDV_Kj","hr.restart.sk.repPrijavaPDV_K","pdvk09.jrxml","Obrazac PDV-K 2009");
    this.addReport("hr.restart.sk.repPrijavaPDV_K","hr.restart.sk.repPrijavaPDV_K","PrijavaPDV_K06","Obrazac PDV-K 22%");    
/*    this.addReport("hr.restart.sk.repPrijavaPDV","hr.restart.sk.repPrijavaPDV_K","PrijavaPDV","Obrazac PDV 2005");
    this.addReport("hr.restart.sk.repPrijavaPDV_K","hr.restart.sk.repPrijavaPDV_K","PrijavaPDV_K","Obrazac PDV-K 2005");*/
  }

  void formatReportSet(){
    reportSet.setColumns(new Column[] {
      dm.createBigDecimalColumn("UKUPNO_I_II",2),
      dm.createBigDecimalColumn("UKUPNO_I",2),
      dm.createBigDecimalColumn("BEZ_POREZA",2),
      dm.createBigDecimalColumn("UKUPNO_I2",2),
      dm.createBigDecimalColumn("TUZEMNE",2),
      dm.createBigDecimalColumn("IZVOZNE",2),
      dm.createBigDecimalColumn("STOPA_NULA",2),
      dm.createBigDecimalColumn("UKUPNO_II_V",2),
      dm.createBigDecimalColumn("UKUPNO_II_P",2),
      dm.createBigDecimalColumn("IZDANI_RACUNI_V",2),
      dm.createBigDecimalColumn("IZDANI_RACUNI_P",2),
      dm.createBigDecimalColumn("NEZARACUNANE_V",2),
      dm.createBigDecimalColumn("NEZARACUNANE_P",2),
      dm.createBigDecimalColumn("VLASTITA_POT_V",2),
      dm.createBigDecimalColumn("VLASTITA_POT_P",2),
      dm.createBigDecimalColumn("NENAP_IZVOZ_V",2),
      dm.createBigDecimalColumn("NENAP_IZVOZ_P",2),
      dm.createBigDecimalColumn("NAK_OSL_IZV_V",2),
      dm.createBigDecimalColumn("NAK_OSL_IZV_P",2),
      dm.createBigDecimalColumn("UKUPNO_III_V",2),
      dm.createBigDecimalColumn("UKUPNO_III_P",2),
      dm.createBigDecimalColumn("PPOR_PR_RAC_V",2),
      dm.createBigDecimalColumn("PPOR_PR_RAC_P",2),
      dm.createBigDecimalColumn("PL_PPOR_UVOZ_V",2),
      dm.createBigDecimalColumn("PL_PPOR_UVOZ_P",2),
      dm.createBigDecimalColumn("PL_PPOR_USLUGE_V",2),
      dm.createBigDecimalColumn("PL_PPOR_USLUGE_P",2),
      dm.createBigDecimalColumn("ISPRAVCI_PPORA",2),
      dm.createBigDecimalColumn("POR_OBV",2),
      dm.createBigDecimalColumn("PO_PRETHOD_OBR",2),
      dm.createBigDecimalColumn("RAZLIKA",2),
      //2006
      dm.createBigDecimalColumn("PRIJEVOZ",2),
      dm.createBigDecimalColumn("OSTALO_I24",2),
      dm.createBigDecimalColumn("R23_V",2),
      dm.createBigDecimalColumn("R23_P",2),
      dm.createBigDecimalColumn("III4V",2),
      dm.createBigDecimalColumn("III4P",2),
      dm.createBigDecimalColumn("III5V",2),
      dm.createBigDecimalColumn("III5P",2),
      dm.createBigDecimalColumn("III6V",2),
      dm.createBigDecimalColumn("III6P",2),
      dm.createBigDecimalColumn("III7V",2),
      dm.createBigDecimalColumn("III7P",2),
      
      /* PDV-K */

      dm.createBigDecimalColumn("UKUPNO_VII",2),
      dm.createBigDecimalColumn("UKUPNO_VII1",2),
      dm.createBigDecimalColumn("NAB_NEK_ISPOR",2),
      dm.createBigDecimalColumn("PRO_NEK_PRIM",2),
      dm.createBigDecimalColumn("NAB_OSOB_VOZIL",2),
      dm.createBigDecimalColumn("PRO_OSOB_VOZIL",2),
      dm.createBigDecimalColumn("NAB_RAB_VOZIL",2),
      dm.createBigDecimalColumn("PRO_OSTALO",2),
      dm.createBigDecimalColumn("NAB_OSTALO",2),

      dm.createBigDecimalColumn("VII3",2),
      dm.createBigDecimalColumn("VII4",2),
      dm.createBigDecimalColumn("VII5",2),
      
      dm.createBigDecimalColumn("OTUDJ_STJEC",2),
      dm.createTimestampColumn("POC_I_PREST"),
      
      dm.createBigDecimalColumn("POV",2),
      dm.createBigDecimalColumn("PRED",2),
      dm.createBigDecimalColumn("UST",2)
      
      });
  }

  public DataSet getReportSet(){
    return reportSet;
  }

  public java.sql.Timestamp getDatumOd(){
    return stds.getTimestamp("DATUMOD");
  }

  public java.sql.Timestamp getDatumDo(){
    return stds.getTimestamp("DATUMDO");
  }

  public String getNapomena(){
    return panelPDV_K.getNapomena();
  }

  void jbSwitchPdv_PdvK_actionPerformed(ActionEvent e) {
    switchButton();
  }

  void switchButton(){
    if (!pdvK){
      mainPanel.remove(panelPDV);
      mainPanel.add(panelPDV_K, BorderLayout.CENTER);
      jbSwitchPdv_PdvK.setText("Obrazac PDV");
      panelPDV_K.VII11.requestFocus();
    } else {
      mainPanel.remove(panelPDV_K);
      mainPanel.add(panelPDV, BorderLayout.CENTER);
      jbSwitchPdv_PdvK.setText("Obrazac PDV-K");
      panelPDV.I1.requestFocus();
    }
    mainPanel.repaint();
    pdvK = !pdvK;
  }

  public void keyF6Press(){
    if (jbSwitchPdv_PdvK.isEnabled()){
      switchButton();
    }
  }

  public void setEnabled(boolean en){}
  
  private void switchPDV13() {
    if (panelPDV13 != null) mainPanel.remove(panelPDV13);
    if (pdvK) {
      mainPanel.remove(panelPDV_K);
    } else {
      mainPanel.remove(panelPDV);
    }
    mainPanel.add(getPanelPDV13(), BorderLayout.CENTER);
    if (!isEU13()) {//vrati stare panele
      if (pdvK) {
        mainPanel.add(panelPDV_K);
      } else {
        mainPanel.add(panelPDV);
      }
    }
  }

  StorageDataSet mapset = null;
  JPanel panelPDV13 = null;
  private Component getPanelPDV13() {
    try {
      Object rPDVD = Class.forName("hr.restart.sk.repPDVDisk").newInstance();
      HashMap data = (HashMap)rPDVD.getClass().getMethod("tijeloData", null).invoke(rPDVD, null);
      mapset = createMapSet(data);
      TreeSet keyset = new TreeSet(data.keySet());
      panelPDV13 = new JPanel(new GridLayout(0, 1));
      for (Iterator iterator = keyset.iterator(); iterator.hasNext();) {
        String ciz = (String) iterator.next();
        if (!ciz.trim().endsWith("p")) //preskoci p-ove, oni idu s o-ovima 8-)
          panelPDV13.add(getPanelRow(ciz, data));
      }
      sumMapset();
      return panelPDV13;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private void sumMapset() {
    mapset.setBigDecimal("Pod100", sumcols(new String[]{
        "Pod101",
        "Pod102",
        "Pod103",
        "Pod104",
        "Pod105",
        "Pod106",
        "Pod107",
        "Pod108",
        "Pod109",
        "Pod110"
        }));
    mapset.setBigDecimal("Pod200o", sumcols(new String[]{
        "Pod201o",
        "Pod202o",
        "Pod203o",
        "Pod204o",
        "Pod205o",
        "Pod206o",
        "Pod207o",
        "Pod208o",
        "Pod209o",
        "Pod210o",
        "Pod211o",
        "Pod212o",
        "Pod213o",
        "Pod214o"
    }));
    mapset.setBigDecimal("Pod200p", sumcols(new String[]{
        "Pod201p",
        "Pod202p",
        "Pod203p",
        "Pod204p",
        "Pod205p",
        "Pod206p",
        "Pod207p",
        "Pod208p",
        "Pod209p",
        "Pod210p",
        "Pod211p",
        "Pod212p",
        "Pod213p",
        "Pod214p"
        }));
    mapset.setBigDecimal("Pod300o", sumcols(new String[]{
        "Pod301o",
        "Pod302o",
        "Pod303o",
        "Pod304o",
        "Pod305o",
        "Pod306o",
        "Pod307o"
    }));
    mapset.setBigDecimal("Pod300p", sumcols(new String[]{
        "Pod301p",
        "Pod302p",
        "Pod303p",
        "Pod304p",
        "Pod305p",
        "Pod306p",
        "Pod307p"
        }));
    mapset.setBigDecimal("Pod400", mapset.getBigDecimal("Pod200p").subtract(mapset.getBigDecimal("Pod300p")));
    mapset.setBigDecimal("Pod600", mapset.getBigDecimal("Pod400").subtract(mapset.getBigDecimal("Pod500")));
    
  }

  private BigDecimal sumcols(String[] cols) {
    BigDecimal sum = Aus.zero2;
    for (int i = 0; i < cols.length; i++) {
      sum = sum.add(mapset.getBigDecimal(cols[i]));
    }
    return sum;
  }

  private StorageDataSet createMapSet(HashMap data) {
    StorageDataSet mset = new StorageDataSet();
    for (Iterator iterator = data.keySet().iterator(); iterator.hasNext();) {
      String ciz = (String) iterator.next();
      mset.addColumn(dM.createBigDecimalColumn(ciz));
    }
    mset.open();
    mset.insertRow(true);
    return mset;
  }

  private JPanel getPanelRow(String ciz, HashMap data) {
    JPanel panelRow = new JPanel(new BorderLayout());
    QueryDataSet ipdv = IzvjPDV.getDataModule().getFilteredDataSet(Condition.equal("CIZ", ciz));
    ipdv.open(); ipdv.first();
    String jltxt = numbers(ciz) + " " + ipdv.getString("OPIS");
    JLabel jL = new JLabel(jltxt.length()>70?jltxt.substring(0,70):jltxt);
    jL.setToolTipText(jltxt);
    panelRow.add(jL, BorderLayout.CENTER);
    JPanel panelNum = new JPanel(new GridLayout(1,2));
    JraTextField jO = new JraTextField();
    JraTextField jP = new JraTextField();
    raCommonClass.getraCommonClass().setLabelLaF(jO, false);
    raCommonClass.getraCommonClass().setLabelLaF(jP, false);
    if (ciz.endsWith("o")) {//za osnovicu napraviti i porez
      jO.setDataSet(mapset);
      jO.setColumnName(ciz);
      jP.setDataSet(mapset);
      jP.setColumnName(new VarStr(ciz).chop().toString()+"p");
      panelNum.add(jO);
      panelNum.add(jP);
      mapset.setBigDecimal(jO.getColumnName(), (BigDecimal)data.get(jO.getColumnName()));
      mapset.setBigDecimal(jP.getColumnName(), (BigDecimal)data.get(jP.getColumnName()));
    } else {
      panelNum.add(new JLabel(""));
      jP.setDataSet(mapset);
      jP.setColumnName(ciz);
      panelNum.add(jP);
      mapset.setBigDecimal(jP.getColumnName(), (BigDecimal)data.get(jP.getColumnName()));
    }
    panelRow.add(panelNum, BorderLayout.EAST);
    return panelRow;
  }

  private String numbers(String ciz) {
    char[] in = ciz.toCharArray();
    StringBuffer out = new StringBuffer();
    for (int i = 0; i < in.length; i++) {
      if (Character.isDigit(in[i])) out.append(in[i]);
    }
    return out.toString();
  }
  public void show() {
    // TODO Auto-generated method stub
    super.show();
    getWindow().setSize(new Dimension(800, 700));
  }
}