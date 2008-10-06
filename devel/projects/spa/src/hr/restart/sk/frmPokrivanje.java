/****license*****************************************************************
**   file: frmPokrivanje.java
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
package hr.restart.sk;

import hr.restart.baza.dM;
import hr.restart.swing.jpCorg;
import hr.restart.swing.jpCpar;
import hr.restart.util.OKpanel;
import hr.restart.util.raFrame;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;

import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
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

public class frmPokrivanje extends raFrame {
  XYLayout lay = new XYLayout();
  JPanel pan = new JPanel(lay);
  jpCorg jpc = new jpCorg(325, true) {
    public void afterLookUp(boolean succ) {
      if (succ) jpp.focusCpar();
    }
  };
  jpCpar jpp = new jpCpar(325, false);
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      CancelPress();
    }
  };
  StorageDataSet data = new StorageDataSet();

  public frmPokrivanje() {
    try {
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void CancelPress() {
    setVisible(false);
  }

  public void OKPress() {
  }

  public void setFokus() {
    jpc.setCorg(hr.restart.zapod.OrgStr.getKNJCORG(false));
    jpp.clear();
    jpp.focusCpar();
  }

  private void init() throws Exception {
    lay.setWidth(620);
    lay.setHeight(80);
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(pan);
    getContentPane().add(okp, BorderLayout.SOUTH);
    pan.add(jpc, new XYConstraints(0, 20, -1, -1));
    pan.add(jpp, new XYConstraints(0, 45, -1, -1));
    pack();
    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        setFokus();
      }
    });
    data.setColumns(new Column[] {
      dM.createStringColumn("CORG", "Org. jedinica", 12),
      dM.createIntColumn("CPAR", "Partner"),
      dM.createShortColumn("DEPTH", "Stupanj"),
      dM.createBigDecimalColumn("FACTOR", "Faktor podudarnosti", 2)
    });
    data.open();
    data.setShort("DEPTH", (short) 1);
    data.setBigDecimal("FACTOR", new BigDecimal(0.60));
    jpc.bind(data);
    jpp.bind(data);
    jpp.shiftLabel();
    okp.registerOKPanelKeys(this);
  }
}
