/****license*****************************************************************
**   file: jpCorg.java
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
package hr.restart.swing;

import hr.restart.baza.Condition;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class jpCorg extends JPanel {
  public XYLayout lay = new XYLayout();
  public JlrNavField corg = new JlrNavField() {
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };
  public JlrNavField naziv = new JlrNavField() {
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };

  public JraButton but = new JraButton();
  public JLabel lab;
  public raComboBox rcb;

  private boolean recurSelected, recurEnabled;

  public jpCorg() {
    this(100, 350, false);
  }

  public jpCorg(int wnaziv) {
    this(100, wnaziv, false);
  }

  public jpCorg(int wcorg, int wnaziv) {
    this(wcorg, wnaziv, false);
  }

  public jpCorg(int wnaziv, boolean recur) {
    this(100, wnaziv, recur);
  }

  public jpCorg(int wcorg, int wnaziv, boolean recur) {
    try {
      recurEnabled = recur;
      init(wcorg, wnaziv);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init(int wcorg, int wnaziv) throws Exception {
    this.setLayout(lay);
    lay.setHeight(25);
    lay.setWidth(150+wcorg+5+wnaziv+5+25);

    corg.setColumnName("CORG");
    corg.setColNames(new String[] {"NAZIV"});
    corg.setTextFields(new JTextComponent[] {naziv});
    corg.setVisCols(new int[] {0, 1});
    corg.setSearchMode(0);
    corg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    corg.setNavButton(but);

    naziv.setColumnName("NAZIV");
    naziv.setNavProperties(corg);
    naziv.setSearchMode(1);

    if (recurEnabled) {
      rcb = new raComboBox() {
        public void this_itemStateChanged() {
          recurSelected = getSelectedIndex() == 1;
        }
      };
      rcb.setRaItems(new String[][] {
        {"Org. jedinica", "N"},
        {"OJ + pripadne", "D"}
      });
      rcb.setSelectedIndex(1);
      this.add(rcb, new XYConstraints(15, 0, 130, -1));
    } else {
      lab = new JLabel();
      lab.setText("Org. jedinica");
      this.add(lab, new XYConstraints(15, 0, -1, -1));
    }
    this.add(corg, new XYConstraints(150, 0, wcorg, -1));
    this.add(naziv, new XYConstraints(155 + wcorg, 0, wnaziv, -1));
    this.add(but, new XYConstraints(160 + wcorg + wnaziv, 0, 21, 21));

    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldk, String newk) {
        corg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
  }

  public void bind(DataSet ds) {
    corg.setDataSet(ds);
  }

  public void bind(DataSet ds, String nazcol) {
    corg.setDataSet(ds);
    corg.setColumnName(nazcol);
    corg.setNavColumnName("CORG");
  }

  public void EnabDisabAll(boolean enab) {
    if (rcb != null) raCommonClass.getraCommonClass().setLabelLaF(rcb, enab);
    raCommonClass.getraCommonClass().setLabelLaF(corg, enab);
    raCommonClass.getraCommonClass().setLabelLaF(naziv, enab);
    raCommonClass.getraCommonClass().setLabelLaF(but, enab);
    if (!enab) repaint();
  }

  public void init() {
    corg.setText(OrgStr.getKNJCORG(false));
    corg.forceFocLost();
  }

  public boolean Validacija() {
    return !Valid.getValid().isEmpty(corg);
  }

  public boolean isRecursive() {
    return recurSelected && recurEnabled;
  }

  public void setCorg(String c) {
    corg.setText(c);
    corg.forceFocLost();
  }

  public String getCorg() {
    return corg.getText();
  }

  public String getNaziv() {
    return naziv.getText();
  }

  public Condition getOptCondition() {
    if (isRecursive() && corg.getText().equals(OrgStr.getOrgStr().getKNJCORG(false)))
      return Condition.none;
    return getCondition();
  }
  
  public Condition getAnyCondition() {
    if (corg.isEmpty()) return Condition.none;
    return getOptCondition();
  }
  
  public Condition getCondition() {
    if (isRecursive()) return getRecursiveCondition();
    return Condition.equal("CORG", corg.getText());
  }

  public Condition getRecursiveCondition() {
    return Condition.in("CORG", OrgStr.getOrgStr().getOrgstrAndKnjig(corg.getText()));
  }

  public void afterLookUp(boolean succ) {}
}
