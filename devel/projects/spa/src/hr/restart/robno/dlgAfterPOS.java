/****license*****************************************************************
**   file: dlgAfterPOS.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 * @version 1.0
 */

public class dlgAfterPOS extends JraDialog {
  static dlgAfterPOS dAfterPOS;
  double maxRata;
  double minRata;
  static char fromWhere;
  char okUnos='F';
  java.math.BigDecimal prvaRata;
  java.math.BigDecimal ostaleRate;
  java.math.BigDecimal maxBrojRata;
  _Main main;
  String bankKart;
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jp1 = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JraTextField jtfUKUPNO = new JraTextField();
  JraTextField jtfUIPOPUST = new JraTextField();
  JraTextField jtfPLATITI = new JraTextField();
  JraTextField jtfUPPOPUST = new JraTextField() {
    public void valueChanged() {
      jtfUPPOPUST_focusLost(null);
    }
  };
  JLabel jLabel4 = new JLabel();
  JPanel jp2 = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JLabel jLabel5 = new JLabel();
  JlrNavField jrfNAZNACPL = new JlrNavField() {
    public void after_lookUp() {
      afterNacPlac();
    }
  };
  JraButton jbCNACPL = new JraButton();
  JlrNavField jrfCNACPL = new JlrNavField() {
    public void after_lookUp() {
      afterNacPlac();
    }
  };
  OKpanel oKpanel1 = new OKpanel() {
    public void jBOK_actionPerformed() {
      pressOK();
    }
    public void jPrekid_actionPerformed() {
      pressCancel();
    }
  };
  dM dm;
  JLabel jLabel6 = new JLabel();
  JraTextField jtfBRRATA = new JraTextField();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel jLabel9 = new JLabel();
  JlrNavField jrfNAZIV = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JraButton jbCBANKA = new JraButton();
  JlrNavField jrfCBANKA = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JraTextField jtfTRG = new JraTextField();
  JraTextField jtfDAT_PRVE = new JraTextField();
  TableDataSet tds = new TableDataSet();
  Column column1 = new Column();
  Column column2 = new Column();
  Column column3 = new Column();
  Column column5 = new Column();
  JLabel jLabel10 = new JLabel();
  JraTextField jtfCEK = new JraTextField();

  public dlgAfterPOS(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
      dAfterPOS=this;
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public dlgAfterPOS() {
    this((Frame) _Main.getStartFrame(), "", false);
  }
  /**
   * Statichki getter
   */
  public static dlgAfterPOS getdlgAfterPOS(char mode) {
    fromWhere=mode;
    if (dAfterPOS == null) {
      dAfterPOS = new dlgAfterPOS();
    }
    return dAfterPOS;
  }
  void jbInit() throws Exception {
    this.setModal(true);
    this.setTitle("Dodatni podaci na ra\u010Dunu");
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
    dm = hr.restart.baza.dM.getDataModule();
    panel1.setLayout(borderLayout1);
    jp1.setLayout(xYLayout1);
    jLabel1.setText("Ukupno");
    jLabel2.setText("Popust");
    jLabel3.setText("Za platiti");
/*    jtfUKUPNO.setColumnName("UKUPNO");
    jtfUKUPNO.setDataSet(dm.getPos());
    jtfUIPOPUST.setColumnName("UIPOPUST2");
    jtfUIPOPUST.setDataSet(dm.getPos());
    jtfPLATITI.setColumnName("PLATITI");
    jtfPLATITI.setDataSet(dm.getPos());
    jtfUPPOPUST.setColumnName("UPPOPUST2");
    jtfUPPOPUST.setDataSet(dm.getPos());
*/    /*jtfUPPOPUST.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfUPPOPUST_focusLost(e);
      }
    });*/
    jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel4.setText("%");
    jp2.setLayout(xYLayout2);
    jLabel5.setText("Na\u010Din pla\u0107anja");

//    jrfCNACPL.setColumnName("CNACPL");
//    jrfCNACPL.setDataSet(dm.getPos());
    jrfCNACPL.setColNames(new String[] {"NAZNACPL"});
    jrfCNACPL.setVisCols(new int[]{0,1});
    jrfCNACPL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZNACPL});
    jrfCNACPL.setRaDataSet(dm.getNacpl());
    jrfCNACPL.setNavButton(jbCNACPL);
    jrfNAZNACPL.setColumnName("NAZNACPL");
    jrfNAZNACPL.setSearchMode(1);
    jrfNAZNACPL.setNavProperties(jrfCNACPL);

