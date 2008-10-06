/****license*****************************************************************
**   file: ispAmor.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.raIspisDialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ispAmor extends raIspisDialog{

  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.zapod.OrgStr knjOrgStr = hr.restart.zapod.OrgStr.getOrgStr();

  JPanel jp = new JPanel();
  JPanel jPanel2 = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  XYLayout xYLayout2 = new XYLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  JRadioButton jrbIspKont = new JRadioButton();
  JRadioButton jrbIspAmGr = new JRadioButton();
  JRadioButton jrbLokacije = new JRadioButton();
  JRadioButton jrbIspRevSk = new JRadioButton();
  JRadioButton jrbIspAtrikl = new JRadioButton();
  JraCheckBox jcbInvBr = new JraCheckBox();
  Column column1 = new Column();
  Column temp = new Column();

  TableDataSet tds = new TableDataSet();
  Timestamp oldValue;

  StorageDataSet fake = new StorageDataSet();
  QueryDataSet defQDS = new QueryDataSet();
  public static boolean poPripCorg = false;

  static int selectedRB = 0;
  public static double [] sume;
  public static String paramCOrg="";
  public static String datumOd="";
  public static String datumDo="";

  private int OJ = 0;
  private int OL = 0;
  private int IB = 0;
  private int PO = 0;

  public static int getSelectedRB()
  {
    return selectedRB;
  }

  public ispAmor() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    this.setModal(true);
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Vrsta Liste");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Oblik ispisa");
    border1 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    border2 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder3 = new TitledBorder(border2,"Org. jedinice");
    jp.setLayout(xYLayout1);
    xYLayout1.setWidth(480);
    xYLayout1.setHeight(430);
    this.setJPan(jp);
    jPanel2.setBorder(titledBorder2);
    jPanel2.setLayout(xYLayout2);
    jrbIspKont.setText("Ispis po kontima");
    jrbIspKont.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbIspKont_actionPerformed(e);
      }
    });
    jrbIspAmGr.setText("Ispis po amortizacijskim grupama");
    jrbIspAmGr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbIspAmGr_actionPerformed(e);
      }
    });
    jrbLokacije.setText("Ispis po lokacijama");
    jrbLokacije.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbLokacije_actionPerformed(e);
      }
    });
    jrbIspRevSk.setText("Ispis po revalorizacijskim skupinama");
    jrbIspRevSk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbIspRevSk_actionPerformed(e);
      }
    });
    jrbIspAtrikl.setText("Ispis po artiklima");
    jrbIspAtrikl.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbIspAtrikl_actionPerformed(e);
      }
    });
    jcbInvBr.setText("Ispis s inventarskim brojem");
    jcbInvBr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbInvBr_actionPerformed(e);
      }
    });
    jp.setMinimumSize(new Dimension(478, 430));
    jp.setPreferredSize(new Dimension(478, 430));
    jp.setToolTipText("");
    jcbOrgJed.setText("Ispis po org. jedinici");
    jcbOrgJed.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbOrgJed_actionPerformed(e);
      }
    });
    jcbOblikListe.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbOblikListe.setText("Oblik ispisa");
    jcbOblikListe.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbOblikListe_actionPerformed(e);
      }
    });
    jPanel1.setBorder(BorderFactory.createEtchedBorder());
    jPanel1.setLayout(xYLayout3);
    jLabel1.setText("Vrsta ispisa");
    jrbAmortizacija.setText("Amortizacija");
    jrbAmortizacija.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbAmortizacija_actionPerformed(e);
      }
    });
    jrbLikvidacija.setText("Likvidirana sredstva");
    jrbLikvidacija.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbLikvidacija_actionPerformed(e);
      }
    });
    jcbPripOrgJed.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbPripOrgJed_actionPerformed(e);
      }
    });
    jcbPripOrgJed.setText("Ispis pripadajuæih org. jedinica");
    jPanel3.setLayout(xYLayout4);
    jPanel3.setBorder(titledBorder3);
    jbCOrg.setText("...");

//    jrfCOrg.setDataSet(fake);
//    jrfCOrg.setVisCols(new int[]{0,1});
//    jrfCOrg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
//    jrfCOrg.setColNames(new String[] {"NAZIV"});
//    jrfCOrg.setTextFields(new javax.swing.text.JTextComponent[] {jrfCOrgNaz});
//    jrfCOrg.setColumnName("CORG");
//    jrfCOrg.setSearchMode(0);
//    jrfCOrg.setNavButton(this.jbCOrg);
//    jrfCOrgNaz.setColumnName("NAZIV");
//    jrfCOrgNaz.setNavProperties(jrfCOrg);
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
      new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnjig, String newKnjig)
      {
        bindCorg();
      };
    }
    );
    bindCorg();
    jp.add(jPanel2,               new XYConstraints(15, 208, 450, 162));
    jPanel2.add(jrbIspKont, new XYConstraints(15, 5, -1, -1));
    jPanel2.add(jrbIspAmGr, new XYConstraints(15, 30, -1, -1));
    jPanel2.add(jrbLokacije, new XYConstraints(15, 55, -1, -1));
    jPanel2.add(jrbIspAtrikl, new XYConstraints(15, 105, -1, -1));
    jPanel2.add(jrbIspRevSk, new XYConstraints(15, 80, -1, -1));
    jp.add(jcbInvBr,         new XYConstraints(36, 375, -1, -1));
    jp.add(jcbOrgJed,         new XYConstraints(36, 177, -1, -1));
    jp.add(jcbOblikListe,         new XYConstraints(357, 177, -1, -1));
    jp.add(jLabel1,      new XYConstraints(22, 138, -1, -1));
    jp.add(jPanel1,       new XYConstraints(150, 138, 315, 30));
    jPanel1.add(jrbAmortizacija,      new XYConstraints(15, 0, -1, -1));
    jPanel1.add(jrbLikvidacija,   new XYConstraints(166, 0, -1, -1));
    jp.add(jcbPripOrgJed,       new XYConstraints(36, 100, -1, -1));
    jp.add(jPanel3,   new XYConstraints(15, 24, 450, 70));
    jPanel3.add(jrfCOrg,  new XYConstraints(15, 7, 100, -1));
    jPanel3.add(jbCOrg,  new XYConstraints(401, 7, 21, 21));
    jPanel3.add(jrfCOrgNaz,  new XYConstraints(120, 7, 275, -1));



    column1.setCaption("datum");
    column1.setColumnName("datum");
    column1.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    column1.setDisplayMask("dd-MM-yyyy");
    column1.setEditMask("dd-MM-yyyy");
    column1.setResolvable(false);
    column1.setSqlType(0);
    column1.setServerColumnName("NewColumn1");

    tds.setColumns(new Column[]{dM.createTimestampColumn("datum")});
    temp.setColumnName("CORG");
    temp.setDataType(com.borland.dx.dataset.Variant.STRING);
    fake.setColumns(new Column[] {temp});

    //**** binding


    setRadioButton(0);
    setRadioButtonVrIsp(0);
    rcc.EnabDisabAll(jPanel2, false);

  }

  public void componentShow()
  {
    oldValue=null;
    showDefaultValues();
  }

  public void showDefaultValues()
  {

    if(!tds.isOpen())
      tds.open();
    if(oldValue==null)
      tds.setTimestamp("datum", hr.restart.util.Valid.getValid().findDate(false, 0));
    reset();
    jrfCOrg.setText(hr.restart.zapod.OrgStr.getKNJCORG());
    jrfCOrg.forceFocLost();
    jrfCOrg.requestFocus();



    if(!defQDS.isOpen())
    {
      getDefValues();
      defQDS.open();
    }
  }

  public boolean runFirstESC()
  {
    paramCOrg="";
    if(!jrfCOrg.getText().trim().equals("") )
    {

      return true;
    }
    return false;
  }

  public void firstESC()
  {
    reset();
    jrfCOrgNaz.setText("");
    jrfCOrg.setText("");
    jrfCOrg.forceFocLost();
    fake.setString("CORG","");
    jrfCOrg.requestFocus();
  }

  public boolean okPress()
  {
    int rows = 0;
    if(jrfCOrg.getText().equals(""))
    {
      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos org. jedinice !","Greška !",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      jrfCOrg.requestFocus();
      return false;
    }
    if(jrbAmortizacija.isSelected())
    {
      if (jrfCOrg.getText().trim().equals("0") || jrfCOrg.getText().trim().equals(""))
      {
        rows = prepareIspis(0);
      }
      else
        rows = prepareIspis(1);
    }
    else
    {
      rows = prepareIspisLikvidacija();
    }

     if (rows == 0)
      {
        EnabDisabOrg(0);
        JOptionPane.showConfirmDialog(this.jp,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
        return false;
      }
      if(OJ+OL+IB == 0)
     {
       EnabDisabOrg(0);
       jcbOrgJed.requestFocus();
       JOptionPane.showConfirmDialog(this.jp,"Obavezno odabrati vrstu ispisa !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
       return false;
    }

    getRepRunner().clearAllReports();

    if(jrbAmortizacija.isSelected())
    {
      switch (OJ+OL+IB) {
        case 0:
          this.addReport("hr.restart.os.repIspisAmor_0","Ispis osnovnih sredstava", 5);
          break;
        case 1:
          this.addReport("hr.restart.os.repIspisAmor_1","Ispis osnovnih sredstava", 5);
          break;
        case 2:
          this.addReport("hr.restart.os.repIspisAmor_2","Ispis osnovnih sredstava", 5);
          break;
        case 3:
          this.addReport("hr.restart.os.repIspisAmor_3","Ispis osnovnih sredstava", 5);
          break;
        case 4:
          this.addReport("hr.restart.os.repIspisAmor_4","Ispis osnovnih sredstava", 5);
          break;
        case 5:
          this.addReport("hr.restart.os.repIspisAmor_5","Ispis osnovnih sredstava", 5);
          break;
        case 6:
          this.addReport("hr.restart.os.repIspisAmor_6","Ispis osnovnih sredstava", 5);
          break;
        case 7:
          this.addReport("hr.restart.os.repIspisAmor_7","Ispis osnovnih sredstava", 5);
          break;
      }
    }
    else
    {
      if(jcbInvBr.isSelected())
        this.addReport("hr.restart.os.repAmRev", "Likvidacija osnovnih sredstava", 5);
      else
        this.addReport("hr.restart.os.repAmRevTot", "Likvidacija osnovnih sredstava", 5);
    }
    return true;
  }

  void jrbIspKont_actionPerformed(ActionEvent e) {
    setRadioButton(0);
  }

  void jrbIspAmGr_actionPerformed(ActionEvent e) {
    setRadioButton(1);
  }

  void jrbLokacije_actionPerformed(ActionEvent e) {
    setRadioButton(2);
  }

  void jrbIspRevSk_actionPerformed(ActionEvent e) {
    setRadioButton(3);
  }

  void jrbIspAtrikl_actionPerformed(ActionEvent e) {
    setRadioButton(4);
  }

  public void setRadioButton(int i)
  {

    jrbIspAtrikl.setSelected(false);
    jrbIspRevSk.setSelected(false);
    jrbLokacije.setSelected(false);
    jrbIspAmGr.setSelected(false);
    jrbIspKont.setSelected(false);

    switch (i) {
      case 0:
        jrbIspKont.setSelected(true);
        selectedRB=0;
        break;
      case 1:
        jrbIspAmGr.setSelected(true);
        selectedRB=1;
        break;
      case 2:
        jrbLokacije.setSelected(true);
        selectedRB=2;
        break;
      case 3:
        jrbIspRevSk.setSelected(true);
        selectedRB=3;
        break;
      case 4:
        jrbIspAtrikl.setSelected(true);
        selectedRB=4;
        break;
    }
  }

  public void setRadioButtonVrIsp(int z)
  {
    this.jrbAmortizacija.setSelected(false);
    this.jrbLikvidacija.setSelected(false);
    switch (z) {
      case 0:
        jrbAmortizacija.setSelected(true);
        break;
      case 1:
        jrbLikvidacija.setSelected(true);
        break;
    }

  }

  public void EnabDisabOrg(int i)
  {
    if (i==0)
    {
      rcc.setLabelLaF(jrfCOrg, true);
      rcc.setLabelLaF(jrfCOrgNaz, true);
      rcc.setLabelLaF(jbCOrg, true);

    }
    else
    {
      this.jrbAmortizacija.requestFocus();
      rcc.setLabelLaF(jrfCOrg, false);
      rcc.setLabelLaF(jrfCOrgNaz, false);
      rcc.setLabelLaF(jbCOrg, false);

    }
  }

  //******* report
  public static QueryDataSet qds = new QueryDataSet();
  public QueryDataSet tempQds = new QueryDataSet();
  JraCheckBox jcbOrgJed = new JraCheckBox();
  JraCheckBox jcbOblikListe = new JraCheckBox();
  JPanel jPanel1 = new JPanel();
  Border border1;
  JLabel jLabel1 = new JLabel();
  JRadioButton jrbAmortizacija = new JRadioButton();
  XYLayout xYLayout3 = new XYLayout();
  JRadioButton jrbLikvidacija = new JRadioButton();
  JraCheckBox jcbPripOrgJed = new JraCheckBox();
  JPanel jPanel3 = new JPanel();
  XYLayout xYLayout4 = new XYLayout();
  JlrNavField jrfCOrg = new JlrNavField();
  JraButton jbCOrg = new JraButton();
  JlrNavField jrfCOrgNaz = new JlrNavField();
  Border border2;
  TitledBorder titledBorder3;
  public static QueryDataSet getQdsIspis()
  {
    return qds;
  }

  public int prepareIspis(int i)
  {
    String qStr = "";
    
    qStr = rdOSUtil.getUtil().getTST_AMIspis(jrfCOrg.getText().trim(), OJ, OL, IB, PO);

    Aus.refilter(qds, qStr);    
    qds.getColumn("CORG").setRowId(true);
    qds.first();
    for(int j=0;j<qds.getRowCount();j++)
    {
      if (qds.getString("CORG").equals(""))
      {
         qds.deleteRow();
      }
      qds.next();
    }
     qds.first();
    if((OJ==0 && PO==8) && qds.getRowCount()>0)
    {
      for(int j=0;j<qds.getRowCount();j++)
      {
        qds.setString("CORG", this.jrfCOrg.getText());
      }
      qds.next();
    }

    BigDecimal osnSum = new BigDecimal(0);
    BigDecimal ispSum = new BigDecimal(0);
    BigDecimal amSum = new BigDecimal(0);
    BigDecimal pamSum = new BigDecimal(0);
    BigDecimal sadSum = new BigDecimal(0);

    qds.open();
    qds.first();
    do
    {
      osnSum=osnSum.add(qds.getBigDecimal("OSNOVICA"));
      ispSum=ispSum.add(qds.getBigDecimal("ISPRAVAK"));
      amSum=amSum.add(qds.getBigDecimal("AMORTIZACIJA"));
      pamSum=pamSum.add(qds.getBigDecimal("PAMORTIZACIJA"));
      sadSum=sadSum.add(qds.getBigDecimal("SADVRIJED"));

      qds.next();
    }while(qds.inBounds());


    sume = new double[] {osnSum.doubleValue(), ispSum.doubleValue(), amSum.doubleValue(),
                         pamSum.doubleValue(), sadSum.doubleValue()};

    return qds.getRowCount();
  }

  public int prepareIspisLikvidacija()
  {
    String qStr="";
    datumOd = defQDS.getTimestamp("DATUMOD").toString();
    datumDo = defQDS.getTimestamp("DATUMDO").toString();
    boolean poOrgJed = jcbPripOrgJed.isSelected();
   
     if(jcbInvBr.isSelected())
      qStr = rdOSUtil.getUtil().getAmRev(jrfCOrg.getText().trim(),
            util.getTimestampValue(defQDS.getTimestamp("DATUMOD"),0), util.getTimestampValue(defQDS.getTimestamp("DATUMDO"),1), PO, poOrgJed);

    else
      qStr = rdOSUtil.getUtil().getAmRevTot(jrfCOrg.getText().trim(),
          util.getTimestampValue(defQDS.getTimestamp("DATUMOD"),0), util.getTimestampValue(defQDS.getTimestamp("DATUMDO"),1), PO, poOrgJed);
   
    Aus.refilter(qds, qStr);    

    qds.getColumn("CORG").setRowId(true);
    qds.first();

    for(int j=0;j<qds.getRowCount();j++)
    {
      if (qds.getString("CORG").equals(""))
      {
         qds.deleteRow();
      }

      qds.next();
    }
    return qds.getRowCount();
  }

  public void getDefValues()
  {
//    hr.restart.robno._Main.getStartFrame().statusMSG("Priprema podataka");
    String qStr = rdOSUtil.getUtil().getDefaultRekap();
    Aus.setFilter(defQDS, qStr);
//    hr.restart.robno._Main.getStartFrame().statusMSG();
  }


  private String getPocDatum()
  {
    StringBuffer sb = new StringBuffer(util.getTimestampValue(tds.getTimestamp("datum"),0));
    return sb.replace(6, 11, "01-01").toString();
  }

  void jcbOblikListe_actionPerformed(ActionEvent e) {
    if(!jcbOblikListe.isSelected())
    {
      rcc.EnabDisabAll(jPanel2, false);
      OL = 0;
    }
    else
    {
      rcc.EnabDisabAll(jPanel2, true);
      OL = 2;
    }
  }

  void jcbOrgJed_actionPerformed(ActionEvent e) {
    if(jcbOrgJed.isSelected())
    {
      OJ = 1;
    }
    else
    {
      OJ = 0;
    }
  }

  void jcbInvBr_actionPerformed(ActionEvent e) {
    if(jcbInvBr.isSelected())
    {
      IB = 4;
    }
    else
    {
      IB = 0;
    }
  }

  public void hide()
  {
    paramCOrg="";
    super.hide();
  }

  void jrbAmortizacija_actionPerformed(ActionEvent e) {
    setRadioButtonVrIsp(0);
    rcc.setLabelLaF(jcbOblikListe, true);
    rcc.setLabelLaF(jcbOrgJed, true);
    if(jcbOblikListe.isSelected())
      rcc.EnabDisabAll(jPanel2, true);
  }

  void jrbLikvidacija_actionPerformed(ActionEvent e) {
    setRadioButtonVrIsp(1);
    rcc.EnabDisabAll(jPanel2, false);
    rcc.setLabelLaF(jcbOblikListe, false);
    rcc.setLabelLaF(jcbOrgJed, false);
  }
  void jcbPripOrgJed_actionPerformed(ActionEvent e) {
    if(this.jcbPripOrgJed.isSelected())
    {
      poPripCorg = true;
      PO = 8;
      OJ = 1;
      jcbOrgJed.setSelected(true);
    }
    else
    {
      poPripCorg = false;
      PO = 0;
    }
  }

  void reset()
  {
    jcbInvBr.setSelected(false);
    jcbOblikListe.setSelected(false);
    jcbInvBr.setSelected(false);
    setRadioButton(0);
    jcbOrgJed.setSelected(true);
    jcbPripOrgJed.setSelected(false);
    rcc.EnabDisabAll(jPanel2, false);
    OJ=1;
  }
  void bindCorg()
  {
    jrfCOrg.setDataSet(fake);
    jrfCOrg.setVisCols(new int[]{0,1});
    jrfCOrg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jrfCOrg.setColNames(new String[] {"NAZIV"});
    jrfCOrg.setTextFields(new javax.swing.text.JTextComponent[] {jrfCOrgNaz});
    jrfCOrg.setColumnName("CORG");
    jrfCOrg.setSearchMode(0);
    jrfCOrg.setNavButton(this.jbCOrg);
    jrfCOrgNaz.setColumnName("NAZIV");
    jrfCOrgNaz.setNavProperties(jrfCOrg);
  }
}