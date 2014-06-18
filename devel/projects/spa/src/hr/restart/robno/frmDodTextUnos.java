/****license*****************************************************************
**   file: frmDodTextUnos.java
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

import java.awt.BorderLayout;
import java.awt.Dimension;

import hr.restart.baza.VTText;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.raMatPodaci;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;


public class frmDodTextUnos extends raMatPodaci {
  
  Valid vl = Valid.getValid();
  
  static frmDodTextUnos inst = null;
  
  String main = null;
  
  JPanel jp = new JPanel();
  hr.restart.swing.JraTextArea dodtxt = new hr.restart.swing.JraTextArea();
  JraScrollPane scroller = new JraScrollPane(dodtxt);
  JScrollBar hs = new JScrollBar(JScrollBar.HORIZONTAL) {
    public void setEnabled(boolean dummy) {
      super.setEnabled(true);
    }
  };
  JScrollBar vs = new JScrollBar(JScrollBar.VERTICAL) {
    public void setEnabled(boolean dummy) {
      super.setEnabled(true);
    }
  };
  

  public frmDodTextUnos() {
    super(2);
    try {
      inst = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    this.setRaQueryDataSet(VTText.getDataModule().getFilteredDataSet("1=0"));
    dodtxt.setBorder(new JraTextField().getBorder());
    scroller.setBorder(BorderFactory.createLoweredBevelBorder());
    scroller.setHorizontalScrollBar(hs);
    scroller.setVerticalScrollBar(vs);
    scroller.setPreferredSize(new Dimension(500,200));
    jp.setLayout(new BorderLayout());
    jp.add(scroller);
    this.setRaDetailPanel(jp);
    this.setVisibleCols(new int[] {1});    
  }
  
  public void raQueryDataSet_navigated(NavigationEvent e) {
    System.out.println(getRaQueryDataSet());
    System.out.println(getRaQueryDataSet().getStatus());
    if (getRaQueryDataSet().rowCount() == 0) dodtxt.setText("");
    else dodtxt.setText(getRaQueryDataSet().getString("TEXTFAK"));
  }
  
  public void beforeShow() {
    if (main != null)
      VTText.getDataModule().setFilter(getRaQueryDataSet(), "CKEY LIKE '" + main + "%'");
    getRaQueryDataSet().open();
  }
  
  public void AfterCancel() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        System.out.println(getRaQueryDataSet());
        System.out.println(getRaQueryDataSet().getRow());
        raQueryDataSet_navigated(null);
      }
    });
    
  }
  
  public static frmDodTextUnos getInstance() {
    return inst;
  }
  
  public void setKey(String key) {
    main = key;
  }
  
  public void SetFokus(char mode) {
    if (mode != 'B') dodtxt.requestFocus();
  }
  
  public boolean Validacija(char mode) {
    return true;
  }
  
  public boolean doBeforeSave(char mode) {
    if (mode != 'B') getRaQueryDataSet().setString("TEXTFAK", dodtxt.getText());
    if (mode == 'N') findNextKey();
    return true;
  }
  
  void findNextKey() {
    DataSet old = VTText.getDataModule().getTempSet("CKEY", "CKEY LIKE '" + main + "%'");
    old.open();
    int max = 0;
    for (old.first(); old.inBounds(); old.next()) {
      String key = old.getString("CKEY");
      int num = Aus.getNumber(key.substring(key.lastIndexOf('-') + 1));
      if (num > max) max = num;
    }
    VarStr mx = new VarStr(Integer.toString(max + 1));
    if (mx.length() < 4) mx.paddLeft(4 - mx.length(), '0');
    getRaQueryDataSet().setString("CKEY", main + mx);
  }
}
