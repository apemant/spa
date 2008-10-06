/****license*****************************************************************
**   file: jpCustomSections.java
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
package hr.restart.util.reports;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpCustomSections extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmCustomSections fCustomSections;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCorg = new JLabel();
  JLabel jlVrdok = new JLabel();
  JLabel jlVrsec = new JLabel();
  JraButton jbSelCorg = new JraButton();
  JraButton jbSelVrdok = new JraButton();
  JraButton jbSelVrsec = new JraButton();
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazdok = new JlrNavField() {
    public void after_lookUp() {
      fCustomSections.vrdokChanged();
    }
  };
  JlrNavField jlrVrsec = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrVrdok = new JlrNavField() {
    public void after_lookUp() {
      fCustomSections.vrdokChanged();
    }
  };

  public jpCustomSections(frmCustomSections f) {
    try {
      fCustomSections = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jlrVrsec.setDataSet(ds);
    jlrCorg.setDataSet(ds);
    jlrVrdok.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(521);
    lay.setHeight(110);

    jbSelCorg.setText("...");
    jbSelVrdok.setText("...");
    jbSelVrsec.setText("...");
    jlCorg.setText("Org. jedinica");
    jlVrdok.setText("Vrsta dokumenta");
    jlVrsec.setText("Dio izvještaja");

    jlrVrsec.setColumnName("VRSEC");
    jlrVrsec.setColNames(new String[] {"OPIS"});
    jlrVrsec.setTextFields(new JTextComponent[] {jlrOpis});
    jlrVrsec.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrVrsec.setSearchMode(0);
    jlrVrsec.setRaDataSet(fCustomSections.vrsec);
    jlrVrsec.setNavButton(jbSelVrsec);

    jlrOpis.setColumnName("OPIS");
    jlrOpis.setNavProperties(jlrVrsec);
    jlrOpis.setSearchMode(1);

    jlrCorg.setColumnName("CORG");
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(dm.getOrgstruktura());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    jlrVrdok.setColumnName("VRDOK");
    jlrVrdok.setColNames(new String[] {"NAZDOK"});
    jlrVrdok.setTextFields(new JTextComponent[] {jlrNazdok});
    jlrVrdok.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrVrdok.setSearchMode(0);
    jlrVrdok.setRaDataSet(dm.getVrdokum());
    jlrVrdok.setNavButton(jbSelVrdok);

    jlrNazdok.setColumnName("NAZDOK");
    jlrNazdok.setNavProperties(jlrVrdok);
    jlrNazdok.setSearchMode(1);

    jpDetail.add(jbSelCorg, new XYConstraints(485, 20, 21, 21));
    jpDetail.add(jbSelVrdok, new XYConstraints(485, 45, 21, 21));
    jpDetail.add(jbSelVrsec, new XYConstraints(485, 70, 21, 21));
    jpDetail.add(jlCorg, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlVrdok, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlVrsec, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlrCorg, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jlrNazdok, new XYConstraints(230, 45, 250, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(230, 20, 250, -1));
    jpDetail.add(jlrOpis, new XYConstraints(230, 70, 250, -1));
    jpDetail.add(jlrVrdok, new XYConstraints(150, 45, 75, -1));
    jpDetail.add(jlrVrsec, new XYConstraints(150, 70, 75, -1));

/*    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldk, String newk) {
        jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });*/

    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
