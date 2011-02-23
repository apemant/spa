/****license*****************************************************************
**   file: jpOrgpl.java
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
package hr.restart.pl;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpOrgpl extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmOrgpl fOrgpl;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCopcine = new JLabel();
  JLabel jlCorg = new JLabel();
  JLabel jlCsif = new JLabel();
  JLabel jlCsif1 = new JLabel();
  JLabel jlOozo = new JLabel();
  JLabel jlOsnkoef = new JLabel();
  JLabel jlPodmatbr = new JLabel();
  JLabel jlPodruredzo = new JLabel();
  JLabel jlRedbrmio = new JLabel();
  JLabel jlRedbrzo = new JLabel();
  JLabel jlSatimj = new JLabel();
  JLabel jlStopak = new JLabel();
  JraButton jbSelCopcine = new JraButton();
  JraButton jbSelCorg = new JraButton();
  JraButton jbSelCsif = new JraButton();
  JraButton jbSelCsif1 = new JraButton();
  JraCheckBox jcbAktiv = new JraCheckBox();
  String[][] statusItems = new String[][] {
      {"Arhivirana", "X"},
      {"Inicirana", "I"},
      {"Izvršena", "O"}
  };
  raComboBox rcbStatus = new raComboBox() {
    public void this_itemStateChanged() {
      if (!isShowing()) return;
      raParam.setParam(getRaDataSet(),raParam.ORGPL_STATUS,statusItems[getSelectedIndex()][1]);
    }
    public void findCombo() {
      for (int i=0;i<statusItems.length;i++) {
        if (statusItems[i][1].equals(raParam.getParam(getRaDataSet(),raParam.ORGPL_STATUS))) {
          setSelectedIndex(i);
          return;
        }
      }
    }
  };
  JraTextField jraCorg = new JraTextField();
  JraTextField jraOozo = new JraTextField();
  JraTextField jraOsnkoef = new JraTextField();
  JraTextField jraPodmatbr = new JraTextField();
  JraTextField jraPodruredzo = new JraTextField();
  JraTextField jraRedbrmio = new JraTextField();
  JraTextField jraRedbrzo = new JraTextField();
  JraTextField jraSatimj = new JraTextField();
  JraTextField jraStopak = new JraTextField();
  JraTextField jraDatumIspl = new JraTextField();

  JlrNavField jlrCorg = new JlrNavField()
  {
    public void after_lookUp() {
    }
  };

  JlrNavField jlrCorgNaz = new JlrNavField()
  {
    public void after_lookUp() {
    }
  };

  JlrNavField jlrCsif1 = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  JlrNavField jlrNaziv1 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivop = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCopcine = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCsif = new JlrNavField() {
    public void after_lookUp() {
    }
  };
//  raComboBox jcbNacObS = new raComboBox();
  raComboBox jcbNacObB = new raComboBox();
//  JLabel jlNacObrS = new JLabel();
  JLabel jlNacObB = new JLabel();
  JLabel jlOznaka = new JLabel();
  JLabel jlOpis = new JLabel();

  public jpOrgpl(frmOrgpl f) {
    try {
      fOrgpl = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(596);
//    lay.setHeight(285);
    lay.setHeight(310);
    rcbStatus.setRaDataSet(fOrgpl.getRaQueryDataSet());
    rcbStatus.setRaColumn("PARAMETRI");
    rcbStatus.setRaItems(statusItems);
    jbSelCopcine.setText("...");
    jbSelCsif.setText("...");
    jbSelCsif1.setText("...");
    jlCopcine.setText("Op\u0107ina sjedista");
    jlCorg.setText("Org. jedinica");
    jlCsif.setText("Oznaka gr. org. jedinica");
    jlCsif1.setText("Oznaka zdrav. os.");
    jlOozo.setText("Osn. osig. - zdr. kartica");
    jlOsnkoef.setText("Osn. za primjenu koef.");
    jlPodmatbr.setText("Podbr. mat. broju pod.");
    jlPodruredzo.setText("Podr. ured - zdr. kartica");
    jlRedbrmio.setText("Registarski broj MIO");
    jlRedbrzo.setText("Registarski broj ZO");
    jlSatimj.setText("Sati za fiksnu satnicu");
    jlStopak.setText("Stopa akontacije");
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fOrgpl.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan"); /**@todo: Odrediti tekst checkboxa */
    jcbAktiv.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */
    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(fOrgpl.getRaQueryDataSet());
    jraOozo.setColumnName("OOZO");
    jraOozo.setDataSet(fOrgpl.getRaQueryDataSet());
    jraOsnkoef.setColumnName("OSNKOEF");
    jraOsnkoef.setDataSet(fOrgpl.getRaQueryDataSet());
    jraPodmatbr.setColumnName("PODMATBR");
    jraPodmatbr.setDataSet(fOrgpl.getRaQueryDataSet());
    jraPodruredzo.setColumnName("PODRUREDZO");
    jraPodruredzo.setDataSet(fOrgpl.getRaQueryDataSet());
    jraRedbrmio.setColumnName("REGBRMIO");
    jraRedbrmio.setDataSet(fOrgpl.getRaQueryDataSet());
    jraRedbrzo.setColumnName("REGBRZO");
    jraRedbrzo.setDataSet(fOrgpl.getRaQueryDataSet());
    jraSatimj.setColumnName("SATIMJ");
    jraSatimj.setDataSet(fOrgpl.getRaQueryDataSet());
    jraStopak.setColumnName("STOPAK");
    jraStopak.setDataSet(fOrgpl.getRaQueryDataSet());
    jraDatumIspl.setColumnName("DATUMISPL");
    jraDatumIspl.setDataSet(fOrgpl.getRaQueryDataSet());

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(fOrgpl.getRaQueryDataSet());
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrCorgNaz});
    jlrCorg.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(dm.getOrgstruktura());
    jlrCorg.setNavButton(jbSelCorg);

    jlrCorgNaz.setColumnName("NAZIV");
    jlrCorgNaz.setNavProperties(jlrCorg);
    jlrCorgNaz.setSearchMode(1);

    jlrCsif1.setColumnName("RSZ");
    jlrCsif1.setNavColumnName("CSIF");
    jlrCsif1.setDataSet(fOrgpl.getRaQueryDataSet());
    jlrCsif1.setColNames(new String[] {"NAZIV"});
    jlrCsif1.setTextFields(new JTextComponent[] {jlrNaziv1});
    jlrCsif1.setVisCols(new int[] {0, 1});
    jlrCsif1.setSearchMode(0);
    jlrCsif1.setRaDataSet(hr.restart.zapod.Sifrarnici.getSifre("PLZZ"));
    jlrCsif1.setNavButton(jbSelCsif1);

    jlrNaziv1.setColumnName("NAZIV");
    jlrNaziv1.setNavProperties(jlrCsif1);
    jlrNaziv1.setSearchMode(1);

    jlrCopcine.setColumnName("COPCINE");
    jlrCopcine.setDataSet(fOrgpl.getRaQueryDataSet());
    jlrCopcine.setColNames(new String[] {"NAZIVOP"});
    jlrCopcine.setTextFields(new JTextComponent[] {jlrNazivop});
    jlrCopcine.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCopcine.setSearchMode(0);
    jlrCopcine.setRaDataSet(dm.getOpcine());
    jlrCopcine.setNavButton(jbSelCopcine);

    jlrNazivop.setColumnName("NAZIVOP");
    jlrNazivop.setNavProperties(jlrCopcine);
    jlrNazivop.setSearchMode(1);

    jlrCsif.setColumnName("CGRORG");
    jlrCsif.setNavColumnName("CSIF");
    jlrCsif.setDataSet(fOrgpl.getRaQueryDataSet());
    jlrCsif.setColNames(new String[] {"NAZIV"});
    jlrCsif.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCsif.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCsif.setSearchMode(0);
    jlrCsif.setRaDataSet(hr.restart.zapod.Sifrarnici.getSifre("PLOJ"));
    jlrCsif.setNavButton(jbSelCsif);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCsif);
    jlrNaziv.setSearchMode(1);


