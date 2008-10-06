/****license*****************************************************************
**   file: frmObradaPL.java
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
package hr.restart.pl;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raFrame;
import hr.restart.util.raUpitLite;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmObradaPL extends raUpitLite {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
      findOther();
    }
  };
  JraButton jbSelCorg = new JraButton();
  JLabel jlCorg = new JLabel();
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
      findOther();
    }
  };
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JraTextField jtfGOD = new JraTextField();
  JraTextField jtfMJ = new JraTextField();
  JraTextField jtfRBR = new JraTextField();
  JraTextField jtfDATUM = new JraTextField();
  JraTextField jtfDAN = new JraTextField();
  TableDataSet tds = new TableDataSet();
  Column colGodina = new Column();
  Column colMjesec = new Column();
  Column colRbr = new Column();
  Column colDana = new Column();
  private Column colDatum = dM.createTimestampColumn("datum");
//  Column colDatum = new Column();
  Column colCORG = new Column();

  public frmObradaPL() {
    super(raFrame.DIALOG, null);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void componentShow() {
    System.out.println("componentShow");
    tds.open();
    if (tds.getRowCount()==0) {
      tds.insertRow(true);
    }

    jlrCorg.setText("");
    jlrCorg.emptyTextFields();
//    tds.setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG());
//    jlrCorg.forceFocLost();
//    findOther();
    setEnable(false);
    jlrCorg.requestFocus();
  }
  public boolean Validacija() {
    if (vl.isEmpty(jlrCorg)) return false;
    if (vl.isEmpty(jtfGOD)) return false;
    if (vl.isEmpty(jtfMJ)) return false;
    if (vl.isEmpty(jtfRBR)) return false;
//    if (vl.isEmpty(jtfDAN)) return false;
    return true;
  }
  public void okPress() {
  }
  public void firstESC() {
    setEnable(false);
    tds.setString("CORG", "");
    jlrCorg.emptyTextFields();
    jlrCorg.requestFocus();
  }
  public boolean isIspis() {
    return false;
  }
  public boolean runFirstESC() {
    if (jlrCorg.getText().equals("")) {
      return false;
    }
    return true;
  }
  public void showMessage() {
//    JOptionPane.showConfirmDialog(jp,"Inicijalizacija je uspješno završena !","Informacija",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
//    componentShow();
  }
  private void jbInit() throws Exception {
    this.getJdialog().setModal(true);
    jlrCorg.setNavButton(jbSelCorg);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
    jlrCorg.setSearchMode(0);
    jlrCorg.setVisCols(new int[] {0, 1});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setDataSet(tds);
    jlrCorg.setColumnName("CORG");
    jlCorg.setText("Org. jedinica");
    jbSelCorg.setText("...");
    jlrNaziv.setSearchMode(1);
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setFocusLostOnShow(false);
    this.setJPan(jp);
    jp.setLayout(xYLayout1);
    xYLayout1.setWidth(606);
    xYLayout1.setHeight(130);
    jLabel1.setText("Obraèun za");
    jLabel2.setText("Datum isplate");
    jLabel3.setText("Broj radnih dana");
    jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel4.setHorizontalTextPosition(SwingConstants.CENTER);
    jLabel4.setText("Godina");
    jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel5.setHorizontalTextPosition(SwingConstants.CENTER);
    jLabel5.setText("Mjesec");
    jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel6.setHorizontalTextPosition(SwingConstants.CENTER);
    jLabel6.setText("RBr");
    colGodina.setColumnName("GODINA");
    colGodina.setDataType(com.borland.dx.dataset.Variant.SHORT);
    colGodina.setServerColumnName("NewColumn1");
    colGodina.setSqlType(0);
    colMjesec.setColumnName("MJESEC");
    colMjesec.setDataType(com.borland.dx.dataset.Variant.SHORT);
    colMjesec.setServerColumnName("NewColumn2");
    colMjesec.setSqlType(0);
    colRbr.setColumnName("RBR");
    colRbr.setDataType(com.borland.dx.dataset.Variant.SHORT);
    colRbr.setServerColumnName("NewColumn1");
    colRbr.setSqlType(0);
    colDana.setColumnName("DANA");
    colDana.setDataType(com.borland.dx.dataset.Variant.SHORT);
    colDana.setDefault("0");
    colDana.setServerColumnName("NewColumn2");
    colDana.setSqlType(0);
//    colDatum.setColumnName("DATUM");
//    colDatum.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
//    colDatum.setDisplayMask("dd-MM-yyyy");
//    colDatum.setEditMask("dd-MM-yyyy");
//    colDatum.setServerColumnName("NewColumn1");
//    colDatum.setSqlType(0);
    colCORG.setColumnName("CORG");
    colCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    colCORG.setServerColumnName("NewColumn1");
    colCORG.setSqlType(0);
    tds.setColumns(new Column[] {colGodina, colMjesec, colRbr, colDana, colDatum, colCORG});
    jtfGOD.setColumnName("GODINA");
    jtfGOD.setDataSet(tds);
    jtfMJ.setColumnName("MJESEC");
    jtfMJ.setDataSet(tds);
    jtfRBR.setColumnName("RBR");
    jtfRBR.setDataSet(tds);
    jtfDAN.setColumnName("DANA");
    jtfDAN.setDataSet(tds);
    jtfDATUM.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATUM.setColumnName("DATUM");
    jtfDATUM.setDataSet(tds);
    jp.add(jlCorg, new XYConstraints(15, 20, -1, -1));
    jp.add(jlrCorg, new XYConstraints(150, 20, 100, -1));
    jp.add(jlrNaziv, new XYConstraints(255, 20, 305, -1));
    jp.add(jbSelCorg, new XYConstraints(565, 20, 21, 21));
    jp.add(jLabel1,   new XYConstraints(15, 65, -1, -1));
    jp.add(jLabel4,       new XYConstraints(150, 50, 55, -1));
    jp.add(jLabel5,   new XYConstraints(210, 50, 40, -1));
    jp.add(jLabel6,    new XYConstraints(255, 50, 40, -1));
    jp.add(jtfGOD,   new XYConstraints(150, 65, 55, -1));
    jp.add(jtfMJ,   new XYConstraints(210, 65, 40, -1));
    jp.add(jtfRBR,  new XYConstraints(255, 65, 40, -1));
    jp.add(jLabel3,     new XYConstraints(380, 65, -1, -1));
    jp.add(jLabel2,  new XYConstraints(15, 90, -1, -1));
    jp.add(jtfDATUM,  new XYConstraints(150, 90, 100, -1));
    jp.add(jtfDAN,       new XYConstraints(505, 65, 55, -1));
  }
  void findOther() {
    System.out.println("Aftzerlukamp");
    if (tds.getString("CORG").equals("")) return;
    raIniciranje.getInstance().posOrgsPl(tds.getString("CORG"));
//    lookupData ld = lookupData.getlookupData();
//    if (ld.raLocate(dm.getOrgpl(), new java.lang.String[] {"CORG"}, new java.lang.String[] {jlrCorg.getText()})) {
      if (dm.getOrgpl().getShort("GODOBR")!=0) {
        tds.setShort("GODINA", dm.getOrgpl().getShort("GODOBR"));
        tds.setShort("MJESEC", dm.getOrgpl().getShort("MJOBR"));
        tds.setShort("RBR", dm.getOrgpl().getShort("RBROBR"));
        tds.setShort("DANA", dm.getOrgpl().getShort("BROJDANA"));
        tds.setTimestamp("DATUM", dm.getOrgpl().getTimestamp("DATUMISPL"));
      }
      else {
        tds.setShort("GODINA", Short.parseShort(vl.findYear(hr.restart.util.Valid.getValid().getToday())));
        tds.setShort("MJESEC", (short) 1);
        tds.setShort("RBR", (short) 1);
        tds.setShort("DANA", (short) 0);
        tds.setTimestamp("DATUM", hr.restart.util.Valid.getValid().getToday());
      }
      if (!raParam.getParam(dm.getOrgpl(), raParam.ORGPL_STATUS).equals("I")) {
        setEnable(true);
      }
      jtfGOD.requestFocus();
/*    }
    else {
      jlrCorg.this_ExceptionHandling(null);
      jlrCorg.emptyTextFields();
    }*/
    doAfterLookup();
  }
  public void doAfterLookup() {
  }

  void setEnable(boolean enable) {
    setEnableCorg(enable);
    setEnableObr(enable);
  }

  void setEnableCorg(boolean enable) {
    rcc.setLabelLaF(jlrCorg, !enable);
    rcc.setLabelLaF(jlrNaziv, !enable);
    rcc.setLabelLaF(jbSelCorg, !enable);
  }

  public void setEnableObr(boolean enable) {
    rcc.setLabelLaF(jtfGOD, enable);
    rcc.setLabelLaF(jtfMJ, enable);
    rcc.setLabelLaF(jtfRBR, enable);
    rcc.setLabelLaF(jtfDATUM, enable);
    rcc.setLabelLaF(jtfDAN, enable);
  }
}