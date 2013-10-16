/****license*****************************************************************
**   file: raPripInv.java
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
import hr.restart.baza.Inventura;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;
import hr.restart.util.raTransaction;
import hr.restart.util.raUpitLite;

import java.awt.Color;
import java.sql.Timestamp;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class raPripInv extends raUpitLite {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  sgQuerys ss = sgQuerys.getSgQuerys();

  QueryDataSet fieldSet = new QueryDataSet();
  QueryDataSet usporedbaQDS;

  Column datum = new Column();
  Column cskl = new Column();

  JPanel mainPanel = new JPanel();
  JPanel panel01 = new JPanel();

  XYLayout xYLayout1 = new XYLayout();
  XYLayout xYLayout2 = new XYLayout();

  JraTextField jraDATINV = new JraTextField();
  JLabel jlDatum = new JLabel();
  JLabel jlSljed = new JLabel();
  JraRadioButton jrbPripremaInventure = new JraRadioButton();
  JraRadioButton jrbPonistenjeInventure = new JraRadioButton();
  raButtonGroup rbGroupAkcija = new raButtonGroup();

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

  String year, actionPerformed, qInsertToInventura, qUpdateRest, qDeleteInventura, ac;

  public raPripInv() {
    try {
      System.out.println("rPripInv");
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void componentShow() {
    difolts(true);
  }

  public void okPress() {
    ac = "";
    if (actionPerformed.equals("PRIN")) {
      pripremaInventure();
      ac = "pripremljena";
    } else {
      brisanjeInventure();
      ac = "poništena";
    }
  }

  public void afterOKPress(){
    try {
      if (!actionPerformed.equals("NONE")){
        int kamoDalje = JOptionPane.showOptionDialog(this.mainPanel,
            new String[]{"Inventura uspješno " + ac,
            "Želite li ponovni izbor akcije?"},
            "Pitanje",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,new String[] {"Da","Ne"},"Ne");

        if (kamoDalje != JOptionPane.YES_OPTION) {
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              System.out.print("");
              difolts(false);
              cancelPress();
            }
          });
        }
        else {
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              System.out.print("");
              difolts(false);
            }
          });
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public boolean Validacija(){
    if (vl.isEmpty(jlrCskl)){
      actionPerformed = "NONE";
      jlrCskl.requestFocus();
//      JOptionPane.showConfirmDialog(this.mainPanel,"Obvezatan unos - SKLADIŠTE !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }

//    if (vl.isEmpty(jlrCskl)) return false;
    onAndOffPanel(false);

    year = ut.getYear(fieldSet.getTimestamp("DATUM"));

    provjeraPostavki();
    if (actionPerformed.equals("NONE")){
      int kamoDalje = JOptionPane.showOptionDialog(this.mainPanel,
          "Želite li ponovni izbor akcije?",
          "Pitanje",
          javax.swing.JOptionPane.YES_NO_OPTION,
          javax.swing.JOptionPane.QUESTION_MESSAGE,
          null,new String[] {"Da","Ne"},"Ne");
      if (kamoDalje != JOptionPane.YES_OPTION) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            System.out.print("");
            difolts(false);
            cancelPress();
          }
        });
      }
      else {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            System.out.print("");
            difolts(false);
          }
        });
      }
      return false;
    }
//    ac = "";
//    if (actionPerformed.equals("PRIN")) {
//      pripremaInventure();
//      ac = "pripremljena";
//    } else {
//      brisanjeInventure();
//      ac = "poništena";
//    }
    return true;
  }

  public void firstESC() {
    difolts(false);
  }

  private void difolts(boolean show) {
    rbGroupAkcija.setSelected(jrbPripremaInventure);

    fieldSet.setTimestamp("DATUM", ut.getYearEnd(Aut.getAut().getKnjigodRobno()));
    if (!show){
      onAndOffPanel(true);
      fieldSet.setString("CSKL", "");
      jlrCskl.emptyTextFields();
      jlrCskl.requestFocus();
    }
  }

  public boolean isIspis() {
    return false;
  }

  public boolean ispisNow() {
    return false;
  }

  public boolean runFirstESC() {
    if (!fieldSet.getString("CSKL").equals("")) return true;
    return false;
  }

  private void jbInit() throws Exception {

    datum = dm.createTimestampColumn("DATUM");
    cskl = dm.createStringColumn("CSKL",0);

    fieldSet.setColumns(new Column[] {datum, cskl});
    fieldSet.open();

    jlDatum.setText("Datum");
    jlSljed.setText("Akcija");

    mainPanel.setLayout(xYLayout1);
    panel01.setLayout(xYLayout2);

    rbGroupAkcija.setHorizontalTextPosition(SwingConstants.TRAILING);
    rbGroupAkcija.add(jrbPripremaInventure, "Priprema");
    rbGroupAkcija.add(jrbPonistenjeInventure, "Poništenje");

    jbSelCskl.setText("...");
    jlCskl.setText("Skladište");
    jlaCskl.setHorizontalAlignment(SwingConstants.CENTER);
    jlaCskl.setText("Šifra");
    jlaNazskl.setHorizontalAlignment(SwingConstants.CENTER);
    jlaNazskl.setText("Naziv");

    jraDATINV.setHorizontalAlignment(SwingConstants.CENTER);
    jraDATINV.setDataSet(fieldSet);
    jraDATINV.setColumnName("DATUM");

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

    this.setJPan(mainPanel);

    xYLayout1.setWidth(595);
    xYLayout1.setHeight(135);

    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(139, 139, 139)),"");
    panel01.setBorder(titledBorder1);

    mainPanel.add(jlaCskl, new XYConstraints(151, 13, 98, -1));
    mainPanel.add(jlaNazskl, new XYConstraints(256, 13, 293, -1));
    mainPanel.add(jbSelCskl, new XYConstraints(555, 30, 21, 21));
    mainPanel.add(jlCskl, new XYConstraints(15, 30, -1, -1));
    mainPanel.add(jlrCskl, new XYConstraints(150, 30, 100, -1));
    mainPanel.add(jlrNazskl, new XYConstraints(255, 30, 295, -1));
    mainPanel.add(jlDatum,    new XYConstraints(15, 55, -1, -1));
    mainPanel.add(jraDATINV,    new XYConstraints(150, 55, 100, -1));
    mainPanel.add(jlSljed,    new XYConstraints(15, 90, -1, -1));

    mainPanel.add(panel01,          new XYConstraints(150, 80, 400, -1));

    panel01.add(jrbPripremaInventure, new XYConstraints(15, 0, -1, -1));
    panel01.add(jrbPonistenjeInventure,  new XYConstraints(215, 0, -1, -1));

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        jlrCskl.setRaDataSet(Util.getSkladFromCorg());
      }
    });
  }

  void onAndOffPanel(boolean ocul){
    rcc.EnabDisabAll(mainPanel, ocul);
  }

  private void provjeraPostavki(){
    String usporedba = sgQuerys.getSgQuerys().getStringUsporedba(fieldSet.getString("CSKL"));
    usporedbaQDS = ut.getNewQueryDataSet(usporedba);

    if (jrbPripremaInventure.isSelected()){

      if (usporedbaQDS.getString("STATINV").equals("D")){
        jlrCskl.requestFocus();
        JOptionPane.showConfirmDialog(this.mainPanel,"Za zadano skladište inventura je vec zapocela","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        fieldSet.setString("CSKL", "");
        jlrCskl.emptyTextFields();
        actionPerformed = "NONE";
      } else {
        usporedbaQDS.setString("STATINV", "D");
        Timestamp stariDatum = usporedbaQDS.getTimestamp("DATINV");
        usporedbaQDS.setTimestamp("STARIDATINV", stariDatum);
        usporedbaQDS.setTimestamp("DATINV", fieldSet.getTimestamp("DATUM"));
        actionPerformed = "PRIN";
      }
    } else if (jrbPonistenjeInventure.isSelected()){
      if (usporedbaQDS.getString("STATINV").equals("N")){
        jlrCskl.requestFocus();
        JOptionPane.showConfirmDialog(this.mainPanel,"Za zadano skladište inventura nije pocela","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        fieldSet.setString("CSKL", "");
        jlrCskl.emptyTextFields();
        actionPerformed = "NONE";
      } else {
        pitanja();
      }
    }
  }

  void pitanja(){
    int answ = javax.swing.JOptionPane.showOptionDialog(
        this.mainPanel,
        "Želite poništiti inventuru?",
        "Pitanje",
        javax.swing.JOptionPane.YES_NO_OPTION,
        javax.swing.JOptionPane.QUESTION_MESSAGE,
        null,new String[] {"Da","Ne"},"Ne");

    if (answ == javax.swing.JOptionPane.OK_OPTION) {
      int answ2 = javax.swing.JOptionPane.showOptionDialog(
          this.mainPanel,
          new hr.restart.swing.raMultiLineMessage(new String[]{"OVA AKCIJA \u0106E POBRISATI INVENTURU.", "DA LI TO STVARNO ŽELITE? "}),
          "Upozorenje",
          javax.swing.JOptionPane.YES_NO_OPTION,
          javax.swing.JOptionPane.WARNING_MESSAGE,
          null,new String[] {"Da","Ne"},"Ne");
      if (answ2 != javax.swing.JOptionPane.OK_OPTION){
        actionPerformed = "NONE";
      } else {
        actionPerformed = "POIN";
      }
    } else {
      actionPerformed = "NONE";
    }
  }

  private void brisanjeInventure(){
    usporedbaQDS.setString("STATINV", "N");
    Timestamp stariNoviDatum = usporedbaQDS.getTimestamp("STARIDATINV");
    usporedbaQDS.setTimestamp("DATINV", stariNoviDatum);
    pripremaInventure();
  }

  private void pripremaInventure(){
    if (actionPerformed.equals("PRIN")){
      qInsertToInventura = sgQuerys.getSgQuerys().getStringInsertToInventura(fieldSet.getString("CSKL"), year);
      qUpdateRest = sgQuerys.getSgQuerys().getStringInsertToInventuraUpdateRest(fieldSet.getString("CSKL"));

    } else if (actionPerformed.equals("POIN")){
      qDeleteInventura =  sgQuerys.getSgQuerys().getStringDeleteInventura(fieldSet.getString("CSKL"));
    }
    transactionsForSave();
  }

  private void transactionsForSave(){
    if (actionPerformed.equals("PRIN")){
/*      raTransaction.getLocalTransaction(new String[] {qInsertToInventura}).execTransaction();
      raTransaction.getLocalTransaction(new String[] {qUpdateRest}).execTransaction();*/
      
      DataSet promet = new RaLogicStanjeSkladiste().datasetZaEkran(
          fieldSet.getString("CSKL"), "", "",  true, 
          vl.findYear(fieldSet.getTimestamp("DATUM")), true, "", "", false);
      QueryDataSet inv = Inventura.getDataModule().getTempSet(Condition.nil);
      inv.open();
      String[] cc = {"CSKL", "CART", "CART1", "BC", "NAZART", "JM", "ZC"};
      HashSet css = new HashSet();
      for (promet.first(); promet.inBounds(); promet.next()) {
        inv.insertRow(false);
        if (!css.add(new Integer(promet.getInt("CART")))) {
          System.out.println(promet);
        }
        dM.copyColumns(promet, inv, cc);
        Aus.set(inv, "KOLKNJ", promet, "KOL");
        Aus.set(inv, "VRIKNJ", promet, "VRI");
        Aus.set(inv, "KOLINV", "KOLKNJ");
        Aus.set(inv, "VRIINV", "VRIKNJ");
        inv.setBigDecimal("KOLVIS", Aus.zero3);
        inv.setBigDecimal("VRIVIS", Aus.zero2);
        inv.setBigDecimal("KOLMANJ", Aus.zero3);
        inv.setBigDecimal("VRIMANJ", Aus.zero2);
      }
      inv.saveChanges();
    } else {
      raTransaction.getLocalTransaction(new String[] {qDeleteInventura}).execTransaction();
    }
    raTransaction.saveChanges(usporedbaQDS);
  }
}