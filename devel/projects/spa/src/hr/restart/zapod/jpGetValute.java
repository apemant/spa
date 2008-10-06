/****license*****************************************************************
**   file: jpGetValute.java
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

import hr.restart.baza.Valute;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title: jpGetValute
 * Description: Panel za odabir valute.
 * Copyright:    Copyright (c) 2001
 * Company: REST-ART
 * @author
 * @version 1.0
 *
 * Panel za odabir valute moze se koristiti u cetiri varijante. Default varijanta sluzi
 * samo za (opcionalni) dohvat valute. Ukoliko valuta mora biti stalno aktivna,
 * moze se pozvati metoda setAlwaysSelected() koja zamjenjuje checkbox s labelom.
 * Za prikazivanje tecaja izabrane valute sluzi metoda setTecajVisible(), a za
 * omogucavanje i promjene tecaja metoda setTecajEditable(). U oba posljednja
 * slucaja kolona u koju se zapisuje tecaj po defaultu je 'TECAJ' a moze se
 * promijeniti metodom setTecajColumn(). Metoda setDataSet() postavlja dataset
 * na koji se bindaju polja OZNVAL i (eventualno) TECAJ, odnosno kolona zadana
 * metodom setTecajColumn(). Sve te metode trebaju se pozvati prilikom jednokratne
 * inicijalizacije ovog panela (nakon instanciranja).<p>
 * Za daljnje koristenje dovoljno je u metodi SetFokus() raMatPodataka pozvati
 * metodu ove klase, initJP(mode), gdje je mode parametar iz SetFokus(). Ukoliko
 * je tecaj vidljiv, potrebno je i u afterSetMode() metodi pozvati disableDohvat().
 * Dodatno se moze, recimo, na focusLost odgovarajuceg datumskog polja pozvati
 * metoda setTecajDatum(), da bi se automatski dohvatio odgovarajuci tecaj.
 * Alternativno se moze tecaj rucno popunjavati gazenjem ometode afterGet_Val(),
 * koja se zove prilikom promjene valute na ovom panelu.<p>
 */

public class jpGetValute extends JPanel {
  public XYLayout xYLayout1 = new XYLayout();

  class MyNavField extends JlrNavField {
    public void after_lookUp() {
      if (isLastLookSuccessfull() && lookupCalled)
        tecajChanged = false;
      lookupCalled = false;
      afterLookUpVal();
    }
    public void keyF9Pressed() {
      lookupCalled = true;
      super.keyF9Pressed();
    }
  }

  public JlrNavField jtNAZVAL = new MyNavField();
  public JlrNavField jtCVAL = new MyNavField();
  public JlrNavField jtOZNVAL = new MyNavField();

  public JraButton jbGetVal = new JraButton();
  public JLabel jlOZNVAL = new JLabel();
  public JLabel jlCVAL = new JLabel();

  private String tecajColumn = "TECAJ";

  private boolean borderVisible = true;
  private boolean doGetTecaj = true;
  private boolean tecajAlways = false;
  private boolean tecajVisible = false;
  private boolean tecajEditable = false;
  private boolean tecajChanged = false;
  private boolean lookupCalled = false;
  private boolean valutaEditable = true;
  private boolean protectVal = true;
  private boolean defaultEntryEnabled = false;

  /* dohvaceni tecaj */
  private BigDecimal dohTecaj = new BigDecimal(0.);

  private java.sql.Timestamp tecajDate = new java.sql.Timestamp(System.currentTimeMillis());

  private int tecajTip = Tecajevi.DEFAULT;

  hr.restart.baza.dM dm;

  hr.restart.util.raCommonClass rCC = hr.restart.util.raCommonClass.getraCommonClass();

//  JLabel jlValuta = new JLabel();

  public JraCheckBox jcbValuta = new JraCheckBox();
  public JLabel jlNAZVAL = new JLabel();
  public JraTextField jtTECAJ;
  public JLabel jlDVAL;
  public JLabel jlValuta = new JLabel();
  XYConstraints jcbValutaXYC = new XYConstraints(15, 20, -1, -1);
  
