/****license*****************************************************************
**   file: frmPripremaToAktiv.java
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
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;
import hr.restart.util.sysoutTEST;

import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataRow;
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

public class frmPripremaToAktiv extends raUpitLite {
  com.borland.dx.sql.dataset.QueryDataSet qdsLokacije;
  com.borland.dx.sql.dataset.QueryDataSet qdsObjekti;
  sysoutTEST st = new sysoutTEST(false);
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JlrNavField jrfNazKonto = new JlrNavField();
  JlrNavField jrfCOBJEKT = new JlrNavField(){
/*     public void keyF9Pressed() {
      newKeyF9Pressed();
    }*/
  };
  JlrNavField jrfCORG = new JlrNavField(){
/*     public void keyF9Pressed() {
      newKeyF9Pressed();
    }*/
  };
  JLabel jlInvBr = new JLabel();
  JlrNavField jrfNAZORG = new JlrNavField();
  JlrNavField jrfNAZOBJEKT = new JlrNavField();
  JlrNavField jrfLokacija = new JlrNavField(){
//     public void keyF9Pressed() {
//      newKeyF9Pressed();
//    }
  };
  JLabel jlKonto = new JLabel();
  JlrNavField jrfKonto = new JlrNavField();
  JraTextField jtfNazInvBr = new JraTextField();
  JLabel jlSifra = new JLabel();
  JLabel jlLokacija = new JLabel();
  JraButton jbLokacija = new JraButton();
  JraTextField jtfInvBr = new JraTextField();
  JLabel jlNaziv = new JLabel();
  JraButton jbCORG = new JraButton();
  JlrNavField jrfNazLokacija = new JlrNavField();
  JraButton jbCOBJEKT = new JraButton();
  JLabel jLabel8 = new JLabel();
  JLabel jLabel9 = new JLabel();
  JraButton jbKonto = new JraButton();
  private JLabel jLabel1 = new JLabel();
  private JraTextField jtfDatum = new JraTextField();

  public frmPripremaToAktiv() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void componentShow() {
    rcc.setLabelLaF(jtfInvBr, false);
    rcc.setLabelLaF(jtfNazInvBr, false);
    dm.getOS_Sredstvo().setTimestamp("DATAKTIVIRANJA",vl.getToday());
    jrfCORG.setText("");
    jrfCORG.forceFocLost();
    jrfCOBJEKT.setText("");
    jrfCOBJEKT.forceFocLost();
    jrfLokacija.setText("");
    jrfLokacija.forceFocLost();
    jrfKonto.setText("");
    jrfKonto.forceFocLost();
    jrfCORG.requestFocus();
  }
  public void okPress() {
    java.sql.Timestamp dan = dm.getOS_Sredstvo().getTimestamp("DATAKTIVIRANJA");

    dm.getOS_Vrpromjene().open();

    dm.getOS_Sredstvo().setString("AKTIV", "N");
    dm.getOS_Sredstvo().setTimestamp("DATAKTIVIRANJA", null);
    dm.getOS_Sredstvo().post();

    DataRow copy = new DataRow(dm.getOS_Sredstvo());
    dm.getOS_Sredstvo().getDataRow(copy);
    long nr = dm.getOS_Sredstvo().addRowReturnInternalRow(copy);
    dm.getOS_Sredstvo().goToInternalRow(nr);

    dm.getOS_Sredstvo().setString("AKTIV", "D");
    dm.getOS_Sredstvo().setString("CORG2", jrfCORG.getText());
    dm.getOS_Sredstvo().setString("COBJEKT", jrfCOBJEKT.getText());
    dm.getOS_Sredstvo().setString("CLOKACIJE", jrfLokacija.getText());
    dm.getOS_Sredstvo().setString("BROJKONTA", jrfKonto.getText());
    dm.getOS_Sredstvo().setString("STATUS", "A");
    dm.getOS_Sredstvo().setTimestamp("DATAKTIVIRANJA", dan);
    dm.getOS_Sredstvo().setString("STARISTATUS", "P");
    dm.getOS_Sredstvo().saveChanges();


    dm.getOS_Promjene().insertRow(false);
    dm.getOS_Promjene().setString("INVBROJ", dm.getOS_Sredstvo().getString("INVBROJ"));
    dm.getOS_Promjene().setString("CORG", dm.getOS_Sredstvo().getString("CORG"));
    dm.getOS_Promjene().setString("CORG2", dm.getOS_Sredstvo().getString("CORG2"));
    dm.getOS_Promjene().setString("STATUS", dm.getOS_Sredstvo().getString("STATUS"));
    dm.getOS_Promjene().setTimestamp("DATPROMJENE", dm.getOS_Sredstvo().getTimestamp("DATAKTIVIRANJA"));
    dm.getOS_Promjene().setBigDecimal("OSNDUGUJE", dm.getOS_Sredstvo().getBigDecimal("OSNDUGUJE"));
    dm.getOS_Promjene().setBigDecimal("ISPPOTRAZUJE", dm.getOS_Sredstvo().getBigDecimal("ISPPOTRAZUJE"));
    dm.getOS_Promjene().setString("CPROMJENE", osUtil.getUtil().getSifraPriprema());
    dm.getOS_Promjene().setInt("RBR", 1);
    dm.getOS_Promjene().setBigDecimal("SALDO", util.negateValue(util.negateValue(dm.getOS_Promjene().getBigDecimal("OSNDUGUJE"), dm.getOS_Promjene().getBigDecimal("OSNPOTRAZUJE")), util.negateValue(dm.getOS_Promjene().getBigDecimal("ISPPOTRAZUJE"), dm.getOS_Promjene().getBigDecimal("ISPDUGUJE"))));

    System.out.println("DATAKTIVIRANJA: "+dm.getOS_Sredstvo().getTimestamp("DATAKTIVIRANJA"));
    dm.getOS_Promjene().saveChanges();
    dm.getOS_Sredstvo().refresh();
    this.hide();

    /**@todo Implement this hr.restart.util.raUpitLite abstract method*/
  }
  public void cancelPress() {
    dm.getOS_Sredstvo().cancel();
    super.cancelPress();
  }
  public void firstESC() {
    /**@todo Implement this hr.restart.util.raUpitLite abstract method*/
  }
  public boolean isIspis() {
    return false;
  }
  public boolean Validacija() {
    if (vl.isEmpty(jrfCORG)) {
      return false;
    }
    if (vl.isEmpty(jrfCOBJEKT)) {
      return false;
    }
    if (vl.isEmpty(jrfLokacija)) {
      return false;
    }
    if (vl.isEmpty(jrfKonto)) {
      return false;
    }
    if(!kontrolaDatUnosa()) {
      JOptionPane.showConfirmDialog(jp,"Datum mora biti veæi ili jednak datumu na zadnjoj stavci",
                                    "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      this.jtfDatum.requestFocus();
      return false;
    }
    return true;
  }
  public boolean runFirstESC() {
    return false;
    /**@todo Implement this hr.restart.util.raUpitLite abstract method*/
//    throw new java.lang.UnsupportedOperationException("Method runFirstESC() not yet implemented.");
  }
  private void jbInit() throws Exception {
    this.setJPan(jp);

    jp.setLayout(xYLayout1);
    xYLayout1.setWidth(640);
    xYLayout1.setHeight(190);
    jrfCOBJEKT.setNavButton(jbCOBJEKT);
    jrfCOBJEKT.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jrfCOBJEKT_focusGained(e);
      }
    });
    jrfCORG.setNavButton(jbCORG);
    jlInvBr.setText("Inventarski broj");
    jrfLokacija.setNavButton(jbLokacija);
    jrfLokacija.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jrfLokacija_focusGained(e);
      }
    });
    jlKonto.setText("Konto");
    jrfKonto.setNavButton(jbKonto);
    jlSifra.setText("Šifra");
    jlLokacija.setText("Lokacija");
    jbLokacija.setText("jButton1");
    jlNaziv.setText("Naziv");
    jLabel1.setText("Datum aktiviranja");
    jbCORG.setText("jButton1");
    jrfNazLokacija.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jrfNazLokacija_focusGained(e);
      }
    });
    jbCOBJEKT.setText("jButton1");
    jLabel8.setText("Objekt");
    jLabel9.setText("Org. jedinica");
    jbKonto.setText("jButton1");

    jtfInvBr.setDataSet(dm.getOS_Sredstvo());
    jtfInvBr.setColumnName("INVBROJ");
    jtfNazInvBr.setDataSet(dm.getOS_Sredstvo());
    jtfNazInvBr.setColumnName("NAZSREDSTVA");
    jtfDatum.setDataSet(dm.getOS_Sredstvo());
    jtfDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDatum.setColumnName("DATAKTIVIRANJA");

    //org. jedinica na masteru
    jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jrfCORG.setVisCols(new int[]{0,1});
    jrfCORG.setColNames(new String[] {"NAZIV"});
    jrfCORG.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZORG});
