/****license*****************************************************************
**   file: jpGetRepGK.java
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
 * Created on Sep 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.gk;

import hr.restart.baza.Repgk;
import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class jpGetRepGK extends JPanel {
  private StorageDataSet ds;
  public XYLayout lay = new XYLayout();
  public JLabel jl = new JLabel("Izvještaj");
  public JlrNavField crepgk = new JlrNavField() {
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };
  public JlrNavField title = new JlrNavField() {
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };

  public JraButton but = new JraButton();
  public JLabel lab;
  private int wc, wt;
  public jpGetRepGK(int _wc, int _wt) {
    if (_wc < 0) {
      wc = 100;
    } else {
      wc = _wc;
    }
    if (_wt < 0) {
      wt = 350;
    } else {
      wt = _wt;
    }
    dsInit();
    jpInit();
  }
  public jpGetRepGK() {
    this(-1,-1);
  }

  /**
   * @param b
   */
  protected void afterLookUp(boolean b) {
    // TODO Auto-generated method stub
    
  }
  
  private void dsInit() {
    ds = new StorageDataSet();
    ds.addColumn(Repgk.getDataModule().getColumn("CREPGK").cloneColumn());
    crepgk.setDataSet(ds);
  }
  
  public final StorageDataSet getData() {
    return ds;
  }
  /**
   * 
   */
  private void jpInit() {
    this.setLayout(lay);
    lay.setHeight(25);
    lay.setWidth(150+wc+5+wt+5+25);

    crepgk.setColumnName("CREPGK");
    crepgk.setColNames(new String[] {"TITLE"});
    crepgk.setTextFields(new JTextComponent[] {title});
    crepgk.setVisCols(new int[] {0, 1});
    crepgk.setSearchMode(0);
    crepgk.setRaDataSet(Repgk.getDataModule().getQueryDataSet());
    crepgk.setNavButton(but);

    title.setColumnName("TITLE");
    title.setNavProperties(crepgk);
    title.setSearchMode(1);
    add(jl, new XYConstraints(15, 0, -1, -1));
    add(crepgk, new XYConstraints(150, 0, wc, -1));
    add(title, new XYConstraints(155 + wc, 0, wt, -1));
    add(but, new XYConstraints(160 + wc + wt, 0, 21, 21));
  }
}
