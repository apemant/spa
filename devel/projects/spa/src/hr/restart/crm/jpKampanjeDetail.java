/****license*****************************************************************
**   file: jpKampanjeDetail.java
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
package hr.restart.crm;

import hr.restart.baza.Condition;
import hr.restart.baza.Kanali;
import hr.restart.baza.Klijenti;
import hr.restart.baza.Kontosobe;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextArea;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raComboBox;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpKampanjeDetail extends JPanel {
  
  
  JPanel podaci = new JPanel();
  JraTextField jraNaslov = new JraTextField();
  raComboBox rcbKanal = new raComboBox();
  JraTextField jraKanal = new JraTextField();
  JlrNavField jlrAgent = new JlrNavField();
  JlrNavField jlrNazagent = new JlrNavField();
  JraButton jbGetAgent = new JraButton();
  JraTextField jraTrajanje = new JraTextField();
  JlrNavField jlrCklijent = new JlrNavField() {
    public boolean isFocusTraversable() {
      return false;
    }
  };
  JlrNavField jlrKlijent = new JlrNavField();
  JraButton jbGetKlijent = new JraButton();
  JlrNavField jlrKosoba = new JlrNavField();
  JraButton jbGetKosoba = new JraButton();
  JraTextArea opis = new JraTextArea() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
  };
  
  public jpKampanjeDetail() {
    opis.setLineWrap(true);
    opis.setWrapStyleWord(true);
    
    this.setLayout(new XYLayout(570, 300));
    
    podaci.setLayout(new XYLayout(570, 280));
    
    jraKanal.setColumnName("INFOKANAL");
    jraNaslov.setColumnName("NASLOV");
    jraTrajanje.setColumnName("TRAJANJE");
    opis.setColumnName("OPIS");
    
    
    jlrAgent.setSearchMode(-1);
    jlrAgent.setColumnName("CUSER");
    
    jlrAgent.setColNames(new String[] {"NAZIV"});
    jlrAgent.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazagent});
    jlrAgent.setVisCols(new int[] {0, 1});
    jlrAgent.setRaDataSet(dM.getDataModule().getUseri());
    jlrAgent.setNavButton(jbGetAgent);
    jlrAgent.setSearchMode(0);
    
    jlrNazagent.setSearchMode(-1);
    jlrNazagent.setColumnName("NAZIV");
    jlrNazagent.setNavProperties(jlrAgent);
    jlrNazagent.setFocusLostOnShow(false);
    jlrNazagent.setAfterLookUpOnClear(false);
    jlrNazagent.setSearchMode(1);
    
    jlrCklijent.setColumnName("CKLIJENT");
    jlrCklijent.setColNames(new String[] {"NAZIV"});
    jlrCklijent.setTextFields(new javax.swing.text.JTextComponent[] {jlrKlijent});
    jlrCklijent.setVisCols(new int[] {1, 2});
    jlrCklijent.setRaDataSet(Klijenti.getDataModule().getQueryDataSet());
    jlrCklijent.setNavButton(jbGetKlijent);
    //jlrCklijent.setVisible(false);
    jlrCklijent.setEnabled(false);
    jlrCklijent.setFocusLostOnShow(true);
    jlrCklijent.setAfterLookUpOnClear(true);
    jlrCklijent.setSearchMode(0);
    
    jlrKlijent.setColumnName("NAZIV");
    jlrKlijent.setNavProperties(jlrCklijent);
    jlrKlijent.setFocusLostOnShow(false);
    jlrKlijent.setAfterLookUpOnClear(false);
    jlrKlijent.setSearchMode(1);
    
    jlrKosoba.setColumnName("KOSOBA");
    jlrKosoba.setNavColumnName("IME");
    jlrKosoba.setVisCols(new int[] {1, 5});
    jlrKosoba.setRaDataSet(Kontosobe.getDataModule().getFilteredDataSet(Condition.nil));
    jlrKosoba.setNavButton(jbGetKosoba);
    jlrKosoba.setFocusLostOnShow(false);
    jlrKosoba.setAfterLookUpOnClear(false);
    jlrKosoba.setSearchMode(1);
    
    rcbKanal.setRaColumn("CKANAL");
    rcbKanal.setRaItems(Kanali.getDataModule().getTempSet(), "CKANAL", "NAZIV");
    
    JraScrollPane osc = new JraScrollPane();
    osc.setViewportView(opis);
    osc.setPreferredSize(new Dimension(410, 125));
    
    podaci.add(new JLabel("Naslov kampanje"), new XYConstraints(15, 5, -1, -1));
    podaci.add(jraNaslov, new XYConstraints(150, 5, 400, -1));
    podaci.add(new JLabel("Kanal komunikacije"), new XYConstraints(15, 30, -1, -1));
    podaci.add(rcbKanal, new XYConstraints(150, 30, 150, -1));
    podaci.add(jraKanal, new XYConstraints(305, 30, 245, -1));
    podaci.add(new JLabel("Klijent"), new XYConstraints(15, 55, -1, -1));
    podaci.add(jlrCklijent, new XYConstraints(150, 55, 1, 1));
    podaci.add(jlrKlijent, new XYConstraints(150, 55, 375, -1));
    podaci.add(jbGetKlijent, new XYConstraints(530, 55, 21, 21));
    podaci.add(new JLabel("Kontakt osoba"), new XYConstraints(15, 80, -1, -1));
    podaci.add(jlrKosoba, new XYConstraints(150, 80, 250, -1));
    podaci.add(jbGetKosoba, new XYConstraints(405, 80, 21, 21));
    podaci.add(new JLabel("Trajanje"), new XYConstraints(15, 105, -1, -1));
    podaci.add(jraTrajanje, new XYConstraints(150, 105, 250, -1));
    podaci.add(new JLabel("Agent"), new XYConstraints(15, 130, -1, -1));
    podaci.add(jlrAgent, new XYConstraints(150, 130, 100, -1));
    podaci.add(jlrNazagent, new XYConstraints(255, 130, 250, -1));
    podaci.add(jbGetAgent, new XYConstraints(510, 130, 21, 21));
    podaci.add(new JLabel("Napomene"), new XYConstraints(15, 155, -1, -1));
    podaci.add(osc, new XYConstraints(150, 155, 400, 120));
    
    
    /*this.add(jbOdlozi, new XYConstraints(440, 15, 75, -1));
    this.add(jtf, new XYConstraints(540, 15, 50, -1));
    this.add(jtf, new XYConstraints(595, 15, 50, -1));*/
    
    this.add(podaci, new XYConstraints(0, 10, -1, -1));
  }
  
  public void BindComponents(DataSet ds) {
    jlrCklijent.setDataSet(ds);
    jraKanal.setDataSet(ds);
    jraNaslov.setDataSet(ds);
    jraTrajanje.setDataSet(ds);
    opis.setDataSet(ds);
    jlrAgent.setDataSet(ds);
    jlrKosoba.setDataSet(ds);
    rcbKanal.setRaDataSet(ds);
  }

}
