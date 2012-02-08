/****license*****************************************************************
**   file: raPopListaInv.java
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

import hr.restart.baza.Artikli;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;
import hr.restart.util.reports.raReportDescriptor;

import java.awt.Color;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class raPopListaInv extends raUpitLite {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  sgQuerys ss = sgQuerys.getSgQuerys();

  QueryDataSet fieldSet = new QueryDataSet();
  static private QueryDataSet repSet = new QueryDataSet();

  static public String sortBy;

  Column datum = new Column();
  Column cskl = new Column();

  JPanel mainPanel = new JPanel();
  JPanel panel01 = new JPanel();

  XYLayout xYLayout1 = new XYLayout();
  XYLayout xYLayout2 = new XYLayout();

  JraTextField jraDATINV = new JraTextField();
  JLabel jlDatum = new JLabel();
  JLabel jlSljed = new JLabel();
  JraRadioButton jrbSortiranjePoCART1 = new JraRadioButton();
  JraRadioButton jrbSortiranjePoNAZART = new JraRadioButton();
  raButtonGroup radioSortButton = new raButtonGroup();
  JraCheckBox jcbIspisStanjeNula = new JraCheckBox();

  JLabel jlCskl = new JLabel();
  JLabel jlaCskl = new JLabel();
  JLabel jlaNazskl = new JLabel();
  JraButton jbSelCskl = new JraButton();
  JlrNavField jlrCskl = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazskl = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  TitledBorder titledBorder1;

  static raPopListaInv rapli;

  public static raPopListaInv getInstance(){
    if (rapli == null){
      rapli = new raPopListaInv();
    }
    return rapli;
  }

  public raPopListaInv() {
    try {
      jbInit();
      setCsklFields();
      setPanels();
    }
    catch (Exception ex) {
    }
  }

  public void componentShow() {
    datumInv = ut.getYearEnd(Aut.getAut().getKnjigodRobno());
    fieldSet.setTimestamp("DATUM", datumInv);
    System.out.println("datuminv = " + datumInv);
//    datumInv = ut.getLastSecondOfDay(ut.getLastDayOfYear());
  }

  public void okPress() {
//    fieldSet.setTimestamp("DATUM", ut.getLastSecondOfDay(ut.getLastDayOfYear()));
    makeRepSet();
    onAndOffPanel(false);
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(fieldSet);
  }

  public boolean Validacija(){
    if (fieldSet.getString("CSKL").equals("")){
      jlrCskl.requestFocus();
      JOptionPane.showConfirmDialog(this.mainPanel,"Obvezatan unos - SKLADIŠTE !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public void firstESC() {
    onAndOffPanel(true);
    fieldSet.setString("CSKL", "");
    jlrCskl.emptyTextFields();
    jlrCskl.requestFocus();
  }

  public boolean isIspis() {
    return false;
  }

  public boolean ispisNow() {
    if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(this,
        "Prikazati rezultat u tablici?", "Tablièni prikaz",
        JOptionPane.OK_CANCEL_OPTION)) return true;
    
    showTable();
    firstESC();
    return false;
  }

  void updateColumn(String name, String title) {
    if (repSet.hasColumn(name) == null) return;
    if (title == null) repSet.hasColumn(name).setVisible(0);
    else {
      repSet.hasColumn(name).setCaption(title);
      repSet.hasColumn(name).setWidth(
          Artikli.getDataModule().getColumn(name).getWidth());
    }
  }
  
  void showTable() {
    frmTableDataView view = new frmTableDataView();
    view.setTitle("Popisna lista za skladište " + fieldSet.getString("CSKL"));
    updateColumn("CSKL", null);
    updateColumn("CART", "Šifra");
    updateColumn("CART1", "Oznaka");
    updateColumn("BC", "Barkod");
    updateColumn("CGRART", "Grupa");
    updateColumn("NAZART", "Naziv");
    updateColumn("JM", "Jmj");
    updateColumn("NAZSKL", null);
    updateColumn("SORTKOL", "Poredak");
    view.setDataSet(repSet);
    view.setSaveName("popisna-lista");
    view.setCustomReport(raReportDescriptor.create("hr.restart.robno.repPopLista","hr.restart.robno.repPopLista","PopLista","Popisna lista"));
    view.show();
    view.resizeLater();
  }
    
  public boolean runFirstESC() {
    if (!fieldSet.getString("CSKL").equals("")) return true;
    return false;
  }

  private void jbInit() throws Exception{

    datum = dm.createTimestampColumn("DATUM");
    cskl = dm.createStringColumn("CSKL",0);

    fieldSet.setColumns(new Column[] {datum, cskl});

    jlDatum.setText("Datum");
    jlSljed.setText("Sljed");

    mainPanel.setLayout(xYLayout1);
    panel01.setLayout(xYLayout2);

    jcbIspisStanjeNula.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbIspisStanjeNula.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbIspisStanjeNula.setText("Ispis artikala sa stanjem nula     ");
    jcbIspisStanjeNula.setSelected(true);

    radioSortButton.setHorizontalTextPosition(SwingConstants.TRAILING);
    radioSortButton.add(jrbSortiranjePoCART1, "ŠIFRA");
    radioSortButton.add(jrbSortiranjePoNAZART, "NAZIV");
    jrbSortiranjePoCART1.setSelected(true);

    jbSelCskl.setText("...");
    jlCskl.setText("Skladište");
    jlaCskl.setHorizontalAlignment(SwingConstants.CENTER);
    jlaCskl.setText("Šifra");
    jlaNazskl.setHorizontalAlignment(SwingConstants.CENTER);
    jlaNazskl.setText("Naziv");

    jraDATINV.setHorizontalAlignment(SwingConstants.CENTER);
    jraDATINV.setDataSet(fieldSet);
    jraDATINV.setColumnName("DATUM");

    this.setJPan(mainPanel);

    xYLayout1.setWidth(595);
    xYLayout1.setHeight(135);

    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(139, 139, 139)),"");
    panel01.setBorder(titledBorder1);

    setReportProviders();
  }

  public void setPanels() {
    mainPanel.add(jlaCskl, new XYConstraints(151, 13, 98, -1));
    mainPanel.add(jlaNazskl, new XYConstraints(256, 13, 293, -1));
    mainPanel.add(jbSelCskl, new XYConstraints(555, 30, 21, 21));
    mainPanel.add(jlCskl, new XYConstraints(15, 30, -1, -1));
    mainPanel.add(jlrCskl, new XYConstraints(150, 30, 100, -1));
    mainPanel.add(jlrNazskl, new XYConstraints(255, 30, 295, -1));
    mainPanel.add(jlDatum,    new XYConstraints(15, 55, -1, -1));
    mainPanel.add(jraDATINV,    new XYConstraints(150, 55, 100, -1));
    mainPanel.add(jlSljed,    new XYConstraints(15, 90, -1, -1));
    mainPanel.add(jcbIspisStanjeNula,       new XYConstraints(291, 55, 259, 19));

    mainPanel.add(panel01,          new XYConstraints(150, 80, 400, -1));

    panel01.add(jrbSortiranjePoCART1, new XYConstraints(15, 0, -1, -1));
    panel01.add(jrbSortiranjePoNAZART,  new XYConstraints(215, 0, -1, -1));
  }

  public void setCsklFields() {
    jlrCskl.setColumnName("CSKL");
    jlrCskl.setColNames(new String[] {"NAZSKL"});
    jlrCskl.setDataSet(fieldSet);
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazskl});
    jlrCskl.setVisCols(new int[] {0, 1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(Util.getSkladFromCorg());
    jlrCskl.setNavButton(jbSelCskl);

    jlrNazskl.setColumnName("NAZSKL");
    jlrNazskl.setNavProperties(jlrCskl);
    jlrNazskl.setSearchMode(1);

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        jlrCskl.setRaDataSet(Util.getSkladFromCorg());
      }
    });
  }

  public void setReportProviders(){
    this.addReport("hr.restart.robno.repPopLista","hr.restart.robno.repPopLista","PopLista","Popisna lista");
//    this.addReport("hr.restart.robno.repPopLista", "Popisna lista", 2);
  }

  public void makeRepSet(){
    String stanjeNula = "";
    if (jrbSortiranjePoNAZART.isSelected()) sortBy = "NAZART";
    else sortBy = hr.restart.sisfun.frmParam.getParam("robno","indiCart");
    if (!jcbIspisStanjeNula.isSelected()) stanjeNula = " and kol > 0 ";

    datumInv = fieldSet.getTimestamp("datum");
    
    repSet.close();

    repSet.setQuery(new QueryDescriptor(dm.getDatabase1(),ss.getInventuraPopisnaListaQuery(fieldSet.getString("CSKL"), stanjeNula, sortBy,ut.getYear(fieldSet.getTimestamp("DATUM")))));

    repSet.open();
  }

  public String getSORTER(){
    return sortBy;
  }

  private static Timestamp datumInv;

  public static Timestamp getDatum(){
//    System.out.println("DATUM Klasa "+fieldSet.getTimestamp("DATUM"));
    return datumInv;//fieldSet.getTimestamp("DATUM");
  }

  public QueryDataSet getRepSet(){
    return repSet;
  }

  void onAndOffPanel(boolean ocul){
    rcc.EnabDisabAll(mainPanel, ocul);
  }

}