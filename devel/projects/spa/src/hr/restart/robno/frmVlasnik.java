/****license*****************************************************************
**   file: frmVlasnik.java
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
package hr.restart.robno;

import hr.restart.baza.dM;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;

import java.awt.event.KeyEvent;

import com.borland.dx.sql.dataset.QueryDataSet;


public class frmVlasnik extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpVlasnik jpDetail;

  static frmVlasnik vlasnik;

  hr.restart.util.raNavAction odabir = new hr.restart.util.raNavAction("Odabir",raImages.IMGIMPORT,KeyEvent.VK_ENTER) {
    public void actionPerformed(java.awt.event.ActionEvent ev) {
      table2Clicked();
    }
  };

  private boolean insideCall = false;
  private boolean selected = false;
  private int ckupac;

  public frmVlasnik() {
    try {
      jbInit();
      vlasnik = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmVlasnik getInstance() {
    return vlasnik;
  }

  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraCkupac, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode !='B') jpDetail.jraIme.requestFocus();
    if (mode == 'N')
      getRaQueryDataSet().setInt("CKUPAC", getNextCKupac());
  }

  public boolean Validacija(char mode) {
    if (mode == 'N') {
      if (vl.chkIsEmpty(jpDetail.jraCkupac)) getRaQueryDataSet().setInt("CKUPAC", getNextCKupac());
      if (vl.notUnique(jpDetail.jraCkupac)) return false;
    }
    if (vl.isEmpty(jpDetail.jraIme)) return false;
    return true;
  }

  public static int getNextCKupac() {
    QueryDataSet maxSet = hr.restart.util.Util.getNewQueryDataSet("SELECT max(CKUPAC) from KUPCI");
    if (maxSet.getRowCount() == 0) return 1;
    else return maxSet.getInt(0)+1;
  }

  public void table2Clicked() {
    if (!insideCall)
      super.table2Clicked();
    else {
      selected = true;
      ckupac = this.getRaQueryDataSet().getInt("CKUPAC");
      rnvExit_action();
    }
  }

  public void SetInner(boolean inner) {
    if (this.getNavBar().contains(odabir) && !inner) {
      this.getNavBar().getNavContainer().remove(odabir);
    } else if (inner && !this.getNavBar().contains(odabir)) {
      this.addOption(odabir,1);
    }
    insideCall = inner;
  }

  public int getKupac() {
    return ckupac;
  }

  public boolean isSelected() {
    return selected;
  }

  public void beforeShow() {
    selected = false;
  }

  public void AfterAfterSave(char mode) {
    if (!insideCall)
      super.AfterAfterSave(mode);
    else
      table2Clicked();
  }

  public void rnvExit_action() {
    SetInner(false);
    super.rnvExit_action();
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getAllKupci());
    this.setVisibleCols(new int[] {0, 1, 2});
    jpDetail = new jpVlasnik(this);
    this.setRaDetailPanel(jpDetail);
    raDataIntegrity.installFor(this);
  }
}



/////////////////////////////////////////////////////////////////////////////////
/* Staro & nestandardno
 package hr.restart.robno;

import javax.swing.*;
import javax.swing.text.*;
import com.borland.jbcl.layout.*;
import java.awt.*;
import java.awt.event.*;
import hr.restart.util.*;
import hr.restart.baza.*;
import hr.restart.swing.*;
import com.borland.dx.dataset.*;
import com.borland.dx.sql.dataset.*;

public class frmVlasnik extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  static frmVlasnik vlasnik;

  XYLayout xYLayout1 = new XYLayout();
  JPanel jpDetail = new JPanel();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JraTextField jraSifra = new JraTextField();
  JraTextField jraAdresa = new JraTextField();
  JraTextField jraPbr = new JraTextField();
  JraTextField jraIme = new JraTextField();
  JraTextField jraEmail = new JraTextField();
  JraTextField jraTelefon = new JraTextField();
  JraTextField jraMjesto = new JraTextField();
  JraTextField jraJMBG = new JraTextField();
  JLabel jLabel9 = new JLabel();
  JLabel jLabel10 = new JLabel();

  private boolean insideCall = false;
  private boolean selected = false;
  private int ckupac;
  JraTextField jraPrezime = new JraTextField();
  JLabel jLabel3 = new JLabel();

  hr.restart.util.raNavAction odabir = new hr.restart.util.raNavAction("Odabir",raImages.IMGIMPORT,KeyEvent.VK_ENTER) {
    public void actionPerformed(java.awt.event.ActionEvent ev) {
      table2Clicked();
    }
  };

  public frmVlasnik() {
    try {
      vlasnik = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmVlasnik getInstance() {
    return vlasnik;
  }

  public void SetInner(boolean inner) {
    if (this.getNavBar().contains(odabir) && !inner) {
//      System.out.println("removing");
//      System.out.println("position "+this.getNavBar().getNavContainer().getComponentIndex(odabir));
      this.getNavBar().getNavContainer().remove(odabir);
    } else if (inner && !this.getNavBar().contains(odabir)) {
//      System.out.println("adding");
      this.addOption(odabir,1);
    }
    insideCall = inner;
  }

  public int getKupac() {
    return ckupac;
  }

  public boolean isSelected() {
    return selected;
  }

  public void beforeShow() {
    selected = false;
  }

  public void EntryPoint(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jraSifra, false);
    }
  }
  //Andrej
  private int getNewCkupac() {
    vl.execSQL("SELECT max(CKUPAC) as MAXCKUPAC FROM kupci");
    vl.RezSet.open();
    return vl.RezSet.getInt(0)+1;
  }
  public void SetFokus(char mode) {
    if (mode == 'N') {
//      jraSifra.requestFocus();
      getRaQueryDataSet().setInt("CKUPAC",getNewCkupac());
      jraIme.requestFocus();
    } else if (mode == 'I') {
      jraIme.requestFocus();
    }
  }
  public boolean Validacija(char mode) {
    if (vl.isEmpty(jraSifra) || vl.isEmpty(jraIme))
      return false;
    if (mode == 'N' && vl.notUnique(jraSifra))
      return false;
    return true;
  }
  public void rnvExit_action() {
//    System.out.println("here");
    SetInner(false);
    super.rnvExit_action();
  }

  public void AfterAfterSave(char mode) {
    if (!insideCall)
      super.AfterAfterSave(mode);
    else
      table2Clicked();
  }

  public void table2Clicked() {
    if (!insideCall)
      super.table2Clicked();
    else {
      selected = true;
      ckupac = this.getRaQueryDataSet().getInt("CKUPAC");
      rnvExit_action();
    }
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getKupci());
    this.setRaDetailPanel(jpDetail);
    this.setVisibleCols(new int[] {0,1,2});

    jpDetail.setLayout(xYLayout1);
    jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel1.setText("Šifra");
    jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel2.setText("Ime");
    jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel4.setText("JMBG/MB");

    jraSifra.setColumnName("CKUPAC");
    jraSifra.setDataSet(this.getRaQueryDataSet());

    jraIme.setColumnName("IME");
    jraIme.setDataSet(this.getRaQueryDataSet());

    jraPrezime.setColumnName("PREZIME");
    jraPrezime.setDataSet(this.getRaQueryDataSet());

    jraJMBG.setColumnName("JMBG");
    jraJMBG.setDataSet(this.getRaQueryDataSet());
    jraAdresa.setColumnName("ADR");
    jraAdresa.setDataSet(this.getRaQueryDataSet());
    jraMjesto.setColumnName("MJ");
    jraMjesto.setDataSet(this.getRaQueryDataSet());
    jraTelefon.setColumnName("TEL");
    jraTelefon.setDataSet(this.getRaQueryDataSet());
    jraPbr.setColumnName("PBR");
    jraPbr.setDataSet(this.getRaQueryDataSet());
    jraEmail.setColumnName("EMADR");
    jraEmail.setDataSet(this.getRaQueryDataSet());

    jLabel8.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel8.setText("E-mail");
    jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel7.setText("Telefon");

    jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel6.setText("Mjesto");
    jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel5.setText("Adresa");
    jLabel9.setText("Kupac");

    xYLayout1.setWidth(645);
    xYLayout1.setHeight(135);

    jLabel10.setText("Pbr");
    jLabel10.setHorizontalAlignment(SwingConstants.CENTER);

    jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel3.setText("Prezime");
    jpDetail.add(jLabel1,     new XYConstraints(150, 10, 70, -1));
    jpDetail.add(jraSifra,    new XYConstraints(150, 25, 70, -1));
    jpDetail.add(jLabel2,         new XYConstraints(225, 10, 160, -1));
    jpDetail.add(jraIme,       new XYConstraints(225, 25, 160, -1));
    jpDetail.add(jraJMBG,     new XYConstraints(150, 60, 125, -1));
    jpDetail.add(jLabel4,     new XYConstraints(150, 45, 125, -1));
    jpDetail.add(jraAdresa,       new XYConstraints(280, 60, 150, -1));
    jpDetail.add(jraPbr,    new XYConstraints(435, 60, 60, -1));
    jpDetail.add(jraMjesto,      new XYConstraints(500, 60, 130, -1));
    jpDetail.add(jLabel6,            new XYConstraints(500, 45, 130, -1));
    jpDetail.add(jLabel5,    new XYConstraints(280, 45, 150, -1));
    jpDetail.add(jraTelefon,     new XYConstraints(150, 95, 125, -1));
    jpDetail.add(jLabel7,    new XYConstraints(150, 80, 120, -1));
    jpDetail.add(jLabel8,       new XYConstraints(280, 80, 150, -1));
    jpDetail.add(jraEmail,    new XYConstraints(280, 95, 150, -1));
    jpDetail.add(jLabel9,     new XYConstraints(15, 25, -1, -1));
    jpDetail.add(jLabel10,    new XYConstraints(435, 45, 60, -1));
    jpDetail.add(jraPrezime,  new XYConstraints(390, 25, 240, -1));
    jpDetail.add(jLabel3,   new XYConstraints(390, 10, 240, -1));
  }
}
 */