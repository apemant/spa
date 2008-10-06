/****license*****************************************************************
**   file: jpPrijavaPN.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpPrijavaPN extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  raMatPodaci fPrijavaPN;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlAkontacija = new JLabel();
  JLabel jlCradnik = new JLabel();
  JLabel jlCsif = new JLabel();
  JLabel jlDatumodl = new JLabel();
  JLabel jlMjesta = new JLabel();
  JLabel jlRazlogputa = new JLabel();
  JLabel jlTrajanje = new JLabel();
  JLabel jlIndiPuta = new JLabel();
  JraButton jbSelCradnik = new JraButton();
  JraButton jbSelCsif = new JraButton();
  JraTextField jraAkontacija = new JraTextField();
  JraTextField jraDatumodl = new JraTextField();
  JraTextField jraMjesta = new JraTextField();
  JraTextField jraRazlogputa = new JraTextField();
  JraTextField jraTrajanje = new JraTextField() {
    public void valueChanged() {
      if (fPrijavaPN instanceof frmPrijavaPN)
        ((frmPrijavaPN)fPrijavaPN).akontacija(fPrijavaPN.getRaQueryDataSet().getString("CZEMLJE"), fPrijavaPN.getMode());
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (fPrijavaPN instanceof frmPrijavaPN)
        ((frmPrijavaPN)fPrijavaPN).akontacija(fPrijavaPN.getRaQueryDataSet().getString("CZEMLJE"), fPrijavaPN.getMode());
    }*/
  };
