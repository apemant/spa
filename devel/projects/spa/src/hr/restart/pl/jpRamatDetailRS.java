/****license*****************************************************************
**   file: jpRamatDetailRS.java
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

import hr.restart.baza.Radnici;
import hr.restart.baza.Sifrarnici;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

//import java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;
//import javax.swing.text.JTextComponent;
//import com.borland.jbcl.layout.*;
//import com.borland.dx.dataset.*;
//import com.borland.dx.sql.dataset.*;
//import hr.restart.swing.*;
//import hr.restart.baza.*;
//import hr.restart.util.*;


public class jpRamatDetailRS extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.zapod.OrgStr orgStr = hr.restart.zapod.OrgStr.getOrgStr();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmRamatDetailRS fRamatDetailRS;
  JPanel jpDetail = new JPanel();
  JPanel jpForWhom = new JPanel();

  XYLayout lay = new XYLayout();
  XYLayout lax = new XYLayout();
  JLabel jlBruto = new JLabel();
  JLabel jlMio1 = new JLabel();
  JLabel jlMio2 = new JLabel();
  JLabel jlNetopk = new JLabel();
  JLabel jlOsodb = new JLabel();
  JLabel jlPorez = new JLabel();
  JLabel jlPremos = new JLabel();
  JLabel jlPrirez = new JLabel();
  JLabel jlZapos = new JLabel();
  JLabel jlZo = new JLabel();
  JLabel jlUplaceno = new JLabel();
  JLabel jlObracunato = new JLabel();
  JraTextField jraBruto = new JraTextField(){
//    public void focusLost(FocusEvent e) {
//      super.focusLost(e);
//      fRamatDetailRS.getRaQueryDataSet().setBigDecimal("BRUTOMJ",fRamatDetailRS.getRaQueryDataSet().getBigDecimal("BRUTO"));
//      fRamatDetailRS.getRaQueryDataSet().setBigDecimal("ZOMJ",fRamatDetailRS.getRaQueryDataSet().getBigDecimal("BRUTO"));
//      fRamatDetailRS.getRaQueryDataSet().setBigDecimal("ZAPOSMJ",fRamatDetailRS.getRaQueryDataSet().getBigDecimal("BRUTO"));
//    }
  };
  JraTextField jraBrutomj = new JraTextField();
  JraTextField jraMio1 = new JraTextField(){
//    public void focusLost(FocusEvent e) {
//      super.focusLost(e);
//      fRamatDetailRS.getRaQueryDataSet().setBigDecimal("MIO1MJ",fRamatDetailRS.getRaQueryDataSet().getBigDecimal("MIO1"));
//    }
  };
  JraTextField jraMio1mj = new JraTextField();
  JraTextField jraMio2 = new JraTextField(){
//    public void focusLost(FocusEvent e) {
//      super.focusLost(e);
//      fRamatDetailRS.getRaQueryDataSet().setBigDecimal("MIO2MJ",fRamatDetailRS.getRaQueryDataSet().getBigDecimal("MIO2"));
//    }
  };
  JraTextField jraMio2mj = new JraTextField();
  JraTextField jraNetopk = new JraTextField();
  JraTextField jraOsodb = new JraTextField();
  JraTextField jraPorez = new JraTextField(){
//    public void focusLost(FocusEvent e) {
//      super.focusLost(e);
//      fRamatDetailRS.getRaQueryDataSet().setBigDecimal("POREZMJ",fRamatDetailRS.getRaQueryDataSet().getBigDecimal("POREZ"));
//    }
  };
  JraTextField jraPorezmj = new JraTextField();
  JraTextField jraPremos = new JraTextField();
  JraTextField jraPrirez = new JraTextField(){
//    public void focusLost(FocusEvent e) {
//      super.focusLost(e);
//      fRamatDetailRS.getRaQueryDataSet().setBigDecimal("PRIREZMJ",fRamatDetailRS.getRaQueryDataSet().getBigDecimal("PRIREZ"));
//    }
  };
  JraTextField jraPrirezmj = new JraTextField();
  JraTextField jraZapos = new JraTextField();
  JraTextField jraZaposmj = new JraTextField();
  JraTextField jraZo = new JraTextField();
  JraTextField jraZomj = new JraTextField();

  JraTextField jraSati = new JraTextField();

  JraTextField jraOD = new JraTextField();
  JraTextField jraDO = new JraTextField();

  JLabel jlOsnovaOsiguranja = new JLabel();
  JLabel jlInvalidnost = new JLabel();
  JLabel jlZdravstveno = new JLabel();
  JLabel jlStaz = new JLabel();

  JraButton jbSelRadOdnos = new JraButton();
  JraButton jbSelInvalidnost = new JraButton();
  JraButton jbSelZdravstveno = new JraButton();
  JraButton jbSelStaz = new JraButton();
  JLabel jlCradnik = new JLabel();
  JraButton jbSelCradnik = new JraButton();
  JlrNavField jlrCradnik = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrIme = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrPrezime = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivZdravstvenog = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrInvalidnost = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrStaz = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrZdravstveno = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivRadOdn = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivStaz = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivInvalidnosti = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrRadOdnos = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  private JLabel jlOsnovica = new JLabel();
  private JLabel jlSati = new JLabel();
  private JLabel jlOD = new JLabel();
  private JLabel jlDO = new JLabel();

  public jpRamatDetailRS(frmRamatDetailRS f) {
    try {
      fRamatDetailRS = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setLayout(new BorderLayout());
    jpDetail.setLayout(lay);
    jpForWhom.setLayout(lax);
//    lay.setWidth(600);
    lay.setHeight(290);
//    lax.setWidth(600);
//    lax.setHeight(175);

    jlBruto.setText("Bruto iznos");
    jlMio1.setText("Mirovinsko osig. I stup");
    jlMio2.setText("Mirovinsko osig. II stup");
    jlNetopk.setText("Iznos za isplatu");
    jlOsodb.setText("Osobni odbitak");
    jlPorez.setText("Porez na dohodak");
    jlPremos.setText("Premije osiguranja");
    jlPrirez.setText("Prirez porezu na doh.");
    jlZapos.setText("Dopr za zapošljavanj");
    jlZo.setText("Zdravstveno osig.");
    jlUplaceno.setHorizontalAlignment(SwingConstants.CENTER);
    jlUplaceno.setText("Upla\u0107eno");
    jlObracunato.setHorizontalAlignment(SwingConstants.CENTER);
    jlObracunato.setText("Obra\u010Dunano");
    jraBruto.setColumnName("BRUTO");
    jraBruto.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraBrutomj.setColumnName("BRUTOMJ");
    jraBrutomj.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraMio1.setColumnName("MIO1");
    jraMio1.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraMio1mj.setVisible(false);
    jraMio1mj.setColumnName("MIO1MJ");
    jraMio1mj.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraMio2.setColumnName("MIO2");
    jraMio2.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraMio2mj.setVisible(false);
    jraMio2mj.setColumnName("MIO2MJ");
    jraMio2mj.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraNetopk.setColumnName("NETOPK");
    jraNetopk.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraOsodb.setColumnName("OSODB");
    jraOsodb.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraPorez.setColumnName("POREZ");
    jraPorez.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraPorezmj.setVisible(false);
    jraPorezmj.setColumnName("POREZMJ");
    jraPorezmj.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraPremos.setColumnName("PREMOS");
    jraPremos.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraPrirez.setColumnName("PRIREZ");
    jraPrirez.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraPrirezmj.setVisible(false);
    jraPrirezmj.setColumnName("PRIREZMJ");
    jraPrirezmj.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraZapos.setColumnName("ZAPOS");
    jraZapos.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraZaposmj.setColumnName("ZAPOSMJ");
    jraZaposmj.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraZo.setColumnName("ZO");
    jraZo.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jraZomj.setColumnName("ZOMJ");
    jraZomj.setDataSet(fRamatDetailRS.getRaQueryDataSet());

    jlOsnovica.setHorizontalAlignment(SwingConstants.CENTER);
    jlOsnovica.setText("Osnovica");
    jlSati.setText("Sati");
    jlOD.setText("OD:");
    jlDO.setText("DO:");
    jpDetail.add(jlBruto,   new XYConstraints(15, 22, -1, -1));
    jpDetail.add(jlMio1,   new XYConstraints(15, 47, -1, -1));
    jpDetail.add(jlMio2,   new XYConstraints(15, 72, -1, -1));
    jpDetail.add(jlNetopk,   new XYConstraints(15, 247, -1, -1));
    jpDetail.add(jlOsodb,    new XYConstraints(15, 172, -1, -1));
    jpDetail.add(jlPorez,   new XYConstraints(15, 197, -1, -1));
    jpDetail.add(jlPremos,   new XYConstraints(15, 147, -1, -1));
    jpDetail.add(jlPrirez,   new XYConstraints(15, 222, -1, -1));
    jpDetail.add(jlZapos,   new XYConstraints(15, 122, -1, -1));
    jpDetail.add(jlZo,   new XYConstraints(15, 97, -1, -1));
    jpDetail.add(jlUplaceno,  new XYConstraints(151, 3, 98, -1));
    jpDetail.add(jlObracunato,  new XYConstraints(256, 3, 98, -1));
    jpDetail.add(jraBruto,  new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraBrutomj,  new XYConstraints(255, 20, 100, -1));
    jpDetail.add(jraMio1,  new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jraMio1mj, new XYConstraints(255, 65, 100, -1));
    jpDetail.add(jraMio2,  new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jraMio2mj, new XYConstraints(255, 90, 100, -1));
    jpDetail.add(jraNetopk,  new XYConstraints(150, 245, 100, -1));
    jpDetail.add(jraOsodb,  new XYConstraints(150, 170, 100, -1));
    jpDetail.add(jraPorez,  new XYConstraints(150, 195, 100, -1));
    jpDetail.add(jraPorezmj, new XYConstraints(255, 215, 100, -1));
    jpDetail.add(jraPremos,  new XYConstraints(150, 145, 100, -1));
    jpDetail.add(jraPrirez,  new XYConstraints(150, 220, 100, -1));
    jpDetail.add(jraPrirezmj, new XYConstraints(255, 240, 100, -1));
    jpDetail.add(jraZapos,  new XYConstraints(150, 120, 100, -1));
    jpDetail.add(jraZaposmj,  new XYConstraints(255, 120, 100, -1));
    jpDetail.add(jraZo,  new XYConstraints(150, 95, 100, -1));
    jpDetail.add(jraZomj,   new XYConstraints(255, 95, 100, -1));
    jpDetail.add(jlOsnovica,       new XYConstraints(256, 78, 98, -1));

    jlOsnovaOsiguranja.setText("Osnova osiguranja");
    jlInvalidnost.setText("Invalidnost");
    jlZdravstveno.setText("Zdravstveno osig.");
    jlStaz.setText("Staž");
    jlCradnik.setText("Radnik");

    jraSati.setColumnName("SATI");
    jraSati.setDataSet(fRamatDetailRS.getRaQueryDataSet());

    jraOD.setColumnName("ODDANA");
    jraOD.setDataSet(fRamatDetailRS.getRaQueryDataSet());

    jraDO.setColumnName("DODANA");
    jraDO.setDataSet(fRamatDetailRS.getRaQueryDataSet());

    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jlrCradnik.setColNames(new String[] {"IME", "PREZIME"});
    jlrCradnik.setTextFields(new JTextComponent[] {jlrIme, jlrPrezime});
    jlrCradnik.setVisCols(new int[] {0, 1, 2});
    jlrCradnik.setSearchMode(0);
    jlrCradnik.setRaDataSet(Radnici.getDataModule().getTempSet("LOKK='N' and AKTIV='D' and (CORG in " +
        orgStr.getInQuery(orgStr.getOrgstrAndCurrKnjig())+")"));
    jlrCradnik.setNavButton(jbSelCradnik);

    jlrIme.setColumnName("IME");
    jlrIme.setNavProperties(jlrCradnik);
    jlrIme.setSearchMode(1);

    jlrPrezime.setColumnName("PREZIME");
    jlrPrezime.setNavProperties(jlrCradnik);
    jlrPrezime.setSearchMode(1);

    jlrRadOdnos.setColumnName("RSOO");
    jlrRadOdnos.setNavColumnName("CSIF");
    jlrRadOdnos.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jlrRadOdnos.setColNames(new String[] {"NAZIV"});
    jlrRadOdnos.setTextFields(new JTextComponent[] {jlrNazivRadOdn});
    jlrRadOdnos.setVisCols(new int[] {0, 2});
    jlrRadOdnos.setSearchMode(0);
    jlrRadOdnos.setRaDataSet(Sifrarnici.getDataModule().getTempSet("LOKK='N' and AKTIV='D' and VRSTASIF='PLOO'")); // Sifrarnici.getDataModule().getTempSet("vbnv=4234")
    jlrRadOdnos.setNavButton(jbSelRadOdnos);

    jlrNazivRadOdn.setColumnName("NAZIV");
    jlrNazivRadOdn.setNavProperties(jlrRadOdnos);
    jlrNazivRadOdn.setSearchMode(1);

    jlrInvalidnost.setColumnName("RSINV");
    jlrInvalidnost.setNavColumnName("CSIF");
    jlrInvalidnost.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jlrInvalidnost.setColNames(new String[] {"NAZIV"});
    jlrInvalidnost.setTextFields(new JTextComponent[] {jlrNazivInvalidnosti});
    jlrInvalidnost.setVisCols(new int[] {0, 2});
    jlrInvalidnost.setSearchMode(0);
    jlrInvalidnost.setRaDataSet(Sifrarnici.getDataModule().getTempSet("LOKK='N' and AKTIV='D' and VRSTASIF='PLIN'"));
    jlrInvalidnost.setNavButton(jbSelInvalidnost);

    jlrNazivInvalidnosti.setColumnName("NAZIV");
    jlrNazivInvalidnosti.setNavProperties(jlrInvalidnost);
    jlrNazivInvalidnosti.setSearchMode(1);

    jlrStaz.setColumnName("RSB");
    jlrStaz.setNavColumnName("CSIF");
    jlrStaz.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jlrStaz.setColNames(new String[] {"NAZIV"});
    jlrStaz.setTextFields(new JTextComponent[] {jlrNazivStaz});
    jlrStaz.setVisCols(new int[] {0, 2});
    jlrStaz.setSearchMode(0);
    jlrStaz.setRaDataSet(Sifrarnici.getDataModule().getTempSet("LOKK='N' and AKTIV='D' and VRSTASIF='PLBS'"));
    jlrStaz.setNavButton(jbSelStaz);

    jlrNazivStaz.setColumnName("NAZIV");
    jlrNazivStaz.setNavProperties(jlrStaz);
    jlrNazivStaz.setSearchMode(1);

    jlrZdravstveno.setColumnName("RSZ");
    jlrZdravstveno.setNavColumnName("CSIF");
    jlrZdravstveno.setDataSet(fRamatDetailRS.getRaQueryDataSet());
    jlrZdravstveno.setColNames(new String[] {"NAZIV"});
    jlrZdravstveno.setTextFields(new JTextComponent[] {jlrNazivZdravstvenog});
    jlrZdravstveno.setVisCols(new int[] {0, 2});
    jlrZdravstveno.setSearchMode(0);
    jlrZdravstveno.setRaDataSet(Sifrarnici.getDataModule().getTempSet("LOKK='N' and AKTIV='D' and VRSTASIF='PLZZ'"));
    jlrZdravstveno.setNavButton(jbSelZdravstveno);

    jlrNazivZdravstvenog.setColumnName("NAZIV");
    jlrNazivZdravstvenog.setNavProperties(jlrZdravstveno);
    jlrNazivZdravstvenog.setSearchMode(1);


    jpForWhom.add(jlCradnik,  new XYConstraints(15, 27, -1, -1));
    jpForWhom.add(jlrCradnik, new XYConstraints(150, 25, 100, -1));
    jpForWhom.add(jlrIme, new XYConstraints(255, 25, 145, -1));
    jpForWhom.add(jlrPrezime, new XYConstraints(405, 25, 145, -1));
    jpForWhom.add(jbSelCradnik, new XYConstraints(555, 25, 21, 21));

    jpForWhom.add(jbSelRadOdnos,  new XYConstraints(555, 50, 21, 21));
    jpForWhom.add(jbSelInvalidnost,  new XYConstraints(555, 75, 21, 21));
    jpForWhom.add(jbSelZdravstveno,  new XYConstraints(555, 100, 21, 21));
    jpForWhom.add(jbSelStaz,   new XYConstraints(555, 125, 21, 21));
    jpForWhom.add(jlOsnovaOsiguranja,   new XYConstraints(15, 52, -1, -1));
    jpForWhom.add(jlInvalidnost,   new XYConstraints(15, 77, -1, -1));
    jpForWhom.add(jlZdravstveno,   new XYConstraints(15, 102, -1, -1));
    jpForWhom.add(jlStaz,   new XYConstraints(15, 127, -1, -1));
    jpForWhom.add(jlrRadOdnos,  new XYConstraints(150, 50, 100, -1));
    jpForWhom.add(jlrInvalidnost,  new XYConstraints(150, 75, 100, -1));
    jpForWhom.add(jlrZdravstveno,  new XYConstraints(150, 100, 100, -1));
    jpForWhom.add(jlrStaz,       new XYConstraints(150, 125, 100, -1));
    jpForWhom.add(jlrNazivRadOdn,  new XYConstraints(255, 50, 295, -1));
    jpForWhom.add(jlrNazivInvalidnosti,  new XYConstraints(255, 75, 295, -1));
    jpForWhom.add(jlrNazivZdravstvenog,  new XYConstraints(255, 100, 295, -1));
    jpForWhom.add(jlrNazivStaz,   new XYConstraints(255, 125, 295, -1));
    jpForWhom.add(jraSati,  new XYConstraints(150, 150, 75, -1));
    jpForWhom.add(jlSati,   new XYConstraints(15, 152, -1, -1));
    jpForWhom.add(jraOD,  new XYConstraints(255, 150, 25, -1));
    jpForWhom.add(jraDO,    new XYConstraints(315, 150, 25, -1));
    jpForWhom.add(jlOD,    new XYConstraints(230, 152, -1, -1));

    this.add(jpForWhom, BorderLayout.NORTH);
    this.add(jpDetail, BorderLayout.CENTER);
    jpForWhom.add(jlDO,     new XYConstraints(290, 152, -1, -1));
  }

  public void clearFields(){
    jlrCradnik.emptyTextFields();
    jlrInvalidnost.emptyTextFields();
    jlrRadOdnos.emptyTextFields();
    jlrStaz.emptyTextFields();
    jlrZdravstveno.emptyTextFields();
  }
}