//    jrfCORG.setDataSet(getMasterSet());
    jrfCORG.setColumnName("CORG");
    jrfCORG.setNavColumnName("CORG");
    jrfNAZORG.setColumnName("NAZIV");
    jrfNAZORG.setNavProperties(jrfCORG);
    jrfNAZORG.setSearchMode(1);

    //Objekti
    jrfCOBJEKT.setRaDataSet(dm.getOS_Objekt());
    jrfCOBJEKT.setVisCols(new int[]{3,4});
    jrfCOBJEKT.setColNames(new String[] {"NAZOBJEKT"});
    jrfCOBJEKT.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZOBJEKT});
//    jrfCOBJEKT.setDataSet(getMasterSet());
    jrfCOBJEKT.setColumnName("COBJEKT");
    jrfNAZOBJEKT.setColumnName("NAZOBJEKT");
    jrfNAZOBJEKT.setNavProperties(jrfCOBJEKT);
    jrfNAZOBJEKT.setSearchMode(1);
    jrfNAZOBJEKT.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jrfNAZOBJEKT_focusGained(e);
      }
    });

    //lokacija

    jrfLokacija.setRaDataSet(dm.getOS_Lokacije());
    jrfLokacija.setVisCols(new int[]{4,5});
    jrfLokacija.setColNames(new String[] {"NAZLOKACIJE"});
    jrfLokacija.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazLokacija});
