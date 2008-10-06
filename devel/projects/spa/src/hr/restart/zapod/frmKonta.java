/****license*****************************************************************
**   file: frmKonta.java
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
package hr.restart.zapod;
import hr.restart.baza.dM;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraLabel;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import java.awt.Font;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
/**
 * Title: Robno poslovanje
 * Description:
 * Copyright: Copyright (c) 2000
 * Company: REST-ART
 * @author REST-ART development team
 * @version 1.0
 */
public class frmKonta extends raMatPodaci {
  //sysoutTEST ST = new sysoutTEST(false);
  static frmKonta fkonta;
  Valid vl = Valid.getValid();
  raCommonClass rcc = new raCommonClass();
  java.util.ResourceBundle zpRes = java.util.ResourceBundle
      .getBundle(hr.restart.zapod.frmZapod.RESBUNDLENAME);
  /*
   * RADIOGROUP VRSTAKONTA
   * 
   * JPanel jPVrstaKonta = new JPanel();
   * 
   * JraRadioButton jdbRBAnaliticki = new JraRadioButton();
   * 
   * JraRadioButton jdbRBSinteticki = new JraRadioButton();
   *  
   */
  raComboBox racbVRSTAKONTA = new raComboBox();
  JPanel jp = new JPanel();
  JraTextField jtfNazivKonta = new JraTextField();
  JraTextField jtfBrojKonta = new JraTextField();
  JLabel jLNazivKonta = new JLabel();
  JLabel jLBrojKonta = new JLabel();
  JraCheckBox jdbCBispisBB = new JraCheckBox();
  JraCheckBox jdbCBOrgStr = new JraCheckBox();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JraCheckBox jcbPSOJ = new JraCheckBox();
  XYLayout xYLayout4 = new XYLayout();
  XYLayout xYLayout3 = new XYLayout();
  dM dm;
  TitledBorder tBorderVrstaKonta;
  TitledBorder tBorderKarakt;
  TitledBorder tBorderSK;
  raComboBox racbKARAKTERISTIKA = new raComboBox();
  raComboBox racbSALDAK = new raComboBox();
  raComboBox racbNACPR = new raComboBox();
  JLabel jlNACPR = new JLabel();
  JLabel jlVRSTAKONTA = new JLabel();
  JLabel jlKARAKTERISTKA = new JLabel();
  JLabel jlSALDAK = new JLabel();
  JraLabel jrlKARAKTERISTIKA = new JraLabel();
  JraLabel jrlVRSTAKONTA = new JraLabel();
  JraLabel jrlSALDAK = new JraLabel();
  JraLabel jrlNACPR = new JraLabel();
  JLabel jlKPlan = new JLabel();
  public frmKonta() {
    try {
      jbInit();
      fkonta = this;
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  public static frmKonta getFrmKonta() {
    if (fkonta == null)
      fkonta = new frmKonta();
    return fkonta;
  }
  private void jbInit() throws Exception {
    dm = dM.getDataModule();
    this.setRaDetailPanel(jp);
    this.setRaQueryDataSet(dm.getAllKonta());
    jp.setLayout(xYLayout3);
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N");
    jtfNazivKonta.setColumnName("NAZIVKONTA");
    jtfNazivKonta.setDataSet(getRaQueryDataSet());
    jtfBrojKonta.setColumnName("BROJKONTA");
    jtfBrojKonta.setDataSet(getRaQueryDataSet());
    jtfBrojKonta.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfBrojKonta_focusLost(e);
      }
    });
    jLNazivKonta.setText(zpRes.getString("jLNazivKonta_text"));
    jLBrojKonta.setText(zpRes.getString("jLBrojKonta_text"));
    jdbCBispisBB.setText(zpRes.getString("jdbCBispisBB_text"));
    jdbCBispisBB.setColumnName("ISPISBB");
    jdbCBispisBB.setDataSet(getRaQueryDataSet());
    jdbCBispisBB.setSelectedDataValue("1");
    jdbCBispisBB.setUnselectedDataValue("0");
    jdbCBOrgStr.setText(zpRes.getString("jdbCBOrgStr_text"));
    jdbCBOrgStr.setColumnName("ORGSTR");
    jdbCBOrgStr.setDataSet(getRaQueryDataSet());
    jdbCBOrgStr.setSelectedDataValue("D");
    jdbCBOrgStr.setUnselectedDataValue("N");
    jcbPSOJ.setText("Poèetno stanje po organizacijskim jedinicama");
    jcbPSOJ.setColumnName("PSORGSTR");
    jcbPSOJ.setDataSet(getRaQueryDataSet());
    jcbPSOJ.setSelectedDataValue("D");
    jcbPSOJ.setUnselectedDataValue("N");
    this.setVisibleCols(new int[] { 0, 1 });
    xYLayout3.setWidth(555);
    xYLayout3.setHeight(244);
    racbKARAKTERISTIKA.setRaDataSet(getRaQueryDataSet());
    racbKARAKTERISTIKA.setRaColumn("KARAKTERISTIKA");
    racbKARAKTERISTIKA.setRaItems(new String[][] {
    { "Oboje", "O" },
    { "Dugovni", "D" },
    { "Potražni", "P" }
    });
    racbSALDAK.setRaDataSet(getRaQueryDataSet());
    racbSALDAK.setRaColumn("SALDAK");
    racbSALDAK.setRaItems(new String[][] {
    { "Financijski konto", "F" },
    { "Kupac", "K" },
    { "Dobavlja\u010D", "D" }
    });
    racbVRSTAKONTA.setRaDataSet(getRaQueryDataSet());
    racbVRSTAKONTA.setRaColumn("VRSTAKONTA");
    racbVRSTAKONTA.setRaItems(new String[][] {
    { "Analiti\u010Dki", "A" },
    { "Sinteti\u010Dki", "S" }
    });
    racbNACPR.setRaDataSet(getRaQueryDataSet());
    racbNACPR.setRaColumn("NACPR");
    racbNACPR.setRaItems(new String[][] {
    { "Pojedina\u010Dno", "P" },
    { "Zbirno", "Z" }
    });
    jlNACPR.setText("Na\u010Din prijenosa");
    jlVRSTAKONTA.setText(zpRes.getString("jlVRSTAKONTA_text"));
    jlKARAKTERISTKA.setText(zpRes.getString("jlKARAKTERISTIKA_text"));
    jlSALDAK.setText(zpRes.getString("jlSALDAK_text"));
    jrlVRSTAKONTA.setFont(jrlVRSTAKONTA.getFont().deriveFont(Font.BOLD));
    jrlVRSTAKONTA.setColumnName("VRSTAKONTA");
    jrlVRSTAKONTA.setDataSet(getRaQueryDataSet());
    jrlKARAKTERISTIKA
        .setFont(jrlKARAKTERISTIKA.getFont().deriveFont(Font.BOLD));
    jrlKARAKTERISTIKA.setDataSet(getRaQueryDataSet());
    jrlKARAKTERISTIKA.setColumnName("KARAKTERISTIKA");
    jrlSALDAK.setColumnName("SALDAK");
    jrlSALDAK.setDataSet(getRaQueryDataSet());
    jrlSALDAK.setFont(jrlSALDAK.getFont().deriveFont(Font.BOLD));
    jrlNACPR.setColumnName("NACPR");
    jrlNACPR.setDataSet(getRaQueryDataSet());
    jrlNACPR.setFont(jrlSALDAK.getFont().deriveFont(Font.BOLD));
    jlKPlan.setText("Kontni plan");
    jp.add(jtfBrojKonta, new XYConstraints(150, 30, 80, -1));
    jp.add(racbVRSTAKONTA, new XYConstraints(150, 55, 150, -1));
    jp.add(jlVRSTAKONTA, new XYConstraints(15, 55, -1, -1));
    jp.add(jlKARAKTERISTKA, new XYConstraints(15, 80, -1, -1));
    jp.add(racbKARAKTERISTIKA, new XYConstraints(150, 80, 150, -1));
    jp.add(jlSALDAK, new XYConstraints(15, 105, -1, -1));
    jp.add(racbSALDAK, new XYConstraints(150, 105, 150, -1));
    jp.add(jlNACPR, new XYConstraints(15, 130, -1, -1));
    jp.add(racbNACPR, new XYConstraints(150, 130, 150, -1));
    jp.add(jrlNACPR, new XYConstraints(305, 130, -1, -1));
    jp.add(jdbCBOrgStr, new XYConstraints(150, 155, -1, -1));
    jp.add(jdbCBispisBB, new XYConstraints(150, 180, -1, -1));
    jp.add(jcbPSOJ, new XYConstraints(150, 205, -1, -1));
    jp.add(jrlKARAKTERISTIKA, new XYConstraints(305, 80, -1, -1));
    jp.add(jrlVRSTAKONTA, new XYConstraints(305, 55, -1, -1));
    jp.add(jrlSALDAK, new XYConstraints(305, 105, -1, -1));
    jp.add(jtfNazivKonta, new XYConstraints(235, 30, 305, -1));
    jp.add(jLBrojKonta, new XYConstraints(150, 12, -1, -1));
    jp.add(jLNazivKonta, new XYConstraints(235, 12, -1, -1));
    jp.add(jlKPlan, new XYConstraints(15, 30, -1, -1));
    jp.add(jcbAktiv, new XYConstraints(469, 7, 70, -1));
    //jdbCBispisBB.setNextFocusableComponent(jcbAktiv);
    raDataIntegrity.installFor(this);
  }
  public boolean Validacija(char mode) {
    if (mode == 'N') {
      if (!chkBrojKonta())
        return false;
      if (vl.notUnique(jtfBrojKonta))
        return false;
      if (!isKlasaExist())
        return false;
    }
    if (vl.isEmpty(jtfNazivKonta))
      return false;
    return true;
  }
  private boolean chkBrojKonta() {
    if (!chkMask()) {
      javax.swing.JOptionPane.showMessageDialog(null, zpRes
          .getString("errKont_unos"), zpRes.getString("jzpMenuKonta_text"),
          javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  private boolean isKlasaExist() {
    if (jtfBrojKonta.getText().trim().length() == 1)
      return true;
    String sintKlasa = "";
    if (jtfBrojKonta.getText().trim().length() < 4) {
      try {
        sintKlasa = jtfBrojKonta.getText(0, jtfBrojKonta.getText().trim()
            .length() - 1);
      } catch (Exception e) {
      }
    } else {
      try {
        sintKlasa = jtfBrojKonta.getText(0, 3);
      } catch (Exception e) {
      }
    }
    try {
      if (vl.chkExistsSQL(getRaQueryDataSet(), "BROJKONTA", sintKlasa)) {
        return true;
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    javax.swing.JOptionPane.showMessageDialog(null, zpRes
        .getString("errKont_klasa"), zpRes.getString("jzpMenuKonta_text"),
        javax.swing.JOptionPane.ERROR_MESSAGE);
    return false;
  }
  private boolean chkMask() {
    if (vl.chkIsEmpty(jtfBrojKonta))
      return false;
    String sBrKon = jtfBrojKonta.getText();
    if (sBrKon.charAt(0) == '-') {
      return false;
    }
    if (sBrKon.length() < 4)
      return isACDigit(sBrKon, "");
    if (!isACDigit(sBrKon.substring(0, 3), "")) {
      return false; //prva tri moraju biti brojevi
    }
    return isACDigit(sBrKon.substring(3), "/-"); //ostali mogu biti brojevi ili
                                                 // '-' ili '/'
  }
  private boolean isACDigit(String stringToCheck, String allovedNoDigitChars) {
    for (int i = 0; i < stringToCheck.length(); i++) {
      if (!(
      (java.lang.Character.isDigit(stringToCheck.charAt(i))) ||
      !(allovedNoDigitChars.indexOf(stringToCheck.charAt(i)) == -1)
      )) {
        return false;
      }
    }
    return true;
  }
  public void SetFokus(char mode) {
    if (mode == 'N') {
      jtfBrojKonta.setEnabled(true);
      jtfBrojKonta.requestFocus();
    } else if (mode == 'I') {
      rcc.setLabelLaF(jtfBrojKonta, false);
      jtfNazivKonta.requestFocus();
    }
    rcc.setLabelLaF(racbVRSTAKONTA, false);
    //    racbVRSTAKONTA.setEnabled(false);
  }
  public void EntryPoint(char mode) {
    //      racbVRSTAKONTA.setEnabled(false);
    //    jdbRBAnaliticki.setEnabled(false);
    //    jdbRBSinteticki.setEnabled(false);
  }
  void jtfBrojKonta_focusLost(FocusEvent e) {
    setVRSTAKONTA();
    if (chkMask()) {
      hr.restart.util.startFrame.getStartFrame().statusMSG();
    } else {
      hr.restart.util.startFrame.getStartFrame().statusMSG(
          zpRes.getString("errKont_unos"));
    }
  }
  void setVRSTAKONTA() {
    if (jtfBrojKonta.getText().length() < 4) {
      getRaQueryDataSet().setString("VRSTAKONTA", "S");
      racbVRSTAKONTA.setSelectedIndex(1);
    } else {
      getRaQueryDataSet().setString("VRSTAKONTA", "A");
      racbVRSTAKONTA.setSelectedIndex(0);
    }
  }
  {
  }
}
