/****license*****************************************************************
**   file: jpRazlikaPN_V2.java
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
package hr.restart.blpn;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpRazlikaPN_V2 extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  sgStuff ss = sgStuff.getStugg();

  frmRazlikaPN fRazlikaPN;
  frmRazlikaPN_V2 fRazlikaPN_V2;

  JPanel mainPanel = new JPanel();
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  XYLayout layNac = new XYLayout();
  JLabel jlCblag = new JLabel();

  JLabel jlCpn = new JLabel();
  JLabel jlDatum = new JLabel();
  JLabel jlIzdatak = new JLabel();
  JLabel jlaIzdatak = new JLabel();
  JLabel jlaPrimitak = new JLabel();
  JLabel jlaNacinUpIs = new JLabel();
  JraTextField jraDatum = new JraTextField();
  JraTextField jraIzdatak = new JraTextField();
  JraTextField jraPrimitak = new JraTextField();
  JlrNavField jlrCblag = new JlrNavField() {
    public void after_lookUp() {
      fRazlikaPN_V2.updateValutaLabel();
    }
  };
  JlrNavField jlrOznval = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  JlrNavField jlrRbs = new JlrNavField() {
    public void after_lookUp() {
//      fRazlikaPN_V2.calculate();
      fRazlikaPN_V2.clc();
    }
  };
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  JlrNavField jlrValuta = new JlrNavField() {
    public void after_lookUp() {
      /** @todo schangat radataset za jlrRbs */
