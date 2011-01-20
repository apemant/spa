/****license*****************************************************************
**   file: jpIzvodiDetail.java
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
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpIzvodiDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();

  dM dm = dM.getDataModule();

  //  jpBrojNaloga jpBrnal = new jpBrojNaloga();
  frmIzvodi fIzvodi;

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();

  JLabel jlBrojdok = new JLabel();

  JLabel jlBrojkonta = new JLabel();

  JLabel jlCorg = new JLabel();

  JLabel jlDatdok = new JLabel();

  JLabel jlId = new JLabel();

  JLabel jlIp = new JLabel();

  JLabel jlOpis = new JLabel();

  JLabel jlZr = new JLabel();

  JLabel jlaCpar = new JLabel();

  JLabel jlaNazpar = new JLabel();

  JLabel jlaZr = new JLabel();

  JLabel jlVRDOK = new JLabel();

  JraButton jbSelZr = new JraButton();

  String izvunospar = frmParam.getParam("gk",
      "izvunospar","ZSN","Na unosu izvoda - partner unosi se Naziv/Sifra/Ziro ili Ziro/Sifra/Naziv"
      ).trim().toUpperCase();

  JraTextField jraBrojdok = new JraTextField() {
    public void valueChanged() {
      fIzvodi.setOpis(false);
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      fIzvodi.setOpis(false);
    }*/
  };

  JraTextField jraDatdok = new JraTextField() {
    public void valueChanged() {
      jpDevI.setTecDate(jraDatdok.getDataSet().getTimestamp(
          jraDatdok.getColumnName()));
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      jpDevI.setTecDate(jraDatdok.getDataSet().getTimestamp(
          jraDatdok.getColumnName()));
    }*/
  };

  JraTextField jraExtbrojdok = new JraTextField();

  JraTextField jraId = new JraTextField() {
    public void valueChanged() {
      if (jraId.getDataSet().getBigDecimal(jraId.getColumnName()).signum() != 0)
        jpDevI
            .setPVval(jraId.getDataSet().getBigDecimal(jraId.getColumnName()));
      if (fIzvodi.devizni)
        fIzvodi.calcTecaj("ID", "DEVID");
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (jraId.getDataSet().getBigDecimal(jraId.getColumnName()).signum() != 0)
        jpDevI
            .setPVval(jraId.getDataSet().getBigDecimal(jraId.getColumnName()));
    }*/
  };

  JraTextField jraIp = new JraTextField() {
    public void valueChanged() {
      if (jraIp.getDataSet().getBigDecimal(jraIp.getColumnName()).signum() != 0)
        jpDevI
            .setPVval(jraIp.getDataSet().getBigDecimal(jraIp.getColumnName()));
      if (fIzvodi.devizni)
        fIzvodi.calcTecaj("IP", "DEVIP");
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (jraIp.getDataSet().getBigDecimal(jraIp.getColumnName()).signum() != 0)
        jpDevI
            .setPVval(jraIp.getDataSet().getBigDecimal(jraIp.getColumnName()));
    }*/
  };
  
  JraTextField jraPvid = new JraTextField() {
    public void valueChanged() {
      if (fIzvodi.devizni)
        fIzvodi.calcTecaj("ID", "DEVID");
    }
  };
  
  JraTextField jraPvip = new JraTextField() {
    public void valueChanged() {
      if (fIzvodi.devizni)
        fIzvodi.calcTecaj("IP", "DEVIP");
    }
  };
  
  JPanel jpDev = new JPanel(new XYLayout(580, 25));

  JraTextField jraOpis = new JraTextField();

  JraTextField jraTecaj = new JraTextField();

  JLabel jlaTecaj = new JLabel();

  JlrNavField jlrCpar = new JlrNavField();

  JlrNavField jlrNazpar = new JlrNavField();

  JlrNavField jlrZr = new JlrNavField();

  raComboBox rcVRDOK = new raComboBox() {
    public void this_itemStateChanged() {
      if (fIzvodi.raDetail.getMode() != 'B') {
        super.this_itemStateChanged();
        fIzvodi.setVRDOK();
      }
    }
  };

  raKontoCorgGroup kcGroup;

  private Panel panel1 = new Panel();

  private Panel panel2 = new Panel();

  private JLabel jlD = new JLabel();

  private JLabel jlP = new JLabel();

  private JLabel jlKontoConcat = new JLabel();

  public JraTextField jtHor1 = new JraTextField();

  public JraTextField jtVer1 = new JraTextField();
  
  public JraButton jbCopyVals = new JraButton();
  JLabel jlCopyVals = new JLabel();

  jpDevIznos jpDevI = new jpDevIznos();

  public jpIzvodiDetail(frmIzvodi md) {
    try {
      fIzvodi = md;
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    kcGroup = new raKontoCorgGroup(fIzvodi.raDetail) {
      public void setScrKonto(String brKonta) {
        super.setScrKonto(brKonta);
        fIzvodi.setScrKonto(brKonta);
      }

      public void afterAfterLookupKonto() {
        //        jlKontoConcat.setText(kcGroup.getJlrBROJKONTA().getText());
        setKontoConcat(1);
        //       setPanelsVisible(1);
      }
    };
    jpDetail.setLayout(lay);
    lay.setWidth(641);
    lay.setHeight(300);//280
    jlVRDOK.setText("Vrsta dokumenta");
    rcVRDOK.setRaColumn("VRDOK");
    rcVRDOK.setRaDataSet(fIzvodi.getDetailSet());
    rcVRDOK.setRaItems(new String[][] { { "Uplata", "KUP" }, { "K.O.", "KOB" },
        { "Ostalo", "" }, {"Kompenzacija", "KOB"} });
    jlBrojdok.setText("Broj dokumenta");
    jlBrojkonta.setText("Konto");
    jlCorg.setText("Org jedinica");
    jlDatdok.setText("Datum dokumenta");
    jlId.setText("Duguje");
    jlIp.setText("Potražuje");
    jlOpis.setText("Opis");
    jlZr.setText("Partner");
    jlaCpar.setHorizontalAlignment(SwingConstants.LEFT);
    jlaCpar.setText("Oznaka");
    jlaNazpar.setHorizontalAlignment(SwingConstants.LEFT);
    jlaNazpar.setText("Naziv");
    jlaZr.setHorizontalAlignment(SwingConstants.LEFT);
    jlaZr.setText("Žiro ra\u010Dun");
    jraBrojdok.setColumnName("BROJDOK");
    jraBrojdok.setDataSet(fIzvodi.getDetailSet());
    jraDatdok.setColumnName("DATDOK");
    jraDatdok.setDataSet(fIzvodi.getDetailSet());
    jraExtbrojdok.setColumnName("EXTBRDOK");
    jraExtbrojdok.setDataSet(fIzvodi.getDetailSet());
    jraId.setColumnName("ID");
    jraId.setDataSet(fIzvodi.getDetailSet());
    jraIp.setColumnName("IP");
    jraIp.setDataSet(fIzvodi.getDetailSet());
    jraPvid.setColumnName("DEVID");
    jraPvid.setDataSet(fIzvodi.getDetailSet());
    jraPvip.setColumnName("DEVIP");
    jraPvip.setDataSet(fIzvodi.getDetailSet());
    jraOpis.setColumnName("OPIS");
    jraOpis.setDataSet(fIzvodi.getDetailSet());
    jraTecaj.setColumnName("TECAJ");
    jraTecaj.setDataSet(fIzvodi.getDetailSet());
    jlaTecaj.setText("Te\u010Daj");
    jlrCpar.setColumnName("CPAR");
    jlrCpar.setDataSet(fIzvodi.getDetailSet());
    jlrCpar.setColNames(new String[] { fIzvodi.getZiroColumn(), "NAZPAR" });
    jlrCpar.setTextFields(new JTextComponent[] { jlrZr, jlrNazpar });
    jlrCpar.setVisCols(fIzvodi.getParVisCols());
    
    jlrCpar.setSearchMode(0);
    jlrCpar.setRaDataSet(fIzvodi.getParIzvodSet());
    jlrCpar.setNavButton(jbSelZr);
    jpDevI.setDataSet(fIzvodi.getDetailSet());
    jlrZr.setColumnName(fIzvodi.getZiroColumn());
    jlrZr.setNavProperties(jlrCpar);
    jlrZr.setSearchMode(lookupData.EXACT, lookupData.TEXTAW);
    jlrNazpar.setColumnName("NAZPAR");
    jlrNazpar.setNavProperties(jlrCpar);
    jlrNazpar.setSearchMode(1);
    panel1.setBackground(Color.black);
    panel2.setBackground(Color.black);
    jlD.setFont(new java.awt.Font("Dialog", 1, 14));
    jlD.setText("D");
    jlP.setFont(new java.awt.Font("Dialog", 1, 14));
    jlP.setText("P");
    jlKontoConcat.setFont(new java.awt.Font("Dialog", 1, 12));
    jlKontoConcat.setHorizontalAlignment(SwingConstants.CENTER);
    jtVer1.setBackground(Color.black);
    jtVer1.setEnabled(false);
    jtVer1.setBorder(BorderFactory.createLineBorder(Color.black));
    jtVer1.setEditable(false);
    jtHor1.setBackground(Color.black);
    jtHor1.setEnabled(false);
    jtHor1.setBorder(BorderFactory.createLineBorder(Color.black));
    jtHor1.setEditable(false);
    
    jbCopyVals.setIcon(raImages.getImageIcon(raImages.IMGCOPYCURR));
    jbCopyVals.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fIzvodi.copyPrevious();
      }
    });
    jlCopyVals.setText("Kopiraj vrijednosti s prethodne stavke");
    jlCopyVals.setHorizontalAlignment(SwingConstants.TRAILING);
    //    jTextField1.setText("jTextField1");
    //    jTextField2.setText("jTextField2");
    jpDetail.add(jlVRDOK, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(rcVRDOK, new XYConstraints(150, 20, 140, -1));
    //jpDetail.add(jlCopyVals, new XYConstraints(295, 20, 300, -1));
    jpDetail.add(jbCopyVals, new XYConstraints(295, 20, 21, 21));
    jpDetail.add(jbSelZr, new XYConstraints(605, 60, 21, 21));
    jpDetail.add(jlBrojdok, new XYConstraints(15, 85, -1, -1));
    jpDetail.add(jlBrojkonta, new XYConstraints(15, 160, -1, -1));
    jpDetail.add(jlCorg, new XYConstraints(15, 185, -1, -1));
    jpDetail.add(jlDatdok, new XYConstraints(15, 110, -1, -1));
    jpDetail.add(jlOpis, new XYConstraints(15, 135, -1, -1));
    
    jpDetail.add(jlZr, new XYConstraints(15, 60, -1, -1));
    if (izvunospar.equals("NSZ")) {
	    jpDetail.add(jlaCpar, new XYConstraints(376, 43, 73, -1));
	    jpDetail.add(jlaZr, new XYConstraints(456, 43, 223, -1));
	    jpDetail.add(jlaNazpar, new XYConstraints(151, 43, 138, -1));
	    jpDetail.add(jlrNazpar, new XYConstraints(150, 60, 220, -1));      
	    jpDetail.add(jlrCpar, new XYConstraints(375, 60, 75, -1));
	    jpDetail.add(jlrZr, new XYConstraints(455, 60, 145, -1));
    } else {//ZSN
	    jpDetail.add(jlaCpar, new XYConstraints(296, 43, 73, -1));
	    jpDetail.add(jlaNazpar, new XYConstraints(376, 43, 223, -1));
	    jpDetail.add(jlaZr, new XYConstraints(151, 43, 138, -1));
	    jpDetail.add(jlrCpar, new XYConstraints(295, 60, 75, -1));
	    jpDetail.add(jlrNazpar, new XYConstraints(375, 60, 225, -1));
	    jpDetail.add(jlrZr, new XYConstraints(150, 60, 140, -1));      
    }
    jpDetail.add(jraBrojdok, new XYConstraints(150, 85, 220, -1));
    jpDetail.add(jraDatdok, new XYConstraints(150, 110, 100, -1));
    jpDetail.add(jraExtbrojdok, new XYConstraints(375, 85, 225, -1));
    jpDetail.add(new JLabel("Iznos"), new XYConstraints(15, 240, -1, -1));
    jpDetail.add(jraId, new XYConstraints(190, 240, 140, -1));
    jpDetail.add(jraIp, new XYConstraints(420, 240, 140, -1));
    jpDetail.add(jlaTecaj, new XYConstraints(390, 110, -1, -1));
    jpDetail.add(jraTecaj, new XYConstraints(480, 110, 120, -1));
    jpDetail.add(jraOpis, new XYConstraints(150, 135, 450, -1));
    //    jpDetail.add(panel1, new XYConstraints(150, 232, 450, 2));
    //    jpDetail.add(panel2, new XYConstraints(374, 232, 2, 28));
    jpDetail.add(jlD, new XYConstraints(150, 212, -1, -1));
    jpDetail.add(jlP, new XYConstraints(591, 212, -1, -1));
    jpDetail.add(jlKontoConcat, new XYConstraints(250, 215, 250, -1));
    jpDetail.add(jtVer1, new XYConstraints(150, 231, 450, 2));
    jpDetail.add(jtHor1, new XYConstraints(374, 231, 2, 32));
    jpDetail.add(kcGroup.getJbGetKonto(), new XYConstraints(605, 160, 21, 21));
    jpDetail.add(kcGroup.getJbGetCorg(), new XYConstraints(605, 185, 21, 21));
    //    jpDetail.add(jlId, new XYConstraints(15, 210, -1, -1));
    //    jpDetail.add(jlIp, new XYConstraints(390, 210, -1, -1));
    jpDetail.add(kcGroup.getJlrBROJKONTA(),
        new XYConstraints(150, 160, 100, -1));
    jpDetail.add(kcGroup.getJlrCORG(), new XYConstraints(150, 185, 100, -1));
    jpDetail
        .add(kcGroup.getJlrNAZIVORG(), new XYConstraints(255, 185, 345, -1));
    jpDetail.add(kcGroup.getJlrNAZIVKONTA(), new XYConstraints(255, 160, 345,
        -1));
    jpDetail.add(jpDevI, new XYConstraints(0, 270, -1, -1));
    
    jpDev.add(new JLabel("Iznos u kunama"), new XYConstraints(15, 0, -1, -1));
    jpDev.add(jraPvid, new XYConstraints(190, 0, 140, -1));
    jpDev.add(jraPvip, new XYConstraints(420, 0, 140, -1));
    
    jpDetail.add(jpDev, new XYConstraints(0, 270, -1, -1));
    
    this.add(jpDetail, BorderLayout.CENTER);
  }

  public void setPanelsVisible(int i) {
    if (i == 0) {
      panel1.setVisible(false);
      panel2.setVisible(false);
    } else {
      panel1.setVisible(true);
      panel2.setVisible(true);
    }
  }

  public void setKontoConcat(int i) {
    if (i == 1)
      jlKontoConcat.setText(kcGroup.getJlrBROJKONTA().getText());
    else
      jlKontoConcat.setText("");
  }
  JraTextField getFirstFocusableComponent() {
    if (izvunospar.equals("NSZ")) {
      return jlrNazpar;
    } else {
      return jlrZr;
    }
  }
}