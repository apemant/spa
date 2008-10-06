/****license*****************************************************************
**   file: frmSpecKart.java
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

import hr.restart.sisfun.raUser;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.swing.raDateRange;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raUpitLite;

import java.awt.Color;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmSpecKart extends raUpitLite {
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

  JPanel jp = new JPanel();

  static String param;

  static double suma;

  static Timestamp datOd, datDo;

  static QueryDataSet qds = new QueryDataSet();

  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

  static frmSpecKart fSK;

  Valid vl = Valid.getValid();

  String spec;

  hr.restart.robno.Util utRobno = hr.restart.robno.Util.getUtil();

  public static frmSpecKart getInstance() {
    if (fSK == null)
      fSK = new frmSpecKart();
    return fSK;
  }

  private XYLayout xYLayout1 = new XYLayout();

  private JPanel jpCB = new JPanel();

  private XYLayout xYLayout2 = new XYLayout();

  private JRadioButton jrbDatPrim = new JRadioButton();

  private JRadioButton jrbDatNap = new JRadioButton();

  raButtonGroup rbGroup = new raButtonGroup();

  private JraTextField jraDatumOd = new JraTextField();

  private JraTextField jraDatumDo = new JraTextField();

  private JLabel jlMinus = new JLabel();

  private JLabel jlDatum = new JLabel();

  private Border border1;

  private TitledBorder titledBorder1;

  TableDataSet tds = new TableDataSet();

  private JlrNavField jlfCBanka = new JlrNavField() {
    public void after_lookUp() {
      if (!jlfCBanka.getText().equals("")) {
        jraDatumOd.requestFocus();
        EnDisCBAN(false);
      }
    }
  };

  private JlrNavField jlfNazBanke = new JlrNavField() {
    public void after_lookUp() {
      if (!jlfCBanka.getText().equals("")) {
        jraDatumOd.requestFocus();
        EnDisCBAN(false);
      }
    }
  };

  private JraButton jbCBanka = new JraButton();

  JLabel jlBanka = new JLabel();

  TitledBorder titledBorder2;

  private JlrNavField jrfCSKL = new JlrNavField() {
    public void after_lookUp() {
      if (!jrfCSKL.getText().equals("")) {
        EnDisCSKL(false);
        jlfCBanka.requestFocus();
      }
    }
  };

  private JlrNavField jrfNAZSKL = new JlrNavField() {
    public void after_lookUp() {
      if (!jrfCSKL.getText().equals("")) {
        EnDisCSKL(false);
        jlfCBanka.requestFocus();
      }
    }
  };

  private JraButton jbCSKL = new JraButton();

  private JLabel jlSklad = new JLabel();



  private JlrNavField jrfCORG = new JlrNavField() {
    public void after_lookUp() {
      if (!jrfCORG.getText().equals("")) {
        EnDisCORG(false);
        jrfCSKL.requestFocus();
      }
    }
  };

  private JlrNavField jrfNAZORG = new JlrNavField() {
    public void after_lookUp() {
      if (!jrfCORG.getText().equals("")) {
        EnDisCORG(false);
        jrfCSKL.requestFocus();
      }
    }
  };

  private JraButton jbCORG = new JraButton();

  private JLabel jlOrganizacija = new JLabel("Org. jedinica");

  
  
//  private JLabel jlSifra = new JLabel();
//
//  private JLabel jlNaziv = new JLabel();

  private JLabel jlSpecifikacija = new JLabel();

  public frmSpecKart() {
    fSK = this;
    try {
      jbInit();
    } catch (Exception ex) {}
  }

  public boolean isIspis() {
    return false;
  }

  public boolean ispisNow() {
    return !nemaPodataka;
  }

  private void jbInit() throws Exception {
    this.addReport("hr.restart.robno.repSpecKart", "Specifikacija kartica", 5);
    jrbDatNap.setHorizontalTextPosition(SwingUtilities.LEADING);
    border1 = BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140));
    titledBorder2 = new TitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.white, new Color(224, 255, 255), new Color(76, 90, 98), new Color(109, 129, 140)), "");
    titledBorder1 = new TitledBorder(border1, "Specifikacija");
    //    this.setModal(true);
    xYLayout1.setWidth(555);
    xYLayout1.setHeight(155);
    jp.setLayout(xYLayout1);
    jpCB.setLayout(xYLayout2);
    xYLayout2.setWidth(400);
    xYLayout2.setHeight(50);
    rbGroup.add(jrbDatNap);
    rbGroup.add(jrbDatPrim);
    jrbDatPrim.setHorizontalTextPosition(SwingConstants.TRAILING);
    jrbDatPrim.setText("Po datumu primitka");
    jrbDatNap.setHorizontalTextPosition(SwingConstants.LEADING);
    jrbDatNap.setText("Po datumu naplate");

    tds.setColumns(new Column[]{dm.createTimestampColumn("pocDatum"), dm.createTimestampColumn("zavDatum"), dm.createStringColumn("CBANKA", 4), dm.createStringColumn("CSKL", 12), dm.createStringColumn("CORG", 12)});
    jraDatumOd.setColumnName("pocDatum");
    jraDatumOd.setDataSet(tds);
    jraDatumOd.setText("jraTextField1");
    jraDatumOd.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumDo.setColumnName("zavDatum");
    jraDatumDo.setDataSet(tds);
    jraDatumDo.setText("jraTextField2");
    jraDatumDo.setHorizontalAlignment(SwingConstants.CENTER);
    new raDateRange(jraDatumOd, jraDatumDo);

    jlfCBanka.setColumnName("CBANKA");
    jlfCBanka.setColumnName("CBANKA");
    jlfCBanka.setDataSet(tds);
    jlfCBanka.setColNames(new String[]{"NAZIV"});
    jlfCBanka.setTextFields(new JTextComponent[]{jlfNazBanke});
    jlfCBanka.setVisCols(new int[]{0, 1});
    jlfCBanka.setSearchMode(0);
    jlfCBanka.setRaDataSet(dm.getKartice());
    jlfCBanka.setNavButton(jbCBanka);
    jlfNazBanke.setColumnName("NAZIV");
    jlfNazBanke.setNavProperties(jlfCBanka);
    jlfNazBanke.setSearchMode(1);

    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setDataSet(tds);
    jrfCSKL.setColNames(new String[]{"NAZSKL"});
    jrfCSKL.setTextFields(new JTextComponent[]{jrfNAZSKL});
    jrfCSKL.setVisCols(new int[]{0, 1});
    jrfCSKL.setSearchMode(0);
    jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jrfCSKL.setNavButton(jbCSKL);
    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jrfNAZSKL.setSearchMode(1);

    jrfCORG.setColumnName("CORG");
    jrfCORG.setDataSet(tds);
    jrfCORG.setColNames(new String[]{"NAZIV"});
    jrfCORG.setTextFields(new JTextComponent[]{jrfNAZORG});
    jrfCORG.setVisCols(new int[]{0, 1});
    jrfCORG.setSearchMode(0);
    jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jrfCORG.setNavButton(jbCORG);
    jrfNAZORG.setColumnName("NAZIV");
    jrfNAZORG.setNavProperties(jrfCORG);
    jrfNAZORG.setSearchMode(1);

    jlMinus.setText("-");
    jlDatum.setText("Datum (od - do)");
    jlBanka.setText("Banka");
    jpCB.setBorder(titledBorder2);
    jlSklad.setText("Skladište");
//    jlSifra.setText("Šifra");
//    jlNaziv.setText("Naziv");
    jlSpecifikacija.setText("Specifikacija");
    
    jpCB.add(jrbDatPrim, new XYConstraints(15, 0, -1, -1));
    jpCB.add(jrbDatNap, new XYConstraints(210, 0, -1, -1));
    
//    jp.add(jlNaziv, new XYConstraints(255, 7, -1, -1));
//    jp.add(jlSifra, new XYConstraints(150, 7, -1, -1));
    
    jp.add(jlOrganizacija, new XYConstraints(15, 15, -1, -1));
    jp.add(jrfCORG, new XYConstraints(150, 15, 100, -1));
    jp.add(jrfNAZORG, new XYConstraints(255, 15, 260, -1));
    jp.add(jbCORG, new XYConstraints(519, 15, 21, 21));
    
    jp.add(jlSklad, new XYConstraints(15, 40, -1, -1));
    jp.add(jrfCSKL, new XYConstraints(150, 40, 100, -1));
    jp.add(jrfNAZSKL, new XYConstraints(255, 40, 260, -1));
    jp.add(jbCSKL, new XYConstraints(519, 40, 21, 21));
    
    jp.add(jlfCBanka, new XYConstraints(150, 65, 100, -1));
    jp.add(jlfNazBanke, new XYConstraints(255, 65, 260, -1));
    jp.add(jbCBanka, new XYConstraints(519, 65, 21, 21));
    jp.add(jlBanka, new XYConstraints(15, 65, -1, -1));
    
    jp.add(jraDatumOd, new XYConstraints(150, 90, 100, -1));
    jp.add(jraDatumDo, new XYConstraints(255, 90, 100, -1));
    jp.add(jlDatum, new XYConstraints(15, 90, -1, -1));  // was 75
    
    jp.add(jpCB, new XYConstraints(150, 117, 365, 37)); // was 102
    
    jp.add(jlSpecifikacija, new XYConstraints(15, 127, -1, -1)); // was 112
    
    this.setJPan(jp);
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldKnj, String newKnj) {
        jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
        jrfCORG.setDataSet(tds);
        jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
        jrfCSKL.setDataSet(tds);
      }
    });
  }

  public void componentShow() {
    //*** obrisati kad Srky sredi stvar
    if (!dm.getSklad().isOpen())
      dm.getSklad().open();
    jrfCSKL.setRaDataSet(dm.getSklad());
    showDefaultValues();
    tds.setTimestamp("pocDatum", hr.restart.util.Valid.getValid().findDate(false, 0));
    tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false, 0));
  }

  public boolean runFirstESC() {
    if (!jrfCORG.getText().equals(""))
      return true;
    return false;
  }

  public void firstESC() {
    EnDisRest(true);
    if (!jlfCBanka.getText().equals("")) {
      EnDisCBAN(true);
      jlfCBanka.setText("");
      jlfCBanka.forceFocLost();
      jlfCBanka.requestFocus();
    } else if (!jrfCSKL.getText().equals("")) {
      EnDisCSKL(true);
      jrfCSKL.setText("");
      jrfCSKL.forceFocLost();
      jrfCSKL.requestFocus();
    } else if (!jrfCORG.getText().equals("")) {
      EnDisCORG(true);
      jrfCORG.setText("");
      jrfCORG.forceFocLost();
      jrfCORG.requestFocus();
    }
  }

  public boolean Validacija() {
    if (vl.isEmpty(jrfCORG) || vl.isEmpty(jrfCSKL) || !hr.restart.util.Aus.checkDateRange(jraDatumOd, jraDatumDo))
      return false;
    //    if(preparePrint()==0){
    //      showDefaultValues();
    //      JOptionPane.showConfirmDialog(this.jp,"Nema podataka koji zadovoljavaju
    // traženi uvjet
    // !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
    //      return false;
    //    }
    //    this.getRepRunner().clearAllReports();
    //    this.addReport("hr.restart.robno.repSpecKart","Specifikacija kartica",5);
    return true;
  }

  boolean nemaPodataka;

  public void okPress() {
    nemaPodataka = false;
    if (preparePrint() == 0) {
      nemaPodataka = true;
      return;
    }
  }

  public void afterOKPress() {
    if (nemaPodataka) {
      showDefaultValues();
      JOptionPane.showConfirmDialog(this.jp, "Nema podataka koji zadovoljavaju traženi uvjet !", "Upozorenje", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
    } else {
      this.requestFocus();
    }
  }

  private int preparePrint() {
    suma = 0;
    qds.close();
    if (jrbDatPrim.isSelected())
      spec = "prim";
    else
      spec = "nap";
    param = spec;
    datOd = tds.getTimestamp("pocDatum");
    datDo = tds.getTimestamp("zavDatum");
    // TODO marker
    String qStr = rdUtil.getUtil().getSpecKart(tds.getString("CORG"), tds.getString("CSKL"), utRobno.getTimestampValue(tds.getTimestamp("pocDatum"), 0), utRobno.getTimestampValue(tds.getTimestamp("zavDatum"), 1), tds.getString("CBANKA"), spec);

    //    System.out.println("qStr : " + qStr);

    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    qds.open();
    qds.first();

    while (qds.inBounds()) {
      suma += qds.getBigDecimal("IZNOS").doubleValue();
      qds.next();
    }

    return qds.getRowCount();
  }

  public static QueryDataSet getQDS() {
    return qds;
  }

  private void deselectDate() {
    jraDatumOd.select(0, 0);
    jraDatumDo.select(0, 0);
  }

  public void showDefaultValues() {
    jlfCBanka.setText("");
    jlfCBanka.forceFocLost();
    EnDisCBAN(true);
    jrbDatPrim.setSelected(true);
    jrfCSKL.requestFocus();
    boolean cskl_corg = rdUtil.getUtil().getCSKL_CORG(raUser.getInstance().getDefSklad(), hr.restart.zapod.OrgStr.getKNJCORG());

    jrfCORG.setText(hr.restart.zapod.OrgStr.getKNJCORG());
    jrfCORG.forceFocLost();

    if (cskl_corg) {
      jrfCSKL.setText(raUser.getInstance().getDefSklad());
      jrfCSKL.forceFocLost();
    } else if (!cskl_corg)
      jrfCSKL.requestFocus();
  }

  //  void jrbDatPrim_actionPerformed(ActionEvent e) {
  //    if(jrbDatPrim.isSelected())
  //      jrbDatNap.setSelected(false);
  //    else
  //      jrbDatPrim.setSelected(true);
  //  }
  //
  //  void jrbDatNap_actionPerformed(ActionEvent e) {
  //    if(jrbDatNap.isSelected())
  //      jrbDatPrim.setSelected(false);
  //    else
  //      jrbDatNap.setSelected(true);
  //  }

  private Timestamp setFirstDay(Timestamp today) {
    String strToday = today.toString();
    String date = strToday.substring(0, 8) + "01" + strToday.substring(10, strToday.length());
    return Timestamp.valueOf(date);
  }

  private void EnDisCSKL(boolean enable) {
    rcc.setLabelLaF(jrfCSKL, enable);
    rcc.setLabelLaF(jrfNAZSKL, enable);
    rcc.setLabelLaF(jbCSKL, enable);
  }

  private void EnDisCORG(boolean enable) {
    rcc.setLabelLaF(jrfCORG, enable);
    rcc.setLabelLaF(jrfNAZORG, enable);
    rcc.setLabelLaF(jbCORG, enable);
  }

  private void EnDisCBAN(boolean enable) {
    rcc.setLabelLaF(jlfCBanka, enable);
    rcc.setLabelLaF(jlfNazBanke, enable);
    rcc.setLabelLaF(jbCBanka, enable);
  }
  
  private void EnDisRest(boolean enabled){
    rcc.EnabDisabAll(jpCB, enabled);
    rcc.setLabelLaF(jlfCBanka, enabled);
    rcc.setLabelLaF(jlfNazBanke, enabled);
    rcc.setLabelLaF(jbCBanka, enabled);
    rcc.setLabelLaF(jraDatumOd, enabled);
    rcc.setLabelLaF(jraDatumDo, enabled);
  }
}