  public jpGetValute() {
    this(true);
  }
  
  public jpGetValute(boolean all) {
    try {
      jbInit(all);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit(boolean all) throws Exception {

    dm = hr.restart.baza.dM.getDataModule();
    jlCVAL.setText("Šifra");
    jtOZNVAL.setSearchMode(0);
    jtOZNVAL.setTextFields(new javax.swing.text.JTextComponent[] {jtCVAL,jtNAZVAL});
    jtOZNVAL.setVisCols(new int[] {0,1,3});
    jtOZNVAL.setColNames(new String[] {"CVAL","NAZVAL"});
    if (all) {
      jtOZNVAL.setRaDataSet(dm.getAllValute());
    } else {
      jtOZNVAL.setRaDataSet(Valute.getDataModule().getStrane());
    }
    jtOZNVAL.setNavButton(jbGetVal);

//    jtOZNVAL.setDataSet(dm.getTecajevi()); -> u this.setDataSet

    jtOZNVAL.setColumnName("OZNVAL");
    jlOZNVAL.setText("Oznaka");

    jtCVAL.setNavProperties(jtOZNVAL);
    jtCVAL.setColumnName("CVAL");

    jtNAZVAL.setNavProperties(jtCVAL);
    jtNAZVAL.setSearchMode(1);
    jtNAZVAL.setColumnName("NAZVAL");

    this.setLayout(xYLayout1);

    xYLayout1.setWidth(516);
    xYLayout1.setHeight(50);
    jcbValuta.setText("Valuta");
    jlValuta.setText("Valuta");
    jlNAZVAL.setText("Naziv");
    this.add(jtOZNVAL, new XYConstraints(150, 20, 45, -1));
    this.add(jlOZNVAL,  new XYConstraints(150, 2, -1, -1));
    this.add(jtCVAL, new XYConstraints(200, 20, 45, -1));
    this.add(jtNAZVAL, new XYConstraints(250, 20, 224, -1));
    this.add(jlCVAL,  new XYConstraints(200, 2, -1, -1));
    this.add(jcbValuta, jcbValutaXYC);
    this.add(jlNAZVAL,  new XYConstraints(250, 2, -1, -1));
    this.add(jbGetVal, new XYConstraints(479, 20, 21, 21));//425

    jcbValuta.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
          
//        if (!tecajAlways) protectValute(!jcbValuta.isSelected());
          actionPerformedjcbValuta();   // T.V.
      }
    });
/*    jcbValuta.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent ev) {

      }
    });*/
  }
  
  public void actionPerformedjcbValuta(){
      if (!tecajAlways) protectValute(!jcbValuta.isSelected());
      
  }

/**

 * Overridaj ako ne zelis checkbox Valuta nego labelu i returnaj true

 * @return

 */

/*  public boolean isTecajevi() {

    return false;

/*    try {

      if (jtOZNVAL.getDataSet()==null) return false;

      jtOZNVAL.getDataSet().open();

      dm.getTecajevi().open();

      return jtOZNVAL.getDataSet().equals(dm.getTecajevi());

    } catch (Exception e) {

      return false;

    }



  } */


