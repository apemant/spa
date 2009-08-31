/****license*****************************************************************
**   file: jpVrstePrim.java
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
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;





public class jpVrstePrim extends JPanel {

  raCommonClass rcc = raCommonClass.getraCommonClass();

  dM dm = dM.getDataModule();

  Valid vl = Valid.getValid();



  frmVrstePrim fVrstePrim;

  JPanel jpDetail = new JPanel();



  XYLayout lay = new XYLayout();

  JLabel jlCobr = new JLabel();

  JLabel jlCosn = new JLabel();

  JLabel jlCsif = new JLabel();

  JLabel jlCsif1 = new JLabel();

  JLabel jlCvrp = new JLabel();

  JLabel jlCvrparh = new JLabel();

  JLabel jlStavka = new JLabel();

  JLabel jlaCvrp = new JLabel();

  JLabel jlaNaziv = new JLabel();

  JLabel jlPovjerioc = new JLabel();

  JraButton jbSelCsif = new JraButton();

  JraButton jbSelCsif1 = new JraButton();

  JraButton jbSelCvrpArh = new JraButton();

  JraButton jbSelStavka = new JraButton();

  JraButton jbSelOsnovica = new JraButton();

  JraButton jbSelObracun = new JraButton();

  JraButton jbSelPov = new JraButton();





  JraCheckBox jcbRegres = new JraCheckBox();

  JraCheckBox jcbRnalog = new JraCheckBox();

  JraTextField jraCvrp = new JraTextField();

  JraTextField jraNaziv = new JraTextField();

  JraTextField jraKoef = new JraTextField();

  JlrNavField jlrCobr = new JlrNavField();

  JlrNavField jlrCosn = new JlrNavField();



  JlrNavField jlrStavka = new JlrNavField()

  {

    public void after_lookUp() {

    }

  };



  JlrNavField jlrStavkaNaz = new JlrNavField()

  {

    public void after_lookUp() {

    }

  };



  JlrNavField jlrCsif1 = new JlrNavField() {

    public void after_lookUp() {

    }

  };

  JlrNavField jlrCsif = new JlrNavField() {

    public void after_lookUp() {

      jcbRnalog.requestFocus();

    }

  };

  JlrNavField jlrNaziv1 = new JlrNavField() {

    public void after_lookUp() {

    }

  };

  JlrNavField jlrNaziv = new JlrNavField() {

    public void after_lookUp() {

//      sysoutTEST ST = new sysoutTEST(false);

//      ST.prn(hr.restart.zapod.Sifrarnici.getSifre("PLOO"));

//      ST.prn(fVrstePrim.getRaQueryDataSet());

    }

  };

  JlrNavField jlrCvrpArhNaz = new JlrNavField() {

    public void after_lookUp() {

    }

  };

  JlrNavField jlrCvrparh = new JlrNavField(){

    public void after_lookUp() {

    }

  };



  JlrNavField jlrPovjerioc = new JlrNavField()

  {

    public void after_lookUp() {

    }

  };



  JlrNavField jlrNazPov = new JlrNavField()

  {

    public void after_lookUp() {

    }

  };



  JPanel jPanel1 = new JPanel();

  XYLayout xYLayout1 = new XYLayout();

  JraCheckBox jcbAktiv = new JraCheckBox();

  JLabel jlKoef = new JLabel();

  public JPanel jPanel2 = new JPanel();

  public Border border1;

  public TitledBorder titledBorder1;

  public JLabel jlParam = new JLabel();

  public JraCheckBox jcDop = new JraCheckBox();

  public XYLayout xYLayout2 = new XYLayout();

  public JraCheckBox jcPor = new JraCheckBox();

  public JraCheckBox jcKred = new JraCheckBox();
  public JraCheckBox jcHar = new JraCheckBox();





  public jpVrstePrim(frmVrstePrim f) {

    try {



      fVrstePrim = f;

      jbInit();

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }



  private void jbInit() throws Exception {

    border1 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));

    titledBorder1 = new TitledBorder(border1,"Obra\u010Dunati");

    jpDetail.setLayout(lay);

    lay.setWidth(605);

    lay.setHeight(385);

    jlrNaziv.setNextFocusableComponent(jcbRnalog);

//    arhivaDS = plUtil.getPlUtil().getArhivaDS();



    jbSelCsif.setText("...");

    jbSelCsif1.setText("...");

    jlCobr.setText("Na\u010Din obra\u010Duna");

    jlCosn.setText("Oznaka osnovice");

    jlCsif.setText("Osnova osiguranja");

    jlCsif1.setText("Grupa primanja");

    jlCvrp.setText("Vrsta primanja");

    jlCvrparh.setText("Dohvat iz arhive");

    jlStavka.setText("Shema knjiženja");

    jlaCvrp.setHorizontalAlignment(SwingConstants.LEFT);

    jlaCvrp.setText("Oznaka");

    jlaNaziv.setHorizontalAlignment(SwingConstants.LEFT);

    jlaNaziv.setText("Naziv");

    jlPovjerioc.setText("Povjerioc");



    jcbRegres.setColumnName("REGRES");

    jcbRegres.setDataSet(fVrstePrim.getRaQueryDataSet());

    jcbRegres.setHorizontalAlignment(SwingConstants.RIGHT);

    jcbRegres.setHorizontalTextPosition(SwingConstants.LEADING);

    jcbRegres.setSelectedDataValue("D");

    jcbRegres.setText("Primanje na godišnjoj osnovi (regres)");

    jcbRegres.setUnselectedDataValue("N");



    jcbRnalog.setColumnName("RNALOG");

    jcbRnalog.setDataSet(fVrstePrim.getRaQueryDataSet());

    jcbRnalog.setHorizontalAlignment(SwingConstants.RIGHT);

    jcbRnalog.setHorizontalTextPosition(SwingConstants.LEADING);

    jcbRnalog.setSelectedDataValue("D");

    jcbRnalog.setText("Unos radnog naloga");

    jcbRnalog.setUnselectedDataValue("N");



    jcbAktiv.setColumnName("AKTIV");

    jcbAktiv.setDataSet(fVrstePrim.getRaQueryDataSet());

    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);

    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);

    jcbAktiv.setSelectedDataValue("D");

    jcbAktiv.setText("Aktivan");

    jcbAktiv.setUnselectedDataValue("N");



    jlrCobr.setColumnName("COBR");

    jlrCobr.setDataSet(fVrstePrim.getRaQueryDataSet());

    jlrCobr.setVisCols(new int[] {0,1});

    jlrCobr.setSearchMode(0);

    jlrCobr.setRaDataSet(dm.getNacobr());

    jlrCobr.setNavButton(jbSelObracun);



    jraKoef.setColumnName("KOEF");

    jraKoef.setDataSet(fVrstePrim.getRaQueryDataSet());



    jlrCosn.setColumnName("COSN");

    jlrCosn.setDataSet(fVrstePrim.getRaQueryDataSet());

    jlrCosn.setVisCols(new int[] {0,1});

    jlrCosn.setSearchMode(0);

    jlrCosn.setRaDataSet(dm.getPlosnovice());

    jlrCosn.setNavButton(jbSelOsnovica);



    jraCvrp.setColumnName("CVRP");

    jraCvrp.setDataSet(fVrstePrim.getRaQueryDataSet());



    jraNaziv.setColumnName("NAZIV");

    jraNaziv.setDataSet(fVrstePrim.getRaQueryDataSet());

    jlrStavka.setColumnName("STAVKA");

    jlrStavka.setDataSet(fVrstePrim.getRaQueryDataSet());



    jlrCsif1.setColumnName("CGRPRIM");

    jlrCsif1.setNavColumnName("CSIF");

    jlrCsif1.setDataSet(fVrstePrim.getRaQueryDataSet());

    jlrCsif1.setColNames(new String[] {"NAZIV"});

    jlrCsif1.setTextFields(new JTextComponent[] {jlrNaziv1});

    jlrCsif1.setVisCols(new int[] {0,1});

    jlrCsif1.setSearchMode(0);

    jlrCsif1.setRaDataSet(hr.restart.zapod.Sifrarnici.getSifre("PLPR"));

    jlrCsif1.setNavButton(jbSelCsif1);



    jlrNaziv1.setColumnName("NAZIV");

    jlrNaziv1.setNavProperties(jlrCsif1);

    jlrNaziv1.setSearchMode(1);



    jlrCsif.setColumnName("RSOO");

    jlrCsif.setNavColumnName("CSIF");

    jlrCsif.setDataSet(fVrstePrim.getRaQueryDataSet());

    jlrCsif.setColNames(new String[] {"NAZIV"});

    jlrCsif.setTextFields(new JTextComponent[] {jlrNaziv});

    jlrCsif.setVisCols(new int[] {0,1});

    jlrCsif.setSearchMode(0);

    jlrCsif.setRaDataSet(hr.restart.zapod.Sifrarnici.getSifre("PLOO"));

    jlrCsif.setNavButton(jbSelCsif);





    jlrNaziv.setColumnName("NAZIV");

    jlrNaziv.setNavProperties(jlrCsif);

    jlrNaziv.setSearchMode(1);



    jlrCvrparh.setColumnName("CVRPARH");

    jlrCvrparh.setNavColumnName("CVRP");

    jlrCvrparh.setDataSet(fVrstePrim.getRaQueryDataSet());

    jlrCvrparh.setColNames(new String[] {"NAZIV"});

    jlrCvrparh.setTextFields(new JTextComponent[] {jlrCvrpArhNaz});

    jlrCvrparh.setVisCols(new int[] {2,3});

    jlrCvrparh.setSearchMode(0);

    jlrCvrparh.setRaDataSet(plUtil.getPlUtil().getArhivaDS());

    jlrCvrparh.setNavButton(jbSelCvrpArh);



    jlrCvrpArhNaz.setColumnName("NAZIV");

    jlrCvrpArhNaz.setNavProperties(jlrCvrparh);

    jlrCvrpArhNaz.setSearchMode(1);



    jlrStavka.setColumnName("STAVKA");

    jlrStavka.setDataSet(fVrstePrim.getRaQueryDataSet());

    jlrStavka.setColNames(new String[] {"OPIS"});

    jlrStavka.setTextFields(new JTextComponent[] {jlrStavkaNaz});

    jlrStavka.setVisCols(new int[] {0,1});

    jlrStavka.setSearchMode(0);

    jlrStavka.setRaDataSet(hr.restart.sisfun.Asql.getShkonta("pl","1","PL"));

    jlrStavka.setNavButton(jbSelStavka);



    jlrPovjerioc.setColumnName("CPOV");

    jlrPovjerioc.setDataSet(fVrstePrim.getRaQueryDataSet());

    jlrPovjerioc.setColNames(new String[] {"NAZPOV"});

    jlrPovjerioc.setTextFields(new JTextComponent[] {jlrNazPov});

    jlrPovjerioc.setVisCols(new int[] {0,1});

    jlrPovjerioc.setSearchMode(0);

    jlrPovjerioc.setRaDataSet(dm.getPovjerioci());

    jlrPovjerioc.setNavButton(jbSelPov);



    jlrNazPov.setColumnName("NAZPOV");

    jlrNazPov.setNavProperties(jlrPovjerioc);

    jlrNazPov.setSearchMode(1);



   // hr.restart.sisfun.Asql.getShkonta("pl","1","PL").open();

   // ST.prn(hr.restart.sisfun.Asql.getShkonta("pl","1","PL"));



    jlrStavkaNaz.setColumnName("OPIS");

    jlrStavkaNaz.setNavProperties(jlrStavka);

    jlrStavkaNaz.setSearchMode(1);



    jPanel1.setBorder(BorderFactory.createEtchedBorder());

    jPanel1.setLayout(xYLayout1);



    jbSelCvrpArh.setText("...");

    jbSelStavka.setText("...");



    jbSelOsnovica.setText("...");

    jbSelObracun.setText("...");

    jbSelPov.setText("...");

    jlKoef.setText("Koeficijent");

    jPanel2.setBorder(titledBorder1);

    jPanel2.setLayout(xYLayout2);

    jlParam.setText("Parametri");

//    jcDop.setHorizontalTextPosition(SwingConstants.LEADING);

    jcDop.setText("Doprinose");

//    jcPor.setHorizontalTextPosition(SwingConstants.LEADING);

    jcPor.setText("Poreze");

//    jcKred.setHorizontalTextPosition(SwingConstants.LEADING);
    
    jcKred.setText("Kredite");

//    jcHar.setHorizontalTextPosition(SwingConstants.TRAILING);

    jcHar.setText("Krizni porez");

    xYLayout2.setWidth(406);

    xYLayout2.setHeight(/*57*/87);

    jpDetail.add(jbSelCsif,    new XYConstraints(560, 90, 21, 21));

    jpDetail.add(jbSelCsif1,    new XYConstraints(560, 165, 21, 21));

    jpDetail.add(jlCobr,   new XYConstraints(15, 65, -1, -1));

    jpDetail.add(jlCsif,    new XYConstraints(15, 90, -1, -1));

    jpDetail.add(jlCsif1,    new XYConstraints(15, 165, -1, -1));

    jpDetail.add(jlCvrp,    new XYConstraints(15, 40, -1, -1));

    jpDetail.add(jlCvrparh,    new XYConstraints(15, 190, -1, -1));

    jpDetail.add(jlaCvrp,       new XYConstraints(151, 23, 98, -1));

    jpDetail.add(jlaNaziv,  new XYConstraints(256, 23, 237, -1));

    jpDetail.add(jlrCsif,    new XYConstraints(150, 90, 100, -1));

    jpDetail.add(jlrCsif1,        new XYConstraints(150, 165, 100, -1));

    jpDetail.add(jlrNaziv,     new XYConstraints(255, 90, 300, -1));

    jpDetail.add(jlrNaziv1,    new XYConstraints(255, 165, 300, -1));

    jpDetail.add(jlrCobr,   new XYConstraints(150, 65, 100, -1));

    jpDetail.add(jbSelObracun,    new XYConstraints(255, 65, 21, 21));

    jpDetail.add(jraCvrp,   new XYConstraints(150, 40, 100, -1));

    jpDetail.add(jlrCvrparh,    new XYConstraints(150, 190, 100, -1));

    jpDetail.add(jraNaziv,   new XYConstraints(255, 40, 300, -1));

    jpDetail.add(jlCosn,   new XYConstraints(322, 65, -1, -1));

    jpDetail.add(jPanel1,          new XYConstraints(150, 117, 406, 42));

    jPanel1.add(jcbRnalog,   new XYConstraints(5, 7, -1, -1));

    jPanel1.add(jcbRegres,  new XYConstraints(161, 7, 224, -1));

    jpDetail.add(jlStavka,     new XYConstraints(15, 215, -1, -1));

    jpDetail.add(jlrStavka,    new XYConstraints(150, 215, 100, -1));

    jpDetail.add(jlrStavkaNaz,    new XYConstraints(255, 215, 300, -1));

    jpDetail.add(jbSelStavka,    new XYConstraints(560, 215, 21, 21));

    jpDetail.add(jlrCvrpArhNaz,      new XYConstraints(255, 190, 300, -1));

    jpDetail.add(jbSelCvrpArh,    new XYConstraints(560, 190, 21, 21));

    jpDetail.add(jcbAktiv,           new XYConstraints(492, 17, -1, -1));

    jpDetail.add(jlrCosn,   new XYConstraints(455, 65, 100, -1));

    jpDetail.add(jbSelOsnovica,    new XYConstraints(560, 65, 21, 21));

    jpDetail.add(jlrPovjerioc,    new XYConstraints(150, 240, 100, -1));

    jpDetail.add(jlrNazPov,    new XYConstraints(255, 240, 300, -1));

    jpDetail.add(jbSelPov,    new XYConstraints(560, 240, 21, 21));

    jpDetail.add(jlPovjerioc,    new XYConstraints(15, 240, -1, -1));

    jpDetail.add(jraKoef,    new XYConstraints(150, 265, 100, -1));

    jpDetail.add(jlKoef,    new XYConstraints(15, 265, -1, -1));

    jpDetail.add(jPanel2,            new XYConstraints(150, 290, 406, 87));

    jpDetail.add(jlParam,  new XYConstraints(15, 290, -1, -1));

    this.add(jpDetail, BorderLayout.CENTER);

    jPanel2.add(jcDop,  new XYConstraints(15, 0, -1, -1));
    jPanel2.add(jcPor,     new XYConstraints(250, 0, -1, -1));
    jPanel2.add(jcKred,        new XYConstraints(15, 25, -1, -1));
    jPanel2.add(jcHar,        new XYConstraints(250, 25, -1, -1));

  }

 }

