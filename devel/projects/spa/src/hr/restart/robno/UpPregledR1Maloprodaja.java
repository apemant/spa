/****license*****************************************************************
**   file: UpPregledR1Maloprodaja.java
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
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;
import hr.restart.util.sysoutTEST;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * @author S.G.
 *
 * Started 2005.02.11
 * 
 */

public class UpPregledR1Maloprodaja extends raUpitLite {
  dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  Valid vl = Valid.getValid();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  lookupData ld = lookupData.getlookupData();

  JPanel glavniPanel = new JPanel();
  private XYLayout xyLay = new XYLayout();

  TableDataSet tds = new TableDataSet();

  JLabel jlCskl = new JLabel();
  JlrNavField jlrCskl = new JlrNavField();
  JlrNavField jlrNazSkl = new JlrNavField();
  JraButton jbSelCskl = new JraButton();

  JLabel jlDatum = new JLabel();
  JraTextField pocDat = new JraTextField();
  JraTextField zavDat = new JraTextField();
  
  String doki_stdoki, vtztr, vzt;
  QueryDataSet reportSet;
  
  sysoutTEST st = new sysoutTEST(false);
  
  private static UpPregledR1Maloprodaja instanceOfMe;

  public static UpPregledR1Maloprodaja getInstance() {
    return instanceOfMe;
  }

  public UpPregledR1Maloprodaja() {
    try {
      initilizer();
      instanceOfMe = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  boolean lupiEsc = false;

  /* (non-Javadoc)
   * @see hr.restart.util.raUpitLite#okPress()
   */
  public void okPress() {
    reportSet = ut.getNewQueryDataSet(getUpit());
    if (reportSet == null || reportSet.isEmpty()) setNoDataAndReturnImmediately();
    lupiEsc = true;
  }
  
  private String getUpit(){
   return "SELECT max(doki.cskl) as cskl, max(doki.vrdok) as vrdok, max(doki.god)as god, max(doki.brdok) as brdok, max(doki.datdok) as datdok, " +
   		"max(doki.ckupac) as ckupac, max(kupci.ime) as ime, max(kupci.prezime) as prezime, max(kupci.jmbg) as jmbg, " +
   		"sum(stdoki.iprodsp) as iprodsp, sum(stdoki.iprodbp) as iprodbp , sum(stdoki.iprodsp - stdoki.iprodbp) as ipor " +
   		"FROM doki, stdoki, kupci " +
   		"WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok AND kupci.ckupac = doki.ckupac " +
   		"and doki.vrdok in ('GRN','GOT') " +
   		"and doki.ckupac != 0 " +
   		"and doki.cskl = '" + tds.getString("CSKL") + "' " +
   		"and " + Condition.between("DOKI.DATDOK",tds.getTimestamp("PDAT"),tds.getTimestamp("ZDAT")) + " " +
   		"group by doki.brdok"; 
  }

  public void firstESC() {
    rcc.EnabDisabAll(this.getJPan(),true);
    jlrCskl.setText("");
    jlrCskl.emptyTextFields();
    lupiEsc = false;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jlrCskl.requestFocus();
      }
    });
  }

  public boolean runFirstESC() {
    return lupiEsc;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.raUpitLite#componentShow()
   */
  public void componentShow() {
    tds.setTimestamp("PDAT", ut.getFirstDayOfMonth()); // ut.getYearBegin("2004"));// 
    tds.setTimestamp("ZDAT", vl.getToday()); // ut.getYearEnd("2004")); // 
    jlrCskl.setText(hr.restart.sisfun.raUser.getInstance().getDefSklad());
    jlrCskl.forceFocLost();
  }
  
  private void initilizer() throws Exception {
    this.addReport("hr.restart.robno.repPregledR1Maloprodaja","hr.restart.robno.repPregledR1Maloprodaja","PregledR1Maloprodaja","Pregled R1 raèuna - maloprodaja");
    tds.setColumns(new Column[]{
        dm.createTimestampColumn("PDAT", "Po\u010Detni datum"), 
        dm.createTimestampColumn("ZDAT", "Završni datum"), 
        dm.createStringColumn("CSKL", "Organizacijska jedinica", 12)
    });
    tds.open();

    jlCskl.setText("Skladište");

    jlrCskl.setColumnName("CSKL");
    jlrCskl.setDataSet(this.tds);
    jlrCskl.setColNames(new String[]{"NAZSKL"});
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[]{jlrNazSkl});
    jlrCskl.setVisCols(new int[]{0, 1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(hr.restart.robno.Util.getMPSklDataset());
    jlrCskl.setNavButton(jbSelCskl);

    jlrNazSkl.setColumnName("NAZSKL");
    jlrNazSkl.setNavProperties(jlrCskl);
    jlrNazSkl.setSearchMode(1);

    jlDatum.setText("Datum (od - do)");
    pocDat.setDataSet(tds);
    pocDat.setColumnName("PDAT");
    zavDat.setDataSet(tds);
    zavDat.setColumnName("ZDAT");

    glavniPanel.setLayout(xyLay);
    xyLay.setWidth(645);
    xyLay.setHeight(85);
    this.setJPan(glavniPanel);

    glavniPanel.add(jlCskl, new XYConstraints(15, 20, -1, -1));
    glavniPanel.add(jlrCskl, new XYConstraints(150, 20, 100, -1));
    glavniPanel.add(jlrNazSkl, new XYConstraints(255, 20, 350, -1));
    glavniPanel.add(jbSelCskl, new XYConstraints(610, 20, 21, 21));

    glavniPanel.add(jlDatum, new XYConstraints(15, 45, 100, -1));
    glavniPanel.add(pocDat, new XYConstraints(150, 45, 100, -1));
    glavniPanel.add(zavDat, new XYConstraints(255, 45, 100, -1));

  }
  
  public boolean isIspis() {
    return false;
  }
  
  public boolean ispisNow() {
    return true;
  }

  public QueryDataSet getReportSet() {
    return reportSet;
  }
  
  public String getCSKL(){
    return tds.getString("CSKL");
  }
  
  public String getNazSkl(){
    return jlrNazSkl.getText();
  }

  public java.sql.Timestamp getDatumOd(){
    return tds.getTimestamp("PDAT");
  }

  public java.sql.Timestamp getDatumDo(){
    return tds.getTimestamp("ZDAT");
  }
}