//    jcbNacObS.setRaColumn("NACOBRS");
//    jcbNacObS.setRaDataSet(fOrgpl.getRaQueryDataSet());
//    jcbNacObS.setRaItems(new String[][] {
//      {"Fiksna pla\u0107a", "1"},
//      {"Fiksna satnica","2"}
//    });

    jcbNacObB.setRaColumn("NACOBRB");
    jcbNacObB.setRaDataSet(fOrgpl.getRaQueryDataSet());
    jcbNacObB.setRaItems(new String[][] {
      {"Bruto na neto", "1"},
//      {"Koeficijent rm","2"},
//      {"Koeficijent radnika", "3"},
//      {"Akontacija poreza","4"},
//      {"Fiksni bruto", "5"},
      {"Neto na bruto","6"}
//      {"Fiksni neto na bruto", "7"}
    });


//    jlNacObrS.setText("Na\u010Din obra\u010Duna satnice");
    jlNacObB.setText("Na\u010Din obra\u010Duna bruta");
    jbSelCorg.setText("...");
    jlOznaka.setText("Oznaka");
    jlOpis.setText("Opis");
    jpDetail.add(jbSelCopcine,  new XYConstraints(560, 65, 21, 21));
    jpDetail.add(jbSelCorg,  new XYConstraints(560, 40, 21, 21));
    jpDetail.add(jbSelCsif,    new XYConstraints(560, 215, 21, 21));
    jpDetail.add(jbSelCsif1,     new XYConstraints(560, 240, 21, 21));
    jpDetail.add(jcbAktiv,  new XYConstraints(465, 13, 90, -1));
    jpDetail.add(jlCopcine,  new XYConstraints(15, 65, -1, -1));
    jpDetail.add(jlCorg,  new XYConstraints(15, 40, -1, -1));
    jpDetail.add(jlCsif,     new XYConstraints(15, 215, -1, -1));
    jpDetail.add(jlCsif1,     new XYConstraints(15, 240, -1, -1));
    jpDetail.add(jlPodruredzo,    new XYConstraints(15, 140, -1, -1));
    jpDetail.add(jlRedbrmio,    new XYConstraints(15, 165, -1, -1));
    jpDetail.add(jlSatimj,   new XYConstraints(15, 115, -1, -1));
    jpDetail.add(jlStopak,     new XYConstraints(300, 190, -1, -1));
    jpDetail.add(jlrCopcine,  new XYConstraints(150, 65, 120, -1));
    jpDetail.add(jlrCsif,    new XYConstraints(150, 215, 120, -1));
    jpDetail.add(jlrNaziv,    new XYConstraints(275, 215, 280, -1));
    jpDetail.add(jlrCorgNaz,    new XYConstraints(275, 40, 280, -1));
    jpDetail.add(jlrNaziv1,     new XYConstraints(275, 240, 280, -1));
    jpDetail.add(jlrNazivop,  new XYConstraints(275, 65, 280, -1));
    jpDetail.add(jlrCorg,  new XYConstraints(150, 40, 120, -1));
    jpDetail.add(jraPodmatbr,     new XYConstraints(150, 190, 120, -1));
    jpDetail.add(jraPodruredzo,     new XYConstraints(150, 140, 120, -1));
    jpDetail.add(jraRedbrmio,     new XYConstraints(150, 165, 120, -1));
    jpDetail.add(jraRedbrzo,     new XYConstraints(435, 165, 120, -1));
    jpDetail.add(jraSatimj,    new XYConstraints(150, 115, 120, -1));
    jpDetail.add(jlOsnkoef,     new XYConstraints(300, 115, -1, -1));
    jpDetail.add(jraOozo,     new XYConstraints(435, 140, 120, -1));
    jpDetail.add(jlOozo,     new XYConstraints(300, 140, -1, -1));
    jpDetail.add(jlRedbrzo,    new XYConstraints(300, 165, -1, -1));
    jpDetail.add(jlPodmatbr,     new XYConstraints(15, 190, -1, -1));
    jpDetail.add(jraStopak,      new XYConstraints(435, 190, 120, -1));
    jpDetail.add(jlrCsif1,     new XYConstraints(150, 240, 120, -1));
//    jpDetail.add(jcbNacObS,        new XYConstraints(150, 90, 120, -1));
    jpDetail.add(jlNacObB,   new XYConstraints(15, 90, -1, -1));
    jpDetail.add(new JLabel("Status obrade"), new XYConstraints(15,265,-1,-1));
    jpDetail.add(rcbStatus, new XYConstraints(150, 265, 120, -1));

//    jpDetail.add(jlNacObB,   new XYConstraints(300, 90, -1, -1));
    jpDetail.add(jcbNacObB,    new XYConstraints(150, 90, 200, -1));
    jpDetail.add(jraOsnkoef,   new XYConstraints(435, 115, 120, -1));
    jpDetail.add(jlOznaka,  new XYConstraints(150, 18, -1, -1));
    jpDetail.add(jlOpis,  new XYConstraints(255, 18, -1, -1));

    jpDetail.add(new JLabel("Datum isplate tek. obr."),    new XYConstraints(275, 265, -1, -1));
    jpDetail.add(jraDatumIspl, new XYConstraints(435, 265, 120, -1));
    this.add(jpDetail, BorderLayout.CENTER);
  }

}
