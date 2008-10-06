/****license*****************************************************************
**   file: ispKarticaRadnikaBLPN.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.swing.raDateRange;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class ispKarticaRadnikaBLPN extends raUpitLite {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  Util ut = Util.getUtil();

  JPanel mainPanel = new JPanel();
  JPanel jpRadnik = new JPanel();
  JPanel jpRadio = new JPanel();

  private BorderLayout borderLayout1 = new BorderLayout();
  private XYLayout lay = new XYLayout();

  JLabel jlCradnik = new JLabel();
  JLabel jlDatumi = new JLabel();
  JLabel jlVal = new JLabel();
  JLabel jlButoni = new JLabel();
  JraButton jbSelCradnik = new JraButton();
  JraButton jbSelVal = new JraButton();
  JraTextField jraDatumOd = new JraTextField();
  JraTextField jraDatumDo = new JraTextField();

  JraRadioButton jrbPN = new JraRadioButton();
  JraRadioButton jrbBL = new JraRadioButton();
  JraRadioButton jrbSve = new JraRadioButton();

  raButtonGroup botunskaGrupa = new raButtonGroup();
  JlrNavField jlrCradnik = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrPrezime = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrIme = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrVal = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  StorageDataSet stds = new StorageDataSet();
  QueryDataSet bl, pn;

  public ispKarticaRadnikaBLPN() {
    try {
      ispKaR = this;
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  static ispKarticaRadnikaBLPN ispKaR;

  public static ispKarticaRadnikaBLPN getinstance(){
    return ispKaR;
  }

  StorageDataSet repStDS;
  Timestamp datumOd; // = ut.getFirstSecondOfDay(stds.getTimestamp("DATUMOD"));
  Timestamp datumDo; // = ut.getLastSecondOfDay(stds.getTimestamp("DATUMDO"));

  public boolean Validacija(){
    datumOd = ut.getFirstSecondOfDay(stds.getTimestamp("DATUMOD"));
    datumDo = ut.getLastSecondOfDay(stds.getTimestamp("DATUMDO"));
    if (vl.isEmpty(jlrCradnik)) return false;
    boolean pun = true;
    bl = ut.getNewQueryDataSet(getAllTrosBlag());
    pn = ut.getNewQueryDataSet(getAllTrosPN());
    if (jrbSve.isSelected() && (bl.isEmpty() && pn.isEmpty())
        || (jrbPN.isSelected() && pn.isEmpty())
        || (jrbBL.isSelected() && bl.isEmpty())) pun = false;
    if (!pun) {
      JOptionPane.showMessageDialog(this,"Nema podataka","Ispis kartice djelatnika",JOptionPane.ERROR_MESSAGE);
      /** @todo sredit joptionpane */
