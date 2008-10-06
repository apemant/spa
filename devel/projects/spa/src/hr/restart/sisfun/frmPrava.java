/****license*****************************************************************
**   file: frmPrava.java
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
package hr.restart.sisfun;


import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raComboBox;
import hr.restart.util.raMatPodaci;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.NavigationEvent;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmPrava extends raMatPodaci {
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  JPanel jpMain = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlOznaka = new JLabel();
  JLabel jlDomena = new JLabel();
  dM dm = dM.getDataModule();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  JraTextField jtOznaka = new JraTextField();
  raComboBox jcbPravo = new raComboBox();
  JLabel jlSifra = new JLabel();
  JlrNavField jlrSif = new JlrNavField();
  JlrNavField jlrOpis = new JlrNavField();
  JraTextField jtKljuc = new JraTextField();
  JLabel jlKljuc = new JLabel();
  JPanel jpPravo = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JraCheckBox jcbPregled = new JraCheckBox();
  JraCheckBox jcbIzmjena = new JraCheckBox();
  JraCheckBox jcbDod = new JraCheckBox();
  JraCheckBox jcbBris = new JraCheckBox();
  JraButton jbSelOpis = new JraButton();
  JLabel jlOpis = new JLabel();
  JraTextField jraOpis = new JraTextField();
  String lastdom = "";
  HashMap stdnames = new HashMap(23);


  public frmPrava() {
    super(2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void setPrava() {
    String all = this.getRaQueryDataSet().getString("PRAVO");
    if (all.length() != 4) all = "0000";
    jcbPregled.setSelected(all.charAt(0) == '1');
    jcbDod.setSelected(all.charAt(1) == '1');
    jcbIzmjena.setSelected(all.charAt(2) == '1');
    jcbBris.setSelected(all.charAt(3) == '1');
  }
  public void SetFokus(char mode) {
     if (mode == 'N') {
       setPrava();
       updateDomena();
       setDomena();
       updateOpis();
       jlrSif.setText("");
       jlrSif.forceFocLost();
       jtOznaka.requestFocus();
     } else if (mode == 'I') {
       setPrava();
       rcc.setLabelLaF(jtOznaka,false);
       updateDomena();
     }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jtOznaka))  return false;
     if (mode == 'N') {
       if (vl.notUnique(jtOznaka)) return false;
     }
     if (vl.isEmpty(jlrSif)) return false;
     this.getRaQueryDataSet().setString("SIFRA", jlrSif.getText());


     String all = (jcbPregled.isSelected() ? "1" : "0") +
                  (jcbDod.isSelected() ? "1" : "0") +
                  (jcbIzmjena.isSelected() ? "1" : "0") +
                  (jcbBris.isSelected() ? "1" : "0");

     if (all.equals("0000")) {
       javax.swing.JOptionPane.showMessageDialog(null,
         "Pobogu, kakvo ti je to pravo gdje ne smiješ ništa radit?",
         "Greška",
         javax.swing.JOptionPane.ERROR_MESSAGE);
       return false;
     }

//     try {
//       Class.forName(jtOpis.getText());

     this.getRaQueryDataSet().setString("PRAVO", (jcbPregled.isSelected() ? "1" : "0") +
                                      (jcbDod.isSelected() ? "1" : "0") +
                                      (jcbIzmjena.isSelected() ? "1" : "0") +
                                      (jcbBris.isSelected() ? "1" : "0"));
    this.getRaQueryDataSet().getColumn("VRPRAVA").setDefault(this.getRaQueryDataSet().getString("VRPRAVA"));
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaDetailPanel(this.jpMain);
    this.setRaQueryDataSet(dm.getPrava());
    this.setVisibleCols(new int[] {0,1,2,3,5});
    jlOznaka.setText("Oznaka");
    jpMain.setLayout(xYLayout1);
    jlDomena.setText("Domena");
    xYLayout1.setWidth(525);
    xYLayout1.setHeight(164);
    jtOznaka.setText("");
    jtOznaka.setColumnName("CPRAVA");
    jtOznaka.setDataSet(this.getRaQueryDataSet());
    jcbPravo.setRaDataSet(this.getRaQueryDataSet());
    jcbPravo.setRaColumn("VRPRAVA");
    jcbPravo.setRaItems(new String[][] {
      {"Aplikacija", "A"},
      {"Program", "P"},
      {"Tablica", "T"},
      {"Funkcija", "F"}});
    jlSifra.setText("Opis");
    jtKljuc.setColumnName("KLJUC");
    jtKljuc.setDataSet(this.getRaQueryDataSet());
    jlKljuc.setText("Klju\u010D");
    jpPravo.setBorder(BorderFactory.createEtchedBorder());
    jpPravo.setLayout(xYLayout2);
    jcbPregled.setText("Pregled");
    jcbIzmjena.setText("Izmjena");
    jcbDod.setText("Dodavanje");
    jcbBris.setText("Brisanje");
    jbSelOpis.setText("...");

    jlrSif.setColumnName("SIFRA");
    jlrSif.setSearchMode(0);
    jlrSif.setDataSet(this.getRaQueryDataSet());
    jlrSif.setTextFields(new JTextComponent[] {jlrOpis});
    jlrSif.setColNames(new String[] {"OPIS"});
    jlrSif.setNavColumnName("APP");
    jlrSif.setRaDataSet(dm.getAplikacija());
    jlrSif.setVisCols(new int[] {0,1});
    jlrSif.setNavButton(jbSelOpis);

    jlrOpis.setColumnName("OPIS");
    jlrOpis.setNavProperties(jlrSif);
    jlrOpis.setSearchMode(1);

    jlOpis.setText("Opis");
    jraOpis.setDataSet(this.getRaQueryDataSet());
    jraOpis.setColumnName("OPIS");

    jpMain.add(jlOznaka,new XYConstraints(15, 20, -1, -1));
    jpMain.add(jtOznaka,new XYConstraints(150, 20, 60, -1));
    jpMain.add(jlrSif,new XYConstraints(150, 100, 75, -1));
    jpMain.add(jlrOpis,new XYConstraints(230, 100, 245, -1));
    jpMain.add(jlKljuc,new XYConstraints(15, 125, -1, -1));
    jpMain.add(jlDomena,new XYConstraints(15, 45, -1, -1));
    jpMain.add(jpPravo,new XYConstraints(286, 19, 189, -1));
    jpPravo.add(jcbPregled,new XYConstraints(15, 0, -1, -1));
    jpPravo.add(jcbDod,new XYConstraints(15, 18, -1, -1));
    jpPravo.add(jcbBris,new XYConstraints(110, 18, -1, -1));
    jpPravo.add(jcbIzmjena,new XYConstraints(110, 1, -1, -1));
    jpMain.add(jcbPravo,new XYConstraints(150, 44, 120, -1));
    jpMain.add(jbSelOpis,new XYConstraints(480, 100, 21, 21));
    jpMain.add(jlSifra,new XYConstraints(15, 100, -1, -1));
    jpMain.add(jtKljuc, new XYConstraints(150, 125, 325, -1));
    jpMain.add(jlOpis, new XYConstraints(15, 75, -1, -1));
    jpMain.add(jraOpis, new XYConstraints(150, 75, 325, -1));
    stdnames.put("1111", "Sva prava na ");
    stdnames.put("1110", "Unos i ažuriranje ");
    stdnames.put("1100", "Unos podataka u ");
    stdnames.put("1010", "Ažuriranje ");
    stdnames.put("1000", "Pregled ");
    stdnames.put("0100", "Samo unos u ");
    stdnames.put("0010", "Samo izmjena ");
    stdnames.put("0001", "Samo brisanje ");
    stdnames.put("0011", "Izmjena i brisanje ");
    stdnames.put("0101", "Unos i brisanje ");
    stdnames.put("0111", "Unos, izmjena i brisanje ");
    stdnames.put("1001", "\u010Cudno pravo ");
    stdnames.put("1011", "\u010Cudno pravo ");
    stdnames.put("1101", "\u010Cudno pravo ");
    stdnames.put("0110", "Unos i izmjena ");
    stdnames.put("0000", "\u010Cudno pravo ");

    jcbPravo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!getRaQueryDataSet().getString("VRPRAVA").equals(lastdom)) {
          updateDomena();
          setDomena();
          jlrSif.setText("");
          jlrSif.forceFocLost();
        }
      }
    });
    jcbPregled.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        updateOpis();
      }
    });
    jcbIzmjena.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        updateOpis();
      }
    });
    jcbDod.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        updateOpis();
      }
    });
    jcbBris.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        updateOpis();
      }
    });
  }
  private void updateDomena() {
    if (this.getRaQueryDataSet().getString("VRPRAVA").equals("T")) {
      rcc.setLabelLaF(jtKljuc, true);
    } else {
      rcc.setLabelLaF(jtKljuc, false);
      jtKljuc.setText("");
    }
  }

  private void setDomena() {
    String dom = this.getRaQueryDataSet().getString("VRPRAVA").trim().toUpperCase();
    jlrSif.setColumnName("SIFRA");
    jlrSif.setSearchMode(0);
    jlrSif.setDataSet(this.getRaQueryDataSet());
    jlrSif.setTextFields(new JTextComponent[] {jlrOpis});
    if (dom.equals("A")) {
      jlSifra.setText("Aplikacija");
      jlrSif.setRaDataSet(dm.getAplikacija());
      jlrSif.setNavColumnName("APP");
      jlrSif.setColNames(new String[] {"OPIS"});
      jlrSif.setVisCols(new int[] {0,1});
      jlrOpis.setColumnName("OPIS");
      jlrOpis.setNavProperties(jlrSif);
    } else if (dom.equals("P")) {
      jlSifra.setText("Program");
      jlrSif.setRaDataSet(dm.getProgrami());
      jlrSif.setNavColumnName("CPROG");
      jlrSif.setColNames(new String[] {"OPISPROG"});
      jlrSif.setVisCols(new int[] {0,1});
      jlrOpis.setColumnName("OPISPROG");
      jlrOpis.setNavProperties(jlrSif);
    } else if (dom.equals("F")) {
      jlSifra.setText("Funkcija");
      jlrSif.setRaDataSet(dm.getFunkcije());
      jlrSif.setNavColumnName("CFUNC");
      jlrSif.setColNames(new String[] {"OPISFUNC"});
      jlrSif.setVisCols(new int[] {0,1});
      jlrOpis.setColumnName("OPISFUNC");
      jlrOpis.setNavProperties(jlrSif);
    } else {
      jlSifra.setText("Tablica");
      jlrSif.setRaDataSet(dm.getTablice());
      jlrSif.setNavColumnName("IMETAB");
      jlrSif.setColNames(new String[] {"KLASATAB"});
      jlrSif.setVisCols(new int[] {0,1,2});
      jlrOpis.setColumnName("KLASATAB");
      jlrOpis.setNavProperties(jlrSif);
    }
    jlrOpis.setSearchMode(1);
    lastdom = this.getRaQueryDataSet().getString("VRPRAVA");
  }

  private String getStd() {
    return (jcbPregled.isSelected() ? "1" : "0") +
           (jcbDod.isSelected() ? "1" : "0") +
           (jcbIzmjena.isSelected() ? "1" : "0") +
           (jcbBris.isSelected() ? "1" : "0");
  }

  private void updateOpis() {
    String orig = this.getRaQueryDataSet().getString("OPIS");
    if (orig.equals(""))
      this.getRaQueryDataSet().setString("OPIS", (String) stdnames.get(getStd()));
    else {
      Iterator it = stdnames.values().iterator();
      while (it.hasNext()) {
        String pref = (String) it.next();
        if (orig.startsWith(pref)) {
          this.getRaQueryDataSet().setString("OPIS",
              ((String) stdnames.get(getStd())) + orig.substring(pref.length()));
        }
      }
    }
  }

  public void raQueryDataSet_navigated(NavigationEvent e) {
    if (!this.getRaQueryDataSet().getString("VRPRAVA").equals(lastdom))
      setDomena();
    setPrava();
  }
}
