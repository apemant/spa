/****license*****************************************************************
**   file: jpObracunPNDetail_V1_1.java
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
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpObracunPNDetail_V1_1 extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  sgStuff ss = sgStuff.getStugg();
  Border okvir;
  BorderLayout glavniLayout = new BorderLayout();
  XYLayout lay = new XYLayout();

  frmObracunPN_V1_1 fPN_V1_1;

  public jpObracunPNDetail_V1_1(frmObracunPN_V1_1 md) {
    try {
      fPN_V1_1 = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  JPanel jpDetail01 = new JPanel();
  JPanel jpDetail02 = new JPanel();
  JPanel jpDetail03 = new JPanel();
  JPanel jpDetail04 = new JPanel();

  // jpUpper - BOB
  JPanel jpUpper = new JPanel();
  XYLayout layUpper = new XYLayout();
  JLabel jlDatObr = new JLabel();
  JraTextField jraDatObr = new JraTextField();

  private void setUpper() {
    jpUpper.setLayout(layUpper);
    layUpper.setWidth(650);
    layUpper.setHeight(35);
    jpUpper.setBorder(okvir);
    jlDatObr.setText("Datum obra\u010Duna");
    jraDatObr.setColumnName("DATOBR");

    jraDatObr.setDataSet(fPN_V1_1.getMasterSet());
    jraDatObr.setHorizontalAlignment(SwingConstants.CENTER);

    jpUpper.add(jlDatObr, new XYConstraints(15,5,-1,-1));
    jpUpper.add(jraDatObr, new XYConstraints(150,5,100,-1));
  }
  // jpUpper - EOB

  // jpMidel - BOB
  JPanel jpMidel = new JPanel();
  XYLayout layMidel = new XYLayout();
  JLabel jlCzemlje = new JLabel();
  JlrNavField jlrCzemlje = new JlrNavField() {
    public void after_lookUp() {
      fPN_V1_1.prepareIznQDS(jlrCzemlje.getText().trim());
    }
  };
  JlrNavField jlrNazivzem = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrVal = new JlrNavField(){
    public void after_lookUp() {
      fPN_V1_1.changeUkupnoSet();
    }
  };
  JraButton jbSelCzemlje = new JraButton();
  JraButton jbSelValuta = new JraButton();

  private void setMidel() {
    jpMidel.setLayout(layMidel);
    layMidel.setWidth(650);
    layMidel.setHeight(35);
    jpMidel.setBorder(okvir);
    jlCzemlje.setText("Zemlja i valuta");

    jlrCzemlje.setColumnName("CZEMLJE");
    jlrCzemlje.setDataSet(fPN_V1_1.getDetailSet());
    jlrCzemlje.setColNames(new String[] {"NAZIVZEM"});
    jlrCzemlje.setTextFields(new JTextComponent[] {jlrNazivzem});
    jlrCzemlje.setVisCols(new int[] {0, 1});
    jlrCzemlje.setSearchMode(0);
    jlrCzemlje.setRaDataSet(dm.getZemlje());
    jlrCzemlje.setNavButton(jbSelCzemlje);

    jlrNazivzem.setColumnName("NAZIVZEM");
    jlrNazivzem.setNavProperties(jlrCzemlje);
    jlrNazivzem.setSearchMode(1);

    jlrVal.setColumnName("OZNVAL");
    jlrVal.setDataSet(fPN_V1_1.getDetailSet());
    jlrVal.setColNames(new String[] {"OZNVAL"});
    jlrVal.setVisCols(new int[] {0, 3});
    jlrVal.setSearchMode(0);
    jlrVal.setRaDataSet(dm.getValute());
    jlrVal.setNavButton(jbSelValuta);

    jpMidel.add(jlCzemlje, new XYConstraints(15,5,-1,-1));
    jpMidel.add(jlrCzemlje,    new XYConstraints(150, 5, 45, -1));
    jpMidel.add(jlrNazivzem,          new XYConstraints(200, 5, 249, -1));
    jpMidel.add(jlrVal,             new XYConstraints(485, 5, 65, -1));
    jpMidel.add(jbSelCzemlje, new XYConstraints(455,5,21,21));
    jpMidel.add(jbSelValuta, new XYConstraints(555,5,21,21));
  }
  // jpMidel - EOB

  // jTabed - BOB
  JTabbedPane jTabed = new JTabbedPane();

  private void setTabed(){
    jTabed.addTab("Info", jpDetail00);
    jTabed.addTab("Obra\u010Dun dnevnica", jpDetail01);
    jTabed.addTab("Obra\u010Dun prijevoznih troškova", jpDetail02);
    jTabed.addTab("Obra\u010Dun no\u0107enja", jpDetail03);
    jTabed.addTab("Obra\u010Dun ostalih troškova", jpDetail04);
  }
  // jTabed - EOB

  // jpDetail00 - BOB
  JPanel jpDetail00 = new JPanel();
  JLabel jlDnev = new JLabel();
  JLabel jlPutTr = new JLabel();
  JLabel jlNoc = new JLabel();
  JLabel jlOstalo = new JLabel();
  JLabel jlUkupno = new JLabel();
  JraButton jbGetDnevnice = new JraButton();
  JraButton jbGetPutTrosk = new JraButton();
  JraButton jbGetNocenja = new JraButton();
  JraButton jbGetOstalo = new JraButton();
  JraTextField jraUkupnoDnevnica = new JraTextField();
  JraTextField jraUkupnoPriSred = new JraTextField();
  JraTextField jraUkupnoNocenja = new JraTextField();
  JraTextField jraUkupnoOstalo = new JraTextField();
  JraTextField jraUkupno = new JraTextField();

  private void setDetail00(){
    jpDetail00.setLayout(lay);

    jlDnev.setText("Trošak dnevnica");
    jlPutTr.setText("Trošak prijevoza");
    jlNoc.setText("Trošak no\u0107enja");
    jlOstalo.setText("Ostali troškovi");
    jlUkupno.setText("UKUPNI TROŠKOVI");
    jbGetDnevnice.setText("F5");
    jbGetPutTrosk.setText("F6");
    jbGetNocenja.setText("F7");
    jbGetOstalo.setText("F8");

    jraUkupnoDnevnica.setColumnName("UKUDNEV");
    jraUkupnoDnevnica.setDataSet(fPN_V1_1.ukupnjaci);

    jraUkupnoPriSred.setColumnName("UKUPUTR");
    jraUkupnoPriSred.setDataSet(fPN_V1_1.ukupnjaci);

    jraUkupnoNocenja.setColumnName("UKUNOCE");
    jraUkupnoNocenja.setDataSet(fPN_V1_1.ukupnjaci);

    jraUkupnoOstalo.setColumnName("UKUREST");
    jraUkupnoOstalo.setDataSet(fPN_V1_1.ukupnjaci);

    jraUkupno.setColumnName("UKUSUKU");
    jraUkupno.setDataSet(fPN_V1_1.ukupnjaci);

    jpDetail00.add(jlDnev, new XYConstraints(15, 15, -1, -1));
    jpDetail00.add(jlPutTr, new XYConstraints(15, 65, -1, -1));
    jpDetail00.add(jlNoc,   new XYConstraints(330, 18, -1, -1));
    jpDetail00.add(jlOstalo,   new XYConstraints(330, 65, -1, -1));
    jpDetail00.add(jlUkupno, new XYConstraints(15,130,-1,-1));
    jpDetail00.add(jbGetDnevnice, new XYConstraints(235, 15, 50, 21));
    jpDetail00.add(jbGetPutTrosk, new XYConstraints(235,65,50,21));
    jpDetail00.add(jbGetNocenja, new XYConstraints(535,15,50,21));
    jpDetail00.add(jbGetOstalo, new XYConstraints(535,65,50,21));
    jpDetail00.add(jraUkupnoDnevnica,  new XYConstraints(150, 15, 80, -1));
    jpDetail00.add(jraUkupnoPriSred,  new XYConstraints(150, 65, 80, -1));
    jpDetail00.add(jraUkupnoNocenja,  new XYConstraints(450, 15, 80, -1));
    jpDetail00.add(jraUkupnoOstalo,  new XYConstraints(450, 65, 80, -1));
    jpDetail00.add(jraUkupno,  new XYConstraints(150, 130, 80, -1));
    addButtonActionListeners();
  }
  // jpDetail00 - EOB

  // jpDetail01 - BOB
  JLabel jlDatOdl = new JLabel();
  JLabel jlDatDol = new JLabel();
  JLabel jlBrSati = new JLabel();
  JLabel jlBrDnev = new JLabel();
  JLabel jlJednaDnev = new JLabel();
  JLabel jlIznosDnev = new JLabel();
  JLabel jlSifTros = new JLabel();
  JLabel jlDvotockaJedan = new JLabel();
  JLabel jlDvotockaDva = new JLabel();

  JraTextField jraBrSati = new JraTextField();

  JraTextField jraDatOdl = new JraTextField(){
    public void focusLost(FocusEvent e) {
      if (jraDatOdl.isValueChanged()){
        super.focusLost(e);
        fPN_V1_1.calculateSate();
      }
    }
  };
  JraTextField jraDatDol = new JraTextField(){
    public void focusLost(FocusEvent e) {
      if (jraDatDol.isValueChanged()){
        super.focusLost(e);
        fPN_V1_1.calculateSate();
      }
    }
  };
  JraTextField jraVrimeOdlH = new JraTextField(){
    public void focusLost(FocusEvent e) {
    if (!jraVrimeOdlH.maskCheck()){
      jraVrimeOdlH.selectAll();
      return;
    }
    if (fPN_V1_1.validSat(Integer.parseInt(jraVrimeOdlH.getText().trim()))){
      fPN_V1_1.satiOdlDol.setInt("SATODL", Integer.parseInt(jraVrimeOdlH.getText().trim()));
      fPN_V1_1.setVrimenaOdlDol(fPN_V1_1.satiOdlDol.getInt("SATODL"),fPN_V1_1.satiOdlDol.getInt("MINODL"),
                                fPN_V1_1.satiOdlDol.getInt("SATDOL"),fPN_V1_1.satiOdlDol.getInt("MINDOL"));
          fPN_V1_1.calculateSate();
      } else {
        jraVrimeOdlH.setText("08");
        jraVrimeOdlH.requestFocus();
        jraVrimeOdlH.selectAll();
      }
    }
  };

  JraTextField jraVrimeDolH = new JraTextField(){
    public void focusLost(FocusEvent e) {
    if (!jraVrimeDolH.maskCheck()) return;
    if (fPN_V1_1.validSat(Integer.parseInt(jraVrimeDolH.getText().trim()))){
      if (jraVrimeDolH.isValueChanged()){
        fPN_V1_1.satiOdlDol.setInt("SATDOL", Integer.parseInt(jraVrimeDolH.getText().trim()));
        fPN_V1_1.setVrimenaOdlDol(fPN_V1_1.satiOdlDol.getInt("SATODL"),fPN_V1_1.satiOdlDol.getInt("MINODL"),
                                  fPN_V1_1.satiOdlDol.getInt("SATDOL"),fPN_V1_1.satiOdlDol.getInt("MINDOL"));
        fPN_V1_1.calculateSate();
      }
    } else {
      jraVrimeDolH.setText("20");
        jraVrimeDolH.requestFocus();
        jraVrimeDolH.selectAll();
      }
    }
  };
  JraTextField jraVrimeOdlM = new JraTextField(){
    public void focusLost(FocusEvent e) {
    if (!jraVrimeOdlM.maskCheck()) return;
      if (fPN_V1_1.validMin(Integer.parseInt(jraVrimeOdlM.getText().trim()))){
        if (jraVrimeOdlM.isValueChanged()){
          fPN_V1_1.satiOdlDol.setInt("MINODL", Integer.parseInt(jraVrimeOdlM.getText().trim()));
          fPN_V1_1.setVrimenaOdlDol(fPN_V1_1.satiOdlDol.getInt("SATODL"),fPN_V1_1.satiOdlDol.getInt("MINODL"),
                                    fPN_V1_1.satiOdlDol.getInt("SATDOL"),fPN_V1_1.satiOdlDol.getInt("MINDOL"));
          fPN_V1_1.calculateSate();
        }
      } else {
        jraVrimeOdlM.setText("00");
        jraVrimeOdlM.requestFocus();
        jraVrimeOdlM.selectAll();
      }
    }
  };
  JraTextField jraVrimeDolM = new JraTextField(){
    public void focusLost(FocusEvent e) {
    if (!jraVrimeDolM.maskCheck()) return;
      if (fPN_V1_1.validMin(Integer.parseInt(jraVrimeDolM.getText().trim()))){
        if(jraVrimeDolM.isValueChanged()){
          fPN_V1_1.satiOdlDol.setInt("MINDOL", Integer.parseInt(jraVrimeDolM.getText().trim()));
          fPN_V1_1.setVrimenaOdlDol(fPN_V1_1.satiOdlDol.getInt("SATODL"),fPN_V1_1.satiOdlDol.getInt("MINODL"),
                                    fPN_V1_1.satiOdlDol.getInt("SATDOL"),fPN_V1_1.satiOdlDol.getInt("MINDOL"));
          fPN_V1_1.calculateSate();
        }
      } else {
        jraVrimeDolM.setText("00");
        jraVrimeDolM.requestFocus();
        jraVrimeDolM.selectAll();
      }
    }
  };
    JraTextField jraJednaDnev = new JraTextField(){
      public void focusLost(FocusEvent e) {
        super.focusLost(e);
        fPN_V1_1.calculateIznosDnev();
      }
    };

  private void setDetail01(){
    jpDetail01.setLayout(lay);
    jlDatOdl.setText("Datum i vrijeme odlaska");
    jlDatDol.setText("Datum i vrijeme dolaska");
    jlBrSati.setText("Broj sati");
    jlBrDnev.setText("Broj dnevnica");
    jlJednaDnev.setText("Jedna dnevnica");
    jlIznosDnev.setText("Iznos dnevnica");
    jlSifTros.setText("Šifra troška");
    jlDvotockaJedan.setText(":");
    jlDvotockaDva.setText(":");

    // --------------Odlazak---------------

    jraDatOdl.setColumnName("DATUMODL");
    jraDatOdl.setDataSet(fPN_V1_1.getDetailSet());
    jraDatOdl.setHorizontalAlignment(SwingConstants.CENTER);

    jraVrimeOdlH.setColumnName("SATODL");
    jraVrimeOdlH.setDataSet(fPN_V1_1.satiOdlDol);

    jraVrimeOdlM.setColumnName("MINODL");
    jraVrimeOdlM.setDataSet(fPN_V1_1.satiOdlDol);

    //-------------------------------------

    // --------------Povratak--------------

    jraDatDol.setColumnName("DATUMDOL");
    jraDatDol.setDataSet(fPN_V1_1.getDetailSet());
    jraDatDol.setHorizontalAlignment(SwingConstants.CENTER);

    jraVrimeDolH.setColumnName("SATDOL");
    jraVrimeDolH.setDataSet(fPN_V1_1.satiOdlDol);

    jraVrimeDolM.setColumnName("MINDOL");
    jraVrimeDolM.setDataSet(fPN_V1_1.satiOdlDol);

    //-------------------------------------

    jraBrSati.setColumnName("BROJSATI");
    jraBrSati.setDataSet(fPN_V1_1.getDetailSet());

    jpDetail01.add(jlBrDnev, new XYConstraints(15, 95, -1, -1));
    jpDetail01.add(jlBrSati, new XYConstraints(15, 70, -1, -1));
    jpDetail01.add(jlSifTros, new XYConstraints(15, 120, -1, -1));
    jpDetail01.add(jlDatDol, new XYConstraints(15, 45, -1, -1));
    jpDetail01.add(jlDatOdl, new XYConstraints(15, 20, -1, -1));
    jpDetail01.add(jlIznosDnev, new XYConstraints(320, 95, -1, -1)); // <- 300
    jpDetail01.add(jlJednaDnev, new XYConstraints(320, 70, -1, -1)); // <- 300
    jpDetail01.add(jraDatDol, new XYConstraints(150, 45, 100, -1));
    jpDetail01.add(jraDatOdl, new XYConstraints(150, 20, 100, -1));
    jpDetail01.add(jraVrimeOdlH,      new XYConstraints(255, 20, 25, -1));
    jpDetail01.add(jraVrimeDolH,    new XYConstraints(255, 45, 25, -1));
    jpDetail01.add(jraVrimeOdlM,     new XYConstraints(285, 20, 25, -1));
    jpDetail01.add(jraVrimeDolM,     new XYConstraints(285, 45, 25, -1));
    jpDetail01.add(jlDvotockaJedan,     new XYConstraints(280, 20, -1, -1));
    jpDetail01.add(jlDvotockaDva,   new XYConstraints(280, 45, -1, -1));
    jpDetail01.add(jraBrSati, new XYConstraints(150, 70, 100, -1));
    jpDetail01.add(jraJednaDnev, new XYConstraints(450, 70, 100, -1));
  }
  // jpDetail01 - EOB

  // jpDetail02 - BOB
  JLabel jlBrojdnk = new JLabel();
  JLabel jlPrSredstvo = new JLabel();
  JLabel jlIznos2 = new JLabel();
  JLabel jlStavka2 = new JLabel();
  JLabel jlOdmj = new JLabel();
  JLabel jlaDomj = new JLabel();
  JLabel jlaOdmj = new JLabel();
  JLabel jlCrtica = new JLabel();

  JraButton jbSelPrSr = new JraButton();
  JraButton jbSelRels = new JraButton();

  JraTextField jraDomj = new JraTextField();
  
  JlrNavField jraOdmj = new JlrNavField(){
    public void after_lookUp() {
      aftlRelacije();
    }
  };

  JlrNavField jlrSifPrSredstva = new JlrNavField() {
    public void after_lookUp() {
      calcIznos();
    }
  };
  JlrNavField jlrNazivPrSredstva = new JlrNavField() {
    public void after_lookUp() {
      calcIznos();
    }
  };

  private void setDetail02(){
    jpDetail02.setLayout(lay);
    jlBrojdnk.setText("Cijena x km = 0.00 x");
    jlPrSredstvo.setText("Prijevozno sredstvo");
    jlIznos2.setText("Iznos troškova");
    jlOdmj.setText("Relacija");
    jlaOdmj.setText("Od mjesta");
    jlaDomj.setText("Do mjesta");
    jlStavka2.setText("Šifra troška");
    jlCrtica.setText("-");
    jlaOdmj.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDomj.setHorizontalAlignment(SwingConstants.CENTER);

    jraOdmj.setColumnName("ODMJ");
    jraOdmj.setDataSet(fPN_V1_1.getDetailSet());
    jraOdmj.setNavColumnName("NAZIV");
    jraOdmj.setVisCols(new int[] {0,1});
    jraOdmj.setSearchMode(1);
    jraOdmj.setRaDataSet(frmRelacije.getFRelacije().getSifraSet());
    jraOdmj.setNavButton(jbSelRels);
    
    jraDomj.setColumnName("DOMJ");
    jraDomj.setDataSet(fPN_V1_1.getDetailSet());

    
    jlrSifPrSredstva.setColumnName("CPRIJSRED");
    jlrSifPrSredstva.setNavColumnName("CSIF");
    jlrSifPrSredstva.setDataSet(fPN_V1_1.getDetailSet());
    jlrSifPrSredstva.setColNames(new String[] {"NAZIV"});
    jlrSifPrSredstva.setTextFields(new JTextComponent[] {jlrNazivPrSredstva});
    jlrSifPrSredstva.setVisCols(new int[] {0, 2});
    jlrSifPrSredstva.setSearchMode(0);
    jlrSifPrSredstva.setRaDataSet(frmPSredstva.getPSInstance().getSifraSet()/*ss.prijevoznaSredstvaIzSifrarnika()*/);
    jlrSifPrSredstva.setNavButton(jbSelPrSr);

    jlrNazivPrSredstva.setColumnName("NAZIV");
    jlrNazivPrSredstva.setNavProperties(jlrSifPrSredstva);
    jlrNazivPrSredstva.setSearchMode(1);

    jpDetail02.add(jlBrojdnk, new XYConstraints(15, 90, -1, -1));
    jpDetail02.add(jlPrSredstvo, new XYConstraints(15, 65, -1, -1));
    jpDetail02.add(jlIznos2, new XYConstraints(15, 115, -1, -1));
    jpDetail02.add(jlOdmj, new XYConstraints(15, 40, -1, -1));
    jpDetail02.add(jlaDomj, new XYConstraints(351, 23, 193, -1));
    jpDetail02.add(jraOdmj, new XYConstraints(150, 40, 195, -1));
    jpDetail02.add(jlCrtica,      new XYConstraints(348, 40, -1, -1));
    jpDetail02.add(jlaOdmj, new XYConstraints(151, 23, 193, -1));
    jpDetail02.add(jraDomj, new XYConstraints(355, 40, 195, -1));
    jpDetail02.add(jlrSifPrSredstva, new XYConstraints(150, 65, 100, -1));
    jpDetail02.add(jlrNazivPrSredstva, new XYConstraints(255, 65, 295, -1));
    jpDetail02.add(jbSelPrSr, new XYConstraints(555, 65, 21, 21));
    jpDetail02.add(jbSelRels, new XYConstraints(555, 40, 21, 21));
    jpDetail02.add(jlStavka2, new XYConstraints(15, 140, -1, -1));
  }
  // jpDetail02 - EOB

  protected void aftlRelacije() {
    String naz = jraOdmj.getRaDataSet().getString("NAZIV");
    String s_od = naz.substring(0,naz.indexOf("--")).trim();
    String s_do = naz.substring(naz.indexOf("--")+2,naz.length()).trim();
    jraOdmj.getDataSet().setString("ODMJ", s_od);
    jraOdmj.getDataSet().setString("DOMJ", s_do);
    jraOdmj.getDataSet().setBigDecimal("BROJDNK", getDistance());
    calcIznos();
  }

  public BigDecimal getDistance() {
    if ("".equals(jraOdmj.getText().trim())) {
      return Aus.zero2;
    }
    BigDecimal distance;
    try {
      distance = new BigDecimal(jraOdmj.getRaDataSet().getString("PARAMETRI").trim());
    } catch (Exception e) {
      distance = Aus.zero2;
    }
    return distance;
  }

  public BigDecimal getCijena() {
    if ("".equals(jlrSifPrSredstva.getText().trim())) {
      System.err.println("getCijena::vracam nulu!!");
      return Aus.zero2;
    }
    BigDecimal cijena;
    try {
//      jlrSifPrSredstva.forceFocLost();
//      System.out.println("jlrSifPrSredstva.getRaDataSet():: "+jlrSifPrSredstva.getRaDataSet());
      if (!lookupData.getlookupData().raLocate(jlrSifPrSredstva.getRaDataSet(), "CSIF", jlrSifPrSredstva.getText())) return Aus.zero2;
      cijena = new BigDecimal(jlrSifPrSredstva.getRaDataSet().getString("PARAMETRI").trim());
    } catch (Exception e) {
      cijena = Aus.zero2;
    }
    return cijena;
  }

  private void calcIznos() {
    if (!jlBrojdnk.isShowing()) return;
    jlBrojdnk.setText("Cijena x km = "+getCijena()+" x");
    fPN_V1_1.getDetailSet().setBigDecimal("IZNOS", getCijena().multiply(fPN_V1_1.getDetailSet().getBigDecimal("BROJDNK")));
  }

  // jpDetail03 - BOB
  JLabel jlBrojDNK03 = new JLabel();
  JLabel jlIznos03 = new JLabel();
  JLabel jlStavka3 = new JLabel();

  private void setDetail03(){
    jpDetail03.setLayout(lay);
    jlBrojDNK03.setText("Broj no\u0107enja");
    jlIznos03.setText("Iznos troškova");
    jlStavka3.setText("Šifra troška");
    jpDetail03.add(jlBrojDNK03, new XYConstraints(15, 20, -1, -1));
    jpDetail03.add(jlIznos03, new XYConstraints(15, 45, -1, -1));
    jpDetail03.add(jlStavka3, new XYConstraints(15, 70, -1, -1));
  }
  // jpDetail03 - EOB

  // jpDetail04 - BOB
  JLabel jlIznos04 = new JLabel();
  JLabel jlStavka4 = new JLabel();

  private void setDetail04(){
    jpDetail04.setLayout(lay);
    jlIznos04.setText("Iznos troškova");
    jlStavka4.setText("Šifra troška");
    jpDetail04.add(jlIznos04, new XYConstraints(15, 20, -1, -1));
    jpDetail04.add(jlStavka4, new XYConstraints(15, 45, -1, -1));
  }
  // jpDetail04 - EOB

  private void jbInit() throws Exception {
    setShared();
    setUpper();
    setMidel();
    setDetail00();
    setDetail01();
    setDetail02();
    setDetail03();
    setDetail04();
    setTabed();
    this.setLayout(glavniLayout);
    this.add(jpUpper, BorderLayout.NORTH);
    this.add(jpMidel, BorderLayout.CENTER);
    this.add(jTabed, BorderLayout.SOUTH);
  }

  // shared - BOB

  JlrNavField jlrStavka = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrOpisStavke = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JraTextField jraIznos = new JraTextField();
  JraTextField jraBrojDNK = new JraTextField();
  JraButton jbSelStavka = new JraButton();

  private void setShared(){
    okvir = new EtchedBorder(EtchedBorder.RAISED,new Color(224, 255, 255),new Color(109, 129, 140));
    lay.setWidth(650);
    lay.setHeight(200);

    jraBrojDNK.setColumnName("BROJDNK");
    jraBrojDNK.setDataSet(fPN_V1_1.getDetailSet());
    jraBrojDNK.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        calcIznos();
      }
    });
    jraIznos.setColumnName("IZNOS");
    jraIznos.setDataSet(fPN_V1_1.getDetailSet());
  }
  // shared - EOB

  private void setStavka(){
    jlrStavka.setColumnName("STAVKA");
    jlrStavka.setNavColumnName("STAVKA");
    jlrStavka.setDataSet(fPN_V1_1.getDetailSet());
    jlrStavka.setColNames(new String[] {"OPIS"});
    jlrStavka.setTextFields(new JTextComponent[] {jlrOpisStavke});
    jlrStavka.setVisCols(new int[] {2, 3});
    jlrStavka.setSearchMode(0);
    jlrStavka.setNavButton(jbSelStavka);
  }
  private void setOpisStavke(){
    jlrOpisStavke.setColumnName("OPIS");
    jlrOpisStavke.setDataSet(fPN_V1_1.getDetailSet());
    jlrOpisStavke.setNavProperties(jlrStavka);
    jlrOpisStavke.setSearchMode(1);
  }

  public void addD01(){
    setStavka();
    jlrStavka.setRaDataSet(hr.restart.sisfun.Asql.getShkonta("blpn", "2", "PN"));
    setOpisStavke();

    jpDetail01.add(jlrStavka, new XYConstraints(150, 120, 100, -1));
    jpDetail01.add(jlrOpisStavke, new XYConstraints(255, 120, 295, -1));
    jpDetail01.add(jraBrojDNK, new XYConstraints(150, 95, 100, -1));
    jpDetail01.add(jraIznos, new XYConstraints(450, 95, 100, -1));
    jpDetail01.add(jbSelStavka, new XYConstraints(555, 120, 21, 21));
  }
  public void addD02(){
    setStavka();
    jlrStavka.setRaDataSet(hr.restart.sisfun.Asql.getShkonta("blpn", "3", "PN"));
    setOpisStavke();

    jpDetail02.add(jraBrojDNK, new XYConstraints(150, 90, 100, -1));
    jpDetail02.add(jraIznos, new XYConstraints(150, 115, 100, -1));
    jpDetail02.add(jlrStavka, new XYConstraints(150, 140, 100, -1));
    jpDetail02.add(jlrOpisStavke, new XYConstraints(255, 140, 295, -1));
    jpDetail02.add(jbSelStavka, new XYConstraints(555, 140, 21, 21));
  }
  public void addD03(){
    setStavka();
    jlrStavka.setRaDataSet(hr.restart.sisfun.Asql.getShkonta("blpn", "4", "PN"));
    setOpisStavke();

    jpDetail03.add(jraBrojDNK, new XYConstraints(150, 20, 100, -1));
    jpDetail03.add(jraIznos, new XYConstraints(150, 45, 100, -1));
    jpDetail03.add(jlrStavka, new XYConstraints(150, 70, 100, -1));
    jpDetail03.add(jlrOpisStavke, new XYConstraints(255, 70, 295, -1));
    jpDetail03.add(jbSelStavka, new XYConstraints(555, 70, 21, 21));
  }
  public void addD04(){
    setStavka();
    jlrStavka.setRaDataSet(hr.restart.sisfun.Asql.getShkonta("blpn", "5", "PN"));
    setOpisStavke();

    jpDetail04.add(jraIznos, new XYConstraints(150, 20, 100, -1));
    jpDetail04.add(jlrOpisStavke, new XYConstraints(255, 45, 295, -1));
    jpDetail04.add(jlrStavka, new XYConstraints(150, 45, 100, -1));
    jpDetail04.add(jbSelStavka, new XYConstraints(555, 45, 21, 21));
  }
  private void addButtonActionListeners(){
    jbGetDnevnice.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fPN_V1_1.posljediceJP01();
      }
    });

    jbGetPutTrosk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fPN_V1_1.posljediceJP02();
      }
    });

    jbGetNocenja.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
          fPN_V1_1.posljediceJP03();
      }
    });

    jbGetOstalo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
          fPN_V1_1.posljediceJP04();
      }
    });
  }
}