//      System.out.println("nema podataka");
      return false;
    }
    return true;
  }

  private String getAllTrosPN(){
    String trosBl = "SELECT Stavkepn.oznval, PutniNalog.datobr, Stavkepn.iznos, Stavkepn.pviznos, Stavkepn.stavka, Stavkepn.cskl, Stavkepn.vrdok, Stavkepn.cpn "+
    "FROM Stavkepn, PutniNalog "+
    "WHERE stavkepn.knjig = putninalog.knjig " +
    "AND stavkepn.godina = putninalog.godina " +
    "AND stavkepn.broj = putninalog.broj " +
    "AND stavkepn.indputa = putninalog.indputa " +
    "AND PutniNalog.datobr BETWEEN '" + datumOd + "' AND '" + datumDo + "' "+
                   "AND cradnik ='" + stds.getString("CRADNIK") + "' "+
    "UNION "+
    "SELECT Stavpnarh.oznval, Putnalarh.datobr, Stavpnarh.iznos, Stavpnarh.pviznos, Stavpnarh.stavka, Stavpnarh.cskl, Stavpnarh.vrdok, Stavpnarh.cpn "+
    "FROM Stavpnarh, Putnalarh "+
    "WHERE stavpnarh.knjig = putnalarh.knjig " +
    "AND stavpnarh.godina = putnalarh.godina " +
    "AND stavpnarh.broj = putnalarh.broj " +
    "AND stavpnarh.indputa = putnalarh.indputa " +
    "AND Putnalarh.datobr BETWEEN '" + datumOd + "' AND '" + datumDo + "' "+
                   "AND cradnik ='" + stds.getString("CRADNIK") + "'";
//    System.out.println("getAllTrosPN *** \n"+trosBl);
    return trosBl;
  }

  private String getAllTrosBlag(){
    String trosPN = "SELECT Stavblag.oznval, Stavblag.datum, Stavblag.primitak, Stavblag.izdatak, Stavblag.pvprimitak, Stavblag.pvizdatak, Stavblag.stavka, Stavblag.cskl, Stavblag.vrdok, Stavblag.cpn "+
    "FROM Stavblag "+
    "WHERE datum BETWEEN '" + datumOd + "' AND '" + datumDo + "' "+
                    "AND cradnik ='" + stds.getString("CRADNIK") + "' "+
    "UNION "+
    "SELECT Stavkeblarh.oznval, Stavkeblarh.datum, Stavkeblarh.primitak, Stavkeblarh.izdatak, Stavkeblarh.pvprimitak, Stavkeblarh.pvizdatak, Stavkeblarh.stavka, Stavkeblarh.cskl, Stavkeblarh.vrdok, Stavkeblarh.cpn "+
    "FROM Stavkeblarh "+
    "WHERE datum BETWEEN '" + datumOd + "' AND '" + datumDo + "' "+
                    "AND cradnik ='" + stds.getString("CRADNIK") + "'";
//    System.out.println("getAllTrosBlag *** \n"+trosPN);
    return trosPN;
  }

  private void defaultValues(boolean inOrOut){
    rcc.EnabDisabAll(this.mainPanel,inOrOut);
  }

  public void componentShow() {
    initialValues();
  }

  private void initialValues() {
    stds.setTimestamp("DATUMOD", ut.getFirstDayOfMonth());
    stds.setTimestamp("DATUMDO", vl.getToday());
    jrbSve.setSelected(true);
  }

  boolean pressed = false;

  public void okPress() {
    pressed = true;
    makeRepStDs();
  }

  private void makeRepStDs(){
    if (!repStDS.isOpen()) repStDS.open();
    if (repStDS.rowCount() != 0) repStDS.deleteAllRows();
    if (jrbSve.isSelected()){
      for (bl.first(); bl.inBounds(); bl.next()) {
        blagajna(bl);
      }
      for (pn.first(); pn.inBounds(); pn.next()) {
        putnal(pn);
      }
    }
    if (jrbBL.isSelected()){
      for (bl.first(); bl.inBounds(); bl.next()) {
        blagajna(bl);
      }
    }
    if (jrbPN.isSelected()){
      for (pn.first(); pn.inBounds(); pn.next()) {
        putnal(pn);
      }
    }
  }

  private void blagajna(DataSet bls) {
    repStDS.insertRow(false);
    repStDS.setBigDecimal("PRIMITAK", bls.getBigDecimal("PRIMITAK"));
    repStDS.setBigDecimal("IZDATAK", bls.getBigDecimal("IZDATAK"));
    repStDS.setBigDecimal("PVPRIMITAK", bls.getBigDecimal("PRIMITAK"));
    repStDS.setBigDecimal("PVIZDATAK", bls.getBigDecimal("IZDATAK"));
    repStDS.setString("OZNVAL", bls.getString("OZNVAL"));
    repStDS.setTimestamp("DATUM", bls.getTimestamp("DATUM"));
    repStDS.setString("STAVKA", bls.getString("STAVKA"));
    repStDS.setString("CSKL", bls.getString("CSKL"));
    repStDS.setString("VRDOK", bls.getString("VRDOK"));
    repStDS.setString("CPN", bls.getString("CPN"));
  }

  private void putnal(DataSet putn) {
    repStDS.insertRow(false);
    repStDS.setBigDecimal("PRIMITAK", putn.getBigDecimal("IZNOS"));
    repStDS.setBigDecimal("IZDATAK", new java.math.BigDecimal("0"));     //putn.getBigDecimal("IZNOS"));
    repStDS.setBigDecimal("PVPRIMITAK", putn.getBigDecimal("PVIZNOS"));
    repStDS.setBigDecimal("PVIZDATAK", new java.math.BigDecimal("0"));     //putn.getBigDecimal("IZNOS"));
    repStDS.setString("OZNVAL", putn.getString("OZNVAL"));
    repStDS.setTimestamp("DATUM", putn.getTimestamp("DATOBR"));
    repStDS.setString("STAVKA", String.valueOf(putn.getShort("STAVKA")));
    repStDS.setString("CSKL", putn.getString("CSKL"));
    repStDS.setString("VRDOK", putn.getString("VRDOK"));
    repStDS.setString("CPN", putn.getString("CPN"));
  }

  public void firstESC() {
    pressed = false;
    defaultValues(true);
    stds.setString("OZNVAL", "");
    jlrCradnik.setText("");
    jlrCradnik.emptyTextFields();
    jlrCradnik.requestFocus();
  }

  public boolean isIspis() {
    return false;
  }

  public boolean ispisNow() {
    return true;
  }

  public boolean runFirstESC() {
    if (pressed) return true;
    return false;
  }


  Column colCradnik = new Column();
  Column colIme = new Column();
  Column colPrezime = new Column();
  Column colDatumOd = new Column();
  Column colDatumDo = new Column();
  Column colValuta = new Column();

  private XYLayout xYLayout1 = new XYLayout();
  private Border border1;

  private void jbInit() throws Exception {
    border1 = new EtchedBorder(EtchedBorder.RAISED,new Color(224, 255, 255),new Color(109, 129, 140));
    jpRadio.setLayout(xYLayout1);
    this.addReport("hr.restart.blpn.repKarticaRadnikaBLPN", "Ispis kartice djelatnika",2);

    setTableSet();

    setStorageRepSet();

    jlCradnik.setText("Djelatnik");
    jlDatumi.setText("U periodu (od - do)");
    jlVal.setText("U valuti (prazno za sve)");
    jlButoni.setText("Troškovi");

    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setDataSet(stds);
    jlrCradnik.setColNames(new String[] {"IME", "PREZIME"});
    jlrCradnik.setTextFields(new JTextComponent[] {jlrIme, jlrPrezime});
    jlrCradnik.setVisCols(new int[] {0, 1, 2});
    jlrCradnik.setSearchMode(0);
    jlrCradnik.setRaDataSet(presPN.getRadnici());
    jlrCradnik.setNavButton(jbSelCradnik);
    
    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        jlrCradnik.setRaDataSet(presPN.getRadnici());
        jlrIme.setRaDataSet(presPN.getRadnici());
        jlrPrezime.setRaDataSet(presPN.getRadnici());
      }
    });
    jlrIme.setColumnName("IME");
    jlrIme.setNavProperties(jlrCradnik);
    jlrIme.setDataSet(stds);
    jlrIme.setSearchMode(1);

    jlrPrezime.setColumnName("PREZIME");
    jlrPrezime.setNavProperties(jlrCradnik);
    jlrPrezime.setDataSet(stds);
    jlrPrezime.setSearchMode(1);

    jlrVal.setColumnName("OZNVAL");
    jlrVal.setDataSet(stds);
    jlrVal.setVisCols(new int[] {0, 3});
    jlrVal.setSearchMode(0);
    jlrVal.setRaDataSet(dm.getValute());
    jlrVal.setNavButton(jbSelVal);

    jraDatumOd.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumOd.setDataSet(stds);
    jraDatumOd.setColumnName("DATUMOD");

    jraDatumDo.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumDo.setDataSet(stds);
    jraDatumDo.setColumnName("DATUMDO");

    mainPanel.setLayout(borderLayout1);
    jpRadnik.setLayout(lay);
    lay.setWidth(600);
    lay.setHeight(150);

    botunskaGrupa.setHorizontalTextPosition(SwingConstants.TRAILING);
    botunskaGrupa.add(jrbBL, "Blagajne");
    botunskaGrupa.add(jrbPN, "Putnih naloga");
    botunskaGrupa.add(jrbSve, "Svi");

    jpRadio.setBorder(border1);
    mainPanel.add(jpRadnik, BorderLayout.CENTER);

    jpRadnik.add(jbSelCradnik, new XYConstraints(555, 20, 21, 21));
    jpRadnik.add(jlCradnik, new XYConstraints(15, 20, -1, -1));
    jpRadnik.add(jlrCradnik, new XYConstraints(150, 20, 50, -1));
    jpRadnik.add(jlrIme, new XYConstraints(205, 20, 170, -1));
    jpRadnik.add(jlrPrezime, new XYConstraints(380, 20, 170, -1));
    jpRadnik.add(jlDatumi, new XYConstraints(15, 45, -1, -1));
    jpRadnik.add(jraDatumOd, new XYConstraints(150, 45, 100, -1));
    jpRadnik.add(jraDatumDo, new XYConstraints(255, 45, 100, -1));
    jpRadnik.add(jlVal, new XYConstraints(15,70,-1,-1));
    jpRadnik.add(jlrVal, new XYConstraints(150,70,50,-1));
    jpRadnik.add(jlButoni, new XYConstraints(15,97,-1,-1));
    jpRadnik.add(jbSelVal, new XYConstraints(205,70,21,21));
    jpRadnik.add(jpRadio, new XYConstraints(150,95,400,-1));
    jpRadio.add(jrbSve,   new XYConstraints(15, 0, -1, -1));
    jpRadio.add(jrbBL,   new XYConstraints(145, 0, -1, -1));
    jpRadio.add(jrbPN,   new XYConstraints(280, 0, -1, -1));

    this.setJPan(mainPanel);
    new raDateRange(jraDatumOd, jraDatumDo);
  }

  public DataSet getRepStDS(){
    repStDS.setSort(new SortDescriptor(new String[]{"OZNVAL","DATUM"}));
    return repStDS;
  }

  public Timestamp dateRangeOd(){
    return datumOd;
  }

  public Timestamp dateRangeDo(){
    return datumDo;
  }

  public String getCradnik(){
    return stds.getString("CRADNIK");
  }

  Column colPrim = new Column();
  Column colIzd = new Column();
  Column colPVPrim = new Column();
  Column colPVIzd = new Column();
  Column colVal = new Column();
  Column colCSKL = new Column();
  Column colSTAVKA = new Column();
  Column colVRDOK = new Column();
  Column colDatum = new Column();
  Column colCPN = new Column();

  private void setStorageRepSet(){

    repStDS = new StorageDataSet();

    colPrim = dm.createBigDecimalColumn("PRIMITAK");
    colIzd = dm.createBigDecimalColumn("IZDATAK");
    colPVPrim = dm.createBigDecimalColumn("PVPRIMITAK");
    colPVIzd = dm.createBigDecimalColumn("PVIZDATAK");    
    colVal = dm.createStringColumn("OZNVAL",0);
    colCSKL = dm.createStringColumn("CSKL",0);
    colSTAVKA = dm.createStringColumn("STAVKA",0);
    colVRDOK = dm.createStringColumn("VRDOK",0);
    colDatum = dm.createTimestampColumn("DATUM");
    colCPN = dm.createStringColumn("CPN",0);

    repStDS.setColumns(new Column[] {colDatum, colVRDOK, colPrim, colIzd, colPVPrim, colPVIzd, colVal, colCSKL, colSTAVKA, colCPN});
  }

  private void setTableSet() {
    colCradnik = dm.createStringColumn("CRADNIK",0);
    colIme = dm.createStringColumn("IME",0);
    colPrezime = dm.createStringColumn("PREZIME",0);
    colDatumOd = dm.createTimestampColumn("DATUMOD");
    colDatumDo = dm.createTimestampColumn("DATUMDO");
    colValuta = dm.createStringColumn("OZNVAL",0);


    try {
      stds.setColumns(new Column[] {colCradnik, colIme, colPrezime, colDatumOd, colDatumDo, colValuta});
      stds.open();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
