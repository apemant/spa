/****license*****************************************************************
**   file: jpPlacanje.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class jpPlacanje extends JPanel {
  static jpPlacanje jPlac;
  private raCommonClass rcc = raCommonClass.getraCommonClass();
  private Valid vl = Valid.getValid();
  private Util util = Util.getUtil();

  private XYLayout xYLayout1 = new XYLayout();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private JLabel jLabel3 = new JLabel();
  private JLabel jLabel4 = new JLabel();
  private JLabel jLabel5 = new JLabel();
  private JLabel jLabel6 = new JLabel();
  private JLabel jLabel7 = new JLabel();
  private JraTextField jtfDATDOK = new JraTextField();
  private JlrNavField jrfCNACPL = new JlrNavField();
  private JlrNavField jrfNAZNACPL = new JlrNavField();
  private JlrNavField jrfCBANKA = new JlrNavField();
  private JlrNavField jrfNAZBANKA = new JlrNavField();
  private JraTextField jtfBROJ_TRG = new JraTextField();
  private JraTextField jtfBROJ_CEK = new JraTextField();
  private JraTextField jtfIRATA = new JraTextField();
  private JraTextField jtfDATUM = new JraTextField();
  private JraTextField jtfUIRAC = new JraTextField();
  private JLabel jLabel8 = new JLabel();
  private JraButton jbCNACPL = new JraButton();
  private JraButton jbCBANKA = new JraButton();
  private JLabel jLabel9 = new JLabel();
  private JLabel jLabel10 = new JLabel();
  private JraTextField jtfBROJ = new JraTextField();
  private JraTextField jtfNAPLAC = new JraTextField();
  private static TableDataSet tdsTemp = new TableDataSet();
  private static QueryDataSet mDoki;
  public static QueryDataSet qdsRate;

  hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);

  private Column column1 = dM.createBigDecimalColumn("naplac");
  private Column column2 = dM.createStringColumn("broj", 15);
  private Column column3 = dM.createBigDecimalColumn("uirac");
  private Column column4 = dM.createTimestampColumn("datum");

  public static jpPlacanje getInstance() {
    if (jPlac==null) {
      jPlac=new jpPlacanje();
    }
    return jPlac;
  }

  public jpPlacanje() {
    jPlac=this;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public jpPlacanje(QueryDataSet mSet) {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dM dm = dM.getDataModule();

    this.setLayout(xYLayout1);
    jLabel1.setText("Naèin plaæanja");
    jLabel2.setText("Banka");
    jLabel3.setText("Tekuæi raèun");
    jLabel4.setText("Serijski broj èeka");
    jLabel5.setText("Iznos");
    jLabel6.setText("Datum za naplatu");
    jLabel7.setText("Iznos raèuna");
    jLabel8.setText("Datum raèuna");
    xYLayout1.setWidth(560);
    xYLayout1.setHeight(215);
    jbCNACPL.setText("...");
    jbCBANKA.setText("...");

    jtfDATDOK.setDataSet(tdsTemp);
    jtfDATDOK.setColumnName("DATUM");
    jtfDATDOK.setFont(jtfDATDOK.getFont().deriveFont(Font.BOLD));
    jtfDATDOK.setForeground(Color.red);
    jtfDATDOK.setHorizontalAlignment(SwingConstants.CENTER);
    jtfUIRAC.setDataSet(tdsTemp);
    jtfUIRAC.setColumnName("UIRAC");
    jtfUIRAC.setFont(jtfUIRAC.getFont().deriveFont(Font.BOLD));
    jtfUIRAC.setForeground(Color.red);

    jtfBROJ.setColumnName("BROJ");
    jtfBROJ.setDataSet(tdsTemp);
    jtfBROJ.setFont(jtfBROJ.getFont().deriveFont(Font.BOLD));
    jtfBROJ.setForeground(Color.red);
    jtfBROJ.setHorizontalAlignment(SwingConstants.CENTER);
    jtfNAPLAC.setColumnName("NAPLAC");
    jtfNAPLAC.setDataSet(tdsTemp);
    jtfNAPLAC.setFont(jtfNAPLAC.getFont().deriveFont(Font.BOLD));
    jtfNAPLAC.setForeground(Color.red);

    jtfBROJ_TRG.setDataSet(qdsRate);
    jtfBROJ_TRG.setColumnName("BROJ_TRG");
    jtfBROJ_CEK.setDataSet(qdsRate);
    jtfBROJ_CEK.setColumnName("BROJ_CEK");
    jtfIRATA.setDataSet(qdsRate);
    jtfIRATA.setColumnName("IRATA");
    jtfDATUM.setDataSet(qdsRate);
    jtfDATUM.setColumnName("DATUM");
    jtfBROJ.setDataSet(tdsTemp);
    jtfBROJ.setColumnName("BROJ");
    jtfNAPLAC.setDataSet(tdsTemp);
    jtfNAPLAC.setColumnName("NAPLAC");

    jrfCNACPL.setColumnName("CNACPL");
    jrfCNACPL.setDataSet(qdsRate);
    jrfCNACPL.setColNames(new String[] {"NAZNACPL"});
    jrfCNACPL.setVisCols(new int[]{0,1});
    jrfCNACPL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZNACPL});
    jrfCNACPL.setRaDataSet(dm.getNacpl());
    jrfCNACPL.setNavButton(jbCNACPL);
    jrfCNACPL.setRaDataSet(dm.getNacpl());
    jrfNAZNACPL.setColumnName("NAZNACPL");
    jrfNAZNACPL.setNavProperties(jrfCNACPL);
    jrfNAZNACPL.setSearchMode(1);

    jrfCBANKA.setColumnName("CBANKA");
    jrfCBANKA.setDataSet(qdsRate);
    jrfCBANKA.setColNames(new String[] {"NAZIV"});
    jrfCBANKA.setVisCols(new int[]{0,1});
    jrfCBANKA.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZBANKA});
    jrfCBANKA.setRaDataSet(dm.getBanke());
    jrfNAZBANKA.setColumnName("NAZIV");
    jrfNAZBANKA.setNavProperties(jrfCBANKA);
    jrfNAZBANKA.setSearchMode(1);

    jLabel9.setText("Broj raèuna");
    jLabel10.setText("Naplaæeno");
    column1.setColumnName("naplac");
    column1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    column1.setServerColumnName("NewColumn1");
    column1.setSqlType(0);
    column2.setColumnName("broj");
    column2.setDataType(com.borland.dx.dataset.Variant.STRING);
    column2.setServerColumnName("NewColumn2");
    column2.setSqlType(0);
    column3.setColumnName("uirac");
    column3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    column3.setServerColumnName("NewColumn1");
    column3.setSqlType(0);
/*    column4.setColumnName("datum");
    column4.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    column4.setServerColumnName("NewColumn2");
    column4.setSqlType(0);*/
    tdsTemp.setColumns(new Column[] {column1, column2, column3, column4});
    this.add(jLabel1, new XYConstraints(15, 75, -1, -1));
    this.add(jrfNAZNACPL, new XYConstraints(255, 75, 265, -1));
    this.add(jLabel2, new XYConstraints(15, 100, -1, -1));
    this.add(jrfCBANKA, new XYConstraints(150, 100, -1, -1));
    this.add(jbCNACPL, new XYConstraints(524, 75, 21, 21));
    this.add(jrfCNACPL, new XYConstraints(150, 75, -1, -1));
    this.add(jrfNAZBANKA, new XYConstraints(255, 100, 265, -1));
    this.add(jbCBANKA, new XYConstraints(524, 100, 21, 21));
    this.add(jLabel3, new XYConstraints(15, 125, -1, -1));
    this.add(jtfBROJ_TRG, new XYConstraints(150, 125, -1, -1));
    this.add(jLabel4, new XYConstraints(15, 150, -1, -1));
    this.add(jtfBROJ_CEK, new XYConstraints(150, 150, -1, -1));
    this.add(jLabel5, new XYConstraints(15, 175, -1, -1));
    this.add(jtfIRATA, new XYConstraints(150, 175, -1, -1));
    this.add(jLabel6,  new XYConstraints(300, 175, -1, -1));
    this.add(jtfDATUM, new XYConstraints(420, 175, -1, -1));
    this.add(jLabel7,  new XYConstraints(15, 45, -1, -1));
    this.add(jLabel10,   new XYConstraints(300, 45, -1, -1));
    this.add(jtfNAPLAC,    new XYConstraints(420, 45, 100, -1));
    this.add(jLabel8,    new XYConstraints(300, 20, -1, -1));
    this.add(jLabel9,  new XYConstraints(15, 20, -1, -1));
    this.add(jtfDATDOK,  new XYConstraints(420, 20, 100, -1));
    this.add(jtfBROJ,   new XYConstraints(150, 20, 100, -1));
    this.add(jtfUIRAC,  new XYConstraints(150, 45, -1, -1));
  }
  public static void setDataSet(QueryDataSet mSet, QueryDataSet dSet) {
    tdsTemp.open();
    if (tdsTemp.getRowCount()==0) tdsTemp.insertRow(false);
    String tmpString="000000"+mSet.getInt("BRDOK");
    tdsTemp.setString("broj", mSet.getString("GOD")+"-"+tmpString.substring(tmpString.length()-6, tmpString.length()));
    tdsTemp.setBigDecimal("uirac", mSet.getBigDecimal("UIRAC"));
    tdsTemp.setTimestamp("datum", mSet.getTimestamp("DATDOK"));
    tdsTemp.setBigDecimal("naplac", new java.math.BigDecimal(0.00));
    mDoki=mSet;
  }

  public void entryMe(char mode) {
    rcc.setLabelLaF(jtfBROJ, false);
    rcc.setLabelLaF(jtfDATDOK, false);
    rcc.setLabelLaF(jtfUIRAC, false);
    rcc.setLabelLaF(jtfNAPLAC, false);
    st.prn(mDoki);
    if (mode=='N') {
      qdsRate.setString("CSKL", mDoki.getString("CSKL"));
      qdsRate.setString("VRDOK", mDoki.getString("VRDOK"));
      qdsRate.setString("GOD", mDoki.getString("GOD"));
      qdsRate.setInt("BRDOK", mDoki.getInt("BRDOK"));
      qdsRate.setShort("RBR", util.getMaxRbr4Rate(mDoki));
    }
  }
  public void focusMe(char mode) {
    jrfCNACPL.requestFocus();
  }
  public void bindComp(QueryDataSet dSet) {

  }
}