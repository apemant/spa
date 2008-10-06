/****license*****************************************************************
**   file: jpValuesRS.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpValuesRS extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmRS fRS;
//  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBruto = new JLabel();
  JLabel jlBrutomj = new JLabel();
  JLabel jlMio1 = new JLabel();
  JLabel jlMio1mj = new JLabel();
  JLabel jlMio2 = new JLabel();
  JLabel jlMio2mj = new JLabel();
  JLabel jlNetopk = new JLabel();
  JLabel jlPorez = new JLabel();
  JLabel jlPorezmj = new JLabel();
  JLabel jlPrirez = new JLabel();
  JLabel jlPrirezmj = new JLabel();
  JLabel jlZapos = new JLabel();
  JLabel jlZaposmj = new JLabel();
  JLabel jlZo = new JLabel();
  JLabel jlZomj = new JLabel();
  JraTextField jraBruto = new JraTextField();
  JraTextField jraBrutomj = new JraTextField();
  JraTextField jraMio1 = new JraTextField();
  JraTextField jraMio1mj = new JraTextField();
  JraTextField jraMio2 = new JraTextField();
  JraTextField jraMio2mj = new JraTextField();
  JraTextField jraNetopk = new JraTextField();
  JraTextField jraPorez = new JraTextField();
  JraTextField jraPorezmj = new JraTextField();
  JraTextField jraPrirez = new JraTextField();
  JraTextField jraPrirezmj = new JraTextField();
  JraTextField jraZapos = new JraTextField();
  JraTextField jraZaposmj = new JraTextField();
  JraTextField jraZo = new JraTextField();
  JraTextField jraZomj = new JraTextField();

  public jpValuesRS(frmRS f) {
    try {
      fRS = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    setLayout(lay);
    lay.setWidth(545);
    lay.setHeight(435);
    setBorder(BorderFactory.createTitledBorder("KONTROLNI PODACI PO OBVEZNIKU PODNOŠENJA PODATAKA"));
    jlBruto.setText("2. Dio iznosa prema kojem se vrši uplata");
    jlBrutomj.setText("1. Obra\u010Dunati mj. iznos (pla\u0107a i dr.)");
    jlMio1.setText("3b.Uplata doprinosa za I.stup MIO");
    jlMio1mj.setText("3a.Mjese\u010Dna obv. doprinosa za I.stup MIO");
    jlMio2.setText("4b.Uplata doprinosa za II.stup MIO");
    jlMio2mj.setText("4a.Mjese\u010Dna obv.doprinosa za II.stup MIO");
    jlNetopk.setText("9. Iznos za isplatu (pla\u0107a i dr.)");
    jlPorez.setText("7b.Uplata poreza na dohodak");
    jlPorezmj.setText("7a.Obveza poreza na dohodak");
    jlPrirez.setText("8b.Uplata prireza porezu na dohodak");
    jlPrirezmj.setText("8a.Obveza prireza porezu na dohodak");
    jlZapos.setText("6b.Uplata doprinosa za zapošljavanje");
    jlZaposmj.setText("6a.Mjese\u010Dna obv.dopr. za zapošljavanje");
    jlZo.setText("5b.Uplata doprinosa za osnovno zdr. osiguranje");
    jlZomj.setText("5a.Mjese\u010Dna obv.dopr. za osnovno zdr. osiguranje");

    add(jlBruto, new XYConstraints(15, 45, -1, -1));
    add(jlBrutomj, new XYConstraints(15, 20, -1, -1));
    add(jlMio1, new XYConstraints(15, 95, -1, -1));
    add(jlMio1mj, new XYConstraints(15, 70, -1, -1));
    add(jlMio2, new XYConstraints(15, 145, -1, -1));
    add(jlMio2mj, new XYConstraints(15, 120, -1, -1));
    add(jlNetopk, new XYConstraints(15, 370, -1, -1));
    add(jlPorez, new XYConstraints(15, 295, -1, -1));
    add(jlPorezmj, new XYConstraints(15, 270, -1, -1));
    add(jlPrirez, new XYConstraints(15, 345, -1, -1));
    add(jlPrirezmj, new XYConstraints(15, 320, -1, -1));
    add(jlZapos, new XYConstraints(15, 245, -1, -1));
    add(jlZaposmj, new XYConstraints(15, 220, -1, -1));
    add(jlZo, new XYConstraints(15, 195, -1, -1));
    add(jlZomj, new XYConstraints(15, 170, -1, -1));

    add(jraBruto,    new XYConstraints(400, 45, 120, -1));
    add(jraBrutomj,  new XYConstraints(400, 20, 120, -1));
    add(jraMio1,  new XYConstraints(400, 95, 120, -1));
    add(jraMio1mj,  new XYConstraints(400, 70, 120, -1));
    add(jraMio2,  new XYConstraints(400, 145, 120, -1));
    add(jraMio2mj,  new XYConstraints(400, 120, 120, -1));
    add(jraNetopk,  new XYConstraints(400, 370, 120, -1));
    add(jraPorez,  new XYConstraints(400, 295, 120, -1));
    add(jraPorezmj,  new XYConstraints(400, 270, 120, -1));
    add(jraPrirez,  new XYConstraints(400, 345, 120, -1));
    add(jraPrirezmj,  new XYConstraints(400, 320, 120, -1));
    add(jraZapos,  new XYConstraints(400, 245, 120, -1));
    add(jraZaposmj,  new XYConstraints(400, 220, 120, -1));
    add(jraZo,  new XYConstraints(400, 195, 120, -1));
    add(jraZomj,  new XYConstraints(400, 170, 120, -1));
  }
  void rebind() {
    jraBruto.setColumnName("BRUTO");
    jraBruto.setDataSet(fRS.getKumulRS());
    jraBrutomj.setColumnName("BRUTOMJ");
    jraBrutomj.setDataSet(fRS.getKumulRS());
    jraMio1.setColumnName("MIO1");
    jraMio1.setDataSet(fRS.getKumulRS());
    jraMio1mj.setColumnName("MIO1MJ");
    jraMio1mj.setDataSet(fRS.getKumulRS());
    jraMio2.setColumnName("MIO2");
    jraMio2.setDataSet(fRS.getKumulRS());
    jraMio2mj.setColumnName("MIO2MJ");
    jraMio2mj.setDataSet(fRS.getKumulRS());
    jraNetopk.setColumnName("NETOPK");
    jraNetopk.setDataSet(fRS.getKumulRS());
    jraPorez.setColumnName("POREZ");
    jraPorez.setDataSet(fRS.getKumulRS());
    jraPorezmj.setColumnName("POREZMJ");
    jraPorezmj.setDataSet(fRS.getKumulRS());
    jraPrirez.setColumnName("PRIREZ");
    jraPrirez.setDataSet(fRS.getKumulRS());
    jraPrirezmj.setColumnName("PRIREZMJ");
    jraPrirezmj.setDataSet(fRS.getKumulRS());
    jraZapos.setColumnName("ZAPOS");
    jraZapos.setDataSet(fRS.getKumulRS());
    jraZaposmj.setColumnName("ZAPOSMJ");
    jraZaposmj.setDataSet(fRS.getKumulRS());
    jraZo.setColumnName("ZO");
    jraZo.setDataSet(fRS.getKumulRS());
    jraZomj.setColumnName("ZOMJ");
    jraZomj.setDataSet(fRS.getKumulRS());
  }
}