//    jrfLokacija.setDataSet(getMasterSet());
    jrfLokacija.setColumnName("CLOKACIJE");
    jrfNazLokacija.setColumnName("NAZLOKACIJE");
    jrfNazLokacija.setNavProperties(jrfLokacija);
    jrfNazLokacija.setSearchMode(1);

    //konta
    jrfKonto.setRaDataSet(dm.getKontaAnalitic());
    jrfKonto.setVisCols(new int[]{0,1});
    jrfKonto.setColNames(new String[] {"NAZIVKONTA"});
    jrfKonto.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazKonto});
//    jrfKonto.setDataSet(getMasterSet());
    jrfKonto.setSearchMode(3);
    jrfKonto.setColumnName("BROJKONTA");
    jrfNazKonto.setColumnName("NAZIVKONTA");
    jrfNazKonto.setNavProperties(jrfKonto);
    jrfNazKonto.setSearchMode(1);


    jp.add(jlInvBr, new XYConstraints(15, 25, -1, -1));
    jp.add(jrfCOBJEKT, new XYConstraints(150, 75, 100, -1));
    jp.add(jrfCORG, new XYConstraints(151, 50, 100, -1));
    jp.add(jrfNAZORG, new XYConstraints(256, 50, 350, -1));
    jp.add(jrfNAZOBJEKT, new XYConstraints(255, 75, 350, -1));
    jp.add(jrfLokacija, new XYConstraints(150, 100, 100, -1));
    jp.add(jtfNazInvBr, new XYConstraints(255, 25, 350, -1));
    jp.add(jlSifra,  new XYConstraints(150, 8, -1, -1));
    jp.add(jlLokacija, new XYConstraints(15, 100, -1, -1));
    jp.add(jbLokacija, new XYConstraints(610, 100, 21, 21));
    jp.add(jtfInvBr, new XYConstraints(150, 25, 100, -1));
    jp.add(jlNaziv,  new XYConstraints(255, 8, -1, -1));
    jp.add(jbCORG, new XYConstraints(611, 50, 21, 21));
    jp.add(jrfNazLokacija, new XYConstraints(255, 100, 350, -1));
    jp.add(jbCOBJEKT, new XYConstraints(610, 75, 21, 21));
    jp.add(jLabel8, new XYConstraints(15, 75, -1, -1));
    jp.add(jLabel9, new XYConstraints(16, 50, -1, -1));
    jp.add(jlKonto, new XYConstraints(15, 125, -1, -1));
    jp.add(jrfKonto, new XYConstraints(150, 125, 100, -1));
    jp.add(jrfNazKonto, new XYConstraints(255, 125, 350, -1));
    jp.add(jbKonto, new XYConstraints(610, 125, 21, 21));
    jp.add(jLabel1,   new XYConstraints(15, 150, -1, -1));
    jp.add(jtfDatum,    new XYConstraints(150, 150, 100, -1));
  }

  void jrfCOBJEKT_focusGained(FocusEvent e) {
    qdsObjekti=getObjektiDS(jrfCORG.getText());
    jrfCOBJEKT.setRaDataSet(qdsObjekti);
  }

  void jrfNAZOBJEKT_focusGained(FocusEvent e) {
    qdsObjekti=getObjektiDS(jrfCORG.getText());
    jrfCOBJEKT.setRaDataSet(qdsObjekti);
  }

  void jrfLokacija_focusGained(FocusEvent e) {
    qdsLokacije=getLokacijeDS(jrfCORG.getText(), jrfCOBJEKT.getText());
    jrfLokacija.setRaDataSet(qdsLokacije);
  }

  void jrfNazLokacija_focusGained(FocusEvent e) {
    qdsLokacije=getLokacijeDS(jrfCORG.getText(), jrfCOBJEKT.getText());
    jrfLokacija.setRaDataSet(qdsLokacije);
  }
  public com.borland.dx.sql.dataset.QueryDataSet getLokacijeDS(String corg, String obj) {
    System.out.println("getLokacije");
    com.borland.dx.sql.dataset.QueryDataSet aa= hr.restart.util.Util.getNewQueryDataSet("select * from os_lokacije where corg='"+corg+"' and cobjekt='"+obj+"'");
    st.prn(aa);
    return aa;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getObjektiDS(String corg) {
    System.out.println("getObjekti");
    com.borland.dx.sql.dataset.QueryDataSet aa= hr.restart.util.Util.getNewQueryDataSet("select * from os_objekt where corg='"+corg+"'");
    st.prn(aa);
    return aa;
  }
  private boolean kontrolaDatUnosa() {
    java.sql.Timestamp DSdat;
    com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
    for (int i=0; i<dm.getOS_Promjene().rowCount();i++) {
      dm.getOS_Promjene().getVariant("DATPROMJENE",i,v);
      DSdat = hr.restart.util.Util.getUtil().clearTime(v.getTimestamp());
      if (DSdat.after(dm.getOS_Sredstvo().getTimestamp("DATAKTIVIRANJA"))) {
        System.out.println("");
        return false;
      }
    }
    return true;
  }
}