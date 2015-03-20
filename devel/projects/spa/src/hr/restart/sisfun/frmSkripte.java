/****license*****************************************************************
**   file: frmSkripte.java
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

import java.awt.BorderLayout;
import java.awt.Dimension;

import hr.restart.baza.Skripte;
import hr.restart.baza.dM;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTableModifier;
import hr.restart.util.raMatPodaci;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.Variant;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 * @version 1.0
 */

public class frmSkripte extends raMatPodaci {

  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();

  JPanel jpc = new JPanel(new XYLayout(500, 50));
  JPanel jp = new JPanel(new BorderLayout());

  JraTextField jraCKEY = new JraTextField();
  
  
  JraScrollPane vp = new JraScrollPane();
  
  JEditorPane script = new JEditorPane() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
  };
 
  public frmSkripte() {
    super(2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    this.setRaQueryDataSet(Skripte.getDataModule().copyDataSet());

    jraCKEY.setDataSet(getRaQueryDataSet());
    jraCKEY.setColumnName("CKEY");

    jpc.add(new JLabel("Šifra"), new XYConstraints(15, 20, -1, -1));
    jpc.add(jraCKEY, new XYConstraints(150, 20, 100, -1));
    
    setVisibleCols(new int[] {0,1});
    //getJpTableView().getMpTable().setPreferredScrollableViewportSize(new Dimension(500, 150));
    vp.setPreferredSize(new Dimension(500, 200));
    vp.setViewportView(script);
    
    jp.add(jpc, BorderLayout.NORTH);
    jp.add(vp);
    
    setRaDetailPanel(jp);
    
    jpDetailView.add(jp);
    
    getJpTableView().addTableModifier(new raTableModifier() {
      Variant v = new Variant();
      public boolean doModify() {
        return isColumn("TEKST");
      }
      public void modify() {
        ((JraTable2) getTable()).getDataSet().getVariant("TEKST", getRow(), v);
        String text = v.getString().trim();
        int nl = text.indexOf('\n');
        if (nl > 0)
          setComponentText(text.substring(0, nl));
      }
    });
    
    setRaDetailPanel(new JPanel());
  }
  
  public boolean Validacija(char mode) {
    if (mode=='N') {
      if (hr.restart.util.Valid.getValid().notUnique(jraCKEY)) 
        return false;
    }
    getRaQueryDataSet().setString("TEKST", script.getText());
    return true;
  }

  public void SetFokus(char mode) {
    if (mode=='N') {
      rcc.setLabelLaF(jraCKEY,true);
      jraCKEY.requestFocus();
    }
    else if (mode=='I') {
      rcc.setLabelLaF(jraCKEY,false);
      script.requestFocus();
    }
  }

  public void EntryPoint(char mode) {
  }
  
  public void raQueryDataSet_navigated(NavigationEvent e) {
    if (getRaQueryDataSet().rowCount() == 0) script.setText("");
    else {
      String tx = getRaQueryDataSet().getString("TEKST");
      script.setText(tx);
    }
  }
}