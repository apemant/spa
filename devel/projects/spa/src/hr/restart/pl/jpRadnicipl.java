/****license*****************************************************************
**   file: jpRadnicipl.java
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
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCustomAttribDoh;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpRadnicipl extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  raMatPodaci fRadnicipl;
  Border border1;
  TitledBorder titledBorder1;
  Border border2;
  TitledBorder titledBorder2;
  JPanel jpDetail1 = new JPanel();
  JPanel jpDetail2 = new JPanel();
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  XYLayout xYLayout3 = new XYLayout();
  XYLayout xYLayout4 = new XYLayout();
//  boolean tabStateCh = false;

//******** PANEL 1 **********************************
  JlrNavField jlrNazivro = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivrm = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JLabel jlRsb = new JLabel();
  JLabel jlRsoo = new JLabel();
  JlrNavField jlrNaziv5 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv4 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv3 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv2 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JLabel jlCisplmj = new JLabel();
  JlrNavField jlrNaziv1 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JLabel jlCvro = new JLabel();
  JLabel jlaNazivrm = new JLabel();
  JLabel jlCradnik = new JLabel();
  JLabel jlCopcine = new JLabel();
  JlrNavField jlrRsz = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JraCheckBox jcbAktiv = new JraCheckBox();
  JraCheckBox jcbClanomf = new JraCheckBox();
  JraButton jbSelRsinv = new JraButton();
  JLabel jlCradmj = new JLabel();
  JlrNavField jlrRsoo = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JraButton jbSelCss = new JraButton();
  JlrNavField jlrCvro = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrRsb = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JLabel jlCss = new JLabel();
  JraButton jbSelCradmj = new JraButton();
  JlrNavField jlrNazivop = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JLabel jlRsinv = new JLabel();
  JraButton jbSelCisplmj = new JraButton();
  JlrNavField jlrRsinv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCss = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCradnik = new JlrNavField(){
    public void after_lookUp() {
    }
  };
  JlrNavField jlrRadNaziv = new JlrNavField(){
    public void after_lookUp() {
    }
  };

  JlrNavField jlrRadIme = new JlrNavField(){
    public void after_lookUp() {
    }
  };

  JraButton jbSelCopcine = new JraButton();
  JraButton jbSelRsz = new JraButton();
  JlrNavField jlrCradmj = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCisplmj = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JraButton jbSelRsoo = new JraButton();
  JLabel jlaCradmj = new JLabel();
  JraButton jbSelCvro = new JraButton();
  JLabel jlRsz = new JLabel();
  JraButton jbSelRsb = new JraButton();
  JlrNavField jlrCopcine = new JlrNavField() {
    public void after_lookUp() {
    }
  };
//******************************************************
//******** PANEL 1 **********************************
  JLabel jlBrobveze = new JLabel();
  JLabel jlOIB = new JLabel();
  JLabel jlBrosigzo = new JLabel();
  JLabel jlBrradknj = new JLabel();
  JLabel jlBrutdod = new JLabel();
  JLabel jlBrutmr = new JLabel();
  JLabel jlBrutosn = new JLabel();
  JLabel jlBrutuk = new JLabel();
  JLabel jlClanomf = new JLabel();
  JLabel jlDatdol = new JLabel();
  JLabel jlDatodl = new JLabel();
  JLabel jlDatpodstaz = new JLabel();
  JLabel jlDatregres = new JLabel();
  JLabel jlDatstaz = new JLabel();
  JLabel jlGodstaz = new JLabel();
  JLabel jlJmbg = new JLabel();
  JLabel jlKoef = new JLabel();
  JLabel jlKoefzar = new JLabel();
  JLabel jlOlos = new JLabel();
  JLabel jlOluk = new JLabel();
  JLabel jlPodstaz = new JLabel();
  JLabel jlRegbrmio = new JLabel();
  JLabel jlRegbrrk = new JLabel();
  JLabel jlStopastaz = new JLabel();
  JLabel jlZijmbgzo = new JLabel();
  JraTextField jraBrobveze = new JraTextField();
  JraTextField jraBrosigzo = new JraTextField();
  JraTextField jraBrradknj = new JraTextField();
  JraTextField jraBrutdod = new JraTextField() {
    public void valueChanged() {
      jraBrutdod_focusLost(null);
    }
  };
  JraTextField jraBrutmr = new JraTextField();
  JraTextField jraBrutosn = new JraTextField() {
    public void valueChanged() {
      jraBrutosn_focusLost(null);
    }
  };
  JraTextField jraBrutuk = new JraTextField();

  JraTextField jraDatdol = new JraTextField();
  JraTextField jraDatodl = new JraTextField();
  JraTextField jraDatpodstaz = new JraTextField();
  JraTextField jraDatregres = new JraTextField();
  JraTextField jraDatstaz = new JraTextField();
  JraTextField jraGodstaz = new JraTextField() {
    public void valueChanged() {
      jraGodstaz_focusLost(null);
    }
  };
  JraTextField jraJmbg = new JraTextField();
  JraTextField jraOIB = new JraTextField();
  JraTextField jraBrojTek = new JraTextField();
  JraTextField jraKoef = new JraTextField();
  JraTextField jraKoefzar = new JraTextField();
  JraTextField jraOlos = new JraTextField() {
    public void valueChanged() {
      jraOlos_focusLost(null);
    }
  };
  JraTextField jraOluk = new JraTextField();
  JraTextField jraPodstaz = new JraTextField();
  JraTextField jraRegbrmio = new JraTextField();
  JraTextField jraRegbrrk = new JraTextField();
  JraTextField jraStopastaz = new JraTextField();
  JraTextField jraZijmbgzo = new JraTextField();
  JPanel jPanel1 = new JPanel();
  Border border3;
  TitledBorder titledBorder3;
  XYLayout xYLayout1 = new XYLayout();
  JPanel jPanel2 = new JPanel();
  Border border4;
  TitledBorder titledBorder4;
  XYLayout xYLayout2 = new XYLayout();
  JPanel jPanel3 = new JPanel();
  Border border5;
  TitledBorder titledBorder5;
  XYLayout xYLayout5 = new XYLayout();
  raComboBox jcbNacObr = new raComboBox();
  JLabel jlNacObr = new JLabel();

  JraButton jcbSelRad = new JraButton();
  JLabel jlRadnik = new JLabel();
  JLabel jlPrezime = new JLabel();
  JLabel jlRadIme = new JLabel();
  public JraTextField jraAdresa = new JraTextField();
  private JLabel jlAdresa = new JLabel();
  JLabel jlBrojTek = new JLabel();
//**************************************************************


  public jpRadnicipl(raMatPodaci f) {
    try {
      fRadnicipl = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    border1 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder1 = new TitledBorder(border1,"Bruto");
    border2 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder2 = new TitledBorder(border2,"Staž");

    border3 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder3 = new TitledBorder(border3,"Bruto");
    border4 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder4 = new TitledBorder(border4,"Staž");
    border5 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder5 = new TitledBorder(border5,"Brojevi");
    jpDetail1.setLayout(xYLayout3);
    jpDetail2.setLayout(xYLayout4);
//********* panel 1
    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setDataSet(fRadnicipl.getRaQueryDataSet());
    jlCradnik.setText("Mati\u010Dni broj");
    jlCss.setText("Stru\u010Dna sprema");
    jbSelCisplmj.setText("...");
    jbSelCopcine.setText("...");
    jlaCradmj.setText("Oznaka");
    jbSelCvro.setText("...");
    jlRsinv.setText("Invalidnost");
    jlKoef.setText("Koeficijent");
    jlKoefzar.setText("Koeficijent za zaradu");

    jlOlos.setText("Osnovna olakšica");
    jlOluk.setText("Olakšica ukupno");
    jraOlos.setColumnName("OLOS");
    jraOlos.setDataSet(fRadnicipl.getRaQueryDataSet());
    /*jraOlos.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jraOlos_focusLost(e);
      }
    });*/
    jraOluk.setColumnName("OLUK");
    jraOluk.setDataSet(fRadnicipl.getRaQueryDataSet());


    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fRadnicipl.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N");

    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setDataSet(fRadnicipl.getRaQueryDataSet());
    jlrCradnik.setColNames(new String[] {"IME","PREZIME"});
    jlrCradnik.setTextFields(new JTextComponent[] {jlrRadIme, jlrRadNaziv});
    jlrCradnik.setVisCols(new int[] {0,1,2});
    jlrCradnik.setSearchMode(0);
    jlrCradnik.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromCurrentKnjig());
    jlrCradnik.setNavButton(this.jcbSelRad);
    jlrRadNaziv.setSearchMode(1);
    jlrRadNaziv.setNavProperties(jlrCradnik);
    jlrRadNaziv.setColumnName("PREZIME");
    jlrRadIme.setSearchMode(1);
    jlrRadIme.setNavProperties(jlrCradnik);
    jlrRadIme.setColumnName("IME");


    jlRsz.setText("Zdravstveno osiguranje");
    jbSelRsz.setText("...");
    jlrRsz.setColumnName("RSZ");
    jlrRsz.setNavColumnName("CSIF");
    jlrRsz.setDataSet(fRadnicipl.getRaQueryDataSet());
    jlrRsz.setColNames(new String[] {"NAZIV"});
    jlrRsz.setTextFields(new JTextComponent[] {jlrNaziv5});
    jlrRsz.setVisCols(new int[] { 0, 1});
    jlrRsz.setSearchMode(0);
    jlrRsz.setRaDataSet(hr.restart.zapod.Sifrarnici.getSifre("PLZZ"));
    jlrRsz.setNavButton(jbSelRsz);
    jlrNaziv5.setSearchMode(1);
    jlrNaziv5.setNavProperties(jlrRsz);
    jlrNaziv5.setColumnName("NAZIV");

    jbSelRsoo.setText("...");
    jlRsoo.setText("Osnova osiguranja");
    jlrRsoo.setColumnName("RSOO");
    jlrRsoo.setNavColumnName("CSIF");
    jlrRsoo.setDataSet(fRadnicipl.getRaQueryDataSet());
    jlrRsoo.setColNames(new String[] {"NAZIV"});
    jlrRsoo.setTextFields(new JTextComponent[] {jlrNaziv3});
    jlrRsoo.setVisCols(new int[] {0, 1});
    jlrRsoo.setSearchMode(0);
    jlrRsoo.setRaDataSet(hr.restart.zapod.Sifrarnici.getSifre("PLOO"));
    jlrRsoo.setNavButton(jbSelRsoo);
    jlrNaziv3.setSearchMode(1);
    jlrNaziv3.setNavProperties(jlrRsoo);
    jlrNaziv3.setColumnName("NAZIV");

    jlCvro.setText("Vrsta radnog odnosa");
    jlrCvro.setColumnName("CVRO");
    jlrCvro.setDataSet(fRadnicipl.getRaQueryDataSet());
    jlrCvro.setColNames(new String[] {"NAZIVRO"});
    jlrCvro.setTextFields(new JTextComponent[] {jlrNazivro});
    jlrCvro.setVisCols(new int[] {0,1});
    jlrCvro.setSearchMode(0);
    jlrCvro.setRaDataSet(dm.getVrodn());
    jlrCvro.setNavButton(jbSelCvro);
    jlrNazivro.setSearchMode(1);
    jlrNazivro.setNavProperties(jlrCvro);
    jlrNazivro.setColumnName("NAZIVRO");

    jbSelRsb.setText("...");
    jlRsb.setText("Staž sa uve\u0107anim traj.");
    jlrRsb.setColumnName("RSB");
    jlrRsb.setNavColumnName("CSIF");
    jlrRsb.setDataSet(fRadnicipl.getRaQueryDataSet());
    jlrRsb.setColNames(new String[] {"NAZIV"});
    jlrRsb.setTextFields(new JTextComponent[] {jlrNaziv4});
    jlrRsb.setVisCols(new int[] {0, 1});
    jlrRsb.setSearchMode(0);
    jlrRsb.setRaDataSet(hr.restart.zapod.Sifrarnici.getSifre("PLBS"));
    jlrRsb.setNavButton(jbSelRsb);
    jlrNaziv4.setSearchMode(1);
    jlrNaziv4.setNavProperties(jlrRsb);
    jlrNaziv4.setColumnName("NAZIV");

    jbSelRsinv.setText("...");
    jlrRsinv.setColumnName("RSINV");
    jlrRsinv.setNavColumnName("CSIF");
    jlrRsinv.setDataSet(fRadnicipl.getRaQueryDataSet());
    jlrRsinv.setColNames(new String[] {"NAZIV"});
    jlrRsinv.setTextFields(new JTextComponent[] {jlrNaziv2});
    jlrRsinv.setVisCols(new int[] {0, 1});
    jlrRsinv.setSearchMode(0);
    jlrRsinv.setRaDataSet(hr.restart.zapod.Sifrarnici.getSifre("PLIN"));
    jlrRsinv.setNavButton(jbSelRsinv);
    jlrNaziv2.setSearchMode(1);
    jlrNaziv2.setNavProperties(jlrRsinv);
    jlrNaziv2.setColumnName("NAZIV");

    jbSelCss.setText("...");
    jlrCss.setColumnName("CSS");
    jlrCss.setNavColumnName("CSIF");
    jlrCss.setDataSet(fRadnicipl.getRaQueryDataSet());
    jlrCss.setColNames(new String[] {"NAZIV"});
    jlrCss.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCss.setVisCols(new int[] {0, 1});
    jlrCss.setSearchMode(0);
    jlrCss.setRaDataSet(hr.restart.zapod.Sifrarnici.getSifre("PLSS"));
    jlrCss.setNavButton(jbSelCss);
    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCss);
    jlrNaziv.setSearchMode(1);

    jlCisplmj.setText("Isplatno mjesto");
    jlrCisplmj.setColumnName("CISPLMJ");
    jlrCisplmj.setDataSet(fRadnicipl.getRaQueryDataSet());
    jlrCisplmj.setColNames(new String[] {"NAZIV"});
    jlrCisplmj.setTextFields(new JTextComponent[] {jlrNaziv1});
    jlrCisplmj.setVisCols(new int[] {0,1});
    jlrCisplmj.setSearchMode(0);
    jlrCisplmj.setRaDataSet(dm.getIsplMJ());
    jlrCisplmj.setNavButton(jbSelCisplmj);
    jlrNaziv1.setSearchMode(1);
    jlrNaziv1.setNavProperties(jlrCisplmj);
    jlrNaziv1.setColumnName("NAZIV");

    jlCopcine.setText("Op\u0107ina poreza");
    jlrCopcine.setColumnName("COPCINE");
    jlrCopcine.setDataSet(fRadnicipl.getRaQueryDataSet());
    jlrCopcine.setColNames(new String[] {"NAZIVOP"});
    jlrCopcine.setTextFields(new JTextComponent[] {jlrNazivop});
    jlrCopcine.setVisCols(new int[] {0,1});
    jlrCopcine.setSearchMode(0);
    jlrCopcine.setRaDataSet(dm.getOpcine());
    jlrCopcine.setNavButton(jbSelCopcine);
    jlrNazivop.setColumnName("NAZIVOP");
    jlrNazivop.setNavProperties(jlrCopcine);
    jlrNazivop.setSearchMode(1);

    jbSelCradmj.setText("...");
    jlCradmj.setText("Radno mjesto");
    jlrCradmj.setColumnName("CRADMJ");
    jlrCradmj.setDataSet(fRadnicipl.getRaQueryDataSet());
    jlrCradmj.setColNames(new String[] {"NAZIVRM"});
    jlrCradmj.setTextFields(new JTextComponent[] {jlrNazivrm});
    jlrCradmj.setVisCols(new int[] {0,1});
    jlrCradmj.setSearchMode(0);
    jlrCradmj.setRaDataSet(dm.getRadMJ());
    jlrCradmj.setNavButton(jbSelCradmj);
    jlrNazivrm.setSearchMode(1);
    jlrNazivrm.setNavProperties(jlrCradmj);
    jlrNazivrm.setColumnName("NAZIVRM");
    jlaNazivrm.setText("Naziv");
    jlaNazivrm.setHorizontalAlignment(SwingConstants.LEFT);
    jlaCradmj.setHorizontalAlignment(SwingConstants.LEFT);

    jraKoef.setColumnName("KOEF");
    jraKoef.setDataSet(fRadnicipl.getRaQueryDataSet());

    jraAdresa.setColumnName("ADRESA");
    jraAdresa.setDataSet(fRadnicipl.getRaQueryDataSet());

    jraKoefzar.setColumnName("KOEFZAR");
    jraKoefzar.setDataSet(fRadnicipl.getRaQueryDataSet());

    jpDetail1.setMinimumSize(new Dimension(605, 315));
    jpDetail1.setPreferredSize(new Dimension(605, 315));
    xYLayout3.setWidth(605);
    xYLayout3.setHeight(315);
    xYLayout4.setWidth(595);
    xYLayout4.setHeight(800);
    jPanel1.setBorder(titledBorder3);
    jPanel1.setLayout(xYLayout1);
    jPanel2.setBorder(titledBorder4);
    jPanel2.setLayout(xYLayout2);
    jPanel3.setBorder(titledBorder5);
    jPanel3.setLayout(xYLayout5);
    jpDetail2.setMinimumSize(new Dimension(595, 450));
    jpDetail2.setPreferredSize(new Dimension(595, 415));
    /*jraGodstaz.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jraGodstaz_focusLost(e);
      }
    });*/
    jTabbedPane1.setPreferredSize(new Dimension(640, 500));
    jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        jTabbedPane1_stateChanged(e);
      }
    });
    jlNacObr.setText("Na\u010Din obra\u010Duna");
    jcbSelRad.setText("...");
    jlRadnik.setText("Radnik");
    jlPrezime.setText("Prezime");
    /*jraBrutosn.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jraBrutosn_focusLost(e);
      }
    });*/
    /*jraBrutdod.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jraBrutdod_focusLost(e);
      }
    });*/
    jraJmbg.setHorizontalAlignment(SwingConstants.RIGHT);
    jraOIB.setHorizontalAlignment(SwingConstants.RIGHT);
    jraRegbrmio.setHorizontalAlignment(SwingConstants.RIGHT);
    jraZijmbgzo.setHorizontalAlignment(SwingConstants.RIGHT);
    jraBrobveze.setHorizontalAlignment(SwingConstants.RIGHT);
    jraBrradknj.setHorizontalAlignment(SwingConstants.RIGHT);
    jraRegbrrk.setHorizontalAlignment(SwingConstants.RIGHT);
    jraBrosigzo.setHorizontalAlignment(SwingConstants.RIGHT);
    jlRadIme.setText("Ime");
    jlAdresa.setText("Adresa");
    jlBrojTek.setText("Broj ra\u010Duna");
    jraBrojTek.setHorizontalAlignment(SwingConstants.RIGHT);
    jpDetail1.add(jlrNazivro,  new XYConstraints(255, 170, 300, -1));
    jpDetail1.add(jlrNazivrm,  new XYConstraints(255, 120, 300, -1));
    jpDetail1.add(jlRsb,  new XYConstraints(15, 295, -1, -1));
    jpDetail1.add(jlRsoo,  new XYConstraints(15, 270, -1, -1));
    jpDetail1.add(jlrNaziv5,   new XYConstraints(255, 320, 300, -1));
    jpDetail1.add(jlrNaziv4,  new XYConstraints(255, 295, 300, -1));
    jpDetail1.add(jlrNaziv3,  new XYConstraints(255, 270, 300, -1));
    jpDetail1.add(jlrNaziv2,  new XYConstraints(255, 245, 300, -1));
    jpDetail1.add(jlCisplmj,  new XYConstraints(15, 195, -1, -1));
    jpDetail1.add(jlrNaziv1,  new XYConstraints(255, 195, 300, -1));
    jpDetail1.add(jlCvro,  new XYConstraints(15, 170, -1, -1));
    jpDetail1.add(jlaNazivrm,  new XYConstraints(255, 103, 298, -1));
    jpDetail1.add(jlCopcine,  new XYConstraints(15, 220, -1, -1));
    jpDetail1.add(jlrRsz,   new XYConstraints(150, 320, 100, -1));
    jpDetail1.add(jlrNaziv,  new XYConstraints(255, 145, 300, -1));
    jpDetail1.add(jcbAktiv,      new XYConstraints(465, 23, 90, -1));
    jpDetail1.add(jbSelRsinv,  new XYConstraints(560, 245, 21, 21));
    jpDetail1.add(jlCradmj,  new XYConstraints(15, 120, -1, -1));
    jpDetail1.add(jlrRsoo,  new XYConstraints(150, 270, 100, -1));
    jpDetail1.add(jbSelCss,  new XYConstraints(560, 145, 21, 21));
    jpDetail1.add(jlrCvro,  new XYConstraints(150, 170, 100, -1));
    jpDetail1.add(jlrRsb,  new XYConstraints(150, 295, 100, -1));
    jpDetail1.add(jlCss,  new XYConstraints(15, 145, -1, -1));
    jpDetail1.add(jbSelCradmj,  new XYConstraints(560, 120, 21, 21));
    jpDetail1.add(jlrNazivop,  new XYConstraints(255, 220, 300, -1));
    jpDetail1.add(jlRsinv,  new XYConstraints(15, 245, -1, -1));
    jpDetail1.add(jbSelCisplmj,  new XYConstraints(560, 195, 21, 21));
    jpDetail1.add(jlrRsinv,  new XYConstraints(150, 245, 100, -1));
    jpDetail1.add(jlrCss,  new XYConstraints(150, 145, 100, -1));
    jpDetail1.add(jlrCradnik, new XYConstraints(150, 50, 100, -1));
    jpDetail1.add(jbSelCopcine,  new XYConstraints(560, 220, 21, 21));
    jpDetail1.add(jbSelRsz,   new XYConstraints(560, 320, 21, 21));
    jpDetail1.add(jlrCradmj,  new XYConstraints(150, 120, 100, -1));
    jpDetail1.add(jlrCisplmj,  new XYConstraints(150, 195, 100, -1));
    jpDetail1.add(jbSelRsoo,  new XYConstraints(560, 270, 21, 21));
    jpDetail1.add(jlaCradmj,  new XYConstraints(150, 103, 98, -1));
    jpDetail1.add(jbSelCvro,  new XYConstraints(560, 170, 21, 21));
    jpDetail1.add(jlRsz,   new XYConstraints(15, 320, -1, -1));
    jpDetail1.add(jbSelRsb,  new XYConstraints(560, 295, 21, 21));
    jpDetail1.add(jlrCopcine,  new XYConstraints(150, 220, 100, -1));
    jpDetail1.add(jraKoef,  new XYConstraints(150, 355, 100, -1));
    jpDetail1.add(jlKoef,  new XYConstraints(15, 355, -1, -1));
    jpDetail1.add(jlKoefzar,  new XYConstraints(290, 355, -1, -1));
    jpDetail1.add(jraOlos,  new XYConstraints(150, 380, 100, -1));
    jpDetail1.add(jraOluk,  new XYConstraints(455, 380, 100, -1));
    jpDetail1.add(jlOluk,  new XYConstraints(290, 380, -1, -1));
    jpDetail1.add(jlOlos,   new XYConstraints(15, 380, -1, -1));
    jpDetail1.add(jraKoefzar,  new XYConstraints(455, 355, 100, -1));
    jpDetail1.add(jlCradnik,    new XYConstraints(150, 27, -1, -1));
    jpDetail1.add(jlrRadNaziv,   new XYConstraints(385, 50, 170, -1));
    jpDetail1.add(jlrRadIme,   new XYConstraints(255, 50, 125, -1));
    this.add(jTabbedPane1, null);