/**
 * Inicijalizira panel ovisno o modu (raMatPodaci.mode) a to je da brise svoje smece i
 * postavlja referentnu valutu. Obavezno zvati na kraju SetFokus metode u raMatPodaci.
 */

  public void initJP(char mode) {
    dm.getTecajevi().open();
    tecajChanged = false;
    if (mode=='N') {
      if (!tecajAlways) {
        jcbValuta.setSelected(defaultEntryEnabled);
        protectValute(!defaultEntryEnabled);
      }
      jtOZNVAL.forceFocLost();
      doGetTecaj = true;
    } else if (mode=='I') {
      jcbValuta.setSelected(jtOZNVAL.getText().length() > 0);
      if (!tecajAlways && !valutaEditable) {
        enableComps(false);
        rCC.setLabelLaF(jcbValuta, false);
      } else if (!tecajAlways) {
        enableComps(jcbValuta.isSelected());
        doGetTecaj = true;
      } else doGetTecaj = true;
    } else
      jcbValuta.setSelected(jtOZNVAL.getText().length() > 0);

    if (tecajVisible && !jcbValuta.isSelected()) {
      jtTECAJ.getDataSet().setBigDecimal(tecajColumn, hr.restart.sk.raSaldaKonti.n0);
      jtTECAJ.setText("");
    }
    if (mode == 'N' && tecajAlways)
      jtOZNVAL.requestFocus();
  }

  /**
   * metodu koju je nuzno zvati u metodi afterSetMode() raMatPodataka,
   * kad se mode mijenja u 'B'.  (newMode == 'B'). Ruzno, ali potrebno
   * da JlrNavField ne bi pregazio eventualno promijenjen tecaj.
   * Ukoliko tecaj nije uopce na ekranu, onda nema potrebe.
   */
  public void disableDohvat() {
    doGetTecaj = false;
  }

  public void enableDohvat() {
    doGetTecaj = true;
  }

  private void enableComps(boolean enb) {
    rCC.setLabelLaF(jtOZNVAL,enb);
    rCC.setLabelLaF(jtCVAL,enb);
    rCC.setLabelLaF(jbGetVal,enb);
    rCC.setLabelLaF(jtNAZVAL,enb);
    if (tecajVisible) rCC.setLabelLaF(jtTECAJ,enb && tecajEditable);
  }

/**
 * Nakon dohvata valute radi getTecaj ako mu je tako receno, ako je tecajVisible=true prikazuje ga na ekranu
 */

  private void afterLookUpVal() {
    if (jtOZNVAL.getText().length() == 0) {
      afterClearVal();
      return;
    }
    if (tecajVisible) dohTecaj = getTecajDB();
    if (tecajVisible && doGetTecaj && !tecajChanged)
      jtTECAJ.getDataSet().setBigDecimal(tecajColumn, dohTecaj);
    afterGet_Val();
  }

/**
 * Dohvaca tecaj ovisno o vrijednosti u jtOZNVAL, te parametrima tecajTip i tecajDate
 */

  public java.math.BigDecimal getTecajDB() {
    return Tecajevi.getTecaj(new java.sql.Date(tecajDate.getTime()),jtOZNVAL,tecajTip);
  }

  public java.math.BigDecimal getTecajForReal() {
    return Tecajevi.getTecaj(new java.sql.Date(tecajDate.getTime()),jtOZNVAL,tecajTip).divide(Tecajevi.getJedVal(jtOZNVAL.getText()),6,BigDecimal.ROUND_HALF_UP);
  }

  private void protectValute(boolean toprotect) {
    protectVal = toprotect;
    enableComps(!protectVal);
    if (protectVal) {
      jtOZNVAL.setText("");
      jtOZNVAL.forceFocLost();
      if (tecajVisible) {
        tecajChanged = false;
        jlDVAL.setVisible(false);
        jtTECAJ.getDataSet().setBigDecimal(tecajColumn, hr.restart.sk.raSaldaKonti.n0);
        jtTECAJ.setText("");
        jtTECAJ.setColumnName("DUMMY");
      }
    } else {
      if (tecajVisible) {
        jlDVAL.setVisible(true);
        jtTECAJ.setColumnName(tecajColumn);
      }
      jtOZNVAL.setText(Tecajevi.getRefOZNVAL());
      jtOZNVAL.forceFocLost();
    }
  }

/**
 * Postavlja da li je kad se pritisne 'Novi' u raMatPodaci na panelu omogucen unos ili ne. Default je false.
 */

  public void setDefaultEntryEnabled(boolean p1) {
    defaultEntryEnabled = p1;
  }

  public boolean isDefaultEntryEnabled() {
    return defaultEntryEnabled;
  }

/**
 * Vraca da li je omogucen unos valute, odnosno checkbox kliknut ili ne
 */
  public boolean isProtectedVal() {
    return protectVal;
  }

