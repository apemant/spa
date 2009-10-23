/****license*****************************************************************
**   file: jpCpar.java
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
import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

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

public class jpCpar extends JPanel {
  private XYLayout lay = new XYLayout();
  private raComboBox rcb;
  private JLabel lab;

  /*private raComboBox rcb = new raComboBox() {
    public void this_itemStateChanged() {
      if (this.getSelectedIndex() == 0) kupSelected();
      else dobSelected();
    }
  };*/

  public JlrNavField cpar = new JlrNavField() {
    public boolean isFocusTraversable() {
      return !skipCpar;
    }
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };

  public JlrNavField nazpar = new JlrNavField() {
    public boolean isFocusTraversable() {
      if (skipCpar) return (skipCpar = false);
      return true;
    }
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };

  JraButton but = new JraButton();

  private boolean oboje = false;
  private boolean kupdob = false;
  private boolean iskup = true;
  private boolean isdob = false;
  private boolean skipCpar = false;

  public jpCpar() {
    this(100, 350, false);
  }

  public jpCpar(int wnazpar) {
    this(100, wnazpar, false);
  }

  public jpCpar(int wcpar, int wnazpar) {
    this(wcpar, wnazpar, false);
  }

  public jpCpar(int wnazpar, boolean kupdob) {
    this(100, wnazpar, kupdob);
  }

  public jpCpar(int wcpar, int wnazpar, boolean kupdob) {
    try {
      init(wcpar, wnazpar, kupdob, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public jpCpar(int wcpar, int wnazpar, boolean kupdob, boolean all) {
    try {
      init(wcpar, wnazpar, kupdob, all);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init(int wcpar, int wnazpar, boolean kupdob, boolean all) throws Exception {
    oboje = all;
    this.kupdob = kupdob;
    this.setLayout(lay);
    lay.setHeight(25);
    lay.setWidth(150+wcpar+5+wnazpar+5+25);

    if (kupdob) {
      rcb = new raComboBox() {
        public void this_itemStateChanged() {
          if (this.getSelectedIndex() == 0) kupSelected();
          else if (this.getSelectedIndex() == 1) dobSelected();
          else obSelected();
        }
      };
      if (oboje)
        rcb.setRaItems(new String[][] {
            {"Kupac", "K"},
            {"Dobavlja\u010D", "D"},
            {"Partner", "O"}
          });
      else
        rcb.setRaItems(new String[][] {
            {"Kupac", "K"},
            {"Dobavlja\u010D", "D"}
        });
      rcb.setSelectedIndex(oboje ? 2 : 0);
      this.add(rcb, new XYConstraints(15, 0, 130, -1));
    } else {
      lab = new JLabel();
      lab.setText("Partner");
      this.add(lab, new XYConstraints(15, 0, -1, -1));
    }
    
    cpar.setColumnName("CPAR");
    cpar.setColNames(new String[] {"NAZPAR"});
    cpar.setTextFields(new JTextComponent[] {nazpar});
    cpar.setVisCols(new int[] {0, 1});
    cpar.setSearchMode(0);
    cpar.setRaDataSet(dM.getDataModule().getPartneri());
    cpar.setNavButton(but);

    nazpar.setColumnName("NAZPAR");
    nazpar.setNavProperties(cpar);
    nazpar.setSearchMode(1);

    this.add(cpar, new XYConstraints(150, 0, wcpar, -1));
    this.add(nazpar, new XYConstraints(155 + wcpar, 0, wnazpar, -1));
    this.add(but, new XYConstraints(160 + wcpar + wnazpar, 0, 21, 21));
  }

  public void EnabDisabAll(boolean enab) {
    if (rcb != null) raCommonClass.getraCommonClass().setLabelLaF(rcb, enab);
    raCommonClass.getraCommonClass().setLabelLaF(cpar, enab);
    raCommonClass.getraCommonClass().setLabelLaF(nazpar, enab);
    raCommonClass.getraCommonClass().setLabelLaF(but, enab);
    if (!enab) repaint();
  }

  public boolean isCombo() {
    return kupdob;
  }

  public boolean isDisabled() {
    return !cpar.isEnabled();
  }

  public boolean isEmpty() {
    return cpar.getText().trim().length() == 0;
  }
  
  public void setAllowMultiple(boolean allow) {
    nazpar.setAllowMultiple(allow);
  }
  
  public Condition getCondition() {
    return nazpar.getCondition();
  }

  public void shiftLabel() {
    if (lab != null) {
      this.remove(lab);
      this.add(lab, new XYConstraints(15, 2, -1, -1));
    }
  }

  public void setPartnerAny() {
    if (lab != null) {
      cpar.setRaDataSet(dM.getDataModule().getPartneri());
      lab.setText("Partner");
      iskup = isdob = false;
    }
  }
  
  public void setPartnerOboje() {
    if (lab != null) {
      cpar.setRaDataSet(dM.getDataModule().getPartneriOboje());
      lab.setText("Partner");
      iskup = isdob = false;
    }
  }

  public void setPartnerKup() {
    if (lab != null) {
      cpar.setRaDataSet(dM.getDataModule().getPartneriKup());
      lab.setText("Kupac");
      iskup = true;
      isdob = false;
    }
  }

  public void setPartnerDob() {
    if (lab != null) {
      cpar.setRaDataSet(dM.getDataModule().getPartneriDob());
      lab.setText("Dobavljaè");
      iskup = false;
      isdob = true;
    }
  }

  public void setSkipCpar() {
    skipCpar = true;
  }

  public void setKupci(boolean yes) {
    rcb.setSelectedIndex(yes ? 0 : 1);
    if (yes) kupSelected();
    else dobSelected();
  }
  
  public void setOboje() {
    rcb.setSelectedIndex(2);
    obSelected();
  }

  public void setCpar(int c) {
    cpar.setText(String.valueOf(c));
    cpar.forceFocLost();
  }

  public void setCpar(String c) {
    cpar.setText(c);
    cpar.forceFocLost();
  }

  public void clear() {
    setCpar("");
  }

  public void focusCombo() {
    if (kupdob) rcb.requestFocus();
    else focusCpar();
  }

  public void focusCpar() {
    cpar.requestFocus();
  }

  public void focusCparLater() {
    cpar.requestFocusLater();
  }

  public boolean isKupci() {
    return iskup;
  }
  
  public boolean isOboje() {
    return !iskup && !isdob;
  }
  
  public boolean isKupdob() {
    return iskup && isdob;
  }

  public boolean Validacija() {
    return !Valid.getValid().isEmpty(cpar);
  }

  public void bind(DataSet ds) {
    cpar.setDataSet(ds);
  }

  public void init() {
    cpar.setText("");
    cpar.forceFocLost();
  }

  public int getCpar() {
    return Aus.getNumber(cpar.getText());
  }

  public String getNazpar() {
    return nazpar.getText();
  }

  protected void kupSelected() {
    cpar.setRaDataSet(dM.getDataModule().getPartneriKup());
    nazpar.setRaDataSet(dM.getDataModule().getPartneriKup());
    cpar.forceFocLost();
    iskup = true;
    isdob = false;
  }

  protected void dobSelected() {
    cpar.setRaDataSet(dM.getDataModule().getPartneriDob());
    nazpar.setRaDataSet(dM.getDataModule().getPartneriDob());
    cpar.forceFocLost();
    iskup = false;
    isdob = true;
  }
  
  protected void obSelected() {
    cpar.setRaDataSet(dM.getDataModule().getPartneriOboje());
    nazpar.setRaDataSet(dM.getDataModule().getPartneriOboje());
    cpar.forceFocLost();
    iskup = true;
    isdob = true;
  }

  public void afterLookUp(boolean succ) {}
}
