/****license*****************************************************************
**   file: upPocStanje.java
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
package hr.restart.os;

import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raUpitLite;

import java.awt.Color;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
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

public class upPocStanje extends raUpitLite {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
  java.util.TreeSet p;
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  JPanel jp = new JPanel();

  XYLayout xYLayout1 = new XYLayout();
  JlrNavField jrfCORG = new JlrNavField() {
    public void after_lookUp() {
      afterCORG();
    }
  };
  JlrNavField jrfNAZORG = new JlrNavField() {
    public void after_lookUp() {
      afterCORG();
    }
  };
  JraButton jbCORG = new JraButton();
  JLabel jlCORG = new JLabel();
  JLabel jLabel1 = new JLabel();
  JraTextField jtfGODINA = new JraTextField() {
    public void valueChanged() {
      jtfGODINA_focusLost(null);
    }
  };
  TableDataSet tds = new TableDataSet();
  Column column1 = new Column();
  Column column2 = new Column();

  public upPocStanje() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void componentShow() {
//    String[] pero = {"aba", "aaa", "ècè", "bbb", "aca", "ggg", "aaa"};
//    st.prn(pero);
//    java.util.Arrays.sort(pero);
//    st.prn(pero);
    if (tds.getRowCount()==0) {
      tds.insertRow(true);
    }
    jrfCORG.setText(hr.restart.zapod.OrgStr.getKNJCORG());
    jrfCORG.forceFocLost();
    hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
    dm.getOS_Kontrola().open();
    if (ld.raLocate(dm.getOS_Kontrola(),
      new java.lang.String[] {"CORG"},
      new java.lang.String[] {jrfCORG.getText()})) {
      int god1=new Integer(dm.getOS_Kontrola().getString("GODINA")).intValue();
      System.out.println("GOD1: "+god1);
      god1=god1+1;
      System.out.println("GOD2: "+god1);
      tds.setString("GODINA", String.valueOf(god1));
//      jtfGODINA.setText(String.valueOf(god1));
    }
    else {
      JOptionPane.showConfirmDialog(jp ,"Odabrano knjigovodstvo nije aktivno u osnovnim sredstvima !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      this.hide();
    }
    rcc.setLabelLaF(jrfCORG, false);
    rcc.setLabelLaF(jrfNAZORG, false);
    rcc.setLabelLaF(jbCORG, false);
    rcc.setLabelLaF(jtfGODINA, false);
  }
  public void firstESC() {

  }
  public boolean runFirstESC() {
    return false;
  }
  public boolean isIspis() {
    return false;
  }
  public void showMessage() {
    JOptionPane.showConfirmDialog(null,"Formiranje poèetnog stanja uspješno završeno !","Informacija",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
  }

  public boolean Validacija() {
    System.out.println("okpress");
    if (vl.isEmpty(jrfCORG)) {
      return false;
    }
    if (vl.isEmpty(jtfGODINA)) {
      return false;
    }
    hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
    dm.getOS_Kontrola().open();
    tds.open();
    if (ld.raLocate(dm.getOS_Kontrola(),
      new com.borland.dx.dataset.DataSet[] {tds},
      new java.lang.String[] {"CORG"},
      new java.lang.String[] {"CORG"})) {
      if (Integer.parseInt(tds.getString("GODINA"))==Integer.parseInt(vl.findYear(dm.getOS_Kontrola().getTimestamp("DATUM")))+1) {
        System.out.println("Sve je super i sve je za pet");
      }
      else {
//        JOptionPane.showConfirmDialog(null,"Nije moguæe izvršiti prijenos u navedenu godinu !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        JOptionPane.showConfirmDialog(null,"Pogrešna godina !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        jtfGODINA.requestFocus();
        return false;
      }
    }
    else {
      JOptionPane.showConfirmDialog(null,"Odabrano knjigovodstvo nije aktivno u osnovnim sredstvima !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      jrfCORG.setText("");

      jrfCORG.forceFocLost();
      jrfCORG.requestFocus();
      return false;
    }
    System.out.println("Kontrola 1: "+dm.getOS_Kontrola().getString("CORG")+", "+tds.getString("CORG"));
//    dm.getOS_Kontrola().interactiveLocate(tds.getString("CORG"), "CORG", com.borland.dx.dataset.Locate.FIRST, false);
    if (!dm.getOS_Kontrola().getString("AMOR").equals("D")) {
//      JOptionPane.showConfirmDialog(null,"Prijenos nije moguæe izvršiti, dok se ne izvrši amortizacija !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      JOptionPane.showConfirmDialog(null,"Potrebno je obaviti konaèni obraèun amortizacije !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (JOptionPane.showConfirmDialog(null,"Da li \u017Eelite izvršiti prijenos u novu godinu ?","Formiranje poèetnog stanja",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
      if (JOptionPane.showConfirmDialog(null,"Da li ste sigurni ?","Formiranje poèetnog stanja",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
        return true;
      }
    }
    return false;
  }
  public void okPress() {
    dm.getOS_Kontrola().setTimestamp("DATUM", util.findFirstDayOfYear(Integer.parseInt(tds.getString("GODINA"))));
    dm.getOS_Kontrola().setString("GODINA", vl.findYear(dm.getOS_Kontrola().getTimestamp("DATUM")));
    dm.getOS_Kontrola().setString("PROM", "N");
    dm.getOS_Kontrola().setString("AMOR", "N");
    dm.getOS_Kontrola().setString("REVA", "N");
    dm.getOS_Kontrola().saveChanges();
    hr.restart.baza.OS_Sredstvo.getDataModule().setFilter("CORG="+dm.getOS_Kontrola().getString("CORG")+" AND AKTIV='D'");
    dm.getOS_Sredstvo().open();
    dm.getOS_Sredstvo().first();
    do {
      dm.getOS_Sredstvo().setBigDecimal("OSNPOCETAK", dm.getOS_Sredstvo().getBigDecimal("OSNOVICA"));
      dm.getOS_Sredstvo().setBigDecimal("ISPPOCETAK", dm.getOS_Sredstvo().getBigDecimal("ISPRAVAK"));
      dm.getOS_Sredstvo().setBigDecimal("OSNDUGUJE", util.nul);
      dm.getOS_Sredstvo().setBigDecimal("OSNPOTRAZUJE", util.nul);
      dm.getOS_Sredstvo().setBigDecimal("ISPDUGUJE", util.nul);
      dm.getOS_Sredstvo().setBigDecimal("ISPPOTRAZUJE", util.nul);
      dm.getOS_Sredstvo().setBigDecimal("REVOSN", util.nul);
      dm.getOS_Sredstvo().setBigDecimal("REVISP", util.nul);
      dm.getOS_Sredstvo().setBigDecimal("AMORTIZACIJA", util.nul);
      dm.getOS_Sredstvo().setBigDecimal("PAMORTIZACIJA", util.nul);
      dm.getOS_Sredstvo().next();
    } while (dm.getOS_Sredstvo().inBounds());
    dm.getOS_Sredstvo().saveChanges();
    osUtil.getUtil().emptyAmorTable(false);
    this.hide();
  }
  private void jbInit() throws Exception {
    this.setJPan(jp);

    jlCORG.setText("Knjigovodstvo");
    jbCORG.setText("...");
    jrfNAZORG.setNavProperties(jrfCORG);
    jrfNAZORG.setSearchMode(1);
    jrfNAZORG.setColumnName("NAZIV");
    jrfCORG.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZORG});
    jrfCORG.setColNames(new String[] {"NAZIV"});
    jrfCORG.setSearchMode(0);
    jrfCORG.setVisCols(new int[] {0,1});
    jrfCORG.setRaDataSet(dm.getKnjig());
    jrfCORG.setDataSet(tds);
    jrfCORG.setColumnName("CORG");
    jrfCORG.setNavButton(jbCORG);
    jtfGODINA.setDataSet(tds);

    /*jtfGODINA.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfGODINA_focusLost(e);
      }
    });*/
    jtfGODINA.setColumnName("GODINA");
    jtfGODINA.setHorizontalAlignment(SwingConstants.CENTER);

    xYLayout1.setWidth(640);
    xYLayout1.setHeight(85);
    jp.setLayout(xYLayout1);
    jLabel1.setText("Godina");
    column1.setColumnName("CORG");
    column1.setDataType(com.borland.dx.dataset.Variant.STRING);
    column1.setServerColumnName("NewColumn1");
    column1.setSqlType(0);
    column2.setColumnName("GODINA");
    column2.setDataType(com.borland.dx.dataset.Variant.STRING);
    column2.setServerColumnName("NewColumn2");
    column2.setSqlType(0);
    //Rade ------
    column2.setPrecision(4);
    //-----------
    tds.setColumns(new Column[] {column1, column2});
    jp.add(jrfCORG,  new XYConstraints(150, 20, 100, -1));
    jp.add(jrfNAZORG,  new XYConstraints(255, 20, 345, -1));
    jp.add(jbCORG,    new XYConstraints(604, 20, 21, 21));
    jp.add(jlCORG,     new XYConstraints(15, 20, -1, -1));
    jp.add(jLabel1,  new XYConstraints(15, 45, -1, -1));
    jp.add(jtfGODINA,   new XYConstraints(150, 45, 100, -1));
  }
  private void afterCORG() {
/*    hr.restart.util.lookupData ld = new hr.restart.util.lookupData();
//    tds.open();
    dm.getOS_Kontrola().open();
    if (ld.raLocate(dm.getOS_Kontrola(),
      new com.borland.dx.dataset.DataSet[] {tds},
      new java.lang.String[] {"CORG"},
      new java.lang.String[] {"CORG"})) {
      tds.setString("GODINA", vl.findYear(dm.getOS_Kontrola().getTimestamp("DATUM")));
    }
    else {
      jrfCORG.setText("");
      jrfCORG.forceFocLost();
    }*/
  }

  // Dodao Rade
  void jtfGODINA_focusLost(FocusEvent e) {
     try {
      rdOSUtil.getUtil().provjeraGodine(jtfGODINA);
    }
    catch (Exception ex) {
      jtfGODINA.setBackground(Color.red);
      jtfGODINA.requestFocus();
    }
  }

}