/****license*****************************************************************
**   file: jpSelKonto.java
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
/*
 * Created on 2005.05.02
 *
 */
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpSelKonto extends JPanel {
  private XYLayout lay = new XYLayout();
  private raComboBox rcb;
  private JLabel lab;

  /*private raComboBox rcb = new raComboBox() {
    public void this_itemStateChanged() {
      if (this.getSelectedIndex() == 0) kupSelected();
      else dobSelected();
    }
  };*/

  public JlrNavField konto = new JlrNavField() {
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };

  public JlrNavField naziv = new JlrNavField() {
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };

  JraButton but = new JraButton();

  private boolean isallow = true;

  public jpSelKonto() {
    this(100, 350, false);
  }

  public jpSelKonto(int wnaziv) {
    this(100, wnaziv, false);
  }

  public jpSelKonto(int wkonto, int wnaziv) {
    this(wkonto, wnaziv, false);
  }

  public jpSelKonto(int wnaziv, boolean allow) {
    this(100, wnaziv, allow);
  }

  public jpSelKonto(int wkonto, int wnaziv, boolean allow) {
    try {
      init(wkonto, wnaziv, allow);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init(int wkonto, int wnaziv, boolean allow) throws Exception {
    this.isallow = allow;
    this.setLayout(lay);
    lay.setHeight(25);
    lay.setWidth(150+wkonto+5+wnaziv+5+25);

    if (allow) {
      rcb = new raComboBox() {
        public void this_itemStateChanged() {
          if (this.getSelectedIndex() == 0) noSelected();
          else yesSelected();
        }
      };
      rcb.setRaItems(new String[][] {
        {"Sva konta", "S"},
        {"Konto", "K"}
      });
      rcb.setSelectedIndex(0);
      this.add(rcb, new XYConstraints(15, 0, 130, -1));
    } else {
      lab = new JLabel();
      lab.setText("Konto");
      this.add(lab, new XYConstraints(15, 0, -1, -1));
    }

    konto.setColumnName("BROJKONTA");
    konto.setColNames(new String[] {"NAZIVKONTA"});
    konto.setTextFields(new JTextComponent[] {naziv});
    konto.setVisCols(new int[] {0, 1});
    konto.setSearchMode(3);
    konto.setRaDataSet(dM.getDataModule().getKonta());
    konto.setNavButton(but);

    naziv.setColumnName("NAZIVKONTA");
    naziv.setNavProperties(konto);
    naziv.setSearchMode(1);
    
    if (allow) enabDohvat(false);

    this.add(konto, new XYConstraints(150, 0, wkonto, -1));
    this.add(naziv, new XYConstraints(155 + wkonto, 0, wnaziv, -1));
    this.add(but, new XYConstraints(160 + wkonto + wnaziv, 0, 21, 21));

  }
  
  public void enabDohvat(boolean enab) {
    raCommonClass.getraCommonClass().setLabelLaF(konto, enab);
    raCommonClass.getraCommonClass().setLabelLaF(naziv, enab);
    raCommonClass.getraCommonClass().setLabelLaF(but, enab);
  }

  public void EnabDisabAll(boolean enab) {
    if (rcb != null) raCommonClass.getraCommonClass().setLabelLaF(rcb, enab);
    enabDohvat(enab);
    if (!enab) repaint();
  }

  public boolean isCombo() {
    return isallow;
  }

  public boolean isDisabled() {
    return !konto.isEnabled();
  }

  public boolean isEmpty() {
    return konto.getText().equals("");
  }

  public void shiftLabel() {
    if (lab != null) {
      this.remove(lab);
      this.add(lab, new XYConstraints(15, 3, -1, -1));
    }
  }

  public void setKonto(String kon) {
    if (kon == null) kon = "";
    setKontaAllow(kon.length() > 0);
    konto.setText(kon);
    konto.forceFocLost();
  }

  public void clear() {
    konto.setText("");
    naziv.setText("");
  }

  public void focusCombo() {
    if (isallow) rcb.requestFocus();
    else focusKonto();
  }

  public void focusKonto() {
    konto.requestFocus();
  }

  public void focusKontoLater() {
    konto.requestFocusLater();
  }

  public void bind(DataSet ds) {
    konto.setDataSet(ds);
  }
  
  public void bindOwn(String cap) {
  	StorageDataSet set = new StorageDataSet();
		set.setColumns(new Column[] {
				dM.createStringColumn("BROJKONTA", cap, 8)
		});
		konto.setDataSet(set);
		if (lab != null) lab.setText(cap);
  }
  
  public void setKontaSet(DataSet ds) {
    konto.setRaDataSet(ds);
  }

  public void setKontaAllow(boolean allow) {
    rcb.setSelectedIndex(allow ? 1 : 0);
  }
  
  public boolean isKontaAllow() {
    return rcb.getSelectedIndex() != 0;
  }
  
  public String getKonto() {
    return konto.getText();
  }

  public String getNazivKonta() {
    return naziv.getText();
  }
  
  public Condition getCondition() {
    if (isEmpty()) return Condition.none;
    return Condition.equal("BROJKONTA", getKonto());
  }
  
  public DataRow getKontoRow() {
    return lookupData.getlookupData().raLookup(konto.getRaDataSet(), "BROJKONTA", getKonto());
  }

  protected void noSelected() {
    konto.setText("");
    konto.forceFocLost();
    enabDohvat(false);
    repaint();
  }

  protected void yesSelected() {
    enabDohvat(true);
  }

  public void afterLookUp(boolean succ) {}
}