//      System.out.println("jlrValuta = " + !fRazlikaPN_V2.directIspl.getString("OZNVAL").equals(""));
      if(!fRazlikaPN_V2.directIspl.getString("OZNVAL").equals("")){
        ss.setNeisplaceneStavkeUValPN(fRazlikaPN_V2.fromPres.getString("CPN"), fRazlikaPN_V2.directIspl.getString("OZNVAL"), false);
        jlrRbs.setRaDataSet(ss.getNeisplaceneStavkePNUVal());
//        System.out.println("jlrRbs.getRaDataSet().isEmpty() - " + jlrRbs.getRaDataSet().isEmpty());
//        fRazlikaPN_V2.calculate();
        fRazlikaPN_V2.clc();
      }

    }
  };

  JraButton jbSelStavka = new JraButton();
  JraButton jbSelValuta = new JraButton();

  JraTextField jraCpn = new JraTextField();
  JLabel jlValuta = new JLabel();
  JLabel jlValuta2 = new JLabel();

  JPanel nacini = new JPanel();

  raButtonGroup valOrPv = new raButtonGroup();
  JraCheckBox jcbUValuti = new JraCheckBox();
  JraRadioButton jrbProtuvr = new JraRadioButton();
  raButtonGroup valutno = new raButtonGroup();
  JraRadioButton jrbValBlag = new JraRadioButton();

  JraCheckBox jcbStavka = new JraCheckBox();



  public jpRazlikaPN_V2(frmRazlikaPN_V2 f) {
    try {
      fRazlikaPN_V2 = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {

    jlCblag.setText("Blagajna");

    jrbValBlag.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        jrbValBlag_itemStateChanged(e);
      }
    });
    jrbProtuvr.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        jrbProtuvr_itemStateChanged(e);
      }
    });
    jcbUValuti.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbUValuti_actionPerformed(e);
      }
    });
    jcbStavka.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbStavka_actionPerformed(e);
      }
    });
    valOrPv.add(jrbValBlag, "Sve stavke u valuti blagajne");
    valOrPv.add(jrbProtuvr, "Sve stavke u protuvrijednosti");
    valOrPv.setHorizontalTextPosition(SwingConstants.TRAILING);
    valOrPv.setHorizontalAlignment(SwingConstants.LEFT);

    jcbStavka.setText("Stavka");
    jcbStavka.setHorizontalTextPosition(SwingConstants.TRAILING);
    jcbStavka.setHorizontalAlignment(SwingConstants.LEFT);

    jcbUValuti.setText("i u valuti");
    jcbUValuti.setHorizontalTextPosition(SwingConstants.TRAILING);
    jcbUValuti.setHorizontalAlignment(SwingConstants.LEFT);

    layNac.setWidth(397);
    layNac.setHeight(100);
    nacini.setLayout(layNac);

    nacini.add(jcbUValuti,   new XYConstraints(245, 35, -1, -1));
    nacini.add(jrbProtuvr,    new XYConstraints(10, 35, -1, -1));
    nacini.add(jrbValBlag,   new XYConstraints(10, 10, -1, -1));
    nacini.add(jcbStavka,     new XYConstraints(10, 60, -1, -1));
    nacini.add(jlrRbs,      new XYConstraints(80, 63, 45, -1));
    nacini.add(jlrOpis,      new XYConstraints(130, 63, 230, -1));
    nacini.add(jbSelStavka,    new XYConstraints(365, 63, 21, 21));
    nacini.add(jbSelValuta,   new XYConstraints(365, 38, 21, 21));
    nacini.add(jlrValuta,         new XYConstraints(315, 38, 45, -1));

    nacini.setBorder(BorderFactory.createEtchedBorder());

    jlaNacinUpIs.setText("Na\u010Din uplate - isplate");
    jlCpn.setText("Broj putnog naloga");
    jlDatum.setText("Datum uplate - isplate");
    jlIzdatak.setText("Razlika");
    jlaIzdatak.setHorizontalAlignment(SwingConstants.CENTER);
    jlaIzdatak.setText("Izdatak");
    jlaPrimitak.setHorizontalAlignment(SwingConstants.CENTER);
    jlaPrimitak.setText("Primitak");
    jraDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatum.setColumnName("DATUM");
    jraDatum.setDataSet(fRazlikaPN_V2.getRaQueryDataSet());

    jraIzdatak.setColumnName("IZDATAK");
    jraIzdatak.setDataSet(fRazlikaPN_V2.getRaQueryDataSet());

    jraPrimitak.setColumnName("PRIMITAK");
    jraPrimitak.setDataSet(fRazlikaPN_V2.getRaQueryDataSet());

    jlrCblag.setColumnName("CBLAG");
    jlrCblag.setDataSet(fRazlikaPN_V2.getRaQueryDataSet());
    jlrCblag.setColNames(new String[] {"OZNVAL", "NAZIV"});
    jlrCblag.setTextFields(new JTextComponent[] {jlrOznval, jlrNaziv});
    jlrCblag.setVisCols(new int[] {1, 2, 3});
    jlrCblag.setSearchMode(0);
    jlrCblag.setRaDataSet(frmBlag.getBlagajneKnjig());
    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        jlrCblag.setRaDataSet(frmBlag.getBlagajneKnjig());
        jlrOznval.setRaDataSet(frmBlag.getBlagajneKnjig());
        jlrNaziv.setRaDataSet(frmBlag.getBlagajneKnjig());

      }
    });
    jlrOznval.setColumnName("OZNVAL");
    jlrOznval.setNavProperties(jlrCblag);
    jlrOznval.setSearchMode(1);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCblag);
    jlrNaziv.setSearchMode(1);

    jlrRbs.setColumnName("RBS");
    jlrRbs.setDataSet(fRazlikaPN_V2.getDirectIsplata());
    jlrRbs.setColNames(new String[] {"OPIS"});
    jlrRbs.setTextFields(new JTextComponent[] {jlrOpis});
    jlrRbs.setVisCols(new int[] {8,9,10,12,11});
    jlrRbs.setSearchMode(0);
    jlrRbs.setRaDataSet(ss.getNeisplaceneStavkePN());
    jlrRbs.setNavButton(jbSelStavka);

    jlrOpis.setColumnName("OPIS");
    jlrOpis.setNavProperties(jlrRbs);
    jlrOpis.setSearchMode(1);

    jlrValuta.setColumnName("OZNVAL");
    jlrValuta.setDataSet(fRazlikaPN_V2.getDirectIsplata());
    jlrValuta.setVisCols(new int[] {0,3});
    jlrValuta.setSearchMode(0,1);
    jlrValuta.setRaDataSet(dm.getValute());
    jlrValuta.setNavButton(jbSelValuta);
    jlrValuta.setHorizontalAlignment(SwingConstants.CENTER);

    jraCpn.setHorizontalAlignment(SwingConstants.CENTER);
    jraCpn.setColumnName("CPN");
    jraCpn.setDataSet(fRazlikaPN_V2.getRaQueryDataSet());

    lay.setWidth(591);
    lay.setHeight(210);
    jpDetail.setLayout(lay);

    this.add(jpDetail, BorderLayout.CENTER);

    jpDetail.add(jlCblag, new XYConstraints(15, 37, -1, -1));
    jpDetail.add(jlCpn, new XYConstraints(300, 12, -1, -1));
    jpDetail.add(jlDatum, new XYConstraints(15, 12, -1, -1));
    jpDetail.add(jlIzdatak, new XYConstraints(15, 182, -1, -1));
    jpDetail.add(jlrCblag, new XYConstraints(150, 35, 45, -1));
    jpDetail.add(jraCpn,   new XYConstraints(420, 10, 130, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 35, 295, -1));
    jpDetail.add(jlValuta,    new XYConstraints(255, 182, -1, -1));
    jpDetail.add(jlValuta2,    new XYConstraints(555, 182, -1, -1));
    jpDetail.add(jlrOznval, new XYConstraints(200, 35, 50, -1));
    jpDetail.add(jraDatum, new XYConstraints(150, 10, 100, -1));
    jpDetail.add(jraIzdatak, new XYConstraints(450, 180, 100, -1));
    jpDetail.add(jraPrimitak, new XYConstraints(150, 180, 100, -1));
    jpDetail.add(jlaIzdatak, new XYConstraints(451, 163, 98, -1));
    jpDetail.add(jlaPrimitak, new XYConstraints(151, 163, 98, -1));

    jpDetail.add(jlaNacinUpIs,     new XYConstraints(15, 75, -1, -1));
    jpDetail.add(nacini,     new XYConstraints(150, 60, -1, 100));
  }

  void jrbValBlag_itemStateChanged(ItemEvent e) {
    clearStavke();
    fRazlikaPN_V2.paliGasi();
//    fRazlikaPN_V2.calculate();
//    fRazlikaPN_V2.clc();
    fRazlikaPN_V2.initSets();
  }

  void jrbProtuvr_itemStateChanged(ItemEvent e) {
    clearStavke();
    fRazlikaPN_V2.paliGasi();
//    fRazlikaPN_V2.calculate();
//    fRazlikaPN_V2.clc();
    fRazlikaPN_V2.initSets();
  }

  void jcbUValuti_actionPerformed(ActionEvent e) {
    clearStavke();
    jlrValuta.setText("");
    fRazlikaPN_V2.paliGasi();
    if (!jcbUValuti.isSelected()) /*fRazlikaPN_V2.calculate();*/fRazlikaPN_V2.clc();
  }

  void jcbStavka_actionPerformed(ActionEvent e) {
    clearStavke();
    fRazlikaPN_V2.paliGasi();
    if (!jcbStavka.isSelected()) /*fRazlikaPN_V2.calculate();*/fRazlikaPN_V2.clc();
  }

  void clearStavke(){
    jlrRbs.setText("");
    jlrRbs.emptyTextFields();
  }
}