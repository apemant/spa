/****license*****************************************************************
**   file: jpKampanjeMaster.java
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

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Kanali;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextArea;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCorg;
import hr.restart.util.JlrNavField;
import hr.restart.util.raComboBox;


public class jpKampanjeMaster extends JPanel {
  
  jpCorg jpc = new jpCorg(350);
  JraTextField jraDatpoc = new JraTextField();
  JlrNavField jlrAgent = new JlrNavField();
  JlrNavField jlrNazagent = new JlrNavField();
  JraButton jbGetAgent = new JraButton();
  JraTextField jraNaslov = new JraTextField();
  raComboBox rcbKanal = new raComboBox();
  JraTextArea opis = new JraTextArea() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
  };

  JraCheckBox jbUpdate = new JraCheckBox();
  JraComboBox jcb = new JraComboBox();
  JraCheckBox jbNaslov = new JraCheckBox();
  JraCheckBox jbAgent = new JraCheckBox();
  JraCheckBox jbOpis = new JraCheckBox();

  public jpKampanjeMaster(boolean full) {
    this.setLayout(new XYLayout(650, full ? 295 : 210));
    
    jraDatpoc.setColumnName("DATPOC");
    jraNaslov.setColumnName("NASLOV");
    rcbKanal.setRaColumn("CKANAL");
    rcbKanal.setRaItems(Kanali.getDataModule().getTempSet(), "CKANAL", "NAZIV");
    
    if (full) {
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
    }
    
    opis.setColumnName("OPIS");
    opis.setPostOnFocusLost(true);
    
    JraScrollPane osc = new JraScrollPane();
    osc.setViewportView(opis);
    osc.setPreferredSize(new Dimension(460, 125));
    
    if (full) {
      jbUpdate.setText("Ažurirati");
      jcb.addItem("otvorenim stavkama");
      jcb.addItem("svim stavkama");
      jbNaslov.setText("naslov");
      jbAgent.setText("agenta");
      jbOpis.setText("napomene");
      jbUpdate.setSelected(false);
      jbNaslov.setSelected();
      jbAgent.setSelected();
      jbOpis.setSelected(false);
    }
    int down = full ? 0 : 25;
    
    this.add(jpc, new XYConstraints(0, 20, -1, -1));
    if (full) {
      this.add(new JLabel("Agent"), new XYConstraints(15, 45, -1, -1));
      this.add(jlrAgent, new XYConstraints(150, 45, 100, -1));
      this.add(jlrNazagent, new XYConstraints(255, 45, 350, -1));
      this.add(jbGetAgent, new XYConstraints(610, 45, 21, 21));
    }
    this.add(new JLabel("Naslov kampanje"), new XYConstraints(15, 70 - down, -1, -1));
    this.add(jraNaslov, new XYConstraints(150, 70 - down, 455, -1));
    this.add(new JLabel("Poèetni datum"), new XYConstraints(15, 95 - down, -1, -1));
    this.add(jraDatpoc, new XYConstraints(150, 95 - down, 100, -1));
    this.add(new JLabel("Kanal komunikacije"), new XYConstraints(300, 95 - down, -1, -1));
    this.add(rcbKanal, new XYConstraints(455, 95 - down, 150, -1));
    this.add(new JLabel("Napomene"), new XYConstraints(15, 120 - down, -1, -1));
    this.add(osc, new XYConstraints(150, 120 - down, 455, full ? 125 : 100));
    
    if (full) {
      this.add(jbUpdate, new XYConstraints(15, 255, -1, -1));
      this.add(jcb, new XYConstraints(150, 255, 180, -1));
      this.add(jbNaslov, new XYConstraints(355, 255, -1, -1));
      this.add(jbAgent, new XYConstraints(435, 255, -1, -1));
      this.add(jbOpis, new XYConstraints(515, 255, -1, -1));
    }
  }

  public void showEditPanel(boolean yes) {
    ((XYLayout) getLayout()).setHeight(yes ? 295 : 265);
    jbUpdate.setVisible(yes);
    jcb.setVisible(yes);
    jbNaslov.setVisible(yes);
    jbAgent.setVisible(yes);
    jbOpis.setVisible(yes);
  }

  public void BindComponents(DataSet ds) {
    jpc.bind(ds);
    jraDatpoc.setDataSet(ds);
    jraNaslov.setDataSet(ds);
    rcbKanal.setDataSet(ds);
    jlrAgent.setDataSet(ds);
    opis.setDataSet(ds);
  }
}
