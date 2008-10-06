/****license*****************************************************************
**   file: raPrintAllDocs.java
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

import hr.restart.swing.JraButton;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraFrame;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raPrintAllDocs extends JraFrame{
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  JPanel jPanel1 = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  TableDataSet tdsOd = new TableDataSet();
  TableDataSet tdsDo = new TableDataSet();
  Column column1 = new Column();
  Column column2 = new Column();
  String vrstaUI;
  JraComboBox jComboBox1 = new JraComboBox();
  JraTextField datumOd = new JraTextField();
  JraTextField datumDo = new JraTextField();
  JraTextField brojOd  = new JraTextField();
  JraTextField brojDo  = new JraTextField();
  JlrNavField cskl = new JlrNavField(){
    public void after_lookUp() {
    }
  };
  JlrNavField nazskl = new JlrNavField(){
    public void after_lookUp() {
    }
  };
  JraButton jbSelCorg = new JraButton();

  hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){
    public void jBOK_actionPerformed() {
      okPress();
    }
    public void jPrekid_actionPerformed() {
      cancel_press();
    }
  };

  hr.restart.util.reports.raRunReport TPRun = hr.restart.util.reports.raRunReport.getRaRunReport();
  public raPrintAllDocs() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public boolean runFirstESC(){
    return false;
  }

  public void firstESC(){}

  public void okPress(){

  if(getVrDokIdx()==0) {
    jComboBox1.requestFocus();
    JOptionPane.showConfirmDialog(null,"Niste izabrali vrstu dokumenta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    return;
  }


//    sysoutTEST ST = new sysoutTEST(false);
//    DataSet data = dm.getVrdokum();
//    data.open();
//    ST.prn(data);

//    System.out.println("OK pritisnut (ili F10, jeli :) )");
//    System.out.println("shaljem sljedece");
//    System.out.println("naziv dokumenta  : " + getVrDokOpis());
//    System.out.println("vrsta dokumenta  : " + getVrDok());
//    System.out.println("index vrste dok. : " + getVrDokIdx());
//    System.out.println("od datuma        : " + getPocDatum());
//    System.out.println("do datuma        : " + getZavDatum());
//    System.out.println("od broja         : " + getOdBroja());
//    System.out.println("do broja         : " + getDoBroja());
//    System.out.println("za skladiste     : " + getCSKL() + "   " + getNAZSKL());
//    System.out.println("sql: " + getQDString());
    reportsQuerysCollector.getRQCModule().ReSql(getQDString(),getVrDok());

    TPRun.clearAllReports();
    if (getVrDok().equals("PST")){
      TPRun.addReport("hr.restart.robno.repPocetnoStanje","Po\u010Detno stanje - koli\u010Dine",2);
      TPRun.addReport("hr.restart.robno.repPocetnoStanjeExtendedVersion","Po\u010Detno stanje - vrijednosti",2);
      TPRun.addReport("hr.restart.robno.repPocetnoStanjeMegablastVersion","Po\u010Detno stanje - kalkulacije",2);
    }
    else if (getVrDok().equals("PRK")){
      TPRun.addReport("hr.restart.robno.repPrkKol","hr.restart.robno.repPrkProvider","PriKalk", "Primka - kolièinska"); //"hr.restart.robno.repPriKalk","Primka - koli\u010Dinska",2);
      TPRun.addReport("hr.restart.robno.repPrkVri","hr.restart.robno.repPrkProvider","PriKalkExtendedVersion","Primka - vrijednosna"); //"hr.restart.robno.repPriKalkExtendedVersion","Primka - vrijednosna",2);
      TPRun.addReport("hr.restart.robno.repPrkKAl","hr.restart.robno.repPrkProvider","PriKalkMegablastVersion","Primka - kalkulacija"); //"hr.restart.robno.repPrkProvider","Primka - kalkulacija",2);
    }
    else if (getVrDok().equals("POR")){
/**
 * ne postoji
 */
    }
    else if (getVrDok().equals("OTP")){
//      TPRun.addReport("hr.restart.robno.repOTP","Otpremnica",2);
      TPRun.addReport("hr.restart.robno.repOTP","hr.restart.robno.repOTP","OTP","Otpremnica");
    }
    else if (getVrDok().equals("RAC")){
      TPRun.addReport("hr.restart.robno.repRac","hr.restart.robno.repIzlazni","Rac", "Raèun 1 red");
      TPRun.addReport("hr.restart.robno.repRac2","hr.restart.robno.repIzlazni","Rac2", "Raèun 2 red");
    }
    else if (getVrDok().equals("ROT")){
      TPRun.addReport("hr.restart.robno.repRacuni","hr.restart.robno.repIzlazni","Racuni","Raèun-otpremnica 1 red");
      TPRun.addReport("hr.restart.robno.repRacuni2","hr.restart.robno.repIzlazni","Racuni2","Raèun-otpremnica 2 red");
    }
    else if (getVrDok().equals("GRN")){
      TPRun.addReport("hr.restart.robno.repGrnRac","hr.restart.robno.repIzlazni","GrnRac","Raèuni 1 red");
      TPRun.addReport("hr.restart.robno.repGrnRac2","hr.restart.robno.repIzlazni","GrnRac2","Raèuni 2 red");
    }
    else if (getVrDok().equals("POS")){
      TPRun.addReport("hr.restart.robno.repPOS","Razduženje maloprodaje",2);
      TPRun.addReport("hr.restart.robno.repPOS","Razduženje maloprodaje",2);
    }
    else if (getVrDok().equals("IZD")){
      TPRun.addReport("hr.restart.robno.repIzdatnica","hr.restart.robno.repIzlazni","Izdatnica","Izdatnice");
      TPRun.addReport("hr.restart.robno.repIzdatnicaExtendedVersion","hr.restart.robno.repIzlazni","IzdatnicaExtendedVersion","Izdatnice s vrijednostima");
    }
    else if (getVrDok().equals("INV")){
      TPRun.addReport("hr.restart.robno.repPregledVisak","Ispis svih inventurnih viškova",2);
      TPRun.addReport("hr.restart.robno.repPregledVisakExtendedVersion","Ispis svih inventurnih viškova - vrijednosni",2);
    }
    else if (getVrDok().equals("INM")){
      TPRun.addReport("hr.restart.robno.repPregledManjak","Ispis svih inventurnih manjaka",2);
      TPRun.addReport("hr.restart.robno.repPregledManjakExtendedVersion","Ispis svih inventurnih manjaka - vrijednosni",2);
    }
    else if (getVrDok().equals("OTR")){
      TPRun.addReport("hr.restart.robno.repOtpisRobe","Ispis svih otpisa", 2);
      TPRun.addReport("hr.restart.robno.repOtpisRobeExtendedVersion","Ispis svih otpisa - koli\u010Dinski",2);
    }
    else if (getVrDok().equals("NKU")){
      TPRun.addReport("hr.restart.robno.repNarudzba","hr.restart.robno.repIzlazni","Narudzba","Narudžba 1 red");
      TPRun.addReport("hr.restart.robno.repNarudzba2","hr.restart.robno.repIzlazni","Narudzba2","Narudžba 2 red");
    }
    else if (getVrDok().equals("NDO")){
/**
 * ne postoji
 */
    }
    else if (getVrDok().equals("PRD")){
      TPRun.addReport("hr.restart.robno.repPredracuni","hr.restart.robno.repIzlazni","Predracuni","Raèun za predujam 1 red");
      TPRun.addReport("hr.restart.robno.repPredracuni2","hr.restart.robno.repIzlazni","Predracuni2","Raèun za predujam 2 red");
    }
    else if (getVrDok().equals("PON")){
      TPRun.addReport("hr.restart.robno.repPonuda","hr.restart.robno.repIzlazni","Ponuda","Ponuda 1 red");
      TPRun.addReport("hr.restart.robno.repPonuda2","hr.restart.robno.repIzlazni","Ponuda2","Ponuda 2 red");
    }
    else if (getVrDok().equals("MEI")){
      TPRun.addReport("hr.restart.robno.repMei","Me\u0111uskladišnica izlaz",2);
      TPRun.addReport("hr.restart.robno.repMeiExtendedVersion","Me\u0111uskladišnica izlaz - vrijednosna",2);
    }
    else if (getVrDok().equals("MEU")){
      TPRun.addReport("hr.restart.robno.repMeu","Me\u0111uskladišnica ulazna",2);
      TPRun.addReport("hr.restart.robno.repMeuExtendedVersion","Me\u0111uskladišnica ulazna - vrijednosna",2);
    }
    else if (getVrDok().equals("PTE")){
    }
    else if (getVrDok().equals("ODB")){
    }
    else if (getVrDok().equals("TER")){
    }
    else if (getVrDok().equals("POD")){
      TPRun.addReport("hr.restart.robno.repPovratnicaOdobrenje",
                                           "Ispis svih povratnica", 2);

      TPRun.addReport("hr.restart.robno.repPovratnicaOdobrenje",
                                           "Ispis povratnice - odobrenja", 2);

    }
    else if (getVrDok().equals("MES")){
      TPRun.addReport("hr.restart.robno.repMeskla","Me\u0111uskladišnice-koli\u010Dinske",2);
      TPRun.addReport("hr.restart.robno.repMesklaExtendedVersion","Me\u0111uskladišnice-vrijednosne",2);
    }
    else {
      System.out.println("neobradjeni dokument " +getVrDok());
    }

    TPRun.go();
  }
  public void cancel_press(){
    this.setVisible(false);
  }

  private void jbInit() throws Exception {

    TPRun.setOwner(this,getClass().getName());
    brojOd.setHorizontalAlignment(SwingConstants.RIGHT);
    brojDo.setHorizontalAlignment(SwingConstants.RIGHT);

    column1.setCaption("pocDatum");
    column1.setColumnName("pocDatum");
    column1.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    column1.setDisplayMask("dd-MM-yyyy");
//    column1.setEditMask("dd-MM-yyyy");
    column1.setResolvable(false);
    column1.setSqlType(0);
    column1.setServerColumnName("NewColumn1");

    column2.setCaption("zavDatum");
    column2.setColumnName("zavDatum");
    column2.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    column2.setDisplayMask("dd-MM-yyyy");
//    column2.setEditMask("dd-MM-yyyy");
    column2.setResolvable(false);
    column2.setSqlType(0);
    column2.setServerColumnName("NewColumn2");

    tdsDo.setColumns(new Column[] {column2});
    tdsOd.setColumns(new Column[] {column1});

    datumDo.setColumnName("zavDatum");
    datumDo.setText("");
    datumDo.setDataSet(tdsDo);
    datumDo.setHorizontalAlignment(SwingConstants.CENTER);

    datumOd.setColumnName("pocDatum");
    datumOd.setText("");
    datumOd.setDataSet(tdsOd);
    datumOd.setHorizontalAlignment(SwingConstants.CENTER);

    cskl.setColumnName("CSKL");
    cskl.setColNames(new String[] {"NAZSKL"});
    cskl.setTextFields(new javax.swing.text.JTextComponent[] {nazskl});
    cskl.setVisCols(new int[]{0,1});
    cskl.setSearchMode(0);
    cskl.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    cskl.setDataSet(dm.getSklad());
    cskl.setNavButton(jbSelCorg);

    nazskl.setNavProperties(cskl);
    nazskl.setColumnName("NAZSKL");
    nazskl.setSearchMode(1);

    jComboBox1.addItem("  -   Izaberite vrstu dokumenta   -");
    jComboBox1.addItem("Po\u010Detno stanje");
    jComboBox1.addItem("Primka - kalkulacija");
    jComboBox1.addItem("Poravnanje");
    jComboBox1.addItem("Otpremnica");
    jComboBox1.addItem("Ra\u010Dun");
    jComboBox1.addItem("Ra\u010Dun - otpremnica");
    jComboBox1.addItem("Gotovinski ra\u010Dun");
    jComboBox1.addItem("Gotovinski ra\u010Dun - otpremnica");
    jComboBox1.addItem("Razduženje POS-a");
    jComboBox1.addItem("Izdatnica");
    jComboBox1.addItem("Inventurni višak");
    jComboBox1.addItem("Inventurni manjak");
    jComboBox1.addItem("Otpis robe");
    jComboBox1.addItem("Narudžba kupca");
    jComboBox1.addItem("Narudžba dobavlja\u010Du");
    jComboBox1.addItem("Ra\u010Dun za predujam");
    jComboBox1.addItem("Ponuda");
    jComboBox1.addItem("Me\u0111uskladišnica izlaz");
    jComboBox1.addItem("Me\u0111uskladišnica ulaz");
    jComboBox1.addItem("Povratnica - tere\u0107enje");
    jComboBox1.addItem("Odobrenje");
    jComboBox1.addItem("Tere\u0107enje");
    jComboBox1.addItem("Povratnica odobrenje");
    jComboBox1.addItem("Me\u0111uskladišnica izlaz - ulaz");

    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        this_componentShown(e);
      }
    });
    this.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        this_keyPressed(e);
      }
    });
    this.getContentPane().add(jPanel1,BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);
    jLabel1.setText("Vrsta dokumenta");
    jPanel1.setLayout(xYLayout1);
    jLabel2.setText("Broj (OD - DO)");
    jLabel3.setText("Datum (OD - DO)");
    jLabel4.setText("Skladište");
    xYLayout1.setWidth(577);
    xYLayout1.setHeight(118);
    jPanel1.add(jLabel1,    new XYConstraints(15, 10, -1, -1));
    jPanel1.add(jLabel2,     new XYConstraints(15, 85, -1, -1));
    jPanel1.add(jLabel3,      new XYConstraints(15, 60, -1, -1));
    jPanel1.add(jComboBox1,           new XYConstraints(150, 10, 205, -1));
    jPanel1.add(datumOd,   new XYConstraints(150, 60, 100, -1));
    jPanel1.add(datumDo,   new XYConstraints(255, 60, 100, -1));
    jPanel1.add(brojOd,   new XYConstraints(150, 85, 100, -1));
    jPanel1.add(brojDo,   new XYConstraints(255, 85, 100, -1));
    jPanel1.add(jLabel4,   new XYConstraints(15, 35, -1, -1));
    jPanel1.add(cskl,   new XYConstraints(150, 35, 100, -1));
    jPanel1.add(nazskl,           new XYConstraints(255, 35, 285, -1));
    jPanel1.add(jbSelCorg,        new XYConstraints(545, 35, 21, 21));

  }

  public String getQDString(){
    String qds = "";
    String inORout = "";
    String sklad = "";
    String brojrangemin = "";
    String brojrangemax = "";
    String vrdok = "vrdok='" + getVrDok() + "' ";
    try {
      if (vrstaUI.equals("I")) inORout = "doki";
      if (vrstaUI.equals("U")) inORout = "doku";
      if (vrstaUI.equals("X")) inORout = "meskla";
    }
    catch (Exception ex) {}
    String datumrange = "and " + inORout + ".datdok >= '" + getPocDatum() + "' and " + inORout + ".datdok <= '" + getZavDatum() + "' ";
    if (!getOdBroja().equals("")) brojrangemin = "and " + inORout + ".brdok >=" + getOdBroja() + " ";
    if (!getDoBroja().equals("")) brojrangemax = "and " + inORout + ".brdok<=" + getDoBroja() + " ";
    if (!getCSKL().equals("")){
      if (!vrstaUI.equals("X")) sklad = "and " + inORout + ".cskl ='" + getCSKL() + "'";
      else sklad = "and " + inORout + ".cskliz ='" + getCSKL() + "' or " + inORout + ".csklul ='" + getCSKL() + "'";
    }
    return " and " +inORout+"."+ vrdok + datumrange + brojrangemin + brojrangemax + sklad;

  }

  void showDefaultValues() {
    tdsOd.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    tdsDo.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));
    cskl.setText("");
    nazskl.setText("");
    jComboBox1.requestFocus();
  }

  void this_componentShown(ComponentEvent e) {
    showDefaultValues();
  }
  public String getVrDok(){
    switch (getVrDokIdx()) {
      case  0: return "";
      case  1:
        vrstaUI = "U";
        return "PST";
      case  2:
        vrstaUI = "U";
        return "PRK";
      case  3:
        vrstaUI = "U";
        return "POR";
      case  4:
        vrstaUI = "I";
        return "OTP";
      case  5:
        vrstaUI = "I";
        return "RAC";
      case  6:
        vrstaUI = "I";
        return "ROT";
      case  7:
        vrstaUI = "I";
        return "GRN";
      case  8:
        vrstaUI = "I";
        return "OTP";
      case  9:
        vrstaUI = "I";
        return "POS";
      case 10:
        vrstaUI = "I";
        return "IZD";
      case 11:
        vrstaUI = "U";
        return "INV";
      case 12:
        vrstaUI = "I";
        return "INM";
      case 13:
        vrstaUI = "I";
        return "OTR";
      case 14:
        vrstaUI = "I";
        return "NKU";
      case 15:
        vrstaUI = "I";
        return "NDO";
      case 16:
        vrstaUI = "I";
        return "PRD";
      case 17:
        vrstaUI = "I";
        return "PON";
      case 18:
        vrstaUI = "X";
        return "MEI";
      case 19:
        vrstaUI = "X";
        return "MEU";
      case 20:
        vrstaUI = "U";
        return "PTE";
      case 21:
        vrstaUI = "I";
        return "ODB";
      case 22:
        vrstaUI = "I";
        return "TER";
      case 23:
        vrstaUI = "I";
        return "POD";
      case 24:
        vrstaUI = "X";
        return "MES";
    }
    return "";
  }
  public String getVrDokOpis(){
    return jComboBox1.getSelectedItem().toString();
  }
  public int getVrDokIdx(){
    return jComboBox1.getSelectedIndex();
  }
  public String getCSKL(){
    return cskl.getText().trim();
  }
  public String getNAZSKL(){
    return nazskl.getText().trim();
  }
  public java.sql.Timestamp getPocDatum() {
    return tdsOd.getTimestamp("pocDatum");
  }
  public java.sql.Timestamp getZavDatum() {
    return tdsDo.getTimestamp("zavDatum");
  }
  public String getOdBroja(){
    return brojOd.getText().trim();
  }
  public String getDoBroja(){
    return brojDo.getText().trim();
  }

  void this_keyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_F10) {
      okPress();
    }
    else if (e.getKeyCode()==e.VK_ESCAPE) {
      if (runFirstESC()) {
        firstESC();
      }
      else {
        cancel_press();
      }
    }
  }
}