/**
 * Dataset u koji treba dohvatiti strani kljuc "OZNVAL"
 */

  public void setRaDataSet(com.borland.dx.dataset.DataSet newRaDataSet) {
    jtOZNVAL.setDataSet(newRaDataSet);
    if (tecajVisible)
      jtTECAJ.setDataSet(newRaDataSet);
  }



  public com.borland.dx.dataset.DataSet getRaDataSet() {
    return jtOZNVAL.getDataSet();
  }

/**
 * Treba li border oko panela?
 */
  public void setBorderVisible(boolean newBorderVisible) {
    borderVisible = newBorderVisible;
    if (borderVisible) setBorder(BorderFactory.createEtchedBorder());
  }

  public boolean isValutaSelected() {
    return tecajAlways || jcbValuta.isSelected();
  }

/**
 * Datum po kojem se trazi tecaj. Default je danasnji datum
 */

  public void setTecajDate(java.sql.Timestamp newTecajDate) {
    tecajDate = newTecajDate;
    if (tecajVisible && jcbValuta.isSelected() && !tecajChanged)
      jtTECAJ.getDataSet().setBigDecimal(tecajColumn, dohTecaj = getTecajDB());
  }

  public java.sql.Timestamp getTecajDate() {
    return tecajDate;
  }

/**
 * Tip tecaja je Tecajevi.SREDNJI, KUPOVNI, PRODAJNI ili DEFAULT. Default = sto mislis?
 */

  public void setTecajTip(int newTecajTip) {
    tecajTip = newTecajTip;
  }

  public int getTecajTip() {
    return tecajTip;
  }

/**
 * Da li da se na panelu pojavi textField u kojem pise tecaj + label u kojem pise domicilna valuta (VALUTE.STRVAL='N')
 */
  public void setTecajVisible(boolean newTecajVisible) {
    tecajVisible = newTecajVisible;
    if (tecajVisible) createTecajUI();
  }

  public boolean isTecajVisible() {
    return tecajVisible;
  }

  public void setAlwaysSelected(boolean always) {
    if (always == tecajAlways) return;
    tecajAlways = always;
    if (always) {
      this.remove(jcbValuta);
      this.add(jlValuta,jcbValutaXYC);
    } else {
      this.remove(jlValuta);
      this.add(jcbValuta,jcbValutaXYC);
    }
  }

/**
 * Ako je tecajVisible da li je ujedno i editable? Default = true
 */

  public void setTecajEditable(boolean newTecajEditable) {
    tecajEditable = newTecajEditable;
    if (tecajEditable) doGetTecaj = false;
    if (tecajVisible) jtTECAJ.setEditable(tecajEditable);
  }

  public boolean isTecajEditable() {
    return tecajEditable;
  }

  public void setValutaEditable(boolean newValutaEditable) {
    valutaEditable = newValutaEditable;

  }

  public boolean isValutaEditable() {
    return valutaEditable;
  }

  void createTecajUI() {
    jtTECAJ = new JraTextField();
    jlDVAL = new JLabel();
    jlDVAL.setHorizontalAlignment(SwingConstants.RIGHT);
    jlDVAL.setText(Tecajevi.getDomOZNVAL());
    jtTECAJ.setDataSet(jtOZNVAL.getDataSet());
    jtTECAJ.setColumnName(tecajColumn);
    jtTECAJ.setEditable(tecajEditable);
    jtTECAJ.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (!jtTECAJ.getDataSet().getBigDecimal(tecajColumn).equals(dohTecaj))
          tecajChanged = true;
      }
    });

    this.add(jlDVAL, new XYConstraints(601, 20, 30, -1));//zavrsava na 631
    this.add(jtTECAJ, new XYConstraints(505, 20, 100, -1));//531
    xYLayout1.setWidth(640);
  }

  /**
   * Mijenja ima kolone tecaja u datasetu. Default je 'TECAJ'
   */
  public void setTecajColumn(String colName) {
    tecajColumn = colName;
    if (tecajVisible) jtTECAJ.setColumnName(colName);
  }

/**
 * Funkcija za overridanje nakon dohvata valute (i tecaja)
 */
  public void afterGet_Val() {
  }

  public void afterClearVal() {
  }
}

