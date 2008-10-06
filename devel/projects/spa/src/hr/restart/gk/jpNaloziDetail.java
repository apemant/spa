/****license*****************************************************************
**   file: jpNaloziDetail.java
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
package hr.restart.gk;

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpNaloziDetail extends JPanel {
  XYLayout xYLay = new XYLayout();
  private frmNalozi fNalozi;
  lookupData ld = lookupData.getlookupData();
  public jpBrojNaloga jpBrNal = new jpBrojNaloga(true);
  public jpTemSK jpTSK = new jpTemSK();
  jpDevIznos jpDevI = new jpDevIznos();
  JPanel jpEntry = new JPanel();
  JLabel jlOPIS = new JLabel();
  JraTextField jtOPIS = new JraTextField();

  JLabel jlBROJKONTA = new JLabel();

  JLabel jlCORG = new JLabel();
  
  JLabel jlOZN = new JLabel();
  JLabel jlNAZ = new JLabel();
  dM dm = dM.getDataModule();
  raCommonClass rCC = raCommonClass.getraCommonClass();
  hr.restart.zapod.OrgStr Ojs = hr.restart.zapod.OrgStr.getOrgStr();
  hr.restart.zapod.raKonta rKon;
  JLabel jlDATDOK = new JLabel();
  JraTextField jtDATDOK = new JraTextField();
  JLabel jlID = new JLabel();
  JraTextField jtID = new JraTextField() {
    public void valueChanged() {
      if (jtID.getDataSet().getBigDecimal(jtID.getColumnName()).signum() != 0)
        jpDevI.setPVval(jtID.getDataSet().getBigDecimal(jtID.getColumnName()));
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (jtID.getDataSet().getBigDecimal(jtID.getColumnName()).signum() != 0)
        jpDevI.setPVval(jtID.getDataSet().getBigDecimal(jtID.getColumnName()));
    }*/
  };
  JLabel jlIP = new JLabel();
  JraTextField jtIP = new JraTextField() {
    public void valueChanged() {
      if (jtIP.getDataSet().getBigDecimal(jtIP.getColumnName()).signum() != 0)
        jpDevI.setPVval(jtIP.getDataSet().getBigDecimal(jtIP.getColumnName()));
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (jtIP.getDataSet().getBigDecimal(jtIP.getColumnName()).signum() != 0)
        jpDevI.setPVval(jtIP.getDataSet().getBigDecimal(jtIP.getColumnName()));
    }*/
  };
  JLabel jlVRDOK = new JLabel();
  raComboBox rcVRDOK = new raComboBox();
  raKontoCorgGroup kcGroup;
  public JLabel jlKontoConcat = new JLabel();
  private JLabel jlD = new JLabel();
  private JLabel jlP = new JLabel();
  public JraTextField jtHor1 = new JraTextField();
  public JraTextField jtVer1 = new JraTextField();
  public jpNaloziDetail(frmNalozi fNal) {
    try {
      fNalozi = fNal;
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    kcGroup = new raKontoCorgGroup(fNalozi.raDetail, jtDATDOK) {
      public void setScrKonto(String brKonta) {
        super.setScrKonto(brKonta);
        setScrKontoNalozi(brKonta);
      }
      public void afterAfterLookupKonto() {
//        jlKontoConcat.setText(kcGroup.getJlrBROJKONTA().getText());
        setKontoConcat(1);
//        setPanelsVisible(1);
      }
    };
    this.setLayout(new BorderLayout());
    jpEntry.setLayout(xYLay);
//    jpCorg.setDataSet(fNalozi.getDetailSet());
    xYLay.setWidth(645);
    xYLay.setHeight(245);

    jlOPIS.setText("Opis");
    jtOPIS.setColumnName("OPIS");
    jtOPIS.setDataSet(fNalozi.getDetailSet());

    jpTSK.setJpDevIznos(jpDevI);
    
    jlBROJKONTA.setText("Konto");
//    jlrBROJKONTA.setColumnName("BROJKONTA");
//    jlrBROJKONTA.setDataSet(fNalozi.getDetailSet());
//    jlrBROJKONTA.setColNames(new String[] {"NAZIVKONTA"});
//    jlrBROJKONTA.setVisCols(new int[] {0,1});
//    jlrBROJKONTA.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZIVKONTA});
//    jlrBROJKONTA.setRaDataSet(rKon.getAnalitickaKonta());
//    jlrBROJKONTA.setSearchMode(0);
//    jlrNAZIVKONTA.setColumnName("NAZIVKONTA");
//    jlrNAZIVKONTA.setSearchMode(1);
//    jlrNAZIVKONTA.setNavProperties(jlrBROJKONTA);
//    jbGetKonto.setText("...");
//    jlrBROJKONTA.setNavButton(jbGetKonto);

    jlCORG.setText("Org jedinica");
//    jlrCORG.setColumnName("CORG");
//    jlrCORG.setDataSet(fNalozi.getDetailSet());
//    jlrCORG.setColNames(new String[] {"NAZIV"});
//    jlrCORG.setVisCols(new int[] {0,1});
//    jlrCORG.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZIVORG});
//    jlrCORG.setRaDataSet(dm.getOrgstruktura());
//    jlrCORG.setSearchMode(0);
//    jlrNAZIVORG.setColumnName("NAZIV");
//    jlrNAZIVORG.setSearchMode(1);
//    jlrNAZIVORG.setNavProperties(jlrCORG);
//    jbGetCorg.setText("...");
//    jlrCORG.setNavButton(jbGetCorg);


    jlOZN.setText("Oznaka");
    jlNAZ.setText("Naziv");

    jlDATDOK.setText("Datum dokumenta");
    jtDATDOK.setHorizontalAlignment(SwingConstants.CENTER);
    jtDATDOK.setColumnName("DATDOK");
    jtDATDOK.setDataSet(fNalozi.getDetailSet());

    jtID.setColumnName("ID");
    jtID.setDataSet(fNalozi.getDetailSet());
    jtIP.setColumnName("IP");
    jtIP.setDataSet(fNalozi.getDetailSet());

    rcVRDOK.setRaColumn("VRDOK");
    rcVRDOK.setRaDataSet(fNalozi.getDetailSet());

    jlID.setText("Duguje");
    jlIP.setText("Potražuje");
    jlVRDOK.setText("Vrsta dokumenta");

    jpBrNal.initJPStat(fNalozi.getMasterSet());
//    add(jpBrNal,   new XYConstraints(0, 0, -1, -1));
//    add(jpCorg, new XYConstraints(0,40,700,-1));
    jlKontoConcat.setFont(new java.awt.Font("Dialog", 1, 12));
    jlKontoConcat.setHorizontalAlignment(SwingConstants.CENTER);
    jlD.setFont(new java.awt.Font("Dialog", 1, 14));
    jlD.setText("D");
    jlP.setFont(new java.awt.Font("Dialog", 1, 14));
    jlP.setText("P");
    jtHor1.setBackground(Color.black);
    jtHor1.setEnabled(false);
    jtHor1.setBorder(BorderFactory.createLineBorder(Color.black));
    jtHor1.setEditable(false);
    jtVer1.setBackground(Color.black);
    jtVer1.setEnabled(false);
    jtVer1.setBorder(BorderFactory.createLineBorder(Color.black));
    jtVer1.setEditable(false);
    jtVer1.setText("jTextField2");
    jpEntry.add(jlOPIS,      new XYConstraints(15, 10, -1, -1));
    jpEntry.add(jtOPIS,      new XYConstraints(150, 10, 450, -1));
    jpEntry.add(jlBROJKONTA,        new XYConstraints(15, 55, -1, -1));
    jpEntry.add(kcGroup.getJlrBROJKONTA(),         new XYConstraints(150, 55, 100, -1));
    jpEntry.add(kcGroup.getJlrNAZIVKONTA(),        new XYConstraints(255, 55, 345, -1));
    jpEntry.add(kcGroup.getJbGetKonto(),   new XYConstraints(605, 55, 21, 21));

    jpEntry.add(jlCORG,        new XYConstraints(15, 80, -1, -1));
    jpEntry.add(kcGroup.getJlrCORG(),         new XYConstraints(150, 80, 100, -1));
    jpEntry.add(kcGroup.getJlrNAZIVORG(),        new XYConstraints(255, 80, 345, -1));
    jpEntry.add(kcGroup.getJbGetCorg(),   new XYConstraints(605, 80, 21, 21));

    jpEntry.add(jlOZN,     new XYConstraints(150, 34, -1, -1));
    jpEntry.add(jlNAZ,     new XYConstraints(255, 34, -1, -1));
//    jpEntry.add(jlDATDOK,     new XYConstraints(15, 105, -1, -1));
//    jpEntry.add(jtDATDOK,      new XYConstraints(150, 105, 100, -1));
    jpEntry.add(jpTSK, new XYConstraints(0, 105, -1, 50));
//    jpEntry.add(jlID,       new XYConstraints(15, 160, -1, -1));
    jpEntry.add(new JLabel("Iznos"),new XYConstraints(15, 180, -1, -1));
    jpEntry.add(jtID,          new XYConstraints(190, 180, 140, -1));//y=160
    jpEntry.add(jtIP,            new XYConstraints(420, 180, 140, -1));//y=160
//    jpEntry.add(jlIP,      new XYConstraints(365, 160, -1, -1));
    jpEntry.add(jlVRDOK,     new XYConstraints(255, 105, -1, -1));
    jpEntry.add(rcVRDOK,       new XYConstraints(400, 105, 200, -1));
    this.add(jpBrNal,BorderLayout.NORTH);
    this.add(jpEntry,BorderLayout.CENTER);
    jpEntry.add(jlKontoConcat,      new XYConstraints(250, 155, 250, -1));//y=135
    jpEntry.add(jlD,   new XYConstraints(150, 152, -1, -1));//y=132
    jpEntry.add(jlP,  new XYConstraints(591, 153, -1, -1));//y=133
    jpEntry.add(jtHor1,       new XYConstraints(150, 173, 450, 2)); //y=153
    jpEntry.add(jtVer1,   new XYConstraints(374, 175, 2, 26)); //y=155
    jpEntry.add(jpDevI, new XYConstraints(0, 212, -1, -1));
    kcGroup.setNextComponent(jpTSK.jraDatdok);
  }

  void setVisibleVRDOK(boolean visible) {
    //boolean visible1 = visible;
    boolean visible1=false;
    jlVRDOK.setVisible(visible1); // to bi trebalo sluziti za unos salda konti dokumenata kroz naloge
    rcVRDOK.setVisible(visible1); /** @todo staviti additionalfilter da ne moze salda konti (suprotno nego kod izvoda) */
  }



  void setScrKontoNalozi(String brKonta) {
    setScr(rKon.isSaldak(brKonta),rKon.isDugovni(brKonta),rKon.isPotrazni(brKonta));
  }

  void setScr(boolean isSaldak,boolean isDugovni,boolean isPotrazni) {
    if (fNalozi.shutTheFuckUp_setScrKonto) return;
//    fNalozi.setItemsForVRDOK(fNalozi.raDetail.getMode());
    hr.restart.sk.raGkSkUnosHandler.checkParam();
    setVisibleVRDOK(isSaldak && hr.restart.sk.raGkSkUnosHandler.isGKSK);
//    if (rcVRDOK.isVisible()) fNalozi.findDefaultComboVRDOK();
    rCC.setLabelLaF(jtID,isDugovni);
    rCC.setLabelLaF(jtIP,isPotrazni);
    if (isSaldak) {
//      rCC.setLabelLaF(jtID, raKonta.isKupac());  //ovo je za racune, a treba handlati ispravke izvoda
//      rCC.setLabelLaF(jtIP, raKonta.isDobavljac()); //ovo je za racune, a treba handlati ispravke izvoda
    }
  }

//  void aft_lookUpKonto() {
//    if (fNalozi.raDetail.getMode() == 'B') return;
//    kcGroup.aft_lookUpKonto();
//  }

  void clrScr() {
    long ldatdok = fNalozi.getDetailSet().getTimestamp("DATDOK").getTime();
    fNalozi.getDetailSet().setDefaultValues();
    fNalozi.getDetailSet().setTimestamp("DATDOK",ldatdok);
    kcGroup.clrCORG();
    kcGroup.clrBROJKONTA();
//    jlKontoConcat.setText("");
    setKontoConcat(0);
  }
//  public void setPanelsVisible(int i)
//  {
//    if(i==0)
//    {
//      panel1.setVisible(false);
//      panel2.setVisible(false);
//    }
//    else
//    {
//      panel1.setVisible(true);
//      panel2.setVisible(true);
//    }
//  }
  public void setKontoConcat(String konto) {
    jlKontoConcat.setText(konto);
    boolean isSaldak;
    try {
      isSaldak = rKon.isSaldak(konto);
    } catch (IllegalArgumentException e) {
      isSaldak = false;
    }
    jpTSK.enable(isSaldak, fNalozi.raDetail);
  }
  
  public void setKontoConcat(int i)
  {
    if(i == 1) {
      setKontoConcat(kcGroup.getJlrBROJKONTA().getText());
    } else {
      jlKontoConcat.setText("");
      jpTSK.enable(false, fNalozi.raDetail);
    }
  }
}