//  JLabel jlCzemlje = new JLabel();
  JraButton jbSelCzemlje = new JraButton();
  JlrNavField jlrNazivzem = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCzemlje = new JlrNavField() {
    public void after_lookUp() {
      if (rcbIndiPuta.getDataValue().equals("I") || fPrijavaPN.getMode() == 'B')
        if (fPrijavaPN instanceof frmPrijavaPN){
          ((frmPrijavaPN)fPrijavaPN).akontacija(jlrCzemlje.getText().trim(), fPrijavaPN.getMode());
        }
    }
  };
  raComboBox rcbIndiPuta = new raComboBox(){
    public void this_itemStateChanged(){
      if (fPrijavaPN instanceof frmPrijavaPN)
        ((frmPrijavaPN)fPrijavaPN).stateIsChanged();
      super.this_itemStateChanged();
    }
  };
  JlrNavField jlrIme = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCradnik = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCsif = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrPrezime = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JraTextField fieldValuta = new JraTextField();/* {
    public void after_lookUp() {
    }
  };//JraTextField();*/
  JLabel jlCorg = new JLabel();
  JraButton jbSelCorg = new JraButton();
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv1 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  public jpPrijavaPN(raMatPodaci f) {
    try {
      fPrijavaPN = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        jlrCradnik.setRaDataSet(presPN.getRadnici());
        jlrIme.setRaDataSet(presPN.getRadnici());
        jlrPrezime.setRaDataSet(presPN.getRadnici());
        jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
        jlrNaziv1.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(250);

    jlCorg.setText("Na teret");
    jlAkontacija.setText("Akontacija");
    jlCradnik.setText("Podaci o djelatniku");
    jlCsif.setText("Prijevozno sredstvo");
    jlDatumodl.setText("Datum odlaska");
    jlMjesta.setText("Mjesta");
    jlRazlogputa.setText("Razlog puta");
    jlTrajanje.setText("Trajanje (dana)");
    jlIndiPuta.setText("Podruèje / Zemlja");

    jraAkontacija.setColumnName("AKONTACIJA");
    jraAkontacija.setDataSet(fPrijavaPN.getRaQueryDataSet());

    jraDatumodl.setColumnName("DATUMODL");
    jraDatumodl.setDataSet(fPrijavaPN.getRaQueryDataSet());
    jraDatumodl.setHorizontalAlignment(SwingConstants.CENTER);

    jraMjesta.setColumnName("MJESTA");
    jraMjesta.setDataSet(fPrijavaPN.getRaQueryDataSet());

    jraRazlogputa.setColumnName("RAZLOGPUTA");
    jraRazlogputa.setDataSet(fPrijavaPN.getRaQueryDataSet());

    jraTrajanje.setColumnName("TRAJANJE");
    jraTrajanje.setDataSet(fPrijavaPN.getRaQueryDataSet());

    rcbIndiPuta.setDataSet(fPrijavaPN.getRaQueryDataSet());
    rcbIndiPuta.setRaColumn("INDPUTA");
    rcbIndiPuta.setRaItems(new String[][] {
      {"Tuzemstvo","Z"},
      {"Inozemstvo","I"},
    });

    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setDataSet(fPrijavaPN.getRaQueryDataSet());
    jlrCradnik.setColNames(new String[] {"IME", "PREZIME"});
    jlrCradnik.setTextFields(new JTextComponent[] {jlrIme, jlrPrezime});
    jlrCradnik.setVisCols(new int[] {0, 1});
    jlrCradnik.setSearchMode(0);
    jlrCradnik.setRaDataSet(presPN.getRadnici());
    jlrCradnik.setNavButton(jbSelCradnik);

    jlrIme.setColumnName("IME");
    jlrIme.setNavProperties(jlrCradnik);
    jlrIme.setSearchMode(1);

    jlrPrezime.setColumnName("PREZIME");
    jlrPrezime.setNavProperties(jlrCradnik);
    jlrPrezime.setSearchMode(1);

    jlrCsif.setColumnName("CPRIJSRED");
    jlrCsif.setNavColumnName("CSIF");
    jlrCsif.setDataSet(fPrijavaPN.getRaQueryDataSet());
    jlrCsif.setColNames(new String[] {"NAZIV"});
    jlrCsif.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCsif.setVisCols(new int[] {2, 4});
    jlrCsif.setSearchMode(0);
    jlrCsif.setRaDataSet(sgStuff.getStugg().prijevoznaSredstvaIzSifrarnika());
    jlrCsif.setNavButton(jbSelCsif);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCsif);
    jlrNaziv.setSearchMode(1);

//    jlCzemlje.setText("Zemlja");

    jlrCzemlje.setColumnName("CZEMLJE");
    jlrCzemlje.setDataSet(fPrijavaPN.getRaQueryDataSet());
    jlrCzemlje.setColNames(new String[] {"NAZIVZEM","OZNVAL"});
    jlrCzemlje.setTextFields(new JTextComponent[] {jlrNazivzem, fieldValuta});
    jlrCzemlje.setVisCols(new int[] {0, 1});
    jlrCzemlje.setSearchMode(0);
    jlrCzemlje.setRaDataSet(dm.getZemlje());
    jlrCzemlje.setNavButton(jbSelCzemlje);

    jlrNazivzem.setColumnName("NAZIVZEM");
    jlrNazivzem.setNavProperties(jlrCzemlje);
    jlrNazivzem.setSearchMode(1);

    fieldValuta.setColumnName("OZNVAL");
//    fieldValuta.setNavProperties(jlrCzemlje);
//    fieldValuta.setSearchMode(1);

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(fPrijavaPN.getRaQueryDataSet());
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv1});
    jlrCorg.setVisCols(new int[] {0, 1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);
    jlrNaziv1.setColumnName("NAZIV");
    jlrNaziv1.setNavProperties(jlrCorg);
    jlrNaziv1.setSearchMode(1);

    rcc.setLabelLaF(fieldValuta,false);

    jpDetail.add(jbSelCradnik, new XYConstraints(555, 20, 21, 21));
    jpDetail.add(jbSelCsif, new XYConstraints(555, 145, 21, 21));
    jpDetail.add(jlAkontacija, new XYConstraints(15, 195, -1, -1));
    jpDetail.add(jlCradnik, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlCsif, new XYConstraints(15, 145, -1, -1));
    jpDetail.add(jlDatumodl, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlMjesta, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlRazlogputa, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlTrajanje, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlIndiPuta, new XYConstraints(15, 170, -1, -1));
    jpDetail.add(jlrCradnik, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrCsif, new XYConstraints(150, 145, 100, -1));
    jpDetail.add(jlrIme, new XYConstraints(255, 20, 145, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 145, 295, -1));
    jpDetail.add(jlrPrezime, new XYConstraints(405, 20, 145, -1));
    jpDetail.add(jraAkontacija, new XYConstraints(150, 195, 100, -1));
    jpDetail.add(jraDatumodl, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jraMjesta, new XYConstraints(150, 95, 400, -1));
    jpDetail.add(jraRazlogputa, new XYConstraints(150, 120, 400, -1));
    jpDetail.add(jraTrajanje,  new XYConstraints(150, 70, 100, -1));
    jpDetail.add(fieldValuta,       new XYConstraints(255, 195, 50, -1));
    jpDetail.add(jbSelCzemlje, new XYConstraints(555, 170, 21, 21));
//    jpDetail.add(jlCzemlje, new XYConstraints(255, 170, -1, -1));
    jpDetail.add(jlrCzemlje,  new XYConstraints(255, 170, 50, -1));
    jpDetail.add(jlrNazivzem,         new XYConstraints(310, 170, 240, -1));
    jpDetail.add(rcbIndiPuta, new XYConstraints(150, 170, 100, -1));
    jpDetail.add(jlCorg, new XYConstraints(15, 220, -1, -1));
    jpDetail.add(jlrCorg,  new XYConstraints(150, 220, 100, -1));
    jpDetail.add(jlrNaziv1, new XYConstraints(255, 220, 295, -1));
    jpDetail.add(jbSelCorg, new XYConstraints(555, 220, 21, 21));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