    xYLayout1.setWidth(450);
    xYLayout1.setHeight(125);
    xYLayout2.setWidth(450);
    xYLayout2.setHeight(145);
    jLabel6.setText("Broj rata");
//    jtfBRRATA.setColumnName("BRRATA");
//    jtfBRRATA.setDataSet(dm.getPos());
    jLabel7.setText("Banka");
    jLabel8.setText("Broj teku\u0107eg ra\u010Duna");
    jLabel9.setText("Datum prve rate");
    jrfNAZIV.setNavProperties(jrfCBANKA);
    jrfNAZIV.setSearchMode(1);
    jrfNAZIV.setColumnName("NAZIV");
    jrfCBANKA.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZIV});
    jrfCBANKA.setVisCols(new int[]{0,1});
    jrfCBANKA.setColNames(new String[] {"NAZIV"});
    jrfCBANKA.setDataSet(tds);
    jrfCBANKA.setNavButton(jbCBANKA);
    column1.setColumnName("CBANKA");
    column1.setDataType(com.borland.dx.dataset.Variant.STRING);
    column1.setPrecision(4);
    column1.setSqlType(0);
    column2.setColumnName("TRG");
    column2.setDataType(com.borland.dx.dataset.Variant.STRING);
    column2.setPrecision(20);
    column2.setSqlType(0);
    column3.setColumnName("DAT_PRVE");
    column3.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    column3.setDisplayMask("dd-MM-yyyy");
