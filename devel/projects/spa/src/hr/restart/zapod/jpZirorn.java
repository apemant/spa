/****license*****************************************************************
**   file: jpZirorn.java
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
package hr.restart.zapod;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpZirorn extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmZirorn fZirorn;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBrojkonta = new JLabel();
  JLabel jlKkonta = new JLabel();
  JLabel jlCorg = new JLabel();
  JLabel jlCvrnal = new JLabel();
  JLabel jlOznval = new JLabel();
  JLabel jlZiro = new JLabel();
  JraButton jbSelBrojkonta = new JraButton();
  JraButton jbSelKkonta = new JraButton();
  JraButton jbSelCorg = new JraButton();
  JraButton jbSelCvrnal = new JraButton();
  JraButton jbSelOznval = new JraButton();
  JraCheckBox jcbDev = new JraCheckBox();
  JraCheckBox jcbPromet = new JraCheckBox();
  JraTextField jraZiro = new JraTextField();
  JraTextField jraBanka = new JraTextField();
  JraTextField jraIBAN = new JraTextField();
  JraTextField jraSWIFT = new JraTextField();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JlrNavField jlrBrojkonta = new JlrNavField();
  JlrNavField jlrNazivkonta = new JlrNavField();
  JlrNavField jlrKkonta = new JlrNavField();
  JlrNavField jlrNazkkonta = new JlrNavField();
  JlrNavField jlrOznval = new JlrNavField();
  JlrNavField jlrNaziv = new JlrNavField();
  JlrNavField jlrNazval = new JlrNavField();
  JlrNavField jlrCvrnal = new JlrNavField();
  JlrNavField jlrOpisvrnal = new JlrNavField();
  JlrNavField jlrCval = new JlrNavField();
  JlrNavField jlrCorg = new JlrNavField();
  JLabel jlKnjOzn = new JLabel();
  JLabel jlKnjNaziv = new JLabel();
  JLabel jlKontoOzn = new JLabel();
  JLabel jlKontoNaziv = new JLabel();
  JLabel jlVrNalOzn = new JLabel();
  JLabel jlVrNalNaziv = new JLabel();
  JLabel jlBankaNaziv = new JLabel();

  public jpZirorn(frmZirorn f) {
    try {
      fZirorn = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jlKontoOzn.setText("Oznaka");
    jlKontoNaziv.setText("Naziv");
    jlVrNalOzn.setText("Oznaka");
    jlVrNalNaziv.setText("Naziv");

    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(350);

    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fZirorn.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N");

    jbSelBrojkonta.setText("...");
    jbSelCorg.setText("...");
    jbSelCvrnal.setText("...");
    jbSelOznval.setText("...");
    jlBrojkonta.setText("Konto prometa");
    jlKkonta.setText("Konto kompenzacija");
    jlCorg.setText("Knjigovodstvo");
    jlCvrnal.setText("Vrsta naloga");
    jlOznval.setText("Valuta");
    jlZiro.setText("Žiro ra\u010Dun");
    jlBankaNaziv.setText("Ime banke");
    jcbDev.setColumnName("DEV");
    jcbDev.setDataSet(fZirorn.getRaQueryDataSet());
    jcbDev.setSelectedDataValue("D");
    jcbDev.setText("Devizni");
    jcbDev.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */
    jcbPromet.setColumnName("PROMET");
    jcbPromet.setDataSet(fZirorn.getRaQueryDataSet());
    jcbPromet.setSelectedDataValue("D");
    jcbPromet.setText("Kreirati stavku prometa");
    jcbPromet.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */
    jraZiro.setColumnName("ZIRO");
    jraZiro.setDataSet(fZirorn.getRaQueryDataSet());
    jraBanka.setColumnName("BANKA");
    jraBanka.setDataSet(fZirorn.getRaQueryDataSet());
    jraSWIFT.setColumnName("SWIFT");
    jraSWIFT.setDataSet(fZirorn.getRaQueryDataSet());
    jraIBAN.setColumnName("IBAN");
    jraIBAN.setDataSet(fZirorn.getRaQueryDataSet());

    jlrBrojkonta.setColumnName("BROJKONTA");
    jlrBrojkonta.setDataSet(fZirorn.getRaQueryDataSet());
    jlrBrojkonta.setColNames(new String[] {"NAZIVKONTA"});
    jlrBrojkonta.setTextFields(new JTextComponent[] {jlrNazivkonta});
    jlrBrojkonta.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrBrojkonta.setSearchMode(0);
    jlrBrojkonta.setRaDataSet(raKonta.getAnalitickaKonta());
    jlrBrojkonta.setNavButton(jbSelBrojkonta);

    jlrNazivkonta.setColumnName("NAZIVKONTA");
    jlrNazivkonta.setNavProperties(jlrBrojkonta);
    jlrNazivkonta.setSearchMode(1);

    jlrKkonta.setColumnName("KONTOKOMP");
    jlrKkonta.setNavColumnName("BROJKONTA");
    jlrKkonta.setDataSet(fZirorn.getRaQueryDataSet());
    jlrKkonta.setColNames(new String[] {"NAZIVKONTA"});
    jlrKkonta.setTextFields(new JTextComponent[] {jlrNazkkonta});
    jlrKkonta.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrKkonta.setSearchMode(0);
    jlrKkonta.setRaDataSet(raKonta.getAnalitickaKonta());
    jlrKkonta.setNavButton(jbSelKkonta);

    jlrNazkkonta.setColumnName("NAZIVKONTA");
    jlrNazkkonta.setNavProperties(jlrKkonta);
    jlrNazkkonta.setSearchMode(1);

    
    jlrOznval.setColumnName("OZNVAL");
    jlrOznval.setDataSet(fZirorn.getRaQueryDataSet());
    jlrOznval.setColNames(new String[] {"CVAL", "NAZVAL"});
    jlrOznval.setTextFields(new JTextComponent[] {jlrCval, jlrNazval});
    jlrOznval.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrOznval.setSearchMode(0);
    jlrOznval.setRaDataSet(dm.getValute());
    jlrOznval.setNavButton(jbSelOznval);

    jlrCval.setColumnName("CVAL");
    jlrCval.setNavProperties(jlrOznval);
    jlrCval.setSearchMode(0);

    jlrNazval.setColumnName("NAZVAL");
    jlrNazval.setNavProperties(jlrOznval);
    jlrNazval.setSearchMode(1);

    jlrCvrnal.setColumnName("CVRNAL");
    jlrCvrnal.setDataSet(fZirorn.getRaQueryDataSet());
    jlrCvrnal.setColNames(new String[] {"OPISVRNAL"});
    jlrCvrnal.setTextFields(new JTextComponent[] {jlrOpisvrnal});
    jlrCvrnal.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCvrnal.setSearchMode(0);
    jlrCvrnal.setRaDataSet(dm.getVrstenaloga());
    jlrCvrnal.setNavButton(jbSelCvrnal);

    jlrOpisvrnal.setColumnName("OPISVRNAL");
    jlrOpisvrnal.setNavProperties(jlrCvrnal);
    jlrOpisvrnal.setSearchMode(1);

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(fZirorn.getRaQueryDataSet());
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(dm.getKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    jlKnjOzn.setText("Oznaka");
    jlKnjNaziv.setText("Naziv");
    this.add(jpDetail, BorderLayout.CENTER);
    jpDetail.add(jlKontoNaziv,  new XYConstraints(255, 267, -1, -1));
    jpDetail.add(jlVrNalOzn,  new XYConstraints(150, 132, -1, -1));
    jpDetail.add(jlVrNalNaziv,  new XYConstraints(255, 132, -1, -1));
    jpDetail.add(jbSelBrojkonta,  new XYConstraints(555, 285, 21, 21));
    jpDetail.add(jbSelCorg, new XYConstraints(555, 30, 21, 21));
    jpDetail.add(jbSelCvrnal,  new XYConstraints(555, 150, 21, 21));
    jpDetail.add(jbSelOznval,  new XYConstraints(555, 210, 21, 21));
    jpDetail.add(jcbDev,   new XYConstraints(150, 175, -1, -1));
    jpDetail.add(jcbPromet,    new XYConstraints(150, 235, -1, -1));
    jpDetail.add(jlBrojkonta,  new XYConstraints(15, 285, -1, -1));
    jpDetail.add(jlCorg, new XYConstraints(15, 30, -1, -1));
    jpDetail.add(jlCvrnal,  new XYConstraints(15, 160, -1, -1));
    jpDetail.add(jlOznval,  new XYConstraints(15, 210, -1, -1));
    jpDetail.add(jlZiro, new XYConstraints(15, 55, -1, -1));
    jpDetail.add(jlBankaNaziv, new XYConstraints(15, 80, -1, -1));
    jpDetail.add(jlrBrojkonta, new XYConstraints(150, 285, 100, -1));
    jpDetail.add(jlrCorg, new XYConstraints(150, 30, 100, -1));
    jpDetail.add(jlrCval,  new XYConstraints(200, 210, 50, -1));
    jpDetail.add(jlrCvrnal,   new XYConstraints(150, 150, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 30, 295, -1));
    jpDetail.add(jlrNazivkonta,  new XYConstraints(255, 285, 295, -1));
    jpDetail.add(jlrNazval,  new XYConstraints(255, 210, 295, -1));
    jpDetail.add(jlrOpisvrnal,   new XYConstraints(255, 150, 295, -1));
    jpDetail.add(jlrOznval,  new XYConstraints(150, 210, 45, -1));
    jpDetail.add(jraZiro, new XYConstraints(150, 55, 400, -1));
    jpDetail.add(jraBanka, new XYConstraints(150, 80, 400, -1));
    jpDetail.add(new JLabel("IBAN/SWIFT"), new XYConstraints(15, 105, -1, -1));
    jpDetail.add(jraIBAN, new XYConstraints(150, 105, 275, -1));
    jpDetail.add(jraSWIFT, new XYConstraints(430, 105, 120, -1));
    jpDetail.add(jcbAktiv, new XYConstraints(479, 7, 70, -1));
    jpDetail.add(jlKnjOzn, new XYConstraints(150, 12, -1, -1));
    jpDetail.add(jlKnjNaziv, new XYConstraints(255, 12, -1, -1));
    jpDetail.add(jlKontoOzn,  new XYConstraints(150, 267, -1, -1));
    
    jpDetail.add(jlKkonta,  new XYConstraints(15, 310, -1, -1));
    jpDetail.add(jlrKkonta, new XYConstraints(150, 310, 100, -1));
    jpDetail.add(jlrNazkkonta,  new XYConstraints(255, 310, 295, -1));
    jpDetail.add(jbSelKkonta,  new XYConstraints(555, 310, 21, 21));
    
    jcbDev.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fZirorn.handleVal(true);
      }
    });
//
    jcbPromet.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fZirorn.handleKonto();
      }
    });
  }
}
