/****license*****************************************************************
**   file: raSelectValuta.java
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
import hr.restart.swing.JraDialog;
import hr.restart.util.OKpanel;
import hr.restart.zapod.jpGetValute;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raSelectValuta {
  JraDialog dlg;
  JPanel content = new JPanel();
  jpGetValute jpv = new jpGetValute() {
    public void afterClearVal() {
      jpv.jtTECAJ.setText("");
    }
  };
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      CancelPress();
    }
  };

  StorageDataSet dat = new StorageDataSet();
  boolean ok;

  public raSelectValuta() {
    try {
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void reset(Timestamp datum) {
    jpv.initJP('N');
    jpv.setTecajDate(datum);
  }

  public void show(Container parent) {
    Container realparent = null;

    if (parent instanceof JComponent)
      realparent = ((JComponent) parent).getTopLevelAncestor();
    else if (parent instanceof Window)
      realparent = parent;

    String title = "Odabir valute";

    if (realparent instanceof Dialog)
      dlg = new JraDialog((Dialog) realparent, title, true);
    else if (realparent instanceof Frame)
      dlg = new JraDialog((Frame) realparent, title, true);
    else dlg = new JraDialog((Frame) null, title, true);

    dlg.setContentPane(content);
    dlg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    dlg.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        CancelPress();
      }
    });
    dlg.pack();
    dlg.setLocationRelativeTo(realparent);
    okp.registerOKPanelKeys(dlg);
    jpv.initJP('I');
    jpv.disableDohvat();
    dlg.show();
  }

  private void OKPress() {
    ok = true;
    kill();
  }

  private void CancelPress() {
    ok = false;
    kill();
  }

  public BigDecimal getTecaj() {
    if (ok) return dat.getBigDecimal("TECAJ");
    else return null;
  }

  public String getOznval() {
    if (ok) return dat.getString("OZNVAL");
    else return null;
  }

  private void kill() {
    jpv.disableDohvat();
    if (dlg != null) {
      dlg.dispose();
      dlg = null;
    }
  }

  private void init() throws Exception {
    dat.setColumns(new Column[] {
      dM.createStringColumn("OZNVAL", 3),
      dM.createBigDecimalColumn("TECAJ", 6)
    });
    dat.open();
    dat.insertRow(false);
    dat.post();
    content.setLayout(new BorderLayout());
    jpv.setTecajVisible(true);
    jpv.setTecajEditable(true);
    jpv.setAlwaysSelected(true);
    jpv.jtOZNVAL.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jpv.enableDohvat();
      }
    });
    content.add(jpv);
    content.add(okp, BorderLayout.SOUTH);
    jpv.setRaDataSet(dat);
  }
}