//    column3.setEditMask("dd-MM-yyyy");
    column3.setSqlType(0);
    jtfTRG.setColumnName("TRG");
    jtfTRG.setDataSet(tds);
    jtfDAT_PRVE.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDAT_PRVE.setColumnName("DAT_PRVE");
    jtfDAT_PRVE.setDataSet(tds);
    jp1.setBorder(BorderFactory.createEtchedBorder());
    jp2.setBorder(BorderFactory.createEtchedBorder());
    column5.setColumnName("CEK");
    column5.setDataType(com.borland.dx.dataset.Variant.STRING);
    column5.setSqlType(0);
    column5.setServerColumnName("NewColumn1");
    jLabel10.setText("Broj prvog èeka");
    jtfCEK.setColumnName("CEK");
    jtfCEK.setDataSet(tds);
    tds.setColumns(new Column[] {column1, column2, column3, column5});
    getContentPane().add(panel1);
    panel1.add(jp2, BorderLayout.CENTER);
    panel1.add(oKpanel1, BorderLayout.SOUTH);
    panel1.add(jp1, BorderLayout.NORTH);
    jp1.add(jLabel1, new XYConstraints(15, 20, -1, -1));
    jp1.add(jLabel2, new XYConstraints(15, 45, -1, -1));
    jp1.add(jLabel3, new XYConstraints(15, 70, -1, -1));
    jp1.add(jtfUKUPNO, new XYConstraints(210, 20, 100, -1));
    jp1.add(jtfUIPOPUST, new XYConstraints(210, 45, 100, -1));
    jp1.add(jtfPLATITI, new XYConstraints(210, 70, 100, -1));
    jp1.add(jtfUPPOPUST, new XYConstraints(150, 45, 50, -1));
    jp1.add(jLabel4, new XYConstraints(125, 45, 20, -1));
    jp1.add(jLabel5, new XYConstraints(15, 95, -1, -1));
    jp1.add(jrfCNACPL, new XYConstraints(150, 95, 50, -1));
    jp1.add(jrfNAZNACPL, new XYConstraints(210, 95, 200, -1));
    jp1.add(jbCNACPL, new XYConstraints(414, 95, 21, 21));
    jp2.add(jLabel6, new XYConstraints(15, 5, -1, -1));
    jp2.add(jtfBRRATA, new XYConstraints(150, 5, 50, -1));
    jp2.add(jLabel7, new XYConstraints(15, 30, -1, -1));
    jp2.add(jLabel8, new XYConstraints(15, 55, -1, -1));
    jp2.add(jrfCBANKA, new XYConstraints(150, 30, 50, -1));
    jp2.add(jrfNAZIV, new XYConstraints(210, 30, 200, -1));
    jp2.add(jbCBANKA, new XYConstraints(414, 30, 21, 21));
    jp2.add(jtfTRG,  new XYConstraints(150, 55, 200, -1));
    jp2.add(jLabel9, new XYConstraints(15, 105, -1, -1));
    jp2.add(jtfDAT_PRVE, new XYConstraints(150, 105, 100, -1));
    jp2.add(jLabel10,   new XYConstraints(15, 80, -1, -1));
    jp2.add(jtfCEK,   new XYConstraints(150, 80, 200, -1));
  }
  void pressOK() {
    if (dm.getNacpl().getString("FL_CEK").equals("D")) {
      if (!posValidacija()) {
        return;
      }
      bankKart="C";
      if (makeRate()) {
        okUnos='M';
      }
      else {
        return;
      }
    }
    else if (dm.getNacpl().getString("FL_KARTICA").equals("D")) {
      if (!posValidacija()) {
        return;
      }
      bankKart="K";
      if (makeRate()) {
        okUnos='M';
      }
      else {
        return;
      }
    }
    else {
      if (fromWhere=='B') {
        okUnos='T';
      }
    }
    this.hide();
  }
  void pressCancel() {
    okUnos='F';
    this.hide();
  }
  void this_keyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_ESCAPE) {
      pressCancel();
    }
    else if (e.getKeyCode()==e.VK_F10) {
      pressOK();
    }
  }
  void this_componentShown(ComponentEvent e) {
    jrfCNACPL.forceFocLost();
    rcc.setLabelLaF(this.jtfUKUPNO, false);
    rcc.setLabelLaF(this.jtfUIPOPUST, false);
    rcc.setLabelLaF(this.jtfPLATITI, false);
    rcc.setLabelLaF(this.jrfCBANKA, false);
    rcc.setLabelLaF(this.jrfNAZIV, false);
    rcc.setLabelLaF(this.jbCBANKA, false);
    rcc.setLabelLaF(this.jtfBRRATA, false);
    rcc.setLabelLaF(this.jtfTRG, false);
    rcc.setLabelLaF(this.jtfCEK, false);
    rcc.setLabelLaF(this.jtfDAT_PRVE, false);
    if (tds.rowCount()>0) {
      tds.deleteRow();
    }
    jtfUPPOPUST.requestFocus();
    jtfUPPOPUST.selectAll();
  }
  void jtfUPPOPUST_focusLost(FocusEvent e) {
    if (fromWhere=='B') {
      dm.getPos().setBigDecimal("UIPOPUST2", dm.getPos().getBigDecimal("UKUPNO").multiply(dm.getPos().getBigDecimal("UPPOPUST2").divide(main.sto,1)));
      dm.getPos().setBigDecimal("PLATITI", dm.getPos().getBigDecimal("UKUPNO").add(dm.getPos().getBigDecimal("UIPOPUST2").negate()));
    }
    else if (fromWhere=='G') {
      dm.getZagPos().setBigDecimal("UIPOPUST", dm.getZagPos().getBigDecimal("UIRAC").multiply(dm.getZagPos().getBigDecimal("UPPOPUST").divide(main.sto,1)));
      dm.getZagPos().setBigDecimal("PLATITI", dm.getZagPos().getBigDecimal("UIRAC").add(dm.getZagPos().getBigDecimal("UIPOPUST").negate()));
    }
  }
    hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  void afterNacPlac() {
    System.out.println("FromWhere: "+fromWhere);
    dm.getNacpl().open();
// Tomo prepravio
    if (fromWhere=='B') {
      if (!lD.raLocate(dm.getNacpl(),"CNACPL",dm.getPos().getString("CNACPL"))){
        System.err.println("Greška nisu naðeni naèini plaæanja linija 323 source dlgAfterPOS.java");
    }
//      dm.getNacpl().interactiveLocate(dm.getPos().getString("CNACPL"), "CNACPL" ,com.borland.dx.dataset.Locate.FIRST, false);
//
    }
    else if (fromWhere=='G') {
// Tomo prepravio
      if (!lD.raLocate(dm.getNacpl(),"CNACPL",dm.getZagPos().getString("CNACPL"))){
        System.err.println("Greška nisu naðeni naèini plaæanja linija 331 source dlgAfterPOS.java !!!!");
    }
//      dm.getNacpl().interactiveLocate(dm.getZagPos().getString("CNACPL"), "CNACPL" ,com.borland.dx.dataset.Locate.FIRST, false);
//
    }
    if (dm.getNacpl().getString("FL_CEK").equals("D")) {
      jLabel7.setText("Banka");
      jLabel8.setText("Broj teku\u0107eg ra\u010Duna");
      jLabel10.setText("Broj prvog èeka");
      jrfCBANKA.setRaDataSet(dm.getBanke());
      jrfCBANKA.setColumnName("CBANKA");
      jrfCBANKA.setNavColumnName("CBANKA");
      MakeNewTempRecord();
    }
    else if (dm.getNacpl().getString("FL_KARTICA").equals("D")) {
      jLabel7.setText("Kartica");
      jLabel8.setText("Broj kartice");
      jLabel10.setText("Broj prvog slipa");
      jrfCBANKA.setRaDataSet(dm.getKartice());
      jrfCBANKA.setColumnName("CBANKA");
      jrfCBANKA.setNavColumnName("CBANKA");
      MakeNewTempRecord();
    }
    else {
      rcc.setLabelLaF(this.jrfCBANKA, false);
      rcc.setLabelLaF(this.jrfNAZIV, false);
      rcc.setLabelLaF(this.jbCBANKA, false);
      rcc.setLabelLaF(this.jtfBRRATA, false);
      rcc.setLabelLaF(this.jtfTRG, false);
      rcc.setLabelLaF(this.jtfCEK, false);
      rcc.setLabelLaF(this.jtfDAT_PRVE, false);
    }
  }
  void MakeNewTempRecord() {
    rcc.setLabelLaF(this.jrfCBANKA, true);
    rcc.setLabelLaF(this.jrfNAZIV, true);
    rcc.setLabelLaF(this.jbCBANKA, true);
    rcc.setLabelLaF(this.jtfBRRATA, true);
    rcc.setLabelLaF(this.jtfTRG, true);
    rcc.setLabelLaF(this.jtfCEK, true);
    rcc.setLabelLaF(this.jtfDAT_PRVE, true);
    if (tds.rowCount()==0) {
      tds.insertRow(false);
    }
    tds.setTimestamp("DAT_PRVE",vl.findDate(false,0));
    if (fromWhere=='B') {
      dm.getPos().setShort("BRRATA", (short) 1);
    }
    else if (fromWhere=='G') {
      dm.getZagPos().setShort("BRRATA", (short) 1);
    }
    jrfCBANKA.forceFocLost();
    jtfBRRATA.requestFocus();
  }
  boolean posValidacija() {
    if (vl.isEmpty(jrfCBANKA)) {
      return false;
    }
    if (vl.isEmpty(jtfTRG)) {
      return false;
    }
    if (vl.isEmpty(jtfCEK)) {
      return false;
    }
    return true;
  }
  boolean makeRate() {
    findMinMaxRata();
    short rata=0;
    if (fromWhere=='B') {
      rata=dm.getPos().getShort("BRRATA");
    }
    else if (fromWhere=='G') {
      rata=dm.getZagPos().getShort("BRRATA");
    }
    if (rata>maxBrojRata.shortValue()) {
      JOptionPane.showConfirmDialog(null,"Broj rata veæi od dozvoljenog !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      jtfBRRATA.requestFocus();
      return false;
    }
    return true;
  }
  void findMinMaxRata() {
// Tomo popravak
//    jrfCBANKA.getRaDataSet().interactiveLocate(jrfCBANKA.getText(), jrfCBANKA.getColumnName(),com.borland.dx.dataset.Locate.FIRST, false);
    if (!lD.raLocate(jrfCBANKA.getRaDataSet(),jrfCBANKA.getColumnName(),jrfCBANKA.getText())){
      System.err.println("Greška nisu naðene banke linija 416 source dlgAfterPOS.java !!!!");
    }

    minRata=jrfCBANKA.getRaDataSet().getBigDecimal("MIN_IZNOS").doubleValue();
    maxRata=jrfCBANKA.getRaDataSet().getBigDecimal("MAX_IZNOS").doubleValue();
    maxBrojRata = new java.math.BigDecimal(jrfCBANKA.getRaDataSet().getShort("MAX_RATA"));
    if (fromWhere=='B') {
      ostaleRate=util.divideValue(dm.getPos().getBigDecimal("PLATITI"),new java.math.BigDecimal(dm.getPos().getShort("BRRATA")));
      ostaleRate=ostaleRate.setScale(0, java.math.BigDecimal.ROUND_DOWN);
      prvaRata=util.negateValue(dm.getPos().getBigDecimal("PLATITI"), util.multiValue(ostaleRate, new java.math.BigDecimal(dm.getPos().getShort("BRRATA")-1)));
    }
    else if (fromWhere=='G') {
      ostaleRate=util.divideValue(dm.getZagPos().getBigDecimal("PLATITI"),new java.math.BigDecimal(dm.getZagPos().getShort("BRRATA")));
      ostaleRate=ostaleRate.setScale(0, java.math.BigDecimal.ROUND_DOWN);
      prvaRata=util.negateValue(dm.getZagPos().getBigDecimal("PLATITI"), util.multiValue(ostaleRate, new java.math.BigDecimal(dm.getZagPos().getShort("BRRATA")-1)));
    }
  }
  String findCek(String cek, int broj) {
    if (broj==0) {
      return cek;
    }
    return (util.getNumeric(cek).add(new java.math.BigDecimal(broj))).toString();
  }
  java.sql.Timestamp findDatum(int i) {
    return vl.findDate(false, i*(new java.lang.Integer(hr.restart.sisfun.frmParam.getParam("robno","danaOdgode")).intValue()));
  }
  public void show(char mode) {
    if (fromWhere=='B') {     // iz blagajne
      jtfUKUPNO.setColumnName("UKUPNO");
      jtfUKUPNO.setDataSet(dm.getPos());
      jtfUIPOPUST.setColumnName("UIPOPUST2");
      jtfUIPOPUST.setDataSet(dm.getPos());
      jtfPLATITI.setColumnName("PLATITI");
      jtfPLATITI.setDataSet(dm.getPos());
      jtfUPPOPUST.setColumnName("UPPOPUST2");
      jtfUPPOPUST.setDataSet(dm.getPos());
      jrfCNACPL.setColumnName("CNACPL");
      jrfCNACPL.setDataSet(dm.getPos());
      jtfBRRATA.setColumnName("BRRATA");
      jtfBRRATA.setDataSet(dm.getPos());
    }
    else if (fromWhere=='G') {
      jtfUKUPNO.setColumnName("UIRAC");
      jtfUKUPNO.setDataSet(dm.getZagPos());
      jtfUIPOPUST.setColumnName("UIPOPUST");
      jtfUIPOPUST.setDataSet(dm.getZagPos());
      jtfPLATITI.setColumnName("PLATITI");
      jtfPLATITI.setDataSet(dm.getZagPos());
      jtfUPPOPUST.setColumnName("UPPOPUST");
      jtfUPPOPUST.setDataSet(dm.getZagPos());
      jrfCNACPL.setColumnName("CNACPL");
      jrfCNACPL.setDataSet(dm.getZagPos());
      jtfBRRATA.setColumnName("BRRATA");
      jtfBRRATA.setDataSet(dm.getZagPos());
    }
    super.show();
  }
/**
 * @deprecated
 * @return
 */
  public boolean isOK() {
    System.out.println("isOK");
    show('B');
    System.out.println("Vracam: "+okUnos);
    return (okUnos=='T');
  }
  public char chkOK() {
    show('B');
    return okUnos;
  }
}