//********* panel 2
//
    jcbNacObr.setRaColumn("NACOBRB");
    jcbNacObr.setRaDataSet(fRadnicipl.getRaQueryDataSet());
    jcbNacObr.setRaItems(new String[][] {
      {"Definirano po oj", "0"},
      {"Bruto na neto", "1"},
//      {"Koeficijent rm","2"},
//      {"Koeficijent radnika", "3"},
//      {"Akontacija poreza","4"},
//      {"Fiksni bruto", "5"},
      {"Neto na bruto","6"}
//      {"Fiksni neto na bruto", "7"}
    });

    jcbClanomf.setColumnName("CLANOMF");
    jcbClanomf.setDataSet(fRadnicipl.getRaQueryDataSet());
    jcbClanomf.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbClanomf.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbClanomf.setSelectedDataValue("D");
    jcbClanomf.setText("\u010Clan OMF (II stup)");
    jcbClanomf.setUnselectedDataValue("N");


    jlBrobveze.setText("Obveza - zdr. kart.");
    jlOIB.setText("OIB");
    jlBrosigzo.setText("Os. osoba iz zd. kart.");
    jlBrradknj.setText("Radna knjižica");
    jlBrutdod.setText("Dodatni");
    jlBrutmr.setText("Radni staž");
    jlBrutosn.setText("Osnovni");
    jlBrutuk.setText("Ukupni");
    jlDatdol.setText("Datum zapošljavanja");
    jlDatodl.setText("Datum odlaska");
    jlDatpodstaz.setText("Datum postizanja pod.");
    jlDatregres.setText("Datum regresa");
    jlDatstaz.setText("Datum postizanja");
    jlGodstaz.setText("Godine");
    jlJmbg.setText("JMBG");

    jlPodstaz.setText("Godine u poduze\u0107u");
    jlRegbrmio.setText("Rbr. MIO");
    jlRegbrrk.setText("Reg.br.rad. knj.");
    jlStopastaz.setText("Stopa");
    jlZijmbgzo.setText("Znak ispred JMBG-a");
    jraBrobveze.setColumnName("BROBVEZE");
    jraBrobveze.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraBrosigzo.setColumnName("BROSIGZO");
    jraBrosigzo.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraBrradknj.setColumnName("BRRADKNJ");
    jraBrradknj.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraBrutdod.setColumnName("BRUTDOD");
    jraBrutdod.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraBrutmr.setColumnName("BRUTMR");
    jraBrutmr.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraBrutosn.setColumnName("BRUTOSN");
    jraBrutosn.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraBrutuk.setColumnName("BRUTUK");
    jraBrutuk.setDataSet(fRadnicipl.getRaQueryDataSet());

    jraDatdol.setColumnName("DATDOL");
    jraDatdol.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraDatodl.setColumnName("DATODL");
    jraDatodl.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraDatpodstaz.setColumnName("DATPODSTAZ");
    jraDatpodstaz.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraDatregres.setColumnName("DATREGRES");
    jraDatregres.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraDatstaz.setColumnName("DATSTAZ");
    jraDatstaz.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraGodstaz.setColumnName("GODSTAZ");
    jraGodstaz.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraJmbg.setColumnName("JMBG");
    jraJmbg.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraOIB.setColumnName("OIB");
    jraOIB.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraBrojTek.setColumnName("BROJTEK");
    jraBrojTek.setDataSet(fRadnicipl.getRaQueryDataSet());


    jraPodstaz.setColumnName("PODSTAZ");
    jraPodstaz.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraRegbrmio.setColumnName("REGBRMIO");
    jraRegbrmio.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraRegbrrk.setColumnName("REGBRRK");
    jraRegbrrk.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraStopastaz.setColumnName("STOPASTAZ");
    jraStopastaz.setDataSet(fRadnicipl.getRaQueryDataSet());
    jraZijmbgzo.setColumnName("ZIJMBGZO");
    jraZijmbgzo.setDataSet(fRadnicipl.getRaQueryDataSet());

    jPanel1.add(jraBrutosn,     new XYConstraints(150, 25, 100, -1));
    jPanel1.add(jraBrutdod,      new XYConstraints(150, 50, 100, -1));
    jPanel1.add(jlBrutosn,   new XYConstraints(15, 25, -1, -1));
    jPanel1.add(jlBrutmr,     new XYConstraints(310, 25, -1, -1));
    jPanel1.add(jraBrutmr,       new XYConstraints(445, 25, 100, -1));
    jPanel1.add(jraBrutuk,    new XYConstraints(445, 50, 100, -1));
    jPanel1.add(jlBrutuk,     new XYConstraints(310, 50, -1, -1));
    jPanel1.add(jlBrutdod,   new XYConstraints(15, 50, -1, -1));
    jPanel1.add(jcbNacObr,   new XYConstraints(149, 0, 120, -1));
    jPanel1.add(jlNacObr,  new XYConstraints(16, 0, -1, -1));
    jpDetail2.add(jPanel2,                new XYConstraints(15, 15, 575, 107));
    jPanel2.add(jraGodstaz,    new XYConstraints(150, 0, 100, -1));
    jPanel2.add(jlGodstaz,  new XYConstraints(15, 0, -1, -1));
    jPanel2.add(jlStopastaz,     new XYConstraints(310, 0, -1, -1));
    jPanel2.add(jraStopastaz,  new XYConstraints(445, 0, 100, -1));
    jPanel2.add(jlPodstaz,  new XYConstraints(15, 25, -1, -1));
    jPanel2.add(jraPodstaz,    new XYConstraints(150, 25, 100, -1));
    jPanel2.add(jlDatstaz,     new XYConstraints(15, 50, -1, -1));
    jPanel2.add(jraDatstaz,     new XYConstraints(150, 50, 100, -1));
    jPanel2.add(jlDatpodstaz,       new XYConstraints(310, 50, -1, -1));
    jPanel2.add(jraDatpodstaz,       new XYConstraints(445, 50, 100, -1));
    jPanel3.add(jraJmbg,    new XYConstraints(150, 0, 120, -1));
    jPanel3.add(jraBrojTek,    new XYConstraints(425, 25, 120, -1));
    jPanel3.add(jlJmbg,  new XYConstraints(15, 0, -1, -1));
    jPanel3.add(jraBrradknj,     new XYConstraints(425, 0, 120, -1));
    jPanel3.add(jlBrradknj,   new XYConstraints(310, 0, -1, -1));
    jPanel3.add(jlZijmbgzo,   new XYConstraints(15, 25, -1, -1));
    jPanel3.add(jraZijmbgzo,     new XYConstraints(150, 25, 120, -1));
    jPanel3.add(jlRegbrrk,    new XYConstraints(310, 50, -1, -1));
    jPanel3.add(jraRegbrrk,      new XYConstraints(425, 50, 120, -1));
    jPanel3.add(jraRegbrmio,      new XYConstraints(150, 50, 120, -1));
    jPanel3.add(jlRegbrmio,    new XYConstraints(15, 50, -1, -1));
    jPanel3.add(jlBrosigzo,    new XYConstraints(310, 75, -1, -1));
    jPanel3.add(jraBrosigzo,      new XYConstraints(425, 75, 120, -1));
    jPanel3.add(jraBrobveze,        new XYConstraints(150, 75, 120, -1));
    jPanel3.add(jlBrobveze,    new XYConstraints(15, 75, -1, -1));
    jPanel3.add(jlOIB,    new XYConstraints(15, 100, -1, -1));
    jPanel3.add(jraOIB,    new XYConstraints(150, 100, 120, -1));
    jPanel3.add(jlClanomf,   new XYConstraints(250, 75, -1, -1));
    jPanel3.add(jcbClanomf,      new XYConstraints(427, 100, 119, -1));
    jPanel3.add(jlBrojTek,   new XYConstraints(310, 25, -1, -1));
    jpDetail2.add(jPanel1,    new XYConstraints(15, 125, 575, 103));
    jpDetail2.add(jraDatdol,        new XYConstraints(170, 395, 100, -1));
    jpDetail2.add(jraDatodl,         new XYConstraints(465, 395, 100, -1));
    jpDetail2.add(jlDatodl,         new XYConstraints(330, 395, -1, -1));
    jpDetail2.add(jraDatregres,         new XYConstraints(170, 420, 100, -1));
    jpDetail2.add(jlDatregres,       new XYConstraints(15, 420, -1, -1));
    jpDetail2.add(jlDatdol,       new XYConstraints(15, 395, -1, -1));
    jpDetail2.add(jPanel3,        new XYConstraints(15, 230, 575, 157));
    jpDetail1.add(jcbSelRad,   new XYConstraints(560, 50, 21, 21));
    jpDetail1.add(jlRadnik,   new XYConstraints(15, 50, -1, -1));
    jpDetail1.add(jlPrezime,    new XYConstraints(385, 27, -1, -1));
    jpDetail1.add(jlRadIme,    new XYConstraints(255, 27, -1, -1));
    jpDetail1.add(jraAdresa,       new XYConstraints(150, 75, 405, -1));
    jpDetail1.add(jlAdresa,  new XYConstraints(15, 75, -1, -1));

    jTabbedPane1.addTab("Detalji 1", jpDetail1);
    jTabbedPane1.addTab("Detalji 2", jpDetail2);
    if (fRadnicipl instanceof frmRadnicipl) {
      jTabbedPane1.addTab("Ostali podaci", getCustomScroll());
    }
  }
  JraScrollPane getCustomScroll() {
    JraScrollPane jsp = new JraScrollPane(getCustomPanel());
    jsp.setBorder(null);
    return jsp;
  }
  jpCustomAttribDoh jpCAD;
  jpCustomAttribDoh getCustomPanel() {
    if (jpCAD!=null) return jpCAD;
    dM.getDataModule().loadModules();
    jpCAD = new jpCustomAttribDoh();
    jpCAD.setTables("PlZnacRad", "PlZnacRadData");
    jpCAD.setAttrCols("CZNAC", "ZNACOPIS", "ZNACTIP", "ZNACREQ", "ZNACDOH");
    jpCAD.setValueCols("CRADNIK", "VRI");
    jpCAD.setDohvatCols("DOHATTR", "DOHCOLS");
    jpCAD.setLabelWidth(250);
    jpCAD.setFields();
    return jpCAD;
  }
  void jraGodstaz_focusLost(FocusEvent e) {
    BigDecimal ss = new BigDecimal(hr.restart.sisfun.frmParam.getParam("pl", "StopaStaz"));
    int godst = (int)(fRadnicipl.getRaQueryDataSet().getShort("GODSTAZ"));
    fRadnicipl.getRaQueryDataSet().setBigDecimal("STOPASTAZ", ss.multiply(new BigDecimal(godst+"")));
    kalkulacija();

  }

  void jraBrutosn_focusLost(FocusEvent e) {
      kalkulacija();
  }

  void jraBrutdod_focusLost(FocusEvent e) {
   kalkulacija();
  }
  void jraOlos_focusLost(FocusEvent e) {
      fRadnicipl.getRaQueryDataSet().setBigDecimal("OLUK", fRadnicipl.getRaQueryDataSet().getBigDecimal("OLOS"));
  }

  void jTabbedPane1_stateChanged(ChangeEvent e) {
    if (fRadnicipl instanceof frmRadnicipl)
      ((frmRadnicipl)fRadnicipl).tabStChanged();
  }

  void kalkulacija()
  {
    if(fRadnicipl.getRaQueryDataSet().getBigDecimal("STOPASTAZ").compareTo(new BigDecimal(0))>0)
    {
      BigDecimal sec = fRadnicipl.getRaQueryDataSet().getBigDecimal("STOPASTAZ").setScale(8).divide(new BigDecimal(100),
          BigDecimal.ROUND_HALF_UP);
      fRadnicipl.getRaQueryDataSet().setBigDecimal("BRUTMR",
                                   fRadnicipl.getRaQueryDataSet().getBigDecimal("BRUTOSN").multiply(sec));
    }
    fRadnicipl.getRaQueryDataSet().setBigDecimal("BRUTUK",fRadnicipl.getRaQueryDataSet().getBigDecimal("BRUTOSN").add(
                                                   fRadnicipl.getRaQueryDataSet().getBigDecimal("BRUTMR")).add(
                                                   fRadnicipl.getRaQueryDataSet().getBigDecimal("BRUTDOD")));
  }
}
//