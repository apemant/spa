/****license*****************************************************************
**   file: frmUsersklad.java
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
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmUsersklad extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCskl = new JLabel();
  JLabel jlCuser = new JLabel();
  JraButton jbSelCskl = new JraButton();
  JraButton jbSelCuser = new JraButton();
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazskl = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCuser = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCskl = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public frmUsersklad() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    // Disabla tekst komponentu kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jlrCuser, false);
      rcc.setLabelLaF(jlrNaziv, false);
      rcc.setLabelLaF(jbSelCuser, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jlrCuser.forceFocLost();
      jlrCskl.forceFocLost();
      jlrCuser.requestFocus();
    } else if (mode == 'I') {
      jlrCskl.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jlrCuser) || vl.isEmpty(jlrCskl))
      return false;
    if (mode == 'N' && vl.notUnique(jlrCuser))
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaDetailPanel(this.jpDetail);
    this.setRaQueryDataSet(dm.getUsersklad());
    this.setVisibleCols(new int[] {0, 1});

    jpDetail.setLayout(lay);
    lay.setWidth(571);
    lay.setHeight(85);

    jbSelCskl.setText("...");
    jbSelCuser.setText("...");
    jlCskl.setText("Skladište");
    jlCuser.setText("Korisnik");

    jlrCuser.setColumnName("CUSER");
    jlrCuser.setDataSet(this.getRaQueryDataSet());
    jlrCuser.setColNames(new String[] {"NAZIV"});
    jlrCuser.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCuser.setVisCols(new int[] {0, 1});
    jlrCuser.setSearchMode(0);
    jlrCuser.setRaDataSet(dm.getUseri());
    jlrCuser.setNavButton(jbSelCuser);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCuser);
    jlrNaziv.setSearchMode(1);

    jlrCskl.setColumnName("CSKL");
    jlrCskl.setDataSet(this.getRaQueryDataSet());
    jlrCskl.setColNames(new String[] {"NAZSKL"});
    jlrCskl.setTextFields(new JTextComponent[] {jlrNazskl});
    jlrCskl.setVisCols(new int[] {0, 1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(dm.getSklad());
    jlrCskl.setNavButton(jbSelCskl);

    jlrNazskl.setColumnName("NAZSKL");
    jlrNazskl.setNavProperties(jlrCskl);
    jlrNazskl.setSearchMode(1);

    jpDetail.add(jbSelCskl, new XYConstraints(535, 45, 21, 21));
    jpDetail.add(jbSelCuser, new XYConstraints(535, 20, 21, 21));
    jpDetail.add(jlCskl, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlCuser, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlrCskl, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlrCuser, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 20, 275, -1));
    jpDetail.add(jlrNazskl, new XYConstraints(255, 45, 275, -1));
  }
}
