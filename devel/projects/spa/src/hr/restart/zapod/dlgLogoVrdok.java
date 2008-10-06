/****license*****************************************************************
**   file: dlgLogoVrdok.java
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
package hr.restart.zapod;

import hr.restart.baza.Vrdokum;
import hr.restart.swing.JraDialog;
import hr.restart.util.OKpanel;
import hr.restart.util.lookupData;
import hr.restart.util.raJPTableView;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.StorageDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class dlgLogoVrdok {
  private JraDialog dlg;
  private raJPTableView view = new raJPTableView(false) {
    public void mpTable_doubleClicked() {
      doubleClick();
    }
    public void mpTable_killFocus(EventObject e) {
      okp.jPrekid.requestFocus();
    }
  };

  private JPanel content = new JPanel();
  private StorageDataSet data;

  private hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  private lookupData ld = lookupData.getlookupData();
  private static dlgLogoVrdok inst = new dlgLogoVrdok();

  private OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      CancelPress();
    }
  };

  public static void show(Container owner) {
    inst.showInstance(owner);
  }

  private void showInstance(Container owner) {
    if (owner instanceof JComponent)
      owner = ((JComponent) owner).getTopLevelAncestor();
    if (owner instanceof Frame)
      dlg = new JraDialog((Frame) owner, true);
    else if (owner instanceof Dialog)
      dlg = new JraDialog((Dialog) owner, true);
    else dlg = new JraDialog((Frame) null, true);

    dlg.setTitle("Ispis logotipa po vrstama dokumenta");
    dlg.setContentPane(content);
    dlg.pack();
    dlg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    dlg.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        CancelPress();
      }
    });
    dlg.addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            okp.jPrekid.requestFocus();
          }
        });
      }
    });
    okp.registerOKPanelKeys(dlg);
    view.initKeyListener(dlg);
    if (owner != null) dlg.setLocationRelativeTo(owner);
    data.refresh();
    addKeyListeners();
    dlg.show();
  }

  private dlgLogoVrdok() {
    try {
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addKeyListeners() {
    dlg.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_ENTER) {
          doubleClick();
          data.next();
          e.consume();
        } else if (e.getKeyCode() == e.VK_SPACE) {
          doubleClick();
          e.consume();
        }
      }
    });
  }

  private void kill() {
    if (dlg != null) {
      dlg.dispose();
      dlg = null;
    }
  }

/*  private void findData() {
    DataSet vd = dM.getDataModule().getVrdokum();
    DataSet rep = dM.getDataModule().getReportdef();
    vd.open();
    rep.open();
    data.open();
    data.empty();
    HashSet izlazVrdoks = new HashSet();
    for (rep.first(); rep.inBounds(); rep.next())
      if ("D".equalsIgnoreCase(rep.getString("IZLAZNI")) &&
          rep.getString("VRDOK").length() > 0)
        izlazVrdoks.add(rep.getString("VRDOK"));

    for (Iterator i = izlazVrdoks.iterator(); i.hasNext(); ) {
      String vrdok = (String) i.next();
      if (ld.raLocate(vd, "VRDOK", vrdok)) {
        data.insertRow(false);
        data.setString("VRDOK", vrdok);
        data.setString("NAZDOK", vd.getString("NAZDOK"));
        data.setString("LOGO", "N".equalsIgnoreCase(vd.getString("ISPLOGO")) ? "NE" : "da");
        data.post();
      }
    }
  } */

  private void doubleClick() {
    data.setString("ISPLOGO", "D".equalsIgnoreCase(data.getString("ISPLOGO")) ? "N" : "D");
    data.goToClosestRow(data.getRow());
  }

  private void OKPress() {
    data.saveChanges();
    kill();
  }

  private void CancelPress() {
    data.refresh();
    kill();
  }

  private void init() throws Exception {
    data = Vrdokum.getDataModule().getFilteredDataSet("vrdok IN ("+
        "SELECT vrdok FROM reportdef WHERE izlazni='D')");
    data.getColumn("VRDOK").setWidth(6);
    data.getColumn("NAZDOK").setCaption("Vrsta dokumenta");
    data.getColumn("ISPLOGO").setCaption("Ispis");
    data.getColumn("ISPLOGO").setWidth(5);

    data.getColumn("VRSDOK").setVisible(0);
    data.getColumn("TIPDOK").setVisible(0);
    data.getColumn("KNJIZ").setVisible(0);
    data.getColumn("APP").setVisible(0);
    data.getColumn("ID").setVisible(0);
    view.setDataSet(data);
    view.setPreferredSize(new Dimension(450, 275));
    view.addTableModifier(new hr.restart.swing.raTableValueModifier("ISPLOGO",
        new String[] {"D", "N"}, new String[] {"da", "NE"}) {
        public int getMaxModifiedTextLength() {
          return 5;
        }
    });
    okp.jPrekid.setFocusPainted(false);

    content.setLayout(new BorderLayout());
    view.getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    content.add(view);
    content.add(okp, BorderLayout.SOUTH);
  }
}
