/****license*****************************************************************
**   file: ispOS.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raIspisDialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
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

public class ispOS extends raIspisDialog{

  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.zapod.OrgStr knjOrgStr = hr.restart.zapod.OrgStr.getOrgStr();
  public QueryDataSet tempQds = new QueryDataSet();
  StorageDataSet knjSDS = new StorageDataSet();
  String currKnj="";
  QueryDataSet defQDS = new QueryDataSet();

  JPanel jPanel3 = new JPanel();
  JPanel jp = new JPanel();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  TitledBorder titledBorder2;
  TitledBorder titledBorder1;
  XYLayout xYLayout1 = new XYLayout();
  XYLayout xYLayout2 = new XYLayout();
  XYLayout xYLayout3 = new XYLayout();
  XYLayout xYLayout4 = new XYLayout();
  JRadioButton jrbTrStanje = new JRadioButton();
  JRadioButton jrbStNaDan = new JRadioButton();
  JRadioButton jrbPocStanje = new JRadioButton();
  JRadioButton jrbIspKont = new JRadioButton();
  JRadioButton jrbIspAmGr = new JRadioButton();
  JRadioButton jrbLokacije = new JRadioButton();
  JRadioButton jrbIspRevSk = new JRadioButton();
  JRadioButton jrbIspAtrikl = new JRadioButton();
  JraTextField jtfStNaDan = new JraTextField();
  JraTextField jrfGodProizPoc = new JraTextField();
  JraTextField jrfGodProizZav = new JraTextField();
  JraCheckBox jcbInvBr = new JraCheckBox();
  JraCheckBox jcbOrgJed = new JraCheckBox();
  JraCheckBox jcbOblikListe = new JraCheckBox();
  JraCheckBox jcbPripOrgJed = new JraCheckBox();
  JLabel jlStatus = new JLabel();
  JLabel jlPorijeklo = new JLabel();
  JLabel jlGodProiz = new JLabel();
  JLabel jlAktivnost = new JLabel();
  JLabel jlDo = new JLabel();
  JlrNavField jrfCOrgNaz = new JlrNavField();
  JlrNavField jrfCOrg = new JlrNavField();
  JraButton jbCOrg = new JraButton();
  raComboBox rcbStatus = new raComboBox();
  raComboBox rcbPorijeklo = new raComboBox();
  raComboBox rcbAktivnost = new raComboBox();

//  Column column1 = new Column();
  Column temp = new Column();
  Column GPPoc = new Column();
  Column GPZav = new Column();
  Column statusTemp = new Column();
  Column porijekloTemp = new Column();
  Column aktivnostTemp = new Column();
  public StorageDataSet statusDS = new StorageDataSet();
  StorageDataSet fake = new StorageDataSet();
  TableDataSet tds = new TableDataSet();
  Valid vl = Valid.getValid();
  Timestamp oldValue;
  Border border1;
  TitledBorder titledBorder3;
  Timestamp initDate = hr.restart.util.Valid.getValid().findDate(false, 0);

  public static int selectedRB = 0;
  public static int selectedRB2 = 0;
  public static String dan = "";
  public static double [] sume;
  public static String status ;
  public static String aktivnost ;
  public static String porijeklo ;
  public static QueryDataSet qds = new QueryDataSet();
  public static boolean poPripCorg = false;

  boolean inDate = false;
  public int OJ = 0;
  public int OL = 0;
  public int IB = 0;
  public int PO = 0;

  public static int getSelectedRB()
  {
    return selectedRB;
  }

  public static int getSelectedRB2()
  {
    return selectedRB2;
  }

  public static String getDan()
  {
    return dan;
  }

  public ispOS() {
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
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Vrsta ispisa");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Oblik ispisa");
    titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Org. jedinice");
    border1 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    xYLayout1.setWidth(480);
    xYLayout1.setHeight(500);
    jp.setLayout(xYLayout1);
    this.setJPan(jp);
    jPanel1.setBorder(titledBorder1);
    jPanel1.setLayout(xYLayout3);
    jPanel2.setBorder(titledBorder2);
    jPanel2.setLayout(xYLayout2);
    jrbTrStanje.setText("Trenutno stanje");
    jrbTrStanje.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbTrStanje_actionPerformed(e);
      }
    });
    jrbStNaDan.setText("Stanje na dan");
    jrbStNaDan.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jrbStNaDan_focusGained(e);
      }
    });
    jrbStNaDan.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbStNaDan_actionPerformed(e);
      }
    });
    jrbPocStanje.setText("Poèetno stanje");
    jrbPocStanje.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbPocStanje_actionPerformed(e);
      }
    });
    xYLayout3.setWidth(280);
    xYLayout3.setHeight(100);
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
    jp.setMinimumSize(new Dimension(478, 500));
    jp.setPreferredSize(new Dimension(478, 500));

    jtfStNaDan.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfStNaDan_focusGained(e);
      }
      public void focusLost(FocusEvent e) {
        jtfStNaDan_focusLost(e);
      }
    });
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
    jPanel3.setLayout(xYLayout4);
    jPanel3.setBorder(titledBorder3);
    jcbPripOrgJed.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbPripOrgJed_actionPerformed(e);
      }
    });
    jcbPripOrgJed.setText("Ispis pripadajuæih org. jedinca");
    jrfCOrgNaz.setColumnName("NAZIV");
    jrfCOrgNaz.setNavProperties(jrfCOrg);
    jbCOrg.setText("...");
    jlStatus.setText("Status");
    jlPorijeklo.setText("Porijeklo");
    jlGodProiz.setText("God. pr.");
    jlDo.setText("-");
    jrfGodProizPoc.setHorizontalAlignment(SwingConstants.RIGHT);
    jrfGodProizZav.setHorizontalAlignment(SwingConstants.RIGHT);
    jlAktivnost.setText("Aktivnost");
    rcbAktivnost.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        rcbAktivnost_actionPerformed(e);
      }
    });
    jPanel1.add(jrbPocStanje, new XYConstraints(15, 5, -1, -1));
    jPanel1.add(jrbStNaDan, new XYConstraints(15, 30, -1, -1));
    jPanel1.add(jrbTrStanje, new XYConstraints(15, 55, -1, -1));
    jPanel1.add(jtfStNaDan,   new XYConstraints(130, 30, 100, -1));
    jp.add(rcbStatus,     new XYConstraints(345, 175, 120, -1));
    jp.add(rcbPorijeklo,      new XYConstraints(345, 200, 120, -1));
    jp.add(jlPorijeklo,  new XYConstraints(280, 200, -1, -1));
    jp.add(rcbAktivnost,     new XYConstraints(345, 225, 120, -1));
    jp.add(jlAktivnost,   new XYConstraints(280, 225, -1, -1));
    jp.add(jPanel3, new XYConstraints(15, 24, 450, 70));
    jp.add(jcbPripOrgJed,    new XYConstraints(36, 103, -1, -1));
    jPanel3.add(jrfCOrg,   new XYConstraints(15, 7, 100, -1));
    jPanel3.add(jrfCOrgNaz,     new XYConstraints(120, 7, 275, -1));
    jPanel3.add(jbCOrg,      new XYConstraints(401, 7, 21, 21));
    jp.add(jPanel2,             new XYConstraints(15, 291, 450, 162));
    jPanel2.add(jrbIspKont, new XYConstraints(15, 5, -1, -1));
    jPanel2.add(jrbIspAmGr, new XYConstraints(15, 30, -1, -1));
    jPanel2.add(jrbLokacije, new XYConstraints(15, 55, -1, -1));
    jPanel2.add(jrbIspAtrikl, new XYConstraints(15, 105, -1, -1));
    jPanel2.add(jrbIspRevSk, new XYConstraints(15, 80, -1, -1));
    jp.add(jcbInvBr,       new XYConstraints(36, 458, -1, -1));
    jp.add(jcbOrgJed,       new XYConstraints(36, 260, -1, -1));
    jp.add(jcbOblikListe,         new XYConstraints(357, 260, -1, -1));
    jp.add(jlStatus,     new XYConstraints(280, 175, -1, -1));
    jp.add(jlGodProiz,    new XYConstraints(280, 150, -1, -1));
    jp.add(jrfGodProizPoc,       new XYConstraints(345, 150, 50, -1));
    jp.add(jlDo,    new XYConstraints(402, 152, -1, -1));
    jp.add(jrfGodProizZav,   new XYConstraints(413, 150, 50, -1));
    jp.add(jPanel1,   new XYConstraints(15, 138, 259, 115));



//    column1.setCaption("datum");
//    column1.setColumnName("datum");
//    column1.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
//    column1.setDisplayMask("dd-MM-yyyy");
//    column1.setEditMask("dd-MM-yyyy");
//    column1.setResolvable(false);
//    column1.setSqlType(0);
//    column1.setServerColumnName("NewColumn1");

    temp.setColumnName("CORG");
    temp.setDataType(com.borland.dx.dataset.Variant.STRING);

    GPPoc.setColumnName("GPP");
    GPPoc.setDataType(com.borland.dx.dataset.Variant.STRING);

    GPZav.setColumnName("GPZ");
    GPZav.setDataType(com.borland.dx.dataset.Variant.STRING);

    fake.setColumns(new Column[] {temp, GPPoc, GPZav});
    jrfGodProizPoc.setDataSet(fake);
    jrfGodProizZav.setDataSet(fake);
    jrfGodProizPoc.setColumnName("GPP");
    jrfGodProizZav.setColumnName("GPZ");

    bindCorg();
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
        new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnjig, String newKnjig)
      {
        bindCorg();
      };
    }
    );
     tds.setColumns(new Column[] {dM.createTimestampColumn("datum")});
    jtfStNaDan.setColumnName("datum");
    jtfStNaDan.setDataSet(tds);
    jtfStNaDan.setHorizontalAlignment(SwingConstants.CENTER);


    statusTemp.setColumnName("STATUS");
    statusTemp.setDataType(com.borland.dx.dataset.Variant.STRING);

    aktivnostTemp.setColumnName("AKTIV");
    aktivnostTemp.setDataType(com.borland.dx.dataset.Variant.STRING);


    rcbStatus.setRaColumn("STATUS");
    rcbStatus.setRaDataSet(statusDS);
    rcbStatus.setRaItems(new String[][] {
      {"Sva OS","S"},
      {"OS u pripremi","P"},
      {"OS u upotrebi","A"}
    });

    rcbAktivnost.setRaColumn("AKTIV");
    rcbAktivnost.setRaDataSet(statusDS);
    rcbAktivnost.setRaItems(new String[][] {
      {"Sva OS",""},
      {"Aktivna OS","D"},
      {"Neaktivna OS","N"}
    });

    porijekloTemp.setColumnName("PORIJEKLO");
    porijekloTemp.setDataType(com.borland.dx.dataset.Variant.STRING);

    rcbPorijeklo.setRaColumn("PORIJEKLO");
    rcbPorijeklo.setRaDataSet(statusDS);
    rcbPorijeklo.setRaItems(new String[][] {
      {"Sva porijekla",""},
      {"Tuzemstvo","1"},
      {"Inozemstvo","2"},
      {"Vrijednosnice","3"}
    });

    statusDS.setColumns(new Column[] {statusTemp, porijekloTemp,aktivnostTemp});

    statusDS.open();
    setRadioButton(0);
    setRadioButton2(0);
    rcc.EnabDisabAll(jPanel2, false);
    rcc.setLabelLaF(jtfStNaDan, false);
  }

  public void componentShow()
  {
    oldValue=null;
    rcc.setLabelLaF(jtfStNaDan, false);
    fake.setString("CORG",hr.restart.zapod.OrgStr.getKNJCORG());
    this.jrfCOrg.forceFocLost();
    showDefaultValues();
  }

  public void showDefaultValues()
  {
    if(!tds.isOpen())
      tds.open();
    if(!fake.isOpen())
      fake.open();
    if(oldValue==null)
    {      
      currKnj = knjOrgStr.getKNJCORG();
      String qStr = rdOSUtil.getUtil().getDefaultRekap(currKnj);
      Aus.refilter(defQDS, qStr);
      
      defQDS.first();

      Timestamp  tsCurrY;

      String currY = vl.findYear(defQDS.getTimestamp("DATUM"));
      String trenGod = vl.findYear();

     if(rdOSUtil.getUtil().StrToInt(currY)!=rdOSUtil.getUtil().StrToInt(trenGod))
     {
       tsCurrY = hr.restart.util.Util.getUtil().getLastDayOfYear(defQDS.getTimestamp("DATUM"));
     }
     else
     {
//       lastY = Timestamp.valueOf((rdOSUtil.getUtil().StrToInt(currY)-1)+"-12-31 23:59:59.0");
       tsCurrY = vl.findDate(false, 0);
    }
//      tds.setTimestamp("datum", initDate);
      tds.setTimestamp("datum", tsCurrY);
    }



    reset();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        jrfCOrg.requestFocus();
      }
    });

  }

  public boolean runFirstESC()
  {
    if(inDate)
    {
      return true;
    }
    if(!jrfCOrg.getText().trim().equals("") )
    {
      fake.setString("CORG", "");
      jrfCOrgNaz.setText("");
      return true;
    }

    return false;
  }

  public void firstESC()
  {
    if(inDate)
    {
      jtfStNaDan.selectAll();
    }
    else
    {
      jrfCOrg.setText("");
      jrfCOrg.forceFocLost();
      this.showDefaultValues();
      jrfCOrg.requestFocus();
    }
  }

  public boolean okPress()
  {

    if(!vl.findYear(defQDS.getTimestamp("DATUM")).equals(vl.findYear(tds.getTimestamp("datum"))))
    {
      jtfStNaDan.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Pogrešan datum !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }

    int rows = 0;
    dan = jtfStNaDan.getText().trim();
    if(selectedRB2==2)
      oldValue = tds.getTimestamp("datum");

    if (jrfCOrg.getText().trim().equals("0") || jrfCOrg.getText().trim().equals(""))
    {
      rows = prepareIspis(0);
    }
    else
      rows = prepareIspis(1);

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

    System.out.println("OJ+OL+IB = " + (OJ+OL+IB));

    switch (OJ+OL+IB) {
      case 0:
        this.addReport("hr.restart.os.repIspisOS_0","Ispis osnovnih sredstava", 5);
        break;
      case 1:
        this.addReport("hr.restart.os.repIspisOS_1","Ispis osnovnih sredstava", 5);
        break;
      case 2:
        this.addReport("hr.restart.os.repIspisOS_2","Ispis osnovnih sredstava", 5);
        break;
      case 3:
        this.addReport("hr.restart.os.repIspisOS","Ispis osnovnih sredstava", 5);
        break;
      case 4:
        this.addReport("hr.restart.os.repIspisOS_4","Ispis osnovnih sredstava", 5);
        break;
      case 5:
        this.addReport("hr.restart.os.repIspisOS_5","Ispis osnovnih sredstava", 5);
        break;
      case 6:
        this.addReport("hr.restart.os.repIspisOS_6","Ispis osnovnih sredstava", 5);
        break;
      case 7:
        this.addReport("hr.restart.os.repInvIspOS","Ispis osnovnih sredstava", 5);
        break;
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

  void jrbPocStanje_actionPerformed(ActionEvent e) {
    this.setRadioButton2(0);
    oldValue = null;
    rcc.setLabelLaF(this.jtfStNaDan, false);
  }

  void jrbTrStanje_actionPerformed(ActionEvent e) {
    this.setRadioButton2(1);
    oldValue = null;
    rcc.setLabelLaF(this.jtfStNaDan, false);

  }

  void jrbStNaDan_actionPerformed(ActionEvent e) {
    setRadioButton2(2);
    rcc.setLabelLaF(this.jtfStNaDan, true);
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

  public void setRadioButton2(int i)
  {
    jrbPocStanje.setSelected(false);
    jrbTrStanje.setSelected(false);
    jrbStNaDan.setSelected(false);

    switch (i) {
      case 0:
        jrbPocStanje.setSelected(true);
        jrbPocStanje.grabFocus();
        selectedRB2=0;
        break;
      case 1:
        jrbTrStanje.setSelected(true);
        selectedRB2=1;
        break;
      case 2:
        this.jrbStNaDan.setSelected(true);
        selectedRB2=2;
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
      jcbPripOrgJed.requestFocus();
      rcc.setLabelLaF(jrfCOrg, false);
      rcc.setLabelLaF(jrfCOrgNaz, false);
      rcc.setLabelLaF(jbCOrg, false);
    }
  }

  //******* report
  public static QueryDataSet getQdsIspis()
  {
    return qds;
  }

  public int prepareIspis(int i)
  {
    status = statusDS.getString("STATUS");
    aktivnost = statusDS.getString("AKTIV");
    porijeklo = statusDS.getString("PORIJEKLO");    
    String qStr ="";
    if (selectedRB2==0) // Pocetno stanje
    {
      qStr = rdOSUtil.getUtil().getPST_OSIspis(jrfCOrg.getText().trim(), OJ,OL,IB,PO,
          statusDS.getString("STATUS"), fake.getString("GPP"), fake.getString("GPZ"), statusDS.getString("PORIJEKLO"),
          statusDS.getString("AKTIV"));
    }
    else if (this.selectedRB2==2) // Stanje na dan
    {
      qStr = rdOSUtil.getUtil().getSND_OSIspis(jrfCOrg.getText().trim(), getPocDatum(),
          util.getTimestampValue(tds.getTimestamp("datum"),1), OJ, OL, IB, PO, statusDS.getString("STATUS"),
          fake.getString("GPP"), fake.getString("GPZ"),statusDS.getString("PORIJEKLO"),
          statusDS.getString("AKTIV"));
    }
    else if (selectedRB2==1) //  Trenutno stanje
    {
      qStr = rdOSUtil.getUtil().getTST_OSIspis(jrfCOrg.getText().trim(), OJ, OL, IB, PO, statusDS.getString("STATUS"),
          fake.getString("GPP"), fake.getString("GPZ"),statusDS.getString("PORIJEKLO"),
          statusDS.getString("AKTIV"));
    }
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
    if(qds.getRowCount()==0) return 0;

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
    BigDecimal osn_ispSum = new BigDecimal(0);

    qds.open();
    qds.first();
    do
    {
      // pocetno st
      if (this.selectedRB2==0 )
      {
        osnSum=osnSum.add(qds.getBigDecimal("OSNPOCETAK"));
        ispSum=ispSum.add(qds.getBigDecimal("ISPPOCETAK"));
      }
      // st na dan
        else if (this.selectedRB2==1 )
      {
        qds.setBigDecimal("OSNDUGUJE", qds.getBigDecimal("OSNDUGUJE").add(qds.getBigDecimal("OSNPOCETAK")));
        qds.setBigDecimal("ISPPOTRAZUJE", qds.getBigDecimal("ISPPOTRAZUJE").add(qds.getBigDecimal("ISPPOCETAK")));

        osnSum=osnSum.add(qds.getBigDecimal("OSNDUGUJE").add(qds.getBigDecimal("OSNPOTRAZUJE").negate()));
        ispSum=ispSum.add(qds.getBigDecimal("ISPPOTRAZUJE").add(qds.getBigDecimal("ISPDUGUJE").negate()));
//        osnSum=osnSum.add(qds.getBigDecimal("OSNDUGUJE").add(qds.getBigDecimal("OSNPOTRAZUJE")));
//        ispSum=ispSum.add(qds.getBigDecimal("ISPPOTRAZUJE").add(qds.getBigDecimal("ISPDUGUJE")));
      }
      // tr stanje
      else
      {
        osnSum=osnSum.add(qds.getBigDecimal("OSNDUGUJE").add(qds.getBigDecimal("OSNPOTRAZUJE").negate()));
        ispSum=ispSum.add(qds.getBigDecimal("ISPPOTRAZUJE").add(qds.getBigDecimal("ISPDUGUJE").negate()));
//        osnSum=osnSum.add(qds.getBigDecimal("OSNDUGUJE").add(qds.getBigDecimal("OSNPOTRAZUJE")));
//        ispSum=ispSum.add(qds.getBigDecimal("ISPPOTRAZUJE").add(qds.getBigDecimal("ISPDUGUJE")));
      }
      qds.next();
    }while(qds.inBounds());
    osn_ispSum = osn_ispSum.add(osnSum.add(ispSum.negate()));
    sume = new double[] {osnSum.doubleValue(), ispSum.doubleValue(), osn_ispSum.doubleValue()};
    return qds.getRowCount();
  }

  public String getPocDatum()
  {
    Integer tempYear = new Integer((tds.getTimestamp("datum").toString()).substring(0,4));
    int year= tempYear.intValue();
    return ((year-1)+"-12-31 00:00:00.0");
  }

  void jrbStNaDan_focusGained(FocusEvent e) {
    if (jrbStNaDan.isSelected())
      rcc.setLabelLaF(this.jtfStNaDan, true);
  }

  void jtfStNaDan_focusGained(FocusEvent e) {
    inDate = true;
  }
  void jtfStNaDan_focusLost(FocusEvent e) {
    inDate = false;
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
      OJ = 1;
    else
      OJ = 0;
  }

  void jcbInvBr_actionPerformed(ActionEvent e) {
    if(jcbInvBr.isSelected())
      IB = 4;
    else
      IB = 0;
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

  private void reset()
  {
    fake.setString("GPP","1900");
    fake.setString("GPZ", hr.restart.util.Valid.getValid().findYear());

    rcbPorijeklo.setSelectedIndex(0);
    rcbStatus.setSelectedIndex(2);
    rcbAktivnost.setSelectedIndex(1);
    jcbInvBr.setSelected(false);
    jcbOblikListe.setSelected(false);
    jcbInvBr.setSelected(false);
    setRadioButton(0);
    setRadioButton2(0);
    jcbOrgJed.setSelected(true);
    jcbPripOrgJed.setSelected(false);
    rcc.EnabDisabAll(jPanel2, false);
    statusDS.setString("STATUS","A");
    statusDS.setString("AKTIV","D");
    statusDS.setString("PORIJEKLO","");
    OJ=1;
  }

  void rcbAktivnost_actionPerformed(ActionEvent e) {
  }

  private void bindCorg()
  {
    jrfCOrg.setDataSet(fake);
    jrfCOrg.setNavButton(this.jbCOrg);
    jrfCOrg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jrfCOrg.setVisCols(new int[]{0,1});
    jrfCOrg.setColNames(new String[] {"NAZIV"});
    jrfCOrg.setTextFields(new javax.swing.text.JTextComponent[] {jrfCOrgNaz});
    jrfCOrg.setColumnName("CORG");
    jrfCOrg.setSearchMode(0);
  }
}//