/****license*****************************************************************
**   file: jpPovjerioci.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMnemonics;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpPovjerioci extends JPanel {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  boolean f9FlagS = false;
  boolean f9FlagNP= false;
  boolean f9FlagPBO = false;
  boolean f9FlagPBZ = false;
  boolean f9FlagZ = false;

  frmPovjerioci fPovjerioci;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlAdresa = new JLabel();
  JLabel jlCpov = new JLabel();
  JLabel jlMjesto = new JLabel();
  JLabel jlNacisp = new JLabel();
  JLabel jlNazpov = new JLabel();
  JLabel jlPbr = new JLabel();
  JLabel jlPnbo1 = new JLabel();
  JLabel jlPnbz1 = new JLabel();
  JLabel jlSif1 = new JLabel();
  JLabel jlSvrha = new JLabel();
  JLabel jlZiro = new JLabel();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JraTextField jraAdresa = new JraTextField();
  JraTextField jraCpov = new JraTextField();
  JraTextField jraMjesto = new JraTextField();
  JraTextField jraNacisp = new JraTextField();
  JraTextField jraNazpov = new JraTextField();
  JraTextField jraPbr = new JraTextField();
  JraTextField jraPnbo1 = new JraTextField();
  JraTextField jraPnbo2 = new JraTextField();
  JraTextField jraPnbz1 = new JraTextField();
  JraTextField jraPnbz2 = new JraTextField();
  JraTextField jraSif1 = new JraTextField();
  JraTextField jraSif2 = new JraTextField();
  JraTextField jraSif3 = new JraTextField();
  JraTextField jraSvrha = new JraTextField();
  JraTextField jraZiro = new JraTextField();
  raComboBox jrbNacIsp = new raComboBox();
  JLabel jlPovjerioc = new JLabel();

  public jpPovjerioci(frmPovjerioci f) {
    try {
      fPovjerioci = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    jlAdresa.setText("Adresa");
    jlCpov.setText("Oznaka");
    jlMjesto.setText("Mjesto");
    jlNacisp.setText("Na\u010Din ispisa");
    jlNazpov.setText("Naziv");
    jlPbr.setText("Poštanski broj");
    jlPnbo1.setText("Poziv na br. odobrenja");
    jlPnbz1.setText("Poziv na br. zaduženja");
    jlSif1.setText("Šifra");
    jlSvrha.setText("Svrha doznake");
    jlZiro.setText("Žiro ra\u010Dun");
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fPovjerioci.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N");
    jraAdresa.setColumnName("ADRESA");
    jraAdresa.setDataSet(fPovjerioci.getRaQueryDataSet());
    jraCpov.setColumnName("CPOV");
    jraCpov.setDataSet(fPovjerioci.getRaQueryDataSet());
    jraMjesto.setColumnName("MJESTO");
    jraMjesto.setDataSet(fPovjerioci.getRaQueryDataSet());
    jraNacisp.setColumnName("NACISP");
    jraNacisp.setDataSet(fPovjerioci.getRaQueryDataSet());
    jraNazpov.setColumnName("NAZPOV");
    jraNazpov.setDataSet(fPovjerioci.getRaQueryDataSet());
    raMnemonics.createMnemonicLookup(jraNazpov, raVirPlMnWorker.getInstance().getID());
    jraPbr.setColumnName("PBR");
    jraPbr.setDataSet(fPovjerioci.getRaQueryDataSet());
    jraPnbo1.setColumnName("PNBO1");
    jraPnbo1.setDataSet(fPovjerioci.getRaQueryDataSet());
    jraPnbo2.setColumnName("PNBO2");
    jraPnbo2.setDataSet(fPovjerioci.getRaQueryDataSet());
    raMnemonics.createMnemonicLookup(jraPnbo2, raVirPlMnWorker.getInstance().getID());
    jraPnbz1.setColumnName("PNBZ1");
    jraPnbz1.setDataSet(fPovjerioci.getRaQueryDataSet());
    jraPnbz2.setColumnName("PNBZ2");
    jraPnbz2.setDataSet(fPovjerioci.getRaQueryDataSet());
    raMnemonics.createMnemonicLookup(jraPnbz2, raVirPlMnWorker.getInstance().getID());
    jraSif1.setColumnName("SIF1");
    jraSif1.setDataSet(fPovjerioci.getRaQueryDataSet());
    jraSif2.setColumnName("SIF2");
    jraSif2.setDataSet(fPovjerioci.getRaQueryDataSet());
    jraSif3.setColumnName("SIF3");
    jraSif3.setDataSet(fPovjerioci.getRaQueryDataSet());
    jraSvrha.setColumnName("SVRHA");
    jraSvrha.setDataSet(fPovjerioci.getRaQueryDataSet());
    raMnemonics.createMnemonicLookup(jraSvrha, raVirPlMnWorker.getInstance().getID());
    jraZiro.setColumnName("ZIRO");
    jraZiro.setDataSet(fPovjerioci.getRaQueryDataSet());
    raMnemonics.createMnemonicLookup(jraZiro, raVirPlMnWorker.getInstance().getID());
    jrbNacIsp.setRaColumn("NACISP");
    jrbNacIsp.setRaDataSet(fPovjerioci.getRaQueryDataSet());
    jrbNacIsp.setRaItems(new String[][] {
      {"Zbirno","2"},
      {"Pojedina\u010Dno","3"},
      {"Ne ispisati","1"}
    });

    lay.setWidth(570);
    lay.setHeight(285);
    jlPovjerioc.setText("Povjerioc");
    jpDetail.add(jcbAktiv,   new XYConstraints(485, 20, 70, -1));
    jpDetail.add(jlPnbz1,  new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jraCpov,   new XYConstraints(150, 45, 50, -1));
    jpDetail.add(jraNazpov,    new XYConstraints(205, 45, 350, -1));
    jpDetail.add(jraPnbz1,   new XYConstraints(150, 120, 50, -1));
    jpDetail.add(jraAdresa,        new XYConstraints(150, 70, 405, -1));
    jpDetail.add(jlAdresa,   new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jraMjesto,        new XYConstraints(435, 95, 120, -1));
    jpDetail.add(jlMjesto,       new XYConstraints(370, 95, -1, -1));
    jpDetail.add(jlPbr,   new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jraPbr,    new XYConstraints(150, 95, 80, -1));
    jpDetail.add(jraPnbz2,              new XYConstraints(205, 120, 350, -1));
    jpDetail.add(jlPnbo1,   new XYConstraints(15, 145, -1, -1));
    jpDetail.add(jraPnbo1,   new XYConstraints(150, 145, 50, -1));
    jpDetail.add(jraPnbo2,        new XYConstraints(205, 145, 350, -1));
    jpDetail.add(jlSif1,  new XYConstraints(15, 170, -1, -1));
    jpDetail.add(jraSif1,  new XYConstraints(150, 170, 50, -1));
    jpDetail.add(jraSif3,    new XYConstraints(260, 170, 50, -1));
    jpDetail.add(jraSif2,       new XYConstraints(205, 170, 50, -1));
    jpDetail.add(jlNacisp,   new XYConstraints(15, 195, -1, -1));
    jpDetail.add(jlZiro,   new XYConstraints(15, 220, -1, -1));
    jpDetail.add(jraZiro,        new XYConstraints(150, 220, 405, -1));
    jpDetail.add(jlSvrha,   new XYConstraints(15, 245, -1, -1));
    jpDetail.add(jraSvrha,      new XYConstraints(150, 245, 405, -1));
    jpDetail.add(jrbNacIsp,         new XYConstraints(150, 195, 160, -1));
    jpDetail.add(jlNazpov,   new XYConstraints(205, 25, -1, -1));
    jpDetail.add(jlCpov,  new XYConstraints(150, 25, -1, -1));
    jpDetail.add(jlPovjerioc,  new XYConstraints(15, 45, -1, -1));
    this.add(jpDetail, BorderLayout.CENTER);
  }

}
